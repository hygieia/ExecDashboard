package com.capitalone.dashboard.exec.model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * UnmappedRoutingRequest
 * 
 * @return
 */
@Document(collection = "unmapped_request")
public class UnmappedRoutingRequest extends BaseModel {

	private String mode;
	private Object createdRequest;

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Object getCreatedRequest() {
		return createdRequest;
	}

	public void setCreatedRequest(Object createdRequest) {
		this.createdRequest = createdRequest;
	}

}