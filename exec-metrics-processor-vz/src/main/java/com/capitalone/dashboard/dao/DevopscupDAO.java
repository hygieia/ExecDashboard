package com.capitalone.dashboard.dao;

import java.util.ArrayList;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.collector.MetricsProcessorConfig;
import com.capitalone.dashboard.exec.model.vz.DevOpsCupScores;
import com.mongodb.MongoClient;

/**
 * Devopcup DAO details
 * 
 * @author RHE94MG
 *
 */
@Component
public class DevopscupDAO {

	public static final Logger LOG = LoggerFactory.getLogger(DevopscupDAO.class);

	@Autowired
	MetricsProcessorConfig metricsProcessorConfig;

	/**
	 * getMongoClient()
	 * 
	 * @param
	 * @return MongoClient
	 */
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
	 * 
	 * @param client
	 * @return List
	 */
	public List<DevOpsCupScores> getAllDetails(MongoClient client) {
		MongoOperations mongoOperations;
		try {
			mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);

			return mongoOperations.findAll(DevOpsCupScores.class);

		} catch (Exception e) {
			LOG.info("Error in getAllDetails() (DevopscupDAO Class)" + e);
		}
		return new ArrayList<>();
	}
}
