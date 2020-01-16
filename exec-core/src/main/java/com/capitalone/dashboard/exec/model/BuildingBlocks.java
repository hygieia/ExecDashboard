package com.capitalone.dashboard.exec.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
 *
 *
 */

@Document(collection = "building_blocks")
public class BuildingBlocks extends BaseModel {

	@Indexed(name = "metricLevelId")
	private String metricLevelId; // AppId - Product/Component , Eid - Portfolio
	private String name;
	private String commonName;
	private int completeness;
	private String dashboardDisplayName;
	private String lob;
	private String poc;
	private String url;
	private MetricLevel metricLevel;
	private int totalExpectedMetrics;
	private int totalComponents;
	private int reportingComponents;
	private MetricType metricType;
	private List<MetricSummary> metrics = new ArrayList<>();
	private String appCriticality;
	private String customField;


	/**
	 * @return the metricLevelId
	 */
	public String getMetricLevelId() {
		return metricLevelId;
	}

	/**
	 * @param metricLevelId the metricLevelId to set
	 */
	public void setMetricLevelId(String metricLevelId) {
		this.metricLevelId = metricLevelId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCommonName() {
		return commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	public int getCompleteness() {
		return completeness;
	}

	public void setCompleteness(int completeness) {
		this.completeness = completeness;
	}

	public String getDashboardDisplayName() {
		return dashboardDisplayName;
	}

	public void setDashboardDisplayName(String dashboardDisplayName) {
		this.dashboardDisplayName = dashboardDisplayName;
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

	public MetricLevel getMetricLevel() {
		return metricLevel;
	}

	public void setMetricLevel(MetricLevel metricLevel) {
		this.metricLevel = metricLevel;
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

	public int getReportingComponents() {
		return reportingComponents;
	}

	public void setReportingComponents(int reportingComponents) {
		this.reportingComponents = reportingComponents;
	}

	public MetricType getMetricType() {
		return metricType;
	}

	public void setMetricType(MetricType metricType) {
		this.metricType = metricType;
	}

	public List<MetricSummary> getMetrics() {
		return metrics;
	}

	public void setMetrics(List<MetricSummary> metrics) {
		this.metrics = metrics;
	}

	public String getAppCriticality() {
		return appCriticality;
	}

	public void setAppCriticality(String appCriticality) {
		this.appCriticality = appCriticality;
	}

	public String getCustomField() {
		return customField;
	}

	public void setCustomField(String customField) {
		this.customField = customField;
	}

}
