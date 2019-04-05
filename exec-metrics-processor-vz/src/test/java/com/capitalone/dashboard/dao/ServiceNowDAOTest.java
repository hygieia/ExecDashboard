package com.capitalone.dashboard.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.capitalone.dashboard.collector.MetricsProcessorConfig;
import com.capitalone.dashboard.exec.model.vz.ServiceNowIssues;
import com.capitalone.dashboard.exec.model.vz.ServiceNowTicket;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

@RunWith(MockitoJUnitRunner.class)
public class ServiceNowDAOTest {

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
	ServiceNowDAO serviceNowDAOTest;

	@Test
	public void getMongoClientTest() throws Exception {
		Mockito.when(metricsProcessorConfig.mongo()).thenReturn(mongoClient);
		serviceNowDAOTest.getMongoClient();
	}

	@Test
	public void getMongoClientTest2() throws Exception {
		Mockito.when(metricsProcessorConfig.mongo()).thenThrow(new NullPointerException());
		serviceNowDAOTest.getMongoClient();
	}

	@Test
	public void testMappingVastId() throws Exception {
		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(Mockito.any(MongoClient.class)))
				.thenReturn(mongoTemplate);
		serviceNowDAOTest.getMappingVastId(mongoClient);
	}

	@Test
	public void testMappingVastId_2() throws Exception {
		serviceNowDAOTest.getMappingVastId(mongoClient);
	}

	@Test
	public void testGetServiceNowTickets() throws Exception {

		Query basicQuery = new Query();
		basicQuery.addCriteria(Criteria.where("applicationName").is("D40V"));
		basicQuery.addCriteria(Criteria.where("category").is("Application Software"));
		List<ServiceNowTicket> results = new ArrayList<>();

		ServiceNowTicket ticket = new ServiceNowTicket();
		ticket.setAppName("D40V");
		ticket.setStartdate("2018-04-17 12:00:00");
		ticket.setEnddate("2018-04-18 12:00:00");
		results.add(ticket);

		ticket = new ServiceNowTicket();
		ticket.setAppName("D40V");
		ticket.setStartdate("2018-03-15 12:00:00");
		ticket.setEnddate("2018-03-16 12:00:00");
		results.add(ticket);

		Mockito.when(mongoTemplate.find(basicQuery, ServiceNowTicket.class)).thenReturn(results);
		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(mongoClient)).thenReturn(mongoTemplate);
		serviceNowDAOTest.getServiceNowTickets("D40V", mongoClient);
	}

	@Test
	public void testGetServiceNowTickets_1() throws Exception {

		Query basicQuery = new Query();
		basicQuery.addCriteria(Criteria.where("applicationName").is("D40V"));
		basicQuery.addCriteria(Criteria.where("category").is("Application Software"));
		List<ServiceNowTicket> results = new ArrayList<>();

		ServiceNowTicket ticket = new ServiceNowTicket();
		ticket.setAppName("D40V");
		ticket.setStartdate("2018-04-17 12:00:00");
		ticket.setEnddate("2018-04-18 12:00:00");
		results.add(ticket);

		ticket = new ServiceNowTicket();
		ticket.setAppName("D40V");
		ticket.setStartdate("2018-03-15 12:00:00");
		ticket.setEnddate("2018-03-16 12:00:00");
		results.add(ticket);

		Mockito.when(mongoTemplate.find(basicQuery, ServiceNowTicket.class)).thenReturn(null);
		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(mongoClient)).thenReturn(mongoTemplate);
		serviceNowDAOTest.getServiceNowTickets("D40V", mongoClient);
	}

	@Test
	public void testGetServiceNowTickets_2() throws Exception {

		Query basicQuery = new Query();
		basicQuery.addCriteria(Criteria.where("applicationName").is("D40V"));
		basicQuery.addCriteria(Criteria.where("category").is("Application Software"));
		List<ServiceNowTicket> results = new ArrayList<>();

		ServiceNowTicket ticket = new ServiceNowTicket();

		ticket = new ServiceNowTicket();
		ticket.setAppName("D40V");
		ticket.setStartdate("124563");
		ticket.setEnddate("2018-03-16 12:00:00");
		results.add(ticket);

		Mockito.when(mongoTemplate.find(basicQuery, ServiceNowTicket.class)).thenReturn(null);
		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(mongoClient)).thenReturn(mongoTemplate);
		serviceNowDAOTest.getServiceNowTickets("D40V", mongoClient);
	}

	@Test
	public void getConvertStringToData() throws Exception {
		serviceNowDAOTest.convertStringToData("fewfweqfwqf");
	}

	@Test
	public void testGetSNDefectsList() throws Exception {
		// Mockito.when(metricsProcessorConfig.mongo()).thenReturn(mongoClient);
		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(mongoClient)).thenReturn(mongoTemplate);
		Query basicQuery = new Query();
		basicQuery.addCriteria(new Criteria().andOperator(Criteria.where("appId").is("D40V"),
				Criteria.where("aysCreatedTimeStamp").gte(124523612311l).lt(124523612311l)));
		Mockito.when(mongoTemplate.find(basicQuery, ServiceNowIssues.class)).thenReturn(getSNDefectsList());
		serviceNowDAOTest.getSNDefectsList("D40V", 124523612311l, 124523612311l, mongoClient);
	}

	@Test
	public void testGetLatestSNSorted() throws Exception {
		Mockito.when(metricsProcessorConfig.mongo()).thenReturn(mongoClient);
		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(mongoClient)).thenReturn(mongoTemplate);
		Query basicQuery = new Query();
		basicQuery.addCriteria(new Criteria().andOperator(Criteria.where("appId").is("D40V"),
				Criteria.where("aysCreatedTimeStamp").gte(124523612311l).lt(124523612311l)));
		basicQuery.with(new Sort(Sort.Direction.DESC, "aysCreatedTimeStamp"));
		// Mockito.when(mongoTemplate.findOne(basicQuery,
		// ServiceNowIssues.class)).thenReturn(getServiceNowSorted());
		serviceNowDAOTest.getLatestServiceNowSorted("D40V");
	}

	@Test
	public void testGetLatestSNSorted_1() {
		Mockito.when(serviceNowDAOTest.getLatestServiceNowSorted("D40V")).thenThrow(new NullPointerException());
		serviceNowDAOTest.getLatestServiceNowSorted("D40V");
	}

	@Test
	public void testGetDefectsSNList_2() {
		Mockito.when(serviceNowDAOTest.getSNDefectsList("D40V", 124523612311l, 124523612311l, mongoClient))
				.thenThrow(new NullPointerException());
		serviceNowDAOTest.getSNDefectsList("D40V", 124523612311l, 124523612311l, mongoClient);
	}

	@Test
	public void testIsDataAvailableForSN() throws Exception {
		// Mockito.when(metricsProcessorConfig.mongo()).thenReturn(mongoClient);
		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(mongoClient)).thenReturn(mongoTemplate);
		Query basicQuery = new Query();
		basicQuery.addCriteria(new Criteria().andOperator(Criteria.where("appId").is("D40V"),
				Criteria.where("aysCreatedTimeStamp").gte(124523612311l)));
		Mockito.when(mongoTemplate.find(basicQuery, ServiceNowIssues.class)).thenReturn(getSNDefectsList());
		serviceNowDAOTest.isDataAvailableForSN("D40V", 124523612311l, mongoClient);
	}

	@Test
	public void testIsDataAvailableForSN_1() {
		Mockito.when(serviceNowDAOTest.isDataAvailableForSN("D40V", 124523612311l, mongoClient))
				.thenThrow(new NullPointerException());
		serviceNowDAOTest.isDataAvailableForSN("D40V", 124523612311l, mongoClient);
	}

	private List<ServiceNowIssues> getSNDefectsList() {
		List<ServiceNowIssues> snIssuesList = new ArrayList<>();
		ServiceNowIssues sn = new ServiceNowIssues();
		ServiceNowIssues sn1 = new ServiceNowIssues();
		ServiceNowIssues sn2 = new ServiceNowIssues();
		ServiceNowIssues sn3 = new ServiceNowIssues();
		ServiceNowIssues sn4 = new ServiceNowIssues();
		ServiceNowIssues sn5 = new ServiceNowIssues();
		ServiceNowIssues sn6 = new ServiceNowIssues();
		ServiceNowIssues sn7 = new ServiceNowIssues();
		sn.setActive(true);
		sn.setAppId("D40V");
		sn.setAysPriority((long) 1);
		sn.setAysCreatedTimeStamp(1536397994l);
		sn1.setActive(false);
		sn1.setAppId("D40V");
		sn1.setAysPriority((long) 1);
		sn1.setAysCreatedTimeStamp(1536397994l);
		sn2.setActive(true);
		sn2.setAppId("D40V");
		sn2.setAysPriority((long) 2);
		sn2.setAysCreatedTimeStamp(1536397994l);
		sn3.setActive(false);
		sn3.setAppId("D40V");
		sn3.setAysPriority((long) 2);
		sn3.setAysCreatedTimeStamp(1536397994l);
		sn4.setActive(true);
		sn4.setAppId("D40V");
		sn4.setAysPriority((long) 3);
		sn4.setAysCreatedTimeStamp(1536397994l);
		sn5.setActive(false);
		sn5.setAppId("D40V");
		sn5.setAysPriority((long) 3);
		sn5.setAysCreatedTimeStamp(1536397994l);
		sn6.setActive(true);
		sn6.setAppId("D40V");
		sn6.setAysPriority((long) 4);
		sn6.setAysCreatedTimeStamp(1536397994l);
		sn7.setActive(false);
		sn7.setAppId("D40V");
		sn7.setAysPriority((long) 4);
		sn7.setAysCreatedTimeStamp(1536397994l);

		snIssuesList.add(sn7);
		snIssuesList.add(sn6);
		snIssuesList.add(sn5);
		snIssuesList.add(sn4);
		snIssuesList.add(sn3);
		snIssuesList.add(sn2);
		snIssuesList.add(sn1);
		snIssuesList.add(sn);
		return snIssuesList;
	}

}
