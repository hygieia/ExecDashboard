package com.capitalone.dashboard.exec.model;

import java.util.List;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 *
 */
@Document(collection = "track_user_views")
@CompoundIndex(def = "{'userId':1, 'timeStamp':1}", name = "userId_timeStamp")
public class TrackPageViews {

	@Indexed(name = "view")
	private String view;
	private String userId;
	private List<String> executiveViewId;
	private List<String> applicationViewId;
	private String metricsName;
	private Long timeStamp;

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<String> getExecutiveViewId() {
		return executiveViewId;
	}

	public void setExecutiveViewId(List<String> executiveViewId) {
		this.executiveViewId = executiveViewId;
	}

	public List<String> getApplicationViewId() {
		return applicationViewId;
	}

	public void setApplicationViewId(List<String> applicationViewId) {
		this.applicationViewId = applicationViewId;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getMetricsName() {
		return metricsName;
	}

	public void setMetricsName(String metricsName) {
		this.metricsName = metricsName;
	}

}
