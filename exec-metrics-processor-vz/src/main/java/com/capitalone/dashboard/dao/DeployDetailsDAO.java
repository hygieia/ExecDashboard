package com.capitalone.dashboard.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.collector.MetricsProcessorConfig;
import com.capitalone.dashboard.exec.model.Build;
import com.capitalone.dashboard.exec.model.DeployMetrics;
import com.mongodb.MongoClient;

/**
 * 
 * @author asthaaa
 *
 */
@Component
@SuppressWarnings("PMD")
public class DeployDetailsDAO {

	private static final Logger LOG = LoggerFactory.getLogger(DeployDetailsDAO.class);

	private static final String APPID = "appId";
	private static final String VASTAPPLID = "vastApplID";
	private static final String STARTTIME = "startTime";

	@Autowired
	MetricsProcessorConfig metricsProcessorConfig;

	/**
	 * getMongoClient()
	 * 
	 * @param
	 * @return MongoClient client
	 */
	public MongoClient getMongoClient() {
		MongoClient client = null;
		try {
			client = metricsProcessorConfig.mongo();
			return client;
		} catch (Exception e) {
			LOG.info("Error MongoClient file " + e);
		}
		return client;
	}

	/**
	 * getEntireAppListFromBuild()
	 * 
	 * @param client
	 * @param timeStamp
	 * @return List<String> appIds
	 */
	public List<String> getEntireAppListFromBuild(MongoClient client, Long timeStamp) {
		List<String> results = new ArrayList<>();
		List<String> vastAppList = new ArrayList<>();
		List<String> newAppList = new ArrayList<>();
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where("isIT").is(true));
			vastAppList = mongoOperations.getCollection("vast").distinct(VASTAPPLID, basicQuery.getQueryObject());

			Query basicBuildQuery = new Query();
			basicBuildQuery.addCriteria(Criteria.where(STARTTIME).gte(timeStamp));
			results = mongoOperations.getCollection("builds").distinct(APPID, basicBuildQuery.getQueryObject());

			for (String appId : results) {
				if (vastAppList.contains(appId)) {
					newAppList.add(appId);
				}
			}
			LOG.info("No. of apps are: " + newAppList.size());
		} catch (Exception e) {
			LOG.info("Error in getEntireAppListFromBuild() (DeployDetailsDAO Class)" + e);
		}
		return newAppList;
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
			basicQuery.addCriteria(Criteria.where("isIT").is(true));
			results = mongoOperations.getCollection("vast").distinct(VASTAPPLID, basicQuery.getQueryObject());
		} catch (Exception e) {
			LOG.info("Error in getEntireAppList() (DeployDetailsDAO Class)" + e);
		}
		return results;
	}

	/**
	 * getEntireAppListFromDeploy()
	 * 
	 * @param client
	 * @param timeStamp
	 * @return List<String> appIds
	 */
	public List<String> getEntireAppListFromDeploy(MongoClient client, Long timeStamp) {
		List<String> results = new ArrayList<>();
		List<String> vastAppList = new ArrayList<>();
		List<String> newAppList = new ArrayList<>();
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where("isIT").is(true));
			vastAppList = mongoOperations.getCollection("vast").distinct(VASTAPPLID, basicQuery.getQueryObject());

			Query basicDeployQuery = new Query();
			basicDeployQuery.addCriteria(Criteria.where(STARTTIME).gte(timeStamp));
			results = mongoOperations.getCollection("deployments").distinct(APPID, basicDeployQuery.getQueryObject());

			for (String appId : results) {
				if (vastAppList.contains(appId)) {
					newAppList.add(appId);
				}
			}
			LOG.info("No. of apps are: " + newAppList.size());
		} catch (Exception e) {
			LOG.info("Error in getEntireAppListFromDeploy() (DeployDetailsDAO Class)" + e);
		}
		return newAppList;
	}

	/**
	 * 
	 * @param client
	 * @param appId
	 * @param timeStamp
	 * @return
	 */
	public List<String> getListOfModules(MongoClient client, String appId, Long timeStamp) {
		List<String> result = new ArrayList();
		List<String> finalList = new ArrayList();
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where(APPID).is(appId));
			basicQuery.addCriteria(Criteria.where(STARTTIME).gte(timeStamp));
			result = mongoOperations.getCollection("builds").distinct("jobUrl", basicQuery.getQueryObject());
			for (String jobUrl : result) {
				if (jobUrl != null && jobUrl.toLowerCase().contains("deploy")) {
					finalList.add(jobUrl);
				}
			}
			return finalList;

		} catch (Exception e) {
			LOG.info("Error in getListOfModules() (DeployDetailsDAO Class)" + e);
			return finalList;
		}
	}

	/**
	 * 
	 * @param client
	 * @param appId
	 * @param timeStamp
	 * @return
	 */
	public List<String> getListOfModulesForDeploy(MongoClient client, String appId, Long timeStamp) {
		List<String> result = new ArrayList<>();
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where(APPID).is(appId));
			basicQuery.addCriteria(Criteria.where(STARTTIME).gte(timeStamp));
			result = mongoOperations.getCollection("deployments").distinct("environmentUrl",
					basicQuery.getQueryObject());
		} catch (Exception e) {
			LOG.info("Error in getListOfModulesForDeploy() (DeployDetailsDAO Class)" + e);
		}
		return result;
	}

	/**
	 * 
	 * @param appId
	 * @param jobUrl
	 * @param startDate
	 * @param endDate
	 * @param client
	 * @return
	 */
	public List<Build> getDeploysForModule(String appId, String jobUrl, Long startDate, Long endDate,
			MongoClient client) {
		List<Build> result = new ArrayList();
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(new Criteria().andOperator(Criteria.where(APPID).is(appId),
					Criteria.where("jobUrl").is(jobUrl), Criteria.where(STARTTIME).gte(startDate).lt(endDate)));
			result = mongoOperations.find(basicQuery, Build.class);
			if (result != null)
				return result;
		} catch (Exception e) {
			LOG.info("Error in getDeploysForModule() (DeployDetailsDAO Class)" + e);
		}
		return result;
	}

	/**
	 * 
	 * @param appId
	 * @param jobUrl
	 * @param startDate
	 * @param endDate
	 * @param client
	 * @return
	 */
	public List<DeployMetrics> getDeploymentsForModule(String appId, String jobUrl, Long startDate, Long endDate,
			MongoClient client) {
		List<DeployMetrics> result = new ArrayList<>();
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(new Criteria().andOperator(Criteria.where(APPID).is(appId),
					Criteria.where("environmentUrl").is(jobUrl), Criteria.where(STARTTIME).gte(startDate).lt(endDate)));
			result = mongoOperations.find(basicQuery, DeployMetrics.class);
		} catch (Exception e) {
			LOG.info("Error in getDeploymentsForModule() (DeployDetailsDAO Class)" + e);
		}
		return result;
	}
}
