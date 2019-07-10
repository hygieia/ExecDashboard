package com.capitalone.dashboard.exec.model.vz;

public class BunitCredentials {

	private String host;
	private int port;
	private String dbName;
	private String dbUserName;
	private String dbUserCredentials;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDbUserName() {
		return dbUserName;
	}

	public void setDbUserName(String dbUserName) {
		this.dbUserName = dbUserName;
	}

	public String getDbUserCredentials() {
		return dbUserCredentials;
	}

	public void setDbUserCredentials(String dbUserCredentials) {
		this.dbUserCredentials = dbUserCredentials;
	}

}