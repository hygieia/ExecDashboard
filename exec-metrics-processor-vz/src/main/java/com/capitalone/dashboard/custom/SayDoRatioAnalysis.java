package com.capitalone.dashboard.custom;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.collector.MetricsProcessorSettings;
import com.capitalone.dashboard.dao.SprintMetricsDAO;
import com.capitalone.dashboard.dao.VastDetailsDAO;
import com.capitalone.dashboard.exec.model.MetricCount;
import com.capitalone.dashboard.exec.model.MetricLevel;
import com.capitalone.dashboard.exec.model.MetricSummary;
import com.capitalone.dashboard.exec.model.MetricTimeSeriesElement;
import com.capitalone.dashboard.exec.model.MetricType;
import com.capitalone.dashboard.exec.model.ApplicationDetails;
import com.capitalone.dashboard.exec.model.BuildingBlocks;
import com.capitalone.dashboard.exec.model.DateWiseMetricsSeries;
import com.capitalone.dashboard.exec.model.ExecutiveComponents;
import com.capitalone.dashboard.exec.model.ExecutiveMetrics;
import com.capitalone.dashboard.exec.model.ExecutiveMetricsSeries;
import com.capitalone.dashboard.exec.model.ExecutiveModuleMetrics;
import com.capitalone.dashboard.exec.model.ExecutiveSummaryList;
import com.capitalone.dashboard.exec.model.MetricsDetail;
import com.capitalone.dashboard.exec.model.PortfolioResponse;
import com.capitalone.dashboard.exec.model.SeriesCount;
import com.capitalone.dashboard.exec.model.SprintMetrics;
import com.capitalone.dashboard.exec.repository.ApplicationDetailsRepository;
import com.capitalone.dashboard.exec.repository.BuildingBlocksRepository;
import com.capitalone.dashboard.exec.repository.DateWiseMetricsSeriesRepository;
import com.capitalone.dashboard.exec.repository.ExecutiveComponentRepository;
import com.capitalone.dashboard.exec.repository.ExecutiveSummaryListRepository;
import com.capitalone.dashboard.exec.repository.MetricsDetailRepository;
import com.capitalone.dashboard.exec.repository.PortfolioResponseRepository;
import com.capitalone.dashboard.utils.LinearRegression;
import com.mongodb.MongoClient;

/**
 * 
 * @author asthaaa
 *
 */
@Component
@SuppressWarnings("PMD")
public class SayDoRatioAnalysis implements MetricsProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(SayDoRatioAnalysis.class);

	private final SprintMetricsDAO sprintMetricsDAO;
	private final MetricsProcessorSettings metricsSettings;
	private final DateWiseMetricsSeriesRepository dateWiseMetricsSeriesRepository;
	private final ApplicationDetailsRepository applicationDetailsRepository;
	private final ExecutiveComponentRepository executiveComponentRepository;
	private final VastDetailsDAO vastDetailsDAO;
	private final MongoTemplate mongoTemplate;
	private final MetricsDetailRepository metricsDetailRepository;
	private final ExecutiveSummaryListRepository executiveSummaryListRepository;
	private final PortfolioResponseRepository portfolioResponseRepository;
	private final BuildingBlocksRepository buildingBlocksRepository;

	private static final String SAYDORATIO = "saydoratio";
	private static final String METRICSNAME = "metrics.metricsName";
	private static final String EXECUTIVEMETRICS = "executives_metrics";
	private static final String APPID = "appId";
	private static final String TOTALSTORIES = "totalStories";
	private static final String COMPLETEDSTORIES = "completedStories";
	private static final String TOTALSTORYPOINTS = "totalStoryPoints";
	private static final String COMPLETEDSTORYPOINTS = "completedStoryPoints";
	private static final String SPRINTNAME = "sprintName";
	private static final String SPRINTDURATIONINDAYS = "sprintDurationInDays";
	private static final String STORYPOINTSSAYDORATIO = "storyPointsSayDoRatio";

	private static final String AVGTOTALSTORIES = "avgTotalStories";
	private static final String AVGTOTALSTORYPOINTS = "avgTotalStoryPoints";
	private static final String AVGCOMPLETEDSTORIES = "avgCompletedStories";
	private static final String AVGCOMPLETEDSTORYPOINTS = "avgCompletedStoryPoints";
	private static final String AVGDURATIONINDAYS = "avgSprintDurationInDays";

	/**
	 * 
	 * @param sprintMetricsDAO
	 * @param metricsSettings
	 * @param dateWiseMetricsSeriesRepository
	 * @param applicationDetailsRepository
	 * @param executiveComponentRepository
	 * @param vastDetailsDAO
	 * @param mongoTemplate
	 * @param metricsDetailRepository
	 * @param executiveSummaryListRepository
	 * @param portfolioResponseRepository
	 * @param buildingBlocksRepository
	 */
	@Autowired
	public SayDoRatioAnalysis(SprintMetricsDAO sprintMetricsDAO, MetricsProcessorSettings metricsSettings,
			DateWiseMetricsSeriesRepository dateWiseMetricsSeriesRepository,
			ApplicationDetailsRepository applicationDetailsRepository,
			ExecutiveComponentRepository executiveComponentRepository, VastDetailsDAO vastDetailsDAO,
			MongoTemplate mongoTemplate, MetricsDetailRepository metricsDetailRepository,
			ExecutiveSummaryListRepository executiveSummaryListRepository,
			PortfolioResponseRepository portfolioResponseRepository,
			BuildingBlocksRepository buildingBlocksRepository) {
		this.sprintMetricsDAO = sprintMetricsDAO;
		this.metricsSettings = metricsSettings;
		this.dateWiseMetricsSeriesRepository = dateWiseMetricsSeriesRepository;
		this.applicationDetailsRepository = applicationDetailsRepository;
		this.executiveComponentRepository = executiveComponentRepository;
		this.vastDetailsDAO = vastDetailsDAO;
		this.mongoTemplate = mongoTemplate;
		this.metricsDetailRepository = metricsDetailRepository;
		this.executiveSummaryListRepository = executiveSummaryListRepository;
		this.portfolioResponseRepository = portfolioResponseRepository;
		this.buildingBlocksRepository = buildingBlocksRepository;
	}

	/**
	 * 
	 * @return
	 */
	public boolean processDateWiseTrend() {
		LOG.info("Processing Say/Do Ratio Date Wise Trend : date_wise_metrics . . . . ");
		MongoClient client = null;
		try {
			client = sprintMetricsDAO.getMongoClient();
			List<String> appIds = sprintMetricsDAO.getEntireAppList(client);
			Long timeStamp = metricsSettings.getDateRange();
			for (String appId : appIds) {
				processDateWiseTrendSayDoRatioSeries(client, appId, timeStamp);
			}
		} catch (Exception e) {
			LOG.info("Error in Say/Do Ratio Analysis Date Wise Trend :: " + e, e);
		} finally {
			if (client != null)
				client.close();
		}
		LOG.info("Completed Say/Do Ratio Date Wise Trend : date_wise_metrics . . . . ");

		return true;
	}

	/**
	 * 
	 * @param client
	 * @param appId
	 * @param timeStamp
	 * @return
	 */

	private Boolean processDateWiseTrendSayDoRatioSeries(MongoClient client, String appId, Long timeStamp) {
		try {
			Long revisedTimeStamp = timeStamp;
			if (revisedTimeStamp == null || revisedTimeStamp == 0l) {
				revisedTimeStamp = getLast90daystd();
			}
			List<String> moduleList = sprintMetricsDAO.getListOfModules(client, appId, revisedTimeStamp);
			for (String module : moduleList) {
				fetchAndUpdateSayDoDataForRevisedTime(client, appId, module, revisedTimeStamp);
			}
		} catch (Exception e) {
			LOG.info("Error in Say/Do Analysis Date Wise Trend Series :: " + e, e);
		}
		return true;
	}

	/**
	 * method getLast90daystd()
	 * 
	 * @return long
	 */
	private long getLast90daystd() {
		Date today = new Date();
		today.setHours(0);
		today.setMinutes(0);
		today.setSeconds(0);
		Calendar c = Calendar.getInstance();
		c.setTime(today);
		c.add(Calendar.DAY_OF_YEAR, -(90));
		return (c.getTimeInMillis() / 1000) * 1000;
	}

	private boolean fetchAndUpdateSayDoDataForRevisedTime(MongoClient client, String appId, String module,
			Long timestamp) {
		try {
			Long presentTimeStamp = System.currentTimeMillis();
			Date revisedDate = new Date(timestamp);
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String revisedStringDate = dateFormat.format(revisedDate);
			Date date = dateFormat.parse(revisedStringDate);
			Long revisedTimeStamp = date.getTime();
			while (revisedTimeStamp < presentTimeStamp) {
				DateWiseMetricsSeries dateWiseMetricsSeries = dateWiseMetricsSeriesRepository
						.findByAppIdAndModuleNameAndMetricsNameAndTimeStamp(appId, module, SAYDORATIO,
								revisedTimeStamp);

				if (dateWiseMetricsSeries == null)
					dateWiseMetricsSeries = new DateWiseMetricsSeries();

				List<SeriesCount> seriesCountList = new ArrayList<>();
				List<SprintMetrics> sprintDataSet = sprintMetricsDAO.getDataForModule(module, revisedTimeStamp,
						revisedTimeStamp + 86399999, client);

				if (!sprintDataSet.isEmpty()) {

					for (SprintMetrics sprintData : sprintDataSet) {

						SeriesCount seriesCount = new SeriesCount();
						seriesCount.setCount(Long.valueOf(sprintData.getStoryPointsSayDoRatio()));
						Map<String, String> label = new HashMap<>();
						label.put(TOTALSTORIES, String.valueOf(sprintData.getTotalStories()));
						label.put(COMPLETEDSTORIES, String.valueOf(sprintData.getCompletedStories()));
						label.put(TOTALSTORYPOINTS, String.valueOf(sprintData.getTotalStoryPoints()));
						label.put(COMPLETEDSTORYPOINTS, String.valueOf(sprintData.getCompletedStoryPoints()));
						label.put("startDate", String.valueOf(sprintData.getStartDate()));
						label.put("endDate", String.valueOf(sprintData.getEndDate()));
						label.put(SPRINTNAME, sprintData.getSprintName());
						label.put("sprintClosedBy", String.valueOf(sprintData.getSprintClosedBy()));
						label.put(SPRINTDURATIONINDAYS, String.valueOf(sprintData.getSprintDurationInDays()));
						label.put("sprintId", String.valueOf(sprintData.getSprintId()));
						label.put("completeDate", String.valueOf(sprintData.getCompleteDate()));
						label.put("storiesSayDoRatio", String.valueOf(sprintData.getStoriesSayDoRatio()));
						label.put("boardId", String.valueOf(sprintData.getBoardId()));
						label.put("projectKey", String.valueOf(sprintData.getProjectKey()));
						label.put(STORYPOINTSSAYDORATIO, String.valueOf(sprintData.getStoryPointsSayDoRatio()));

						seriesCount.setLabel(label);
						seriesCountList.add(seriesCount);

					}
					dateWiseMetricsSeries.setAppId(appId);
					dateWiseMetricsSeries.setMetricsName(SAYDORATIO);

					dateWiseMetricsSeries.setModuleName(module);

					dateWiseMetricsSeries.setTimeStamp(revisedTimeStamp);
					dateWiseMetricsSeries.setDateValue(dateFormat.format(revisedDate));
					dateWiseMetricsSeries.setCounts(seriesCountList);
					dateWiseMetricsSeriesRepository.save(dateWiseMetricsSeries);

				}
				revisedTimeStamp += 86400000;
				revisedDate = new Date(revisedTimeStamp);
			}

		} catch (Exception e) {
			LOG.info("Error in SayDoRatio Analysis Date Wise Trend fetch And Update For Revised Time :: " + e, e);
		}
		return true;
	}

	/**
	 * method processSayDoDetails
	 * 
	 * @return
	 */
	public Boolean processExecutiveMetricsDetails() {
		LOG.info("Processing Say/Do Details : executives_metrics . . . . ");
		MongoClient client = null;
		try {
			client = sprintMetricsDAO.getMongoClient();
			List<String> appIds = sprintMetricsDAO.getEntireAppList(client);
			for (String appId : appIds) {
				ApplicationDetails appDetails = applicationDetailsRepository.findByAppId(appId);
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
						SAYDORATIO);
				if (executiveComponents == null)
					executiveComponents = new ExecutiveComponents();

				executiveComponents.setAppId(appId);
				executiveComponents.setAppName(appDetails != null ? appDetails.getAppName() : "");
				executiveComponents.setTeamBoardLink(appDetails != null ? appDetails.getTeamBoardLink() : "");
				executiveComponents.setMetrics(processMetrics(appId));
				executiveComponentRepository.save(executiveComponents);
			}

		} catch (Exception e) {
			LOG.info("Error in Say/Do Analysis processBuildDetails :: " + e, e);
		} finally {
			if (client != null)
				client.close();
		}
		LOG.info("Completed Say/Do Details : executives_metrics . . . . ");
		return true;
	}

	/**
	 * removeUnusedBuildDetails
	 * 
	 * @return
	 */
	public boolean removeUnusedSayDoDetails() {
		LOG.info("Removing Unused Say/Do Details : executives_metrics . . . . ");
		MongoClient client = null;
		try {
			client = sprintMetricsDAO.getMongoClient();
			List<String> appIdList = vastDetailsDAO.getAllAppIds(client);
			if (appIdList != null && !appIdList.isEmpty()) {
				List<ExecutiveComponents> unusedDataList = executiveComponentRepository.getNotUsedAppIdList(appIdList,
						SAYDORATIO);
				if (unusedDataList != null)
					executiveComponentRepository.delete(unusedDataList);
			}
		} catch (Exception e) {
			LOG.info("Error inside removeUnusedSayDoDetails " + e, e);
		} finally {
			if (client != null)
				client.close();
		}
		LOG.info("Completed Removing Unused Say/Do Details : executives_metrics . . . . ");
		return true;
	}

	/**
	 * processMetricsDetailResponse
	 * 
	 * @return
	 */
	public Boolean processMetricsDetailResponse() {
		LOG.info("Processing Say/Do Details : app_metrics_details . . . . ");
		Query query = new Query(new Criteria().where(METRICSNAME).is(SAYDORATIO));
		List<String> appIds = mongoTemplate.getCollection(EXECUTIVEMETRICS).distinct(APPID, query.getQueryObject());
		if (!appIds.isEmpty()) {
			for (String appId : appIds) {
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
						SAYDORATIO);
				MetricsDetail metricDetailResponseProcessed = processMetricDetailResponse(executiveComponents);
				MetricsDetail metricDetailResponseStored = metricsDetailRepository
						.findByMetricLevelIdAndLevelAndType(appId, MetricLevel.PRODUCT, MetricType.SAY_DO_RATIO);
				if (metricDetailResponseStored != null) {
					metricsDetailRepository.delete(metricDetailResponseStored);
				}
				metricsDetailRepository.save(metricDetailResponseProcessed);
			}
		}
		LOG.info("Completed Say/Do Details : app_metrics_details . . . . ");
		return true;
	}

	/**
	 * 
	 * @return
	 */
	public Boolean processBuildingBlockMetrics() {

		LOG.info("Processing Say/Do Details : building_block_metrics . . . . ");
		try {
			Query query = new Query(new Criteria().where(METRICSNAME).is(SAYDORATIO));
			List<String> appIds = mongoTemplate.getCollection(EXECUTIVEMETRICS).distinct(APPID, query.getQueryObject());
			if (!appIds.isEmpty()) {
				for (String appId : appIds) {
					BuildingBlocks buildingBlockMetricSummary = buildingBlocksRepository
							.findByMetricLevelIdAndMetricLevel(appId, MetricLevel.PRODUCT);
					if (buildingBlockMetricSummary == null)
						buildingBlockMetricSummary = new BuildingBlocks();

					ApplicationDetails appDetails = applicationDetailsRepository.findByAppId(appId);
					MetricsDetail metricDetailResponse = metricsDetailRepository
							.findByMetricLevelIdAndLevelAndType(appId, MetricLevel.PRODUCT, MetricType.SAY_DO_RATIO);
					if (metricDetailResponse != null && appDetails != null) {
						buildingBlockMetricSummary.setMetricLevelId(appId);
						buildingBlockMetricSummary.setLob(appDetails.getLob());
						buildingBlockMetricSummary.setName(appDetails.getAppName());
						buildingBlockMetricSummary.setMetricLevel(MetricLevel.PRODUCT);
						List<MetricSummary> metricsResponseStored = buildingBlockMetricSummary
								.getMetrics();
						List<MetricSummary> metricsResponseProcessed = new ArrayList<>();
						if (metricsResponseStored != null && !metricsResponseStored.isEmpty()) {
							for (MetricSummary metricSummaryResponse : metricsResponseStored) {
								if (metricSummaryResponse != null && metricSummaryResponse.getName() != null
										&& !metricSummaryResponse.getName().equalsIgnoreCase(SAYDORATIO))
									metricsResponseProcessed.add(metricSummaryResponse);
							}
						}
						metricsResponseProcessed.add(metricDetailResponse.getSummary());
						buildingBlockMetricSummary.setMetrics(metricsResponseProcessed);
						buildingBlocksRepository.save(buildingBlockMetricSummary);
					}
				}
			}
			LOG.info("Completed Say/Do Details : building_block_metrics . . . . ");
		} catch (Exception e) {
			LOG.info("processBuildingBlockMetrics Build Analysis Info :: " + e, e);
		}
		return true;
	}

	/**
	 * 
	 * @return
	 */
	public Boolean processExecutiveDetailsMetrics() {
		LOG.info("Processing Say/Do Details : portfolio_metrics_details . . . . ");
		List<ExecutiveSummaryList> executiveSummaryLists = (List<ExecutiveSummaryList>) executiveSummaryListRepository
				.findAll();

		if (!executiveSummaryLists.isEmpty()) {
			for (ExecutiveSummaryList executiveSummaryList : executiveSummaryLists) {
				String eid = executiveSummaryList.getEid();
				MetricsDetail metricPortfolioDetailResponse = metricsDetailRepository
						.findByMetricLevelIdAndLevelAndType(eid, MetricLevel.PORTFOLIO, MetricType.SAY_DO_RATIO);
				if (metricPortfolioDetailResponse == null)
					metricPortfolioDetailResponse = new MetricsDetail();
				metricPortfolioDetailResponse.setType(MetricType.SAY_DO_RATIO);
				metricPortfolioDetailResponse.setLevel(MetricLevel.PORTFOLIO);
				metricPortfolioDetailResponse.setMetricLevelId(eid);
				metricPortfolioDetailResponse.setCustomField(getPortfolioId(eid));
				metricPortfolioDetailResponse.setSummary(processExecutiveSummary(executiveSummaryList.getAppId()));
				metricPortfolioDetailResponse
						.setTimeSeries(processExecutiveTimeSeries(executiveSummaryList.getAppId()));
				if (metricPortfolioDetailResponse.getSummary() != null) {
					metricPortfolioDetailResponse.getSummary().setTotalComponents(executiveSummaryList.getTotalApps());
					metricPortfolioDetailResponse.getSummary()
							.setReportingComponents(noOfAppsWithData(executiveSummaryList));
				}
				metricsDetailRepository.save(metricPortfolioDetailResponse);
			}
		}
		LOG.info("Completed Say/Do Details : portfolio_metrics_details . . . . ");
		return true;
	}

	/**
	 * 
	 * @return
	 */
	public Boolean processComponentDetailsMetrics() {
		LOG.info("Processing Say/Do Details : building_block_components . . . . ");
		Query query = new Query(new Criteria().where(METRICSNAME).is(SAYDORATIO));
		List<String> appIds = mongoTemplate.getCollection(EXECUTIVEMETRICS).distinct(APPID, query.getQueryObject());
		if (!appIds.isEmpty()) {
			for (String appId : appIds) {
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
						SAYDORATIO);
				List<BuildingBlocks> response = buildingBlocksRepository
						.findByMetricLevelIdAndMetricLevelAndMetricType(appId, MetricLevel.COMPONENT, MetricType.SAY_DO_RATIO);
				List<BuildingBlocks> buildingBlockResponse = new ArrayList<>();
				List<ExecutiveModuleMetrics> modules = executiveComponents.getMetrics().get(0).getModules();
				ApplicationDetails appDetails = applicationDetailsRepository.findByAppId(appId);

				if (!modules.isEmpty() && appDetails != null) {
					for (ExecutiveModuleMetrics module : modules) {
						BuildingBlocks summaryResponse = new BuildingBlocks();
						summaryResponse.setMetricLevelId(appId);
						summaryResponse.setLob(appDetails.getLob());
						summaryResponse.setMetrics(processComponentMetrics(module));
						summaryResponse.setName(module.getModuleName());
						summaryResponse.setPoc(appDetails.getPoc());
						summaryResponse.setTotalComponents(1);
						summaryResponse.setTotalExpectedMetrics(1);
						summaryResponse.setUrl(getSprintLink(module.getSeries()));
						summaryResponse.setMetricType(MetricType.SAY_DO_RATIO);
						summaryResponse.setMetricLevel(MetricLevel.COMPONENT);
						buildingBlockResponse.add(summaryResponse);
					}
				}
				if (response != null)
					buildingBlocksRepository.delete(response);
				buildingBlocksRepository.save(buildingBlockResponse);
			}
		}
		LOG.info("Completed Say/Do Details : building_block_components . . . . ");
		return true;
	}

	private String getSprintLink(List<ExecutiveMetricsSeries> seriesList) {
		String rapidViewId = "";
		String projectKey = "";
		String sprintId = "";
		for(ExecutiveMetricsSeries series : seriesList){
			List<SeriesCount> seriesCountList = series.getCounts();
			if(!seriesCountList.isEmpty()){
				for(SeriesCount seriesCount : seriesCountList){
					rapidViewId = getLabelValue(seriesCount, "boardId" );
					projectKey = getLabelValue(seriesCount, "projectKey" );
					sprintId = getLabelValue(seriesCount, "sprintId" );
				}
			}
		}
		return String.format(
				metricsSettings.getSprintViewLink(), rapidViewId, projectKey, sprintId);
	}

	private String getLabelValue(SeriesCount seriesCount, String key) {
		if(!seriesCount.getLabel().isEmpty()){
			Map<String, String> labels = seriesCount.getLabel();
			return labels.get(key);
		}
		return null;
	}

	/**
	 * 
	 * @param module
	 * @return
	 */

	private List<MetricSummary> processComponentMetrics(ExecutiveModuleMetrics module) {
		List<MetricSummary> metricSummaryResponseList = new ArrayList<>();
		List<ExecutiveModuleMetrics> modules = new ArrayList<>();
		MetricSummary metricSummaryResponse = new MetricSummary();
		metricSummaryResponse.setName(SAYDORATIO);
		metricSummaryResponse.setTotalComponents(1);
		metricSummaryResponse.setReportingComponents(1);
		metricSummaryResponse.setLastScanned(module.getLastScanned());
		metricSummaryResponse.setLastUpdated(module.getLastUpdated());
		modules.add(module);
		metricSummaryResponse.setCounts(processMetricSummaryCountsForComponents(modules));
		Boolean dataAvailabilityStatus = checkForDataAvailability(modules);
		metricSummaryResponse.setDataAvailable(dataAvailabilityStatus);
		if (!dataAvailabilityStatus)
			metricSummaryResponse.setConfMessage(checkForDataAvailabilityStatus(modules));
		metricSummaryResponseList.add(metricSummaryResponse);
		return metricSummaryResponseList;
	}

	/**
	 * 
	 * @param modules
	 * @return
	 */

	private String checkForDataAvailabilityStatus(List<ExecutiveModuleMetrics> modules) {
		if (modules.isEmpty())
			return "Not Configured";
		return null;
	}

	/**
	 * 
	 * @param configuredAppId
	 * @return
	 */

	private List<MetricTimeSeriesElement> processExecutiveTimeSeries(List<String> configuredAppId) {
		List<MetricTimeSeriesElement> timeSeries = new ArrayList<>();
		List<ExecutiveModuleMetrics> modules = new ArrayList<>();
		if (configuredAppId != null && !configuredAppId.isEmpty()) {
			Map<Integer, Object> data = new HashMap<>();
			for (String appId : configuredAppId) {
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
						SAYDORATIO);
				if (executiveComponents != null) {
					List<ExecutiveMetrics> executiveMetricsList = executiveComponents.getMetrics();
					if (executiveMetricsList != null) {
						for (ExecutiveMetrics metric : executiveMetricsList) {
							if (metric.getMetricsName().equalsIgnoreCase(SAYDORATIO)) {
								List<ExecutiveModuleMetrics> modulesList = metric.getModules();
								for (ExecutiveModuleMetrics executiveModuleMetrics : modulesList) {
									modules.add(executiveModuleMetrics);
								}
							}
						}
					}
				}
			}

			for (ExecutiveModuleMetrics executiveModuleMetrics : modules) {
				List<ExecutiveMetricsSeries> series = executiveModuleMetrics.getSeries();
				if (series != null) {
					for (ExecutiveMetricsSeries item : series) {
						if (data.get(item.getDaysAgo()) == null) {
							data.put(item.getDaysAgo(), item.getCounts());
						} else {
							List<SeriesCount> counts = (List<SeriesCount>) data.get(item.getDaysAgo());
							counts.addAll(item.getCounts());
							data.put(item.getDaysAgo(), counts);
						}
					}
				}
			}

			int totalStoryPointRatio = 0;
			int count = 0;

			for (Entry<Integer, Object> item : data.entrySet()) {
				MetricTimeSeriesElement metricTimeSeriesElementResponse = new MetricTimeSeriesElement();
				metricTimeSeriesElementResponse.setDaysAgo(item.getKey());
				List<MetricCount> counts = new ArrayList<>();
				MetricCount metricCountResponse = new MetricCount();

				List<SeriesCount> getListOfData = (List<SeriesCount>) item.getValue();
				for (SeriesCount rec : getListOfData) {
					totalStoryPointRatio += rec.getCount();
					count += 1;
				}

				Map<String, String> label = new HashMap<>();
				if (count != 0) {
					label.put(STORYPOINTSSAYDORATIO, String.valueOf(totalStoryPointRatio / count));
					metricCountResponse.setValue(totalStoryPointRatio / count);
				} else {
					label.put(STORYPOINTSSAYDORATIO, "NA");
					metricCountResponse.setValue(0);
				}

				metricCountResponse.setValue(getListOfData.size());
				metricCountResponse.setLabel(label);
				counts.add(metricCountResponse);
				metricTimeSeriesElementResponse.setCounts(counts);
				timeSeries.add(metricTimeSeriesElementResponse);
			}
		}

		return timeSeries;
	}

	/**
	 * 
	 * @param executiveSummaryList
	 * @return
	 */
	private Integer noOfAppsWithData(ExecutiveSummaryList executiveSummaryList) {
		int count = 0;
		List<String> appList = executiveSummaryList.getAppId();
		for (String appId : appList) {
			int countCheck = 0;
			ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
					SAYDORATIO);
			if (executiveComponents != null) {
				List<ExecutiveMetrics> executiveMetricsList = executiveComponents.getMetrics();
				if (executiveMetricsList != null) {
					for (ExecutiveMetrics metric : executiveMetricsList) {
						List<ExecutiveModuleMetrics> modulesList = metric.getModules();
						if (modulesList != null && !modulesList.isEmpty()) {
							countCheck++;
						}
					}
				}
			}
			if (countCheck > 0)
				count++;
		}
		return count;
	}

	/**
	 * 
	 * @param configuredAppId
	 * @return
	 */
	private MetricSummary processExecutiveSummary(List<String> configuredAppId) {
		List<ExecutiveModuleMetrics> modules = new ArrayList<>();
		if (configuredAppId != null && !configuredAppId.isEmpty()) {
			for (String appId : configuredAppId) {
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
						SAYDORATIO);
				if (executiveComponents != null) {
					List<ExecutiveMetrics> executiveMetricsList = executiveComponents.getMetrics();
					if (executiveMetricsList != null) {
						for (ExecutiveMetrics metric : executiveMetricsList) {
							if (metric.getMetricsName().equalsIgnoreCase(SAYDORATIO)) {
								List<ExecutiveModuleMetrics> modulesList = metric.getModules();
								for (ExecutiveModuleMetrics executiveModuleMetrics : modulesList) {
									modules.add(executiveModuleMetrics);
								}
							}
						}
					}
				}
			}

			MetricSummary metricSummaryResponse = new MetricSummary();
			metricSummaryResponse.setLastScanned(new Date(System.currentTimeMillis()));
			metricSummaryResponse.setLastUpdated(new Date(System.currentTimeMillis()));
			metricSummaryResponse.setCounts(processMetricSummaryCounts(modules));
			metricSummaryResponse.setTrendSlope(getTrendSlopesForModules(modules));
			metricSummaryResponse.setName(SAYDORATIO);
			metricSummaryResponse.setDataAvailable(checkForDataAvailability(modules));
			return metricSummaryResponse;
		}
		return null;
	}

	/**
	 * 
	 * @param modules
	 * @return
	 */

	private Boolean checkForDataAvailability(List<ExecutiveModuleMetrics> modules) {
		if (!modules.isEmpty())
			return true;
		return false;
	}

	/**
	 * 
	 * @param modules
	 * @return
	 */
	private Double getTrendSlopesForModules(List<ExecutiveModuleMetrics> modules) {
		Map<Integer, Long> seriesList = new HashMap<>();
		if (!modules.isEmpty()) {
			for (ExecutiveModuleMetrics executiveModuleMetrics : modules) {
				List<ExecutiveMetricsSeries> executiveMetricsSeriesList = executiveModuleMetrics.getSeries();
				if (!executiveMetricsSeriesList.isEmpty()) {
					for (ExecutiveMetricsSeries executiveMetricsSeries : executiveMetricsSeriesList) {
						int days = executiveMetricsSeries.getDaysAgo();
						Long count = executiveMetricsSeries.getCounts().size() > 0
								? executiveMetricsSeries.getCounts().get(0).getCount() : 0l;
						if (seriesList.containsKey(days)) {
							count += seriesList.get(days);
							seriesList.replace(days, count);
						} else {
							seriesList.put(days, count);
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
	 * @param eid
	 * @return
	 */
	private String getPortfolioId(String eid) {
		PortfolioResponse portfolioResponse = portfolioResponseRepository.findByEid(eid);
		return portfolioResponse == null ? null : portfolioResponse.getId().toString();
	}

	/**
	 * 
	 * @param executiveComponents
	 * @return
	 */
	private MetricsDetail processMetricDetailResponse(ExecutiveComponents executiveComponents) {
		MetricsDetail metricDetailResponse = new MetricsDetail();
		metricDetailResponse.setMetricLevelId(executiveComponents.getAppId());
		metricDetailResponse.setLevel(MetricLevel.PRODUCT);
		metricDetailResponse.setType(MetricType.SAY_DO_RATIO);
		metricDetailResponse.setSummary(processMetricsSummary(executiveComponents));
		metricDetailResponse.setTimeSeries(processTimeSeries(executiveComponents));
		return metricDetailResponse;
	}

	/**
	 * 
	 * @param executiveComponents
	 * @return
	 */
	private List<MetricTimeSeriesElement> processTimeSeries(ExecutiveComponents executiveComponents) {

		List<MetricTimeSeriesElement> timeSeries = new ArrayList<>();
		Map<Integer, Object> data = new HashMap<>();
		if (executiveComponents.getMetrics() != null) {
			List<ExecutiveMetrics> metrics = executiveComponents.getMetrics();
			for (ExecutiveMetrics metric : metrics) {
				if (metric.getMetricsName().equalsIgnoreCase(SAYDORATIO)) {
					List<ExecutiveModuleMetrics> modulesList = metric.getModules();
					for (ExecutiveModuleMetrics executiveModuleMetrics : modulesList) {
						List<ExecutiveMetricsSeries> series = executiveModuleMetrics.getSeries();
						if (series != null) {
							for (ExecutiveMetricsSeries item : series) {
								if (data.get(item.getDaysAgo()) == null) {
									data.put(item.getDaysAgo(), item.getCounts());
								} else {
									List<SeriesCount> counts = (List<SeriesCount>) data.get(item.getDaysAgo());
									counts.addAll(item.getCounts());
									data.put(item.getDaysAgo(), counts);
								}
							}
						}
					}
				}
			}

			int totalStoryPointRatio = 0;
			int count = 0;

			for (Entry<Integer, Object> item : data.entrySet()) {
				MetricTimeSeriesElement metricTimeSeriesElementResponse = new MetricTimeSeriesElement();
				metricTimeSeriesElementResponse.setDaysAgo(item.getKey());
				List<MetricCount> counts = new ArrayList<>();
				MetricCount metricCountResponse = new MetricCount();
				List<SeriesCount> getListOfData = (List<SeriesCount>) item.getValue();
				for (SeriesCount rec : getListOfData) {
					totalStoryPointRatio += rec.getCount();
					count += 1;
				}

				Map<String, String> label = new HashMap<>();
				if (count != 0) {
					label.put(STORYPOINTSSAYDORATIO, String.valueOf(totalStoryPointRatio / count));
					metricCountResponse.setValue(totalStoryPointRatio / count);
				} else {
					label.put(STORYPOINTSSAYDORATIO, "NA");
					metricCountResponse.setValue(0);
				}

				metricCountResponse.setLabel(label);
				counts.add(metricCountResponse);
				metricTimeSeriesElementResponse.setCounts(counts);
				timeSeries.add(metricTimeSeriesElementResponse);
			}
		}
		return timeSeries;
	}

	/**
	 * 
	 * @param executiveComponents
	 * @return
	 */

	private MetricSummary processMetricsSummary(ExecutiveComponents executiveComponents) {
		MetricSummary metricSummaryResponse = new MetricSummary();
		List<ExecutiveModuleMetrics> modules = new ArrayList<>();
		metricSummaryResponse.setLastScanned(executiveComponents.getMetrics().get(0).getLastScanned());
		metricSummaryResponse.setLastUpdated(executiveComponents.getMetrics().get(0).getLastUpdated());

		if (executiveComponents.getMetrics() != null) {
			List<ExecutiveMetrics> metrics = executiveComponents.getMetrics();
			for (ExecutiveMetrics metric : metrics) {
				if (metric.getMetricsName().equalsIgnoreCase(SAYDORATIO)) {
					List<ExecutiveModuleMetrics> modulesList = metric.getModules();
					for (ExecutiveModuleMetrics executiveModuleMetrics : modulesList) {
						modules.add(executiveModuleMetrics);
					}
					metricSummaryResponse.setCounts(processMetricSummaryCounts(modules));
					metricSummaryResponse.setReportingComponents(modules.size());
					metricSummaryResponse.setTotalComponents(modulesList.size());
					metricSummaryResponse.setTrendSlope(executiveComponents.getMetrics().get(0).getTrendSlope());

					metricSummaryResponse.setDataAvailable(!modulesList.isEmpty() ? true : false);
					if (modulesList.isEmpty())
						metricSummaryResponse.setConfMessage("No Sprints");
				}
			}
		}
		metricSummaryResponse.setName(SAYDORATIO);
		return metricSummaryResponse;
	}

	/**
	 * 
	 * @param modules
	 * @return
	 */
	private List<MetricCount> processMetricSummaryCounts(List<ExecutiveModuleMetrics> modules) {

		List<MetricCount> metricCountResponseList = new ArrayList<>();
		int totalStories = 0;
		double totalStoryPoints = 0;
		int completedStories = 0;
		double completedStoryPoints = 0;
		int durationInDays = 0;
		int sprintCount = 0;

		if (!modules.isEmpty()) {
			for (ExecutiveModuleMetrics moduleMetric : modules) {
				List<ExecutiveMetricsSeries> seriesList = moduleMetric.getSeries();
				if (!seriesList.isEmpty()) {
					for (ExecutiveMetricsSeries series : seriesList) {
						List<SeriesCount> countsList = series.getCounts();
						if (!countsList.isEmpty()) {
							for (SeriesCount count : countsList) {
								if (count != null) {
									sprintCount += 1;
									totalStories += Integer.valueOf(count.getLabel().get(TOTALSTORIES));
									totalStoryPoints += Double.valueOf(count.getLabel().get(TOTALSTORYPOINTS));
									completedStories += Integer.valueOf(count.getLabel().get(COMPLETEDSTORIES));
									completedStoryPoints += Double.valueOf(count.getLabel().get(COMPLETEDSTORYPOINTS));
									durationInDays += Integer.valueOf(count.getLabel().get(SPRINTDURATIONINDAYS));

								}
							}
						}
					}
				}
			}
			Map<String, String> label = new HashMap<>();
			if (sprintCount != 0) {
				label.put(AVGTOTALSTORIES, String.valueOf(totalStories / sprintCount));
				label.put(AVGTOTALSTORYPOINTS, String.valueOf(totalStoryPoints / (double) sprintCount));
				label.put(AVGCOMPLETEDSTORIES, String.valueOf(completedStories / sprintCount));
				label.put(AVGCOMPLETEDSTORYPOINTS, String.valueOf(completedStoryPoints / (double) sprintCount));
				label.put(AVGDURATIONINDAYS, String.valueOf(durationInDays / sprintCount));
			} else {
				label.put(AVGTOTALSTORIES, String.valueOf(0));
				label.put(AVGTOTALSTORYPOINTS, String.valueOf(0));
				label.put(AVGCOMPLETEDSTORIES, String.valueOf(0));
				label.put(AVGCOMPLETEDSTORYPOINTS, String.valueOf(0));
				label.put(AVGDURATIONINDAYS, String.valueOf(0));
			}
			label.put(TOTALSTORIES, String.valueOf(totalStories));
			label.put(TOTALSTORYPOINTS, String.valueOf(totalStoryPoints));
			label.put(COMPLETEDSTORIES, String.valueOf(completedStories));
			label.put(COMPLETEDSTORYPOINTS, String.valueOf(completedStoryPoints));

			double avgTotalStoryPoints = sprintCount != 0 ? totalStoryPoints / (double) sprintCount : 0;
			double avgCompletedStoryPoints = sprintCount != 0 ? completedStoryPoints / (double) sprintCount : 0;

			MetricCount metricCountResponseNormal = new MetricCount();
			metricCountResponseNormal.setLabel(label);
			metricCountResponseNormal
					.setValue(avgTotalStoryPoints > 0 ? Math.round(avgCompletedStoryPoints / avgTotalStoryPoints) : 0);
			metricCountResponseList.add(metricCountResponseNormal);

			return metricCountResponseList;
		}

		Map<String, String> label = new HashMap<>();
		label.put(AVGTOTALSTORIES, "NA");
		label.put(AVGTOTALSTORYPOINTS, "NA");
		label.put(AVGCOMPLETEDSTORIES, "NA");
		label.put(AVGCOMPLETEDSTORYPOINTS, "NA");
		label.put(AVGDURATIONINDAYS, "NA");
		MetricCount metricCountResponseNormal = new MetricCount();
		metricCountResponseNormal.setLabel(label);
		metricCountResponseNormal.setValue(0);
		metricCountResponseList.add(metricCountResponseNormal);
		return metricCountResponseList;
	}

	private List<MetricCount> processMetricSummaryCountsForComponents(List<ExecutiveModuleMetrics> modules) {

		List<MetricCount> metricCountResponseList = new ArrayList<>();
		int totalStories = 0;
		double totalStoryPoints = 0;
		int completedStories = 0;
		double completedStoryPoints = 0;
		int durationInDays = 0;
		int sprintCount = 0;
		String sprintName = "";

		if (!modules.isEmpty()) {
			for (ExecutiveModuleMetrics moduleMetric : modules) {
				List<ExecutiveMetricsSeries> seriesList = moduleMetric.getSeries();
				if (!seriesList.isEmpty()) {
					for (ExecutiveMetricsSeries series : seriesList) {
						List<SeriesCount> countsList = series.getCounts();
						if (!countsList.isEmpty()) {
							for (SeriesCount count : countsList) {
								if (count != null) {
									sprintCount += 1;
									totalStories += Integer.valueOf(count.getLabel().get(TOTALSTORIES));
									totalStoryPoints += Double.valueOf(count.getLabel().get(TOTALSTORYPOINTS));
									completedStories += Integer.valueOf(count.getLabel().get(COMPLETEDSTORIES));
									completedStoryPoints += Double.valueOf(count.getLabel().get(COMPLETEDSTORYPOINTS));
									durationInDays += Integer.valueOf(count.getLabel().get(SPRINTDURATIONINDAYS));
									sprintName = count.getLabel().get(SPRINTNAME);

								}
							}

						}
					}
				}

				if (sprintName.isEmpty()) {
					sprintName = moduleMetric.getModuleName();
				}
			}

			Map<String, String> label = new HashMap<>();
			if (sprintCount != 0) {
				label.put(AVGTOTALSTORIES, String.valueOf(totalStories / sprintCount));
				label.put(AVGTOTALSTORYPOINTS, String.valueOf(totalStoryPoints / (double) sprintCount));
				label.put(AVGCOMPLETEDSTORIES, String.valueOf(completedStories / sprintCount));
				label.put(AVGCOMPLETEDSTORYPOINTS, String.valueOf(completedStoryPoints / (double) sprintCount));
				label.put(AVGDURATIONINDAYS, String.valueOf(durationInDays / sprintCount));
			} else {
				label.put(AVGTOTALSTORIES, String.valueOf(0));
				label.put(AVGTOTALSTORYPOINTS, String.valueOf(0));
				label.put(AVGCOMPLETEDSTORIES, String.valueOf(0));
				label.put(AVGCOMPLETEDSTORYPOINTS, String.valueOf(0));
				label.put(AVGDURATIONINDAYS, String.valueOf(0));

			}
			label.put(TOTALSTORIES, String.valueOf(totalStories));
			label.put(TOTALSTORYPOINTS, String.valueOf(totalStoryPoints));
			label.put(COMPLETEDSTORIES, String.valueOf(completedStories));
			label.put(COMPLETEDSTORYPOINTS, String.valueOf(completedStoryPoints));
			label.put(SPRINTNAME, sprintName);

			double avgTotalStoryPoints = sprintCount != 0 ? totalStoryPoints / (double) sprintCount : 0;
			double avgCompletedStoryPoints = sprintCount != 0 ? completedStoryPoints / (double) sprintCount : 0;

			MetricCount metricCountResponseNormal = new MetricCount();
			metricCountResponseNormal.setLabel(label);
			metricCountResponseNormal
					.setValue(avgTotalStoryPoints > 0 ? Math.round(avgCompletedStoryPoints / avgTotalStoryPoints) : 0);
			metricCountResponseList.add(metricCountResponseNormal);

			return metricCountResponseList;
		}

		Map<String, String> label = new HashMap<>();
		label.put(AVGTOTALSTORIES, "NA");
		label.put(AVGTOTALSTORYPOINTS, "NA");
		label.put(AVGCOMPLETEDSTORIES, "NA");
		label.put(AVGCOMPLETEDSTORYPOINTS, "NA");
		label.put(AVGDURATIONINDAYS, "NA");
		MetricCount metricCountResponseNormal = new MetricCount();
		metricCountResponseNormal.setLabel(label);
		metricCountResponseNormal.setValue(0);
		metricCountResponseList.add(metricCountResponseNormal);
		return metricCountResponseList;
	}

	private List<ExecutiveMetrics> processMetrics(String appId) {
		List<ExecutiveMetrics> metricsList = new ArrayList<>();
		ExecutiveMetrics metrics = new ExecutiveMetrics();
		List<ExecutiveModuleMetrics> modulesList = processModules(appId);
		metrics.setLastUpdated(getISODateTime(System.currentTimeMillis()));
		metrics.setLastScanned(getISODateTime(System.currentTimeMillis()));
		metrics.setMetricsName(SAYDORATIO);
		metrics.setModules(modulesList);
		metrics.setTrendSlope(processTrendSlope(modulesList));
		metricsList.add(metrics);
		return metricsList;
	}

	/**
	 * 
	 * @param modules
	 * @return
	 */

	private Double processTrendSlope(List<ExecutiveModuleMetrics> modules) {
		Map<Integer, Long> seriesList = new HashMap<>();
		if (!modules.isEmpty()) {
			for (ExecutiveModuleMetrics executiveModuleMetrics : modules) {
				List<ExecutiveMetricsSeries> executiveMetricsSeriesList = executiveModuleMetrics.getSeries();
				if (!executiveMetricsSeriesList.isEmpty()) {
					for (ExecutiveMetricsSeries executiveMetricsSeries : executiveMetricsSeriesList) {
						int days = executiveMetricsSeries.getDaysAgo();
						if (executiveMetricsSeries.getCounts() != null
								&& executiveMetricsSeries.getCounts().size() > 0) {
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

	/**
	 * 
	 * @param appId
	 * @param lastUpdated
	 * @return
	 */

	private List<ExecutiveModuleMetrics> processModules(String appId) {
		List<ExecutiveModuleMetrics> moduleList = new ArrayList<>();
		List<DateWiseMetricsSeries> dateWiseMetricsSeriesList = dateWiseMetricsSeriesRepository
				.getThreeMonthsListWithAppId(appId, SAYDORATIO, getTimeStamp(90));
		if (dateWiseMetricsSeriesList != null && !dateWiseMetricsSeriesList.isEmpty()) {
			Map<String, Object> modulesData = getModuleList(dateWiseMetricsSeriesList);
			for (Map.Entry<String, Object> module : modulesData.entrySet()) {
				List<ExecutiveMetricsSeries> seriesData = processSeries(
						(List<DateWiseMetricsSeries>) module.getValue());
				ExecutiveModuleMetrics executiveModuleMetrics = new ExecutiveModuleMetrics();
				executiveModuleMetrics.setModuleName(module.getKey());
				executiveModuleMetrics.setLastScanned(getISODateTime(System.currentTimeMillis()));
				executiveModuleMetrics.setLastUpdated(getISODateTime(System.currentTimeMillis()));
				executiveModuleMetrics.setSeries(seriesData);
				executiveModuleMetrics.setTrendSlope(processTrendSlopeForModule(seriesData));
				moduleList.add(executiveModuleMetrics);
			}
		}

		return moduleList;
	}

	/**
	 * 
	 * @param execMetricsSeriesList
	 * @return
	 */

	private Double processTrendSlopeForModule(List<ExecutiveMetricsSeries> execMetricsSeriesList) {
		Map<Long, Integer> mappedValue = new HashMap<>();
		if (execMetricsSeriesList != null) {
			for (ExecutiveMetricsSeries seris : execMetricsSeriesList) {
				long timestamp = seris.getTimeStamp();
				int counting = 0;
				List<SeriesCount> counts = seris.getCounts();
				for (SeriesCount count : counts) {
					counting += count.getCount();
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
	 * 
	 * @param moduleData
	 * @return
	 */

	private List<ExecutiveMetricsSeries> processSeries(List<DateWiseMetricsSeries> moduleData) {
		List<ExecutiveMetricsSeries> lastNintyDaysData = new ArrayList<>();
		for (DateWiseMetricsSeries record : moduleData) {
			if(!record.getCounts().isEmpty()){
				ExecutiveMetricsSeries executiveMetricsSeries = new ExecutiveMetricsSeries();
				executiveMetricsSeries.setDaysAgo(getDaysAgoValue(record.getTimeStamp()));
				executiveMetricsSeries.setTimeStamp(record.getTimeStamp());
				executiveMetricsSeries.setCounts(record.getCounts());
				lastNintyDaysData.add(executiveMetricsSeries);
			}
		}
		return lastNintyDaysData;
	}

	/**
	 * 
	 * @param timeStamp
	 * @return
	 */

	private int getDaysAgoValue(Long timeStamp) {
		Date dateFromDB = new Date(timeStamp);
		Date presentDate = new Date();
		long diff = presentDate.getTime() - dateFromDB.getTime();
		return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}

	/**
	 * 
	 * @param dataList
	 * @return
	 */

	private Map<String, Object> getModuleList(List<DateWiseMetricsSeries> dataList) {
		Map<String, Object> modulesData = new HashMap<>();
		for (DateWiseMetricsSeries record : dataList) {
			if (modulesData.get(record.getModuleName()) != null) {
				List<DateWiseMetricsSeries> moduleDataList = (List<DateWiseMetricsSeries>) modulesData
						.get(record.getModuleName());
				moduleDataList.add(record);
			} else {
				List<DateWiseMetricsSeries> initialList = new ArrayList<>();
				initialList.add(record);
				modulesData.put(record.getModuleName(), initialList);
			}
		}
		return modulesData;
	}

	/**
	 * 
	 * @param days
	 * @return
	 */

	private Long getTimeStamp(int days) {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calendar.add(Calendar.DAY_OF_MONTH, -days);
		return calendar.getTimeInMillis();
	}

	/**
	 * getISODateTime
	 * 
	 * @param lastScanned
	 * @return Date
	 */
	public Date getISODateTime(Long lastScanned) {
		if (lastScanned == null)
			return null;
		return new Date(lastScanned);
	}
}
