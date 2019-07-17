package com.capitalone.dashboard.custom;

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
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.collector.MetricsProcessorSettings;
import com.capitalone.dashboard.dao.SecurityCsDetailsDAO;
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
import com.capitalone.dashboard.exec.model.Vast;
import com.capitalone.dashboard.exec.repository.ApplicationDetailsRepository;
import com.capitalone.dashboard.exec.repository.BuildingBlocksRepository;
import com.capitalone.dashboard.exec.repository.DateWiseMetricsSeriesRepository;
import com.capitalone.dashboard.exec.repository.ExecutiveComponentRepository;
import com.capitalone.dashboard.exec.repository.ExecutiveSummaryListRepository;
import com.capitalone.dashboard.exec.repository.MetricsDetailRepository;
import com.capitalone.dashboard.exec.repository.PortfolioResponseRepository;
import com.capitalone.dashboard.utils.GenericMethods;
import com.capitalone.dashboard.utils.LinearRegression;
import com.mongodb.MongoClient;

/**
 * Security Analysis
 * 
 * @param
 * @return
 * @author Guru
 */
@Component
@SuppressWarnings("PMD")
public class SecurityAnalysis implements MetricsProcessor {

	private final SecurityCsDetailsDAO securityDetailsDAO;
	private final VastDetailsDAO vastDetailsDAO;
	private final ExecutiveComponentRepository executiveComponentRepository;
	private final MongoTemplate mongoTemplate;
	private final MetricsDetailRepository metricsDetailRepository;
	private final BuildingBlocksRepository buildingBlocksRepository;
	private final ApplicationDetailsRepository applicationDetailsRepository;
	private final PortfolioResponseRepository portfolioResponseRepository;
	private final ExecutiveSummaryListRepository executiveSummaryListRepository;
	private final DateWiseMetricsSeriesRepository dateWiseMetricsSeriesRepository;
	private final MetricsProcessorSettings metricSettings;
	private final GenericMethods genericMethods;
	private static final Logger LOG = LoggerFactory.getLogger(SecurityAnalysis.class);

	private static final String CODEMAJORSTRING = "codeMajor";
	private static final String CODECRITICALSTRING = "codeCritical";
	private static final String CODEBLOCKERSTRING = "codeBlocker";

	private static final String PORTMAJORSTRING = "portMajor";
	private static final String PORTCRITICALSTRING = "portCritical";
	private static final String PORTBLOCKERSTRING = "portBlocker";

	private static final String WEBMAJORSTRING = "webMajor";
	private static final String WEBCRITICALSTRING = "webCritical";
	private static final String WEBBLOCKERSTRING = "webBlocker";

	private static final String BLACKDUCKMAJORSTRING = "blackDuckMajor";
	private static final String BLACKDUCKCRITICALSTRING = "blackDuckCritical";
	private static final String BLACKDUCKBLOCKERSTRING = "blackDuckBlocker";

	private static final String CRITICALOVERDUE = "overdueCritical";
	private static final String MEDIUMOVERDUE = "overdueMajor";
	private static final String HIGHOVERDUE = "overdueBlocker";

	private static final String SEVERITY = "severity";
	private static final String SECURITYVIOLATIONS = "security-violations";
	private static final String APPID = "appId";
	private static final String METRICNAME = "metrics.metricsName";
	private static final String EXECUTIVEMETRICS = "executives_metrics";

	/**
	 * SecurityAnalysis
	 * 
	 * @param securityDetailsDAO
	 *            ...
	 * @param executiveComponentRepository
	 * @param dashboardDetailsDAO
	 * @param mongoTemplate
	 * @param executiveSummaryListRepository
	 * @param metricsDetailRepository
	 * @param applicationDetailsRepository
	 * @param buildingBlocksRepository
	 * @param collectorStatusRepository
	 * @param portfolioResponseRepository
	 * @param vastDetailsDAO
	 * @param dateWiseMetricsSeriesRepository
	 * @param metricSettings
	 * @param genericMethods
	 * @return
	 */

	@Autowired
	public SecurityAnalysis(SecurityCsDetailsDAO securityDetailsDAO,
			ExecutiveComponentRepository executiveComponentRepository, MongoTemplate mongoTemplate,
			MetricsDetailRepository metricsDetailRepository, BuildingBlocksRepository buildingBlocksRepository,
			ApplicationDetailsRepository applicationDetailsRepository,
			PortfolioResponseRepository portfolioResponseRepository,
			ExecutiveSummaryListRepository executiveSummaryListRepository, VastDetailsDAO vastDetailsDAO,
			DateWiseMetricsSeriesRepository dateWiseMetricsSeriesRepository, MetricsProcessorSettings metricSettings,
			GenericMethods genericMethods) {
		this.securityDetailsDAO = securityDetailsDAO;
		this.executiveComponentRepository = executiveComponentRepository;
		this.mongoTemplate = mongoTemplate;
		this.metricsDetailRepository = metricsDetailRepository;
		this.buildingBlocksRepository = buildingBlocksRepository;
		this.applicationDetailsRepository = applicationDetailsRepository;
		this.portfolioResponseRepository = portfolioResponseRepository;
		this.executiveSummaryListRepository = executiveSummaryListRepository;
		this.vastDetailsDAO = vastDetailsDAO;
		this.dateWiseMetricsSeriesRepository = dateWiseMetricsSeriesRepository;
		this.metricSettings = metricSettings;
		this.genericMethods = genericMethods;
	}

	/**
	 * SecurityAnalysis processDatewiseDetails()
	 * 
	 * @return
	 */
	public Boolean processDatewiseDetails() {
		LOG.info("Processing Security Details : datewise_metrics . . . . ");
		MongoClient client = null;
		try {
			client = securityDetailsDAO.getMongoClient();
			Long timeStamp = metricSettings.getDateRange();
			List<String> appIdList = securityDetailsDAO.getConfiguredAppIds(client);

			for (String appId : appIdList) {

				Query query = new Query();
				query.addCriteria(new Criteria().where("appId").is(appId));
				query.addCriteria(new Criteria().where("metricsName").is(SECURITYVIOLATIONS));
				query.with(new Sort(new Order(Direction.DESC, "timeStamp")));

				List<DateWiseMetricsSeries> results = mongoTemplate.find(query, DateWiseMetricsSeries.class);

				if (!results.isEmpty() && results.get(0).getTimeStamp() != null)
					timeStamp = results.get(0).getTimeStamp();

				List<DateWiseMetricsSeries> securityData = securityDetailsDAO.getSecurityData(appId, client, timeStamp);

				if (!securityData.isEmpty()
						&& dateWiseMetricsSeriesRepository.findByAppIdAndMetricsNameAndDateValue(appId,
								SECURITYVIOLATIONS, securityData.get(0).getDateValue()) == null)
					dateWiseMetricsSeriesRepository.save(securityData);

			}

		} catch (Exception e) {
			LOG.info("Error while parsing Security Analysis file " + e);
		} finally {
			if (client != null)
				client.close();
		}
		LOG.info("Completed Security Details : datewise_metrics . . . . ");
		return true;
	}

	/**
	 * SecurityAnalysis processSecurityDetails()
	 * 
	 * @return
	 */
	public Boolean processExecutiveMetricsDetails() {

		LOG.info("Processing Security Details : executives_metrics . . . . ");
		MongoClient client = null;
		try {
			client = securityDetailsDAO.getMongoClient();
			List<String> appIdList = securityDetailsDAO.getConfiguredAppIds(client);

			for (String appId : appIdList) {

				ExecutiveComponents security = calculateExecutiveComponents(appId);
				ExecutiveComponents securityDataStored = executiveComponentRepository.findByAppIdAndMetric(appId,
						SECURITYVIOLATIONS);
				if (security != null) {
					if (securityDataStored != null) {
						executiveComponentRepository.delete(securityDataStored);
					}
					executiveComponentRepository.save(security);
				}

			}
		} catch (Exception e) {
			LOG.info("Error inside Security Analysis file " + e);
		} finally {
			if (client != null)
				client.close();
		}
		LOG.info("Completed Security Details : executives_metrics . . . . ");
		return true;
	}

	private ExecutiveComponents calculateExecutiveComponents(String appId) {
		long timeStamp = System.currentTimeMillis() - 7776000000l;

		List<ExecutiveModuleMetrics> modules = new ArrayList<>();
		ExecutiveMetrics exeMetrics = new ExecutiveMetrics();
		ExecutiveModuleMetrics executiveModuleMetrics = new ExecutiveModuleMetrics();
		List<ExecutiveMetrics> execumetricsList = new ArrayList<>();
		ExecutiveComponents security = new ExecutiveComponents();
		List<ExecutiveMetricsSeries> seriesList = new ArrayList<>();
		List<Integer> daysAgoList = new ArrayList<>();
		try {

			Sort sort = new Sort(new Order(Direction.ASC, "timeStamp"));
			List<DateWiseMetricsSeries> dateWiseSeriesList = dateWiseMetricsSeriesRepository
					.findByAppIdAndMetricsNameAndTimeStamp(appId, SECURITYVIOLATIONS, timeStamp, sort);

			for (DateWiseMetricsSeries dateWiseSeries : dateWiseSeriesList) {
				int daysAgo = getDaysAgoValue(dateWiseSeries.getTimeStamp());
				if (!daysAgoList.contains(daysAgo)) {
					daysAgoList.add(daysAgo);
					List<SeriesCount> countList = dateWiseSeries.getCounts();
					ExecutiveMetricsSeries series = new ExecutiveMetricsSeries();
					series.setDaysAgo(getDaysAgoValue(dateWiseSeries.getTimeStamp()));
					series.setTimeStamp(dateWiseSeries.getTimeStamp());
					series.setCounts(countList);
					seriesList.add(series);
				}
			}

			exeMetrics.setMetricsName(SECURITYVIOLATIONS);
			exeMetrics.setLastScanned(securityDetailsDAO.getISODateTime(System.currentTimeMillis()));
			exeMetrics.setLastUpdated(securityDetailsDAO.getISODateTime(System.currentTimeMillis()));
			executiveModuleMetrics.setModuleName(appId + " Security Module");
			executiveModuleMetrics.setSeries(seriesList);
			exeMetrics.setTrendSlope(securityDetailsDAO.getTrendSlope(seriesList));
			modules.add(executiveModuleMetrics);

			exeMetrics.setModules(modules);

			execumetricsList.add(exeMetrics);

			security.setAppName(null);
			security.setMetrics(execumetricsList);
			security.setAppId(appId);
		} catch (Exception e) {
			LOG.info("Error in Security Analysis file calculateExecutiveComponents function" + e);
		}

		return security;
	}

	private int getDaysAgoValue(Long timeStamp) {
		Date dateFromDB = new Date(timeStamp);
		Date presentDate = new Date();
		long diff = presentDate.getTime() - dateFromDB.getTime();
		return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}

	/**
	 * SecurityAnalysis processMetricsDetailResponse()
	 * 
	 * @return
	 **/
	public Boolean processMetricsDetailResponse() {
		LOG.info("Processing Security Details : app_metrics_details . . . . ");
		Query query = new Query(new Criteria().where(METRICNAME).is(SECURITYVIOLATIONS));

		List<String> appIds = mongoTemplate.getCollection(EXECUTIVEMETRICS).distinct(APPID, query.getQueryObject());

		if (!appIds.isEmpty()) {
			for (String appId : appIds) {
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
						SECURITYVIOLATIONS);
				MetricsDetail metricDetailResponseProcessed = processMetricDetailResponse(executiveComponents);
				MetricsDetail metricDetailResponseStored = metricsDetailRepository.findByMetricLevelIdAndLevelAndType(appId,
						MetricLevel.PRODUCT, MetricType.SECURITY_VIOLATIONS);
				if (metricDetailResponseStored != null) {
					metricsDetailRepository.delete(metricDetailResponseStored);
				}
				metricsDetailRepository.save(metricDetailResponseProcessed);

			}
		}
		LOG.info("Completed Security Details : app_metrics_details . . . . ");
		return true;
	}

	private MetricsDetail processMetricDetailResponse(ExecutiveComponents executiveComponents) {
		MetricsDetail metricDetailResponse = new MetricsDetail();
		metricDetailResponse.setMetricLevelId(executiveComponents.getAppId());
		metricDetailResponse.setType(MetricType.SECURITY_VIOLATIONS);
		metricDetailResponse.setLevel(MetricLevel.PRODUCT);
		metricDetailResponse.setSummary(processMetricsSummary(executiveComponents));
		metricDetailResponse.setTimeSeries(processTimeSeries(executiveComponents));
		return metricDetailResponse;
	}

	private List<MetricTimeSeriesElement> processTimeSeries(ExecutiveComponents executiveComponents) {
		List<MetricTimeSeriesElement> metricTimeSeriesElementResponseList = new ArrayList<>();
		List<ExecutiveMetricsSeries> executiveMetricsSeriesList = executiveComponents.getMetrics().get(0).getModules()
				.get(0).getSeries();
		for (ExecutiveMetricsSeries executiveMetricsSeries : executiveMetricsSeriesList) {
			MetricTimeSeriesElement metricTimeSeriesElementResponse = new MetricTimeSeriesElement();
			List<SeriesCount> seriesCountList = executiveMetricsSeries.getCounts();
			List<MetricCount> metricCountResponseList = new ArrayList<>();
			for (SeriesCount seriesCount : seriesCountList) {
				MetricCount metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(seriesCount.getLabel());
				metricCountResponse.setValue(seriesCount.getCount());
				metricCountResponseList.add(metricCountResponse);
			}
			metricTimeSeriesElementResponse.setCounts(metricCountResponseList);
			metricTimeSeriesElementResponse.setDaysAgo(executiveMetricsSeries.getDaysAgo());
			metricTimeSeriesElementResponseList.add(metricTimeSeriesElementResponse);
		}
		return metricTimeSeriesElementResponseList;
	}

	private MetricSummary processMetricsSummary(ExecutiveComponents executiveComponents) {
		MetricSummary metricSummaryResponse = new MetricSummary();
		metricSummaryResponse.setLastScanned(executiveComponents.getMetrics().get(0).getLastScanned());
		metricSummaryResponse.setLastUpdated(executiveComponents.getMetrics().get(0).getLastUpdated());
		metricSummaryResponse.setTotalComponents(1);
		metricSummaryResponse.setReportingComponents(1);
		metricSummaryResponse.setTrendSlope(executiveComponents.getMetrics().get(0).getTrendSlope());
		metricSummaryResponse.setAppCriticality(genericMethods.processAppCriticality(executiveComponents.getAppId()));
		metricSummaryResponse.setCounts(
				processMetricSummaryCounts(executiveComponents.getMetrics().get(0).getModules().get(0).getSeries()));
		metricSummaryResponse.setName(SECURITYVIOLATIONS);
		metricSummaryResponse.setDataAvailable(true);
		return metricSummaryResponse;
	}

	private List<MetricCount> processMetricSummaryCounts(List<ExecutiveMetricsSeries> executiveMetricsSeriesList) {
		List<MetricCount> metricCountResponseList = null;
		int daysAgo = 90;
		for (ExecutiveMetricsSeries executiveMetricsSeries : executiveMetricsSeriesList) {
			List<SeriesCount> seriesCountList = executiveMetricsSeries.getCounts();
			if (daysAgo > executiveMetricsSeries.getDaysAgo()) {
				metricCountResponseList = new ArrayList<>();
				for (SeriesCount seriesCount : seriesCountList) {
					MetricCount metricCountResponse = new MetricCount();
					metricCountResponse.setLabel(seriesCount.getLabel());
					metricCountResponse.setValue(seriesCount.getCount());
					metricCountResponseList.add(metricCountResponse);
				}
			}
		}
		return metricCountResponseList;
	}

	/**
	 * SecurityAnalysis processBuildingBlockMetrics
	 * 
	 * @return
	 */
	public Boolean processBuildingBlockMetrics() {
		LOG.info("Processing Security Details : building_block_metrics . . . . ");
		try {
			Query query = new Query(new Criteria().where(METRICNAME).is(SECURITYVIOLATIONS));
			List<String> appIds = mongoTemplate.getCollection(EXECUTIVEMETRICS).distinct(APPID, query.getQueryObject());

			List<BuildingBlocks> buildingBlockMetricSummaryResponseList = new ArrayList<>();

			if (!appIds.isEmpty()) {
				for (String appId : appIds) {
					BuildingBlocks buildingBlockMetricSummaryResponse = buildingBlocksRepository
							.findByMetricLevelIdAndMetricLevel(appId, MetricLevel.PRODUCT);
					if (buildingBlockMetricSummaryResponse == null) {
						buildingBlockMetricSummaryResponse = new BuildingBlocks();
					}

					ApplicationDetails appDetails = applicationDetailsRepository.findByAppId(appId);
					MetricsDetail metricDetailResponse = metricsDetailRepository.findByMetricLevelIdAndLevelAndType(appId,
							MetricLevel.PRODUCT, MetricType.SECURITY_VIOLATIONS);
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
								if (!(SECURITYVIOLATIONS).equalsIgnoreCase(metricSummaryResponse.getName()))
									metricsResponseProcessed.add(metricSummaryResponse);
							}
						}
						metricsResponseProcessed.add(metricDetailResponse.getSummary());
						buildingBlockMetricSummaryResponse.setMetrics(metricsResponseProcessed);
						buildingBlockMetricSummaryResponse.setTotalComponents(1);
						buildingBlockMetricSummaryResponse.setTotalExpectedMetrics(5);
						buildingBlockMetricSummaryResponse.setPoc(appDetails.getPoc());
						buildingBlockMetricSummaryResponse.setCustomField(appDetails.getVastId());
						buildingBlockMetricSummaryResponseList.add(buildingBlockMetricSummaryResponse);
					}
				}

				if (!buildingBlockMetricSummaryResponseList.isEmpty()) {
					buildingBlocksRepository.save(buildingBlockMetricSummaryResponseList);
				}
			}
		} catch (Exception e) {
			LOG.info("processBuildingBlockMetrics Security Analysis Info :: " + e);
		}
		LOG.info("Completed Security Details : building_block_metrics . . . . ");
		return true;
	}

	/**
	 * SecurityAnalysis processExecutiveDetailsMetrics
	 * 
	 * @return
	 */
	public Boolean processExecutiveDetailsMetrics() {
		LOG.info("Processing Security Details : portfolio_metrics_details . . . . ");
		List<ExecutiveSummaryList> executiveSummaryLists = (List<ExecutiveSummaryList>) executiveSummaryListRepository
				.findAll();

		if (!executiveSummaryLists.isEmpty()) {
			for (ExecutiveSummaryList executiveSummaryList : executiveSummaryLists) {
				String eid = executiveSummaryList.getEid();
				MetricsDetail metricPortfolioDetailResponse = metricsDetailRepository.findByMetricLevelIdAndLevelAndType(eid,
						MetricLevel.PORTFOLIO, MetricType.SECURITY_VIOLATIONS);
				if (metricPortfolioDetailResponse == null)
					metricPortfolioDetailResponse = new MetricsDetail();
				metricPortfolioDetailResponse.setType(MetricType.SECURITY_VIOLATIONS);
				metricPortfolioDetailResponse.setLevel(MetricLevel.PORTFOLIO);
				metricPortfolioDetailResponse.setMetricLevelId(eid);
				metricPortfolioDetailResponse.setCustomField(getPortfolioId(eid));
				metricPortfolioDetailResponse.setSummary(processExecutiveSummary(executiveSummaryList.getAppId()));
				metricPortfolioDetailResponse
						.setTimeSeries(processExecutiveTimeSeries(executiveSummaryList.getAppId()));
				if (metricPortfolioDetailResponse.getSummary() != null) {
					metricPortfolioDetailResponse.getSummary().setTotalComponents(executiveSummaryList.getTotalApps());
					metricPortfolioDetailResponse.getSummary()
							.setReportingComponents(executiveSummaryList.getTotalApps());
				}
				metricsDetailRepository.save(metricPortfolioDetailResponse);
			}
		}
		LOG.info("Completed Security Details : portfolio_metrics_details . . . . ");
		return true;
	}

	private List<MetricTimeSeriesElement> processExecutiveTimeSeries(List<String> configuredAppId) {
		List<MetricTimeSeriesElement> metricTimeSeriesElementResponseList = new ArrayList<>();
		if (configuredAppId != null && !configuredAppId.isEmpty()) {
			List<ExecutiveModuleMetrics> modules = new ArrayList<>();
			for (String appId : configuredAppId) {
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
						SECURITYVIOLATIONS);
				if (executiveComponents != null) {
					List<ExecutiveMetrics> executiveMetricsList = executiveComponents.getMetrics();
					if (executiveMetricsList != null) {
						List<ExecutiveModuleMetrics> modulesList = executiveMetricsList.get(0).getModules();
						for (ExecutiveModuleMetrics executiveModuleMetrics : modulesList) {
							modules.add(executiveModuleMetrics);
						}
					}
				}
			}

			Map<Integer, List<MetricCount>> seriesTimeList = new HashMap<>();
			if (!modules.isEmpty()) {
				for (ExecutiveModuleMetrics executiveModuleMetrics : modules) {
					List<ExecutiveMetricsSeries> executiveMetricsSeriesList = executiveModuleMetrics.getSeries();
					if (!executiveMetricsSeriesList.isEmpty()) {
						for (ExecutiveMetricsSeries executiveMetricsSeries : executiveMetricsSeriesList) {
							int days = executiveMetricsSeries.getDaysAgo();

							long codeMajor = 0;
							long codeCritical = 0;
							long codeBlocker = 0;
							long portMajor = 0;
							long portCritical = 0;
							long portBlocker = 0;
							long webMajor = 0;
							long webCritical = 0;
							long webBlocker = 0;
							long blackDuckMajor = 0;
							long blackDuckCritical = 0;
							long blackDuckBlocker = 0;
							long overdueCritical = 0;
							long overdueMedium = 0;
							long overdueHigh = 0;

							if (!seriesTimeList.containsKey(days)) {
								List<SeriesCount> countList = executiveMetricsSeries.getCounts();
								List<MetricCount> counts = new ArrayList<>();

								for (SeriesCount count : countList) {
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(CODEMAJORSTRING)) {
										codeMajor += count.getCount();
									}
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(CODEBLOCKERSTRING)) {
										codeBlocker += count.getCount();
									}
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(CODECRITICALSTRING)) {
										codeCritical += count.getCount();
									}
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(PORTMAJORSTRING)) {
										portMajor += count.getCount();
									}
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(PORTBLOCKERSTRING)) {
										portBlocker += count.getCount();
									}
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(PORTCRITICALSTRING)) {
										portCritical += count.getCount();
									}
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(WEBMAJORSTRING)) {
										webMajor += count.getCount();
									}
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(WEBBLOCKERSTRING)) {
										webBlocker += count.getCount();
									}
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(WEBCRITICALSTRING)) {
										webCritical += count.getCount();
									}
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(BLACKDUCKMAJORSTRING)) {
										blackDuckMajor += count.getCount();
									}
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(BLACKDUCKBLOCKERSTRING)) {
										blackDuckBlocker += count.getCount();
									}
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(BLACKDUCKCRITICALSTRING)) {
										blackDuckCritical += count.getCount();
									}
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(CRITICALOVERDUE)) {
										overdueCritical += count.getCount();
									}
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(MEDIUMOVERDUE)) {
										overdueMedium += count.getCount();
									}
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(HIGHOVERDUE)) {
										overdueHigh += count.getCount();
									}
								}

								Map<String, String> codeBlockerLabel = new HashMap<>();
								codeBlockerLabel.put(SEVERITY, CODEBLOCKERSTRING);

								Map<String, String> codeCriticalLabel = new HashMap<>();
								codeCriticalLabel.put(SEVERITY, CODECRITICALSTRING);

								Map<String, String> codeMajorLabel = new HashMap<>();
								codeMajorLabel.put(SEVERITY, CODEMAJORSTRING);

								Map<String, String> webBlockerLabel = new HashMap<>();
								webBlockerLabel.put(SEVERITY, WEBBLOCKERSTRING);

								Map<String, String> webCriticalLabel = new HashMap<>();
								webCriticalLabel.put(SEVERITY, WEBCRITICALSTRING);

								Map<String, String> webMajorLabel = new HashMap<>();
								webMajorLabel.put(SEVERITY, WEBMAJORSTRING);

								Map<String, String> portBlockerLabel = new HashMap<>();
								portBlockerLabel.put(SEVERITY, PORTBLOCKERSTRING);

								Map<String, String> portCriticalLabel = new HashMap<>();
								portCriticalLabel.put(SEVERITY, PORTCRITICALSTRING);

								Map<String, String> portMajorLabel = new HashMap<>();
								portMajorLabel.put(SEVERITY, PORTMAJORSTRING);

								Map<String, String> blackDuckBlockerLabel = new HashMap<>();
								blackDuckBlockerLabel.put(SEVERITY, BLACKDUCKBLOCKERSTRING);

								Map<String, String> blackDuckCriticalLabel = new HashMap<>();
								blackDuckCriticalLabel.put(SEVERITY, BLACKDUCKCRITICALSTRING);

								Map<String, String> blackDuckMajorLabel = new HashMap<>();
								blackDuckMajorLabel.put(SEVERITY, BLACKDUCKMAJORSTRING);

								Map<String, String> criticalOverDueMap = new HashMap<>();
								criticalOverDueMap.put(SEVERITY, CRITICALOVERDUE);

								Map<String, String> mediumOverDueMap = new HashMap<>();
								mediumOverDueMap.put(SEVERITY, MEDIUMOVERDUE);

								Map<String, String> highOverDueMap = new HashMap<>();
								highOverDueMap.put(SEVERITY, HIGHOVERDUE);

								MetricCount metricCountResponse = new MetricCount();
								metricCountResponse.setLabel(codeMajorLabel);
								metricCountResponse.setValue(codeMajor);
								counts.add(metricCountResponse);

								metricCountResponse = new MetricCount();
								metricCountResponse.setLabel(codeBlockerLabel);
								metricCountResponse.setValue(codeBlocker);
								counts.add(metricCountResponse);

								metricCountResponse = new MetricCount();
								metricCountResponse.setLabel(codeCriticalLabel);
								metricCountResponse.setValue(codeCritical);
								counts.add(metricCountResponse);

								metricCountResponse = new MetricCount();
								metricCountResponse.setLabel(portMajorLabel);
								metricCountResponse.setValue(portMajor);
								counts.add(metricCountResponse);

								metricCountResponse = new MetricCount();
								metricCountResponse.setLabel(portBlockerLabel);
								metricCountResponse.setValue(portBlocker);
								counts.add(metricCountResponse);

								metricCountResponse = new MetricCount();
								metricCountResponse.setLabel(portCriticalLabel);
								metricCountResponse.setValue(portCritical);
								counts.add(metricCountResponse);

								metricCountResponse = new MetricCount();
								metricCountResponse.setLabel(webMajorLabel);
								metricCountResponse.setValue(webMajor);
								counts.add(metricCountResponse);

								metricCountResponse = new MetricCount();
								metricCountResponse.setLabel(webBlockerLabel);
								metricCountResponse.setValue(webBlocker);
								counts.add(metricCountResponse);

								metricCountResponse = new MetricCount();
								metricCountResponse.setLabel(webCriticalLabel);
								metricCountResponse.setValue(webCritical);
								counts.add(metricCountResponse);

								metricCountResponse = new MetricCount();
								metricCountResponse.setLabel(blackDuckMajorLabel);
								metricCountResponse.setValue(blackDuckMajor);
								counts.add(metricCountResponse);

								metricCountResponse = new MetricCount();
								metricCountResponse.setLabel(blackDuckBlockerLabel);
								metricCountResponse.setValue(blackDuckBlocker);
								counts.add(metricCountResponse);

								metricCountResponse = new MetricCount();
								metricCountResponse.setLabel(blackDuckCriticalLabel);
								metricCountResponse.setValue(blackDuckCritical);
								counts.add(metricCountResponse);

								metricCountResponse = new MetricCount();
								metricCountResponse.setLabel(criticalOverDueMap);
								metricCountResponse.setValue(overdueCritical);
								counts.add(metricCountResponse);

								metricCountResponse = new MetricCount();
								metricCountResponse.setLabel(mediumOverDueMap);
								metricCountResponse.setValue(overdueMedium);
								counts.add(metricCountResponse);

								metricCountResponse = new MetricCount();
								metricCountResponse.setLabel(highOverDueMap);
								metricCountResponse.setValue(overdueHigh);
								counts.add(metricCountResponse);

								seriesTimeList.put(days, counts);

							} else {
								List<SeriesCount> countList = executiveMetricsSeries.getCounts();
								List<MetricCount> countsProcessed = new ArrayList<>();
								List<MetricCount> counts = seriesTimeList.get(days);

								for (MetricCount metricCountResponse : counts) {
									if (metricCountResponse.getLabel().get(SEVERITY)
											.equalsIgnoreCase(CODEMAJORSTRING)) {
										codeMajor += metricCountResponse.getValue();
									}
									if (metricCountResponse.getLabel().get(SEVERITY)
											.equalsIgnoreCase(CODEBLOCKERSTRING)) {
										codeBlocker += metricCountResponse.getValue();
									}
									if (metricCountResponse.getLabel().get(SEVERITY)
											.equalsIgnoreCase(CODECRITICALSTRING)) {
										codeCritical += metricCountResponse.getValue();
									}
									if (metricCountResponse.getLabel().get(SEVERITY)
											.equalsIgnoreCase(PORTMAJORSTRING)) {
										portMajor += metricCountResponse.getValue();
									}
									if (metricCountResponse.getLabel().get(SEVERITY)
											.equalsIgnoreCase(PORTBLOCKERSTRING)) {
										portBlocker += metricCountResponse.getValue();
									}
									if (metricCountResponse.getLabel().get(SEVERITY)
											.equalsIgnoreCase(PORTCRITICALSTRING)) {
										portCritical += metricCountResponse.getValue();
									}
									if (metricCountResponse.getLabel().get(SEVERITY).equalsIgnoreCase(WEBMAJORSTRING)) {
										webMajor += metricCountResponse.getValue();
									}
									if (metricCountResponse.getLabel().get(SEVERITY)
											.equalsIgnoreCase(WEBBLOCKERSTRING)) {
										webBlocker += metricCountResponse.getValue();
									}
									if (metricCountResponse.getLabel().get(SEVERITY)
											.equalsIgnoreCase(WEBCRITICALSTRING)) {
										webCritical += metricCountResponse.getValue();
									}

									if (metricCountResponse.getLabel().get(SEVERITY)
											.equalsIgnoreCase(BLACKDUCKMAJORSTRING)) {
										blackDuckMajor += metricCountResponse.getValue();
									}
									if (metricCountResponse.getLabel().get(SEVERITY)
											.equalsIgnoreCase(BLACKDUCKBLOCKERSTRING)) {
										blackDuckBlocker += metricCountResponse.getValue();
									}
									if (metricCountResponse.getLabel().get(SEVERITY)
											.equalsIgnoreCase(BLACKDUCKCRITICALSTRING)) {
										blackDuckCritical += metricCountResponse.getValue();
									}

									if (metricCountResponse.getLabel().get(SEVERITY)
											.equalsIgnoreCase(CRITICALOVERDUE)) {
										overdueCritical += metricCountResponse.getValue();
									}
									if (metricCountResponse.getLabel().get(SEVERITY).equalsIgnoreCase(MEDIUMOVERDUE)) {
										overdueMedium += metricCountResponse.getValue();
									}
									if (metricCountResponse.getLabel().get(SEVERITY).equalsIgnoreCase(HIGHOVERDUE)) {
										overdueHigh += metricCountResponse.getValue();
									}
								}

								for (SeriesCount count : countList) {
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(CODEMAJORSTRING)) {
										codeMajor += count.getCount();
									}
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(CODEBLOCKERSTRING)) {
										codeBlocker += count.getCount();
									}
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(CODECRITICALSTRING)) {
										codeCritical += count.getCount();
									}
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(PORTMAJORSTRING)) {
										portMajor += count.getCount();
									}
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(PORTBLOCKERSTRING)) {
										portBlocker += count.getCount();
									}
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(PORTCRITICALSTRING)) {
										portCritical += count.getCount();
									}
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(WEBMAJORSTRING)) {
										webMajor += count.getCount();
									}
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(WEBBLOCKERSTRING)) {
										webBlocker += count.getCount();
									}
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(WEBCRITICALSTRING)) {
										webCritical += count.getCount();
									}

									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(BLACKDUCKMAJORSTRING)) {
										blackDuckMajor += count.getCount();
									}
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(BLACKDUCKBLOCKERSTRING)) {
										blackDuckBlocker += count.getCount();
									}
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(BLACKDUCKCRITICALSTRING)) {
										blackDuckCritical += count.getCount();
									}

									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(CRITICALOVERDUE)) {
										overdueCritical += count.getCount();
									}
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(MEDIUMOVERDUE)) {
										overdueMedium += count.getCount();
									}
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(HIGHOVERDUE)) {
										overdueHigh += count.getCount();
									}
								}

								Map<String, String> codeBlockerLabel = new HashMap<>();
								codeBlockerLabel.put(SEVERITY, CODEBLOCKERSTRING);

								Map<String, String> codeCriticalLabel = new HashMap<>();
								codeCriticalLabel.put(SEVERITY, CODECRITICALSTRING);

								Map<String, String> codeMajorLabel = new HashMap<>();
								codeMajorLabel.put(SEVERITY, CODEMAJORSTRING);

								Map<String, String> webBlockerLabel = new HashMap<>();
								webBlockerLabel.put(SEVERITY, WEBBLOCKERSTRING);

								Map<String, String> webCriticalLabel = new HashMap<>();
								webCriticalLabel.put(SEVERITY, WEBCRITICALSTRING);

								Map<String, String> webMajorLabel = new HashMap<>();
								webMajorLabel.put(SEVERITY, WEBMAJORSTRING);

								Map<String, String> portBlockerLabel = new HashMap<>();
								portBlockerLabel.put(SEVERITY, PORTBLOCKERSTRING);

								Map<String, String> portCriticalLabel = new HashMap<>();
								portCriticalLabel.put(SEVERITY, PORTCRITICALSTRING);

								Map<String, String> portMajorLabel = new HashMap<>();
								portMajorLabel.put(SEVERITY, PORTMAJORSTRING);

								Map<String, String> blackDuckBlockerLabel = new HashMap<>();
								blackDuckBlockerLabel.put(SEVERITY, BLACKDUCKBLOCKERSTRING);

								Map<String, String> blackDuckCriticalLabel = new HashMap<>();
								blackDuckCriticalLabel.put(SEVERITY, BLACKDUCKCRITICALSTRING);

								Map<String, String> blackDuckMajorLabel = new HashMap<>();
								blackDuckMajorLabel.put(SEVERITY, BLACKDUCKMAJORSTRING);

								Map<String, String> criticalOverDueMap = new HashMap<>();
								criticalOverDueMap.put(SEVERITY, CRITICALOVERDUE);

								Map<String, String> mediumOverDueMap = new HashMap<>();
								mediumOverDueMap.put(SEVERITY, MEDIUMOVERDUE);

								Map<String, String> highOverDueMap = new HashMap<>();
								highOverDueMap.put(SEVERITY, HIGHOVERDUE);

								MetricCount metricCountResponse = new MetricCount();
								metricCountResponse.setLabel(codeMajorLabel);
								metricCountResponse.setValue(codeMajor);
								countsProcessed.add(metricCountResponse);

								metricCountResponse = new MetricCount();
								metricCountResponse.setLabel(codeBlockerLabel);
								metricCountResponse.setValue(codeBlocker);
								countsProcessed.add(metricCountResponse);

								metricCountResponse = new MetricCount();
								metricCountResponse.setLabel(codeCriticalLabel);
								metricCountResponse.setValue(codeCritical);
								countsProcessed.add(metricCountResponse);

								metricCountResponse = new MetricCount();
								metricCountResponse.setLabel(portMajorLabel);
								metricCountResponse.setValue(portMajor);
								countsProcessed.add(metricCountResponse);

								metricCountResponse = new MetricCount();
								metricCountResponse.setLabel(portBlockerLabel);
								metricCountResponse.setValue(portBlocker);
								countsProcessed.add(metricCountResponse);

								metricCountResponse = new MetricCount();
								metricCountResponse.setLabel(portCriticalLabel);
								metricCountResponse.setValue(portCritical);
								countsProcessed.add(metricCountResponse);

								metricCountResponse = new MetricCount();
								metricCountResponse.setLabel(webMajorLabel);
								metricCountResponse.setValue(webMajor);
								countsProcessed.add(metricCountResponse);

								metricCountResponse = new MetricCount();
								metricCountResponse.setLabel(webBlockerLabel);
								metricCountResponse.setValue(webBlocker);
								countsProcessed.add(metricCountResponse);

								metricCountResponse = new MetricCount();
								metricCountResponse.setLabel(webCriticalLabel);
								metricCountResponse.setValue(webCritical);
								countsProcessed.add(metricCountResponse);

								metricCountResponse = new MetricCount();
								metricCountResponse.setLabel(blackDuckMajorLabel);
								metricCountResponse.setValue(blackDuckMajor);
								counts.add(metricCountResponse);

								metricCountResponse = new MetricCount();
								metricCountResponse.setLabel(blackDuckBlockerLabel);
								metricCountResponse.setValue(blackDuckBlocker);
								counts.add(metricCountResponse);

								metricCountResponse = new MetricCount();
								metricCountResponse.setLabel(blackDuckCriticalLabel);
								metricCountResponse.setValue(blackDuckCritical);
								counts.add(metricCountResponse);

								metricCountResponse = new MetricCount();
								metricCountResponse.setLabel(criticalOverDueMap);
								metricCountResponse.setValue(overdueCritical);
								counts.add(metricCountResponse);

								metricCountResponse = new MetricCount();
								metricCountResponse.setLabel(mediumOverDueMap);
								metricCountResponse.setValue(overdueMedium);
								counts.add(metricCountResponse);

								metricCountResponse = new MetricCount();
								metricCountResponse.setLabel(highOverDueMap);
								metricCountResponse.setValue(overdueHigh);
								counts.add(metricCountResponse);

								seriesTimeList.replace(days, countsProcessed);
							}

						}
					}
				}

				if (!seriesTimeList.isEmpty()) {
					for (Entry<Integer, List<MetricCount>> entry : seriesTimeList.entrySet()) {
						MetricTimeSeriesElement metricTimeSeriesElementResponse = new MetricTimeSeriesElement();
						metricTimeSeriesElementResponse.setDaysAgo(entry.getKey());
						metricTimeSeriesElementResponse.setCounts(entry.getValue());
						metricTimeSeriesElementResponseList.add(metricTimeSeriesElementResponse);
					}
				}

			}

			return metricTimeSeriesElementResponseList;
		}
		return metricTimeSeriesElementResponseList;
	}

	private Long getTimeStamp(int days) {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calendar.add(Calendar.DAY_OF_MONTH, -days);
		return calendar.getTimeInMillis();
	}

	private MetricSummary processExecutiveSummary(List<String> configuredAppId) {
		MetricSummary metricSummaryResponse = new MetricSummary();
		List<ExecutiveModuleMetrics> modules = new ArrayList<>();
		if (configuredAppId != null && !configuredAppId.isEmpty()) {
			for (String appId : configuredAppId) {
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
						SECURITYVIOLATIONS);

				if (executiveComponents != null) {
					List<ExecutiveMetrics> executiveMetricsList = executiveComponents.getMetrics();
					if (executiveMetricsList != null) {
						for (ExecutiveMetrics metric : executiveMetricsList) {
							if (metric.getMetricsName().equalsIgnoreCase(SECURITYVIOLATIONS)) {
								List<ExecutiveModuleMetrics> modulesList = metric.getModules();
								for (ExecutiveModuleMetrics executiveModuleMetrics : modulesList) {
									modules.add(executiveModuleMetrics);
								}
							}
						}
					}
				}
			}

			metricSummaryResponse.setLastScanned(new Date(System.currentTimeMillis()));
			metricSummaryResponse.setLastUpdated(new Date(System.currentTimeMillis()));
			metricSummaryResponse.setCounts(processMetricsSummaryCounts(modules));
			metricSummaryResponse.setTrendSlope(getTrendSlopesForModules(modules));
			metricSummaryResponse.setName(SECURITYVIOLATIONS);
			metricSummaryResponse.setDataAvailable(true);
			return metricSummaryResponse;
		}
		return metricSummaryResponse;
	}

	private List<MetricCount> processMetricsSummaryCounts(List<ExecutiveModuleMetrics> modules) {

		List<MetricCount> metricCountResponseList = new ArrayList<>();
		if (!modules.isEmpty()) {
			long codeMajor = 0;
			long codeCritical = 0;
			long codeBlocker = 0;
			long portMajor = 0;
			long portCritical = 0;
			long portBlocker = 0;
			long webMajor = 0;
			long webCritical = 0;
			long webBlocker = 0;
			long blackDuckMajor = 0;
			long blackDuckCritical = 0;
			long blackDuckBlocker = 0;
			long overdueCritical = 0;
			long overdueMedium = 0;
			long overdueHigh = 0;

			for (ExecutiveModuleMetrics executiveModuleMetrics : modules) {
				List<ExecutiveMetricsSeries> executiveMetricsSeriesList = executiveModuleMetrics.getSeries();
				int size = executiveMetricsSeriesList.size();
				int daysAgo = executiveMetricsSeriesList.get(size - 1).getDaysAgo();

				if (!executiveMetricsSeriesList.isEmpty()) {
					for (ExecutiveMetricsSeries executiveMetricsSeries : executiveMetricsSeriesList) {
						if (executiveMetricsSeries.getDaysAgo() == daysAgo) {
							List<SeriesCount> countList = executiveMetricsSeries.getCounts();
							if (!countList.isEmpty()) {
								for (SeriesCount count : countList) {

									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(CODEMAJORSTRING)) {
										codeMajor += count.getCount();
									}
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(CODEBLOCKERSTRING)) {
										codeBlocker += count.getCount();
									}
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(CODECRITICALSTRING)) {
										codeCritical += count.getCount();
									}
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(PORTMAJORSTRING)) {
										portMajor += count.getCount();
									}
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(PORTBLOCKERSTRING)) {
										portBlocker += count.getCount();
									}
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(PORTCRITICALSTRING)) {
										portCritical += count.getCount();
									}
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(WEBMAJORSTRING)) {
										webMajor += count.getCount();
									}
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(WEBBLOCKERSTRING)) {
										webBlocker += count.getCount();
									}
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(WEBCRITICALSTRING)) {
										webCritical += count.getCount();
									}

									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(BLACKDUCKMAJORSTRING)) {
										blackDuckMajor += count.getCount();
									}
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(BLACKDUCKBLOCKERSTRING)) {
										blackDuckBlocker += count.getCount();
									}
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(BLACKDUCKCRITICALSTRING)) {
										blackDuckCritical += count.getCount();
									}

									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(CRITICALOVERDUE)) {
										overdueCritical += count.getCount();
									}
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(MEDIUMOVERDUE)) {
										overdueMedium += count.getCount();
									}
									if (count.getLabel().get(SEVERITY).equalsIgnoreCase(HIGHOVERDUE)) {
										overdueHigh += count.getCount();
									}
								}

							}
						}
					}
				}
			}

			Map<String, String> codeBlockerLabel = new HashMap<>();
			codeBlockerLabel.put(SEVERITY, CODEBLOCKERSTRING);

			Map<String, String> codeCriticalLabel = new HashMap<>();
			codeCriticalLabel.put(SEVERITY, CODECRITICALSTRING);

			Map<String, String> codeMajorLabel = new HashMap<>();
			codeMajorLabel.put(SEVERITY, CODEMAJORSTRING);

			Map<String, String> webBlockerLabel = new HashMap<>();
			webBlockerLabel.put(SEVERITY, WEBBLOCKERSTRING);

			Map<String, String> webCriticalLabel = new HashMap<>();
			webCriticalLabel.put(SEVERITY, WEBCRITICALSTRING);

			Map<String, String> webMajorLabel = new HashMap<>();
			webMajorLabel.put(SEVERITY, WEBMAJORSTRING);

			Map<String, String> portBlockerLabel = new HashMap<>();
			portBlockerLabel.put(SEVERITY, PORTBLOCKERSTRING);

			Map<String, String> portCriticalLabel = new HashMap<>();
			portCriticalLabel.put(SEVERITY, PORTCRITICALSTRING);

			Map<String, String> portMajorLabel = new HashMap<>();
			portMajorLabel.put(SEVERITY, PORTMAJORSTRING);

			Map<String, String> blackDuckBlockerLabel = new HashMap<>();
			blackDuckBlockerLabel.put(SEVERITY, BLACKDUCKBLOCKERSTRING);

			Map<String, String> blackDuckCriticalLabel = new HashMap<>();
			blackDuckCriticalLabel.put(SEVERITY, BLACKDUCKCRITICALSTRING);

			Map<String, String> blackDuckMajorLabel = new HashMap<>();
			blackDuckMajorLabel.put(SEVERITY, BLACKDUCKMAJORSTRING);

			Map<String, String> criticalOverDueMap = new HashMap<>();
			criticalOverDueMap.put(SEVERITY, CRITICALOVERDUE);

			Map<String, String> mediumOverDueMap = new HashMap<>();
			mediumOverDueMap.put(SEVERITY, MEDIUMOVERDUE);

			Map<String, String> highOverDueMap = new HashMap<>();
			highOverDueMap.put(SEVERITY, HIGHOVERDUE);

			MetricCount metricCountResponse = new MetricCount();
			metricCountResponse.setLabel(codeMajorLabel);
			metricCountResponse.setValue(codeMajor);
			metricCountResponseList.add(metricCountResponse);

			metricCountResponse = new MetricCount();
			metricCountResponse.setLabel(codeBlockerLabel);
			metricCountResponse.setValue(codeBlocker);
			metricCountResponseList.add(metricCountResponse);

			metricCountResponse = new MetricCount();
			metricCountResponse.setLabel(codeCriticalLabel);
			metricCountResponse.setValue(codeCritical);
			metricCountResponseList.add(metricCountResponse);

			metricCountResponse = new MetricCount();
			metricCountResponse.setLabel(portMajorLabel);
			metricCountResponse.setValue(portMajor);
			metricCountResponseList.add(metricCountResponse);

			metricCountResponse = new MetricCount();
			metricCountResponse.setLabel(portBlockerLabel);
			metricCountResponse.setValue(portBlocker);
			metricCountResponseList.add(metricCountResponse);

			metricCountResponse = new MetricCount();
			metricCountResponse.setLabel(portCriticalLabel);
			metricCountResponse.setValue(portCritical);
			metricCountResponseList.add(metricCountResponse);

			metricCountResponse = new MetricCount();
			metricCountResponse.setLabel(webMajorLabel);
			metricCountResponse.setValue(webMajor);
			metricCountResponseList.add(metricCountResponse);

			metricCountResponse = new MetricCount();
			metricCountResponse.setLabel(webBlockerLabel);
			metricCountResponse.setValue(webBlocker);
			metricCountResponseList.add(metricCountResponse);

			metricCountResponse = new MetricCount();
			metricCountResponse.setLabel(webCriticalLabel);
			metricCountResponse.setValue(webCritical);
			metricCountResponseList.add(metricCountResponse);

			metricCountResponse = new MetricCount();
			metricCountResponse.setLabel(blackDuckMajorLabel);
			metricCountResponse.setValue(blackDuckMajor);
			metricCountResponseList.add(metricCountResponse);

			metricCountResponse = new MetricCount();
			metricCountResponse.setLabel(blackDuckBlockerLabel);
			metricCountResponse.setValue(blackDuckBlocker);
			metricCountResponseList.add(metricCountResponse);

			metricCountResponse = new MetricCount();
			metricCountResponse.setLabel(blackDuckCriticalLabel);
			metricCountResponse.setValue(blackDuckCritical);
			metricCountResponseList.add(metricCountResponse);

			metricCountResponse = new MetricCount();
			metricCountResponse.setLabel(criticalOverDueMap);
			metricCountResponse.setValue(overdueCritical);
			metricCountResponseList.add(metricCountResponse);

			metricCountResponse = new MetricCount();
			metricCountResponse.setLabel(mediumOverDueMap);
			metricCountResponse.setValue(overdueMedium);
			metricCountResponseList.add(metricCountResponse);

			metricCountResponse = new MetricCount();
			metricCountResponse.setLabel(highOverDueMap);
			metricCountResponse.setValue(overdueHigh);
			metricCountResponseList.add(metricCountResponse);

			return metricCountResponseList;

		}
		return metricCountResponseList;
	}

	private Double getTrendSlopesForModules(List<ExecutiveModuleMetrics> modules) {

		Map<Integer, Long> seriesTimeList = new HashMap<>();
		if (!modules.isEmpty()) {
			for (ExecutiveModuleMetrics executiveModuleMetrics : modules) {
				List<ExecutiveMetricsSeries> executiveMetricsSeriesList = executiveModuleMetrics.getSeries();
				if (!executiveMetricsSeriesList.isEmpty()) {
					for (ExecutiveMetricsSeries executiveMetricsSeries : executiveMetricsSeriesList) {
						int days = executiveMetricsSeries.getDaysAgo();

						long totalViolations = 0;

						if (!seriesTimeList.containsKey(days)) {
							List<SeriesCount> countList = executiveMetricsSeries.getCounts();

							for (SeriesCount count : countList) {
								if (count.getLabel().get(SEVERITY).equalsIgnoreCase(CODEMAJORSTRING)) {
									totalViolations += count.getCount();
								}
								if (count.getLabel().get(SEVERITY).equalsIgnoreCase(CODEBLOCKERSTRING)) {
									totalViolations += count.getCount();
								}
								if (count.getLabel().get(SEVERITY).equalsIgnoreCase(CODECRITICALSTRING)) {
									totalViolations += count.getCount();
								}
								if (count.getLabel().get(SEVERITY).equalsIgnoreCase(PORTMAJORSTRING)) {
									totalViolations += count.getCount();
								}
								if (count.getLabel().get(SEVERITY).equalsIgnoreCase(PORTBLOCKERSTRING)) {
									totalViolations += count.getCount();
								}
								if (count.getLabel().get(SEVERITY).equalsIgnoreCase(PORTCRITICALSTRING)) {
									totalViolations += count.getCount();
								}
								if (count.getLabel().get(SEVERITY).equalsIgnoreCase(WEBMAJORSTRING)) {
									totalViolations += count.getCount();
								}
								if (count.getLabel().get(SEVERITY).equalsIgnoreCase(WEBBLOCKERSTRING)) {
									totalViolations += count.getCount();
								}
								if (count.getLabel().get(SEVERITY).equalsIgnoreCase(WEBCRITICALSTRING)) {
									totalViolations += count.getCount();
								}
								if (count.getLabel().get(SEVERITY).equalsIgnoreCase(BLACKDUCKMAJORSTRING)) {
									totalViolations += count.getCount();
								}
								if (count.getLabel().get(SEVERITY).equalsIgnoreCase(BLACKDUCKBLOCKERSTRING)) {
									totalViolations += count.getCount();
								}
								if (count.getLabel().get(SEVERITY).equalsIgnoreCase(BLACKDUCKCRITICALSTRING)) {
									totalViolations += count.getCount();
								}
							}

							seriesTimeList.put(days, totalViolations);
						} else {
							List<SeriesCount> countList = executiveMetricsSeries.getCounts();
							totalViolations += seriesTimeList.get(days);

							for (SeriesCount count : countList) {
								if (count.getLabel().get(SEVERITY).equalsIgnoreCase(CODEMAJORSTRING)) {
									totalViolations += count.getCount();
								}
								if (count.getLabel().get(SEVERITY).equalsIgnoreCase(CODEBLOCKERSTRING)) {
									totalViolations += count.getCount();
								}
								if (count.getLabel().get(SEVERITY).equalsIgnoreCase(CODECRITICALSTRING)) {
									totalViolations += count.getCount();
								}
								if (count.getLabel().get(SEVERITY).equalsIgnoreCase(PORTMAJORSTRING)) {
									totalViolations += count.getCount();
								}
								if (count.getLabel().get(SEVERITY).equalsIgnoreCase(PORTBLOCKERSTRING)) {
									totalViolations += count.getCount();
								}
								if (count.getLabel().get(SEVERITY).equalsIgnoreCase(PORTCRITICALSTRING)) {
									totalViolations += count.getCount();
								}
								if (count.getLabel().get(SEVERITY).equalsIgnoreCase(WEBMAJORSTRING)) {
									totalViolations += count.getCount();
								}
								if (count.getLabel().get(SEVERITY).equalsIgnoreCase(WEBBLOCKERSTRING)) {
									totalViolations += count.getCount();
								}
								if (count.getLabel().get(SEVERITY).equalsIgnoreCase(WEBCRITICALSTRING)) {
									totalViolations += count.getCount();
								}
								if (count.getLabel().get(SEVERITY).equalsIgnoreCase(BLACKDUCKMAJORSTRING)) {
									totalViolations += count.getCount();
								}
								if (count.getLabel().get(SEVERITY).equalsIgnoreCase(BLACKDUCKBLOCKERSTRING)) {
									totalViolations += count.getCount();
								}
								if (count.getLabel().get(SEVERITY).equalsIgnoreCase(BLACKDUCKCRITICALSTRING)) {
									totalViolations += count.getCount();
								}
							}

							seriesTimeList.replace(days, totalViolations);
						}
					}
				}
			}

			if (!seriesTimeList.isEmpty()) {
				Map<Long, Long> mappedValue = new HashMap<>();
				for (Entry<Integer, Long> entry : seriesTimeList.entrySet()) {
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

	private String getPortfolioId(String eid) {
		PortfolioResponse portfolioResponse = portfolioResponseRepository.findByEid(eid);
		return portfolioResponse == null ? null : portfolioResponse.getId().toString();
	}

	/**
	 * SecurityAnalysis processComponentDetailsMetrics
	 * 
	 * @return
	 */
	public Boolean processComponentDetailsMetrics() {
		LOG.info("Processing Security Details : building_block_components . . . . ");
		Query query = new Query(new Criteria().where(METRICNAME).is(SECURITYVIOLATIONS));
		List<String> appIds = mongoTemplate.getCollection(EXECUTIVEMETRICS).distinct(APPID, query.getQueryObject());

		if (!appIds.isEmpty()) {
			for (String appId : appIds) {
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
						SECURITYVIOLATIONS);
				List<BuildingBlocks> response = buildingBlocksRepository.findByMetricLevelIdAndMetricLevelAndMetricType(appId,
						MetricLevel.COMPONENT, MetricType.SECURITY_VIOLATIONS);
				List<BuildingBlocks> buildingBlockResponse = new ArrayList<>();

				List<ExecutiveModuleMetrics> modules = executiveComponents.getMetrics().get(0).getModules();
				ApplicationDetails appDetails = applicationDetailsRepository.findByAppId(appId);

				if (!modules.isEmpty() && appDetails != null) {
					for (ExecutiveModuleMetrics module : modules) {
						BuildingBlocks summaryResponse = new BuildingBlocks();
						summaryResponse.setMetricLevelId(appId);
						summaryResponse.setCustomField(processVastId(appId));
						summaryResponse.setLob(appDetails.getLob());
						summaryResponse.setMetrics(processComponentMetrics(module));
						summaryResponse.setName(module.getModuleName());
						summaryResponse.setPoc(appDetails.getPoc());
						summaryResponse.setTotalComponents(1);
						summaryResponse.setTotalExpectedMetrics(1);
						summaryResponse.setUrl(appDetails.getTeamBoardLink());
						summaryResponse.setMetricType(MetricType.SECURITY_VIOLATIONS);
						summaryResponse.setMetricLevel(MetricLevel.COMPONENT);
						buildingBlockResponse.add(summaryResponse);
					}
				}

				if (response != null)
					buildingBlocksRepository.delete(response);

				buildingBlocksRepository.save(buildingBlockResponse);

			}
		}
		LOG.info("Completed Security Details : building_block_components . . . . ");
		return true;

	}

	private List<MetricSummary> processComponentMetrics(ExecutiveModuleMetrics module) {
		List<MetricSummary> metricSummaryResponseList = new ArrayList<>();
		List<ExecutiveModuleMetrics> modules = new ArrayList<>();

		if (module.getSeries() != null && !module.getSeries().isEmpty()) {
			MetricSummary metricSummaryResponse = new MetricSummary();
			metricSummaryResponse.setName(SECURITYVIOLATIONS);
			metricSummaryResponse.setTotalComponents(1);
			metricSummaryResponse.setReportingComponents(1);
			metricSummaryResponse.setLastScanned(module.getLastScanned());
			metricSummaryResponse.setLastUpdated(module.getLastUpdated());
			modules.add(module);
			metricSummaryResponse.setDataAvailable(true);
			metricSummaryResponse.setCounts(processMetricsSummaryCounts(modules));
			metricSummaryResponseList.add(metricSummaryResponse);
		}
		return metricSummaryResponseList;
	}

	private String processVastId(String appId) {
		String result = null;
		MongoClient client = null;
		try {
			client = securityDetailsDAO.getMongoClient();
			List<Vast> vastDetails = securityDetailsDAO.getVastId(client, appId);
			result = vastDetails.get(0).getVastID();
			return result;
		} catch (Exception e) {
			LOG.info("Error inside Security Analysis file " + e);
		} finally {
			if (client != null)
				client.close();
		}
		return null;

	}

	/**
	 * SecurityAnalysis removeUnusedSecurityDetails()
	 * 
	 * @return Boolean
	 **/
	public Boolean removeUnusedSecurityDetails() {
		LOG.info("Removing Unused Security Details : executives_metrics . . . . ");
		MongoClient client = null;
		try {
			client = securityDetailsDAO.getMongoClient();
			List<String> appIdList = vastDetailsDAO.getAllAppIds(client);
			if (appIdList != null && !appIdList.isEmpty()) {
				List<ExecutiveComponents> securityDataList = executiveComponentRepository.getNotUsedAppIdList(appIdList,
						SECURITYVIOLATIONS);
				if (securityDataList != null)
					executiveComponentRepository.delete(securityDataList);
			}
		} catch (Exception e) {
			LOG.info("Error inside removeUnusedSecurityDetails " + e);
		} finally {
			if (client != null)
				client.close();
		}
		LOG.info("Completed Removing Unused Security Details : executives_metrics . . . . ");
		return true;
	}
}
