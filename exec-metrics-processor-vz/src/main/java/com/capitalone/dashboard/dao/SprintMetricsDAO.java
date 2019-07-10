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
import com.capitalone.dashboard.exec.model.vz.SprintMetrics;
import com.mongodb.MongoClient;

/**
 * 
 * @author asthaaa
 *
 */
@Component
public class SprintMetricsDAO {

	private static final Logger LOG = LoggerFactory.getLogger(SprintMetricsDAO.class);
	private static final String ENDDATE = "endDate";

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
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where("isIT").is(true));
			results = mongoOperations.getCollection("vast").distinct("vastApplID", basicQuery.getQueryObject());
		} catch (Exception e) {
			LOG.info("Error in getMappingVastId() (DeployDetailsDAO Class)" + e);
		}
		return results;
	}

	/**
	 * 
	 * @param client
	 * @param appId
	 * @param timestamp
	 * @return
	 */
	public List<String> getListOfModules(MongoClient client, String appId, long timestamp) {
		List<String> result = new ArrayList();
		List<String> resultNew = new ArrayList();
		List<String> resultFinal = new ArrayList();
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where("appId").is(appId));
			result = mongoOperations.getCollection("jira_final_list").distinct("jiraInfo.projectKey",
					basicQuery.getQueryObject());

			for (String projectKey : result) {
				basicQuery = new Query();
				basicQuery.addCriteria(Criteria.where("projectKey").is(projectKey)
						.andOperator(Criteria.where(ENDDATE).gte(timestamp)));
				resultNew = mongoOperations.getCollection("sprint_metrics").distinct("sprintId",
						basicQuery.getQueryObject());
				resultFinal.addAll(resultNew);
			}
		} catch (Exception e) {
			LOG.info("Error in getListOfModules() (SprintMetricsDAO Class)" + e);
		}
		return resultFinal;
	}

	/**
	 * 
	 * @param module
	 * @param startDate
	 * @param endDate
	 * @param client
	 * @return
	 */
	public List<SprintMetrics> getDataForModule(String module, Long startDate, long endDate, MongoClient client) {
		List<SprintMetrics> result = new ArrayList();
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(new Criteria().andOperator(Criteria.where("sprintId").is(module),
					Criteria.where(ENDDATE).gte(startDate), Criteria.where(ENDDATE).lte(endDate)));

			result = mongoOperations.find(basicQuery, SprintMetrics.class);
			if (result != null)
				return result;
		} catch (Exception e) {
			LOG.info("Error in getBuildsForModule() (DeployDetailsDAO Class)" + e);
		}
		return result;
	}

}
