package com.capitalone.dashboard.dao;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.capitalone.dashboard.exec.model.MTTR;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

@RunWith(MockitoJUnitRunner.class)
public class ProductionIncidentsDAOTest {

	@InjectMocks
	private ProductionIncidentsDAO productionIncidentsDAO;
	@Mock
	private MetricsProcessorConfig metricsProcessorConfig;
	@Mock
	private MongoTemplate mongoTemplate;
	@Mock
	private MongoClient mongoClient;
	@Mock
	private DBCollection mongoCollection;

	@Mock
	private MongoOperations mongoOperations;

	@Test
	public void testGetMongoClient() throws Exception {
		Mockito.when(metricsProcessorConfig.mongo()).thenReturn(mongoClient);
		productionIncidentsDAO.getMongoClient();
	}

	@Test
	public void testGetMongoClient_1() throws Exception {
		Mockito.when(metricsProcessorConfig.mongo()).thenThrow(new Exception());
		productionIncidentsDAO.getMongoClient();
	}

	@Test
	public void testGetProductionIncidentsDataByAppId() {
		try {
			Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(Mockito.any(MongoClient.class)))
					.thenReturn(mongoTemplate);
			productionIncidentsDAO.getProductionIncidentsDataByAppId("B6LV", mongoClient);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetProductionIncidentsDataByAppId_1() {
		try {
			Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(Mockito.any(MongoClient.class)))
					.thenThrow(new Exception());
			productionIncidentsDAO.getProductionIncidentsDataByAppId("B6LV", mongoClient);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetAppIdList() {
		try {
			Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(Mockito.any(MongoClient.class)))
					.thenReturn(mongoTemplate);
			Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
			productionIncidentsDAO.getAppIdList(mongoClient);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetAppIdList_1() {
		try {
			Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(Mockito.any(MongoClient.class)))
					.thenThrow(new Exception());
			productionIncidentsDAO.getAppIdList(mongoClient);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetEntireAppList() {
		try {
			Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(Mockito.any(MongoClient.class)))
					.thenReturn(mongoTemplate);
			Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
			productionIncidentsDAO.getEntireAppList(mongoClient);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetEntireAppList_1() {
		try {
			Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(Mockito.any(MongoClient.class)))
					.thenThrow(new Exception());
			productionIncidentsDAO.getEntireAppList(mongoClient);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void getProductionIncidentsDataByAppIdByRegex_1() {
		try {
			Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(Mockito.any(MongoClient.class)))
					.thenThrow(new Exception());
			productionIncidentsDAO.getProductionIncidentsDataByAppIdByRegex("D40V", mongoClient, "2018-05-05");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void getProductionIncidentsDataByAppIdByRegex_2() throws Exception {

		Query basicQuery = new Query();
		basicQuery.addCriteria(Criteria.where("appId").is("D40V"));
		basicQuery.addCriteria(Criteria.where("eventStartDT").gte("2018-05-05"));
		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(Mockito.any(MongoClient.class)))
				.thenReturn(mongoTemplate);
		productionIncidentsDAO.getProductionIncidentsDataByAppIdByRegex("D40V", mongoClient, "2018-05-05");
	}

	@Test
	public void getProductionIncidentsDataByAppId() throws Exception {

		Mockito.when(metricsProcessorConfig.mongo()).thenReturn(mongoClient);
		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(mongoClient)).thenReturn(mongoTemplate);

		Query basicQuery = new Query();
		basicQuery.addCriteria(Criteria.where("appId").is("D40V"));
		basicQuery.addCriteria(Criteria.where("eventStartDT").gte("2018-05-19"));

		productionIncidentsDAO.getMTBFDataforApp("D40V");
	}

	@Test
	public void getMTTRDetails() throws Exception {

		List<String> productionEvents = new ArrayList<>();
		productionEvents.add("CRISIS123");

		Query basicQuery = new Query();
		basicQuery.addCriteria(Criteria.where("crisisId").in(productionEvents));
		Mockito.when(mongoTemplate.find(basicQuery, MTTR.class)).thenReturn(getMTTRLists());

		Mockito.when(metricsProcessorConfig.mongo()).thenReturn(mongoClient);
		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(mongoClient)).thenReturn(mongoTemplate);

		productionIncidentsDAO.getMTTRDetails(productionEvents);
	}

	private List<MTTR> getMTTRLists() {
		MTTR mttr = new MTTR();
		mttr.setAppId("B6LV");
		mttr.setCrisisId("12341234");
		mttr.setCrisisLevel("SEV2");
		mttr.setEventStartDT("asdfasdfasdfaasdf");
		mttr.setItduration(44);
		mttr.setOwningEntity("some entity");
		mttr.setServiceLevel("some level");
		return Arrays.asList(mttr);
	}

	@Test
	public void testGetMTTRDetails_1() throws Exception {
		List<String> productionEvents = new ArrayList<>();
		productionEvents.add("CRISIS123");
		Mockito.when(metricsProcessorConfig.mongo()).thenThrow(new Exception());
		productionIncidentsDAO.getMTTRDetails(productionEvents);
	}

	@Test
	public void testGetProductionIncidentsDataByAppId_3() throws Exception {
		Mockito.when(metricsProcessorConfig.mongo()).thenThrow(new Exception());
		productionIncidentsDAO.getMTBFDataforApp("D40V");
	}
}
