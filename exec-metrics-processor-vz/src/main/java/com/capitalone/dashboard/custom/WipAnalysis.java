package com.capitalone.dashboard.custom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.collector.MetricsProcessorSettings;
import com.capitalone.dashboard.dao.VastDetailsDAO;
import com.capitalone.dashboard.dao.WipDetailsDAO;
import com.capitalone.dashboard.exec.model.MetricCount;
import com.capitalone.dashboard.exec.model.MetricLevel;
import com.capitalone.dashboard.exec.model.MetricSummary;
import com.capitalone.dashboard.exec.model.MetricTimeSeriesElement;
import com.capitalone.dashboard.exec.model.MetricType;
import com.capitalone.dashboard.exec.model.ApplicationDetails;
import com.capitalone.dashboard.exec.model.BuildingBlocks;
import com.capitalone.dashboard.exec.model.CollectorStatus;
import com.capitalone.dashboard.exec.model.CollectorType;
import com.capitalone.dashboard.exec.model.ExecutiveComponents;
import com.capitalone.dashboard.exec.model.ExecutiveMetrics;
import com.capitalone.dashboard.exec.model.ExecutiveMetricsSeries;
import com.capitalone.dashboard.exec.model.ExecutiveModuleMetrics;
import com.capitalone.dashboard.exec.model.ExecutiveSummaryList;
import com.capitalone.dashboard.exec.model.FeatureUserStory;
import com.capitalone.dashboard.exec.model.JiraDetailsFinal;
import com.capitalone.dashboard.exec.model.JiraInfo;
import com.capitalone.dashboard.exec.model.MetricsDetail;
import com.capitalone.dashboard.exec.model.PortfolioResponse;
import com.capitalone.dashboard.exec.model.SeriesCount;
import com.capitalone.dashboard.exec.repository.ApplicationDetailsRepository;
import com.capitalone.dashboard.exec.repository.BuildingBlocksRepository;
import com.capitalone.dashboard.exec.repository.CollectorStatusRepository;
import com.capitalone.dashboard.exec.repository.ExecutiveComponentRepository;
import com.capitalone.dashboard.exec.repository.ExecutiveSummaryListRepository;
import com.capitalone.dashboard.exec.repository.MetricsDetailRepository;
import com.capitalone.dashboard.exec.repository.PortfolioResponseRepository;
import com.capitalone.dashboard.utils.GenericMethods;
import com.capitalone.dashboard.utils.LinearRegression;
import com.mongodb.MongoClient;

/**
 * WipAnalysis
 * 
 * @param
 * @return
 * @author Guru
 */
@Component
@SuppressWarnings("PMD")
public class WipAnalysis implements MetricsProcessor {

	private final WipDetailsDAO wipDetailsDAO;
	private final VastDetailsDAO vastDetailsDAO;
	private final ExecutiveComponentRepository executiveComponentRepository;
	private final ExecutiveSummaryListRepository executiveSummaryListRepository;
	private final MetricsDetailRepository metricsDetailRepository;
	private final ApplicationDetailsRepository applicationDetailsRepository;
	private final BuildingBlocksRepository buildingBlocksRepository;
	private final MongoTemplate mongoTemplate;
	private final CollectorStatusRepository collectorStatusRepository;
	private final PortfolioResponseRepository portfolioResponseRepository;
	private final GenericMethods genericMethods;
	private final MetricsProcessorSettings metricsProcessorSettings;
	private static final Logger LOG = LoggerFactory.getLogger(WipAnalysis.class);

	private static final String WIP = "work-in-progress";
	private static final String APPID = "appId";
	private static final String METRICSNAME = "metrics.metricsName";
	private static final String EXECUTIVEMETRICS = "executives_metrics";
	private static final String TYPE = "type";
	private static final String STORY = "Story";
	private static final String VZAGILESTORY = "VZAgile Story";
	private static final String OTHER = "Other";
	private static final String BUGS = "Bugs";
	private static final String BUG = "Bug";
	private static final String EPIC = "Epic";
	private static final String STORYCOUNT = "storyCount";
	private static final String EPICCOUNT = "epicCount";
	private static final String BUGCOUNT = "bugCount";
	private static final String OTHERCOUNT = "otherCount";
	private static final String STORYONE = "Story One";
	private static final String OTHERONE = "Other One";
	private static final String BUGSONE = "Bugs One";
	private static final String EPICONE = "Epic One";
	private static final String STORYTWO = "Story Two";
	private static final String OTHERTWO = "Other Two";
	private static final String BUGSTWO = "Bugs Two";
	private static final String EPICTWO = "Epic Two";
	private static final String STORYTHREE = "Story Three";
	private static final String OTHERTHREE = "Other Three";
	private static final String BUGSTHREE = "Bugs Three";
	private static final String EPICTHREE = "Epic Three";
	public static final String ISO_DATE_TIME_FORMATZ = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS";

	/**
	 * WipAnalysis
	 * 
	 * @param wipDetailsDAO
	 * @param executiveComponentRepository
	 * @param executiveSummaryListRepository
	 * @param metricsDetailRepository
	 * @param applicationDetailsRepository
	 * @param buildingBlocksRepository
	 * @param mongoTemplate
	 * @param collectorStatusRepository
	 * @param portfolioResponseRepository
	 * @param vastDetailsDAO
	 * @param genericMethods
	 * @param metricsProcessorSettings
	 * @return
	 */
	@Autowired
	public WipAnalysis(WipDetailsDAO wipDetailsDAO, ExecutiveComponentRepository executiveComponentRepository,
			ExecutiveSummaryListRepository executiveSummaryListRepository,
			MetricsDetailRepository metricsDetailRepository, ApplicationDetailsRepository applicationDetailsRepository,
			BuildingBlocksRepository buildingBlocksRepository, MongoTemplate mongoTemplate,
			CollectorStatusRepository collectorStatusRepository,
			PortfolioResponseRepository portfolioResponseRepository, VastDetailsDAO vastDetailsDAO,
			GenericMethods genericMethods, MetricsProcessorSettings metricsProcessorSettings) {
		this.wipDetailsDAO = wipDetailsDAO;
		this.executiveComponentRepository = executiveComponentRepository;
		this.executiveSummaryListRepository = executiveSummaryListRepository;
		this.metricsDetailRepository = metricsDetailRepository;
		this.applicationDetailsRepository = applicationDetailsRepository;
		this.buildingBlocksRepository = buildingBlocksRepository;
		this.mongoTemplate = mongoTemplate;
		this.collectorStatusRepository = collectorStatusRepository;
		this.portfolioResponseRepository = portfolioResponseRepository;
		this.vastDetailsDAO = vastDetailsDAO;
		this.genericMethods = genericMethods;
		this.metricsProcessorSettings = metricsProcessorSettings;
	}

	/**
	 * processWipDetails
	 * 
	 * @return Boolean
	 */

	public Boolean processExecutiveMetricsDetails() {
		LOG.info("Processing WIP Details : executives_metrics . . . . ");
		MongoClient client = null;
		try {
			client = wipDetailsDAO.getMongoClient();
			List<String> appIds = wipDetailsDAO.getEntireAppList(client);
			for (String appId : appIds) {
				ApplicationDetails appDetails = applicationDetailsRepository.findByAppId(appId);
				ExecutiveComponents execComponents = executiveComponentRepository.findByAppIdAndMetric(appId, WIP);
				if (execComponents == null)
					execComponents = new ExecutiveComponents();
				execComponents.setMetrics(processAppMetrics(client, appId));
				execComponents.setAppId(appId);
				if (appDetails != null) {
					execComponents.setAppName(appDetails.getAppName());
					execComponents.setTeamBoardLink(appDetails.getTeamBoardLink());
				}
				executiveComponentRepository.save(execComponents);
			}
		} catch (Exception e) {
			LOG.info("Error in WIP Analysis :: " + e);
		} finally {
			if (client != null)
				client.close();
		}
		LOG.info("Completed WIP Details : executives_metrics . . . . ");
		return true;
	}

	/**
	 *
	 * processAppMetrics
	 * 
	 * @param appId
	 * @param client
	 * @return List<ExecutiveMetrics>
	 */

	private List<ExecutiveMetrics> processAppMetrics(MongoClient client, String appId) {
		Date lastUpdated = null;
		CollectorStatus collectorStatus = collectorStatusRepository.findByType(CollectorType.MetricsProcessor);
		if (collectorStatus != null)
			lastUpdated = collectorStatus.getLastUpdated();

		List<ExecutiveMetrics> execMetricsList = new ArrayList<>();
		List<ExecutiveModuleMetrics> modules = new ArrayList<>();
		ExecutiveMetrics executiveMetrics = new ExecutiveMetrics();
		executiveMetrics.setLastScanned(lastUpdated);
		executiveMetrics.setLastUpdated(getISODateTime(System.currentTimeMillis()));
		executiveMetrics.setMetricsName(WIP);

		List<JiraDetailsFinal> jiraDetailsList = wipDetailsDAO.getEntireProjectList(client, appId);
		for (JiraDetailsFinal jiraDetails : jiraDetailsList) {
			List<JiraInfo> jiraInfoList = jiraDetails.getJiraInfo();
			for (JiraInfo jiraInfo : jiraInfoList) {
				String projectName = jiraInfo.getProjectName();
				String projectKey = jiraInfo.getProjectKey();
				ExecutiveModuleMetrics executiveModuleMetrics = getModuleMetrics(projectName, projectKey, client);
				modules.add(executiveModuleMetrics);
			}
		}
		executiveMetrics.setTrendSlope(wipDetailsDAO.getTrendSlopesForModules(modules));
		executiveMetrics.setModules(modules);
		execMetricsList.add(executiveMetrics);
		return execMetricsList;
	}

	/**
	 *
	 * getModuleMetrics
	 * 
	 * @param projectName
	 * @param client
	 * @return ExecutiveModuleMetrics
	 */

	private ExecutiveModuleMetrics getModuleMetrics(String projectName, String projectKey, MongoClient client) {

		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		long todayDate = calendar.getTimeInMillis();
		calendar.add(Calendar.DAY_OF_MONTH, -30);
		long oneMonthAgoDate = calendar.getTimeInMillis();
		calendar.add(Calendar.DAY_OF_MONTH, -30);
		long twoMonthsAgoDate = calendar.getTimeInMillis();
		calendar.add(Calendar.DAY_OF_MONTH, -30);
		long threeMonthsAgoDate = calendar.getTimeInMillis();

		ExecutiveModuleMetrics execModuleMetrics = new ExecutiveModuleMetrics();
		List<ExecutiveMetricsSeries> execMetricsSeriesList = new ArrayList<>();

		ExecutiveMetricsSeries execMetricsSeries1 = getSeriesCountList(todayDate, oneMonthAgoDate, 30, projectName,
				client);
		ExecutiveMetricsSeries execMetricsSeries2 = getSeriesCountList(oneMonthAgoDate, twoMonthsAgoDate, 60,
				projectName, client);
		ExecutiveMetricsSeries execMetricsSeries3 = getSeriesCountList(twoMonthsAgoDate, threeMonthsAgoDate, 90,
				projectName, client);

		execMetricsSeriesList.add(execMetricsSeries1);
		execMetricsSeriesList.add(execMetricsSeries2);
		execMetricsSeriesList.add(execMetricsSeries3);

		execModuleMetrics.setModuleName(projectName);
		execModuleMetrics.setTeamId(projectKey);
		execModuleMetrics.setLastScanned(getLastCreatedDateForModule(projectName, client));
		execModuleMetrics.setLastUpdated(getISODateTime(System.currentTimeMillis()));
		execModuleMetrics.setTrendSlope(getTrendSlope(execMetricsSeriesList));
		execModuleMetrics.setSeries(execMetricsSeriesList);

		return execModuleMetrics;
	}

	/**
	 * getSeriesCountList
	 * 
	 * @param startDate
	 * @param endDate
	 * @param interval
	 * @param projectName
	 * @param client
	 * @return
	 */
	public ExecutiveMetricsSeries getSeriesCountList(long startDate, long endDate, int interval, String projectName,
			MongoClient client) {

		ExecutiveMetricsSeries rtExecMetricsSeries = new ExecutiveMetricsSeries();

		SeriesCount epicCount = new SeriesCount();
		SeriesCount storyCount = new SeriesCount();
		SeriesCount bugCount = new SeriesCount();
		SeriesCount otherCount = new SeriesCount();
		List<SeriesCount> seriesCountList = new ArrayList<>();

		Map<String, String> epicCountLabel = new HashMap<>();
		Map<String, String> storyCountLabel = new HashMap<>();
		Map<String, String> bugCountLabel = new HashMap<>();
		Map<String, String> otherCountLabel = new HashMap<>();

		storyCountLabel.put(TYPE, STORY);
		epicCountLabel.put(TYPE, EPIC);
		bugCountLabel.put(TYPE, BUGS);
		otherCountLabel.put(TYPE, OTHER);

		List<FeatureUserStory> userStoryList = wipDetailsDAO.getUserStoriesList(projectName, startDate, endDate,
				client);
		Map<String, Object> wipMap = getWipDetailsMap(userStoryList);

		storyCount.setCount((long) wipMap.get(STORYCOUNT));
		storyCount.setLabel(storyCountLabel);
		epicCount.setCount((long) wipMap.get(EPICCOUNT));
		epicCount.setLabel(epicCountLabel);
		bugCount.setCount((long) wipMap.get(BUGCOUNT));
		bugCount.setLabel(bugCountLabel);
		otherCount.setCount((long) wipMap.get(OTHERCOUNT));
		otherCount.setLabel(otherCountLabel);

		seriesCountList.add(storyCount);
		seriesCountList.add(epicCount);
		seriesCountList.add(bugCount);
		seriesCountList.add(otherCount);

		rtExecMetricsSeries.setCounts(seriesCountList);
		rtExecMetricsSeries.setTimeStamp(endDate);
		rtExecMetricsSeries.setDaysAgo(interval);

		return rtExecMetricsSeries;
	}

	/**
	 * getWipDetailsMap
	 * 
	 * @param userStoryList
	 * @return Map
	 */

	private Map<String, Object> getWipDetailsMap(List<FeatureUserStory> userStoryList) {
		long epicCount = 0;
		long storyCount = 0;
		long bugCount = 0;
		long otherCount = 0;
		Map<String, Object> countMap = new HashMap<>();

		if (userStoryList != null && !userStoryList.isEmpty()) {
			Iterator<FeatureUserStory> featureUserStoryItr = userStoryList.iterator();
			while (featureUserStoryItr.hasNext()) {
				FeatureUserStory featureUserStory = featureUserStoryItr.next();
				switch (featureUserStory.getsTypeName()) {

				case EPIC:
					epicCount++;
					break;

				case STORY:
					storyCount++;
					break;

				case VZAGILESTORY:
					storyCount++;
					break;

				case BUG:
					bugCount++;
					break;

				default:
					otherCount++;
					break;

				}
			}
		}
		countMap.put(STORYCOUNT, storyCount);
		countMap.put(EPICCOUNT, epicCount);
		countMap.put(BUGCOUNT, bugCount);
		countMap.put(OTHERCOUNT, otherCount);
		return countMap;
	}

	/**
	 * removeUnusedWipDetails
	 * 
	 * @return Boolean
	 **/

	public Boolean removeUnusedWipDetails() {
		LOG.info("Removing Unused Wip Details : executives_metrics . . . . ");
		MongoClient client = null;
		try {
			client = wipDetailsDAO.getMongoClient();
			List<String> appIdList = vastDetailsDAO.getAllAppIds(client);
			if (appIdList != null && !appIdList.isEmpty()) {
				List<ExecutiveComponents> securityDataList = executiveComponentRepository.getNotUsedAppIdList(appIdList,
						WIP);
				if (securityDataList != null)
					executiveComponentRepository.delete(securityDataList);
			}
		} catch (Exception e) {
			LOG.info("Error inside removeUnusedWipDetails " + e);
		} finally {
			if (client != null)
				client.close();
		}
		LOG.info("Completed Removing Unused Wip Details : executives_metrics . . . . ");
		return true;
	}

	/**
	 * WipAnalysis Processing WIP Details : app_metrics_details
	 * 
	 * @return
	 */
	public Boolean processMetricsDetailResponse() {
		LOG.info("Processing WIP Details : app_metrics_details . . . . ");
		Query query = new Query(new Criteria().where(METRICSNAME).is(WIP));
		List<String> appIds = mongoTemplate.getCollection(EXECUTIVEMETRICS).distinct(APPID, query.getQueryObject());
		if (!appIds.isEmpty()) {
			for (String appId : appIds) {
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId, WIP);
				MetricsDetail metricDetailResponseProcessed = getMetricDetailResponse(executiveComponents);
				MetricsDetail metricDetailResponseStored = metricsDetailRepository.findByMetricLevelIdAndLevelAndType(appId,
						MetricLevel.PRODUCT, MetricType.WORK_IN_PROGRESS);
				if (metricDetailResponseStored != null) {
					metricsDetailRepository.delete(metricDetailResponseStored);
				}
				metricsDetailRepository.save(metricDetailResponseProcessed);
			}
		}
		LOG.info("Completed WIP Details : app_metrics_details . . . . ");
		return true;

	}

	/**
	 *
	 * getMetricDetailResponse
	 * 
	 * @param executiveComponents
	 * @return MetricsDetail
	 */

	private MetricsDetail getMetricDetailResponse(ExecutiveComponents executiveComponents) {
		MetricsDetail metricDetailResponse = new MetricsDetail();
		metricDetailResponse.setMetricLevelId(executiveComponents.getAppId());
		metricDetailResponse.setType(MetricType.WORK_IN_PROGRESS);
		metricDetailResponse.setLevel(MetricLevel.PRODUCT);
		metricDetailResponse.setSummary(processMetricsSummary(executiveComponents));
		metricDetailResponse.setTimeSeries(processTimeSeries(executiveComponents));

		return metricDetailResponse;
	}

	/**
	 *
	 * processTimeSeries
	 * 
	 * @param executiveComponents
	 * @return List<MetricTimeSeriesElement>
	 */

	private List<MetricTimeSeriesElement> processTimeSeries(ExecutiveComponents executiveComponents) {
		List<MetricTimeSeriesElement> timeSeries = new ArrayList<>();
		MetricTimeSeriesElement timeSeries30 = timeSeriesDataForApp(executiveComponents, 30);
		MetricTimeSeriesElement timeSeries60 = timeSeriesDataForApp(executiveComponents, 60);
		MetricTimeSeriesElement timeSeries90 = timeSeriesDataForApp(executiveComponents, 90);
		timeSeries.add(timeSeries30);
		timeSeries.add(timeSeries60);
		timeSeries.add(timeSeries90);

		return timeSeries;
	}

	/**
	 *
	 * timeSeriesDataForApp
	 * 
	 * @param executiveComponents
	 * @param daysAgo
	 * @return MetricTimeSeriesElement
	 */

	private MetricTimeSeriesElement timeSeriesDataForApp(ExecutiveComponents executiveComponents, int daysAgo) {
		MetricTimeSeriesElement metricTimeSeriesElementResponse = new MetricTimeSeriesElement();
		try {
			List<ExecutiveModuleMetrics> modules = new ArrayList<>();
			List<String> teamIds = new ArrayList<>();
			List<ExecutiveModuleMetrics> execModuleMetricList = executiveComponents.getMetrics().get(0).getModules();
			List<MetricCount> metricCountResponse = new ArrayList<>();

			MetricCount metricStory = new MetricCount();
			MetricCount metricEpic = new MetricCount();
			MetricCount metricBug = new MetricCount();
			MetricCount metricOther = new MetricCount();

			long storyCount = 0;
			long epicCount = 0;
			long bugCount = 0;
			long otherCount = 0;

			List<ExecutiveMetrics> executiveMetricsList = executiveComponents.getMetrics();
			if (executiveMetricsList != null) {
				List<ExecutiveModuleMetrics> modulesList = executiveMetricsList.get(0).getModules();
				for (ExecutiveModuleMetrics executiveModuleMetrics : modulesList) {
					if (!teamIds.contains(executiveModuleMetrics.getModuleName())) {
						teamIds.add(executiveModuleMetrics.getModuleName());
						modules.add(executiveModuleMetrics);
					}
				}
			}

			for (ExecutiveModuleMetrics module : modules) {
				List<ExecutiveMetricsSeries> execMetricSeries = module.getSeries();
				for (ExecutiveMetricsSeries series : execMetricSeries) {
					if (series.getDaysAgo() == daysAgo) {
						List<SeriesCount> seriesCountList = series.getCounts();

						SeriesCount storySeries = seriesCountList.get(0);
						metricStory.setLabel(storySeries.getLabel());
						storyCount += storySeries.getCount();

						SeriesCount epicSeries = seriesCountList.get(1);
						metricEpic.setLabel(epicSeries.getLabel());
						epicCount += epicSeries.getCount();

						SeriesCount bugSeries = seriesCountList.get(2);
						metricBug.setLabel(bugSeries.getLabel());
						bugCount += bugSeries.getCount();

						SeriesCount otherSeries = seriesCountList.get(3);
						metricOther.setLabel(otherSeries.getLabel());
						otherCount += otherSeries.getCount();
					}
				}
			}
			if (!execModuleMetricList.isEmpty()) {
				metricStory.setValue(storyCount);
				metricEpic.setValue(epicCount);
				metricBug.setValue(bugCount);
				metricOther.setValue(otherCount);
			}
			metricCountResponse.add(metricStory);
			metricCountResponse.add(metricEpic);
			metricCountResponse.add(metricBug);
			metricCountResponse.add(metricOther);

			metricTimeSeriesElementResponse.setDaysAgo(daysAgo);
			metricTimeSeriesElementResponse.setCounts(metricCountResponse);
		} catch (Exception e) {
			LOG.info("ERROR in WIPAnalysis File - timeSeriesDataForApp() : " + e);
		}
		return metricTimeSeriesElementResponse;
	}

	/**
	 *
	 * processMetricsSummary
	 * 
	 * @param executiveComponents
	 * @return MetricSummary
	 */

	private MetricSummary processMetricsSummary(ExecutiveComponents executiveComponents) {

		MetricSummary metricSummaryResponse = new MetricSummary();
		List<ExecutiveModuleMetrics> modules = new ArrayList<>();
		List<String> teamIds = new ArrayList<>();
		metricSummaryResponse.setLastUpdated(executiveComponents.getMetrics().get(0).getLastUpdated());
		metricSummaryResponse.setLastScanned(executiveComponents.getMetrics().get(0).getLastScanned());
		metricSummaryResponse.setName(WIP);

		if (executiveComponents.getMetrics() != null) {
			List<ExecutiveMetrics> metrics = executiveComponents.getMetrics();
			for (ExecutiveMetrics metric : metrics) {
				if (metric.getMetricsName().equalsIgnoreCase(WIP)) {
					List<ExecutiveModuleMetrics> modulesList = metric.getModules();
					for (ExecutiveModuleMetrics executiveModuleMetrics : modulesList) {
						if (!teamIds.contains(executiveModuleMetrics.getModuleName())) {
							teamIds.add(executiveModuleMetrics.getModuleName());
							modules.add(executiveModuleMetrics);
						}
					}
					metricSummaryResponse.setCounts(processMetricSummaryCounts(modules));
					metricSummaryResponse.setReportingComponents(modules.size());
					metricSummaryResponse.setTotalComponents(modulesList.size());
					metricSummaryResponse.setTrendSlope(executiveComponents.getMetrics().get(0).getTrendSlope());
					metricSummaryResponse
							.setAppCriticality(genericMethods.processAppCriticality(executiveComponents.getAppId()));
					Boolean dataAvailability = checkForDataAvailability(metricSummaryResponse.getCounts());
					metricSummaryResponse.setDataAvailable(dataAvailability);
					if (!dataAvailability)
						metricSummaryResponse.setConfMessage(checkForDataAvailabilityStatus(modules));
				}
			}
		}
		return metricSummaryResponse;
	}

	/**
	 * WipAnalysis Processing WIP Details : building_block_metrics
	 * 
	 * @return
	 */

	public Boolean processBuildingBlockMetrics() {
		LOG.info("Processing WIP Details : building_block_metrics . . . . ");
		try {
			Query query = new Query(new Criteria().where(METRICSNAME).is(WIP));
			List<String> appIds = mongoTemplate.getCollection(EXECUTIVEMETRICS).distinct(APPID, query.getQueryObject());
			List<BuildingBlocks> buildingBlockMetricSummaryList = new ArrayList<>();
			if (!appIds.isEmpty()) {
				for (String appId : appIds) {
					BuildingBlocks buildingBlockMetricSummary = buildingBlocksRepository.findByMetricLevelIdAndMetricLevel(appId,
							MetricLevel.PRODUCT);
					if (buildingBlockMetricSummary == null)
						buildingBlockMetricSummary = new BuildingBlocks();

					ApplicationDetails appDetails = applicationDetailsRepository.findByAppId(appId);
					MetricsDetail metricDetailResponse = metricsDetailRepository.findByMetricLevelIdAndLevelAndType(appId,
							MetricLevel.PRODUCT, MetricType.WORK_IN_PROGRESS);
					if (metricDetailResponse != null && appDetails != null) {
						buildingBlockMetricSummary.setMetricLevelId(appId);
						buildingBlockMetricSummary.setLob(appDetails.getLob());
						buildingBlockMetricSummary.setName(appDetails.getAppName());
						buildingBlockMetricSummary.setMetricLevel(MetricLevel.PRODUCT);
						buildingBlockMetricSummary
								.setAppCriticality(metricDetailResponse.getSummary().getAppCriticality());
						List<MetricSummary> metricsResponseStored = buildingBlockMetricSummary.getMetrics();
						List<MetricSummary> metricsResponseProcessed = new ArrayList<>();
						if (metricsResponseStored != null && !metricsResponseStored.isEmpty()) {
							for (MetricSummary metricSummaryResponse : metricsResponseStored) {
								if (metricSummaryResponse.getName() != null
										&& !metricSummaryResponse.getName().equalsIgnoreCase(WIP))
									metricsResponseProcessed.add(metricSummaryResponse);

							}
						}
						metricsResponseProcessed.add(metricDetailResponse.getSummary());
						buildingBlockMetricSummary.setMetrics(metricsResponseProcessed);
						buildingBlockMetricSummaryList.add(buildingBlockMetricSummary);
					}
				}

				if (!buildingBlockMetricSummaryList.isEmpty())
					buildingBlocksRepository.save(buildingBlockMetricSummaryList);
			}

		} catch (Exception e) {
			LOG.info("processBuildingBlockMetrics WIP Analysis Info :: " + e);
		}
		LOG.info("Completed WIP Details : building_block_metrics . . . . ");
		return true;

	}

	/**
	 * WipAnalysis Processing WIP Details : portfolio_metrics_details .
	 * 
	 * @return
	 */

	public Boolean processExecutiveDetailsMetrics() {
		LOG.info("Processing  WIP Details : portfolio_metrics_details . . . . ");
		List<ExecutiveSummaryList> executiveSummaryLists = (List<ExecutiveSummaryList>) executiveSummaryListRepository
				.findAll();
		if (!executiveSummaryLists.isEmpty()) {
			for (ExecutiveSummaryList execSummaryList : executiveSummaryLists) {
				String eid = execSummaryList.getEid();
				MetricsDetail metricPortfolioDetailResponse = metricsDetailRepository.findByMetricLevelIdAndLevelAndType(eid,
						MetricLevel.PORTFOLIO, MetricType.WORK_IN_PROGRESS);
				if (metricPortfolioDetailResponse == null)
					metricPortfolioDetailResponse = new MetricsDetail();
				metricPortfolioDetailResponse.setType(MetricType.WORK_IN_PROGRESS);
				metricPortfolioDetailResponse.setLevel(MetricLevel.PORTFOLIO);
				metricPortfolioDetailResponse.setMetricLevelId(eid);
				metricPortfolioDetailResponse.setCustomField(getPortfolioId(eid));
				metricPortfolioDetailResponse.setSummary(processExecutiveSummary(execSummaryList.getAppId()));
				metricPortfolioDetailResponse.setTimeSeries(processExecutiveTimeSeries(execSummaryList));
				if (metricPortfolioDetailResponse.getSummary() != null)
					metricPortfolioDetailResponse.getSummary().setTotalComponents(execSummaryList.getTotalApps());
				metricsDetailRepository.save(metricPortfolioDetailResponse);
			}
		}
		LOG.info("Completed WIP Details : portfolio_metrics_details . . . . ");
		return true;
	}

	/**
	 *
	 * processExecutiveTimeSeries
	 * 
	 * @param executiveSummaryList
	 * @return List<MetricTimeSeriesElement>
	 */

	private List<MetricTimeSeriesElement> processExecutiveTimeSeries(ExecutiveSummaryList executiveSummaryList) {
		List<MetricTimeSeriesElement> timeSeries = new ArrayList<>();
		MetricTimeSeriesElement timeSeries30 = new MetricTimeSeriesElement();
		MetricTimeSeriesElement timeSeries60 = new MetricTimeSeriesElement();
		MetricTimeSeriesElement timeSeries90 = new MetricTimeSeriesElement();

		List<MetricCount> counts30 = new ArrayList<>();
		MetricCount metricStory30 = new MetricCount();
		MetricCount metricEpic30 = new MetricCount();
		MetricCount metricBug30 = new MetricCount();
		MetricCount metricOther30 = new MetricCount();
		List<MetricCount> counts60 = new ArrayList<>();
		MetricCount metricStory60 = new MetricCount();
		MetricCount metricEpic60 = new MetricCount();
		MetricCount metricBug60 = new MetricCount();
		MetricCount metricOther60 = new MetricCount();
		List<MetricCount> counts90 = new ArrayList<>();
		MetricCount metricStory90 = new MetricCount();
		MetricCount metricEpic90 = new MetricCount();
		MetricCount metricBug90 = new MetricCount();
		MetricCount metricOther90 = new MetricCount();

		long storyCount30 = 0;
		long epicCount30 = 0;
		long bugCount30 = 0;
		long otherCount30 = 0;
		long storyCount60 = 0;
		long epicCount60 = 0;
		long bugCount60 = 0;
		long otherCount60 = 0;
		long storyCount90 = 0;
		long epicCount90 = 0;
		long bugCount90 = 0;
		long otherCount90 = 0;

		List<ExecutiveModuleMetrics> modules = new ArrayList<>();
		try {
			List<String> configuredAppId = executiveSummaryList.getAppId();
			if (configuredAppId != null && !configuredAppId.isEmpty()) {
				List<String> teamIds = new ArrayList<>();
				for (String appId : configuredAppId) {
					ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
							WIP);
					if (executiveComponents != null) {
						List<ExecutiveMetrics> executiveMetricsList = executiveComponents.getMetrics();
						if (executiveMetricsList != null) {
							List<ExecutiveModuleMetrics> modulesList = executiveMetricsList.get(0).getModules();
							for (ExecutiveModuleMetrics executiveModuleMetrics : modulesList) {
								if (!teamIds.contains(executiveModuleMetrics.getModuleName())) {
									teamIds.add(executiveModuleMetrics.getModuleName());
									modules.add(executiveModuleMetrics);
								}
							}
						}
					}
				}
			}

			if (!modules.isEmpty()) {
				for (ExecutiveModuleMetrics executiveModuleMetrics : modules) {
					List<ExecutiveMetricsSeries> executiveMetricsSeriesList = executiveModuleMetrics.getSeries();
					if (!executiveMetricsSeriesList.isEmpty()) {
						for (ExecutiveMetricsSeries executiveMetricsSeries : executiveMetricsSeriesList) {
							int days = executiveMetricsSeries.getDaysAgo();
							List<SeriesCount> seriesCountList = executiveMetricsSeries.getCounts();
							switch (days) {
							case 30:
								if (!seriesCountList.isEmpty()) {
									SeriesCount storySeries = seriesCountList.get(0);
									storyCount30 += storySeries.getCount();
									SeriesCount epicSeries = seriesCountList.get(1);
									epicCount30 += epicSeries.getCount();
									SeriesCount bugSeries = seriesCountList.get(2);
									bugCount30 += bugSeries.getCount();
									SeriesCount otherSeries = seriesCountList.get(3);
									otherCount30 += otherSeries.getCount();
								}
								break;
							case 60:
								if (!seriesCountList.isEmpty()) {
									SeriesCount storySeries = seriesCountList.get(0);
									storyCount60 += storySeries.getCount();
									SeriesCount epicSeries = seriesCountList.get(1);
									epicCount60 += epicSeries.getCount();
									SeriesCount bugSeries = seriesCountList.get(2);
									bugCount60 += bugSeries.getCount();
									SeriesCount otherSeries = seriesCountList.get(3);
									otherCount60 += otherSeries.getCount();
								}
								break;
							case 90:
								if (!seriesCountList.isEmpty()) {
									SeriesCount storySeries = seriesCountList.get(0);
									storyCount90 += storySeries.getCount();
									SeriesCount epicSeries = seriesCountList.get(1);
									epicCount90 += epicSeries.getCount();
									SeriesCount bugSeries = seriesCountList.get(2);
									bugCount90 += bugSeries.getCount();
									SeriesCount otherSeries = seriesCountList.get(3);
									otherCount90 += otherSeries.getCount();
								}
								break;
							}
						}
					}
				}
			}

			Map<String, String> storyMap = new HashMap<>();
			storyMap.put(TYPE, STORY);
			metricStory30.setLabel(storyMap);
			metricStory60.setLabel(storyMap);
			metricStory90.setLabel(storyMap);
			Map<String, String> epicMap = new HashMap<>();
			epicMap.put(TYPE, EPIC);
			metricEpic30.setLabel(epicMap);
			metricEpic60.setLabel(epicMap);
			metricEpic90.setLabel(epicMap);
			Map<String, String> bugMap = new HashMap<>();
			bugMap.put(TYPE, BUGS);
			metricBug30.setLabel(bugMap);
			metricBug60.setLabel(bugMap);
			metricBug90.setLabel(bugMap);
			Map<String, String> otherMap = new HashMap<>();
			otherMap.put(TYPE, OTHER);
			metricOther30.setLabel(otherMap);
			metricOther60.setLabel(otherMap);
			metricOther90.setLabel(otherMap);

			if (configuredAppId != null) {
				metricStory30.setValue(storyCount30);
				metricEpic30.setValue(epicCount30);
				metricBug30.setValue(bugCount30);
				metricOther30.setValue(otherCount30);
				metricStory60.setValue(storyCount60);
				metricEpic60.setValue(epicCount60);
				metricBug60.setValue(bugCount60);
				metricOther60.setValue(otherCount60);
				metricStory90.setValue(storyCount90);
				metricEpic90.setValue(epicCount90);
				metricBug90.setValue(bugCount90);
				metricOther90.setValue(otherCount90);
			}

			counts30.add(metricStory30);
			counts30.add(metricEpic30);
			counts30.add(metricBug30);
			counts30.add(metricOther30);
			timeSeries30.setDaysAgo(30);
			timeSeries30.setCounts(counts30);
			counts60.add(metricStory60);
			counts60.add(metricEpic60);
			counts60.add(metricBug60);
			counts60.add(metricOther60);
			timeSeries60.setDaysAgo(60);
			timeSeries60.setCounts(counts60);
			counts90.add(metricStory90);
			counts90.add(metricEpic90);
			counts90.add(metricBug90);
			counts90.add(metricOther90);
			timeSeries90.setDaysAgo(90);
			timeSeries90.setCounts(counts90);

			timeSeries.add(timeSeries30);
			timeSeries.add(timeSeries60);
			timeSeries.add(timeSeries90);

		} catch (Exception e) {
			LOG.info("Error inside WipAnalysis file - processExecutiveTimeSeries() " + e);
		}
		return timeSeries;
	}

	/**
	 *
	 * processExecutiveSummary
	 * 
	 * @param configuredAppId
	 * @return MetricSummary
	 */

	private MetricSummary processExecutiveSummary(List<String> configuredAppId) {

		List<ExecutiveModuleMetrics> modules = new ArrayList<>();
		MetricSummary metricSummaryResponse = new MetricSummary();
		int reportingComponents = 0;
		if (configuredAppId != null && !configuredAppId.isEmpty()) {
			List<String> teamIds = new ArrayList<>();
			for (String appId : configuredAppId) {
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId, WIP);
				List<ExecutiveModuleMetrics> appLevelModules = new ArrayList<>();
				if (executiveComponents != null) {
					List<ExecutiveMetrics> executiveMetricsList = executiveComponents.getMetrics();
					if (executiveMetricsList != null) {
						List<ExecutiveModuleMetrics> modulesList = executiveMetricsList.get(0).getModules();
						for (ExecutiveModuleMetrics executiveModuleMetrics : modulesList) {
							if (!teamIds.contains(executiveModuleMetrics.getModuleName())) {
								teamIds.add(executiveModuleMetrics.getModuleName());
								appLevelModules.add(executiveModuleMetrics);
							}
						}
						if (!appLevelModules.isEmpty()) {
							reportingComponents++;
							modules.addAll(appLevelModules);
						}
					}
				}
			}
			metricSummaryResponse.setCounts(processMetricSummaryCounts(modules));
			metricSummaryResponse.setTrendSlope(wipDetailsDAO.getTrendSlopesForModules(modules));
		}
		metricSummaryResponse.setLastUpdated(new Date(System.currentTimeMillis()));
		if (collectorStatusRepository.findByType(CollectorType.JiraUserStory) != null)
			metricSummaryResponse
					.setLastScanned(collectorStatusRepository.findByType(CollectorType.JiraUserStory).getLastUpdated());
		metricSummaryResponse.setName(WIP);
		metricSummaryResponse.setDataAvailable(checkForDataAvailability(metricSummaryResponse.getCounts()));
		metricSummaryResponse.setReportingComponents(reportingComponents);
		return metricSummaryResponse;
	}

	/**
	 *
	 * processMetricSummaryCounts
	 * 
	 * @param execModuleMetricsList
	 * @return List<MetricCount>
	 */

	private List<MetricCount> processMetricSummaryCounts(List<ExecutiveModuleMetrics> execModuleMetricsList) {
		List<MetricCount> metricCountResponseList = new ArrayList<>();
		MetricCount metricStory = new MetricCount();
		MetricCount metricEpic = new MetricCount();
		MetricCount metricBug = new MetricCount();
		MetricCount metricOther = new MetricCount();
		MetricCount metricStoryOne = new MetricCount();
		MetricCount metricEpicOne = new MetricCount();
		MetricCount metricBugOne = new MetricCount();
		MetricCount metricOtherOne = new MetricCount();
		MetricCount metricStoryTwo = new MetricCount();
		MetricCount metricEpicTwo = new MetricCount();
		MetricCount metricBugTwo = new MetricCount();
		MetricCount metricOtherTwo = new MetricCount();
		MetricCount metricStoryThree = new MetricCount();
		MetricCount metricEpicThree = new MetricCount();
		MetricCount metricBugThree = new MetricCount();
		MetricCount metricOtherThree = new MetricCount();

		long storyCount = 0;
		long epicCount = 0;
		long bugCount = 0;
		long otherCount = 0;
		long storyCountOne = 0;
		long epicCountOne = 0;
		long bugCountOne = 0;
		long otherCountOne = 0;
		long storyCountTwo = 0;
		long epicCountTwo = 0;
		long bugCountTwo = 0;
		long otherCountTwo = 0;
		long storyCountThree = 0;
		long epicCountThree = 0;
		long bugCountThree = 0;
		long otherCountThree = 0;

		Map<String, String> storyLabel = new HashMap<>();
		storyLabel.put(TYPE, STORY);
		Map<String, String> epicLabel = new HashMap<>();
		epicLabel.put(TYPE, EPIC);
		Map<String, String> bugsLabel = new HashMap<>();
		bugsLabel.put(TYPE, BUGS);
		Map<String, String> otherLabel = new HashMap<>();
		otherLabel.put(TYPE, OTHER);
		Map<String, String> storyLabelOne = new HashMap<>();
		storyLabelOne.put(TYPE, STORYONE);
		Map<String, String> epicLabelOne = new HashMap<>();
		epicLabelOne.put(TYPE, EPICONE);
		Map<String, String> bugsLabelOne = new HashMap<>();
		bugsLabelOne.put(TYPE, BUGSONE);
		Map<String, String> otherLabelOne = new HashMap<>();
		otherLabelOne.put(TYPE, OTHERONE);
		Map<String, String> storyLabelTwo = new HashMap<>();
		storyLabelTwo.put(TYPE, STORYTWO);
		Map<String, String> epicLabelTwo = new HashMap<>();
		epicLabelTwo.put(TYPE, EPICTWO);
		Map<String, String> bugsLabelTwo = new HashMap<>();
		bugsLabelTwo.put(TYPE, BUGSTWO);
		Map<String, String> otherLabelTwo = new HashMap<>();
		otherLabelTwo.put(TYPE, OTHERTWO);
		Map<String, String> storyLabelThree = new HashMap<>();
		storyLabelThree.put(TYPE, STORYTHREE);
		Map<String, String> epicLabelThree = new HashMap<>();
		epicLabelThree.put(TYPE, EPICTHREE);
		Map<String, String> bugsLabelThree = new HashMap<>();
		bugsLabelThree.put(TYPE, BUGSTHREE);
		Map<String, String> otherLabelThree = new HashMap<>();
		otherLabelThree.put(TYPE, OTHERTHREE);

		if (!execModuleMetricsList.isEmpty()) {
			for (ExecutiveModuleMetrics executiveModuleMetrics : execModuleMetricsList) {
				List<ExecutiveMetricsSeries> executiveMetricsSeriesList = executiveModuleMetrics.getSeries();
				if (!executiveMetricsSeriesList.isEmpty()) {
					for (ExecutiveMetricsSeries executiveMetricsSeries : executiveMetricsSeriesList) {
						List<SeriesCount> seriesCountList = executiveMetricsSeries.getCounts();
						if (!seriesCountList.isEmpty()) {
							for (SeriesCount sc : seriesCountList) {
								if (sc.getLabel().get(TYPE).equalsIgnoreCase(STORY))
									storyCount += sc.getCount();
								if (sc.getLabel().get(TYPE).equalsIgnoreCase(EPIC))
									epicCount += sc.getCount();
								if (sc.getLabel().get(TYPE).equalsIgnoreCase(BUGS))
									bugCount += sc.getCount();
								if (sc.getLabel().get(TYPE).equalsIgnoreCase(OTHER))
									otherCount += sc.getCount();
							}

							if (executiveMetricsSeries.getDaysAgo() == 30) {
								for (SeriesCount sc : seriesCountList) {
									if (sc.getLabel().get(TYPE).equalsIgnoreCase(STORY))
										storyCountOne += sc.getCount();
									if (sc.getLabel().get(TYPE).equalsIgnoreCase(EPIC))
										epicCountOne += sc.getCount();
									if (sc.getLabel().get(TYPE).equalsIgnoreCase(BUGS))
										bugCountOne += sc.getCount();
									if (sc.getLabel().get(TYPE).equalsIgnoreCase(OTHER))
										otherCountOne += sc.getCount();
								}
							}

							if (executiveMetricsSeries.getDaysAgo() == 60) {
								for (SeriesCount sc : seriesCountList) {
									if (sc.getLabel().get(TYPE).equalsIgnoreCase(STORY))
										storyCountTwo += sc.getCount();
									if (sc.getLabel().get(TYPE).equalsIgnoreCase(EPIC))
										epicCountTwo += sc.getCount();
									if (sc.getLabel().get(TYPE).equalsIgnoreCase(BUGS))
										bugCountTwo += sc.getCount();
									if (sc.getLabel().get(TYPE).equalsIgnoreCase(OTHER))
										otherCountTwo += sc.getCount();
								}
							}

							if (executiveMetricsSeries.getDaysAgo() == 90) {
								for (SeriesCount sc : seriesCountList) {
									if (sc.getLabel().get(TYPE).equalsIgnoreCase(STORY))
										storyCountThree += sc.getCount();
									if (sc.getLabel().get(TYPE).equalsIgnoreCase(EPIC))
										epicCountThree += sc.getCount();
									if (sc.getLabel().get(TYPE).equalsIgnoreCase(BUGS))
										bugCountThree += sc.getCount();
									if (sc.getLabel().get(TYPE).equalsIgnoreCase(OTHER))
										otherCountThree += sc.getCount();
								}
							}
						}
					}
				}
			}

			metricStory.setLabel(storyLabel);
			metricEpic.setLabel(epicLabel);
			metricBug.setLabel(bugsLabel);
			metricOther.setLabel(otherLabel);
			metricStory.setValue(storyCount);
			metricEpic.setValue(epicCount);
			metricBug.setValue(bugCount);
			metricOther.setValue(otherCount);
			metricStoryOne.setLabel(storyLabelOne);
			metricEpicOne.setLabel(epicLabelOne);
			metricBugOne.setLabel(bugsLabelOne);
			metricOtherOne.setLabel(otherLabelOne);
			metricStoryOne.setValue(storyCountOne);
			metricEpicOne.setValue(epicCountOne);
			metricBugOne.setValue(bugCountOne);
			metricOtherOne.setValue(otherCountOne);
			metricStoryTwo.setLabel(storyLabelTwo);
			metricEpicTwo.setLabel(epicLabelTwo);
			metricBugTwo.setLabel(bugsLabelTwo);
			metricOtherTwo.setLabel(otherLabelTwo);
			metricStoryTwo.setValue(storyCountTwo);
			metricEpicTwo.setValue(epicCountTwo);
			metricBugTwo.setValue(bugCountTwo);
			metricOtherTwo.setValue(otherCountTwo);
			metricStoryThree.setLabel(storyLabelThree);
			metricEpicThree.setLabel(epicLabelThree);
			metricBugThree.setLabel(bugsLabelThree);
			metricOtherThree.setLabel(otherLabelThree);
			metricStoryThree.setValue(storyCountThree);
			metricEpicThree.setValue(epicCountThree);
			metricBugThree.setValue(bugCountThree);
			metricOtherThree.setValue(otherCountThree);

			metricCountResponseList.add(metricStory);
			metricCountResponseList.add(metricEpic);
			metricCountResponseList.add(metricBug);
			metricCountResponseList.add(metricOther);
			metricCountResponseList.add(metricStoryOne);
			metricCountResponseList.add(metricEpicOne);
			metricCountResponseList.add(metricBugOne);
			metricCountResponseList.add(metricOtherOne);
			metricCountResponseList.add(metricStoryTwo);
			metricCountResponseList.add(metricEpicTwo);
			metricCountResponseList.add(metricBugTwo);
			metricCountResponseList.add(metricOtherTwo);
			metricCountResponseList.add(metricStoryThree);
			metricCountResponseList.add(metricEpicThree);
			metricCountResponseList.add(metricBugThree);
			metricCountResponseList.add(metricOtherThree);
		}
		return metricCountResponseList;
	}

	/**
	 * WipAnalysis Processing WIP Details : building_block_components .
	 * 
	 * @return
	 */
	public Boolean processComponentDetailsMetrics() {
		LOG.info("Processing WIP Details : building_block_components . . . . ");
		Query query = new Query(new Criteria().where(METRICSNAME).is(WIP));
		List<String> appIds = mongoTemplate.getCollection(EXECUTIVEMETRICS).distinct(APPID, query.getQueryObject());
		if (!appIds.isEmpty()) {
			for (String appId : appIds) {
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId, WIP);
				List<BuildingBlocks> response = buildingBlocksRepository.findByMetricLevelIdAndMetricLevelAndMetricType(appId,
						MetricLevel.COMPONENT, MetricType.WORK_IN_PROGRESS);
				List<BuildingBlocks> buildingBlockResponse = new ArrayList<>();
				List<ExecutiveModuleMetrics> modules = executiveComponents.getMetrics().get(0).getModules();
				ApplicationDetails appDetails = applicationDetailsRepository.findByAppId(appId);

				if (!modules.isEmpty() && appDetails != null) {
					for (ExecutiveModuleMetrics module : modules) {
						BuildingBlocks summaryResponse = new BuildingBlocks();
						String projectKey = module.getTeamId();
						summaryResponse.setMetricLevelId(appId);
						summaryResponse.setLob(appDetails.getLob());
						summaryResponse.setMetrics(processComponentMetrics(module));
						summaryResponse.setName(module.getModuleName());
						summaryResponse.setCustomField(projectKey);
						summaryResponse.setPoc(appDetails.getPoc());
						summaryResponse.setTotalComponents(1);
						summaryResponse.setTotalExpectedMetrics(1);
						summaryResponse.setMetricType(MetricType.WORK_IN_PROGRESS);
						summaryResponse.setMetricLevel(MetricLevel.COMPONENT);
						summaryResponse.setUrl(getJiraProjectLink(projectKey));
						buildingBlockResponse.add(summaryResponse);
					}
				}
				if (response != null)
					buildingBlocksRepository.delete(response);

				buildingBlocksRepository.save(buildingBlockResponse);
			}
		}
		LOG.info("Completed WIP Details : building_block_components . . . . ");
		return true;

	}

	private String getJiraProjectLink(String projectKey) {
		if (projectKey != null)
			return metricsProcessorSettings.getJiraBaseUrl() + projectKey + metricsProcessorSettings.getWipLink();
		return null;
	}

	/**
	 *
	 * processComponentMetrics
	 * 
	 * @param module
	 * @return List<MetricSummary>
	 */

	private List<MetricSummary> processComponentMetrics(ExecutiveModuleMetrics module) {
		List<MetricSummary> metricSummaryResponseList = new ArrayList<>();
		List<ExecutiveModuleMetrics> modules = new ArrayList<>();
		MetricSummary metricSummaryResponse = new MetricSummary();
		metricSummaryResponse.setName(WIP);
		metricSummaryResponse.setTotalComponents(1);
		metricSummaryResponse.setReportingComponents(1);
		metricSummaryResponse.setLastScanned(module.getLastScanned());
		metricSummaryResponse.setLastUpdated(module.getLastUpdated());
		modules.add(module);
		metricSummaryResponse.setCounts(processMetricSummaryCounts(modules));
		Boolean dataAvailability = checkForDataAvailability(metricSummaryResponse.getCounts());
		metricSummaryResponse.setDataAvailable(dataAvailability);
		if (!dataAvailability)
			metricSummaryResponse.setConfMessage(checkForDataAvailabilityStatus(modules));
		metricSummaryResponseList.add(metricSummaryResponse);
		return metricSummaryResponseList;
	}

	/**
	 *
	 * getLastCreatedDateForModule
	 * 
	 * @param client
	 * @param projectName
	 * @return Date
	 */
	private Date getLastCreatedDateForModule(String projectName, MongoClient client) {
		FeatureUserStory userStory = wipDetailsDAO.getLatestUserStorySorted(projectName, client);
		if (userStory != null && userStory.getChangeDate() != null) {
			String creationDate = userStory.getChangeDate();
			LOG.info("WIP ::: Return Updated Date from JIRA for Module : " + projectName + " :: "
					+ fromISODateTimeFormatz(creationDate));
			return fromISODateTimeFormatz(creationDate);
		} else {
			Date lastUpdated = null;
			CollectorStatus collectorStatus = collectorStatusRepository.findByType(CollectorType.JiraUserStory);
			if (collectorStatus != null)
				lastUpdated = collectorStatus.getLastUpdated();

			LOG.info("WIP ::: Return LastUpdated Date collectorStatusRepository : " + projectName + " :: "
					+ lastUpdated);
			return lastUpdated;
		}

	}

	/**
	 *
	 * fromISODateTimeFormatz
	 * 
	 * @param isoString
	 * @return Date
	 */

	private Date fromISODateTimeFormatz(String isoString) {
		String iString = isoString;
		if (isoString != null && !isoString.isEmpty()) {
			int charIndex = iString.indexOf(".");
			if (charIndex != -1) {
				iString = iString.substring(0, charIndex);
			}
		}
		Date dt = null;
		try {
			dt = new SimpleDateFormat(ISO_DATE_TIME_FORMATZ).parse(isoString);
		} catch (ParseException e) {
			LOG.info("Parsing ISO DateTime: " + isoString, e);
		}
		return dt;
	}

	/**
	 *
	 * getISODateTime
	 * 
	 * @param lastScanned
	 * @return Date
	 */

	private Date getISODateTime(Long currentTime) {
		if (currentTime != null)
			return null;
		return new Date(currentTime);
	}

	/**
	 *
	 * getTrendSlope
	 * 
	 * @param seriesList
	 * @return double
	 */

	private double getTrendSlope(List<ExecutiveMetricsSeries> seriesList) {
		Map<Long, Integer> mappedValue = new HashMap<>();

		if (seriesList != null && !seriesList.isEmpty()) {
			for (ExecutiveMetricsSeries series : seriesList) {
				if (series.getTimeStamp() != null) {
					long timestamp = series.getTimeStamp();
					int counting = 0;
					List<SeriesCount> counts = series.getCounts();
					for (SeriesCount count : counts) {
						if (count.getLabel().get(TYPE).equalsIgnoreCase(STORY))
							counting += count.getCount();
						if (count.getLabel().get(TYPE).equalsIgnoreCase(EPIC))
							counting += count.getCount();
						if (count.getLabel().get(TYPE).equalsIgnoreCase(BUGS))
							counting += count.getCount();
						if (count.getLabel().get(TYPE).equalsIgnoreCase(OTHER))
							counting += count.getCount();
					}
					mappedValue.put(timestamp, counting);
				}
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
				LinearRegression lr = new LinearRegression(count, timestamp);

				return lr.slope();
			}
		}
		return 0;
	}

	/**
	 *
	 * checkForDataAvailabilityStatus
	 * 
	 * @param modules
	 * @return String
	 */

	private String checkForDataAvailabilityStatus(List<ExecutiveModuleMetrics> modules) {
		if (modules.isEmpty())
			return "Not Configured";
		return "No Tickets Available In Progress State";
	}

	/**
	 *
	 * checkForDataAvailability
	 * 
	 * @param counts
	 * @return Boolean
	 */

	private Boolean checkForDataAvailability(List<MetricCount> counts) {
		try {
			if (!counts.isEmpty()) {
				for (MetricCount response : counts) {
					if (response != null && response.getLabel() != null) {
						double totalCount = response.getValue();
						if (totalCount > 0)
							return true;
					}
				}
			}
		} catch (Exception e) {
			LOG.info("Error while checkForDataAvailability: " + e);
		}
		return false;
	}

	/**
	 *
	 * getPortfolioId
	 * 
	 * @param eid
	 * @return ObjectId
	 */
	private String getPortfolioId(String eid) {
		PortfolioResponse portfolioResponse = portfolioResponseRepository.findByEid(eid);
		return portfolioResponse == null ? null : portfolioResponse.getId().toString();
	}

}
