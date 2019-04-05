package com.capitalone.dashboard.exec.model.vz;

import java.util.Map;

/**
 * Model ComputedPipelineMetrics
 * @param
 * @return 
 */
public class ComputedPipelineMetrics {
	
	private Map<String, Object> stageDetails;
	private String apiUrl;
	private long beginTimestamp;
	private long endTimestamp;
	private int interval;
	
	public Map<String, Object> getStageDetails() {
		return stageDetails;
	}
	public void setStageDetails(Map<String, Object> stageDetails) {
		this.stageDetails = stageDetails;
	}
	public String getApiUrl() {
		return apiUrl;
	}
	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}
	public long getBeginTimestamp() {
		return beginTimestamp;
	}
	public void setBeginTimestamp(long beginTimestamp) {
		this.beginTimestamp = beginTimestamp;
	}
	public long getEndTimestamp() {
		return endTimestamp;
	}
	public void setEndTimestamp(long endTimestamp) {
		this.endTimestamp = endTimestamp;
	}
	
	public int getInterval() {
		return interval;
	}
	public void setInterval(int interval) {
		this.interval = interval;
	}

}
