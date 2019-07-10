package com.capitalone.dashboard.custom;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.collector.MetricsProcessorSettings;
import com.capitalone.dashboard.dao.TestMetricsDAO;
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
import com.capitalone.dashboard.exec.model.vz.TestMetrics;
import com.capitalone.dashboard.exec.repository.vz.ApplicationDetailsRepository;
import com.capitalone.dashboard.exec.repository.vz.BuildingBlocksRepository;
import com.capitalone.dashboard.exec.repository.vz.CollectorStatusRepository;
import com.capitalone.dashboard.exec.repository.vz.DateWiseMetricsSeriesRepository;
import com.capitalone.dashboard.exec.repository.vz.ExecutiveComponentRepository;
import com.capitalone.dashboard.exec.repository.vz.ExecutiveSummaryListRepository;
import com.capitalone.dashboard.exec.repository.vz.MetricsDetailRepository;
import com.capitalone.dashboard.exec.repository.vz.PortfolioResponseRepository;
import com.capitalone.dashboard.utils.GenericMethods;
import com.capitalone.dashboard.utils.LinearRegression;
import com.mongodb.MongoClient;

/**
 * 
 * @author asthaaa
 *
 */
@SuppressWarnings("PMD")
@Component
public class TestAnalysis implements MetricsProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(TestAnalysis.class);

	private final TestMetricsDAO testMetricsDAO;
	private final MetricsProcessorSettings metricsSettings;
	private final DateWiseMetricsSeriesRepository dateWiseMetricsSeriesRepository;
	private final ApplicationDetailsRepository applicationDetailsRepository;
	private final ExecutiveComponentRepository executiveComponentRepository;
	private final BuildingBlocksRepository buildingBlocksRepository;
	private final CollectorStatusRepository collectorStatusRepository;
	private final MongoTemplate mongoTemplate;
	private final VastDetailsDAO vastDetailsDAO;
	private final MetricsDetailRepository metricsDetailRepository;
	private final GenericMethods genericMethods;
	private final ExecutiveSummaryListRepository executiveSummaryListRepository;
	private final PortfolioResponseRepository portfolioResponseRepository;

	private static final String TEST = "test";
	private static final String METRICSNAME = "metrics.metricsName";
	private static final String EXECUTIVEMETRICS = "executives_metrics";
	private static final String APPID = "appId";
	private static final String FAILED = "failed";
	private static final String SKIPPED = "skipped";
	private static final String TOTAL = "total";
	private static final String FRAMEWORK = "framework";
	private static final String SUCCESS = "success";

	/**
	 * 
	 * @param testMetricsDAO
	 * @param metricsSettings
	 * @param dateWiseMetricsSeriesRepository
	 * @param applicationDetailsRepository
	 * @param executiveComponentRepository
	 * @param collectorStatusRepository
	 * @param vastDetailsDAO
	 * @param mongoTemplate
	 * @param genericMethods
	 * @param executiveSummaryListRepository
	 * @param metricsDetailRepository
	 * @param portfolioResponseRepository
	 * @param buildingBlocksRepository
	 */
	@Autowired
	public TestAnalysis(TestMetricsDAO testMetricsDAO, MetricsProcessorSettings metricsSettings,
			DateWiseMetricsSeriesRepository dateWiseMetricsSeriesRepository,
			ApplicationDetailsRepository applicationDetailsRepository,
			ExecutiveComponentRepository executiveComponentRepository,
			CollectorStatusRepository collectorStatusRepository, VastDetailsDAO vastDetailsDAO,
			MongoTemplate mongoTemplate, MetricsDetailRepository metricsDetailRepository,
			GenericMethods genericMethods, BuildingBlocksRepository buildingBlocksRepository,
			ExecutiveSummaryListRepository executiveSummaryListRepository,
			PortfolioResponseRepository portfolioResponseRepository) {
		this.testMetricsDAO = testMetricsDAO;
		this.metricsSettings = metricsSettings;
		this.dateWiseMetricsSeriesRepository = dateWiseMetricsSeriesRepository;
		this.applicationDetailsRepository = applicationDetailsRepository;
		this.executiveComponentRepository = executiveComponentRepository;
		this.collectorStatusRepository = collectorStatusRepository;
		this.vastDetailsDAO = vastDetailsDAO;
		this.mongoTemplate = mongoTemplate;
		this.metricsDetailRepository = metricsDetailRepository;
		this.genericMethods = genericMethods;
		this.buildingBlocksRepository = buildingBlocksRepository;
		this.executiveSummaryListRepository = executiveSummaryListRepository;
		this.portfolioResponseRepository = portfolioResponseRepository;
	}

	/**
	 * 
	 * @return
	 */
	public boolean processDateWiseTrend() {
		LOG.info("Processing Test Date Wise Trend : date_wise_metrics . . . . ");
		MongoClient client = null;
		try {
			client = testMetricsDAO.getMongoClient();
			List<String> appIds = testMetricsDAO.getEntireAppList(client);
			Long timeStamp = metricsSettings.getDateRange();
			for (String appId : appIds) {
				processDateWiseTrendTestSeries(client, appId, timeStamp);
			}
		} catch (Exception e) {
			LOG.info("Error in Test Analysis Date Wise Trend :: " + e, e);
		} finally {
			if (client != null)
				client.close();
		}
		LOG.info("Completed Test Date Wise Trend : date_wise_metrics . . . . ");

		return true;
	}

	/**
	 * 
	 * @param client
	 * @param appId
	 * @param timeStamp
	 * @return
	 */

	private Boolean processDateWiseTrendTestSeries(MongoClient client, String appId, Long timeStamp) {
		try {

			Long ts = timeStamp;
			if (ts == null || ts == 0l) {
				ts = testMetricsDAO.getLast90daystd();
			}
			List<String> moduleList = testMetricsDAO.getListOfModules(client, appId, ts);
			for (String module : moduleList) {
				fetchAndUpdateForTestRevisedTime(client, appId, module, ts);
			}
		} catch (Exception e) {
			LOG.info("Error in Test Analysis Date Wise Trend Series :: " + e);
		}
		return true;
	}

	private Boolean fetchAndUpdateForTestRevisedTime(MongoClient client, String appId, String module, Long timeStamp) {
		try {
			Long presentTimeStamp = System.currentTimeMillis();
			Date revisedDate = new Date(timeStamp);
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String revisedStringDate = dateFormat.format(revisedDate);
			Date date = dateFormat.parse(revisedStringDate);
			Long revisedTimeStamp = date.getTime();
			String jobLink = null;
			while (revisedTimeStamp < presentTimeStamp) {

				DateWiseMetricsSeries dateWiseMetricsSeries = dateWiseMetricsSeriesRepository
						.findByAppIdAndModuleNameAndMetricsNameAndTimeStamp(appId, module, TEST, revisedTimeStamp);
				if (dateWiseMetricsSeries == null)
					dateWiseMetricsSeries = new DateWiseMetricsSeries();

				List<TestMetrics> testMetricsList = testMetricsDAO.getTestDetailsForModule(appId, module,
						revisedTimeStamp, revisedTimeStamp + 86399999, client);
				List<SeriesCount> counts = new ArrayList();
				for (TestMetrics testMetrics : testMetricsList) {
					SeriesCount count = new SeriesCount();
					count.setCount(1l);
					jobLink = getTestJobLink(testMetrics.getJobUrl(), jobLink);
					Map<String, String> label = new HashMap();
					label.put(SUCCESS, String.valueOf(testMetrics.getSuccess()));
					label.put(FAILED, String.valueOf(testMetrics.getFailed()));
					label.put(SKIPPED, String.valueOf(testMetrics.getSkipped()));
					label.put(TOTAL, String.valueOf(testMetrics.getTotal()));
					label.put("jobName", testMetrics.getJobName());
					label.put("buildNo", String.valueOf(testMetrics.getBuildNo()));
					label.put("testType", testMetrics.getTestType());
					label.put("env", testMetrics.getEnv());
					label.put("timestamp", String.valueOf(testMetrics.getTimestamp()));
					label.put(FRAMEWORK, testMetrics.getFramework());
					count.setLabel(label);
					counts.add(count);
				}

				if (!counts.isEmpty()) {
					dateWiseMetricsSeries.setAppId(appId);
					dateWiseMetricsSeries.setMetricsName(TEST);
					dateWiseMetricsSeries.setModuleName(module);
					dateWiseMetricsSeries.setTimeStamp(revisedTimeStamp);
					dateWiseMetricsSeries.setDateValue(dateFormat.format(revisedDate));
					dateWiseMetricsSeries.setCounts(counts);
					dateWiseMetricsSeries.setTeamId(jobLink);
					dateWiseMetricsSeriesRepository.save(dateWiseMetricsSeries);
				}

				revisedTimeStamp += 86400000;
			}

		} catch (Exception e) {
			LOG.info("Error in Test Analysis Date Wise Trend fetch And Update For Revised Time :: " + e);
		}
		return true;
	}

	private String getTestJobLink(String jobUrl, String jobLink) {
		if (jobUrl != null && !"".equalsIgnoreCase(jobLink))
			return jobUrl;
		return jobLink;
	}

	/**
	 * 
	 * @return
	 */
	public Boolean processExecutiveMetricsDetails() {
		LOG.info("Processing Test Details : executives_metrics . . . . ");
		MongoClient client = null;
		try {
			client = testMetricsDAO.getMongoClient();
			List<String> appIds = testMetricsDAO.getEntireAppList(client);
			for (String appId : appIds) {
				ApplicationDetails appDetails = applicationDetailsRepository.findByAppId(appId);
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
						TEST);
				if (executiveComponents == null)
					executiveComponents = new ExecutiveComponents();
				executiveComponents.setAppId(appId);
				executiveComponents.setAppName(appDetails != null ? appDetails.getAppName() : "");
				executiveComponents.setTeamBoardLink(appDetails != null ? appDetails.getTeamBoardLink() : "");
				executiveComponents.setMetrics(processMetrics(appId));
				executiveComponentRepository.save(executiveComponents);
			}

		} catch (Exception e) {
			LOG.info("Error in Test Analysis processTestDetails :: " + e);
		} finally {
			if (client != null)
				client.close();
		}
		LOG.info("Completed Test Details : executives_metrics . . . . ");
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
		CollectorStatus collectorStatus = collectorStatusRepository.findByType(CollectorType.Test);
		if (collectorStatus != null)
			lastUpdated = collectorStatus.getLastUpdated();
		List<ExecutiveModuleMetrics> modulesList = processModules(appId, lastUpdated);
		metrics.setLastUpdated(testMetricsDAO.getISODateTime(System.currentTimeMillis()));
		metrics.setLastScanned(testMetricsDAO.getISODateTime(System.currentTimeMillis()));
		metrics.setMetricsName(TEST);
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
					mappedValue.put(testMetricsDAO.getTimeStamp(entry.getKey()), entry.getValue());
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

	private List<ExecutiveModuleMetrics> processModules(String appId, Date lastUpdated) {
		List<ExecutiveModuleMetrics> moduleList = new ArrayList<>();
		List<DateWiseMetricsSeries> dateWiseMetricsSeriesList = dateWiseMetricsSeriesRepository
				.getThreeMonthsListWithAppId(appId, TEST, testMetricsDAO.getTimeStamp(90));
		if (dateWiseMetricsSeriesList != null && !dateWiseMetricsSeriesList.isEmpty()) {
			Map<String, Object> modulesData = getModuleList(dateWiseMetricsSeriesList);
			for (Map.Entry<String, Object> module : modulesData.entrySet()) {
				List<ExecutiveMetricsSeries> seriesData = processSeries(
						(List<DateWiseMetricsSeries>) module.getValue());
				ExecutiveModuleMetrics executiveModuleMetrics = new ExecutiveModuleMetrics();
				executiveModuleMetrics.setModuleName(module.getKey());
				executiveModuleMetrics.setLastScanned(lastUpdated);
				executiveModuleMetrics.setLastUpdated(testMetricsDAO.getISODateTime(System.currentTimeMillis()));
				executiveModuleMetrics.setSeries(seriesData);
				executiveModuleMetrics.setTrendSlope(processTrendSlopeForModule(seriesData));
				executiveModuleMetrics.setTeamId(getJobLink((List<DateWiseMetricsSeries>) module.getValue()));
				moduleList.add(executiveModuleMetrics);
			}
		}
		return moduleList;
	}

	private String getJobLink(List<DateWiseMetricsSeries> moduleData) {
		for (DateWiseMetricsSeries record : moduleData) {
			String jobLink = record.getTeamId();
			if (jobLink != null && !"".equalsIgnoreCase(jobLink))
				return jobLink;
		}
		return null;
	}

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

	private List<ExecutiveMetricsSeries> processSeries(List<DateWiseMetricsSeries> moduleData) {
		List<ExecutiveMetricsSeries> lastNintyDaysData = new ArrayList<>();
		for (DateWiseMetricsSeries record : moduleData) {
			ExecutiveMetricsSeries executiveMetricsSeries = new ExecutiveMetricsSeries();
			executiveMetricsSeries.setDaysAgo(testMetricsDAO.getDaysAgoValue(record.getTimeStamp()));
			executiveMetricsSeries.setTimeStamp(record.getTimeStamp());
			executiveMetricsSeries.setCounts(record.getCounts());
			lastNintyDaysData.add(executiveMetricsSeries);
		}
		return lastNintyDaysData;
	}

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
	 * @return
	 */
	public boolean removeUnusedTestDetails() {
		LOG.info("Removing Unused Test Details : executives_metrics . . . . ");
		MongoClient client = null;
		try {
			client = testMetricsDAO.getMongoClient();
			List<String> appIdList = vastDetailsDAO.getAllAppIds(client);
			if (appIdList != null && !appIdList.isEmpty()) {
				List<ExecutiveComponents> unusedDataList = executiveComponentRepository.getNotUsedAppIdList(appIdList,
						TEST);
				if (unusedDataList != null)
					executiveComponentRepository.delete(unusedDataList);
			}
		} catch (Exception e) {
			LOG.info("Error inside removeUnusedTestDetails " + e);
		} finally {
			if (client != null)
				client.close();
		}
		LOG.info("Completed Removing Unused Test Details : executives_metrics . . . . ");
		return true;
	}

	/**
	 * 
	 * @return
	 */
	public Boolean processMetricsDetailResponse() {
		LOG.info("Processing Test Details : app_metrics_details . . . . ");
		Query query = new Query(new Criteria().where(METRICSNAME).is(TEST));
		List<String> appIds = mongoTemplate.getCollection(EXECUTIVEMETRICS).distinct(APPID, query.getQueryObject());
		if (!appIds.isEmpty()) {
			for (String appId : appIds) {
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
						TEST);
				MetricsDetail metricDetailResponseProcessed = processMetricDetailResponse(executiveComponents);
				MetricsDetail metricDetailResponseStored = metricsDetailRepository
						.findByMetricLevelIdAndLevelAndType(appId, MetricLevel.PRODUCT, MetricType.TEST);
				if (metricDetailResponseStored != null) {
					metricsDetailRepository.delete(metricDetailResponseStored);
				}
				metricsDetailRepository.save(metricDetailResponseProcessed);
			}
		}
		LOG.info("Completed Test Details : app_metrics_details . . . . ");
		return true;
	}

	private MetricsDetail processMetricDetailResponse(ExecutiveComponents executiveComponents) {
		MetricsDetail metricDetailResponse = new MetricsDetail();
		metricDetailResponse.setMetricLevelId(executiveComponents.getAppId());
		metricDetailResponse.setType(MetricType.TEST);
		metricDetailResponse.setLevel(MetricLevel.PRODUCT);
		metricDetailResponse.setSummary(processMetricsSummary(executiveComponents));
		metricDetailResponse.setTimeSeries(processTimeSeries(executiveComponents));
		return metricDetailResponse;
	}

	private List<MetricTimeSeriesElement> processTimeSeries(ExecutiveComponents executiveComponents) {

		List<MetricTimeSeriesElement> timeSeries = new ArrayList<>();
		Map<Integer, Object> data = new HashMap<>();
		if (executiveComponents.getMetrics() != null) {
			List<ExecutiveMetrics> metrics = executiveComponents.getMetrics();
			for (ExecutiveMetrics metric : metrics) {
				if (metric.getMetricsName().equalsIgnoreCase(TEST)) {
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

			for (Entry<Integer, Object> item : data.entrySet()) {

				int totalTest = 0;
				int totalFailed = 0;
				int totalSkipped = 0;

				MetricTimeSeriesElement metricTimeSeriesElementResponse = new MetricTimeSeriesElement();
				List<MetricCount> counts = new ArrayList<>();
				MetricCount metricCountResponse = new MetricCount();

				metricTimeSeriesElementResponse.setDaysAgo(item.getKey());

				List<SeriesCount> getListOfData = (List<SeriesCount>) item.getValue();
				for (SeriesCount rec : getListOfData) {
					totalTest += Integer.valueOf(rec.getLabel().get(TOTAL));
					totalSkipped += Integer.valueOf(rec.getLabel().get(SKIPPED));
					totalFailed += Integer.valueOf(rec.getLabel().get(FAILED));
				}
				Map<String, String> label = new HashMap<>();
				label.put("tests", String.valueOf(totalTest - totalSkipped));
				label.put(SUCCESS, String.valueOf((totalTest - totalSkipped) - totalFailed));
				metricCountResponse.setValue(getListOfData.size());
				metricCountResponse.setLabel(label);
				counts.add(metricCountResponse);
				metricTimeSeriesElementResponse.setCounts(counts);
				timeSeries.add(metricTimeSeriesElementResponse);
			}
		}
		return timeSeries;
	}

	private MetricSummary processMetricsSummary(ExecutiveComponents executiveComponents) {
		MetricSummary metricSummaryResponse = new MetricSummary();
		List<ExecutiveModuleMetrics> modules = new ArrayList<>();
		List<String> teamIds = new ArrayList<>();
		metricSummaryResponse.setLastScanned(executiveComponents.getMetrics().get(0).getLastScanned());
		metricSummaryResponse.setLastUpdated(executiveComponents.getMetrics().get(0).getLastUpdated());

		if (executiveComponents.getMetrics() != null) {
			List<ExecutiveMetrics> metrics = executiveComponents.getMetrics();
			for (ExecutiveMetrics metric : metrics) {
				if (metric.getMetricsName().equalsIgnoreCase(TEST)) {
					List<ExecutiveModuleMetrics> modulesList = metric.getModules();
					for (ExecutiveModuleMetrics executiveModuleMetrics : modulesList) {
						if (!teamIds.contains(executiveModuleMetrics.getModuleName())) {
							teamIds.add(executiveModuleMetrics.getModuleName());
							modules.add(executiveModuleMetrics);
						}
					}
					metricSummaryResponse.setCounts(processMetricSummaryCounts(modules));
					metricSummaryResponse.setReportingComponents(testMetricsDAO.getReporting(modules));
					metricSummaryResponse.setTotalComponents(modulesList.size());
					metricSummaryResponse.setTrendSlope(executiveComponents.getMetrics().get(0).getTrendSlope());
					metricSummaryResponse
							.setAppCriticality(genericMethods.processAppCriticality(executiveComponents.getAppId()));
					Boolean dataAvailability = testMetricsDAO.checkForDataAvailability(modules);
					metricSummaryResponse.setLastScanned(new Date(System.currentTimeMillis()));
					metricSummaryResponse.setLastUpdated(new Date(System.currentTimeMillis()));
					metricSummaryResponse.setDataAvailable(dataAvailability);
					if (!dataAvailability)
						metricSummaryResponse.setConfMessage(testMetricsDAO.checkForDataAvailabilityStatus(modules));
				}
			}
		}
		metricSummaryResponse.setName(TEST);
		return metricSummaryResponse;
	}

	private List<MetricCount> processMetricSummaryCounts(List<ExecutiveModuleMetrics> modules) {

		List<MetricCount> metricCountResponseList = new ArrayList<>();

		int selTotalCount = 0;
		int selSuccessCount = 0;
		int selFailCount = 0;
		int selSkipCount = 0;

		int cucTotalCount = 0;
		int cucSuccessCount = 0;
		int cucFailCount = 0;
		int cucSkipCount = 0;

		int soapTotalCount = 0;
		int soapSuccessCount = 0;
		int soapFailCount = 0;
		int soapSkipCount = 0;

		int htmlTotalCount = 0;
		int htmlSuccessCount = 0;
		int htmlFailCount = 0;
		int htmlSkipCount = 0;
		
		int jmeterTotalCount = 0;
		int jmeterSuccessCount = 0;
		int jmeterFailCount = 0;
		int jmeterSkipCount = 0;

		if (!modules.isEmpty()) {
			for (ExecutiveModuleMetrics moduleMetric : modules) {
				List<ExecutiveMetricsSeries> seriesList = moduleMetric.getSeries();
				if (!seriesList.isEmpty()) {
					for (ExecutiveMetricsSeries series : seriesList) {
						List<SeriesCount> countsList = series.getCounts();
						if (!countsList.isEmpty()) {
							for (SeriesCount count : countsList) {
								if ("SELENIUM".equalsIgnoreCase(String.valueOf(count.getLabel().get(FRAMEWORK)))) {
									selTotalCount += Integer.valueOf(count.getLabel().get(TOTAL));
									selFailCount += Integer.valueOf(count.getLabel().get(FAILED));
									selSkipCount += Integer.valueOf(count.getLabel().get(SKIPPED));
								} else if ("Test".equalsIgnoreCase(String.valueOf(count.getLabel().get(FRAMEWORK)))) {
									cucTotalCount += Integer.valueOf(count.getLabel().get(TOTAL));
									cucFailCount += Integer.valueOf(count.getLabel().get(FAILED));
									cucSkipCount += Integer.valueOf(count.getLabel().get(SKIPPED));
								} else if ("SoapUI".equalsIgnoreCase(String.valueOf(count.getLabel().get(FRAMEWORK)))) {
									soapTotalCount += Integer.valueOf(count.getLabel().get(TOTAL));
									soapFailCount += Integer.valueOf(count.getLabel().get(FAILED));
									soapSkipCount += Integer.valueOf(count.getLabel().get(SKIPPED));
								} else if ("Html".equalsIgnoreCase(String.valueOf(count.getLabel().get(FRAMEWORK)))) {
									htmlTotalCount += Integer.valueOf(count.getLabel().get(TOTAL));
									htmlFailCount += Integer.valueOf(count.getLabel().get(FAILED));
									htmlSkipCount += Integer.valueOf(count.getLabel().get(SKIPPED));
								} else if ("Jmeter".equalsIgnoreCase(String.valueOf(count.getLabel().get(FRAMEWORK)))) {
									jmeterTotalCount += Integer.valueOf(count.getLabel().get(TOTAL));
									jmeterFailCount += Integer.valueOf(count.getLabel().get(FAILED));
									jmeterSkipCount += Integer.valueOf(count.getLabel().get(SKIPPED));
								}
							}
						}
					}
				}
			}

			selSuccessCount = (selTotalCount - selSkipCount) - selFailCount;
			cucSuccessCount = (cucTotalCount - cucSkipCount) - cucFailCount;
			soapSuccessCount = (soapTotalCount - soapSkipCount) - soapFailCount;
			htmlSuccessCount = (htmlTotalCount - htmlSkipCount) - htmlFailCount;
			jmeterSuccessCount = (jmeterTotalCount - jmeterSkipCount) - jmeterFailCount;

			Map<String, String> label = new HashMap<>();
			label.put("selTotalCount", String.valueOf(selTotalCount));
			label.put("selFailCount", String.valueOf(selFailCount));
			label.put("selSkipCount", String.valueOf(selSkipCount));
			label.put("selSuccessCount", String.valueOf(selSuccessCount));

			label.put("cucTotalCount", String.valueOf(cucTotalCount));
			label.put("cucFailCount", String.valueOf(cucFailCount));
			label.put("cucSkipCount", String.valueOf(cucSkipCount));
			label.put("cucSuccessCount", String.valueOf(cucSuccessCount));

			label.put("soapTotalCount", String.valueOf(soapTotalCount));
			label.put("soapFailCount", String.valueOf(soapFailCount));
			label.put("soapSkipCount", String.valueOf(soapSkipCount));
			label.put("soapSuccessCount", String.valueOf(soapSuccessCount));

			label.put("htmlTotalCount", String.valueOf(htmlTotalCount));
			label.put("htmlFailCount", String.valueOf(htmlFailCount));
			label.put("htmlSkipCount", String.valueOf(htmlSkipCount));
			label.put("htmlSuccessCount", String.valueOf(htmlSuccessCount));
			
			label.put("jmeterTotalCount", String.valueOf(jmeterTotalCount));
			label.put("jmeterFailCount", String.valueOf(jmeterFailCount));
			label.put("jmeterSkipCount", String.valueOf(jmeterSkipCount));
			label.put("jmeterSuccessCount", String.valueOf(jmeterSuccessCount));

			MetricCount metricCountResponseNormal = new MetricCount();
			metricCountResponseNormal.setLabel(label);
			metricCountResponseNormal.setValue(selTotalCount + cucTotalCount + soapTotalCount + htmlTotalCount);
			metricCountResponseList.add(metricCountResponseNormal);

			return metricCountResponseList;
		}

		Map<String, String> label = new HashMap<>();
		label.put("selTotalCount", String.valueOf(selTotalCount));
		label.put("selFailCount", String.valueOf(selFailCount));
		label.put("selSkipCount", String.valueOf(selSkipCount));
		label.put("selSuccessCount", String.valueOf(selSuccessCount));

		label.put("cucTotalCount", String.valueOf(cucTotalCount));
		label.put("cucFailCount", String.valueOf(cucFailCount));
		label.put("cucSkipCount", String.valueOf(cucSkipCount));
		label.put("cucSuccessCount", String.valueOf(cucSuccessCount));

		label.put("soapTotalCount", String.valueOf(soapTotalCount));
		label.put("soapFailCount", String.valueOf(soapFailCount));
		label.put("soapSkipCount", String.valueOf(soapSkipCount));
		label.put("soapSuccessCount", String.valueOf(soapSuccessCount));

		label.put("htmlTotalCount", String.valueOf(htmlTotalCount));
		label.put("htmlFailCount", String.valueOf(htmlFailCount));
		label.put("htmlSkipCount", String.valueOf(htmlSkipCount));
		label.put("htmlSuccessCount", String.valueOf(htmlSuccessCount));
		
		label.put("jmeterTotalCount", String.valueOf(jmeterTotalCount));
		label.put("jmeterFailCount", String.valueOf(jmeterFailCount));
		label.put("jmeterSkipCount", String.valueOf(jmeterSkipCount));
		label.put("jmeterSuccessCount", String.valueOf(jmeterSuccessCount));

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
		LOG.info("Processing Test Details : building_block_metrics . . . . ");
		try {
			Query query = new Query(new Criteria().where(METRICSNAME).is(TEST));
			List<String> appIds = mongoTemplate.getCollection(EXECUTIVEMETRICS).distinct(APPID, query.getQueryObject());
			if (!appIds.isEmpty()) {
				for (String appId : appIds) {
					BuildingBlocks buildingBlockMetricSummary = buildingBlocksRepository
							.findByMetricLevelIdAndMetricLevel(appId, MetricLevel.PRODUCT);
					if (buildingBlockMetricSummary == null)
						buildingBlockMetricSummary = new BuildingBlocks();

					ApplicationDetails appDetails = applicationDetailsRepository.findByAppId(appId);
					MetricsDetail metricDetailResponse = metricsDetailRepository
							.findByMetricLevelIdAndLevelAndType(appId, MetricLevel.PRODUCT, MetricType.TEST);
					if (metricDetailResponse != null && appDetails != null) {
						buildingBlockMetricSummary.setMetricLevelId(appId);
						buildingBlockMetricSummary.setLob(appDetails.getLob());
						buildingBlockMetricSummary.setName(appDetails.getAppName());
						buildingBlockMetricSummary.setMetricLevel(MetricLevel.PRODUCT);
						buildingBlockMetricSummary
								.setAppCriticality(metricDetailResponse.getSummary().getAppCriticality());
						List<MetricSummary> metricsResponseStored = buildingBlockMetricSummary
								.getMetrics();
						List<MetricSummary> metricsResponseProcessed = new ArrayList<>();
						if (metricsResponseStored != null && !metricsResponseStored.isEmpty()) {
							for (MetricSummary metricSummaryResponse : metricsResponseStored) {
								if (!metricSummaryResponse.getName().equalsIgnoreCase(TEST))
									metricsResponseProcessed.add(metricSummaryResponse);
							}
						}
						metricsResponseProcessed.add(metricDetailResponse.getSummary());
						buildingBlockMetricSummary.setMetrics(metricsResponseProcessed);
						buildingBlocksRepository.save(buildingBlockMetricSummary);
					}
				}
			}
			LOG.info("Completed Test Details : building_block_metrics . . . . ");
		} catch (Exception e) {
			LOG.info("processBuildingBlockMetrics Test Analysis Info :: " + e);
		}
		return true;
	}

	/**
	 * 
	 * @return
	 */
	public Boolean processExecutiveDetailsMetrics() {
		LOG.info("Processing Test Details : portfolio_metrics_details . . . . ");
		List<ExecutiveSummaryList> executiveSummaryLists = (List<ExecutiveSummaryList>) executiveSummaryListRepository
				.findAll();
		if (!executiveSummaryLists.isEmpty()) {
			for (ExecutiveSummaryList executiveSummaryList : executiveSummaryLists) {
				String eid = executiveSummaryList.getEid();
				MetricsDetail metricPortfolioDetailResponse = metricsDetailRepository
						.findByMetricLevelIdAndLevelAndType(eid, MetricLevel.PORTFOLIO, MetricType.TEST);
				if (metricPortfolioDetailResponse == null)
					metricPortfolioDetailResponse = new MetricsDetail();
				metricPortfolioDetailResponse.setType(MetricType.TEST);
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
		LOG.info("Completed Test Details : portfolio_metrics_details . . . . ");
		return true;
	}

	private List<MetricTimeSeriesElement> processExecutiveTimeSeries(List<String> configuredAppId) {
		List<MetricTimeSeriesElement> timeSeries = new ArrayList<>();
		if (configuredAppId != null && !configuredAppId.isEmpty()) {
			List<ExecutiveModuleMetrics> modules = new ArrayList<>();
			for (String appId : configuredAppId) {
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
						TEST);
				if (executiveComponents != null) {
					List<ExecutiveMetrics> executiveMetricsList = executiveComponents.getMetrics();
					if (executiveMetricsList != null) {
						for (ExecutiveMetrics metric : executiveMetricsList) {
							if (metric.getMetricsName().equalsIgnoreCase(TEST)) {
								List<ExecutiveModuleMetrics> modulesList = metric.getModules();
								for (ExecutiveModuleMetrics executiveModuleMetrics : modulesList) {
									modules.add(executiveModuleMetrics);
								}
							}
						}
					}
				}
			}

			Map<Integer, Object> data = new HashMap<>();
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

			for (Entry<Integer, Object> item : data.entrySet()) {

				int totalTest = 0;
				int totalFailed = 0;
				int totalSkipped = 0;

				MetricTimeSeriesElement metricTimeSeriesElementResponse = new MetricTimeSeriesElement();
				List<MetricCount> counts = new ArrayList<>();
				MetricCount metricCountResponse = new MetricCount();

				metricTimeSeriesElementResponse.setDaysAgo(item.getKey());

				List<SeriesCount> getListOfData = (List<SeriesCount>) item.getValue();
				for (SeriesCount rec : getListOfData) {
					totalTest += Integer.valueOf(rec.getLabel().get(TOTAL));
					totalSkipped += Integer.valueOf(rec.getLabel().get(SKIPPED));
					totalFailed += Integer.valueOf(rec.getLabel().get(FAILED));
				}
				Map<String, String> label = new HashMap<>();
				label.put("tests", String.valueOf(totalTest - totalSkipped));
				label.put(SUCCESS, String.valueOf((totalTest - totalSkipped) - totalFailed));
				metricCountResponse.setValue(getListOfData.size());
				metricCountResponse.setLabel(label);
				counts.add(metricCountResponse);
				metricTimeSeriesElementResponse.setCounts(counts);
				timeSeries.add(metricTimeSeriesElementResponse);
			}
		}
		return timeSeries;
	}

	private MetricSummary processExecutiveSummary(List<String> configuredAppId) {
		List<ExecutiveModuleMetrics> modules = new ArrayList<>();
		if (configuredAppId != null && !configuredAppId.isEmpty()) {
			for (String appId : configuredAppId) {
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
						TEST);
				if (executiveComponents != null) {
					List<ExecutiveMetrics> executiveMetricsList = executiveComponents.getMetrics();
					if (executiveMetricsList != null) {
						for (ExecutiveMetrics metric : executiveMetricsList) {
							if (metric.getMetricsName().equalsIgnoreCase(TEST)) {
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
			List<MetricCount> counts = processMetricSummaryCounts(modules); // processMetricSummaryCountsForModules
			metricSummaryResponse.setCounts(counts);
			metricSummaryResponse.setTrendSlope(getTrendSlopesForModules(modules));
			metricSummaryResponse.setName(TEST);
			metricSummaryResponse.setDataAvailable(testMetricsDAO.checkForDataAvailability(modules));
			return metricSummaryResponse;
		}
		return null;
	}

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
					mappedValue.put(testMetricsDAO.getTimeStamp(entry.getKey()), entry.getValue());
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

	private String getPortfolioId(String eid) {
		PortfolioResponse portfolioResponse = portfolioResponseRepository.findByEid(eid);
		return portfolioResponse == null ? null : portfolioResponse.getId().toString();
	}

	private Integer noOfAppsWithData(ExecutiveSummaryList executiveSummaryList) {
		int count = 0;
		List<String> appList = executiveSummaryList.getAppId();
		for (String appId : appList) {
			int countCheck = 0;
			ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId, TEST);
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
	 * @return
	 */
	public Boolean processComponentDetailsMetrics() {
		LOG.info("Processing Test Details : building_block_components . . . . ");
		Query query = new Query(new Criteria().where(METRICSNAME).is(TEST));
		List<String> appIds = mongoTemplate.getCollection(EXECUTIVEMETRICS).distinct(APPID, query.getQueryObject());
		if (!appIds.isEmpty()) {
			for (String appId : appIds) {
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
						TEST);
				List<BuildingBlocks> response = buildingBlocksRepository
						.findByMetricLevelIdAndMetricLevelAndMetricType(appId, MetricLevel.COMPONENT, MetricType.TEST);
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
						summaryResponse.setUrl(module.getTeamId());
						summaryResponse.setMetricLevel(MetricLevel.COMPONENT);
						summaryResponse.setMetricType(MetricType.TEST);
						buildingBlockResponse.add(summaryResponse);
					}
				}

				if (response != null)
					buildingBlocksRepository.delete(response);
				buildingBlocksRepository.save(buildingBlockResponse);

			}
		}
		LOG.info("Completed Test Details : building_block_components . . . . ");
		return true;
	}

	private List<MetricSummary> processComponentMetrics(ExecutiveModuleMetrics module) {
		List<MetricSummary> metricSummaryResponseList = new ArrayList<>();
		List<ExecutiveModuleMetrics> modules = new ArrayList<>();
		MetricSummary metricSummaryResponse = new MetricSummary();
		metricSummaryResponse.setName(TEST);
		metricSummaryResponse.setTotalComponents(1);
		metricSummaryResponse.setReportingComponents(1);
		metricSummaryResponse.setLastScanned(module.getLastScanned());
		metricSummaryResponse.setLastUpdated(module.getLastUpdated());
		modules.add(module);
		metricSummaryResponse.setCounts(processMetricSummaryCounts(modules));
		Boolean dataAvailabilityStatus = testMetricsDAO.checkForDataAvailability(modules);
		metricSummaryResponse.setDataAvailable(dataAvailabilityStatus);
		if (!dataAvailabilityStatus)
			metricSummaryResponse.setConfMessage(testMetricsDAO.checkForDataAvailabilityStatus(modules));
		metricSummaryResponseList.add(metricSummaryResponse);
		return metricSummaryResponseList;
	}

}