package com.capitalone.dashboard.custom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
import com.capitalone.dashboard.dao.CloudDAO;
import com.capitalone.dashboard.dao.DashboardDetailsDAO;
import com.capitalone.dashboard.dao.VastDetailsDAO;
import com.capitalone.dashboard.exec.model.MetricLevel;
import com.capitalone.dashboard.exec.model.MetricSummary;
import com.capitalone.dashboard.exec.model.MetricTimeSeriesElement;
import com.capitalone.dashboard.exec.model.MetricType;
import com.capitalone.dashboard.exec.model.vz.Application;
import com.capitalone.dashboard.exec.model.vz.ApplicationDetails;
import com.capitalone.dashboard.exec.model.vz.BuildingBlocks;
import com.capitalone.dashboard.exec.model.vz.CloudCost;
import com.capitalone.dashboard.exec.model.vz.CollectorStatus;
import com.capitalone.dashboard.exec.model.vz.CollectorType;
import com.capitalone.dashboard.exec.model.vz.Dashboard;
import com.capitalone.dashboard.exec.model.vz.DateWiseMetricsSeries;
import com.capitalone.dashboard.exec.model.vz.ExecutiveComponents;
import com.capitalone.dashboard.exec.model.vz.ExecutiveMetrics;
import com.capitalone.dashboard.exec.model.vz.ExecutiveMetricsSeries;
import com.capitalone.dashboard.exec.model.vz.ExecutiveModuleMetrics;
import com.capitalone.dashboard.exec.model.vz.ExecutiveResponse;
import com.capitalone.dashboard.exec.model.vz.ExecutiveSummaryList;
import com.capitalone.dashboard.exec.model.vz.InstanceCollectorStatus;
import com.capitalone.dashboard.exec.model.vz.MetricsDetail;
import com.capitalone.dashboard.exec.model.vz.PortfolioResponse;
import com.capitalone.dashboard.exec.repository.vz.ApplicationDetailsRepository;
import com.capitalone.dashboard.exec.repository.vz.BuildingBlocksRepository;
import com.capitalone.dashboard.exec.repository.vz.CloudCostRepository;
import com.capitalone.dashboard.exec.repository.vz.CollectorStatusRepository;
import com.capitalone.dashboard.exec.repository.vz.DateWiseMetricsSeriesRepository;
import com.capitalone.dashboard.exec.repository.vz.ExecutiveComponentRepository;
import com.capitalone.dashboard.exec.repository.vz.ExecutiveSummaryListRepository;
import com.capitalone.dashboard.exec.repository.vz.MetricsDetailRepository;
import com.capitalone.dashboard.exec.repository.vz.PortfolioResponseRepository;
import com.capitalone.dashboard.ops.CloudOps;
import com.capitalone.dashboard.ops.CloudTestUtils;
import com.capitalone.dashboard.utils.GenericMethods;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

/**
 * @author raish4s
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CloudAnalysisTest {

	@InjectMocks
	private CloudAnalysis cloudAnalysis;
	@Mock
	private ExecutiveSummaryListRepository executiveSummaryListRepository;
	@Mock
	private CloudDAO cloudDAO;
	@Mock
	private CloudOps cloudOps;
	@Mock
	private GenericMethods genericMethods;
	@Mock
	private CloudCostRepository cloudCostRepository;
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
	CloudTestUtils cloudTestUtils = new CloudTestUtils();

	@Test
	public void testDateWiseMetrics() {
		List<String> appIds = new ArrayList<>();
		appIds.add("F86V");
		appIds.add("ICGV");
		appIds.add("POSB");
		DateWiseMetricsSeries datewise = new DateWiseMetricsSeries();
		datewise.setTimeStamp((long) 0);
		Mockito.when(cloudDAO.getEntireAppList(mongoClient)).thenReturn(appIds);
		Mockito.when(cloudDAO.getMongoClient()).thenReturn(mongoClient);
		Mockito.when(dateWiseMetricsSeriesRepository.findByAppIdAndModuleNameAndMetricsNameOrderByTimeStampDesc(
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(datewise);
		cloudAnalysis.processDateWiseTrend();
	}

	@Test
	public void testDateWiseMetrics_1() {
		Mockito.when(cloudDAO.getMongoClient()).thenReturn(mongoClient);
		Mockito.when(cloudDAO.getEntireAppList(mongoClient)).thenThrow(new NullPointerException());
		cloudAnalysis.processDateWiseTrend();
	}

	@Test
	public void testDateWiseMetrics_2() {
		List<String> appIds = new ArrayList<>();
		appIds.add("F86V");
		appIds.add("ICGV");
		appIds.add("POSB");
		Mockito.when(cloudDAO.getEntireAppList(mongoClient)).thenReturn(appIds);
		Mockito.when(cloudDAO.getMongoClient()).thenReturn(mongoClient);
		Mockito.when(dateWiseMetricsSeriesRepository.findByAppIdAndModuleNameAndMetricsNameAndTimeStamp(
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyLong()))
				.thenThrow(new NullPointerException());
		cloudAnalysis.processDateWiseTrend();
	}

	@Test
	public void testProcessCloudDetails() {
		List<String> excludeApps = new ArrayList<>();
		excludeApps.add("someapps");
		metricsSettings.setExcludedAppIds(excludeApps);
		List<String> appIds = new ArrayList<>();
		appIds.add("F86V");
		appIds.add("ICGV");
		appIds.add("POSB");
		Mockito.when(cloudDAO.getEntireAppList(mongoClient)).thenReturn(appIds);
		Mockito.when(cloudDAO.getMongoClient()).thenReturn(mongoClient);
		List<CloudCost> costList = new ArrayList<>();
		CloudCost cost = new CloudCost();
		Calendar cal = Calendar.getInstance();
		String lastmonth = "30-0" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.YEAR);
		cost.setAppId("D40V");
		cost.setTime(lastmonth);
		cost.setCost(2345.0);
		CloudCost cost1 = new CloudCost();
		cost1.setAppId("D40V");
		cost1.setTime("30-06-2018");
		cost1.setCost(2345.0);
		costList.add(cost);
		costList.add(cost1);
		List<String> timeRanges = new ArrayList<>();
		timeRanges.add("30-06-2018");
		timeRanges.add("30-05-2018");
		timeRanges.add(lastmonth);
		Mockito.when(cloudCostRepository.findByAppId(Mockito.anyString())).thenReturn(costList);
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString())).thenReturn(timeRanges);
		Mockito.when(genericMethods.getDaysAgoValue(Mockito.anyString())).thenReturn(39);
		Mockito.when(cloudCostRepository.findByAppIdAndTime(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(cost1);
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(getExecutiveComponents());
		Mockito.when(collectorStatusRepository.findByType(CollectorType.CloudCustodian))
				.thenReturn(getCollectorStatusCloudCustodian());
		Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getApplicationDetails());
		cloudAnalysis.processExecutiveMetricsDetails();
	}

	@Test
	public void testProcessCloudDetails_2() {
		List<String> excludeApps = new ArrayList<>();
		excludeApps.add("someapps");
		metricsSettings.setExcludedAppIds(excludeApps);
		List<String> appIds = new ArrayList<>();
		appIds.add("F86V");
		appIds.add("ICGV");
		appIds.add("POSB");
		Dashboard dashboard = new Dashboard("", "", new Application(), "", "appId", "");
		dashboard.setAppId("F86V");
		dashboard.setInstance("some instance");
		Mockito.when(cloudDAO.getEntireAppList(mongoClient)).thenReturn(appIds);
		Mockito.when(cloudDAO.getMongoClient()).thenReturn(mongoClient);
		List<CloudCost> costList = new ArrayList<>();
		CloudCost cost = new CloudCost();
		Calendar cal = Calendar.getInstance();
		String lastmonth = "30-0" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.YEAR);
		cost.setAppId("D40V");
		cost.setTime(lastmonth);
		cost.setCost(2345.0);
		CloudCost cost1 = new CloudCost();
		cost1.setAppId("D40V");
		cost1.setTime("30-06-2018");
		cost1.setCost(2345.0);
		costList.add(cost);
		costList.add(cost1);
		List<String> timeRanges = new ArrayList<>();
		timeRanges.add("30-06-2018");
		timeRanges.add("30-05-2018");
		timeRanges.add(lastmonth);
		Mockito.when(cloudCostRepository.findByAppId(Mockito.anyString())).thenReturn(costList);
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString())).thenReturn(timeRanges);
		Mockito.when(genericMethods.getDaysAgoValue(Mockito.anyString())).thenReturn(39);
		Mockito.when(cloudCostRepository.findByAppIdAndTime(Mockito.anyString(), Mockito.anyString())).thenReturn(cost);
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(getExecutiveComponents());
		Mockito.when(collectorStatusRepository.findByType(CollectorType.CloudCustodian))
				.thenReturn(getCollectorStatusCloudCustodian());
		Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getApplicationDetails());
		cloudAnalysis.processExecutiveMetricsDetails();
	}

	@Test
	public void testProcessCloudDetails_1() {
		List<String> excludeApps = new ArrayList<>();
		excludeApps.add("someapps");
		metricsSettings.setExcludedAppIds(excludeApps);
		List<String> appIds = new ArrayList<>();
		appIds.add("F86V");
		appIds.add("ICGV");
		appIds.add("POSB");
		Dashboard dashboard = new Dashboard("", "", new Application(), "", "appId", "");
		dashboard.setAppId("F86V");
		dashboard.setInstance("some instance");
		List<CloudCost> costList = new ArrayList<>();
		Mockito.when(cloudDAO.getEntireAppList(mongoClient)).thenReturn(appIds);
		Mockito.when(cloudDAO.getMongoClient()).thenReturn(mongoClient);
		Mockito.when(cloudCostRepository.findByAppId(Mockito.anyString())).thenReturn(costList);
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(getExecutiveComponents());
		Mockito.when(collectorStatusRepository.findByType(CollectorType.CloudCustodian))
				.thenReturn(getCollectorStatusCloudCustodian());
		Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getApplicationDetails());
		cloudAnalysis.processExecutiveMetricsDetails();
	}

	@Test
	public void testProcessExecutiveDetailsMetrics() {
		Mockito.when(executiveSummaryListRepository.findAll()).thenReturn(getExecutiveSummaryList());
		Mockito.when(collectorStatusRepository.findByType(CollectorType.CloudCustodian))
				.thenReturn(getCollectorStatusCloudCustodian());
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(getExecutiveComponents());
		Mockito.when(portfolioResponseRepository.findByEid(Mockito.anyString())).thenReturn(getPortfolioResponse());
		Mockito.when(genericMethods.processExecSeriesCount(Mockito.any())).thenReturn(getSeriesMap());
		cloudAnalysis.processExecutiveDetailsMetrics();
	}

	@Test
	public void testProcessMetricPortfolioDetailResponse_1() {
		ExecutiveSummaryList executiveSummaryList = new ExecutiveSummaryList();
		List<String> appIds = new ArrayList<>();
		appIds.add("F86V");
		executiveSummaryList.setAppId(appIds);
		executiveSummaryList.setTotalApps(10);
		executiveSummaryList.setConfiguredApps(6);
		Mockito.when(executiveSummaryListRepository.findAll()).thenReturn(Arrays.asList(executiveSummaryList));
		cloudAnalysis.processExecutiveDetailsMetrics();
	}

	@Test
	public void testProcessBuildingBlockMetrics() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("F86V");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getApplicationDetails());
		Mockito.when(metricsDetailRepository.findByMetricLevelIdAndLevelAndType("F86V", MetricLevel.PRODUCT,
				MetricType.CLOUD)).thenReturn(getMetricsDetail());
		Mockito.when(buildingBlocksRepository.findByMetricLevelIdAndMetricLevel("F86V", MetricLevel.PRODUCT))
				.thenReturn(buildingBlockMetricsDetail());
		cloudAnalysis.processBuildingBlockMetrics();
	}

	@Test
	public void testProcessBuildingBlockMetrics_1() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("F86V");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		cloudAnalysis.processBuildingBlockMetrics();
	}

	@Test
	public void testProcessMetricsDetailResponse() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("F86V");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric("F86V", "cloud"))
				.thenReturn(getExecutiveComponents());
		Mockito.when(metricsDetailRepository.findByMetricLevelIdAndLevelAndType("F86V", MetricLevel.PRODUCT,
				MetricType.CLOUD)).thenReturn(getMetricsDetail());
		cloudAnalysis.processMetricsDetailResponse();
	}

	@Test
	public void testProcessMetricsDetailResponse_1() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("F86V");
		ExecutiveComponents executiveComponents = new ExecutiveComponents();
		executiveComponents.setAppId("F86V");
		executiveComponents.setAppName("WFM");
		ExecutiveMetrics executiveMetrics = new ExecutiveMetrics();
		executiveMetrics.setLastScanned(new Date());
		executiveMetrics.setLastUpdated(new Date());
		executiveMetrics.setMetricsName("cloud");
		executiveMetrics.setModules(new ArrayList<>());
		executiveMetrics.setTrendSlope(0.2);
		executiveComponents.setMetrics(Arrays.asList(executiveMetrics));
		executiveComponents.setTeamBoardLink("some link");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(executiveComponents);
		cloudAnalysis.processMetricsDetailResponse();
	}

	@Test
	public void testProcessComponentDetailsMetrics() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("F86V");
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(getExecutiveComponents());
		Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getApplicationDetails());
		Mockito.when(buildingBlocksRepository.findByMetricLevelIdAndMetricLevelAndMetricType("F86V",
				MetricLevel.COMPONENT, MetricType.CLOUD)).thenReturn(getBuildingBlocksList());
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		cloudAnalysis.processComponentDetailsMetrics();
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
		buildingBlockMetricsDetail.setMetricLevelId("F86V");
		MetricSummary summary = new MetricSummary();
		summary.setLastScanned(new Date());
		summary.setLastUpdated(new Date());
		summary.setName("cloud");
		summary.setReportingComponents(1);
		summary.setTotalComponents(1);
		summary.setTrendSlope(2.3);
		summary.setCounts(cloudTestUtils.getCounts());
		buildingBlockMetricsDetail.setMetrics(Arrays.asList(summary));
		return buildingBlockMetricsDetail;
	}

	private List<BuildingBlocks> getBuildingBlocksList() {
		List<BuildingBlocks> buildingBlockComponentSummaryResponseList = new ArrayList<BuildingBlocks>();
		BuildingBlocks buildingBlockComponentSummaryResponse = new BuildingBlocks();
		buildingBlockComponentSummaryResponse.setMetricLevelId("F86V");
		buildingBlockComponentSummaryResponse.setLob("NTS");
		buildingBlockComponentSummaryResponse.setName("cloud");
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
		summary.setName("cloud");
		summary.setReportingComponents(1);
		summary.setTotalComponents(1);
		summary.setTrendSlope(2.3);
		summary.setCounts(cloudTestUtils.getCounts());
		return summary;
	}

	private ApplicationDetails getApplicationDetails() {
		ApplicationDetails appDetails = new ApplicationDetails();
		appDetails.setAppAcronym("appAcronym");
		appDetails.setAppId("F86V");
		appDetails.setAvailabilityStatus("available");
		appDetails.setAppName("WFM");
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
		executiveSummaryList.setAppId(Arrays.asList("F86V"));
		executiveSummaryList.setBusinessUnits(Arrays.asList("F86V"));
		executiveSummaryList.setLastName("some name");
		executiveSummaryList.setConfiguredApps(1);
		executiveSummaryList.setConfiguredAppId(Arrays.asList("F86V"));
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
		executiveComponents.setAppId("F86V");
		executiveComponents.setAppName("SomeAppName");
		executiveComponents.setMetrics(Arrays.asList(getExecutiveMetricsList()));
		executiveComponents.setTeamBoardLink("some link");
		return executiveComponents;
	}

	private ExecutiveMetrics getExecutiveMetricsList() {
		ExecutiveMetrics executiveMetrics = new ExecutiveMetrics();
		executiveMetrics.setLastScanned(new Date());
		executiveMetrics.setLastUpdated(new Date());
		executiveMetrics.setMetricsName("cloud");
		ExecutiveModuleMetrics module = new ExecutiveModuleMetrics();
		module.setLastScanned(new Date());
		module.setLastUpdated(new Date());
		module.setModuleName("module");
		module.setTrendSlope((double) 0);
		ExecutiveMetricsSeries executiveMetricsSeries = new ExecutiveMetricsSeries();
		executiveMetricsSeries.setDaysAgo(0);
		executiveMetricsSeries.setTimeStamp(123456789l);
		executiveMetricsSeries.setCounts(cloudTestUtils.getSeries());
		ExecutiveMetricsSeries executiveMetricsSeries1 = new ExecutiveMetricsSeries();
		executiveMetricsSeries1.setTimeStamp(123456789l);
		executiveMetricsSeries1.setCounts(cloudTestUtils.getSeries());
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
		metricDetailResponse.setMetricLevelId("F86V");
		metricDetailResponse.setType(MetricType.CLOUD);
		MetricSummary summary = new MetricSummary();
		summary.setLastScanned(new Date());
		summary.setLastUpdated(new Date());
		summary.setName("cloud");
		summary.setReportingComponents(1);
		summary.setTotalComponents(1);
		summary.setTrendSlope(2.3);
		summary.setCounts(cloudTestUtils.getCounts());
		metricDetailResponse.setSummary(summary);
		List<MetricTimeSeriesElement> timeSeries = new ArrayList<>();
		MetricTimeSeriesElement time = new MetricTimeSeriesElement();
		time.setCounts(cloudTestUtils.getCounts());
		time.setDaysAgo(30);
		timeSeries.add(time);
		metricDetailResponse.setTimeSeries(timeSeries);
		return metricDetailResponse;
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

		processedSeries.put("prodCostCount", (long) 10);
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

		processedSeries.put("nonprodCostCount", (long) 10);
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

		processedSeries.put("stagingCostCount", (long) 10);
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

	private CollectorStatus getCollectorStatusCloudCustodian() {
		CollectorStatus collectorStatus = new CollectorStatus();
		collectorStatus.setCollectorName("CloudCustodian");
		collectorStatus.setLastUpdated(new Date());
		collectorStatus.setType(CollectorType.CloudCustodian);
		return collectorStatus;
	}

}
