package com.capitalone.dashboard.executive.service;

import com.capitalone.dashboard.exec.model.BuildingBlockMetricSummary;
import com.capitalone.dashboard.exec.model.ComponentMetricDetail;
import com.capitalone.dashboard.exec.model.MetricDetails;
import com.capitalone.dashboard.exec.model.MetricType;
import com.capitalone.dashboard.exec.model.Portfolio;
import com.capitalone.dashboard.exec.model.PortfolioMetricDetail;
import com.capitalone.dashboard.exec.model.Product;
import com.capitalone.dashboard.exec.model.ProductComponent;
import com.capitalone.dashboard.exec.model.ProductMetricDetail;
import com.capitalone.dashboard.exec.repository.PortfolioMetricRepository;
import com.capitalone.dashboard.exec.repository.PortfolioRepository;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;

@Component
public class MetricsServiceImpl implements MetricsService {
    private final PortfolioRepository portfolioRepository;
    private final PortfolioMetricRepository portfolioMetricRepository;

    @Autowired
    public MetricsServiceImpl(PortfolioRepository portfolioRepository, PortfolioMetricRepository portfolioMetricRepository) {
        this.portfolioRepository = portfolioRepository;
        this.portfolioMetricRepository = portfolioMetricRepository;
    }

    /**
     * Gets metric summary for a given metric type, portfolio name and lines of business
     * End point: "/{metric}/portfolio/{name}/{lob}/summary"
     * @param metricType Metric Type
     * @param name Portfolio name
     * @param lob Portfolio Lines of Business
     * @return Metric Details without the time series
     */
    public MetricDetails getPortfolioMetricSummary(MetricType metricType, String name, String lob) {
        MetricDetails metricDetails = getPortfolioMetricDetail(metricType, name, lob);

        if (metricDetails == null) { metricDetails = PortfolioMetricDetail.getInstance(name, lob, metricType); }

        metricDetails.setTimeSeries(null);

        return metricDetails;
    }

    /**
     * Gets metric details for a given metric type, portfolio name and line of business
     * End point: "/{metric}/portfolio/{name}/{lob}/detail"
     * @param metricType Metric Type
     * @param name Portfolio name
     * @param lob Portfolio Line of Business
     * @return Metric Details without the product and component level details
     */
    public MetricDetails getPortfolioMetricDetail(MetricType metricType, String name, String lob) {
        PortfolioMetricDetail portfolioMetricDetail
                = portfolioMetricRepository.findByNameAndLobAndType(name, lob, metricType);

        if (portfolioMetricDetail == null) { return null; }

        portfolioMetricDetail.setProductMetricDetailList(null);
        return portfolioMetricDetail;
    }

    /**
     * Get a list of building block summary response for a given metric type, porfolio name, line of business
     * End point: "/{metric}/portfolio/{name}/{lob}/product"
     * @param metricType Metric Type
     * @param name Portfolio name
     * @param lob Portfolio Line of Business
     * @return Building block summary list
     */
    public List<BuildingBlockMetricSummary> getPortfolioMetricProducts(MetricType metricType, String name, String lob) {
        Portfolio portfolio = portfolioRepository.findByNameAndLob(name, lob);
        PortfolioMetricDetail portfolioMetricDetail
                = portfolioMetricRepository.findByNameAndLobAndType(name, lob, metricType);
        if ((portfolio == null) || (portfolioMetricDetail == null)) { return null; }

        return buildPortfolioMetricProducts(portfolioMetricDetail, portfolio);
    }

    /**
     * Builds portfolio metric product information for a given portfolio metric detail entry
     * @param portfolioMetricDetail
     * @return List of BuildingBlockMetricSummary
     */

    private static List<BuildingBlockMetricSummary> buildPortfolioMetricProducts(PortfolioMetricDetail portfolioMetricDetail,
                                                                                 Portfolio portfolio) {
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
                if (buildingBlockMetricSummary != null) {
                    buildingBlockMetricSummaryList.add(buildingBlockMetricSummary);
                }
            }
        }

        List<ProductMetricDetail> productMetricDetailList = portfolioMetricDetail.getProductMetricDetailList();

        Optional.ofNullable(productMetricDetailList)
            .orElseGet(Collections::emptyList).forEach(pmd -> {
                BuildingBlockMetricSummary buildingBlockMetricSummary
                    = buildingBlockMetricSummaryList.stream()
                    .filter(bbms -> bbms.getName().equalsIgnoreCase(pmd.getName())
                            && bbms.getLob().equalsIgnoreCase(pmd.getLob()))
                    .findFirst().orElse(null);

           if (buildingBlockMetricSummary != null) {
               buildingBlockMetricSummary.setTotalComponents(pmd.getTotalComponents());
               buildingBlockMetricSummary.setTotalExpectedMetrics(1);
               buildingBlockMetricSummary.setReportingComponents(pmd.getReportingComponents());
               buildingBlockMetricSummary.setMetrics(singletonList(pmd.getSummary()));
           }
        });

        return buildingBlockMetricSummaryList;
    }

    /**
     * Get summary for a given metric type, portfolio name, line of business, product name
     * End point: "/{metric}/product/{name}/{lob}/{productName}/summary"
     * TODO: Verify is the path is correct. Should be this??? "/{metric}/portfolio/{name}/{lob}/{productName}/summary"
     * @param metricType Metric type
     * @param name portfolio name
     * @param lob
     * @param productName
     * @return
     */
    public MetricDetails getProductMetricSummary(MetricType metricType, String name, String lob, String productName) {
        ProductMetricDetail productMetricDetail = getProductMetricDetail (metricType, name, lob, productName);

        if (productMetricDetail == null) { return null; }

        productMetricDetail.setTimeSeries(null);
        productMetricDetail.setComponentMetricDetailList(null);
        return productMetricDetail;
    }


    /**
     * Get details for a given metric type, portfolio name, line of business, product name
     * End point: "/{metric}/product/{name}/{lob}/{productName}/detail"
     * TODO: Verify is the path is correct. Should be this??? "/{metric}/portfolio/{name}/{lob}/{productName}/summary"
     * @param metricType Metric type
     * @param name portfolio name
     * @param lob
     * @param productName
     * @return
     */
    public ProductMetricDetail getProductMetricDetail(MetricType metricType, String name, String lob, String productName) {
        if (StringUtils.isEmpty(productName)) { return null; }

        PortfolioMetricDetail portfolioMetricDetail
                = portfolioMetricRepository.findByNameAndLobAndType(name, lob, metricType);
        if ((portfolioMetricDetail == null)||(CollectionUtils.isEmpty(portfolioMetricDetail.getProductMetricDetailList()))) {
            return null;
        }
        return Optional.ofNullable(portfolioMetricDetail.getProductMetricDetailList())
                .orElseGet(Collections::emptyList).stream()
                .filter(pmd -> (productName.equalsIgnoreCase(pmd.getName())))
                .findFirst().orElse(null);
    }

    /** Get Building block summary for product
     * End point: "/{metric}/product/{name}/{lob}/{productName}/component"
     * @param metricType
     * @param name
     * @param lob
     * @param productName
     * @return
     */
    public List<BuildingBlockMetricSummary> getProductMetricComponents(MetricType metricType, String name, String lob, String productName) {
        if (StringUtils.isEmpty(productName)) { return null; }

        Portfolio portfolio = portfolioRepository.findByNameAndLob(name, lob);
        PortfolioMetricDetail portfolioMetricDetail = portfolioMetricRepository.findByNameAndLobAndType(name, lob, metricType);
        if ((portfolio == null)||(portfolioMetricDetail == null)|| CollectionUtils.isEmpty(portfolio.getProducts())) {
            return null;
        }
        Product product = portfolio.getProducts().stream().filter(pl -> pl.getName().equalsIgnoreCase(productName)).findFirst().orElse(null);

        return buildProductMetricComponents(portfolioMetricDetail, product);
    }


    public static List<BuildingBlockMetricSummary> buildProductMetricComponents(PortfolioMetricDetail portfolioMetricDetail,
                                                                                Product product) {
        if (product == null) {return null;}

        List<ProductComponent> productComponentList = product.getProductComponentList();

        List<ProductMetricDetail> productMetricDetailList = portfolioMetricDetail.getProductMetricDetailList();
        ProductMetricDetail productMetricDetail =
                Optional.ofNullable(productMetricDetailList)
                .orElseGet(Collections::emptyList).stream()
                .filter(pmd -> pmd.getName().equalsIgnoreCase(product.getName()))
                .findFirst().orElse(null);
        List<BuildingBlockMetricSummary> buildingBlockMetricSummaryList = new ArrayList<>();

        if (productMetricDetail != null) {
            List<ComponentMetricDetail> componentMetricDetailList
                = Optional.ofNullable(productMetricDetail.getComponentMetricDetailList())
                .orElseGet(Collections::emptyList);

            componentMetricDetailList
                .forEach(cmd -> {
                    BuildingBlockMetricSummary buildingBlockMetricSummary = BuildingBlockMetricSummary.getInstance(cmd);
                    buildingBlockMetricSummary.setMetrics(singletonList(cmd.getSummary()));
                    buildingBlockMetricSummaryList.add(buildingBlockMetricSummary);
                });
        }

        productComponentList.stream().map(BuildingBlockMetricSummary::getInstance)
                .filter(buildingBlockMetricSummary -> !buildingBlockMetricSummaryList.contains(buildingBlockMetricSummary))
                .forEach(buildingBlockMetricSummaryList::add);

        return buildingBlockMetricSummaryList;
    }
}
