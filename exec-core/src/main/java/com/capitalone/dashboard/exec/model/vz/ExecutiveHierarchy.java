package com.capitalone.dashboard.exec.model.vz;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "executives_hierarchy")
public class ExecutiveHierarchy extends BaseModel {

	@Indexed(name = "eid")
	private String eid;
	private String designation;
	private String role;
	private Map<String, List<String>> reportees;
	private List<String> directReportees;
	private List<String> linkedReportees;
	private Long lastUpdated;

	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public Map<String, List<String>> getReportees() {
		return reportees;
	}

	public void setReportees(Map<String, List<String>> reportees) {
		this.reportees = reportees;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<String> getDirectReportees() {
		return directReportees;
	}

	public void setDirectReportees(List<String> directReportees) {
		this.directReportees = directReportees;
	}

	public List<String> getLinkedReportees() {
		return linkedReportees;
	}

	public void setLinkedReportees(List<String> linkedReportees) {
		this.linkedReportees = linkedReportees;
	}

	public Long getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Long lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

}
