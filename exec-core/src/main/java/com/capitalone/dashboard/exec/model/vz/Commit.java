package com.capitalone.dashboard.exec.model.vz;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A specific commit in a version control repository.
 *
 * Possible collectors: Subversion (in scope) Git (in scope) GitHub TFS
 * BitBucket Unfuddle
 *
 */
@Document(collection = "commits")
@CompoundIndex(def = "{'scmUrl':1, 'appId':1, 'scmCommitTimestamp':1}", name = "scmUrl_appId_scmCommitTimestamp")
public class Commit extends SCM {
	@Id
	private ObjectId id;
	@Indexed(name = "appId")
	private String appId;
	private ObjectId collectorItemId;
	private long timestamp;

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public ObjectId getCollectorItemId() {
		return collectorItemId;
	}

	public void setCollectorItemId(ObjectId collectorItemId) {
		this.collectorItemId = collectorItemId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
}