package com.capitalone.dashboard.exec.model;


public enum ExternalSystemType {
	API, DB;

	/**
	 * @param value
	 * @return
	 */
	public static ExternalSystemType fromString(String value) {
		for (ExternalSystemType systemType : values()) {
			if (systemType.toString().equalsIgnoreCase(value)) {
				return systemType;
			}
		}
		throw new IllegalArgumentException(value + " is not a valid system Type");
	}
}
