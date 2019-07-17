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
import com.mongodb.MongoClient;

/**
 * 
 * @author asthaaa
 *
 */
@Component
@SuppressWarnings("PMD")
public class BuildDetailsDAO {

	private static final Logger LOG = LoggerFactory.getLogger(BuildDetailsDAO.class);

	private static final String APPID = "appId";
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
	 * getEntireAppList()
	 * 
	 * @param client
	 * @return List<String> appIds
	 */
	public List<String> getEntireAppList(MongoClient client) {
		List<String> results = new ArrayList<>();
		List<String> newAppList = new ArrayList<>();
		List<String> vastAppList = new ArrayList<>();

		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where("isIT").is(true));
			vastAppList = mongoOperations.getCollection("vast").distinct("vastApplID", basicQuery.getQueryObject());

			results = mongoOperations.getCollection("builds").distinct(APPID);

			for (String appId : results) {
				if (vastAppList.contains(appId)) {
					newAppList.add(appId);
				}
			}
			LOG.info("No. of apps are: " + newAppList.size());

		} catch (Exception e) {
			LOG.info("Error in getEntireAppList() (BuildDetailsDAO Class)" + e);
		}
		return newAppList;
	}

	/**
	 * getEntireAppListForBuild()
	 * 
	 * @param client
	 * @param timeStamp
	 * @return List<String> appIds
	 */
	public List<String> getEntireAppListForBuild(MongoClient client, Long timeStamp) {
		List<String> results = new ArrayList<>();
		List<String> newAppList = new ArrayList<>();
		List<String> vastAppList = new ArrayList<>();

		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where("isIT").is(true));
			vastAppList = mongoOperations.getCollection("vast").distinct("vastApplID", basicQuery.getQueryObject());

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
			LOG.info("Error in getEntireAppListForBuild() (BuildDetailsDAO Class)" + e);
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

			if (result != null) {
				finalList = getFinalList(result);
			}
			return finalList;

		} catch (Exception e) {
			LOG.info("Error in getListOfModules() (BuildDetailsDAO Class)" + e);
			return finalList;
		}
	}

	private List<String> getFinalList(List<String> result) {
		List<String> finalList = new ArrayList();
		for (String jobUrl : result) {
			if (jobUrl != null && jobUrl.toLowerCase().contains("build")) {
				finalList.add(jobUrl);
			}
		}
		return finalList;
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
	public List<Build> getBuildsForModule(String appId, String jobUrl, Long startDate, Long endDate,
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
			LOG.info("Error in getBuildsForModule() (BuildDetailsDAO Class)" + e);
		}
		return result;
	}
}
