package com.capitalone.dashboard.exec.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
/**
 * 
 * @author ASTHAAA
 *
 */
@Document(collection = "sprint_metrics")
public class SprintMetrics extends BaseModel {
	
	@Indexed(name = "sprintId")
	private String sprintId;
	private String appId;
	private String sprintName;
	private String projectKey;
	private String projectName;
	private String boardId;
	private long startDate;
	private long endDate;
	private long completeDate;
	private int storiesSayDoRatio;
	private int storyPointsSayDoRatio;
	private int sprintDurationInDays;
	private String sprintClosedBy;
	private int totalStories;
	private int completedStories;
	private double totalStoryPoints;
	private double completedStoryPoints;
	
	public String getProjectKey() {
		return projectKey;
	}
	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getBoardId() {
		return boardId;
	}
	public void setBoardId(String boardId) {
		this.boardId = boardId;
	}
	public String getSprintId() {
		return sprintId;
	}
	public void setSprintId(String sprintId) {
		this.sprintId = sprintId;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getSprintName() {
		return sprintName;
	}
	public void setSprintName(String sprintName) {
		this.sprintName = sprintName;
	}
	public long getStartDate() {
		return startDate;
	}
	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}
	public long getEndDate() {
		return endDate;
	}
	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}
	public long getCompleteDate() {
		return completeDate;
	}
	public void setCompleteDate(long completeDate) {
		this.completeDate = completeDate;
	}
	public int getStoriesSayDoRatio() {
		return storiesSayDoRatio;
	}
	public void setStoriesSayDoRatio(int storiesSayDoRatio) {
		this.storiesSayDoRatio = storiesSayDoRatio;
	}
	public int getStoryPointsSayDoRatio() {
		return storyPointsSayDoRatio;
	}
	public void setStoryPointsSayDoRatio(int storyPointsSayDoRatio) {
		this.storyPointsSayDoRatio = storyPointsSayDoRatio;
	}
	public int getSprintDurationInDays() {
		return sprintDurationInDays;
	}
	public void setSprintDurationInDays(int sprintDurationInDays) {
		this.sprintDurationInDays = sprintDurationInDays;
	}
	public String getSprintClosedBy() {
		return sprintClosedBy;
	}
	public void setSprintClosedBy(String sprintClosedBy) {
		this.sprintClosedBy = sprintClosedBy;
	}
	public int getTotalStories() {
		return totalStories;
	}
	public void setTotalStories(int totalStories) {
		this.totalStories = totalStories;
	}
	public int getCompletedStories() {
		return completedStories;
	}
	public void setCompletedStories(int completedStories) {
		this.completedStories = completedStories;
	}
	public double getTotalStoryPoints() {
		return totalStoryPoints;
	}
	public void setTotalStoryPoints(double totalStoryPoints) {
		this.totalStoryPoints = totalStoryPoints;
	}
	public double getCompletedStoryPoints() {
		return completedStoryPoints;
	}
	public void setCompletedStoryPoints(double completedStoryPoints) {
		this.completedStoryPoints = completedStoryPoints;
	}
	
	
}