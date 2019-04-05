package com.capitalone.dashboard.exec.model.vz;

import java.util.List;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "executives_metrics")
@CompoundIndex(def = "{'appId':1, 'metrics.metricsName':1}", name = "appId_metricsName")
public class ExecutiveComponents extends BaseModel {

	@Indexed(name = "appId")
	private String appId;
	private String appName;
	private String teamBoardLink;
	private List<ExecutiveMetrics> metrics;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getTeamBoardLink() {
		return teamBoardLink;
	}

	public void setTeamBoardLink(String teamBoardLink) {
		this.teamBoardLink = teamBoardLink;
	}

	public List<ExecutiveMetrics> getMetrics() {
		return metrics;
	}

	public void setMetrics(List<ExecutiveMetrics> metrics) {
		this.metrics = metrics;
	}

}