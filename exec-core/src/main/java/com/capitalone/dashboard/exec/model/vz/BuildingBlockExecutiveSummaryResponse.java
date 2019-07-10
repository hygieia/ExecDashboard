package com.capitalone.dashboard.exec.model.vz;

import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "building_block_executive")
public class BuildingBlockExecutiveSummaryResponse extends BaseModel {

	// executiveName
	private String name;
	// only for executives collections
	private String lastName;
	private String firstName;
	@Indexed(name = "appId")
	private String appId; // eid
	// eid's objectId from portfolio response
	private String lob;
	// vast custodian
	private String poc;
	// default board url
	private String url;
	// expectedMetrics
	private int totalExpectedMetrics;
	// no of modules
	private int totalComponents;
	private int completeness;
	public long timestamp;
	private List<MetricSummaryResponse> metrics;

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLob() {
		return lob;
	}

	public void setLob(String lob) {
		this.lob = lob;
	}

	public String getPoc() {
		return poc;
	}

	public void setPoc(String poc) {
		this.poc = poc;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getTotalExpectedMetrics() {
		return totalExpectedMetrics;
	}

	public void setTotalExpectedMetrics(int totalExpectedMetrics) {
		this.totalExpectedMetrics = totalExpectedMetrics;
	}

	public int getTotalComponents() {
		return totalComponents;
	}

	public void setTotalComponents(int totalComponents) {
		this.totalComponents = totalComponents;
	}

	public List<MetricSummaryResponse> getMetrics() {
		return metrics;
	}

	public void setMetrics(List<MetricSummaryResponse> metrics) {
		this.metrics = metrics;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public int getCompleteness() {
		return completeness;
	}

	public void setCompleteness(int completeness) {
		this.completeness = completeness;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
}
