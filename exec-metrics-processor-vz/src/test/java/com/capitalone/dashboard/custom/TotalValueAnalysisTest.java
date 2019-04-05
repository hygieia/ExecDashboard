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
import org.springframework.data.mongodb.core.MongoTemplate;

import com.capitalone.dashboard.dao.DashboardDetailsDAO;
import com.capitalone.dashboard.dao.VastDetailsDAO;
import com.capitalone.dashboard.dao.VelocityDetailsDAO;
import com.capitalone.dashboard.exec.model.MetricCount;
import com.capitalone.dashboard.exec.model.MetricLevel;
import com.capitalone.dashboard.exec.model.MetricSummary;
import com.capitalone.dashboard.exec.model.MetricTimeSeriesElement;
import com.capitalone.dashboard.exec.model.MetricType;
import com.capitalone.dashboard.exec.model.vz.ApplicationDetails;
import com.capitalone.dashboard.exec.model.vz.BuildingBlocks;
import com.capitalone.dashboard.exec.model.vz.ExecutiveComponents;
import com.capitalone.dashboard.exec.model.vz.ExecutiveMetrics;
import com.capitalone.dashboard.exec.model.vz.ExecutiveMetricsSeries;
import com.capitalone.dashboard.exec.model.vz.ExecutiveModuleMetrics;
import com.capitalone.dashboard.exec.model.vz.ExecutiveResponse;
import com.capitalone.dashboard.exec.model.vz.ExecutiveSummaryList;
import com.capitalone.dashboard.exec.model.vz.InstanceCollectorStatus;
import com.capitalone.dashboard.exec.model.vz.MetricsDetail;
import com.capitalone.dashboard.exec.model.vz.PortfolioResponse;
import com.capitalone.dashboard.exec.model.vz.SeriesCount;
import com.capitalone.dashboard.exec.repository.vz.ApplicationDetailsRepository;
import com.capitalone.dashboard.exec.repository.vz.BuildingBlocksRepository;
import com.capitalone.dashboard.exec.repository.vz.CollectorStatusRepository;
import com.capitalone.dashboard.exec.repository.vz.ExecutiveComponentRepository;
import com.capitalone.dashboard.exec.repository.vz.ExecutiveSummaryListRepository;
import com.capitalone.dashboard.exec.repository.vz.MetricsDetailRepository;
import com.capitalone.dashboard.exec.repository.vz.PortfolioResponseRepository;
import com.capitalone.dashboard.utils.GenericMethods;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

@RunWith(MockitoJUnitRunner.class)
public class TotalValueAnalysisTest {

	@InjectMocks
	private TotalValueAnalysis totalValueAnalysis;
	@Mock
	private ExecutiveSummaryListRepository executiveSummaryListRepository;
	@Mock
	private VelocityDetailsDAO velocityDetailsDAO;
	@Mock
	private ExecutiveComponentRepository executiveComponentRepository;
	@Mock
	private CollectorStatusRepository collectorStatusRepository;
	@Mock
	private ApplicationDetailsRepository applicationDetailsRepository;
	@Mock
	private BuildingBlocksRepository buildingBlocksRepository;
	@Mock
	private MongoTemplate mongoTemplate;
	@Mock
	private MetricsDetailRepository metricsDetailRepository;
	@Mock
	private DBCollection mongoCollection;
	@Mock
	private PortfolioResponseRepository portfolioResponseRepository;
	@Mock
	private MongoClient mongoClient;
	@Mock
	private DashboardDetailsDAO dashboardDetailsDAO;
	@Mock
	private VastDetailsDAO vastDetailsDAO;
	@Mock
	private GenericMethods genericMethods;

	@Test
	public void testProcessExecutiveDetailsMetrics() {
		Mockito.when(executiveSummaryListRepository.findAll()).thenReturn(getExecutiveSummaryList());
		// Mockito.when(metricDetailResponseRepository.findByAppIdAndMetricsName(Mockito.anyString(),Mockito.anyString())).thenReturn(getMetricsDetail());
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(getExecutiveComponents());
		Mockito.when(portfolioResponseRepository.findByEid(Mockito.anyString())).thenReturn(getPortfolioResponse());
		totalValueAnalysis.processExecutiveDetailsMetrics();
	}

	@Test
	public void testProcessMetricPortfolioDetailResponse_1() {
		ExecutiveSummaryList executiveSummaryList = new ExecutiveSummaryList();
		List<String> appIds = new ArrayList<>();
		executiveSummaryList.setAppId(appIds);
		executiveSummaryList.setTotalApps(10);
		executiveSummaryList.setConfiguredApps(6);
		executiveSummaryList.setAppId(appIds);
		executiveSummaryList.setConfiguredAppId(appIds);
		executiveSummaryList.setEid("124563");
		Mockito.when(executiveSummaryListRepository.findAll()).thenReturn(Arrays.asList(executiveSummaryList));

		totalValueAnalysis.processExecutiveDetailsMetrics();
	}

	@Test
	public void testProcessBuildingBlockMetrics() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("B6LV");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getApplicationDetails());
		Mockito.when(metricsDetailRepository.findByMetricLevelIdAndLevelAndType("B6LV", MetricLevel.PRODUCT,
				MetricType.TOTAL_VALUE)).thenReturn(getMetricsDetail());
		Mockito.when(buildingBlocksRepository.findByMetricLevelIdAndMetricLevel("B6LV", MetricLevel.PRODUCT))
				.thenReturn(buildingBlockMetricSummaryResponse());
		totalValueAnalysis.processBuildingBlockMetrics();
	}

	@Test
	public void testProcessBuildingBlockMetrics_1() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("B6LV");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		totalValueAnalysis.processBuildingBlockMetrics();
	}

	@Test
	public void testProcessBuildingBlockMetrics_2() throws Exception {
		Mockito.when(totalValueAnalysis.processBuildingBlockMetrics()).thenThrow(new NullPointerException());
		totalValueAnalysis.processBuildingBlockMetrics();
	}

	@Test
	public void testProcessMetricsDetailResponse() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("B6LV");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric("B6LV", "open-source-violations"))
				.thenReturn(getExecutiveComponents());
		Mockito.when(metricsDetailRepository.findByMetricLevelIdAndLevelAndType("B6LV", MetricLevel.PRODUCT,
				MetricType.TOTAL_VALUE)).thenReturn(getMetricsDetail());
		// Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getApplicationDetails());
		Mockito.when(genericMethods.processAppCriticality(Mockito.anyString())).thenReturn("Critical");
		totalValueAnalysis.processMetricsDetailResponse();
	}

	@Test
	public void testProcessMetricsDetailResponse_1() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("B6LV");
		ExecutiveComponents executiveComponents = new ExecutiveComponents();
		executiveComponents.setAppId("B6LV");
		executiveComponents.setAppName("ACSS");
		ExecutiveMetrics executiveMetrics = new ExecutiveMetrics();
		List<ExecutiveModuleMetrics> moduleMetricsList = new ArrayList<>();
		executiveMetrics.setLastScanned(new Date());
		executiveMetrics.setLastUpdated(new Date());
		executiveMetrics.setMetricsName("open-source-violations");
		executiveMetrics.setModules(moduleMetricsList);
		executiveMetrics.setTrendSlope(0.2);
		executiveComponents.setMetrics(Arrays.asList(executiveMetrics));
		executiveComponents.setTeamBoardLink("some link");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(executiveComponents);
		Mockito.when(genericMethods.processAppCriticality(Mockito.anyString())).thenReturn("Critical");
		totalValueAnalysis.processMetricsDetailResponse();
	}

	@Test
	public void testProcessComponentDetailsMetrics() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("B6LV");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		Mockito.when(velocityDetailsDAO.getMongoClient()).thenReturn(mongoClient);
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(getExecutiveComponents());
		Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getApplicationDetails());
		Mockito.when(buildingBlocksRepository.findByMetricLevelIdAndMetricLevelAndMetricType("B6LV",
				MetricLevel.COMPONENT, MetricType.TOTAL_VALUE)).thenReturn(getBuildingBlocksList());
		totalValueAnalysis.processComponentDetailsMetrics();
	}

	private PortfolioResponse getPortfolioResponse() {
		PortfolioResponse portfolioResponse = new PortfolioResponse();
		portfolioResponse.setEid("123456");
		portfolioResponse.setId(new ObjectId());
		portfolioResponse.setLob("NTS");
		portfolioResponse.setName("some name");
		ExecutiveResponse executive = new ExecutiveResponse();
		executive.setFirstName("some first name");
		executive.setLastName("some last name");
		executive.setRole("some role");
		portfolioResponse.setExecutive(executive);
		return portfolioResponse;
	}

	private BuildingBlocks buildingBlockMetricSummaryResponse() {
		BuildingBlocks buildingBlockMetricSummaryResponse = new BuildingBlocks();
		buildingBlockMetricSummaryResponse.setMetricLevelId("B6LV");
		MetricSummary summary = new MetricSummary();
		summary.setLastScanned(new Date());
		summary.setLastUpdated(new Date());
		summary.setName("open-source-violations");
		summary.setReportingComponents(1);
		summary.setTotalComponents(1);
		summary.setTrendSlope(2.3);
		summary.setCounts(settingCounts());
		buildingBlockMetricSummaryResponse.setMetrics(Arrays.asList(summary));
		return buildingBlockMetricSummaryResponse;
	}

	private List<BuildingBlocks> getBuildingBlocksList() {
		List<BuildingBlocks> buildingBlockComponentSummaryResponseList = new ArrayList<BuildingBlocks>();
		BuildingBlocks buildingBlockComponentSummaryResponse = new BuildingBlocks();
		buildingBlockComponentSummaryResponse.setMetricLevelId("B6LV");
		buildingBlockComponentSummaryResponse.setLob("NTS");
		buildingBlockComponentSummaryResponse.setName("open-source-violations");
		buildingBlockComponentSummaryResponse.setPoc("some poc");
		buildingBlockComponentSummaryResponse.setTotalComponents(1);
		buildingBlockComponentSummaryResponse.setUrl("some url");
		List<MetricSummary> metrics = new ArrayList<>(Arrays.asList(getMetricSummaryResponse()));
		buildingBlockComponentSummaryResponse.setMetrics(metrics);
		return buildingBlockComponentSummaryResponseList;
	}

	private MetricSummary getMetricSummaryResponse() {

		MetricSummary summary = new MetricSummary();
		summary.setLastScanned(new Date());
		summary.setLastUpdated(new Date());
		summary.setName("open-source-violations");
		summary.setReportingComponents(1);
		summary.setTotalComponents(1);
		summary.setTrendSlope(2.3);
		summary.setCounts(settingCounts());

		return summary;
	}

	private List<MetricCount> settingCounts() {
		List<MetricCount> counts = new ArrayList<MetricCount>();

		MetricCount count = new MetricCount();

		Map<String, String> label = new HashMap<>();
		label.put("type", "Total Time");
		count.setLabel(label);
		count.setValue(34.0);
		counts.add(count);

		count = new MetricCount();
		Map<String, String> label1 = new HashMap<>();
		label1.put("type", "Total Stories");
		count.setLabel(label);
		count.setValue(2.0);
		counts.add(count);

		count = new MetricCount();
		Map<String, String> label2 = new HashMap<>();
		label2.put("type", "Total Story Points");
		count.setLabel(label);
		count.setValue(2.0);
		counts.add(count);

		return counts;
	}

	private ApplicationDetails getApplicationDetails() {
		ApplicationDetails appDetails = new ApplicationDetails();
		appDetails.setAppAcronym("appAcronym");
		appDetails.setAppId("B6LV");
		appDetails.setAvailabilityStatus("available");
		appDetails.setAppName("ACSS");
		appDetails.setCollectorStatus(new ArrayList<InstanceCollectorStatus>());
		appDetails.setDashboardAvailable(true);
		appDetails.setLastScanned(new Date());
		appDetails.setLob("vzw");
		appDetails.setPoc("Shankar");
		appDetails.setTeamBoardLink("https://");
		appDetails.setTotalTeamBoards(1);
		return appDetails;
	}

	private List<ExecutiveSummaryList> getExecutiveSummaryList() {
		List<ExecutiveSummaryList> listOfExecutiveSummaryList = new ArrayList<>();

		ExecutiveSummaryList executiveSummaryList = new ExecutiveSummaryList();
		executiveSummaryList.setAppId(Arrays.asList("B6LV"));
		executiveSummaryList.setBusinessUnits(Arrays.asList("B6LV"));
		executiveSummaryList.setLastName("some name");
		executiveSummaryList.setConfiguredApps(1);
		executiveSummaryList.setConfiguredAppId(Arrays.asList("B6LV"));
		executiveSummaryList.setEid("123456789");
		executiveSummaryList.setFirstName("some name");
		executiveSummaryList.setReportingPercentage(100.0);
		executiveSummaryList.setRole("some role");
		executiveSummaryList.setTotalApps(1);

		listOfExecutiveSummaryList.add(executiveSummaryList);

		return listOfExecutiveSummaryList;
	}

	private ExecutiveComponents getExecutiveComponents() {

		ExecutiveComponents executiveComponents = new ExecutiveComponents();
		executiveComponents.setAppId("B6LV");
		executiveComponents.setAppName("SomaAppName");
		executiveComponents.setMetrics(Arrays.asList(getExecutiveMetricsList()));
		executiveComponents.setTeamBoardLink("some link");
		return executiveComponents;
	}

	private ExecutiveMetrics getExecutiveMetricsList() {

		ExecutiveMetrics executiveMetrics = new ExecutiveMetrics();
		executiveMetrics.setLastScanned(new Date());
		executiveMetrics.setLastUpdated(new Date());
		executiveMetrics.setMetricsName("open-source-violations");
		ExecutiveModuleMetrics module = new ExecutiveModuleMetrics();
		module.setLastScanned(new Date());
		module.setLastUpdated(new Date());
		module.setModuleName("module");
		module.setTrendSlope((double) 0);

		List<SeriesCount> counts = new ArrayList<SeriesCount>();
		SeriesCount series1 = new SeriesCount();
		Map<String, String> label1 = new HashMap<>();
		label1.put("type", "Total Time");
		series1.setLabel(label1);
		series1.setCount((long) 232);
		counts.add(series1);

		SeriesCount series2 = new SeriesCount();
		Map<String, String> label2 = new HashMap<>();
		label2.put("type", "Total Stories");
		series2.setLabel(label2);
		series2.setCount((long) 5);
		counts.add(series2);

		SeriesCount series3 = new SeriesCount();
		Map<String, String> label3 = new HashMap<>();
		label3.put("type", "Total Story Points");
		series3.setLabel(label3);
		series3.setCount((long) 5);
		counts.add(series3);

		SeriesCount series4 = new SeriesCount();
		Map<String, String> label4 = new HashMap<>();
		label4.put("type", "Total Enhancements");
		series4.setLabel(label4);
		series4.setCount((long) 5);
		counts.add(series4);

		SeriesCount series5 = new SeriesCount();
		Map<String, String> label5 = new HashMap<String, String>();
		label5.put("type", "Total New Features");
		series5.setLabel(label5);
		series5.setCount((long) 5);
		counts.add(series5);

		SeriesCount series6 = new SeriesCount();
		Map<String, String> label6 = new HashMap<String, String>();
		label6.put("type", "Total User Stories");
		series6.setLabel(label6);
		series6.setCount((long) 5);
		counts.add(series6);

		SeriesCount series7 = new SeriesCount();
		Map<String, String> label7 = new HashMap<String, String>();
		label7.put("type", "Total User Story Time");
		series7.setLabel(label7);
		series7.setCount((long) 5);
		counts.add(series7);

		ExecutiveMetricsSeries executiveMetricsSeries30 = new ExecutiveMetricsSeries();
		executiveMetricsSeries30.setDaysAgo(30);
		executiveMetricsSeries30.setTimeStamp(123456789l);
		executiveMetricsSeries30.setCounts(counts);
		ExecutiveMetricsSeries executiveMetricsSeries60 = new ExecutiveMetricsSeries();
		executiveMetricsSeries60.setDaysAgo(60);
		executiveMetricsSeries60.setTimeStamp(123456789l);
		executiveMetricsSeries60.setCounts(counts);
		ExecutiveMetricsSeries executiveMetricsSeries90 = new ExecutiveMetricsSeries();
		executiveMetricsSeries90.setDaysAgo(90);
		executiveMetricsSeries90.setTimeStamp(123456789l);
		executiveMetricsSeries90.setCounts(counts);
		List<ExecutiveMetricsSeries> executiveMetricsSeriesList = new ArrayList<ExecutiveMetricsSeries>();
		executiveMetricsSeriesList.add(executiveMetricsSeries30);
		executiveMetricsSeriesList.add(executiveMetricsSeries60);
		executiveMetricsSeriesList.add(executiveMetricsSeries90);
		module.setSeries(executiveMetricsSeriesList);

		List<ExecutiveModuleMetrics> moduleList = new ArrayList<ExecutiveModuleMetrics>();
		moduleList.add(module);

		executiveMetrics.setModules(moduleList);
		executiveMetrics.setTrendSlope((double) 0);

		return executiveMetrics;
	}

	private MetricsDetail getMetricsDetail() {

		MetricsDetail metricDetailResponse = new MetricsDetail();
		metricDetailResponse.setMetricLevelId("B6LV");

		MetricSummary summary = new MetricSummary();
		summary.setLastScanned(new Date());
		summary.setLastUpdated(new Date());
		summary.setName("open-source-violations");
		summary.setReportingComponents(1);
		summary.setTotalComponents(1);
		summary.setTrendSlope(2.3);
		List<MetricCount> counts = new ArrayList<MetricCount>();

		MetricCount count = new MetricCount();
		Map<String, String> label = new HashMap<String, String>();
		label.put("type", "Total Time");
		count.setLabel(label);
		count.setValue(232);
		counts.add(count);

		MetricCount count1 = new MetricCount();
		Map<String, String> label1 = new HashMap<String, String>();
		label1.put("type", "Total Stories");
		count1.setLabel(label1);
		count1.setValue(5);
		counts.add(count1);

		MetricCount count2 = new MetricCount();
		Map<String, String> label2 = new HashMap<String, String>();
		label2.put("type", "Total Story Points");
		count2.setLabel(label2);
		count2.setValue(5);
		counts.add(count2);

		MetricCount count3 = new MetricCount();
		Map<String, String> label3 = new HashMap<String, String>();
		label3.put("type", "Total Enhancements");
		count3.setLabel(label3);
		count3.setValue(5);
		counts.add(count3);

		MetricCount count4 = new MetricCount();
		Map<String, String> label4 = new HashMap<String, String>();
		label4.put("type", "Total New Features");
		count4.setLabel(label4);
		count4.setValue(5);
		counts.add(count4);

		MetricCount count5 = new MetricCount();
		Map<String, String> label5 = new HashMap<String, String>();
		label5.put("type", "Total User Stories");
		count5.setLabel(label5);
		count5.setValue(5);
		counts.add(count5);

		summary.setCounts(counts);

		metricDetailResponse.setSummary(summary);
		List<MetricTimeSeriesElement> timeSeries = new ArrayList<>();
		MetricTimeSeriesElement time30 = new MetricTimeSeriesElement();
		time30.setCounts(counts);
		time30.setDaysAgo(30);
		timeSeries.add(time30);

		MetricTimeSeriesElement time60 = new MetricTimeSeriesElement();
		time60.setCounts(counts);
		time60.setDaysAgo(60);
		timeSeries.add(time60);

		MetricTimeSeriesElement time90 = new MetricTimeSeriesElement();
		time90.setCounts(counts);
		time90.setDaysAgo(90);
		timeSeries.add(time90);
		metricDetailResponse.setTimeSeries(timeSeries);
		return metricDetailResponse;

	}

}
