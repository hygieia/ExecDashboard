package com.capitalone.dashboard.collector;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.capitalone.dashboard.custom.CloudAnalysis;
import com.capitalone.dashboard.custom.ExecutiveViewAnalysis;
import com.capitalone.dashboard.custom.ProductionIncidentsAnalysis;
import com.capitalone.dashboard.custom.QualityAnalysis;
import com.capitalone.dashboard.custom.SecurityAnalysis;
import com.capitalone.dashboard.custom.StashAnalysis;
import com.capitalone.dashboard.custom.ThroughPutAnalysis;
import com.capitalone.dashboard.custom.VastAnalysis;
import com.capitalone.dashboard.custom.VelocityAnalysis;
import com.capitalone.dashboard.custom.WipAnalysis;

@RunWith(MockitoJUnitRunner.class)
public class DefaultMetricsProcessorClientTest {
	
	@InjectMocks
	private DefaultMetricsProcessorClient defaultMetricsProcessorClient;
	@Mock
	private VastAnalysis vastAnalysis;
	@Mock
	private VelocityAnalysis velocityAnalysis;
	@Mock
	private ThroughPutAnalysis throughPutAnalysis;
	@Mock
	private SecurityAnalysis securityAnalysis;
	@Mock
	private ProductionIncidentsAnalysis productionIncidentsAnalysis;
	@Mock
	private QualityAnalysis qualityAnalysis;
	@Mock
	private ExecutiveViewAnalysis executiveViewAnalysis;
	@Mock
	private CloudAnalysis cloudAnalysis;
	@Mock
	private StashAnalysis stashAnalysis;
	@Mock
	private WipAnalysis wipAnalysis;

	@Test
	public void testDefaultMetricsProcessorClient() {
		defaultMetricsProcessorClient.processProductionIncidentsMetricsDetails("true");
		defaultMetricsProcessorClient.processQualityMetricsDetails();
		defaultMetricsProcessorClient.processThroughPutMetricsDetails();
		defaultMetricsProcessorClient.processVastMetricsDetails();
		defaultMetricsProcessorClient.processVelocityMetricsDetails();
		defaultMetricsProcessorClient.securityMetricsDetails("true");
		defaultMetricsProcessorClient.processStashMetricDetails("true");
		defaultMetricsProcessorClient.processCloudMetricsDetails("true");
		defaultMetricsProcessorClient.processExecutiveViewMetrics();
		defaultMetricsProcessorClient.processWipMetricDetails();
		defaultMetricsProcessorClient.collectCommonMetrics();
		defaultMetricsProcessorClient.updateCollectorStatus("Security",1230L,false);
	}


}