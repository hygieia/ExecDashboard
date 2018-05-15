package com.capitalone.dashboard.exec.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BuildingBlockMetricSummary {
    private String id;
    private String name;
    private String commonName;
    private String dashboardDisplayName;
    private String lob;
    private String poc;
    private String url;
    private MetricLevel itemType;
    private int totalExpectedMetrics;
    private int totalComponents;
    private int reportingComponents;
    private List<MetricSummary> metrics = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getPoc() {
        return poc;
    }

    public void setPoc(String poc) {
        this.poc = poc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTotalExpectedMetrics() {
        return totalExpectedMetrics;
    }

    public void setTotalExpectedMetrics(int totalExpectedMetrics) {
        this.totalExpectedMetrics = totalExpectedMetrics;
    }

    public int getTotalComponents() {
        return totalComponents;
    }

    public void setTotalComponents(int totalComponents) {
        this.totalComponents = totalComponents;
    }

    public List<MetricSummary> getMetrics() { return metrics; }

    public void setMetrics(List<MetricSummary> metrics) {
        this.metrics = metrics;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getDashboardDisplayName() {
        return dashboardDisplayName;
    }

    public void setDashboardDisplayName(String dashboardDisplayName) {
        this.dashboardDisplayName = dashboardDisplayName;
    }

    public MetricLevel getItemType() {
        return itemType;
    }

    public void setItemType(MetricLevel itemType) {
        this.itemType = itemType;
    }

    public int getReportingComponents() {
        return reportingComponents;
    }

    public void setReportingComponents(int reportingComponents) {
        this.reportingComponents = reportingComponents;
    }

    public static BuildingBlockMetricSummary getInstance(BaseConfigItem fromItem) {
        if (fromItem == null) {return null;}

        BuildingBlockMetricSummary response = new BuildingBlockMetricSummary();
        response.setId(fromItem.getName());
        response.setName(fromItem.getName());
        response.setCommonName(fromItem.getCommonName());
        response.setLob(fromItem.getLob());
        response.setItemType(fromItem.getMetricLevel());
        response.setMetrics(new ArrayList<>());
        return response;
    }

    public static BuildingBlockMetricSummary getInstance(MetricDetails fromItem) {
        if (fromItem == null) {return null;}

        BuildingBlockMetricSummary response = new BuildingBlockMetricSummary();
        response.setId(fromItem.getName());
        response.setName(fromItem.getName());
        response.setLob(fromItem.getLob());
        response.setItemType(fromItem.getLevel());
        response.setMetrics(new ArrayList<>());
        return response;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BuildingBlockMetricSummary)) return false;
        BuildingBlockMetricSummary that = (BuildingBlockMetricSummary) o;
        return Objects.equals(getName(), that.getName()) &&
                Objects.equals(getLob(), that.getLob());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getLob());
    }
}
