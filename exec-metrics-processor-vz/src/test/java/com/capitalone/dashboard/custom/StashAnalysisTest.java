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
import com.capitalone.dashboard.dao.StashDAO;
import com.capitalone.dashboard.dao.VastDetailsDAO;
import com.capitalone.dashboard.exec.model.MetricCount;
import com.capitalone.dashboard.exec.model.MetricLevel;
import com.capitalone.dashboard.exec.model.MetricSummary;
import com.capitalone.dashboard.exec.model.MetricTimeSeriesElement;
import com.capitalone.dashboard.exec.model.MetricType;
import com.capitalone.dashboard.exec.model.ApplicationDetails;
import com.capitalone.dashboard.exec.model.BitbucketPullRequest;
import com.capitalone.dashboard.exec.model.BuildingBlocks;
import com.capitalone.dashboard.exec.model.CollectorStatus;
import com.capitalone.dashboard.exec.model.CollectorType;
import com.capitalone.dashboard.exec.model.Commit;
import com.capitalone.dashboard.exec.model.DateWiseMetricsSeries;
import com.capitalone.dashboard.exec.model.ExecutiveComponents;
import com.capitalone.dashboard.exec.model.ExecutiveMetrics;
import com.capitalone.dashboard.exec.model.ExecutiveMetricsSeries;
import com.capitalone.dashboard.exec.model.ExecutiveModuleMetrics;
import com.capitalone.dashboard.exec.model.ExecutiveResponse;
import com.capitalone.dashboard.exec.model.ExecutiveSummaryList;
import com.capitalone.dashboard.exec.model.InstanceCollectorStatus;
import com.capitalone.dashboard.exec.model.MergeType;
import com.capitalone.dashboard.exec.model.MetricsDetail;
import com.capitalone.dashboard.exec.model.PortfolioResponse;
import com.capitalone.dashboard.exec.model.RepoDetails;
import com.capitalone.dashboard.exec.model.SeriesCount;
import com.capitalone.dashboard.exec.model.StashDetailsInfo;
import com.capitalone.dashboard.exec.repository.ApplicationDetailsRepository;
import com.capitalone.dashboard.exec.repository.BuildingBlocksRepository;
import com.capitalone.dashboard.exec.repository.CollectorStatusRepository;
import com.capitalone.dashboard.exec.repository.DateWiseMetricsSeriesRepository;
import com.capitalone.dashboard.exec.repository.ExecutiveComponentRepository;
import com.capitalone.dashboard.exec.repository.ExecutiveSummaryListRepository;
import com.capitalone.dashboard.exec.repository.MetricsDetailRepository;
import com.capitalone.dashboard.exec.repository.PortfolioResponseRepository;
import com.capitalone.dashboard.utils.GenericMethods;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

@RunWith(MockitoJUnitRunner.class)
public class StashAnalysisTest {

	@InjectMocks
	private StashAnalysis stashAnalysis;
	@Mock
	private ExecutiveSummaryListRepository executiveSummaryListRepository;
	@Mock
	private StashDAO stashDAO;
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
	private DateWiseMetricsSeriesRepository dateWiseMetricsSeriesRepository;
	@Mock
	private GenericMethods genericMethods;

	@Test
	public void testProcessDateWiseTrend() throws Exception {

		List<String> excludeApps = new ArrayList<>();
		excludeApps.add("someapps");
		metricsSettings.setExcludedAppIds(excludeApps);
		List<DateWiseMetricsSeries> seriesList = new ArrayList<>();
		DateWiseMetricsSeries series = new DateWiseMetricsSeries();
		series.setAppId("D40V");
		series.setDateValue("05-05-2018");
		series.setTeamId("1234423");
		seriesList.add(series);

		Mockito.when(stashDAO.getMongoClient()).thenReturn(mongoClient);
		Mockito.when(stashDAO.getEntireAppList(Mockito.any(MongoClient.class))).thenReturn(getAppList());
		Mockito.when(stashDAO.getEntireProjectList(Mockito.any(MongoClient.class), Mockito.anyString()))
				.thenReturn(getStashDetailsInfoList());
		Mockito.when(dateWiseMetricsSeriesRepository.findByAppIdAndModuleNameAndMetricsNameAndTimeStamp(
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenReturn(series);
		Mockito.when(dateWiseMetricsSeriesRepository.findByAppIdAndModuleNameAndMetricsNameOrderByTimeStampDesc(
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(series);
		Mockito.when(stashDAO.isDataAvailableForCommits(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyLong(), Mockito.any(MongoClient.class))).thenReturn(true);
		Mockito.when(stashDAO.isDataAvailableforPullRequests(Mockito.anyString(), Mockito.anyLong(),
				Mockito.any(MongoClient.class))).thenReturn(true);
		Mockito.when(stashDAO.getCommitsList(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyLong(), Mockito.anyLong(), Mockito.any(MongoClient.class))).thenReturn(getCommitList());
		Mockito.when(stashDAO.getPullRequestList(Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong(),
				Mockito.any(MongoClient.class))).thenReturn(getPullRequestList());
		stashAnalysis.processDateWiseTrend();
	}

	@Test
	public void testProcessStashDetails() throws Exception {

		List<String> excludeApps = new ArrayList<>();
		excludeApps.add("someapps");
		List<DateWiseMetricsSeries> seriesList = new ArrayList<>();
		DateWiseMetricsSeries series = new DateWiseMetricsSeries();
		series.setAppId("D40V");
		series.setDateValue("05-05-2018");
		series.setTeamId("1234423");
		series.setTimeStamp(1521417600000l);
		series.setCounts(getExecutiveMetricsList().getModules().get(0).getSeries().get(0).getCounts());
		seriesList.add(series);

		Mockito.when(stashDAO.getMongoClient()).thenReturn(mongoClient);
		Mockito.when(stashDAO.getEntireAppList(Mockito.any(MongoClient.class))).thenReturn(getAppList());
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(getExecutiveComponents());
		Mockito.when(collectorStatusRepository.findByType(CollectorType.MetricsProcessor))
				.thenReturn(getCollectorStatus());
		// Mockito.when(collectorStatusRepository.findByType(CollectorType.SCM)).thenReturn(getCollectorStatusSCM());
		Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getApplicationDetails());
		Mockito.when(stashDAO.getEntireProjectList(Mockito.any(MongoClient.class), Mockito.anyString()))
				.thenReturn(getStashDetailsInfoList());
		Mockito.when(dateWiseMetricsSeriesRepository.getThreeMonthsList(Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString(), Mockito.any(Long.class))).thenReturn(seriesList);
		Mockito.when(stashDAO.getTotalCommitsSorted(Mockito.anyString(), Mockito.anyString(),
				Mockito.any(MongoClient.class))).thenReturn(getCommitListSorted());
		stashAnalysis = new StashAnalysis(stashDAO, executiveComponentRepository, metricsDetailRepository,
				buildingBlocksRepository, applicationDetailsRepository, collectorStatusRepository,
				executiveSummaryListRepository, portfolioResponseRepository,
				mongoTemplate, vastDetailsDAO, metricsSettings,
				dateWiseMetricsSeriesRepository, genericMethods);
		stashAnalysis.processExecutiveMetricsDetails();
	}

	@Test
	public void testProcessStashDetails_2() throws Exception {

		List<String> excludeApps = new ArrayList<>();
		excludeApps.add("someapps");
		List<DateWiseMetricsSeries> seriesList = new ArrayList<>();
		DateWiseMetricsSeries series = new DateWiseMetricsSeries();
		series.setAppId("D40V");
		series.setDateValue("05-05-2018");
		series.setTeamId("1234423");
		series.setTimeStamp(1521417600000l);
		series.setCounts(getExecutiveMetricsList().getModules().get(0).getSeries().get(0).getCounts());
		seriesList.add(series);

		Mockito.when(stashDAO.getMongoClient()).thenReturn(mongoClient);
		Mockito.when(stashDAO.getEntireAppList(Mockito.any(MongoClient.class))).thenReturn(getAppList());
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(getExecutiveComponents_1());
		Mockito.when(collectorStatusRepository.findByType(CollectorType.MetricsProcessor))
				.thenReturn(getCollectorStatus());
		// Mockito.when(collectorStatusRepository.findByType(CollectorType.SCM)).thenReturn(getCollectorStatusSCM());
		Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getApplicationDetails());
		Mockito.when(stashDAO.getEntireProjectList(Mockito.any(MongoClient.class), Mockito.anyString()))
				.thenReturn(getStashDetailsInfoList());
		Mockito.when(dateWiseMetricsSeriesRepository.getThreeMonthsList(Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString(), Mockito.any(Long.class))).thenReturn(seriesList);
		Mockito.when(stashDAO.getTotalCommitsSorted(Mockito.anyString(), Mockito.anyString(),
				Mockito.any(MongoClient.class))).thenReturn(getCommitListSorted());
		stashAnalysis = new StashAnalysis(stashDAO, executiveComponentRepository, metricsDetailRepository,
				buildingBlocksRepository, applicationDetailsRepository, collectorStatusRepository,
				executiveSummaryListRepository, portfolioResponseRepository,
				mongoTemplate, vastDetailsDAO, metricsSettings,
				dateWiseMetricsSeriesRepository, genericMethods);
		stashAnalysis.processExecutiveMetricsDetails();
	}

	@Test
	public void testProcessStashDetails_1() throws Exception {

		List<String> excludeApps = new ArrayList<>();
		excludeApps.add("someapps");
		List<DateWiseMetricsSeries> seriesList = new ArrayList<>();
		DateWiseMetricsSeries series = new DateWiseMetricsSeries();
		series.setAppId("D40V");
		series.setDateValue("05-05-2018");
		series.setTeamId("1234423");
		series.setTimeStamp(1521417600000l);
		series.setCounts(getExecutiveMetricsList().getModules().get(0).getSeries().get(0).getCounts());
		seriesList.add(series);

		Mockito.when(stashDAO.getMongoClient()).thenReturn(mongoClient);
		Mockito.when(stashDAO.getEntireAppList(Mockito.any(MongoClient.class))).thenReturn(getAppList());
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(getExecutiveComponents());
		Mockito.when(collectorStatusRepository.findByType(CollectorType.MetricsProcessor))
				.thenReturn(getCollectorStatus());
		Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getApplicationDetails());
		Mockito.when(stashDAO.getEntireProjectList(Mockito.any(MongoClient.class), Mockito.anyString()))
				.thenReturn(getStashDetailsInfoList());
		Mockito.when(dateWiseMetricsSeriesRepository.getThreeMonthsList(Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString(), Mockito.any(Long.class))).thenReturn(seriesList);
		Mockito.when(stashDAO.getTotalCommitsSorted(Mockito.anyString(), Mockito.anyString(),
				Mockito.any(MongoClient.class))).thenReturn(getCommitListSorted_1());
		stashAnalysis = new StashAnalysis(stashDAO, executiveComponentRepository, metricsDetailRepository,
				buildingBlocksRepository, applicationDetailsRepository, collectorStatusRepository,
				executiveSummaryListRepository, portfolioResponseRepository,
				mongoTemplate, vastDetailsDAO, metricsSettings,
				dateWiseMetricsSeriesRepository, genericMethods);
		stashAnalysis.processExecutiveMetricsDetails();
	}

	@Test
	public void testProcessStashDetails_3() throws Exception {

		List<String> excludeApps = new ArrayList<>();
		excludeApps.add("someapps");
		List<DateWiseMetricsSeries> seriesList = new ArrayList<>();
		DateWiseMetricsSeries series = new DateWiseMetricsSeries();
		series.setAppId("D40V");
		series.setDateValue("05-05-2018");
		series.setTeamId("1234423");
		series.setTimeStamp(1521417600000l);
		series.setCounts(getExecutiveMetricsList().getModules().get(0).getSeries().get(0).getCounts());
		seriesList.add(series);

		Mockito.when(stashDAO.getMongoClient()).thenReturn(mongoClient);
		Mockito.when(stashDAO.getEntireAppList(Mockito.any(MongoClient.class))).thenReturn(getAppList());
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(getExecutiveComponents_1());
		Mockito.when(collectorStatusRepository.findByType(CollectorType.MetricsProcessor))
				.thenReturn(getCollectorStatus());
		Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getApplicationDetails());
		Mockito.when(stashDAO.getEntireProjectList(Mockito.any(MongoClient.class), Mockito.anyString()))
				.thenReturn(getStashDetailsInfoList());
		Mockito.when(dateWiseMetricsSeriesRepository.getThreeMonthsList(Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString(), Mockito.any(Long.class))).thenReturn(seriesList);
		Mockito.when(stashDAO.getTotalCommitsSorted(Mockito.anyString(), Mockito.anyString(),
				Mockito.any(MongoClient.class))).thenReturn(getCommitListSorted_1());
		stashAnalysis = new StashAnalysis(stashDAO, executiveComponentRepository, metricsDetailRepository,
				buildingBlocksRepository, applicationDetailsRepository, collectorStatusRepository,
				executiveSummaryListRepository, portfolioResponseRepository,
				mongoTemplate, vastDetailsDAO, metricsSettings,
				dateWiseMetricsSeriesRepository, genericMethods);
		stashAnalysis.processExecutiveMetricsDetails();
	}

	@Test
	public void testProcessExecutiveDetailsMetrics() {
		Mockito.when(executiveSummaryListRepository.findAll()).thenReturn(getExecutiveSummaryList());
		Mockito.when(collectorStatusRepository.findByType(CollectorType.SCM)).thenReturn(getCollectorStatusSCM());
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(getExecutiveComponents());
		Mockito.when(portfolioResponseRepository.findByEid(Mockito.anyString())).thenReturn(getPortfolioResponse());

		stashAnalysis.processExecutiveDetailsMetrics();
	}

	@Test
	public void testProcessMetricPortfolioDetailResponse_1() {
		ExecutiveSummaryList executiveSummaryList = new ExecutiveSummaryList();
		List<String> appIds = new ArrayList<>();
		executiveSummaryList.setAppId(appIds);
		executiveSummaryList.setTotalApps(10);
		executiveSummaryList.setConfiguredApps(6);
		Mockito.when(executiveSummaryListRepository.findAll()).thenReturn(Arrays.asList(executiveSummaryList));

		stashAnalysis.processExecutiveDetailsMetrics();
	}

	@Test
	public void testProcessBuildingBlockMetrics() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("B6LV");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getApplicationDetails());
		Mockito.when(metricsDetailRepository.findByMetricLevelIdAndLevelAndType("B6LV",
				MetricLevel.PRODUCT, MetricType.VELOCITY))
				.thenReturn(getMetricsDetail());
		Mockito.when(buildingBlocksRepository.findByMetricLevelIdAndMetricLevel("B6LV",
				MetricLevel.PRODUCT))
				.thenReturn(buildingBlockMetricsDetail());
		stashAnalysis.processBuildingBlockMetrics();
	}

	@Test
	public void testProcessBuildingBlockMetrics_1() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("B6LV");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		stashAnalysis.processBuildingBlockMetrics();
	}

	@Test
	public void testProcessMetricsDetailResponse() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("B6LV");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric("B6LV", "stash"))
				.thenReturn(getExecutiveComponents());
		Mockito.when(metricsDetailRepository.findByMetricLevelIdAndLevelAndType("B6LV",
				MetricLevel.PRODUCT, MetricType.VELOCITY))
				.thenReturn(getMetricsDetail());
		Mockito.when(genericMethods.processAppCriticality(Mockito.anyString())).thenReturn("Critical");
		// Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getApplicationDetails());
		stashAnalysis.processMetricsDetailResponse();
	}

	@Test
	public void testProcessMetricsDetailResponse_2() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("B6LV");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric("B6LV", "stash"))
				.thenReturn(getExecutiveComponents_1());
		// Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getApplicationDetails());
		stashAnalysis.processMetricsDetailResponse();
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
		executiveMetrics.setMetricsName("stash");
		executiveMetrics.setModules(new ArrayList<>());
		executiveMetrics.setTrendSlope(0.2);
		executiveComponents.setMetrics(Arrays.asList(executiveMetrics));
		executiveComponents.setTeamBoardLink("some link");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(executiveComponents);
		Mockito.when(genericMethods.processAppCriticality(Mockito.anyString())).thenReturn("Critical");

		stashAnalysis.processMetricsDetailResponse();
	}

	@Test
	public void testProcessComponentDetailsMetrics() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("B6LV");
		// Mockito.when(stashDAO.getMongoClient()).thenReturn(mongoClient);
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(getExecutiveComponents());
		Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getApplicationDetails());
		Mockito.when(buildingBlocksRepository.findByMetricLevelIdAndMetricLevelAndMetricType("B6LV",
				MetricLevel.COMPONENT, MetricType.VELOCITY)).thenReturn(getBuildingBlocksList());
		stashAnalysis.processComponentDetailsMetrics();
	}

	@Test
	public void removeUnusedStashDetails() {

		try {
			List<String> appIds = new ArrayList<>();
			appIds.add("TestMe");
			Mockito.when(vastDetailsDAO.getAllAppIds(mongoClient)).thenReturn(appIds);
			List<ExecutiveComponents> qualityDataList = new ArrayList<>();
			ExecutiveComponents executiveComponents = new ExecutiveComponents();
			qualityDataList.add(executiveComponents);
			Mockito.when(executiveComponentRepository.getNotUsedAppIdList(appIds, "stash")).thenReturn(qualityDataList);
			when(stashDAO.getMongoClient()).thenReturn(mongoClient);
			stashAnalysis.removeUnusedStashDetails();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void removeUnusedStashDetails_1() throws Exception {
		Mockito.when(stashDAO.getMongoClient()).thenThrow(new NullPointerException());
		stashAnalysis.removeUnusedStashDetails();
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
		summary.setName("stash");
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
		buildingBlockComponentSummaryResponse.setName("stash");
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
		summary.setName("stash");
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
		label.put("type", "totalCommits");
		count.setLabel(label);
		count.setValue(34.0);
		counts.add(count);

		count = new MetricCount();
		Map<String, String> label1 = new HashMap<>();
		label1.put("type", "totalContributors");
		count.setLabel(label1);
		count.setValue(2.0);
		counts.add(count);

		count = new MetricCount();
		Map<String, String> label2 = new HashMap<>();
		label2.put("type", "openPullRequests");
		count.setLabel(label2);
		count.setValue(2.0);
		counts.add(count);

		count = new MetricCount();
		Map<String, String> label3 = new HashMap<>();
		label3.put("type", "mergedPullRequests");
		count.setLabel(label3);
		count.setValue(2.0);
		counts.add(count);

		count = new MetricCount();
		Map<String, String> label4 = new HashMap<>();
		label4.put("type", "declinedPullRequests");
		count.setLabel(label4);
		count.setValue(2.0);
		counts.add(count);

		return counts;
	}

	private List<StashDetailsInfo> getStashDetailsInfoList() {
		List<StashDetailsInfo> rtList = new ArrayList<>();
		List<RepoDetails> repoDetailsList = new ArrayList<>();
		StashDetailsInfo stashDetailsFinalObject = new StashDetailsInfo();
		RepoDetails repoDetails = new RepoDetails();
		repoDetails.setProjectKey("test Project Key");
		repoDetails.setRepoBranch("projectNAme");
		repoDetails.setRepoUrl("teamID");
		repoDetails.setRepoSlug("somereposlug");
		repoDetails.setUnlimitedData(true);
		repoDetailsList.add(repoDetails);
		stashDetailsFinalObject.setAppId("B6LV");
		stashDetailsFinalObject.setRepoDetails(repoDetailsList);
		rtList.add(stashDetailsFinalObject);
		return rtList;

	}

	private List<Commit> getCommitList() {
		List<Commit> rtList = new ArrayList<>();
		Commit c1 = new Commit();
		Commit c2 = new Commit();
		c1.setAppId("someAppId");
		c1.setAddedNoOfLines(45);
		c1.setNumberOfChanges(4);
		c1.setRemovedNoOfLines(8);
		c1.setScmAuthor("someauthor");
		c1.setScmCommitLog("somecommitLog");
		c1.setScmCommitTimestamp(45789878);
		c1.setScmUrl("someUrl");
		c1.setTimestamp(459877445);
		c2.setAppId("someAppId");
		c2.setAddedNoOfLines(45);
		c2.setNumberOfChanges(4);
		c2.setRemovedNoOfLines(8);
		c2.setScmAuthor("someauthor");
		c2.setScmCommitLog("somecommitLog");
		c2.setScmCommitTimestamp(45789878);
		c2.setScmUrl("someUrl");
		c2.setTimestamp(459877445);

		rtList.add(c2);
		rtList.add(c1);
		return rtList;
	}

	private Commit getCommitListSorted() {
		Commit c = new Commit();
		c.setScmCommitTimestamp(45789878);
		return c;

	}

	private Commit getCommitListSorted_1() {
		return null;

	}

	private List<BitbucketPullRequest> getPullRequestList() {
		List<BitbucketPullRequest> rtList = new ArrayList<>();
		BitbucketPullRequest pr1 = new BitbucketPullRequest();
		BitbucketPullRequest pr2 = new BitbucketPullRequest();
		pr1.setMergetype(MergeType.MERGED);
		pr1.setTimestamp(459877445);
		pr1.setAppId("D40V");
		pr2.setMergetype(MergeType.DECLINED);
		pr2.setTimestamp(459877800);
		pr2.setAppId("D40V");
		rtList.add(pr1);
		rtList.add(pr2);
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

	private ExecutiveComponents getExecutiveComponents_1() {

		ExecutiveComponents executiveComponents = new ExecutiveComponents();
		executiveComponents.setAppId("B6LV");
		executiveComponents.setAppName("SomaAppName");
		executiveComponents.setMetrics(Arrays.asList(getExecutiveMetricsList_1()));
		executiveComponents.setTeamBoardLink("some link");
		return executiveComponents;
	}

	private ExecutiveMetrics getExecutiveMetricsList_1() {

		ExecutiveMetrics executiveMetrics = new ExecutiveMetrics();
		executiveMetrics.setLastScanned(new Date());
		executiveMetrics.setLastUpdated(new Date());
		executiveMetrics.setMetricsName("stash");
		ExecutiveModuleMetrics module = new ExecutiveModuleMetrics();
		module.setLastScanned(new Date());
		module.setLastUpdated(new Date());
		module.setModuleName("module");
		module.setTrendSlope((double) 0);

		List<SeriesCount> counts = new ArrayList<>();
		SeriesCount series1 = new SeriesCount();
		Map<String, String> label = new HashMap<>();
		label.put("type", "totalCommits");
		series1.setLabel(label);
		series1.setCount((long) 232);
		counts.add(series1);

		SeriesCount series2 = new SeriesCount();
		Map<String, String> label1 = new HashMap<>();
		label1.put("type", "uniqueContributors");
		series2.setLabel(label1);
		series2.setCount((long) 5);
		counts.add(series2);

		SeriesCount series4 = new SeriesCount();
		Map<String, String> label3 = new HashMap<>();
		label3.put("type", "mergedPullRequests");
		series4.setLabel(label3);
		series4.setCount((long) 5);
		counts.add(series4);

		SeriesCount series5 = new SeriesCount();
		Map<String, String> label4 = new HashMap<>();
		label4.put("type", "declinedPullRequests");
		series5.setLabel(label4);
		series5.setCount((long) 5);
		counts.add(series5);

		SeriesCount series6 = new SeriesCount();
		Map<String, List<String>> label5 = new HashMap<>();
		List<String> contributorsList = new ArrayList<>();
		contributorsList.add("prakpr5");
		contributorsList.add("batman");
		contributorsList.add("joker");

		Map<String, Map<String, Long>> labelLoc = new HashMap<>();

		Map<String, Long> details = new HashMap<>();
		details.put("addedLines", 285448l);
		details.put("removedLines", 216752l);
		details.put("commits", 4092l);
		labelLoc.put("Hari", details);
		details = new HashMap<>();
		details.put("commits", 4092l);
		labelLoc.put("Hari1", details);
		details = new HashMap<>();
		details.put("removedLines", 216752l);
		labelLoc.put("Hari2", details);

		label5.put("contributorsList", contributorsList);
		series6.setLabelStash(label5);
		series6.setLabelLoc(labelLoc);
		series6.setCount((long) 5);
		counts.add(series6);

		ExecutiveMetricsSeries executiveMetricsSeries = new ExecutiveMetricsSeries();
		executiveMetricsSeries.setDaysAgo(0);
		executiveMetricsSeries.setTimeStamp(123456789l);
		executiveMetricsSeries.setCounts(counts);

		ExecutiveMetricsSeries executiveMetricsSeries1 = new ExecutiveMetricsSeries();
		executiveMetricsSeries1.setTimeStamp(123456789l);
		executiveMetricsSeries1.setCounts(counts);

		ExecutiveMetricsSeries executiveMetricsSeries2 = new ExecutiveMetricsSeries();
		executiveMetricsSeries2.setTimeStamp(123456789l);
		executiveMetricsSeries.setDaysAgo(50);
		executiveMetricsSeries2.setCounts(counts);

		List<ExecutiveMetricsSeries> executiveMetricsSeriesList = new ArrayList<ExecutiveMetricsSeries>();
		executiveMetricsSeriesList.add(executiveMetricsSeries);
		executiveMetricsSeriesList.add(executiveMetricsSeries1);
		executiveMetricsSeriesList.add(executiveMetricsSeries2);
		module.setSeries(executiveMetricsSeriesList);

		List<ExecutiveModuleMetrics> moduleList = new ArrayList<ExecutiveModuleMetrics>();
		moduleList.add(module);

		executiveMetrics.setModules(moduleList);
		executiveMetrics.setTrendSlope((double) 5);

		return executiveMetrics;
	}

	private ExecutiveMetrics getExecutiveMetricsList() {

		ExecutiveMetrics executiveMetrics = new ExecutiveMetrics();
		executiveMetrics.setLastScanned(new Date());
		executiveMetrics.setLastUpdated(new Date());
		executiveMetrics.setMetricsName("stash");
		ExecutiveModuleMetrics module = new ExecutiveModuleMetrics();
		module.setLastScanned(new Date());
		module.setLastUpdated(new Date());
		module.setModuleName("module");
		module.setTrendSlope((double) 0);

		List<SeriesCount> counts = new ArrayList<>();
		SeriesCount series1 = new SeriesCount();
		Map<String, String> label = new HashMap<>();
		label.put("type", "totalCommits");
		series1.setLabel(label);
		series1.setCount((long) 232);
		counts.add(series1);

		SeriesCount series2 = new SeriesCount();
		Map<String, String> label1 = new HashMap<>();
		label1.put("type", "uniqueContributors");
		series2.setLabel(label1);
		series2.setCount((long) 5);
		counts.add(series2);

		SeriesCount series4 = new SeriesCount();
		Map<String, String> label3 = new HashMap<>();
		label3.put("type", "mergedPullRequests");
		series4.setLabel(label3);
		series4.setCount((long) 5);
		counts.add(series4);

		SeriesCount series5 = new SeriesCount();
		Map<String, String> label4 = new HashMap<>();
		label4.put("type", "declinedPullRequests");
		series5.setLabel(label4);
		series5.setCount((long) 5);
		counts.add(series5);

		SeriesCount series6 = new SeriesCount();
		Map<String, List<String>> label5 = new HashMap<>();
		List<String> contributorsList = new ArrayList<>();
		contributorsList.add("prakpr5");
		contributorsList.add("batman");
		contributorsList.add("joker");

		Map<String, Map<String, Long>> labelLoc = new HashMap<>();

		Map<String, Long> details = new HashMap<>();
		details.put("addedLines", 285448l);
		details.put("removedLines", 216752l);
		details.put("commits", 4092l);
		labelLoc.put("Hari", details);
		details = new HashMap<>();
		details.put("commits", 4092l);
		labelLoc.put("Hari1", details);
		details = new HashMap<>();
		details.put("removedLines", 216752l);
		labelLoc.put("Hari2", details);

		label5.put("contributorsList", contributorsList);
		series6.setLabelStash(label5);
		series6.setCount((long) 5);
		counts.add(series6);

		ExecutiveMetricsSeries executiveMetricsSeries = new ExecutiveMetricsSeries();
		executiveMetricsSeries.setDaysAgo(0);
		executiveMetricsSeries.setTimeStamp(123456789l);
		executiveMetricsSeries.setCounts(counts);

		ExecutiveMetricsSeries executiveMetricsSeries1 = new ExecutiveMetricsSeries();
		executiveMetricsSeries1.setTimeStamp(123456789l);
		executiveMetricsSeries1.setCounts(counts);

		ExecutiveMetricsSeries executiveMetricsSeries2 = new ExecutiveMetricsSeries();
		executiveMetricsSeries2.setTimeStamp(123456789l);
		executiveMetricsSeries.setDaysAgo(50);
		executiveMetricsSeries2.setCounts(counts);

		List<ExecutiveMetricsSeries> executiveMetricsSeriesList = new ArrayList<ExecutiveMetricsSeries>();
		executiveMetricsSeriesList.add(executiveMetricsSeries);
		executiveMetricsSeriesList.add(executiveMetricsSeries1);
		executiveMetricsSeriesList.add(executiveMetricsSeries2);
		module.setSeries(executiveMetricsSeriesList);

		List<ExecutiveModuleMetrics> moduleList = new ArrayList<ExecutiveModuleMetrics>();
		moduleList.add(module);

		executiveMetrics.setModules(moduleList);
		executiveMetrics.setTrendSlope((double) 5);

		return executiveMetrics;
	}

	private MetricsDetail getMetricsDetail() {

		MetricsDetail metricDetailResponse = new MetricsDetail();
		metricDetailResponse.setMetricLevelId("B6LV");
		metricDetailResponse.setType(MetricType.STASH);

		MetricSummary summary = new MetricSummary();
		summary.setLastScanned(new Date());
		summary.setLastUpdated(new Date());
		summary.setName("stash");
		summary.setReportingComponents(1);
		summary.setTotalComponents(1);
		summary.setTrendSlope(2.3);
		List<MetricCount> counts = new ArrayList<>();

		MetricCount count = new MetricCount();
		Map<String, String> label = new HashMap<>();
		label.put("type", "totalCommits");
		count.setLabel(label);
		count.setValue(232);
		counts.add(count);

		MetricCount count1 = new MetricCount();
		Map<String, String> label1 = new HashMap<>();
		label1.put("type", "openPullRequests");
		count1.setLabel(label1);
		count1.setValue(5);
		counts.add(count1);

		MetricCount count2 = new MetricCount();
		Map<String, String> label2 = new HashMap<String, String>();
		label2.put("type", "mergedPullRequests");
		count2.setLabel(label2);
		count2.setValue(5);
		counts.add(count2);

		MetricCount count3 = new MetricCount();
		Map<String, String> label3 = new HashMap<>();
		label3.put("type", "declinedPullRequests");
		count3.setLabel(label3);
		count3.setValue(5);
		counts.add(count3);

		MetricCount count4 = new MetricCount();
		Map<String, String> label4 = new HashMap<>();
		label4.put("type", "uniqueContributors");
		count4.setLabel(label4);
		count4.setValue(5);
		counts.add(count4);

		summary.setCounts(counts);

		metricDetailResponse.setSummary(summary);
		List<MetricTimeSeriesElement> timeSeries = new ArrayList<>();
		MetricTimeSeriesElement time = new MetricTimeSeriesElement();
		time.setCounts(counts);
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

	private CollectorStatus getCollectorStatusSCM() {
		CollectorStatus collectorStatus = new CollectorStatus();
		collectorStatus.setCollectorName("SCM");
		collectorStatus.setLastUpdated(new Date());
		collectorStatus.setType(CollectorType.SCM);
		return collectorStatus;
	}

}
