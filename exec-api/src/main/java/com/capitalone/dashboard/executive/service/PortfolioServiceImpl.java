package com.capitalone.dashboard.executive.service;

import com.capitalone.dashboard.exec.model.BaseConfigItem;
import com.capitalone.dashboard.exec.model.BuildingBlockMetricSummary;
import com.capitalone.dashboard.exec.model.ComponentMetricDetail;
import com.capitalone.dashboard.exec.model.Environment;
import com.capitalone.dashboard.exec.model.MetricType;
import com.capitalone.dashboard.exec.model.Portfolio;
import com.capitalone.dashboard.exec.model.PortfolioMetricDetail;
import com.capitalone.dashboard.exec.model.Product;
import com.capitalone.dashboard.exec.model.ProductComponent;
import com.capitalone.dashboard.exec.model.ProductMetricDetail;

import com.capitalone.dashboard.exec.repository.PortfolioMetricRepository;
import com.capitalone.dashboard.exec.repository.PortfolioRepository;
import com.capitalone.dashboard.executive.model.PortfolioResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PortfolioServiceImpl implements PortfolioService {
    private final PortfolioRepository portfolioRepository;
    private final PortfolioMetricRepository portfolioMetricRepository;

    @Autowired
    public PortfolioServiceImpl(PortfolioRepository portfolioRepository,
                                PortfolioMetricRepository portfolioMetricRepository) {
        this.portfolioRepository = portfolioRepository;
        this.portfolioMetricRepository = portfolioMetricRepository;
    }


    /**
     * Gets portfolio response for a given portfolio name and line of business
     * End Point: "/{name}/{lob}"
     *
     * @param name
     * @param lob
     * @return PortfolioResponse
     */
    public PortfolioResponse getPortfolio(String name, String lob) {
        Portfolio portfolio = portfolioRepository.findByNameAndLob(name, lob);
        return PortfolioResponse.getPortfolio(portfolio);
    }

    /**
     * Returns Products list for a portfolio with product metrics summary only. No metric time series, and no components.
     * End point: /{name}/{lob}/products
     *
     * @param name
     * @param lob
     * @param productName
     * @return
     */
    public List<BuildingBlockMetricSummary> getProductComponents(String name, String lob, String productName) {
        if (StringUtils.isEmpty(productName)) { return null; }
        // Get the portfolio from repository
        Portfolio portfolio = portfolioRepository.findByNameAndLob(name, lob);
        if (portfolio == null) { return null; }

        // Get the portfolio metric results for repository
        List<PortfolioMetricDetail> portfolioMetricDetailList = portfolioMetricRepository.findByNameAndLob(name, lob);
        if (CollectionUtils.isEmpty(portfolioMetricDetailList)) { return null; }

        // Get product list from portfolio
        List<Product> productList = portfolio.getProducts();
        if (CollectionUtils.isEmpty(productList)) { return null; }

        Product product = productList.stream()
                .filter(p -> productName.equalsIgnoreCase(p.getName()))
                .findFirst().orElse(null);
        if (product == null) { return null; }

        List<ProductComponent> productComponentList = product.getProductComponentList();
        List<Environment> environmentList = product.getEnvironments();

        List<BuildingBlockMetricSummary> buildingBlockMetricSummaryList = new ArrayList<>();
        portfolioMetricDetailList.forEach(portfolioMetricDetail -> productComponentList.forEach(productComponent -> buildComponentBuildingBlockMetricSummary(buildingBlockMetricSummaryList, productComponent,
                portfolioMetricDetail)));

        portfolioMetricDetailList.forEach(portfolioMetricDetail -> environmentList.forEach(environment -> buildComponentBuildingBlockMetricSummary(buildingBlockMetricSummaryList, environment,
                portfolioMetricDetail)));

        return buildingBlockMetricSummaryList;
    }

    private <T extends BaseConfigItem> void buildComponentBuildingBlockMetricSummary(
            List<BuildingBlockMetricSummary> buildingBlockMetricSummaryList, T item,
            PortfolioMetricDetail portfolioMetricDetail) {
        if (item == null) {return;}

        BuildingBlockMetricSummary buildingBlockMetricSummary
                = Optional.ofNullable(buildingBlockMetricSummaryList)
                .orElseGet(Collections::emptyList).stream()
                .filter(bbms -> bbms.getName().equalsIgnoreCase(item.getName())
                        && bbms.getLob().equalsIgnoreCase(item.getLob())
                        && bbms.getItemType().equals(item.getMetricLevel()))
                .findFirst().orElse(null);
        if (buildingBlockMetricSummary == null) {
            buildingBlockMetricSummary = BuildingBlockMetricSummary.getInstance(item);
            if (buildingBlockMetricSummary != null) {
                buildingBlockMetricSummaryList.add(buildingBlockMetricSummary);
            }
        }
        ComponentMetricDetail componentMetricDetail =
                Optional.ofNullable(portfolioMetricDetail.getProductMetricDetailList())
                        .orElseGet(Collections::emptyList).stream()
                        .flatMap(pmd -> pmd.getComponentMetricDetailList().stream())
                        .filter(pcmd -> (pcmd.getName().equalsIgnoreCase(item.getName())))
                        .findFirst().orElse(null);
        if (buildingBlockMetricSummary != null) {
            if (componentMetricDetail != null) {
                buildingBlockMetricSummary.getMetrics().add(componentMetricDetail.getSummary());
                componentMetricDetail.setTimeSeries(null);
            }
            buildingBlockMetricSummary.setTotalExpectedMetrics(MetricType.values().length);
            buildingBlockMetricSummary.setTotalComponents(1);
            buildingBlockMetricSummary.setReportingComponents(componentMetricDetail != null ? componentMetricDetail.getReportingComponents() : 0);
        }
    }


    /**
     * Get all the portfolios in the list of portfolio responses
     *
     * @return List of portfolio responses
     */
    public List<PortfolioResponse> getPortfolios() {
        List<PortfolioResponse> portfolioResponses = new ArrayList<>();
        List<Portfolio> portfolioList;
        portfolioList =  portfolioRepository.findAllByOwnersNotNull();
        Optional.ofNullable(portfolioList).orElseGet(Collections::emptyList)
                .forEach(p -> portfolioResponses.add(PortfolioResponse.getPortfolio(p)));
        return portfolioResponses;
    }


    public BuildingBlockMetricSummary getPortfolioProduct(String name, String lob, String productName) {
        Portfolio portfolio = portfolioRepository.findByNameAndLob(name, lob);
        List<PortfolioMetricDetail> portfolioMetricDetailList = portfolioMetricRepository.findByNameAndLob(name, lob);
        List<Product> productList = portfolio.getProducts();
        Product product = Optional.ofNullable(productList)
                .orElseGet(Collections::emptyList).stream()
                .filter(p -> (p.getName().equalsIgnoreCase(productName)))
                .findFirst().orElse(null);

        if (product == null) { return null; }

        BuildingBlockMetricSummary buildingBlockMetricSummary = null;
        for (PortfolioMetricDetail portfolioMetricDetail : portfolioMetricDetailList) {
            ProductMetricDetail productMetricDetail =
                    Optional.ofNullable(portfolioMetricDetail.getProductMetricDetailList())
                            .orElseGet(Collections::emptyList).stream()
                            .filter(pmd -> (pmd.getName().equalsIgnoreCase(productName)))
                            .findFirst().orElse(null);
            if (productMetricDetail == null) { return null; }

            productMetricDetail.setTimeSeries(null);
            productMetricDetail.setComponentMetricDetailList(null);
            if (buildingBlockMetricSummary == null) {
                buildingBlockMetricSummary = BuildingBlockMetricSummary.getInstance(productMetricDetail);
            }
            buildingBlockMetricSummary.getMetrics().add(productMetricDetail.getSummary());
            buildingBlockMetricSummary.setTotalExpectedMetrics(MetricType.values().length);
            buildingBlockMetricSummary.setTotalComponents(CollectionUtils.size(product.getProductComponentList()));
            buildingBlockMetricSummary.setReportingComponents(productMetricDetail.getReportingComponents());
        }

        return buildingBlockMetricSummary;
    }

    /**
     * Gets a Products list for a portfolio with product metrics summary. No metric time series, and no components.
     * End point: /{name}/{lob}/products
     *
     * @param name
     * @param lob
     * @return List of Building Block Summary
     */
    public List<BuildingBlockMetricSummary> getPortfolioProducts(String name, String lob) {
        Portfolio portfolio = portfolioRepository.findByNameAndLob(name, lob);
        List<PortfolioMetricDetail> portfolioMetricDetailList = portfolioMetricRepository.findByNameAndLob(name, lob);

        if (portfolio == null) {return null;}

        List<BuildingBlockMetricSummary> buildingBlockMetricSummaryList = new ArrayList<>();

        List<Product> productList = portfolio.getProducts();
        for (Product product : productList) {
            BuildingBlockMetricSummary buildingBlockMetricSummary
                = buildingBlockMetricSummaryList.stream()
                    .filter(bbms -> bbms.getName().equalsIgnoreCase(product.getName())
                            && bbms.getLob().equalsIgnoreCase(product.getLob())
                            && bbms.getItemType().equals(product.getMetricLevel()))
                .findFirst().orElse(null);

            if (buildingBlockMetricSummary == null) {
                buildingBlockMetricSummary = BuildingBlockMetricSummary.getInstance(product);
                if (buildingBlockMetricSummary == null) { continue; }

                buildingBlockMetricSummaryList.add(buildingBlockMetricSummary);
            }

            for (PortfolioMetricDetail portfolioMetricDetail : portfolioMetricDetailList) {
                ProductMetricDetail productMetricDetail =
                        Optional.ofNullable(portfolioMetricDetail.getProductMetricDetailList())
                                .orElseGet(Collections::emptyList).stream()
                                .filter(pmd -> (pmd.getName().equalsIgnoreCase(product.getName())))
                                .findFirst().orElse(null);

                if (productMetricDetail != null) {
                    buildingBlockMetricSummary.getMetrics().add(productMetricDetail.getSummary());
                    buildingBlockMetricSummary.setTotalExpectedMetrics(MetricType.values().length);
                    buildingBlockMetricSummary.setTotalComponents(CollectionUtils.size(product.getProductComponentList()));
                    buildingBlockMetricSummary.setReportingComponents(productMetricDetail.getReportingComponents());
                }
            }
        }

        return buildingBlockMetricSummaryList;
    }
}
