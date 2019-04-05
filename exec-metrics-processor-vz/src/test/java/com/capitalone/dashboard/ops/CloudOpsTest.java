package com.capitalone.dashboard.ops;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.capitalone.dashboard.dao.CloudDAO;
import com.capitalone.dashboard.exec.model.vz.CloudCost;
import com.capitalone.dashboard.exec.model.vz.ExecutiveMetricsSeries;
import com.capitalone.dashboard.exec.model.vz.ExecutiveModuleMetrics;
import com.capitalone.dashboard.exec.repository.vz.CloudCostRepository;
import com.capitalone.dashboard.utils.GenericMethods;
import com.mongodb.MongoClient;

/**
 * @author raish4s
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CloudOpsTest {
	@InjectMocks
	CloudOps ops;
	@Mock
	MongoClient client;
	@Mock
	CloudDAO cloudDAO;
	@Mock
	GenericMethods genericMethods;
	@Mock
	CloudCostRepository cloudCostRepository;
	CloudTestUtils cloudTestUtils = new CloudTestUtils();

	@Test
	public void test() {
		Mockito.when(cloudDAO.getEBSByAppId("B6LV", client)).thenReturn((long) 5);
		Mockito.when(cloudDAO.getEncryptedEBSByAppId("B6LV", client)).thenReturn((long) 5);
		Mockito.when(cloudDAO.getEncryptedEBSByAppIdAndEnv("B6LV", client, "PROD")).thenReturn((long) 5);
		Mockito.when(cloudDAO.getEncryptedS3BucketsByAppId("B6LV", client)).thenReturn((long) 5);
		Mockito.when(cloudDAO.getELBsByAppId("B6LV", client)).thenReturn((long) 5);
		Mockito.when(cloudDAO.getELBsByAppIdAndEnv("B6LV", client, "PROD")).thenReturn((long) 5);
		Mockito.when(cloudDAO.getS3BucketsByAppId("B6LV", client)).thenReturn((long) 5);
		Mockito.when(cloudDAO.getInstancesByAppId("B6LV", client)).thenReturn((long) 5);
		Mockito.when(cloudDAO.getInstancesByAppIdAndEnv("B6LV", client, "PROD")).thenReturn((long) 5);
		Mockito.when(cloudDAO.getUnusedEBsByAppId("B6LV", client)).thenReturn((long) 5);
		Mockito.when(cloudDAO.getUnusedEBsByAppIdAndEnv("B6LV", client, "PROD")).thenReturn((long) 5);
		Mockito.when(cloudDAO.getUnusedELBsByAppId("B6LV", client)).thenReturn((long) 5);
		Mockito.when(cloudDAO.getUnusedELBsByAppIdAndEnv("B6LV", client, "PROD")).thenReturn((long) 5);
		Mockito.when(cloudDAO.getUnusedENIsByAppId("B6LV", client)).thenReturn((long) 5);
		Mockito.when(cloudDAO.getUnusedENIsByAppIdAndEnv("B6LV", client, "PROD")).thenReturn((long) 5);
		Mockito.when(cloudDAO.getRDSByAppId("B6LV", client)).thenReturn((long) 5);
		Mockito.when(cloudDAO.getRDSByAppIdAndEnv("B6LV", client, "PROD")).thenReturn((long) 5);
		Mockito.when(genericMethods.processSeriesCount(Mockito.any())).thenReturn(getSeriesMap());
		CloudCost cost = new CloudCost();
		String date = "30-01-2019";
		Mockito.when(cloudCostRepository.findByAppIdAndTime(Mockito.anyString(), Mockito.anyString())).thenReturn(cost);
		ops.processSeriesCount("B6LV", client, date);
		List<ExecutiveModuleMetrics> modules = new ArrayList<>();
		ExecutiveModuleMetrics executiveModuleMetrics = new ExecutiveModuleMetrics();
		List<ExecutiveMetricsSeries> series = new ArrayList<>();
		ExecutiveMetricsSeries seriesElement = new ExecutiveMetricsSeries();
		ExecutiveMetricsSeries seriesElement1 = new ExecutiveMetricsSeries();
		seriesElement1.setCounts(cloudTestUtils.getSeries());
		seriesElement1.setDaysAgo(0);
		seriesElement1.setTimeStamp((long) 0);
		seriesElement.setCounts(cloudTestUtils.getSeries());
		seriesElement.setDaysAgo(0);
		seriesElement.setTimeStamp((long) 0);
		series.add(seriesElement);
		series.add(seriesElement1);
		executiveModuleMetrics.setSeries(series);
		modules.add(executiveModuleMetrics);
		ops.processExecutiveMetricsSeries(modules);
	}

	private Map<String, Long> getSeriesMap() {
		Map<String, Long> processedSeries = new HashMap<>();
		processedSeries.put("costCount", (long) 10);
		processedSeries.put("encryptedEBSCount", (long) 10);
		processedSeries.put("unencryptedEBSCount", (long) 10);
		processedSeries.put("encryptedS3Count", (long) 10);
		processedSeries.put("unencryptedS3Count", (long) 10);
		processedSeries.put("migrationEnabledCount", (long) 10);
		processedSeries.put("costOptimizedCount", (long) 10);
		processedSeries.put("amiCount", (long) 10);
		processedSeries.put("elbCount", (long) 10);
		processedSeries.put("rdsCount", (long) 10);
		processedSeries.put("unusedElbCount", (long) 10);
		processedSeries.put("unusedEniCount", (long) 10);
		processedSeries.put("unusedEbsCount", (long) 10);

		processedSeries.put("prodEncryptedEBSCount", (long) 10);
		processedSeries.put("prodUnencryptedEBSCount", (long) 10);
		processedSeries.put("prodEncryptedS3Count", (long) 10);
		processedSeries.put("prodUnencryptedS3Count", (long) 10);
		processedSeries.put("prodMigrationEnabledCount", (long) 10);
		processedSeries.put("prodCostOptimizedCount", (long) 10);
		processedSeries.put("prodAmiCount", (long) 10);
		processedSeries.put("prodElbCount", (long) 10);
		processedSeries.put("prodRdsCount", (long) 10);
		processedSeries.put("prodUnusedElbCount", (long) 10);
		processedSeries.put("prodUnusedEniCount", (long) 10);
		processedSeries.put("prodUnusedEbsCount", (long) 10);

		processedSeries.put("nonprodEncryptedEBSCount", (long) 10);
		processedSeries.put("nonprodUnencryptedEBSCount", (long) 10);
		processedSeries.put("nonprodEncryptedS3Count", (long) 10);
		processedSeries.put("nonprodUnencryptedS3Count", (long) 10);
		processedSeries.put("nonprodMigrationEnabledCount", (long) 10);
		processedSeries.put("nonprodCostOptimizedCount", (long) 10);
		processedSeries.put("nonprodAmiCount", (long) 10);
		processedSeries.put("nonprodElbCount", (long) 10);
		processedSeries.put("nonprodRdsCount", (long) 10);
		processedSeries.put("nonprodUnusedElbCount", (long) 10);
		processedSeries.put("nonprodUnusedEniCount", (long) 10);
		processedSeries.put("nonprodUnusedEbsCount", (long) 10);

		processedSeries.put("stagingEncryptedEBSCount", (long) 10);
		processedSeries.put("stagingUnencryptedEBSCount", (long) 10);
		processedSeries.put("stagingEncryptedS3Count", (long) 10);
		processedSeries.put("stagingUnencryptedS3Count", (long) 10);
		processedSeries.put("stagingMigrationEnabledCount", (long) 10);
		processedSeries.put("stagingCostOptimizedCount", (long) 10);
		processedSeries.put("stagingAmiCount", (long) 10);
		processedSeries.put("stagingElbCount", (long) 10);
		processedSeries.put("stagingRdsCount", (long) 10);
		processedSeries.put("stagingUnusedElbCount", (long) 10);
		processedSeries.put("stagingUnusedEniCount", (long) 10);
		processedSeries.put("stagingUnusedEbsCount", (long) 10);
		return processedSeries;
	}
}
