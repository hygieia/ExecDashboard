package com.capitalone.dashboard.custom;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.capitalone.dashboard.dao.ServiceNowDAO;
import com.capitalone.dashboard.exec.model.ServiceNowTicket;
import com.mongodb.MongoClient;

@RunWith(MockitoJUnitRunner.class)
public class ServiceNowAnalysisTest {

	@InjectMocks
	private ServiceNowAnalysis serviceNowAnalysisTest;
	@Mock
	private MongoClient mongoClient;
	@Mock
	private ServiceNowDAO serviceNowDAO;

	@Test
	public void getConvertStringToDataTest() throws Exception {
		serviceNowAnalysisTest.convertStringToData("fewfweqfwqf");
	}

	@Test
	public void getDeploymentCadenceTest() throws Exception {
		List<ServiceNowTicket> snTickets = new ArrayList<>();
		Mockito.when(serviceNowDAO.getServiceNowTickets("D40V", mongoClient)).thenReturn(snTickets);
		serviceNowAnalysisTest.getDeploymentCadence("D40V", mongoClient);
	}

	@Test
	public void getDeploymentCadenceTest_1() throws Exception {
		Mockito.when(serviceNowDAO.getServiceNowTickets("D40V", mongoClient)).thenReturn(null);
		serviceNowAnalysisTest.getDeploymentCadence("D40V", mongoClient);
	}

	@Test
	public void getDeploymentCadenceTest_2() throws Exception {
		Mockito.when(serviceNowDAO.getServiceNowTickets("D40V", mongoClient)).thenThrow(new NullPointerException());
		serviceNowAnalysisTest.getDeploymentCadence("D40V", mongoClient);
	}

	@Test
	public void getDeploymentCadenceTest_3() throws Exception {
		serviceNowAnalysisTest.getDeploymentCadence("D40V", mongoClient);
	}

	@Test
	public void getDeploymentCadenceTest_4() throws Exception {
		List<ServiceNowTicket> snTickets = new ArrayList<>();

		ServiceNowTicket sn = new ServiceNowTicket();
		sn.setAppName("D40V");
		sn.setStartdate("2018-04-16 12:00:00");
		sn.setEnddate("2018-04-16 14:00:00");
		sn.setNumber("12345");
		snTickets.add(sn);

		Mockito.when(serviceNowDAO.getServiceNowTickets("D40V", mongoClient)).thenReturn(snTickets);
		serviceNowAnalysisTest.getDeploymentCadence("D40V", mongoClient);
	}

	@Test
	public void getDeploymentCadenceTest_5() throws Exception {
		List<ServiceNowTicket> snTickets = new ArrayList<>();

		ServiceNowTicket sn = new ServiceNowTicket();
		sn.setAppName("D40V");
		sn.setStartdate("2018-04-16 12:00:00");
		sn.setEnddate("2018-04-16 14:00:00");
		sn.setNumber("12345");
		snTickets.add(sn);

		sn = new ServiceNowTicket();
		sn.setAppName("D40V");
		sn.setStartdate("2018-03-16 12:00:00");
		sn.setEnddate("2018-03-16 15:00:00");
		sn.setNumber("12347");
		snTickets.add(sn);

		Mockito.when(serviceNowDAO.getServiceNowTickets("D40V", mongoClient)).thenReturn(snTickets);
		serviceNowAnalysisTest.getDeploymentCadence("D40V", mongoClient);
	}

}
