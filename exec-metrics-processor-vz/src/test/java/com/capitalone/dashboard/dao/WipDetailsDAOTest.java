package com.capitalone.dashboard.dao;

import static org.mockito.Mockito.mock;

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
import com.capitalone.dashboard.exec.model.vz.FeatureUserStory;
import com.capitalone.dashboard.exec.model.vz.JiraDetailsFinal;
import com.capitalone.dashboard.exec.model.vz.JiraInfo;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

@RunWith(MockitoJUnitRunner.class)
public class WipDetailsDAOTest {

	@InjectMocks
	private WipDetailsDAO wipDetailsDAOTest;
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
		wipDetailsDAOTest.getMongoClient();
	}

	@Test
	public void testGetMongoClient_1() {
		Mockito.when(wipDetailsDAOTest.getMongoClient()).thenThrow(new NullPointerException());
		wipDetailsDAOTest.getMongoClient();
	}

	@Test
	public void testGetUserStoriesList() throws Exception {
		List<FeatureUserStory> featureUserStoryList = new ArrayList<>();
		FeatureUserStory featureUserStory = new FeatureUserStory();
		featureUserStory.setsTeamName("DevopsCup");
		featureUserStory.setsTypeName("Story");
		featureUserStoryList.add(featureUserStory);
		Query basicQuery = new Query();
		basicQuery.addCriteria(new Criteria().andOperator(Criteria.where("sTeamName").is("DevopsCup"),
				Criteria.where("creationDate").gte(254798714).lte(254798124),
				Criteria.where("sTypeName").in("VZAgile Story", "Story", "Bug", "Task", "Epic"),
				Criteria.where("sStatus").is("In Progress")));
		MongoClient mongoClient = mock(MongoClient.class);
		// Mockito.when(mongoOps.find(basicQuery,
		// FeatureUserStory.class)).thenReturn(featureUserStoryList);
		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(mongoClient)).thenReturn(mongoOps);
		Mockito.when(wipDetailsDAOTest.getMongoClient()).thenReturn(mongoClient);
		wipDetailsDAOTest.getUserStoriesList("DevopsCup", 254798714, 254798124, mongoClient);
	}

	@Test
	public void testGetLatestUserStorySorted() throws Exception {
		List<FeatureUserStory> featureUserStoryList = new ArrayList<>();
		FeatureUserStory featureUserStory = new FeatureUserStory();
		featureUserStory.setsTeamName("DevopsCup");
		featureUserStory.setsTypeName("Story");
		featureUserStoryList.add(featureUserStory);
		// Mockito.when(metricsProcessorConfig.mongo()).thenReturn(mongoClient);
		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(mongoClient)).thenReturn(mongoOps);
		Query basicQuery = new Query();
		basicQuery.addCriteria(new Criteria().andOperator(Criteria.where("sTeamName").is("DevopsCup"),
				Criteria.where("creationDate").gte(254798714).lte(254798124),
				Criteria.where("sTypeName").in("VZAgile Story", "Story", "Bug", "Task", "Epic"),
				Criteria.where("sStatus").is("In Progress")));
		// Mockito.when(mongoOps.find(basicQuery,
		// FeatureUserStory.class)).thenReturn(null);
		wipDetailsDAOTest.getLatestUserStorySorted("DevopsCup", mongoClient);
	}

	@Test
	public void testIsDashboardAvailable_1() throws Exception {
		Mockito.when(wipDetailsDAOTest.getUserStoriesList("DevopsCup", 254798714, 254798124, mongoClient))
				.thenThrow(new NullPointerException());
		wipDetailsDAOTest.getUserStoriesList("DevopsCup", 254798714, 254798124, mongoClient);
	}

	@Test
	public void testGetEntireAppList() throws Exception {
		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(mongoClient)).thenReturn(mongoOps);
		wipDetailsDAOTest.getEntireAppList(mongoClient);
	}

	@Test
	public void testGetEntireProjectList() throws Exception {
		List<JiraDetailsFinal> rtList = new ArrayList<>();
		List<JiraInfo> jiraInfoList = new ArrayList<>();
		JiraDetailsFinal jiraDetailsFinalObject = new JiraDetailsFinal();
		JiraInfo jiraInfoObject = new JiraInfo();
		jiraInfoObject.setProjectKey("test Project Key");
		jiraInfoObject.setProjectName("projectNAme");
		jiraInfoObject.setTeamId("teamID");
		jiraInfoList.add(jiraInfoObject);
		jiraDetailsFinalObject.setAppId("B6LV");
		jiraDetailsFinalObject.setJiraInfo(jiraInfoList);
		rtList.add(jiraDetailsFinalObject);
		Query basicQuery = new Query();
		basicQuery.addCriteria(Criteria.where("appId").is("B6LV"));
		Mockito.when(mongoOps.find(basicQuery, JiraDetailsFinal.class)).thenReturn(rtList);
		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(mongoClient)).thenReturn(mongoOps);
		Mockito.when(wipDetailsDAOTest.getMongoClient()).thenReturn(mongoClient);
		wipDetailsDAOTest.getEntireProjectList(mongoClient, "B6LV");
	}

	@Test
	public void testprojectsList() throws Exception {
		List<String> appIdList = new ArrayList<>();
		appIdList.add("B6LV");
		appIdList.add("D40V");
		Mockito.when(mongoCollection.distinct(Mockito.anyString())).thenReturn(appIdList);
		Mockito.when(mongoOps.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(mongoClient)).thenReturn(mongoOps);
		wipDetailsDAOTest.getTotalList(mongoClient);
	}

	@Test
	public void tetsGetEntireProjectList_1() throws Exception {
		Mockito.when(wipDetailsDAOTest.getEntireProjectList(mongoClient, "B6LV")).thenThrow(new NullPointerException());
		wipDetailsDAOTest.getEntireProjectList(mongoClient, "B6LV");
	}

}
