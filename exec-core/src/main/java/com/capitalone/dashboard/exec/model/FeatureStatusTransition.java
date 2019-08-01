package com.capitalone.dashboard.exec.model;

public class FeatureStatusTransition {
	
	private String changedPerson;
	private String changedPersonDisplayName;
	private String changedDate;
	private String fromStatus;
	private String toStatus;
	private String fromStatusCategory;
	private String toStatusCategory;
	 
	public String getChangedPerson() {
		return changedPerson;
	}
	public void setChangedPerson(String changedPerson) {
		this.changedPerson = changedPerson;
	}
	public String getChangedDate() {
		return changedDate;
	}
	public void setChangedDate(String changedDate) {
		this.changedDate = changedDate;
	}
	public String getFromStatus() {
		return fromStatus;
	}
	public void setFromStatus(String fromStatus) {
		this.fromStatus = fromStatus;
	}
	public String getToStatus() {
		return toStatus;
	}
	public void setToStatus(String toStatus) {
		this.toStatus = toStatus;
	}
	public String getChangedPersonDisplayName() {
		return changedPersonDisplayName;
	}
	public void setChangedPersonDisplayName(String changedPersonDisplayName) {
		this.changedPersonDisplayName = changedPersonDisplayName;
	}
	public String getFromStatusCategory() {
		return fromStatusCategory;
	}
	public void setFromStatusCategory(String fromStatusCategory) {
		this.fromStatusCategory = fromStatusCategory;
	}
	public String getToStatusCategory() {
		return toStatusCategory;
	}
	public void setToStatusCategory(String toStatusCategory) {
		this.toStatusCategory = toStatusCategory;
	}

}
