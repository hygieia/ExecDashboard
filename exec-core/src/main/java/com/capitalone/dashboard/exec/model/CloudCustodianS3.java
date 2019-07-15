package com.capitalone.dashboard.exec.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author raish4s
 *
 */
@Document(collection = "s3")
public class CloudCustodianS3 extends BaseModel {
	private long s3VastID;
	@Indexed(name = "s3VastAcronym")
	private String s3VastAcronym;
	private String s3Region;
	private String s3BucketName;
	private String s3Encrypted;
	private long s3EmployeeID;
	private String s3Owner;
	private String s3Portfolio;
	private long s3AccountNumber;
	private String s3CreatedDate;
	private String vzManagedKey;
	private boolean s3Active;
	@Indexed(name = "environment")
	private String environment;
	private String updatedDate;
	private String accountName;
	private String accountNumber;

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

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public long getS3VastID() {
		return s3VastID;
	}

	public void setS3VastID(long s3VastID) {
		this.s3VastID = s3VastID;
	}

	public String getS3VastAcronym() {
		return s3VastAcronym;
	}

	public void setS3VastAcronym(String s3VastAcronym) {
		this.s3VastAcronym = s3VastAcronym;
	}

	public String getS3Region() {
		return s3Region;
	}

	public void setS3Region(String s3Region) {
		this.s3Region = s3Region;
	}

	public String getS3BucketName() {
		return s3BucketName;
	}

	public void setS3BucketName(String s3BucketName) {
		this.s3BucketName = s3BucketName;
	}

	public String getS3Encrypted() {
		return s3Encrypted;
	}

	public void setS3Encrypted(String s3Encrypted) {
		this.s3Encrypted = s3Encrypted;
	}

	public long getS3EmployeeID() {
		return s3EmployeeID;
	}

	public void setS3EmployeeID(long s3EmployeeID) {
		this.s3EmployeeID = s3EmployeeID;
	}

	public String getS3Owner() {
		return s3Owner;
	}

	public void setS3Owner(String s3Owner) {
		this.s3Owner = s3Owner;
	}

	public String getS3Portfolio() {
		return s3Portfolio;
	}

	public void setS3Portfolio(String s3Portfolio) {
		this.s3Portfolio = s3Portfolio;
	}

	public long getS3AccountNumber() {
		return s3AccountNumber;
	}

	public void setS3AccountNumber(long s3AccountNumber) {
		this.s3AccountNumber = s3AccountNumber;
	}

	public String getS3CreatedDate() {
		return s3CreatedDate;
	}

	public void setS3CreatedDate(String s3CreatedDate) {
		this.s3CreatedDate = s3CreatedDate;
	}

	public boolean isS3Active() {
		return s3Active;
	}

	public void setS3Active(boolean s3Active) {
		this.s3Active = s3Active;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getVzManagedKey() {
		return vzManagedKey;
	}

	public void setVzManagedKey(String vzManagedKey) {
		this.vzManagedKey = vzManagedKey;
	}

}
