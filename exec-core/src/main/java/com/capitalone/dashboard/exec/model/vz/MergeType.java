package com.capitalone.dashboard.exec.model.vz;

public enum MergeType {
	MERGED, // pull requests merged
	DECLINED;// pull request declined

	public static MergeType fromString(String value) {
		for (MergeType mergeType : values()) {
			if (mergeType.toString().equalsIgnoreCase(value)) {
				return mergeType;
			}
		}
		throw new IllegalArgumentException(value + " is not a valid merge Type");
	}
}
