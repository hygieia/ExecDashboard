package com.capitalone.dashboard.exec.model.vz;

import java.util.List;

/**
 * @author V143611
 *
 */
public class OperationalMetrics {

	private String category;
	private List<String> impactedOrgs;
	private List<String> impactedApps;
	private Long outages;
	private Long outageMTTR;
	private Long events;
	private Long eventMTTR;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<String> getImpactedOrgs() {
		return impactedOrgs;
	}

	public void setImpactedOrgs(List<String> impactedOrgs) {
		this.impactedOrgs = impactedOrgs;
	}

	public List<String> getImpactedApps() {
		return impactedApps;
	}

	public void setImpactedApps(List<String> impactedApps) {
		this.impactedApps = impactedApps;
	}

	public Long getOutages() {
		return outages;
	}

	public void setOutages(Long outages) {
		this.outages = outages;
	}

	public Long getOutageMTTR() {
		return outageMTTR;
	}

	public void setOutageMTTR(Long outageMTTR) {
		this.outageMTTR = outageMTTR;
	}

	public Long getEvents() {
		return events;
	}

	public void setEvents(Long events) {
		this.events = events;
	}

	public Long getEventMTTR() {
		return eventMTTR;
	}

	public void setEventMTTR(Long eventMTTR) {
		this.eventMTTR = eventMTTR;
	}

}
