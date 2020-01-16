package com.capitalone.dashboard.exec.model;

import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "usertrack")
public class UserTrack extends BaseModel {

	@Indexed(name = "userEid")
	private String userEid;
	private String userEmail;
	private String userName;

	public List<Long> getLogginTime() {
		return logginTime;
	}

	public void setLogginTime(List<Long> logginTime) {
		this.logginTime = logginTime;
	}

	private List<Long> logginTime;

	public String getUserEid() {
		return userEid;
	}

	public void setUserEid(String userEid) {
		this.userEid = userEid;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
