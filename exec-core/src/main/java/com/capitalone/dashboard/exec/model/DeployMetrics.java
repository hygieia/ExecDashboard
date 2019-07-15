package com.capitalone.dashboard.exec.model;


import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents deployable units (components) deployed to an environment.
 */
@CompoundIndex(name = "uniquedeploys",unique=true, def = "{'endTime' : 1, 'appId' : 1,'environmentName':1,'buildUrl':1}")
@Document(collection = "deployments")
public class DeployMetrics extends BaseModel {
	 		    
    private String environmentName;
    private String environmentUrl;    
	private String componentName;// dashboard title
    private String appId;
    private long duration;
    private String buildUrl;
    private long startTime;
    private long endTime;	    
    private BuildStatus buildStatus;
    private String startedBy;
	    
    public String getEnvironmentName() {
        return environmentName;
    }

    public void setEnvironmentName(String environmentName) {
        this.environmentName = environmentName;
    }

	
    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public String getBuildUrl() {
		return buildUrl;
	}

	public void setBuildUrl(String buildUrl) {
		this.buildUrl = buildUrl;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public BuildStatus getBuildStatus() {
		return buildStatus;
	}

	public void setBuildStatus(BuildStatus buildStatus) {
		this.buildStatus = buildStatus;
	}

	public String getStartedBy() {
		return startedBy;
	}

	public void setStartedBy(String startedBy) {
		this.startedBy = startedBy;
	}

	public String getEnvironmentUrl() {
		return environmentUrl;
	}

	public void setEnvironmentUrl(String environmentUrl) {
		this.environmentUrl = environmentUrl;
	}
}