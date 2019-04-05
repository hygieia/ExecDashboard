package com.capitalone.dashboard.custom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.capitalone.dashboard.exec.model.MetricCount;
import com.capitalone.dashboard.exec.model.MetricLevel;
import com.capitalone.dashboard.exec.model.MetricSummary;
import com.capitalone.dashboard.exec.model.vz.BuildingBlocks;
import com.capitalone.dashboard.exec.model.vz.ExecutiveSummaryList;
import com.capitalone.dashboard.exec.model.vz.MetricsDetail;
import com.capitalone.dashboard.exec.model.vz.PortfolioResponse;
import com.capitalone.dashboard.exec.repository.vz.BuildingBlocksRepository;
import com.capitalone.dashboard.exec.repository.vz.ExecutiveSummaryListRepository;
import com.capitalone.dashboard.exec.repository.vz.MetricsDetailRepository;
import com.capitalone.dashboard.exec.repository.vz.PortfolioResponseRepository;

@RunWith(MockitoJUnitRunner.class)
public class ExecutiveViewAnalysisTest {

	@InjectMocks
	private ExecutiveViewAnalysis executiveViewAnalysisTest;
	@Mock
	private ExecutiveSummaryListRepository executiveSummaryListRepository;
	@Mock
	private BuildingBlocksRepository buildingBlocksRepository;
	@Mock
	private PortfolioResponseRepository portfolioResponseRepository;
	@Mock
	private MetricsDetailRepository metricsDetailRepository;

	@Test
	public void testprocessExecutiveDetailsMetrics_1() throws Exception {
		ExecutiveSummaryList executiveSummaryList = new ExecutiveSummaryList();
		List<String> appIds = new ArrayList<>();
		executiveSummaryList.setAppId(appIds);
		executiveSummaryList.setTotalApps(10);
		executiveSummaryList.setConfiguredApps(6);
		executiveSummaryList.setEid("12333221");
		Mockito.when(executiveSummaryListRepository.findAll()).thenReturn(Arrays.asList(executiveSummaryList));
		MetricsDetail response = new MetricsDetail();
		response.setMetricLevelId("12333221");
		response.setSummary(getMetricsDetail());
		Mockito.when(metricsDetailRepository.findByMetricLevelIdAndLevel("12333221",
				MetricLevel.PORTFOLIO)).thenReturn(Arrays.asList(response));
		PortfolioResponse pr = new PortfolioResponse();
		pr.setEid("12333221");
		pr.setName("Siva");
		pr.setId(new ObjectId());
		Mockito.when(portfolioResponseRepository.findByEid("12333221")).thenReturn(pr);
		Mockito.when( buildingBlocksRepository.findByMetricLevelIdAndMetricLevel("12333221",
				MetricLevel.PORTFOLIO)).thenReturn(new BuildingBlocks());
		executiveViewAnalysisTest.processExecutiveDetailsMetrics();

	}
	
	@Test
	public void testprocessExecutiveDetailsMetrics_2() throws Exception {
		ExecutiveSummaryList executiveSummaryList = new ExecutiveSummaryList();
		List<String> appIds = new ArrayList<>();
		executiveSummaryList.setAppId(appIds);
		executiveSummaryList.setTotalApps(10);
		executiveSummaryList.setConfiguredApps(6);
		executiveSummaryList.setEid("12333221");
		Mockito.when(executiveSummaryListRepository.findAll()).thenReturn(Arrays.asList(executiveSummaryList));
		MetricsDetail response = new MetricsDetail();
		response.setMetricLevelId("12333221");
		response.setSummary(getMetricsDetail());
		Mockito.when(metricsDetailRepository.findByMetricLevelIdAndLevel("12333221",
				MetricLevel.PORTFOLIO)).thenReturn(Arrays.asList(response));
		PortfolioResponse pr = new PortfolioResponse();
		pr.setEid("12333221");
		pr.setName("Siva");
		pr.setId(new ObjectId());
		Mockito.when(portfolioResponseRepository.findByEid("12333221")).thenReturn(pr);
		Mockito.when( buildingBlocksRepository.findByMetricLevelIdAndMetricLevel("12333221",
				MetricLevel.PORTFOLIO)).thenReturn(null);
		executiveViewAnalysisTest.processExecutiveDetailsMetrics();

	}

	private MetricSummary getMetricsDetail() {

		MetricSummary summary = new MetricSummary();
		summary.setLastScanned(new Date());
		summary.setLastUpdated(new Date());
		summary.setName("production-incidents");
		summary.setReportingComponents(1);
		summary.setTotalComponents(1);
		summary.setTrendSlope(2.3);
		summary.setCounts(settingCounts());

		return summary;
	}

	private List<MetricCount> settingCounts() {
		List<MetricCount> counts = new ArrayList<MetricCount>();

		MetricCount count = new MetricCount();

		Map<String, String> label = new HashMap<String, String>();
		label.put("severity", "1");
		label.put("timeToResolve", "43");
		count.setLabel(label);
		count.setValue(2.0);
		counts.add(count);

		count = new MetricCount();
		label = new HashMap<String, String>();
		label.put("severity", "1");
		label.put("timeToResolve", "43");
		count.setLabel(label);
		count.setValue(2.0);
		counts.add(count);

		count = new MetricCount();
		label = new HashMap<String, String>();
		label.put("severity", "2");
		label.put("timeToResolve", "43");
		count.setLabel(label);
		count.setValue(2.0);
		counts.add(count);

		count = new MetricCount();
		label = new HashMap<String, String>();
		label.put("severity", "2");
		label.put("timeToResolve", "21");
		count.setLabel(label);
		count.setValue(4.0);
		counts.add(count);

		return counts;
	}

}
