package com.capitalone.dashboard.custom;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.dao.ThroughPutDAO;
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
import com.capitalone.dashboard.exec.model.ComputedPipelineMetrics;
import com.capitalone.dashboard.exec.model.ExecutiveComponents;
import com.capitalone.dashboard.exec.model.ExecutiveMetrics;
import com.capitalone.dashboard.exec.model.ExecutiveMetricsSeries;
import com.capitalone.dashboard.exec.model.ExecutiveModuleMetrics;
import com.capitalone.dashboard.exec.model.ExecutiveSummaryList;
import com.capitalone.dashboard.exec.model.JenkinsUnlimitedData;
import com.capitalone.dashboard.exec.model.MetricsDetail;
import com.capitalone.dashboard.exec.model.PortfolioResponse;
import com.capitalone.dashboard.exec.model.ProductPipelineData;
import com.capitalone.dashboard.exec.model.SeriesCount;
import com.capitalone.dashboard.exec.model.ThroughPutModel;
import com.capitalone.dashboard.exec.repository.ApplicationDetailsRepository;
import com.capitalone.dashboard.exec.repository.BuildingBlocksRepository;
import com.capitalone.dashboard.exec.repository.CollectorStatusRepository;
import com.capitalone.dashboard.exec.repository.ExecutiveComponentRepository;
import com.capitalone.dashboard.exec.repository.ExecutiveSummaryListRepository;
import com.capitalone.dashboard.exec.repository.JenkinsUnlimitedDataRepository;
import com.capitalone.dashboard.exec.repository.MetricsDetailRepository;
import com.capitalone.dashboard.exec.repository.PortfolioResponseRepository;
import com.capitalone.dashboard.utils.GenericMethods;
import com.capitalone.dashboard.utils.LinearRegression;
import com.mongodb.MongoClient;

/**
 * ThroughPutAnalysis
 * 
 * @param <ThroughPutDAO>
 *            ...
 * @return
 * @author raish4s
 */
@Component
@SuppressWarnings("PMD")
public class ThroughPutAnalysis implements MetricsProcessor {

	private final ThroughPutDAO throughPutDAO;
	private final VastDetailsDAO vastDetailsDAO;
	private final MongoTemplate mongoTemplate;
	private final ExecutiveComponentRepository executiveComponentRepository;
	private final ExecutiveSummaryListRepository executiveSummaryListRepository;
	private final MetricsDetailRepository metricsDetailRepository;
	private final ApplicationDetailsRepository applicationDetailsRepository;
	private final CollectorStatusRepository collectorStatusRepository;
	private final BuildingBlocksRepository buildingBlocksRepository;
	private final JenkinsUnlimitedDataRepository jenkinsUnlimitedDataRepository;
	private final GenericMethods genericMethods;
	private final ServiceNowAnalysis serviceNowAnalysis;
	private final PortfolioResponseRepository portfolioResponseRepository;

	private static final String PIPELINEMETRICS = "pipeline-lead-time";
	private static final String LEADTIME = "lead-time";
	private static final String COMMIT = "commit";
	private static final String CADENCE = "cadence";
	private static final String CACTIVITY = "changeactivity";
	private static final String METRICSNAME = "metrics.metricsName";
	private static final String EXECUTIVECOLLECTION = "executives_metrics";
	private static final String APPID = "appId";
	private static final String FALSE = "false";
	private static final String TRUE = "true";
	private static final String PRODCONF = "prodConf";
	private static final Logger LOG = LoggerFactory.getLogger(ThroughPutAnalysis.class);

	/**
	 * @param throughPutDAO
	 * @param executiveComponentRepository
	 * @param mongoTemplate
	 * @param executiveSummaryListRepository
	 * @param metricsDetailRepository
	 * @param applicationDetailsRepository
	 * @param buildingBlocksRepository
	 * @param collectorStatusRepository
	 * @param jenkinsUnlimitedDataRepository
	 * @param serviceNowAnalysis
	 * @param vastDetailsDAO
	 * @param genericMethods
	 * @param portfolioResponseRepository
	 */
	@Autowired
	public ThroughPutAnalysis(ThroughPutDAO throughPutDAO, ExecutiveComponentRepository executiveComponentRepository,
			MongoTemplate mongoTemplate, ExecutiveSummaryListRepository executiveSummaryListRepository,
			MetricsDetailRepository metricsDetailRepository, ApplicationDetailsRepository applicationDetailsRepository,
			BuildingBlocksRepository buildingBlocksRepository, CollectorStatusRepository collectorStatusRepository,
			JenkinsUnlimitedDataRepository jenkinsUnlimitedDataRepository, ServiceNowAnalysis serviceNowAnalysis,
			VastDetailsDAO vastDetailsDAO, GenericMethods genericMethods,
			PortfolioResponseRepository portfolioResponseRepository) {
		this.throughPutDAO = throughPutDAO;
		this.executiveComponentRepository = executiveComponentRepository;
		this.executiveSummaryListRepository = executiveSummaryListRepository;
		this.metricsDetailRepository = metricsDetailRepository;
		this.applicationDetailsRepository = applicationDetailsRepository;
		this.buildingBlocksRepository = buildingBlocksRepository;
		this.collectorStatusRepository = collectorStatusRepository;
		this.jenkinsUnlimitedDataRepository = jenkinsUnlimitedDataRepository;
		this.serviceNowAnalysis = serviceNowAnalysis;
		this.vastDetailsDAO = vastDetailsDAO;
		this.mongoTemplate = mongoTemplate;
		this.genericMethods = genericMethods;
		this.portfolioResponseRepository = portfolioResponseRepository;
	}

	/**
	 * ThroughPutAnalysis processThroughPutMetricsDetails() conversion of
	 * throughPutModel to New Model executives_metrics collections
	 * 
	 * @param ...
	 * @return Boolean
	 */
	public Boolean processExecutiveMetricsDetails() {
		LOG.info("Processing ThroughPut Details : executives_metrics . . . . ");
		MongoClient client = null;
		int count = 1;
		try {
			client = throughPutDAO.getMongoClient();
			List<String> appIds = throughPutDAO.getEntireAppList(client);
			if (!appIds.isEmpty()) {
				for (String appId : appIds) {
					processExecutiveComponent(appId, client);

					LOG.info(appIds.size() + "/" + count);
					count += 1;
				}
			}
			LOG.info("Completed ThroughPut Details : executives_metrics . . . . ");
		} catch (Exception e) {
			LOG.error("Error inside Throughput Analysis file - processThroughPutMetricsDetails() : " + e);
		} finally {
			if (client != null)
				client.close();
		}
		return true;
	}

	private Boolean processExecutiveComponent(String appId, MongoClient client) {
		try {
			ApplicationDetails appDetails = applicationDetailsRepository.findByAppId(appId);
			ExecutiveComponents executiveComponent = executiveComponentRepository.findByAppIdAndMetric(appId,
					PIPELINEMETRICS);
			if (executiveComponent == null)
				executiveComponent = new ExecutiveComponents();

			executiveComponent.setAppId(appId);
			if (appDetails != null) {
				executiveComponent.setAppName(appDetails.getAppName());
				executiveComponent.setTeamBoardLink(appDetails.getTeamBoardLink());
			}

			List<ExecutiveMetrics> executiveMetricsList = processAppMetrics(appId, client);

			executiveComponent.setMetrics(executiveMetricsList);
			executiveComponentRepository.save(executiveComponent);
		} catch (Exception e) {
			LOG.error("Error inside Throughput Analysis file - processExecutiveComponent() : " + e);
		}
		LOG.info("Data Collected from OneHygieia for " + appId);
		return true;

	}

	private List<String> processJenkinsMetrics(String appId) {
		Query query = new Query(new Criteria().where("appId").is(appId));
		return mongoTemplate.getCollection("jenkins_unlimited").distinct("buildJob", query.getQueryObject());
	}

	private List<ExecutiveMetrics> processAppMetrics(String appId, MongoClient client) {

		List<ExecutiveMetrics> executiveMetricsList = new ArrayList<>();
		List<ExecutiveModuleMetrics> executiveModuleMetricsList = new ArrayList<>();
		ExecutiveMetrics executiveMetrics = new ExecutiveMetrics();

		ProductPipelineData pipelineData = throughPutDAO.getByAppId(appId, client);
		if (pipelineData != null) {
			List<ThroughPutModel> throughputDataList = pipelineData.getThroughPutModel();
			if (throughputDataList != null) {
				for (ThroughPutModel throughputData : throughputDataList) {
					executiveModuleMetricsList.add(processThroughPutMetrics(throughputData));
				}
			}
		}

		List<String> jobList = processJenkinsMetrics(appId);
		if (jobList != null && !jobList.isEmpty()) {
			for (String jobName : jobList) {
				executiveModuleMetricsList.add(processThroughPutJenkinsMetrics(jobName));
			}
		}

		executiveMetrics.setLastScanned(new Date(System.currentTimeMillis()));
		executiveMetrics.setLastUpdated(new Date(System.currentTimeMillis()));
		executiveMetrics.setModules(executiveModuleMetricsList);
		executiveMetrics.setMetricsName(PIPELINEMETRICS);
		executiveMetrics.setTrendSlope(getTrendSlopesForModules(executiveModuleMetricsList));
		executiveMetricsList.add(executiveMetrics);
		return executiveMetricsList;
	}

	/**
	 * addRemainingModules
	 * 
	 * @param executiveModuleMetricsList
	 * @param appId
	 * @param client
	 * @param lastUpdated
	 * @return List<ExecutiveModuleMetrics>
	 */
	public List<ExecutiveModuleMetrics> addRemainingModules(List<ExecutiveModuleMetrics> executiveModuleMetricsList,
			String appId, MongoClient client, Date lastUpdated) {

		if (!executiveModuleMetricsList.isEmpty()) {
			List<String> remainingModulesList = getRemainingModuleNamesFromDefaultHygieia(appId, client);
			if (!remainingModulesList.isEmpty()) {
				for (String module : remainingModulesList) {
					ExecutiveModuleMetrics moduleData = new ExecutiveModuleMetrics();
					moduleData.setLastScanned(lastUpdated);
					moduleData.setLastUpdated(new Date(System.currentTimeMillis()));
					moduleData.setModuleName(module);
					moduleData.setSeries(null);
					moduleData.setTrendSlope(0.0);
					executiveModuleMetricsList.add(moduleData);
				}
			}
		}

		return executiveModuleMetricsList;
	}

	/**
	 * getRemainingModuleNamesFromDefaultHygieia
	 * 
	 * @param appId
	 * @param client
	 * @return List<String>
	 */
	public List<String> getRemainingModuleNamesFromDefaultHygieia(String appId, MongoClient client) {
		List<String> remainingModulesList = new ArrayList<>();
		Map<String, String> dbDetails = throughPutDAO.getDbDetails(appId, client);
		if (!dbDetails.isEmpty()) {
			remainingModulesList = throughPutDAO.getRemainingModuleList(dbDetails);
		}
		return remainingModulesList;
	}

	private ExecutiveModuleMetrics processThroughPutMetrics(ThroughPutModel throughputData) {
		Date lastUpdated = null;
		CollectorStatus collectorStatus = collectorStatusRepository.findByType(CollectorType.Portfolio_DB);
		if (collectorStatus != null)
			lastUpdated = collectorStatus.getLastUpdated();
		List<ExecutiveMetricsSeries> metricSeries = new ArrayList<>();
		ExecutiveModuleMetrics executiveModuleMetrics = new ExecutiveModuleMetrics();
		if (throughputData != null) {
			List<ComputedPipelineMetrics> computedMetrics = throughputData.getComputedPipelineMetrics();
			if (!computedMetrics.isEmpty()) {
				for (ComputedPipelineMetrics metrics : computedMetrics) {
					metricSeries.add(processComputedMetrics(metrics));
				}
			}
			executiveModuleMetrics.setModuleName(throughputData.getDashboardName());
		}
		executiveModuleMetrics.setSeries(metricSeries);
		executiveModuleMetrics.setLastScanned(lastUpdated);
		executiveModuleMetrics.setLastUpdated(new Date(System.currentTimeMillis()));
		executiveModuleMetrics.setTrendSlope(getTrendSlope(metricSeries));
		return executiveModuleMetrics;
	}

	private ExecutiveModuleMetrics processThroughPutJenkinsMetrics(String jobName) {
		Date lastUpdated = new Date(System.currentTimeMillis());
		List<ExecutiveMetricsSeries> metricSeries = new ArrayList<>();
		ExecutiveModuleMetrics executiveModuleMetrics = new ExecutiveModuleMetrics();
		if (jobName != null) {
			List<JenkinsUnlimitedData> computedMetrics = jenkinsUnlimitedDataRepository.findByBuildJob(jobName);
			if (!computedMetrics.isEmpty()) {
				for (JenkinsUnlimitedData metrics : computedMetrics) {
					metricSeries.add(processComputedJenkinsMetrics(metrics));
				}
			}
			executiveModuleMetrics.setModuleName(jobName);
		}
		executiveModuleMetrics.setSeries(metricSeries);
		executiveModuleMetrics.setLastScanned(lastUpdated);
		executiveModuleMetrics.setLastUpdated(lastUpdated);
		executiveModuleMetrics.setTrendSlope(getTrendSlope(metricSeries));
		return executiveModuleMetrics;
	}

	private ExecutiveMetricsSeries processComputedMetrics(ComputedPipelineMetrics metrics) {
		ExecutiveMetricsSeries executiveMetricsSeries = new ExecutiveMetricsSeries();
		if (metrics != null) {
			if (metrics.getStageDetails() != null) {
				Map<String, Object> stages = metrics.getStageDetails();
				executiveMetricsSeries.setCounts(processSeriesCounts(stages));
			} else {
				Map<String, Object> stages = new HashMap<>();
				executiveMetricsSeries.setCounts(processSeriesCounts(stages));
			}
			executiveMetricsSeries.setTimeStamp(metrics.getBeginTimestamp());
			executiveMetricsSeries.setDaysAgo(metrics.getInterval());
			return executiveMetricsSeries;
		} else {
			return null;
		}
	}

	private ExecutiveMetricsSeries processComputedJenkinsMetrics(JenkinsUnlimitedData metrics) {
		ExecutiveMetricsSeries executiveMetricsSeries = new ExecutiveMetricsSeries();
		if (metrics != null) {
			executiveMetricsSeries.setCounts(processSeriesJenkinsCounts(metrics));
			executiveMetricsSeries.setTimeStamp((long) 0);
			executiveMetricsSeries.setDaysAgo(metrics.getPeriod());
			return executiveMetricsSeries;
		} else {
			return null;
		}
	}

	private List<SeriesCount> processSeriesJenkinsCounts(JenkinsUnlimitedData stages) {
		List<SeriesCount> counts = new ArrayList<>();
		String prodConf = FALSE;
		if (stages.getProdJobAvailable()) {
			prodConf = "true";
		}
		SeriesCount series1 = new SeriesCount();
		Map<String, String> label1 = new HashMap<>();
		label1.put("type", LEADTIME);
		series1.setLabel(label1);
		series1.setCount(stages.getDuration());
		counts.add(series1);

		SeriesCount series2 = new SeriesCount();
		Map<String, String> label2 = new HashMap<>();
		label2.put("type", COMMIT);
		series2.setLabel(label2);
		series2.setCount(stages.getCommits());
		counts.add(series2);

		SeriesCount series3 = new SeriesCount();
		Map<String, String> label3 = new HashMap<>();
		label3.put(PRODCONF, prodConf);
		series3.setLabel(label3);
		series3.setCount((long) 0);
		counts.add(series3);
		return counts;
	}

	private List<SeriesCount> processSeriesCounts(Map<String, Object> stages) {
		List<SeriesCount> counts = new ArrayList<>();
		Long duration = (long) 0;
		Long commits = (long) 0;
		String prodConf = FALSE;
		if (!stages.isEmpty() && stages.get("prodStage") != null) {
			String prod = stages.get("prodStage").toString();
			if (!prod.isEmpty() && stages.get(prod) != null) {
				prodConf = "true";
				Map<String, Object> prodStage = (Map<String, Object>) stages.get(prod);
				duration = getLong(prodStage.get("durationTimestamp"));
				if (prodStage.get("durationTimestamp") == null)
					duration = (long) 0;
				commits = getLong(prodStage.get("commits"));
				if (prodStage.get("commits") == null)
					commits = (long) 0;
			}
		}
		SeriesCount series1 = new SeriesCount();
		Map<String, String> label1 = new HashMap<>();
		label1.put("type", LEADTIME);
		series1.setLabel(label1);
		series1.setCount(duration);
		counts.add(series1);

		SeriesCount series2 = new SeriesCount();
		Map<String, String> label2 = new HashMap<>();
		label2.put("type", COMMIT);
		series2.setLabel(label2);
		series2.setCount(commits);
		counts.add(series2);

		SeriesCount series3 = new SeriesCount();
		Map<String, String> label3 = new HashMap<>();
		label3.put(PRODCONF, prodConf);
		series3.setLabel(label3);
		series3.setCount((long) 0);
		counts.add(series3);
		return counts;
	}

	/**
	 * ThroughPutAnalysis processMetricsDetail() portfolio level data
	 * 
	 * @param ...
	 * @return Boolean
	 */
	public Boolean processExecutiveDetailsMetrics() {
		LOG.info("Processing ThroughPut Details : portfolio_metrics_details . . . . ");
		List<ExecutiveSummaryList> executives = (List<ExecutiveSummaryList>) executiveSummaryListRepository.findAll();
		if (!executives.isEmpty()) {
			for (ExecutiveSummaryList executive : executives) {
				String eid = executive.getEid();
				MetricsDetail metricPortfolioDetailResponse = metricsDetailRepository.findByMetricLevelIdAndLevelAndType(eid,
						MetricLevel.PORTFOLIO, MetricType.PIPELINE_LEAD_TIME);
				if (metricPortfolioDetailResponse == null)
					metricPortfolioDetailResponse = new MetricsDetail();
				metricPortfolioDetailResponse.setType(MetricType.PIPELINE_LEAD_TIME);
				metricPortfolioDetailResponse.setLevel(MetricLevel.PORTFOLIO);
				metricPortfolioDetailResponse.setMetricLevelId(eid);
				metricPortfolioDetailResponse.setCustomField(getPortfolioId(eid));
				metricPortfolioDetailResponse.setSummary(getThroughPutForPortfolio(executive.getAppId()));
				metricPortfolioDetailResponse.setTimeSeries(processPortfolioTimeSeries(executive.getAppId()));
				if (metricPortfolioDetailResponse.getSummary() != null) {
					metricPortfolioDetailResponse.getSummary().setTotalComponents(executive.getTotalApps());
					metricPortfolioDetailResponse.getSummary()
							.setReportingComponents(getReportingCount(executive.getAppId()));
				}
				metricsDetailRepository.save(metricPortfolioDetailResponse);
			}
		}
		LOG.info("Completed ThroughPut Details : portfolio_metrics_details . . . . ");
		return true;
	}

	private String getPortfolioId(String eid) {
		PortfolioResponse portfolioResponse = portfolioResponseRepository.findByEid(eid);
		return portfolioResponse == null ? null : portfolioResponse.getId().toString();
	}

	private Integer getReportingCount(List<String> configuredAppId) {
		int reportingComponents = 0;
		for (String appId : configuredAppId) {
			BuildingBlocks buildingblock = buildingBlocksRepository.findByMetricLevelIdAndMetricLevel(appId, MetricLevel.PRODUCT);
			if (buildingblock != null && buildingblock.getMetrics() != null) {
				for (MetricSummary metrics : buildingblock.getMetrics()) {
					if (PIPELINEMETRICS.equalsIgnoreCase(metrics.getName()) && metrics.getDataAvailable())
						reportingComponents++;
				}
			}
		}
		return reportingComponents;
	}

	private MetricSummary getThroughPutForPortfolio(List<String> appIds) {
		MetricSummary metricSummaryResponse = new MetricSummary();
		List<MetricCount> counts = new ArrayList<>();
		MetricCount metricLeadTime = new MetricCount();
		MetricCount metricCommit = new MetricCount();
		MetricCount metricCadence = new MetricCount();
		MetricCount metricCactivity = new MetricCount();
		MetricCount checkConfigData = new MetricCount();
		metricSummaryResponse.setLastScanned(new Date(System.currentTimeMillis()));
		metricSummaryResponse.setLastUpdated(new Date(System.currentTimeMillis()));
		metricSummaryResponse.setName(PIPELINEMETRICS);
		Boolean dataAvailability = false;

		boolean checkConfig = false;
		long leadTime = 0;
		long commits = 0;
		double cadence = 0;
		long cadenceCount = 0;
		double cactivityNo = 0;
		long count = 0;
		if (!appIds.isEmpty()) {
			for (String appId : appIds) {
				MetricsDetail metricDetail = metricsDetailRepository.findByMetricLevelIdAndLevelAndType(appId, MetricLevel.PRODUCT,
						MetricType.PIPELINE_LEAD_TIME);
				if (metricDetail != null) {
					MetricSummary summaryForApp = metricDetail.getSummary();
					metricSummaryResponse.setTrendSlope(summaryForApp.getTrendSlope());
					List<MetricCount> seriesCount = summaryForApp.getCounts();
					if (!seriesCount.isEmpty()) {
						MetricCount leadTimeSeries = seriesCount.get(0);
						leadTime += leadTimeSeries.getValue();

						if (leadTimeSeries.getValue() > 0)
							count++;
						MetricCount commitSeries = seriesCount.get(1);
						commits += commitSeries.getValue();

						if (seriesCount.get(2) != null) {
							MetricCount confCheck = seriesCount.get(2);
							if ("true".equals(confCheck.getLabel().get(PRODCONF)))
								checkConfig = true;
						}

						MetricCount cadenceSeries = seriesCount.get(3);
						cadence += cadenceSeries.getValue();

						if (cadenceSeries.getValue() > 0)
							cadenceCount++;

						if (seriesCount.get(4) != null) {
							MetricCount cactiv = seriesCount.get(4);
							cactivityNo += cactiv.getValue();
						}

					}
				}
			}
		}
		Map<String, String> leadtime = new HashMap<>();
		leadtime.put("type", LEADTIME);
		metricLeadTime.setLabel(leadtime);

		Map<String, String> commit = new HashMap<>();
		commit.put("type", COMMIT);
		metricCommit.setLabel(commit);

		if (!appIds.isEmpty() && count > 0) {
			metricLeadTime.setValue(leadTime / count);
			metricCommit.setValue(commits);
			dataAvailability = true;
		}

		counts.add(metricLeadTime);
		counts.add(metricCommit);

		if (commits == 0 && !checkConfig) {
			Map<String, String> label3 = new HashMap<>();
			label3.put(PRODCONF, FALSE);
			checkConfigData.setLabel(label3);
			checkConfigData.setValue(0);
			counts.add(checkConfigData);
		} else {
			Map<String, String> label3 = new HashMap<>();
			label3.put(PRODCONF, "true");
			checkConfigData.setLabel(label3);
			checkConfigData.setValue(0);
			counts.add(checkConfigData);
		}

		Map<String, String> cadences = new HashMap<>();
		cadences.put("type", CADENCE);
		metricCadence.setLabel(cadences);

		if (!appIds.isEmpty() && count > 0) {
			metricCadence.setValue(cadence / cadenceCount);
		}

		counts.add(metricCadence);

		Map<String, String> cactivitys = new HashMap<>();
		cactivitys.put("type", CACTIVITY);
		metricCactivity.setLabel(cactivitys);

		if (!appIds.isEmpty() && count > 0) {
			metricCactivity.setValue(cactivityNo);
		}

		counts.add(metricCactivity);

		metricSummaryResponse.setCounts(counts);
		metricSummaryResponse.setDataAvailable(dataAvailability);
		return metricSummaryResponse;
	}

	private List<MetricTimeSeriesElement> processPortfolioTimeSeries(List<String> appIds) {
		List<MetricTimeSeriesElement> timeSeries = new ArrayList<>();
		MetricTimeSeriesElement timeSeries0 = new MetricTimeSeriesElement();
		MetricTimeSeriesElement timeSeries30 = new MetricTimeSeriesElement();
		MetricTimeSeriesElement timeSeries60 = new MetricTimeSeriesElement();

		List<MetricCount> counts0 = new ArrayList<>();
		MetricCount metricLeadTime0 = new MetricCount();
		MetricCount metricCommit0 = new MetricCount();
		List<MetricCount> counts30 = new ArrayList<>();
		MetricCount metricLeadTime30 = new MetricCount();
		MetricCount metricCommit30 = new MetricCount();
		List<MetricCount> counts60 = new ArrayList<>();
		MetricCount metricLeadTime60 = new MetricCount();
		MetricCount metricCommit60 = new MetricCount();

		long leadTime0 = 0;
		long commits0 = 0;
		long count0 = 0;
		long leadTime30 = 0;
		long commits30 = 0;
		long count30 = 0;
		long leadTime60 = 0;
		long commits60 = 0;
		long count60 = 0;
		if (!appIds.isEmpty()) {
			for (String appId : appIds) {
				MetricsDetail metricDetails = metricsDetailRepository.findByMetricLevelIdAndLevelAndType(appId,
						MetricLevel.PRODUCT, MetricType.PIPELINE_LEAD_TIME);
				if (metricDetails != null) {
					List<MetricTimeSeriesElement> series = metricDetails.getTimeSeries();
					for (MetricTimeSeriesElement seriesElement : series) {
						List<MetricCount> seriesCount = seriesElement.getCounts();
						if (!seriesCount.isEmpty()) {
							switch (seriesElement.getDaysAgo()) {
							case 0:
								MetricCount leadTimeSeries = seriesCount.get(0);
								leadTime0 += leadTimeSeries.getValue();
								if (leadTimeSeries.getValue() > 0)
									count0++;
								MetricCount commitSeries = seriesCount.get(1);
								commits0 += commitSeries.getValue();
								break;
							case 30:
								MetricCount leadTimeSeries30 = seriesCount.get(0);
								leadTime30 += leadTimeSeries30.getValue();
								if (leadTimeSeries30.getValue() > 0)
									count30++;
								MetricCount commitSeries30 = seriesCount.get(1);
								commits30 += commitSeries30.getValue();
								break;
							case 60:
								MetricCount leadTimeSeries60 = seriesCount.get(0);
								leadTime60 += leadTimeSeries60.getValue();
								if (leadTimeSeries60.getValue() > 0)
									count60++;
								MetricCount commitSeries60 = seriesCount.get(1);
								commits60 += commitSeries60.getValue();
								break;
							default:
								break;
							}
						}
					}
				}
			}
		}
		Map<String, String> label1 = new HashMap<>();
		label1.put("type", LEADTIME);
		metricLeadTime0.setLabel(label1);
		metricLeadTime30.setLabel(label1);
		metricLeadTime60.setLabel(label1);

		Map<String, String> label2 = new HashMap<>();
		label2.put("type", COMMIT);
		metricCommit0.setLabel(label2);
		metricCommit30.setLabel(label2);
		metricCommit60.setLabel(label2);

		if (!appIds.isEmpty()) {
			if (count0 > 0) {
				metricLeadTime0.setValue(leadTime0 / count0);
				metricCommit0.setValue(commits0);
			} else {
				metricLeadTime0.setValue(0);
				metricCommit0.setValue(0);
			}
			if (count30 > 0) {
				metricLeadTime30.setValue(leadTime30 / count30);
				metricCommit30.setValue(commits30);
			} else {
				metricLeadTime30.setValue(30);
				metricCommit30.setValue(0);
			}
			if (count60 > 0) {
				metricLeadTime60.setValue(leadTime60 / count60);
				metricCommit60.setValue(commits60);
			} else {
				metricLeadTime60.setValue(60);
				metricCommit60.setValue(0);
			}
		}
		counts0.add(metricLeadTime0);
		counts0.add(metricCommit0);
		timeSeries0.setDaysAgo(0);
		timeSeries0.setCounts(counts0);

		counts30.add(metricLeadTime30);
		counts30.add(metricCommit30);
		timeSeries30.setDaysAgo(30);
		timeSeries30.setCounts(counts30);

		counts60.add(metricLeadTime60);
		counts60.add(metricCommit60);
		timeSeries60.setDaysAgo(60);
		timeSeries60.setCounts(counts60);

		timeSeries.add(timeSeries0);
		timeSeries.add(timeSeries30);
		timeSeries.add(timeSeries60);
		return timeSeries;
	}

	/**
	 * ThroughPutAnalysis processBuildingBlocks() building_block_metrics
	 * Collection
	 * 
	 * @param ...
	 * @return Boolean
	 */
	public Boolean processBuildingBlockMetrics() {
		LOG.info("Processing ThroughPut Details : building_block_metrics . . . . ");
		Query query = new Query(new Criteria().where(METRICSNAME).is(PIPELINEMETRICS));
		List<String> appIds = mongoTemplate.getCollection(EXECUTIVECOLLECTION).distinct(APPID, query.getQueryObject());
		List<BuildingBlocks> buildingBlockMetricSummaryList = new ArrayList<>();
		if (!appIds.isEmpty()) {
			for (String appId : appIds) {
				buildingBlockMetricSummaryList.add(processBuildingBlockSummary(appId));
			}
			if (!buildingBlockMetricSummaryList.isEmpty())
				buildingBlocksRepository.save(buildingBlockMetricSummaryList);
		}
		LOG.info("Completed ThroughPut Details : building_block_metrics . . . . ");
		return true;
	}

	private BuildingBlocks processBuildingBlockSummary(String appId) {
		BuildingBlocks buildingBlockMetricSummary = buildingBlocksRepository.findByMetricLevelIdAndMetricLevel(appId,
				MetricLevel.PRODUCT);
		if (buildingBlockMetricSummary == null)
			buildingBlockMetricSummary = new BuildingBlocks();

		ApplicationDetails appDetails = applicationDetailsRepository.findByAppId(appId);
		MetricsDetail metricDetails = metricsDetailRepository.findByMetricLevelIdAndLevelAndType(appId, MetricLevel.PRODUCT,
				MetricType.PIPELINE_LEAD_TIME);

		if (metricDetails != null) {
			MetricSummary pipelineLeadtimeSummary = metricDetails.getSummary();

			buildingBlockMetricSummary.setMetricLevelId(appId);
			if (appDetails != null) {
				buildingBlockMetricSummary.setLob(appDetails.getLob());
				buildingBlockMetricSummary.setName(appDetails.getAppName());
				buildingBlockMetricSummary.setPoc(appDetails.getPoc());
			}
			buildingBlockMetricSummary.setMetricLevel(MetricLevel.PRODUCT);
			buildingBlockMetricSummary.setAppCriticality(pipelineLeadtimeSummary.getAppCriticality());
			buildingBlockMetricSummary.setTotalComponents(pipelineLeadtimeSummary.getTotalComponents());
			buildingBlockMetricSummary.setTotalExpectedMetrics(pipelineLeadtimeSummary.getReportingComponents());
			List<MetricSummary> metricsResponseStored = buildingBlockMetricSummary.getMetrics();
			List<MetricSummary> metricsResponseProcessed = new ArrayList<>();
			if (metricsResponseStored != null && !metricsResponseStored.isEmpty()) {
				for (MetricSummary metricSummaryResponse : metricsResponseStored) {
					if (!metricSummaryResponse.getName().equalsIgnoreCase(PIPELINEMETRICS))
						metricsResponseProcessed.add(metricSummaryResponse);
				}
			}
			metricsResponseProcessed.add(pipelineLeadtimeSummary);
			buildingBlockMetricSummary.setMetrics(metricsResponseProcessed);
		}
		return buildingBlockMetricSummary;

	}

	/**
	 * ThroughPutAnalysis processMetricsDetailResponse() app_metrics_details
	 * Collection
	 * 
	 * @param ...
	 * @return Boolean
	 */
	public Boolean processMetricsDetailResponse() {
		LOG.info("Processing ThroughPut Details : app_metrics_details . . . . ");
		MongoClient client = null;
		try {
			Query query = new Query(new Criteria().where(METRICSNAME).is(PIPELINEMETRICS));
			List<String> appIds = mongoTemplate.getCollection(EXECUTIVECOLLECTION).distinct(APPID,
					query.getQueryObject());
			if (!appIds.isEmpty()) {
				client = throughPutDAO.getMongoClient();
				for (String appId : appIds) {
					ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
							PIPELINEMETRICS);
					MetricsDetail metricDetailResponseProcessed = getMetricDetailResponseForApp(executiveComponents,
							client);
					MetricsDetail metricDetailResponseStored = metricsDetailRepository.findByMetricLevelIdAndLevelAndType(appId,
							MetricLevel.PRODUCT, MetricType.PIPELINE_LEAD_TIME);
					if (metricDetailResponseStored != null) {
						metricsDetailRepository.delete(metricDetailResponseStored);
					}
					metricsDetailRepository.save(metricDetailResponseProcessed);
				}
			}
		} catch (Exception e) {
			LOG.info("Error in ThroughPut Details : app_metrics_details . . . . " + e);
		} finally {
			if (client != null)
				client.close();
		}
		LOG.info("Completed ThroughPut Details : app_metrics_details . . . . ");
		return true;
	}

	private MetricsDetail getMetricDetailResponseForApp(ExecutiveComponents executiveComponents, MongoClient client) {
		MetricsDetail metricDetailResponse = new MetricsDetail();
		List<MetricTimeSeriesElement> timeSeries = new ArrayList<>();
		if (executiveComponents.getMetrics() != null) {
			List<ExecutiveMetrics> metrics = executiveComponents.getMetrics();
			for (ExecutiveMetrics metric : metrics) {
				if (metric.getMetricsName().equalsIgnoreCase(PIPELINEMETRICS)) {
					MetricTimeSeriesElement timeSeries0 = timeSeriesDataForApp(metric, 0);
					MetricTimeSeriesElement timeSeries30 = timeSeriesDataForApp(metric, 30);
					MetricTimeSeriesElement timeSeries60 = timeSeriesDataForApp(metric, 60);
					timeSeries.add(timeSeries0);
					timeSeries.add(timeSeries30);
					timeSeries.add(timeSeries60);
				}
			}
		}
		MetricSummary summary = getMetricsForApp(executiveComponents, client);
		metricDetailResponse.setSummary(summary);
		metricDetailResponse.setMetricLevelId(executiveComponents.getAppId());
		metricDetailResponse.setType(MetricType.PIPELINE_LEAD_TIME);
		metricDetailResponse.setLevel(MetricLevel.PRODUCT);
		metricDetailResponse.setTimeSeries(timeSeries);
		return metricDetailResponse;
	}

	private MetricTimeSeriesElement timeSeriesDataForApp(ExecutiveMetrics metric, int daysAgo) {
		MetricTimeSeriesElement metricTimeSeriesElementResponse = new MetricTimeSeriesElement();
		List<ExecutiveModuleMetrics> modules = metric.getModules();
		List<MetricCount> metricCountResponse = new ArrayList<>();
		MetricCount metricLeadTime = new MetricCount();
		MetricCount metricCommit = new MetricCount();
		long leadTime = 0;
		long commits = 0;
		long count = 0;
		if (metric.getModules() != null) {
			for (ExecutiveModuleMetrics module : modules) {
				List<ExecutiveMetricsSeries> metricSeries = module.getSeries();
				if (metricSeries != null) {
					for (ExecutiveMetricsSeries series : metricSeries) {
						if (series.getDaysAgo() == daysAgo) {
							List<SeriesCount> seriesCount = series.getCounts();
							if (seriesCount != null) {
								SeriesCount leadTimeSeries = seriesCount.get(0);
								leadTime += leadTimeSeries.getCount();
								if (leadTimeSeries.getCount() > 0)
									count++;

								SeriesCount commitSeries = seriesCount.get(1);
								commits += commitSeries.getCount();
							}
						}
					}
				}
			}
		}

		Map<String, String> label1 = new HashMap<>();
		label1.put("type", LEADTIME);
		metricLeadTime.setLabel(label1);

		Map<String, String> label2 = new HashMap<>();
		label2.put("type", COMMIT);
		metricCommit.setLabel(label2);

		if (!modules.isEmpty() && count > 0) {
			metricLeadTime.setValue(leadTime / count);
			metricCommit.setValue(commits);
		}
		metricCountResponse.add(metricLeadTime);
		metricCountResponse.add(metricCommit);
		metricTimeSeriesElementResponse.setDaysAgo(daysAgo);
		metricTimeSeriesElementResponse.setCounts(metricCountResponse);
		return metricTimeSeriesElementResponse;
	}

	private MetricSummary getMetricsForApp(ExecutiveComponents executiveComponent, MongoClient client) {
		MetricSummary metricSummaryResponse = new MetricSummary();
		metricSummaryResponse.setLastScanned(new Date(System.currentTimeMillis()));
		metricSummaryResponse.setLastUpdated(new Date(System.currentTimeMillis()));
		metricSummaryResponse.setName(PIPELINEMETRICS);

		String appId = executiveComponent.getAppId();
		if (executiveComponent.getMetrics() != null) {
			List<ExecutiveMetrics> metrics = executiveComponent.getMetrics();
			for (ExecutiveMetrics metric : metrics) {
				if (metric.getMetricsName().equalsIgnoreCase(PIPELINEMETRICS)) {
					List<ExecutiveModuleMetrics> modules = metric.getModules();
					metricSummaryResponse.setCounts(processMetricsCount(modules, appId, client));
					if (!modules.isEmpty())
						metricSummaryResponse.setTotalComponents(modules.size());

					metricSummaryResponse.setReportingComponents(getModulesReporting(modules));
					metricSummaryResponse.setTrendSlope(executiveComponent.getMetrics().get(0).getTrendSlope());
					metricSummaryResponse
							.setAppCriticality(genericMethods.processAppCriticality(executiveComponent.getAppId()));
					Boolean dataAvailability = checkForDataAvailability(metricSummaryResponse.getCounts());
					metricSummaryResponse.setDataAvailable(dataAvailability);
					if (!dataAvailability)
						metricSummaryResponse.setConfMessage(checkForDataAvailabilityStatus(modules));
				}
			}
		}
		return metricSummaryResponse;
	}

	private Integer getModulesReporting(List<ExecutiveModuleMetrics> modules) {
		int count = 0;
		for (ExecutiveModuleMetrics module : modules) {
			if (module.getSeries() != null) {
				count += 1;
			}
		}
		return count;
	}

	private Boolean checkForDataAvailability(List<MetricCount> counts) {
		try {
			if (!counts.isEmpty()) {
				for (MetricCount response : counts) {
					if (response != null && response.getLabel() != null && response.getLabel().get("type") != null
							&& response.getLabel().get("type").equalsIgnoreCase(LEADTIME) && response.getValue() > 0)
						return true;
				}
			}
		} catch (Exception e) {
			LOG.info("Error while checkForDataAvailability: " + e);
		}
		return false;
	}

	private String checkForDataAvailabilityStatus(List<ExecutiveModuleMetrics> modules) {
		boolean check = false;
		if (modules.isEmpty())
			return "Product Dashboard Not Configured";
		for (ExecutiveModuleMetrics module : modules) {
			if (module.getSeries() != null) {
				for (ExecutiveMetricsSeries series : module.getSeries()) {
					if (series.getCounts() != null && series.getCounts().size() > 2
							&& TRUE.equals(series.getCounts().get(2).getLabel().get(PRODCONF))) {
						check = true;
					}
				}
			}
		}
		if (check) {
			return "No Commits to Prod Stage";
		} else {
			return "Pipeline Not Configured properly till Production";
		}
	}

	private String checkForDataAvailabilityStatusForModules(List<ExecutiveModuleMetrics> modules) {
		boolean check = false;
		if (modules.isEmpty())
			return "Product Dashboard Not Configured";
		for (ExecutiveModuleMetrics module : modules) {
			if (module.getSeries() != null) {
				for (ExecutiveMetricsSeries series : module.getSeries()) {
					if (series.getCounts() != null) {
						if (TRUE.equals(series.getCounts().get(2).getLabel().get(PRODCONF))) {
							check = true;
						}
					} else {
						return "Module Not Configured";
					}
				}
			} else {
				return "Module Not Configured";
			}
		}
		if (check) {
			return "No Commits to Prod Stage";
		} else {
			return "Pipeline Not Configured properly till Production";
		}
	}

	private List<MetricCount> processMetricsCount(List<ExecutiveModuleMetrics> modules, String appId,
			MongoClient client) {
		List<MetricCount> metricCountResponse = new ArrayList<>();
		MetricCount metricLeadTime = new MetricCount();
		MetricCount metricCommit = new MetricCount();
		MetricCount metricCadence = new MetricCount();
		MetricCount metricCa = new MetricCount();
		MetricCount checkConfigData = new MetricCount();
		boolean checkConfig = false;
		long leadTime = 0;
		long commits = 0;
		long count = 0;
		if (!modules.isEmpty()) {
			for (ExecutiveModuleMetrics module : modules) {
				if (module.getSeries() != null) {
					for (ExecutiveMetricsSeries series : module.getSeries()) {
						if (series.getDaysAgo() == 90) {
							List<SeriesCount> seriesCount = series.getCounts();
							SeriesCount leadTimeSeries = seriesCount.get(0);
							leadTime += leadTimeSeries.getCount();
							if (leadTimeSeries.getCount() > 0)
								count++;

							SeriesCount commitSeries = seriesCount.get(1);
							commits += commitSeries.getCount();

							if (seriesCount.size() > 2 && seriesCount.get(2) != null) {
								SeriesCount confCheck = seriesCount.get(2);
								if (TRUE.equals(confCheck.getLabel().get(PRODCONF)))
									checkConfig = true;
							}
						}
					}
				}
			}
			Map<String, String> label1 = new HashMap<>();
			label1.put("type", LEADTIME);
			metricLeadTime.setLabel(label1);

			Map<String, String> label2 = new HashMap<>();
			label2.put("type", COMMIT);
			metricCommit.setLabel(label2);

			if (!modules.isEmpty() && count > 0) {
				metricLeadTime.setValue(leadTime / count);
				metricCommit.setValue(commits);
			}
			metricCountResponse.add(metricLeadTime);
			metricCountResponse.add(metricCommit);

			if (commits == 0 && !checkConfig) {
				Map<String, String> label3 = new HashMap<>();
				label3.put(PRODCONF, FALSE);
				checkConfigData.setLabel(label3);
				checkConfigData.setValue(0);
				metricCountResponse.add(checkConfigData);
			} else {
				Map<String, String> label3 = new HashMap<>();
				label3.put(PRODCONF, "true");
				checkConfigData.setLabel(label3);
				checkConfigData.setValue(0);
				metricCountResponse.add(checkConfigData);
			}

			Map<String, String> label4 = new HashMap<>();
			label4.put("type", CADENCE);
			metricCadence.setLabel(label4);
			metricCadence.setValue(serviceNowAnalysis.getDeploymentCadence(appId, client));
			metricCountResponse.add(metricCadence);

			Map<String, String> label5 = new HashMap<>();
			label5.put("type", CACTIVITY);
			metricCa.setLabel(label5);
			metricCa.setValue(serviceNowAnalysis.getChangeActivity(appId, client));
			metricCountResponse.add(metricCa);

		}
		return metricCountResponse;

	}

	private List<MetricCount> getCountsForModules(ExecutiveModuleMetrics module) {
		List<MetricCount> metricCountResponse = new ArrayList<>();
		MetricCount metricLeadTime = new MetricCount();
		MetricCount metricCommit = new MetricCount();
		MetricCount checkConfigData = new MetricCount();
		List<ExecutiveMetricsSeries> metricSeries = module.getSeries();
		if (metricSeries != null) {
			for (ExecutiveMetricsSeries series : metricSeries) {
				if (series.getDaysAgo() == 90) {
					List<SeriesCount> seriesCount = series.getCounts();
					SeriesCount leadTimeSeries = seriesCount.get(0);
					Map<String, String> label1 = new HashMap<>();
					label1.put("type", LEADTIME);
					metricLeadTime.setLabel(label1);
					metricLeadTime.setValue(leadTimeSeries.getCount());

					SeriesCount commitSeries = seriesCount.get(1);
					Map<String, String> label2 = new HashMap<>();
					label2.put("type", COMMIT);
					metricCommit.setLabel(label2);
					metricCommit.setValue(commitSeries.getCount());

					if (seriesCount.get(2) != null) {

						Map<String, String> label3 = new HashMap<>();
						label3.put(PRODCONF, seriesCount.get(2).getLabel().get(PRODCONF));
						checkConfigData.setLabel(label3);
						checkConfigData.setValue(0);
					}

				}
			}
			metricCountResponse.add(metricLeadTime);
			metricCountResponse.add(metricCommit);
			metricCountResponse.add(checkConfigData);
		} else {

			Map<String, String> label1 = new HashMap<>();
			label1.put("type", LEADTIME);
			metricLeadTime.setLabel(label1);
			metricLeadTime.setValue(0);

			Map<String, String> label2 = new HashMap<>();
			label2.put("type", COMMIT);
			metricCommit.setLabel(label2);
			metricCommit.setValue(0);

			metricCountResponse.add(metricLeadTime);
			metricCountResponse.add(metricCommit);
		}

		return metricCountResponse;
	}

	/**
	 * ThroughPutAnalysis processComponentDetailsMetrics()
	 * building_block_components Collection
	 * 
	 * @param ...
	 * @return Boolean
	 */
	public Boolean processComponentDetailsMetrics() {
		LOG.info("Processing ThroughPut Details : building_block_components . . . . ");
		Query query = new Query(new Criteria().where(METRICSNAME).is(PIPELINEMETRICS));
		List<String> appIds = mongoTemplate.getCollection(EXECUTIVECOLLECTION).distinct(APPID, query.getQueryObject());
		if (!appIds.isEmpty()) {
			for (String appId : appIds) {
				ExecutiveComponents executiveComponent = executiveComponentRepository.findByAppIdAndMetric(appId,
						PIPELINEMETRICS);
				List<BuildingBlocks> response = buildingBlocksRepository.findByMetricLevelIdAndMetricLevelAndMetricType(appId,
						MetricLevel.COMPONENT, MetricType.PIPELINE_LEAD_TIME);
				List<BuildingBlocks> buildingBlockResponse = new ArrayList<>();

				ApplicationDetails appDetails = applicationDetailsRepository.findByAppId(appId);
				List<ExecutiveMetrics> metrics = executiveComponent.getMetrics();
				if (!metrics.isEmpty()) {
					for (ExecutiveMetrics metric : metrics) {
						if (metric.getMetricsName().equalsIgnoreCase(PIPELINEMETRICS)) {
							List<ExecutiveModuleMetrics> modules = metric.getModules();
							if (!modules.isEmpty()) {
								for (ExecutiveModuleMetrics module : modules) {
									BuildingBlocks summaryResponse = new BuildingBlocks();
									summaryResponse.setMetricLevelId(appId);
									summaryResponse.setMetrics(processComponentMetrics(module));
									summaryResponse.setName(module.getModuleName());
									summaryResponse.setTotalComponents(1);
									summaryResponse.setTotalExpectedMetrics(1);
									summaryResponse.setMetricType(MetricType.PIPELINE_LEAD_TIME);
									summaryResponse.setMetricLevel(MetricLevel.COMPONENT);
									if (appDetails != null) {
										summaryResponse.setLob(appDetails.getLob());
										summaryResponse.setPoc(appDetails.getPoc());
										summaryResponse.setUrl(appDetails.getTeamBoardLink());
									}
									buildingBlockResponse.add(summaryResponse);
								}
							}
						}
						if (response != null)
							buildingBlocksRepository.delete(response);

						buildingBlocksRepository.save(buildingBlockResponse);
					}
				}
			}
		}
		LOG.info("Completed ThroughPut Details : building_block_components . . . . ");
		return true;
	}

	private List<MetricSummary> processComponentMetrics(ExecutiveModuleMetrics module) {
		List<MetricSummary> metricSummaryResponseList = new ArrayList<>();
		List<ExecutiveModuleMetrics> modules = new ArrayList<>();
		MetricSummary metricSummaryResponse = new MetricSummary();
		metricSummaryResponse.setName(PIPELINEMETRICS);
		metricSummaryResponse.setTotalComponents(1);
		metricSummaryResponse.setReportingComponents(1);
		metricSummaryResponse.setLastScanned(module.getLastScanned());
		metricSummaryResponse.setLastUpdated(module.getLastUpdated());
		modules.add(module);
		metricSummaryResponse.setCounts(getCountsForModules(module));
		Boolean dataAvailability = checkForDataAvailability(metricSummaryResponse.getCounts());
		metricSummaryResponse.setDataAvailable(dataAvailability);
		if (!dataAvailability)
			metricSummaryResponse.setConfMessage(checkForDataAvailabilityStatusForModules(modules));
		metricSummaryResponseList.add(metricSummaryResponse);

		return metricSummaryResponseList;
	}

	private Long getLong(Object value) {
		if (value != null)
			return Long.parseLong(value.toString());
		return (long) 0;
	}

	private Double getTrendSlopesForModules(List<ExecutiveModuleMetrics> modules) {
		Map<Integer, Long> seriesList = new HashMap<>();
		if (!modules.isEmpty()) {
			for (ExecutiveModuleMetrics executiveModuleMetrics : modules) {
				List<ExecutiveMetricsSeries> executiveMetricsSeriesList = executiveModuleMetrics.getSeries();
				if (executiveMetricsSeriesList != null && !executiveMetricsSeriesList.isEmpty()) {
					for (ExecutiveMetricsSeries executiveMetricsSeries : executiveMetricsSeriesList) {
						int days = executiveMetricsSeries.getDaysAgo();
						if (executiveMetricsSeries.getCounts() != null) {
							Long count = executiveMetricsSeries.getCounts().get(0).getCount();
							if (seriesList.containsKey(days)) {
								count += seriesList.get(days);
								seriesList.replace(days, count);
							} else {
								seriesList.put(days, count);
							}
						}
					}
				}
			}

			if (!seriesList.isEmpty()) {
				Map<Long, Long> mappedValue = new HashMap<>();
				for (Entry<Integer, Long> entry : seriesList.entrySet()) {
					mappedValue.put(getTimeStamp(entry.getKey()), entry.getValue());
				}
				if (mappedValue.size() > 1) {
					double[] timestamp = new double[mappedValue.size()];
					double[] count = new double[mappedValue.size()];
					int i = 0;
					for (Map.Entry<Long, Long> entry : mappedValue.entrySet()) {
						count[i] = (double) entry.getValue();
						timestamp[i] = (double) entry.getKey();
						i++;
					}
					LinearRegression lr = new LinearRegression(timestamp, count);

					return lr.slope();
				}
			}
		}
		return (double) 0;
	}

	private Long getTimeStamp(int days) {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calendar.add(Calendar.DAY_OF_MONTH, -days);
		return calendar.getTimeInMillis();
	}

	private Double getTrendSlope(List<ExecutiveMetricsSeries> execMetricsSeriesList) {
		Map<Long, Integer> mappedValue = new HashMap<>();
		if (execMetricsSeriesList != null) {

			for (ExecutiveMetricsSeries seris : execMetricsSeriesList) {
				long timestamp = seris.getTimeStamp();
				int counting = 0;
				List<SeriesCount> counts = seris.getCounts();
				if (counts != null && !counts.isEmpty()) {
					for (SeriesCount count : counts) {
						counting += count.getCount();
					}
				}
				mappedValue.put(timestamp, counting);
			}
			if (mappedValue.size() > 1) {
				double[] timestamp = new double[mappedValue.size()];
				double[] count = new double[mappedValue.size()];
				int i = 0;
				for (Map.Entry<Long, Integer> entry : mappedValue.entrySet()) {
					count[i] = (double) entry.getValue();
					timestamp[i] = (double) entry.getKey();
					i++;
				}
				LinearRegression lr = new LinearRegression(timestamp, count);

				return lr.slope();
			}
		}
		return (double) 0;
	}

	/**
	 * ThroughPutAnalysis removeUnusedThroughPutDetails()
	 * 
	 * @return Boolean
	 **/
	public Boolean removeUnusedThroughPutDetails() {
		LOG.info("Removing Unused ThroughPut Details : executives_metrics . . . . ");
		MongoClient client = null;
		try {
			client = throughPutDAO.getMongoClient();
			List<String> appIdList = vastDetailsDAO.getAllAppIds(client);
			if (appIdList != null && !appIdList.isEmpty()) {
				List<ExecutiveComponents> securityDataList = executiveComponentRepository.getNotUsedAppIdList(appIdList,
						PIPELINEMETRICS);
				if (securityDataList != null)
					executiveComponentRepository.delete(securityDataList);
			}
		} catch (Exception e) {
			LOG.info("Error inside removeUnusedThroughPutDetails " + e);
		} finally {
			if (client != null)
				client.close();
		}
		LOG.info("Completed Removing Unused ThroughPut Details : executives_metrics . . . . ");
		return true;
	}

}
