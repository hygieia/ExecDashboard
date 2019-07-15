package com.capitalone.dashboard.exec.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author raish4s
 *
 */
@Document(collection = "elb")
public class ELB extends BaseModel {
	private String loadbalancerName;
	private String vpcID;
	private String region;
	private long vastID;
	@Indexed(name = "vastAcronym")
	private String vastAcronym;
	private String owner;
	private long accountNumber;
	private String accountName;
	private String portfolio;
	private String dnsName;
	private int used;
	@Indexed(name = "environment")
	private String environment;
	private String updatedDate;

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public long getVastID() {
		return vastID;
	}

	public void setVastID(long vastID) {
		this.vastID = vastID;
	}

	public String getVastAcronym() {
		return vastAcronym;
	}

	public void setVastAcronym(String vastAcronym) {
		this.vastAcronym = vastAcronym;
	}

	public long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(String portfolio) {
		this.portfolio = portfolio;
	}

	public String getVpcID() {
		return vpcID;
	}

	public void setVpcID(String vpcID) {
		this.vpcID = vpcID;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getLoadbalancerName() {
		return loadbalancerName;
	}

	public void setLoadbalancerName(String loadbalancerName) {
		this.loadbalancerName = loadbalancerName;
	}

	public String getDnsName() {
		return dnsName;
	}

	public void setDnsName(String dnsName) {
		this.dnsName = dnsName;
	}

	public int getUsed() {
		return used;
	}

	public void setUsed(int used) {
		this.used = used;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}
}
