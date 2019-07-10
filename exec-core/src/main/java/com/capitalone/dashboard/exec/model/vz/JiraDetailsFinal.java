package com.capitalone.dashboard.exec.model.vz;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="jira_final_list")
public class JiraDetailsFinal extends BaseModel{

	private String appId;
	private List<JiraInfo> jiraInfo;

	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public List<JiraInfo> getJiraInfo() {
		return jiraInfo;
	}
	public void setJiraInfo(List<JiraInfo> jiraInfo) {
		this.jiraInfo = jiraInfo;
	}
}
