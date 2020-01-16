package com.capitalone.dashboard.exec.model;

import java.util.Date;
import java.util.List;

/**
 * Model ExecutiveModuleMetrics
 * 
 * @param
 * @return
 */
public class ExecutiveModuleMetrics {

	private String moduleName;
	private Double trendSlope;
	private Date lastScanned;
	private Date lastUpdated;
	private String teamId;
	private List<ExecutiveMetricsSeries> series;

	public List<ExecutiveMetricsSeries> getSeries() {
		return series;
	}

	public void setSeries(List<ExecutiveMetricsSeries> series) {
		this.series = series;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public Double getTrendSlope() {
		return trendSlope;
	}

	public void setTrendSlope(Double trendSlope) {
		this.trendSlope = trendSlope;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
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

}
