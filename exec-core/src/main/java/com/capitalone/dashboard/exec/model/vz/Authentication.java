package com.capitalone.dashboard.exec.model.vz;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "authentication")
public class Authentication extends BaseModel {

	@Indexed(unique = true)
	private String username;
	private String firstname;
	private String lastname;
	private String eid;
	private String email;
	private long lastLoggedin;
	private Boolean isAdmin;

	public long getLastLoggedin() {
		return lastLoggedin;
	}

	public void setLastLoggedin(long lastLoggedin) {
		this.lastLoggedin = lastLoggedin;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}


}
