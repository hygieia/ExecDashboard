package com.capitalone.dashboard.dao;

import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.capitalone.dashboard.collector.MetricsProcessorConfig;
import com.capitalone.dashboard.exec.model.Collector;
import com.capitalone.dashboard.exec.model.CollectorType;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

@RunWith(MockitoJUnitRunner.class)
public class CollectorDetailsDAOTest {

	@InjectMocks
	private CollectorDetailsDAO collectorDetailsDAOTest;
	@Mock
	MetricsProcessorConfig metricsProcessorConfig;
	@Mock
	private DBCollection mongoCollection;
	@Mock
	MongoTemplate mongoTemplate;
	@Mock
	MongoClient mongoClient;
	@Mock
	MongoTemplate mongoOps;

	@Test
	public void testGetMongoClient() throws Exception {
		Mockito.when(metricsProcessorConfig.mongo()).thenReturn(mongoClient);
		collectorDetailsDAOTest.getMongoClient();
	}

	@Test
	public void testGetMongoClient_1() {
		Mockito.when(collectorDetailsDAOTest.getMongoClient()).thenThrow(new NullPointerException());
		collectorDetailsDAOTest.getMongoClient();
	}

	@Test
	public void testGetConfiguredAppIds() throws Exception {
		MongoClient mongoClient = mock(MongoClient.class);
		Query basicQuery = new Query();
		basicQuery.addCriteria(Criteria.where("collectorType").is(CollectorType.MetricsProcessor));
		Collector collector = new Collector();
		collector.setLastExecuted(123456l);
		Mockito.when(mongoOps.findOne(basicQuery, Collector.class)).thenReturn(collector);
		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(mongoClient)).thenReturn(mongoOps);
		collectorDetailsDAOTest.getCollectorLastRun(CollectorType.MetricsProcessor, mongoClient);
	}

	@Test
	public void testGetISODateTime() throws Exception {
		collectorDetailsDAOTest.getISODateTime(12456325441l);
	}

	@Test
	public void testGetISODateTime_1() throws Exception {
		collectorDetailsDAOTest.getISODateTime(null);
	}

}
