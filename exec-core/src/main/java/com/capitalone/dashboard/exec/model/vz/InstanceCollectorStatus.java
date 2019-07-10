package com.capitalone.dashboard.exec.model.vz;

public class InstanceCollectorStatus {

	private String collectorName;
	private boolean enabled;
	private boolean online;
	private CollectorType collectorType;
	private long lastExecuted;

	public String getCollectorName() {
		return collectorName;
	}

	public void setCollectorName(String collectorName) {
		this.collectorName = collectorName;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public CollectorType getCollectorType() {
		return collectorType;
	}

	public void setCollectorType(CollectorType collectorType) {
		this.collectorType = collectorType;
	}

	public long getLastExecuted() {
		return lastExecuted;
	}

	public void setLastExecuted(long lastExecuted) {
		this.lastExecuted = lastExecuted;
	}

}
