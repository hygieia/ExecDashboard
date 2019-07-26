package com.capitalone.dashboard.exec.model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
 *
 *
 */
@Document(collection = "hygieia_artifactories")
public class HygieiaArtifactDetails extends BaseModel {

	private String artifactName;
	private long version;
	private String artifactUrl;
	private long timeStamp;

	public String getArtifactName() {
		return artifactName;
	}

	public void setArtifactName(String artifactName) {
		this.artifactName = artifactName;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public String getArtifactUrl() {
		return artifactUrl;
	}

	public void setArtifactUrl(String artifactUrl) {
		this.artifactUrl = artifactUrl;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

}
