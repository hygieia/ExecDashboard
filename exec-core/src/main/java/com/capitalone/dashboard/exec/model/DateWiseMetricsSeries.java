package com.capitalone.dashboard.exec.model;

import java.util.List;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Model DateWiseMetricsSeries
 * 
 * @param
 * @return
 */
@Document(collection = "date_wise_metrics")
@CompoundIndex(def = "{'appId':1, 'metricsName':1}", name = "appId_metricsName")
public class DateWiseMetricsSeries extends BaseModel {

	@Indexed(name = "appId")
	private String appId;
	private String moduleName;
	private String metricsName;
	private String dateValue;
	private Long timeStamp;
	private String teamId;
	private List<SeriesCount> counts;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getMetricsName() {
		return metricsName;
	}

	public void setMetricsName(String metricsName) {
		this.metricsName = metricsName;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getDateValue() {
		return dateValue;
	}

	public void setDateValue(String dateValue) {
		this.dateValue = dateValue;
	}

	public List<SeriesCount> getCounts() {
		return counts;
	}

	public void setCounts(List<SeriesCount> counts) {
		this.counts = counts;
	}

}
