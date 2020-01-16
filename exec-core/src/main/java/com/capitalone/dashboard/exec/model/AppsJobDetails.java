package com.capitalone.dashboard.exec.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "apps_job_details")
public class AppsJobDetails extends BaseModel {

	private String appId;
	private List<String> buildJobs;
	private List<String> deployJobs;
	private List<String> otherJobs;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public List<String> getBuildJobs() {
		return buildJobs;
	}

	public void setBuildJobs(List<String> buildJobs) {
		this.buildJobs = buildJobs;
	}

	public List<String> getDeployJobs() {
		return deployJobs;
	}

	public void setDeployJobs(List<String> deployJobs) {
		this.deployJobs = deployJobs;
	}

	public List<String> getOtherJobs() {
		return otherJobs;
	}

	public void setOtherJobs(List<String> otherJobs) {
		this.otherJobs = otherJobs;
	}

}
