package com.capitalone.dashboard.custom;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.collector.MetricsProcessorSettings;
import com.capitalone.dashboard.dao.ProductionIncidentsDAO;
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
import com.capitalone.dashboard.exec.model.MTTR;
import com.capitalone.dashboard.exec.model.MetricsDetail;
import com.capitalone.dashboard.exec.model.OperationalMetrics;
import com.capitalone.dashboard.exec.model.PortfolioResponse;
import com.capitalone.dashboard.exec.model.SeriesCount;
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
 * ProductionIncidents data collection and analysis
 */
@Component
@SuppressWarnings("PMD")
public class ProductionIncidentsAnalysis implements MetricsProcessor {

	private final ProductionIncidentsDAO productionIncidentsDAO;
	private final VastDetailsDAO vastDetailsDAO;
	private final ExecutiveComponentRepository executiveComponentRepository;
	private final ExecutiveSummaryListRepository executiveSummaryListRepository;
	private final ApplicationDetailsRepository applicationDetailsRepository;
	private final MongoTemplate mongoTemplate;
	private final MetricsDetailRepository metricsDetailRepository;
	private final PortfolioResponseRepository portfolioResponseRepository;
	private final BuildingBlocksRepository buildingBlocksRepository;
	private final MetricsProcessorSettings metricsSettings;
	private final DateWiseMetricsSeriesRepository dateWiseMetricsSeriesRepository;
	private final GenericMethods genericMethods;
	private static final Logger LOG = LoggerFactory.getLogger(ProductionIncidentsAnalysis.class);

	private static final String PRODINCIDENTS = "production-incidents";
	private static final String APPID = "appId";
	private static final String SEVERITY = "severity";
	private static final String SEVERITY1 = "SEV1";
	private static final String SEVERITY2 = "SEV2";
	private static final String TIMETORESOLVE = "timeToResolve";
	private static final String METRICSNAME = "metrics.metricsName";
	private static final String EXECUTIVEMETRICS = "executives_metrics";
	private static final String IMPACTEDAPPS = "impactedApps";

	/**
	 * ProductionIncidentsAnalysis
	 * 
	 * @param executiveComponentRepository
	 * @param executiveSummaryListRepository
	 * @param productionIncidentsDAO
	 * @param applicationDetailsRepository
	 * @param mongoTemplate
	 * @param metricsDetailRepository
	 * @param portfolioResponseRepository
	 * @param buildingBlocksRepository
	 * @param vastDetailsDAO
	 * @param metricsSettings
	 * @param dateWiseMetricsSeriesRepository
	 * @param genericMethods
	 * @return
	 */
	@Autowired
	public ProductionIncidentsAnalysis(ExecutiveComponentRepository executiveComponentRepository,
			ExecutiveSummaryListRepository executiveSummaryListRepository,
			ProductionIncidentsDAO productionIncidentsDAO, ApplicationDetailsRepository applicationDetailsRepository,
			MongoTemplate mongoTemplate, MetricsDetailRepository metricsDetailRepository,
			PortfolioResponseRepository portfolioResponseRepository,
			BuildingBlocksRepository buildingBlocksRepository,
			VastDetailsDAO vastDetailsDAO, MetricsProcessorSettings metricsSettings,
			DateWiseMetricsSeriesRepository dateWiseMetricsSeriesRepository, GenericMethods genericMethods) {
		this.executiveComponentRepository = executiveComponentRepository;
		this.executiveSummaryListRepository = executiveSummaryListRepository;
		this.productionIncidentsDAO = productionIncidentsDAO;
		this.applicationDetailsRepository = applicationDetailsRepository;
		this.mongoTemplate = mongoTemplate;
		this.metricsDetailRepository = metricsDetailRepository;
		this.portfolioResponseRepository = portfolioResponseRepository;
		this.buildingBlocksRepository = buildingBlocksRepository;
		this.vastDetailsDAO = vastDetailsDAO;
		this.metricsSettings = metricsSettings;
		this.dateWiseMetricsSeriesRepository = dateWiseMetricsSeriesRepository;
		this.genericMethods = genericMethods;
	}

	/**
	 * function to collect data and store in executives_metrics collections
	 * 
	 * @return gets some value
	 */
	public Boolean processExecutiveMetricsDetails() {
		LOG.info("Processing Production Incidents Details : executives_metrics . . . . ");
		MongoClient client = null;
		try {
			client = productionIncidentsDAO.getMongoClient();
			List<String> appIds = productionIncidentsDAO.getEntireAppList(client);
			for (String appId : appIds) {
				ExecutiveComponents executiveData = executiveComponentRepository.findByAppIdAndMetric(appId,
						PRODINCIDENTS);
				if (executiveData == null)
					executiveData = new ExecutiveComponents();
				executiveData.setMetrics(getExecutiveMetricsList(appId));
				executiveData.setAppId(appId);
				ApplicationDetails appDetails = applicationDetailsRepository.findByAppId(appId);
				executiveData.setAppName(appDetails != null ? appDetails.getAppName() : "");
				executiveData.setTeamBoardLink(appDetails != null ? appDetails.getTeamBoardLink() : "");
				executiveComponentRepository.save(executiveData);
			}
		} catch (Exception e) {
			LOG.info("Error in processProductionIncidentsDetails of Production Incodent Analysis: " + e);
		} finally {
			if (client != null)
				client.close();
		}
		LOG.info("Completed Production Incidents Details : executives_metrics . . . . ");
		return true;
	}

	private List<ExecutiveMetrics> getExecutiveMetricsList(String appId) {
		List<ExecutiveMetrics> executiveMetricsList = new ArrayList<>();
		List<ExecutiveModuleMetrics> executiveModuleMetricsList = new ArrayList<>();
		List<ExecutiveMetricsSeries> executiveMetricsSeriesList = new ArrayList<>();
		ExecutiveMetrics executiveMetrics = new ExecutiveMetrics();
		Date date = new Date(System.currentTimeMillis());
		List<DateWiseMetricsSeries> dateWiseMetricsSeriesList = dateWiseMetricsSeriesRepository
				.getThreeMonthsListWithoutProject(appId, PRODINCIDENTS, getTimeStamp(90));
		for (DateWiseMetricsSeries dateWiseMetricsSeries : dateWiseMetricsSeriesList) {
			ExecutiveMetricsSeries executiveMetricsSeries = new ExecutiveMetricsSeries();
			executiveMetricsSeries.setDaysAgo(getDaysAgoValue(dateWiseMetricsSeries.getTimeStamp()));
			executiveMetricsSeries.setTimeStamp(dateWiseMetricsSeries.getTimeStamp());
			executiveMetricsSeries.setCounts(dateWiseMetricsSeries.getCounts());
			ExecutiveModuleMetrics executiveModuleMetrics = new ExecutiveModuleMetrics();
			executiveModuleMetrics.setLastScanned(new Date(dateWiseMetricsSeries.getTimeStamp()));
			executiveModuleMetrics.setLastUpdated(new Date(dateWiseMetricsSeries.getTimeStamp()));
			executiveModuleMetrics.setModuleName(dateWiseMetricsSeries.getModuleName());
			executiveModuleMetrics.setSeries(Arrays.asList(executiveMetricsSeries));
			executiveModuleMetrics.setTrendSlope(getTrendSlope(Arrays.asList(executiveMetricsSeries)));
			executiveModuleMetrics.setSeries(Arrays.asList(executiveMetricsSeries));
			executiveModuleMetrics.setTeamId(appId);
			executiveMetricsSeriesList.add(executiveMetricsSeries);
			executiveModuleMetricsList.add(executiveModuleMetrics);
		}
		executiveMetrics.setLastScanned(date);
		executiveMetrics.setLastUpdated(date);
		executiveMetrics.setMetricsName(PRODINCIDENTS);
		executiveMetrics.setTrendSlope(getTrendSlope(executiveMetricsSeriesList));
		executiveMetrics.setModules(executiveModuleMetricsList);
		executiveMetricsList.add(executiveMetrics);
		return executiveMetricsList;
	}

	private int getDaysAgoValue(Long timeStamp) {
		Date dateFromDB = new Date(timeStamp);
		Date presentDate = new Date();
		long diff = presentDate.getTime() - dateFromDB.getTime();
		return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}

	private int getSev(String crisis) {
		if ("SEV1".equalsIgnoreCase(crisis)) {
			return 1;
		} else {
			return 2;
		}
	}

	private double getTrendSlope(List<ExecutiveMetricsSeries> seriesList) {
		Map<Long, Integer> mappedValue = new HashMap<>();
		if (seriesList != null) {
			for (ExecutiveMetricsSeries seris : seriesList) {
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
		return 0;
	}

	/**
	 * function to collect data and store in app_metrics_details collections
	 * 
	 * @return check if done
	 */
	public Boolean processMetricsDetailResponse() {
		LOG.info("Processing Production Incidents Details : app_metrics_details . . . . ");
		Query query = new Query(new Criteria().where(METRICSNAME).is(PRODINCIDENTS));
		List<String> appIds = mongoTemplate.getCollection(EXECUTIVEMETRICS).distinct(APPID, query.getQueryObject());
		if (!appIds.isEmpty()) {
			for (String appId : appIds) {
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
						PRODINCIDENTS);
				MetricsDetail metricDetailResponseProcessed = processMetricDetailResponse(executiveComponents);
				MetricsDetail metricDetailResponseStored = metricsDetailRepository
						.findByMetricLevelIdAndLevelAndType(appId, MetricLevel.PRODUCT, MetricType.PRODUCTION_INCIDENTS);
				if (metricDetailResponseStored != null) {
					metricsDetailRepository.delete(metricDetailResponseStored);
				}
				metricsDetailRepository.save(metricDetailResponseProcessed);
			}
		}
		LOG.info("Completed Production Incidents Details : app_metrics_details . . . . ");
		return true;
	}

	private MetricsDetail processMetricDetailResponse(ExecutiveComponents executiveComponents) {
		MetricsDetail metricDetailResponse = new MetricsDetail();
		metricDetailResponse.setMetricLevelId(executiveComponents.getAppId());
		metricDetailResponse.setLevel(MetricLevel.PRODUCT);
		metricDetailResponse.setType(MetricType.PRODUCTION_INCIDENTS);
		metricDetailResponse.setSummary(processMetricsSummary(executiveComponents));
		metricDetailResponse.setTimeSeries(processTimeSeries(executiveComponents));
		return metricDetailResponse;
	}

	private MetricSummary processMetricsSummary(ExecutiveComponents executiveComponents) {
		MetricSummary metricSummaryResponse = new MetricSummary();
		metricSummaryResponse.setLastScanned(metricSummaryResponse.getLastScanned());
		metricSummaryResponse.setLastUpdated(metricSummaryResponse.getLastUpdated());
		metricSummaryResponse.setTotalComponents(1);
		metricSummaryResponse.setReportingComponents(1);
		metricSummaryResponse.setTrendSlope(executiveComponents.getMetrics().get(0).getTrendSlope());
		metricSummaryResponse.setAppCriticality(genericMethods.processAppCriticality(executiveComponents.getAppId()));
		metricSummaryResponse.setCounts(processMetricSummaryCounts(executiveComponents.getMetrics().get(0).getModules(),
				executiveComponents.getAppId()));
		metricSummaryResponse.setName(PRODINCIDENTS);
		metricSummaryResponse.setDataAvailable(true);
		return metricSummaryResponse;
	}

	private List<MetricCount> processMetricSummaryCounts(
			List<ExecutiveModuleMetrics> executiveMetricsSeriesList, String appId) {
		List<MetricCount> metricCountResponseList = new ArrayList<>();
		Long mtbf = productionIncidentsDAO.getMTBFDataforApp(appId);
		List<String> productionEvents = new ArrayList<>();
		List<MTTR> productionOutagesLabel = new ArrayList<>();
		List<MTTR> productionEventsLabel = new ArrayList<>();
		List<OperationalMetrics> operationalMetrics = new ArrayList<>();
		int daysAgo = 91;
		Long sev1Count = (long) 0;
		Long sev2Count = (long) 0;
		Long timeToResolveSev1 = (long) 0;
		Long timeToResolveSev2 = (long) 0;
		for (ExecutiveModuleMetrics moduleSeries : executiveMetricsSeriesList) {
			String crisisId = moduleSeries.getModuleName();
			if (!productionEvents.contains(crisisId)) {
				productionEvents.add(crisisId);
			}
			for (ExecutiveMetricsSeries executiveMetricsSeries : moduleSeries.getSeries()) {
				List<SeriesCount> seriesCountList = executiveMetricsSeries.getCounts();
				if (daysAgo > executiveMetricsSeries.getDaysAgo()) {

					for (SeriesCount seriesCount : seriesCountList) {

						if ("1".equals(seriesCount.getLabel().get(SEVERITY))) {
							sev1Count += seriesCount.getCount();
							timeToResolveSev1 += Long.valueOf(seriesCount.getLabel().get(TIMETORESOLVE).toString());
						} else {
							sev2Count += seriesCount.getCount();
							timeToResolveSev2 += Long.valueOf(seriesCount.getLabel().get(TIMETORESOLVE).toString());
						}
					}
				}
			}
		}

		List<MTTR> productionTotalEventsLabel = productionIncidentsDAO.getMTTRDetails(productionEvents);
		if (!productionTotalEventsLabel.isEmpty()) {
			List<MTTR> productionTotalOutageLabel = productionTotalEventsLabel;
			List<MTTR> productionTotalEventLabel = productionTotalEventsLabel;
			operationalMetrics = productionIncidentsDAO.getOperationalMetrics(productionTotalEventsLabel);
			productionOutagesLabel = getProcessedDetails(productionTotalOutageLabel, SEVERITY1, appId);
			productionEventsLabel = getProcessedDetails(productionTotalEventLabel, SEVERITY2, appId);

		}
		MetricCount metricCountResponse = new MetricCount();
		Map<String, String> sev1Label = new HashMap<>();
		sev1Label.put(SEVERITY, "1");
		sev1Label.put(TIMETORESOLVE, timeToResolveSev1.toString());
		metricCountResponse.setLabel(sev1Label);
		metricCountResponse.setValue(sev1Count);
		metricCountResponse.setLabelMttr(productionOutagesLabel);
		metricCountResponse.setOperationMetrics(operationalMetrics);
		metricCountResponseList.add(metricCountResponse);
		metricCountResponse = new MetricCount();
		Map<String, String> sev2Label = new HashMap<>();
		sev2Label.put(SEVERITY, "2");
		sev2Label.put(TIMETORESOLVE, timeToResolveSev2.toString());
		metricCountResponse.setLabel(sev2Label);
		metricCountResponse.setValue(sev2Count);
		metricCountResponse.setLabelMttr(productionEventsLabel);
		metricCountResponseList.add(metricCountResponse);
		metricCountResponse = new MetricCount();
		Map<String, String> mtbfLabel = new HashMap<>();
		mtbfLabel.put("type", "MTBF");
		metricCountResponse.setLabel(mtbfLabel);
		metricCountResponse.setValue(mtbf);
		metricCountResponseList.add(metricCountResponse);
		return metricCountResponseList;
	}

	private List<MTTR> getProcessedDetails(List<MTTR> productionTotalEventsLabel, String crisisLevel, String appId) {
		return productionTotalEventsLabel.stream()
				.filter(c -> (c.getCrisisLevel().equalsIgnoreCase(crisisLevel) && c.getAppId().equalsIgnoreCase(appId)))
				.collect(Collectors.toList());
	}

	private List<MetricTimeSeriesElement> processTimeSeries(ExecutiveComponents executiveComponents) {
		List<MetricTimeSeriesElement> metricTimeSeriesElementResponseList = new ArrayList<>();
		List<ExecutiveMetricsSeries> executiveMetricsSeriesList = new ArrayList<>();
		List<ExecutiveModuleMetrics> moduleSeries = executiveComponents.getMetrics().get(0).getModules();
		for (ExecutiveModuleMetrics module : moduleSeries) {
			executiveMetricsSeriesList.addAll(module.getSeries());
		}
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

	/**
	 * function to collect data and store in building_block_metrics collections
	 * 
	 * @return check if done
	 */
	public Boolean processBuildingBlockMetrics() {
		LOG.info("Processing Production Incidents Details : building_block_metrics . . . . ");
		Query query = new Query(new Criteria().where(METRICSNAME).is(PRODINCIDENTS));
		List<String> appIds = mongoTemplate.getCollection(EXECUTIVEMETRICS).distinct(APPID, query.getQueryObject());
		List<BuildingBlocks> buildingBlockMetricSummaryResponseList = new ArrayList<>();
		if (!appIds.isEmpty()) {
			for (String appId : appIds) {
				BuildingBlocks buildingBlockMetricSummaryResponse = buildingBlocksRepository
						.findByMetricLevelIdAndMetricLevel(appId, MetricLevel.PRODUCT);
				if (buildingBlockMetricSummaryResponse == null)
					buildingBlockMetricSummaryResponse = new BuildingBlocks();
				ApplicationDetails appDetails = applicationDetailsRepository.findByAppId(appId);
				MetricsDetail metricDetailResponse = metricsDetailRepository
						.findByMetricLevelIdAndLevelAndType(appId, MetricLevel.PRODUCT, MetricType.PRODUCTION_INCIDENTS);
				if (metricDetailResponse != null && appDetails != null) {
					buildingBlockMetricSummaryResponse.setMetricLevelId(appId);
					buildingBlockMetricSummaryResponse.setLob(appDetails.getLob());
					buildingBlockMetricSummaryResponse.setName(appDetails.getAppName());
					buildingBlockMetricSummaryResponse
							.setAppCriticality(metricDetailResponse.getSummary().getAppCriticality());
					List<MetricSummary> metricsResponseStored = buildingBlockMetricSummaryResponse.getMetrics();
					List<MetricSummary> metricsResponseProcessed = new ArrayList<>();
					if (metricsResponseStored != null && !metricsResponseStored.isEmpty()) {
						for (MetricSummary metricSummaryResponse : metricsResponseStored) {
							if (!PRODINCIDENTS.equalsIgnoreCase(metricSummaryResponse.getName()))
								metricsResponseProcessed.add(metricSummaryResponse);
						}
					}
					metricsResponseProcessed.add(metricDetailResponse.getSummary());
					buildingBlockMetricSummaryResponse.setMetrics(metricsResponseProcessed);
					buildingBlockMetricSummaryResponse.setTotalComponents(1);
					buildingBlockMetricSummaryResponse.setTotalExpectedMetrics(5);
					buildingBlockMetricSummaryResponse.setPoc(appDetails.getPoc());
					buildingBlockMetricSummaryResponse.setMetricLevel(MetricLevel.PRODUCT);
					buildingBlockMetricSummaryResponseList.add(buildingBlockMetricSummaryResponse);
				}
			}

			if (!buildingBlockMetricSummaryResponseList.isEmpty())
				buildingBlocksRepository.save(buildingBlockMetricSummaryResponseList);
		}
		LOG.info("Completed Production Incidents Details : building_block_metrics . . . . ");
		return true;
	}

	/**
	 * function to collect data and store in portfolio_metrics_details
	 * collections
	 * 
	 * @return check if done
	 */
	public Boolean processExecutiveDetailsMetrics() {
		LOG.info("Processing Production Incidents Details : portfolio_metrics_details . . . . ");
		List<ExecutiveSummaryList> executiveSummaryLists = (List<ExecutiveSummaryList>) executiveSummaryListRepository
				.findAll();
		if (!executiveSummaryLists.isEmpty()) {
			for (ExecutiveSummaryList executiveSummaryList : executiveSummaryLists) {
				String eid = executiveSummaryList.getEid();
				MetricsDetail metricPortfolioDetailResponse = metricsDetailRepository
						.findByMetricLevelIdAndLevelAndType(eid, MetricLevel.PORTFOLIO, MetricType.PRODUCTION_INCIDENTS);
				if (metricPortfolioDetailResponse == null)
					metricPortfolioDetailResponse = new MetricsDetail();
				metricPortfolioDetailResponse.setType(MetricType.PRODUCTION_INCIDENTS);
				metricPortfolioDetailResponse.setMetricLevelId(eid);
				metricPortfolioDetailResponse.setLevel(MetricLevel.PORTFOLIO);
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
		LOG.info("Completed Production Incidents Details : portfolio_metrics_details . . . . ");
		return true;
	}

	private List<MetricTimeSeriesElement> processExecutiveTimeSeries(List<String> configuredAppId) {
		List<String> modulesName = new ArrayList<>();
		if (configuredAppId != null && !configuredAppId.isEmpty()) {
			List<MetricTimeSeriesElement> metricTimeSeriesElementResponseList = new ArrayList<>();
			List<ExecutiveModuleMetrics> modules = new ArrayList<>();
			for (String appId : configuredAppId) {
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
						PRODINCIDENTS);
				if (executiveComponents != null) {
					List<ExecutiveMetrics> executiveMetricsList = executiveComponents.getMetrics();
					if (executiveMetricsList != null) {
						for (ExecutiveMetrics executiveMetrics : executiveMetricsList) {
							if (PRODINCIDENTS.equals(executiveMetrics.getMetricsName())) {
								List<ExecutiveModuleMetrics> moduleList = executiveMetrics.getModules();
								for (ExecutiveModuleMetrics module : moduleList) {
									if (!modulesName.contains(module.getModuleName())) {
										modules.add(module);
										modulesName.add(module.getModuleName());
									}
								}
							}
						}
					}
				}
			}
			Map<Integer, List<MetricCount>> seriesList = new HashMap<>();
			if (!modules.isEmpty()) {
				for (ExecutiveModuleMetrics executiveModuleMetrics : modules) {
					List<ExecutiveMetricsSeries> executiveMetricsSeriesList = executiveModuleMetrics.getSeries();
					if (!executiveMetricsSeriesList.isEmpty()) {
						for (ExecutiveMetricsSeries executiveMetricsSeries : executiveMetricsSeriesList) {
							int days = executiveMetricsSeries.getDaysAgo();
							Long sev1Count = (long) 0;
							Long sev2Count = (long) 0;
							if (!seriesList.containsKey(days)) {
								List<SeriesCount> countList = executiveMetricsSeries.getCounts();
								List<MetricCount> counts = new ArrayList<>();
								for (SeriesCount count : countList) {
									if ("1".equals(count.getLabel().get(SEVERITY))) {
										sev1Count += count.getCount();
									} else {
										sev2Count += count.getCount();
									}
								}
								Map<String, String> sev1 = new HashMap<>();
								sev1.put(SEVERITY, "1");
								MetricCount seriesCount = new MetricCount();
								seriesCount.setValue(sev1Count);
								seriesCount.setLabel(sev1);
								counts.add(seriesCount);
								seriesCount = new MetricCount();
								Map<String, String> sev2 = new HashMap<>();
								sev2.put(SEVERITY, "2");
								seriesCount.setValue(sev2Count);
								seriesCount.setLabel(sev2);
								counts.add(seriesCount);
								seriesList.put(days, counts);
							} else {
								List<SeriesCount> countList = executiveMetricsSeries.getCounts();
								List<MetricCount> counts = seriesList.get(days);
								for (MetricCount metricCountResponse : counts) {
									if ("1".equals(metricCountResponse.getLabel())) {
										sev1Count += (long) metricCountResponse.getValue();
									}
									if ("2".equals(metricCountResponse.getLabel())) {
										sev2Count += (long) metricCountResponse.getValue();
									}
								}
								for (SeriesCount count : countList) {
									if ("1".equalsIgnoreCase(count.getLabel().get(SEVERITY))) {
										sev1Count += count.getCount();
									} else {
										sev2Count += count.getCount();
									}
								}
								counts = new ArrayList<>();
								Map<String, String> sev1 = new HashMap<>();
								sev1.put(SEVERITY, "1");
								MetricCount seriesCount = new MetricCount();
								seriesCount.setValue(sev1Count);
								seriesCount.setLabel(sev1);
								counts.add(seriesCount);
								seriesCount = new MetricCount();
								Map<String, String> sev2 = new HashMap<>();
								sev2.put(SEVERITY, "2");
								seriesCount.setValue(sev2Count);
								seriesCount.setLabel(sev2);
								counts.add(seriesCount);
								seriesList.replace(days, counts);
							}
						}
					}
				}
				if (!seriesList.isEmpty()) {
					for (Entry<Integer, List<MetricCount>> entry : seriesList.entrySet()) {
						MetricTimeSeriesElement metricTimeSeriesElementResponse = new MetricTimeSeriesElement();
						metricTimeSeriesElementResponse.setDaysAgo(entry.getKey());
						metricTimeSeriesElementResponse.setCounts(entry.getValue());
						metricTimeSeriesElementResponseList.add(metricTimeSeriesElementResponse);
					}
				}
			}
			return metricTimeSeriesElementResponseList;
		}
		return null;
	}

	private String getPortfolioId(String eid) {
		PortfolioResponse portfolioResponse = portfolioResponseRepository.findByEid(eid);
		return portfolioResponse == null ? null : portfolioResponse.getId().toString();
	}

	private MetricSummary processExecutiveSummary(List<String> allAppId) {
		List<ExecutiveModuleMetrics> modules = new ArrayList<>();
		if (allAppId != null && !allAppId.isEmpty()) {

			for (String appId : allAppId) {
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
						PRODINCIDENTS);
				if (executiveComponents != null) {
					List<ExecutiveMetrics> executiveMetricsList = executiveComponents.getMetrics();
					if (executiveMetricsList != null) {
						for (ExecutiveMetrics executiveMetrics : executiveMetricsList) {
							if (PRODINCIDENTS.equals(executiveMetrics.getMetricsName())) {
								List<ExecutiveModuleMetrics> moduleList = executiveMetrics.getModules();
								for (ExecutiveModuleMetrics module : moduleList) {
									module.setTeamId(appId);
									modules.add(module);
								}
							}
						}
					}

				}
			}

			MetricSummary metricSummaryResponse = new MetricSummary();
			metricSummaryResponse.setLastScanned(new Date(System.currentTimeMillis()));
			metricSummaryResponse.setLastUpdated(new Date(System.currentTimeMillis()));
			metricSummaryResponse.setCounts(processMetricSummaryCount(modules));
			metricSummaryResponse.setTrendSlope(getTrendSlopesForModules(modules));
			metricSummaryResponse.setName(PRODINCIDENTS);
			metricSummaryResponse.setDataAvailable(true);
			return metricSummaryResponse;
		}
		return null;
	}

	private Double getTrendSlopesForModules(List<ExecutiveModuleMetrics> modules) {
		Map<Integer, Long> seriesList = new HashMap<>();
		List<String> modulesList = new ArrayList<>();
		if (!modules.isEmpty()) {
			for (ExecutiveModuleMetrics executiveModuleMetrics : modules) {
				if (!modulesList.contains(executiveModuleMetrics.getTeamId())) {
					List<ExecutiveMetricsSeries> executiveMetricsSeriesList = executiveModuleMetrics.getSeries();
					if (!executiveMetricsSeriesList.isEmpty()) {
						for (ExecutiveMetricsSeries executiveMetricsSeries : executiveMetricsSeriesList) {
							int days = executiveMetricsSeries.getDaysAgo();
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

	private List<MetricCount> processMetricSummaryCount(List<ExecutiveModuleMetrics> modules) {
		List<MetricCount> metricCountResponseList = new ArrayList<>();
		MetricCount metricCountResponseSev1 = new MetricCount();
		MetricCount metricCountResponseSev2 = new MetricCount();
		MetricCount metricCountResponseMTBF = new MetricCount();

		Long sevOneCount = (long) 0;
		Long sevTwoCount = (long) 0;
		Long timeToResolveSev1 = (long) 0;
		Long timeToResolveSev2 = (long) 0;
		Long mtbf = (long) 0;
		int count = 0;

		List<String> moduleNames = new ArrayList<>();
		List<String> sev1ModuleNames = new ArrayList<>();
		List<String> sev2ModuleNames = new ArrayList<>();
		List<MTTR> productionOutagesLabel = new ArrayList<>();
		List<MTTR> productionEventsLabel = new ArrayList<>();
		List<OperationalMetrics> operationalMetrics = new ArrayList<>();

		if (!modules.isEmpty()) {
			for (ExecutiveModuleMetrics moduleMetric : modules) {
				String moduleName = moduleMetric.getModuleName();
				String appId = moduleMetric.getTeamId();

				MetricsDetail metricDetailResponse = metricsDetailRepository
						.findByMetricLevelIdAndLevelAndType(appId, MetricLevel.PRODUCT, MetricType.PRODUCTION_INCIDENTS);
				Long mtbfProcessed = processMtbf(metricDetailResponse);

				if (mtbfProcessed < 90)
					count++;

				mtbf += mtbfProcessed;

				if (!moduleNames.contains(moduleName)) {
					List<ExecutiveMetricsSeries> seriesList = moduleMetric.getSeries();
					for (ExecutiveMetricsSeries series : seriesList) {
						List<SeriesCount> countsList = series.getCounts();

						for (SeriesCount counts : countsList) {

							if ("1".equals(counts.getLabel().get(SEVERITY))) {
								sevOneCount += counts.getCount();
								timeToResolveSev1 += Long.valueOf(counts.getLabel().get(TIMETORESOLVE).toString());
							} else {
								sevTwoCount += counts.getCount();
								timeToResolveSev2 += Long.valueOf(counts.getLabel().get(TIMETORESOLVE).toString());
							}
						}
					}
					moduleNames.add(moduleName);
				}
			}

			List<MTTR> productionTotalEventsLabel = productionIncidentsDAO.getMTTRDetails(moduleNames);
			for (MTTR mttr : productionTotalEventsLabel) {
				String appId = mttr.getAppId();
				String severity = mttr.getCrisisLevel();
				if (SEVERITY1.equalsIgnoreCase(severity)) {
					if (!sev1ModuleNames.contains(appId))
						sev1ModuleNames.add(appId);
				} else {
					if (!sev2ModuleNames.contains(appId))
						sev2ModuleNames.add(appId);
				}
			}

			if (!productionTotalEventsLabel.isEmpty()) {
				List<MTTR> productionTotalOutageLabel = productionTotalEventsLabel;
				List<MTTR> productionTotalEventLabel = productionTotalEventsLabel;
				List<MTTR> productionTotalOperationsLabel = productionTotalEventsLabel;
				operationalMetrics = productionIncidentsDAO.getOperationalMetrics(productionTotalOperationsLabel);
				productionOutagesLabel = productionIncidentsDAO.getProcessedDetailsForExec(productionTotalOutageLabel,
						SEVERITY1);
				productionEventsLabel = productionIncidentsDAO.getProcessedDetailsForExec(productionTotalEventLabel,
						SEVERITY2);
			}

			Map<String, String> labelSev1 = new HashMap<>();
			labelSev1.put(SEVERITY, "1");
			labelSev1.put(TIMETORESOLVE, timeToResolveSev1.toString());
			labelSev1.put(IMPACTEDAPPS, Long.toString(sev1ModuleNames.size()));
			metricCountResponseSev1.setLabel(labelSev1);
			metricCountResponseSev1.setValue(sevOneCount);
			metricCountResponseSev1.setLabelMttr(productionOutagesLabel);
			metricCountResponseSev1.setOperationMetrics(operationalMetrics);
			Map<String, String> labelSev2 = new HashMap<>();
			labelSev2.put(SEVERITY, "2");
			labelSev2.put(TIMETORESOLVE, timeToResolveSev2.toString());
			labelSev2.put(IMPACTEDAPPS, Long.toString(sev2ModuleNames.size()));
			metricCountResponseSev2.setLabel(labelSev2);
			metricCountResponseSev2.setValue(sevTwoCount);
			metricCountResponseSev2.setLabelMttr(productionEventsLabel);
			Map<String, String> labelMTBF = new HashMap<>();
			labelMTBF.put("type", "MTBF");
			metricCountResponseMTBF.setLabel(labelMTBF);
			if (count > 0) {
				metricCountResponseMTBF.setValue(mtbf / count);
			} else {
				metricCountResponseMTBF.setValue(90);
			}
			metricCountResponseList.add(metricCountResponseSev1);
			metricCountResponseList.add(metricCountResponseSev2);
			metricCountResponseList.add(metricCountResponseMTBF);
		} else {
			Map<String, String> labelSev1 = new HashMap<>();
			labelSev1.put(SEVERITY, "1");
			labelSev1.put(TIMETORESOLVE, "0");
			labelSev1.put(IMPACTEDAPPS, "0");
			metricCountResponseSev1.setLabel(labelSev1);
			metricCountResponseSev1.setValue(0);
			Map<String, String> labelSev2 = new HashMap<>();
			labelSev2.put(SEVERITY, "2");
			labelSev2.put(TIMETORESOLVE, "0");
			labelSev2.put(IMPACTEDAPPS, "0");
			metricCountResponseSev2.setLabel(labelSev2);
			metricCountResponseSev2.setValue(0);
			Map<String, String> labelMTBF = new HashMap<>();
			labelMTBF.put("type", "MTBF");
			metricCountResponseMTBF.setLabel(labelMTBF);
			metricCountResponseMTBF.setValue(0);
			metricCountResponseList.add(metricCountResponseSev1);
			metricCountResponseList.add(metricCountResponseSev2);
			metricCountResponseList.add(metricCountResponseMTBF);
		}
		return metricCountResponseList;
	}

	private Long processMtbf(MetricsDetail metricDetailResponse) {
		MetricSummary summary = metricDetailResponse.getSummary();
		Long mtbf = (long) 0;
		List<MetricCount> metricCountResponse = summary.getCounts();
		for (MetricCount metricCount : metricCountResponse) {
			if ("MTBF".equals(metricCount.getLabel().get("type"))) {
				mtbf = (long) metricCount.getValue();
			}
		}
		return mtbf;
	}

	/**
	 * function to collect data and store in building_block_components
	 * collections
	 * 
	 * @return check if done
	 */
	public Boolean processComponentDetailsMetrics() {
		LOG.info("Processing Production Incidents Details : building_block_components . . . . ");
		Query query = new Query(new Criteria().where(METRICSNAME).is(PRODINCIDENTS));
		List<String> appIds = mongoTemplate.getCollection(EXECUTIVEMETRICS).distinct(APPID, query.getQueryObject());
		if (!appIds.isEmpty()) {
			for (String appId : appIds) {
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
						PRODINCIDENTS);
				List<BuildingBlocks> response = buildingBlocksRepository
						.findByMetricLevelIdAndMetricLevelAndMetricType(appId, MetricLevel.COMPONENT, MetricType.PRODUCTION_INCIDENTS);
				List<BuildingBlocks> buildingBlockResponse = new ArrayList<>();
				List<ExecutiveModuleMetrics> modules = executiveComponents.getMetrics().get(0).getModules();
				ApplicationDetails appDetails = applicationDetailsRepository.findByAppId(appId);
				if (!modules.isEmpty() && appDetails != null) {
					for (ExecutiveModuleMetrics module : modules) {
						BuildingBlocks summaryResponse = new BuildingBlocks();
						String crisisId = module.getModuleName();
						summaryResponse.setMetricLevelId(appId);
						summaryResponse.setLob(appDetails.getLob());
						summaryResponse.setMetrics(processComponentMetrics(module));
						summaryResponse.setName(crisisId);
						summaryResponse.setCustomField(crisisId);
						summaryResponse.setPoc(appDetails.getPoc());
						summaryResponse.setTotalComponents(1);
						summaryResponse.setTotalExpectedMetrics(1);
						summaryResponse.setUrl(getCrisisLink(crisisId));
						summaryResponse.setMetricType(MetricType.PRODUCTION_INCIDENTS);
						summaryResponse.setMetricLevel(MetricLevel.COMPONENT);
						buildingBlockResponse.add(summaryResponse);
					}
				}
				if (response != null)
					buildingBlocksRepository.delete(response);

				buildingBlocksRepository.save(buildingBlockResponse);
			}
		}
		LOG.info("Completed Production Incidents Details : building_block_components . . . . ");
		return true;
	}

	private String getCrisisLink(String crisisId) {
		if (crisisId != null)
			return metricsSettings.getIncidentLink() + crisisId;
		return null;
	}

	private List<MetricSummary> processComponentMetrics(ExecutiveModuleMetrics module) {
		List<MetricSummary> metricSummaryResponseList = new ArrayList<>();
		List<ExecutiveModuleMetrics> modules = new ArrayList<>();
		MetricSummary metricSummaryResponse = new MetricSummary();
		metricSummaryResponse.setName(PRODINCIDENTS);
		metricSummaryResponse.setTotalComponents(1);
		metricSummaryResponse.setReportingComponents(1);
		metricSummaryResponse.setLastScanned(module.getLastScanned());
		metricSummaryResponse.setLastUpdated(module.getLastUpdated());
		modules.add(module);
		metricSummaryResponse.setCounts(processMetricSummaryCount(modules));
		metricSummaryResponse.setDataAvailable(true);
		metricSummaryResponseList.add(metricSummaryResponse);
		return metricSummaryResponseList;
	}

	/**
	 * SecurityAnalysis removeUnusedProductionIncidentsDetails()
	 * 
	 * @return Boolean
	 **/
	public Boolean removeUnusedProductionIncidentsDetails() {
		LOG.info("Removing Unused Production Incidents Details : executives_metrics . . . . ");
		MongoClient client = null;
		try {
			client = productionIncidentsDAO.getMongoClient();
			List<String> appIdList = vastDetailsDAO.getAllAppIds(client);
			if (appIdList != null && !appIdList.isEmpty()) {
				List<ExecutiveComponents> securityDataList = executiveComponentRepository.getNotUsedAppIdList(appIdList,
						PRODINCIDENTS);
				if (securityDataList != null)
					executiveComponentRepository.delete(securityDataList);
			}
		} catch (Exception e) {
			LOG.info("Error inside removeUnusedProductionIncidentsDetails " + e);
		} finally {
			if (client != null)
				client.close();
		}
		LOG.info("Completed Removing Unused Production Incidents Details : executives_metrics . . . . ");
		return true;
	}

	/**
	 * processDateWiseTrend
	 * 
	 * @return Boolean
	 */
	public Boolean processDateWiseTrend() {
		LOG.info("Processing Velocity Date Wise Trend : executives_metrics . . . . ");
		MongoClient client = null;
		try {
			client = productionIncidentsDAO.getMongoClient();
			List<String> appIds = productionIncidentsDAO.getEntireAppList(client);
			Long timeStamp = metricsSettings.getDateRange();
			for (String appId : appIds)
				processDateWiseTrendSeries(client, appId, timeStamp);
		} catch (Exception e) {
			LOG.info("Error in Quality Analysis Date Wise Trend :: " + e);
		} finally {
			if (client != null)
				client.close();
		}
		LOG.info("Completed Quality Date Wise Trend : executives_metrics . . . . ");
		return true;
	}

	private Boolean processDateWiseTrendSeries(MongoClient client, String appId, Long timeStamp) {
		try {
			Long revisedTimeStamp = timeStamp;
			DateWiseMetricsSeries dateWiseMetricsSeries = dateWiseMetricsSeriesRepository
					.findByAppIdAndMetricsNameOrderByTimeStampDesc(appId, PRODINCIDENTS);
			if (dateWiseMetricsSeries != null && dateWiseMetricsSeries.getTimeStamp() != null)
				revisedTimeStamp = dateWiseMetricsSeries.getTimeStamp();
			fetchAndUpdateForRevisedTime(client, appId, revisedTimeStamp);
		} catch (Exception e) {
			LOG.info("Error in Quality Analysis Date Wise Trend Series :: " + e);
		}
		return true;
	}

	private Boolean fetchAndUpdateForRevisedTime(MongoClient client, String appId, Long timeStamp) {
		try {
			Date revisedDate = new Date(timeStamp);
			SimpleDateFormat formattedJiraDate = new SimpleDateFormat("yyyy-MM-dd");
			String revisedStringDate = formattedJiraDate.format(revisedDate);
			List<MTTR> productionIncidentsData = productionIncidentsDAO.getProductionIncidentsDataByAppIdByRegex(appId,
					client, revisedStringDate);
			if (productionIncidentsData != null) {
				for (MTTR mttr : productionIncidentsData) {
					DateWiseMetricsSeries dateWiseMetricsSeries = dateWiseMetricsSeriesRepository
							.findByAppIdAndModuleNameAndMetricsName(appId, mttr.getCrisisId(), PRODINCIDENTS);
					if (dateWiseMetricsSeries == null)
						dateWiseMetricsSeries = new DateWiseMetricsSeries();
					List<SeriesCount> seriesCountList = new ArrayList<>();
					SeriesCount seriesCount = new SeriesCount();
					Map<String, String> label = new HashMap<>();
					label.put(SEVERITY, String.valueOf(getSev(mttr.getCrisisLevel())));
					label.put(TIMETORESOLVE, String.valueOf(mttr.getItduration()));
					seriesCount.setCount((long) 1);
					seriesCount.setLabel(label);
					seriesCountList.add(seriesCount);
					Date eventDate = formattedJiraDate.parse(mttr.getEventStartDT());
					dateWiseMetricsSeries.setAppId(appId);
					dateWiseMetricsSeries.setMetricsName(PRODINCIDENTS);
					dateWiseMetricsSeries.setModuleName(mttr.getCrisisId());
					dateWiseMetricsSeries.setTimeStamp(eventDate.getTime());
					dateWiseMetricsSeries.setDateValue(formattedJiraDate.format(eventDate));
					dateWiseMetricsSeries.setCounts(seriesCountList);
					dateWiseMetricsSeriesRepository.save(dateWiseMetricsSeries);
				}
			}
		} catch (Exception e) {
			LOG.info("Error in Quality Analysis Date Wise Trend fetch And Update For Revised Time :: " + e);
		}
		return true;
	}
}