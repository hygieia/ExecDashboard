package com.capitalone.dashboard.exec.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 *
 */
@Document(collection = "ebs")
public class CloudCustodianEBS extends BaseModel {

	private long ebsVastID;
	@Indexed(name = "ebsVastAcronym")
	private String ebsVastAcronym;
	private String ebsRegion;

	private String ebsVolumeName;
	private String ebsVolumeID;
	private String ebsEncrypted;
	private String ebsAttachedInstanceId;
	private String ebsAttachedInstanceName;

	private long ebsEmployeeID;
	private String ebsOwner;
	private String ebsPortfolio;
	private long ebsAccountNumber;
	private String ebsCreatedDate;
	private long ebsSize;
	@Indexed(name = "environment")
	private String environment;
	private String rootvolume;
	private String vzManagedKey;

	private boolean ebsActive;
	private String updatedDate;
	private String accountName;

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public long getEbsVastID() {
		return ebsVastID;
	}

	public void setEbsVastID(long ebsVastID) {
		this.ebsVastID = ebsVastID;
	}

	public String getEbsVastAcronym() {
		return ebsVastAcronym;
	}

	public void setEbsVastAcronym(String ebsVastAcronym) {
		this.ebsVastAcronym = ebsVastAcronym;
	}

	public String getEbsRegion() {
		return ebsRegion;
	}

	public void setEbsRegion(String ebsRegion) {
		this.ebsRegion = ebsRegion;
	}

	public String getEbsVolumeName() {
		return ebsVolumeName;
	}

	public void setEbsVolumeName(String ebsVolumeName) {
		this.ebsVolumeName = ebsVolumeName;
	}

	public String getEbsVolumeID() {
		return ebsVolumeID;
	}

	public void setEbsVolumeID(String ebsVolumeID) {
		this.ebsVolumeID = ebsVolumeID;
	}

	public String getEbsEncrypted() {
		return ebsEncrypted;
	}

	public void setEbsEncrypted(String ebsEncrypted) {
		this.ebsEncrypted = ebsEncrypted;
	}

	public String getEbsAttachedInstanceId() {
		return ebsAttachedInstanceId;
	}

	public void setEbsAttachedInstanceId(String ebsAttachedInstanceId) {
		this.ebsAttachedInstanceId = ebsAttachedInstanceId;
	}

	public String getEbsAttachedInstanceName() {
		return ebsAttachedInstanceName;
	}

	public void setEbsAttachedInstanceName(String ebsAttachedInstanceName) {
		this.ebsAttachedInstanceName = ebsAttachedInstanceName;
	}

	public long getEbsEmployeeID() {
		return ebsEmployeeID;
	}

	public void setEbsEmployeeID(long ebsEmployeeID) {
		this.ebsEmployeeID = ebsEmployeeID;
	}

	public String getEbsOwner() {
		return ebsOwner;
	}

	public void setEbsOwner(String ebsOwner) {
		this.ebsOwner = ebsOwner;
	}

	public String getEbsPortfolio() {
		return ebsPortfolio;
	}

	public void setEbsPortfolio(String ebsPortfolio) {
		this.ebsPortfolio = ebsPortfolio;
	}

	public long getEbsAccountNumber() {
		return ebsAccountNumber;
	}

	public void setEbsAccountNumber(long ebsAccountNumber) {
		this.ebsAccountNumber = ebsAccountNumber;
	}

	public String getEbsCreatedDate() {
		return ebsCreatedDate;
	}

	public void setEbsCreatedDate(String ebsCreatedDate) {
		this.ebsCreatedDate = ebsCreatedDate;
	}

	public boolean isEbsActive() {
		return ebsActive;
	}

	public void setEbsActive(boolean ebsActive) {
		this.ebsActive = ebsActive;
	}

	public long getEbsSize() {
		return ebsSize;
	}

	public void setEbsSize(long ebsSize) {
		this.ebsSize = ebsSize;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getRootvolume() {
		return rootvolume;
	}

	public void setRootvolume(String rootvolume) {
		this.rootvolume = rootvolume;
	}

	public String getVzManagedKey() {
		return vzManagedKey;
	}

	public void setVzManagedKey(String vzManagedKey) {
		this.vzManagedKey = vzManagedKey;
	}

}
