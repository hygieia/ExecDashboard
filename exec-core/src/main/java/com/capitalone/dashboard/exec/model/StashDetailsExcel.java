package com.capitalone.dashboard.exec.model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Data Model for Stash Details obtained from excel sheet
 * 
 * @author prakpr5
 */

@Document(collection = "stash_details_excel")
public class StashDetailsExcel extends BaseModel {

	private String appId;
	private String repoUrl;
	private String repoBranch;
	private boolean isUnlimitedData;
	private String projectKey;
	private String repoSlug;

	public String getProjectKey() {
		return projectKey;
	}

	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}

	public String getRepoSlug() {
		return repoSlug;
	}

	public void setRepoSlug(String repoSlug) {
		this.repoSlug = repoSlug;
	}

	public boolean isUnlimitedData() {
		return isUnlimitedData;
	}

	public void setUnlimitedData(boolean isUnlimitedData) {
		this.isUnlimitedData = isUnlimitedData;
	}

	public String getRepoUrl() {
		return repoUrl;
	}

	public void setRepoUrl(String repoUrl) {
		this.repoUrl = repoUrl;
	}

	public String getRepoBranch() {
		return repoBranch;
	}

	public void setRepoBranch(String repoBranch) {
		this.repoBranch = repoBranch;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
}
