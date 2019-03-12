package com.capitalone.dashboard.exec.model;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.capitalone.dashboard.exec.util.HygieiaExecutiveUtil.getEmptyTimeSeries;

@Document(collection = "portfolio_metric")
public class PortfolioMetricDetail extends RollupMetricDetail {
    private List<ProductMetricDetail> productMetricDetailList;


    public PortfolioMetricDetail() {
        setLevel(MetricLevel.PORTFOLIO);
    }

    public MetricSummary getSummary() {
        return summary;
    }

    public void setSummary(MetricSummary summary) {
        this.summary = summary;
    }

    public List<ProductMetricDetail> getProductMetricDetailList() {
        return productMetricDetailList;
    }

    public void setProductMetricDetailList(List<ProductMetricDetail> productMetricDetailList) {
        this.productMetricDetailList = productMetricDetailList;
    }


    public void addProductMetricDetail(ProductMetricDetail productMetricDetail) {
        if (productMetricDetail.getSummary() == null) {
            return;
        }
        if (getType() == null) {
            setType(productMetricDetail.getType());
        }
        if (productMetricDetailList == null) {
            productMetricDetailList = new ArrayList<>();
        }
        productMetricDetailList.add(productMetricDetail);
        updateSummary();
        updateTimeSeries();
        productMetricDetail.setProcessed(true);
    }

    protected void updateSummary() {
        if (summary == null) {
            summary = new MetricSummary();
        }
        summary.setLastUpdated(new Date());
        productMetricDetailList.forEach(metricDetails -> updateSummary(metricDetails,productMetricDetailList.size() ));
    }

    protected void updateTimeSeries() {
        if (timeSeries == null) {
            timeSeries = getEmptyTimeSeries();
        }
        productMetricDetailList.forEach(itemMetricDetails -> updateTimeSeries(itemMetricDetails, productMetricDetailList.size()));
    }

    public static PortfolioMetricDetail getInstance(String name, String lob, MetricType metricType) {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(lob) || (metricType == null)) { return null; }

        PortfolioMetricDetail portfolioMetricDetail = new PortfolioMetricDetail();
        portfolioMetricDetail.setName(name);
        portfolioMetricDetail.setLob(lob);
        portfolioMetricDetail.setLevel(MetricLevel.PORTFOLIO);
        portfolioMetricDetail.setType(metricType);
        MetricSummary metricSummary = new MetricSummary();
        metricSummary.setName(metricType.getName());
        metricSummary.setCounts(new ArrayList<>());
        portfolioMetricDetail.setSummary(metricSummary);

        return portfolioMetricDetail;
    }
}
