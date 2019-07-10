package com.capitalone.dashboard.exec.model.vz;

import org.bson.types.ObjectId;

/**
 * 
 * @author PRAKPR5
 *
 */

public class DigitalCockpitResponse {

	private ObjectId id;
	private double value;
	private String eid;

	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
}
