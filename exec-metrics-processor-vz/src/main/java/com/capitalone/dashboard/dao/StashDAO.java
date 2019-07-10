package com.capitalone.dashboard.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.collector.MetricsProcessorConfig;
import com.capitalone.dashboard.exec.model.vz.BitbucketPullRequest;
import com.capitalone.dashboard.exec.model.vz.Commit;
import com.capitalone.dashboard.exec.model.vz.StashDetailsInfo;
import com.mongodb.MongoClient;

/**
 * StashDAO
 * 
 * @param
 * @return
 * @author pranav
 */
@Component
public class StashDAO {

	private static final Logger LOG = LoggerFactory.getLogger(StashDAO.class);
	private static final String SCMBRANCH = "scmBranch";
	private static final String SCMURL = "scmUrl";
	private static final String SCMTIMESTAMP = "scmCommitTimestamp";
	private static final String TIMESTAMP = "timestamp";
	private static final String ISIT = "isIT";
	private static final String VAST = "vast";
	private static final String VASTAPPLN = "vastApplID";
	private static final String APPID = "appId";

	@Autowired
	MetricsProcessorConfig metricsProcessorConfig;

	public MongoClient getMongoClient() {
		MongoClient client = null;
		try {
			client = metricsProcessorConfig.mongo();
			return client;
		} catch (Exception e) {
			LOG.info("Error MongoClient file " + e);
		}
		return null;
	}

	/**
	 * getCommitsList()
	 * 
	 * @param repoUrl,
	 * @param repoBranch,
	 * @param startDate,
	 * @param endDate,
	 * @param client
	 * @return List<Commit>
	 */

	public List<Commit> getCommitsList(String appId, String repoUrl, String repoBranch, Long startDate, Long endDate,
			MongoClient client) {
		List<Commit> result = new ArrayList<>();
		try {
			MongoOperations mongoOpertaions = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(new Criteria().andOperator(Criteria.where(SCMURL).is(repoUrl),
					Criteria.where(SCMBRANCH).is(repoBranch), Criteria.where(APPID).is(appId),
					Criteria.where(SCMTIMESTAMP).gte(startDate).lt(endDate)));
			result = mongoOpertaions.find(basicQuery, Commit.class);
			return result;

		} catch (Exception e) {
			LOG.info("Error in getCommitsList() (StashDAO Class)" + e);
		}
		return result;

	}

	/**
	 * isDataAvailableForCommits()
	 * 
	 * checks whether data is available or not
	 * 
	 * @param projectName,
	 * @param date
	 * @param client
	 * @return Boolean true/false
	 */

	public Boolean isDataAvailableForCommits(String appId, String repoUrl, String repoBranch, Long timeStamp,
			MongoClient client) {

		try {
			MongoOperations mongoOpertaions = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQueryCommit = new Query();
			basicQueryCommit.addCriteria(new Criteria().andOperator(Criteria.where(SCMURL).is(repoUrl),
					Criteria.where(SCMBRANCH).is(repoBranch), Criteria.where(APPID).is(appId),
					Criteria.where(SCMTIMESTAMP).gte(timeStamp - 7776000000l)));
			List<Commit> resultCommit = mongoOpertaions.find(basicQueryCommit, Commit.class);
			if (resultCommit != null && !resultCommit.isEmpty())
				return true;
		} catch (Exception e) {
			LOG.info("Error in isDataAvailable (StashDAO Class)" + e);
		}
		return false;

	}

	/**
	 * isDataAvailableforPullRequests()
	 * 
	 * checks whether data is available or not
	 * 
	 * @param appId,
	 * @param timeStamp
	 * @param client
	 * @return Boolean true/false
	 */

	public Boolean isDataAvailableforPullRequests(String appId, Long timeStamp, MongoClient client) {

		try {
			MongoOperations mongoOpertaions = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQueryPullRequest = new Query();
			basicQueryPullRequest.addCriteria(new Criteria().andOperator(Criteria.where(APPID).is(appId),
					Criteria.where(TIMESTAMP).gte(timeStamp - 7776000000l)));
			List<BitbucketPullRequest> resultPullRequest = mongoOpertaions.find(basicQueryPullRequest,
					BitbucketPullRequest.class);
			if (resultPullRequest != null && !resultPullRequest.isEmpty())
				return true;
		} catch (Exception e) {
			LOG.info("Error in isDataAvailable (StashDAO Class)" + e);
		}
		return false;

	}

	/**
	 * getEntireAppList()
	 * 
	 * @param client
	 * @return List<String> appIds
	 */

	public List<String> getEntireAppList(MongoClient client) {
		List<String> results = new ArrayList<>();
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);

			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where(ISIT).is(true));
			results = mongoOperations.getCollection(VAST).distinct(VASTAPPLN, basicQuery.getQueryObject());
		} catch (Exception e) {
			LOG.info("Error in getEntireAppList() (StashDAO Class)" + e);
		}
		return results;
	}

	/**
	 * getEntireProjectList()
	 * 
	 * @param client
	 * @param appId
	 * @return List<String> StashDetailsInfo
	 */

	public List<StashDetailsInfo> getEntireProjectList(MongoClient client, String appId) {
		List<StashDetailsInfo> result = new ArrayList<>();
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where(APPID).is(appId));
			return mongoOperations.find(basicQuery, StashDetailsInfo.class);
		} catch (Exception e) {
			LOG.info("Error in getEntireProjectList() (StashDAO Class)" + e);
		}
		return result;
	}

	/**
	 * 
	 * @param appId
	 * @param startDate
	 * @param endDate
	 * @param client
	 * @return
	 */

	public List<BitbucketPullRequest> getPullRequestList(String appId, Long startDate, Long endDate,
			MongoClient client) {
		List<BitbucketPullRequest> result = new ArrayList<>();
		try {
			MongoOperations mongoOpertaions = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(new Criteria().andOperator(Criteria.where(APPID).is(appId),
					Criteria.where(TIMESTAMP).gte(startDate).lt(endDate)));
			return mongoOpertaions.find(basicQuery, BitbucketPullRequest.class);

		} catch (Exception e) {
			LOG.info("Error in getPullRequestList() (StashDAO Class)" + e);
		}
		return result;
	}

	/**
	 * 
	 * @param repoUrl
	 * @param repoBranch
	 * @param client
	 * @return
	 */

	public List<Commit> getTotalCommitsList(String repoUrl, String repoBranch, MongoClient client) {
		List<Commit> result = new ArrayList<>();
		try {
			Long timeStamp = System.currentTimeMillis();
			MongoOperations mongoOpertaions = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(new Criteria().andOperator(Criteria.where(SCMURL).is(repoUrl),
					Criteria.where(SCMBRANCH).is(repoBranch),
					Criteria.where(SCMTIMESTAMP).gte(timeStamp - 7776000000l)));
			return mongoOpertaions.find(basicQuery, Commit.class);
		} catch (Exception e) {
			LOG.info("Error in isDataAvailableForStash() (StashDAO Class)" + e);
		}
		return result;
	}

	/**
	 * getTotalCommitsSorted()
	 * 
	 * @param repoUrl
	 * @param repoBranch
	 * @param client
	 * @return Commit
	 */

	public Commit getTotalCommitsSorted(String repoUrl, String repoBranch, MongoClient client) {
		Commit result = new Commit();
		try {
			Long timeStamp = System.currentTimeMillis();
			MongoOperations mongoOpertaions = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(new Criteria().andOperator(Criteria.where(SCMURL).is(repoUrl),
					Criteria.where(SCMBRANCH).is(repoBranch),
					Criteria.where(SCMTIMESTAMP).gte(timeStamp - 7776000000l)));
			basicQuery.with(new Sort(Sort.Direction.DESC, SCMTIMESTAMP));
			return mongoOpertaions.findOne(basicQuery, Commit.class);
		} catch (Exception e) {
			LOG.info("Error in isDataAvailableForStash() (StashDAO Class)" + e);
		}
		return result;
	}

}
