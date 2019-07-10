package com.capitalone.dashboard.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.capitalone.dashboard.collector.MetricsProcessorConfig;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

@RunWith(MockitoJUnitRunner.class)
public class VastDetailsDAOTest {

	@InjectMocks
	private VastDetailsDAO vastDetailsDAO;
	@Mock
	MetricsProcessorConfig metricsProcessorConfig;
	@Mock
	private DBCollection mongoCollection;
	@Mock
	MongoTemplate mongoTemplate;
	@Mock
	MongoOperations mongoOperations;
	@Mock
	MongoClient mongoClient;

	private static final String ISIT = "isIT";
	private static final String APPID = "vastApplID";
	private static final String VAST = "vast";

	@Test
	public void testGetMongoClient() throws Exception {
		Mockito.when(metricsProcessorConfig.mongo()).thenReturn(mongoClient);
		vastDetailsDAO.getMongoClient();
	}

	@Test
	public void testGetMongoClient_1() {
		Mockito.when(vastDetailsDAO.getMongoClient()).thenThrow(new NullPointerException());
		vastDetailsDAO.getMongoClient();
	}

	@Test
	public void testGetConfiguredAppIds() throws Exception {
		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(Mockito.any(MongoClient.class)))
				.thenReturn(mongoTemplate);
		vastDetailsDAO.getMappingVastId(mongoClient);
	}

	@Test
	public void testGetConfiguredAppIds_2() throws Exception {
		vastDetailsDAO.getMappingVastId(mongoClient);
	}

	@Test
	public void testGetVastEids_1() throws Exception {
		vastDetailsDAO.getVastEids(mongoClient, 1);
		vastDetailsDAO.getVastEids(mongoClient, 2);
		vastDetailsDAO.getVastEids(mongoClient, 4);
		vastDetailsDAO.getVastEids(mongoClient, 3);
		vastDetailsDAO.getVastEids(mongoClient, 5);
		vastDetailsDAO.getVastEids(mongoClient, 6);
		vastDetailsDAO.getVastEids(mongoClient, 7);
		vastDetailsDAO.getVastEids(mongoClient, 10);
	}

	@Test
	public void testGetVastEidsForEid_1() throws Exception {
		vastDetailsDAO.getVastEidsForEid(mongoClient, 1, "23434533");
		vastDetailsDAO.getVastEidsForEid(mongoClient, 2, "23434533");
		vastDetailsDAO.getVastEidsForEid(mongoClient, 4, "23434533");
		vastDetailsDAO.getVastEidsForEid(mongoClient, 3, "23434533");
		vastDetailsDAO.getVastEidsForEid(mongoClient, 5, "23434533");
		vastDetailsDAO.getVastEidsForEid(mongoClient, 6, "23434533");
		vastDetailsDAO.getVastEidsForEid(mongoClient, 7, "23434533");
		vastDetailsDAO.getVastEidsForEid(mongoClient, 10, "23434533");
	}

	@Test
	public void testGetVastEidsForEid_2() throws Exception {
		Query basicQuery = new Query();
		basicQuery.addCriteria(Criteria.where("isIT").is(true));
		basicQuery.addCriteria(Criteria.where("vastTierTwoContactEid").is("23434533"));
		Mockito.when(mongoCollection.distinct("vastTierThreeContactEid", basicQuery.getQueryObject())).thenReturn(null);
		Mockito.when(mongoTemplate.getCollection("vast")).thenReturn(mongoCollection);
		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(mongoClient)).thenReturn(mongoTemplate);
		vastDetailsDAO.getVastEidsForEid(mongoClient, 1, "23434533");
		vastDetailsDAO.getVastEidsForEid(mongoClient, 2, "23434533");
		vastDetailsDAO.getVastEidsForEid(mongoClient, 4, "23434533");
		vastDetailsDAO.getVastEidsForEid(mongoClient, 3, "23434533");
		vastDetailsDAO.getVastEidsForEid(mongoClient, 5, "23434533");
		vastDetailsDAO.getVastEidsForEid(mongoClient, 6, "23434533");
		vastDetailsDAO.getVastEidsForEid(mongoClient, 7, "23434533");
		vastDetailsDAO.getVastEidsForEid(mongoClient, 10, "23434533");
	}

	@Test
	public void getAllAppIds() throws Exception {
		Query basicQuery = new Query();
		basicQuery.addCriteria(Criteria.where(ISIT).is(true));
		Mockito.when(mongoCollection.distinct(APPID, basicQuery.getQueryObject())).thenReturn(null);
		Mockito.when(mongoTemplate.getCollection("vast")).thenReturn(mongoCollection);
		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(mongoClient)).thenReturn(mongoTemplate);
		vastDetailsDAO.getAllAppIds(mongoClient);
	}

	@Test
	public void getAllAppIds_1() throws Exception {
		Query basicQuery = new Query();
		basicQuery.addCriteria(Criteria.where(ISIT).is(true));
		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(mongoClient)).thenReturn(mongoTemplate);
		vastDetailsDAO.getAllAppIds(mongoClient);
	}

	@Test
	public void getAppCountTest() throws Exception {
		String collectorNames[] = { "Security", "Velocity", "Total Stories", "WIP", "Quality", "Production Incidents",
				"Stash", "Cloud", "Test", "Build", "Deploy", "Say Do", "Error", "Through Put" };
		for (String name : collectorNames) {
			vastDetailsDAO.getAppCount(name);
		}

	}

}
