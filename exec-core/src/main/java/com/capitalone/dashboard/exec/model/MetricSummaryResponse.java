package com.capitalone.dashboard.exec.model;

import java.util.Date;
import java.util.List;

public class MetricSummaryResponse {
	private List<MetricCountResponse> counts;
	private Date lastScanned;
	private Date lastUpdated;
	private double trendSlope;
	private int totalComponents;
	// application having ThroughPut
	private int reportingComponents;
	// Metrics Name
	private String name;
	private String confMessage;
	private Boolean dataAvailable;
	private String appCriticality;

	public String getAppCriticality() {
		return appCriticality;
	}

	public void setAppCriticality(String appCriticality) {
		this.appCriticality = appCriticality;
	}

	public List<MetricCountResponse> getCounts() {
		return counts;
	}

	public void setCounts(List<MetricCountResponse> counts) {
		this.counts = counts;
	}

	public Date getLastScanned() {
		return lastScanned;
	}

	public void setLastScanned(Date lastScanned) {
		this.lastScanned = lastScanned;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public double getTrendSlope() {
		return trendSlope;
	}

	public void setTrendSlope(double trendSlope) {
		this.trendSlope = trendSlope;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getConfMessage() {
		return confMessage;
	}

	public void setConfMessage(String confMessage) {
		this.confMessage = confMessage;
	}

	public Boolean getDataAvailable() {
		return dataAvailable;
	}

	public void setDataAvailable(Boolean dataAvailable) {
		this.dataAvailable = dataAvailable;
	}
}
