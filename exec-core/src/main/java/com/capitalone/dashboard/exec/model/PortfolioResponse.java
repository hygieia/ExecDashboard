package com.capitalone.dashboard.exec.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "portfolio_response")
public class PortfolioResponse extends BaseModel {

	@Indexed(name = "eid")
	private String eid;
	private String lob;
	private String name;
	private Integer order;
	private ExecutiveResponse executive;
	private Long lastUpdated;

	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}

	public String getLob() {
		return lob;
	}

	public void setLob(String lob) {
		this.lob = lob;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ExecutiveResponse getExecutive() {
		return executive;
	}

	public void setExecutive(ExecutiveResponse executive) {
		this.executive = executive;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Long getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Long lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

}
