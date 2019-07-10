package com.capitalone.dashboard.custom;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import com.capitalone.dashboard.collector.MetricsProcessorSettings;
import com.capitalone.dashboard.dao.SecurityCsDetailsDAO;
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
import com.capitalone.dashboard.exec.model.vz.ExecutiveSummaryList;
import com.capitalone.dashboard.exec.model.vz.MetricsDetail;
import com.capitalone.dashboard.exec.model.vz.PortfolioResponse;
import com.capitalone.dashboard.exec.model.vz.SeriesCount;
import com.capitalone.dashboard.exec.repository.vz.ApplicationDetailsRepository;
import com.capitalone.dashboard.exec.repository.vz.BuildingBlocksRepository;
import com.capitalone.dashboard.exec.repository.vz.CollectorStatusRepository;
import com.capitalone.dashboard.exec.repository.vz.DateWiseMetricsSeriesRepository;
import com.capitalone.dashboard.exec.repository.vz.ExecutiveComponentRepository;
import com.capitalone.dashboard.exec.repository.vz.ExecutiveSummaryListRepository;
import com.capitalone.dashboard.exec.repository.vz.MetricsDetailRepository;
import com.capitalone.dashboard.exec.repository.vz.PortfolioResponseRepository;
import com.capitalone.dashboard.utils.GenericMethods;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

@RunWith(MockitoJUnitRunner.class)
public class SecurityAnalysisTest {

	@InjectMocks
	private SecurityAnalysis securityAnalysis;

	@Mock
	Query query;
	@Mock
	private GenericMethods genericMethods;

	@Mock
	private MongoTemplate mongoTemplate;

	@Mock
	private SecurityCsDetailsDAO securityDetailsDAO;
	@Mock
	private ExecutiveComponentRepository executiveComponentRepository;

	@Mock
	private CollectorStatusRepository collectorStatusRepository;

	@Mock
	private ExecutiveSummaryListRepository executiveSummaryListRepository;

	@Mock
	private MetricsDetailRepository metricsDetailRepository;

	@Mock
	private BuildingBlocksRepository buildingBlocksRepository;

	@Mock
	private PortfolioResponseRepository portfolioResponseRepository;

	@Mock
	private ApplicationDetailsRepository applicationDetailsRepository;

	@Mock
	private DateWiseMetricsSeriesRepository dateWiseMetricsSeriesRepository;
	@Mock
	private MongoClient mongoClient;
	@Mock
	private VastDetailsDAO vastDetailsDAO;
	@Mock
	DBCollection mongoCollection;

	@Mock
	MetricsProcessorSettings metricSettings;

	private String severity = "severity";
	private static final String CRITICALOVERDUE = "overdueCritical";
	private static final String MEDIUMOVERDUE = "overdueMajor";
	private static final String HIGHOVERDUE = "overdueBlocker";

	private static final String SEVERITY = "severity";
	private static final String SECURITYVIOLATIONS = "security-violations";

	@Test
	public void processSecurityDatewisedetailsTest() throws Exception {

		List<String> appIds = new ArrayList<>();
		appIds.add("hello");

		when(securityDetailsDAO.getConfiguredAppIds(mongoClient)).thenReturn(appIds);
		// when(metricSettings.getDateRange()).thenReturn(Mockito.anyLong());
		when(securityDetailsDAO.getMongoClient()).thenReturn(mongoClient);

		securityAnalysis.processDatewiseDetails();

	}

	@Test
	public void processSecurityDetailsTest() throws Exception {

		List<String> appIds = new ArrayList<>();
		appIds.add("hello");

		// Date lastUpdated = null;
		// when(collectorStatusRepository.findByType(CollectorType.Security)).thenReturn(makeCollectorType());
		// when(securityDetailsDAO.getSecurityData(Mockito.anyString(),Mockito.any(),
		// Mockito.any(MongoClient.class),Mockito.anyLong())).thenReturn(makeExecutiveComponents());
		// when(dateWiseMetricsSeriesRepository
		// .findByAppIdAndModuleNameAndMetricsNameOrderByTimeStampDesc(Mockito.anyString(),
		// Mockito.anyString(), Mockito.anyString())).thenReturn(null);
		when(securityDetailsDAO.getMongoClient()).thenReturn(mongoClient);
		when(securityDetailsDAO.getConfiguredAppIds(mongoClient)).thenReturn(appIds);
		when(dateWiseMetricsSeriesRepository.findByAppIdAndMetricsNameAndTimeStamp(Mockito.anyString(),
				Mockito.anyString(), Mockito.anyLong(), Mockito.any())).thenReturn(datewiseDetails());
		// when(securityDetailsDAO.getSecurityData(lastUpdated,
		// mongoClient)).thenReturn(makeExecutiveComponents());
		// when(executiveComponentRepository.findByAppIdAndMetric("TestMe","security-violations")).thenReturn(makeExecutiveComponents());
		// when(securityDetailsDAO.getConfiguredAppIds(mongoClient)).thenReturn(appIds);
		securityAnalysis.processExecutiveMetricsDetails();

	}

	private List<DateWiseMetricsSeries> datewiseDetails() {
		List<DateWiseMetricsSeries> datewiseList = new ArrayList<>();

		DateWiseMetricsSeries dateWise = new DateWiseMetricsSeries();

		dateWise.setAppId("D40V");
		dateWise.setDateValue(null);
		dateWise.setMetricsName("");
		dateWise.setModuleName("");
		dateWise.setTimeStamp(1234567l);
		datewiseList.add(dateWise);

		return datewiseList;
	}

	@Test
	public void processSecurityDetailsTest1() throws Exception {

		List<String> appIds = new ArrayList<>();
		appIds.add("hello");
		// Date lastUpdated = null;
		// when(executiveComponentRepository.findByAppIdAndMetric("TestMe","security-violations")).thenReturn(makeExecutiveComponents());
		// when(securityDetailsDAO.getSecurityData(Mockito.anyString(),Mockito.any(),
		// Mockito.any(MongoClient.class),Mockito.anyLong())).thenReturn(makeExecutiveComponents());
		// when(
		// .findByAppIdAndModuleNameAndMetricsNameOrderByTimeStampDesc(Mockito.anyString(),
		// Mockito.anyString(), Mockito.anyString())).thenReturn(null);
		// when(securityDetailsDAO.getConfiguredAppIds(mongoClient)).thenReturn(appIds);
		when(securityDetailsDAO.getMongoClient()).thenReturn(mongoClient);
		// when(collectorStatusRepository.findByType(CollectorType.Security)).thenReturn(null);
		securityAnalysis.processExecutiveMetricsDetails();

	}

	@Test
	public void processExecutiveDetailsMetricsTest() {

		try {
			when(executiveSummaryListRepository.findAll()).thenReturn(makeSummaryList());
			when(metricsDetailRepository.findByMetricLevelIdAndLevelAndType("1234", MetricLevel.PORTFOLIO,
					MetricType.SECURITY_VIOLATIONS)).thenReturn(makeMetricPDR());
			when(portfolioResponseRepository.findByEid(Mockito.anyString())).thenReturn(makePRV());
			Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
					.thenReturn(makeComponents());
			securityAnalysis.processExecutiveDetailsMetrics();
		} catch (Exception e) {

		}
	}

	private PortfolioResponse makePRV() {
		// TODO Auto-generated method stub
		PortfolioResponse pr = new PortfolioResponse();
		pr.setEid("1234");
		pr.setExecutive(null);
		pr.setName("I am Executive");
		pr.setLob(" apdina ?? ");

		return pr;
	}

	@Test
	public void processMetricsDetailResponseTest() {

		try {
			List<String> appIds = new ArrayList<String>();
			appIds.add("TestMe");

			Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
			Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);

			Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
					.thenReturn(makeComponents());
			/**
			 * Mockito.when(metricDetailResponseRepository
			 * .findByAppIdAndMetricsName(Mockito.anyString(),
			 * Mockito.anyString())).thenReturn(makeMDR());
			 **/
			Mockito.when(genericMethods.processAppCriticality(Mockito.anyString())).thenReturn("Critical");
			securityAnalysis.processMetricsDetailResponse();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void processBuildingBlockMetrics() {

		try {
			List<String> appIds = new ArrayList<String>();
			appIds.add("TestMe");

			Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
			Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
			Mockito.when(buildingBlocksRepository.findByMetricLevelIdAndMetricLevel("TestMe", MetricLevel.PRODUCT))
					.thenReturn(makeBBMSR());
			Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString()))
					.thenReturn(makeApplicationDetails());
			Mockito.when(metricsDetailRepository.findByMetricLevelIdAndLevelAndType("TestMe", MetricLevel.PRODUCT,
					MetricType.SECURITY_VIOLATIONS)).thenReturn(makeMDR());
			securityAnalysis.processBuildingBlockMetrics();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void processComponentDetailsMetricsTest() {

		try {
			List<String> appIds = new ArrayList<String>();
			appIds.add("TestMe");

			Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
			Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);

			Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
					.thenReturn(makeComponents());
			Mockito.when(buildingBlocksRepository.findByMetricLevelIdAndMetricLevelAndMetricType("TestMe",
					MetricLevel.COMPONENT, MetricType.SECURITY_VIOLATIONS)).thenReturn(makeBBCSR());
			Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString()))
					.thenReturn(makeApplicationDetails());
			securityAnalysis.processComponentDetailsMetrics();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void removeUnusedSecurityDetails() {

		try {
			List<String> appIds = new ArrayList<>();
			appIds.add("TestMe");

			Mockito.when(vastDetailsDAO.getAllAppIds(mongoClient)).thenReturn(appIds);

			List<ExecutiveComponents> securityDataList = new ArrayList<>();

			ExecutiveComponents executiveComponents = new ExecutiveComponents();
			securityDataList.add(executiveComponents);

			Mockito.when(executiveComponentRepository.getNotUsedAppIdList(appIds, SECURITYVIOLATIONS))
					.thenReturn(securityDataList);
			when(securityDetailsDAO.getMongoClient()).thenReturn(mongoClient);
			securityAnalysis.removeUnusedSecurityDetails();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private List<MetricSummary> makeMSR() {
		List<MetricSummary> msrList = new ArrayList<>();
		MetricSummary msr = new MetricSummary();
		List<MetricCount> mcrList = new ArrayList<>();
		MetricTimeSeriesElement mtser = new MetricTimeSeriesElement();

		MetricCount countMajor = new MetricCount();
		MetricCount countCritical = new MetricCount();
		MetricCount countBlocker = new MetricCount();

		Map<String, String> blocker = new HashMap<>();
		blocker.put("severity", "blocker");

		Map<String, String> critical = new HashMap<>();
		critical.put("severity", "critical");

		Map<String, String> major = new HashMap<>();
		major.put("severity", "major");

		countMajor.setLabel(major);
		countMajor.setValue((long) 3);
		countCritical.setLabel(critical);
		countCritical.setValue((long) 4);
		countBlocker.setLabel(blocker);
		countBlocker.setValue((long) 5);
		mcrList.add(countBlocker);
		mcrList.add(countCritical);
		mcrList.add(countMajor);

		mtser.setCounts(mcrList);
		mtser.setDaysAgo(0);
		mtser.setCounts(mcrList);
		mtser.setDaysAgo(1);

		msr.setCounts(mcrList);
		msr.setLastScanned(null);
		msr.setLastUpdated(null);
		msr.setTrendSlope((double) 2);
		msr.setName("security-violations");
		msr.setReportingComponents(100);
		msr.setTotalComponents(100);
		msrList.add(msr);

		return msrList;

	}

	private MetricsDetail makeMDR() {
		MetricsDetail mdr = new MetricsDetail();

		MetricSummary msr = new MetricSummary();
		List<MetricCount> counts = new ArrayList<>();
		List<MetricTimeSeriesElement> mtserList = new ArrayList<>();
		MetricTimeSeriesElement mtser = new MetricTimeSeriesElement();

		Map<String, String> codeBlockerLabel = new HashMap<>();
		codeBlockerLabel.put(severity, "codeBlocker");

		Map<String, String> codeCriticalLabel = new HashMap<>();
		codeCriticalLabel.put(severity, "codeCritical");

		Map<String, String> codeMajorLabel = new HashMap<>();
		codeMajorLabel.put(severity, "codeMajor");

		Map<String, String> webBlockerLabel = new HashMap<>();
		webBlockerLabel.put(severity, "webBlocker");

		Map<String, String> webCriticalLabel = new HashMap<>();
		webCriticalLabel.put(severity, "webCritical");

		Map<String, String> webMajorLabel = new HashMap<>();
		webMajorLabel.put(severity, "webMajor");

		Map<String, String> portBlockerLabel = new HashMap<>();
		portBlockerLabel.put(severity, "portBlocker");

		Map<String, String> portCriticalLabel = new HashMap<>();
		portCriticalLabel.put(severity, "portCritical");

		Map<String, String> portMajorLabel = new HashMap<>();
		portMajorLabel.put(severity, "portMajor");

		Map<String, String> major = new HashMap<>();
		major.put(severity, "major");

		Map<String, String> criticalOverDueMap = new HashMap<>();
		criticalOverDueMap.put(SEVERITY, CRITICALOVERDUE);

		Map<String, String> mediumOverDueMap = new HashMap<>();
		mediumOverDueMap.put(SEVERITY, MEDIUMOVERDUE);

		Map<String, String> highOverDueMap = new HashMap<>();
		highOverDueMap.put(SEVERITY, HIGHOVERDUE);

		MetricCount metricCountResponse = new MetricCount();
		metricCountResponse.setLabel(codeMajorLabel);
		metricCountResponse.setValue((long) 0);
		counts.add(metricCountResponse);

		metricCountResponse = new MetricCount();
		metricCountResponse.setLabel(codeBlockerLabel);
		metricCountResponse.setValue((long) 2);
		counts.add(metricCountResponse);

		metricCountResponse = new MetricCount();
		metricCountResponse.setLabel(codeCriticalLabel);
		metricCountResponse.setValue((long) 0);
		counts.add(metricCountResponse);

		metricCountResponse = new MetricCount();
		metricCountResponse.setLabel(portMajorLabel);
		metricCountResponse.setValue((long) 0);
		counts.add(metricCountResponse);

		metricCountResponse = new MetricCount();
		metricCountResponse.setLabel(portBlockerLabel);
		metricCountResponse.setValue((long) 0);
		counts.add(metricCountResponse);

		metricCountResponse = new MetricCount();
		metricCountResponse.setLabel(portCriticalLabel);
		metricCountResponse.setValue((long) 0);
		counts.add(metricCountResponse);

		metricCountResponse = new MetricCount();
		metricCountResponse.setLabel(webMajorLabel);
		metricCountResponse.setValue((long) 0);
		counts.add(metricCountResponse);

		metricCountResponse = new MetricCount();
		metricCountResponse.setLabel(webBlockerLabel);
		metricCountResponse.setValue((long) 0);
		counts.add(metricCountResponse);

		metricCountResponse = new MetricCount();
		metricCountResponse.setLabel(webCriticalLabel);
		metricCountResponse.setValue((long) 0);
		counts.add(metricCountResponse);

		metricCountResponse = new MetricCount();
		metricCountResponse.setLabel(criticalOverDueMap);
		metricCountResponse.setValue((long) 0);
		counts.add(metricCountResponse);

		metricCountResponse = new MetricCount();
		metricCountResponse.setLabel(mediumOverDueMap);
		metricCountResponse.setValue((long) 0);
		counts.add(metricCountResponse);

		metricCountResponse = new MetricCount();
		metricCountResponse.setLabel(highOverDueMap);
		metricCountResponse.setValue((long) 0);
		counts.add(metricCountResponse);

		mtser.setCounts(counts);
		mtser.setDaysAgo(0);
		mtser.setCounts(counts);
		mtser.setDaysAgo(1);

		msr.setCounts(counts);
		msr.setLastScanned(null);
		msr.setLastUpdated(null);
		msr.setTrendSlope((double) 0);
		msr.setName("security-violations");
		msr.setReportingComponents(100);
		msr.setTotalComponents(100);

		mdr.setMetricLevelId("TestMe");
		mdr.setType(MetricType.SECURITY_VIOLATIONS);
		mdr.setSummary(msr);
		mdr.setTimeSeries(mtserList);

		return mdr;
	}

	public void processMetricSummaryTest() {
		MetricSummary metricSummaryResponse = new MetricSummary();
		metricSummaryResponse.setLastScanned(null);
		metricSummaryResponse.setLastUpdated(null);
		metricSummaryResponse.setTotalComponents(1);
		metricSummaryResponse.setReportingComponents(1);
		metricSummaryResponse.setTrendSlope((double) 2);
		metricSummaryResponse.setCounts(null);
		metricSummaryResponse.setName("security-violations");

	}

	private ApplicationDetails makeApplicationDetails() {

		ApplicationDetails applicationDetails = new ApplicationDetails();

		applicationDetails.setAppAcronym("");
		applicationDetails.setAppId("TestMe");
		applicationDetails.setAppName("");
		applicationDetails.setAvailabilityStatus("Yes");
		applicationDetails.setCollectorStatus(null);
		applicationDetails.setDashboardAvailable(true);
		applicationDetails.setLob("Test");
		applicationDetails.setLastScanned(null);
		applicationDetails.setPoc("GradleTest");
		applicationDetails.setTeamBoardLink("www.google.com");
		applicationDetails.setTotalTeamBoards(5);

		// TODO Auto-generated method stub
		return applicationDetails;
	}

	private BuildingBlocks makeBBMSR() {
		// TODO Auto-generated method stub

		BuildingBlocks BBMSR = new BuildingBlocks();
		BBMSR.setMetricLevelId("TestMe");
		BBMSR.setCompleteness(5);
		BBMSR.setLob("Test");
		BBMSR.setMetrics(makeMSR());
		BBMSR.setName("security-violations");
		BBMSR.setPoc("GradleTest");
		BBMSR.setTotalComponents(100);
		BBMSR.setTotalExpectedMetrics(5);
		BBMSR.setUrl("onehygieia");
		return BBMSR;
	}

	private MetricsDetail makeMetricPDR() {
		// TODO Auto-generated method stub
		MetricsDetail MPDR = new MetricsDetail();

		List<MetricSummary> msrs = makeMSR();

		for (MetricSummary msr : msrs) {
			MPDR.setSummary(msr);
		}
		MPDR.setTimeSeries(makeMtser());
		MPDR.setType(MetricType.SECURITY_VIOLATIONS);

		MPDR.setCustomField(null);
		MPDR.setMetricLevelId("1234");

		return MPDR;
	}

	private List<MetricTimeSeriesElement> makeMtser() {
		List<MetricTimeSeriesElement> mtserList = new ArrayList<>();
		List<MetricCount> counts = new ArrayList<>();
		MetricTimeSeriesElement mtser = new MetricTimeSeriesElement();

		Map<String, String> codeBlockerLabel = new HashMap<>();
		codeBlockerLabel.put(severity, "codeBlocker");

		Map<String, String> codeCriticalLabel = new HashMap<>();
		codeCriticalLabel.put(severity, "codeCritical");

		Map<String, String> codeMajorLabel = new HashMap<>();
		codeMajorLabel.put(severity, "codeMajor");

		Map<String, String> webBlockerLabel = new HashMap<>();
		webBlockerLabel.put(severity, "webBlocker");

		Map<String, String> webCriticalLabel = new HashMap<>();
		webCriticalLabel.put(severity, "webCritical");

		Map<String, String> webMajorLabel = new HashMap<>();
		webMajorLabel.put(severity, "webMajor");

		Map<String, String> portBlockerLabel = new HashMap<>();
		portBlockerLabel.put(severity, "portBlocker");

		Map<String, String> portCriticalLabel = new HashMap<>();
		portCriticalLabel.put(severity, "portCritical");

		Map<String, String> portMajorLabel = new HashMap<>();
		portMajorLabel.put(severity, "portMajor");

		Map<String, String> major = new HashMap<>();
		major.put(severity, "major");

		Map<String, String> criticalOverDueMap = new HashMap<>();
		criticalOverDueMap.put(SEVERITY, CRITICALOVERDUE);

		Map<String, String> mediumOverDueMap = new HashMap<>();
		mediumOverDueMap.put(SEVERITY, MEDIUMOVERDUE);

		Map<String, String> highOverDueMap = new HashMap<>();
		highOverDueMap.put(SEVERITY, HIGHOVERDUE);

		MetricCount metricCountResponse = new MetricCount();
		metricCountResponse.setLabel(codeMajorLabel);
		metricCountResponse.setValue((long) 1);
		counts.add(metricCountResponse);

		metricCountResponse = new MetricCount();
		metricCountResponse.setLabel(codeBlockerLabel);
		metricCountResponse.setValue((long) 2);
		counts.add(metricCountResponse);

		metricCountResponse = new MetricCount();
		metricCountResponse.setLabel(codeCriticalLabel);
		metricCountResponse.setValue((long) 0);
		counts.add(metricCountResponse);

		metricCountResponse = new MetricCount();
		metricCountResponse.setLabel(portMajorLabel);
		metricCountResponse.setValue((long) 0);
		counts.add(metricCountResponse);

		metricCountResponse = new MetricCount();
		metricCountResponse.setLabel(portBlockerLabel);
		metricCountResponse.setValue((long) 0);
		counts.add(metricCountResponse);

		metricCountResponse = new MetricCount();
		metricCountResponse.setLabel(portCriticalLabel);
		metricCountResponse.setValue((long) 0);
		counts.add(metricCountResponse);

		metricCountResponse = new MetricCount();
		metricCountResponse.setLabel(webMajorLabel);
		metricCountResponse.setValue((long) 0);
		counts.add(metricCountResponse);

		metricCountResponse = new MetricCount();
		metricCountResponse.setLabel(webBlockerLabel);
		metricCountResponse.setValue((long) 0);
		counts.add(metricCountResponse);

		metricCountResponse = new MetricCount();
		metricCountResponse.setLabel(webCriticalLabel);
		metricCountResponse.setValue((long) 0);
		counts.add(metricCountResponse);

		metricCountResponse = new MetricCount();
		metricCountResponse.setLabel(criticalOverDueMap);
		metricCountResponse.setValue((long) 0);
		counts.add(metricCountResponse);

		metricCountResponse = new MetricCount();
		metricCountResponse.setLabel(mediumOverDueMap);
		metricCountResponse.setValue((long) 0);
		counts.add(metricCountResponse);

		metricCountResponse = new MetricCount();
		metricCountResponse.setLabel(highOverDueMap);
		metricCountResponse.setValue((long) 0);
		counts.add(metricCountResponse);

		mtser.setCounts(counts);
		mtser.setDaysAgo(0);
		mtser.setCounts(counts);
		mtser.setDaysAgo(1);
		mtserList.add(mtser);
		return mtserList;
	}

	private Iterable<ExecutiveSummaryList> makeSummaryList() {
		// TODO Auto-generated method stub
		List<ExecutiveSummaryList> exeList = new ArrayList<>();
		List<String> bussinessList = new ArrayList<>();
		bussinessList.add("Eclipse");
		ExecutiveSummaryList exeSummary = new ExecutiveSummaryList();

		exeSummary.setAppId(makeAppIds());
		exeSummary.setBusinessUnits(bussinessList);
		exeSummary.setConfiguredAppId(null);
		exeSummary.setConfiguredApps(0);
		exeSummary.setEid("1234");
		exeSummary.setFirstName("Gradle");
		exeSummary.setLastName("Test");
		exeSummary.setReportingPercentage((double) 0);
		exeSummary.setRole("CEO");
		exeSummary.setTotalApps(10);
		exeList.add(exeSummary);
		return exeList;
	}

	private List<BuildingBlocks> makeBBCSR() {

		List<BuildingBlocks> BBCSRList = new ArrayList<>();
		BuildingBlocks BBCSR = new BuildingBlocks();

		BBCSR.setMetricLevelId("TestMe");
		BBCSR.setCompleteness(10);
		BBCSR.setLob("");
		BBCSR.setMetrics(makeMSR());
		BBCSR.setName("security-violations");
		BBCSR.setPoc("GradleUser");
		BBCSR.setTotalComponents(100);
		BBCSR.setTotalExpectedMetrics(100);
		BBCSR.setUrl("");

		BBCSRList.add(BBCSR);
		return BBCSRList;
	}

	private ExecutiveComponents makeComponents() {
		ExecutiveComponents component = new ExecutiveComponents();

		List<ExecutiveMetrics> exeMetricsList = new ArrayList<ExecutiveMetrics>();
		ExecutiveMetrics metrics = new ExecutiveMetrics();

		List<ExecutiveModuleMetrics> exeModuleList = new ArrayList<>();
		ExecutiveModuleMetrics moduleMetrics = new ExecutiveModuleMetrics();

		List<ExecutiveMetricsSeries> exeMetricSerList = new ArrayList<>();
		ExecutiveMetricsSeries metricSeries = new ExecutiveMetricsSeries();

		List<SeriesCount> countList = new ArrayList<>();
		SeriesCount countCodeMajor = new SeriesCount();
		SeriesCount countCodeCritical = new SeriesCount();
		SeriesCount countCodeBlocker = new SeriesCount();

		SeriesCount countPortMajor = new SeriesCount();
		SeriesCount countPortCritical = new SeriesCount();
		SeriesCount countPortBlocker = new SeriesCount();

		SeriesCount countWebMajor = new SeriesCount();
		SeriesCount countWebCritical = new SeriesCount();
		SeriesCount countWebBlocker = new SeriesCount();

		SeriesCount countOverDueCritical = new SeriesCount();
		SeriesCount countOverDueHigh = new SeriesCount();
		SeriesCount countOverDueMedium = new SeriesCount();

		Map<String, String> codeBlocker = new HashMap<>();
		codeBlocker.put("severity", "codeBlocker");

		Map<String, String> codeCritical = new HashMap<>();
		codeCritical.put("severity", "codeCritical");

		Map<String, String> codeMajor = new HashMap<>();
		codeMajor.put("severity", "codeMajor");

		Map<String, String> webBlocker = new HashMap<>();
		webBlocker.put("severity", "webBlocker");

		Map<String, String> webCritical = new HashMap<>();
		webCritical.put("severity", "webCritical");

		Map<String, String> webMajor = new HashMap<>();
		webMajor.put("severity", "webMajor");

		Map<String, String> portBlocker = new HashMap<>();
		portBlocker.put("severity", "portBlocker");

		Map<String, String> portCritical = new HashMap<>();
		portCritical.put("severity", "portCritical");

		Map<String, String> portMajor = new HashMap<>();
		portMajor.put("severity", "portMajor");

		Map<String, String> criticalOverDueMap = new HashMap<>();
		criticalOverDueMap.put(SEVERITY, CRITICALOVERDUE);

		Map<String, String> mediumOverDueMap = new HashMap<>();
		mediumOverDueMap.put(SEVERITY, MEDIUMOVERDUE);

		Map<String, String> highOverDueMap = new HashMap<>();
		highOverDueMap.put(SEVERITY, HIGHOVERDUE);

		countCodeMajor.setLabel(codeMajor);
		countCodeMajor.setCount((long) 0);
		countCodeCritical.setLabel(codeCritical);
		countCodeCritical.setCount((long) 0);
		countCodeBlocker.setLabel(codeBlocker);
		countCodeBlocker.setCount((long) 0);

		countPortMajor.setLabel(webMajor);
		countPortMajor.setCount((long) 0);
		countPortCritical.setLabel(webCritical);
		countPortCritical.setCount((long) 0);
		countPortBlocker.setLabel(webBlocker);
		countPortBlocker.setCount((long) 0);

		countWebMajor.setLabel(portMajor);
		countWebMajor.setCount((long) 2);
		countWebCritical.setLabel(portCritical);
		countWebCritical.setCount((long) 0);
		countWebBlocker.setLabel(portBlocker);
		countWebBlocker.setCount((long) 0);

		countOverDueCritical.setLabel(criticalOverDueMap);
		countOverDueCritical.setCount((long) 10);
		countOverDueHigh.setLabel(mediumOverDueMap);
		countOverDueHigh.setCount((long) 18);
		countOverDueMedium.setLabel(highOverDueMap);
		countOverDueMedium.setCount((long) 25);

		countList.add(countWebBlocker);
		countList.add(countWebCritical);
		countList.add(countWebMajor);

		countList.add(countPortBlocker);
		countList.add(countPortCritical);
		countList.add(countPortMajor);

		countList.add(countCodeBlocker);
		countList.add(countCodeCritical);
		countList.add(countCodeMajor);

		countList.add(countOverDueCritical);
		countList.add(countOverDueHigh);
		countList.add(countOverDueMedium);

		metricSeries.setDaysAgo(0);
		metricSeries.setTimeStamp(12315l);
		metricSeries.setCounts(countList);
		exeMetricSerList.add(metricSeries);

		moduleMetrics.setLastScanned(null);
		moduleMetrics.setLastUpdated(null);
		moduleMetrics.setModuleName("security-violations");
		moduleMetrics.setTeamId("Avengers");
		moduleMetrics.setTrendSlope((double) 23l);
		moduleMetrics.setSeries(exeMetricSerList);
		exeModuleList.add(moduleMetrics);

		metrics.setLastScanned(null);
		metrics.setLastUpdated(null);
		metrics.setTrendSlope((double) 23l);
		metrics.setMetricsName("security-violations");
		metrics.setModules(exeModuleList);
		exeMetricsList.add(metrics);

		component.setAppId("TestMe");
		component.setAppName("UnitTest");
		component.setTeamBoardLink("www.google.com");
		component.setMetrics(exeMetricsList);
		return component;
	}

	private List<String> makeAppIds() {
		// TODO Auto-generated method stub
		List<String> appIds = new ArrayList<>();
		appIds.add("TestMe");
		appIds.add("DontTestMe");
		return appIds;
	}

	private ExecutiveComponents makeExecutiveComponents() {
		// TODO Auto-generated method stub
		List<ExecutiveComponents> exeCompList = new ArrayList<ExecutiveComponents>();
		ExecutiveComponents component = new ExecutiveComponents();

		List<ExecutiveMetrics> exeMetricsList = new ArrayList<ExecutiveMetrics>();
		ExecutiveMetrics metrics = new ExecutiveMetrics();

		List<ExecutiveModuleMetrics> exeModuleList = new ArrayList<>();
		ExecutiveModuleMetrics moduleMetrics = new ExecutiveModuleMetrics();

		List<ExecutiveMetricsSeries> exeMetricSerList = new ArrayList<>();
		ExecutiveMetricsSeries metricSeries = new ExecutiveMetricsSeries();

		List<SeriesCount> countList = new ArrayList<>();
		SeriesCount countMajor = new SeriesCount();
		SeriesCount countCritical = new SeriesCount();
		SeriesCount countBlocker = new SeriesCount();

		Map<String, String> blocker = new HashMap<>();
		blocker.put("severity", "blocker");

		Map<String, String> critical = new HashMap<>();
		critical.put("severity", "critical");

		Map<String, String> major = new HashMap<>();
		major.put("severity", "major");

		countMajor.setLabel(major);
		countMajor.setCount((long) 3);
		countCritical.setLabel(critical);
		countCritical.setCount((long) 4);
		countBlocker.setLabel(blocker);
		countBlocker.setCount((long) 5);
		countList.add(countBlocker);
		countList.add(countCritical);
		countList.add(countMajor);

		metricSeries.setDaysAgo(0);
		metricSeries.setTimeStamp(12315l);
		metricSeries.setCounts(countList);
		exeMetricSerList.add(metricSeries);

		moduleMetrics.setLastScanned(null);
		moduleMetrics.setLastUpdated(null);
		moduleMetrics.setModuleName("security-violations");
		moduleMetrics.setTeamId("Avengers");
		moduleMetrics.setTrendSlope((double) 23l);
		moduleMetrics.setSeries(exeMetricSerList);
		exeModuleList.add(moduleMetrics);

		metrics.setLastScanned(null);
		metrics.setLastUpdated(null);
		metrics.setMetricsName("security-violations");
		metrics.setModules(exeModuleList);
		exeMetricsList.add(metrics);

		component.setAppId("TestMe");
		component.setAppName("UnitTest");
		component.setTeamBoardLink("www.google.com");
		component.setMetrics(exeMetricsList);
		exeCompList.add(component);

		return component;
	}

	private CollectorStatus makeCollectorType() {
		// TODO Auto-generated method stub

		CollectorStatus status = new CollectorStatus();
		status.setCollectorName("Security");
		status.setLastUpdated(null);
		status.setType(CollectorType.Security);
		return status;
	}

}
