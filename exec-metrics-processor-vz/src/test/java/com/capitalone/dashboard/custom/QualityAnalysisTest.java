package com.capitalone.dashboard.custom;

import static org.mockito.Mockito.when;

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
import com.capitalone.dashboard.dao.QualityDetailsDAO;
import com.capitalone.dashboard.dao.ServiceNowDAO;
import com.capitalone.dashboard.dao.VastDetailsDAO;
import com.capitalone.dashboard.exec.model.MetricCount;
import com.capitalone.dashboard.exec.model.MetricLevel;
import com.capitalone.dashboard.exec.model.MetricSummary;
import com.capitalone.dashboard.exec.model.MetricTimeSeriesElement;
import com.capitalone.dashboard.exec.model.MetricType;
import com.capitalone.dashboard.exec.model.ApplicationDetails;
import com.capitalone.dashboard.exec.model.BuildingBlocks;
import com.capitalone.dashboard.exec.model.CollectorStatus;
import com.capitalone.dashboard.exec.model.CollectorType;
import com.capitalone.dashboard.exec.model.ExecutiveComponents;
import com.capitalone.dashboard.exec.model.ExecutiveMetrics;
import com.capitalone.dashboard.exec.model.ExecutiveMetricsSeries;
import com.capitalone.dashboard.exec.model.ExecutiveModuleMetrics;
import com.capitalone.dashboard.exec.model.ExecutiveResponse;
import com.capitalone.dashboard.exec.model.ExecutiveSummaryList;
import com.capitalone.dashboard.exec.model.FeatureUserStory;
import com.capitalone.dashboard.exec.model.InstanceCollectorStatus;
import com.capitalone.dashboard.exec.model.JiraDetailsFinal;
import com.capitalone.dashboard.exec.model.JiraInfo;
import com.capitalone.dashboard.exec.model.MetricsDetail;
import com.capitalone.dashboard.exec.model.PortfolioResponse;
import com.capitalone.dashboard.exec.model.SeriesCount;
import com.capitalone.dashboard.exec.model.ServiceNowIssues;
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
public class QualityAnalysisTest {

	@InjectMocks
	private QualityAnalysis qualityAnalysis;
	@Mock
	private ExecutiveSummaryListRepository executiveSummaryListRepository;
	@Mock
	private QualityDetailsDAO qualityDetailsDAO;
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
	private MetricsProcessorSettings metricsSettings;
	@Mock
	private VastDetailsDAO vastDetailsDAO;
	@Mock
	private ServiceNowDAO serviceNowDAO;
	@Mock
	private GenericMethods genericMethods;

	private static final String QUALITY = "quality";
	private static final String CMIS = "cmis";
	private static final String SN = "serviceNow";
	private static final String CLOSEDSN = "closeServiceNow";
	private static final String OPENSN = "openServiceNow";
	private static final String SNONE = "serviceNowOne";
	private static final String SNTWO = "serviceNowTwo";
	private static final String SNTHREE = "serviceNowThree";
	private static final String SNFOUR = "serviceNowFour";
	private static final String SNONEOPEN = "openserviceNowOne";
	private static final String SNTWOOPEN = "openserviceNowTwo";
	private static final String SNTHREEOPEN = "openserviceNowThree";
	private static final String SNFOUROPEN = "openserviceNowFour";
	private static final String SNONECLOSED = "closeserviceNowOne";
	private static final String SNTWOCLOSED = "closeserviceNowTwo";
	private static final String SNTHREECLOSED = "closeserviceNowThree";
	private static final String SNFOURCLOSED = "closeserviceNowFour";
	private static final String PRIORITY = "priority";
	private static final String NORMAL = "normal";
	private static final String BLOCKER = "blocker";
	private static final String HIGH = "high";
	private static final String LOW = "low";
	private static final String TOTAL = "total";
	private static final String OPEN_CMIS = "opencmis";
	private static final String OPEN_NORMAL = "opennormal";
	private static final String OPEN_BLOCKER = "openblocker";
	private static final String OPEN_HIGH = "openhigh";
	private static final String OPEN_LOW = "openlow";
	private static final String OPEN_TOTAL = "opentotal";
	private static final String CLOSED_CMIS = "closecmis";
	private static final String CLOSED_NORMAL = "closenormal";
	private static final String CLOSED_BLOCKER = "closeblocker";
	private static final String CLOSED_HIGH = "closehigh";
	private static final String CLOSED_LOW = "closelow";
	private static final String CLOSED_TOTAL = "closetotal";
	private static final String CHANGEFAILURERATE = "changeFailureRate";
	private static final String TYPE = "type";

	@Test
	public void testProcessQualityDetails() throws Exception {
		List<String> excludeApps = new ArrayList<>();
		excludeApps.add("someapps");
		metricsSettings.setExcludedAppIds(excludeApps);
		Mockito.when(qualityDetailsDAO.getMongoClient()).thenReturn(mongoClient);
		Mockito.when(qualityDetailsDAO.getEntireAppList(Mockito.any(MongoClient.class))).thenReturn(getAppList());
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(getExecutiveComponents());
		Mockito.when(collectorStatusRepository.findByType(CollectorType.MetricsProcessor))
				.thenReturn(getCollectorStatus());
		// Mockito.when(collectorStatusRepository.findByType(CollectorType.CMIS)).thenReturn(getCollectorStatusCMIS());
		// Mockito.when(collectorStatusRepository.findByType(CollectorType.JiraUserStory)).thenReturn(getCollectorStatusJiraUSerStory());
		Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getApplicationDetails());
		Mockito.when(qualityDetailsDAO.getEntireProjectList(Mockito.any(MongoClient.class), Mockito.anyString()))
				.thenReturn(getJiraDetailsList());
		Mockito.when(qualityDetailsDAO.getLatestUserStorySorted(Mockito.anyString(), Mockito.any(MongoClient.class)))
				.thenReturn(getUserStorySorted());
		Mockito.when(qualityDetailsDAO.isDataAvailableForQuality(Mockito.anyString(), Mockito.anyString(),
				Mockito.any(MongoClient.class))).thenReturn(true);
		Mockito.when(serviceNowDAO.isDataAvailableForSN(Mockito.anyString(), Mockito.anyLong(),
				Mockito.any(MongoClient.class))).thenReturn(true);
		Mockito.when(serviceNowDAO.getLatestServiceNowSorted(Mockito.anyString())).thenReturn(getServiceNowSorted());
		Mockito.when(serviceNowDAO.getSNDefectsList(Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong(),
				Mockito.any(MongoClient.class))).thenReturn(getSNDefectsList());
		Mockito.when(qualityDetailsDAO.getDefectsList(Mockito.anyString(), Mockito.anyString(),
				Mockito.any(MongoClient.class))).thenReturn(getUserStoryList());
		QualityAnalysis qualityAnalysis = new QualityAnalysis(qualityDetailsDAO,
				executiveComponentRepository, serviceNowDAO,
				metricsDetailRepository, applicationDetailsRepository,
				collectorStatusRepository,
				executiveSummaryListRepository,
				portfolioResponseRepository, buildingBlocksRepository,
				vastDetailsDAO, mongoTemplate, metricsSettings,
				 genericMethods);
		qualityAnalysis.processExecutiveMetricsDetails();
	}

	@Test
	public void testProcessQualityDetails_1() throws Exception {
		List<String> excludeApps = new ArrayList<>();
		excludeApps.add("someapps");
		metricsSettings.setExcludedAppIds(excludeApps);
		Mockito.when(qualityDetailsDAO.getMongoClient()).thenReturn(mongoClient);
		Mockito.when(qualityDetailsDAO.getEntireAppList(Mockito.any(MongoClient.class))).thenReturn(getAppList());
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(getExecutiveComponents());
		Mockito.when(collectorStatusRepository.findByType(CollectorType.MetricsProcessor))
				.thenReturn(getCollectorStatus());
		Mockito.when(collectorStatusRepository.findByType(CollectorType.CMIS)).thenReturn(getCollectorStatusCMIS());
		Mockito.when(collectorStatusRepository.findByType(CollectorType.JiraUserStory))
				.thenReturn(getCollectorStatusJiraUSerStory());
		Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getApplicationDetails());
		Mockito.when(qualityDetailsDAO.getEntireProjectList(Mockito.any(MongoClient.class), Mockito.anyString()))
				.thenReturn(getJiraDetailsList());
		Mockito.when(qualityDetailsDAO.getLatestUserStorySorted(Mockito.anyString(), Mockito.any(MongoClient.class)))
				.thenReturn(getUserStorySorted_1());
		Mockito.when(qualityDetailsDAO.isDataAvailableForQuality(Mockito.anyString(), Mockito.anyString(),
				Mockito.any(MongoClient.class))).thenReturn(true);
		Mockito.when(qualityDetailsDAO.getDefectsList(Mockito.anyString(), Mockito.anyString(),
				Mockito.any(MongoClient.class))).thenReturn(getUserStoryList());
		QualityAnalysis qualityAnalysis = new QualityAnalysis(qualityDetailsDAO,
				executiveComponentRepository, serviceNowDAO,
				metricsDetailRepository, applicationDetailsRepository,
				collectorStatusRepository,
				executiveSummaryListRepository,
				portfolioResponseRepository, buildingBlocksRepository,
				vastDetailsDAO, mongoTemplate, metricsSettings,
				 genericMethods);
		qualityAnalysis.processExecutiveMetricsDetails();
	}

	@Test
	public void testProcessExecutiveDetailsMetrics() {
		List<String> appIds = new ArrayList<>();
		appIds.add("D40V");
		Mockito.when(executiveSummaryListRepository.findAll()).thenReturn(getExecutiveSummaryList());
		Mockito.when(collectorStatusRepository.findByType(CollectorType.JiraUserStory))
				.thenReturn(getCollectorStatusJiraUSerStory());
		// Mockito.when(executiveSummaryListRepository.findByEid(Mockito.anyString())).thenReturn(getExecutiveSummary());
		// Mockito.when(metricDetailResponseRepository.findByAppIdAndMetricsName(Mockito.anyString(),Mockito.anyString())).thenReturn(getMetricsDetail());
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(getExecutiveComponents());
		Mockito.when(portfolioResponseRepository.findByEid(Mockito.anyString())).thenReturn(getPortfolioResponse());
		// Mockito.when(qualityDetailsDAO.getEntireProjectList(Mockito.any(MongoClient.class),
		// Mockito.anyString())).thenReturn(getJiraDetailsList());
		Mockito.when(qualityDetailsDAO.getMongoClient()).thenReturn(mongoClient);
		// Mockito.when(serviceNowDAO.isDataAvailableForSN(Mockito.anyString(),Mockito.anyLong(),
		// Mockito.any(MongoClient.class))).thenReturn(true);
		qualityAnalysis.processExecutiveDetailsMetrics();
	}

	/*
	 * @Test public void testProcessExecutiveDetailsMetrics_1() {
	 * Mockito.when(executiveSummaryListRepository.findAll()).thenReturn(
	 * getExecutiveSummaryList());
	 * Mockito.when(collectorStatusRepository.findByType(CollectorType.
	 * JiraUserStory)).thenReturn(getCollectorStatusJiraUSerStory()); //
	 * Mockito.when(metricDetailResponseRepository.findByAppIdAndMetricsName(
	 * Mockito.anyString(),Mockito.anyString())).thenReturn(
	 * getMetricsDetail_1());
	 * Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.
	 * anyString(),Mockito.anyString())).thenReturn(getExecutiveComponents_1());
	 * Mockito.when(portfolioResponseRepository.findByEid(Mockito.anyString())).
	 * thenReturn(getPortfolioResponse());
	 * qualityAnalysis.processExecutiveDetailsMetrics(); }
	 */

	@Test
	public void testProcessMetricPortfolioDetailResponse_1() {
		ExecutiveSummaryList executiveSummaryList = new ExecutiveSummaryList();
		List<String> appIds = new ArrayList<>();
		executiveSummaryList.setAppId(appIds);
		executiveSummaryList.setTotalApps(10);
		executiveSummaryList.setConfiguredApps(6);
		Mockito.when(executiveSummaryListRepository.findAll()).thenReturn(Arrays.asList(executiveSummaryList));

		qualityAnalysis.processExecutiveDetailsMetrics();
	}

	@Test
	public void testProcessBuildingBlockMetrics() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("B6LV");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getApplicationDetails());
		Mockito.when(metricsDetailRepository.findByMetricLevelIdAndLevelAndType("B6LV",
				MetricLevel.PRODUCT, MetricType.QUALITY))
				.thenReturn(getMetricsDetail());
		Mockito.when(buildingBlocksRepository.findByMetricLevelIdAndMetricLevel("B6LV",
				MetricLevel.PRODUCT))
				.thenReturn(buildingBlockMetricsDetail());
		qualityAnalysis.processBuildingBlockMetrics();
	}

	@Test
	public void testProcessBuildingBlockMetrics_1() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("B6LV");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		qualityAnalysis.processBuildingBlockMetrics();
	}

	@Test
	public void testProcessMetricsDetailResponse() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("B6LV");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric("B6LV", QUALITY))
				.thenReturn(getExecutiveComponents());
		Mockito.when(metricsDetailRepository.findByMetricLevelIdAndLevelAndType("B6LV",
				MetricLevel.PRODUCT, MetricType.QUALITY))
				.thenReturn(getMetricsDetail());
		// Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getApplicationDetails());
		Mockito.when(genericMethods.processAppCriticality(Mockito.anyString())).thenReturn("Critical");
		qualityAnalysis.processMetricsDetailResponse();
	}

	@Test
	public void testProcessMetricsDetailResponse_1() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("B6LV");
		ExecutiveComponents executiveComponents = new ExecutiveComponents();
		executiveComponents.setAppId("B6LV");
		executiveComponents.setAppName("ACSS");
		ExecutiveMetrics executiveMetrics = new ExecutiveMetrics();
		executiveMetrics.setLastScanned(new Date());
		executiveMetrics.setLastUpdated(new Date());
		executiveMetrics.setMetricsName("quality");
		executiveMetrics.setModules(new ArrayList<>());
		executiveMetrics.setTrendSlope(0.2);
		executiveComponents.setMetrics(Arrays.asList(executiveMetrics));
		executiveComponents.setTeamBoardLink("some link");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(executiveComponents);
		Mockito.when(genericMethods.processAppCriticality(Mockito.anyString())).thenReturn("Critical");
		qualityAnalysis.processMetricsDetailResponse();
	}

	@Test
	public void testProcessComponentDetailsMetrics() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("B6LV");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(getExecutiveComponents());
		Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getApplicationDetails());
		Mockito.when(buildingBlocksRepository.findByMetricLevelIdAndMetricLevelAndMetricType("B6LV",
				MetricLevel.COMPONENT, MetricType.QUALITY)).thenReturn(getBuildingBlocksList());
		qualityAnalysis.processComponentDetailsMetrics();
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

	private ServiceNowIssues getServiceNowSorted() {
		ServiceNowIssues sn = new ServiceNowIssues();
		sn.setActive(true);
		sn.setAppId("D40V");
		sn.setAysPriority((long) 1);
		sn.setAysCreatedTimeStamp(1536397994l);
		return sn;
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
		summary.setName("quality");
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
		buildingBlockComponentSummaryResponse.setName("quality");
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
		summary.setName("quality");
		summary.setReportingComponents(1);
		summary.setTotalComponents(1);
		summary.setTrendSlope(2.3);
		summary.setCounts(settingCounts());

		return summary;
	}

	private List<MetricCount> settingCounts() {
		List<MetricCount> metricCountResponseList = new ArrayList<>();

		Map<String, String> normalDefects = new HashMap<>();
		normalDefects.put(PRIORITY, NORMAL);
		Map<String, String> blockerDefects = new HashMap<>();
		blockerDefects.put(PRIORITY, BLOCKER);
		Map<String, String> highDefects = new HashMap<>();
		highDefects.put(PRIORITY, HIGH);
		Map<String, String> lowDefects = new HashMap<>();
		lowDefects.put(PRIORITY, LOW);
		Map<String, String> totalDefects = new HashMap<>();
		totalDefects.put(PRIORITY, TOTAL);
		Map<String, String> cmisDefects = new HashMap<>();
		cmisDefects.put(PRIORITY, CMIS);
		Map<String, String> normalOpenDefects = new HashMap<>();
		normalOpenDefects.put(PRIORITY, OPEN_NORMAL);
		Map<String, String> blockerOpenDefects = new HashMap<>();
		blockerOpenDefects.put(PRIORITY, OPEN_BLOCKER);
		Map<String, String> highOpenDefects = new HashMap<>();
		highOpenDefects.put(PRIORITY, OPEN_HIGH);
		Map<String, String> lowOpenDefects = new HashMap<>();
		lowOpenDefects.put(PRIORITY, OPEN_LOW);
		Map<String, String> totalOpenDefects = new HashMap<>();
		totalOpenDefects.put(PRIORITY, OPEN_TOTAL);
		Map<String, String> cmisOpenDefects = new HashMap<>();
		cmisOpenDefects.put(PRIORITY, OPEN_CMIS);
		Map<String, String> normalClosedDefects = new HashMap<>();
		normalClosedDefects.put(PRIORITY, CLOSED_NORMAL);
		Map<String, String> blockerClosedDefects = new HashMap<>();
		blockerClosedDefects.put(PRIORITY, CLOSED_BLOCKER);
		Map<String, String> highClosedDefects = new HashMap<>();
		highClosedDefects.put(PRIORITY, CLOSED_HIGH);
		Map<String, String> lowClosedDefects = new HashMap<>();
		lowClosedDefects.put(PRIORITY, CLOSED_LOW);
		Map<String, String> totalClosedDefects = new HashMap<>();
		totalClosedDefects.put(PRIORITY, CLOSED_TOTAL);
		Map<String, String> cmisClosedDefects = new HashMap<>();
		cmisClosedDefects.put(PRIORITY, CLOSED_CMIS);
		Map<String, String> changeFailureRate = new HashMap<>();
		changeFailureRate.put(TYPE, CHANGEFAILURERATE);
		Map<String, String> labelSn = new HashMap<>();
		labelSn.put(PRIORITY, SN);
		Map<String, String> labelSnOne = new HashMap<>();
		labelSnOne.put(PRIORITY, SNONE);
		Map<String, String> labelSnTwo = new HashMap<>();
		labelSnTwo.put(PRIORITY, SNTWO);
		Map<String, String> labelSnThree = new HashMap<>();
		labelSnThree.put(PRIORITY, SNTHREE);
		Map<String, String> labelSnFour = new HashMap<>();
		labelSnFour.put(PRIORITY, SNFOUR);
		Map<String, String> closedLabel = new HashMap<>();
		closedLabel.put(PRIORITY, CLOSEDSN);
		Map<String, String> labelSnOneClosed = new HashMap<>();
		labelSnOneClosed.put(PRIORITY, SNONECLOSED);
		Map<String, String> labelSnTwoClosed = new HashMap<>();
		labelSnTwoClosed.put(PRIORITY, SNTWOCLOSED);
		Map<String, String> labelSnThreeClosed = new HashMap<>();
		labelSnThreeClosed.put(PRIORITY, SNTHREECLOSED);
		Map<String, String> labelSnFourClosed = new HashMap<>();
		labelSnFourClosed.put(PRIORITY, SNFOURCLOSED);
		Map<String, String> openLabel = new HashMap<>();
		openLabel.put(PRIORITY, OPENSN);
		Map<String, String> labelSnOneOpen = new HashMap<>();
		labelSnOneOpen.put(PRIORITY, SNONEOPEN);
		Map<String, String> labelSnTwoOpen = new HashMap<>();
		labelSnTwoOpen.put(PRIORITY, SNTWOOPEN);
		Map<String, String> labelSnThreeOpen = new HashMap<>();
		labelSnThreeOpen.put(PRIORITY, SNTHREEOPEN);
		Map<String, String> labelSnFourOpen = new HashMap<>();
		labelSnFourOpen.put(PRIORITY, SNFOUROPEN);

		MetricCount metricCountResponseNormal = new MetricCount();
		metricCountResponseNormal.setLabel(normalDefects);
		metricCountResponseNormal.setValue(5);
		metricCountResponseList.add(metricCountResponseNormal);
		MetricCount metricCountResponseBlocker = new MetricCount();
		metricCountResponseBlocker.setLabel(blockerDefects);
		metricCountResponseBlocker.setValue(4);
		metricCountResponseList.add(metricCountResponseBlocker);
		MetricCount metricCountResponseHigh = new MetricCount();
		metricCountResponseHigh.setLabel(highDefects);
		metricCountResponseHigh.setValue(10);
		metricCountResponseList.add(metricCountResponseHigh);
		MetricCount metricCountResponseLow = new MetricCount();
		metricCountResponseLow.setLabel(lowDefects);
		metricCountResponseLow.setValue(5);
		metricCountResponseList.add(metricCountResponseLow);
		MetricCount metricCountResponseTotal = new MetricCount();
		metricCountResponseTotal.setLabel(totalDefects);
		metricCountResponseTotal.setValue(24);
		metricCountResponseList.add(metricCountResponseTotal);
		MetricCount metricCountResponseCMIS = new MetricCount();
		metricCountResponseCMIS.setLabel(cmisDefects);
		metricCountResponseCMIS.setValue(25);
		metricCountResponseList.add(metricCountResponseCMIS);
		MetricCount metricCountOpenResponseNormal = new MetricCount();
		metricCountOpenResponseNormal.setLabel(normalOpenDefects);
		metricCountOpenResponseNormal.setValue(10);
		metricCountResponseList.add(metricCountOpenResponseNormal);
		MetricCount metricCountOpenResponseBlocker = new MetricCount();
		metricCountOpenResponseBlocker.setLabel(blockerOpenDefects);
		metricCountOpenResponseBlocker.setValue(10);
		metricCountResponseList.add(metricCountOpenResponseBlocker);
		MetricCount metricCountOpenResponseHigh = new MetricCount();
		metricCountOpenResponseHigh.setLabel(highOpenDefects);
		metricCountOpenResponseHigh.setValue(11);
		metricCountResponseList.add(metricCountOpenResponseHigh);
		MetricCount metricCountOpenResponseLow = new MetricCount();
		metricCountOpenResponseLow.setLabel(lowOpenDefects);
		metricCountOpenResponseLow.setValue(14);
		metricCountResponseList.add(metricCountOpenResponseLow);
		MetricCount metricCountOpenResponseTotal = new MetricCount();
		metricCountOpenResponseTotal.setLabel(totalOpenDefects);
		metricCountOpenResponseTotal.setValue(14);
		metricCountResponseList.add(metricCountOpenResponseTotal);
		MetricCount metricCountOpenResponseCMIS = new MetricCount();
		metricCountOpenResponseCMIS.setLabel(cmisOpenDefects);
		metricCountOpenResponseCMIS.setValue(17);
		metricCountResponseList.add(metricCountOpenResponseCMIS);
		MetricCount metricCountClosedResponseNormal = new MetricCount();
		metricCountClosedResponseNormal.setLabel(normalClosedDefects);
		metricCountClosedResponseNormal.setValue(25);
		metricCountResponseList.add(metricCountClosedResponseNormal);
		MetricCount metricCountClosedResponseBlocker = new MetricCount();
		metricCountClosedResponseBlocker.setLabel(blockerClosedDefects);
		metricCountClosedResponseBlocker.setValue(3);
		metricCountResponseList.add(metricCountClosedResponseBlocker);
		MetricCount metricCountClosedResponseHigh = new MetricCount();
		metricCountClosedResponseHigh.setLabel(highClosedDefects);
		metricCountClosedResponseHigh.setValue(4);
		metricCountResponseList.add(metricCountClosedResponseHigh);
		MetricCount metricCountClosedResponseLow = new MetricCount();
		metricCountClosedResponseLow.setLabel(lowClosedDefects);
		metricCountClosedResponseLow.setValue(5);
		metricCountResponseList.add(metricCountClosedResponseLow);
		MetricCount metricCountClosedResponseTotal = new MetricCount();
		metricCountClosedResponseTotal.setLabel(totalClosedDefects);
		metricCountClosedResponseTotal.setValue(8);
		metricCountResponseList.add(metricCountClosedResponseTotal);
		MetricCount metricCountClosedResponseCMIS = new MetricCount();
		metricCountClosedResponseCMIS.setLabel(cmisClosedDefects);
		metricCountClosedResponseCMIS.setValue(9);
		metricCountResponseList.add(metricCountClosedResponseCMIS);
		MetricCount metricCountResponseSN = new MetricCount();
		metricCountResponseSN.setLabel(labelSn);
		metricCountResponseSN.setValue(1600);
		metricCountResponseList.add(metricCountResponseSN);
		MetricCount metricCountResponseSNOne = new MetricCount();
		metricCountResponseSNOne.setLabel(labelSnOne);
		metricCountResponseSNOne.setValue(400);
		metricCountResponseList.add(metricCountResponseSNOne);
		MetricCount metricCountResponseSNTwo = new MetricCount();
		metricCountResponseSNTwo.setLabel(labelSnTwo);
		metricCountResponseSNTwo.setValue(400);
		metricCountResponseList.add(metricCountResponseSNTwo);
		MetricCount metricCountResponseSNThree = new MetricCount();
		metricCountResponseSNThree.setLabel(labelSnThree);
		metricCountResponseSNThree.setValue(400);
		metricCountResponseList.add(metricCountResponseSNThree);
		MetricCount metricCountResponseSNFour = new MetricCount();
		metricCountResponseSNFour.setLabel(labelSnFour);
		metricCountResponseSNFour.setValue(400);
		metricCountResponseList.add(metricCountResponseSNFour);
		MetricCount metricCountResponseSNOpen = new MetricCount();
		metricCountResponseSNOpen.setLabel(openLabel);
		metricCountResponseSNOpen.setValue(400);
		metricCountResponseList.add(metricCountResponseSNOpen);
		MetricCount metricCountResponseSNOneOpen = new MetricCount();
		metricCountResponseSNOneOpen.setLabel(labelSnOneOpen);
		metricCountResponseSNOneOpen.setValue(200);
		metricCountResponseList.add(metricCountResponseSNOneOpen);
		MetricCount metricCountResponseSNTwoOpen = new MetricCount();
		metricCountResponseSNTwoOpen.setLabel(labelSnTwoOpen);
		metricCountResponseSNTwoOpen.setValue(200);
		metricCountResponseList.add(metricCountResponseSNTwoOpen);
		MetricCount metricCountResponseSNThreeOpen = new MetricCount();
		metricCountResponseSNThreeOpen.setLabel(labelSnThreeOpen);
		metricCountResponseSNThreeOpen.setValue(200);
		metricCountResponseList.add(metricCountResponseSNThreeOpen);
		MetricCount metricCountResponseSNFourOpen = new MetricCount();
		metricCountResponseSNFourOpen.setLabel(labelSnFourOpen);
		metricCountResponseSNFourOpen.setValue(200);
		metricCountResponseList.add(metricCountResponseSNFourOpen);
		MetricCount metricCountResponseSNClosed = new MetricCount();
		metricCountResponseSNClosed.setLabel(closedLabel);
		metricCountResponseSNClosed.setValue(200);
		metricCountResponseList.add(metricCountResponseSNClosed);
		MetricCount metricCountResponseSNOneClosed = new MetricCount();
		metricCountResponseSNOneClosed.setLabel(labelSnOneClosed);
		metricCountResponseSNOneClosed.setValue(200);
		metricCountResponseList.add(metricCountResponseSNOneClosed);
		MetricCount metricCountResponseSNTwoClosed = new MetricCount();
		metricCountResponseSNTwoClosed.setLabel(labelSnTwoClosed);
		metricCountResponseSNTwoClosed.setValue(200);
		metricCountResponseList.add(metricCountResponseSNTwoClosed);
		MetricCount metricCountResponseSNThreeClosed = new MetricCount();
		metricCountResponseSNThreeClosed.setLabel(labelSnThreeClosed);
		metricCountResponseSNThreeClosed.setValue(200);
		metricCountResponseList.add(metricCountResponseSNThreeClosed);
		MetricCount metricCountResponseSNFourClosed = new MetricCount();
		metricCountResponseSNFourClosed.setLabel(labelSnFourClosed);
		metricCountResponseSNFourClosed.setValue(200);
		metricCountResponseList.add(metricCountResponseSNFourClosed);

		return metricCountResponseList;
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

	private FeatureUserStory getUserStorySorted() {
		FeatureUserStory rt = new FeatureUserStory();
		rt.setCreationDate("2018-01-01T16:26:44.0000000");
		rt.setDspEnvironment("prod");
		rt.setPriorityLevel("Blocker");
		rt.setResolutionType("Broken Code");
		rt.setSdlcEnvironment("Prod");
		rt.setsTypeName("Bug");
		return rt;
	}

	private FeatureUserStory getUserStorySorted_1() {
		return null;
	}

	private List<FeatureUserStory> getUserStoryList() {
		List<FeatureUserStory> rtList = new ArrayList<>();
		FeatureUserStory rt = new FeatureUserStory();
		FeatureUserStory rt1 = new FeatureUserStory();
		FeatureUserStory rt2 = new FeatureUserStory();
		FeatureUserStory rt3 = new FeatureUserStory();
		FeatureUserStory rt4 = new FeatureUserStory();
		FeatureUserStory rt5 = new FeatureUserStory();
		FeatureUserStory rt6 = new FeatureUserStory();
		FeatureUserStory rt7 = new FeatureUserStory();
		FeatureUserStory rt8 = new FeatureUserStory();
		rt.setPriorityLevel("Normal");
		rt4.setPriority("Critical");
		rt1.setPriorityLevel("blocker");
		rt5.setPriority("Medium");
		rt2.setPriorityLevel("High");
		rt6.setPriority("blocker");
		rt3.setPriorityLevel("low");
		rt7.setPriority("low");

		rt.setStatusCategory("Done");
		rt1.setStatusCategory("Done");
		rt2.setStatusCategory("Done");
		rt3.setStatusCategory("To Do");
		rt4.setStatusCategory("Done");
		rt5.setStatusCategory("Done");
		rt6.setStatusCategory("In Progress");
		rt7.setStatusCategory("Done");
		rt8.setStatusCategory("Done");

		rtList.add(rt);
		rtList.add(rt1);
		rtList.add(rt2);
		rtList.add(rt3);
		rtList.add(rt4);
		rtList.add(rt5);
		rtList.add(rt6);
		rtList.add(rt7);
		rtList.add(rt8);

		return rtList;
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
		executiveMetrics.setMetricsName("quality");
		ExecutiveModuleMetrics module = new ExecutiveModuleMetrics();
		module.setLastScanned(new Date());
		module.setLastUpdated(new Date());
		module.setModuleName("projectName");
		module.setTrendSlope((double) 0);

		List<SeriesCount> counts = new ArrayList<>();
		SeriesCount series = new SeriesCount();
		Map<String, String> label = new HashMap<>();
		label.put(PRIORITY, NORMAL);
		series.setLabel(label);
		series.setCount((long) 232);
		counts.add(series);

		SeriesCount series1 = new SeriesCount();
		Map<String, String> label1 = new HashMap<>();
		label1.put(PRIORITY, BLOCKER);
		series1.setLabel(label1);
		series1.setCount((long) 5);
		counts.add(series1);

		SeriesCount series2 = new SeriesCount();
		Map<String, String> label2 = new HashMap<String, String>();
		label2.put(PRIORITY, HIGH);
		series2.setLabel(label2);
		series2.setCount((long) 5);
		counts.add(series2);

		SeriesCount series3 = new SeriesCount();
		Map<String, String> label3 = new HashMap<>();
		label3.put(PRIORITY, LOW);
		series3.setLabel(label3);
		series3.setCount((long) 5);
		counts.add(series);

		SeriesCount series4 = new SeriesCount();
		Map<String, String> label4 = new HashMap<>();
		label4.put(PRIORITY, CMIS);
		series4.setLabel(label4);
		series4.setCount((long) 5);
		counts.add(series4);

		SeriesCount series5 = new SeriesCount();
		Map<String, String> label5 = new HashMap<>();
		label5.put(PRIORITY, TOTAL);
		series5.setLabel(label5);
		series5.setCount((long) 5);
		counts.add(series5);

		SeriesCount series6 = new SeriesCount();
		Map<String, String> label6 = new HashMap<>();
		label6.put(PRIORITY, OPEN_CMIS);
		series6.setLabel(label5);
		series6.setCount((long) 5);
		counts.add(series6);

		SeriesCount series7 = new SeriesCount();
		Map<String, String> label7 = new HashMap<>();
		label7.put(PRIORITY, OPEN_HIGH);
		series7.setLabel(label7);
		series7.setCount((long) 5);
		counts.add(series7);

		SeriesCount series8 = new SeriesCount();
		Map<String, String> label8 = new HashMap<>();
		label8.put(PRIORITY, OPEN_LOW);
		series8.setLabel(label8);
		series8.setCount((long) 5);
		counts.add(series8);

		SeriesCount series9 = new SeriesCount();
		Map<String, String> label9 = new HashMap<>();
		label9.put(PRIORITY, OPEN_NORMAL);
		series9.setLabel(label9);
		series9.setCount((long) 5);
		counts.add(series9);

		SeriesCount series10 = new SeriesCount();
		Map<String, String> label10 = new HashMap<>();
		label10.put(PRIORITY, OPEN_BLOCKER);
		series10.setLabel(label10);
		series10.setCount((long) 5);
		counts.add(series10);

		SeriesCount series11 = new SeriesCount();
		Map<String, String> label11 = new HashMap<>();
		label11.put(PRIORITY, CLOSED_LOW);
		series11.setLabel(label11);
		series11.setCount((long) 5);
		counts.add(series11);

		SeriesCount series12 = new SeriesCount();
		Map<String, String> label12 = new HashMap<>();
		label12.put(PRIORITY, CLOSED_BLOCKER);
		series12.setLabel(label12);
		series12.setCount((long) 5);
		counts.add(series12);

		SeriesCount series13 = new SeriesCount();
		Map<String, String> label13 = new HashMap<>();
		label13.put(PRIORITY, CLOSED_NORMAL);
		series13.setLabel(label13);
		series13.setCount((long) 5);
		counts.add(series13);

		SeriesCount series14 = new SeriesCount();
		Map<String, String> label14 = new HashMap<>();
		label14.put(PRIORITY, CLOSED_HIGH);
		series14.setLabel(label14);
		series14.setCount((long) 5);
		counts.add(series14);

		SeriesCount series15 = new SeriesCount();
		Map<String, String> label15 = new HashMap<>();
		label15.put(PRIORITY, CLOSED_CMIS);
		series15.setLabel(label15);
		series15.setCount((long) 5);
		counts.add(series15);

		SeriesCount series16 = new SeriesCount();
		Map<String, String> label16 = new HashMap<>();
		label16.put(PRIORITY, SN);
		series16.setLabel(label16);
		series16.setCount((long) 5);
		counts.add(series16);

		SeriesCount series17 = new SeriesCount();
		Map<String, String> label17 = new HashMap<>();
		label17.put(PRIORITY, OPENSN);
		series17.setLabel(label17);
		series17.setCount((long) 5);
		counts.add(series17);

		SeriesCount series18 = new SeriesCount();
		Map<String, String> label18 = new HashMap<>();
		label18.put(PRIORITY, CLOSEDSN);
		series18.setLabel(label18);
		series18.setCount((long) 5);
		counts.add(series18);

		SeriesCount series19 = new SeriesCount();
		Map<String, String> label19 = new HashMap<>();
		label19.put(PRIORITY, SNONEOPEN);
		series19.setLabel(label19);
		series19.setCount((long) 5);
		counts.add(series19);

		SeriesCount series20 = new SeriesCount();
		Map<String, String> label20 = new HashMap<>();
		label20.put(PRIORITY, SNTWOOPEN);
		series20.setLabel(label20);
		series20.setCount((long) 5);
		counts.add(series20);

		SeriesCount series21 = new SeriesCount();
		Map<String, String> label21 = new HashMap<>();
		label21.put(PRIORITY, SNTHREEOPEN);
		series21.setLabel(label21);
		series21.setCount((long) 5);
		counts.add(series21);

		SeriesCount series22 = new SeriesCount();
		Map<String, String> label22 = new HashMap<>();
		label22.put(PRIORITY, SNFOUROPEN);
		series22.setLabel(label22);
		series22.setCount((long) 5);
		counts.add(series22);

		SeriesCount series23 = new SeriesCount();
		Map<String, String> label23 = new HashMap<>();
		label23.put(PRIORITY, SNONEOPEN);
		series23.setLabel(label23);
		series23.setCount((long) 5);
		counts.add(series23);

		SeriesCount series24 = new SeriesCount();
		Map<String, String> label24 = new HashMap<>();
		label24.put(PRIORITY, SNONECLOSED);
		series24.setLabel(label24);
		series24.setCount((long) 5);
		counts.add(series24);

		SeriesCount series25 = new SeriesCount();
		Map<String, String> label25 = new HashMap<>();
		label25.put(PRIORITY, SNTWOOPEN);
		series25.setLabel(label25);
		series25.setCount((long) 5);
		counts.add(series25);

		SeriesCount series26 = new SeriesCount();
		Map<String, String> label26 = new HashMap<>();
		label26.put(PRIORITY, SNTWOCLOSED);
		series26.setLabel(label26);
		series26.setCount((long) 5);
		counts.add(series26);

		SeriesCount series27 = new SeriesCount();
		Map<String, String> label27 = new HashMap<>();
		label27.put(PRIORITY, SNTHREEOPEN);
		series27.setLabel(label27);
		series27.setCount((long) 5);
		counts.add(series27);

		SeriesCount series28 = new SeriesCount();
		Map<String, String> label28 = new HashMap<>();
		label28.put(PRIORITY, SNTHREECLOSED);
		series28.setLabel(label28);
		series28.setCount((long) 5);
		counts.add(series28);

		SeriesCount series29 = new SeriesCount();
		Map<String, String> label29 = new HashMap<>();
		label29.put(PRIORITY, SNFOUROPEN);
		series29.setLabel(label29);
		series29.setCount((long) 5);
		counts.add(series29);

		SeriesCount series30 = new SeriesCount();
		Map<String, String> label30 = new HashMap<>();
		label30.put(PRIORITY, SNFOURCLOSED);
		series30.setLabel(label30);
		series30.setCount((long) 5);
		counts.add(series30);

		ExecutiveMetricsSeries executiveMetricsSeries = new ExecutiveMetricsSeries();
		executiveMetricsSeries.setDaysAgo(0);
		executiveMetricsSeries.setTimeStamp(123456789l);
		executiveMetricsSeries.setCounts(counts);

		ExecutiveMetricsSeries executiveMetricsSeries1 = new ExecutiveMetricsSeries();
		executiveMetricsSeries1.setTimeStamp(123456789l);
		executiveMetricsSeries1.setCounts(counts);

		List<ExecutiveMetricsSeries> executiveMetricsSeriesList = new ArrayList<ExecutiveMetricsSeries>();
		executiveMetricsSeriesList.add(executiveMetricsSeries);
		executiveMetricsSeriesList.add(executiveMetricsSeries1);
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
		metricDetailResponse.setType(MetricType.QUALITY);

		MetricSummary summary = new MetricSummary();
		summary.setLastScanned(new Date());
		summary.setLastUpdated(new Date());
		summary.setName("quality");
		summary.setReportingComponents(1);
		summary.setTotalComponents(1);
		summary.setTrendSlope(2.3);

		summary.setCounts(settingCounts());

		metricDetailResponse.setSummary(summary);
		List<MetricTimeSeriesElement> timeSeries = new ArrayList<>();
		MetricTimeSeriesElement time = new MetricTimeSeriesElement();
		time.setCounts(settingCounts());
		time.setDaysAgo(30);
		timeSeries.add(time);

		metricDetailResponse.setTimeSeries(timeSeries);
		return metricDetailResponse;

	}

	private CollectorStatus getCollectorStatus() {
		CollectorStatus collectorStatus = new CollectorStatus();
		collectorStatus.setCollectorName("MetricsProcessor");
		collectorStatus.setLastUpdated(new Date());
		collectorStatus.setType(CollectorType.MetricsProcessor);
		return collectorStatus;
	}

	private CollectorStatus getCollectorStatusCMIS() {
		CollectorStatus collectorStatus = new CollectorStatus();
		collectorStatus.setCollectorName("CMIS");
		collectorStatus.setLastUpdated(new Date());
		collectorStatus.setType(CollectorType.CMIS);
		return collectorStatus;
	}

	private CollectorStatus getCollectorStatusJiraUSerStory() {
		CollectorStatus collectorStatus = new CollectorStatus();
		collectorStatus.setCollectorName("JiraUserStory");
		collectorStatus.setLastUpdated(new Date());
		collectorStatus.setType(CollectorType.JiraUserStory);
		return collectorStatus;
	}

	@Test
	public void removeUnusedQualityDetails() {

		try {
			List<String> appIds = new ArrayList<>();
			appIds.add("TestMe");

			Mockito.when(vastDetailsDAO.getAllAppIds(mongoClient)).thenReturn(appIds);

			List<ExecutiveComponents> qualityDataList = new ArrayList<>();

			ExecutiveComponents executiveComponents = new ExecutiveComponents();
			qualityDataList.add(executiveComponents);

			Mockito.when(executiveComponentRepository.getNotUsedAppIdList(appIds, "quality"))
					.thenReturn(qualityDataList);
			when(qualityDetailsDAO.getMongoClient()).thenReturn(mongoClient);
			qualityAnalysis.removeUnusedQualityDetails();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void removeUnusedQualityDetails_1() throws Exception {
		Mockito.when(qualityDetailsDAO.getMongoClient()).thenThrow(new NullPointerException());
		qualityAnalysis.removeUnusedQualityDetails();
	}

}
