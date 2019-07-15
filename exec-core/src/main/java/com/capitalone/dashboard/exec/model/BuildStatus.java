package com.capitalone.dashboard.exec.model;

/**
 * Enumeration of valid build statuses.
 */
public enum BuildStatus {
	SUCCESS, UNSTABLE, ABORTED, FAILURE, INPROGRESS, UNKNOWN, Success, Failure, Unstable, Aborted, InProgress, Unknown;

	public static BuildStatus fromString(String value) {
		for (BuildStatus buildStatus : values()) {
			if (buildStatus.toString().equalsIgnoreCase(value)) {
				return buildStatus;
			}
		}
		throw new IllegalArgumentException(value + " is not a valid BuildStatus.");
	}
}
