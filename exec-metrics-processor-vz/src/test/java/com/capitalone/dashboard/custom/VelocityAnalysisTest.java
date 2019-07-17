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

import com.capitalone.dashboard.collector.MetricsProcessorSettings;
import com.capitalone.dashboard.dao.DashboardDetailsDAO;
import com.capitalone.dashboard.dao.VastDetailsDAO;
import com.capitalone.dashboard.dao.VelocityDetailsDAO;
import com.capitalone.dashboard.exec.model.MetricCount;
import com.capitalone.dashboard.exec.model.MetricLevel;
import com.capitalone.dashboard.exec.model.MetricSummary;
import com.capitalone.dashboard.exec.model.MetricTimeSeriesElement;
import com.capitalone.dashboard.exec.model.MetricType;
import com.capitalone.dashboard.exec.model.Application;
import com.capitalone.dashboard.exec.model.ApplicationDetails;
import com.capitalone.dashboard.exec.model.BuildingBlocks;
import com.capitalone.dashboard.exec.model.CollectorStatus;
import com.capitalone.dashboard.exec.model.CollectorType;
import com.capitalone.dashboard.exec.model.Dashboard;
import com.capitalone.dashboard.exec.model.ExecutiveComponents;
import com.capitalone.dashboard.exec.model.ExecutiveMetrics;
import com.capitalone.dashboard.exec.model.ExecutiveMetricsSeries;
import com.capitalone.dashboard.exec.model.ExecutiveModuleMetrics;
import com.capitalone.dashboard.exec.model.ExecutiveResponse;
import com.capitalone.dashboard.exec.model.ExecutiveSummaryList;
import com.capitalone.dashboard.exec.model.FeatureStatusCategoryRequest;
import com.capitalone.dashboard.exec.model.FeatureUserStory;
import com.capitalone.dashboard.exec.model.InstanceCollectorStatus;
import com.capitalone.dashboard.exec.model.JiraDetailsFinal;
import com.capitalone.dashboard.exec.model.JiraInfo;
import com.capitalone.dashboard.exec.model.MetricsDetail;
import com.capitalone.dashboard.exec.model.PortfolioResponse;
import com.capitalone.dashboard.exec.model.SeriesCount;
import com.capitalone.dashboard.exec.repository.ApplicationDetailsRepository;
import com.capitalone.dashboard.exec.repository.BuildingBlocksRepository;
import com.capitalone.dashboard.exec.repository.CollectorStatusRepository;
import com.capitalone.dashboard.exec.repository.ExecutiveComponentRepository;
import com.capitalone.dashboard.exec.repository.ExecutiveSummaryListRepository;
import com.capitalone.dashboard.exec.repository.MetricsDetailRepository;
import com.capitalone.dashboard.exec.repository.PortfolioResponseRepository;
import com.capitalone.dashboard.utils.GenericMethods;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

@RunWith(MockitoJUnitRunner.class)
public class VelocityAnalysisTest {

	@InjectMocks
	private VelocityAnalysis velocityAnalysis;
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
	@Mock
	private MetricsProcessorSettings metricsProcessorSettings;

	@Test
	public void testProcessVelocityDetails() throws Exception {

		Mockito.when(velocityDetailsDAO.getMongoClient()).thenReturn(mongoClient);
		Mockito.when(velocityDetailsDAO.getEntireAppList(Mockito.any(MongoClient.class))).thenReturn(getAppList());
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(getExecutiveComponents());
		Mockito.when(collectorStatusRepository.findByType(CollectorType.MetricsProcessor))
				.thenReturn(getCollectorStatus());
		Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getApplicationDetails());
		Mockito.when(velocityDetailsDAO.getEntireProjectList(Mockito.any(MongoClient.class), Mockito.anyString()))
				.thenReturn(getJiraDetailsList());
		Mockito.when(velocityDetailsDAO.getUserStoriesList(Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong(),
				Mockito.any(MongoClient.class))).thenReturn(getUserStoryList());
		Mockito.when(velocityDetailsDAO.getLatestUserStorySorted(Mockito.anyString(), Mockito.any(MongoClient.class)))
				.thenReturn(getUserStoryListSorted());
		velocityAnalysis.processExecutiveMetricsDetails();
	}

	@Test
	public void testProcessVelocityDetails_1() throws Exception {

		Mockito.when(velocityDetailsDAO.getMongoClient()).thenReturn(mongoClient);
		Mockito.when(velocityDetailsDAO.getEntireAppList(Mockito.any(MongoClient.class))).thenReturn(getAppList());
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(getExecutiveComponents());
		Mockito.when(collectorStatusRepository.findByType(CollectorType.MetricsProcessor))
				.thenReturn(getCollectorStatus());
		Mockito.when(collectorStatusRepository.findByType(CollectorType.JiraUserStory))
				.thenReturn(getCollectorStatusJiraUSerStory());
		Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getApplicationDetails());
		Mockito.when(velocityDetailsDAO.getEntireProjectList(Mockito.any(MongoClient.class), Mockito.anyString()))
				.thenReturn(getJiraDetailsList());
		Mockito.when(velocityDetailsDAO.getUserStoriesList(Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong(),
				Mockito.any(MongoClient.class))).thenReturn(getUserStoryList());
		Mockito.when(velocityDetailsDAO.getLatestUserStorySorted(Mockito.anyString(), Mockito.any(MongoClient.class)))
				.thenReturn(getUserStoryListSorted_1());
		velocityAnalysis.processExecutiveMetricsDetails();
	}

	@Test
	public void testProcessVelocityDetails_2() throws Exception {
		Mockito.when(velocityAnalysis.processExecutiveMetricsDetails()).thenThrow(new NullPointerException());
		velocityAnalysis.processExecutiveMetricsDetails();
	}

	@Test
	public void testProcessExecutiveDetailsMetrics() {
		Mockito.when(executiveSummaryListRepository.findAll()).thenReturn(getExecutiveSummaryList());
		Mockito.when(collectorStatusRepository.findByType(CollectorType.JiraUserStory))
				.thenReturn(getCollectorStatusJiraUSerStory());
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(getExecutiveComponents());
		Mockito.when(portfolioResponseRepository.findByEid(Mockito.anyString())).thenReturn(getPortfolioResponse());
		velocityAnalysis.processExecutiveDetailsMetrics();
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

		velocityAnalysis.processExecutiveDetailsMetrics();
	}

	@Test
	public void testProcessBuildingBlockMetrics() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("B6LV");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getApplicationDetails());
		Mockito.when(metricsDetailRepository.findByMetricLevelIdAndLevelAndType("B6LV", MetricLevel.PRODUCT,
				MetricType.VELOCITY)).thenReturn(getMetricsDetail());
		Mockito.when(buildingBlocksRepository.findByMetricLevelIdAndMetricLevel("B6LV", MetricLevel.PRODUCT))
				.thenReturn(buildingBlockMetricsDetail());
		velocityAnalysis.processBuildingBlockMetrics();
	}

	@Test
	public void testProcessBuildingBlockMetrics_1() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("B6LV");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		velocityAnalysis.processBuildingBlockMetrics();
	}

	@Test
	public void testProcessBuildingBlockMetrics_2() throws Exception {
		Mockito.when(velocityAnalysis.processBuildingBlockMetrics()).thenThrow(new NullPointerException());
		velocityAnalysis.processBuildingBlockMetrics();
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
		velocityAnalysis.processMetricsDetailResponse();
	}

	@Test
	public void testProcessComponentDetailsMetrics() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("B6LV");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		Mockito.when(velocityDetailsDAO.getEntireProjectList(Mockito.any(MongoClient.class), Mockito.anyString()))
				.thenReturn(getJiraDetailsList());
		Mockito.when(velocityDetailsDAO.getMongoClient()).thenReturn(mongoClient);
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(getExecutiveComponents());
		Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getApplicationDetails());
		Mockito.when(buildingBlocksRepository.findByMetricLevelIdAndMetricLevelAndMetricType("B6LV",
				MetricLevel.COMPONENT, MetricType.VELOCITY)).thenReturn(getBuildingBlocksList());
		velocityAnalysis.processComponentDetailsMetrics();
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
		summary.setName("open-source-violations");
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
		buildingBlockComponentSummaryResponse.setName("open-source-violations");
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

	private List<JiraDetailsFinal> getJiraDetailsList() {
		List<JiraDetailsFinal> rtList = new ArrayList<>();
		List<JiraInfo> jiraInfoList = new ArrayList<>();
		JiraDetailsFinal jiraDetailsFinalObject = new JiraDetailsFinal();
		JiraInfo jiraInfoObject = new JiraInfo();
		jiraInfoObject.setProjectKey("projectKey");
		jiraInfoObject.setProjectName("projectName");
		jiraInfoObject.setTeamId("teamID");
		jiraInfoList.add(jiraInfoObject);
		jiraDetailsFinalObject.setAppId("B6LV");
		jiraDetailsFinalObject.setJiraInfo(jiraInfoList);
		rtList.add(jiraDetailsFinalObject);
		return rtList;

	}

	private FeatureUserStory getUserStoryListSorted() {
		FeatureUserStory rt = new FeatureUserStory();
		rt.setsProjectName("project Name");
		rt.setCreationDate("2018-06-03T16:26:44.0000000");
		rt.setsTypeName("Story");
		rt.setStatusCategory("Done");
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

		rt.setsTypeName("Story");
		rt.setsStatus("Done");
		rt.setsEstimate("5");
		rt.setsEstimateTime(7);
		rt.setCreationDate("2018-01-01T16:26:44.0000000");
		rt.setChangeDate("2018-01-01T16:26:44.0000000");
		rt.setsSprintEndDate("2018-02-11T16:26:44.0000000");
		rt.setsSprintBeginDate("2018-01-17T11:47:26.2690000");
		rt.setsSprintName("Sprint 76");
		rt.setStatusTransitionInfo(setStatusCategory());
		rt.setStoryPoints(8.0);
		rt.setTotalTimeTaken(711034000);
		rt.setStatusCategory("Done");
		rt.setCreationDate("2018-01-01T16:26:44.0000000");
		rt.setTestPhase("Production");

		rt1.setsId("ONEH-887");
		rt1.setsName("Name ");
		rt1.setsTypeName("New Feature");
		rt1.setsStatus("Done");
		rt1.setsEstimate("5");
		rt1.setsEstimateTime(7);
		rt1.setCreationDate("2018-01-01T16:26:44.0000000");
		rt1.setChangeDate("2018-01-01T16:26:44.0000000");
		rt1.setStatusTransitionInfo(setStatusCategory_1());
		rt1.setStoryPoints(0.0);
		rt1.setTotalTimeTaken(711034000);
		rt1.setStatusCategory("Done");
		rt1.setCreationDate("2018-01-01T16:26:44.0000000");
		rt1.setTestPhase("Production");

		rt2.setsTypeName("Enhancement");
		rt2.setsStatus("Done");
		rt2.setsState("Active");
		rt2.setsEstimate("5");
		rt2.setsEstimateTime(7);
		rt2.setCreationDate("2018-01-01T16:26:44.0000000");
		rt2.setChangeDate("2018-01-01T16:26:44.0000000");
		rt2.setStatusTransitionInfo(setStatusCategory_2());
		rt2.setStoryPoints(0.0);
		rt2.setTotalTimeTaken(711034000);
		rt2.setStatusCategory("Done");
		rt2.setCreationDate("2018-01-01T16:26:44.0000000");
		rt2.setTestPhase("Production");

		rt3.setsTypeName("Story");
		rt3.setsStatus("Done");
		rt3.setsEstimate("5");
		rt3.setsEstimateTime(7);
		rt3.setCreationDate("2018-01-01T16:26:44.0000000");
		rt3.setChangeDate("2018-01-01T16:26:44.0000000");
		rt3.setStatusTransitionInfo(setStatusCategory_3());
		rt3.setStoryPoints(0.0);
		rt3.setTotalTimeTaken(711034000);
		rt3.setStatusCategory("Done");
		rt3.setCreationDate("2018-01-01T16:26:44.0000000");
		rt3.setTestPhase("Production");

		rtList.add(rt);
		rtList.add(rt1);
		rtList.add(rt2);
		rtList.add(rt3);

		return rtList;
	}

	private List<FeatureStatusCategoryRequest> setStatusCategory() {
		List<FeatureStatusCategoryRequest> rtVal = new ArrayList<>();
		FeatureStatusCategoryRequest val = new FeatureStatusCategoryRequest();
		FeatureStatusCategoryRequest val1 = new FeatureStatusCategoryRequest();
		FeatureStatusCategoryRequest val2 = new FeatureStatusCategoryRequest();
		FeatureStatusCategoryRequest val3 = new FeatureStatusCategoryRequest();

		String timeStamp = "2018-02-01T15:14:15.260+05:30";
		String timeStamp1 = "2018-02-02T15:14:15.260+05:30";
		String timeStamp2 = "2018-02-03T15:14:15.260+05:30";
		String timeStamp3 = "2018-02-02T02:14:15.260+05:30";

		val.setStatus("To Do");
		val.setStatusCategory("To Do");
		val.setTimeStamp(timeStamp);

		val1.setStatus("In Progress");
		val1.setStatusCategory("In Progress");
		val1.setTimeStamp(timeStamp1);

		val2.setStatus("Done");
		val2.setStatusCategory("Done");
		val2.setTimeStamp(timeStamp2);

		val3.setStatus("Backlog");
		val3.setStatusCategory("Backlog");
		val3.setTimeStamp(timeStamp3);

		rtVal.add(val);
		rtVal.add(val1);
		rtVal.add(val2);
		rtVal.add(val3);

		return rtVal;
	}

	private List<FeatureStatusCategoryRequest> setStatusCategory_1() {
		List<FeatureStatusCategoryRequest> rtVal = new ArrayList<>();
		FeatureStatusCategoryRequest val1 = new FeatureStatusCategoryRequest();
		FeatureStatusCategoryRequest val2 = new FeatureStatusCategoryRequest();

		String timeStamp1 = "2018-02-02T15:14:15.260+05:30";
		String timeStamp2 = "2018-02-03T15:14:15.260+05:30";

		val1.setStatus("In Progress");
		val1.setStatusCategory("In Progress");
		val1.setTimeStamp(timeStamp1);

		val2.setStatus("Done");
		val2.setStatusCategory("Done");
		val2.setTimeStamp(timeStamp2);

		rtVal.add(val1);
		rtVal.add(val2);

		return rtVal;
	}

	private List<FeatureStatusCategoryRequest> setStatusCategory_2() {
		List<FeatureStatusCategoryRequest> rtVal = new ArrayList<>();
		FeatureStatusCategoryRequest val2 = new FeatureStatusCategoryRequest();
		FeatureStatusCategoryRequest val3 = new FeatureStatusCategoryRequest();

		String timeStamp2 = "2018-02-03T15:14:15.260+05:30";
		String timeStamp3 = "2018-02-02T02:14:15.260+05:30";

		val2.setStatus("Done");
		val2.setStatusCategory("Done");
		val2.setTimeStamp(timeStamp2);

		val3.setStatus("Backlog");
		val3.setStatusCategory("Backlog");
		val3.setTimeStamp(timeStamp3);

		rtVal.add(val2);
		rtVal.add(val3);

		return rtVal;
	}

	private List<FeatureStatusCategoryRequest> setStatusCategory_3() {
		List<FeatureStatusCategoryRequest> rtVal = new ArrayList<>();
		FeatureStatusCategoryRequest val = new FeatureStatusCategoryRequest();
		FeatureStatusCategoryRequest val1 = new FeatureStatusCategoryRequest();
		FeatureStatusCategoryRequest val2 = new FeatureStatusCategoryRequest();
		FeatureStatusCategoryRequest val3 = new FeatureStatusCategoryRequest();
		FeatureStatusCategoryRequest val4 = new FeatureStatusCategoryRequest();
		FeatureStatusCategoryRequest val5 = new FeatureStatusCategoryRequest();
		FeatureStatusCategoryRequest val6 = new FeatureStatusCategoryRequest();
		FeatureStatusCategoryRequest val7 = new FeatureStatusCategoryRequest();

		String timeStamp = "2018-06-01T15:14:15.260+05:30";
		String timeStamp1 = "2018-06-02T15:14:15.260+05:30";
		String timeStamp2 = "2018-06-03T15:14:15.260+05:30";
		String timeStamp3 = "2018-06-04T02:14:15.260+05:30";
		String timeStamp4 = "2018-0-05T02:14:15.260+05:30";
		String timeStamp5 = "2018-06-06T02:14:15.260+05:30";
		String timeStamp6 = "2018-06-07T02:14:15.260+05:30";
		String timeStamp7 = "2018-06-08T02:14:15.260+05:30";

		val.setStatus("To Do");
		val.setStatusCategory("To Do");
		val.setTimeStamp(timeStamp);

		val1.setStatus("In Progress");
		val1.setStatusCategory("In Progress");
		val1.setTimeStamp(timeStamp1);

		val2.setStatus("To Do");
		val2.setStatusCategory("To Do");
		val2.setTimeStamp(timeStamp2);

		val3.setStatus("Done");
		val3.setStatusCategory("Done");
		val3.setTimeStamp(timeStamp3);

		val4.setStatus("To Do");
		val4.setStatusCategory("To Do");
		val4.setTimeStamp(timeStamp4);

		val5.setStatus("Done");
		val5.setStatusCategory("Done");
		val5.setTimeStamp(timeStamp5);

		val6.setStatus("In Progress");
		val6.setStatusCategory("In Progress");
		val6.setTimeStamp(timeStamp6);

		val7.setStatus("Done");
		val7.setStatusCategory("Done");
		val7.setTimeStamp(timeStamp7);

		rtVal.add(val);
		rtVal.add(val1);
		rtVal.add(val2);
		rtVal.add(val3);
		rtVal.add(val4);
		rtVal.add(val5);
		rtVal.add(val6);
		rtVal.add(val7);

		return rtVal;
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
		executiveMetrics.setMetricsName("open-source-violations");
		ExecutiveModuleMetrics module = new ExecutiveModuleMetrics();
		module.setLastScanned(new Date());
		module.setLastUpdated(new Date());
		module.setModuleName("projectName");
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
		ExecutiveMetricsSeries executiveMetricsSeries50 = new ExecutiveMetricsSeries();
		executiveMetricsSeries50.setDaysAgo(50);
		executiveMetricsSeries50.setTimeStamp(123456789l);
		executiveMetricsSeries50.setCounts(counts);
		ExecutiveMetricsSeries executiveMetricsSeries0 = new ExecutiveMetricsSeries();
		executiveMetricsSeries0.setTimeStamp(123456789l);
		executiveMetricsSeries0.setCounts(counts);
		List<ExecutiveMetricsSeries> executiveMetricsSeriesList = new ArrayList<ExecutiveMetricsSeries>();
		executiveMetricsSeriesList.add(executiveMetricsSeries30);
		executiveMetricsSeriesList.add(executiveMetricsSeries60);
		executiveMetricsSeriesList.add(executiveMetricsSeries90);
		executiveMetricsSeriesList.add(executiveMetricsSeries50);
		executiveMetricsSeriesList.add(executiveMetricsSeries0);
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
		count.setValue(232.0);
		counts.add(count);

		MetricCount count1 = new MetricCount();
		Map<String, String> label1 = new HashMap<String, String>();
		label1.put("type", "Total Stories");
		count1.setLabel(label1);
		count1.setValue(5.0);
		counts.add(count1);

		MetricCount count2 = new MetricCount();
		Map<String, String> label2 = new HashMap<String, String>();
		label2.put("type", "Total Story Points");
		count2.setLabel(label2);
		count2.setValue(5.0);
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
	public void removeUnusedVelocityDetails() {

		try {
			List<String> appIds = new ArrayList<>();
			appIds.add("TestMe");

			Mockito.when(vastDetailsDAO.getAllAppIds(mongoClient)).thenReturn(appIds);

			List<ExecutiveComponents> velocityDataList = new ArrayList<>();

			ExecutiveComponents executiveComponents = new ExecutiveComponents();
			velocityDataList.add(executiveComponents);

			Mockito.when(executiveComponentRepository.getNotUsedAppIdList(appIds, "open-source-violations"))
					.thenReturn(velocityDataList);
			Mockito.when(velocityDetailsDAO.getMongoClient()).thenReturn(mongoClient);
			velocityAnalysis.removeUnusedVelocityDetails();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void removeUnusedVelocityDetails_1() throws Exception {
		Mockito.when(velocityDetailsDAO.getMongoClient()).thenThrow(new NullPointerException());
		velocityAnalysis.removeUnusedVelocityDetails();
	}

}
