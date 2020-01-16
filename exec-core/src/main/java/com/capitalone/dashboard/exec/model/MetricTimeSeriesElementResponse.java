package com.capitalone.dashboard.exec.model;

import java.util.List;

public class MetricTimeSeriesElementResponse {
	private int daysAgo;
	private List<MetricCountResponse> counts;

	public int getDaysAgo() {
		return daysAgo;
	}

	public void setDaysAgo(int daysAgo) {
		this.daysAgo = daysAgo;
	}

	public List<MetricCountResponse> getCounts() {
		return counts;
	}

	public void setCounts(List<MetricCountResponse> counts) {
		this.counts = counts;
	}
}
