package com.capitalone.dashboard.exec.model.vz;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author V143611
 *
 */
@Document(collection = "mttr")
public class MTTR extends BaseModel {

	private String appId;
	private String eventStartDT;
	private String crisisId;
	private String serviceLevel;
	private String owningEntity;
	private String crisisLevel;
	private int itduration;
	private long timeStamp;
	private String causeCode;
	private String ccExecName;
	private String changeRelated;
	private String changeNumber;
	private String mipNumber;
	private List<String> appIdList;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getEventStartDT() {
		return eventStartDT;
	}

	public void setEventStartDT(String eventStartDT) {
		this.eventStartDT = eventStartDT;
	}

	public String getCrisisId() {
		return crisisId;
	}

	public void setCrisisId(String crisisId) {
		this.crisisId = crisisId;
	}

	public String getServiceLevel() {
		return serviceLevel;
	}

	public void setServiceLevel(String serviceLevel) {
		this.serviceLevel = serviceLevel;
	}

	public String getOwningEntity() {
		return owningEntity;
	}

	public void setOwningEntity(String owningEntity) {
		this.owningEntity = owningEntity;
	}

	public String getCrisisLevel() {
		return crisisLevel;
	}

	public void setCrisisLevel(String crisisLevel) {
		this.crisisLevel = crisisLevel;
	}

	public int getItduration() {
		return itduration;
	}

	public void setItduration(int itduration) {
		this.itduration = itduration;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getCauseCode() {
		return causeCode;
	}

	public void setCauseCode(String causeCode) {
		this.causeCode = causeCode;
	}

	public String getCcExecName() {
		return ccExecName;
	}

	public void setCcExecName(String ccExecName) {
		this.ccExecName = ccExecName;
	}

	public String getChangeRelated() {
		return changeRelated;
	}

	public void setChangeRelated(String changeRelated) {
		this.changeRelated = changeRelated;
	}

	public String getChangeNumber() {
		return changeNumber;
	}

	public void setChangeNumber(String changeNumber) {
		this.changeNumber = changeNumber;
	}

	public String getMipNumber() {
		return mipNumber;
	}

	public void setMipNumber(String mipNumber) {
		this.mipNumber = mipNumber;
	}

	public List<String> getAppIdList() {
		return appIdList;
	}

	public void setAppIdList(List<String> appIdList) {
		this.appIdList = appIdList;
	}

}