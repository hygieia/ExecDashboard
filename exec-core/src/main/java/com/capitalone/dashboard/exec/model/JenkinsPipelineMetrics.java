package com.capitalone.dashboard.exec.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "jenkins_pipeline_metrics")
public class JenkinsPipelineMetrics extends BaseModel {

	private String appId;
	private String buildUrl;
	private String jobName;
	private String jobType;
	private String jobUrl;
	private String executionId;
	private Long buildDurationTime;
	private Long buildPauseDurationTime;
	private Long buildQueueDurationTime;
	private Long buildStartTime;
	private Long buildEndTime;
	private String buildStatus;
	private List<JenkinsPipelineStages> stages;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getBuildUrl() {
		return buildUrl;
	}

	public void setBuildUrl(String buildUrl) {
		this.buildUrl = buildUrl;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getJobUrl() {
		return jobUrl;
	}

	public void setJobUrl(String jobUrl) {
		this.jobUrl = jobUrl;
	}

	public Long getBuildDurationTime() {
		return buildDurationTime;
	}

	public void setBuildDurationTime(Long buildDurationTime) {
		this.buildDurationTime = buildDurationTime;
	}

	public Long getBuildPauseDurationTime() {
		return buildPauseDurationTime;
	}

	public void setBuildPauseDurationTime(Long buildPauseDurationTime) {
		this.buildPauseDurationTime = buildPauseDurationTime;
	}

	public Long getBuildQueueDurationTime() {
		return buildQueueDurationTime;
	}

	public void setBuildQueueDurationTime(Long buildQueueDurationTime) {
		this.buildQueueDurationTime = buildQueueDurationTime;
	}

	public Long getBuildStartTime() {
		return buildStartTime;
	}

	public void setBuildStartTime(Long buildStartTime) {
		this.buildStartTime = buildStartTime;
	}

	public Long getBuildEndTime() {
		return buildEndTime;
	}

	public void setBuildEndTime(Long buildEndTime) {
		this.buildEndTime = buildEndTime;
	}

	public String getBuildStatus() {
		return buildStatus;
	}

	public void setBuildStatus(String buildStatus) {
		this.buildStatus = buildStatus;
	}

	public String getExecutionId() {
		return executionId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public List<JenkinsPipelineStages> getStages() {
		return stages;
	}

	public void setStages(List<JenkinsPipelineStages> stages) {
		this.stages = stages;
	}

}
