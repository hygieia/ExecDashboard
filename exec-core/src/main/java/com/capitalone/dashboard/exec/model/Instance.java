package com.capitalone.dashboard.exec.model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Instance class extends BaseModel
 * 
 * @author RHE94MG
 *
 */
@Document(collection = "instances")
public class Instance extends BaseModel {

	private String ami;
	private String instanceName;
	private String appId;
	private String appName;
	private String eid;
	private String subnet;
	private String stackName;
	private String privateIp;
	private String availabilityZone;
	private String instanceId;
	private String protfolio;
	private String requestId;
	private boolean active;
	private long lastDeployedTimeStamp;
	private long instanceCreationTime;
	private String artifactCollectorVersion;
	private String artifactInstanceVersion;
	private String artifactConfigAppVersion;
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAmi() {
		return ami;
	}

	public void setAmi(String ami) {
		this.ami = ami;
	}

	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}

	public String getProtfolio() {
		return protfolio;
	}

	public void setProtfolio(String protfolio) {
		this.protfolio = protfolio;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public long getLastDeployedTimeStamp() {
		return lastDeployedTimeStamp;
	}

	public void setLastDeployedTimeStamp(long lastDeployedTimeStamp) {
		this.lastDeployedTimeStamp = lastDeployedTimeStamp;
	}

	public long getInstanceCreationTime() {
		return instanceCreationTime;
	}

	public void setInstanceCreationTime(long instanceCreationTime) {
		this.instanceCreationTime = instanceCreationTime;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPrivateIp() {
		return privateIp;
	}

	public void setPrivateIp(String privateIp) {
		this.privateIp = privateIp;
	}

	public String getAvailabilityZone() {
		return availabilityZone;
	}

	public void setAvailabilityZone(String availabilityZone) {
		this.availabilityZone = availabilityZone;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getSubnet() {
		return subnet;
	}

	public void setSubnet(String subnet) {
		this.subnet = subnet;
	}

	public String getStackName() {
		return stackName;
	}

	public void setStackName(String stackName) {
		this.stackName = stackName;
	}

	public String getArtifactInstanceVersion() {
		return artifactInstanceVersion;
	}

	public void setArtifactInstanceVersion(String artifactInstanceVersion) {
		this.artifactInstanceVersion = artifactInstanceVersion;
	}

	public String getArtifactConfigAppVersion() {
		return artifactConfigAppVersion;
	}

	public void setArtifactConfigAppVersion(String artifactConfigAppVersion) {
		this.artifactConfigAppVersion = artifactConfigAppVersion;
	}

	public String getArtifactCollectorVersion() {
		return artifactCollectorVersion;
	}

	public void setArtifactCollectorVersion(String artifactCollectorVersion) {
		this.artifactCollectorVersion = artifactCollectorVersion;
	}

}
