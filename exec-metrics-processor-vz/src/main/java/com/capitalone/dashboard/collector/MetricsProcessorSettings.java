package com.capitalone.dashboard.collector;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Bean to hold settings specific to the git collector.
 */
@Component
@ConfigurationProperties(prefix = "metricsProcessor")
public class MetricsProcessorSettings {
	private String cron;
	private String password;
	private String userName;
	private String dbName;
	private String dbHost;
	private Integer dbPort;
	private List<Integer> metrics;
	private Boolean processVast;
	private String collectDateWise;
	private List<String> excludedAppIds;
	private Long dateRange;
	private Boolean copyVast;
	private String jiraBaseUrl;
	private String storyLink;
	private String wipLink;
	private String qualityLink;
	private String incidentLink;
	private String velocityLink;
	private String sprintViewLink;

	public String getCollectDateWise() {
		return collectDateWise;
	}

	public void setCollectDateWise(String collectDateWise) {
		this.collectDateWise = collectDateWise;
	}

	public List<String> getExcludedAppIds() {
		return excludedAppIds;
	}

	public void setExcludedAppIds(List<String> excludedAppIds) {
		this.excludedAppIds = excludedAppIds;
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDbHost() {
		return dbHost;
	}

	public void setDbHost(String dbHost) {
		this.dbHost = dbHost;
	}

	public Integer getDbPort() {
		return dbPort;
	}

	public void setDbPort(Integer dbPort) {
		this.dbPort = dbPort;
	}

	public List<Integer> getMetrics() {
		return metrics;
	}

	public void setMetrics(List<Integer> metrics) {
		this.metrics = metrics;
	}

	public Boolean getProcessVast() {
		return processVast;
	}

	public void setProcessVast(Boolean processVast) {
		this.processVast = processVast;
	}

	public Long getDateRange() {
		return dateRange;
	}

	public void setDateRange(Long dateRange) {
		this.dateRange = dateRange;
	}

	public Boolean getCopyVast() {
		return copyVast;
	}

	public void setCopyVast(Boolean copyVast) {
		this.copyVast = copyVast;
	}

	public String getJiraBaseUrl() {
		return jiraBaseUrl;
	}

	public void setJiraBaseUrl(String jiraBaseUrl) {
		this.jiraBaseUrl = jiraBaseUrl;
	}

	public String getStoryLink() {
		return storyLink;
	}

	public void setStoryLink(String storyLink) {
		this.storyLink = storyLink;
	}

	public String getWipLink() {
		return wipLink;
	}

	public void setWipLink(String wipLink) {
		this.wipLink = wipLink;
	}

	public String getQualityLink() {
		return qualityLink;
	}

	public void setQualityLink(String qualityLink) {
		this.qualityLink = qualityLink;
	}

	public String getIncidentLink() {
		return incidentLink;
	}

	public void setIncidentLink(String incidentLink) {
		this.incidentLink = incidentLink;
	}

	public String getVelocityLink() {
		return velocityLink;
	}

	public void setVelocityLink(String velocityLink) {
		this.velocityLink = velocityLink;
	}

	/**
	 * @return the sprintViewLink
	 */
	public String getSprintViewLink() {
		return sprintViewLink;
	}

	/**
	 * @param sprintViewLink the sprintViewLink to set
	 */
	public void setSprintViewLink(String sprintViewLink) {
		this.sprintViewLink = sprintViewLink;
	}

}
