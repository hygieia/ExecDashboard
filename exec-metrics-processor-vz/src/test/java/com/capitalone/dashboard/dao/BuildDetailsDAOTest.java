package com.capitalone.dashboard.dao;

import java.util.ArrayList;
import java.util.List;

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
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

@RunWith(MockitoJUnitRunner.class)
public class BuildDetailsDAOTest {
	
	@InjectMocks
	private BuildDetailsDAO buildDetailsDAOTest;
	@Mock
	private MetricsProcessorConfig metricsProcessorConfig;
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
		buildDetailsDAOTest.getMongoClient();
	}

	@Test
	public void testGetMongoClient_2() {
		Mockito.when(buildDetailsDAOTest.getMongoClient()).thenThrow(new NullPointerException());
		buildDetailsDAOTest.getMongoClient();
	}
	
	@Test
	public void testGetEntireAppListFromBuild() throws Exception {

		List<String> appIds = new ArrayList<>();
		appIds.add("D40V");
		appIds.add("LVMV");

		Query basicQuery = new Query();
		basicQuery.addCriteria(Criteria.where("isIT").is(true));
		Mockito.when(mongoCollection.distinct("vastApplID", basicQuery.getQueryObject())).thenReturn(appIds);
		Mockito.when(mongoOps.getCollection("vast")).thenReturn(mongoCollection);

		Query basicBuildQuery = new Query();
		basicBuildQuery.addCriteria(Criteria.where("startTime").gte(124523612311l));
		Mockito.when(mongoCollection.distinct("appId", basicBuildQuery.getQueryObject())).thenReturn(appIds);
		Mockito.when(mongoOps.getCollection("builds")).thenReturn(mongoCollection);

		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(mongoClient)).thenReturn(mongoOps);

		buildDetailsDAOTest.getEntireAppListForBuild(mongoClient, 124523612311l);
	}

	@Test
	public void testGetEntireAppListFromBuild_1() {
		Mockito.when(buildDetailsDAOTest.getEntireAppListForBuild(mongoClient, 124523612311l))
				.thenThrow(new NullPointerException());
		buildDetailsDAOTest.getEntireAppListForBuild(mongoClient, 124523612311l);
	}

}
