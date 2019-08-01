package com.capitalone.dashboard.exec.model;

import java.util.List;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "app_metrics_details")
@CompoundIndex(def = "{'appId':1, 'metricsName':1}", name = "appId_metricsName")
public class MetricDetailResponse extends BaseModel {

	@Indexed(name = "appId")
	String appId;
	MetricSummaryResponse summary;
	List<MetricTimeSeriesElementResponse> timeSeries;
	String metricsName;

	public MetricSummaryResponse getSummary() {
		return summary;
	}

	public void setSummary(MetricSummaryResponse summary) {
		this.summary = summary;
	}

	public List<MetricTimeSeriesElementResponse> getTimeSeries() {
		return timeSeries;
	}

	public void setTimeSeries(List<MetricTimeSeriesElementResponse> timeSeries) {
		this.timeSeries = timeSeries;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getMetricsName() {
		return metricsName;
	}

	public void setMetricsName(String metricsName) {
		this.metricsName = metricsName;
	}
}
