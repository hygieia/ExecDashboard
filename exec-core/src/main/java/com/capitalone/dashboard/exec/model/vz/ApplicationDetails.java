package com.capitalone.dashboard.exec.model.vz;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "app_details")
public class ApplicationDetails {

	@Indexed(name = "appId")
	private String appId;
	@Indexed(name = "appName")
	private String appName;
	private String appAcronym;
	private String lob;
	private String availabilityStatus;
	private Boolean dashboardAvailable;
	private String poc;
	private int totalTeamBoards;
	private String teamBoardLink;
	private Date lastScanned;
	private Long lastUpdated;
	private List<InstanceCollectorStatus> collectorStatus;
	private String vastId;

	public String getVastId() {
		return vastId;
	}

	public void setVastId(String vastId) {
		this.vastId = vastId;
	}

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

	public String getAppAcronym() {
		return appAcronym;
	}

	public void setAppAcronym(String appAcronym) {
		this.appAcronym = appAcronym;
	}

	public String getLob() {
		return lob;
	}

	public void setLob(String lob) {
		this.lob = lob;
	}

	public String getAvailabilityStatus() {
		return availabilityStatus;
	}

	public void setAvailabilityStatus(String availabilityStatus) {
		this.availabilityStatus = availabilityStatus;
	}

	public Boolean getDashboardAvailable() {
		return dashboardAvailable;
	}

	public void setDashboardAvailable(Boolean dashboardAvailable) {
		this.dashboardAvailable = dashboardAvailable;
	}

	public String getPoc() {
		return poc;
	}

	public void setPoc(String poc) {
		this.poc = poc;
	}

	public int getTotalTeamBoards() {
		return totalTeamBoards;
	}

	public void setTotalTeamBoards(int totalTeamBoards) {
		this.totalTeamBoards = totalTeamBoards;
	}

	public String getTeamBoardLink() {
		return teamBoardLink;
	}

	public void setTeamBoardLink(String teamBoardLink) {
		this.teamBoardLink = teamBoardLink;
	}

	public List<InstanceCollectorStatus> getCollectorStatus() {
		return collectorStatus;
	}

	public void setCollectorStatus(List<InstanceCollectorStatus> collectorStatus) {
		this.collectorStatus = collectorStatus;
	}

	public Date getLastScanned() {
		return lastScanned;
	}

	public void setLastScanned(Date lastScanned) {
		this.lastScanned = lastScanned;
	}

	public Long getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Long lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

}