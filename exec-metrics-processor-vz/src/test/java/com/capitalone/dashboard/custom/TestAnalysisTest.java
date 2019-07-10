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
import com.capitalone.dashboard.dao.TestMetricsDAO;
import com.capitalone.dashboard.dao.VastDetailsDAO;
import com.capitalone.dashboard.exec.model.MetricCount;
import com.capitalone.dashboard.exec.model.MetricLevel;
import com.capitalone.dashboard.exec.model.MetricSummary;
import com.capitalone.dashboard.exec.model.MetricTimeSeriesElement;
import com.capitalone.dashboard.exec.model.MetricType;
import com.capitalone.dashboard.exec.model.vz.ApplicationDetails;
import com.capitalone.dashboard.exec.model.vz.BuildingBlocks;
import com.capitalone.dashboard.exec.model.vz.CollectorStatus;
import com.capitalone.dashboard.exec.model.vz.CollectorType;
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
import com.capitalone.dashboard.exec.model.vz.SeriesCount;
import com.capitalone.dashboard.exec.model.vz.TestMetrics;
import com.capitalone.dashboard.exec.repository.vz.ApplicationDetailsRepository;
import com.capitalone.dashboard.exec.repository.vz.BuildingBlocksRepository;
import com.capitalone.dashboard.exec.repository.vz.CollectorStatusRepository;
import com.capitalone.dashboard.exec.repository.vz.DateWiseMetricsSeriesRepository;
import com.capitalone.dashboard.exec.repository.vz.ExecutiveComponentRepository;
import com.capitalone.dashboard.exec.repository.vz.ExecutiveSummaryListRepository;
import com.capitalone.dashboard.exec.repository.vz.MetricsDetailRepository;
import com.capitalone.dashboard.exec.repository.vz.PortfolioResponseRepository;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

/**
 * 
 * @author ASTHAAA
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class TestAnalysisTest {

	@InjectMocks
	private TestAnalysis testAnalysis;
	@Mock
	ExecutiveSummaryListRepository executiveSummaryListRepository;
	@Mock
	TestMetricsDAO testMetricsDAO;
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

		Mockito.when(testMetricsDAO.getMongoClient()).thenReturn(client);
		Mockito.when(testMetricsDAO.getEntireAppList(client)).thenReturn(Arrays.asList("D40V"));
		Mockito.when(metricsSettings.getDateRange()).thenReturn(0l);
		Mockito.when(
				testMetricsDAO.getListOfModules(Mockito.any(MongoClient.class), Mockito.anyString(), Mockito.anyLong()))
				.thenReturn(Arrays.asList("module"));
		Mockito.when(dateWiseMetricsSeriesRepository.findByAppIdAndModuleNameAndMetricsNameAndTimeStamp(
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenReturn(null);
		Mockito.when(testMetricsDAO.getTestDetailsForModule(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(),
				Mockito.anyLong(), Mockito.any(MongoClient.class))).thenReturn(getTestMetrics());
		testAnalysis.processDateWiseTrend();

	}

	private List<TestMetrics> getTestMetrics() {

		TestMetrics tm = new TestMetrics();

		tm.setAppId("D40V");
		tm.setBuildNo(1);
		tm.setEnv("DIT");
		tm.setFailed(10);
		tm.setFramework("asdfsa");
		tm.setJobName("NTS.D40V.Check.Check");
		tm.setSkipped(10);
		tm.setSuccess(80);
		tm.setTestType("Selenium");
		tm.setTimestamp(123123123123123l);
		tm.setTotal(100);

		return Arrays.asList(tm);
	}

	@Test
	public void processTestDetails_test_1() {

		Mockito.when(testMetricsDAO.getMongoClient()).thenReturn(client);
		Mockito.when(testMetricsDAO.getEntireAppList(client)).thenReturn(Arrays.asList("D40V"));
		Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getAppDetails());
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(null);
		Mockito.when(collectorStatusRepository.findByType(CollectorType.Test)).thenReturn(getCollectorStatus());
		Mockito.when(dateWiseMetricsSeriesRepository.getThreeMonthsListWithAppId(Mockito.anyString(),
				Mockito.anyString(), Mockito.anyLong())).thenReturn(getDateWise());

		testAnalysis.processExecutiveMetricsDetails();

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
		label.put("success", "80");
		label.put("failed", "10");
		label.put("skipped", "10");
		label.put("total", "100");
		label.put("jobName", "NTS.D40V.Check.Check");
		label.put("buildNo", "1");
		label.put("testType", "Regression");
		label.put("env", "DIT");
		label.put("timestamp", "0l");
		label.put("framework", "SELENIUM");
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
		item.setCollectorType(CollectorType.Test);
		item.setEnabled(true);
		item.setLastExecuted(2l);
		item.setOnline(true);
		ad.setCollectorStatus(Arrays.asList(item));
		return ad;
	}

	private CollectorStatus getCollectorStatus() {
		CollectorStatus cs = new CollectorStatus();
		cs.setLastUpdated(new Date());
		return cs;
	}

	@Test
	public void removeUnusedTestDetails_test_1() {

		try {
			List<String> appIds = new ArrayList<>();
			appIds.add("D40V");

			Mockito.when(vastDetailsDAO.getAllAppIds(client)).thenReturn(appIds);

			List<ExecutiveComponents> saydoDataList = new ArrayList<>();

			ExecutiveComponents executiveComponents = new ExecutiveComponents();
			saydoDataList.add(executiveComponents);

			Mockito.when(executiveComponentRepository.getNotUsedAppIdList(appIds, "test")).thenReturn(saydoDataList);
			when(testMetricsDAO.getMongoClient()).thenReturn(client);
			testAnalysis.removeUnusedTestDetails();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void rremoveUnusedTestDetails_test_2() throws Exception {
		Mockito.when(testMetricsDAO.getMongoClient()).thenThrow(new NullPointerException());
		testAnalysis.removeUnusedTestDetails();
	}

	@Test
	public void processMetricsDetailResponse_test_1() {

		try {
			List<String> appIds = new ArrayList<String>();
			appIds.add("D40V");
			Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
			Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
			Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
					.thenReturn(getExecutiveComponent());
			testAnalysis.processMetricsDetailResponse();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private MetricsDetail getMetricsDetail() {

		MetricsDetail metricDetailResponse = new MetricsDetail();
		metricDetailResponse.setMetricLevelId("D40V");
		metricDetailResponse.setType(MetricType.TEST);

		MetricSummary summary = new MetricSummary();
		summary.setLastScanned(new Date());
		summary.setLastUpdated(new Date());
		summary.setName("test");
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

		int selTotalCount = 0;
		int selSuccessCount = 0;
		int selFailCount = 0;
		int selSkipCount = 0;

		int cucTotalCount = 0;
		int cucSuccessCount = 0;
		int cucFailCount = 0;
		int cucSkipCount = 0;

		int soapTotalCount = 0;
		int soapSuccessCount = 0;
		int soapFailCount = 0;
		int soapSkipCount = 0;

		int htmlTotalCount = 0;
		int htmlSuccessCount = 0;
		int htmlFailCount = 0;
		int htmlSkipCount = 0;

		Map<String, String> label = new HashMap<>();
		label.put("selTotalCount", String.valueOf(selTotalCount));
		label.put("selFailCount", String.valueOf(selFailCount));
		label.put("selSkipCount", String.valueOf(selSkipCount));
		label.put("selSuccessCount", String.valueOf(selSuccessCount));

		label.put("cucTotalCount", String.valueOf(cucTotalCount));
		label.put("cucFailCount", String.valueOf(cucFailCount));
		label.put("cucSkipCount", String.valueOf(cucSkipCount));
		label.put("cucSuccessCount", String.valueOf(cucSuccessCount));

		label.put("soapTotalCount", String.valueOf(soapTotalCount));
		label.put("soapFailCount", String.valueOf(soapFailCount));
		label.put("soapSkipCount", String.valueOf(soapSkipCount));
		label.put("soapSuccessCount", String.valueOf(soapSuccessCount));

		label.put("htmlTotalCount", String.valueOf(htmlTotalCount));
		label.put("htmlFailCount", String.valueOf(htmlFailCount));
		label.put("htmlSkipCount", String.valueOf(htmlSkipCount));
		label.put("htmlSuccessCount", String.valueOf(htmlSuccessCount));

		item.setLabel(label);

		return Arrays.asList(item);

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
		metric.setMetricsName("test");
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
			label.put("success", "80");
			label.put("failed", "10");
			label.put("skipped", "10");
			label.put("total", "100");
			label.put("jobName", "NTS.D40V.Check.Check");
			label.put("buildNo", "1");
			label.put("testType", "Regression");
			label.put("env", "DIT");
			label.put("timestamp", "0l");
			label.put("framework", "SELENIUM");
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

	@Test
	public void processBuildingBlockMetrics_test_1() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("D40V");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getApplicationDetails());
		Mockito.when(buildingBlocksRepository.findByMetricLevelIdAndMetricLevel("D40V", MetricLevel.PRODUCT))
				.thenReturn(buildingBlockMetricsDetail());
		Mockito.when(metricsDetailRepository.findByMetricLevelIdAndLevelAndType("D40V", MetricLevel.PRODUCT,
				MetricType.TEST)).thenReturn(getMetricsDetail());
		testAnalysis.processBuildingBlockMetrics();
	}

	private ApplicationDetails getApplicationDetails() {
		ApplicationDetails appDetails = new ApplicationDetails();
		appDetails.setAppAcronym("appAcronym");
		appDetails.setAppId("D40V");
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

	@Test
	public void processBuildingBlockMetrics_test_2() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("B6LV");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		testAnalysis.processBuildingBlockMetrics();
	}

	@Test
	public void processExecutiveDetailsMetrics_test_1() {

		List<String> appIds = new ArrayList<>();
		appIds.add("D40V");
		Mockito.when(executiveSummaryListRepository.findAll()).thenReturn(getExecutiveSummaryList());
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(getExecutiveComponents());
		Mockito.when(portfolioResponseRepository.findByEid(Mockito.anyString())).thenReturn(getPortfolioResponse());
		Mockito.when(metricsDetailRepository.findByMetricLevelIdAndLevelAndType("123456789",
				MetricLevel.PORTFOLIO, MetricType.TEST)).thenReturn(getMetricsDetail());

		testAnalysis.processExecutiveDetailsMetrics();

	}

	private MetricSummary getMetricSummary() {

		MetricSummary summary = new MetricSummary();
		summary.setLastScanned(new Date());
		summary.setLastUpdated(new Date());
		summary.setName("test");
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
			label.put("success", "80");
			label.put("failed", "10");
			label.put("skipped", "10");
			label.put("total", "100");
			label.put("jobName", "NTS.D40V.Check.Check");
			label.put("buildNo", "1");
			label.put("testType", "Regression");
			label.put("env", "DIT");
			label.put("timestamp", "0l");
			label.put("framework", "SELENIUM");
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

	private ExecutiveComponents getExecutiveComponents() {

		ExecutiveComponents executiveComponents = new ExecutiveComponents();
		executiveComponents.setAppId("D40V");
		executiveComponents.setAppName("SomaAppName");
		executiveComponents.setMetrics(Arrays.asList(getExecutiveMetricsList()));
		executiveComponents.setTeamBoardLink("some link");
		return executiveComponents;
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
				MetricLevel.COMPONENT, MetricType.TEST)).thenReturn(getBuildingBlocksList());

		testAnalysis.processComponentDetailsMetrics();

	}

	private List<BuildingBlocks> getBuildingBlocksList() {
		List<BuildingBlocks> buildingBlockComponentSummaryResponseList = new ArrayList<BuildingBlocks>();
		BuildingBlocks buildingBlockComponentSummaryResponse = new BuildingBlocks();
		buildingBlockComponentSummaryResponse.setMetricLevelId("D40V");
		buildingBlockComponentSummaryResponse.setLob("NTS");
		buildingBlockComponentSummaryResponse.setName("test");
		buildingBlockComponentSummaryResponse.setPoc("some poc");
		buildingBlockComponentSummaryResponse.setTotalComponents(1);
		buildingBlockComponentSummaryResponse.setUrl("some url");
		List<MetricSummary> metrics = new ArrayList<>(Arrays.asList(getMetricSummary()));
		buildingBlockComponentSummaryResponse.setMetrics(metrics);
		return buildingBlockComponentSummaryResponseList;
	}

	private BuildingBlocks buildingBlockMetricsDetail() {
		BuildingBlocks buildingBlockMetricsDetail = new BuildingBlocks();
		buildingBlockMetricsDetail.setMetricLevelId("D40V");
		MetricSummary summary = new MetricSummary();
		summary.setLastScanned(new Date());
		summary.setLastUpdated(new Date());
		summary.setName("test");
		summary.setReportingComponents(1);
		summary.setTotalComponents(1);
		summary.setTrendSlope(2.3);
		summary.setCounts(settingCounts());
		buildingBlockMetricsDetail.setMetrics(Arrays.asList(summary));
		return buildingBlockMetricsDetail;
	}

}
