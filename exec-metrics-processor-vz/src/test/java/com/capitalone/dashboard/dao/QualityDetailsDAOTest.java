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
public class QualityDetailsDAOTest {

	@InjectMocks
	private QualityDetailsDAO qualityDetailsDAOTest;
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
		qualityDetailsDAOTest.getMongoClient();
	}

	@Test
	public void testGetMongoClient_1() {
		Mockito.when(qualityDetailsDAOTest.getMongoClient()).thenThrow(new NullPointerException());
		qualityDetailsDAOTest.getMongoClient();
	}

	@Test
	public void testGetDefectsList() throws Exception {
		List<FeatureUserStory> featureUserStoryList = new ArrayList<>();
		FeatureUserStory featureUserStory = new FeatureUserStory();
		featureUserStory.setsTeamName("B6LV");
		featureUserStoryList.add(featureUserStory);
		Query basicQuery = new Query();
		basicQuery.addCriteria(new Criteria().andOperator(Criteria.where("sTeamName").is("B6LV"),
				Criteria.where("creationDate").lte("formattedDate" + "23:59:59.999Z")
						.gte("formattedDate" + "00:00:00.000Z"),
				new Criteria().orOperator(Criteria.where("testPhase").is("Production"),
						Criteria.where("mEnvironment").is("Production"), Criteria.where("dspEnvironment").is("prod"),
						Criteria.where("sdlcEnvironment").is("Prod")),
				Criteria.where("resolutionType").in("Broken Code", "Code", "Config", null, ""),
				Criteria.where("sTypeName").is("Bug")));
		MongoClient mongoClient = mock(MongoClient.class);
		Mockito.when(qualityDetailsDAOTest.getMongoClient()).thenReturn(mongoClient);
		qualityDetailsDAOTest.getDefectsList("B6LV", "formattedDate", mongoClient);
	}

	@Test
	public void testGetLatestUserStorySorted() throws Exception {
		List<FeatureUserStory> featureUserStoryList = new ArrayList<>();
		FeatureUserStory featureUserStory = new FeatureUserStory();
		featureUserStory.setsTeamName("B6LV");
		featureUserStoryList.add(featureUserStory);
		Query basicQuery = new Query();
		basicQuery.addCriteria(new Criteria().andOperator(Criteria.where("sTeamName").is("B6LV"),
				Criteria.where("creationDate").lte("formattedDate" + "23:59:59.999Z")
						.gte("formattedDate" + "00:00:00.000Z"),
				new Criteria().orOperator(Criteria.where("testPhase").is("Production"),
						Criteria.where("mEnvironment").is("Production"), Criteria.where("dspEnvironment").is("prod"),
						Criteria.where("sdlcEnvironment").is("Prod")),
				Criteria.where("resolutionType").in("Broken Code", "Code", "Config", null, ""),
				Criteria.where("sTypeName").is("Bug")));
		MongoClient mongoClient = mock(MongoClient.class);
		Mockito.when(qualityDetailsDAOTest.getMongoClient()).thenReturn(mongoClient);
		qualityDetailsDAOTest.getLatestUserStorySorted("B6LV", mongoClient);
	}

	@Test
	public void testIsDataAvailableForQuality() throws Exception {
		List<FeatureUserStory> featureUserStoryList = new ArrayList<>();
		FeatureUserStory featureUserStory = new FeatureUserStory();
		featureUserStory.setsTeamName("B6LV");
		featureUserStoryList.add(featureUserStory);
		Query basicQuery = new Query();
		basicQuery.addCriteria(new Criteria().andOperator(Criteria.where("sTeamName").is("B6LV"),
				Criteria.where("creationDate").lte("formattedDate" + "23:59:59.999Z")
						.gte("formattedDate" + "00:00:00.000Z"),
				new Criteria().orOperator(Criteria.where("testPhase").is("Production"),
						Criteria.where("mEnvironment").is("Production"), Criteria.where("dspEnvironment").is("prod"),
						Criteria.where("sdlcEnvironment").is("Prod")),
				Criteria.where("resolutionType").in("Broken Code", "Code", "Config", null, ""),
				Criteria.where("sTypeName").is("Bug")));
		MongoClient mongoClient = mock(MongoClient.class);
		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(mongoClient)).thenReturn(mongoOps);
		Mockito.when(qualityDetailsDAOTest.getMongoClient()).thenReturn(mongoClient);
		qualityDetailsDAOTest.isDataAvailableForQuality("B6LV", "formattedDate", mongoClient);
	}

	public void testIsDataAvailableForQuality_1() throws Exception {
		Mockito.when(qualityDetailsDAOTest.isDataAvailableForQuality("B6LV", "formattedDate", mongoClient))
				.thenThrow(new NullPointerException());
		qualityDetailsDAOTest.isDataAvailableForQuality("B6LV", "formattedDate", mongoClient);
	}

	public void testGetDefectsList_1() throws Exception {
		Mockito.when(qualityDetailsDAOTest.getDefectsList("B6LV", "formattedDate", mongoClient))
				.thenThrow(new NullPointerException());
		qualityDetailsDAOTest.getDefectsList("B6LV", "formattedDate", mongoClient);
	}

	@Test
	public void testGetEntireAppList() throws Exception {
		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(mongoClient)).thenReturn(mongoOps);
		qualityDetailsDAOTest.getEntireAppList(mongoClient);
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
		Mockito.when(qualityDetailsDAOTest.getMongoClient()).thenReturn(mongoClient);
		qualityDetailsDAOTest.getEntireProjectList(mongoClient, "B6LV");
	}

	@Test
	public void testprojectsList() throws Exception {
		List<String> appIdList = new ArrayList<>();
		appIdList.add("B6LV");
		appIdList.add("D40V");
		Mockito.when(mongoCollection.distinct(Mockito.anyString())).thenReturn(appIdList);
		Mockito.when(mongoOps.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(mongoClient)).thenReturn(mongoOps);
		qualityDetailsDAOTest.getJiraApps(mongoClient);
	}

	@Test
	public void tetsGetEntireProjectList_1() throws Exception {
		Mockito.when(qualityDetailsDAOTest.getEntireProjectList(mongoClient, "B6LV"))
				.thenThrow(new NullPointerException());
		qualityDetailsDAOTest.getEntireProjectList(mongoClient, "B6LV");
	}

}
