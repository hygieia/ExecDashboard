package com.capitalone.dashboard.collector;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

/**
 * MetricsProcessorConfig extends AbstractMongoConfiguration
 * 
 * @param ...
 * @return
 */
@Component
public class MetricsProcessorConfig extends AbstractMongoConfiguration {
	private static final Logger LOGGER = LoggerFactory.getLogger(MetricsProcessorConfig.class);

	private MetricsProcessorSettings metricsProcessorSettings;

	@Autowired
	public MetricsProcessorConfig(MetricsProcessorSettings metricsProcessorSettings) {
		this.metricsProcessorSettings = metricsProcessorSettings;
	}

	@Override
	protected String getDatabaseName() {
		return metricsProcessorSettings.getDbName();
	}

	@Override
	public MongoClient mongo() throws Exception {

		MongoClient client;

		ServerAddress serverAddr = new ServerAddress(metricsProcessorSettings.getDbHost(),
				metricsProcessorSettings.getDbPort());
		LOGGER.info("Initializing Mongo Client server at: {}", serverAddr);
		if (StringUtils.isEmpty(metricsProcessorSettings.getUserName())) {
			client = new MongoClient(serverAddr);
		} else {
			MongoCredential mongoCredential = MongoCredential.createScramSha1Credential(
					metricsProcessorSettings.getUserName(), metricsProcessorSettings.getDbName(),
					metricsProcessorSettings.getPassword().toCharArray());
			client = new MongoClient(serverAddr, Collections.singletonList(mongoCredential));
		}

		LOGGER.info("Connecting to Mongo: {}", client);
		return client;
	}

	/**
	 * metricsProcessorTemplate
	 * 
	 * @param client
	 * @return MongoTemplate
	 */
	public MongoTemplate metricsProcessorTemplate(MongoClient client) throws Exception {
		return new MongoTemplate(client, getDatabaseName());
	}

}