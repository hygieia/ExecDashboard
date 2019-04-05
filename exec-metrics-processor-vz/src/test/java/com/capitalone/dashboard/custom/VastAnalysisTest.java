package com.capitalone.dashboard.custom;

import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.capitalone.dashboard.dao.CollectorDetailsDAO;
import com.capitalone.dashboard.dao.ConfigurationMetricsDAO;
import com.capitalone.dashboard.dao.DashboardDetailsDAO;
import com.capitalone.dashboard.dao.QualityDetailsDAO;
import com.capitalone.dashboard.dao.VastDetailsDAO;
import com.capitalone.dashboard.exec.model.vz.ApplicationDetails;
import com.capitalone.dashboard.exec.model.vz.CollectorType;
import com.capitalone.dashboard.exec.model.vz.CollectorUpdatedDetails;
import com.capitalone.dashboard.exec.model.vz.ConfigurationMetrics;
import com.capitalone.dashboard.exec.model.vz.ExecutiveHierarchy;
import com.capitalone.dashboard.exec.model.vz.ExecutiveSummaryList;
import com.capitalone.dashboard.exec.model.vz.PortfolioResponse;
import com.capitalone.dashboard.exec.model.vz.Vast;
import com.capitalone.dashboard.exec.repository.vz.ApplicationDetailsRepository;
import com.capitalone.dashboard.exec.repository.vz.BuildingBlocksRepository;
import com.capitalone.dashboard.exec.repository.vz.CollectorStatusRepository;
import com.capitalone.dashboard.exec.repository.vz.CollectorUpdatedDetailsRepository;
import com.capitalone.dashboard.exec.repository.vz.ExecutiveComponentRepository;
import com.capitalone.dashboard.exec.repository.vz.ExecutiveHierarchyRepository;
import com.capitalone.dashboard.exec.repository.vz.ExecutiveSummaryListRepository;
import com.capitalone.dashboard.exec.repository.vz.MetricsDetailRepository;
import com.capitalone.dashboard.exec.repository.vz.PortfolioResponseRepository;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

@RunWith(MockitoJUnitRunner.class)
public class VastAnalysisTest {

	@InjectMocks
	private VastAnalysis vastAnalysisTest;
	@Mock
	private VastDetailsDAO vastDetailsDAO;
	@Mock
	private QualityDetailsDAO qualityDetailsDAO;
	@Mock
	private ConfigurationMetricsDAO configurationMetricsDAO;
	@Mock
	private DashboardDetailsDAO dashboardDetailsDAO;
	@Mock
	private ExecutiveComponentRepository executiveComponentRepository;
	@Mock
	private MetricsDetailRepository metricDetailResponseRepository;
	@Mock
	private BuildingBlocksRepository buildingBlocksRepository;
	@Mock
	private ApplicationDetailsRepository applicationDetailsRepository;
	@Mock
	private CollectorStatusRepository collectorStatusRepository;
	@Mock
	private CollectorDetailsDAO collectorDetailsDAO;
	@Mock
	private ExecutiveSummaryListRepository executiveSummaryListRepository;
	@Mock
	private PortfolioResponseRepository portfolioResponseRepository;
	@Mock
	private ExecutiveHierarchyRepository executiveHierarchyRepository;
	@Mock
	private MongoTemplate mongoTemplate;
	@Mock
	private MongoClient mongoclient;
	@Mock
	private DBCollection mongoCollection;
	@Mock
	private CollectorUpdatedDetailsRepository collectorUpdatedDetailsRepository;

	@Test
	public void processVastMetricsDetailsTest() {
		List<Vast> vastList = new ArrayList<>();
		Vast vast = new Vast();
		vast.setVastApplID("D40V");
		vast.setVastBusinessUnit("GTS");
		vast.setVastAcronym("SSSSS");
		vast.setUpdatedTimeStamp(System.currentTimeMillis());
		vast.setVastTierOneContactEid("12345");
		vast.setVastTierOneContactEmail("ceo@");
		vast.setVastTierOneContactName("Indra");
		vast.setVastTierOneContactTitle("Chairman & CEO");
		vast.setVastTierTwoContactEid("123415");
		vast.setVastTierTwoContactEmail("evp@");
		vast.setVastTierTwoContactName("Indra Reddy");
		vast.setVastTierTwoContactTitle("EVP");
		vast.setVastTierThreeContactEid("1234156");
		vast.setVastTierThreeContactEmail("svp@");
		vast.setVastTierThreeContactName("Indra Reddy");
		vast.setVastTierThreeContactTitle("SVP");
		vast.setVastTierFourContactEid("1234157");
		vast.setVastTierFourContactEmail("vp@");
		vast.setVastTierFourContactName("Indra Reddy");
		vast.setVastTierFourContactTitle("VP");
		vast.setVastTierFiveContactEid("1234158");
		vast.setVastTierFiveContactEmail("ed@");
		vast.setVastTierFiveContactName("Indra Reddy");
		vast.setVastTierFiveContactTitle("Exec Director");
		vast.setVastTierSixContactEid("1234159");
		vast.setVastTierSixContactEmail("d@");
		vast.setVastTierSixContactName("Indra Reddy");
		vast.setVastTierSixContactTitle("Director");
		vast.setVastTierSevenContactEid("1234150");
		vast.setVastTierSevenContactEmail("ad@");
		vast.setVastTierSevenContactName("Indra Reddy");
		vast.setVastTierSevenContactTitle("Assoc Director");
		vastList.add(vast);
		Vast vastNew = new Vast();
		vastNew.setVastApplID("CELV");
		vastNew.setVastBusinessUnit("GTS");
		vastNew.setVastAcronym("SSSSS");
		vastNew.setUpdatedTimeStamp(System.currentTimeMillis());
		vastNew.setVastTierOneContactEid("12345");
		vastNew.setVastTierOneContactEmail("ceo@");
		vastNew.setVastTierOneContactName("Indra");
		vastNew.setVastTierOneContactTitle("CHAIRMAN");
		vastNew.setVastTierTwoContactEid("123415");
		vastNew.setVastTierTwoContactEmail("evp@");
		vastNew.setVastTierTwoContactName("Indra Reddy");
		vastNew.setVastTierTwoContactTitle("EVP");
		vastNew.setVastTierThreeContactEid("1234156");
		vastNew.setVastTierThreeContactEmail("svp@");
		vastNew.setVastTierThreeContactName("Indra Reddy");
		vastNew.setVastTierThreeContactTitle("SVP");
		vastNew.setVastTierFourContactEid("1234157");
		vastNew.setVastTierFourContactEmail("vp@");
		vastNew.setVastTierFourContactName("Indra Reddy");
		vastNew.setVastTierFourContactTitle("VP");
		vastNew.setVastTierFiveContactEid("1234158");
		vastNew.setVastTierFiveContactEmail("ed@");
		vastNew.setVastTierFiveContactName("Indra Reddy");
		vastNew.setVastTierFiveContactTitle("SVP");
		vastNew.setVastTierSixContactEid("1234159");
		vastNew.setVastTierSixContactEmail("d@");
		vastNew.setVastTierSixContactName("Indra Reddy");
		vastNew.setVastTierSixContactTitle("VP");
		vastNew.setVastTierSevenContactEid("1234150");
		vastNew.setVastTierSevenContactEmail("ad@");
		vastNew.setVastTierSevenContactName("Indra Reddy");
		vastList.add(vastNew);
		List<String> stringList = new ArrayList<>();
		stringList.add("D40V");
		vastAnalysisTest.processVastDirectReportees();
		vastAnalysisTest.processedTitle(null);
		ConfigurationMetrics confMetrics = new ConfigurationMetrics();
		confMetrics.setModules(1);
		confMetrics.setTimeStamp(145678900000l);
		// confMetrics.setInstanceCollectorStatus(null);
		ExecutiveHierarchy executiveHierarchy = new ExecutiveHierarchy();
		executiveHierarchy.setDesignation("CHAIRMAN");
		executiveHierarchy.setEid("12345");
		executiveHierarchy.setRole("CHAIRMAN");
		Map<String, List<String>> reportees = new HashMap<>();
		reportees.put("GTS", stringList);
		executiveHierarchy.setReportees(reportees);
		List<ExecutiveSummaryList> executiveList = new ArrayList<>();
		ExecutiveSummaryList executiveSummaryList = new ExecutiveSummaryList();
		executiveSummaryList.setAppId(stringList);
		executiveSummaryList.setEid("12345");
		executiveSummaryList.setRole("CHAIRMAN");
		executiveList.add(executiveSummaryList);
		List<String> businessList = new ArrayList<>();
		businessList.add("GTS");
		executiveSummaryList.setBusinessUnits(businessList);
		ApplicationDetails appDetails = new ApplicationDetails();
		appDetails.setAppId("D40V");
		appDetails.setLob("GTS");
		appDetails.setAppName("Hygieia");
		Mockito.when(applicationDetailsRepository.findByAppId("D40V")).thenReturn(appDetails);
		appDetails = new ApplicationDetails();
		appDetails.setAppId("CELV");
		appDetails.setLob("GTS");
		appDetails.setAppName("Tools OneCom");
		Mockito.when(applicationDetailsRepository.findByAppId("CELV")).thenReturn(appDetails);
		Mockito.when(executiveSummaryListRepository.findByEid("12345")).thenReturn(executiveSummaryList);
		Mockito.when(executiveHierarchyRepository.findByEid("12345")).thenReturn(executiveHierarchy);
		Mockito.when(configurationMetricsDAO.getConfigurationMetrics("D40V", mongoclient)).thenReturn(confMetrics);
		Mockito.when(vastDetailsDAO.getMongoClient()).thenReturn(mongoclient);
		Mockito.when(vastDetailsDAO.getMappingVastId(mongoclient)).thenReturn(vastList);
		List<String> eidList = new ArrayList<>();
		eidList.add("334534534");
		eidList.add("3123123");
		Mockito.when(executiveHierarchyRepository.findByEid("334534534")).thenReturn(new ExecutiveHierarchy());
		Mockito.when(vastDetailsDAO.getVastEids(mongoclient, 1)).thenReturn(eidList);
		Mockito.when(vastDetailsDAO.getVastEidsForEid(mongoclient, 2, "334534534")).thenReturn(eidList);
		Mockito.when(dashboardDetailsDAO.getConfiguredAppIds(mongoclient)).thenReturn(stringList);
		Mockito.when(executiveSummaryListRepository.findAll()).thenReturn(executiveList);
		Mockito.when(collectorDetailsDAO.getCollectorLastRun(CollectorType.MTTR, mongoclient)).thenReturn(new Date());
		PortfolioResponse response = new PortfolioResponse();
		response.setOrder(0);
		Mockito.when(portfolioResponseRepository.findByEid("334534534")).thenReturn(response);
		vastAnalysisTest.processVastMetricsDetails();
		vastAnalysisTest.processCollectorStatus();
		vastAnalysisTest.processPortfolioResponse();
		vastAnalysisTest.processedTitle("SVP");
		vastAnalysisTest.processedTitle("VP");
		vastAnalysisTest.processedTitle("EVP");
		vastAnalysisTest.processedTitle("Dir");
		vastAnalysisTest.processedTitle("Exec Dir");
		vastAnalysisTest.processedTitle("Assoc Dir");
		vastAnalysisTest.processVastDirectReportees();
		ExecutiveHierarchy eh = new ExecutiveHierarchy();
		eh.setDirectReportees(eidList);
		Map<String, List<String>> reporte = new HashMap<>();
		reporte.put("VES", eidList);
		eh.setReportees(reporte);
		Query basicQuery = new Query();
		basicQuery.addCriteria(Criteria.where("order").in(eidList));
		basicQuery.addCriteria(Criteria.where("eid").in("334534534"));
		Mockito.when(executiveHierarchyRepository.findByEid("334534534")).thenReturn(eh);
		Mockito.when(mongoTemplate.getCollection("portfolio_response")).thenReturn(mongoCollection);
		vastAnalysisTest.processVastDirectReportees();
		response.setOrder(1);
		Mockito.when(portfolioResponseRepository.findByEid("334534534")).thenReturn(response);
		vastAnalysisTest.processVastDirectReportees();
		response.setOrder(2);
		Mockito.when(portfolioResponseRepository.findByEid("334534534")).thenReturn(response);
		vastAnalysisTest.processVastDirectReportees();
		response.setOrder(3);
		Mockito.when(portfolioResponseRepository.findByEid("334534534")).thenReturn(response);
		vastAnalysisTest.processVastDirectReportees();
		response.setOrder(4);
		Mockito.when(portfolioResponseRepository.findByEid("334534534")).thenReturn(response);
		vastAnalysisTest.processVastDirectReportees();
		response.setOrder(5);
		Mockito.when(portfolioResponseRepository.findByEid("334534534")).thenReturn(response);
		vastAnalysisTest.processVastDirectReportees();
		response.setOrder(6);
		Mockito.when(portfolioResponseRepository.findByEid("334534534")).thenReturn(response);
		vastAnalysisTest.processVastDirectReportees();
		Mockito.when(collectorUpdatedDetailsRepository.findByCollectionNameAndType("mttr", CollectorType.MTTR))
				.thenReturn(null);
		vastAnalysisTest.processCollectorUpdatedTimestamp();

		CollectorUpdatedDetails cUD = new CollectorUpdatedDetails("Security", CollectorType.MetricsProcessor, "", "");
		cUD.setCollectorStartTime(1230l);
		Mockito.when(collectorUpdatedDetailsRepository
				.findByCollectionNameAndTypeOrderByCollectorStartTimeDesc("Security", CollectorType.MetricsProcessor))
				.thenReturn(cUD);
		vastAnalysisTest.updateCollectorStatus("Security", 1230l, false);
		vastAnalysisTest.updateCollectorStatus("Security", 1230l, true);

	}
}
