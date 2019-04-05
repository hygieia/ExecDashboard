package com.capitalone.dashboard.exec.model.vz;

import java.util.List;

public class ExecutiveMetricsSeries {

	private Long timeStamp;
	private int daysAgo;
	private List<SeriesCount> counts;

	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public List<SeriesCount> getCounts() {
		return counts;
	}

	public void setCounts(List<SeriesCount> counts) {
		this.counts = counts;
	}

	public int getDaysAgo() {
		return daysAgo;
	}

	public void setDaysAgo(int daysAgo) {
		this.daysAgo = daysAgo;
	}

}
