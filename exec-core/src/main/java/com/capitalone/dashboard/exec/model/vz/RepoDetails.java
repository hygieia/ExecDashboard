package com.capitalone.dashboard.exec.model.vz;

/**
 * Data model for RepoDetails object
 * 
 * @author PRAKPR5
 *
 */
public class RepoDetails {

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

}
