package com.capitalone.dashboard.exec.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * The result of a Continuous Integration build execution. Typically produces
 * binary artifacts. Often triggered by one or more SCM commits.
 *
 * Possible collectors: Hudson (in scope) Team City TFS Go Bamboo TravisCI
 *
 */
@Document(collection = "rds_all")

public class CloudCustodianRdsAll extends BaseModel {

	private String accountName;
	private String region;
	private String dbInstanceClass;
	private String dbInstanceIdentifier;
	private String availabilityZone;
	@Indexed(name = "vsad")
	private String vsad;
	private String owner;
	private String portfolio;
	private String vastId;
	private String accountNumber;
	private String awsCloudFormationStackId;
	private String multiAz;
	private String storageEncrypted;
	private String readReplicaDbInstanceIdentifiers;
	@Indexed(name = "environment")
	private String environment;
	private String updatedDate;

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getDbInstanceClass() {
		return dbInstanceClass;
	}

	public void setDbInstanceClass(String dbInstanceClass) {
		this.dbInstanceClass = dbInstanceClass;
	}

	public String getDbInstanceIdentifier() {
		return dbInstanceIdentifier;
	}

	public void setDbInstanceIdentifier(String dbInstanceIdentifier) {
		this.dbInstanceIdentifier = dbInstanceIdentifier;
	}

	public String getAvailabilityZone() {
		return availabilityZone;
	}

	public void setAvailabilityZone(String availabilityZone) {
		this.availabilityZone = availabilityZone;
	}

	public String getVsad() {
		return vsad;
	}

	public void setVsad(String vsad) {
		this.vsad = vsad;
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

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAwsCloudFormationStackId() {
		return awsCloudFormationStackId;
	}

	public void setAwsCloudFormationStackId(String awsCloudFormationStackId) {
		this.awsCloudFormationStackId = awsCloudFormationStackId;
	}

	public String getMultiAz() {
		return multiAz;
	}

	public void setMultiAz(String multiAz) {
		this.multiAz = multiAz;
	}

	public String getStorageEncrypted() {
		return storageEncrypted;
	}

	public void setStorageEncrypted(String storageEncrypted) {
		this.storageEncrypted = storageEncrypted;
	}

	public String getReadReplicaDbInstanceIdentifiers() {
		return readReplicaDbInstanceIdentifiers;
	}

	public void setReadReplicaDbInstanceIdentifiers(String readReplicaDbInstanceIdentifiers) {
		this.readReplicaDbInstanceIdentifiers = readReplicaDbInstanceIdentifiers;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

}
