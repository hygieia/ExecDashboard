package com.capitalone.dashboard.dao;

import java.util.HashMap;
import java.util.Map;

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
import com.capitalone.dashboard.exec.model.BunitCredentials;
import com.capitalone.dashboard.exec.model.Dashboard;
import com.capitalone.dashboard.exec.model.ProductPipelineData;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

@RunWith(MockitoJUnitRunner.class)
public class ThroughPutDAOTest {
	@InjectMocks
	private ThroughPutDAO throughPutDAO;
	@Mock
	MetricsProcessorConfig metricsProcessorConfig;
	@Mock
	private DBCollection mongoCollection;
	@Mock
	MongoTemplate mongoTemplate;
	@Mock
	MongoClient mongoClient;
	@Mock
	DBCollection collection;

	@Test
	public void testGetMongoClient() throws Exception {
		Mockito.when(metricsProcessorConfig.mongo()).thenReturn(mongoClient);
		throughPutDAO.getMongoClient();
	}

	@Test
	public void testGetMongoClient_1() {
		Mockito.when(throughPutDAO.getMongoClient()).thenThrow(new NullPointerException());
		throughPutDAO.getMongoClient();
	}

	@Test
	public void testGetDbDetails() throws Exception {
		Dashboard dashboard = new Dashboard(null, null, null, null, "B6LV", null);
		BunitCredentials dbCredentials = new BunitCredentials();
		dbCredentials.setDbName("someName");
		dbCredentials.setDbUserCredentials("frdfrd");
		dbCredentials.setDbUserName("some name");
		dbCredentials.setHost("host");
		dbCredentials.setPort(27017);
		dashboard.setDbCredentials(dbCredentials);
		Query basicQuery = new Query();
		basicQuery.addCriteria(Criteria.where("appId").is("B6LV"));
		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(Mockito.any(MongoClient.class)))
				.thenReturn(mongoTemplate);
		// Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoTemplate.findOne(basicQuery, Dashboard.class)).thenReturn(dashboard);
		throughPutDAO.getDbDetails("B6LV", (mongoClient));
	}

	@Test
	public void testGetRemainingModuleList() throws Exception {

		Query basicQuery = new Query();
		basicQuery.addCriteria(Criteria.where("appId").is("B6LV"));
		// Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(Mockito.any(MongoClient.class))).thenReturn(mongoTemplate);
		// Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		/*
		 * BasicDBObject query = new BasicDBObject(); query.put("type",
		 * "Product"); Mockito.when(collection.find(query)).thenReturn(value);
		 */
		Map<String, String> dbDetails = new HashMap<>();
		dbDetails.put("dbname", "dbname");
		dbDetails.put("dbhost", "host");
		dbDetails.put("dbport", String.valueOf(27017));
		dbDetails.put("dbusername", "username");
		dbDetails.put("dbpassword", "pwd");
		throughPutDAO.getRemainingModuleList(dbDetails);
	}

	/*
	 * @Test public void testGetRemainingModuleList_1() throws Exception {
	 * 
	 * Query basicQuery = new Query();
	 * basicQuery.addCriteria(Criteria.where("appId").is("B6LV"));
	 * Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(Mockito.any(
	 * MongoClient.class))).thenReturn(mongoTemplate);
	 * Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn
	 * (mongoCollection);
	 * 
	 * Map<String, String> dbDetails = new HashMap<>();
	 * dbDetails.put("dbname","dbname"); dbDetails.put("dbhost","host");
	 * dbDetails.put("dbport",String.valueOf(27017));
	 * dbDetails.put("dbusername","username");
	 * dbDetails.put("dbpassword","pwd");
	 * Mockito.when(throughPutDAO.getRemainingModuleList(Mockito.any(Map.class))
	 * ).thenThrow(new Exception());
	 * throughPutDAO.getRemainingModuleList(dbDetails); }
	 */

	@Test
	public void testGetDbDetailsOne() throws Exception {
		Mockito.when(throughPutDAO.getDbDetails("B6LV", (mongoClient))).thenThrow(new Exception());
		throughPutDAO.getDbDetails("B6LV", (mongoClient));
	}

	@Test
	public void testGetConfiguredAppIds() throws Exception {
		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(Mockito.any(MongoClient.class)))
				.thenReturn(mongoTemplate);
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		throughPutDAO.getConfiguredAppIds(mongoClient);
	}

	@Test
	public void testGetConfiguredAppIds_1() throws Exception {
		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(Mockito.any(MongoClient.class)))
				.thenReturn(mongoTemplate);
		throughPutDAO.getConfiguredAppIds(mongoClient);
	}

	@Test
	public void testGetConfiguredAppIds_2() throws Exception {
		throughPutDAO.getConfiguredAppIds(mongoClient);
	}

	@Test
	public void testGetByAppId() throws Exception {
		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(Mockito.any(MongoClient.class)))
				.thenReturn(mongoTemplate);
		throughPutDAO.getByAppId("B6LV", mongoClient);
	}

	@Test
	public void testGetByAppId_1() throws Exception {
		ProductPipelineData productPipelineData = new ProductPipelineData();
		productPipelineData.setAppId("B6LV");
		Query basicQuery = new Query();
		basicQuery.addCriteria(Criteria.where("appId").is("B6LV"));
		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(Mockito.any(MongoClient.class)))
				.thenReturn(mongoTemplate);
		Mockito.when(mongoTemplate.findOne(basicQuery, ProductPipelineData.class)).thenReturn(productPipelineData);
		throughPutDAO.getByAppId("B6LV", mongoClient);
	}

	@Test
	public void testGetByAppId_2() {
		Mockito.when(throughPutDAO.getByAppId("B6LV", mongoClient)).thenThrow(new NullPointerException());
		throughPutDAO.getByAppId("B6LV", mongoClient);
	}

	@Test
	public void testGetEntireAppList() throws Exception {
		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(Mockito.any(MongoClient.class)))
				.thenReturn(mongoTemplate);
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		throughPutDAO.getEntireAppList(mongoClient);
	}

	@Test
	public void testGetEntireAppList_1() {
		throughPutDAO.getEntireAppList(mongoClient);
	}

}