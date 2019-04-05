package com.capitalone.dashboard.exec.model.vz;

import java.util.List;

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
@Document(collection = "pull_request")
@CompoundIndex(def = "{'appId':1, 'timestamp':1}", name = "appId_timestamp")
public class BitbucketPullRequest {
	@Id
	private ObjectId id;
	@Indexed(name = "appId")
	private String appId;
	private ObjectId collectorItemId;
	private long timestamp;
	private long mergeId;
	private String title;
	private long createdDate;
	private long closeDate;
	private String author;
	private List<String> reviewer;
	private String source;
	private String destination;
	private MergeType mergetype;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(long createdDate) {
		this.createdDate = createdDate;
	}

	public long getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(long closeDate) {
		this.closeDate = closeDate;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public List<String> getReviewer() {
		return reviewer;
	}

	public void setReviewer(List<String> reviewer) {
		this.reviewer = reviewer;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public MergeType getMergetype() {
		return mergetype;
	}

	public void setMergetype(MergeType mergetype) {
		this.mergetype = mergetype;
	}

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

	public long getMergeId() {
		return mergeId;
	}

	public void setMergeId(long mergeId) {
		this.mergeId = mergeId;
	}
}