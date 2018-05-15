package com.capitalone.dashboard.exec.model;

import org.springframework.data.annotation.Transient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.capitalone.dashboard.exec.util.HygieiaExecutiveUtil.getEmptyTimeSeries;

public class ComponentMetricDetail extends RollupMetricDetail {
    private ProductComponent item;

    public ComponentMetricDetail() {
        setLevel(MetricLevel.COMPONENT);
    }

    @Transient
    private List<CollectorItemMetricDetail> collectorItemMetricDetailList;

    private void updateSummary() {
        if (summary == null) {
            summary = new MetricSummary();
        }
        summary.setLastUpdated(new Date());
        collectorItemMetricDetailList.forEach(this::updateSummary);
    }


    private void updateTimeSeries() {
        if (timeSeries == null) {
            timeSeries = getEmptyTimeSeries();
        }
        collectorItemMetricDetailList.forEach(this::updateTimeSeries);
    }

    public List<CollectorItemMetricDetail> getCollectorItemMetricDetailList() {
        return collectorItemMetricDetailList;
    }

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
        updateSummary();
        updateTimeSeries();
        collectorItemMetricDetail.setProcessed(true);
    }

    public void setSummary(MetricSummary summary) {
        this.summary = summary;
    }

    public MetricSummary getSummary() {
        return summary;
    }

    public ProductComponent getItem() {
        return item;
    }

    public void setItem(ProductComponent item) {
        this.item = item;
    }

}
