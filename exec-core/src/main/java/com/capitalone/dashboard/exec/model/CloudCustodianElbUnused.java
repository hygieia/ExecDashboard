package com.capitalone.dashboard.exec.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author raish4s
 *
 */
@Document(collection = "elb_unused")
public class CloudCustodianElbUnused extends BaseModel {
	private String accountName;
	private String accountNumber;
	private String region;
	private String vpcId;
	private String loadBalancerName;
	private String dnsName;
	@Indexed(name = "vastAcronym")
	private String vastAcronym;
	private String userId;
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

	public String getVpcId() {
		return vpcId;
	}

	public void setVpcId(String vpcId) {
		this.vpcId = vpcId;
	}

	public String getLoadBalancerName() {
		return loadBalancerName;
	}

	public void setLoadBalancerName(String loadBalancerName) {
		this.loadBalancerName = loadBalancerName;
	}

	public String getDnsName() {
		return dnsName;
	}

	public void setDnsName(String dnsName) {
		this.dnsName = dnsName;
	}

	public String getVastAcronym() {
		return vastAcronym;
	}

	public void setVastAcronym(String vastAcronym) {
		this.vastAcronym = vastAcronym;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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
