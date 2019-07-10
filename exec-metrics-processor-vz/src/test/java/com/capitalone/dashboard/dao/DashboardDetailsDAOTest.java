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
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.capitalone.dashboard.collector.MetricsProcessorConfig;
import com.capitalone.dashboard.exec.model.vz.Dashboard;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

@RunWith(MockitoJUnitRunner.class)
public class DashboardDetailsDAOTest {

	@InjectMocks
	private DashboardDetailsDAO dashboardDetailsDAOTest;
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
	@Mock
	MongoTemplate mongoOps;

	@Test
	public void testGetMongoClient() throws Exception {
		Mockito.when(metricsProcessorConfig.mongo()).thenReturn(mongoClient);
		dashboardDetailsDAOTest.getMongoClient();
	}

	@Test
	public void testGetMongoClient_1() {
		Mockito.when(dashboardDetailsDAOTest.getMongoClient()).thenThrow(new NullPointerException());
		dashboardDetailsDAOTest.getMongoClient();
	}

	@Test
	public void testIsDashboardAvailable() throws Exception {
		List<Dashboard> dashboardList = new ArrayList<>();
		Dashboard dashboard = new Dashboard(null, null, null, null, null, null);
		dashboard.setAppId("B6LV");
		dashboardList.add(dashboard);
		Query basicQuery = new Query();
		basicQuery.addCriteria(Criteria.where("appId").is("B6LV"));
		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(Mockito.any(MongoClient.class)))
				.thenReturn(mongoTemplate);
		dashboardDetailsDAOTest.isDashboardAvailable("B6LV", mongoClient);
	}

	@Test
	public void testIsDashboardAvailable_1() throws Exception {
		Mockito.when(dashboardDetailsDAOTest.isDashboardAvailable("B6LV", mongoClient))
				.thenThrow(new NullPointerException());
		dashboardDetailsDAOTest.isDashboardAvailable("B6LV", mongoClient);
	}

	@Test
	public void testGetConfiguredAppIds() throws Exception {
		List<Dashboard> dashboardList = new ArrayList<>();
		Dashboard dashboard = new Dashboard(null, null, null, null, null, null);
		dashboard.setAppId("B6LV");
		dashboardList.add(dashboard);
		Query basicQuery = new Query();
		basicQuery.addCriteria(Criteria.where("appId").exists(true));
		MongoClient mongoClient = mock(MongoClient.class);
		Mockito.when(mongoOps.find(basicQuery, Dashboard.class)).thenReturn(dashboardList);
		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(mongoClient)).thenReturn(mongoOps);
		Mockito.when(dashboardDetailsDAOTest.getMongoClient()).thenReturn(mongoClient);
		dashboardDetailsDAOTest.getConfiguredAppIds(mongoClient);
	}

	@Test
	public void testGetConfiguredAppIds_1() throws Exception {
		Mockito.when(dashboardDetailsDAOTest.getConfiguredAppIds(mongoClient)).thenThrow(new NullPointerException());
		dashboardDetailsDAOTest.getConfiguredAppIds(mongoClient);
	}

	@Test
	public void testGetConfiguredDashboards() throws Exception {
		List<Dashboard> dashboardList = new ArrayList<>();
		Dashboard dashboard = new Dashboard(null, null, null, null, null, null);
		dashboard.setAppId("B6LV");
		dashboardList.add(dashboard);
		Query basicQuery = new Query();
		basicQuery.addCriteria(Criteria.where("appId").exists(true));
		MongoClient mongoClient = mock(MongoClient.class);
		Mockito.when(mongoOps.find(basicQuery, Dashboard.class)).thenReturn(dashboardList);
		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(mongoClient)).thenReturn(mongoOps);
		Mockito.when(dashboardDetailsDAOTest.getMongoClient()).thenReturn(mongoClient);
		dashboardDetailsDAOTest.getConfiguredDashboards(mongoClient);
	}

	@Test
	public void testGetConfiguredDashboards_1() throws Exception {
		Mockito.when(dashboardDetailsDAOTest.getConfiguredDashboards(mongoClient))
				.thenThrow(new NullPointerException());
		dashboardDetailsDAOTest.getConfiguredDashboards(mongoClient);
	}

	@Test
	public void testGetDashboardIp() throws Exception {
		Dashboard dashboard = new Dashboard(null, null, null, null, null, null);
		dashboard.setAppId("B6LV");
		Query basicQuery = new Query();
		basicQuery.addCriteria(Criteria.where("appId").is("B6LV"));
		MongoClient mongoClient = mock(MongoClient.class);
		Mockito.when(mongoOps.findOne(basicQuery, Dashboard.class)).thenReturn(dashboard);
		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(mongoClient)).thenReturn(mongoOps);
		Mockito.when(dashboardDetailsDAOTest.getMongoClient()).thenReturn(mongoClient);
		dashboardDetailsDAOTest.getDashboardIp("B6LV", mongoClient);
	}

	@Test
	public void testGetDashboardIp_1() throws Exception {
		Mockito.when(dashboardDetailsDAOTest.getDashboardIp("B6LV", mongoClient)).thenThrow(new NullPointerException());
		dashboardDetailsDAOTest.getDashboardIp("B6LV", mongoClient);
	}

}
