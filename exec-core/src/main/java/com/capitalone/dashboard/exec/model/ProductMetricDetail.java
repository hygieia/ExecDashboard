package com.capitalone.dashboard.exec.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.capitalone.dashboard.exec.util.HygieiaExecutiveUtil.getEmptyTimeSeries;

public class ProductMetricDetail extends RollupMetricDetail {
    private List<ComponentMetricDetail> componentMetricDetailList;


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
        updateSummary();
        updateTimeSeries();
        componentMetricDetail.setProcessed(true);
    }

    protected void updateSummary() {
        if(summary == null){
            summary = new MetricSummary();
        }
        summary.setLastUpdated(new Date());
        componentMetricDetailList.forEach(this::updateSummary);
    }

    protected void updateTimeSeries() {
        if (timeSeries == null) {
            timeSeries = getEmptyTimeSeries();
        }
        componentMetricDetailList.forEach(this::updateTimeSeries);
    }
}
