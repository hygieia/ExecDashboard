package com.capitalone.dashboard.exec.model.vz;

/**
 * @author V143611
 *
 */
public class ExecutiveStatus {

	private String name;
	private String role;
	private Long lastAccessed;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Long getLastAccessed() {
		return lastAccessed;
	}

	public void setLastAccessed(Long lastAccessed) {
		this.lastAccessed = lastAccessed;
	}

}
