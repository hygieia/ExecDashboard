package com.capitalone.dashboard.exec.model.vz;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "executives")
public class ExecutiveSummaryList extends BaseModel {

	@Indexed(name = "eid")
	private String eid;
	private String firstName;
	private String lastName;
	private String role;
	private List<String> appId;
	private List<String> businessUnits;
	private List<String> configuredAppId;
	private Map<String, String> appDetails;
	private Map<String, Map<String, String>> appDetailsWithBunit;
	private Integer configuredApps;
	private Integer totalApps;
	private Double reportingPercentage;
	private Boolean seniorExecutive;
	private Long lastUpdated;

	private List<String> favourite;

	public List<String> getFavourite() {
		return favourite;
	}

	public void setFavourite(List<String> favourite) {
		this.favourite = favourite;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<String> getAppId() {
		return appId;
	}

	public void setAppId(List<String> appId) {
		this.appId = appId;
	}

	public List<String> getBusinessUnits() {
		return businessUnits;
	}

	public void setBusinessUnits(List<String> businessUnits) {
		this.businessUnits = businessUnits;
	}

	public List<String> getConfiguredAppId() {
		return configuredAppId;
	}

	public void setConfiguredAppId(List<String> configuredAppId) {
		this.configuredAppId = configuredAppId;
	}

	public Integer getConfiguredApps() {
		return configuredApps;
	}

	public void setConfiguredApps(Integer configuredApps) {
		this.configuredApps = configuredApps;
	}

	public Integer getTotalApps() {
		return totalApps;
	}

	public void setTotalApps(Integer totalApps) {
		this.totalApps = totalApps;
	}

	public Double getReportingPercentage() {
		return reportingPercentage;
	}

	public void setReportingPercentage(Double reportingPercentage) {
		this.reportingPercentage = reportingPercentage;
	}

	public Boolean getSeniorExecutive() {
		return seniorExecutive;
	}

	public void setSeniorExecutive(Boolean seniorExecutive) {
		this.seniorExecutive = seniorExecutive;
	}

	public Map<String, String> getAppDetails() {
		return appDetails;
	}

	public void setAppDetails(Map<String, String> appDetails) {
		this.appDetails = appDetails;
	}

	public Map<String, Map<String, String>> getAppDetailsWithBunit() {
		return appDetailsWithBunit;
	}

	public void setAppDetailsWithBunit(Map<String, Map<String, String>> appDetailsWithBunit) {
		this.appDetailsWithBunit = appDetailsWithBunit;
	}

	public Long getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Long lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

}
