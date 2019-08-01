package com.capitalone.dashboard.exec.model;

import java.util.List;

public class StashDetails {

	private String jobMode;
	private List<String> buildJobs;
	private List<RepoDetails> repoDetails;

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

	public List<String> getJobName() {
		return buildJobs;
	}

	public void setJobName(List<String> jobName) {
		this.buildJobs = jobName;
	}

}
