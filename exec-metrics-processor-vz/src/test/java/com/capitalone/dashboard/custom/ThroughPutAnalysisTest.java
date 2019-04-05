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
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.capitalone.dashboard.collector.MetricsProcessorConfig;
import com.capitalone.dashboard.dao.DashboardDetailsDAO;
import com.capitalone.dashboard.dao.ThroughPutDAO;
import com.capitalone.dashboard.dao.VastDetailsDAO;
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
import com.capitalone.dashboard.exec.model.vz.ComputedPipelineMetrics;
import com.capitalone.dashboard.exec.model.vz.Dashboard;
import com.capitalone.dashboard.exec.model.vz.ExecutiveComponents;
import com.capitalone.dashboard.exec.model.vz.ExecutiveMetrics;
import com.capitalone.dashboard.exec.model.vz.ExecutiveMetricsSeries;
import com.capitalone.dashboard.exec.model.vz.ExecutiveModuleMetrics;
import com.capitalone.dashboard.exec.model.vz.ExecutiveSummaryList;
import com.capitalone.dashboard.exec.model.vz.InstanceCollectorStatus;
import com.capitalone.dashboard.exec.model.vz.JenkinsUnlimitedData;
import com.capitalone.dashboard.exec.model.vz.MetricsDetail;
import com.capitalone.dashboard.exec.model.vz.ProductPipelineData;
import com.capitalone.dashboard.exec.model.vz.SeriesCount;
import com.capitalone.dashboard.exec.model.vz.ThroughPutModel;
import com.capitalone.dashboard.exec.repository.vz.ApplicationDetailsRepository;
import com.capitalone.dashboard.exec.repository.vz.BuildingBlocksRepository;
import com.capitalone.dashboard.exec.repository.vz.CollectorStatusRepository;
import com.capitalone.dashboard.exec.repository.vz.ExecutiveComponentRepository;
import com.capitalone.dashboard.exec.repository.vz.ExecutiveSummaryListRepository;
import com.capitalone.dashboard.exec.repository.vz.JenkinsUnlimitedDataRepository;
import com.capitalone.dashboard.exec.repository.vz.MetricsDetailRepository;
import com.capitalone.dashboard.utils.GenericMethods;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

@RunWith(MockitoJUnitRunner.class)
public class ThroughPutAnalysisTest {

	@InjectMocks
	private ThroughPutAnalysis throughPutAnalysis;
	@Mock
	private ThroughPutDAO throughPutDAO;
	@Mock
	private GenericMethods genericMethods;
	@Mock
	private DashboardDetailsDAO dashboardDetailsDAO;
	@Mock
	private MongoTemplate mongoTemplate;
	@Mock
	private ExecutiveComponentRepository executiveComponentRepository;
	@Mock
	private ExecutiveSummaryListRepository executiveSummaryListRepository;
	@Mock
	private MetricsDetailRepository metricDetailResponseRepository;
	@Mock
	private ApplicationDetailsRepository applicationDetailsRepository;
	@Mock
	private CollectorStatusRepository collectorStatusRepository;
	@Mock
	private BuildingBlocksRepository buildingBlocksRepository;
	@Mock
	private MetricsDetailRepository metricsDetailRepository;
	@Mock
	private DBCollection mongoCollection;
	@Mock
	private MongoClient mongoClient;
	@Mock
	MetricsProcessorConfig metricsProcessorConfig;
	@Mock
	JenkinsUnlimitedDataRepository jenkinsUnlimitedDataRepository;
	@Mock
	ServiceNowAnalysis serviceNowAnalysis;
	@Mock
	VastDetailsDAO vastDetailsDAO;

	@Test
	public void testProcessComponentDetailsMetrics() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("B6LV");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric("B6LV", "pipeline-lead-time"))
				.thenReturn(getExecutiveComponents());
		Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getApplicationDetails());
		throughPutAnalysis.processComponentDetailsMetrics();
	}

	@Test
	public void testProcessComponentDetailsMetrics_1() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("B6LV");
		// Mockito.when(throughPutDAO.getDbDetails(Mockito.anyString(),
		// Mockito.any(MongoClient.class))).thenReturn(getDbMap());
		// Mockito.when(throughPutDAO.getRemainingModuleList(new
		// HashMap<>())).thenReturn(Arrays.asList("asdfasdfasdf"));
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric("B6LV", "pipeline-lead-time"))
				.thenReturn(getExecutiveComponents_1());
		Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getApplicationDetails());
		throughPutAnalysis.processComponentDetailsMetrics();
	}

	public Map<String, String> getDbMap() {
		Map<String, String> dbdetails = new HashMap<String, String>();
		dbdetails.put("asdf", "asdf");
		return dbdetails;
	}

	@Test
	public void testProcessThroughPutMetricsDetails() throws Exception {
		List<String> appIds = new ArrayList<String>();
		String app = "B6LV";
		String app1 = "VQEV";
		appIds.add(app1);
		appIds.add(app);
		Query basicQuery = new Query();
		basicQuery.addCriteria(Criteria.where("appId").is("B6LV"));
		Mockito.when(throughPutDAO.getMongoClient()).thenReturn(mongoClient);
		Mockito.when(collectorStatusRepository.findByType(CollectorType.Portfolio_DB))
				.thenReturn(new CollectorStatus());
		Mockito.when(throughPutDAO.getEntireAppList(mongoClient)).thenReturn(appIds);
		Mockito.when(throughPutDAO.getByAppId("B6LV", mongoClient)).thenReturn(getPipelineData());

		Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getApplicationDetails());
		Dashboard dashboard = new Dashboard("", "", new Application(), "", "appId", "");
		dashboard.setAppId("B6LV");
		dashboard.setInstance("some instance");
		// mongoTemplate.getCollection("jenkins_unlimited").distinct("buildJob",
		// query.getQueryObject())
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		List<JenkinsUnlimitedData> jenkinsList = new ArrayList<>();
		jenkinsList.add(createJenkinsDate());
		Mockito.when(jenkinsUnlimitedDataRepository.findByBuildJob(Mockito.anyString())).thenReturn(jenkinsList);
		Map<String, String> dbdetails = new HashMap<>();
		dbdetails.put("dbname", "dbname");
		// Mockito.when(throughPutDAO.getDbDetails("B6LV",mongoClient)).thenReturn(dbdetails);
		List<String> list = new ArrayList<String>();
		list.add("module");
		// Mockito.when(throughPutDAO.getRemainingModuleList(Mockito.any())).thenReturn(list);
		throughPutAnalysis.processExecutiveMetricsDetails();
	}

	private JenkinsUnlimitedData createJenkinsDate() {
		JenkinsUnlimitedData data = new JenkinsUnlimitedData();
		data.setAppId("D40V");
		data.setDuration((long) 1132345778);
		data.setBeginTime((long) 1132345778);
		data.setBuildJob("NTS.build");
		data.setCommits((long) 112);
		List<String> jobs = new ArrayList<String>();
		jobs.add("NTS.deploy.uat");
		jobs.add("NTS.deploy.prod");
		data.setDeployJobs(jobs);
		Map<Integer, String> deployOrderedJobs = new HashMap<>();
		deployOrderedJobs.put(0, "NTS.deploy.uat");
		deployOrderedJobs.put(0, "NTS.deploy.prod");
		data.setDeployOrderedJobs(deployOrderedJobs);
		data.setDurationTimeStamp("10D:23H:23M");
		data.setPeriod(90);
		data.setPortfolio("NTS");
		data.setProdJobAvailable(true);
		return data;
	}

	@Test(expected = RuntimeException.class)
	public void testProcessThroughPutMetricsDetails_1() {
		Mockito.doThrow(new RuntimeException("Add operation not implemented"))
				.when(throughPutAnalysis.processExecutiveMetricsDetails());
	}

	@Test
	public void testProcessThroughPutMetricsDetails_2() throws Exception {
		List<String> appIds = new ArrayList<String>();
		String app = "B6LV";
		String app1 = "VQEV";
		appIds.add(app1);
		appIds.add(app);
		ProductPipelineData productPipelineData = new ProductPipelineData();
		List<ThroughPutModel> throughputDataList = new ArrayList<ThroughPutModel>();
		ThroughPutModel throughPutModel = new ThroughPutModel();
		throughPutModel.setDashboardName("dashboardName");
		List<ComputedPipelineMetrics> computedMetrics = new ArrayList<ComputedPipelineMetrics>();
		ComputedPipelineMetrics metrics = new ComputedPipelineMetrics();
		metrics.setApiUrl("https://11.11.11.11");
		metrics.setBeginTimestamp(0);
		metrics.setEndTimestamp(0);
		metrics.setInterval(0);
		computedMetrics.add(metrics);
		throughPutModel.setComputedPipelineMetrics(computedMetrics);
		throughputDataList.add(throughPutModel);
		productPipelineData.setThroughPutModel(throughputDataList);
		productPipelineData.setId(new ObjectId());
		Mockito.when(throughPutDAO.getMongoClient()).thenReturn(mongoClient);
		Mockito.when(throughPutDAO.getEntireAppList(mongoClient)).thenReturn(appIds);
		throughPutAnalysis.processExecutiveMetricsDetails();
	}

	public void testProcessMetricPortfolioDetailResponse() {
		Mockito.when(collectorStatusRepository.findByType(CollectorType.Portfolio_DB))
				.thenReturn(new CollectorStatus());
		Mockito.when(executiveSummaryListRepository.findAll()).thenReturn(getExecutiveSummaryList());
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(getExecutiveComponents());
		throughPutAnalysis.processExecutiveMetricsDetails();
	}

	public void testProcessMetricPortfolioDetailResponse_nocommits() {
		Mockito.when(collectorStatusRepository.findByType(CollectorType.Portfolio_DB))
				.thenReturn(new CollectorStatus());
		Mockito.when(executiveSummaryListRepository.findAll()).thenReturn(getExecutiveSummaryListWithNoReporting());
		throughPutAnalysis.processExecutiveMetricsDetails();
	}

	// getMetricsDetailsForNoCommitss

	@Test
	public void testProcessMetricPortfolioDetailResponse_1() {
		ExecutiveSummaryList executiveSummaryList = new ExecutiveSummaryList();
		List<String> appIds = new ArrayList<>();
		appIds.add("D40V");
		executiveSummaryList.setTotalApps(10);
		executiveSummaryList.setConfiguredApps(6);
		executiveSummaryList.setAppId(appIds);
		executiveSummaryList.setConfiguredAppId(appIds);
		executiveSummaryList.setEid("123456");
		Mockito.when(executiveSummaryListRepository.findAll()).thenReturn(Arrays.asList(executiveSummaryList));

		throughPutAnalysis.processExecutiveMetricsDetails();
	}

	@Test
	public void testProcessMetricPortfolioDetailResponse_2() {
		ExecutiveSummaryList executiveSummaryList = new ExecutiveSummaryList();
		List<String> appIds = new ArrayList<String>();
		appIds.add("B6LV");
		executiveSummaryList.setAppId(appIds);
		executiveSummaryList.setTotalApps(10);
		executiveSummaryList.setConfiguredApps(6);
		executiveSummaryList.setConfiguredAppId(appIds);
		Mockito.when(executiveSummaryListRepository.findAll()).thenReturn(Arrays.asList(executiveSummaryList));
		throughPutAnalysis.processExecutiveMetricsDetails();
	}

	@Test
	public void testProcessMetricPortfolioDetailResponse_3() {
		Mockito.when(executiveSummaryListRepository.findAll()).thenReturn(getExecutiveSummaryList_1());
		throughPutAnalysis.processExecutiveMetricsDetails();
	}

	@Test
	public void testProcessBuildingBlocks() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("B6LV");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getApplicationDetails());
		throughPutAnalysis.processExecutiveMetricsDetails();
	}

	@Test
	public void testProcessBuildingBlocks_1() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("B6LV");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		throughPutAnalysis.processExecutiveMetricsDetails();
	}

	@Test
	public void testProcessMetricsDetailResponse() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("B6LV");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric("B6LV", "pipeline-lead-time"))
				.thenReturn(getExecutiveComponents());
		Mockito.when(metricsDetailRepository.findByMetricLevelIdAndLevelAndType("B6LV", MetricLevel.PRODUCT,
				MetricType.PIPELINE_LEAD_TIME)).thenReturn(getMetricsDetail());
		throughPutAnalysis.processMetricsDetailResponse();
	}

	@Test
	public void testProcessMetricsDetailResponse_withModules() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("B6LV");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(getExecutiveComponents_withNoCounts());
		Mockito.when(metricsDetailRepository.findByMetricLevelIdAndLevelAndType("B6LV", MetricLevel.PRODUCT,
				MetricType.PIPELINE_LEAD_TIME)).thenReturn(getMetricsDetail());
		Mockito.when(genericMethods.processAppCriticality(Mockito.anyString())).thenReturn("Critical");
		throughPutAnalysis.processMetricsDetailResponse();
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
		executiveMetrics.setMetricsName("pipeline-lead-time");
		executiveMetrics.setModules(new ArrayList<ExecutiveModuleMetrics>());
		executiveMetrics.setTrendSlope(0.2);
		executiveComponents.setMetrics(Arrays.asList(executiveMetrics));
		executiveComponents.setTeamBoardLink("some link");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric("B6LV", "pipeline-lead-time"))
				.thenReturn(executiveComponents);
		Mockito.when(genericMethods.processAppCriticality(Mockito.anyString())).thenReturn("Critical");
		throughPutAnalysis.processMetricsDetailResponse();
	}

	@Test
	public void testProcessMetricsDetailResponse_2() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("B6LV");
		ExecutiveComponents executiveComponents = new ExecutiveComponents();
		executiveComponents.setAppId("B6LV");
		executiveComponents.setAppName("ACSS");
		executiveComponents.setTeamBoardLink("some link");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric("B6LV", "pipeline-lead-time"))
				.thenReturn(executiveComponents);
		// Mockito.when(genericMethods.processAppCriticality(Mockito.anyString())).thenReturn("Critical");
		throughPutAnalysis.processMetricsDetailResponse();
	}

	@Test
	public void testProcessMetricsDetailResponse_3() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("B6LV");
		ExecutiveComponents executiveComponents = new ExecutiveComponents();
		executiveComponents.setAppId("B6LV");
		executiveComponents.setAppName("ACSS");
		ExecutiveMetrics executiveMetrics = new ExecutiveMetrics();
		executiveMetrics.setLastScanned(new Date());
		executiveMetrics.setLastUpdated(new Date());
		executiveMetrics.setMetricsName("pipeline-lead-time");
		ExecutiveModuleMetrics module = new ExecutiveModuleMetrics();
		executiveMetrics.setModules(Arrays.asList(module));
		executiveComponents.setMetrics(null);
		executiveComponents.setTeamBoardLink("some link");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		Mockito.when(executiveComponentRepository.findByAppIdAndMetric("B6LV", "pipeline-lead-time"))
				.thenReturn(executiveComponents);
		throughPutAnalysis.processMetricsDetailResponse();
	}

	private MetricsDetail getMetricsDetail() {
		MetricsDetail metricDetailResponse = new MetricsDetail();
		metricDetailResponse.setMetricLevelId("B6LV");

		MetricSummary summary = new MetricSummary();
		summary.setLastScanned(new Date());
		summary.setLastUpdated(new Date());
		summary.setName("pipeline-lead-time");
		summary.setReportingComponents(1);
		summary.setTotalComponents(1);
		summary.setTrendSlope(2.3);
		List<MetricCount> counts = new ArrayList<MetricCount>();

		MetricCount count = new MetricCount();
		Map<String, String> label1 = new HashMap<String, String>();
		label1.put("type", "lead-time");
		count.setLabel(label1);
		count.setValue(23230);
		counts.add(count);

		MetricCount count1 = new MetricCount();
		Map<String, String> label2 = new HashMap<String, String>();
		label2.put("type", "commit");
		count1.setLabel(label2);
		count1.setValue(0);
		counts.add(count1);

		MetricCount count2 = new MetricCount();
		Map<String, String> label3 = new HashMap<String, String>();
		label3.put("prodConf", "true");
		count2.setLabel(label3);
		count2.setValue(0);
		counts.add(count2);

		MetricCount count3 = new MetricCount();
		Map<String, String> label4 = new HashMap<String, String>();
		label4.put("type", "cadence");
		count3.setLabel(label3);
		count3.setValue(6);
		counts.add(count3);
		summary.setCounts(counts);

		metricDetailResponse.setSummary(summary);
		List<MetricTimeSeriesElement> timeSeries = new ArrayList<>();
		MetricTimeSeriesElement time = new MetricTimeSeriesElement();

		time.setCounts(counts);
		time.setDaysAgo(0);
		timeSeries.add(time);
		MetricTimeSeriesElement time30 = new MetricTimeSeriesElement();

		time30.setCounts(counts);
		time30.setDaysAgo(30);
		timeSeries.add(time30);
		MetricTimeSeriesElement time60 = new MetricTimeSeriesElement();

		time60.setCounts(counts);
		time60.setDaysAgo(60);
		timeSeries.add(time60);
		metricDetailResponse.setTimeSeries(timeSeries);
		return metricDetailResponse;
	}

	private MetricsDetail getMetricsDetailsForNoCommits() {
		MetricsDetail metricDetailResponse = new MetricsDetail();
		metricDetailResponse.setMetricLevelId("B6LV");

		MetricSummary summary = new MetricSummary();
		summary.setLastScanned(new Date());
		summary.setLastUpdated(new Date());
		summary.setName("pipeline-lead-time");
		summary.setReportingComponents(1);
		summary.setTotalComponents(1);
		summary.setTrendSlope(2.3);
		List<MetricCount> counts = new ArrayList<MetricCount>();

		MetricCount count = new MetricCount();
		Map<String, String> label1 = new HashMap<String, String>();
		label1.put("type", "lead-time");
		count.setLabel(label1);
		count.setValue(0);
		counts.add(count);

		MetricCount count1 = new MetricCount();
		Map<String, String> label2 = new HashMap<String, String>();
		label2.put("type", "commit");
		count1.setLabel(label2);
		count1.setValue(0);
		counts.add(count1);

		MetricCount count2 = new MetricCount();
		Map<String, String> label3 = new HashMap<String, String>();
		label3.put("prodConf", "false");
		count2.setLabel(label3);
		count2.setValue(0);
		counts.add(count2);

		MetricCount count3 = new MetricCount();
		Map<String, String> label4 = new HashMap<String, String>();
		label4.put("type", "cadence");
		count3.setLabel(label3);
		count3.setValue(5);
		counts.add(count3);
		summary.setCounts(counts);

		metricDetailResponse.setSummary(summary);
		List<MetricTimeSeriesElement> timeSeries = new ArrayList<>();
		MetricTimeSeriesElement time = new MetricTimeSeriesElement();

		time.setCounts(counts);
		time.setDaysAgo(0);
		timeSeries.add(time);
		MetricTimeSeriesElement time30 = new MetricTimeSeriesElement();

		time30.setCounts(counts);
		time30.setDaysAgo(30);
		timeSeries.add(time30);
		MetricTimeSeriesElement time60 = new MetricTimeSeriesElement();

		time60.setCounts(counts);
		time60.setDaysAgo(60);
		timeSeries.add(time60);
		metricDetailResponse.setTimeSeries(timeSeries);
		return metricDetailResponse;
	}

	private List<ExecutiveSummaryList> getExecutiveSummaryList() {

		ExecutiveSummaryList executiveSummaryList = new ExecutiveSummaryList();
		executiveSummaryList.setAppId(Arrays.asList("B6LV"));
		executiveSummaryList.setBusinessUnits(Arrays.asList("NTS"));
		executiveSummaryList.setConfiguredAppId(Arrays.asList("B6LV"));
		executiveSummaryList.setConfiguredApps(1);
		executiveSummaryList.setEid("123456789");
		executiveSummaryList.setFirstName("shankar");
		executiveSummaryList.setLastName("Arumugavelu");
		executiveSummaryList.setReportingPercentage((double) 100);
		executiveSummaryList.setRole("manager");
		executiveSummaryList.setTotalApps(1);
		return Arrays.asList(executiveSummaryList);
	}

	private List<ExecutiveSummaryList> getExecutiveSummaryListWithNoReporting() {

		ExecutiveSummaryList executiveSummaryList = new ExecutiveSummaryList();
		executiveSummaryList.setAppId(Arrays.asList("B6LV"));
		executiveSummaryList.setBusinessUnits(Arrays.asList("NTS"));
		executiveSummaryList.setConfiguredAppId(null);
		executiveSummaryList.setConfiguredApps(1);
		executiveSummaryList.setEid("123456789");
		executiveSummaryList.setFirstName("shankar");
		executiveSummaryList.setLastName("Arumugavelu");
		executiveSummaryList.setReportingPercentage((double) 100);
		executiveSummaryList.setRole("manager");
		executiveSummaryList.setTotalApps(1);
		return Arrays.asList(executiveSummaryList);
	}

	private List<ExecutiveSummaryList> getExecutiveSummaryList_1() {

		ExecutiveSummaryList executiveSummaryList = new ExecutiveSummaryList();
		executiveSummaryList.setAppId(Arrays.asList("B6LV"));
		executiveSummaryList.setBusinessUnits(Arrays.asList("NTS"));
		executiveSummaryList.setConfiguredAppId(new ArrayList<String>());
		executiveSummaryList.setConfiguredApps(1);
		executiveSummaryList.setEid("123456789");
		executiveSummaryList.setFirstName("shankar");
		executiveSummaryList.setLastName("Arumugavelu");
		executiveSummaryList.setReportingPercentage((double) 100);
		executiveSummaryList.setRole("manager");
		executiveSummaryList.setTotalApps(1);
		return Arrays.asList(executiveSummaryList);
	}

	private ExecutiveComponents getExecutiveComponents() {
		ExecutiveComponents executiveComponents = new ExecutiveComponents();

		executiveComponents.setAppId("B6LV");
		executiveComponents.setAppName("SomaAppName");
		executiveComponents.setMetrics(Arrays.asList(getExecutiveMetricsList()));
		executiveComponents.setTeamBoardLink("some link");

		return executiveComponents;
	}

	private ExecutiveComponents getExecutiveComponents_withNoCounts() {
		ExecutiveComponents executiveComponents = new ExecutiveComponents();

		executiveComponents.setAppId("B6LV");
		executiveComponents.setAppName("SomaAppName");
		executiveComponents.setMetrics(Arrays.asList(getExecutiveMetricsList__withNoCounts()));
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
		executiveMetrics.setMetricsName("pipeline-lead-time");
		ExecutiveModuleMetrics module = new ExecutiveModuleMetrics();
		module.setLastScanned(new Date());
		module.setLastUpdated(new Date());
		module.setModuleName("module");
		module.setTrendSlope((double) 0);
		ExecutiveMetricsSeries executiveMetricsSeries = new ExecutiveMetricsSeries();
		executiveMetricsSeries.setDaysAgo(0);
		executiveMetricsSeries.setTimeStamp(123456789l);

		List<SeriesCount> counts = new ArrayList<SeriesCount>();
		SeriesCount series1 = new SeriesCount();
		Map<String, String> label1 = new HashMap<String, String>();
		label1.put("type", "lead-time");
		series1.setLabel(label1);
		series1.setCount((long) 11);
		counts.add(series1);

		SeriesCount series2 = new SeriesCount();
		Map<String, String> label2 = new HashMap<String, String>();
		label2.put("type", "commit");
		series2.setLabel(label2);
		series2.setCount((long) 12);
		counts.add(series2);

		SeriesCount series3 = new SeriesCount();
		Map<String, String> label3 = new HashMap<String, String>();
		label3.put("prodConf", "true");
		series3.setLabel(label3);
		series3.setCount((long) 0);
		counts.add(series3);

		executiveMetricsSeries.setCounts(counts);
		ExecutiveMetricsSeries executiveMetricsSeries90 = new ExecutiveMetricsSeries();
		executiveMetricsSeries90.setDaysAgo(90);
		executiveMetricsSeries90.setTimeStamp(123456789l);
		executiveMetricsSeries90.setCounts(counts);
		List<ExecutiveMetricsSeries> executiveMetricsSeriesList = new ArrayList<ExecutiveMetricsSeries>();
		executiveMetricsSeriesList.add(executiveMetricsSeries);
		executiveMetricsSeriesList.add(executiveMetricsSeries90);
		module.setSeries(null);

		List<ExecutiveModuleMetrics> moduleList = new ArrayList<ExecutiveModuleMetrics>();
		moduleList.add(module);

		executiveMetrics.setModules(moduleList);
		executiveMetrics.setTrendSlope((double) 0);

		return executiveMetrics;
	}

	// getExecutiveMetricsList__withNoCounts

	private ExecutiveMetrics getExecutiveMetricsList__withNoCounts() {

		ExecutiveMetrics executiveMetrics = new ExecutiveMetrics();
		executiveMetrics.setLastScanned(new Date());
		executiveMetrics.setLastUpdated(new Date());
		executiveMetrics.setMetricsName("pipeline-lead-time");
		ExecutiveModuleMetrics module = new ExecutiveModuleMetrics();
		module.setLastScanned(new Date());
		module.setLastUpdated(new Date());
		module.setModuleName("module");
		module.setTrendSlope((double) 0);
		ExecutiveMetricsSeries executiveMetricsSeries = new ExecutiveMetricsSeries();
		executiveMetricsSeries.setDaysAgo(0);
		executiveMetricsSeries.setTimeStamp(123456789l);

		List<SeriesCount> counts = new ArrayList<SeriesCount>();
		SeriesCount series1 = new SeriesCount();
		Map<String, String> label1 = new HashMap<String, String>();
		label1.put("type", "lead-time");
		series1.setLabel(label1);
		series1.setCount((long) 0);
		counts.add(series1);

		SeriesCount series2 = new SeriesCount();
		Map<String, String> label2 = new HashMap<String, String>();
		label2.put("type", "commit");
		series2.setLabel(label2);
		series2.setCount((long) 0);
		counts.add(series2);

		SeriesCount series3 = new SeriesCount();
		Map<String, String> label3 = new HashMap<String, String>();
		label3.put("prodConf", "false");
		series3.setLabel(label3);
		series3.setCount((long) 0);
		counts.add(series3);

		executiveMetricsSeries.setCounts(counts);
		ExecutiveMetricsSeries executiveMetricsSeries90 = new ExecutiveMetricsSeries();
		executiveMetricsSeries90.setDaysAgo(90);
		executiveMetricsSeries90.setTimeStamp(123456789l);
		executiveMetricsSeries90.setCounts(counts);
		List<ExecutiveMetricsSeries> executiveMetricsSeriesList = new ArrayList<ExecutiveMetricsSeries>();
		executiveMetricsSeriesList.add(executiveMetricsSeries);
		executiveMetricsSeriesList.add(executiveMetricsSeries90);
		module.setSeries(executiveMetricsSeriesList);

		List<ExecutiveModuleMetrics> moduleList = new ArrayList<ExecutiveModuleMetrics>();
		moduleList.add(module);

		executiveMetrics.setModules(moduleList);
		executiveMetrics.setTrendSlope((double) 0);

		return executiveMetrics;
	}

	private ExecutiveMetrics getExecutiveMetricsList() {

		ExecutiveMetrics executiveMetrics = new ExecutiveMetrics();
		executiveMetrics.setLastScanned(new Date());
		executiveMetrics.setLastUpdated(new Date());
		executiveMetrics.setMetricsName("pipeline-lead-time");
		ExecutiveModuleMetrics module = new ExecutiveModuleMetrics();
		module.setLastScanned(new Date());
		module.setLastUpdated(new Date());
		module.setModuleName("module");
		module.setTrendSlope((double) 0);
		ExecutiveMetricsSeries executiveMetricsSeries = new ExecutiveMetricsSeries();
		executiveMetricsSeries.setDaysAgo(0);
		executiveMetricsSeries.setTimeStamp(123456789l);

		List<SeriesCount> counts = new ArrayList<SeriesCount>();
		SeriesCount series1 = new SeriesCount();
		Map<String, String> label1 = new HashMap<String, String>();
		label1.put("type", "lead-time");
		series1.setLabel(label1);
		series1.setCount((long) 11);
		counts.add(series1);

		SeriesCount series2 = new SeriesCount();
		Map<String, String> label2 = new HashMap<String, String>();
		label2.put("type", "commit");
		series2.setLabel(label2);
		series2.setCount((long) 12);
		counts.add(series2);

		SeriesCount series3 = new SeriesCount();
		Map<String, String> label3 = new HashMap<String, String>();
		label3.put("prodConf", "true");
		series3.setLabel(label3);
		series3.setCount((long) 0);
		counts.add(series3);

		executiveMetricsSeries.setCounts(counts);
		ExecutiveMetricsSeries executiveMetricsSeries90 = new ExecutiveMetricsSeries();
		executiveMetricsSeries90.setDaysAgo(90);
		executiveMetricsSeries90.setTimeStamp(123456789l);
		executiveMetricsSeries90.setCounts(counts);
		List<ExecutiveMetricsSeries> executiveMetricsSeriesList = new ArrayList<ExecutiveMetricsSeries>();
		executiveMetricsSeriesList.add(executiveMetricsSeries);
		executiveMetricsSeriesList.add(executiveMetricsSeries90);
		module.setSeries(executiveMetricsSeriesList);

		List<ExecutiveModuleMetrics> moduleList = new ArrayList<ExecutiveModuleMetrics>();
		moduleList.add(module);

		executiveMetrics.setModules(moduleList);
		executiveMetrics.setTrendSlope((double) 0);

		return executiveMetrics;
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
		summary.setName("quality");
		summary.setReportingComponents(1);
		summary.setTotalComponents(1);
		summary.setTrendSlope(2.3);

		buildingBlockMetricsDetail.setMetrics(Arrays.asList(summary));
		return buildingBlockMetricsDetail;
	}

	private ProductPipelineData getPipelineData() {
		ProductPipelineData productPipelineData = new ProductPipelineData();
		List<ThroughPutModel> throughputDataList = new ArrayList<ThroughPutModel>();
		ThroughPutModel throughPutModel = new ThroughPutModel();
		throughPutModel.setDashboardName("dashboardName");
		List<ComputedPipelineMetrics> computedMetrics = new ArrayList<ComputedPipelineMetrics>();

		ComputedPipelineMetrics metrics = new ComputedPipelineMetrics();
		metrics.setApiUrl("https://11.11.11.11");
		metrics.setBeginTimestamp(0);
		metrics.setEndTimestamp(0);
		metrics.setInterval(0);
		Map<String, Object> stageDetails = new HashMap<String, Object>();
		stageDetails.put("prodStage", "prod");
		Map<String, Object> productPipelineComputedMetrics = new HashMap<String, Object>();
		productPipelineComputedMetrics.put("duration", "10D:1H:42M");
		productPipelineComputedMetrics.put("durationTimestamp", null);
		productPipelineComputedMetrics.put("commits", "16");
		stageDetails.put("prod", productPipelineComputedMetrics);
		metrics.setStageDetails(stageDetails);
		computedMetrics.add(metrics);

		ComputedPipelineMetrics metrics30 = new ComputedPipelineMetrics();
		metrics30.setApiUrl("https://11.11.11.11");
		metrics30.setBeginTimestamp(0);
		metrics30.setEndTimestamp(0);
		metrics30.setInterval(30);
		Map<String, Object> stageDetails30 = new HashMap<String, Object>();
		stageDetails30.put("prodStage", "prod");
		Map<String, Object> productPipelineComputedMetrics30 = new HashMap<String, Object>();
		productPipelineComputedMetrics30.put("duration", "10D:1H:42M");
		productPipelineComputedMetrics30.put("durationTimestamp", "870171483");
		productPipelineComputedMetrics30.put("commits", "16");
		stageDetails30.put("prod", productPipelineComputedMetrics30);
		metrics30.setStageDetails(stageDetails30);
		computedMetrics.add(metrics30);

		throughPutModel.setComputedPipelineMetrics(computedMetrics);
		throughputDataList.add(throughPutModel);

		ThroughPutModel throughPutModel2 = new ThroughPutModel();
		throughPutModel2.setDashboardName("dashboardName 2");
		List<ComputedPipelineMetrics> computedMetrics2 = new ArrayList<ComputedPipelineMetrics>();
		ComputedPipelineMetrics metrics_1 = new ComputedPipelineMetrics();
		metrics_1.setApiUrl("https://11.11.11.11");
		metrics_1.setBeginTimestamp(0);
		metrics_1.setEndTimestamp(0);
		metrics_1.setInterval(0);
		Map<String, Object> stageDetails_1 = new HashMap<String, Object>();
		stageDetails_1.put("prodStage", "prod");
		Map<String, Object> productPipelineComputedMetrics_1 = new HashMap<String, Object>();
		productPipelineComputedMetrics_1.put("duration", "10D:1H:42M");
		productPipelineComputedMetrics_1.put("durationTimestamp", "170171483");
		productPipelineComputedMetrics_1.put("commits", "16");
		stageDetails_1.put("prod", productPipelineComputedMetrics_1);
		metrics_1.setStageDetails(stageDetails_1);
		computedMetrics2.add(metrics_1);

		ComputedPipelineMetrics metrics_2 = new ComputedPipelineMetrics();
		metrics_2.setApiUrl("https://11.11.11.11");
		metrics_2.setBeginTimestamp(270171483);
		metrics_2.setEndTimestamp(0);
		metrics_2.setInterval(30);
		Map<String, Object> stageDetails_2 = new HashMap<String, Object>();
		stageDetails_2.put("prodStage", "prod");
		Map<String, Object> productPipelineComputedMetrics_2 = new HashMap<String, Object>();
		productPipelineComputedMetrics_2.put("duration", "10D:1H:42M");
		productPipelineComputedMetrics_2.put("durationTimestamp", "270171483");
		productPipelineComputedMetrics_2.put("commits", "16");
		stageDetails_2.put("prod", productPipelineComputedMetrics_2);
		metrics_2.setStageDetails(stageDetails_2);
		computedMetrics2.add(metrics_2);

		ComputedPipelineMetrics metrics_3 = new ComputedPipelineMetrics();
		metrics_3.setApiUrl("https://11.11.11.11");
		metrics_3.setBeginTimestamp(370171483);
		metrics_3.setEndTimestamp(0);
		metrics_3.setInterval(60);
		Map<String, Object> stageDetails_3 = new HashMap<String, Object>();
		stageDetails_3.put("prodStage", "prod");
		Map<String, Object> productPipelineComputedMetrics_3 = new HashMap<String, Object>();
		productPipelineComputedMetrics_3.put("duration", "10D:1H:42M");
		productPipelineComputedMetrics_3.put("durationTimestamp", "370171483");
		productPipelineComputedMetrics_3.put("commits", "16");
		stageDetails_3.put("prod", productPipelineComputedMetrics_3);
		metrics_3.setStageDetails(stageDetails_3);
		computedMetrics2.add(metrics_3);

		throughPutModel2.setComputedPipelineMetrics(computedMetrics2);
		throughputDataList.add(throughPutModel2);
		productPipelineData.setThroughPutModel(throughputDataList);
		productPipelineData.setId(new ObjectId());
		productPipelineData.setAppId("B6LV");
		return productPipelineData;
	}

	@Test
	public void removeUnusedThroughputDetails() {

		try {
			List<String> appIds = new ArrayList<>();
			appIds.add("TestMe");

			Mockito.when(vastDetailsDAO.getAllAppIds(mongoClient)).thenReturn(appIds);

			List<ExecutiveComponents> throughputDataList = new ArrayList<>();

			ExecutiveComponents executiveComponents = new ExecutiveComponents();
			throughputDataList.add(executiveComponents);

			Mockito.when(executiveComponentRepository.getNotUsedAppIdList(appIds, "pipeline-lead-time"))
					.thenReturn(throughputDataList);
			when(throughPutDAO.getMongoClient()).thenReturn(mongoClient);
			throughPutAnalysis.removeUnusedThroughPutDetails();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void removeUnusedThroughputDetails_1() throws Exception {
		Mockito.when(throughPutDAO.getMongoClient()).thenThrow(new NullPointerException());
		throughPutAnalysis.removeUnusedThroughPutDetails();
	}
}
