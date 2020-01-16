package com.capitalone.dashboard.exec.model;

import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
 *
 *
 */

@Document(collection = "metrics_detail")
public class MetricsDetail extends BaseModel {

	@Indexed(name = "metricLevelId")
	private String metricLevelId; // AppId - Product, Eid - Portfolio
	private String name;
	private String lob;
	private MetricLevel level; // PORTFOLIO, PRODUCT ...
	private MetricType type; // PRODUCTION_INCIDENTS, STATIC_CODE_ANALYSIS ...
	protected MetricSummary summary;
	protected List<MetricTimeSeriesElement> timeSeries;
	private int totalComponents;
	private int reportingComponents;
	private String customField;
	protected boolean processed;

	/**
	 * @return the metricLevelId
	 */
	public String getMetricLevelId() {
		return metricLevelId;
	}

	/**
	 * @param metricLevelId
	 *            the metricLevelId to set
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

	public MetricSummary getSummary() {
		return summary;
	}

	public void setSummary(MetricSummary summary) {
		this.summary = summary;
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

	public String getCustomField() {
		return customField;
	}

	public void setCustomField(String customField) {
		this.customField = customField;
	}

}
