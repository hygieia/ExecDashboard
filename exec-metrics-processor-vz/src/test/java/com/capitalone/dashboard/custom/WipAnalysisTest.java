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
import com.capitalone.dashboard.dao.WipDetailsDAO;
import com.capitalone.dashboard.exec.model.MetricCount;
import com.capitalone.dashboard.exec.model.MetricLevel;
import com.capitalone.dashboard.exec.model.MetricSummary;
import com.capitalone.dashboard.exec.model.MetricTimeSeriesElement;
import com.capitalone.dashboard.exec.model.MetricType;
import com.capitalone.dashboard.exec.model.vz.Application;
import com.capitalone.dashboard.exec.model.vz.ApplicationDetails;
import com.capitalone.dashboard.exec.model.vz.BuildingBlocks;
import com.capitalone.dashboard.exec.model.vz.CollectorStatus;
import com.capitalone.dashboard.exec.model.vz.CollectorType;
import com.capitalone.dashboard.exec.model.vz.Dashboard;
import com.capitalone.dashboard.exec.model.vz.ExecutiveComponents;
import com.capitalone.dashboard.exec.model.vz.ExecutiveMetrics;
import com.capitalone.dashboard.exec.model.vz.ExecutiveMetricsSeries;
import com.capitalone.dashboard.exec.model.vz.ExecutiveModuleMetrics;
import com.capitalone.dashboard.exec.model.vz.ExecutiveResponse;
import com.capitalone.dashboard.exec.model.vz.ExecutiveSummaryList;
import com.capitalone.dashboard.exec.model.vz.FeatureUserStory;
import com.capitalone.dashboard.exec.model.vz.InstanceCollectorStatus;
import com.capitalone.dashboard.exec.model.vz.JiraDetailsFinal;
import com.capitalone.dashboard.exec.model.vz.JiraInfo;
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
public class WipAnalysisTest {

	@Mock
	private DashboardDetailsDAO dashboardDetailsDAO;
	@Mock
	private WipDetailsDAO wipDetailsDAO;
	@Mock
	private ExecutiveSummaryListRepository executiveSummaryListRepository;
	@Mock
	private ExecutiveComponentRepository executiveComponentRepository;
	@Mock
	private MetricsDetailRepository metricDetailResponseRepository;
	@Mock
	private ApplicationDetailsRepository applicationDetailsRepository;
	@Mock
	private BuildingBlocksRepository buildingBlocksRepository;
	@Mock
	private GenericMethods genericMethods;
	@Mock
	private MongoTemplate mongoTemplate;

	@Mock
	private CollectorStatusRepository collectorStatusRepository;
	@Mock
	private MetricsDetailRepository metricsDetailRepository;
	@Mock
	private DBCollection mongoCollection;
	@Mock
	private PortfolioResponseRepository portfolioResponseRepository;
	@Mock
	private MongoClient mongoClient;
	@Mock
	private VastDetailsDAO vastDetailsDAO;

	@InjectMocks
	private WipAnalysis wipAnalysis;

	@Test
	public void testProcessWipDetails() throws Exception {

		Mockito.when(wipDetailsDAO.getMongoClient()).thenReturn(mongoClient);
		Mockito.when(wipDetailsDAO.getEntireAppList(Mockito.any(MongoClient.class))).thenReturn(getAppList());
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(getExecutiveComponents());
		Mockito.when(collectorStatusRepository.findByType(CollectorType.MetricsProcessor))
				.thenReturn(getCollectorStatus());
		Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getApplicationDetails());
		Mockito.when(wipDetailsDAO.getEntireProjectList(mongoClient, "D40V")).thenReturn(getJiraDetailsList());
		Mockito.when(wipDetailsDAO.getUserStoriesList(Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong(),
				Mockito.any(MongoClient.class))).thenReturn(getUserStoryList());
		Mockito.when(wipDetailsDAO.getLatestUserStorySorted(Mockito.anyString(), Mockito.any(MongoClient.class)))
				.thenReturn(getUserStoryListSorted());
		wipAnalysis.processExecutiveMetricsDetails();
	}

	@Test
	public void testProcessWipDetails_1() throws Exception {

		Mockito.when(wipDetailsDAO.getMongoClient()).thenReturn(mongoClient);
		Mockito.when(wipDetailsDAO.getEntireAppList(Mockito.any(MongoClient.class))).thenReturn(getAppList());
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(getExecutiveComponents());
		Mockito.when(collectorStatusRepository.findByType(CollectorType.MetricsProcessor))
				.thenReturn(getCollectorStatus());
		Mockito.when(collectorStatusRepository.findByType(CollectorType.JiraUserStory))
				.thenReturn(getCollectorStatusJiraUSerStory());
		Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getApplicationDetails());
		Mockito.when(wipDetailsDAO.getEntireProjectList(mongoClient, "D40V")).thenReturn(getJiraDetailsList());
		Mockito.when(wipDetailsDAO.getUserStoriesList(Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong(),
				Mockito.any(MongoClient.class))).thenReturn(getUserStoryList());
		Mockito.when(wipDetailsDAO.getLatestUserStorySorted(Mockito.anyString(), Mockito.any(MongoClient.class)))
				.thenReturn(getUserStoryListSorted_1());
		wipAnalysis.processExecutiveMetricsDetails();
	}

	@Test
	public void testProcessWipDetails_2() throws Exception {
		Mockito.when(wipAnalysis.processExecutiveMetricsDetails()).thenThrow(new NullPointerException());
		wipAnalysis.processExecutiveMetricsDetails();
	}

	@Test
	public void testProcessExecutiveDetailsMetrics() {
		Mockito.when(executiveSummaryListRepository.findAll()).thenReturn(getExecutiveSummaryList());
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(getExecutiveComponents());
		Mockito.when(collectorStatusRepository.findByType(CollectorType.JiraUserStory))
				.thenReturn(getCollectorStatusJiraUSerStory());
		Mockito.when(portfolioResponseRepository.findByEid(Mockito.anyString())).thenReturn(getPortfolioResponse());
		wipAnalysis.processExecutiveDetailsMetrics();
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

		wipAnalysis.processExecutiveDetailsMetrics();
	}

	@Test
	public void testProcessBuildingBlockMetrics() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("B6LV");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getApplicationDetails());
		Mockito.when(metricsDetailRepository.findByMetricLevelIdAndLevelAndType("B6LV", MetricLevel.PRODUCT,
				MetricType.WORK_IN_PROGRESS)).thenReturn(getMetricsDetail());
		Mockito.when(buildingBlocksRepository.findByMetricLevelIdAndMetricLevel("B6LV", MetricLevel.PRODUCT))
				.thenReturn(buildingBlockMetricsDetail());
		wipAnalysis.processBuildingBlockMetrics();
	}

	@Test
	public void testProcessBuildingBlockMetrics_1() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("B6LV");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		wipAnalysis.processBuildingBlockMetrics();
	}

	@Test
	public void testProcessBuildingBlockMetrics_2() throws Exception {
		Mockito.when(wipAnalysis.processBuildingBlockMetrics()).thenThrow(new NullPointerException());
		wipAnalysis.processBuildingBlockMetrics();
	}

	@Test
	public void testProcessMetricsDetailResponse() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("B6LV");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric("B6LV", "work-in-progress"))
				.thenReturn(getExecutiveComponents());
		Mockito.when(metricsDetailRepository.findByMetricLevelIdAndLevelAndType("B6LV", MetricLevel.PRODUCT,
				MetricType.WORK_IN_PROGRESS)).thenReturn(getMetricsDetail());
		// Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getApplicationDetails());
		Mockito.when(genericMethods.processAppCriticality(Mockito.anyString())).thenReturn("Critical");
		wipAnalysis.processMetricsDetailResponse();
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
		executiveMetrics.setMetricsName("work-in-progress");
		executiveMetrics.setModules(moduleMetricsList);
		executiveMetrics.setTrendSlope(0.2);
		executiveComponents.setMetrics(Arrays.asList(executiveMetrics));
		executiveComponents.setTeamBoardLink("some link");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(executiveComponents);
		Mockito.when(genericMethods.processAppCriticality(Mockito.anyString())).thenReturn("Critical");
		wipAnalysis.processMetricsDetailResponse();
	}

	@Test
	public void testProcessComponentDetailsMetrics() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("B6LV");
		Mockito.when(wipDetailsDAO.getMongoClient()).thenReturn(mongoClient);
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(getExecutiveComponents());
		Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getApplicationDetails());
		Mockito.when(buildingBlocksRepository.findByMetricLevelIdAndMetricLevelAndMetricType("B6LV",
				MetricLevel.COMPONENT, MetricType.WORK_IN_PROGRESS)).thenReturn(getBuildingBlocksList());
		wipAnalysis.processComponentDetailsMetrics();
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

	private BuildingBlocks buildingBlockMetricsDetail() {
		BuildingBlocks buildingBlockMetricsDetail = new BuildingBlocks();
		buildingBlockMetricsDetail.setMetricLevelId("B6LV");
		MetricSummary summary = new MetricSummary();
		summary.setLastScanned(new Date());
		summary.setLastUpdated(new Date());
		summary.setName("work-in-progress");
		summary.setReportingComponents(1);
		summary.setTotalComponents(1);
		summary.setTrendSlope(2.3);
		summary.setCounts(settingCounts());
		buildingBlockMetricsDetail.setMetrics(Arrays.asList(summary));
		return buildingBlockMetricsDetail;
	}

	private List<BuildingBlocks> getBuildingBlocksList() {
		List<BuildingBlocks> buildingBlockComponentSummaryResponseList = new ArrayList<BuildingBlocks>();
		BuildingBlocks buildingBlockComponentSummaryResponse = new BuildingBlocks();
		buildingBlockComponentSummaryResponse.setMetricLevelId("B6LV");
		buildingBlockComponentSummaryResponse.setLob("NTS");
		buildingBlockComponentSummaryResponse.setName("work-in-progress");
		buildingBlockComponentSummaryResponse.setPoc("some poc");
		buildingBlockComponentSummaryResponse.setTotalComponents(1);
		buildingBlockComponentSummaryResponse.setUrl("some url");
		List<MetricSummary> metrics = new ArrayList<>(Arrays.asList(getMetricSummary()));
		buildingBlockComponentSummaryResponse.setMetrics(metrics);
		return buildingBlockComponentSummaryResponseList;
	}

	private MetricSummary getMetricSummary() {

		MetricSummary summary = new MetricSummary();
		summary.setLastScanned(new Date());
		summary.setLastUpdated(new Date());
		summary.setName("work-in-progress");
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
		label.put("type", "Story");
		count.setLabel(label);
		count.setValue(34.0);
		counts.add(count);

		count = new MetricCount();
		Map<String, String> label1 = new HashMap<>();
		label1.put("type", "Epic");
		count.setLabel(label1);
		count.setValue(2.0);
		counts.add(count);

		count = new MetricCount();
		Map<String, String> label2 = new HashMap<>();
		label2.put("type", "Bugs");
		count.setLabel(label2);
		count.setValue(2.0);
		counts.add(count);

		count = new MetricCount();
		Map<String, String> label3 = new HashMap<>();
		label3.put("type", "Other");
		count.setLabel(label3);
		count.setValue(2.0);
		counts.add(count);

		return counts;
	}

	private List<JiraDetailsFinal> getJiraDetailsList() {
		List<JiraDetailsFinal> rtList = new ArrayList<>();
		List<JiraInfo> jiraInfoList = new ArrayList<>();
		JiraDetailsFinal jiraDetailsFinalObject = new JiraDetailsFinal();
		JiraInfo jiraInfoObject = new JiraInfo();
		jiraInfoObject.setProjectKey("test Project Key");
		jiraInfoObject.setProjectName("projectNAme");
		jiraInfoObject.setTeamId("teamID");
		jiraInfoList.add(jiraInfoObject);
		jiraDetailsFinalObject.setAppId("B6LV");
		jiraDetailsFinalObject.setJiraInfo(jiraInfoList);
		rtList.add(jiraDetailsFinalObject);
		return rtList;

	}

	private FeatureUserStory getUserStoryListSorted() {
		FeatureUserStory rt = new FeatureUserStory();
		rt.setStatusCategory("In Progress");
		rt.setsTypeName("Story");
		rt.setCreationDate("2018-06-01T16:26:44.0000000");
		return rt;
	}

	private FeatureUserStory getUserStoryListSorted_1() {
		return null;
	}

	private List<FeatureUserStory> getUserStoryList() {
		List<FeatureUserStory> rtList = new ArrayList<>();
		FeatureUserStory rt = new FeatureUserStory();
		FeatureUserStory rt1 = new FeatureUserStory();
		FeatureUserStory rt2 = new FeatureUserStory();
		FeatureUserStory rt3 = new FeatureUserStory();
		FeatureUserStory rt4 = new FeatureUserStory();

		rt.setsTypeName("Bug");
		rt.setsStatus("In Progress");
		rt.setCreationDate("2018-03-01T16:26:44.0000000");
		rt.setStatusCategory("In Progress");

		rt1.setsTypeName("Story");
		rt1.setsStatus("In Progress");
		rt1.setCreationDate("2018-03-01T16:26:44.0000000");
		rt1.setStatusCategory("In Progress");

		rt2.setsTypeName("Epic");
		rt2.setsStatus("In Progress");
		rt2.setCreationDate("2018-03-01T16:26:44.0000000");
		rt2.setStatusCategory("In Progress");

		rt3.setsTypeName("Task");
		rt3.setsStatus("In Progress");
		rt3.setCreationDate("2018-03-01T16:26:44.0000000");
		rt3.setStatusCategory("In Progress");

		rt4.setsTypeName("VZAgile Story");
		rt4.setsStatus("In Progress");
		rt4.setCreationDate("2018-03-01T16:26:44.0000000");
		rt4.setStatusCategory("In Progress");

		rtList.add(rt);
		rtList.add(rt1);
		rtList.add(rt2);
		rtList.add(rt3);
		rtList.add(rt4);

		return rtList;
	}

	private Dashboard createDashboard() {
		Dashboard dashboard = new Dashboard(null, null, null, null, null, null);
		dashboard.setAppId("B6LV");
		Application app = new Application();
		app.setLineOfBusiness("GTS");
		app.setName("B6LV");
		app.setOwner("aqwerds");
		dashboard.setApplication(app);
		return dashboard;
	}

	private List<String> getAppList() {
		List<String> appList = new ArrayList<>();
		appList.add("B6LV");
		appList.add("D40V");
		return appList;
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
		executiveMetrics.setMetricsName("work-in-progress");
		ExecutiveModuleMetrics module = new ExecutiveModuleMetrics();
		module.setLastScanned(new Date());
		module.setLastUpdated(new Date());
		module.setModuleName("projectName");
		module.setTrendSlope((double) 0);

		List<SeriesCount> counts = new ArrayList<SeriesCount>();
		SeriesCount series1 = new SeriesCount();
		Map<String, String> label = new HashMap<String, String>();
		label.put("type", "Story");
		series1.setLabel(label);
		series1.setCount((long) 232);
		counts.add(series1);

		SeriesCount series2 = new SeriesCount();
		Map<String, String> label1 = new HashMap<String, String>();
		label1.put("type", "Epic");
		series2.setLabel(label1);
		series2.setCount((long) 5);
		counts.add(series2);

		SeriesCount series3 = new SeriesCount();
		Map<String, String> label2 = new HashMap<String, String>();
		label2.put("type", "Bugs");
		series3.setLabel(label2);
		series3.setCount((long) 5);
		counts.add(series3);

		SeriesCount series4 = new SeriesCount();
		Map<String, String> label3 = new HashMap<String, String>();
		label3.put("type", "Task");
		series4.setLabel(label3);
		series4.setCount((long) 5);
		counts.add(series4);

		SeriesCount series5 = new SeriesCount();
		Map<String, String> label4 = new HashMap<String, String>();
		label4.put("type", "VZAgile Story");
		series5.setLabel(label4);
		series5.setCount((long) 5);
		counts.add(series5);

		SeriesCount series6 = new SeriesCount();
		Map<String, String> label5 = new HashMap<String, String>();
		label5.put("type", "Other");
		series6.setLabel(label5);
		series6.setCount((long) 5);
		counts.add(series6);

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
		summary.setName("work-in-progress");
		summary.setReportingComponents(1);
		summary.setTotalComponents(1);
		summary.setTrendSlope(2.3);
		List<MetricCount> counts = new ArrayList<MetricCount>();

		MetricCount count = new MetricCount();
		Map<String, String> label = new HashMap<String, String>();
		label.put("type", "Story");
		count.setLabel(label);
		count.setValue(232);
		counts.add(count);

		MetricCount count1 = new MetricCount();
		Map<String, String> label1 = new HashMap<String, String>();
		label1.put("type", "Epic");
		count1.setLabel(label1);
		count1.setValue(5);
		counts.add(count1);

		MetricCount count2 = new MetricCount();
		Map<String, String> label2 = new HashMap<String, String>();
		label2.put("type", "Bug");
		count2.setLabel(label2);
		count2.setValue(5);

		MetricCount count3 = new MetricCount();
		Map<String, String> label3 = new HashMap<String, String>();
		label3.put("type", "Other");
		count3.setLabel(label3);
		count3.setValue(5);
		counts.add(count3);

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

	private CollectorStatus getCollectorStatusJiraUSerStory() {
		CollectorStatus collectorStatus = new CollectorStatus();
		collectorStatus.setCollectorName("JiraUserStory");
		collectorStatus.setLastUpdated(new Date());
		collectorStatus.setType(CollectorType.JiraUserStory);
		return collectorStatus;
	}

	private CollectorStatus getCollectorStatus() {
		CollectorStatus collectorStatus = new CollectorStatus();
		collectorStatus.setCollectorName("MetricsProcessor");
		collectorStatus.setLastUpdated(new Date());
		collectorStatus.setType(CollectorType.MetricsProcessor);
		return collectorStatus;
	}

	@Test
	public void removeUnusedWipDetails() {

		try {
			List<String> appIds = new ArrayList<>();
			appIds.add("TestMe");

			Mockito.when(vastDetailsDAO.getAllAppIds(mongoClient)).thenReturn(appIds);

			List<ExecutiveComponents> wipDataList = new ArrayList<>();

			ExecutiveComponents executiveComponents = new ExecutiveComponents();
			wipDataList.add(executiveComponents);

			Mockito.when(executiveComponentRepository.getNotUsedAppIdList(appIds, "work-in-progress"))
					.thenReturn(wipDataList);
			Mockito.when(wipDetailsDAO.getMongoClient()).thenReturn(mongoClient);
			wipAnalysis.removeUnusedWipDetails();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void removeUnusedWipDetails_1() throws Exception {
		Mockito.when(wipDetailsDAO.getMongoClient()).thenThrow(new NullPointerException());
		wipAnalysis.removeUnusedWipDetails();
	}

}
