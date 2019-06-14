package com.capitalone.dashboard.exec.collector;

import com.capitalone.dashboard.exec.model.CollectorItemMetricDetail;
import com.capitalone.dashboard.exec.model.CollectorType;
import com.capitalone.dashboard.exec.model.ComponentMetricDetail;
import com.capitalone.dashboard.exec.model.MetricCollectionStrategy;
import com.capitalone.dashboard.exec.model.MetricCount;
import com.capitalone.dashboard.exec.model.MetricType;
import com.capitalone.dashboard.exec.model.Portfolio;
import com.capitalone.dashboard.exec.model.PortfolioMetricDetail;
import com.capitalone.dashboard.exec.model.Product;
import com.capitalone.dashboard.exec.model.Lob;
import com.capitalone.dashboard.exec.model.LobMetricDetail;
import com.capitalone.dashboard.exec.model.ProductComponent;
import com.capitalone.dashboard.exec.model.ProductMetricDetail;
import com.capitalone.dashboard.exec.repository.PortfolioMetricRepository;
import com.capitalone.dashboard.exec.repository.LobMetricRepository;
import org.apache.commons.collections.CollectionUtils;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Collections;

@Component
public abstract class DefaultMetricCollector {
    private Map<String, List<String>> dashboardCollectorItemsMap = new HashMap<>();
    private PortfolioMetricRepository portfolioMetricRepository;
    private LobMetricRepository lobMetricRepository;

    @Autowired
    public DefaultMetricCollector(PortfolioMetricRepository portfolioMetricRepository) {
        this.portfolioMetricRepository = portfolioMetricRepository;
    }

    @Autowired
    public DefaultMetricCollector(LobMetricRepository lobMetricRepository) {
        this.lobMetricRepository = lobMetricRepository;
    }

    public void collect(SparkSession sparkSession, JavaSparkContext javaSparkContext, List<?> objectList) {
        if ((sparkSession == null) || (javaSparkContext == null) || CollectionUtils.isEmpty(objectList)) { return; }

        if (objectList.get(0) instanceof Portfolio){
            collectPortFolioMetrics(sparkSession, javaSparkContext, (List<Portfolio>) objectList);
            return;
        }
        if (objectList.get(0) instanceof Lob){
            collectLobMetrics(sparkSession, javaSparkContext, (List<Lob>) objectList);
            return;
        }
    }

    public void collectPortFolioMetrics(SparkSession sparkSession, JavaSparkContext javaSparkContext, List<Portfolio> portfolioList) {
        if ((sparkSession == null) || (javaSparkContext == null)) { return; }

        List<String> collectorItemList = getCollectorItemListForPortfolios(portfolioList, sparkSession, javaSparkContext);

        DefaultDataCollector dataCollector
                = new DefaultDataCollector(getCollection(), getQuery(), collectorItemList, sparkSession, javaSparkContext);
        Map<String, List<Row>> rowsListMap = dataCollector.collectAll();
        boolean deleteFlag = true;

        for (Portfolio portfolio: portfolioList) {
            PortfolioMetricDetail portfolioMetricDetail = new PortfolioMetricDetail();
            List<Product> products = portfolio.getProducts();
            portfolioMetricDetail.setName(portfolio.getName());
            portfolioMetricDetail.setLob(portfolio.getLob());
            for (Product product: products) {
                List<ProductComponent> productComponents = product.getProductComponentList();
                ProductMetricDetail productMetricDetail = new ProductMetricDetail();
                productMetricDetail.setName(product.getName());
                productMetricDetail.setLob(product.getLob());
                productComponents.forEach(productComponent -> {
                    ComponentMetricDetail componentMetricDetail = new ComponentMetricDetail();
                    componentMetricDetail.setItem(productComponent);
                    componentMetricDetail.setName(productComponent.getName());
                    componentMetricDetail.setLob(productComponent.getLob());
                    ObjectId dashboardId = productComponent.getProductComponentDashboardId();
                    if (dashboardId == null) { return; }
                    List<String> collectorItems = dashboardCollectorItemsMap.get(dashboardId.toString()) != null ? dashboardCollectorItemsMap.get(dashboardId.toString()) : new ArrayList<>();
                    collectorItems.stream().map(collectorItem -> getCollectorItemMetricDetail(rowsListMap.get(collectorItem), getMetricType())).forEach(componentMetricDetail::addCollectorItemMetricDetail);
                    productMetricDetail.addComponentMetricDetail(componentMetricDetail);
                });
                productMetricDetail.setTotalComponents(productComponents.size());
                portfolioMetricDetail.addProductMetricDetail(productMetricDetail);
            }
            if (portfolioMetricDetail.getSummary() != null) {
                portfolioMetricDetail.setTotalComponents(products.size());
                if (deleteFlag) { // Clear the metrics results in the database before saving the first newly generated result
                    portfolioMetricRepository.deleteAllByType(getMetricType());
                    deleteFlag = false;
                }
                portfolioMetricRepository.save(portfolioMetricDetail);
            }
        }
    }


    public void collectLobMetrics(SparkSession sparkSession, JavaSparkContext javaSparkContext, List<Lob> lobList) {
        if ((sparkSession == null) || (javaSparkContext == null)) { return; }

        List<String> collectorItemList = getCollectorItemListForLobs(lobList, sparkSession, javaSparkContext);

        DefaultDataCollector dataCollector
                = new DefaultDataCollector(getCollection(), getQuery(), collectorItemList, sparkSession, javaSparkContext);
        Map<String, List<Row>> rowsListMap = dataCollector.collectAll();
        boolean deleteFlag = true;

        for (Lob lob: lobList) {
            LobMetricDetail lobMetricDetail = new LobMetricDetail();
            List<Product> products = lob.getProducts();
            lobMetricDetail.setName(lob.getName());
            lobMetricDetail.setLob(lob.getLob());
            for (Product product: products) {
                List<ProductComponent> productComponents = product.getProductComponentList();
                ProductMetricDetail productMetricDetail = new ProductMetricDetail();
                productMetricDetail.setName(product.getName());
                productMetricDetail.setLob(product.getLob());
                productComponents.forEach(productComponent -> {
                    ComponentMetricDetail componentMetricDetail = new ComponentMetricDetail();
                    componentMetricDetail.setItem(productComponent);
                    componentMetricDetail.setName(productComponent.getName());
                    componentMetricDetail.setLob(productComponent.getLob());
                    ObjectId dashboardId = productComponent.getProductComponentDashboardId();
                    if (dashboardId == null) { return; }
                    List<String> collectorItems = dashboardCollectorItemsMap.get(dashboardId.toString()) != null ? dashboardCollectorItemsMap.get(dashboardId.toString()) : new ArrayList<>();
                    collectorItems.stream().map(collectorItem -> getCollectorItemMetricDetail(rowsListMap.get(collectorItem), getMetricType())).forEach(componentMetricDetail::addCollectorItemMetricDetail);
                    productMetricDetail.addComponentMetricDetail(componentMetricDetail);
                });
                lobMetricDetail.setTotalComponents(productComponents.size());
                lobMetricDetail.addProductMetricDetail(productMetricDetail);
            }
            if (lobMetricDetail.getSummary() != null) {
                lobMetricDetail.setTotalComponents(products.size());
                if (deleteFlag) { // Clear the metrics results in the database before saving the first newly generated result
                    lobMetricRepository.deleteAllByType(getMetricType());
                    deleteFlag = false;
                }
                lobMetricRepository.save(lobMetricDetail);
            }
        }
    }

    protected abstract MetricType getMetricType();

    protected abstract String getQuery();

    protected abstract String getCollection();

    protected abstract MetricCollectionStrategy getCollectionStrategy();

    protected abstract CollectorItemMetricDetail getCollectorItemMetricDetail(List<Row> rowList, MetricType metricType);

    protected abstract CollectorType getCollectorType();

    protected abstract MetricCount getMetricCount(String level, double value, String type);

    private List<String> getCollectorItemListForPortfolios(List<Portfolio> portfolioList, SparkSession sparkSession, JavaSparkContext javaSparkContext) {
        dashboardCollectorItemsMap
                = DashBoardCollectorItemMapBuilder.getDashboardNameCollectorItemsMapById(getCollectorType(), sparkSession, javaSparkContext);

        List<String> collectorItemList = new ArrayList<>();
        Optional.ofNullable(portfolioList).orElseGet(Collections::emptyList).stream()
                .map(Portfolio::getProducts)
                .forEach(products -> products.stream()
                        .map(Product::getProductComponentList)
                        .forEach(productComponents -> productComponents
                                .stream()
                                .map(ProductComponent::getProductComponentDashboardId)
                                .filter(Objects::nonNull)
                                .<List<String>>map(dashboardId -> dashboardCollectorItemsMap.get(dashboardId.toString()) != null ? dashboardCollectorItemsMap.get(dashboardId.toString()) : new ArrayList<>())
                                .forEach(collectorItemList::addAll)));
        return collectorItemList;
    }

    private List<String> getCollectorItemListForLobs(List<Lob> lobList, SparkSession sparkSession, JavaSparkContext javaSparkContext) {
        dashboardCollectorItemsMap
                = DashBoardCollectorItemMapBuilder.getDashboardNameCollectorItemsMapById(getCollectorType(), sparkSession, javaSparkContext);

        List<String> collectorItemList = new ArrayList<>();
        Optional.ofNullable(lobList).orElseGet(Collections::emptyList).stream()
                .map(Lob::getProducts)
                .forEach(products -> products.stream()
                        .map(Product::getProductComponentList)
                        .forEach(productComponents -> productComponents
                                .stream()
                                .map(ProductComponent::getProductComponentDashboardId)
                                .filter(Objects::nonNull)
                                .<List<String>>map(dashboardId -> dashboardCollectorItemsMap.get(dashboardId.toString()) != null ? dashboardCollectorItemsMap.get(dashboardId.toString()) : new ArrayList<>())
                                .forEach(collectorItemList::addAll)));
        return collectorItemList;
    }
}