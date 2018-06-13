package com.capitalone.dashboard.exec.model;

import java.util.List;
import java.util.Objects;

public class MetricDetails {
    private String name;
    private String lob;
    private MetricLevel level;  // PORTFOLIO, PRODUCT ...
    private MetricType type;    // PRODUCTION_INCIDENTS, STATIC_CODE_ANALYSIS ...
    protected MetricSummary summary;
    protected List<MetricTimeSeriesElement> timeSeries;
    private int totalComponents;
    private int reportingComponents;
    protected boolean processed;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLob() {
        return lob;
    }

    public void setLob(String lob) {
        this.lob = lob;
    }

    public MetricLevel getLevel() {
        return level;
    }

    public void setLevel(MetricLevel level) {
        this.level = level;
    }

    public MetricType getType() {
        return type;
    }

    public void setType(MetricType type) {
        this.type = type;
    }

    public List<MetricTimeSeriesElement> getTimeSeries() {
        return timeSeries;
    }

    public void setTimeSeries(List<MetricTimeSeriesElement> timeSeries) {
        this.timeSeries = timeSeries;
    }

    public int getTotalComponents() {
        return totalComponents;
    }

    public void setTotalComponents(int totalComponents) {
        this.totalComponents = totalComponents;
    }

    public int getReportingComponents() {
        return reportingComponents;
    }

    public void setReportingComponents(int reportingComponents) {
        this.reportingComponents = reportingComponents;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public MetricSummary getSummary() {
        return summary;
    }

    public void setSummary(MetricSummary summary) {
        this.summary = summary;
    }
}
