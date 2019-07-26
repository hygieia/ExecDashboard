package com.capitalone.dashboard.exec.model;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "portfolio_metrics_details")
@CompoundIndex(def = "{'eid':1, 'metricsName':1}", name = "eid_metricsName")
public class MetricPortfolioDetailResponse extends BaseModel {

	@Indexed(name = "eid")
	String eid;
	MetricSummaryResponse summary;
	List<MetricTimeSeriesElementResponse> timeSeries;
	ObjectId executiveObjectId;
	@Indexed(name = "metricsName")
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

	public ObjectId getExecutiveObjectId() {
		return executiveObjectId;
	}

	public void setExecutiveObjectId(ObjectId executiveObjectId) {
		this.executiveObjectId = executiveObjectId;
	}

	public String getMetricsName() {
		return metricsName;
	}

	public void setMetricsName(String metricsName) {
		this.metricsName = metricsName;
	}

	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}
}
