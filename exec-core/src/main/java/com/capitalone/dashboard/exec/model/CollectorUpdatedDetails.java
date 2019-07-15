package com.capitalone.dashboard.exec.model;


import org.springframework.data.mongodb.core.mapping.Document;

/**
 * CollectorUpdatedDetails for collectiong Timestamp details
 * 
 */
@Document(collection = "collector_updated_details")
public class CollectorUpdatedDetails extends BaseModel {

	private CollectorType type;
	private Long collectorUpdateTime;
	private String collectionName;
	private Long collectionUpdatedTime;
	private String updatedTimeField;
	private String appIdFieldName;
	private Long appCount;
	private Long collectorStartTime;
	private Long totalExecutionTime;
	private Boolean isRunning;

	/**
	 * CollectorUpdatedDetails
	 * 
	 * @param collectionName
	 * @param type
	 * @param updatedTimeField
	 * @param appIdFieldName
	 */
	public CollectorUpdatedDetails(String collectionName, CollectorType type, String updatedTimeField,
                                   String appIdFieldName) {

		this.collectionName = collectionName;
		this.type = type;
		this.updatedTimeField = updatedTimeField;
		this.appIdFieldName = appIdFieldName;
	}

	/**
	 * getType
	 * 
	 * @return CollectorType
	 */
	public CollectorType getType() {
		return type;
	}

	/**
	 * 
	 * @param type
	 */
	public void setType(CollectorType type) {
		this.type = type;
	}

	/**
	 * 
	 * @return
	 */
	public Long getCollectorUpdateTime() {
		return collectorUpdateTime;
	}

	/**
	 * 
	 * @param collectorUpdateTime
	 */
	public void setCollectorUpdateTime(Long collectorUpdateTime) {
		this.collectorUpdateTime = collectorUpdateTime;
	}

	/**
	 * 
	 * @return
	 */
	public String getCollectionName() {
		return collectionName;
	}

	/**
	 * 
	 * @param collectionName
	 */
	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	/**
	 * 
	 * @return
	 */
	public Long getCollectionUpdatedTime() {
		return collectionUpdatedTime;
	}

	/**
	 * 
	 * @param collectionUpdatedTime
	 */
	public void setCollectionUpdatedTime(Long collectionUpdatedTime) {
		this.collectionUpdatedTime = collectionUpdatedTime;
	}

	/**
	 * 
	 * @return
	 */
	public String getUpdatedTimeField() {
		return updatedTimeField;
	}

	/**
	 * 
	 * @param updatedTimeField
	 */
	public void setUpdatedTimeField(String updatedTimeField) {
		this.updatedTimeField = updatedTimeField;
	}

	public String getAppIdFieldName() {
		return appIdFieldName;
	}

	/**
	 * 
	 * @param appIdFieldName
	 */
	public void setAppIdFieldName(String appIdFieldName) {
		this.appIdFieldName = appIdFieldName;
	}

	/**
	 * 
	 * @return
	 */
	public Long getAppCount() {
		return appCount;
	}

	/**
	 * 
	 * @param appCount
	 */
	public void setAppCount(Long appCount) {
		this.appCount = appCount;
	}

	public Long getCollectorStartTime() {
		return collectorStartTime;
	}

	public void setCollectorStartTime(Long collectorStartTime) {
		this.collectorStartTime = collectorStartTime;
	}

	public Long getTotalExecutionTime() {
		return totalExecutionTime;
	}

	public void setTotalExecutionTime(Long totalExecutionTime) {
		this.totalExecutionTime = totalExecutionTime;
	}

	public Boolean getIsRunning() {
		return isRunning;
	}

	public void setIsRunning(Boolean isRunning) {
		this.isRunning = isRunning;
	}

}
