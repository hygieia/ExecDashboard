package com.capitalone.dashboard.exec.model.vz;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "jenkins_unlimited")
public class JenkinsUnlimitedData extends BaseModel {

	@Indexed(name = "appId")
	private String appId;
	private String portfolio;
	private Boolean prodJobAvailable;
	@Indexed(name = "buildJob")
	private String buildJob;
	private List<String> deployJobs;
	private Map<Integer, String> deployOrderedJobs;
	private Long duration;
	private String durationTimeStamp;
	private Integer period;
	private Long commits;
	private Long beginTime;
	private Boolean nonProdJobConsidered;
	private Long nonProdCommits;
	private Long nonProdDuration;
	private String nonProdDurationTimeStamp;
	

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(String portfolio) {
		this.portfolio = portfolio;
	}

	public Boolean getProdJobAvailable() {
		return prodJobAvailable;
	}

	public void setProdJobAvailable(Boolean prodJobAvailable) {
		this.prodJobAvailable = prodJobAvailable;
	}

	public String getBuildJob() {
		return buildJob;
	}

	public void setBuildJob(String buildJob) {
		this.buildJob = buildJob;
	}

	public List<String> getDeployJobs() {
		return deployJobs;
	}

	public void setDeployJobs(List<String> deployJobs) {
		this.deployJobs = deployJobs;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public String getDurationTimeStamp() {
		return durationTimeStamp;
	}

	public void setDurationTimeStamp(String durationTimeStamp) {
		this.durationTimeStamp = durationTimeStamp;
	}

	public Long getCommits() {
		return commits;
	}

	public void setCommits(Long commits) {
		this.commits = commits;
	}

	public Map<Integer, String> getDeployOrderedJobs() {
		return deployOrderedJobs;
	}

	public void setDeployOrderedJobs(Map<Integer, String> deployOrderedJobs) {
		this.deployOrderedJobs = deployOrderedJobs;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public Long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Long beginTime) {
		this.beginTime = beginTime;
	}

	public Boolean getNonProdJobConsidered() {
		return nonProdJobConsidered;
	}

	public void setNonProdJobConsidered(Boolean nonProdJobConsidered) {
		this.nonProdJobConsidered = nonProdJobConsidered;
	}

	public Long getNonProdCommits() {
		return nonProdCommits;
	}

	public void setNonProdCommits(Long nonProdCommits) {
		this.nonProdCommits = nonProdCommits;
	}

	public Long getNonProdDuration() {
		return nonProdDuration;
	}

	public void setNonProdDuration(Long nonProdDuration) {
		this.nonProdDuration = nonProdDuration;
	}

	public String getNonProdDurationTimeStamp() {
		return nonProdDurationTimeStamp;
	}

	public void setNonProdDurationTimeStamp(String nonProdDurationTimeStamp) {
		this.nonProdDurationTimeStamp = nonProdDurationTimeStamp;
	}
	
}
