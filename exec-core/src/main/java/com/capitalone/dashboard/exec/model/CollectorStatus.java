package com.capitalone.dashboard.exec.model;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "collector_status")
public class CollectorStatus extends BaseModel {

	private CollectorType type;
	private Date lastUpdated;
	private String collectorName;

	public CollectorType getType() {
		return type;
	}

	public void setType(CollectorType type) {
		this.type = type;
	}

	public String getCollectorName() {
		return collectorName;
	}

	public void setCollectorName(String collectorName) {
		this.collectorName = collectorName;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

}
