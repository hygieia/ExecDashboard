package com.capitalone.dashboard.exec.model.vz;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "security_formulated_data")
@CompoundIndex(def = "{'appId':1, 'timestamp':1}", name = "appId_timestamp")
public class ExecutiveSecurityData extends BaseModel {

	private long timestamp;
	
	@Indexed(name = "appId")
	private String appId;

	private int webCriticalValue;
	private int webMajorValue;
	private int webBlockerValue;
	
	private int webCriticalOverdue;
	private int webHighOverdue;
	private int webMediumOverdue;

	private int vulnerCriticalValue;
	private int vulnerMajorValue;
	private int vulnerBlockerValue;
	
	private int vulnerCriticalOverdue;
	private int vulnerMediumOverdue;
	private int vulnerHighOverdue;

	private int scanCriticalValue;
	private int scanMajorValue;
	private int scanBlockerValue;

	private int totalCriticalValue;
	private int totalMajorValue;
	private int totalBlockerValue;
	
	private int blackDuckCriticalValue;
	private int blackDuckMajorValue;
	private int blackDuckBlockerValue;
	
	
	private int blackDuckCriticalOverdue;
	private int blackDuckMediumOverdue;
	private int blackDuckHighOverdue;
	
	private int totalCriticalOverdue;
	private int totalMediumOverdue;
	private int totalHighOverdue;
	private String datewise;
	
	
	

	public int getWebCriticalOverdue() {
		return webCriticalOverdue;
	}

	public void setWebCriticalOverdue(int webCriticalOverdue) {
		this.webCriticalOverdue = webCriticalOverdue;
	}

	public int getWebHighOverdue() {
		return webHighOverdue;
	}

	public void setWebHighOverdue(int webHighOverdue) {
		this.webHighOverdue = webHighOverdue;
	}

	public int getWebMediumOverdue() {
		return webMediumOverdue;
	}

	public void setWebMediumOverdue(int webMediumOverdue) {
		this.webMediumOverdue = webMediumOverdue;
	}

	public int getVulnerCriticalOverdue() {
		return vulnerCriticalOverdue;
	}

	public void setVulnerCriticalOverdue(int vulnerCriticalOverdue) {
		this.vulnerCriticalOverdue = vulnerCriticalOverdue;
	}

	public int getVulnerMediumOverdue() {
		return vulnerMediumOverdue;
	}

	public void setVulnerMediumOverdue(int vulnerMediumOverdue) {
		this.vulnerMediumOverdue = vulnerMediumOverdue;
	}

	public int getVulnerHighOverdue() {
		return vulnerHighOverdue;
	}

	public void setVulnerHighOverdue(int vulnerHighOverdue) {
		this.vulnerHighOverdue = vulnerHighOverdue;
	}

	public int getTotalCriticalOverdue() {
		return totalCriticalOverdue;
	}

	public void setTotalCriticalOverdue(int totalCriticalOverdue) {
		this.totalCriticalOverdue = totalCriticalOverdue;
	}

	public int getTotalMediumOverdue() {
		return totalMediumOverdue;
	}

	public void setTotalMediumOverdue(int totalMediumOverdue) {
		this.totalMediumOverdue = totalMediumOverdue;
	}

	public int getTotalHighOverdue() {
		return totalHighOverdue;
	}

	public void setTotalHighOverdue(int totalHighOverdue) {
		this.totalHighOverdue = totalHighOverdue;
	}

	public int getTotalCriticalValue() {
		return totalCriticalValue;
	}

	public void setTotalCriticalValue(int totalCriticalValue) {
		this.totalCriticalValue = totalCriticalValue;
	}

	public int getTotalMajorValue() {
		return totalMajorValue;
	}

	public void setTotalMajorValue(int totalMajorValue) {
		this.totalMajorValue = totalMajorValue;
	}

	public int getTotalBlockerValue() {
		return totalBlockerValue;
	}

	public void setTotalBlockerValue(int totalBlockerValue) {
		this.totalBlockerValue = totalBlockerValue;
	}

	private String uniqueId;

	private String securityUrl;

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public int getWebCriticalValue() {
		return webCriticalValue;
	}

	public void setWebCriticalValue(int webCriticalValue) {
		this.webCriticalValue = webCriticalValue;
	}

	public int getWebMajorValue() {
		return webMajorValue;
	}

	public void setWebMajorValue(int webMajorValue) {
		this.webMajorValue = webMajorValue;
	}

	public int getWebBlockerValue() {
		return webBlockerValue;
	}

	public void setWebBlockerValue(int webBlockerValue) {
		this.webBlockerValue = webBlockerValue;
	}

	public int getVulnerCriticalValue() {
		return vulnerCriticalValue;
	}

	public void setVulnerCriticalValue(int vulnerCriticalValue) {
		this.vulnerCriticalValue = vulnerCriticalValue;
	}

	public int getVulnerMajorValue() {
		return vulnerMajorValue;
	}

	public void setVulnerMajorValue(int vulnerMajorValue) {
		this.vulnerMajorValue = vulnerMajorValue;
	}

	public int getVulnerBlockerValue() {
		return vulnerBlockerValue;
	}

	public void setVulnerBlockerValue(int vulnerBlockerValue) {
		this.vulnerBlockerValue = vulnerBlockerValue;
	}

	public int getScanCriticalValue() {
		return scanCriticalValue;
	}

	public void setScanCriticalValue(int scanCriticalValue) {
		this.scanCriticalValue = scanCriticalValue;
	}

	public int getScanMajorValue() {
		return scanMajorValue;
	}

	public void setScanMajorValue(int scanMajorValue) {
		this.scanMajorValue = scanMajorValue;
	}

	public int getScanBlockerValue() {
		return scanBlockerValue;
	}

	public void setScanBlockerValue(int scanBlockerValue) {
		this.scanBlockerValue = scanBlockerValue;
	}

	public String getSecurityUrl() {
		return securityUrl;
	}

	public void setSecurityUrl(String securityUrl) {
		this.securityUrl = securityUrl;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getDatewise() {
		return datewise;
	}

	public void setDatewise(String datewise) {
		this.datewise = datewise;
	}
	
	public int getBlackDuckCriticalValue() {
		return blackDuckCriticalValue;
	}

	public void setBlackDuckCriticalValue(int blackDuckCriticalValue) {
		this.blackDuckCriticalValue = blackDuckCriticalValue;
	}

	public int getBlackDuckMajorValue() {
		return blackDuckMajorValue;
	}

	public void setBlackDuckMajorValue(int blackDuckMajorValue) {
		this.blackDuckMajorValue = blackDuckMajorValue;
	}

	public int getBlackDuckBlockerValue() {
		return blackDuckBlockerValue;
	}

	public void setBlackDuckBlockerValue(int blackDuckBlockerValue) {
		this.blackDuckBlockerValue = blackDuckBlockerValue;
	}

	public int getBlackDuckCriticalOverdue() {
		return blackDuckCriticalOverdue;
	}

	public void setBlackDuckCriticalOverdue(int blackDuckCriticalOverdue) {
		this.blackDuckCriticalOverdue = blackDuckCriticalOverdue;
	}

	public int getBlackDuckMediumOverdue() {
		return blackDuckMediumOverdue;
	}

	public void setBlackDuckMediumOverdue(int blackDuckMediumOverdue) {
		this.blackDuckMediumOverdue = blackDuckMediumOverdue;
	}

	public int getBlackDuckHighOverdue() {
		return blackDuckHighOverdue;
	}

	public void setBlackDuckHighOverdue(int blackDuckHighOverdue) {
		this.blackDuckHighOverdue = blackDuckHighOverdue;
	}


}
