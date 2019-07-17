package com.capitalone.dashboard.dao;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.collector.MetricsProcessorConfig;
import com.capitalone.dashboard.exec.model.Collector;
import com.capitalone.dashboard.exec.model.CollectorType;
import com.mongodb.MongoClient;

@Component
/**
 * 
 * @author rhe94mg
 *
 */
public class CollectorDetailsDAO {
	private static final Logger LOG = LoggerFactory.getLogger(CollectorDetailsDAO.class);

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
	 * getCollectorLastRun
	 * 
	 * @param type
	 * @param client
	 * @return Date
	 */
	public Date getCollectorLastRun(CollectorType type, MongoClient client) {
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where("collectorType").is(type));
			Collector result = mongoOperations.findOne(basicQuery, Collector.class);
			if (result != null) {
				return getISODateTime(result.getLastExecuted());
			}
		} catch (Exception e) {
			LOG.info("Error in getConfigurationMetrics() (ConfigurationMetricsDAO Class)" + e);
		}
		return null;
	}

	/**
	 * getCollectorLastRun()
	 * 
	 * @param lastScanned
	 * @return Date
	 */
	public Date getISODateTime(Long lastScanned) {
		if (lastScanned == null)
			return null;
		return new Date(lastScanned);
	}

	/**
	 * getCollectorLastRunInLong
	 * 
	 * @param type
	 * @param client
	 * @return Long
	 */
	public Long getCollectorLastRunInLong(CollectorType type, MongoClient client) {
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where("collectorType").is(type));
			Collector result = mongoOperations.findOne(basicQuery, Collector.class);
			if (result != null) {
				return result.getLastExecuted();
			}
		} catch (Exception e) {
			LOG.info("Error in getConfigurationMetrics() (ConfigurationMetricsDAO Class)" + e);
		}
		return 0L;
	}

}
