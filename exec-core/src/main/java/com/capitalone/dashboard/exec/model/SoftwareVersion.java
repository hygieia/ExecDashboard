package com.capitalone.dashboard.exec.model;

import java.util.List;

/**
 * SoftwareVersion Class
 */
public class SoftwareVersion {

	private Integer totalCount;
	private String standardVersion;
	private Integer updatedCount;
	private List<PatchRequest> patch;

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public String getStandardVersion() {
		return standardVersion;
	}

	public void setStandardVersion(String standardVersion) {
		this.standardVersion = standardVersion;
	}

	public Integer getUpdatedCount() {
		return updatedCount;
	}

	public void setUpdatedCount(Integer updatedCount) {
		this.updatedCount = updatedCount;
	}

	public List<PatchRequest> getPatch() {
		return patch;
	}

	public void setPatch(List<PatchRequest> patch) {
		this.patch = patch;
	}

}
