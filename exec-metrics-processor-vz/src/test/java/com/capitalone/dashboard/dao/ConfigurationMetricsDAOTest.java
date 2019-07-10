package com.capitalone.dashboard.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.capitalone.dashboard.collector.MetricsProcessorConfig;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

@RunWith(MockitoJUnitRunner.class)
public class ConfigurationMetricsDAOTest {

	@InjectMocks
	private ConfigurationMetricsDAO configurationMetricsDAOTest;
	@Mock
	MetricsProcessorConfig metricsProcessorConfig;
	@Mock
	private DBCollection mongoCollection;
	@Mock
	MongoTemplate mongoTemplate;
	@Mock
	MongoClient mongoClient;

	@Test
	public void testGetMongoClient() throws Exception {
		Mockito.when(metricsProcessorConfig.mongo()).thenReturn(mongoClient);
		configurationMetricsDAOTest.getMongoClient();
	}

	@Test
	public void testGetMongoClient_1() {
		Mockito.when(configurationMetricsDAOTest.getMongoClient()).thenThrow(new NullPointerException());
		configurationMetricsDAOTest.getMongoClient();
	}

	@Test
	public void testGetConfiguredAppIds() throws Exception {
		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(Mockito.any(MongoClient.class)))
				.thenReturn(mongoTemplate);
		configurationMetricsDAOTest.getConfigurationMetrics("B6LV", mongoClient);
	}

	@Test
	public void testGetConfiguredAppIds_2() throws Exception {
		configurationMetricsDAOTest.getConfigurationMetrics("B6LV", mongoClient);
	}

}
