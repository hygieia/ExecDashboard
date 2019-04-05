package com.capitalone.dashboard.dao;

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

import com.capitalone.dashboard.collector.MetricsProcessorConfig;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

@RunWith(MockitoJUnitRunner.class)
public class DevopscupDAOTest {
	@InjectMocks DevopscupDAO devopscupDAO;
	@Mock
	private MongoTemplate mongoTemplate;
	@Mock
	private MongoClient mongoClient;
	@Mock
	MetricsProcessorConfig metricsProcessorConfig;
	
	@Mock
	private MongoOperations mongoOperations;
	
	
	@Mock
	private DBCollection mongoCollection;
	
	
	@Test
	public void getMongoClientTest() throws Exception {
		
			Mockito.when(metricsProcessorConfig.mongo()).thenReturn(mongoClient);
			devopscupDAO.getMongoClient();
	}

	@Test
	public void getMongoClientTest2() throws Exception {

			Mockito.when(metricsProcessorConfig.mongo()).thenThrow(new NullPointerException());
			devopscupDAO.getMongoClient();

	}
	
	@Test
	public void getAllDetails() throws Exception
	{
		String appId="D40V";
		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(Mockito.any(MongoClient.class))).thenReturn(mongoTemplate);
		devopscupDAO.getAllDetails( mongoClient);
	}
	@Test
	public void getAllDetails2() throws Exception
	{
		String appId="D40V";
		Mockito.when(metricsProcessorConfig.metricsProcessorTemplate(Mockito.any(MongoClient.class)))
		.thenThrow(new NullPointerException());
		devopscupDAO.getAllDetails( mongoClient);
		
	}

}
