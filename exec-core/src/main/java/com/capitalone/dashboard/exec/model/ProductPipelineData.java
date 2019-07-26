package com.capitalone.dashboard.exec.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Model ProductPipelineData
 * collection="product_view_data"
 * @param 
 * @return 
 */
@Document(collection="product_view_data")
public class ProductPipelineData extends BaseModel {
	
	private String appId;
	private String portfolio;
	private String appName;
	private List<ThroughPutModel> throughPutModel;
	private long lastUpdated;
	private int enabledBoards;
	
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	public String getPortfolio() {
		return portfolio;
	}
	public void setPortfolio(String portfolio) {
		this.portfolio = portfolio;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	public long getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(long lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	public int getEnabledBoards() {
		return enabledBoards;
	}
	public void setEnabledBoards(int enabledBoards) {
		this.enabledBoards = enabledBoards;
	}
	public List<ThroughPutModel> getThroughPutModel() {
		return throughPutModel;
	}
	public void setThroughPutModel(List<ThroughPutModel> throughPutModel) {
		this.throughPutModel = throughPutModel;
	}
	
}
