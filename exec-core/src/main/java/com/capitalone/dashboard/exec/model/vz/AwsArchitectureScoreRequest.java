package com.capitalone.dashboard.exec.model.vz;

/**
 * 
 * AwsArchitectureScoreRequest for Cloud Excellence scores
 *
 */
public class AwsArchitectureScoreRequest {

	private Integer spotInstance;
	private Integer waf;
	private Integer openSourceSw;
	private Integer retdSw;
	private Integer templateReused;
	private Integer supportScore;
	private Integer totalScore;

	public Integer getSpotInstance() {
		return spotInstance;
	}

	public void setSpotInstance(Integer spotInstance) {
		this.spotInstance = spotInstance;
	}

	public Integer getOpenSourceSw() {
		return openSourceSw;
	}

	public void setOpenSourceSw(Integer openSourceSw) {
		this.openSourceSw = openSourceSw;
	}

	public Integer getRetdSw() {
		return retdSw;
	}

	public void setRetdSw(Integer retdSw) {
		this.retdSw = retdSw;
	}

	public Integer getTemplateReused() {
		return templateReused;
	}

	public void setTemplateReused(Integer templateReused) {
		this.templateReused = templateReused;
	}

	public Integer getSupportScore() {
		return supportScore;
	}

	public void setSupportScore(Integer supportScore) {
		this.supportScore = supportScore;
	}

	public Integer getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
	}

	public Integer getWaf() {
		return waf;
	}

	public void setWaf(Integer waf) {
		this.waf = waf;
	}

}
