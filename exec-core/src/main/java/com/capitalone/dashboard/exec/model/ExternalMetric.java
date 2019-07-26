package com.capitalone.dashboard.exec.model;

public class ExternalMetric {

	private String queryOrApi;
	private Boolean status;
	private String impacteData;
	private long executedTime;

	public String getQueryOrApi() {
		return queryOrApi;
	}

	public void setQueryOrApi(String queryOrApi) {
		this.queryOrApi = queryOrApi;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getImpacteData() {
		return impacteData;
	}

	public void setImpacteData(String impacteData) {
		this.impacteData = impacteData;
	}

	public long getExecutedTime() {
		return executedTime;
	}

	public void setExecutedTime(long executedTime) {
		this.executedTime = executedTime;
	}
}
