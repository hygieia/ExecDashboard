package com.capitalone.dashboard.exec.model;

import org.springframework.stereotype.Component;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created for ServiceNow Details
 */
@Component
@Document(collection="service_now_new")
public class ServiceNowTicket extends BaseModel {

	private String applicationName;	
    private String startdate;
    private String enddate;
	private String description;
    private String shortDescription;
    private String justification;
    private String category;
    private String state;
    private String number;
    private String isdevops;
    private String reason;
    private String active;
    private long lastUpdated;
    private List<String> servers;
    private List<String> appList;


    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getAppName() {
		return applicationName;
	}

	public void setAppName(String applicationName) {
		this.applicationName = applicationName;
	}
    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getJustification() {
		return justification;
	}

	public void setJustification(String justification) {
		this.justification = justification;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<String> getServers() {
		return servers;
	}

	public void setServers(List<String> servers) {
		this.servers = servers;
	}

	public List<String> getAppList() {
		return appList;
	}

	public void setAppList(List<String> appList) {
		this.appList = appList;
	}

	public String getIsdevops() {
		return isdevops;
	}

	public void setIsdevops(String isdevops) {
		this.isdevops = isdevops;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}
	
    
}
