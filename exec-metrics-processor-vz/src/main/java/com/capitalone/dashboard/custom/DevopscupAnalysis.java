package com.capitalone.dashboard.custom;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.dao.DevopscupDAO;
import com.capitalone.dashboard.exec.model.MetricCount;
import com.capitalone.dashboard.exec.model.MetricLevel;
import com.capitalone.dashboard.exec.model.MetricSummary;
import com.capitalone.dashboard.exec.model.MetricType;
import com.capitalone.dashboard.exec.model.ApplicationDetails;
import com.capitalone.dashboard.exec.model.BuildingBlocks;
import com.capitalone.dashboard.exec.model.DevOpsCupScores;
import com.capitalone.dashboard.exec.model.DevopscupRoundDetails;
import com.capitalone.dashboard.exec.model.ExecutiveSummaryList;
import com.capitalone.dashboard.exec.model.MetricsDetail;
import com.capitalone.dashboard.exec.model.PortfolioResponse;
import com.capitalone.dashboard.exec.repository.ApplicationDetailsRepository;
import com.capitalone.dashboard.exec.repository.BuildingBlocksRepository;
import com.capitalone.dashboard.exec.repository.DevOpsCupScoresRepository;
import com.capitalone.dashboard.exec.repository.DevopscupRoundDetailsRepository;
import com.capitalone.dashboard.exec.repository.ExecutiveSummaryListRepository;
import com.capitalone.dashboard.exec.repository.MetricsDetailRepository;
import com.capitalone.dashboard.exec.repository.PortfolioResponseRepository;
import com.mongodb.MongoClient;

/**
 * Devops Cup Analysis to get DevopsCup card details
 * 
 * @author RHE94MG
 *
 */
@Component
@SuppressWarnings("PMD")
public class DevopscupAnalysis implements MetricsProcessor {
	private static final Logger LOG = LoggerFactory.getLogger(DevopscupAnalysis.class);

	private final DevopscupDAO devopscupDAO;
	private final MetricsDetailRepository metricsDetailRepository;
	private final ExecutiveSummaryListRepository executiveSummaryListRepository;
	private final PortfolioResponseRepository portfolioResponseRepository;
	private final MongoTemplate mongoTemplate;
	private final ApplicationDetailsRepository applicationDetailsRepository;
	private final BuildingBlocksRepository buildingBlocksRepository;
	private final DevOpsCupScoresRepository devopscupScoreRepository;
	private final DevopscupRoundDetailsRepository devopscupRoundDetailsRepository;
	private static final String DEVOPSCUP = "devopscup";
	private static final String ENGGEXCELPOINTS = "enggExcelPoints";
	private static final String ENGGIMPROVEMENTS = "enggImprovements";
	private static final String CLOUDEXCELPOINTS = "cloudExcelPoints";
	private static final String CLOUDIMPROVEMENTS = "cloudImprovements";
	private static final String TOTALPOINTS = "totalPoints";
	private static final String TOTALPERCENT = "totalPercent";
	private static final String TYPE = "type";
	private static final String METRICSNAME = "metricsName";
	private static final String APPMETRICDETAILS = "app_metrics_details";
	private static final String APPID = "appId";
	private static final String DEVOPSCUPSCORES = "devopscup_scores";
	private static final String MESSAGE = "No DevOps Cup Scores";
	private Boolean isDevopsCupUpdate = true;

	/**
	 * 
	 * @param devopscupDAO
	 * @param metricsDetailRepository
	 * @param executiveSummaryListRepository
	 * @param portfolioResponseRepository
	 * @param mongoTemplate
	 * @param applicationDetailsRepository
	 * @param buildingBlocksRepository
	 * @param devopscupScoreRepository
	 * @param devopscupRoundDetailsRepository
	 */
	@Autowired
	public DevopscupAnalysis(DevopscupDAO devopscupDAO, MetricsDetailRepository metricsDetailRepository,
			ExecutiveSummaryListRepository executiveSummaryListRepository,
			PortfolioResponseRepository portfolioResponseRepository, MongoTemplate mongoTemplate,
			BuildingBlocksRepository buildingBlocksRepository,
			ApplicationDetailsRepository applicationDetailsRepository,
			DevOpsCupScoresRepository devopscupScoreRepository,
			DevopscupRoundDetailsRepository devopscupRoundDetailsRepository) {
		this.devopscupDAO = devopscupDAO;
		this.metricsDetailRepository = metricsDetailRepository;
		this.executiveSummaryListRepository = executiveSummaryListRepository;
		this.portfolioResponseRepository = portfolioResponseRepository;
		this.mongoTemplate = mongoTemplate;
		this.applicationDetailsRepository = applicationDetailsRepository;
		this.buildingBlocksRepository = buildingBlocksRepository;
		this.devopscupScoreRepository = devopscupScoreRepository;
		this.devopscupRoundDetailsRepository = devopscupRoundDetailsRepository;
	}

	/**
	 * process Metrics Details response
	 * 
	 * @return Boolean
	 */
	public Boolean processMetricsDetailResponse() {
		LOG.info("Processing DEVOPSCUP Metrics Details  . . . . ");
		try {
			List<DevOpsCupScores> devopscupList = (List<DevOpsCupScores>) devopscupScoreRepository.findAll();
			for (DevOpsCupScores devopscupDetails : devopscupList) {
				String appId = devopscupDetails.getAppId();
				MetricsDetail metricDetailResponseProcessed = processMetricDetailResponse(devopscupDetails);
				MetricsDetail metricDetailsStored = metricsDetailRepository.findByMetricLevelIdAndLevelAndType(appId,
						MetricLevel.PRODUCT, MetricType.DEVOPSCUP);
				if (metricDetailsStored != null) {
					metricsDetailRepository.delete(metricDetailsStored);
				}
				metricsDetailRepository.save(metricDetailResponseProcessed);
			}
		} catch (Exception e) {
			LOG.info("Error inside Devopscup Analysis file " + e);
		}
		LOG.info("Completed DevopsCup Metrics Details : app_metrics_details . . . . ");
		return true;
	}

	private MetricsDetail processMetricDetailResponse(DevOpsCupScores devopscupDetails) {
		MetricsDetail metricDetailResponse = new MetricsDetail();
		metricDetailResponse.setMetricLevelId(devopscupDetails.getAppId());
		metricDetailResponse.setLevel(MetricLevel.PRODUCT);
		metricDetailResponse.setType(MetricType.DEVOPSCUP);
		metricDetailResponse.setSummary(processMetricsSummary(devopscupDetails.getAppId()));
		return metricDetailResponse;
	}

	private MetricSummary processMetricsSummary(String appId) {
		MetricSummary summaryResponse = new MetricSummary();
		DevOpsCupScores devopsCupDetails = devopscupScoreRepository.getDevOpsCupScoresByAppId(appId);
		Long enggExcelPoints = 0L;
		Long cloudExcelPoints = 0L;
		Long totalPoints = 0L;
		Double enggImprovements = 0.0;
		Double cloudImprovements = 0.0;
		Double totalPercent = 0.0;
		int reportingComponent = 0;
		boolean dataAvailable = false;
		Long lastUpdated = 0L;
		if (devopsCupDetails != null) {
			enggExcelPoints = devopsCupDetails.getEnggExcelPoints();
			enggImprovements = devopsCupDetails.getEnggExcel().getTotalImprovements();
			cloudExcelPoints = devopsCupDetails.getCloudExcelPoints();
			cloudImprovements = devopsCupDetails.getCloudExcel().getTotalImprovements();
			totalPoints = devopsCupDetails.getTotalPoints();
			totalPercent = devopsCupDetails.getTotalPercent();
			reportingComponent++;
			lastUpdated = devopsCupDetails.getTimeStamp();
			dataAvailable = true;
		}

		List<MetricCount> metricCountResponseList = new ArrayList<>();

		MetricCount metricCount = new MetricCount();
		HashMap<String, String> hsh = new HashMap<>();

		hsh.put(TYPE, ENGGEXCELPOINTS);
		metricCount.setLabel(hsh);
		metricCount.setValue(enggExcelPoints);
		metricCountResponseList.add(metricCount);

		metricCount = new MetricCount();
		hsh = new HashMap<>();
		hsh.put(TYPE, ENGGIMPROVEMENTS);
		metricCount.setLabel(hsh);
		metricCount.setValue(enggImprovements);
		metricCountResponseList.add(metricCount);

		metricCount = new MetricCount();
		hsh = new HashMap<>();
		hsh.put(TYPE, CLOUDEXCELPOINTS);
		metricCount.setLabel(hsh);
		metricCount.setValue(cloudExcelPoints);
		metricCountResponseList.add(metricCount);

		metricCount = new MetricCount();
		hsh = new HashMap<>();
		hsh.put(TYPE, CLOUDIMPROVEMENTS);
		metricCount.setLabel(hsh);
		metricCount.setValue(cloudImprovements);
		metricCountResponseList.add(metricCount);

		metricCount = new MetricCount();
		hsh = new HashMap<>();
		hsh.put(TYPE, TOTALPOINTS);
		metricCount.setLabel(hsh);
		metricCount.setValue(totalPoints);
		metricCountResponseList.add(metricCount);

		metricCount = new MetricCount();
		hsh = new HashMap<>();
		hsh.put(TYPE, TOTALPERCENT);
		metricCount.setLabel(hsh);
		metricCount.setValue(totalPercent);

		metricCountResponseList.add(metricCount);
		summaryResponse.setCounts(metricCountResponseList);
		summaryResponse.setTotalComponents(1);
		summaryResponse.setReportingComponents(reportingComponent);
		summaryResponse.setName(DEVOPSCUP);
		summaryResponse.setDataAvailable(dataAvailable);
		summaryResponse.setLastScanned(lastUpdated == 0L ? new Date() : new Date(lastUpdated));

		if (!dataAvailable)
			summaryResponse.setConfMessage(MESSAGE);

		return summaryResponse;
	}

	/**
	 * @param ...
	 * @return Boolean
	 */
	public Boolean processExecutiveDetailsMetrics() {
		LOG.info("Processing DevopsCup Executive Details : portfolio_metrics_details . . . . ");
		try {

			List<ExecutiveSummaryList> executiveSummaryLists = (List<ExecutiveSummaryList>) executiveSummaryListRepository
					.findAll();
			List<String> devopscupAppList = getDistinctDevopscupApps();
			if (!executiveSummaryLists.isEmpty()) {
				for (ExecutiveSummaryList executiveSummaryList : executiveSummaryLists) {
					String eid = executiveSummaryList.getEid();
					MetricsDetail metricPortfolioDetailResponse = metricsDetailRepository
							.findByMetricLevelIdAndLevelAndType(eid, MetricLevel.PORTFOLIO, MetricType.DEVOPSCUP);
					if (metricPortfolioDetailResponse == null)
						metricPortfolioDetailResponse = new MetricsDetail();
					metricPortfolioDetailResponse.setType(MetricType.DEVOPSCUP);
					metricPortfolioDetailResponse.setLevel(MetricLevel.PORTFOLIO);
					metricPortfolioDetailResponse.setMetricLevelId(eid);
					metricPortfolioDetailResponse.setCustomField(getPortfolioId(eid));
					List<String> exeAppIdList = executiveSummaryList.getAppId();
					metricPortfolioDetailResponse.setSummary(processExecutiveSummary(exeAppIdList));
					metricPortfolioDetailResponse.setTimeSeries(null);

					if (metricPortfolioDetailResponse.getSummary() != null) {
						metricPortfolioDetailResponse.getSummary().setTotalComponents(devopscupAppList.size());
						int reportingApps = getReportingApp(devopscupAppList, exeAppIdList);
						metricPortfolioDetailResponse.getSummary().setReportingComponents(reportingApps);
					}
					metricsDetailRepository.save(metricPortfolioDetailResponse);
				}
			}
		} catch (Exception ex) {
			LOG.error("Error occurred in  DevopsCup Details  ::::" + ex);
		}

		LOG.info("Completed DevopsCup Executive Details : portfolio_metrics_details . . . . ");
		return true;

	}

	/**
	 * 
	 * @return List<String>
	 */
	private List<String> getDistinctDevopscupApps() {
		return mongoTemplate.getCollection(DEVOPSCUPSCORES).distinct(APPID);
	}

	private int getReportingApp(List<String> devopscupAppList, List<String> appIdList) {
		Set<String> intersect = new HashSet<>(appIdList);
		intersect.retainAll(devopscupAppList);
		return intersect.size();
	}

	private String getPortfolioId(String eid) {
		PortfolioResponse portfolioResponse = portfolioResponseRepository.findByEid(eid);
		return portfolioResponse == null ? null : portfolioResponse.getId().toString();
	}

	private MetricSummary processExecutiveSummary(List<String> configuredAppId) {
		MetricSummary metricSummaryResponse = new MetricSummary();
		List<MetricsDetail> modules = new ArrayList<>();
		if (configuredAppId != null && !configuredAppId.isEmpty()) {
			for (String appId : configuredAppId) {
				MetricsDetail metricDetailResponseStored = metricsDetailRepository.findByMetricLevelIdAndLevelAndType(appId,
						MetricLevel.PRODUCT, MetricType.DEVOPSCUP);
				modules.add(metricDetailResponseStored);
			}
			metricSummaryResponse.setLastUpdated(new Date(System.currentTimeMillis()));
			metricSummaryResponse.setCounts(processMetricsSummaryCounts(modules));
			metricSummaryResponse.setTrendSlope(0.0);
			metricSummaryResponse.setName(DEVOPSCUP);
			metricSummaryResponse.setDataAvailable(true);
			metricSummaryResponse.setLastScanned(getLastScannedDate());
			return metricSummaryResponse;
		}
		return null;
	}

	private List<MetricCount> processMetricsSummaryCounts(List<MetricsDetail> metricDetailResponseList) {

		List<MetricCount> metricCountResponseList = new ArrayList<>();
		String appId = "";
		try {
			Double enggExcelPoints = 0.0;
			Double cloudExcelPoints = 0.0;
			Double totalPoints = 0.0;
			Double enggImprovements = 0.0;
			Double cloudImprovements = 0.0;
			Double totalPercent = 0.0;
			int noOfResponse = metricDetailResponseList != null ? metricDetailResponseList.size() : 1;
			for (MetricsDetail metricDetailResponse : metricDetailResponseList) {
				if (metricDetailResponse != null) {
					MetricSummary metricSummaryResponse = metricDetailResponse.getSummary();
					appId = metricDetailResponse.getMetricLevelId();
					List<MetricCount> mCountResponseList = metricSummaryResponse.getCounts();
					if (!mCountResponseList.isEmpty()) {
						for (MetricCount metricCountResponse : mCountResponseList) {
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(ENGGEXCELPOINTS))
								enggExcelPoints += metricCountResponse.getValue();
							else if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(ENGGIMPROVEMENTS))
								enggImprovements += metricCountResponse.getValue();
							else if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(CLOUDEXCELPOINTS))
								cloudExcelPoints += metricCountResponse.getValue();
							else if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(CLOUDIMPROVEMENTS))
								cloudImprovements += metricCountResponse.getValue();
							else if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(TOTALPOINTS))
								totalPoints += metricCountResponse.getValue();
							else if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(TOTALPERCENT))
								totalPercent += metricCountResponse.getValue();
						}
					}
				}
			}

			MetricCount metricCount = new MetricCount();
			HashMap<String, String> hsh = new HashMap<>();
			hsh.put(TYPE, ENGGEXCELPOINTS);
			metricCount.setValue(enggExcelPoints / noOfResponse);
			metricCount.setLabel(hsh);
			metricCountResponseList.add(metricCount);

			metricCount = new MetricCount();
			hsh = new HashMap<>();
			hsh.put(TYPE, ENGGIMPROVEMENTS);
			metricCount.setValue(enggImprovements / noOfResponse);
			metricCount.setLabel(hsh);
			metricCountResponseList.add(metricCount);

			metricCount = new MetricCount();
			hsh = new HashMap<>();
			hsh.put(TYPE, CLOUDEXCELPOINTS);
			metricCount.setValue(cloudExcelPoints / noOfResponse);
			metricCount.setLabel(hsh);
			metricCountResponseList.add(metricCount);

			metricCount = new MetricCount();
			hsh = new HashMap<>();
			hsh.put(TYPE, CLOUDIMPROVEMENTS);
			metricCount.setValue(cloudImprovements / noOfResponse);
			metricCount.setLabel(hsh);
			metricCountResponseList.add(metricCount);

			metricCount = new MetricCount();
			hsh = new HashMap<>();
			hsh.put(TYPE, TOTALPOINTS);
			metricCount.setValue(totalPoints / noOfResponse);
			metricCount.setLabel(hsh);
			metricCountResponseList.add(metricCount);

			metricCount = new MetricCount();
			hsh = new HashMap<>();
			hsh.put(TYPE, TOTALPERCENT);
			metricCount.setValue(totalPercent / noOfResponse);
			metricCount.setLabel(hsh);

			metricCount.setValue(totalPercent);
			metricCountResponseList.add(metricCount);
		} catch (Exception ex) {
			LOG.error("Error Captured while calculating executive wise data for DevopsCup ::  " + ex
					+ "   for appId :: " + appId);
		}
		return metricCountResponseList;
	}

	/**
	 * 
	 * @return boolean
	 */
	public Boolean processBuildingBlockMetrics() {
		String strAppId = "";
		LOG.info("Processing DevopsCup Details : building_block_metrics . . . . ");
		try {
			Query query = new Query(new Criteria().where(METRICSNAME).is(DEVOPSCUP));
			List<String> appIds = mongoTemplate.getCollection(APPMETRICDETAILS).distinct("appId",
					query.getQueryObject());
			if (!appIds.isEmpty()) {
				for (String appId : appIds) {
					strAppId = appId;
					BuildingBlocks buildingBlockMetricSummaryResponse = buildingBlocksRepository
							.findByMetricLevelIdAndMetricLevel(appId, MetricLevel.PRODUCT);
					if (buildingBlockMetricSummaryResponse == null)
						buildingBlockMetricSummaryResponse = new BuildingBlocks();
					ApplicationDetails appDetails = applicationDetailsRepository.findByAppId(appId);
					MetricsDetail metricDetailResponse = metricsDetailRepository.findByMetricLevelIdAndLevelAndType(appId,
							MetricLevel.PRODUCT, MetricType.DEVOPSCUP);
					if (metricDetailResponse != null && appDetails != null) {
						buildingBlockMetricSummaryResponse.setMetricLevelId(appId);
						buildingBlockMetricSummaryResponse.setLob(appDetails.getLob());
						buildingBlockMetricSummaryResponse.setName(appDetails.getAppName());
						List<MetricSummary> metricsResponseStored = buildingBlockMetricSummaryResponse.getMetrics();
						List<MetricSummary> metricsResponseProcessed = new ArrayList<>();
						if (metricsResponseStored != null && !metricsResponseStored.isEmpty()) {
							for (MetricSummary metricSummaryResponse : metricsResponseStored) {
								if (metricSummaryResponse.getName() == null) {
									metricsResponseProcessed.remove(metricSummaryResponse);
								}
								if (!metricSummaryResponse.getName().equalsIgnoreCase(DEVOPSCUP))
									metricsResponseProcessed.add(metricSummaryResponse);
							}
						}
						metricsResponseProcessed.add(metricDetailResponse.getSummary());
						buildingBlockMetricSummaryResponse.setMetrics(metricsResponseProcessed);
						buildingBlockMetricSummaryResponse.setTotalComponents(1);
						buildingBlockMetricSummaryResponse.setTotalExpectedMetrics(1);
						buildingBlockMetricSummaryResponse.setPoc(appDetails.getPoc());
						buildingBlockMetricSummaryResponse.setCustomField(appDetails.getVastId());
						buildingBlockMetricSummaryResponse.setMetricLevel(MetricLevel.PRODUCT);
						buildingBlocksRepository.save(buildingBlockMetricSummaryResponse);
					}
				}
			}
			LOG.info("Completed Devopscup Details : building_block_metrics . . . . ");
		} catch (Exception e) {
			LOG.info("processBuildingBlockMetrics  Info :: " + strAppId, e);
		}
		return true;
	}

	/**
	 * 
	 * @return
	 */
	public Boolean processComponentDetailsMetrics() {

		try {

			LOG.info("Processing DevOpscup Details : building_block_components . . . . ");
			Query query = new Query(new Criteria().where(METRICSNAME).is(DEVOPSCUP));
			List<String> appIds = mongoTemplate.getCollection(APPMETRICDETAILS).distinct(APPID, query.getQueryObject());
			if (!appIds.isEmpty()) {
				for (String appId : appIds) {
					List<BuildingBlocks> response = buildingBlocksRepository
							.findByMetricLevelIdAndMetricLevelAndMetricType(appId, MetricLevel.COMPONENT, MetricType.DEVOPSCUP);
					List<BuildingBlocks> buildingBlockResponse = new ArrayList<>();
					ApplicationDetails appDetails = applicationDetailsRepository.findByAppId(appId);
					if (appDetails != null) {
						MetricsDetail metricDetailsStored = metricsDetailRepository.findByMetricLevelIdAndLevelAndType(appId,
								MetricLevel.PRODUCT, MetricType.DEVOPSCUP);
						List<MetricSummary> metricsResponseProcessed = new ArrayList<>();
						if (metricDetailsStored != null) {
							metricsResponseProcessed.add(metricDetailsStored.getSummary());
						} else {
							metricsResponseProcessed.add(processMetricsSummary(appId));
						}
						BuildingBlocks summaryResponse = new BuildingBlocks();
						summaryResponse.setMetricLevelId(appId);
						summaryResponse.setLob(appDetails.getLob());
						summaryResponse.setMetrics(metricsResponseProcessed);
						summaryResponse.setName(appId + " " + DEVOPSCUP);
						summaryResponse.setPoc(appDetails.getPoc());
						summaryResponse.setTotalComponents(1);
						summaryResponse.setTotalExpectedMetrics(1);
						summaryResponse.setMetricType(MetricType.DEVOPSCUP);
						summaryResponse.setMetricLevel(MetricLevel.COMPONENT);
						summaryResponse.setUrl(appDetails.getTeamBoardLink());
						buildingBlockResponse.add(summaryResponse);
						if (response != null)
							buildingBlocksRepository.delete(response);
						buildingBlocksRepository.save(buildingBlockResponse);
					}
				}
			}
			LOG.info("Completed DevopsCup Details : building_block_components . . . . ");
		} catch (Exception ex) {
			LOG.info("Error in DevopsCup Details : building_block_components . . . . " + ex);
		}
		return true;
	}

	/**
	 * 
	 * @return boolean
	 */
	public Boolean processExecutiveMetricsDetails() {

		if (isDevopsCupUpdate) {
			LOG.info("Copying DEVOPSCUP Scores   . . . . ");
			MongoClient client = null;
			try {
				/** Connecting to OneHygiea DB */
				client = devopscupDAO.getMongoClient();

				/** delete all entries from Executive Hyg DB **/
				devopscupScoreRepository.deleteAll();

				/** getting details From OneHygieia DB **/
				List<DevOpsCupScores> devopscupDetails = devopscupDAO.getAllDetails(client);
				if (devopscupDetails != null) {
					devopscupScoreRepository.save(devopscupDetails);
					isDevopsCupUpdate = false;
				}

			} catch (Exception ex) {
				LOG.error("Exception while fetching devopscup data :: " + ex);
				return false;
			} finally {
				if (client != null) {
					client.close();
				}
			}
			LOG.info("Completed copying DevopsCup Scores");
		}
		return true;
	}

	/**
	 * 
	 * @return Date
	 */
	private Date getLastScannedDate() {
		Date scannedDate = new Date();
		try {
			SortOperation sortOperation = Aggregation.sort(Sort.Direction.DESC, "timeStamp");
			LimitOperation limitOperation = Aggregation.limit(1);
			Aggregation agg = Aggregation.newAggregation(sortOperation, limitOperation);
			AggregationResults<DevOpsCupScores> result = mongoTemplate.aggregate(agg, DEVOPSCUPSCORES,
					DevOpsCupScores.class);
			DevOpsCupScores devopsScores = result.getUniqueMappedResult();
			scannedDate = new Date(devopsScores.getTimeStamp());
		} catch (Exception ex) {
			LOG.error("Exception in  getLastScannedDate :: " + ex);
		}
		return scannedDate;
	}

	/**
	 * setDevopscupRoundDetails()
	 * 
	 * @return
	 */
	public void setDevopscupRoundDetails() {
		Integer round;
		Integer quarter;
		try {
			Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			c.setTimeZone(TimeZone.getTimeZone("UTC"));
			Calendar baseLineStartCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			baseLineStartCal.setTimeZone(TimeZone.getTimeZone("UTC"));

			Calendar baseLineEndCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			baseLineEndCal.setTimeZone(TimeZone.getTimeZone("UTC"));
			int month = c.get(Calendar.MONTH) / 3;
			int year = c.get(Calendar.YEAR);
			quarter = (month / 3) + 1;
			Date d;
			Date baseLineStartDate;
			Date baseLineEndDate;
			baseLineStartCal.set(Calendar.MONTH, 3);/** Refers April Month */
			baseLineEndCal.set(Calendar.MONTH, 2);/** Refers March Month */

			if (quarter == 1) {
				round = 3;
				c.add(Calendar.MONTH, -3);
				baseLineStartCal.set(Calendar.YEAR, year - 2);
				baseLineEndCal.set(Calendar.YEAR, year - 1);
			} else {
				round = quarter - 1;
				baseLineStartCal.set(Calendar.YEAR, year - 1);
			}
			devopscupRoundDetailsRepository.deleteAll();
			d = c.getTime();
			baseLineStartDate = baseLineStartCal.getTime();
			baseLineEndDate = baseLineEndCal.getTime();
			DevopscupRoundDetails devopscupRoundDet = new DevopscupRoundDetails();
			devopscupRoundDet.setActive(true);
			devopscupRoundDet.setQuarter(quarter);
			devopscupRoundDet.setRound(round);
			devopscupRoundDet.setStartDate(getFirstDayOfQuarter(d));
			devopscupRoundDet.setEndDate(getLastDayOfQuarter(d));
			devopscupRoundDet.setBaseLineStartDate(getFirstDayOfQuarter(baseLineStartDate));
			devopscupRoundDet.setBaseLineEndDate(getLastDayOfQuarter(baseLineEndDate));
			devopscupRoundDet.setMetricsAsOfDate(getLastScannedDate().getTime());
			devopscupRoundDetailsRepository.save(devopscupRoundDet);
		} catch (Exception ex) {
			LOG.error("Exception in setDevopscupRoundDetails :: " + ex);
		}
	}

	private static long getFirstDayOfQuarter(Date date) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal.setTimeZone(TimeZone.getTimeZone("UTC"));
		cal.setTime(date);
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) / 3 * 3);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}

	private static long getLastDayOfQuarter(Date date) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal.setTimeZone(TimeZone.getTimeZone("UTC"));
		cal.setTime(date);
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) / 3 * 3 + 2);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}
}
