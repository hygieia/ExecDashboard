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
import com.capitalone.dashboard.dao.SprintMetricsDAO;
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
import com.capitalone.dashboard.exec.model.DateWiseMetricsSeries;
import com.capitalone.dashboard.exec.model.ExecutiveComponents;
import com.capitalone.dashboard.exec.model.ExecutiveMetrics;
import com.capitalone.dashboard.exec.model.ExecutiveMetricsSeries;
import com.capitalone.dashboard.exec.model.ExecutiveModuleMetrics;
import com.capitalone.dashboard.exec.model.ExecutiveResponse;
import com.capitalone.dashboard.exec.model.ExecutiveSummaryList;
import com.capitalone.dashboard.exec.model.InstanceCollectorStatus;
import com.capitalone.dashboard.exec.model.MetricsDetail;
import com.capitalone.dashboard.exec.model.PortfolioResponse;
import com.capitalone.dashboard.exec.model.SeriesCount;
import com.capitalone.dashboard.exec.model.SprintMetrics;
import com.capitalone.dashboard.exec.repository.ApplicationDetailsRepository;
import com.capitalone.dashboard.exec.repository.BuildingBlocksRepository;
import com.capitalone.dashboard.exec.repository.CollectorStatusRepository;
import com.capitalone.dashboard.exec.repository.DateWiseMetricsSeriesRepository;
import com.capitalone.dashboard.exec.repository.ExecutiveComponentRepository;
import com.capitalone.dashboard.exec.repository.ExecutiveSummaryListRepository;
import com.capitalone.dashboard.exec.repository.MetricsDetailRepository;
import com.capitalone.dashboard.exec.repository.PortfolioResponseRepository;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

/**
 * 
 * @author ASTHAAA
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class SayDoRatioAnalysisTest {

	@InjectMocks
	private SayDoRatioAnalysis sayDoRatioAnalysis;
	@Mock
	ExecutiveSummaryListRepository executiveSummaryListRepository;
	@Mock
	SprintMetricsDAO sprintMetricsDAO;
	@Mock
	MongoClient client;
	@Mock
	MetricsProcessorSettings metricsSettings;
	@Mock
	DateWiseMetricsSeriesRepository dateWiseMetricsSeriesRepository;
	@Mock
	ApplicationDetailsRepository applicationDetailsRepository;
	@Mock
	ExecutiveComponentRepository executiveComponentRepository;
	@Mock
	CollectorStatusRepository collectorStatusRepository;
	@Mock
	VastDetailsDAO vastDetailsDAO;
	@Mock
	MongoTemplate mongoTemplate;
	@Mock
	MetricsDetailRepository metricDetailResponseRepository;
	@Mock
	DBCollection mongoCollection;
	@Mock
	BuildingBlocksRepository buildingBlocksRepository;
	@Mock
	PortfolioResponseRepository portfolioResponseRepository;
	@Mock
	MetricsDetailRepository metricsDetailRepository;

	@Test
	public void processDateWiseTrend_test_1() {

		Mockito.when(sprintMetricsDAO.getMongoClient()).thenReturn(client);
		Mockito.when(sprintMetricsDAO.getEntireAppList(client)).thenReturn(Arrays.asList("D40V"));
		Mockito.when(metricsSettings.getDateRange()).thenReturn(0l);
		Mockito.when(sprintMetricsDAO.getListOfModules(Mockito.any(MongoClient.class), Mockito.anyString(),
				Mockito.anyLong())).thenReturn(Arrays.asList("module"));
		Mockito.when(dateWiseMetricsSeriesRepository.findByAppIdAndModuleNameAndMetricsNameAndTimeStamp(
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenReturn(null);
		Mockito.when(sprintMetricsDAO.getDataForModule(Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong(),
				Mockito.any(MongoClient.class))).thenReturn(getSprintMetrics());
		sayDoRatioAnalysis.processDateWiseTrend();

	}

	@Test
	public void processSayDoDetails_test_1() {

		Mockito.when(sprintMetricsDAO.getMongoClient()).thenReturn(client);
		Mockito.when(sprintMetricsDAO.getEntireAppList(client)).thenReturn(Arrays.asList("D40V"));
		Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getAppDetails());
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(null);
		Mockito.when(dateWiseMetricsSeriesRepository.getThreeMonthsListWithAppId(Mockito.anyString(),
				Mockito.anyString(), Mockito.anyLong())).thenReturn(getDateWise());

		sayDoRatioAnalysis.processExecutiveMetricsDetails();

	}

	@Test
	public void removeUnusedSayDoDetails_test_1() {

		try {
			List<String> appIds = new ArrayList<>();
			appIds.add("TestMe");

			Mockito.when(vastDetailsDAO.getAllAppIds(client)).thenReturn(appIds);

			List<ExecutiveComponents> saydoDataList = new ArrayList<>();

			ExecutiveComponents executiveComponents = new ExecutiveComponents();
			saydoDataList.add(executiveComponents);

			Mockito.when(executiveComponentRepository.getNotUsedAppIdList(appIds, "saydoratio"))
					.thenReturn(saydoDataList);
			when(sprintMetricsDAO.getMongoClient()).thenReturn(client);
			sayDoRatioAnalysis.removeUnusedSayDoDetails();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void removeUnusedSayDoDetails_test_2() throws Exception {
		Mockito.when(sprintMetricsDAO.getMongoClient()).thenThrow(new NullPointerException());
		sayDoRatioAnalysis.removeUnusedSayDoDetails();
	}

	@Test
	public void processMetricsDetailResponse_test_1() {

		try {
			List<String> appIds = new ArrayList<String>();
			appIds.add("B6LV");
			Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
			Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
			Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
					.thenReturn(getExecutiveComponent());
			Mockito.when(metricDetailResponseRepository.findByMetricLevelIdAndLevelAndType("B6LV", MetricLevel.PRODUCT,
					MetricType.SAY_DO_RATIO)).thenReturn(getMetricsDetail());
			sayDoRatioAnalysis.processMetricsDetailResponse();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void processBuildingBlockMetrics_test_1() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("B6LV");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getApplicationDetails());
		Mockito.when(metricDetailResponseRepository.findByMetricLevelIdAndLevelAndType("B6LV", MetricLevel.PRODUCT,
				MetricType.SAY_DO_RATIO)).thenReturn(getMetricsDetail());
		Mockito.when(buildingBlocksRepository.findByMetricLevelIdAndMetricLevel("B6LV", MetricLevel.PRODUCT))
				.thenReturn(buildingBlockMetricsDetail());

		sayDoRatioAnalysis.processBuildingBlockMetrics();

	}

	@Test
	public void processBuildingBlockMetrics_test_2() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("B6LV");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		sayDoRatioAnalysis.processBuildingBlockMetrics();
	}

	@Test
	public void processExecutiveDetailsMetrics_test_1() {

		List<String> appIds = new ArrayList<>();
		appIds.add("D40V");
		Mockito.when(executiveSummaryListRepository.findAll()).thenReturn(getExecutiveSummaryList());
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(getExecutiveComponents());
		Mockito.when(portfolioResponseRepository.findByEid(Mockito.anyString())).thenReturn(getPortfolioResponse());
		Mockito.when(metricsDetailRepository.findByMetricLevelIdAndLevelAndType("D40V", MetricLevel.PORTFOLIO,
				MetricType.SAY_DO_RATIO)).thenReturn(getMetricsDetail());

		sayDoRatioAnalysis.processExecutiveDetailsMetrics();

	}

	@Test
	public void processComponentDetailsMetrics_test_1() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("B6LV");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(getExecutiveComponents());
		Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getApplicationDetails());
		Mockito.when(buildingBlocksRepository.findByMetricLevelIdAndMetricLevelAndMetricType("B6LV",
				MetricLevel.COMPONENT, MetricType.SAY_DO_RATIO)).thenReturn(getBuildingBlocksList());
		MetricsProcessorSettings metricsSettings = new MetricsProcessorSettings();
		metricsSettings.setSprintViewLink("sfsfsd%s=ffwfwe%s=ffwfewqf%s");
		sayDoRatioAnalysis = new SayDoRatioAnalysis(sprintMetricsDAO, metricsSettings, dateWiseMetricsSeriesRepository,
				applicationDetailsRepository, executiveComponentRepository, vastDetailsDAO, mongoTemplate,
				metricsDetailRepository, executiveSummaryListRepository, portfolioResponseRepository,
				buildingBlocksRepository);
		sayDoRatioAnalysis.processComponentDetailsMetrics();

	}

	private List<BuildingBlocks> getBuildingBlocksList() {
		List<BuildingBlocks> buildingBlockComponentSummaryResponseList = new ArrayList<BuildingBlocks>();
		BuildingBlocks buildingBlockComponentSummaryResponse = new BuildingBlocks();
		buildingBlockComponentSummaryResponse.setMetricLevelId("B6LV");
		buildingBlockComponentSummaryResponse.setLob("NTS");
		buildingBlockComponentSummaryResponse.setName("saydoratio");
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
		executiveMetrics.setMetricsName("saydoratio");
		ExecutiveModuleMetrics module = new ExecutiveModuleMetrics();
		module.setLastScanned(new Date());
		module.setLastUpdated(new Date());
		module.setModuleName("projectName");
		module.setTrendSlope((double) 0);

		List<ExecutiveMetricsSeries> series = new ArrayList();

		for (int i = 1; i < 91; i++) {
			ExecutiveMetricsSeries ems = new ExecutiveMetricsSeries();
			ems.setDaysAgo(i);
			ems.setTimeStamp(2l);
			List<SeriesCount> counts = new ArrayList();
			SeriesCount count = new SeriesCount();
			count.setCount(0l);
			Map<String, String> label = new HashMap<>();
			label.put("totalStoryPoints", "0.0");
			label.put("endDate", "1531428000000");
			label.put("storiesSayDoRatio", "100");
			label.put("totalStories", "1");
			label.put("completeDate", "1531386540000");
			label.put("sprintId", "66821");
			label.put("sprintClosedBy", "Rush, Ronald T (Ron)");
			label.put("completedStories", "1");
			label.put("sprintDurationInDays", "15");
			label.put("completedStoryPoints", "0.0");
			label.put("sprintName", "RushSprint June 28 - July 11");
			label.put("startDate", "1530132000000");
			label.put("storyPointsSayDoRatio", "0");
			count.setLabel(label);
			counts.add(count);
			ems.setCounts(counts);
			series.add(ems);
		}

		module.setSeries(series);

		List<ExecutiveModuleMetrics> moduleList = new ArrayList<ExecutiveModuleMetrics>();
		moduleList.add(module);

		executiveMetrics.setModules(moduleList);
		executiveMetrics.setTrendSlope((double) 0);

		return executiveMetrics;
	}

	private ExecutiveSummaryList getExecutiveSummary() {
		ExecutiveSummaryList executiveSummaryList = new ExecutiveSummaryList();
		List<String> appIds = new ArrayList<>();
		appIds.add("D40V");
		executiveSummaryList.setAppId(appIds);
		executiveSummaryList.setTotalApps(10);
		executiveSummaryList.setConfiguredApps(6);

		return executiveSummaryList;
	}

	private CollectorStatus getCollectorStatusSprintData() {
		CollectorStatus collectorStatus = new CollectorStatus();
		collectorStatus.setCollectorName("SprintData");
		collectorStatus.setLastUpdated(new Date());
		collectorStatus.setType(CollectorType.SprintData);
		return collectorStatus;
	}

	private List<ExecutiveSummaryList> getExecutiveSummaryList() {
		List<ExecutiveSummaryList> listOfExecutiveSummaryList = new ArrayList<>();

		ExecutiveSummaryList executiveSummaryList = new ExecutiveSummaryList();
		executiveSummaryList.setAppId(Arrays.asList("D40V"));
		executiveSummaryList.setBusinessUnits(Arrays.asList("D40V"));
		executiveSummaryList.setLastName("some name");
		executiveSummaryList.setConfiguredApps(1);
		executiveSummaryList.setConfiguredAppId(Arrays.asList("D40V"));
		executiveSummaryList.setEid("123456789");
		executiveSummaryList.setFirstName("some name");
		executiveSummaryList.setReportingPercentage(100.0);
		executiveSummaryList.setRole("some role");
		executiveSummaryList.setTotalApps(1);

		listOfExecutiveSummaryList.add(executiveSummaryList);

		return listOfExecutiveSummaryList;
	}

	private ExecutiveComponents getExecutiveComponent() {
		ExecutiveComponents ec = new ExecutiveComponents();
		ec.setAppId("D40V");
		ec.setAppName("Hygieia");
		ec.setTeamBoardLink("boardLink");
		List<ExecutiveMetrics> metrics = new ArrayList();

		ExecutiveMetrics metric = new ExecutiveMetrics();
		metric.setLastScanned(new Date());
		metric.setLastUpdated(new Date());
		metric.setMetricsName("saydoratio");
		metric.setTrendSlope(2.0);

		ExecutiveModuleMetrics module = new ExecutiveModuleMetrics();
		module.setLastScanned(new Date());
		module.setLastUpdated(new Date());
		module.setModuleName("onehygieia");
		module.setTrendSlope(2.0);
		List<ExecutiveMetricsSeries> series = new ArrayList();

		for (int i = 1; i < 91; i++) {
			ExecutiveMetricsSeries ems = new ExecutiveMetricsSeries();
			ems.setDaysAgo(i);
			ems.setTimeStamp(2l);
			List<SeriesCount> counts = new ArrayList();
			SeriesCount count = new SeriesCount();
			count.setCount(0l);
			Map<String, String> label = new HashMap<>();
			label.put("totalStoryPoints", "0.0");
			label.put("endDate", "1531428000000");
			label.put("storiesSayDoRatio", "100");
			label.put("totalStories", "1");
			label.put("completeDate", "1531386540000");
			label.put("sprintId", "66821");
			label.put("sprintClosedBy", "Rush, Ronald T (Ron)");
			label.put("completedStories", "1");
			label.put("sprintDurationInDays", "15");
			label.put("completedStoryPoints", "0.0");
			label.put("sprintName", "RushSprint June 28 - July 11");
			label.put("startDate", "1530132000000");
			label.put("storyPointsSayDoRatio", "0");
			count.setLabel(label);
			counts.add(count);
			ems.setCounts(counts);
			series.add(ems);
		}

		module.setSeries(series);
		metric.setModules(Arrays.asList(module));
		metrics.add(metric);
		ec.setMetrics(metrics);

		return ec;

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

	private BuildingBlocks buildingBlockMetricsDetail() {
		BuildingBlocks buildingBlockMetricsDetail = new BuildingBlocks();
		buildingBlockMetricsDetail.setMetricLevelId("B6LV");
		MetricSummary summary = new MetricSummary();
		summary.setLastScanned(new Date());
		summary.setLastUpdated(new Date());
		summary.setName("saydoratio");
		summary.setReportingComponents(1);
		summary.setTotalComponents(1);
		summary.setTrendSlope(2.3);
		summary.setCounts(settingCounts());
		buildingBlockMetricsDetail.setMetrics(Arrays.asList(summary));
		return buildingBlockMetricsDetail;
	}

	private MetricsDetail getMetricsDetail() {

		MetricsDetail metricDetailResponse = new MetricsDetail();
		metricDetailResponse.setMetricLevelId("B6LV");
		metricDetailResponse.setType(MetricType.SAY_DO_RATIO);

		MetricSummary summary = new MetricSummary();
		summary.setLastScanned(new Date());
		summary.setLastUpdated(new Date());
		summary.setName("saydoratio");
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

	private List<MetricCount> settingCounts() {

		MetricCount item = new MetricCount();
		item.setValue(47.0);
		Map<String, String> label = new HashMap<>();
		label.put("totalStoryPoints", "47.0");
		label.put("avgCompletedStories", "0");
		label.put("avgTotalStories", "1");
		label.put("completedStories", "2");
		label.put("avgTotalStoryPoints", "9.4");
		label.put("avgCompletedStoryPoints", "0.8");
		label.put("totalStories", "5");
		label.put("completedStoryPoints", "4.0");
		label.put("avgSprintDurationInDays", "11");

		item.setLabel(label);

		return Arrays.asList(item);

	}

	private List<DateWiseMetricsSeries> getDateWise() {
		DateWiseMetricsSeries data = new DateWiseMetricsSeries();
		data.setAppId("D40V");
		data.setDateValue("02-10-2018");
		data.setMetricsName("saydoratio");
		data.setTimeStamp(2l);
		List<SeriesCount> counts = new ArrayList();
		SeriesCount count = new SeriesCount();
		count.setCount(0l);
		Map<String, String> label = new HashMap<>();
		label.put("totalStoryPoints", "0.0");
		label.put("endDate", "1531428000000");
		label.put("storiesSayDoRatio", "100");
		label.put("totalStories", "1");
		label.put("completeDate", "1531386540000");
		label.put("sprintId", "66821");
		label.put("sprintClosedBy", "Rush, Ronald T (Ron)");
		label.put("completedStories", "1");
		label.put("sprintDurationInDays", "15");
		label.put("completedStoryPoints", "0.0");
		label.put("sprintName", "RushSprint June 28 - July 11");
		label.put("startDate", "1530132000000");
		label.put("storyPointsSayDoRatio", "0");
		count.setLabel(label);
		counts.add(count);
		data.setCounts(counts);

		return Arrays.asList(data);
	}

	private ApplicationDetails getAppDetails() {
		ApplicationDetails ad = new ApplicationDetails();
		ad.setAppId("D40V");
		ad.setAppName("Name");
		ad.setAppAcronym("Acc");
		ad.setLob("NTS");
		ad.setAvailabilityStatus("yes");
		ad.setDashboardAvailable(true);
		ad.setPoc("Name");
		ad.setTotalTeamBoards(12);
		ad.setTeamBoardLink("somelink");
		ad.setLastScanned(new Date());
		ad.setLastUpdated(0l);
		InstanceCollectorStatus item = new InstanceCollectorStatus();
		item.setCollectorName("Name");
		item.setCollectorType(CollectorType.SprintData);
		item.setEnabled(true);
		item.setLastExecuted(2l);
		item.setOnline(true);
		ad.setCollectorStatus(Arrays.asList(item));
		return ad;
	}

	private List<SprintMetrics> getSprintMetrics() {
		SprintMetrics sp = new SprintMetrics();
		sp.setSprintId("123456");
		sp.setAppId("D40V");
		sp.setSprintName("SPNAME");
		sp.setStartDate(0l);
		sp.setEndDate(0l);
		sp.setCompleteDate(0l);
		sp.setStoriesSayDoRatio(0);
		sp.setStoryPointsSayDoRatio(0);
		sp.setSprintDurationInDays(7);
		sp.setSprintClosedBy("ak");
		sp.setTotalStories(0);
		sp.setCompletedStories(0);
		sp.setTotalStoryPoints(0.0);
		sp.setCompletedStoryPoints(0.0);
		return Arrays.asList(sp);
	}
}
