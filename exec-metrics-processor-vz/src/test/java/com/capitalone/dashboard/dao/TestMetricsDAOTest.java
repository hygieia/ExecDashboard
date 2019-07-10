package com.capitalone.dashboard.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.capitalone.dashboard.collector.MetricsProcessorConfig;
import com.mongodb.MongoClient;

/**
 * 
 * @author ASTHAAA
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class TestMetricsDAOTest {

	@InjectMocks
	private TestMetricsDAO testMetricsDAO;
	@Mock
	MetricsProcessorConfig metricsProcessorConfig;
	@Mock
	MongoClient mongoClient;
	@Mock
	MongoTemplate mongoOps;

	private MongoClient client;

	@Test
	public void getEntireAppList_test_1() throws Exception {
		// Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(mongoClient)).thenReturn(mongoOps);
		testMetricsDAO.getEntireAppList(client);

	}

	@Test
	public void getListOfModules_test_1() {

		testMetricsDAO.getListOfModules(client, "", 0l);

	}

	@Test
	public void getDataForModule_test_1() {

		testMetricsDAO.getTestDetailsForModule("", "", 0l, 0l, client);

	}

	@Test
	public void getMongoClient_test_1() throws Exception {
		Mockito.when(metricsProcessorConfig.mongo()).thenReturn(mongoClient);
		testMetricsDAO.getMongoClient();

	}

	@Test
	public void getMongoClient_test_2() {
		Mockito.when(testMetricsDAO.getMongoClient()).thenThrow(new NullPointerException());
		testMetricsDAO.getMongoClient();
	}
}
