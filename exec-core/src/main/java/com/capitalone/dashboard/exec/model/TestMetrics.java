package com.capitalone.dashboard.exec.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

/**
 * 
 * @author asthaaa
 *
 */
@Component
@Document(collection = "testmetrics")
public class TestMetrics extends BaseModel {

	private String appId;
	private int success;
	private int failed;
	private int skipped;
	private int total;
	private String jobName;
	private String jobUrl;
	private int buildNo;
	private String testType;
	private String env;
	private long timestamp;
	private String framework;

	public String getFramework() {
		return framework;
	}

	public void setFramework(String framework) {
		this.framework = framework;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public int getFailed() {
		return failed;
	}

	public void setFailed(int failed) {
		this.failed = failed;
	}

	public int getSkipped() {
		return skipped;
	}

	public void setSkipped(int skipped) {
		this.skipped = skipped;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public int getBuildNo() {
		return buildNo;
	}

	public void setBuildNo(int buildNo) {
		this.buildNo = buildNo;
	}

	public String getTestType() {
		return testType;
	}

	public void setTestType(String testType) {
		this.testType = testType;
	}

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getJobUrl() {
		return jobUrl;
	}

	public void setJobUrl(String jobUrl) {
		this.jobUrl = jobUrl;
	}

}