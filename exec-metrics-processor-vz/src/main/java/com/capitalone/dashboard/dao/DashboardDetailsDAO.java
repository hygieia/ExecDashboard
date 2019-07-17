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
import com.capitalone.dashboard.exec.model.Dashboard;
import com.mongodb.MongoClient;

@Component
public class DashboardDetailsDAO {

	private static final Logger LOG = LoggerFactory.getLogger(DashboardDetailsDAO.class);
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

	public boolean isDashboardAvailable(String appId, MongoClient client) {

		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where(APPID).is(appId));
			List<Dashboard> results = mongoOperations.find(basicQuery, Dashboard.class);
			if (results != null)
				return true;
		} catch (Exception e) {
			LOG.info("Error in isDashboardAvailable() (DashboardDetailsDAO Class)" + e);
		}
		return false;

	}

	public List<String> getConfiguredAppIds(MongoClient client) {

		List<String> confAppIds = new ArrayList<>();

		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where(APPID).exists(true));
			List<Dashboard> results = mongoOperations.find(basicQuery, Dashboard.class);
			if (results != null) {
				for (Dashboard dash : results) {
					confAppIds.add(dash.getAppId());
				}
			}
		} catch (Exception e) {
			LOG.info("Error in getConfiguredAppIds() (DashboardDetailsDAO Class)" + e);
		}
		return confAppIds;
	}

	public List<Dashboard> getConfiguredDashboards(MongoClient client) {

		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where(APPID).exists(true));
			List<Dashboard> results = mongoOperations.find(basicQuery, Dashboard.class);
			if (results != null)
				return results;
		} catch (Exception e) {
			LOG.info("Error in getConfiguredAppIds() (DashboardDetailsDAO Class)" + e);
		}
		return null;
	}

	public String getDashboardIp(String appId, MongoClient client) {

		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where(APPID).is(appId));
			Dashboard result = mongoOperations.findOne(basicQuery, Dashboard.class);
			if (result != null) {
				return result.getInstance();
			}
		} catch (Exception e) {
			LOG.info("Error in getDashboardIp() (DashboardDetailsDAO Class)" + e);
		}
		return null;
	}

}
