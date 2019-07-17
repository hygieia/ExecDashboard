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

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.mongodb.MongoClient;
import com.capitalone.dashboard.dao.BuildDetailsDAO;
import com.capitalone.dashboard.dao.VastDetailsDAO;
import com.capitalone.dashboard.exec.model.MetricCount;
import com.capitalone.dashboard.exec.model.MetricLevel;
import com.capitalone.dashboard.exec.model.MetricSummary;
import com.capitalone.dashboard.exec.model.MetricTimeSeriesElement;
import com.capitalone.dashboard.exec.model.MetricType;
import com.capitalone.dashboard.exec.model.ApplicationDetails;
import com.capitalone.dashboard.exec.model.Build;
import com.capitalone.dashboard.exec.model.BuildingBlocks;
import com.capitalone.dashboard.exec.model.CollectorStatus;
import com.capitalone.dashboard.exec.model.CollectorType;
import com.capitalone.dashboard.exec.model.DateWiseMetricsSeries;
import com.capitalone.dashboard.exec.model.ExecutiveComponents;
import com.capitalone.dashboard.exec.model.ExecutiveMetrics;
import com.capitalone.dashboard.exec.model.ExecutiveMetricsSeries;
import com.capitalone.dashboard.exec.model.ExecutiveModuleMetrics;
import com.capitalone.dashboard.exec.model.ExecutiveSummaryList;
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
import com.capitalone.dashboard.utils.LinearRegression;

/**
 * 
 * @author asthaaa
 *
 */
@Component
@SuppressWarnings("PMD")
public class BuildAnalysis implements MetricsProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(BuildAnalysis.class);
	private final BuildDetailsDAO buildDetailsDAO;
	private final DateWiseMetricsSeriesRepository dateWiseMetricsSeriesRepository;
	private final ApplicationDetailsRepository applicationDetailsRepository;
	private final ExecutiveComponentRepository executiveComponentRepository;
	private final CollectorStatusRepository collectorStatusRepository;
	private final VastDetailsDAO vastDetailsDAO;
	private final MongoTemplate mongoTemplate;
	private final MetricsDetailRepository metricsDetailRepository;
	private final ExecutiveSummaryListRepository executiveSummaryListRepository;
	private final PortfolioResponseRepository portfolioResponseRepository;
	private final BuildingBlocksRepository buildingBlocksRepository;
	private final GenericMethods genericMethods;

	private static final String BUILD = "build";
	private static final String METRICSNAME = "metrics.metricsName";
	private static final String EXECUTIVEMETRICS = "executives_metrics";
	private static final String APPID = "appId";
	private static final String DURATION = "duration";
	private static final String STATUS = "status";
	private static final String INPROGRESS = "InProgress";
	private static final String FREQUENCY = "frequency";
	private static final String AVGDURATION = "avgDuration";
	private static final String SUCCESSRATE = "successRate";
	private static final String TOTALJOBS = "totalJobs";

	/**
	 * 
	 * @param buildDetailsDAO
	 * @param dateWiseMetricsSeriesRepository
	 * @param applicationDetailsRepository
	 * @param executiveComponentRepository
	 * @param collectorStatusRepository
	 * @param vastDetailsDAO
	 * @param mongoTemplate
	 * @param metricsDetailRepository
	 * @param executiveSummaryListRepository
	 * @param portfolioResponseRepository
	 * @param buildingBlocksRepository
	 * @param genericMethods
	 */
	@Autowired
	public BuildAnalysis(BuildDetailsDAO buildDetailsDAO,
			DateWiseMetricsSeriesRepository dateWiseMetricsSeriesRepository,
			ApplicationDetailsRepository applicationDetailsRepository,
			ExecutiveComponentRepository executiveComponentRepository,
			CollectorStatusRepository collectorStatusRepository, VastDetailsDAO vastDetailsDAO,
			MongoTemplate mongoTemplate, MetricsDetailRepository metricsDetailRepository,
			ExecutiveSummaryListRepository executiveSummaryListRepository,
			PortfolioResponseRepository portfolioResponseRepository, BuildingBlocksRepository buildingBlocksRepository,
			GenericMethods genericMethods) {
		this.buildDetailsDAO = buildDetailsDAO;
		this.dateWiseMetricsSeriesRepository = dateWiseMetricsSeriesRepository;
		this.applicationDetailsRepository = applicationDetailsRepository;
		this.executiveComponentRepository = executiveComponentRepository;
		this.collectorStatusRepository = collectorStatusRepository;
		this.vastDetailsDAO = vastDetailsDAO;
		this.mongoTemplate = mongoTemplate;
		this.metricsDetailRepository = metricsDetailRepository;
		this.executiveSummaryListRepository = executiveSummaryListRepository;
		this.portfolioResponseRepository = portfolioResponseRepository;
		this.buildingBlocksRepository = buildingBlocksRepository;
		this.genericMethods = genericMethods;
	}

	/**
	 * 
	 * @return
	 */
	public Boolean processDateWiseTrend() {
		LOG.info("Processing Build Date Wise Trend : date_wise_metrics . . . . ");
		MongoClient client = null;
		try {
			client = buildDetailsDAO.getMongoClient();
			List<String> appIds = buildDetailsDAO.getEntireAppListForBuild(client, getLast90daystd());
			int count = 1;
			int total = appIds.size();
			for (String appId : appIds) {
				LOG.info(appId + ": " + count + "/" + total);
				count += 1;
				processDateWiseTrendBuildSeries(client, appId);
			}
		} catch (Exception e) {
			LOG.info("Error in Build Analysis Date Wise Trend :: " + e);
		} finally {
			if (client != null)
				client.close();
		}
		LOG.info("Completed Build Date Wise Trend : date_wise_metrics . . . . ");
		return true;

	}

	/**
	 * 
	 * @param client
	 * @param appId
	 * @return
	 */

	private Boolean processDateWiseTrendBuildSeries(MongoClient client, String appId) {
		try {
			Long revisedTimeStamp = getLast90daystd();
			List<String> moduleList = buildDetailsDAO.getListOfModules(client, appId, revisedTimeStamp);
			if (moduleList != null && !moduleList.isEmpty()) {
				LOG.info("No of Jobs Available For " + appId + " :: " + moduleList.size());
				for (String module : moduleList) {
					fetchAndUpdateForBuildRevisedTime(client, appId, module, revisedTimeStamp);
				}
			}
		} catch (Exception e) {
			LOG.info("Error in Build Analysis Date Wise Trend Series :: " + e);
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

	/**
	 * 
	 * @param client
	 * @param appId
	 * @param module
	 * @param timeStamp
	 * @return
	 */

	private Boolean fetchAndUpdateForBuildRevisedTime(MongoClient client, String appId, String module, Long timeStamp) {
		try {
			Long presentTimeStamp = System.currentTimeMillis();
			Date revisedDate = new Date(timeStamp);
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String revisedStringDate = dateFormat.format(revisedDate);
			Date date = dateFormat.parse(revisedStringDate);
			Long revisedTimeStamp = date.getTime();

			DateWiseMetricsSeries dateWiseMetricsSeriesLatest = dateWiseMetricsSeriesRepository
					.findByAppIdAndModuleNameAndMetricsNameOrderByTimeStampDesc(appId, module, BUILD);
			if (dateWiseMetricsSeriesLatest != null) {
				revisedTimeStamp = dateWiseMetricsSeriesLatest.getTimeStamp() - 172800000;
			}

			while (revisedTimeStamp < presentTimeStamp) {
				DateWiseMetricsSeries dateWiseMetricsSeries = dateWiseMetricsSeriesRepository
						.findByAppIdAndModuleNameAndMetricsNameAndTimeStamp(appId, module, BUILD, revisedTimeStamp);
				if (dateWiseMetricsSeries == null)
					dateWiseMetricsSeries = new DateWiseMetricsSeries();
				List<SeriesCount> seriesCountList = new ArrayList<>();
				List<Build> builds = buildDetailsDAO.getBuildsForModule(appId, module, revisedTimeStamp,
						revisedTimeStamp + 86399999, client);

				if (!builds.isEmpty()) {
					for (Build build : builds) {
						SeriesCount seriesCount = new SeriesCount();
						seriesCount.setCount(1l);
						Map<String, String> label = new HashMap<>();
						label.put("executionNo", fromString(build.getNumber()));
						label.put(DURATION, fromString(build.getDuration()));
						label.put("url", fromString(build.getJobUrl()));
						label.put("startDate", fromString(build.getStartTime()));
						label.put(STATUS, fromString(build.getBuildStatus()));
						seriesCount.setLabel(label);
						seriesCountList.add(seriesCount);

					}

					dateWiseMetricsSeries.setAppId(appId);
					dateWiseMetricsSeries.setMetricsName(BUILD);
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
			LOG.info("Error in Build Analysis Date Wise Trend fetch And Update For Revised Time :: " + e);
		}
		return true;
	}

	/**
	 * 
	 * @param obj
	 * @return
	 */
	public String fromString(Object obj) {
		return String.valueOf(obj);
	}

	/**
	 * 
	 * @return
	 */
	public Boolean processExecutiveMetricsDetails() {
		LOG.info("Processing Build Details : executives_metrics . . . . ");
		MongoClient client = null;
		try {
			client = buildDetailsDAO.getMongoClient();
			List<String> appIds = buildDetailsDAO.getEntireAppList(client);
			for (String appId : appIds) {
				ApplicationDetails appDetails = applicationDetailsRepository.findByAppId(appId);
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
						BUILD);
				if (executiveComponents == null)
					executiveComponents = new ExecutiveComponents();

				executiveComponents.setAppId(appId);
				executiveComponents.setAppName(appDetails != null ? appDetails.getAppName() : "");
				executiveComponents.setTeamBoardLink(appDetails != null ? appDetails.getTeamBoardLink() : "");
				executiveComponents.setMetrics(processMetrics(appId));
				executiveComponentRepository.save(executiveComponents);
			}

		} catch (Exception e) {
			LOG.info("Error in Build Analysis processBuildDetails :: " + e);
		} finally {
			if (client != null)
				client.close();
		}
		LOG.info("Completed Build Details : executives_metrics . . . . ");
		return true;
	}

	/**
	 * 
	 * @param appId
	 * @return
	 */

	private List<ExecutiveMetrics> processMetrics(String appId) {
		List<ExecutiveMetrics> metricsList = new ArrayList<>();
		ExecutiveMetrics metrics = new ExecutiveMetrics();
		Date lastUpdated = null;
		CollectorStatus collectorStatus = collectorStatusRepository.findByType(CollectorType.Build);
		if (collectorStatus != null)
			lastUpdated = collectorStatus.getLastUpdated();
		List<ExecutiveModuleMetrics> modulesList = processModules(appId, lastUpdated);
		metrics.setLastUpdated(getISODateTime(System.currentTimeMillis()));
		metrics.setLastScanned(getISODateTime(System.currentTimeMillis()));
		metrics.setMetricsName(BUILD);
		metrics.setModules(modulesList);
		metrics.setTrendSlope(processTrendSlope(modulesList));
		metricsList.add(metrics);
		return metricsList;
	}

	/**
	 * 
	 * @param appId
	 * @param lastUpdated
	 * @return
	 */

	private List<ExecutiveModuleMetrics> processModules(String appId, Date lastUpdated) {
		List<ExecutiveModuleMetrics> moduleList = new ArrayList<>();
		List<DateWiseMetricsSeries> dateWiseMetricsSeriesList = dateWiseMetricsSeriesRepository
				.getThreeMonthsListWithAppId(appId, BUILD, getTimeStamp(90));
		if (dateWiseMetricsSeriesList != null && !dateWiseMetricsSeriesList.isEmpty()) {
			Map<String, Object> modulesData = getModuleList(dateWiseMetricsSeriesList);
			for (Map.Entry<String, Object> module : modulesData.entrySet()) {
				List<ExecutiveMetricsSeries> seriesData = processSeries(
						(List<DateWiseMetricsSeries>) module.getValue());
				ExecutiveModuleMetrics executiveModuleMetrics = new ExecutiveModuleMetrics();
				executiveModuleMetrics.setModuleName(module.getKey());
				executiveModuleMetrics.setLastScanned(lastUpdated);
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
	 * @param moduleData
	 * @return
	 */

	private List<ExecutiveMetricsSeries> processSeries(List<DateWiseMetricsSeries> moduleData) {
		List<ExecutiveMetricsSeries> lastNintyDaysData = new ArrayList<>();
		for (DateWiseMetricsSeries record : moduleData) {
			ExecutiveMetricsSeries executiveMetricsSeries = new ExecutiveMetricsSeries();
			executiveMetricsSeries.setDaysAgo(getDaysAgoValue(record.getTimeStamp()));
			executiveMetricsSeries.setTimeStamp(record.getTimeStamp());
			executiveMetricsSeries.setCounts(record.getCounts());
			lastNintyDaysData.add(executiveMetricsSeries);
		}
		return lastNintyDaysData;
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
	 * @param days
	 * @return String
	 */
	public String getISODateTime(int days) {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calendar.add(Calendar.DAY_OF_MONTH, -days);
		return DatatypeConverter.printDateTime(calendar).substring(0, 11);
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

	/**
	 * removeUnusedBuildDetails
	 * 
	 * @return
	 */
	public boolean removeUnusedBuildDetails() {
		LOG.info("Removing Unused Build Details : executives_metrics . . . . ");
		MongoClient client = null;
		try {
			client = buildDetailsDAO.getMongoClient();
			List<String> appIdList = vastDetailsDAO.getAllAppIds(client);
			if (appIdList != null && !appIdList.isEmpty()) {
				List<ExecutiveComponents> unusedDataList = executiveComponentRepository.getNotUsedAppIdList(appIdList,
						BUILD);
				if (unusedDataList != null)
					executiveComponentRepository.delete(unusedDataList);
			}
		} catch (Exception e) {
			LOG.info("Error inside removeUnusedQualityDetails " + e);
		} finally {
			if (client != null)
				client.close();
		}
		LOG.info("Completed Removing Unused Build Details : executives_metrics . . . . ");
		return true;
	}

	/**
	 * processMetricsDetailResponse
	 * 
	 * @return
	 */
	public Boolean processMetricsDetailResponse() {
		LOG.info("Processing Build Details : app_metrics_details . . . . ");
		Query query = new Query(new Criteria().where(METRICSNAME).is(BUILD));
		List<String> appIds = mongoTemplate.getCollection(EXECUTIVEMETRICS).distinct(APPID, query.getQueryObject());
		if (!appIds.isEmpty()) {
			for (String appId : appIds) {
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
						BUILD);
				MetricsDetail metricDetailResponseProcessed = processMetricDetailResponse(executiveComponents);
				MetricsDetail metricDetailResponseStored = metricsDetailRepository
						.findByMetricLevelIdAndLevelAndType(appId, MetricLevel.PRODUCT, MetricType.BUILD);
				if (metricDetailResponseStored != null) {
					metricsDetailRepository.delete(metricDetailResponseStored);
				}
				metricsDetailRepository.save(metricDetailResponseProcessed);
			}
		}
		LOG.info("Completed Build Details : app_metrics_details . . . . ");
		return true;
	}

	/**
	 * 
	 * @param executiveComponents
	 * @return
	 */

	private MetricsDetail processMetricDetailResponse(ExecutiveComponents executiveComponents) {
		MetricsDetail metricDetailResponse = new MetricsDetail();
		metricDetailResponse.setMetricLevelId(executiveComponents.getAppId());
		metricDetailResponse.setType(MetricType.BUILD);
		metricDetailResponse.setLevel(MetricLevel.PRODUCT);
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
				if (metric.getMetricsName().equalsIgnoreCase(BUILD)) {
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

			int totalDuration = 0;
			int successCount = 0;
			int totalExecutions = 0;

			for (Entry<Integer, Object> item : data.entrySet()) {
				MetricTimeSeriesElement metricTimeSeriesElementResponse = new MetricTimeSeriesElement();
				metricTimeSeriesElementResponse.setDaysAgo(item.getKey());
				List<MetricCount> counts = new ArrayList<>();
				MetricCount metricCountResponse = new MetricCount();
				List<SeriesCount> getListOfData = (List<SeriesCount>) item.getValue();
				Map<String, Integer> uniqueJobobList = new HashMap<>();
				for (SeriesCount rec : getListOfData) {
					if (!INPROGRESS.equalsIgnoreCase(rec.getLabel().get(STATUS))) {
						if (rec.getLabel().get("url") == null)
							uniqueJobobList.put(rec.getLabel().get("url"), 1);
						else
							uniqueJobobList.put(rec.getLabel().get("url"),
									uniqueJobobList.get(rec.getLabel().get("url") + 1));

						totalExecutions += 1;
						totalDuration += Integer.valueOf(rec.getLabel().get(DURATION));
						if ("SUCCESS".equalsIgnoreCase(rec.getLabel().get(STATUS))) {
							successCount += 1;
						}
					}
				}

				Map<String, String> label = new HashMap<>();
				int frequency = uniqueJobobList.size();
				label.put(FREQUENCY, fromString(frequency > 0 && frequency < 1 ? 1 : frequency));
				label.put(AVGDURATION, fromString(totalExecutions > 0 ? totalDuration / totalExecutions : 0));
				label.put(SUCCESSRATE, fromString(totalExecutions > 0 ? (successCount * 100) / totalExecutions : 0));
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
				if (metric.getMetricsName().equalsIgnoreCase(BUILD)) {
					List<ExecutiveModuleMetrics> modulesList = metric.getModules();
					for (ExecutiveModuleMetrics executiveModuleMetrics : modulesList) {
						modules.add(executiveModuleMetrics);
					}
					metricSummaryResponse.setCounts(processMetricSummaryCounts(modules));
					metricSummaryResponse.setReportingComponents(getReporting(modules));
					metricSummaryResponse.setTotalComponents(modulesList.size());
					metricSummaryResponse.setTrendSlope(executiveComponents.getMetrics().get(0).getTrendSlope());
					metricSummaryResponse
							.setAppCriticality(genericMethods.processAppCriticality(executiveComponents.getAppId()));
					Boolean dataAvailability = checkForDataAvailability(modules);
					metricSummaryResponse.setDataAvailable(dataAvailability);
					if (!dataAvailability)
						metricSummaryResponse.setConfMessage(checkForDataAvailabilityStatus(modules));
				}
			}
		}
		metricSummaryResponse.setName(BUILD);
		return metricSummaryResponse;
	}

	/**
	 * 
	 * @param modules
	 * @return
	 */

	private Integer getReporting(List<ExecutiveModuleMetrics> modules) {
		int count = 0;
		for (ExecutiveModuleMetrics executiveModuleMetrics : modules) {
			if (checkIfDataAvailableInModule(executiveModuleMetrics)) {
				count += 1;
			}
		}
		return count;
	}

	/**
	 * 
	 * @param executiveModuleMetrics
	 * @return
	 */

	private boolean checkIfDataAvailableInModule(ExecutiveModuleMetrics executiveModuleMetrics) {
		for (ExecutiveMetricsSeries series : executiveModuleMetrics.getSeries()) {
			for (SeriesCount counts : series.getCounts()) {
				if (counts.getCount() > 0l) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param modules
	 * @return
	 */

	private List<MetricCount> processMetricSummaryCounts(List<ExecutiveModuleMetrics> modules) {

		List<MetricCount> metricCountResponseList = new ArrayList<>();
		long totalDuration = 0l;
		int totalExecutions = 0;
		int successCount = 0;
		List<String> jobList = new ArrayList<>();

		if (!modules.isEmpty()) {
			for (ExecutiveModuleMetrics moduleMetric : modules) {
				List<ExecutiveMetricsSeries> seriesList = moduleMetric.getSeries();
				if (!seriesList.isEmpty()) {
					for (ExecutiveMetricsSeries series : seriesList) {
						List<SeriesCount> countsList = series.getCounts();
						if (!countsList.isEmpty()) {
							for (SeriesCount count : countsList) {
								if (count != null && count.getLabel().get(STATUS).toString() != INPROGRESS) {
									totalDuration += Long.valueOf(count.getLabel().get(DURATION));
									totalExecutions += 1;

									if ("Success".equalsIgnoreCase(count.getLabel().get(STATUS)))
										successCount = successCount + 1;
									if (!jobList.contains(count.getLabel().get("url")))
										jobList.add(count.getLabel().get("url"));
								}
							}
						}
					}
				}
			}
			Map<String, String> label = new HashMap<>();
			double frequency = ((double) totalExecutions) / 90;
			label.put(FREQUENCY, fromString(frequency > 0 && frequency < 1 ? 1 : (int) frequency));
			label.put(AVGDURATION, fromString(totalExecutions > 0 ? totalDuration / totalExecutions : 0));
			label.put(SUCCESSRATE, fromString(totalExecutions > 0 ? (successCount * 100) / totalExecutions : 0));
			label.put(TOTALJOBS, fromString(modules.size()));

			MetricCount metricCountResponseNormal = new MetricCount();
			metricCountResponseNormal.setLabel(label);
			metricCountResponseNormal.setValue(totalExecutions);
			metricCountResponseList.add(metricCountResponseNormal);

			return metricCountResponseList;
		}

		Map<String, String> label = new HashMap<>();
		label.put(FREQUENCY, "NA");
		label.put(AVGDURATION, "NA");
		label.put(SUCCESSRATE, "0");
		label.put(TOTALJOBS, "0");
		MetricCount metricCountResponseNormal = new MetricCount();
		metricCountResponseNormal.setLabel(label);
		metricCountResponseNormal.setValue(0);
		metricCountResponseList.add(metricCountResponseNormal);
		return metricCountResponseList;
	}

	/**
	 * 
	 * @return
	 */
	public Boolean processBuildingBlockMetrics() {
		LOG.info("Processing Build Details : building_block_metrics . . . . ");
		try {
			Query query = new Query(new Criteria().where(METRICSNAME).is(BUILD));
			List<String> appIds = mongoTemplate.getCollection(EXECUTIVEMETRICS).distinct(APPID, query.getQueryObject());
			if (!appIds.isEmpty()) {
				for (String appId : appIds) {
					BuildingBlocks buildingBlockMetricSummaryResponse = buildingBlocksRepository
							.findByMetricLevelIdAndMetricLevel(appId, MetricLevel.PRODUCT);
					if (buildingBlockMetricSummaryResponse == null)
						buildingBlockMetricSummaryResponse = new BuildingBlocks();

					ApplicationDetails appDetails = applicationDetailsRepository.findByAppId(appId);
					MetricsDetail metricDetailResponse = metricsDetailRepository
							.findByMetricLevelIdAndLevelAndType(appId, MetricLevel.PRODUCT, MetricType.BUILD);
					if (metricDetailResponse != null && appDetails != null) {
						buildingBlockMetricSummaryResponse.setMetricLevelId(appId);
						buildingBlockMetricSummaryResponse.setLob(appDetails.getLob());
						buildingBlockMetricSummaryResponse.setName(appDetails.getAppName());
						buildingBlockMetricSummaryResponse.setMetricLevel(MetricLevel.PRODUCT);
						buildingBlockMetricSummaryResponse
								.setAppCriticality(metricDetailResponse.getSummary().getAppCriticality());
						List<MetricSummary> metricsResponseStored = buildingBlockMetricSummaryResponse.getMetrics();
						List<MetricSummary> metricsResponseProcessed = new ArrayList<>();
						if (metricsResponseStored != null && !metricsResponseStored.isEmpty()) {
							for (MetricSummary metricSummaryResponse : metricsResponseStored) {
								if (metricSummaryResponse.getName() != null
										&& !metricSummaryResponse.getName().equalsIgnoreCase(BUILD))
									metricsResponseProcessed.add(metricSummaryResponse);
							}
						}
						metricsResponseProcessed.add(metricDetailResponse.getSummary());
						buildingBlockMetricSummaryResponse.setMetrics(metricsResponseProcessed);
						buildingBlocksRepository.save(buildingBlockMetricSummaryResponse);
					}
				}
			}
			LOG.info("Completed Build Details : building_block_metrics . . . . ");
		} catch (Exception e) {
			LOG.info("processBuildingBlockMetrics Build Analysis Info :: " + e);
		}
		return true;
	}

	/**
	 * 
	 * @return
	 */
	public Boolean processExecutiveDetailsMetrics() {
		LOG.info("Processing Build Details : portfolio_metrics_details . . . . ");
		List<ExecutiveSummaryList> executiveSummaryLists = (List<ExecutiveSummaryList>) executiveSummaryListRepository
				.findAll();
		if (!executiveSummaryLists.isEmpty()) {
			for (ExecutiveSummaryList executiveSummaryList : executiveSummaryLists) {
				String eid = executiveSummaryList.getEid();
				MetricsDetail metricPortfolioDetailResponse = metricsDetailRepository
						.findByMetricLevelIdAndLevelAndType(eid, MetricLevel.PORTFOLIO, MetricType.BUILD);
				if (metricPortfolioDetailResponse == null)
					metricPortfolioDetailResponse = new MetricsDetail();
				metricPortfolioDetailResponse.setType(MetricType.BUILD);
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
		LOG.info("Completed Build Details : portfolio_metrics_details . . . . ");
		return true;
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
						BUILD);
				if (executiveComponents != null) {
					List<ExecutiveMetrics> executiveMetricsList = executiveComponents.getMetrics();
					if (executiveMetricsList != null) {
						for (ExecutiveMetrics metric : executiveMetricsList) {
							if (metric.getMetricsName().equalsIgnoreCase(BUILD)) {
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

			int totalDuration = 0;
			int successCount = 0;
			int totalExecutions = 0;

			for (Entry<Integer, Object> item : data.entrySet()) {
				MetricTimeSeriesElement metricTimeSeriesElementResponse = new MetricTimeSeriesElement();
				metricTimeSeriesElementResponse.setDaysAgo(item.getKey());
				List<MetricCount> counts = new ArrayList<>();
				MetricCount metricCountResponse = new MetricCount();

				List<SeriesCount> getListOfData = (List<SeriesCount>) item.getValue();
				Map<String, Integer> uniqueJobobList = new HashMap<>();
				for (SeriesCount rec : getListOfData) {
					if (!INPROGRESS.equalsIgnoreCase(rec.getLabel().get(STATUS))) {
						if (rec.getLabel().get("url") == null)
							uniqueJobobList.put(rec.getLabel().get("url"), 1);
						else
							uniqueJobobList.put(rec.getLabel().get("url"),
									uniqueJobobList.get(rec.getLabel().get("url") + 1));

						totalExecutions += 1;
						totalDuration += Integer.valueOf(rec.getLabel().get(DURATION));
						if ("SUCCESS".equalsIgnoreCase(rec.getLabel().get(STATUS))) {
							successCount += 1;
						}
					}
				}

				Map<String, String> label = new HashMap<>();
				int frequency = uniqueJobobList.size();
				label.put(FREQUENCY, fromString(frequency > 0 && frequency < 1 ? 1 : frequency));
				label.put(AVGDURATION, fromString(totalExecutions > 0 ? totalDuration / totalExecutions : 0));
				label.put(SUCCESSRATE, fromString(totalExecutions > 0 ? (successCount * 100) / totalExecutions : 0));
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
	 * @param configuredAppId
	 * @return
	 */

	private MetricSummary processExecutiveSummary(List<String> configuredAppId) {
		List<ExecutiveModuleMetrics> modules = new ArrayList<>();
		if (configuredAppId != null && !configuredAppId.isEmpty()) {
			for (String appId : configuredAppId) {
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
						BUILD);
				if (executiveComponents != null) {
					List<ExecutiveMetrics> executiveMetricsList = executiveComponents.getMetrics();
					if (executiveMetricsList != null) {
						for (ExecutiveMetrics metric : executiveMetricsList) {
							if (metric.getMetricsName().equalsIgnoreCase(BUILD)) {
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
			if (collectorStatusRepository.findByType(CollectorType.Build) != null)
				metricSummaryResponse
						.setLastScanned(collectorStatusRepository.findByType(CollectorType.Build).getLastUpdated());
			metricSummaryResponse.setLastUpdated(new Date(System.currentTimeMillis()));
			metricSummaryResponse.setLastScanned(new Date(System.currentTimeMillis()));
			metricSummaryResponse.setCounts(processMetricSummaryCounts(modules));
			metricSummaryResponse.setTrendSlope(getTrendSlopesForModules(modules));
			metricSummaryResponse.setName(BUILD);
			metricSummaryResponse.setDataAvailable(checkForDataAvailability(modules));
			return metricSummaryResponse;
		}
		return null;
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
			ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId, BUILD);
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
	 * @return
	 */
	public Boolean processComponentDetailsMetrics() {
		LOG.info("Processing Build Details : building_block_components . . . . ");
		Query query = new Query(new Criteria().where(METRICSNAME).is(BUILD));
		List<String> appIds = mongoTemplate.getCollection(EXECUTIVEMETRICS).distinct(APPID, query.getQueryObject());
		if (!appIds.isEmpty()) {
			for (String appId : appIds) {
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
						BUILD);
				List<BuildingBlocks> response = buildingBlocksRepository
						.findByMetricLevelIdAndMetricLevelAndMetricType(appId, MetricLevel.COMPONENT, MetricType.BUILD);
				List<BuildingBlocks> buildingBlockResponse = new ArrayList<>();
				List<ExecutiveModuleMetrics> modules = executiveComponents.getMetrics().get(0).getModules();
				ApplicationDetails appDetails = applicationDetailsRepository.findByAppId(appId);

				if (!modules.isEmpty() && appDetails != null) {
					for (ExecutiveModuleMetrics module : modules) {
						BuildingBlocks summaryResponse = new BuildingBlocks();
						String jobUrl = module.getModuleName();
						summaryResponse.setMetricLevelId(appId);
						summaryResponse.setLob(appDetails.getLob());
						summaryResponse.setMetrics(processComponentMetrics(module));
						summaryResponse.setName(getJobName(jobUrl));
						summaryResponse.setPoc(appDetails.getPoc());
						summaryResponse.setTotalComponents(1);
						summaryResponse.setTotalExpectedMetrics(1);
						summaryResponse.setUrl(jobUrl);
						summaryResponse.setMetricType(MetricType.BUILD);
						summaryResponse.setMetricLevel(MetricLevel.COMPONENT);
						buildingBlockResponse.add(summaryResponse);
					}
				}
				if (response != null)
					buildingBlocksRepository.delete(response);
				buildingBlocksRepository.save(buildingBlockResponse);
			}
		}
		LOG.info("Completed Build Details : building_block_components . . . . ");
		return true;
	}

	private String getJobName(String jobUrl) {
		String jobName = "";
		String previousJobPart = "";
		String[] jobNameSplit = jobUrl.split("/");
		for (String jobNameParts : jobNameSplit) {
			if ("job".equalsIgnoreCase(previousJobPart))
				jobName = jobNameParts;
			previousJobPart = jobNameParts;
		}
		return jobName;
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
		metricSummaryResponse.setName(BUILD);
		metricSummaryResponse.setTotalComponents(1);
		metricSummaryResponse.setReportingComponents(1);
		metricSummaryResponse.setLastScanned(module.getLastScanned());
		metricSummaryResponse.setLastUpdated(module.getLastUpdated());
		modules.add(module);
		metricSummaryResponse.setCounts(processMetricSummaryCounts(modules));
		Boolean dataAvailabilityStatus = checkForDataAvailability(modules);
		metricSummaryResponse.setDataAvailable(dataAvailabilityStatus);
		if (!dataAvailabilityStatus)
			metricSummaryResponse.setConfMessage(checkForDataAvailabilityStatus(modules));
		metricSummaryResponseList.add(metricSummaryResponse);
		return metricSummaryResponseList;
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

	private String checkForDataAvailabilityStatus(List<ExecutiveModuleMetrics> modules) {
		if (modules.isEmpty())
			return "Not Configured";
		return null;
	}

}
