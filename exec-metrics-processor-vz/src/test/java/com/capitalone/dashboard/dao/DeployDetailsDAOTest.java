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
import com.capitalone.dashboard.exec.model.BuildStatus;
import com.capitalone.dashboard.exec.model.DeployMetrics;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

@RunWith(MockitoJUnitRunner.class)
public class DeployDetailsDAOTest {

	@InjectMocks
	private DeployDetailsDAO deployDetailsDAOTest;
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
		deployDetailsDAOTest.getMongoClient();
	}

	@Test
	public void testGetMongoClient_2() {
		Mockito.when(deployDetailsDAOTest.getMongoClient()).thenThrow(new NullPointerException());
		deployDetailsDAOTest.getMongoClient();
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

		deployDetailsDAOTest.getEntireAppListFromBuild(mongoClient, 124523612311l);
	}

	@Test
	public void testGetEntireAppListFromBuild_1() {
		Mockito.when(deployDetailsDAOTest.getEntireAppListFromBuild(mongoClient, 124523612311l))
				.thenThrow(new NullPointerException());
		deployDetailsDAOTest.getEntireAppListFromBuild(mongoClient, 124523612311l);
	}

	@Test
	public void testGetEntireAppListFromDeploy() throws Exception {

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
		Mockito.when(mongoOps.getCollection("deployments")).thenReturn(mongoCollection);

		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(mongoClient)).thenReturn(mongoOps);

		deployDetailsDAOTest.getEntireAppListFromDeploy(mongoClient, 124523612311l);
	}

	@Test
	public void testGetEntireAppListFromDeploy_1() {
		Mockito.when(deployDetailsDAOTest.getEntireAppListFromDeploy(mongoClient, 124523612311l))
				.thenThrow(new NullPointerException());
		deployDetailsDAOTest.getEntireAppListFromDeploy(mongoClient, 124523612311l);
	}

	@Test
	public void testGetListOfModules() throws Exception {

		List<String> appIds = new ArrayList<>();
		appIds.add("JOB.Executive.API.Deploy");
		appIds.add("JOB.Hygieia.API.Deploy");

		Query basicQuery = new Query();
		basicQuery.addCriteria(Criteria.where("appId").is("D40V"));
		basicQuery.addCriteria(Criteria.where("startTime").gte(124523612311l));

		Mockito.when(mongoCollection.distinct("jobUrl", basicQuery.getQueryObject())).thenReturn(appIds);
		Mockito.when(mongoOps.getCollection("builds")).thenReturn(mongoCollection);

		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(mongoClient)).thenReturn(mongoOps);

		deployDetailsDAOTest.getListOfModules(mongoClient, "D40V", 124523612311l);
	}

	@Test
	public void testGetListOfModules_1() {
		Mockito.when(deployDetailsDAOTest.getListOfModules(mongoClient, "D40V", 124523612311l))
				.thenThrow(new NullPointerException());
		deployDetailsDAOTest.getListOfModules(mongoClient, "D40V", 124523612311l);
	}

	@Test
	public void testGetListOfModulesForDeploy() throws Exception {

		List<String> appIds = new ArrayList<>();
		appIds.add("JOB.Executive.API.Deploy");
		appIds.add("JOB.Hygieia.API.Deploy");

		Query basicQuery = new Query();
		basicQuery.addCriteria(Criteria.where("appId").is("D40V"));
		basicQuery.addCriteria(Criteria.where("startTime").gte(124523612311l));

		Mockito.when(mongoCollection.distinct("environmentUrl", basicQuery.getQueryObject())).thenReturn(appIds);
		Mockito.when(mongoOps.getCollection("deployments")).thenReturn(mongoCollection);

		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(mongoClient)).thenReturn(mongoOps);

		deployDetailsDAOTest.getListOfModulesForDeploy(mongoClient, "D40V", 124523612311l);
	}

	@Test
	public void testGetListOfModulesForDeploy_1() {
		Mockito.when(deployDetailsDAOTest.getListOfModulesForDeploy(mongoClient, "D40V", 124523612311l))
				.thenThrow(new NullPointerException());
		deployDetailsDAOTest.getListOfModulesForDeploy(mongoClient, "D40V", 124523612311l);
	}

	@Test
	public void testGetDeploymentsForModule() throws Exception {

		List<DeployMetrics> deployMetricsList = new ArrayList<>();

		DeployMetrics deployMetrics = new DeployMetrics();
		deployMetrics.setAppId("D40V");
		deployMetrics.setBuildStatus(BuildStatus.SUCCESS);
		deployMetrics.setEnvironmentUrl(
				"https://onejenkinscloud.vpc./nts/job/JOB.EXECUTIVE_HYGIEIA.API.SONAR_CHECK");

		deployMetricsList.add(deployMetrics);

		Query basicQuery = new Query();
		basicQuery.addCriteria(new Criteria().andOperator(Criteria.where("appId").is("D40V"),
				Criteria.where("environmentUrl")
						.is("https://onejenkinscloud.vpc./nts/job/JOB.EXECUTIVE_HYGIEIA.API.SONAR_CHECK"),
				Criteria.where("startTime").gte(124523612311l).lt(125523612311l)));

		Mockito.when(mongoOps.find(basicQuery, DeployMetrics.class)).thenReturn(deployMetricsList);

		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(mongoClient)).thenReturn(mongoOps);

		deployDetailsDAOTest.getDeploymentsForModule("D40V",
				"https://onejenkinscloud.vpc./nts/job/JOB.EXECUTIVE_HYGIEIA.API.SONAR_CHECK",
				124523612311l, 125523612311l, mongoClient);
	}

	@Test
	public void testGetDeploymentsForModule_1() {
		Mockito.when(deployDetailsDAOTest.getDeploymentsForModule("D40V",
				"https://onejenkinscloud.vpc./nts/job/JOB.EXECUTIVE_HYGIEIA.API.SONAR_CHECK",
				124523612311l, 125523612311l, mongoClient)).thenThrow(new NullPointerException());
		deployDetailsDAOTest.getDeploymentsForModule("D40V",
				"https://onejenkinscloud.vpc./nts/job/JOB.EXECUTIVE_HYGIEIA.API.SONAR_CHECK",
				124523612311l, 125523612311l, mongoClient);
	}

}
