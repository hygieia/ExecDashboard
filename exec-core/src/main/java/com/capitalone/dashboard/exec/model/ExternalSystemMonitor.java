package com.capitalone.dashboard.exec.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "externalmonitor")
public class ExternalSystemMonitor extends BaseModel {

	private CollectorType sourceSystemName;
	private List<ExecutiveMetricCard> impaactedExecutiveCard;
	private ExternalSystemType sourceSystemType;
	private Boolean overallStatus;
	private long lastConnectedTime;
	private Set<ExternalMetric> metrics = new HashSet<>();
	private Map<Object, Object> connectionCredentials = new HashMap<>();

	public CollectorType getSourceSystemName() {
		return sourceSystemName;
	}

	public void setSourceSystemName(CollectorType sourceSystemName) {
		this.sourceSystemName = sourceSystemName;
	}

	public long getLastConnectedTime() {
		return lastConnectedTime;
	}

	public void setLastConnectedTime(long lastConnectedTime) {
		this.lastConnectedTime = lastConnectedTime;
	}

	public ExternalSystemType getSourceSystemType() {
		return sourceSystemType;
	}

	public void setSourceSystemType(ExternalSystemType sourceSystemType) {
		this.sourceSystemType = sourceSystemType;
	}

	public Map<Object, Object> getConnectionCredentials() {
		return connectionCredentials;
	}

	public void setConnectionCredentials(Map<Object, Object> connectionCredentials) {
		this.connectionCredentials = connectionCredentials;
	}

	public Set<ExternalMetric> getMetrics() {
		return metrics;
	}

	public void setMetrics(Set<ExternalMetric> metrics) {
		this.metrics = metrics;
	}

	public List<ExecutiveMetricCard> getImpaactedExecutiveCard() {
		return impaactedExecutiveCard;
	}

	public void setImpaactedExecutiveCard(List<ExecutiveMetricCard> impaactedExecutiveCard) {
		this.impaactedExecutiveCard = impaactedExecutiveCard;
	}

	public Boolean getOverallStatus() {
		return overallStatus;
	}

	public void setOverallStatus(Boolean overallStatus) {
		this.overallStatus = overallStatus;
	}
}
