package com.capitalone.dashboard.custom;

import java.text.SimpleDateFormat;
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
import com.capitalone.dashboard.dao.ProductionIncidentsDAO;
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
import com.capitalone.dashboard.exec.model.MTTR;
import com.capitalone.dashboard.exec.model.MetricsDetail;
import com.capitalone.dashboard.exec.model.PortfolioResponse;
import com.capitalone.dashboard.exec.model.SeriesCount;
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
public class ProductionIncidentsAnalysisTest {

	@InjectMocks
	private ProductionIncidentsAnalysis productionIncidentsAnalysis;
	@Mock
	private ExecutiveSummaryListRepository executiveSummaryListRepository;
	@Mock
	private ProductionIncidentsDAO productionIncidentsDAO;
	@Mock
	private ExecutiveComponentRepository executiveComponentRepository;
	@Mock
	private CollectorStatusRepository collectorStatusRepository;
	@Mock
	private ApplicationDetailsRepository applicationDetailsRepository;
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
	private DateWiseMetricsSeriesRepository dateWiseMetricsSeriesRepository;
	@Mock
	private VastDetailsDAO vastDetailsDAO;
	@Mock
	private MetricsProcessorSettings metricsSettings;
	@Mock
	private GenericMethods genericMethods;
	@Mock
	private BuildingBlocksRepository buildingBlocksRepository;

	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

	private static final String PRODINCIDENTS = "production-incidents";
	private static final String SEVERITY = "severity";
	private static final String TIMETORESOLVE = "timeToResolve";
	private static final String IMPACTEDAPPS = "impactedApps";

	@Test
	public void processProductionIncidentsDetails_1() {
		try {

			List<SeriesCount> counts = new ArrayList<>();
			SeriesCount series1 = new SeriesCount();
			Map<String, String> label = new HashMap<>();
			label.put(SEVERITY, "1");
			series1.setLabel(label);
			series1.setCount((long) 232);
			counts.add(series1);

			SeriesCount series2 = new SeriesCount();
			Map<String, String> label1 = new HashMap<>();
			label1.put(TIMETORESOLVE, "23");
			series2.setLabel(label1);
			series2.setCount((long) 5);
			counts.add(series2);

			SeriesCount series3 = new SeriesCount();
			Map<String, String> label2 = new HashMap<String, String>();
			label2.put(IMPACTEDAPPS, "2");
			series3.setLabel(label2);
			series3.setCount((long) 5);
			counts.add(series3);

			List<DateWiseMetricsSeries> seriesList = new ArrayList<>();
			DateWiseMetricsSeries series = new DateWiseMetricsSeries();
			series.setAppId("D40V");
			series.setDateValue("05-05-2018");
			series.setTeamId("1234423");
			series.setTimeStamp(1521417600000l);
			series.setCounts(counts);
			seriesList.add(series);

			Mockito.when(productionIncidentsDAO.getEntireAppList(mongoClient)).thenReturn(getAppList());
			Mockito.when(productionIncidentsDAO.getMongoClient()).thenReturn(mongoClient);
			Mockito.when(executiveComponentRepository.findByAppIdAndMetric("B6LV", "production-incidents"))
					.thenReturn(getExecutiveComponents());
			productionIncidentsAnalysis.processExecutiveMetricsDetails();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void removeUnusedProductionIncidentsDetails_1() {
		productionIncidentsAnalysis.removeUnusedProductionIncidentsDetails();
		productionIncidentsAnalysis.processDateWiseTrend();
	}

	@Test
	public void removeUnusedProductionIncidentsDetails() throws Exception {
		Mockito.when(productionIncidentsDAO.getMongoClient()).thenThrow(new NullPointerException());
		productionIncidentsAnalysis.removeUnusedProductionIncidentsDetails();
		productionIncidentsAnalysis.processDateWiseTrend();
	}

	@Test
	public void removeUnusedProductionIncidentsDetails_2() {

		List<MTTR> mttrList = new ArrayList<>();

		MTTR mttr = new MTTR();
		mttr.setCrisisId("3231432");
		mttr.setItduration(123);
		mttr.setCrisisLevel("SEV1");
		mttr.setEventStartDT("2017-04-19T10:49:00");

		mttrList.add(mttr);

		DateWiseMetricsSeries dateSeries = new DateWiseMetricsSeries();
		dateSeries.setTimeStamp(1530004745000l);
		Mockito.when(productionIncidentsDAO.getProductionIncidentsDataByAppIdByRegex("D40V", mongoClient, "2018-06-26"))
				.thenReturn(mttrList);
		Mockito.when(productionIncidentsDAO.getEntireAppList(mongoClient)).thenReturn(getAppList());
		Mockito.when(vastDetailsDAO.getAllAppIds(mongoClient)).thenReturn(getAppList());
		Mockito.when(productionIncidentsDAO.getMongoClient()).thenReturn(mongoClient);
		Mockito.when(
				dateWiseMetricsSeriesRepository.findByAppIdAndMetricsNameOrderByTimeStampDesc("D40V", PRODINCIDENTS))
				.thenReturn(dateSeries);
		metricsSettings.setDateRange(12456317l);
		ProductionIncidentsAnalysis productionIncidentsAnalysis1 = new ProductionIncidentsAnalysis(
				executiveComponentRepository,
				executiveSummaryListRepository,
				productionIncidentsDAO, applicationDetailsRepository,
				mongoTemplate, metricsDetailRepository,
				portfolioResponseRepository,
				buildingBlocksRepository,
				vastDetailsDAO,  metricsSettings,
				 dateWiseMetricsSeriesRepository,  genericMethods);
		productionIncidentsAnalysis1.removeUnusedProductionIncidentsDetails();
		productionIncidentsAnalysis1.processDateWiseTrend();
	}

	@Test
	public void processProductionIncidentsDetails_2() {
		try {
			Mockito.when(productionIncidentsDAO.getMongoClient()).thenReturn(mongoClient);
			Mockito.when(productionIncidentsDAO.getEntireAppList(Mockito.any(MongoClient.class)))
					.thenReturn(getAppList());
			Mockito.when(executiveComponentRepository.findByAppIdAndMetric("B6LV", "production-incidents"))
					.thenReturn(null);
			productionIncidentsAnalysis.processExecutiveMetricsDetails();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void processProductionIncidentsDetails_3() {
		try {
			Mockito.when(productionIncidentsDAO.getMongoClient()).thenReturn(mongoClient);
			Mockito.when(productionIncidentsDAO.getEntireAppList(Mockito.any(MongoClient.class)))
					.thenReturn(getAppList());
			// Mockito.when(productionIncidentsDAO.getProductionIncidentsDataByAppId(Mockito.anyString(),
			// Mockito.any(MongoClient.class))).thenReturn(getMttrList());
			Mockito.when(executiveComponentRepository.findByAppIdAndMetric("B6LV", "production-incidents"))
					.thenReturn(null);
			productionIncidentsAnalysis.processExecutiveMetricsDetails();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void processProductionIncidentsDetails_4() {
		try {
			Mockito.when(productionIncidentsDAO.getMongoClient()).thenReturn(mongoClient);
			Mockito.when(productionIncidentsDAO.getEntireAppList(Mockito.any(MongoClient.class)))
					.thenReturn(getAppList());
			Mockito.when(executiveComponentRepository.findByAppIdAndMetric("B6LV", "production-incidents"))
					.thenReturn(null);
			productionIncidentsAnalysis.processExecutiveMetricsDetails();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void processMetricsDetailResponse_1() {
		try {
			List<String> appIds = new ArrayList<String>();
			appIds.add("B6LV");

			Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
			Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);

			Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
					.thenReturn(getExecutiveComponents());
			Mockito.when(
					metricsDetailRepository.findByMetricLevelIdAndLevelAndType("B6LV", MetricLevel.PRODUCT, MetricType.PRODUCTION_INCIDENTS))
					.thenReturn(getMetricsDetail());
			Mockito.when(genericMethods.processAppCriticality(Mockito.anyString())).thenReturn("Critical");
			Mockito.when(productionIncidentsDAO.getMTTRDetails(Mockito.anyList())).thenReturn(getMttrList());
			productionIncidentsAnalysis.processMetricsDetailResponse();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void processBuildingBlockMetrics_1() {
		try {
			List<String> appIds = new ArrayList<String>();
			appIds.add("B6LV");

			Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
			Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
			Mockito.when( buildingBlocksRepository
					.findByMetricLevelIdAndMetricLevel("B6LV", MetricLevel.PRODUCT))
					.thenReturn(getBuildingBlockMetricsSummary());
			Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString()))
					.thenReturn(getApplicationDetails());
			Mockito.when(
					metricsDetailRepository
					.findByMetricLevelIdAndLevelAndType("B6LV", MetricLevel.PRODUCT, MetricType.PRODUCTION_INCIDENTS))
					.thenReturn(getMetricsDetail());
			productionIncidentsAnalysis.processBuildingBlockMetrics();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void processBuildingBlockMetrics_2() {
		try {
			List<String> appIds = new ArrayList<String>();
			appIds.add("B6LV");

			Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
			Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
			Mockito.when(buildingBlocksRepository
					.findByMetricLevelIdAndMetricLevel("B6LV", MetricLevel.PRODUCT)).thenReturn(null);
			Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString()))
					.thenReturn(getApplicationDetails());
			Mockito.when(
					metricsDetailRepository.findByMetricLevelIdAndLevelAndType("B6LV", MetricLevel.PRODUCT, MetricType.PRODUCTION_INCIDENTS))
					.thenReturn(getMetricsDetail());
			productionIncidentsAnalysis.processBuildingBlockMetrics();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void processExecutiveDetailsMetrics_1() {

		try {
			Mockito.when(executiveSummaryListRepository.findAll()).thenReturn(getExecutiveSummaryList());
			Mockito.when(
					metricsDetailRepository
					.findByMetricLevelIdAndLevelAndType(Mockito.anyString(), MetricLevel.PORTFOLIO, MetricType.PRODUCTION_INCIDENTS))
					.thenReturn(getMetricPortfolioDetailResponse());
			Mockito.when(portfolioResponseRepository.findByEid(Mockito.anyString())).thenReturn(getPortfolioResponse());
			Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
					.thenReturn(getExecutiveComponents_1());
			productionIncidentsAnalysis.processExecutiveDetailsMetrics();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void processExecutiveDetailsMetrics_2() {

		try {
			Mockito.when(executiveSummaryListRepository.findAll()).thenReturn(getExecutiveSummaryList());
			Mockito.when(
					metricsDetailRepository
					.findByMetricLevelIdAndLevelAndType(Mockito.anyString(), MetricLevel.PORTFOLIO, MetricType.PRODUCTION_INCIDENTS))
					.thenReturn(null);
			Mockito.when(portfolioResponseRepository.findByEid(Mockito.anyString())).thenReturn(getPortfolioResponse());
			Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
					.thenReturn(null);
			productionIncidentsAnalysis.processExecutiveDetailsMetrics();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void processExecutiveDetailsMetrics_3() {

		try {
			Mockito.when(executiveSummaryListRepository.findAll()).thenReturn(getExecutiveSummaryList_1());
			Mockito.when(
					metricsDetailRepository
					.findByMetricLevelIdAndLevelAndType(Mockito.anyString(), MetricLevel.PORTFOLIO, MetricType.PRODUCTION_INCIDENTS))
					.thenReturn(getMetricPortfolioDetailResponse());
			Mockito.when(portfolioResponseRepository.findByEid(Mockito.anyString())).thenReturn(getPortfolioResponse());
			// Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(),Mockito.anyString())).thenReturn(getExecutiveComponents());
			productionIncidentsAnalysis.processExecutiveDetailsMetrics();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void processComponentDetailsMetrics_1() {
		try {
			List<String> appIds = new ArrayList<String>();
			appIds.add("B6LV");

			Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
			Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
			Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
					.thenReturn(getExecutiveComponents_1());
			Mockito.when(buildingBlocksRepository.findByMetricLevelIdAndMetricLevelAndMetricType("B6LV", MetricLevel.COMPONENT, MetricType.PRODUCTION_INCIDENTS)).thenReturn(getBuildingBlocksList());
			Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString()))
					.thenReturn(getApplicationDetails());
			Mockito.when(productionIncidentsDAO.getMTTRDetails(Mockito.anyList())).thenReturn(getMttrList());
			productionIncidentsAnalysis.processComponentDetailsMetrics();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<String> getAppList() {
		List<String> appList = new ArrayList<>();
		appList.add("B6LV");
		appList.add("D40V");
		return appList;
	}

	private List<BuildingBlocks> getBuildingBlocksList() {
		List<BuildingBlocks> buildingBlockComponentSummaryResponseList = new ArrayList<BuildingBlocks>();
		BuildingBlocks buildingBlockComponentSummaryResponse = new BuildingBlocks();
		buildingBlockComponentSummaryResponse.setMetricLevelId("B6LV");
		buildingBlockComponentSummaryResponse.setLob("NTS");
		buildingBlockComponentSummaryResponse.setName("production-incidents");
		buildingBlockComponentSummaryResponse.setPoc("some poc");
		buildingBlockComponentSummaryResponse.setTotalComponents(1);
		buildingBlockComponentSummaryResponse.setUrl("some url");
		List<MetricSummary> metrics = new ArrayList<>(Arrays.asList(getMetricSummary()));
		buildingBlockComponentSummaryResponse.setMetrics(metrics);
		return buildingBlockComponentSummaryResponseList;
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

	private MetricsDetail getMetricPortfolioDetailResponse() {

		MetricsDetail metricPortfolioDetailResponse = new MetricsDetail();

		metricPortfolioDetailResponse.setMetricLevelId("123456");
		metricPortfolioDetailResponse.setLevel(MetricLevel.PRODUCT);
		metricPortfolioDetailResponse.setType(MetricType.PRODUCTION_INCIDENTS);
		metricPortfolioDetailResponse.setCustomField(new ObjectId().toString());
		MetricSummary summary = getMetricSummary();
		metricPortfolioDetailResponse.setSummary(summary);
		List<MetricTimeSeriesElement> timeSeries = new ArrayList<MetricTimeSeriesElement>();
		MetricTimeSeriesElement time = new MetricTimeSeriesElement();
		time.setCounts(settingCounts());
		time.setDaysAgo(10);
		timeSeries.add(time);
		metricPortfolioDetailResponse.setTimeSeries(timeSeries);

		return metricPortfolioDetailResponse;
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

	private List<ExecutiveSummaryList> getExecutiveSummaryList_1() {
		List<ExecutiveSummaryList> listOfExecutiveSummaryList = new ArrayList<>();

		ExecutiveSummaryList executiveSummaryList = new ExecutiveSummaryList();
		executiveSummaryList.setAppId(null);
		executiveSummaryList.setBusinessUnits(Arrays.asList("B6LV"));
		executiveSummaryList.setLastName("some name");
		executiveSummaryList.setConfiguredApps(1);
		executiveSummaryList.setConfiguredAppId(null);
		executiveSummaryList.setEid("123456789");
		executiveSummaryList.setFirstName("some name");
		executiveSummaryList.setReportingPercentage(100.0);
		executiveSummaryList.setRole("some role");
		executiveSummaryList.setTotalApps(1);

		listOfExecutiveSummaryList.add(executiveSummaryList);

		return listOfExecutiveSummaryList;
	}

	private ApplicationDetails getApplicationDetails() {
		ApplicationDetails applicationDetails = new ApplicationDetails();
		applicationDetails.setAppAcronym("Some acronym");
		applicationDetails.setAppId("B6LV");
		applicationDetails.setAppName("Some app name");
		applicationDetails.setAvailabilityStatus("some status");
		applicationDetails.setDashboardAvailable(true);
		applicationDetails.setLastScanned(new Date());
		applicationDetails.setLob("NTS");
		applicationDetails.setPoc("some poc");
		applicationDetails.setTeamBoardLink("some link");
		applicationDetails.setTotalTeamBoards(1);
		return applicationDetails;
	}

	private BuildingBlocks getBuildingBlockMetricsSummary() {
		BuildingBlocks buildingBlockMetricsDetail = new BuildingBlocks();
		buildingBlockMetricsDetail.setMetricLevelId("B6LV");
		buildingBlockMetricsDetail.setLob("NTS");
		buildingBlockMetricsDetail.setName("production-incidents");
		buildingBlockMetricsDetail.setPoc("some POC");
		buildingBlockMetricsDetail.setTotalComponents(1);
		buildingBlockMetricsDetail.setTotalExpectedMetrics(1);
		buildingBlockMetricsDetail.setUrl("some url");
		List<MetricSummary> metrics = new ArrayList<MetricSummary>();
		metrics.add(getMetricsDetailForSecurityViolations());
		metrics.add(getMetricSummary());
		buildingBlockMetricsDetail.setMetrics(metrics);
		return buildingBlockMetricsDetail;
	}

	private MetricsDetail getMetricsDetail() {

		MetricsDetail metricDetailResponse = new MetricsDetail();
		metricDetailResponse.setMetricLevelId("B6LV");
		metricDetailResponse.setType(MetricType.PRODUCTION_INCIDENTS);

		metricDetailResponse.setSummary(getMetricSummary());
		List<MetricTimeSeriesElement> timeSeries = new ArrayList<>();
		MetricTimeSeriesElement time = new MetricTimeSeriesElement();

		time.setCounts(settingCounts());
		time.setDaysAgo(10);
		timeSeries.add(time);
		metricDetailResponse.setTimeSeries(timeSeries);
		return metricDetailResponse;
	}

	private MetricSummary getMetricSummary() {

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

	private MetricSummary getMetricsDetailForSecurityViolations() {

		MetricSummary summary = new MetricSummary();
		summary.setLastScanned(new Date());
		summary.setLastUpdated(new Date());
		summary.setName("security-violations");
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

		count = new MetricCount();
		label = new HashMap<String, String>();
		label.put("type", "MTBF");
		count.setLabel(label);
		count.setValue(90);
		counts.add(count);

		return counts;
	}

	private CollectorStatus getCollectorStatusOfMttr() {
		CollectorStatus collectorStatus = new CollectorStatus();

		collectorStatus.setCollectorName("MTTR");
		collectorStatus.setLastUpdated(new Date());
		collectorStatus.setType(CollectorType.MTTR);

		return collectorStatus;
	}

	private List<MTTR> getMttrList() {
		MTTR mttr = new MTTR();
		mttr.setAppId("B6LV");
		mttr.setCrisisId("12341234");
		mttr.setCrisisLevel("SEV2");
		mttr.setCauseCode("Application");
		mttr.setEventStartDT("asdfasdfasdfaasdf");
		mttr.setItduration(44);
		mttr.setOwningEntity("some entity");
		mttr.setServiceLevel("some level");
		return Arrays.asList(mttr);
	}

	private List<MTTR> getMttrList_1() {

		List<MTTR> mttrList = new ArrayList<>();

		MTTR mttr = new MTTR();
		mttr.setAppId("B6LV");
		mttr.setCrisisId("12341234");
		mttr.setCauseCode("Application");
		mttr.setCrisisLevel("SEV2");
		mttr.setEventStartDT(getDateString(48));
		mttr.setItduration(44);
		mttr.setOwningEntity("some entity");
		mttr.setServiceLevel("some level");

		MTTR mttr_1 = new MTTR();
		mttr_1.setAppId("B6LV");
		mttr_1.setCrisisId("12341124");
		mttr_1.setCauseCode("Application");
		mttr_1.setCrisisLevel("SEV2");
		mttr_1.setEventStartDT(getDateString(48));
		mttr_1.setItduration(53);
		mttr_1.setOwningEntity("some entity");
		mttr_1.setServiceLevel("some level");

		MTTR mttr_2 = new MTTR();
		mttr_2.setAppId("B6LV");
		mttr_2.setCrisisId("12341124");
		mttr_2.setCauseCode("");
		mttr_2.setCrisisLevel("SEV1");
		mttr_2.setEventStartDT(getDateString(39));
		mttr_2.setItduration(53);
		mttr_2.setOwningEntity("some entity");
		mttr_2.setServiceLevel("some level");

		MTTR mttr_3 = new MTTR();
		mttr_3.setAppId("B6LV");
		mttr_3.setCrisisId("12341124");
		mttr_3.setCauseCode("Vendor");
		mttr_3.setCrisisLevel("SEV2");
		mttr_3.setEventStartDT(getDateString(39));
		mttr_3.setItduration(53);
		mttr_3.setOwningEntity("some entity");
		mttr_3.setServiceLevel("some level");

		mttrList.add(mttr);
		mttrList.add(mttr_1);
		mttrList.add(mttr_2);
		mttrList.add(mttr_3);

		return mttrList;
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
		executiveMetrics.setMetricsName("production-incidents");
		ExecutiveModuleMetrics module = new ExecutiveModuleMetrics();
		module.setLastScanned(new Date());
		module.setLastUpdated(new Date());
		module.setModuleName("mttr module");
		module.setTeamId("no sure");
		module.setTrendSlope((double) 0);
		ExecutiveMetricsSeries executiveMetricsSeries = new ExecutiveMetricsSeries();
		executiveMetricsSeries.setDaysAgo(12);
		executiveMetricsSeries.setTimeStamp(123456789l);

		List<SeriesCount> seriesCountList = new ArrayList<SeriesCount>();
		SeriesCount seriesCount = new SeriesCount();
		Map<String, String> label = new HashMap<String, String>();
		label.put("severity", "1");
		label.put("timeToResolve", "43");
		seriesCount.setLabel(label);
		seriesCount.setCount(2l);
		seriesCountList.add(seriesCount);

		seriesCount = new SeriesCount();
		label = new HashMap<String, String>();
		label.put("severity", "2");
		label.put("timeToResolve", "21");
		seriesCount.setLabel(label);
		seriesCount.setCount(5l);
		seriesCountList.add(seriesCount);

		executiveMetricsSeries.setCounts(seriesCountList);
		// executiveMetricsSeries.setTimeStamp(123456789l);
		// executiveMetricsSeries.setDaysAgo(21);
		List<ExecutiveMetricsSeries> executiveMetricsSeriesList = new ArrayList<ExecutiveMetricsSeries>();
		executiveMetricsSeriesList.add(executiveMetricsSeries);
		module.setSeries(executiveMetricsSeriesList);

		List<ExecutiveModuleMetrics> moduleList = new ArrayList<ExecutiveModuleMetrics>();
		moduleList.add(module);

		executiveMetrics.setModules(moduleList);
		executiveMetrics.setTrendSlope((double) 0);

		return executiveMetrics;
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
		executiveMetrics.setMetricsName("production-incidents");
		ExecutiveModuleMetrics module = new ExecutiveModuleMetrics();
		module.setLastScanned(new Date());
		module.setLastUpdated(new Date());
		module.setModuleName("mttr module");
		module.setTeamId("no sure");
		module.setTrendSlope((double) 0);

		List<ExecutiveMetricsSeries> executiveMetricsSeriesList = new ArrayList<ExecutiveMetricsSeries>();

		ExecutiveMetricsSeries executiveMetricsSeries = new ExecutiveMetricsSeries();
		executiveMetricsSeries.setDaysAgo(12);
		executiveMetricsSeries.setTimeStamp(123456789l);
		executiveMetricsSeries.setCounts(getSeriesCountList());
		executiveMetricsSeriesList.add(executiveMetricsSeries);

		executiveMetricsSeries = new ExecutiveMetricsSeries();
		executiveMetricsSeries.setDaysAgo(12);
		executiveMetricsSeries.setTimeStamp(123456789l);
		executiveMetricsSeries.setCounts(getSeriesCountList());
		executiveMetricsSeriesList.add(executiveMetricsSeries);

		executiveMetricsSeries = new ExecutiveMetricsSeries();
		executiveMetricsSeries.setDaysAgo(13);
		executiveMetricsSeries.setTimeStamp(123456789l);
		executiveMetricsSeries.setCounts(getSeriesCountList());
		executiveMetricsSeriesList.add(executiveMetricsSeries);

		module.setSeries(executiveMetricsSeriesList);

		List<ExecutiveModuleMetrics> moduleList = new ArrayList<ExecutiveModuleMetrics>();
		moduleList.add(module);

		executiveMetrics.setModules(moduleList);
		executiveMetrics.setTrendSlope((double) 0);

		return executiveMetrics;
	}

	public List<SeriesCount> getSeriesCountList() {
		List<SeriesCount> seriesCountList = new ArrayList<SeriesCount>();
		SeriesCount seriesCount = new SeriesCount();
		Map<String, String> label = new HashMap<String, String>();
		label.put("severity", "1");
		label.put("timeToResolve", "43");
		seriesCount.setLabel(label);
		seriesCount.setCount(2l);
		seriesCountList.add(seriesCount);

		seriesCount = new SeriesCount();
		label = new HashMap<String, String>();
		label.put("severity", "2");
		label.put("timeToResolve", "21");
		seriesCount.setLabel(label);
		seriesCount.setCount(5l);
		seriesCountList.add(seriesCount);

		seriesCount = new SeriesCount();
		label = new HashMap<String, String>();
		label.put("severity", "2");
		label.put("timeToResolve", "21");
		seriesCount.setLabel(label);
		seriesCount.setCount(5l);
		seriesCountList.add(seriesCount);

		seriesCount = new SeriesCount();
		label = new HashMap<String, String>();
		label.put("severity", "1");
		label.put("timeToResolve", "21");
		seriesCount.setLabel(label);
		seriesCount.setCount(5l);
		seriesCountList.add(seriesCount);

		return seriesCountList;
	}

	private String getDateString(int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -days);
		Date date = calendar.getTime();
		return DATE_FORMAT.format(date) + "T" + TIME_FORMAT.format(date);
	}

}
