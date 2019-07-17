package com.capitalone.dashboard.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.collector.MetricsProcessorConfig;
import com.capitalone.dashboard.exec.model.ConfigurationMetrics;
import com.mongodb.MongoClient;

@Component
public class ConfigurationMetricsDAO {
	private static final Logger LOG = LoggerFactory.getLogger(ConfigurationMetricsDAO.class);

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

	public ConfigurationMetrics getConfigurationMetrics(String appId, MongoClient client) {
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where("vastId").is(appId));
			ConfigurationMetrics result = mongoOperations.findOne(basicQuery, ConfigurationMetrics.class);
			if (result != null) {
				return result;
			}
		} catch (Exception e) {
			LOG.info("Error in getConfigurationMetrics() (ConfigurationMetricsDAO Class)" + e);
		}
		return null;
	}

}
