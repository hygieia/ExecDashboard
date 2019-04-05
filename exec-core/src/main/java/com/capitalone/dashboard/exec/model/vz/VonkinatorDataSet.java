package com.capitalone.dashboard.exec.model.vz;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author V143611
 *
 */
@Document(collection = "vonkinator_data_set")
@CompoundIndex(def = "{'appId':1, 'period':1}", name = "appId_period")
public class VonkinatorDataSet extends BaseModel {

	@Indexed(name = "appId")
	private String appId;
	private String appName;
	private String portfolio;
	private String appStatus;
	private String period;
	private Double meanTimeToResolveOutages;
	private Double meanTimeToResolveEvents;
	private Long outages;
	private Double changeFailureRate;
	private Long changeActivities;
	private Double velocity;
	private Double deploymentCadence;
	private Long fortifyVulnerabilities;
	private Long portScanVulnerabilities;
	private Long webScanVulnerabilities;
	private Long stories;
	private Long storyPoints;
	private Long timeTaken;
	private Long jiraBugs;
	private Double cycleTime;
	private Long cmisTickets;
	private Long snTickets;
	private Long events;
	private String twoEid;
	private String twoExecutive;
	private String threeEid;
	private String threeExecutive;
	private String fourEid;
	private String fourExecutive;
	private String fiveEid;
	private String fiveExecutive;
	private int order;
	private boolean isDojo;
	private boolean isIT;
	private int developers;
	private int hours;
	private int weeks;
	private String dojoStartDate;
	private String dojoStartQtr;

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

	public String getAppStatus() {
		return appStatus;
	}

	public void setAppStatus(String appStatus) {
		this.appStatus = appStatus;
	}

	public Long getOutages() {
		return outages;
	}

	public void setOutages(Long outages) {
		this.outages = outages;
	}

	public Double getChangeFailureRate() {
		return changeFailureRate;
	}

	public void setChangeFailureRate(Double changeFailureRate) {
		this.changeFailureRate = changeFailureRate;
	}

	public Long getChangeActivities() {
		return changeActivities;
	}

	public void setChangeActivities(Long changeActivities) {
		this.changeActivities = changeActivities;
	}

	public Double getDeploymentCadence() {
		return deploymentCadence;
	}

	public void setDeploymentCadence(Double deploymentCadence) {
		this.deploymentCadence = deploymentCadence;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public Long getFortifyVulnerabilities() {
		return fortifyVulnerabilities;
	}

	public void setFortifyVulnerabilities(Long fortifyVulnerabilities) {
		this.fortifyVulnerabilities = fortifyVulnerabilities;
	}

	public Long getPortScanVulnerabilities() {
		return portScanVulnerabilities;
	}

	public void setPortScanVulnerabilities(Long portScanVulnerabilities) {
		this.portScanVulnerabilities = portScanVulnerabilities;
	}

	public Long getWebScanVulnerabilities() {
		return webScanVulnerabilities;
	}

	public void setWebScanVulnerabilities(Long webScanVulnerabilities) {
		this.webScanVulnerabilities = webScanVulnerabilities;
	}

	public Double getVelocity() {
		return velocity;
	}

	public void setVelocity(Double velocity) {
		this.velocity = velocity;
	}

	public Long getStories() {
		return stories;
	}

	public void setStories(Long stories) {
		this.stories = stories;
	}

	public Long getStoryPoints() {
		return storyPoints;
	}

	public void setStoryPoints(Long storyPoints) {
		this.storyPoints = storyPoints;
	}

	public Long getTimeTaken() {
		return timeTaken;
	}

	public void setTimeTaken(Long timeTaken) {
		this.timeTaken = timeTaken;
	}

	public Long getJiraBugs() {
		return jiraBugs;
	}

	public void setJiraBugs(Long jiraBugs) {
		this.jiraBugs = jiraBugs;
	}

	public Double getCycleTime() {
		return cycleTime;
	}

	public void setCycleTime(Double cycleTime) {
		this.cycleTime = cycleTime;
	}

	public Long getCmisTickets() {
		return cmisTickets;
	}

	public void setCmisTickets(Long cmisTickets) {
		this.cmisTickets = cmisTickets;
	}

	public Long getEvents() {
		return events;
	}

	public void setEvents(Long events) {
		this.events = events;
	}

	public Double getMeanTimeToResolveOutages() {
		return meanTimeToResolveOutages;
	}

	public void setMeanTimeToResolveOutages(Double meanTimeToResolveOutages) {
		this.meanTimeToResolveOutages = meanTimeToResolveOutages;
	}

	public Double getMeanTimeToResolveEvents() {
		return meanTimeToResolveEvents;
	}

	public void setMeanTimeToResolveEvents(Double meanTimeToResolveEvents) {
		this.meanTimeToResolveEvents = meanTimeToResolveEvents;
	}

	public String getTwoEid() {
		return twoEid;
	}

	public void setTwoEid(String twoEid) {
		this.twoEid = twoEid;
	}

	public String getTwoExecutive() {
		return twoExecutive;
	}

	public void setTwoExecutive(String twoExecutive) {
		this.twoExecutive = twoExecutive;
	}

	public String getThreeEid() {
		return threeEid;
	}

	public void setThreeEid(String threeEid) {
		this.threeEid = threeEid;
	}

	public String getThreeExecutive() {
		return threeExecutive;
	}

	public void setThreeExecutive(String threeExecutive) {
		this.threeExecutive = threeExecutive;
	}

	public String getFourEid() {
		return fourEid;
	}

	public void setFourEid(String fourEid) {
		this.fourEid = fourEid;
	}

	public String getFourExecutive() {
		return fourExecutive;
	}

	public void setFourExecutive(String fourExecutive) {
		this.fourExecutive = fourExecutive;
	}

	public String getFiveEid() {
		return fiveEid;
	}

	public void setFiveEid(String fiveEid) {
		this.fiveEid = fiveEid;
	}

	public String getFiveExecutive() {
		return fiveExecutive;
	}

	public void setFiveExecutive(String fiveExecutive) {
		this.fiveExecutive = fiveExecutive;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public Long getSnTickets() {
		return snTickets;
	}

	public void setSnTickets(Long snTickets) {
		this.snTickets = snTickets;
	}

	public int getDevelopers() {
		return developers;
	}

	public void setDevelopers(int developers) {
		this.developers = developers;
	}

	public boolean isDojo() {
		return isDojo;
	}

	public void setDojo(boolean isDojo) {
		this.isDojo = isDojo;
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public int getWeeks() {
		return weeks;
	}

	public void setWeeks(int weeks) {
		this.weeks = weeks;
	}

	public String getDojoStartDate() {
		return dojoStartDate;
	}

	public void setDojoStartDate(String dojoStartDate) {
		this.dojoStartDate = dojoStartDate;
	}

	public String getDojoStartQtr() {
		return dojoStartQtr;
	}

	public void setDojoStartQtr(String dojoStartQtr) {
		this.dojoStartQtr = dojoStartQtr;
	}

	public boolean isIT() {
		return isIT;
	}

	public void setIT(boolean isIT) {
		this.isIT = isIT;
	}
}
