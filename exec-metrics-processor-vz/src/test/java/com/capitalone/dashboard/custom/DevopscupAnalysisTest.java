package com.capitalone.dashboard.custom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.capitalone.dashboard.dao.DevopscupDAO;
import com.capitalone.dashboard.exec.model.MetricCount;
import com.capitalone.dashboard.exec.model.MetricLevel;
import com.capitalone.dashboard.exec.model.MetricSummary;
import com.capitalone.dashboard.exec.model.MetricTimeSeriesElement;
import com.capitalone.dashboard.exec.model.MetricType;
import com.capitalone.dashboard.exec.model.ApplicationDetails;
import com.capitalone.dashboard.exec.model.AwsArchitectureScoreRequest;
import com.capitalone.dashboard.exec.model.AwsNonProdScoreRequest;
import com.capitalone.dashboard.exec.model.AwsProdScoreRequest;
import com.capitalone.dashboard.exec.model.BuildingBlocks;
import com.capitalone.dashboard.exec.model.CloudExcellence;
import com.capitalone.dashboard.exec.model.CollectorType;
import com.capitalone.dashboard.exec.model.DevOpsCupScores;
import com.capitalone.dashboard.exec.model.EngineeringExcellence;
import com.capitalone.dashboard.exec.model.ExecutiveSummaryList;
import com.capitalone.dashboard.exec.model.InstanceCollectorStatus;
import com.capitalone.dashboard.exec.model.MetricsDetail;
import com.capitalone.dashboard.exec.repository.ApplicationDetailsRepository;
import com.capitalone.dashboard.exec.repository.BuildingBlocksRepository;
import com.capitalone.dashboard.exec.repository.DevOpsCupScoresRepository;
import com.capitalone.dashboard.exec.repository.DevopscupRoundDetailsRepository;
import com.capitalone.dashboard.exec.repository.ExecutiveSummaryListRepository;
import com.capitalone.dashboard.exec.repository.MetricsDetailRepository;
import com.capitalone.dashboard.exec.repository.PortfolioResponseRepository;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

@RunWith(MockitoJUnitRunner.class)
public class DevopscupAnalysisTest {

	@InjectMocks
	private DevopscupAnalysis devopscupAnalysis;

	@Mock
	private DevopscupDAO devopscupDAO;

	@Mock
	private MetricsDetailRepository metricDetailResponseRepository;
	@Mock
	private DevOpsCupScoresRepository devOpsCupScoresRepository;
	@Mock
	private ExecutiveSummaryListRepository executiveSummaryListRepository;
	@Mock
	private PortfolioResponseRepository portfolioResponseRepository;

	@Mock
	private MongoClient mongoClient;

	@Mock
	private MongoTemplate mongoTemplate;

	@Mock
	DBCollection mongoCollection;
	@Mock
	ApplicationDetailsRepository applicationDetailsRepository;
	@Mock
	private BuildingBlocksRepository buildingBlocksRepository;
	@Mock
	private DevOpsCupScoresRepository devopscupScoreRepository;
	@Mock
	private DevopscupRoundDetailsRepository devopscupRoundDetailsRepository;

	private static final String DEVOPSCUP = "devopscup";
	private static final String ENGGEXCELPOINTS = "enggExcelPoints";
	private static final String ENGGIMPROVEMENTS = "enggImprovements";
	private static final String CLOUDEXCELPOINTS = "cloudExcelPoints";
	private static final String CLOUDIMPROVEMENTS = "cloudImprovements";
	private static final String TOTALPOINTS = "totalPoints";
	private static final String TOTALPERCENT = "totalPercent";
	private static final String TYPE = "type";

	@Test
	public void processMetricsDetailResponse_test() {
		devopscupAnalysis.processMetricsDetailResponse();
	}

	@Test
	public void processMetricsDetailResponse_test1() {
		List<DevOpsCupScores> devopscupScoreList = new ArrayList<>();
		devopscupScoreList.add(getDevopscupScore());
		// Mockito.when(devopscupScoreRepository.findAll()).thenReturn(devopscupScoreList);

		devopscupAnalysis.processMetricsDetailResponse();

	}

	private MetricsDetail getMetricsDetail() {

		MetricsDetail metricDetailResponse = new MetricsDetail();
		metricDetailResponse.setMetricLevelId("B6LV");
		metricDetailResponse.setType(MetricType.DEVOPSCUP);

		MetricSummary summary = new MetricSummary();
		summary.setLastScanned(new Date());
		summary.setLastUpdated(new Date());
		summary.setName(DEVOPSCUP);
		summary.setReportingComponents(1);
		summary.setTotalComponents(1);
		summary.setTrendSlope(0.0);

		summary.setCounts(settingCounts());

		metricDetailResponse.setSummary(summary);
		List<MetricTimeSeriesElement> timeSeries = new ArrayList<>();
		MetricTimeSeriesElement time = new MetricTimeSeriesElement();
		time.setCounts(settingCounts());
		time.setDaysAgo(30);
		timeSeries.add(time);

		metricDetailResponse.setTimeSeries(null);
		return metricDetailResponse;

	}

	private DevOpsCupScores getDevopscupScore() {
		DevOpsCupScores devcupScores = new DevOpsCupScores();
		devcupScores.setActive(true);
		devcupScores.setAppId("D40V");
		devcupScores.setCloudExcelActive(true);
		devcupScores.setAppName("Hygieia");
		devcupScores.setPortfolio("GTS");
		devcupScores.setTimeStamp(1534333599603L);
		devcupScores.setEnggExcelPoints(1559L);
		devcupScores.setCloudExcelPoints(785L);
		devcupScores.setTotalPoints(2344L);

		EngineeringExcellence engExcel = new EngineeringExcellence();
		engExcel.setFortifyPoints(270.0);
		engExcel.setFortifyMaturity(60.0);
		engExcel.setFortifyImprovements(0.0);
		engExcel.setFortifyValue(105L);
		engExcel.setFortifyCriticalVul(19);
		engExcel.setFortifyHighVul(7);
		engExcel.setFortifyMediumVul(79);
		engExcel.setFortifyValueBL(99L);
		engExcel.setCycleTimePoints(0.0);
		engExcel.setCycleTimeMaturity(0.0);
		engExcel.setCycleTimeImprovements(0.0);
		engExcel.setCycleTimeValue(0.0);
		engExcel.setCycleTimeValueBL(576.0);
		engExcel.setDeploymentCadencyPoints(439.0);
		engExcel.setDeploymentCadencyMaturity(73.0);
		engExcel.setDeploymentCadencyImprovements(-5.0);
		engExcel.setDeploymentCadencyValue(8.0);
		engExcel.setDeploymentCadencyValueBL(6.0);
		engExcel.setMttrPoints(200.0);
		engExcel.setMttrMaturity(100.0);
		engExcel.setMttrValue(0.0);
		engExcel.setMttrImprovements(0.0);
		engExcel.setMtbfValue(365.0);
		engExcel.setMtbfValueBL(365.0);
		engExcel.setMtbfPoints(200.0);
		engExcel.setMtbfMaturity(100.0);
		engExcel.setMtbfImprovements(0.0);
		engExcel.setMtbfValue(365.0);
		engExcel.setMtbfValueBL(365.0);
		engExcel.setCfRatePoints(450.0);
		engExcel.setCfRateImprovements(0.0);
		engExcel.setCfRateValue(0.0);
		engExcel.setCfRateValueBL(0.0);
		engExcel.setTechByteValue(0.0);
		engExcel.setTechByteMaturity(0.0);
		engExcel.setTechBytePoints(0.0);
		engExcel.setTotalPoints(1559L);
		engExcel.setTotalImprovements(-0.833333333333333);

		devcupScores.setEnggExcel(engExcel);

		CloudExcellence cloudExcel = new CloudExcellence();
		cloudExcel.setNonProdMigrationPoints(340);
		cloudExcel.setStageMigrationPoints(0);
		cloudExcel.setProdMigrationPoints(205);
		cloudExcel.setArchitecturePoints(190);
		cloudExcel.setPresentationPoints(50);
		cloudExcel.setTotalPoints(785);
		cloudExcel.setTotalImprovements(34.78);
		AwsArchitectureScoreRequest aws = new AwsArchitectureScoreRequest();
		aws.setSpotInstance(0);
		aws.setWaf(20);
		aws.setOpenSourceSw(50);
		aws.setRetdSw(0);
		aws.setTemplateReused(100);
		aws.setSupportScore(20);
		aws.setTotalScore(190);
		cloudExcel.setAwsArchitectureScoreRequest(aws);

		AwsNonProdScoreRequest awsNonProd = new AwsNonProdScoreRequest();
		awsNonProd.setAutomatedDevScore(0);
		awsNonProd.setAutomatedUATScore(0);
		awsNonProd.setNonProdMigrationScore(0);
		awsNonProd.setCostOptimizationScore(0);
		awsNonProd.setOnPremiseNonProdDecomScore(340);
		awsNonProd.setTotalScore(340);
		cloudExcel.setAwsNonProdScoreRequest(awsNonProd);

		AwsProdScoreRequest awsProdScore = new AwsProdScoreRequest();
		awsProdScore.setProdMigrationICPScore(25);
		awsProdScore.setProdMigrationICPScore(0);
		awsProdScore.setCostOptimizationScore(0);
		awsProdScore.setOnPremiseDecom201718Score(180);
		awsProdScore.setDrMigration201718Score(0);
		awsProdScore.setTotalScore(205);
		cloudExcel.setAwsProdScoreRequest(awsProdScore);
		devcupScores.setCloudExcel(cloudExcel);

		devcupScores.setEnggExcelActive(true);
		devcupScores.setCloudExcelActive(true);

		return devcupScores;
	}

	@Test
	public void processExecutiveDetailsMetrics_test() {
		List<String> appIds = new ArrayList<>();
		appIds.add("D40V");

		Mockito.when(executiveSummaryListRepository.findAll()).thenReturn(getExecutiveSummaryList());
		// Mockito.when(metricPortfolioDetailRepository.findByEidAndMetricsName(Mockito.anyString(),Mockito.anyString())).thenReturn(getMetricPortfolioDetailResponse());
		// Mockito.when(mongoTemplate.getCollection(Mockito.anyString()).distinct(Mockito.anyString())).thenReturn(appIds);
		devopscupAnalysis.processExecutiveDetailsMetrics();
	}

	@Test
	public void processBuildingBlockMetrics_test() {
		devopscupAnalysis.processBuildingBlockMetrics();
	}

	@Test
	public void processBuildingBlockMetrics_test2() {
		List<String> appIds = new ArrayList<>();
		appIds.add("D40V");

		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);
		Mockito.when(buildingBlocksRepository.findByMetricLevelIdAndMetricLevel("D40V", MetricLevel.PRODUCT))
				.thenReturn(buildingBlockMetricsDetail());
		Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getAppDetails());
		Mockito.when(metricDetailResponseRepository.findByMetricLevelIdAndLevelAndType("D40V", MetricLevel.PRODUCT,
				MetricType.DEVOPSCUP)).thenReturn(getMetricsDetail());
		devopscupAnalysis.processBuildingBlockMetrics();
	}

	@Test
	public void processComponentDetailsMetrics_test1() {
		devopscupAnalysis.processComponentDetailsMetrics();
	}

	@Test
	public void processComponentDetailsMetrics_test2() {
		List<String> appIds = new ArrayList<String>();
		appIds.add("B6LV");
		Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.distinct(Mockito.anyString(), Mockito.any(DBObject.class))).thenReturn(appIds);

		Mockito.when(applicationDetailsRepository.findByAppId(Mockito.anyString())).thenReturn(getAppDetails());
		Mockito.when(buildingBlocksRepository.findByMetricLevelIdAndMetricLevelAndMetricType("B6LV",
				MetricLevel.COMPONENT, MetricType.DEVOPSCUP)).thenReturn(getBuildingBlocksList());

		devopscupAnalysis.processComponentDetailsMetrics();
	}

	@Test
	public void copyDevopscupScores2() {
		devopscupAnalysis.processExecutiveMetricsDetails();
		devopscupAnalysis.setDevopscupRoundDetails();
	}

	@Test
	public void copyDevopscupScores() {
		// Mockito.when(devopscupDAO.getMongoClient()).thenReturn(mongoClient);
		devopscupAnalysis.processExecutiveMetricsDetails();
	}

	private List<BuildingBlocks> getBuildingBlocksList() {
		List<BuildingBlocks> buildingBlockComponentSummaryResponseList = new ArrayList<BuildingBlocks>();
		BuildingBlocks buildingBlockComponentSummaryResponse = new BuildingBlocks();
		buildingBlockComponentSummaryResponse.setMetricLevelId("B6LV");
		buildingBlockComponentSummaryResponse.setLob("NTS");
		buildingBlockComponentSummaryResponse.setName(DEVOPSCUP);
		buildingBlockComponentSummaryResponse.setPoc("some poc");
		buildingBlockComponentSummaryResponse.setTotalComponents(1);
		buildingBlockComponentSummaryResponse.setUrl("some url");
		List<MetricSummary> metrics = new ArrayList<>(Arrays.asList(getMetricSummary()));
		buildingBlockComponentSummaryResponse.setMetrics(metrics);
		return buildingBlockComponentSummaryResponseList;
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
		item.setCollectorName(DEVOPSCUP);
		item.setCollectorType(CollectorType.DevOpsCup);
		item.setEnabled(true);
		item.setLastExecuted(2l);
		item.setOnline(true);
		ad.setCollectorStatus(Arrays.asList(item));
		return ad;
	}

	private BuildingBlocks buildingBlockMetricsDetail() {
		BuildingBlocks buildingBlockMetricsDetail = new BuildingBlocks();
		buildingBlockMetricsDetail.setMetricLevelId("B6LV");
		MetricSummary summary = new MetricSummary();
		summary.setLastScanned(new Date());
		summary.setLastUpdated(new Date());
		summary.setName("devopscup");
		summary.setReportingComponents(1);
		summary.setTotalComponents(1);
		summary.setTrendSlope(2.3);
		summary.setCounts(settingCounts());
		buildingBlockMetricsDetail.setMetrics(Arrays.asList(summary));
		return buildingBlockMetricsDetail;
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

	private MetricSummary getMetricSummary() {

		MetricSummary summary = new MetricSummary();
		summary.setLastScanned(new Date());
		summary.setLastUpdated(new Date());
		summary.setName(DEVOPSCUP);
		summary.setReportingComponents(1);
		summary.setTotalComponents(1);
		summary.setTrendSlope(0.0);
		summary.setCounts(settingCounts());

		return summary;
	}

	private List<MetricCount> settingCounts() {

		List<MetricCount> metricCountResponseList = new ArrayList<>();

		MetricCount metricCount = new MetricCount();
		HashMap<String, String> hsh = new HashMap<>();

		hsh.put(TYPE, ENGGEXCELPOINTS);
		metricCount.setLabel(hsh);
		metricCount.setValue(0.0);
		metricCountResponseList.add(metricCount);

		metricCount = new MetricCount();
		hsh = new HashMap<>();
		hsh.put(TYPE, ENGGIMPROVEMENTS);
		metricCount.setLabel(hsh);
		metricCount.setValue(0.0);
		metricCountResponseList.add(metricCount);

		metricCount = new MetricCount();
		hsh = new HashMap<>();
		hsh.put(TYPE, CLOUDEXCELPOINTS);
		metricCount.setLabel(hsh);
		metricCount.setValue(0.0);
		metricCountResponseList.add(metricCount);

		metricCount = new MetricCount();
		hsh = new HashMap<>();
		hsh.put(TYPE, CLOUDIMPROVEMENTS);
		metricCount.setLabel(hsh);
		metricCount.setValue(0.0);
		metricCountResponseList.add(metricCount);

		metricCount = new MetricCount();
		hsh = new HashMap<>();
		hsh.put(TYPE, TOTALPOINTS);
		metricCount.setLabel(hsh);
		metricCount.setValue(0.0);
		metricCountResponseList.add(metricCount);

		metricCount = new MetricCount();
		hsh = new HashMap<>();
		hsh.put(TYPE, TOTALPERCENT);
		metricCount.setLabel(hsh);
		metricCount.setValue(0.0);

		metricCountResponseList.add(metricCount);

		return metricCountResponseList;

	}

}
