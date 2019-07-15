package com.capitalone.dashboard.exec.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Represents a feature user story (story/requirement) of a component.
 * 
 */
@Document(collection = "feature_userstory")
@CompoundIndex(def = "{'sTeamName':1, 'creationDate':1}", name = "sTeamName_creationDate")
public class FeatureUserStory extends BaseModel {
	private ObjectId collectorId;
	/*
	 * Story data
	 */
	private String sId;
	private String sNumber;
	private String sName;
	private String sTypeId;
	private String sTypeName;
	private String sStatus;
	private String sState;
	private String sEstimate; // estimate in story points
	private Integer sEstimateTime; // estimate in minutes
	private String sUrl;
	private String changeDate;
	private String isDeleted;
	private String priority;

	/*
	 * Owner data
	 */
	private List<String> sOwnersID;
	private List<String> sOwnersIsDeleted;
	private List<String> sOwnersChangeDate;
	private List<String> sOwnersState;
	private List<String> sOwnersUsername;
	private List<String> sOwnersFullName;
	private List<String> sOwnersShortName;

	/*
	 * ScopeOwner data
	 */
	private String sTeamIsDeleted;
	private String sTeamAssetState;
	private String sTeamChangeDate;
	@Indexed(name = "sTeamName")
	private String sTeamName;
	private String sProjectKey;
	@Indexed(name = "sTeamID")
	private String sTeamID;

	/*
	 * Sprint data
	 */
	private String sSprintIsDeleted;
	private String sSprintChangeDate;
	private String sSprintAssetState;
	private String sSprintEndDate;
	private String sSprintBeginDate;
	private String sSprintName;
	private String sSprintID;
	private String sSprintUrl;

	/*
	 * Epic data
	 */
	private String sEpicIsDeleted;
	private String sEpicChangeDate;
	private String sEpicAssetState;
	private String sEpicType;
	private String sEpicEndDate;
	private String sEpicBeginDate;
	private String sEpicName;
	private String sEpicUrl;
	private String sEpicNumber;
	private String sEpicID;

	/*
	 * Scope data
	 */
	private String sProjectPath;
	private String sProjectIsDeleted;
	private String sProjectState;
	private String sProjectChangeDate;
	private String sProjectEndDate;
	private String sProjectBeginDate;
	private String sProjectName;
	private String sProjectID;

	//Custom Fields
	private List<FeatureStatusTransition> statusTransition;
	private List<FeatureStatusCategoryRequest> statusTransitionInfo;
	private Double storyPoints;
	private String testPhase;
	private String priorityLevel;
	private String creationDate;
	private String mEnvironment;
	private String dspEnvironment;
	private String resolutionType;
	private long totalTimeTaken;
	private String statusCategory;
	private String sdlcEnvironment;
	private String dataEnvironment;
	private String environmentLocationITT;
	private String environmentProd;
	private String deploymentTargetEnvironment;
	private String environmentFRPA;
	private String environmentSessionID;
	private String testEnvironment;
	private String environmentBlocked;
	private String fRPAEnvironment;
	private String environmentType;
	private String aLMEnvironment;
	private String bIBVEnvironment;
	private String cPMENV;

	private String applicationEnvironmentDetected;
	private String environments; 
	private String targetEnvironments;

	private String environmentDetected;
	private String iTTEnvironment;
	private String environment;
	private String testingEnvironment;
	private String qAEnvironment;

  	/**
	 * @return String
	 */
	public String getApplicationEnvironmentDetected() {
		return applicationEnvironmentDetected;
	}

  	/**
	 * @param applicationEnvironmentDetected
	 */
	public void setApplicationEnvironmentDetected(
			String applicationEnvironmentDetected) {
		this.applicationEnvironmentDetected = applicationEnvironmentDetected;
	}

	public String getEnvironments() {
		return environments;
	}

	public void setEnvironments(String environments) {
		this.environments = environments;
	}

	public String getTargetEnvironments() {
		return targetEnvironments;
	}

	public void setTargetEnvironments(String targetEnvironments) {
		this.targetEnvironments = targetEnvironments;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getEnvironmentDetected() {
		return environmentDetected;
	}

	public void setEnvironmentDetected(String environmentDetected) {
		this.environmentDetected = environmentDetected;
	}

	/**
	 * @return String
	 */
	public String getiTTEnvironment() {
		return iTTEnvironment;
	}

	/**
	 * @param iTTEnvironment
	 */
	public void setiTTEnvironment(String iTTEnvironment) {
		this.iTTEnvironment = iTTEnvironment;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getTestingEnvironment() {
		return testingEnvironment;
	}

	public void setTestingEnvironment(String testingEnvironment) {
		this.testingEnvironment = testingEnvironment;
	}

  	/**
	 * @return String
	 */
	public String getqAEnvironment() {
		return qAEnvironment;
	}

	public void setqAEnvironment(String qAEnvironment) {
		this.qAEnvironment = qAEnvironment;
	}

	public String getDataEnvironment() {
		return dataEnvironment;
	}

	public void setDataEnvironment(String dataEnvironment) {
		this.dataEnvironment = dataEnvironment;
	}

	public String getEnvironmentLocationITT() {
		return environmentLocationITT;
	}

	public void setEnvironmentLocationITT(String environmentLocationITT) {
		this.environmentLocationITT = environmentLocationITT;
	}

	public String getEnvironmentProd() {
		return environmentProd;
	}

	public void setEnvironmentProd(String environmentProd) {
		this.environmentProd = environmentProd;
	}

	public String getDeploymentTargetEnvironment() {
		return deploymentTargetEnvironment;
	}

	public void setDeploymentTargetEnvironment(String deploymentTargetEnvironment) {
		this.deploymentTargetEnvironment = deploymentTargetEnvironment;
	}

	public String getEnvironmentFRPA() {
		return environmentFRPA;
	}

	public void setEnvironmentFRPA(String environmentFRPA) {
		this.environmentFRPA = environmentFRPA;
	}

	public String getEnvironmentSessionID() {
		return environmentSessionID;
	}

	public void setEnvironmentSessionID(String environmentSessionID) {
		this.environmentSessionID = environmentSessionID;
	}

	public String getTestEnvironment() {
		return testEnvironment;
	}

	public void setTestEnvironment(String testEnvironment) {
		this.testEnvironment = testEnvironment;
	}

	public String getEnvironmentBlocked() {
		return environmentBlocked;
	}

	public void setEnvironmentBlocked(String environmentBlocked) {
		this.environmentBlocked = environmentBlocked;
	}

  	/**
	 * @return String
	 */
	public String getfRPAEnvironment() {
		return fRPAEnvironment;
	}

	public void setfRPAEnvironment(String fRPAEnvironment) {
		this.fRPAEnvironment = fRPAEnvironment;
	}

	public String getEnvironmentType() {
		return environmentType;
	}

	public void setEnvironmentType(String environmentType) {
		this.environmentType = environmentType;
	}

  	/**
	 * @return String
	 */
	public String getaLMEnvironment() {
		return aLMEnvironment;
	}

	public void setaLMEnvironment(String aLMEnvironment) {
		this.aLMEnvironment = aLMEnvironment;
	}

  	/**
	 * @return String
	 */
	public String getbIBVEnvironment() {
		return bIBVEnvironment;
	}

	public void setbIBVEnvironment(String bIBVEnvironment) {
		this.bIBVEnvironment = bIBVEnvironment;
	}

  	/**
	 * @return String
	 */
	public String getcPMENV() {
		return cPMENV;
	}

	public void setcPMENV(String cPMENV) {
		this.cPMENV = cPMENV;
	}

	public ObjectId getCollectorId() {
		return collectorId;
	}

	public void setCollectorId(ObjectId collectorId) {
		this.collectorId = collectorId;
	}

  	/**
	 * @return String
	 */
	public String getsId() {
		return sId;
	}

	public void setsId(String sId) {
		this.sId = sId;
	}

  	/**
	 * @return String
	 */
	public String getsNumber() {
		return sNumber;
	}

	public void setsNumber(String sNumber) {
		this.sNumber = sNumber;
	}

  	/**
	 * @return String
	 */
	public String getsName() {
		return sName;
	}

	public void setsName(String sName) {
		this.sName = sName;
	}

  	/**
	 * @return String
	 */
	public String getsTypeId() {
		return sTypeId;
	}

	public void setsTypeId(String sTypeId) {
		this.sTypeId = sTypeId;
	}

  	/**
	 * @return String
	 */
	public String getsTypeName() {
		return sTypeName;
	}

	public void setsTypeName(String sTypeName) {
		this.sTypeName = sTypeName;
	}

  	/**
	 * @return String
	 */
	public String getsStatus() {
		return sStatus;
	}

	public void setsStatus(String sStatus) {
		this.sStatus = sStatus;
	}

  	/**
	 * @return String
	 */
	public String getsState() {
		return sState;
	}

	public void setsState(String sState) {
		this.sState = sState;
	}

  	/**
	 * @return String
	 */
	public String getsEstimate() {
		return sEstimate;
	}

	public void setsEstimate(String sEstimate) {
		this.sEstimate = sEstimate;
	}

  	/**
	 * @return Integer
	 */
	public Integer getsEstimateTime() {
		return sEstimateTime;
	}

	public void setsEstimateTime(Integer sEstimateTime) {
		this.sEstimateTime = sEstimateTime;
	}

	public void setsUrl(String sUrl) {
		this.sUrl = sUrl;
	}

  	/**
	 * @return String
	 */
	public String getsUrl() {
		return sUrl;
	}

  	/**
	 * @return String
	 */
	public String getsProjectID() {
		return sProjectID;
	}

	public void setsProjectID(String sProjectID) {
		this.sProjectID = sProjectID;
	}

  	/**
	 * @return String
	 */
	public String getsEpicID() {
		return sEpicID;
	}

	public void setsEpicID(String sEpicID) {
		this.sEpicID = sEpicID;
	}

  	/**
	 * @return String
	 */
	public String getsSprintID() {
		return sSprintID;
	}

	public void setsSprintID(String sSprintID) {
		this.sSprintID = sSprintID;
	}

	public void setsSprintUrl(String sSprintUrl) {
		this.sSprintUrl = sSprintUrl;
	}

  	/**
	 * @return String
	 */
	public String getsSprintUrl() {
		return sSprintUrl;
	}

  	/**
	 * @return String
	 */
	public String getsTeamID() {
		return sTeamID;
	}

	public void setsTeamID(String sTeamID) {
		this.sTeamID = sTeamID;
	}

  	/**
	 * @return String
	 */
	public String getsProjectKey() {
		return sProjectKey;
	}

	public void setsProjectKey(String sProjectKey) {
		this.sProjectKey = sProjectKey;
	}

	public String getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(String changeDate) {
		this.changeDate = changeDate;
	}

  	/**
	 * @return String
	 */
	public List<String> getsOwnersID() {
		return sOwnersID;
	}

	public void setsOwnersID(List<String> sOwnersID) {
		this.sOwnersID = sOwnersID;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public void setsProjectName(String sProjectName) {
		this.sProjectName = sProjectName;
	}

  	/**
	 * @return String
	 */
	public String getsProjectName() {
		return this.sProjectName;
	}

	public void setsProjectBeginDate(String sProjectBeginDate) {
		this.sProjectBeginDate = sProjectBeginDate;
	}

  	/**
	 * @return String
	 */
	public String getsProjectBeginDate() {
		return this.sProjectBeginDate;
	}

	public void setsProjectEndDate(String sProjectEndDate) {
		this.sProjectEndDate = sProjectEndDate;
	}

  	/**
	 * @return String
	 */
	public String getsProjectEndDate() {
		return this.sProjectEndDate;
	}

	public void setsProjectChangeDate(String sProjectChangeDate) {
		this.sProjectChangeDate = sProjectChangeDate;
	}

  	/**
	 * @return String
	 */
	public String getsProjectChangeDate() {
		return this.sProjectChangeDate;
	}

	public void setsProjectState(String sProjectState) {
		this.sProjectState = sProjectState;
	}

  	/**
	 * @return String
	 */
	public String getsProjectState() {
		return this.sProjectState;
	}

	public void setsProjectIsDeleted(String sProjectIsDeleted) {
		this.sProjectIsDeleted = sProjectIsDeleted;
	}

  	/**
	 * @return String
	 */
	public String getsProjectIsDeleted() {
		return this.sProjectIsDeleted;
	}

	public void setsProjectPath(String sProjectPath) {
		this.sProjectPath = sProjectPath;
	}

  	/**
	 * @return String
	 */
	public String getsProjectPath() {
		return this.sProjectPath;
	}

	public void setsEpicNumber(String sEpicNumber) {
		this.sEpicNumber = sEpicNumber;
	}

  	/**
	 * @return String
	 */
	public String getsEpicNumber() {
		return this.sEpicNumber;
	}

	public void setsEpicName(String sEpicName) {
		this.sEpicName = sEpicName;
	}

  	/**
	 * @return String
	 */
	public String getsEpicName() {
		return this.sEpicName;
	}

	public void setsEpicUrl(String sEpicUrl) {
		this.sEpicUrl = sEpicUrl;
	}

  	/**
	 * @return String
	 */
	public String getsEpicUrl() {
		return sEpicUrl;
	}

	public void setsEpicBeginDate(String sEpicBeginDate) {
		this.sEpicBeginDate = sEpicBeginDate;
	}

  	/**
	 * @return String
	 */
	public String getsEpicBeginDate() {
		return this.sEpicBeginDate;
	}

	public void setsEpicEndDate(String sEpicEndDate) {
		this.sEpicEndDate = sEpicEndDate;
	}

  	/**
	 * @return String
	 */
	public String getsEpicEndDate() {
		return this.sEpicEndDate;
	}

	public void setsEpicType(String sEpicType) {
		this.sEpicType = sEpicType;
	}

  	/**
	 * @return String
	 */
	public String getsEpicType() {
		return this.sEpicType;
	}

	public void setsEpicAssetState(String sEpicAssetState) {
		this.sEpicAssetState = sEpicAssetState;
	}

  	/**
	 * @return String
	 */
	public String getsEpicAssetState() {
		return this.sEpicAssetState;
	}

	public void setsEpicChangeDate(String sEpicChangeDate) {
		this.sEpicChangeDate = sEpicChangeDate;
	}

  	/**
	 * @return String
	 */
	public String getsEpicChangeDate() {
		return this.sEpicChangeDate;
	}

	public void setsEpicIsDeleted(String sEpicIsDeleted) {
		this.sEpicIsDeleted = sEpicIsDeleted;
	}

  	/**
	 * @return String
	 */
	public String getsEpicIsDeleted() {
		return this.sEpicIsDeleted;
	}

	public void setsSprintName(String sSprintName) {
		this.sSprintName = sSprintName;
	}

  	/**
	 * @return String
	 */
	public String getsSprintName() {
		return this.sSprintName;
	}

	public void setsSprintBeginDate(String sSprintBeginDate) {
		this.sSprintBeginDate = sSprintBeginDate;
	}

  	/**
	 * @return String
	 */
	public String getsSprintBeginDate() {
		return this.sSprintBeginDate;
	}

	public void setsSprintEndDate(String sSprintEndDate) {
		this.sSprintEndDate = sSprintEndDate;
	}

  	/**
	 * @return String
	 */
	public String getsSprintEndDate() {
		return this.sSprintEndDate;
	}

	public void setsSprintAssetState(String sSprintAssetState) {
		this.sSprintAssetState = sSprintAssetState;
	}

  	/**
	 * @return String
	 */
	public String getsSprintAssetState() {
		return this.sSprintAssetState;
	}

	public void setsSprintChangeDate(String sSprintChangeDate) {
		this.sSprintChangeDate = sSprintChangeDate;
	}

  	/**
	 * @return String
	 */
	public String getsSprintChangeDate() {
		return this.sSprintChangeDate;
	}

	public void setsSprintIsDeleted(String sSprintIsDeleted) {
		this.sSprintIsDeleted = sSprintIsDeleted;
	}

  	/**
	 * @return String
	 */
	public String getsSprintIsDeleted() {
		return this.sSprintIsDeleted;
	}

	public void setsTeamName(String sTeamName) {
		this.sTeamName = sTeamName;
	}

  	/**
	 * @return String
	 */
	public String getsTeamName() {
		return this.sTeamName;
	}

	public void setsTeamChangeDate(String sTeamChangeDate) {
		this.sTeamChangeDate = sTeamChangeDate;
	}

  	/**
	 * @return String
	 */
	public String getsTeamChangeDate() {
		return this.sTeamChangeDate;
	}

	public void setsTeamAssetState(String sTeamAssetState) {
		this.sTeamAssetState = sTeamAssetState;
	}

  	/**
	 * @return String
	 */
	public String getsTeamAssetState() {
		return this.sTeamAssetState;
	}

	public void setsTeamIsDeleted(String sTeamIsDeleted) {
		this.sTeamIsDeleted = sTeamIsDeleted;
	}

  	/**
	 * @return String
	 */
	public String getsTeamIsDeleted() {
		return this.sTeamIsDeleted;
	}

	public void setsOwnersShortName(List<String> list) {
		this.sOwnersShortName = list;
	}

  	/**
	 * @return List<String>
	 */
	public List<String> getsOwnersShortName() {
		return this.sOwnersShortName;
	}

	public void setsOwnersFullName(List<String> list) {
		this.sOwnersFullName = list;
	}

  	/**
	 * @return List<String>
	 */
	public List<String> getsOwnersFullName() {
		return this.sOwnersFullName;
	}

	public void setsOwnersUsername(List<String> list) {
		this.sOwnersUsername = list;
	}

  	/**
	 * @return List<String>
	 */
	public List<String> getsOwnersUsername() {
		return this.sOwnersUsername;
	}

	public void setsOwnersState(List<String> list) {
		this.sOwnersState = list;
	}

  	/**
	 * @return List<String>
	 */
	public List<String> getsOwnersState() {
		return this.sOwnersState;
	}

	public void setsOwnersChangeDate(List<String> list) {
		this.sOwnersChangeDate = list;
	}

  	/**
	 * @return List<String>
	 */
	public List<String> getsOwnersChangeDate() {
		return this.sOwnersChangeDate;
	}

	public void setsOwnersIsDeleted(List<String> list) {
		this.sOwnersIsDeleted = list;
	}

  	/**
	 * @return List<String>
	 */
	public List<String> getsOwnersIsDeleted() {
		return this.sOwnersIsDeleted;
	}

	public List<FeatureStatusTransition> getStatusTransition() {
		return statusTransition;
	}

	public void setStatusTransition(List<FeatureStatusTransition> statusTransition) {
		this.statusTransition = statusTransition;
	}

	public List<FeatureStatusCategoryRequest> getStatusTransitionInfo() {
		return statusTransitionInfo;
	}

	public void setStatusTransitionInfo(List<FeatureStatusCategoryRequest> statusTransitionInfo) {
		this.statusTransitionInfo = statusTransitionInfo;
	}

	public Double getStoryPoints() {
		return storyPoints;
	}

	public void setStoryPoints(Double storyPoints) {
		this.storyPoints = storyPoints;
	}

	public String getTestPhase() {
		return testPhase;
	}

	public void setTestPhase(String testPhase) {
		this.testPhase = testPhase;
	}

	public String getPriorityLevel() {
		return priorityLevel;
	}

	public void setPriorityLevel(String priorityLevel) {
		this.priorityLevel = priorityLevel;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

  	/**
	 * @return String
	 */
	public String getmEnvironment() {
		return mEnvironment;
	}

	public void setmEnvironment(String mEnvironment) {
		this.mEnvironment = mEnvironment;
	}

	public String getDspEnvironment() {
		return dspEnvironment;
	}

	public void setDspEnvironment(String dspEnvironment) {
		this.dspEnvironment = dspEnvironment;
	}

	public String getResolutionType() {
		return resolutionType;
	}

	public void setResolutionType(String resolutionType) {
		this.resolutionType = resolutionType;
	}

	public long getTotalTimeTaken() {
		return totalTimeTaken;
	}

	public void setTotalTimeTaken(long totalTimeTaken) {
		this.totalTimeTaken = totalTimeTaken;
	}

	public String getStatusCategory() {
		return statusCategory;
	}

	public void setStatusCategory(String statusCategory) {
		this.statusCategory = statusCategory;
	}

	public String getSdlcEnvironment() {
		return sdlcEnvironment;
	}

	public void setSdlcEnvironment(String sdlcEnvironment) {
		this.sdlcEnvironment = sdlcEnvironment;
	}
}
