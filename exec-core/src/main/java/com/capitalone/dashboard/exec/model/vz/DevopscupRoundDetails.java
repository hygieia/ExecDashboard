package com.capitalone.dashboard.exec.model.vz;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Model DevopscupRoundDetails
 * 
 * @param
 * @return
 */
@Document(collection = "devopscup_round_details")
public class DevopscupRoundDetails extends BaseModel {

	private Integer round;
	private Integer quarter;
	private Long startDate;
	private Long endDate;
	private Long baseLineStartDate;
	private Long baseLineEndDate;
	private Long metricsAsOfDate;
	private Boolean active;

	public Integer getRound() {
		return round;
	}

	public void setRound(Integer round) {
		this.round = round;
	}

	public Integer getQuarter() {
		return quarter;
	}

	public void setQuarter(Integer quarter) {
		this.quarter = quarter;
	}

	public Long getStartDate() {
		return startDate;
	}

	public void setStartDate(Long startDate) {
		this.startDate = startDate;
	}

	public Long getEndDate() {
		return endDate;
	}

	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}

	public Long getBaseLineStartDate() {
		return baseLineStartDate;
	}

	public void setBaseLineStartDate(Long baseLineStartDate) {
		this.baseLineStartDate = baseLineStartDate;
	}

	public Long getBaseLineEndDate() {
		return baseLineEndDate;
	}

	public void setBaseLineEndDate(Long baseLineEndDate) {
		this.baseLineEndDate = baseLineEndDate;
	}

	public Long getMetricsAsOfDate() {
		return metricsAsOfDate;
	}

	public void setMetricsAsOfDate(Long metricsAsOfDate) {
		this.metricsAsOfDate = metricsAsOfDate;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
}
