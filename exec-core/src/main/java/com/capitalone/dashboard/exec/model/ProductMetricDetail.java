package com.capitalone.dashboard.exec.model;

import org.springframework.data.annotation.Transient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.capitalone.dashboard.exec.util.HygieiaExecutiveUtil.getEmptyTimeSeries;

public class ProductMetricDetail extends RollupMetricDetail {
    private List<ComponentMetricDetail> componentMetricDetailList;

    @Transient
    private List<CollectorItemMetricDetail> collectorItemMetricDetailList;

    public ProductMetricDetail() {
        setLevel(MetricLevel.PRODUCT);
    }

    public MetricSummary getSummary() { return summary; }
    public void setSummary(MetricSummary summary) { this.summary = summary; }

    public List<ComponentMetricDetail> getComponentMetricDetailList() {
        return componentMetricDetailList;
    }

    public void setComponentMetricDetailList(List<ComponentMetricDetail> componentMetricDetailList) {
        this.componentMetricDetailList = componentMetricDetailList;
    }

    public void addComponentMetricDetail (ComponentMetricDetail componentMetricDetail) {
        if (componentMetricDetail.getSummary() == null) {
            return;
        }
        if (getType() == null) {
            setType(componentMetricDetail.getType());
        }
        if (componentMetricDetailList == null) {
            componentMetricDetailList = new ArrayList<>();
        }
        this.componentMetricDetailList.add(componentMetricDetail);
        updateSummary(true);
        updateTimeSeries(true);
        componentMetricDetail.setProcessed(true);
    }

    /*
    *   This is to be able to add a Collector Item directly to a Product, without adding it to a Component under the Product
    */
    public void addCollectorItemMetricDetail(CollectorItemMetricDetail collectorItemMetricDetail) {
        if (collectorItemMetricDetail.getSummary() == null) {
            return;
        }
        if (getType() == null) {
            setType(collectorItemMetricDetail.getType());
        }
        if (collectorItemMetricDetailList == null) {
            collectorItemMetricDetailList = new ArrayList<>();
        }
        this.collectorItemMetricDetailList.add(collectorItemMetricDetail);
        updateSummary(false);
        updateTimeSeries(false);
        collectorItemMetricDetail.setProcessed(true);
    }

    protected void updateSummary(boolean isComponentMetricDetail) {
        if(summary == null){
            summary = new MetricSummary();
        }
        summary.setLastUpdated(new Date());
        if (isComponentMetricDetail) {
            componentMetricDetailList.forEach(this::updateSummary);
        } else {
            collectorItemMetricDetailList.forEach(this::updateSummary);
        }
    }

    protected void updateTimeSeries(boolean isComponentMetricDetail) {
        if (timeSeries == null) {
            timeSeries = getEmptyTimeSeries();
        }
        if (isComponentMetricDetail) {
            componentMetricDetailList.forEach(this::updateTimeSeries);
        } else {
            collectorItemMetricDetailList.forEach(this::updateTimeSeries);
        }
    }
}
