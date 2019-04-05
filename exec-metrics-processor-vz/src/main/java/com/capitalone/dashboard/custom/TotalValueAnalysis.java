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

import com.capitalone.dashboard.collector.MetricsProcessorSettings;
import com.capitalone.dashboard.dao.VelocityDetailsDAO;
import com.capitalone.dashboard.exec.model.MetricCount;
import com.capitalone.dashboard.exec.model.MetricLevel;
import com.capitalone.dashboard.exec.model.MetricSummary;
import com.capitalone.dashboard.exec.model.MetricTimeSeriesElement;
import com.capitalone.dashboard.exec.model.MetricType;
import com.capitalone.dashboard.exec.model.vz.ApplicationDetails;
import com.capitalone.dashboard.exec.model.vz.BuildingBlocks;
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
import com.capitalone.dashboard.exec.repository.vz.ExecutiveComponentRepository;
import com.capitalone.dashboard.exec.repository.vz.ExecutiveSummaryListRepository;
import com.capitalone.dashboard.exec.repository.vz.MetricsDetailRepository;
import com.capitalone.dashboard.exec.repository.vz.PortfolioResponseRepository;
import com.capitalone.dashboard.utils.GenericMethods;
import com.capitalone.dashboard.utils.LinearRegression;

/**
 * TotalValueAnalysis
 * 
 * @param
 * @return
 * @author pranav
 */
@Component
@SuppressWarnings("PMD")
public class TotalValueAnalysis implements MetricsProcessor {

	private final ExecutiveComponentRepository executiveComponentRepository;
	private final ExecutiveSummaryListRepository executiveSummaryListRepository;
	private final MetricsDetailRepository metricsDetailRepository;
	private final ApplicationDetailsRepository applicationDetailsRepository;
	private final BuildingBlocksRepository buildingBlocksRepository;
	private final MongoTemplate mongoTemplate;
	private final PortfolioResponseRepository portfolioResponseRepository;
	private final GenericMethods genericMethods;
	private final MetricsProcessorSettings metricsProcessorSettings;
	private static final Logger LOG = LoggerFactory.getLogger(TotalValueAnalysis.class);

	private static final String VELOCITY = "open-source-violations";
	private static final String TOTALVALUE = "total-value";
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

	private static final String TOTALUSERSTORYTIME = "Total User Story Time";

	/**
	 * TotalValueAnalysis
	 * 
	 * @param velocityDetailsDAO
	 * @param executiveComponentRepository
	 * @param mongoTemplate
	 * @param executiveSummaryListRepository
	 * @param metricsDetailRepository
	 * @param applicationDetailsRepository
	 * @param buildingBlocksRepository
	 * @param portfolioResponseRepository
	 * @param genericMethods
	 * @param metricsProcessorSettings
	 * @return
	 */
	@Autowired
	public TotalValueAnalysis(VelocityDetailsDAO velocityDetailsDAO,
			ExecutiveComponentRepository executiveComponentRepository,
			ExecutiveSummaryListRepository executiveSummaryListRepository,
			MetricsDetailRepository metricsDetailRepository, ApplicationDetailsRepository applicationDetailsRepository,
			BuildingBlocksRepository buildingBlocksRepository, MongoTemplate mongoTemplate,
			PortfolioResponseRepository portfolioResponseRepository, GenericMethods genericMethods,
			MetricsProcessorSettings metricsProcessorSettings) {
		this.executiveComponentRepository = executiveComponentRepository;
		this.executiveSummaryListRepository = executiveSummaryListRepository;
		this.metricsDetailRepository = metricsDetailRepository;
		this.applicationDetailsRepository = applicationDetailsRepository;
		this.buildingBlocksRepository = buildingBlocksRepository;
		this.mongoTemplate = mongoTemplate;
		this.portfolioResponseRepository = portfolioResponseRepository;
		this.genericMethods = genericMethods;
		this.metricsProcessorSettings = metricsProcessorSettings;
	}

	/**
	 * processBuildingBlockMetrics
	 * 
	 * @return Boolean
	 */
	public Boolean processBuildingBlockMetrics() {
		LOG.info("Processing Total Value Details : building_block_metrics . . . . ");
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
							MetricLevel.PRODUCT, MetricType.TOTAL_VALUE);
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
								if (!metricSummaryResponse.getName().equalsIgnoreCase(TOTALVALUE))
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
			LOG.info("processBuildingBlockMetrics Total Value Analysis Info :: " + e);
		}
		LOG.info("Completed Total Value Details : building_block_metrics . . . . ");
		return true;
	}

	/**
	 * processMetricsDetailResponse
	 * 
	 * @return Boolean
	 */
	public Boolean processMetricsDetailResponse() {
		LOG.info("Processing Total Value Details : app_metrics_details . . . . ");
		Query query = new Query(new Criteria().where(METRICSNAME).is(VELOCITY));
		List<String> appIds = mongoTemplate.getCollection(EXECUTIVEMETRICS).distinct(APPID, query.getQueryObject());
		if (!appIds.isEmpty()) {
			for (String appId : appIds) {
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
						VELOCITY);
				MetricsDetail metricDetailResponseProcessed = getMetricDetailResponse(executiveComponents);
				MetricsDetail metricDetailResponseStored = metricsDetailRepository.findByMetricLevelIdAndLevelAndType(appId,
						MetricLevel.PRODUCT, MetricType.TOTAL_VALUE);
				if (metricDetailResponseStored != null) {
					metricsDetailRepository.delete(metricDetailResponseStored);
				}
				metricsDetailRepository.save(metricDetailResponseProcessed);
			}
		}
		LOG.info("Completed Total Value Details : app_metrics_details . . . . ");
		return true;
	}

	private MetricsDetail getMetricDetailResponse(ExecutiveComponents executiveComponents) {
		MetricsDetail metricDetailResponse = new MetricsDetail();
		metricDetailResponse.setMetricLevelId(executiveComponents.getAppId());
		metricDetailResponse.setType(MetricType.TOTAL_VALUE);
		metricDetailResponse.setLevel(MetricLevel.PRODUCT);
		metricDetailResponse.setSummary(processMetricsSummary(executiveComponents));
		metricDetailResponse.setTimeSeries(processTimeSeries(executiveComponents));

		return metricDetailResponse;
	}

	private MetricSummary processMetricsSummary(ExecutiveComponents executiveComponents) {

		MetricSummary metricSummaryResponse = new MetricSummary();
		List<ExecutiveModuleMetrics> modules = new ArrayList<>();
		List<String> teamIds = new ArrayList<>();
		metricSummaryResponse.setLastUpdated(executiveComponents.getMetrics().get(0).getLastUpdated());
		metricSummaryResponse.setLastScanned(executiveComponents.getMetrics().get(0).getLastScanned());
		metricSummaryResponse.setName(TOTALVALUE);

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
					metricSummaryResponse.setTrendSlope(getTrendSlopesForModules(modulesList));
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

	private Boolean checkForDataAvailability(List<MetricCount> counts) {
		try {
			if (!counts.isEmpty()) {
				for (MetricCount response : counts) {
					if (response != null && response.getLabel() != null) {
						if (response.getLabel().get(TYPE).equalsIgnoreCase(TOTALUSERSTORIES) && response.getValue() > 0)
							return true;
					}
				}
			}
		} catch (Exception e) {
			LOG.info("Error while checkForDataAvailability: " + e);
		}
		return false;
	}

	private String checkForDataAvailabilityStatus(List<ExecutiveModuleMetrics> modules) {
		if (modules.isEmpty())
			return "Not Configured";
		return "No Features Moved to Production";
	}

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
			long overAllTimeTakenCount = 0;
			long totalStoriesCount = 0;
			long totalStoryPointsCount = 0;
			long totalUserStoriesCount = 0;
			long totalEnhancementsCount = 0;
			long totalNewFeaturesCount = 0;

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

						SeriesCount overAllTimeTakenSeries = seriesCount.get(6);
						metricOverAllTimeTaken.setLabel(overAllTimeTakenSeries.getLabel());
						overAllTimeTakenCount += overAllTimeTakenSeries.getCount();
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
			}
			metricCountResponse.add(metricOverAllTimeTaken);
			metricCountResponse.add(metricTotalStories);
			metricCountResponse.add(metricTotalStoryPoints);
			metricCountResponse.add(metricTotalUserStories);
			metricCountResponse.add(metricTotalEnhancements);
			metricCountResponse.add(metricTotalNewFeatures);
			metricTimeSeriesElementResponse.setDaysAgo(daysAgo);
			metricTimeSeriesElementResponse.setCounts(metricCountResponse);
		} catch (Exception e) {

			LOG.info("ERROR in TotalValueAnalysis File - timeSeriesDataForApp() : " + e);

		}
		return metricTimeSeriesElementResponse;
	}

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

		long overAllTimeTakenCount = 0;
		long totalStoriesCount = 0;
		long totalStoryPointsCount = 0;
		long totalUserStoriesCount = 0;
		long totalEnhancementsCount = 0;
		long totalNewFeaturesCount = 0;

		long overAllTimeTakenCountOne = 0;
		long totalStoriesCountOne = 0;
		long totalStoryPointsCountOne = 0;
		long totalUserStoriesCountOne = 0;
		long totalEnhancementsCountOne = 0;
		long totalNewFeaturesCountOne = 0;

		long overAllTimeTakenCountTwo = 0;
		long totalStoriesCountTwo = 0;
		long totalStoryPointsCountTwo = 0;
		long totalUserStoriesCountTwo = 0;
		long totalEnhancementsCountTwo = 0;
		long totalNewFeaturesCountTwo = 0;

		long overAllTimeTakenCountThree = 0;
		long totalStoriesCountThree = 0;
		long totalStoryPointsCountThree = 0;
		long totalUserStoriesCountThree = 0;
		long totalEnhancementsCountThree = 0;
		long totalNewFeaturesCountThree = 0;

		Map<String, String> totaltime = new HashMap<>();
		totaltime.put(TYPE, TOTALTIME);
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

		if (!execModuleMetricsList.isEmpty()) {
			for (ExecutiveModuleMetrics execModuleMetric : execModuleMetricsList) {
				List<ExecutiveMetricsSeries> executiveMetricsSeriesList = execModuleMetric.getSeries();
				if (!executiveMetricsSeriesList.isEmpty()) {
					for (ExecutiveMetricsSeries series : executiveMetricsSeriesList) {
						List<SeriesCount> seriesCountList = series.getCounts();
						if (!seriesCountList.isEmpty()) {
							for (SeriesCount count : seriesCountList) {
								if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALUSERSTORYTIME)) {
									overAllTimeTakenCount += count.getCount();
								}
								if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALSTORIES)) {
									totalStoriesCount += count.getCount();
								}
								if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALSTORYPOINTS)) {
									totalStoryPointsCount += count.getCount();
								}
								if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALUSERSTORIES)) {
									totalUserStoriesCount += count.getCount();
								}
								if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALENHANCEMENTS)) {
									totalEnhancementsCount += count.getCount();
								}
								if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALNEWFEATURES)) {
									totalNewFeaturesCount += count.getCount();
								}
							}

							if (series.getDaysAgo() == 30) {
								for (SeriesCount count : seriesCountList) {
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALUSERSTORYTIME)) {
										overAllTimeTakenCountOne += count.getCount();
									}
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALSTORIES)) {
										totalStoriesCountOne += count.getCount();
									}
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALSTORYPOINTS)) {
										totalStoryPointsCountOne += count.getCount();
									}
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALUSERSTORIES)) {
										totalUserStoriesCountOne += count.getCount();
									}
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALENHANCEMENTS)) {
										totalEnhancementsCountOne += count.getCount();
									}
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALNEWFEATURES)) {
										totalNewFeaturesCountOne += count.getCount();
									}
								}

							}
							if (series.getDaysAgo() == 60) {
								for (SeriesCount count : seriesCountList) {
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALUSERSTORYTIME)) {
										overAllTimeTakenCountTwo += count.getCount();
									}
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALSTORIES)) {
										totalStoriesCountTwo += count.getCount();
									}
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALSTORYPOINTS)) {
										totalStoryPointsCountTwo += count.getCount();
									}
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALUSERSTORIES)) {
										totalUserStoriesCountTwo += count.getCount();
									}
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALENHANCEMENTS)) {
										totalEnhancementsCountTwo += count.getCount();
									}
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALNEWFEATURES)) {
										totalNewFeaturesCountTwo += count.getCount();
									}
								}

							}
							if (series.getDaysAgo() == 90) {
								for (SeriesCount count : seriesCountList) {
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALUSERSTORYTIME)) {
										overAllTimeTakenCountThree += count.getCount();
									}
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALSTORIES)) {
										totalStoriesCountThree += count.getCount();
									}
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALSTORYPOINTS)) {
										totalStoryPointsCountThree += count.getCount();
									}
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALUSERSTORIES)) {
										totalUserStoriesCountThree += count.getCount();
									}
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALENHANCEMENTS)) {
										totalEnhancementsCountThree += count.getCount();
									}
									if (count.getLabel().get(TYPE).equalsIgnoreCase(TOTALNEWFEATURES)) {
										totalNewFeaturesCountThree += count.getCount();
									}
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
		}
		return metricCountResponseList;
	}

	/**
	 * processExecutiveDetailsMetrics
	 * 
	 * @return Boolean
	 */
	public Boolean processExecutiveDetailsMetrics() {
		LOG.info("Processing Total Value Details : portfolio_metrics_details . . . . ");
		List<ExecutiveSummaryList> executiveSummaryLists = (List<ExecutiveSummaryList>) executiveSummaryListRepository
				.findAll();
		if (!executiveSummaryLists.isEmpty()) {

			for (ExecutiveSummaryList execSummaryList : executiveSummaryLists) {
				String eid = execSummaryList.getEid();
				MetricsDetail metricPortfolioDetailResponse = metricsDetailRepository.findByMetricLevelIdAndLevelAndType(eid,
						MetricLevel.PORTFOLIO, MetricType.TOTAL_VALUE);
				if (metricPortfolioDetailResponse == null)
					metricPortfolioDetailResponse = new MetricsDetail();
				metricPortfolioDetailResponse.setType(MetricType.TOTAL_VALUE);
				metricPortfolioDetailResponse.setMetricLevelId(eid);
				metricPortfolioDetailResponse.setLevel(MetricLevel.PORTFOLIO);
				metricPortfolioDetailResponse.setCustomField(getPortfolioId(eid));
				metricPortfolioDetailResponse.setSummary(processExecutiveSummary(execSummaryList.getAppId()));
				metricPortfolioDetailResponse.setTimeSeries(processExecutiveTimeSeries(execSummaryList));
				if (metricPortfolioDetailResponse.getSummary() != null)
					metricPortfolioDetailResponse.getSummary().setTotalComponents(execSummaryList.getTotalApps());
				metricsDetailRepository.save(metricPortfolioDetailResponse);
			}
		}
		LOG.info("Completed Total Value Details : portfolio_metrics_details . . . . ");
		return true;
	}

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
			LOG.info("Error inside TotalValueAnalysis file - processExecutiveTimeSeries() " + e);

		}
		return timeSeries;
	}

	private MetricSummary processExecutiveSummary(List<String> configuredAppId) {
		List<ExecutiveModuleMetrics> modules = new ArrayList<>();
		MetricSummary metricSummaryResponse = new MetricSummary();
		List<String> teamIds = new ArrayList<>();
		int reportingComponents = 0;
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
		metricSummaryResponse.setLastScanned(new Date(System.currentTimeMillis()));
		metricSummaryResponse.setName(TOTALVALUE);
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
		LOG.info("Processing Total Value Details : building_block_components . . . . ");
		Query query = new Query(new Criteria().where(METRICSNAME).is(VELOCITY));
		List<String> appIds = mongoTemplate.getCollection(EXECUTIVEMETRICS).distinct(APPID, query.getQueryObject());
		if (!appIds.isEmpty()) {
			for (String appId : appIds) {
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
						VELOCITY);
				List<BuildingBlocks> response = buildingBlocksRepository.findByMetricLevelIdAndMetricLevelAndMetricType(appId,
						MetricLevel.COMPONENT, MetricType.TOTAL_VALUE);
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
						summaryResponse.setMetricType(MetricType.TOTAL_VALUE);
						summaryResponse.setMetricLevel(MetricLevel.COMPONENT);
						buildingBlockResponse.add(summaryResponse);
					}
				}

				if (response != null)
					buildingBlocksRepository.delete(response);

				buildingBlocksRepository.save(buildingBlockResponse);
			}
		}
		LOG.info("Completed Total Value Details : building_block_components . . . . ");
		return true;
	}

	private String getJiraProjectLink(String projectKey) {
		if (projectKey != null)
			return metricsProcessorSettings.getJiraBaseUrl() + projectKey + metricsProcessorSettings.getStoryLink();
		return null;
	}

	private List<MetricSummary> processComponentMetrics(ExecutiveModuleMetrics module) {
		List<MetricSummary> metricSummaryResponseList = new ArrayList<>();
		List<ExecutiveModuleMetrics> modules = new ArrayList<>();
		if (module.getSeries() != null && !module.getSeries().isEmpty()) {
			MetricSummary metricSummaryResponse = new MetricSummary();
			metricSummaryResponse.setName(TOTALVALUE);
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

	private Double getTrendSlopesForModules(List<ExecutiveModuleMetrics> modules) {
		Map<Integer, Long> seriesList = new HashMap<>();
		if (!modules.isEmpty()) {
			for (ExecutiveModuleMetrics executiveModuleMetrics : modules) {
				List<ExecutiveMetricsSeries> executiveMetricsSeriesList = executiveModuleMetrics.getSeries();
				if (!executiveMetricsSeriesList.isEmpty()) {
					for (ExecutiveMetricsSeries executiveMetricsSeries : executiveMetricsSeriesList) {
						int days = executiveMetricsSeries.getDaysAgo();
						if (executiveMetricsSeries.getCounts() != null) {
							List<SeriesCount> countList = executiveMetricsSeries.getCounts();
							for (SeriesCount seriesCount : countList) {
								if (seriesCount.getLabel().containsValue(TOTALUSERSTORIES)) {
									Long count = seriesCount.getCount();
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

	private String getPortfolioId(String eid) {
		PortfolioResponse portfolioResponse = portfolioResponseRepository.findByEid(eid);
		return portfolioResponse == null ? null : portfolioResponse.getId().toString();
	}

	@Override
	public Boolean processExecutiveMetricsDetails() {
		// TODO Auto-generated method stub
		return null;
	}
}
