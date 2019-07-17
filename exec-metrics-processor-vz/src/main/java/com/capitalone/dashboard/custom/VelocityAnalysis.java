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
import java.util.Map.Entry;
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
import com.capitalone.dashboard.dao.VelocityDetailsDAO;
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
 * VelocityAnalysis
 * 
 * @param
 * @return
 * @author Prakash, Pranav
 */
@Component
@SuppressWarnings("PMD")
public class VelocityAnalysis implements MetricsProcessor {

	private final VastDetailsDAO vastDetailsDAO;
	private final VelocityDetailsDAO velocityDetailsDAO;
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
	private static final Logger LOG = LoggerFactory.getLogger(VelocityAnalysis.class);

	private static final String VELOCITY = "open-source-violations";
	private static final String APPID = "appId";
	private static final String METRICSNAME = "metrics.metricsName";
	private static final String EXECUTIVEMETRICS = "executives_metrics";
	private static final String TYPE = "type";
	private static final String TOTALTIME = "Total Time";
	private static final String TOTALSTORIES = "Total Stories";
	private static final String TOTALSTORYPOINTS = "Total Story Points";
	private static final String TOTALUSERSTORIES = "Total User Stories";
	private static final String TOTALENHANCEMENTS = "Total Enhancements";
	private static final String TOTALNEWFEATURES = "Total New Features";
	private static final String TOTALTIMEONE = "Total Time One";
	private static final String TOTALSTORIESONE = "Total Stories One";
	private static final String TOTALSTORYPOINTSONE = "Total Story Points One";
	private static final String TOTALUSERSTORIESONE = "Total User Stories One";
	private static final String TOTALENHANCEMENTSONE = "Total Enhancements One";
	private static final String TOTALNEWFEATURESONE = "Total New Features One";
	private static final String TOTALTIMETWO = "Total Time Two";
	private static final String TOTALSTORIESTWO = "Total Stories Two";
	private static final String TOTALSTORYPOINTSTWO = "Total Story Points Two";
	private static final String TOTALUSERSTORIESTWO = "Total User Stories Two";
	private static final String TOTALENHANCEMENTSTWO = "Total Enhancements Two";
	private static final String TOTALNEWFEATURESTWO = "Total New Features Two";
	private static final String TOTALTIMETHREE = "Total Time Three";
	private static final String TOTALSTORIESTHREE = "Total Stories Three";
	private static final String TOTALSTORYPOINTSTHREE = "Total Story Points Three";
	private static final String TOTALUSERSTORIESTHREE = "Total User Stories Three";
	private static final String TOTALENHANCEMENTSTHREE = "Total Enhancements Three";
	private static final String TOTALNEWFEATURESTHREE = "Total New Features Three";
	private static final String TOTALTIMEMAP = "OverAllTimeTaken";
	private static final String TOTALSTORIESMAP = "totalStories";
	private static final String TOTALSTORYPOINTSMAP = "totalStoryPoints";
	private static final String TOTALUSERSTORIESMAP = "totalUserStories";
	private static final String TOTALENHANCEMENTSMAP = "totalEnhancements";
	private static final String TOTALNEWFEATURESMAP = "totalNewFeatures";
	private static final String STORY = "Story";
	private static final String ENHANCEMENT = "Enhancement";
	private static final String NEWFEATURE = "New Feature";
	private static final String VZAGILESTORY = "VZAgile Story";
	private static final String TOTALUSERSTORYTIME = "Total User Story Time";
	private static final String TOTALUSERSTORYTIMEONE = "Total User Story Time One";
	private static final String TOTALUSERSTORYTIMETWO = "Total User Story Time Two";
	private static final String TOTALUSERSTORYTIMETHREE = "Total User Story Time Three";
	public static final String ISO_DATE_TIME_FORMATZ = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS";

	/**
	 * VelocityAnalysis
	 * 
	 * @param velocityDetailsDAO
	 * @param executiveComponentRepository
	 * @param executiveSummaryListRepository
	 * @param metricsDetailRepository
	 * @param executiveSummaryListRepository
	 * @param metricsDetailRepository
	 * @param applicationDetailsRepository
	 * @param buildingBlocksRepository
	 * @param collectorStatusRepository
	 * @param portfolioResponseRepository
	 * @param vastDetailsDAO
	 * @param mongoTemplate
	 * @param genericMethods
	 * @param metricsProcessorSettings
	 * @return
	 */
	@Autowired
	public VelocityAnalysis(VelocityDetailsDAO velocityDetailsDAO,
			ExecutiveComponentRepository executiveComponentRepository,
			ExecutiveSummaryListRepository executiveSummaryListRepository,
			MetricsDetailRepository metricsDetailRepository, ApplicationDetailsRepository applicationDetailsRepository,
			BuildingBlocksRepository buildingBlocksRepository, MongoTemplate mongoTemplate,
			CollectorStatusRepository collectorStatusRepository,
			PortfolioResponseRepository portfolioResponseRepository, VastDetailsDAO vastDetailsDAO,
			GenericMethods genericMethods, MetricsProcessorSettings metricsProcessorSettings) {
		this.vastDetailsDAO = vastDetailsDAO;
		this.velocityDetailsDAO = velocityDetailsDAO;
		this.executiveComponentRepository = executiveComponentRepository;
		this.executiveSummaryListRepository = executiveSummaryListRepository;
		this.metricsDetailRepository = metricsDetailRepository;
		this.applicationDetailsRepository = applicationDetailsRepository;
		this.buildingBlocksRepository = buildingBlocksRepository;
		this.mongoTemplate = mongoTemplate;
		this.collectorStatusRepository = collectorStatusRepository;
		this.portfolioResponseRepository = portfolioResponseRepository;
		this.genericMethods = genericMethods;
		this.metricsProcessorSettings = metricsProcessorSettings;
	}

	/**
	 * processVelocityDetails
	 * 
	 * @return Boolean
	 */
	public Boolean processExecutiveMetricsDetails() {
		LOG.info("Processing Velocity Details : executives_metrics . . . . ");
		MongoClient client = null;
		try {
			client = velocityDetailsDAO.getMongoClient();
			List<String> appIds = velocityDetailsDAO.getEntireAppList(client);
			for (String appId : appIds) {
				ApplicationDetails appDetails = applicationDetailsRepository.findByAppId(appId);
				ExecutiveComponents execComponents = executiveComponentRepository.findByAppIdAndMetric(appId, VELOCITY);
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
			LOG.info("Error in Velocity Analysis :: " + e);
		} finally {
			if (client != null)
				client.close();
		}
		LOG.info("Completed Velocity Details : executives_metrics . . . . ");
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
		executiveMetrics.setMetricsName(VELOCITY);

		List<JiraDetailsFinal> jiraDetailsList = velocityDetailsDAO.getEntireProjectList(client, appId);
		for (JiraDetailsFinal jiraDetails : jiraDetailsList) {
			List<JiraInfo> jiraInfoList = jiraDetails.getJiraInfo();
			for (JiraInfo jiraInfo : jiraInfoList) {
				String projectName = jiraInfo.getProjectName();
				String projectKey = jiraInfo.getProjectKey();
				ExecutiveModuleMetrics executiveModuleMetrics = getModuleMetrics(projectName, client, projectKey);
				modules.add(executiveModuleMetrics);
			}
		}
		executiveMetrics.setTrendSlope(getTrendSlopesForModules(modules));
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

	private ExecutiveModuleMetrics getModuleMetrics(String projectName, MongoClient client, String projectKey) {

		ExecutiveModuleMetrics execModuleMetrics = new ExecutiveModuleMetrics();
		List<ExecutiveMetricsSeries> execMetricsSeriesList = new ArrayList<>();

		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		long todayDate = calendar.getTimeInMillis();
		calendar.add(Calendar.DAY_OF_MONTH, -30);
		long oneMonthAgoDate = calendar.getTimeInMillis();
		calendar.add(Calendar.DAY_OF_MONTH, -30);
		long twoMonthsAgoDate = calendar.getTimeInMillis();
		calendar.add(Calendar.DAY_OF_MONTH, -30);
		long threeMonthsAgoDate = calendar.getTimeInMillis();

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
	 * @return ExecutiveMetricsSeries
	 */

	public ExecutiveMetricsSeries getSeriesCountList(long startDate, long endDate, int interval, String projectName,
			MongoClient client) {

		ExecutiveMetricsSeries rtExecMetricsSeries = new ExecutiveMetricsSeries();

		SeriesCount overAllTimeTakenCount = new SeriesCount();
		SeriesCount totalStoriesCount = new SeriesCount();
		SeriesCount totalStoryPointsCount = new SeriesCount();
		SeriesCount totalUserStoriesCount = new SeriesCount();
		SeriesCount totalEnhancementsCount = new SeriesCount();
		SeriesCount totalNewFeaturesCount = new SeriesCount();
		SeriesCount overAllTimeTakenForStoriesCount = new SeriesCount();
		List<SeriesCount> seriesCountList = new ArrayList<>();

		Map<String, String> overAllTimeTaken = new HashMap<>();
		Map<String, String> totalStories = new HashMap<>();
		Map<String, String> totalStoryPoints = new HashMap<>();
		Map<String, String> totalUserStories = new HashMap<>();
		Map<String, String> totalEnhancements = new HashMap<>();
		Map<String, String> totalNewFeatures = new HashMap<>();
		Map<String, String> overAllTimeTakenForStories = new HashMap<>();

		List<FeatureUserStory> userStoryList = velocityDetailsDAO.getUserStoriesList(projectName, startDate, endDate,
				client);
		Map<String, Object> velocityMap = getVelocityDetailsMap(userStoryList);

		List<FeatureUserStory> userStoriesNewList = velocityDetailsDAO.getUserStoryList(projectName, startDate, endDate,
				client);
		Map<String, Object> storiesMap = getVelocityDetailsMap(userStoriesNewList);

		overAllTimeTaken.put(TYPE, TOTALTIME);
		totalStories.put(TYPE, TOTALSTORIES);
		totalStoryPoints.put(TYPE, TOTALSTORYPOINTS);
		totalUserStories.put(TYPE, TOTALUSERSTORIES);
		totalEnhancements.put(TYPE, TOTALENHANCEMENTS);
		totalNewFeatures.put(TYPE, TOTALNEWFEATURES);
		overAllTimeTakenForStories.put(TYPE, TOTALUSERSTORYTIME);

		overAllTimeTakenCount.setCount((long) velocityMap.get(TOTALTIMEMAP));
		overAllTimeTakenCount.setLabel(overAllTimeTaken);
		totalStoriesCount.setCount((long) velocityMap.get(TOTALSTORIESMAP));
		totalStoriesCount.setLabel(totalStories);
		totalStoryPointsCount.setCount((long) velocityMap.get(TOTALSTORYPOINTSMAP));
		totalStoryPointsCount.setLabel(totalStoryPoints);
		totalUserStoriesCount.setCount((long) velocityMap.get(TOTALUSERSTORIESMAP));
		totalUserStoriesCount.setLabel(totalUserStories);
		totalEnhancementsCount.setCount((long) velocityMap.get(TOTALENHANCEMENTSMAP));
		totalEnhancementsCount.setLabel(totalEnhancements);
		totalNewFeaturesCount.setCount((long) velocityMap.get(TOTALNEWFEATURESMAP));
		totalNewFeaturesCount.setLabel(totalNewFeatures);
		overAllTimeTakenForStoriesCount.setCount((long) storiesMap.get(TOTALTIMEMAP));
		overAllTimeTakenForStoriesCount.setLabel(overAllTimeTakenForStories);

		seriesCountList.add(overAllTimeTakenCount);
		seriesCountList.add(totalStoriesCount);
		seriesCountList.add(totalStoryPointsCount);
		seriesCountList.add(totalUserStoriesCount);
		seriesCountList.add(totalEnhancementsCount);
		seriesCountList.add(totalNewFeaturesCount);
		seriesCountList.add(overAllTimeTakenForStoriesCount);

		rtExecMetricsSeries.setTimeStamp(endDate);
		rtExecMetricsSeries.setCounts(seriesCountList);
		rtExecMetricsSeries.setDaysAgo(interval);

		return rtExecMetricsSeries;

	}

	/**
	 * getVelocityDetailsMap
	 * 
	 * @param userStoryList
	 * @return Map
	 */

	public Map<String, Object> getVelocityDetailsMap(List<FeatureUserStory> userStoryList) {

		long key = 0;
		long userStories = 0;
		long enhancements = 0;
		long newFeatures = 0;
		Double storyPoints = Double.parseDouble("0");
		long storyPointsLong = 0;
		long totalTime = 0;
		Map<String, Object> rtMap = new HashMap<>();
		try {
			if (userStoryList != null && !userStoryList.isEmpty()) {
				Iterator<FeatureUserStory> featureUserStoryItr = userStoryList.iterator();
				while (featureUserStoryItr.hasNext()) {
					FeatureUserStory featureUserStory = featureUserStoryItr.next();
					totalTime = totalTime + featureUserStory.getTotalTimeTaken();
					if (featureUserStory.getStoryPoints() != null) {
						if (featureUserStory.getStoryPoints() > Double.parseDouble("0")) {
							storyPoints = storyPoints + featureUserStory.getStoryPoints();
							storyPointsLong = (new Double(storyPoints)).longValue();
						} else {
							storyPoints = Double.parseDouble("1");
							storyPointsLong = (new Double(storyPoints)).longValue();
						}
					}
					key++;
					String issueType = featureUserStory.getsTypeName();
					if (issueType.equalsIgnoreCase(STORY) || issueType.equalsIgnoreCase(VZAGILESTORY))
						userStories++;
					if (issueType.equalsIgnoreCase(ENHANCEMENT))
						enhancements++;
					if (issueType.equalsIgnoreCase(NEWFEATURE))
						newFeatures++;
				}
			}

			rtMap.put(TOTALTIMEMAP, totalTime / 86400000);
			rtMap.put(TOTALSTORIESMAP, key);
			rtMap.put(TOTALUSERSTORIESMAP, userStories);
			rtMap.put(TOTALENHANCEMENTSMAP, enhancements);
			rtMap.put(TOTALNEWFEATURESMAP, newFeatures);
			rtMap.put(TOTALSTORYPOINTSMAP, storyPointsLong);

		} catch (Exception e) {
			LOG.info("Error in getVelocityDetailsMap() (VelocityAnalysis Class) : " + e);
		}

		return rtMap;

	}

	/**
	 * VelocityAnalysis removeUnusedVelocityDetails()
	 * 
	 * @return Boolean
	 **/

	public Boolean removeUnusedVelocityDetails() {
		LOG.info("Removing Unused Velocity Details : executives_metrics . . . . ");
		MongoClient client = null;
		try {
			client = velocityDetailsDAO.getMongoClient();
			List<String> appIdList = vastDetailsDAO.getAllAppIds(client);
			if (appIdList != null && !appIdList.isEmpty()) {
				List<ExecutiveComponents> securityDataList = executiveComponentRepository.getNotUsedAppIdList(appIdList,
						VELOCITY);
				if (securityDataList != null)
					executiveComponentRepository.delete(securityDataList);
			}
		} catch (Exception e) {
			LOG.info("Error inside removeUnusedVelocityDetails " + e);
		} finally {
			if (client != null)
				client.close();
		}
		LOG.info("Completed Removing Unused Velocity Details : executives_metrics . . . . ");
		return true;
	}

	/**
	 * processBuildingBlockMetrics
	 * 
	 * @return Boolean
	 */
	public Boolean processBuildingBlockMetrics() {
		LOG.info("Processing Velocity Details : building_block_metrics . . . . ");
		try {
			Query query = new Query(new Criteria().where(METRICSNAME).is(VELOCITY));
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
							MetricLevel.PRODUCT, MetricType.VELOCITY);
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
								if (!metricSummaryResponse.getName().equalsIgnoreCase(VELOCITY))
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
			LOG.info("processBuildingBlockMetrics Velocity Analysis Info :: " + e);
		}
		LOG.info("Completed Velocity Details : building_block_metrics . . . . ");
		return true;
	}

	/**
	 * processMetricsDetailResponse
	 * 
	 * @return Boolean
	 */
	public Boolean processMetricsDetailResponse() {
		LOG.info("Processing Velocity Details : app_metrics_details . . . . ");
		Query query = new Query(new Criteria().where(METRICSNAME).is(VELOCITY));
		List<String> appIds = mongoTemplate.getCollection(EXECUTIVEMETRICS).distinct(APPID, query.getQueryObject());
		if (!appIds.isEmpty()) {
			for (String appId : appIds) {
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
						VELOCITY);
				MetricsDetail metricDetailResponseProcessed = getMetricDetailResponse(executiveComponents);
				MetricsDetail metricDetailResponseStored = metricsDetailRepository.findByMetricLevelIdAndLevelAndType(appId,
						MetricLevel.PRODUCT, MetricType.VELOCITY);
				if (metricDetailResponseStored != null) {
					metricsDetailRepository.delete(metricDetailResponseStored);
				}
				metricsDetailRepository.save(metricDetailResponseProcessed);
			}
		}
		LOG.info("Completed Velocity Details : app_metrics_details . . . . ");
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
		metricDetailResponse.setType(MetricType.VELOCITY);
		metricDetailResponse.setLevel(MetricLevel.PRODUCT);
		metricDetailResponse.setSummary(processMetricsSummary(executiveComponents));
		metricDetailResponse.setTimeSeries(processTimeSeries(executiveComponents));

		return metricDetailResponse;
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
		metricSummaryResponse.setName(VELOCITY);

		if (executiveComponents.getMetrics() != null) {
			List<ExecutiveMetrics> metrics = executiveComponents.getMetrics();
			for (ExecutiveMetrics metric : metrics) {
				if (metric.getMetricsName().equalsIgnoreCase(VELOCITY)) {
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
			MetricCount metricOverAllTimeTaken = new MetricCount();
			MetricCount metricTotalStories = new MetricCount();
			MetricCount metricTotalStoryPoints = new MetricCount();
			MetricCount metricTotalUserStories = new MetricCount();
			MetricCount metricTotalEnhancements = new MetricCount();
			MetricCount metricTotalNewFeatures = new MetricCount();
			MetricCount metricOverAllTimeTakenForStories = new MetricCount();
			long overAllTimeTakenCount = 0;
			long totalStoriesCount = 0;
			long totalStoryPointsCount = 0;
			long totalUserStoriesCount = 0;
			long totalEnhancementsCount = 0;
			long totalNewFeaturesCount = 0;
			long overAllTimeTakenForStoriesCount = 0;

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
						List<SeriesCount> seriesCount = series.getCounts();

						SeriesCount overAllTimeTakenSeries = seriesCount.get(0);
						metricOverAllTimeTaken.setLabel(overAllTimeTakenSeries.getLabel());
						overAllTimeTakenCount += overAllTimeTakenSeries.getCount();

						SeriesCount totalStoriesSeries = seriesCount.get(1);
						metricTotalStories.setLabel(totalStoriesSeries.getLabel());
						totalStoriesCount += totalStoriesSeries.getCount();

						SeriesCount totalStoryPointsSeries = seriesCount.get(2);
						metricTotalStoryPoints.setLabel(totalStoryPointsSeries.getLabel());
						totalStoryPointsCount += totalStoryPointsSeries.getCount();

						SeriesCount totaUserStoriesSeries = seriesCount.get(3);
						metricTotalUserStories.setLabel(totaUserStoriesSeries.getLabel());
						totalUserStoriesCount += totaUserStoriesSeries.getCount();

						SeriesCount totalEnhancementsSeries = seriesCount.get(4);
						metricTotalEnhancements.setLabel(totalEnhancementsSeries.getLabel());
						totalEnhancementsCount += totalEnhancementsSeries.getCount();

						SeriesCount totalNewFeatureSeries = seriesCount.get(5);
						metricTotalNewFeatures.setLabel(totalNewFeatureSeries.getLabel());
						totalNewFeaturesCount += totalNewFeatureSeries.getCount();

						SeriesCount overAllTimeTakenForStoriesSeries = seriesCount.get(6);
						metricOverAllTimeTakenForStories.setLabel(overAllTimeTakenForStoriesSeries.getLabel());
						overAllTimeTakenForStoriesCount += overAllTimeTakenForStoriesSeries.getCount();
					}
				}
			}

			if (!execModuleMetricList.isEmpty()) {
				metricOverAllTimeTaken.setValue(overAllTimeTakenCount);
				metricTotalStories.setValue(totalStoriesCount);
				metricTotalStoryPoints.setValue(totalStoryPointsCount);
				metricTotalUserStories.setValue(totalUserStoriesCount);
				metricTotalEnhancements.setValue(totalEnhancementsCount);
				metricTotalNewFeatures.setValue(totalNewFeaturesCount);
				metricOverAllTimeTakenForStories.setValue(overAllTimeTakenForStoriesCount);
			}
			metricCountResponse.add(metricOverAllTimeTaken);
			metricCountResponse.add(metricTotalStories);
			metricCountResponse.add(metricTotalStoryPoints);
			metricCountResponse.add(metricTotalUserStories);
			metricCountResponse.add(metricTotalEnhancements);
			metricCountResponse.add(metricTotalNewFeatures);
			metricCountResponse.add(metricOverAllTimeTakenForStories);
			metricTimeSeriesElementResponse.setDaysAgo(daysAgo);
			metricTimeSeriesElementResponse.setCounts(metricCountResponse);
		} catch (Exception e) {
			LOG.info("ERROR in VelocityAnalysis File - timeSeriesDataForApp() : " + e);
		}

		return metricTimeSeriesElementResponse;
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
		MetricCount metricOverAllTimeTaken = new MetricCount();
		MetricCount metricTotalStories = new MetricCount();
		MetricCount metricTotalStoryPoints = new MetricCount();
		MetricCount metricTotalUserStories = new MetricCount();
		MetricCount metricTotalEnhancements = new MetricCount();
		MetricCount metricTotalNewFeatures = new MetricCount();
		MetricCount metricOverAllTimeTakenOne = new MetricCount();
		MetricCount metricTotalStoriesOne = new MetricCount();
		MetricCount metricTotalStoryPointsOne = new MetricCount();
		MetricCount metricTotalUserStoriesOne = new MetricCount();
		MetricCount metricTotalEnhancementsOne = new MetricCount();
		MetricCount metricTotalNewFeaturesOne = new MetricCount();
		MetricCount metricOverAllTimeTakenTwo = new MetricCount();
		MetricCount metricTotalStoriesTwo = new MetricCount();
		MetricCount metricTotalStoryPointsTwo = new MetricCount();
		MetricCount metricTotalUserStoriesTwo = new MetricCount();
		MetricCount metricTotalEnhancementsTwo = new MetricCount();
		MetricCount metricTotalNewFeaturesTwo = new MetricCount();
		MetricCount metricOverAllTimeTakenThree = new MetricCount();
		MetricCount metricTotalStoriesThree = new MetricCount();
		MetricCount metricTotalStoryPointsThree = new MetricCount();
		MetricCount metricTotalUserStoriesThree = new MetricCount();
		MetricCount metricTotalEnhancementsThree = new MetricCount();
		MetricCount metricTotalNewFeaturesThree = new MetricCount();
		MetricCount metricOverAllTimeTakenForStories = new MetricCount();
		MetricCount metricOverAllTimeTakenForStoriesOne = new MetricCount();
		MetricCount metricOverAllTimeTakenForStoriesTwo = new MetricCount();
		MetricCount metricOverAllTimeTakenForStoriesThree = new MetricCount();

		long overAllTimeTakenCount = 0;
		long overAllTimeTakenCountForStories = 0;
		long totalStoriesCount = 0;
		long totalStoryPointsCount = 0;
		long totalUserStoriesCount = 0;
		long totalEnhancementsCount = 0;
		long totalNewFeaturesCount = 0;
		long overAllTimeTakenCountOne = 0;
		long overAllTimeTakenCountForStoriesOne = 0;
		long totalStoriesCountOne = 0;
		long totalStoryPointsCountOne = 0;
		long totalUserStoriesCountOne = 0;
		long totalEnhancementsCountOne = 0;
		long totalNewFeaturesCountOne = 0;
		long overAllTimeTakenCountTwo = 0;
		long overAllTimeTakenCountForStoriesTwo = 0;
		long totalStoriesCountTwo = 0;
		long totalStoryPointsCountTwo = 0;
		long totalUserStoriesCountTwo = 0;
		long totalEnhancementsCountTwo = 0;
		long totalNewFeaturesCountTwo = 0;
		long overAllTimeTakenCountThree = 0;
		long overAllTimeTakenCountForStoriesThree = 0;
		long totalStoriesCountThree = 0;
		long totalStoryPointsCountThree = 0;
		long totalUserStoriesCountThree = 0;
		long totalEnhancementsCountThree = 0;
		long totalNewFeaturesCountThree = 0;

		Map<String, String> totaltime = new HashMap<>();
		totaltime.put(TYPE, TOTALTIME);
		Map<String, String> totaltimeForStories = new HashMap<>();
		totaltimeForStories.put(TYPE, TOTALUSERSTORYTIME);
		Map<String, String> totalStories = new HashMap<>();
		totalStories.put(TYPE, TOTALSTORIES);
		Map<String, String> totalStoryPoints = new HashMap<>();
		totalStoryPoints.put(TYPE, TOTALSTORYPOINTS);
		Map<String, String> totalUserStories = new HashMap<>();
		totalUserStories.put(TYPE, TOTALUSERSTORIES);
		Map<String, String> totalEnhancements = new HashMap<>();
		totalEnhancements.put(TYPE, TOTALENHANCEMENTS);
		Map<String, String> totalNewFeatures = new HashMap<>();
		totalNewFeatures.put(TYPE, TOTALNEWFEATURES);
		Map<String, String> totaltimeOne = new HashMap<>();
		totaltimeOne.put(TYPE, TOTALTIMEONE);
		Map<String, String> totaltimeForStoriesOne = new HashMap<>();
		totaltimeForStoriesOne.put(TYPE, TOTALUSERSTORYTIMEONE);
		Map<String, String> totalStoriesOne = new HashMap<>();
		totalStoriesOne.put(TYPE, TOTALSTORIESONE);
		Map<String, String> totalStoryPointsOne = new HashMap<>();
		totalStoryPointsOne.put(TYPE, TOTALSTORYPOINTSONE);
		Map<String, String> totalUserStoriesOne = new HashMap<>();
		totalUserStoriesOne.put(TYPE, TOTALUSERSTORIESONE);
		Map<String, String> totalEnhancementsOne = new HashMap<>();
		totalEnhancementsOne.put(TYPE, TOTALENHANCEMENTSONE);
		Map<String, String> totalNewFeaturesOne = new HashMap<>();
		totalNewFeaturesOne.put(TYPE, TOTALNEWFEATURESONE);
		Map<String, String> totaltimeTwo = new HashMap<>();
		totaltimeTwo.put(TYPE, TOTALTIMETWO);
		Map<String, String> totaltimeForStoriesTwo = new HashMap<>();
		totaltimeForStoriesTwo.put(TYPE, TOTALUSERSTORYTIMETWO);
		Map<String, String> totalStoriesTwo = new HashMap<>();
		totalStoriesTwo.put(TYPE, TOTALSTORIESTWO);
		Map<String, String> totalStoryPointsTwo = new HashMap<>();
		totalStoryPointsTwo.put(TYPE, TOTALSTORYPOINTSTWO);
		Map<String, String> totalUserStoriesTwo = new HashMap<>();
		totalUserStoriesTwo.put(TYPE, TOTALUSERSTORIESTWO);
		Map<String, String> totalEnhancementsTwo = new HashMap<>();
		totalEnhancementsTwo.put(TYPE, TOTALENHANCEMENTSTWO);
		Map<String, String> totalNewFeaturesTwo = new HashMap<>();
		totalNewFeaturesTwo.put(TYPE, TOTALNEWFEATURESTWO);
		Map<String, String> totaltimeThree = new HashMap<>();
		totaltimeThree.put(TYPE, TOTALTIMETHREE);
		Map<String, String> totalStoriesThree = new HashMap<>();
		totalStoriesThree.put(TYPE, TOTALSTORIESTHREE);
		Map<String, String> totalStoryPointsThree = new HashMap<>();
		totalStoryPointsThree.put(TYPE, TOTALSTORYPOINTSTHREE);
		Map<String, String> totalUserStoriesThree = new HashMap<>();
		totalUserStoriesThree.put(TYPE, TOTALUSERSTORIESTHREE);
		Map<String, String> totalEnhancementsThree = new HashMap<>();
		totalEnhancementsThree.put(TYPE, TOTALENHANCEMENTSTHREE);
		Map<String, String> totalNewFeaturesThree = new HashMap<>();
		totalNewFeaturesThree.put(TYPE, TOTALNEWFEATURESTHREE);
		Map<String, String> totaltimeForStoriesThree = new HashMap<>();
		totaltimeForStoriesThree.put(TYPE, TOTALUSERSTORYTIMETHREE);

		if (!execModuleMetricsList.isEmpty()) {
			for (ExecutiveModuleMetrics execModuleMetric : execModuleMetricsList) {
				List<ExecutiveMetricsSeries> executiveMetricsSeriesList = execModuleMetric.getSeries();
				if (!executiveMetricsSeriesList.isEmpty()) {
					for (ExecutiveMetricsSeries series : executiveMetricsSeriesList) {
						List<SeriesCount> seriesCountList = series.getCounts();
						if (!seriesCountList.isEmpty()) {
							for (SeriesCount count : seriesCountList) {
								if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALTIME))
									overAllTimeTakenCount += count.getCount();
								if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALSTORIES))
									totalStoriesCount += count.getCount();
								if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALSTORYPOINTS))
									totalStoryPointsCount += count.getCount();
								if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALUSERSTORIES))
									totalUserStoriesCount += count.getCount();
								if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALENHANCEMENTS))
									totalEnhancementsCount += count.getCount();
								if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALNEWFEATURES))
									totalNewFeaturesCount += count.getCount();
								if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALUSERSTORYTIME))
									overAllTimeTakenCountForStories += count.getCount();
							}

							if (series.getDaysAgo() == 30) {
								for (SeriesCount count : seriesCountList) {
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALTIME))
										overAllTimeTakenCountOne += count.getCount();
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALSTORIES))
										totalStoriesCountOne += count.getCount();
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALSTORYPOINTS))
										totalStoryPointsCountOne += count.getCount();
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALUSERSTORIES))
										totalUserStoriesCountOne += count.getCount();
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALENHANCEMENTS))
										totalEnhancementsCountOne += count.getCount();
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALNEWFEATURES))
										totalNewFeaturesCountOne += count.getCount();
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALUSERSTORYTIME))
										overAllTimeTakenCountForStoriesOne += count.getCount();
								}

							}
							if (series.getDaysAgo() == 60) {
								for (SeriesCount count : seriesCountList) {
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALTIME))
										overAllTimeTakenCountTwo += count.getCount();
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALSTORIES))
										totalStoriesCountTwo += count.getCount();
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALSTORYPOINTS))
										totalStoryPointsCountTwo += count.getCount();
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALUSERSTORIES))
										totalUserStoriesCountTwo += count.getCount();
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALENHANCEMENTS))
										totalEnhancementsCountTwo += count.getCount();
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALNEWFEATURES))
										totalNewFeaturesCountTwo += count.getCount();
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALUSERSTORYTIME))
										overAllTimeTakenCountForStoriesTwo += count.getCount();
								}

							}
							if (series.getDaysAgo() == 90) {
								for (SeriesCount count : seriesCountList) {
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALTIME))
										overAllTimeTakenCountThree += count.getCount();
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALSTORIES))
										totalStoriesCountThree += count.getCount();
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALSTORYPOINTS))
										totalStoryPointsCountThree += count.getCount();
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALUSERSTORIES))
										totalUserStoriesCountThree += count.getCount();
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALENHANCEMENTS))
										totalEnhancementsCountThree += count.getCount();
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALNEWFEATURES))
										totalNewFeaturesCountThree += count.getCount();
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALUSERSTORYTIME))
										overAllTimeTakenCountForStoriesThree += count.getCount();
								}
							}
						}
					}
				}
			}

			metricOverAllTimeTaken.setValue(overAllTimeTakenCount);
			metricOverAllTimeTaken.setLabel(totaltime);
			metricTotalStories.setValue(totalStoriesCount);
			metricTotalStories.setLabel(totalStories);
			metricTotalStoryPoints.setValue(totalStoryPointsCount);
			metricTotalStoryPoints.setLabel(totalStoryPoints);
			metricTotalUserStories.setValue(totalUserStoriesCount);
			metricTotalUserStories.setLabel(totalUserStories);
			metricTotalEnhancements.setValue(totalEnhancementsCount);
			metricTotalEnhancements.setLabel(totalEnhancements);
			metricTotalNewFeatures.setValue(totalNewFeaturesCount);
			metricTotalNewFeatures.setLabel(totalNewFeatures);
			metricOverAllTimeTakenOne.setValue(overAllTimeTakenCountOne);
			metricOverAllTimeTakenOne.setLabel(totaltimeOne);
			metricTotalStoriesOne.setValue(totalStoriesCountOne);
			metricTotalStoriesOne.setLabel(totalStoriesOne);
			metricTotalStoryPointsOne.setValue(totalStoryPointsCountOne);
			metricTotalStoryPointsOne.setLabel(totalStoryPointsOne);
			metricTotalUserStoriesOne.setValue(totalUserStoriesCountOne);
			metricTotalUserStoriesOne.setLabel(totalUserStoriesOne);
			metricTotalEnhancementsOne.setValue(totalEnhancementsCountOne);
			metricTotalEnhancementsOne.setLabel(totalEnhancementsOne);
			metricTotalNewFeaturesOne.setValue(totalNewFeaturesCountOne);
			metricTotalNewFeaturesOne.setLabel(totalNewFeaturesOne);
			metricOverAllTimeTakenTwo.setValue(overAllTimeTakenCountTwo);
			metricOverAllTimeTakenTwo.setLabel(totaltimeTwo);
			metricTotalStoriesTwo.setValue(totalStoriesCountTwo);
			metricTotalStoriesTwo.setLabel(totalStoriesTwo);
			metricTotalStoryPointsTwo.setValue(totalStoryPointsCountTwo);
			metricTotalStoryPointsTwo.setLabel(totalStoryPointsTwo);
			metricTotalUserStoriesTwo.setValue(totalUserStoriesCountTwo);
			metricTotalUserStoriesTwo.setLabel(totalUserStoriesTwo);
			metricTotalEnhancementsTwo.setValue(totalEnhancementsCountTwo);
			metricTotalEnhancementsTwo.setLabel(totalEnhancementsTwo);
			metricTotalNewFeaturesTwo.setValue(totalNewFeaturesCountTwo);
			metricTotalNewFeaturesTwo.setLabel(totalNewFeaturesTwo);
			metricOverAllTimeTakenThree.setValue(overAllTimeTakenCountThree);
			metricOverAllTimeTakenThree.setLabel(totaltimeThree);
			metricTotalStoriesThree.setValue(totalStoriesCountThree);
			metricTotalStoriesThree.setLabel(totalStoriesThree);
			metricTotalStoryPointsThree.setValue(totalStoryPointsCountThree);
			metricTotalStoryPointsThree.setLabel(totalStoryPointsThree);
			metricTotalUserStoriesThree.setValue(totalUserStoriesCountThree);
			metricTotalUserStoriesThree.setLabel(totalUserStoriesThree);
			metricTotalEnhancementsThree.setValue(totalEnhancementsCountThree);
			metricTotalEnhancementsThree.setLabel(totalEnhancementsThree);
			metricTotalNewFeaturesThree.setValue(totalNewFeaturesCountThree);
			metricTotalNewFeaturesThree.setLabel(totalNewFeaturesThree);
			metricOverAllTimeTakenForStories.setLabel(totaltimeForStories);
			metricOverAllTimeTakenForStories.setValue(overAllTimeTakenCountForStories);
			metricOverAllTimeTakenForStoriesOne.setLabel(totaltimeForStoriesOne);
			metricOverAllTimeTakenForStoriesOne.setValue(overAllTimeTakenCountForStoriesOne);
			metricOverAllTimeTakenForStoriesTwo.setLabel(totaltimeForStoriesTwo);
			metricOverAllTimeTakenForStoriesTwo.setValue(overAllTimeTakenCountForStoriesTwo);
			metricOverAllTimeTakenForStoriesThree.setLabel(totaltimeForStoriesThree);
			metricOverAllTimeTakenForStoriesThree.setValue(overAllTimeTakenCountForStoriesThree);

			metricCountResponseList.add(metricOverAllTimeTaken);
			metricCountResponseList.add(metricTotalStories);
			metricCountResponseList.add(metricTotalStoryPoints);
			metricCountResponseList.add(metricTotalUserStories);
			metricCountResponseList.add(metricTotalEnhancements);
			metricCountResponseList.add(metricTotalNewFeatures);
			metricCountResponseList.add(metricOverAllTimeTakenOne);
			metricCountResponseList.add(metricTotalStoriesOne);
			metricCountResponseList.add(metricTotalStoryPointsOne);
			metricCountResponseList.add(metricTotalUserStoriesOne);
			metricCountResponseList.add(metricTotalEnhancementsOne);
			metricCountResponseList.add(metricTotalNewFeaturesOne);
			metricCountResponseList.add(metricOverAllTimeTakenTwo);
			metricCountResponseList.add(metricTotalStoriesTwo);
			metricCountResponseList.add(metricTotalStoryPointsTwo);
			metricCountResponseList.add(metricTotalUserStoriesTwo);
			metricCountResponseList.add(metricTotalEnhancementsTwo);
			metricCountResponseList.add(metricTotalNewFeaturesTwo);
			metricCountResponseList.add(metricOverAllTimeTakenThree);
			metricCountResponseList.add(metricTotalStoriesThree);
			metricCountResponseList.add(metricTotalStoryPointsThree);
			metricCountResponseList.add(metricTotalUserStoriesThree);
			metricCountResponseList.add(metricTotalEnhancementsThree);
			metricCountResponseList.add(metricTotalNewFeaturesThree);
			metricCountResponseList.add(metricOverAllTimeTakenForStories);
			metricCountResponseList.add(metricOverAllTimeTakenForStoriesOne);
			metricCountResponseList.add(metricOverAllTimeTakenForStoriesTwo);
			metricCountResponseList.add(metricOverAllTimeTakenForStoriesThree);
		}
		return metricCountResponseList;
	}

	/**
	 * processExecutiveDetailsMetrics
	 * 
	 * @return Boolean
	 */

	public Boolean processExecutiveDetailsMetrics() {
		LOG.info("Processing Velocity Details : portfolio_metrics_details . . . . ");
		List<ExecutiveSummaryList> executiveSummaryLists = (List<ExecutiveSummaryList>) executiveSummaryListRepository
				.findAll();
		if (!executiveSummaryLists.isEmpty()) {
			for (ExecutiveSummaryList execSummaryList : executiveSummaryLists) {
				String eid = execSummaryList.getEid();
				MetricsDetail metricPortfolioDetailResponse = metricsDetailRepository.findByMetricLevelIdAndLevelAndType(eid,
						MetricLevel.PORTFOLIO, MetricType.VELOCITY);
				if (metricPortfolioDetailResponse == null)
					metricPortfolioDetailResponse = new MetricsDetail();
				metricPortfolioDetailResponse.setType(MetricType.VELOCITY);
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
		LOG.info("Completed Velocity Details : portfolio_metrics_details . . . . ");
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
		MetricCount metricOverAllTimeTaken30 = new MetricCount();
		MetricCount metricTotalStories30 = new MetricCount();
		MetricCount metricTotalStoryPoints30 = new MetricCount();
		MetricCount metricTotalUserStories30 = new MetricCount();
		MetricCount metricTotalEnhancements30 = new MetricCount();
		MetricCount metricTotalNewFeatures30 = new MetricCount();
		List<MetricCount> counts60 = new ArrayList<>();
		MetricCount metricOverAllTimeTaken60 = new MetricCount();
		MetricCount metricTotalStories60 = new MetricCount();
		MetricCount metricTotalStoryPoints60 = new MetricCount();
		MetricCount metricTotalUserStories60 = new MetricCount();
		MetricCount metricTotalEnhancements60 = new MetricCount();
		MetricCount metricTotalNewFeatures60 = new MetricCount();
		List<MetricCount> counts90 = new ArrayList<>();
		MetricCount metricOverAllTimeTaken90 = new MetricCount();
		MetricCount metricTotalStories90 = new MetricCount();
		MetricCount metricTotalStoryPoints90 = new MetricCount();
		MetricCount metricTotalUserStories90 = new MetricCount();
		MetricCount metricTotalEnhancements90 = new MetricCount();
		MetricCount metricTotalNewFeatures90 = new MetricCount();

		long overAllTimeTakenCount30 = 0;
		long totalStoriesCount30 = 0;
		long totalStoryPointsCount30 = 0;
		long totalUserStoriesCount30 = 0;
		long totalEnhancementsCount30 = 0;
		long totalNewFeaturesCount30 = 0;
		long overAllTimeTakenCount60 = 0;
		long totalStoriesCount60 = 0;
		long totalStoryPointsCount60 = 0;
		long totalUserStoriesCount60 = 0;
		long totalEnhancementsCount60 = 0;
		long totalNewFeaturesCount60 = 0;
		long overAllTimeTakenCount90 = 0;
		long totalStoriesCount90 = 0;
		long totalStoryPointsCount90 = 0;
		long totalUserStoriesCount90 = 0;
		long totalEnhancementsCount90 = 0;
		long totalNewFeaturesCount90 = 0;

		List<ExecutiveModuleMetrics> modules = new ArrayList<>();
		try {
			List<String> configuredAppId = executiveSummaryList.getAppId();
			if (configuredAppId != null && !configuredAppId.isEmpty()) {
				List<String> teamIds = new ArrayList<>();
				for (String appId : configuredAppId) {
					ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
							VELOCITY);
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
									SeriesCount overAllTimeTakenSeries = seriesCountList.get(0);
									overAllTimeTakenCount30 += overAllTimeTakenSeries.getCount();
									SeriesCount totalStoriesSeries = seriesCountList.get(1);
									totalStoriesCount30 += totalStoriesSeries.getCount();
									SeriesCount totalStoryPointSeries = seriesCountList.get(2);
									totalStoryPointsCount30 += totalStoryPointSeries.getCount();
									SeriesCount totalUserStoriesSeries = seriesCountList.get(3);
									totalUserStoriesCount30 += totalUserStoriesSeries.getCount();
									SeriesCount totalEnhancementsSeries = seriesCountList.get(4);
									totalEnhancementsCount30 += totalEnhancementsSeries.getCount();
									SeriesCount totalNewFeaturesSeries = seriesCountList.get(5);
									totalNewFeaturesCount30 += totalNewFeaturesSeries.getCount();
								}
								break;
							case 60:
								if (!seriesCountList.isEmpty()) {
									SeriesCount overAllTimeTakenSeries = seriesCountList.get(0);
									overAllTimeTakenCount60 += overAllTimeTakenSeries.getCount();
									SeriesCount totalStoriesSeries = seriesCountList.get(1);
									totalStoriesCount60 += totalStoriesSeries.getCount();
									SeriesCount totalStoryPointSeries = seriesCountList.get(2);
									totalStoryPointsCount60 += totalStoryPointSeries.getCount();
									SeriesCount totalUserStoriesSeries = seriesCountList.get(3);
									totalUserStoriesCount60 += totalUserStoriesSeries.getCount();
									SeriesCount totalEnhancementsSeries = seriesCountList.get(4);
									totalEnhancementsCount60 += totalEnhancementsSeries.getCount();
									SeriesCount totalNewFeaturesSeries = seriesCountList.get(5);
									totalNewFeaturesCount60 += totalNewFeaturesSeries.getCount();
								}
								break;
							case 90:
								if (!seriesCountList.isEmpty()) {
									SeriesCount overAllTimeTakenSeries = seriesCountList.get(0);
									overAllTimeTakenCount90 += overAllTimeTakenSeries.getCount();
									SeriesCount totalStoriesSeries = seriesCountList.get(1);
									totalStoriesCount90 += totalStoriesSeries.getCount();
									SeriesCount totalStoryPointSeries = seriesCountList.get(2);
									totalStoryPointsCount90 += totalStoryPointSeries.getCount();
									SeriesCount totalUserStoriesSeries = seriesCountList.get(3);
									totalUserStoriesCount90 += totalUserStoriesSeries.getCount();
									SeriesCount totalEnhancementsSeries = seriesCountList.get(4);
									totalEnhancementsCount90 += totalEnhancementsSeries.getCount();
									SeriesCount totalNewFeaturesSeries = seriesCountList.get(5);
									totalNewFeaturesCount90 += totalNewFeaturesSeries.getCount();
								}
								break;
							}
						}
					}
				}
			}

			Map<String, String> timeTaken = new HashMap<>();
			timeTaken.put(TYPE, TOTALTIME);
			metricOverAllTimeTaken30.setLabel(timeTaken);
			metricOverAllTimeTaken60.setLabel(timeTaken);
			metricOverAllTimeTaken90.setLabel(timeTaken);
			Map<String, String> totalStories = new HashMap<>();
			totalStories.put(TYPE, TOTALSTORIES);
			metricTotalStories30.setLabel(totalStories);
			metricTotalStories60.setLabel(totalStories);
			metricTotalStories90.setLabel(totalStories);
			Map<String, String> storyPoints = new HashMap<>();
			storyPoints.put(TYPE, TOTALSTORYPOINTS);
			metricTotalStoryPoints30.setLabel(storyPoints);
			metricTotalStoryPoints60.setLabel(storyPoints);
			metricTotalStoryPoints90.setLabel(storyPoints);
			Map<String, String> totalUserStories = new HashMap<>();
			totalUserStories.put(TYPE, TOTALUSERSTORIES);
			metricTotalUserStories30.setLabel(totalUserStories);
			metricTotalUserStories60.setLabel(totalUserStories);
			metricTotalUserStories90.setLabel(totalUserStories);
			Map<String, String> totalEnhancements = new HashMap<>();
			totalEnhancements.put(TYPE, TOTALENHANCEMENTS);
			metricTotalEnhancements30.setLabel(totalEnhancements);
			metricTotalEnhancements60.setLabel(totalEnhancements);
			metricTotalEnhancements90.setLabel(totalEnhancements);
			Map<String, String> totalNewFeatures = new HashMap<>();
			totalNewFeatures.put(TYPE, TOTALNEWFEATURES);
			metricTotalNewFeatures30.setLabel(totalNewFeatures);
			metricTotalNewFeatures60.setLabel(totalNewFeatures);
			metricTotalNewFeatures90.setLabel(totalNewFeatures);

			if (configuredAppId != null) {
				metricOverAllTimeTaken30.setValue(overAllTimeTakenCount30);
				metricTotalStories30.setValue(totalStoriesCount30);
				metricTotalStoryPoints30.setValue(totalStoryPointsCount30);
				metricTotalUserStories30.setValue(totalUserStoriesCount30);
				metricTotalEnhancements30.setValue(totalEnhancementsCount30);
				metricTotalNewFeatures30.setValue(totalNewFeaturesCount30);
				metricOverAllTimeTaken60.setValue(overAllTimeTakenCount60);
				metricTotalStories60.setValue(totalStoriesCount60);
				metricTotalStoryPoints60.setValue(totalStoryPointsCount60);
				metricTotalUserStories60.setValue(totalUserStoriesCount60);
				metricTotalEnhancements60.setValue(totalEnhancementsCount60);
				metricTotalNewFeatures60.setValue(totalNewFeaturesCount60);
				metricOverAllTimeTaken90.setValue(overAllTimeTakenCount90);
				metricTotalStories90.setValue(totalStoriesCount90);
				metricTotalStoryPoints90.setValue(totalStoryPointsCount90);
				metricTotalUserStories90.setValue(totalUserStoriesCount90);
				metricTotalEnhancements90.setValue(totalEnhancementsCount90);
				metricTotalNewFeatures90.setValue(totalNewFeaturesCount90);

			}

			counts30.add(metricOverAllTimeTaken30);
			counts30.add(metricTotalStories30);
			counts30.add(metricTotalStoryPoints30);
			counts30.add(metricTotalUserStories30);
			counts30.add(metricTotalEnhancements30);
			counts30.add(metricTotalNewFeatures30);
			timeSeries30.setDaysAgo(30);
			timeSeries30.setCounts(counts30);
			counts60.add(metricOverAllTimeTaken60);
			counts60.add(metricTotalStories60);
			counts60.add(metricTotalStoryPoints60);
			counts60.add(metricTotalUserStories60);
			counts60.add(metricTotalEnhancements60);
			counts60.add(metricTotalNewFeatures60);
			timeSeries60.setDaysAgo(60);
			timeSeries60.setCounts(counts60);
			counts90.add(metricOverAllTimeTaken90);
			counts90.add(metricTotalStories90);
			counts90.add(metricTotalStoryPoints90);
			counts90.add(metricTotalUserStories90);
			counts90.add(metricTotalEnhancements90);
			counts90.add(metricTotalNewFeatures90);
			timeSeries90.setDaysAgo(90);
			timeSeries90.setCounts(counts90);

			timeSeries.add(timeSeries30);
			timeSeries.add(timeSeries60);
			timeSeries.add(timeSeries90);

		} catch (Exception e) {
			LOG.info("Error inside VelocityAnalysis file - processExecutiveTimeSeries() " + e);

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
		List<String> teamIds = new ArrayList<>();
		if (configuredAppId != null && !configuredAppId.isEmpty()) {
			for (String appId : configuredAppId) {
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
						VELOCITY);
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
			metricSummaryResponse.setTrendSlope(getTrendSlopesForModules(modules));
		}
		metricSummaryResponse.setLastUpdated(new Date(System.currentTimeMillis()));
		if (collectorStatusRepository.findByType(CollectorType.JiraUserStory) != null)
			metricSummaryResponse
					.setLastScanned(collectorStatusRepository.findByType(CollectorType.JiraUserStory).getLastUpdated());
		metricSummaryResponse.setName(VELOCITY);
		metricSummaryResponse.setDataAvailable(checkForDataAvailability(metricSummaryResponse.getCounts()));
		metricSummaryResponse.setReportingComponents(reportingComponents);
		return metricSummaryResponse;
	}

	/**
	 * processComponentDetailsMetrics
	 * 
	 * @return Boolean
	 */
	public Boolean processComponentDetailsMetrics() {
		LOG.info("Processing Velocity Details : building_block_components . . . . ");
		Query query = new Query(new Criteria().where(METRICSNAME).is(VELOCITY));
		List<String> appIds = mongoTemplate.getCollection(EXECUTIVEMETRICS).distinct(APPID, query.getQueryObject());
		if (!appIds.isEmpty()) {
			for (String appId : appIds) {
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
						VELOCITY);
				List<BuildingBlocks> response = buildingBlocksRepository.findByMetricLevelIdAndMetricLevelAndMetricType(appId,
						MetricLevel.COMPONENT, MetricType.VELOCITY);
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
						summaryResponse.setUrl(getJiraProjectLink(projectKey));
						summaryResponse.setMetricLevel(MetricLevel.COMPONENT);
						summaryResponse.setMetricType(MetricType.VELOCITY);
						buildingBlockResponse.add(summaryResponse);
					}
				}
				if (response != null)
					buildingBlocksRepository.delete(response);

				buildingBlocksRepository.save(buildingBlockResponse);
			}
		}
		LOG.info("Completed Velocity Details : building_block_components . . . . ");
		return true;
	}

	private String getJiraProjectLink(String projectKey) {
		if (projectKey != null)
			return metricsProcessorSettings.getJiraBaseUrl() + projectKey + metricsProcessorSettings.getVelocityLink();
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
		if (module.getSeries() != null && !module.getSeries().isEmpty()) {
			MetricSummary metricSummaryResponse = new MetricSummary();
			metricSummaryResponse.setName(VELOCITY);
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
		}
		return metricSummaryResponseList;
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
						if (response.getLabel().get(TYPE).equalsIgnoreCase(TOTALSTORIES) && response.getValue() > 0)
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
	 * checkForDataAvailabilityStatus
	 * 
	 * @param modules
	 * @return String
	 */

	private String checkForDataAvailabilityStatus(List<ExecutiveModuleMetrics> modules) {
		if (modules.isEmpty())
			return "Not Configured";
		return "No User Stories Moved to Production";
	}

	/**
	 *
	 * getTrendSlopesForModules
	 * 
	 * @param modules
	 * @return double
	 */

	private Double getTrendSlopesForModules(List<ExecutiveModuleMetrics> modules) {
		Map<Integer, Long> seriesList = new HashMap<>();
		if (!modules.isEmpty()) {
			for (ExecutiveModuleMetrics executiveModuleMetrics : modules) {
				List<ExecutiveMetricsSeries> executiveMetricsSeriesList = executiveModuleMetrics.getSeries();
				if (!executiveMetricsSeriesList.isEmpty()) {
					for (ExecutiveMetricsSeries executiveMetricsSeries : executiveMetricsSeriesList) {
						int days = executiveMetricsSeries.getDaysAgo();
						Long velocity = (long) 0;
						Long countTotalTime = (long) 0;
						Long countTotalStories = (long) 0;
						Long countTotalStoryPoints = (long) 0;
						if (executiveMetricsSeries.getCounts() != null
								&& !executiveMetricsSeries.getCounts().isEmpty()) {
							if (!seriesList.containsKey(days)) {
								List<SeriesCount> countList = executiveMetricsSeries.getCounts();
								for (SeriesCount count : countList) {
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALTIME))
										countTotalTime += count.getCount();
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALSTORIES))
										countTotalStories += count.getCount();
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALSTORYPOINTS))
										countTotalStoryPoints += count.getCount();
									if (countTotalStoryPoints == 0 && countTotalStories != 0)
										velocity += countTotalTime / countTotalStories;
									else if (countTotalStoryPoints == 0 && countTotalStories == 0)
										velocity += (long) 0;
									else
										velocity += countTotalTime / countTotalStoryPoints;
								}
								seriesList.put(days, velocity);
							} else {
								List<SeriesCount> countList = executiveMetricsSeries.getCounts();
								velocity += seriesList.get(days);
								for (SeriesCount count : countList) {
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALTIME))
										countTotalTime += count.getCount();
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALSTORIES))
										countTotalStories += count.getCount();
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALSTORYPOINTS))
										countTotalStoryPoints += count.getCount();
									if (countTotalStoryPoints == 0 && countTotalStories != 0)
										velocity += countTotalTime / countTotalStories;
									else if (countTotalStoryPoints == 0 && countTotalStories == 0)
										velocity += (long) 0;
									else
										velocity += countTotalTime / countTotalStoryPoints;
								}
								seriesList.replace(days, velocity);
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

	/**
	 *
	 * getTrendSlope
	 * 
	 * @param seriesList
	 * @return double
	 */

	private double getTrendSlope(List<ExecutiveMetricsSeries> seriesList) {
		Map<Long, Integer> mappedValue = new HashMap<>();
		if (seriesList != null) {
			for (ExecutiveMetricsSeries executiveMetricsSeries : seriesList) {
				long timestamp = executiveMetricsSeries.getTimeStamp();
				int velocity = 0;
				Long countTotalTime = (long) 0;
				Long countTotalStories = (long) 0;
				Long countTotalStoryPoints = (long) 0;
				if (executiveMetricsSeries.getCounts() != null) {
					List<SeriesCount> countList = executiveMetricsSeries.getCounts();
					for (SeriesCount count : countList) {
						if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALTIME))
							countTotalTime += count.getCount();
						if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALSTORIES))
							countTotalStories += count.getCount();
						if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALSTORYPOINTS))
							countTotalStoryPoints += count.getCount();
						if (countTotalStoryPoints == 0 && countTotalStories != 0)
							velocity += (int) (countTotalTime / countTotalStories);
						else if (countTotalStoryPoints == 0 && countTotalStories == 0)
							velocity += 0;
						else
							velocity += (int) (countTotalTime / countTotalStoryPoints);

					}
					mappedValue.put(timestamp, velocity);
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
	 * getLastCreatedDateForModule
	 * 
	 * @param client
	 * @param projectName
	 * @return Date
	 */

	private Date getLastCreatedDateForModule(String projectName, MongoClient client) {
		FeatureUserStory userStory = velocityDetailsDAO.getLatestUserStorySorted(projectName, client);
		if (userStory != null && userStory.getChangeDate() != null) {
			String creationDate = userStory.getChangeDate();
			LOG.info("Velocity ::: Return LastUpdated Date from JIRA for Module : " + projectName + " :: "
					+ creationDate);
			return fromISODateTimeFormatz(creationDate);
		} else {
			Date lastUpdated = null;
			CollectorStatus collectorStatus = collectorStatusRepository.findByType(CollectorType.JiraUserStory);
			if (collectorStatus != null)
				lastUpdated = collectorStatus.getLastUpdated();

			LOG.info("Velocity :::  Return LastUpdated Date from CollectorStatus for Module : " + projectName + " :: "
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
	 * getTimeStamp
	 * 
	 * @param days
	 * @return Long
	 */

	private Long getTimeStamp(int days) {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calendar.add(Calendar.DAY_OF_MONTH, -days);
		return calendar.getTimeInMillis();
	}

	/**
	 *
	 * getISODateTime
	 * 
	 * @param lastScanned
	 * @return Date
	 */

	private Date getISODateTime(Long lastScanned) {
		if (lastScanned == null)
			return null;
		return new Date(lastScanned);
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
