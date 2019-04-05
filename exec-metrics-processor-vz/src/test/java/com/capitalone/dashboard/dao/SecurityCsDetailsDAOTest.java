package com.capitalone.dashboard.dao;

import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Date;
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
import com.capitalone.dashboard.exec.model.vz.ExecutiveSecurityData;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

@RunWith(MockitoJUnitRunner.class)
public class SecurityCsDetailsDAOTest {

	@Mock
	private MongoTemplate mongoTemplate;
	@Mock
	private MongoClient mongoClient;
	@Mock
	private DBCollection mongoCollection;
	@Mock
	private MongoOperations mongoOperations;

	@Mock
	MetricsProcessorConfig metricsProcessorConfig;

	@InjectMocks
	SecurityCsDetailsDAO securityDetailsDao;

	@Test
	public void getMongoClientTest() {
		try {
			Mockito.when(metricsProcessorConfig.mongo()).thenReturn(mongoClient);
			securityDetailsDao.getMongoClient();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void getMongoClientTest2() {

		try {
			Mockito.when(metricsProcessorConfig.mongo()).thenThrow(new NullPointerException());
			securityDetailsDao.getMongoClient();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void getSecurityDataTest2() {

		try {
			List<String> appIds = new ArrayList<>();
			String appId = "hello";
			appIds.add(appId);
			long timeStamp = 0;

			Date d = new Date(1220227200);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where("appId").is("hello"));
			basicQuery.addCriteria(Criteria.where("timestamp").gt(timeStamp));

			Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(mongoClient)).thenReturn(mongoTemplate);

			securityDetailsDao.getSecurityData(appId, mongoClient, timeStamp);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void getSecurityDataTest() {
		try {
			MongoClient mongoClient = mock(MongoClient.class);
			List<String> appIds = new ArrayList<>();
			appIds.add("hello");
			long timeStamp = 0;

			Date d = new Date(1220227200);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where("appId").is("hello"));
			basicQuery.addCriteria(Criteria.where("timestamp").gt(timeStamp - 7776000000l));

			List<ExecutiveSecurityData> secDataList = new ArrayList<>();
			ExecutiveSecurityData secData = new ExecutiveSecurityData();

			secData.setAppId("hello");
			secData.setDatewise(null);
			secData.setScanBlockerValue(3);
			secData.setScanCriticalValue(5);
			secData.setScanMajorValue(10);
			secData.setSecurityUrl("www.google.com");
			secData.setTimestamp(0);
			secData.setTotalBlockerValue(45);
			secData.setTotalCriticalValue(50);
			secData.setTotalMajorValue(100);
			secData.setUniqueId(null);
			secData.setVulnerBlockerValue(10);
			secData.setVulnerCriticalValue(10);
			secData.setVulnerMajorValue(10);
			secData.setWebBlockerValue(20);
			secData.setWebCriticalValue(25);
			secData.setWebMajorValue(30);
			secData.setTotalCriticalOverdue(15);
			secData.setWebCriticalOverdue(14);
			secData.setWebMediumOverdue(25);
			secData.setWebHighOverdue(16);
			secData.setVulnerCriticalOverdue(48);
			secData.setVulnerHighOverdue(74);
			secData.setVulnerMediumOverdue(49);
			secData.setTotalHighOverdue(54);
			secData.setTotalMediumOverdue(98);

			ExecutiveSecurityData secData2 = new ExecutiveSecurityData();

			secData2.setAppId("hel");
			secData2.setDatewise(null);
			secData2.setScanBlockerValue(3);
			secData2.setScanCriticalValue(5);
			secData2.setScanMajorValue(10);
			secData2.setSecurityUrl("www.google.com");
			secData2.setTimestamp(2);
			secData2.setTotalBlockerValue(45);
			secData2.setTotalCriticalValue(50);
			secData2.setTotalMajorValue(100);
			secData2.setUniqueId(null);
			secData2.setVulnerBlockerValue(10);
			secData2.setVulnerCriticalValue(10);
			secData2.setVulnerMajorValue(10);
			secData2.setWebBlockerValue(20);
			secData2.setWebCriticalValue(25);
			secData2.setWebMajorValue(30);
			secData2.setTotalCriticalOverdue(15);
			secData2.setWebCriticalOverdue(14);
			secData2.setWebMediumOverdue(25);
			secData2.setWebHighOverdue(16);
			secData2.setVulnerCriticalOverdue(48);
			secData2.setVulnerHighOverdue(74);
			secData2.setVulnerMediumOverdue(49);
			secData2.setTotalHighOverdue(54);
			secData2.setTotalMediumOverdue(98);

			secDataList.add(secData);
			secDataList.add(secData2);

			// Mockito.when(mongoTemplate.find(basicQuery,
			// ExecutiveSecurityData.class)).thenReturn(secDataList);

			Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(mongoClient)).thenReturn(mongoTemplate);

			securityDetailsDao.getSecurityData("hello", mongoClient, timeStamp);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void getConfiguredAppIdsTest() {
		try {

			List<String> appIds = new ArrayList<>();
			appIds.add("hello");

			Mockito.when(mongoCollection.distinct(Mockito.anyString())).thenReturn(appIds);

			Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
			Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(Mockito.any(MongoClient.class)))
					.thenReturn(mongoTemplate);
			securityDetailsDao.getConfiguredAppIds(mongoClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void getConfiguredAppIdsTest2() {
		try {

			List<String> appIds = new ArrayList<>();
			appIds.add("hello");

			Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(Mockito.any(MongoClient.class)))
					.thenThrow(new NullPointerException());
			securityDetailsDao.getConfiguredAppIds(mongoClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void convertDaysAgoTest() {
		try {

			long timeStamp = 1234567l;

			securityDetailsDao.convertToDaysAgo(timeStamp);
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

}
