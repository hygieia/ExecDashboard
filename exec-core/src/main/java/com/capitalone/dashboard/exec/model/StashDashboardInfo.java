package com.capitalone.dashboard.exec.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "stash_dashboard_details")
public class StashDashboardInfo extends BaseModel {

	private String appId;
	private List<RepoDetails> repoDetails;
	private List<String> buildJobs;
	private String jobMode;

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

	public List<RepoDetails> getRepoDetails() {
		return repoDetails;
	}

	public void setRepoDetails(List<RepoDetails> repoDetails) {
		this.repoDetails = repoDetails;
	}

	public String getJobMode() {
		return jobMode;
	}

	public void setJobMode(String jobMode) {
		this.jobMode = jobMode;
	}
}
