package com.capitalone.dashboard.exec.model.vz;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author V143611
 *
 */
@Document(collection = "service_now_issues")
@CompoundIndex(def = "{'appId':1, 'aysCreatedTimeStamp':-1}", name = "appId_aysCreatedTimeStamp")
public class ServiceNowIssues extends BaseModel {

	@Indexed(unique = true)
	private String aysNumber;
	private String aysShortDescription;
	@Indexed(name = "appId")
	private String appId;
	private String appName;
	private String appDisplayName;
	private String aysPriorityDescription;
	private Long aysPriority;
	private String aysCreatedTime;
	private String aysUpdatedTime;
	private Long aysCreatedTimeStamp;
	private Long aysUpdatedTimeStamp;
	private String aysResolutionDescription;
	private Boolean active;
	private String stateDescription;
	private Long state;

	public String getAysNumber() {
		return aysNumber;
	}

	public void setAysNumber(String aysNumber) {
		this.aysNumber = aysNumber;
	}

	public String getAysShortDescription() {
		return aysShortDescription;
	}

	public void setAysShortDescription(String aysShortDescription) {
		this.aysShortDescription = aysShortDescription;
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

	public String getAppDisplayName() {
		return appDisplayName;
	}

	public void setAppDisplayName(String appDisplayName) {
		this.appDisplayName = appDisplayName;
	}

	public String getAysPriorityDescription() {
		return aysPriorityDescription;
	}

	public void setAysPriorityDescription(String aysPriorityDescription) {
		this.aysPriorityDescription = aysPriorityDescription;
	}

	public Long getAysPriority() {
		return aysPriority;
	}

	public void setAysPriority(Long aysPriority) {
		this.aysPriority = aysPriority;
	}

	public String getAysCreatedTime() {
		return aysCreatedTime;
	}

	public void setAysCreatedTime(String aysCreatedTime) {
		this.aysCreatedTime = aysCreatedTime;
	}

	public String getAysUpdatedTime() {
		return aysUpdatedTime;
	}

	public void setAysUpdatedTime(String aysUpdatedTime) {
		this.aysUpdatedTime = aysUpdatedTime;
	}

	public Long getAysCreatedTimeStamp() {
		return aysCreatedTimeStamp;
	}

	public void setAysCreatedTimeStamp(Long aysCreatedTimeStamp) {
		this.aysCreatedTimeStamp = aysCreatedTimeStamp;
	}

	public Long getAysUpdatedTimeStamp() {
		return aysUpdatedTimeStamp;
	}

	public void setAysUpdatedTimeStamp(Long aysUpdatedTimeStamp) {
		this.aysUpdatedTimeStamp = aysUpdatedTimeStamp;
	}

	public String getAysResolutionDescription() {
		return aysResolutionDescription;
	}

	public void setAysResolutionDescription(String aysResolutionDescription) {
		this.aysResolutionDescription = aysResolutionDescription;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getStateDescription() {
		return stateDescription;
	}

	public void setStateDescription(String stateDescription) {
		this.stateDescription = stateDescription;
	}

	public Long getState() {
		return state;
	}

	public void setState(Long state) {
		this.state = state;
	}

}
