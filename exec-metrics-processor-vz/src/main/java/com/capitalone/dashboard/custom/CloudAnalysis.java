package com.capitalone.dashboard.custom;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.dao.CloudDAO;
import com.capitalone.dashboard.exec.model.MetricCount;
import com.capitalone.dashboard.exec.model.MetricLevel;
import com.capitalone.dashboard.exec.model.MetricSummary;
import com.capitalone.dashboard.exec.model.MetricTimeSeriesElement;
import com.capitalone.dashboard.exec.model.MetricType;
import com.capitalone.dashboard.exec.model.vz.ApplicationDetails;
import com.capitalone.dashboard.exec.model.vz.BuildingBlocks;
import com.capitalone.dashboard.exec.model.vz.CloudCost;
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
import com.capitalone.dashboard.exec.repository.vz.ApplicationDetailsRepository;
import com.capitalone.dashboard.exec.repository.vz.BuildingBlocksRepository;
import com.capitalone.dashboard.exec.repository.vz.CloudCostRepository;
import com.capitalone.dashboard.exec.repository.vz.CollectorStatusRepository;
import com.capitalone.dashboard.exec.repository.vz.DateWiseMetricsSeriesRepository;
import com.capitalone.dashboard.exec.repository.vz.ExecutiveComponentRepository;
import com.capitalone.dashboard.exec.repository.vz.ExecutiveSummaryListRepository;
import com.capitalone.dashboard.exec.repository.vz.MetricsDetailRepository;
import com.capitalone.dashboard.exec.repository.vz.PortfolioResponseRepository;
import com.capitalone.dashboard.ops.CloudOps;
import com.capitalone.dashboard.utils.GenericMethods;
import com.mongodb.MongoClient;

/**
 * CloudAnalysis
 * 
 * @param <CloudDAO>
 * @return
 * @author raish4s
 */
@Component
@SuppressWarnings("PMD")
public class CloudAnalysis implements MetricsProcessor {

	private final CloudDAO cloudDAO;
	private final GenericMethods genericMethods;
	private final MongoTemplate mongoTemplate;
	private final ExecutiveComponentRepository executiveComponentRepository;
	private final ExecutiveSummaryListRepository executiveSummaryListRepository;
	private final MetricsDetailRepository metricsDetailRepository;
	private final ApplicationDetailsRepository applicationDetailsRepository;
	private final CollectorStatusRepository collectorStatusRepository;
	private final BuildingBlocksRepository buildingBlocksRepository;
	private final DateWiseMetricsSeriesRepository dateWiseMetricsSeriesRepository;
	private final PortfolioResponseRepository portfolioResponseRepository;
	private final CloudCostRepository cloudCostRepository;
	private final CloudOps cloudOps;
	private static final String CLOUD = "cloud";
	private static final String METRICSNAME = "metrics.metricsName";
	private static final String EXECUTIVECOLLECTION = "executives_metrics";
	private static final String APPID = "appId";
	private static final String TYPE = "type";
	private static final String COST = "cost";
	private static final String ENCRYPTEDEBS = "encryptedEBS";
	private static final String UNENCRYPTEDEBS = "unencryptedEBS";
	private static final String ENCRYPTEDS3 = "encryptedS3";
	private static final String UNENCRYPTEDS3 = "unencryptedS3";
	private static final String MIGRATIONENABLED = "migrationEnabled";
	private static final String COSTOPTIMIZED = "costOptimized";
	private static final String AMI = "Total AMI Count";
	private static final String ELB = "Total ELB Count";
	private static final String RDS = "Total RDS Count";
	private static final String UNUSEDELB = "Total unusedElb";
	private static final String UNUSEDENI = "Total unusedEni";
	private static final String UNUSEDEBS = "Total unusedEbs";
	private static final String PRODENCRYPTEDEBS = "PROD encryptedEBS";
	private static final String PRODUNENCRYPTEDEBS = "PROD unencryptedEBS";
	private static final String PRODENCRYPTEDS3 = "PROD encryptedS3";
	private static final String PRODUNENCRYPTEDS3 = "PROD unencryptedS3";
	private static final String PRODMIGRATIONENABLED = "PROD migrationEnabled";
	private static final String PRODCOSTOPTIMIZED = "PRODcostOptimized";
	private static final String PRODAMI = "PROD AMI Count";
	private static final String PRODELB = "PROD ELB Count";
	private static final String PRODRDS = "PROD RDS Count";
	private static final String PRODUNUSEDELB = "PROD unusedElb";
	private static final String PRODUNUSEDENI = "PROD unusedEni";
	private static final String PRODUNUSEDEBS = "PROD unusedEbs";
	private static final String NONPRODENCRYPTEDEBS = "NONPROD encryptedEBS";
	private static final String NONPRODUNENCRYPTEDEBS = "NONPROD unencryptedEBS";
	private static final String NONPRODENCRYPTEDS3 = "NONPROD encryptedS3";
	private static final String NONPRODUNENCRYPTEDS3 = "NONPROD unencryptedS3";
	private static final String NONPRODMIGRATIONENABLED = "NONPROD migrationEnabled";
	private static final String NONPRODCOSTOPTIMIZED = "NONPROD costOptimized";
	private static final String NONPRODAMI = "NONPROD AMI Count";
	private static final String NONPRODELB = "NONPROD ELB Count";
	private static final String NONPRODRDS = "NONPROD RDS Count";
	private static final String NONPRODUNUSEDELB = "NONPROD unusedElb";
	private static final String NONPRODUNUSEDENI = "NONPROD unusedEni";
	private static final String NONPRODUNUSEDEBS = "NONPROD unusedEbs";
	private static final String STAGINGENCRYPTEDEBS = "STAGING encryptedEBS";
	private static final String STAGINGUNENCRYPTEDEBS = "STAGING unencryptedEBS";
	private static final String STAGINGENCRYPTEDS3 = "STAGING encryptedS3";
	private static final String STAGINGUNENCRYPTEDS3 = "STAGING unencryptedS3";
	private static final String STAGINGMIGRATIONENABLED = "STAGING migrationEnabled";
	private static final String STAGINGCOSTOPTIMIZED = "STAGING costOptimized";
	private static final String STAGINGAMI = "STAGING AMI Count";
	private static final String STAGINGELB = "STAGING ELB Count";
	private static final String STAGINGRDS = "STAGING RDS Count";
	private static final String STAGINGUNUSEDELB = "STAGING unusedElb";
	private static final String STAGINGUNUSEDENI = "STAGING unusedEni";
	private static final String STAGINGUNUSEDEBS = "STAGING unusedEbs";
	private static final String DATEFORMAT = "dd-MM-yyyy";
	private static final Logger LOG = LoggerFactory.getLogger(CloudAnalysis.class);

	/**
	 * @param cloudDAO
	 * @param genericMethods
	 * @param executiveComponentRepository
	 * @param mongoTemplate
	 * @param executiveSummaryListRepository
	 * @param metricsDetailRepository
	 * @param applicationDetailsRepository
	 * @param buildingBlocksRepository
	 * @param collectorStatusRepository
	 * @param dateWiseMetricsSeriesRepository
	 * @param cloudOps
	 * @param portfolioResponseRepository
	 * @param cloudCostRepository
	 */
	@Autowired
	public CloudAnalysis(CloudDAO cloudDAO, GenericMethods genericMethods,
			ExecutiveComponentRepository executiveComponentRepository, MongoTemplate mongoTemplate,
			ExecutiveSummaryListRepository executiveSummaryListRepository,
			MetricsDetailRepository metricsDetailRepository, ApplicationDetailsRepository applicationDetailsRepository,
			BuildingBlocksRepository buildingBlocksRepository, CollectorStatusRepository collectorStatusRepository,
			DateWiseMetricsSeriesRepository dateWiseMetricsSeriesRepository,
			CloudOps cloudOps, PortfolioResponseRepository portfolioResponseRepository,
			CloudCostRepository cloudCostRepository) {
		this.cloudDAO = cloudDAO;
		this.genericMethods = genericMethods;
		this.executiveComponentRepository = executiveComponentRepository;
		this.executiveSummaryListRepository = executiveSummaryListRepository;
		this.metricsDetailRepository = metricsDetailRepository;
		this.applicationDetailsRepository = applicationDetailsRepository;
		this.buildingBlocksRepository = buildingBlocksRepository;
		this.collectorStatusRepository = collectorStatusRepository;
		this.dateWiseMetricsSeriesRepository = dateWiseMetricsSeriesRepository;
		this.mongoTemplate = mongoTemplate;
		this.cloudOps = cloudOps;
		this.portfolioResponseRepository = portfolioResponseRepository;
		this.cloudCostRepository = cloudCostRepository;
	}

	/**
	 * @return
	 */
	public Boolean processDateWiseTrend() {
		LOG.info("Processing Cloud Date Wise Trend : date_wise_metrics . . . . ");
		MongoClient client = null;
		try {
			client = cloudDAO.getMongoClient();
			List<String> appIds = cloudDAO.getEntireAppList(client);
			Long timeStamp = getLast90daystd();
			for (String appId : appIds) {
				processDateWiseTrendSeries(client, appId, timeStamp);
			}
		} catch (Exception e) {
			LOG.error("Error in Cloud Analysis Date Wise Trend :: " + e);
		} finally {
			if (client != null)
				client.close();
		}
		LOG.info("Completed Cloud Date Wise Trend : date_wise_metrics . . . . ");
		return true;
	}
	
	/**
	 * 
	 * @return
	 */
	@SuppressWarnings({ "deprecation" })
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
	 * @param timeStamp
	 * @return
	 */
	

	private Boolean processDateWiseTrendSeries(MongoClient client, String appId, Long timeStamp) {
		Long revisedTimeStamp = timeStamp;
		String moduleName = appId + "CloudModule";
		DateWiseMetricsSeries dateWiseMetricsSeries = dateWiseMetricsSeriesRepository
				.findByAppIdAndModuleNameAndMetricsNameOrderByTimeStampDesc(appId, moduleName, CLOUD);
		if (dateWiseMetricsSeries != null && dateWiseMetricsSeries.getTimeStamp() != null)
			revisedTimeStamp = dateWiseMetricsSeries.getTimeStamp();
		fetchAndUpdateForRevisedTime(client, appId, moduleName, revisedTimeStamp);
		return true;
	}
	
	/**
	 * 
	 * @param client
	 * @param appId
	 * @param projectName
	 * @param revisedTimeStamp
	 * @return
	 */

	private Boolean fetchAndUpdateForRevisedTime(MongoClient client, String appId, String projectName,
			Long revisedTimeStamp) {
		try {
			Long presentTimeStamp = genericMethods.getLastMonthTimestamp();
			Date revisedDate = new Date(revisedTimeStamp);
			SimpleDateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);
			String revisedStringDate = dateFormat.format(revisedDate);
			Date date = dateFormat.parse(revisedStringDate);
			long newRevisedTimeStamp = date.getTime();
			while (newRevisedTimeStamp < presentTimeStamp) {
				String dateValue = dateFormat.format(revisedDate);
				DateWiseMetricsSeries dateWiseMetricsSeries = dateWiseMetricsSeriesRepository
						.findByAppIdAndModuleNameAndMetricsNameAndDateValue(appId, projectName, CLOUD,
								dateValue);
				if (dateWiseMetricsSeries == null)
					dateWiseMetricsSeries = new DateWiseMetricsSeries();
				List<SeriesCount> seriesCountList = cloudOps.processSeriesCount(appId, client, dateValue);
				dateWiseMetricsSeries.setAppId(appId);
				dateWiseMetricsSeries.setMetricsName(CLOUD);
				dateWiseMetricsSeries.setModuleName(projectName);
				dateWiseMetricsSeries.setTimeStamp(newRevisedTimeStamp);
				dateWiseMetricsSeries.setDateValue(dateValue);
				dateWiseMetricsSeries.setCounts(seriesCountList);
				dateWiseMetricsSeriesRepository.save(dateWiseMetricsSeries);
				newRevisedTimeStamp += 86400000;
				revisedDate = new Date(newRevisedTimeStamp);
			}
		} catch (Exception e) {
			LOG.error("Error in Cloud Analysis Date Wise Trend fetch And Update For Revised Time :: " + e);
		}
		return true;
	}

	/**
	 * @return
	 */
	public Boolean processExecutiveMetricsDetails() {
		LOG.info("Processing Executive Cloud Details : executives_metrics . . . . ");
		MongoClient client = null;
		try {
			client = cloudDAO.getMongoClient();
			List<String> appIds = cloudDAO.getEntireAppList(client);
			if (!appIds.isEmpty()) {
				for (String appId : appIds) {
					processExecutiveComponent(appId);
				}
			}
			LOG.info("Completed Executive Cloud Details : executives_metrics . . . . ");
		} catch (Exception e) {
			LOG.error("Error inside Cloud Analysis file - processCloudMetricsDetails() : " + e);
		} finally {
			if (client != null)
				client.close();
		}
		return true;
	}
	
	/**
	 * 
	 * @param appId
	 * @return
	 */

	private Boolean processExecutiveComponent(String appId) {
		ApplicationDetails appDetails = applicationDetailsRepository.findByAppId(appId);
		ExecutiveComponents executiveComponent = executiveComponentRepository.findByAppIdAndMetric(appId, CLOUD);
		if (executiveComponent == null)
			executiveComponent = new ExecutiveComponents();
		executiveComponent.setAppId(appId);
		if (appDetails != null) {
			executiveComponent.setAppName(appDetails.getAppName());
			executiveComponent.setTeamBoardLink(appDetails.getTeamBoardLink());
		}
		executiveComponent.setMetrics(processAppMetrics(appId));
		executiveComponentRepository.save(executiveComponent);
		LOG.info("Data Collected from OneHygieia for " + appId);
		return true;
	}
	
	/**
	 * 
	 * @param appId
	 * @return
	 */

	private List<ExecutiveMetrics> processAppMetrics(String appId) {
		List<ExecutiveMetrics> executiveMetricsList = new ArrayList<>();
		List<ExecutiveModuleMetrics> executiveModuleMetricsList = processCloudModuleMetrics(appId);
		ExecutiveMetrics executiveMetrics = new ExecutiveMetrics();
		executiveMetrics.setLastScanned(new Date(System.currentTimeMillis()));
		executiveMetrics.setLastUpdated(new Date(System.currentTimeMillis()));
		executiveMetrics.setModules(executiveModuleMetricsList);
		executiveMetrics.setMetricsName(CLOUD);
		executiveMetrics.setTrendSlope(genericMethods.getTrendSlopesForModules(executiveModuleMetricsList));
		executiveMetricsList.add(executiveMetrics);
		return executiveMetricsList;
	}
	
	/**
	 * 
	 * @param appId
	 * @return
	 */

	private List<ExecutiveModuleMetrics> processCloudModuleMetrics(String appId) {
		List<ExecutiveModuleMetrics> executiveModuleMetricsList = new ArrayList<>();
		Date lastUpdated = null;
		CollectorStatus collectorStatus = collectorStatusRepository.findByType(CollectorType.CloudCustodian);
		if (collectorStatus != null)
			lastUpdated = collectorStatus.getLastUpdated();
		ExecutiveModuleMetrics executiveModuleMetrics = new ExecutiveModuleMetrics();
		String moduleName = appId + "CloudModule";
		List<ExecutiveMetricsSeries> metricSeries = getSeriesCountList(appId, moduleName);
		executiveModuleMetrics.setModuleName(moduleName);
		executiveModuleMetrics.setSeries(metricSeries);
		executiveModuleMetrics.setLastScanned(lastUpdated);
		executiveModuleMetrics.setLastUpdated(new Date(System.currentTimeMillis()));
		executiveModuleMetrics.setTrendSlope(genericMethods.getTrendSlope(metricSeries));
		executiveModuleMetricsList.add(executiveModuleMetrics);
		return executiveModuleMetricsList;
	}
	
	/**
	 * 
	 * @param appId
	 * @param moduleName
	 * @return
	 */

	@SuppressWarnings("unchecked")
	private List<ExecutiveMetricsSeries> getSeriesCountList(String appId, String moduleName) {
		List<ExecutiveMetricsSeries> executiveMetricsSeriesList = new ArrayList<>();
		try {

			Calendar cal = Calendar.getInstance();
			String lastmonth = "30-0" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.YEAR);
			if (cal.get(Calendar.MONTH) == 0)
				lastmonth = "30-12-" + (cal.get(Calendar.YEAR) - 1);
			if (cal.get(Calendar.MONTH) > 9)
				lastmonth = "30-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.YEAR);
			String yesterday = genericMethods.getYesterdayDateString();
			if (cloudCostRepository.findByAppId(appId).isEmpty()) {
				DateWiseMetricsSeries datewise = dateWiseMetricsSeriesRepository.getMetricsByDateValue(appId,
						moduleName, CLOUD, yesterday);
				ExecutiveMetricsSeries executiveMetricsSeries = new ExecutiveMetricsSeries();
				executiveMetricsSeries.setDaysAgo(genericMethods.getLastMonth());
				executiveMetricsSeries.setCounts(datewise.getCounts());
				executiveMetricsSeries.setTimeStamp(new SimpleDateFormat(DATEFORMAT).parse(lastmonth).getTime());
				executiveMetricsSeriesList.add(executiveMetricsSeries);
			} else {
				List<String> timeRanges = mongoTemplate.getCollection("cloudCost").distinct("time");
				for (String time : timeRanges) {
					ExecutiveMetricsSeries executiveMetricsSeries = new ExecutiveMetricsSeries();
					if (time != null && (genericMethods.getDaysAgoValue(time) > 0
							&& (genericMethods.getDaysAgoValue(time) < 90))) {
						CloudCost cost = cloudCostRepository.findByAppIdAndTime(appId, time);
						if (cost != null) {
							List<SeriesCount> counts = new ArrayList<>();
							SeriesCount count = new SeriesCount();
							Map<String, String> monthcost = new HashMap<>();
							monthcost.put(TYPE, COST);
							count.setLabel(monthcost);
							if (cost.getCost() != null)
								count.setCount(cost.getCost().longValue());
							else
								count.setCount((long) 0);
							counts.add(count);
							if (cost.getTime() != null && cost.getTime().equalsIgnoreCase(lastmonth)) {
								executiveMetricsSeries.setDaysAgo(genericMethods.getLastMonth());
								executiveMetricsSeries.setCounts(dateWiseMetricsSeriesRepository
										.getMetricsByDateValue(appId, moduleName, CLOUD, yesterday).getCounts());
							} else {
								executiveMetricsSeries.setDaysAgo(genericMethods.getDaysAgoValue(cost.getTime()));
								executiveMetricsSeries.setCounts(counts);
							}
							executiveMetricsSeries
									.setTimeStamp(new SimpleDateFormat(DATEFORMAT).parse(cost.getTime()).getTime());
							executiveMetricsSeriesList.add(executiveMetricsSeries);
						}
					}
				}
			}
		} catch (Exception e) {
			LOG.error("ERROR in CloudAnalysis.getSeriesCountList(): " + e);
		}
		return executiveMetricsSeriesList;
	}

	/**
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
	public Boolean processMetricsDetailResponse() {
		LOG.info("Processing Application Cloud Details : app_metrics_details . . . . ");
		try {
			Query query = new Query(new Criteria().where(METRICSNAME).is(CLOUD));
			List<String> appIds = mongoTemplate.getCollection(EXECUTIVECOLLECTION).distinct(APPID,
					query.getQueryObject());
			if (!appIds.isEmpty()) {
				for (String appId : appIds) {
					ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
							CLOUD);
					MetricsDetail metricDetailResponseProcessed = processMetricDetailResponse(executiveComponents);
					MetricsDetail metricDetailResponseStored = metricsDetailRepository
							.findByMetricLevelIdAndLevelAndType(appId, MetricLevel.PRODUCT, MetricType.CLOUD);
					if (metricDetailResponseStored != null)
						metricsDetailRepository.delete(metricDetailResponseStored);
					metricsDetailRepository.save(metricDetailResponseProcessed);
				}
			}
		} catch (Exception e) {
			LOG.info("Error in Application Cloud Details : app_metrics_details . . . . " + e);
		}
		LOG.info("Completed Application Cloud Details : app_metrics_details . . . . ");
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
		metricDetailResponse.setType(MetricType.CLOUD);
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
		List<MetricTimeSeriesElement> metricTimeSeriesElementResponseList = new ArrayList<>();
		List<ExecutiveModuleMetrics> modules = executiveComponents.getMetrics().get(0).getModules();
		List<String> teamIds = new ArrayList<>();
		if (!modules.isEmpty()) {
			for (ExecutiveModuleMetrics executiveModuleMetrics : modules) {
				if (!teamIds.contains(executiveModuleMetrics.getModuleName())) {
					teamIds.add(executiveModuleMetrics.getModuleName());
					List<ExecutiveMetricsSeries> executiveMetricsSeriesList = executiveModuleMetrics.getSeries();
					if (!executiveMetricsSeriesList.isEmpty()) {
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
					}
				}
			}
		}
		return metricTimeSeriesElementResponseList;
	}

	/**
	 * 
	 * @param executiveMetricsSeriesList
	 * @return
	 */
	
	private List<MetricCount> processMetricSummaryCounts(List<ExecutiveMetricsSeries> executiveMetricsSeriesList) {
		List<MetricCount> metricCountResponseList = null;
		for (ExecutiveMetricsSeries executiveMetricsSeries : executiveMetricsSeriesList) {
			List<SeriesCount> seriesCountList = executiveMetricsSeries.getCounts();
			String yesterday = genericMethods.getYesterdayDateString();
			if (executiveMetricsSeries.getDaysAgo() == genericMethods.getLastMonth()
					|| executiveMetricsSeries.getDaysAgo() == genericMethods.getDaysAgoValue(yesterday)) {
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
	 * 
	 * @param executiveComponents
	 * @return
	 */

	private MetricSummary processMetricsSummary(ExecutiveComponents executiveComponents) {
		MetricSummary metricSummaryResponse = new MetricSummary();
		Boolean dataAvailabilityStatus;
		metricSummaryResponse.setLastScanned(executiveComponents.getMetrics().get(0).getLastScanned());
		metricSummaryResponse.setLastUpdated(executiveComponents.getMetrics().get(0).getLastUpdated());
		if (executiveComponents.getMetrics() != null) {
			List<ExecutiveMetrics> metrics = executiveComponents.getMetrics();
			for (ExecutiveMetrics metric : metrics) {
				if (metric.getMetricsName().equalsIgnoreCase(CLOUD)) {
					metricSummaryResponse.setCounts(processMetricSummaryCounts(
							executiveComponents.getMetrics().get(0).getModules().get(0).getSeries()));
					metricSummaryResponse.setReportingComponents(1);
					metricSummaryResponse.setTotalComponents(1);
					metricSummaryResponse.setTrendSlope(executiveComponents.getMetrics().get(0).getTrendSlope());
					metricSummaryResponse
							.setAppCriticality(genericMethods.processAppCriticality(executiveComponents.getAppId()));
					dataAvailabilityStatus = genericMethods.checkForDataAvailability(executiveComponents.getAppId());
					metricSummaryResponse.setDataAvailable(dataAvailabilityStatus);
					if (!dataAvailabilityStatus)
						metricSummaryResponse.setConfMessage(genericMethods
								.checkForDataAvailabilityStatus(executiveComponents.getMetrics().get(0).getModules()));
				}
			}
		}
		metricSummaryResponse.setName(CLOUD);
		return metricSummaryResponse;
	}

	/**
	 * @return Boolean
	 */
	
	@SuppressWarnings({ "static-access", "unchecked" })
	
	public Boolean processBuildingBlockMetrics() {
		LOG.info("PRocessing CLOUD Details : building_block_metrics . . . . ");
		try {
			Query query = new Query(new Criteria().where(METRICSNAME).is(CLOUD));
			List<String> appIds = mongoTemplate.getCollection(EXECUTIVECOLLECTION).distinct(APPID,
					query.getQueryObject());
			List<BuildingBlocks> buildingBlockMetricSummaryResponseList = new ArrayList<>();
			if (!appIds.isEmpty()) {
				for (String appId : appIds) {
					BuildingBlocks buildingBlockMetricSummaryResponse = buildingBlocksRepository
							.findByMetricLevelIdAndMetricLevel(appId, MetricLevel.PRODUCT);
					if (buildingBlockMetricSummaryResponse == null)
						buildingBlockMetricSummaryResponse = new BuildingBlocks();
					ApplicationDetails appDetails = applicationDetailsRepository.findByAppId(appId);
					MetricsDetail metricDetailResponse = metricsDetailRepository
							.findByMetricLevelIdAndLevelAndType(appId, MetricLevel.PRODUCT, MetricType.CLOUD);
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
								if (!metricSummaryResponse.getName().equalsIgnoreCase(CLOUD))
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
						buildingBlocksRepository.save(buildingBlockMetricSummaryResponse);
					}
				}
				if (!buildingBlockMetricSummaryResponseList.isEmpty())
					buildingBlocksRepository.save(buildingBlockMetricSummaryResponseList);
			}
			LOG.info("Completed CLOUD Details : building_block_metrics . . . . ");
		} catch (Exception e) {
			LOG.info("processBuildingBlockMetrics CLOUD Analysis Info :: " + e);
		}
		return true;
	}
	
	/**
	 * 
	 * @param configuredAppId
	 * @return
	 */

	private List<MetricTimeSeriesElement> processExecutiveTimeSeries(List<String> configuredAppId) {
		List<ExecutiveModuleMetrics> modules = new ArrayList<>();
		for (String appId : configuredAppId) {
			ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId, CLOUD);
			if (executiveComponents != null && !executiveComponents.getMetrics().isEmpty()) {
				for (ExecutiveModuleMetrics executiveModuleMetrics : executiveComponents.getMetrics().get(0)
						.getModules()) {
					modules.add(executiveModuleMetrics);
				}
			}
		}
		return cloudOps.processExecutiveMetricsSeries(modules);
	}

	/**
	 * 
	 * @param modules
	 * @return
	 */
	
	private List<MetricCount> processMetricsSummaryCounts(List<ExecutiveModuleMetrics> modules) {
		List<MetricCount> metricCountResponseList = new ArrayList<>();
		if (!modules.isEmpty()) {
			Map<String, String> cost = new HashMap<>();
			Map<String, String> encryptedEBS = new HashMap<>();
			Map<String, String> unencryptedEBS = new HashMap<>();
			Map<String, String> encryptedS3 = new HashMap<>();
			Map<String, String> unencryptedS3 = new HashMap<>();
			Map<String, String> migrationEnabled = new HashMap<>();
			Map<String, String> costOptimized = new HashMap<>();
			Map<String, String> ami = new HashMap<>();
			Map<String, String> elb = new HashMap<>();
			Map<String, String> rds = new HashMap<>();
			Map<String, String> unusedElb = new HashMap<>();
			Map<String, String> unusedEni = new HashMap<>();
			Map<String, String> unusedEbs = new HashMap<>();// prod
			Map<String, String> prodencryptedEBS = new HashMap<>();
			Map<String, String> produnencryptedEBS = new HashMap<>();
			Map<String, String> prodencryptedS3 = new HashMap<>();
			Map<String, String> produnencryptedS3 = new HashMap<>();
			Map<String, String> prodmigrationEnabled = new HashMap<>();
			Map<String, String> prodcostOptimized = new HashMap<>();
			Map<String, String> prodami = new HashMap<>();
			Map<String, String> prodelb = new HashMap<>();
			Map<String, String> prodrds = new HashMap<>();
			Map<String, String> produnusedElb = new HashMap<>();
			Map<String, String> produnusedEni = new HashMap<>();
			Map<String, String> produnusedEbs = new HashMap<>();// nonprod
			Map<String, String> nonprodencryptedEBS = new HashMap<>();
			Map<String, String> nonprodunencryptedEBS = new HashMap<>();
			Map<String, String> nonprodencryptedS3 = new HashMap<>();
			Map<String, String> nonprodunencryptedS3 = new HashMap<>();
			Map<String, String> nonprodmigrationEnabled = new HashMap<>();
			Map<String, String> nonprodcostOptimized = new HashMap<>();
			Map<String, String> nonprodami = new HashMap<>();
			Map<String, String> nonprodelb = new HashMap<>();
			Map<String, String> nonprodrds = new HashMap<>();
			Map<String, String> nonprodunusedElb = new HashMap<>();
			Map<String, String> nonprodunusedEni = new HashMap<>();
			Map<String, String> nonprodunusedEbs = new HashMap<>();// staging
			Map<String, String> stagingencryptedEBS = new HashMap<>();
			Map<String, String> stagingunencryptedEBS = new HashMap<>();
			Map<String, String> stagingencryptedS3 = new HashMap<>();
			Map<String, String> stagingunencryptedS3 = new HashMap<>();
			Map<String, String> stagingmigrationEnabled = new HashMap<>();
			Map<String, String> stagingcostOptimized = new HashMap<>();
			Map<String, String> stagingami = new HashMap<>();
			Map<String, String> stagingelb = new HashMap<>();
			Map<String, String> stagingrds = new HashMap<>();
			Map<String, String> stagingunusedElb = new HashMap<>();
			Map<String, String> stagingunusedEni = new HashMap<>();
			Map<String, String> stagingunusedEbs = new HashMap<>();
			cost.put(TYPE, COST);
			encryptedEBS.put(TYPE, ENCRYPTEDEBS);
			unencryptedEBS.put(TYPE, UNENCRYPTEDEBS);
			encryptedS3.put(TYPE, ENCRYPTEDS3);
			unencryptedS3.put(TYPE, UNENCRYPTEDS3);
			migrationEnabled.put(TYPE, MIGRATIONENABLED);
			costOptimized.put(TYPE, COSTOPTIMIZED);
			ami.put(TYPE, AMI);
			elb.put(TYPE, ELB);
			rds.put(TYPE, RDS);
			unusedElb.put(TYPE, UNUSEDELB);
			unusedEni.put(TYPE, UNUSEDENI);
			unusedEbs.put(TYPE, UNUSEDEBS);
			prodencryptedEBS.put(TYPE, PRODENCRYPTEDEBS);
			produnencryptedEBS.put(TYPE, PRODUNENCRYPTEDEBS);
			prodencryptedS3.put(TYPE, PRODENCRYPTEDS3);
			produnencryptedS3.put(TYPE, PRODUNENCRYPTEDS3);
			prodmigrationEnabled.put(TYPE, PRODMIGRATIONENABLED);
			prodcostOptimized.put(TYPE, PRODCOSTOPTIMIZED);
			prodami.put(TYPE, PRODAMI);
			prodelb.put(TYPE, PRODELB);
			prodrds.put(TYPE, PRODRDS);
			produnusedElb.put(TYPE, PRODUNUSEDELB);
			produnusedEni.put(TYPE, PRODUNUSEDENI);
			produnusedEbs.put(TYPE, PRODUNUSEDEBS);
			nonprodencryptedEBS.put(TYPE, NONPRODENCRYPTEDEBS);
			nonprodunencryptedEBS.put(TYPE, NONPRODUNENCRYPTEDEBS);
			nonprodencryptedS3.put(TYPE, NONPRODENCRYPTEDS3);
			nonprodunencryptedS3.put(TYPE, NONPRODUNENCRYPTEDS3);
			nonprodmigrationEnabled.put(TYPE, NONPRODMIGRATIONENABLED);
			nonprodcostOptimized.put(TYPE, NONPRODCOSTOPTIMIZED);
			nonprodami.put(TYPE, NONPRODAMI);
			nonprodelb.put(TYPE, NONPRODELB);
			nonprodrds.put(TYPE, NONPRODRDS);
			nonprodunusedElb.put(TYPE, NONPRODUNUSEDELB);
			nonprodunusedEni.put(TYPE, NONPRODUNUSEDENI);
			nonprodunusedEbs.put(TYPE, NONPRODUNUSEDEBS); // staging
			stagingencryptedEBS.put(TYPE, STAGINGENCRYPTEDEBS);
			stagingunencryptedEBS.put(TYPE, STAGINGUNENCRYPTEDEBS);
			stagingencryptedS3.put(TYPE, STAGINGENCRYPTEDS3);
			stagingunencryptedS3.put(TYPE, STAGINGUNENCRYPTEDS3);
			stagingmigrationEnabled.put(TYPE, STAGINGMIGRATIONENABLED);
			stagingcostOptimized.put(TYPE, STAGINGCOSTOPTIMIZED);
			stagingami.put(TYPE, STAGINGAMI);
			stagingelb.put(TYPE, STAGINGELB);
			stagingrds.put(TYPE, STAGINGRDS);
			stagingunusedElb.put(TYPE, STAGINGUNUSEDELB);
			stagingunusedEni.put(TYPE, STAGINGUNUSEDENI);
			stagingunusedEbs.put(TYPE, STAGINGUNUSEDEBS);
			Map<String, Long> seriesCounts = genericMethods.processExecSeriesCount(modules);
			if (!seriesCounts.isEmpty()) {
				MetricCount metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(cost);
				metricCountResponse.setValue(seriesCounts.get("costCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(encryptedEBS);
				metricCountResponse.setValue(seriesCounts.get("encryptedEBSCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(unencryptedEBS);
				metricCountResponse.setValue(seriesCounts.get("unencryptedEBSCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(encryptedS3);
				metricCountResponse.setValue(seriesCounts.get("encryptedS3Count"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(unencryptedS3);
				metricCountResponse.setValue(seriesCounts.get("unencryptedS3Count"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(migrationEnabled);
				metricCountResponse.setValue(seriesCounts.get("migrationEnabledCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(costOptimized);
				metricCountResponse.setValue(seriesCounts.get("costOptimizedCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(ami);
				metricCountResponse.setValue(seriesCounts.get("amiCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(elb);
				metricCountResponse.setValue(seriesCounts.get("elbCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(rds);
				metricCountResponse.setValue(seriesCounts.get("rdsCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(unusedElb);
				metricCountResponse.setValue(seriesCounts.get("unusedElbCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(unusedEni);
				metricCountResponse.setValue(seriesCounts.get("unusedEniCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(unusedEbs);
				metricCountResponse.setValue(seriesCounts.get("unusedEbsCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(prodencryptedEBS);
				metricCountResponse.setValue(seriesCounts.get("prodEncryptedEBSCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(produnencryptedEBS);
				metricCountResponse.setValue(seriesCounts.get("prodUnencryptedEBSCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(prodencryptedS3);
				metricCountResponse.setValue(seriesCounts.get("prodEncryptedS3Count"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(produnencryptedS3);
				metricCountResponse.setValue(seriesCounts.get("prodUnencryptedS3Count"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(prodmigrationEnabled);
				metricCountResponse.setValue(seriesCounts.get("prodMigrationEnabledCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(prodcostOptimized);
				metricCountResponse.setValue(seriesCounts.get("prodCostOptimizedCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(prodami);
				metricCountResponse.setValue(seriesCounts.get("prodAmiCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(prodelb);
				metricCountResponse.setValue(seriesCounts.get("prodElbCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(prodrds);
				metricCountResponse.setValue(seriesCounts.get("prodRdsCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(produnusedElb);
				metricCountResponse.setValue(seriesCounts.get("prodUnusedElbCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(produnusedEni);
				metricCountResponse.setValue(seriesCounts.get("prodUnusedEniCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(produnusedEbs);
				metricCountResponse.setValue(seriesCounts.get("prodUnusedEbsCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(nonprodencryptedEBS);
				metricCountResponse.setValue(seriesCounts.get("nonprodEncryptedEBSCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(nonprodunencryptedEBS);
				metricCountResponse.setValue(seriesCounts.get("nonprodUnencryptedEBSCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(nonprodencryptedS3);
				metricCountResponse.setValue(seriesCounts.get("nonprodEncryptedS3Count"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(nonprodunencryptedS3);
				metricCountResponse.setValue(seriesCounts.get("nonprodUnencryptedS3Count"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(nonprodmigrationEnabled);
				metricCountResponse.setValue(seriesCounts.get("nonprodMigrationEnabledCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(nonprodcostOptimized);
				metricCountResponse.setValue(seriesCounts.get("nonprodCostOptimizedCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(nonprodami);
				metricCountResponse.setValue(seriesCounts.get("nonprodAmiCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(nonprodelb);
				metricCountResponse.setValue(seriesCounts.get("nonprodElbCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(nonprodrds);
				metricCountResponse.setValue(seriesCounts.get("nonprodRdsCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(nonprodunusedElb);
				metricCountResponse.setValue(seriesCounts.get("nonprodUnusedElbCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(nonprodunusedEni);
				metricCountResponse.setValue(seriesCounts.get("nonprodUnusedEniCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(nonprodunusedEbs);
				metricCountResponse.setValue(seriesCounts.get("nonprodUnusedEbsCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(stagingencryptedEBS);
				metricCountResponse.setValue(seriesCounts.get("stagingEncryptedEBSCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(stagingunencryptedEBS);
				metricCountResponse.setValue(seriesCounts.get("stagingUnencryptedEBSCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(stagingencryptedS3);
				metricCountResponse.setValue(seriesCounts.get("stagingEncryptedS3Count"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(stagingunencryptedS3);
				metricCountResponse.setValue(seriesCounts.get("stagingUnencryptedS3Count"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(stagingmigrationEnabled);
				metricCountResponse.setValue(seriesCounts.get("stagingMigrationEnabledCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(stagingcostOptimized);
				metricCountResponse.setValue(seriesCounts.get("stagingCostOptimizedCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(stagingami);
				metricCountResponse.setValue(seriesCounts.get("stagingAmiCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(stagingelb);
				metricCountResponse.setValue(seriesCounts.get("stagingElbCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(stagingrds);
				metricCountResponse.setValue(seriesCounts.get("stagingRdsCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(stagingunusedElb);
				metricCountResponse.setValue(seriesCounts.get("stagingUnusedElbCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(stagingunusedEni);
				metricCountResponse.setValue(seriesCounts.get("stagingUnusedEniCount"));
				metricCountResponseList.add(metricCountResponse);
				metricCountResponse = new MetricCount();
				metricCountResponse.setLabel(stagingunusedEbs);
				metricCountResponse.setValue(seriesCounts.get("stagingUnusedEbsCount"));
				metricCountResponseList.add(metricCountResponse);
			}
		}
		return metricCountResponseList;
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
						CLOUD);
				if (executiveComponents != null) {
					List<ExecutiveMetrics> executiveMetricsList = executiveComponents.getMetrics();
					if (executiveMetricsList != null) {
						for (ExecutiveMetrics metric : executiveMetricsList) {
							if (metric.getMetricsName().equalsIgnoreCase(CLOUD)) {
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
			if (collectorStatusRepository.findByType(CollectorType.CloudCustodian) != null)
				metricSummaryResponse.setLastScanned(
						collectorStatusRepository.findByType(CollectorType.CloudCustodian).getLastUpdated());
			metricSummaryResponse.setLastUpdated(new Date(System.currentTimeMillis()));
			metricSummaryResponse.setCounts(processMetricsSummaryCounts(modules));
			metricSummaryResponse.setTrendSlope(genericMethods.getTrendSlopesForModules(modules));
			metricSummaryResponse.setName(CLOUD);
			metricSummaryResponse.setDataAvailable(genericMethods.checkForDataAvailabilityForExec(configuredAppId));
			return metricSummaryResponse;
		}
		return null;
	}

	/**
	 * @return
	 */
	
	public Boolean processExecutiveDetailsMetrics() {
		LOG.info("Processing CLOUD Details : portfolio_metrics_details . . . . ");
		List<ExecutiveSummaryList> executiveSummaryLists = (List<ExecutiveSummaryList>) executiveSummaryListRepository
				.findAll();
		if (!executiveSummaryLists.isEmpty()) {
			for (ExecutiveSummaryList executiveSummaryList : executiveSummaryLists) {
				try {
					String eid = executiveSummaryList.getEid();
					MetricsDetail metricPortfolioDetailResponse = metricsDetailRepository
							.findByMetricLevelIdAndLevelAndType(eid, MetricLevel.PORTFOLIO, MetricType.CLOUD);
					if (metricPortfolioDetailResponse == null)
						metricPortfolioDetailResponse = new MetricsDetail();
					metricPortfolioDetailResponse.setMetricLevelId(eid);
					metricPortfolioDetailResponse.setType(MetricType.CLOUD);
					metricPortfolioDetailResponse.setLevel(MetricLevel.PORTFOLIO);
					metricPortfolioDetailResponse.setCustomField(getPortfolioId(eid));
					metricPortfolioDetailResponse.setSummary(processExecutiveSummary(executiveSummaryList.getAppId()));
					metricPortfolioDetailResponse
							.setTimeSeries(processExecutiveTimeSeries(executiveSummaryList.getAppId()));
					if (metricPortfolioDetailResponse.getSummary() != null) {
						metricPortfolioDetailResponse.getSummary()
								.setTotalComponents(executiveSummaryList.getTotalApps());
						metricPortfolioDetailResponse.getSummary().setReportingComponents(
								genericMethods.getReportingCount(executiveSummaryList.getAppId()));
					}
					metricsDetailRepository.save(metricPortfolioDetailResponse);
				} catch (Exception e) {
					LOG.error("ERROR CloudAnalysis.processExecutiveDetailsMetrics(): " + e);
				}
			}
		}

		LOG.info("Completed CLOUD Details : portfolio_metrics_details . . . . ");
		return true;
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
	 * @return Boolean
	 */
	@SuppressWarnings({ "static-access", "unchecked" })
	
	public Boolean processComponentDetailsMetrics() {
		LOG.info("Processing CLOUD Details : building_block_components . . . . ");
		Query query = new Query(new Criteria().where(METRICSNAME).is(CLOUD));
		List<String> appIds = mongoTemplate.getCollection(EXECUTIVECOLLECTION).distinct(APPID, query.getQueryObject());
		if (!appIds.isEmpty()) {
			for (String appId : appIds) {
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
						CLOUD);
				List<BuildingBlocks> response = buildingBlocksRepository
						.findByMetricLevelIdAndMetricLevelAndMetricType(appId, MetricLevel.COMPONENT, MetricType.CLOUD);
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
						summaryResponse.setCustomField(module.getTeamId());
						summaryResponse.setPoc(appDetails.getPoc());
						summaryResponse.setTotalComponents(1);
						summaryResponse.setTotalExpectedMetrics(1);
						summaryResponse.setUrl(appDetails.getTeamBoardLink());
						summaryResponse.setMetricType(MetricType.CLOUD);
						summaryResponse.setMetricLevel(MetricLevel.COMPONENT);
						buildingBlockResponse.add(summaryResponse);
					}
				}
				if (response != null)
					buildingBlocksRepository.delete(response);
				buildingBlocksRepository.save(buildingBlockResponse);
			}
		}
		LOG.info("Completed CLOUD Details : building_block_components . . . . ");
		return true;
	}

	/**
	 * 
	 * @param module
	 * @return
	 */
	
	private List<MetricSummary> processComponentMetrics(ExecutiveModuleMetrics module) {
		List<MetricSummary> metricSummaryResponseList = new ArrayList<>();
		List<ExecutiveModuleMetrics> modules = new ArrayList<>();
		if (module.getSeries() != null && !module.getSeries().isEmpty()) {
			MetricSummary metricSummaryResponse = new MetricSummary();
			metricSummaryResponse.setName(CLOUD);
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
}
