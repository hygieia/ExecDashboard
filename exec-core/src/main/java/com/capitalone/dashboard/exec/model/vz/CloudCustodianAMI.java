package com.capitalone.dashboard.exec.model.vz;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author raish4s
 *
 */
@Document(collection = "ami")
public class CloudCustodianAMI extends BaseModel {

	private String amiInstanceID;
	private String amiInstanceName;
	private String amiPriavteIP;
	private long amiVastID;
	@Indexed(name = "amiVastAcronym")
	private String amiVastAcronym;
	private String amiRegion;
	private String amiBaseAMIID;
	private String amiBaseAMIName;
	private String amiBaseAMICreationDate;
	private String amiLatestAMIID;
	private long amiAccountNumber;
	private String amiPortfolio;
	private long amiEmployeeID;
	private boolean amiActive;
	private String amiOwner;
	@Indexed(name = "environment")
	private String environment;
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

	public String getAmiInstanceID() {
		return amiInstanceID;
	}

	public void setAmiInstanceID(String amiInstanceID) {
		this.amiInstanceID = amiInstanceID;
	}

	public String getAmiInstanceName() {
		return amiInstanceName;
	}

	public void setAmiInstanceName(String amiInstanceName) {
		this.amiInstanceName = amiInstanceName;
	}

	public String getAMIPriavteIP() {
		return amiPriavteIP;
	}

	public void setAMIPriavteIP(String amiPriavteIP) {
		this.amiPriavteIP = amiPriavteIP;
	}

	public long getAMIVastID() {
		return amiVastID;
	}

	public void setAMIVastID(long amiVastID) {
		this.amiVastID = amiVastID;
	}

	public String getAMIRegion() {
		return amiRegion;
	}

	public void setAMIRegion(String amiRegion) {
		this.amiRegion = amiRegion;
	}

	public String getAMIVastAcronym() {
		return amiVastAcronym;
	}

	public void setAMIVastAcronym(String amiVastAcronym) {
		this.amiVastAcronym = amiVastAcronym;
	}

	public String getAMIBaseAMIID() {
		return amiBaseAMIID;
	}

	public void setAMIBaseAMIID(String amiBaseAMIID) {
		this.amiBaseAMIID = amiBaseAMIID;
	}

	public String getAMIBaseAMIName() {
		return amiBaseAMIName;
	}

	public void setAMIBaseAMIName(String amiBaseAMIName) {
		this.amiBaseAMIName = amiBaseAMIName;
	}

	public String getAMIBaseAMICreationDate() {
		return amiBaseAMICreationDate;
	}

	public void setAMIBaseAMICreationDate(String amiBaseAMICreationDate) {
		this.amiBaseAMICreationDate = amiBaseAMICreationDate;
	}

	public String getAMILatestAMIID() {
		return amiLatestAMIID;
	}

	public void setAMILatestAMIID(String amiLatestAMIID) {
		this.amiLatestAMIID = amiLatestAMIID;
	}

	public long getAMIAccountNumber() {
		return amiAccountNumber;
	}

	public void setAMIAccountNumber(long amiAccountNumber) {
		this.amiAccountNumber = amiAccountNumber;
	}

	public String getAMIPortfolio() {
		return amiPortfolio;
	}

	public void setAMIPortfolio(String amiPortfolio) {
		this.amiPortfolio = amiPortfolio;
	}

	public long getAMIEmployeeID() {
		return amiEmployeeID;
	}

	public void setAMIEmployeeID(long amiEmployeeID) {
		this.amiEmployeeID = amiEmployeeID;
	}

	public boolean isAmiActive() {
		return amiActive;
	}

	public void setAmiActive(boolean amiActive) {
		this.amiActive = amiActive;
	}

	public String getAmiOwner() {
		return amiOwner;
	}

	public void setAmiOwner(String amiOwner) {
		this.amiOwner = amiOwner;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

}
