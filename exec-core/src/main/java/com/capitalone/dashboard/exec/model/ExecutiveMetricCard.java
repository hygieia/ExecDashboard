package com.capitalone.dashboard.exec.model;

/**
 *
 *
 */

public enum ExecutiveMetricCard {
	VELOCITY, CYCLETIME, SECURITY, EVENTS, QUALITY, WIP, STORIES, BUILDS, DEPLOYS, CLOUDCOST, CODEREPO, EXECUTIVEDATA;

	/**
	 * @param value
	 * @return
	 */
	public static ExecutiveMetricCard fromString(String value) {
		for (ExecutiveMetricCard metricType : values()) {
			if (metricType.toString().equalsIgnoreCase(value)) {
				return metricType;
			}
		}
		throw new IllegalArgumentException(value + " is not a valid metricType Type");
	}
}
