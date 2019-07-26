package com.capitalone.dashboard.exec.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
 * DevOpsCupScores collection to get Scores from OneHygieia
 *
 */
@Document(collection = "devopscup_scores")
public class DevOpsCupScores extends BaseModel {

	@Indexed(name = "appId")
	private String appId;
	private String appName;
	private String portfolio;
	private Boolean active;
	private Long timeStamp;

	private Integer quarter; // Q2, 3, 4
	private Long enggExcelPoints;
	private Long enggExcelPercent;
	private Long enggExcelTrend;
	private Long cloudExcelPoints;
	private Long cloudExcelPercent;
	private Long cloudExcelTrend;
	private Long totalPoints;
	private Double totalPercent;

	private EngineeringExcellence enggExcel;
	private CloudExcellence cloudExcel;
	private Boolean enggExcelActive;
	private Boolean cloudExcelActive;
	private Integer rank;

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

	public String getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(String portfolio) {
		this.portfolio = portfolio;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Integer getQuarter() {
		return quarter;
	}

	public void setQuarter(Integer quarter) {
		this.quarter = quarter;
	}

	public Long getEnggExcelPoints() {
		return enggExcelPoints;
	}

	public void setEnggExcelPoints(Long enggExcelPoints) {
		this.enggExcelPoints = enggExcelPoints;
	}

	public Long getEnggExcelPercent() {
		return enggExcelPercent;
	}

	public void setEnggExcelPercent(Long enggExcelPercent) {
		this.enggExcelPercent = enggExcelPercent;
	}

	public Long getEnggExcelTrend() {
		return enggExcelTrend;
	}

	public void setEnggExcelTrend(Long enggExcelTrend) {
		this.enggExcelTrend = enggExcelTrend;
	}

	public Long getCloudExcelPoints() {
		return cloudExcelPoints;
	}

	public void setCloudExcelPoints(Long cloudExcelPoints) {
		this.cloudExcelPoints = cloudExcelPoints;
	}

	public Long getCloudExcelPercent() {
		return cloudExcelPercent;
	}

	public void setCloudExcelPercent(Long cloudExcelPercent) {
		this.cloudExcelPercent = cloudExcelPercent;
	}

	public Long getCloudExcelTrend() {
		return cloudExcelTrend;
	}

	public void setCloudExcelTrend(Long cloudExcelTrend) {
		this.cloudExcelTrend = cloudExcelTrend;
	}

	public Long getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(Long totalPoints) {
		this.totalPoints = totalPoints;
	}

	public Double getTotalPercent() {
		return totalPercent;
	}

	public void setTotalPercent(Double totalPercent) {
		this.totalPercent = totalPercent;
	}

	public EngineeringExcellence getEnggExcel() {
		return enggExcel;
	}

	public void setEnggExcel(EngineeringExcellence enggExcel) {
		this.enggExcel = enggExcel;
	}

	public CloudExcellence getCloudExcel() {
		return cloudExcel;
	}

	public void setCloudExcel(CloudExcellence cloudExcel) {
		this.cloudExcel = cloudExcel;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Boolean getEnggExcelActive() {
		return enggExcelActive;
	}

	public void setEnggExcelActive(Boolean enggExcelActive) {
		this.enggExcelActive = enggExcelActive;
	}

	public Boolean getCloudExcelActive() {
		return cloudExcelActive;
	}

	public void setCloudExcelActive(Boolean cloudExcelActive) {
		this.cloudExcelActive = cloudExcelActive;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

}
