package com.capitalone.dashboard.exec.model;


/**
 * 
 *
 * 
 */
public class CollectorUpdatedStatus {
	private CollectorType type;
	private Long collectorUpdateTime;
	private String collectionName;
	private Long collectionUpdatedTime;
	private Long appCount;
	private Long collectorStartTime;
	private Long duration;
	private Boolean isRunning;
	private Long lastStartTime;

	public CollectorType getType() {
		return type;
	}

	public void setType(CollectorType type) {
		this.type = type;
	}

	public Long getCollectorUpdateTime() {
		return collectorUpdateTime;
	}

	public void setCollectorUpdateTime(Long collectorUpdateTime) {
		this.collectorUpdateTime = collectorUpdateTime;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public Long getCollectionUpdatedTime() {
		return collectionUpdatedTime;
	}

	public void setCollectionUpdatedTime(Long collectionUpdatedTime) {
		this.collectionUpdatedTime = collectionUpdatedTime;
	}

	public Long getAppCount() {
		return appCount;
	}

	public void setAppCount(Long appCount) {
		this.appCount = appCount;
	}

	public Long getCollectorStartTime() {
		return collectorStartTime;
	}

	public void setCollectorStartTime(Long collectorStartTime) {
		this.collectorStartTime = collectorStartTime;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public Boolean getIsRunning() {
		return isRunning;
	}

	public void setIsRunning(Boolean isRunning) {
		this.isRunning = isRunning;
	}

	public Long getLastStartTime() {
		return lastStartTime;
	}

	public void setLastStartTime(Long lastStartTime) {
		this.lastStartTime = lastStartTime;
	}

}
