package com.capitalone.dashboard.exec.model;

import java.util.Date;
import java.util.List;

public class ExecutiveMetrics {

	private String metricsName;
	private Double trendSlope;
	private Date lastScanned;
	private Date lastUpdated;
	private List<ExecutiveModuleMetrics> modules;

	public String getMetricsName() {
		return metricsName;
	}

	public void setMetricsName(String metricsName) {
		this.metricsName = metricsName;
	}

	public Double getTrendSlope() {
		return trendSlope;
	}

	public void setTrendSlope(Double trendSlope) {
		this.trendSlope = trendSlope;
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

	public List<ExecutiveModuleMetrics> getModules() {
		return modules;
	}

	public void setModules(List<ExecutiveModuleMetrics> modules) {
		this.modules = modules;
	}

}
