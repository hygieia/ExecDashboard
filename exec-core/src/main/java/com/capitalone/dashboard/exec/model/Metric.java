package com.capitalone.dashboard.exec.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "component_metric")
public class Metric  {
    @Id
    private ObjectId id;

    private String name;
    private boolean reporting;
    private Date lastScanned;
    private Date lastUpdated;
    private double trendSlope;
    private Long secondaryTime;
    private ObjectId componentDashboardId;
    private List<MetricTimeSeriesElement> series;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public boolean isReporting() { return reporting; }
    public void setReporting(boolean reporting) { this.reporting = reporting; }
    public Date getLastScanned() { return lastScanned; }
    public void setLastScanned(Date lastScanned) { this.lastScanned = lastScanned; }
    public Date getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(Date lastUpdated) { this.lastUpdated = lastUpdated; }
    public double getTrendSlope() { return trendSlope; }
    public void setTrendSlope(double trendSlope) { this.trendSlope = trendSlope; }
    public Long getSecondaryTime() { return secondaryTime; }
    public void setSecondaryTime(Long secondaryTime) { this.secondaryTime = secondaryTime; }
    public List<MetricTimeSeriesElement> getSeries() { return series; }
    public void setSeries(List<MetricTimeSeriesElement> series) { this.series = series; }


    public ObjectId getComponentDashboardId() {
        return componentDashboardId;
    }

    public void setComponentDashboardId(ObjectId componentDashboardId) {
        this.componentDashboardId = componentDashboardId;
    }
}
