package com.capitalone.dashboard.exec.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 *
 */
@Document(collection = "ebs_unused")
public class CloudCustodianEbsUnused extends BaseModel {
	private String accountName;
	private String accountNumber;
	private String region;
	private String volumeId;
	private String size;
	private String availabilityZone;
	private String state;
	@Indexed(name = "vastAcronym")
	private String vastAcronym;
	private String owner;
	private String portfolio;
	private String vastId;
	@Indexed(name = "environment")
	private String environment;

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getVolumeId() {
		return volumeId;
	}

	public void setVolumeId(String volumeId) {
		this.volumeId = volumeId;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getAvailabilityZone() {
		return availabilityZone;
	}

	public void setAvailabilityZone(String availabilityZone) {
		this.availabilityZone = availabilityZone;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getVastAcronym() {
		return vastAcronym;
	}

	public void setVastAcronym(String vastAcronym) {
		this.vastAcronym = vastAcronym;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(String portfolio) {
		this.portfolio = portfolio;
	}

	public String getVastId() {
		return vastId;
	}

	public void setVastId(String vastId) {
		this.vastId = vastId;
	}

}
