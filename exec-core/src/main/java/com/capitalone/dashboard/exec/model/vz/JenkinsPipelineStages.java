package com.capitalone.dashboard.exec.model.vz;

public class JenkinsPipelineStages {

	private Long stageDurationTime;
	private String stageId;
	private String stageName;
	private Long stagePauseDurationTime;
	private Long stageStartTime;
	private String stageStatus;

	public Long getStageDurationTime() {
		return stageDurationTime;
	}

	public void setStageDurationTime(Long stageDurationTime) {
		this.stageDurationTime = stageDurationTime;
	}

	public String getStageId() {
		return stageId;
	}

	public void setStageId(String stageId) {
		this.stageId = stageId;
	}

	public String getStageName() {
		return stageName;
	}

	public void setStageName(String stageName) {
		this.stageName = stageName;
	}

	public Long getStagePauseDurationTime() {
		return stagePauseDurationTime;
	}

	public void setStagePauseDurationTime(Long stagePauseDurationTime) {
		this.stagePauseDurationTime = stagePauseDurationTime;
	}

	public Long getStageStartTime() {
		return stageStartTime;
	}

	public void setStageStartTime(Long stageStartTime) {
		this.stageStartTime = stageStartTime;
	}

	public String getStageStatus() {
		return stageStatus;
	}

	public void setStageStatus(String stageStatus) {
		this.stageStatus = stageStatus;
	}

}
