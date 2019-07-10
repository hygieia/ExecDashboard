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

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.collector.MetricsProcessorSettings;
import com.capitalone.dashboard.dao.QualityDetailsDAO;
import com.capitalone.dashboard.dao.ServiceNowDAO;
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
import com.capitalone.dashboard.exec.model.vz.ExecutiveComponents;
import com.capitalone.dashboard.exec.model.vz.ExecutiveMetrics;
import com.capitalone.dashboard.exec.model.vz.ExecutiveMetricsSeries;
import com.capitalone.dashboard.exec.model.vz.ExecutiveModuleMetrics;
import com.capitalone.dashboard.exec.model.vz.ExecutiveSummaryList;
import com.capitalone.dashboard.exec.model.vz.FeatureUserStory;
import com.capitalone.dashboard.exec.model.vz.JiraDetailsFinal;
import com.capitalone.dashboard.exec.model.vz.JiraInfo;
import com.capitalone.dashboard.exec.model.vz.MetricsDetail;
import com.capitalone.dashboard.exec.model.vz.PortfolioResponse;
import com.capitalone.dashboard.exec.model.vz.SeriesCount;
import com.capitalone.dashboard.exec.model.vz.ServiceNowIssues;
import com.capitalone.dashboard.exec.model.vz.ServiceNowTicket;
import com.capitalone.dashboard.exec.repository.vz.ApplicationDetailsRepository;
import com.capitalone.dashboard.exec.repository.vz.BuildingBlocksRepository;
import com.capitalone.dashboard.exec.repository.vz.CollectorStatusRepository;
import com.capitalone.dashboard.exec.repository.vz.ExecutiveComponentRepository;
import com.capitalone.dashboard.exec.repository.vz.ExecutiveSummaryListRepository;
import com.capitalone.dashboard.exec.repository.vz.MetricsDetailRepository;
import com.capitalone.dashboard.exec.repository.vz.PortfolioResponseRepository;
import com.capitalone.dashboard.utils.GenericMethods;
import com.capitalone.dashboard.utils.LinearRegression;
import com.mongodb.MongoClient;

/**
 * QualityAnalysis
 * 
 * @param
 * @return
 * @author Hari
 */
@Component
@SuppressWarnings("PMD")
public class QualityAnalysis implements MetricsProcessor {

	private final QualityDetailsDAO qualityDetailsDAO;
	private final VastDetailsDAO vastDetailsDAO;
	private final ServiceNowDAO serviceNowDAO;
	private final ExecutiveComponentRepository executiveComponentRepository;
	private final MetricsDetailRepository metricsDetailRepository;
	private final ApplicationDetailsRepository applicationDetailsRepository;
	private final CollectorStatusRepository collectorStatusRepository;
	private final ExecutiveSummaryListRepository executiveSummaryListRepository;
	private final PortfolioResponseRepository portfolioResponseRepository;
	private final BuildingBlocksRepository buildingBlocksRepository;
	private final MongoTemplate mongoTemplate;
	private final MetricsProcessorSettings metricsSettings;
	private final GenericMethods genericMethods;

	private static final String APPID = "appId";
	private static final String QUALITY = "quality";
	private static final String METRICSNAME = "metrics.metricsName";
	private static final String EXECUTIVEMETRICS = "executives_metrics";
	private static final String CMIS = "cmis";
	private static final String SN = "serviceNow";
	private static final String CLOSEDSN = "closeserviceNow";
	private static final String OPENSN = "openserviceNow";
	private static final String SNONE = "serviceNowOne";
	private static final String SNTWO = "serviceNowTwo";
	private static final String SNTHREE = "serviceNowThree";
	private static final String SNFOUR = "serviceNowFour";
	private static final String SNONEOPEN = "openserviceNowOne";
	private static final String SNTWOOPEN = "openserviceNowTwo";
	private static final String SNTHREEOPEN = "openserviceNowThree";
	private static final String SNFOUROPEN = "openserviceNowFour";
	private static final String SNONECLOSED = "closeserviceNowOne";
	private static final String SNTWOCLOSED = "closeserviceNowTwo";
	private static final String SNTHREECLOSED = "closeserviceNowThree";
	private static final String SNFOURCLOSED = "closeserviceNowFour";
	private static final String PRIORITY = "priority";
	private static final String NORMAL = "normal";
	private static final String BLOCKER = "blocker";
	private static final String HIGH = "high";
	private static final String LOW = "low";
	private static final String TOTAL = "total";
	private static final String NORMALDEFECTS = "NormalDefects";
	private static final String BLOCKERDEFECTS = "BlockerDefects";
	private static final String LOWDEFECTS = "LowDefects";
	private static final String HIGHDEFECTS = "HighDefects";
	private static final String TOTALDEFECTS = "TotalDefects";
	private static final String NORMALOPENDEFECTS = "NormalOpenDefects";
	private static final String BLOCKEROPENDEFECTS = "BlockerOpenDefects";
	private static final String LOWOPENDEFECTS = "LowOpenDefects";
	private static final String HIGHOPENDEFECTS = "HighOpenDefects";
	private static final String TOTALOPENDEFECTS = "TotalOpenDefects";
	private static final String NORMALCLOSEDDEFECTS = "NormalClosedDefects";
	private static final String BLOCKERCLOSEDDEFECTS = "BlockerClosedDefects";
	private static final String LOWCLOSEDDEFECTS = "LowClosedDefects";
	private static final String HIGHCLOSEDDEFECTS = "HighClosedDefects";
	private static final String TOTALCLOSEDDEFECTS = "TotalClosedDefects";
	private static final String CRITICAL = "Critical";
	private static final String ESCALATED = "Escalated";
	private static final String HIGHEST = "Highest";
	private static final String IMMEDIATE = "Immediate";
	private static final String VERYHIGH = "Very High";
	private static final String MAJOR = "Major";
	private static final String URGENT = "Urgent";
	private static final String HIGHTWO = "2-High";
	private static final String MEDIUM = "Medium";
	private static final String MINOR = "Minor";
	private static final String UNKNOWN = "Unknown";
	private static final String LOWEST = "Lowest";
	private static final String ENHANCEMENT = "Enhancement";
	private static final String SNDETAILS = " SN Details";
	private static final String TYPE = "type";
	private static final String CHANGEFAILURERATE = "changeFailureRate";
	public static final String ISO_DATE_TIME_FORMATZ = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS";
	private static final String OPEN_CMIS = "opencmis";
	private static final String OPEN_NORMAL = "opennormal";
	private static final String OPEN_BLOCKER = "openblocker";
	private static final String OPEN_HIGH = "openhigh";
	private static final String OPEN_LOW = "openlow";
	private static final String OPEN_TOTAL = "opentotal";
	private static final String CLOSED_CMIS = "closecmis";
	private static final String CLOSED_NORMAL = "closenormal";
	private static final String CLOSED_BLOCKER = "closeblocker";
	private static final String CLOSED_HIGH = "closehigh";
	private static final String CLOSED_LOW = "closelow";
	private static final String CLOSED_TOTAL = "closetotal";
	private static final String MESSAGE = "No Jira/CMIS/SN Defects";

	private static final Logger LOG = LoggerFactory.getLogger(QualityAnalysis.class);

	/**
	 * QualityAnalysis
	 * 
	 * @param qualityDetailsDAO
	 * @param executiveComponentRepository
	 * @param mongoTemplate
	 * @param serviceNowDAO
	 * @param executiveSummaryListRepository
	 * @param metricsDetailRepository
	 * @param applicationDetailsRepository
	 * @param buildingBlocksRepository
	 * @param collectorStatusRepository
	 * @param portfolioResponseRepository
	 * @param vastDetailsDAO
	 * @param metricsSettings
	 * @param genericMethods
	 * @return
	 */
	@Autowired
	public QualityAnalysis(QualityDetailsDAO qualityDetailsDAO,
			ExecutiveComponentRepository executiveComponentRepository, ServiceNowDAO serviceNowDAO,
			MetricsDetailRepository metricsDetailRepository, ApplicationDetailsRepository applicationDetailsRepository,
			CollectorStatusRepository collectorStatusRepository,
			ExecutiveSummaryListRepository executiveSummaryListRepository,
			PortfolioResponseRepository portfolioResponseRepository, BuildingBlocksRepository buildingBlocksRepository,
			VastDetailsDAO vastDetailsDAO, MongoTemplate mongoTemplate, MetricsProcessorSettings metricsSettings,
			GenericMethods genericMethods) {
		this.qualityDetailsDAO = qualityDetailsDAO;
		this.serviceNowDAO = serviceNowDAO;
		this.executiveComponentRepository = executiveComponentRepository;
		this.metricsDetailRepository = metricsDetailRepository;
		this.applicationDetailsRepository = applicationDetailsRepository;
		this.collectorStatusRepository = collectorStatusRepository;
		this.executiveSummaryListRepository = executiveSummaryListRepository;
		this.portfolioResponseRepository = portfolioResponseRepository;
		this.buildingBlocksRepository = buildingBlocksRepository;
		this.mongoTemplate = mongoTemplate;
		this.metricsSettings = metricsSettings;
		this.vastDetailsDAO = vastDetailsDAO;
		this.genericMethods = genericMethods;
	}

	/**
	 * processQualityDetails
	 * 
	 * @return Boolean
	 */
	public Boolean processExecutiveMetricsDetails() {
		LOG.info("Processing Quality Details : executives_metrics . . . . ");
		MongoClient client = null;
		try {
			client = qualityDetailsDAO.getMongoClient();
			List<String> appIds = qualityDetailsDAO.getEntireAppList(client);
			for (String appId : appIds) {
				ApplicationDetails appDetails = applicationDetailsRepository.findByAppId(appId);
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
						QUALITY);
				if (executiveComponents == null)
					executiveComponents = new ExecutiveComponents();
				executiveComponents.setAppId(appId);
				if (appDetails != null) {
					executiveComponents.setAppName(appDetails.getAppName());
					executiveComponents.setTeamBoardLink(appDetails.getTeamBoardLink());
				}
				executiveComponents.setMetrics(processAppMetrics(client, appId));
				executiveComponentRepository.save(executiveComponents);
			}
		} catch (Exception e) {
			LOG.info("processQualityDetails Quality Analysis Error :: " + e.getMessage());
		} finally {
			if (client != null)
				client.close();
		}
		LOG.info("Completed Quality Details : executives_metrics . . . . ");
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
		executiveMetrics.setMetricsName(QUALITY);

		List<JiraDetailsFinal> jiraDetailsList = qualityDetailsDAO.getEntireProjectList(client, appId);
		for (JiraDetailsFinal jiraDetails : jiraDetailsList) {
			List<JiraInfo> jiraInfoList = jiraDetails.getJiraInfo();
			for (JiraInfo jiraInfo : jiraInfoList) {
				String projectName = jiraInfo.getProjectName();
				ExecutiveModuleMetrics executiveModuleMetrics = getModuleMetrics(projectName, jiraInfo.getProjectKey(),
						client);
				modules.add(executiveModuleMetrics);
			}
		}
		modules.add(processSNData(appId, client));
		executiveMetrics.setTrendSlope(getTrendSlopesForModules(modules));
		executiveMetrics.setModules(modules);
		execMetricsList.add(executiveMetrics);
		return execMetricsList;
	}

	/**
	 * getModuleMetrics
	 * 
	 * @param projectName
	 * @param projectKey
	 * @param client
	 * @return
	 */

	private ExecutiveModuleMetrics getModuleMetrics(String projectName, String projectKey, MongoClient client) {
		ExecutiveModuleMetrics execModuleMetrics = new ExecutiveModuleMetrics();
		List<ExecutiveMetricsSeries> execMetricsSeriesList = new ArrayList<>();
		execMetricsSeriesList = getSeriesCountList(projectName, client);
		execModuleMetrics.setModuleName(projectName);
		execModuleMetrics.setTeamId(projectKey);
		execModuleMetrics.setLastScanned(getLastCreatedDateForJiraModule(projectName, client));
		execModuleMetrics.setLastUpdated(getISODateTime(System.currentTimeMillis()));
		execModuleMetrics.setTrendSlope(getTrendSlope(execMetricsSeriesList));
		execModuleMetrics.setSeries(execMetricsSeriesList);
		return execModuleMetrics;
	}

	/**
	 * 
	 * @param appId
	 * @param client
	 * @return
	 */

	private ExecutiveModuleMetrics processSNData(String appId, MongoClient client) {
		ExecutiveModuleMetrics execModuleMetrics = new ExecutiveModuleMetrics();
		if (appId != null) {
			List<ExecutiveMetricsSeries> execMetricsSeriesList = getSeriesCountSNList(appId, client);
			execModuleMetrics.setModuleName(appId + SNDETAILS);
			execModuleMetrics.setLastScanned(getLastCreatedDateForSNModule(appId));
			execModuleMetrics.setLastUpdated(getISODateTime(System.currentTimeMillis()));
			execModuleMetrics.setTrendSlope(getTrendSlope(execMetricsSeriesList));
			execModuleMetrics.setSeries(execMetricsSeriesList);
		}
		return execModuleMetrics;
	}

	/**
	 * getSeriesCountList
	 * 
	 * @param projectName
	 * @param client
	 * @return
	 */

	private List<ExecutiveMetricsSeries> getSeriesCountList(String projectName, MongoClient client) {

		List<ExecutiveMetricsSeries> executiveMetricsSeriesList = new ArrayList<>();
		String formatDate = getISODateTime(90);
		Boolean proceed = qualityDetailsDAO.isDataAvailableForQuality(projectName, formatDate, client);
		if (proceed) {
			for (int i = 0; i < 90; i++) {
				ExecutiveMetricsSeries executiveMetricsSeries = new ExecutiveMetricsSeries();
				List<SeriesCount> seriesCountList = new ArrayList<>();

				SeriesCount priorityNormal = new SeriesCount();
				SeriesCount priorityBlocker = new SeriesCount();
				SeriesCount priorityHigh = new SeriesCount();
				SeriesCount priorityLow = new SeriesCount();
				SeriesCount priorityTotal = new SeriesCount();
				SeriesCount priorityClosedNormal = new SeriesCount();
				SeriesCount priorityClosedBlocker = new SeriesCount();
				SeriesCount priorityClosedHigh = new SeriesCount();
				SeriesCount priorityClosedLow = new SeriesCount();
				SeriesCount priorityClosedTotal = new SeriesCount();
				SeriesCount priorityOpenNormal = new SeriesCount();
				SeriesCount priorityOpenBlocker = new SeriesCount();
				SeriesCount priorityOpenHigh = new SeriesCount();
				SeriesCount priorityOpenLow = new SeriesCount();
				SeriesCount priorityOpenTotal = new SeriesCount();

				Map<String, String> normalDefects = new HashMap<>();
				Map<String, String> blockerDefects = new HashMap<>();
				Map<String, String> highDefects = new HashMap<>();
				Map<String, String> lowDefects = new HashMap<>();
				Map<String, String> totalDefects = new HashMap<>();
				Map<String, String> normalOpenDefects = new HashMap<>();
				Map<String, String> blockerOpenDefects = new HashMap<>();
				Map<String, String> highOpenDefects = new HashMap<>();
				Map<String, String> lowOpenDefects = new HashMap<>();
				Map<String, String> totalOpenDefects = new HashMap<>();
				Map<String, String> normalClosedDefects = new HashMap<>();
				Map<String, String> blockerClosedDefects = new HashMap<>();
				Map<String, String> highClosedDefects = new HashMap<>();
				Map<String, String> lowClosedDefects = new HashMap<>();
				Map<String, String> totalClosedDefects = new HashMap<>();

				normalDefects.put(PRIORITY, NORMAL);
				blockerDefects.put(PRIORITY, BLOCKER);
				highDefects.put(PRIORITY, HIGH);
				lowDefects.put(PRIORITY, LOW);
				totalDefects.put(PRIORITY, TOTAL);
				normalOpenDefects.put(PRIORITY, OPEN_NORMAL);
				blockerOpenDefects.put(PRIORITY, OPEN_BLOCKER);
				highOpenDefects.put(PRIORITY, OPEN_HIGH);
				lowOpenDefects.put(PRIORITY, OPEN_LOW);
				totalOpenDefects.put(PRIORITY, OPEN_TOTAL);
				normalClosedDefects.put(PRIORITY, CLOSED_NORMAL);
				blockerClosedDefects.put(PRIORITY, CLOSED_BLOCKER);
				highClosedDefects.put(PRIORITY, CLOSED_HIGH);
				lowClosedDefects.put(PRIORITY, CLOSED_LOW);
				totalClosedDefects.put(PRIORITY, CLOSED_TOTAL);

				String formattedDate = getISODateTime(i);
				List<FeatureUserStory> userStoryList = qualityDetailsDAO.getDefectsList(projectName, formattedDate,
						client);
				Map<String, Object> defectsMap = getDefectsDetailsMap(userStoryList);

				priorityNormal.setCount((long) defectsMap.get(NORMALDEFECTS));
				priorityNormal.setLabel(normalDefects);
				priorityBlocker.setCount((long) defectsMap.get(BLOCKERDEFECTS));
				priorityBlocker.setLabel(blockerDefects);
				priorityHigh.setCount((long) defectsMap.get(HIGHDEFECTS));
				priorityHigh.setLabel(highDefects);
				priorityLow.setCount((long) defectsMap.get(LOWDEFECTS));
				priorityLow.setLabel(lowDefects);
				priorityTotal.setCount((long) defectsMap.get(TOTALDEFECTS));
				priorityTotal.setLabel(totalDefects);
				priorityClosedNormal.setCount((long) defectsMap.get(NORMALCLOSEDDEFECTS));
				priorityClosedNormal.setLabel(normalClosedDefects);
				priorityClosedBlocker.setCount((long) defectsMap.get(BLOCKERCLOSEDDEFECTS));
				priorityClosedBlocker.setLabel(blockerClosedDefects);
				priorityClosedHigh.setCount((long) defectsMap.get(HIGHCLOSEDDEFECTS));
				priorityClosedHigh.setLabel(highClosedDefects);
				priorityClosedLow.setCount((long) defectsMap.get(LOWCLOSEDDEFECTS));
				priorityClosedLow.setLabel(lowClosedDefects);
				priorityClosedTotal.setCount((long) defectsMap.get(TOTALCLOSEDDEFECTS));
				priorityClosedTotal.setLabel(totalClosedDefects);
				priorityOpenNormal.setCount((long) defectsMap.get(NORMALOPENDEFECTS));
				priorityOpenNormal.setLabel(normalOpenDefects);
				priorityOpenBlocker.setCount((long) defectsMap.get(BLOCKEROPENDEFECTS));
				priorityOpenBlocker.setLabel(blockerOpenDefects);
				priorityOpenHigh.setCount((long) defectsMap.get(HIGHOPENDEFECTS));
				priorityOpenHigh.setLabel(highOpenDefects);
				priorityOpenLow.setCount((long) defectsMap.get(LOWOPENDEFECTS));
				priorityOpenLow.setLabel(lowOpenDefects);
				priorityOpenTotal.setCount((long) defectsMap.get(TOTALOPENDEFECTS));
				priorityOpenTotal.setLabel(totalOpenDefects);

				seriesCountList.add(priorityNormal);
				seriesCountList.add(priorityBlocker);
				seriesCountList.add(priorityHigh);
				seriesCountList.add(priorityLow);
				seriesCountList.add(priorityTotal);
				seriesCountList.add(priorityOpenNormal);
				seriesCountList.add(priorityOpenBlocker);
				seriesCountList.add(priorityOpenHigh);
				seriesCountList.add(priorityOpenLow);
				seriesCountList.add(priorityOpenTotal);
				seriesCountList.add(priorityClosedNormal);
				seriesCountList.add(priorityClosedBlocker);
				seriesCountList.add(priorityClosedHigh);
				seriesCountList.add(priorityClosedLow);
				seriesCountList.add(priorityClosedTotal);

				executiveMetricsSeries.setDaysAgo(i);
				executiveMetricsSeries.setTimeStamp(getTimeStamp(i));
				executiveMetricsSeries.setCounts(seriesCountList);
				executiveMetricsSeriesList.add(executiveMetricsSeries);
			}
		}

		return executiveMetricsSeriesList;
	}

	/**
	 * getDefectsDetailsMap
	 * 
	 * @param userStoryList
	 * @return
	 */

	private Map<String, Object> getDefectsDetailsMap(List<FeatureUserStory> userStoryList) {
		Map<String, Object> rtMap = new HashMap<>();
		long normalDefectsCount = 0;
		long blockerDefectsCount = 0;
		long highDefectsCount = 0;
		long lowDefectsCount = 0;
		long totalDefectsCount = 0;
		long normalClosedDefectsCount = 0;
		long blockerClosedDefectsCount = 0;
		long highClosedDefectsCount = 0;
		long lowClosedDefectsCount = 0;
		long totalClosedDefectsCount = 0;

		try {
			if (userStoryList != null && !userStoryList.isEmpty()) {
				Iterator<FeatureUserStory> featureUserStoryItr = userStoryList.iterator();
				while (featureUserStoryItr.hasNext()) {
					FeatureUserStory featureUserStory = featureUserStoryItr.next();
					String priorityLevel = featureUserStory.getPriorityLevel();
					String priority = featureUserStory.getPriority();

					if (priority != null) {
						if (priority.equalsIgnoreCase(CRITICAL) || priority.equalsIgnoreCase(ESCALATED)
								|| priority.equalsIgnoreCase(HIGHEST) || priority.equalsIgnoreCase(IMMEDIATE)
								|| priority.equalsIgnoreCase(VERYHIGH) || priority.equalsIgnoreCase(MAJOR)
								|| priority.equalsIgnoreCase(URGENT)) {
							blockerDefectsCount++;
							blockerClosedDefectsCount += isClosedTicket(featureUserStory);
						} else if (priority.equalsIgnoreCase(MEDIUM) || priority.equalsIgnoreCase(NORMAL)) {
							normalDefectsCount++;
							normalClosedDefectsCount += isClosedTicket(featureUserStory);
						} else if (priority.equalsIgnoreCase(BLOCKER) || priority.equalsIgnoreCase(HIGH)
								|| priority.equalsIgnoreCase(HIGHTWO)) {
							highDefectsCount++;
							highClosedDefectsCount += isClosedTicket(featureUserStory);
						} else if (priority.equalsIgnoreCase(LOW) || priority.equalsIgnoreCase(MINOR)
								|| priority.equalsIgnoreCase(UNKNOWN) || priority.equalsIgnoreCase(LOWEST)
								|| priority.equalsIgnoreCase(ENHANCEMENT)) {
							lowDefectsCount++;
							lowClosedDefectsCount += isClosedTicket(featureUserStory);
						}
					}

					if (priorityLevel != null && priority == null) {
						if (priorityLevel.equalsIgnoreCase(NORMAL)) {
							normalDefectsCount++;
							normalClosedDefectsCount += isClosedTicket(featureUserStory);
						} else if (priorityLevel.equalsIgnoreCase(BLOCKER)) {
							blockerDefectsCount++;
							blockerClosedDefectsCount += isClosedTicket(featureUserStory);
						} else if (priorityLevel.equalsIgnoreCase(HIGH)) {
							highDefectsCount++;
							highClosedDefectsCount += isClosedTicket(featureUserStory);
						} else if (priorityLevel.equalsIgnoreCase(LOW)) {
							lowDefectsCount++;
							lowClosedDefectsCount += isClosedTicket(featureUserStory);
						}

					}
					totalDefectsCount = normalDefectsCount + blockerDefectsCount + highDefectsCount + lowDefectsCount;
					totalClosedDefectsCount = normalClosedDefectsCount + blockerClosedDefectsCount
							+ highClosedDefectsCount + lowClosedDefectsCount;

					if (priority == null && priorityLevel == null) {
						totalDefectsCount++;
						totalClosedDefectsCount += isClosedTicket(featureUserStory);
					}
				}
			}
			rtMap.put(NORMALDEFECTS, normalDefectsCount);
			rtMap.put(BLOCKERDEFECTS, blockerDefectsCount);
			rtMap.put(HIGHDEFECTS, highDefectsCount);
			rtMap.put(LOWDEFECTS, lowDefectsCount);
			rtMap.put(TOTALDEFECTS, totalDefectsCount);
			rtMap.put(NORMALOPENDEFECTS, normalDefectsCount - normalClosedDefectsCount);
			rtMap.put(BLOCKEROPENDEFECTS, blockerDefectsCount - blockerClosedDefectsCount);
			rtMap.put(HIGHOPENDEFECTS, highDefectsCount - highClosedDefectsCount);
			rtMap.put(LOWOPENDEFECTS, lowDefectsCount - lowClosedDefectsCount);
			rtMap.put(TOTALOPENDEFECTS, totalDefectsCount - totalClosedDefectsCount);
			rtMap.put(NORMALCLOSEDDEFECTS, normalClosedDefectsCount);
			rtMap.put(BLOCKERCLOSEDDEFECTS, blockerClosedDefectsCount);
			rtMap.put(HIGHCLOSEDDEFECTS, highClosedDefectsCount);
			rtMap.put(LOWCLOSEDDEFECTS, lowClosedDefectsCount);
			rtMap.put(TOTALCLOSEDDEFECTS, totalClosedDefectsCount);

		} catch (Exception e) {
			LOG.info("Error in getDefectsDetailsMap() (QualityAnalysis Class)" + e);
		}

		return rtMap;
	}

	/**
	 * 
	 * @param appId
	 * @param client
	 * @return
	 */

	private List<ExecutiveMetricsSeries> getSeriesCountSNList(String appId, MongoClient client) {
		List<ExecutiveMetricsSeries> executiveMetricsSeriesList = new ArrayList<>();

		Long startDate = getDateTimeStamp(90);

		Boolean proceed = serviceNowDAO.isDataAvailableForSN(appId, startDate, client);

		if (proceed) {
			for (int i = 0; i < 90; i++) {
				ExecutiveMetricsSeries executiveMetricsSeries = new ExecutiveMetricsSeries();
				List<SeriesCount> seriesCountList = new ArrayList<>();
				SeriesCount seriesCount = new SeriesCount();
				SeriesCount seriesCountOpen = new SeriesCount();
				SeriesCount seriesCountClosed = new SeriesCount();
				SeriesCount seriesCountOne = new SeriesCount();
				SeriesCount seriesCountTwo = new SeriesCount();
				SeriesCount seriesCountThree = new SeriesCount();
				SeriesCount seriesCountFour = new SeriesCount();
				SeriesCount seriesCountOneOpen = new SeriesCount();
				SeriesCount seriesCountTwoOpen = new SeriesCount();
				SeriesCount seriesCountThreeOpen = new SeriesCount();
				SeriesCount seriesCountFourOpen = new SeriesCount();
				SeriesCount seriesCountOneClosed = new SeriesCount();
				SeriesCount seriesCountTwoClosed = new SeriesCount();
				SeriesCount seriesCountThreeClosed = new SeriesCount();
				SeriesCount seriesCountFourClosed = new SeriesCount();
				startDate = getDateTimeStamp(i);

				Map<String, String> labelSn = new HashMap<>();
				labelSn.put(PRIORITY, SN);
				Map<String, String> labelSnOne = new HashMap<>();
				labelSnOne.put(PRIORITY, SNONE);
				Map<String, String> labelSnTwo = new HashMap<>();
				labelSnTwo.put(PRIORITY, SNTWO);
				Map<String, String> labelSnThree = new HashMap<>();
				labelSnThree.put(PRIORITY, SNTHREE);
				Map<String, String> labelSnFour = new HashMap<>();
				labelSnFour.put(PRIORITY, SNFOUR);
				Map<String, String> closedLabel = new HashMap<>();
				closedLabel.put(PRIORITY, CLOSEDSN);
				Map<String, String> labelSnOneClosed = new HashMap<>();
				labelSnOneClosed.put(PRIORITY, SNONECLOSED);
				Map<String, String> labelSnTwoClosed = new HashMap<>();
				labelSnTwoClosed.put(PRIORITY, SNTWOCLOSED);
				Map<String, String> labelSnThreeClosed = new HashMap<>();
				labelSnThreeClosed.put(PRIORITY, SNTHREECLOSED);
				Map<String, String> labelSnFourClosed = new HashMap<>();
				labelSnFourClosed.put(PRIORITY, SNFOURCLOSED);
				Map<String, String> openLabel = new HashMap<>();
				openLabel.put(PRIORITY, OPENSN);
				Map<String, String> labelSnOneOpen = new HashMap<>();
				labelSnOneOpen.put(PRIORITY, SNONEOPEN);
				Map<String, String> labelSnTwoOpen = new HashMap<>();
				labelSnTwoOpen.put(PRIORITY, SNTWOOPEN);
				Map<String, String> labelSnThreeOpen = new HashMap<>();
				labelSnThreeOpen.put(PRIORITY, SNTHREEOPEN);
				Map<String, String> labelSnFourOpen = new HashMap<>();
				labelSnFourOpen.put(PRIORITY, SNFOUROPEN);

				List<ServiceNowIssues> snDefectsList = serviceNowDAO.getSNDefectsList(appId, startDate,
						getDateTimeStamp(i - 1), client);
				Map<String, Long> snDefectsMap = getSNDefectsMap(snDefectsList);

				seriesCount.setCount(snDefectsMap.get(SN));
				seriesCount.setLabel(labelSn);
				seriesCountOpen.setCount(snDefectsMap.get(OPENSN));
				seriesCountOpen.setLabel(openLabel);
				seriesCountClosed.setCount(snDefectsMap.get(CLOSEDSN));
				seriesCountClosed.setLabel(closedLabel);
				seriesCountOne.setCount(snDefectsMap.get(SNONE));
				seriesCountOne.setLabel(labelSnOne);
				seriesCountTwo.setCount(snDefectsMap.get(SNTWO));
				seriesCountTwo.setLabel(labelSnTwo);
				seriesCountThree.setCount(snDefectsMap.get(SNTHREE));
				seriesCountThree.setLabel(labelSnThree);
				seriesCountFour.setCount(snDefectsMap.get(SNFOUR));
				seriesCountFour.setLabel(labelSnFour);
				seriesCountOneOpen.setCount(snDefectsMap.get(SNONEOPEN));
				seriesCountOneOpen.setLabel(labelSnOneOpen);
				seriesCountTwoOpen.setCount(snDefectsMap.get(SNTWOOPEN));
				seriesCountTwoOpen.setLabel(labelSnTwoOpen);
				seriesCountThreeOpen.setCount(snDefectsMap.get(SNTHREEOPEN));
				seriesCountThreeOpen.setLabel(labelSnThreeOpen);
				seriesCountFourOpen.setCount(snDefectsMap.get(SNFOUROPEN));
				seriesCountFourOpen.setLabel(labelSnFourOpen);
				seriesCountOneClosed.setCount(snDefectsMap.get(SNONECLOSED));
				seriesCountOneClosed.setLabel(labelSnOneClosed);
				seriesCountTwoClosed.setCount(snDefectsMap.get(SNTWOCLOSED));
				seriesCountTwoClosed.setLabel(labelSnTwoClosed);
				seriesCountThreeClosed.setCount(snDefectsMap.get(SNTHREECLOSED));
				seriesCountThreeClosed.setLabel(labelSnThreeClosed);
				seriesCountFourClosed.setCount(snDefectsMap.get(SNFOURCLOSED));
				seriesCountFourClosed.setLabel(labelSnFourClosed);

				seriesCountList.add(seriesCount);
				seriesCountList.add(seriesCountOpen);
				seriesCountList.add(seriesCountClosed);
				seriesCountList.add(seriesCountOne);
				seriesCountList.add(seriesCountTwo);
				seriesCountList.add(seriesCountThree);
				seriesCountList.add(seriesCountFour);
				seriesCountList.add(seriesCountOneOpen);
				seriesCountList.add(seriesCountTwoOpen);
				seriesCountList.add(seriesCountThreeOpen);
				seriesCountList.add(seriesCountFourOpen);
				seriesCountList.add(seriesCountOneClosed);
				seriesCountList.add(seriesCountTwoClosed);
				seriesCountList.add(seriesCountThreeClosed);
				seriesCountList.add(seriesCountFourClosed);

				executiveMetricsSeries.setDaysAgo(i);
				executiveMetricsSeries.setTimeStamp(getTimeStamp(i));
				executiveMetricsSeries.setCounts(seriesCountList);
				executiveMetricsSeriesList.add(executiveMetricsSeries);

			}
		}

		return executiveMetricsSeriesList;
	}

	/**
	 * 
	 * @param snDefectsList
	 * @return
	 */

	private Map<String, Long> getSNDefectsMap(List<ServiceNowIssues> snDefectsList) {
		Map<String, Long> snDefectsMap = new HashMap<>();
		long snDefectsCount = 0;
		long snDefectsCountOpen = 0;
		long snDefectsCountClosed = 0;
		long snDefectsCountOne = 0;
		long snDefectsCountTwo = 0;
		long snDefectsCountThree = 0;
		long snDefectsCountFour = 0;
		long snDefectsCountOneOpen = 0;
		long snDefectsCountTwoOpen = 0;
		long snDefectsCountThreeOpen = 0;
		long snDefectsCountFourOpen = 0;
		long snDefectsCountOneClosed = 0;
		long snDefectsCountTwoClosed = 0;
		long snDefectsCountThreeClosed = 0;
		long snDefectsCountFourClosed = 0;
		try {
			if (snDefectsList != null && !snDefectsList.isEmpty()) {
				for (ServiceNowIssues snIssue : snDefectsList) {
					if (snIssue.getAysPriority() != null && snIssue.getActive() != null) {
						if (snIssue.getAysPriority() == 1) {
							snDefectsCountOne++;
							if (snIssue.getActive())
								snDefectsCountOneOpen++;
							else
								snDefectsCountOneClosed++;
						}
						if (snIssue.getAysPriority() == 2) {
							snDefectsCountTwo++;
							if (snIssue.getActive())
								snDefectsCountTwoOpen++;
							else
								snDefectsCountTwoClosed++;
						}
						if (snIssue.getAysPriority() == 3) {
							snDefectsCountThree++;
							if (snIssue.getActive())
								snDefectsCountThreeOpen++;
							else
								snDefectsCountThreeClosed++;
						}
						if (snIssue.getAysPriority() == 4) {
							snDefectsCountFour++;
							if (snIssue.getActive())
								snDefectsCountFourOpen++;
							else
								snDefectsCountFourClosed++;
						}
					}
				}
				snDefectsCount = snDefectsCountOne + snDefectsCountTwo + snDefectsCountThree + snDefectsCountFour;
				snDefectsCountOpen = snDefectsCountOneOpen + snDefectsCountTwoOpen + snDefectsCountThreeOpen
						+ snDefectsCountFourOpen;
				snDefectsCountClosed = snDefectsCountOneClosed + snDefectsCountTwoClosed + snDefectsCountThreeClosed
						+ snDefectsCountFourClosed;
			}
			snDefectsMap.put(SN, snDefectsCount);
			snDefectsMap.put(OPENSN, snDefectsCountOpen);
			snDefectsMap.put(CLOSEDSN, snDefectsCountClosed);
			snDefectsMap.put(SNONE, snDefectsCountOne);
			snDefectsMap.put(SNTWO, snDefectsCountTwo);
			snDefectsMap.put(SNTHREE, snDefectsCountThree);
			snDefectsMap.put(SNFOUR, snDefectsCountFour);
			snDefectsMap.put(SNONEOPEN, snDefectsCountOneOpen);
			snDefectsMap.put(SNTWOOPEN, snDefectsCountTwoOpen);
			snDefectsMap.put(SNTHREEOPEN, snDefectsCountThreeOpen);
			snDefectsMap.put(SNFOUROPEN, snDefectsCountFourOpen);
			snDefectsMap.put(SNONECLOSED, snDefectsCountOneClosed);
			snDefectsMap.put(SNTWOCLOSED, snDefectsCountTwoClosed);
			snDefectsMap.put(SNTHREECLOSED, snDefectsCountThreeClosed);
			snDefectsMap.put(SNFOURCLOSED, snDefectsCountFourClosed);

		} catch (Exception e) {
			LOG.info("Error in getSNDefectsMap() (QualityAnalysis Class) : " + e);
		}
		return snDefectsMap;
	}

	/**
	 * processMetricsDetailResponse
	 * 
	 * @return Boolean
	 */
	public Boolean processMetricsDetailResponse() {
		LOG.info("Processing Quality Details : app_metrics_details . . . . ");
		Query query = new Query(new Criteria().where(METRICSNAME).is(QUALITY));
		List<String> appIds = mongoTemplate.getCollection(EXECUTIVEMETRICS).distinct(APPID, query.getQueryObject());
		if (!appIds.isEmpty()) {
			for (String appId : appIds) {
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
						QUALITY);
				MetricsDetail metricDetailResponseProcessed = processMetricsDetail(executiveComponents);
				MetricsDetail metricDetailResponseStored = metricsDetailRepository.findByMetricLevelIdAndLevelAndType(appId,
						MetricLevel.PRODUCT, MetricType.QUALITY);
				if (metricDetailResponseStored != null) {
					metricsDetailRepository.delete(metricDetailResponseStored);
				}
				metricsDetailRepository.save(metricDetailResponseProcessed);
			}
		}
		LOG.info("Completed Quality Details : app_metrics_details . . . . ");
		return true;
	}

	/**
	 * processMetricsDetail
	 * 
	 * @param executiveComponents
	 * @return
	 */

	private MetricsDetail processMetricsDetail(ExecutiveComponents executiveComponents) {
		MetricsDetail metricDetailResponse = new MetricsDetail();
		metricDetailResponse.setMetricLevelId(executiveComponents.getAppId());
		metricDetailResponse.setLevel(MetricLevel.PRODUCT);
		metricDetailResponse.setType(MetricType.QUALITY);
		metricDetailResponse.setSummary(processMetricsSummary(executiveComponents));
		metricDetailResponse.setTimeSeries(processTimeSeries(executiveComponents));
		return metricDetailResponse;
	}

	/**
	 * processTimeSeries
	 * 
	 * @param executiveComponents
	 * @return List<MetricTimeSeriesElement>
	 */

	private List<MetricTimeSeriesElement> processTimeSeries(ExecutiveComponents executiveComponents) {
		List<MetricTimeSeriesElement> metricTimeSeriesElementResponseList = new ArrayList<>();
		List<ExecutiveModuleMetrics> modules = executiveComponents.getMetrics().get(0).getModules();
		Map<Integer, List<MetricCount>> seriesList = new HashMap<>();
		if (!modules.isEmpty()) {
			for (ExecutiveModuleMetrics executiveModuleMetrics : modules) {
				List<ExecutiveMetricsSeries> executiveMetricsSeriesList = executiveModuleMetrics.getSeries();
				if (!executiveMetricsSeriesList.isEmpty()) {
					for (ExecutiveMetricsSeries executiveMetricsSeries : executiveMetricsSeriesList) {
						int days = executiveMetricsSeries.getDaysAgo();
						long normalCount = 0;
						long blockerCount = 0;
						long highCount = 0;
						long lowCount = 0;
						long totalCount = 0;
						long cmisCount = 0;
						long snCount = 0;

						if (!seriesList.containsKey(days)) {
							List<SeriesCount> countList = executiveMetricsSeries.getCounts();
							List<MetricCount> metricCountResponseList = new ArrayList<>();

							for (SeriesCount count : countList) {
								if (count.getLabel().get(PRIORITY).equalsIgnoreCase(NORMAL))
									normalCount += count.getCount();
								if (count.getLabel().get(PRIORITY).equalsIgnoreCase(BLOCKER))
									blockerCount += count.getCount();
								if (count.getLabel().get(PRIORITY).equalsIgnoreCase(HIGH))
									highCount += count.getCount();
								if (count.getLabel().get(PRIORITY).equalsIgnoreCase(LOW))
									lowCount += count.getCount();
								if (count.getLabel().get(PRIORITY).equalsIgnoreCase(CMIS))
									cmisCount += count.getCount();
								if (count.getLabel().get(PRIORITY).equalsIgnoreCase(SN))
									snCount += count.getCount();
								if (count.getLabel().get(PRIORITY).equalsIgnoreCase(TOTAL))
									totalCount += count.getCount();
							}

							Map<String, String> normalDefects = new HashMap<>();
							normalDefects.put(PRIORITY, NORMAL);
							Map<String, String> blockerDefects = new HashMap<>();
							blockerDefects.put(PRIORITY, BLOCKER);
							Map<String, String> highDefects = new HashMap<>();
							highDefects.put(PRIORITY, HIGH);
							Map<String, String> lowDefects = new HashMap<>();
							lowDefects.put(PRIORITY, LOW);
							Map<String, String> totalDefects = new HashMap<>();
							totalDefects.put(PRIORITY, TOTAL);
							Map<String, String> cmisDefects = new HashMap<>();
							cmisDefects.put(PRIORITY, CMIS);
							Map<String, String> snDefects = new HashMap<>();
							snDefects.put(PRIORITY, SN);

							MetricCount metricCountResponseNormal = new MetricCount();
							metricCountResponseNormal.setLabel(normalDefects);
							metricCountResponseNormal.setValue(normalCount);
							metricCountResponseList.add(metricCountResponseNormal);
							MetricCount metricCountResponseBlocker = new MetricCount();
							metricCountResponseBlocker.setLabel(blockerDefects);
							metricCountResponseBlocker.setValue(blockerCount);
							metricCountResponseList.add(metricCountResponseBlocker);
							MetricCount metricCountResponseHigh = new MetricCount();
							metricCountResponseHigh.setLabel(highDefects);
							metricCountResponseHigh.setValue(highCount);
							metricCountResponseList.add(metricCountResponseHigh);
							MetricCount metricCountResponseLow = new MetricCount();
							metricCountResponseLow.setLabel(lowDefects);
							metricCountResponseLow.setValue(lowCount);
							metricCountResponseList.add(metricCountResponseLow);
							MetricCount metricCountResponseTotal = new MetricCount();
							metricCountResponseTotal.setLabel(totalDefects);
							metricCountResponseTotal.setValue(totalCount);
							metricCountResponseList.add(metricCountResponseTotal);
							MetricCount metricCountResponseCmis = new MetricCount();
							metricCountResponseCmis.setLabel(cmisDefects);
							metricCountResponseCmis.setValue(cmisCount);
							metricCountResponseList.add(metricCountResponseCmis);
							MetricCount metricCountResponseSN = new MetricCount();
							metricCountResponseSN.setLabel(snDefects);
							metricCountResponseSN.setValue(snCount);
							metricCountResponseList.add(metricCountResponseSN);

							seriesList.put(days, metricCountResponseList);
						} else {
							List<SeriesCount> countList = executiveMetricsSeries.getCounts();
							List<MetricCount> countsProcessed = new ArrayList<>();
							List<MetricCount> counts = seriesList.get(days);

							for (MetricCount metricCountResponse : counts) {
								if (metricCountResponse.getLabel().get(PRIORITY).equalsIgnoreCase(NORMAL))
									normalCount += metricCountResponse.getValue();
								if (metricCountResponse.getLabel().get(PRIORITY).equalsIgnoreCase(BLOCKER))
									blockerCount += metricCountResponse.getValue();
								if (metricCountResponse.getLabel().get(PRIORITY).equalsIgnoreCase(HIGH))
									highCount += metricCountResponse.getValue();
								if (metricCountResponse.getLabel().get(PRIORITY).equalsIgnoreCase(LOW))
									lowCount += metricCountResponse.getValue();
								if (metricCountResponse.getLabel().get(PRIORITY).equalsIgnoreCase(TOTAL))
									totalCount += metricCountResponse.getValue();
								if (metricCountResponse.getLabel().get(PRIORITY).equalsIgnoreCase(CMIS))
									cmisCount += metricCountResponse.getValue();
								if (metricCountResponse.getLabel().get(PRIORITY).equalsIgnoreCase(SN))
									snCount += metricCountResponse.getValue();
							}

							for (SeriesCount count : countList) {
								if (count.getLabel().get(PRIORITY).equalsIgnoreCase(NORMAL))
									normalCount += count.getCount();
								if (count.getLabel().get(PRIORITY).equalsIgnoreCase(BLOCKER))
									blockerCount += count.getCount();
								if (count.getLabel().get(PRIORITY).equalsIgnoreCase(HIGH))
									highCount += count.getCount();
								if (count.getLabel().get(PRIORITY).equalsIgnoreCase(LOW))
									lowCount += count.getCount();
								if (count.getLabel().get(PRIORITY).equalsIgnoreCase(TOTAL))
									totalCount += count.getCount();
								if (count.getLabel().get(PRIORITY).equalsIgnoreCase(CMIS))
									cmisCount += count.getCount();
								if (count.getLabel().get(PRIORITY).equalsIgnoreCase(SN))
									snCount += count.getCount();
							}

							Map<String, String> normalDefects = new HashMap<>();
							normalDefects.put(PRIORITY, NORMAL);
							Map<String, String> blockerDefects = new HashMap<>();
							blockerDefects.put(PRIORITY, BLOCKER);
							Map<String, String> highDefects = new HashMap<>();
							highDefects.put(PRIORITY, HIGH);
							Map<String, String> lowDefects = new HashMap<>();
							lowDefects.put(PRIORITY, LOW);
							Map<String, String> totalDefects = new HashMap<>();
							totalDefects.put(PRIORITY, TOTAL);
							Map<String, String> cmisDefects = new HashMap<>();
							cmisDefects.put(PRIORITY, CMIS);
							Map<String, String> snDefects = new HashMap<>();
							snDefects.put(PRIORITY, SN);

							MetricCount metricCountResponseNormal = new MetricCount();
							metricCountResponseNormal.setLabel(normalDefects);
							metricCountResponseNormal.setValue(normalCount);
							countsProcessed.add(metricCountResponseNormal);
							MetricCount metricCountResponseBlocker = new MetricCount();
							metricCountResponseBlocker.setLabel(blockerDefects);
							metricCountResponseBlocker.setValue(blockerCount);
							countsProcessed.add(metricCountResponseBlocker);
							MetricCount metricCountResponseHigh = new MetricCount();
							metricCountResponseHigh.setLabel(highDefects);
							metricCountResponseHigh.setValue(highCount);
							countsProcessed.add(metricCountResponseHigh);
							MetricCount metricCountResponseLow = new MetricCount();
							metricCountResponseLow.setLabel(lowDefects);
							metricCountResponseLow.setValue(lowCount);
							countsProcessed.add(metricCountResponseLow);
							MetricCount metricCountResponseTotal = new MetricCount();
							metricCountResponseTotal.setLabel(totalDefects);
							metricCountResponseTotal.setValue(totalCount);
							countsProcessed.add(metricCountResponseTotal);
							MetricCount metricCountResponseCmis = new MetricCount();
							metricCountResponseCmis.setLabel(cmisDefects);
							metricCountResponseCmis.setValue(cmisCount);
							countsProcessed.add(metricCountResponseCmis);
							MetricCount metricCountResponseSN = new MetricCount();
							metricCountResponseSN.setLabel(snDefects);
							metricCountResponseSN.setValue(snCount);
							countsProcessed.add(metricCountResponseSN);

							seriesList.replace(days, countsProcessed);
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

	/**
	 * processMetricsSummary
	 * 
	 * @param executiveComponents
	 * @return
	 */

	private MetricSummary processMetricsSummary(ExecutiveComponents executiveComponents) {
		MetricSummary metricSummaryResponse = new MetricSummary();
		List<ExecutiveModuleMetrics> modules = new ArrayList<>();
		List<String> teamIds = new ArrayList<>();
		List<String> appIds = new ArrayList<>();

		metricSummaryResponse.setLastScanned(executiveComponents.getMetrics().get(0).getLastScanned());
		metricSummaryResponse.setLastUpdated(executiveComponents.getMetrics().get(0).getLastUpdated());

		if (executiveComponents.getMetrics() != null) {
			List<ExecutiveMetrics> metrics = executiveComponents.getMetrics();
			appIds.add(executiveComponents.getAppId());
			for (ExecutiveMetrics metric : metrics) {
				if (metric.getMetricsName().equalsIgnoreCase(QUALITY)) {
					List<ExecutiveModuleMetrics> modulesList = metric.getModules();
					for (ExecutiveModuleMetrics executiveModuleMetrics : modulesList) {
						if (!teamIds.contains(executiveModuleMetrics.getModuleName())) {
							teamIds.add(executiveModuleMetrics.getModuleName());
							modules.add(executiveModuleMetrics);
						}
					}
					metricSummaryResponse.setCounts(processMetricSummaryCounts(modules, appIds));
					metricSummaryResponse.setReportingComponents(modules.size());
					metricSummaryResponse.setTotalComponents(modulesList.size());
					metricSummaryResponse.setTrendSlope(executiveComponents.getMetrics().get(0).getTrendSlope());
					metricSummaryResponse
							.setAppCriticality(genericMethods.processAppCriticality(executiveComponents.getAppId()));
					Boolean dataAvailabilityStatus = checkForDataAvailability(modules);
					metricSummaryResponse.setDataAvailable(dataAvailabilityStatus);
					if (!dataAvailabilityStatus)
						metricSummaryResponse.setConfMessage(MESSAGE);
				}
			}
		}
		metricSummaryResponse.setName(QUALITY);
		return metricSummaryResponse;
	}

	/**
	 * processMetricSummaryCounts
	 * 
	 * @param modules
	 * @param appIds
	 * @return
	 */

	private List<MetricCount> processMetricSummaryCounts(List<ExecutiveModuleMetrics> modules, List<String> appIds) {

		List<MetricCount> metricCountResponseList = new ArrayList<>();
		List<String> teamIds = new ArrayList<>();

		Long normalCount = (long) 0;
		Long blockerCount = (long) 0;
		Long highCount = (long) 0;
		Long lowCount = (long) 0;
		Long totalCount = (long) 0;
		Long cmisCount = (long) 0;
		Long snCount = (long) 0;
		Long snCountOne = (long) 0;
		Long snCountTwo = (long) 0;
		Long snCountThree = (long) 0;
		Long snCountFour = (long) 0;
		Long normalOpenCount = (long) 0;
		Long blockerOpenCount = (long) 0;
		Long highOpenCount = (long) 0;
		Long lowOpenCount = (long) 0;
		Long totalOpenCount = (long) 0;
		Long cmisOpenCount = (long) 0;
		Long snCountOpen = (long) 0;
		Long snCountOneOpen = (long) 0;
		Long snCountTwoOpen = (long) 0;
		Long snCountThreeOpen = (long) 0;
		Long snCountFourOpen = (long) 0;
		Long normalClosedCount = (long) 0;
		Long blockerClosedCount = (long) 0;
		Long highClosedCount = (long) 0;
		Long lowClosedCount = (long) 0;
		Long totalClosedCount = (long) 0;
		Long cmisClosedCount = (long) 0;
		Long snCountClosed = (long) 0;
		Long snCountOneClosed = (long) 0;
		Long snCountTwoClosed = (long) 0;
		Long snCountThreeClosed = (long) 0;
		Long snCountFourClosed = (long) 0;

		if (!modules.isEmpty()) {
			for (ExecutiveModuleMetrics moduleMetric : modules) {
				List<ExecutiveMetricsSeries> seriesList = moduleMetric.getSeries();
				if (!teamIds.contains(moduleMetric.getModuleName())) {
					if (!seriesList.isEmpty()) {
						for (ExecutiveMetricsSeries series : seriesList) {
							List<SeriesCount> countsList = series.getCounts();
							if (!countsList.isEmpty()) {
								for (SeriesCount count : countsList) {
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(NORMAL))
										normalCount += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(BLOCKER))
										blockerCount += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(HIGH))
										highCount += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(LOW))
										lowCount += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(CMIS))
										cmisCount += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(OPEN_NORMAL))
										normalOpenCount += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(OPEN_BLOCKER))
										blockerOpenCount += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(OPEN_HIGH))
										highOpenCount += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(OPEN_LOW))
										lowOpenCount += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(OPEN_CMIS))
										cmisOpenCount += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(CLOSED_NORMAL))
										normalClosedCount += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(CLOSED_BLOCKER))
										blockerClosedCount += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(CLOSED_HIGH))
										highClosedCount += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(CLOSED_LOW))
										lowClosedCount += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(CLOSED_CMIS))
										cmisClosedCount += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(SN))
										snCount += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(SNONE))
										snCountOne += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(SNTWO))
										snCountTwo += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(SNTHREE))
										snCountThree += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(SNFOUR))
										snCountFour += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(OPENSN))
										snCountOpen += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(SNONEOPEN))
										snCountOneOpen += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(SNTWOOPEN))
										snCountTwoOpen += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(SNTHREEOPEN))
										snCountThreeOpen += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(SNFOUROPEN))
										snCountFourOpen += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(CLOSEDSN))
										snCountClosed += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(SNONECLOSED))
										snCountOneClosed += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(SNTWOCLOSED))
										snCountTwoClosed += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(SNTHREECLOSED))
										snCountThreeClosed += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(SNFOURCLOSED))
										snCountFourClosed += count.getCount();
								}
								totalCount = normalCount + blockerCount + highCount + lowCount;
								totalOpenCount = normalOpenCount + blockerOpenCount + highOpenCount + lowOpenCount;
								totalClosedCount = normalClosedCount + blockerClosedCount + highClosedCount
										+ lowClosedCount;
							}
						}
					}
					teamIds.add(moduleMetric.getModuleName());
				}
			}
		}

		Map<String, String> normalDefects = new HashMap<>();
		normalDefects.put(PRIORITY, NORMAL);
		Map<String, String> blockerDefects = new HashMap<>();
		blockerDefects.put(PRIORITY, BLOCKER);
		Map<String, String> highDefects = new HashMap<>();
		highDefects.put(PRIORITY, HIGH);
		Map<String, String> lowDefects = new HashMap<>();
		lowDefects.put(PRIORITY, LOW);
		Map<String, String> totalDefects = new HashMap<>();
		totalDefects.put(PRIORITY, TOTAL);
		Map<String, String> cmisDefects = new HashMap<>();
		cmisDefects.put(PRIORITY, CMIS);
		Map<String, String> normalOpenDefects = new HashMap<>();
		normalOpenDefects.put(PRIORITY, OPEN_NORMAL);
		Map<String, String> blockerOpenDefects = new HashMap<>();
		blockerOpenDefects.put(PRIORITY, OPEN_BLOCKER);
		Map<String, String> highOpenDefects = new HashMap<>();
		highOpenDefects.put(PRIORITY, OPEN_HIGH);
		Map<String, String> lowOpenDefects = new HashMap<>();
		lowOpenDefects.put(PRIORITY, OPEN_LOW);
		Map<String, String> totalOpenDefects = new HashMap<>();
		totalOpenDefects.put(PRIORITY, OPEN_TOTAL);
		Map<String, String> cmisOpenDefects = new HashMap<>();
		cmisOpenDefects.put(PRIORITY, OPEN_CMIS);
		Map<String, String> normalClosedDefects = new HashMap<>();
		normalClosedDefects.put(PRIORITY, CLOSED_NORMAL);
		Map<String, String> blockerClosedDefects = new HashMap<>();
		blockerClosedDefects.put(PRIORITY, CLOSED_BLOCKER);
		Map<String, String> highClosedDefects = new HashMap<>();
		highClosedDefects.put(PRIORITY, CLOSED_HIGH);
		Map<String, String> lowClosedDefects = new HashMap<>();
		lowClosedDefects.put(PRIORITY, CLOSED_LOW);
		Map<String, String> totalClosedDefects = new HashMap<>();
		totalClosedDefects.put(PRIORITY, CLOSED_TOTAL);
		Map<String, String> cmisClosedDefects = new HashMap<>();
		cmisClosedDefects.put(PRIORITY, CLOSED_CMIS);
		Map<String, String> changeFailureRate = new HashMap<>();
		changeFailureRate.put(TYPE, CHANGEFAILURERATE);
		Map<String, String> labelSn = new HashMap<>();
		labelSn.put(PRIORITY, SN);
		Map<String, String> labelSnOne = new HashMap<>();
		labelSnOne.put(PRIORITY, SNONE);
		Map<String, String> labelSnTwo = new HashMap<>();
		labelSnTwo.put(PRIORITY, SNTWO);
		Map<String, String> labelSnThree = new HashMap<>();
		labelSnThree.put(PRIORITY, SNTHREE);
		Map<String, String> labelSnFour = new HashMap<>();
		labelSnFour.put(PRIORITY, SNFOUR);
		Map<String, String> closedLabel = new HashMap<>();
		closedLabel.put(PRIORITY, CLOSEDSN);
		Map<String, String> labelSnOneClosed = new HashMap<>();
		labelSnOneClosed.put(PRIORITY, SNONECLOSED);
		Map<String, String> labelSnTwoClosed = new HashMap<>();
		labelSnTwoClosed.put(PRIORITY, SNTWOCLOSED);
		Map<String, String> labelSnThreeClosed = new HashMap<>();
		labelSnThreeClosed.put(PRIORITY, SNTHREECLOSED);
		Map<String, String> labelSnFourClosed = new HashMap<>();
		labelSnFourClosed.put(PRIORITY, SNFOURCLOSED);
		Map<String, String> openLabel = new HashMap<>();
		openLabel.put(PRIORITY, OPENSN);
		Map<String, String> labelSnOneOpen = new HashMap<>();
		labelSnOneOpen.put(PRIORITY, SNONEOPEN);
		Map<String, String> labelSnTwoOpen = new HashMap<>();
		labelSnTwoOpen.put(PRIORITY, SNTWOOPEN);
		Map<String, String> labelSnThreeOpen = new HashMap<>();
		labelSnThreeOpen.put(PRIORITY, SNTHREEOPEN);
		Map<String, String> labelSnFourOpen = new HashMap<>();
		labelSnFourOpen.put(PRIORITY, SNFOUROPEN);

		MetricCount metricCountResponseNormal = new MetricCount();
		metricCountResponseNormal.setLabel(normalDefects);
		metricCountResponseNormal.setValue(normalCount);
		metricCountResponseList.add(metricCountResponseNormal);
		MetricCount metricCountResponseBlocker = new MetricCount();
		metricCountResponseBlocker.setLabel(blockerDefects);
		metricCountResponseBlocker.setValue(blockerCount);
		metricCountResponseList.add(metricCountResponseBlocker);
		MetricCount metricCountResponseHigh = new MetricCount();
		metricCountResponseHigh.setLabel(highDefects);
		metricCountResponseHigh.setValue(highCount);
		metricCountResponseList.add(metricCountResponseHigh);
		MetricCount metricCountResponseLow = new MetricCount();
		metricCountResponseLow.setLabel(lowDefects);
		metricCountResponseLow.setValue(lowCount);
		metricCountResponseList.add(metricCountResponseLow);
		MetricCount metricCountResponseTotal = new MetricCount();
		metricCountResponseTotal.setLabel(totalDefects);
		metricCountResponseTotal.setValue(totalCount);
		metricCountResponseList.add(metricCountResponseTotal);
		MetricCount metricCountResponseCMIS = new MetricCount();
		metricCountResponseCMIS.setLabel(cmisDefects);
		metricCountResponseCMIS.setValue(cmisCount);
		metricCountResponseList.add(metricCountResponseCMIS);
		MetricCount metricCountOpenResponseNormal = new MetricCount();
		metricCountOpenResponseNormal.setLabel(normalOpenDefects);
		metricCountOpenResponseNormal.setValue(normalOpenCount);
		metricCountResponseList.add(metricCountOpenResponseNormal);
		MetricCount metricCountOpenResponseBlocker = new MetricCount();
		metricCountOpenResponseBlocker.setLabel(blockerOpenDefects);
		metricCountOpenResponseBlocker.setValue(blockerOpenCount);
		metricCountResponseList.add(metricCountOpenResponseBlocker);
		MetricCount metricCountOpenResponseHigh = new MetricCount();
		metricCountOpenResponseHigh.setLabel(highOpenDefects);
		metricCountOpenResponseHigh.setValue(highOpenCount);
		metricCountResponseList.add(metricCountOpenResponseHigh);
		MetricCount metricCountOpenResponseLow = new MetricCount();
		metricCountOpenResponseLow.setLabel(lowOpenDefects);
		metricCountOpenResponseLow.setValue(lowOpenCount);
		metricCountResponseList.add(metricCountOpenResponseLow);
		MetricCount metricCountOpenResponseTotal = new MetricCount();
		metricCountOpenResponseTotal.setLabel(totalOpenDefects);
		metricCountOpenResponseTotal.setValue(totalOpenCount);
		metricCountResponseList.add(metricCountOpenResponseTotal);
		MetricCount metricCountOpenResponseCMIS = new MetricCount();
		metricCountOpenResponseCMIS.setLabel(cmisOpenDefects);
		metricCountOpenResponseCMIS.setValue(cmisOpenCount);
		metricCountResponseList.add(metricCountOpenResponseCMIS);
		MetricCount metricCountClosedResponseNormal = new MetricCount();
		metricCountClosedResponseNormal.setLabel(normalClosedDefects);
		metricCountClosedResponseNormal.setValue(normalClosedCount);
		metricCountResponseList.add(metricCountClosedResponseNormal);
		MetricCount metricCountClosedResponseBlocker = new MetricCount();
		metricCountClosedResponseBlocker.setLabel(blockerClosedDefects);
		metricCountClosedResponseBlocker.setValue(blockerClosedCount);
		metricCountResponseList.add(metricCountClosedResponseBlocker);
		MetricCount metricCountClosedResponseHigh = new MetricCount();
		metricCountClosedResponseHigh.setLabel(highClosedDefects);
		metricCountClosedResponseHigh.setValue(highClosedCount);
		metricCountResponseList.add(metricCountClosedResponseHigh);
		MetricCount metricCountClosedResponseLow = new MetricCount();
		metricCountClosedResponseLow.setLabel(lowClosedDefects);
		metricCountClosedResponseLow.setValue(lowClosedCount);
		metricCountResponseList.add(metricCountClosedResponseLow);
		MetricCount metricCountClosedResponseTotal = new MetricCount();
		metricCountClosedResponseTotal.setLabel(totalClosedDefects);
		metricCountClosedResponseTotal.setValue(totalClosedCount);
		metricCountResponseList.add(metricCountClosedResponseTotal);
		MetricCount metricCountClosedResponseCMIS = new MetricCount();
		metricCountClosedResponseCMIS.setLabel(cmisClosedDefects);
		metricCountClosedResponseCMIS.setValue(cmisClosedCount);
		metricCountResponseList.add(metricCountClosedResponseCMIS);
		MetricCount metricCountResponseChangeFailureRate = new MetricCount();
		metricCountResponseChangeFailureRate.setLabel(changeFailureRate);
		metricCountResponseChangeFailureRate.setValue(getFailureRate(appIds, totalCount + cmisCount));
		metricCountResponseList.add(metricCountResponseChangeFailureRate);
		MetricCount metricCountResponseSN = new MetricCount();
		metricCountResponseSN.setLabel(labelSn);
		metricCountResponseSN.setValue(snCount);
		metricCountResponseList.add(metricCountResponseSN);
		MetricCount metricCountResponseSNOne = new MetricCount();
		metricCountResponseSNOne.setLabel(labelSnOne);
		metricCountResponseSNOne.setValue(snCountOne);
		metricCountResponseList.add(metricCountResponseSNOne);
		MetricCount metricCountResponseSNTwo = new MetricCount();
		metricCountResponseSNTwo.setLabel(labelSnTwo);
		metricCountResponseSNTwo.setValue(snCountTwo);
		metricCountResponseList.add(metricCountResponseSNTwo);
		MetricCount metricCountResponseSNThree = new MetricCount();
		metricCountResponseSNThree.setLabel(labelSnThree);
		metricCountResponseSNThree.setValue(snCountThree);
		metricCountResponseList.add(metricCountResponseSNThree);
		MetricCount metricCountResponseSNFour = new MetricCount();
		metricCountResponseSNFour.setLabel(labelSnFour);
		metricCountResponseSNFour.setValue(snCountFour);
		metricCountResponseList.add(metricCountResponseSNFour);
		MetricCount metricCountResponseSNOpen = new MetricCount();
		metricCountResponseSNOpen.setLabel(openLabel);
		metricCountResponseSNOpen.setValue(snCountOpen);
		metricCountResponseList.add(metricCountResponseSNOpen);
		MetricCount metricCountResponseSNOneOpen = new MetricCount();
		metricCountResponseSNOneOpen.setLabel(labelSnOneOpen);
		metricCountResponseSNOneOpen.setValue(snCountOneOpen);
		metricCountResponseList.add(metricCountResponseSNOneOpen);
		MetricCount metricCountResponseSNTwoOpen = new MetricCount();
		metricCountResponseSNTwoOpen.setLabel(labelSnTwoOpen);
		metricCountResponseSNTwoOpen.setValue(snCountTwoOpen);
		metricCountResponseList.add(metricCountResponseSNTwoOpen);
		MetricCount metricCountResponseSNThreeOpen = new MetricCount();
		metricCountResponseSNThreeOpen.setLabel(labelSnThreeOpen);
		metricCountResponseSNThreeOpen.setValue(snCountThreeOpen);
		metricCountResponseList.add(metricCountResponseSNThreeOpen);
		MetricCount metricCountResponseSNFourOpen = new MetricCount();
		metricCountResponseSNFourOpen.setLabel(labelSnFourOpen);
		metricCountResponseSNFourOpen.setValue(snCountFourOpen);
		metricCountResponseList.add(metricCountResponseSNFourOpen);
		MetricCount metricCountResponseSNClosed = new MetricCount();
		metricCountResponseSNClosed.setLabel(closedLabel);
		metricCountResponseSNClosed.setValue(snCountClosed);
		metricCountResponseList.add(metricCountResponseSNClosed);
		MetricCount metricCountResponseSNOneClosed = new MetricCount();
		metricCountResponseSNOneClosed.setLabel(labelSnOneClosed);
		metricCountResponseSNOneClosed.setValue(snCountOneClosed);
		metricCountResponseList.add(metricCountResponseSNOneClosed);
		MetricCount metricCountResponseSNTwoClosed = new MetricCount();
		metricCountResponseSNTwoClosed.setLabel(labelSnTwoClosed);
		metricCountResponseSNTwoClosed.setValue(snCountTwoClosed);
		metricCountResponseList.add(metricCountResponseSNTwoClosed);
		MetricCount metricCountResponseSNThreeClosed = new MetricCount();
		metricCountResponseSNThreeClosed.setLabel(labelSnThreeClosed);
		metricCountResponseSNThreeClosed.setValue(snCountThreeClosed);
		metricCountResponseList.add(metricCountResponseSNThreeClosed);
		MetricCount metricCountResponseSNFourClosed = new MetricCount();
		metricCountResponseSNFourClosed.setLabel(labelSnFourClosed);
		metricCountResponseSNFourClosed.setValue(snCountFourClosed);
		metricCountResponseList.add(metricCountResponseSNFourClosed);

		return metricCountResponseList;

	}

	/**
	 * processBuildingBlockMetrics
	 * 
	 * @return Boolean
	 */
	public Boolean processBuildingBlockMetrics() {
		LOG.info("PRocessing Quality Details : building_block_metrics . . . . ");
		try {
			Query query = new Query(new Criteria().where(METRICSNAME).is(QUALITY));
			List<String> appIds = mongoTemplate.getCollection(EXECUTIVEMETRICS).distinct(APPID, query.getQueryObject());
			if (!appIds.isEmpty()) {
				for (String appId : appIds) {
					BuildingBlocks buildingBlockMetricSummary = buildingBlocksRepository.findByMetricLevelIdAndMetricLevel(appId,
							MetricLevel.PRODUCT);
					if (buildingBlockMetricSummary == null)
						buildingBlockMetricSummary = new BuildingBlocks();

					ApplicationDetails appDetails = applicationDetailsRepository.findByAppId(appId);
					MetricsDetail metricDetailResponse = metricsDetailRepository.findByMetricLevelIdAndLevelAndType(appId,
							MetricLevel.PRODUCT, MetricType.QUALITY);
					if (metricDetailResponse != null && appDetails != null) {
						buildingBlockMetricSummary.setMetricLevelId(appId);
						buildingBlockMetricSummary.setLob(appDetails.getLob());
						buildingBlockMetricSummary.setName(appDetails.getAppName());
						buildingBlockMetricSummary
								.setAppCriticality(metricDetailResponse.getSummary().getAppCriticality());
						List<MetricSummary> metricsResponseStored = buildingBlockMetricSummary.getMetrics();
						List<MetricSummary> metricsResponseProcessed = new ArrayList<>();
						if (metricsResponseStored != null && !metricsResponseStored.isEmpty()) {
							for (MetricSummary metricSummaryResponse : metricsResponseStored) {
								if (!metricSummaryResponse.getName().equalsIgnoreCase(QUALITY))
									metricsResponseProcessed.add(metricSummaryResponse);
							}
						}
						metricsResponseProcessed.add(metricDetailResponse.getSummary());
						buildingBlockMetricSummary.setMetricLevel(MetricLevel.PRODUCT);
						buildingBlockMetricSummary.setMetrics(metricsResponseProcessed);
						buildingBlocksRepository.save(buildingBlockMetricSummary);
					}
				}
			}
			LOG.info("Completed Quality Details : building_block_metrics . . . . ");
		} catch (Exception e) {
			LOG.info("processBuildingBlockMetrics Quality Analysis Info :: " + e);
		}
		return true;
	}

	/**
	 * processExecutiveDetailsMetrics
	 * 
	 * @return Boolean
	 */
	public Boolean processExecutiveDetailsMetrics() {

		LOG.info("Processing Quality Details : portfolio_metrics_details . . . . ");
		List<ExecutiveSummaryList> executiveSummaryLists = (List<ExecutiveSummaryList>) executiveSummaryListRepository
				.findAll();
		MongoClient client = null;
		try {
			if (!executiveSummaryLists.isEmpty()) {
				client = qualityDetailsDAO.getMongoClient();
				for (ExecutiveSummaryList executiveSummaryList : executiveSummaryLists) {
					String eid = executiveSummaryList.getEid();
					MetricsDetail metricPortfolioDetailResponse = metricsDetailRepository.findByMetricLevelIdAndLevelAndType(eid,
							MetricLevel.PORTFOLIO, MetricType.QUALITY);
					if (metricPortfolioDetailResponse == null)
						metricPortfolioDetailResponse = new MetricsDetail();
					metricPortfolioDetailResponse.setType(MetricType.QUALITY);
					metricPortfolioDetailResponse.setLevel(MetricLevel.PORTFOLIO);
					metricPortfolioDetailResponse.setMetricLevelId(eid);
					metricPortfolioDetailResponse.setCustomField(getPortfolioId(eid));
					metricPortfolioDetailResponse.setSummary(processExecutiveSummary(executiveSummaryList.getAppId()));
					metricPortfolioDetailResponse
							.setTimeSeries(processExecutiveTimeSeries(executiveSummaryList.getAppId()));
					if (metricPortfolioDetailResponse.getSummary() != null)
						metricPortfolioDetailResponse.getSummary()
								.setTotalComponents(executiveSummaryList.getTotalApps());
					metricsDetailRepository.save(metricPortfolioDetailResponse);
				}
			}
		} catch (Exception e) {
			LOG.info("Error inside processExecutiveDetailsMetrics " + e);
		} finally {
			if (client != null)
				client.close();
		}
		LOG.info("Completed Quality Details : portfolio_metrics_details . . . . ");
		return true;
	}

	/**
	 * processExecutiveTimeSeries
	 * 
	 * @param configuredAppId
	 * @return
	 */

	private List<MetricTimeSeriesElement> processExecutiveTimeSeries(List<String> configuredAppId) {
		if (configuredAppId != null && !configuredAppId.isEmpty()) {
			List<MetricTimeSeriesElement> metricTimeSeriesElementResponseList = new ArrayList<>();
			List<ExecutiveModuleMetrics> modules = new ArrayList<>();
			List<String> teamIds = new ArrayList<String>();
			for (String appId : configuredAppId) {
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
						QUALITY);
				if (executiveComponents != null) {
					List<ExecutiveMetrics> executiveMetricsList = executiveComponents.getMetrics();
					if (executiveMetricsList != null) {
						for (ExecutiveMetrics metric : executiveMetricsList) {
							if (metric.getMetricsName().equalsIgnoreCase(QUALITY)) {
								List<ExecutiveModuleMetrics> modulesList = metric.getModules();
								for (ExecutiveModuleMetrics executiveModuleMetrics : modulesList) {
									if (!teamIds.contains(executiveModuleMetrics.getModuleName())) {
										modules.add(executiveModuleMetrics);
										teamIds.add(executiveModuleMetrics.getModuleName());
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
							long normalCount = 0;
							long blockerCount = 0;
							long highCount = 0;
							long lowCount = 0;
							long totalCount = 0;
							long cmisCount = 0;
							long snCount = 0;

							if (!seriesList.containsKey(days)) {
								List<SeriesCount> countList = executiveMetricsSeries.getCounts();
								List<MetricCount> metricCountResponseList = new ArrayList<>();

								for (SeriesCount count : countList) {
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(NORMAL))
										normalCount += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(BLOCKER))
										blockerCount += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(HIGH))
										highCount += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(LOW))
										lowCount += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(CMIS))
										cmisCount += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(TOTAL))
										totalCount += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(SN))
										snCount += count.getCount();
								}

								Map<String, String> normalDefects = new HashMap<>();
								normalDefects.put(PRIORITY, NORMAL);
								Map<String, String> blockerDefects = new HashMap<>();
								blockerDefects.put(PRIORITY, BLOCKER);
								Map<String, String> highDefects = new HashMap<>();
								highDefects.put(PRIORITY, HIGH);
								Map<String, String> lowDefects = new HashMap<>();
								lowDefects.put(PRIORITY, LOW);
								Map<String, String> totalDefects = new HashMap<>();
								totalDefects.put(PRIORITY, TOTAL);
								Map<String, String> cmisDefects = new HashMap<>();
								cmisDefects.put(PRIORITY, CMIS);
								Map<String, String> snDefects = new HashMap<>();
								snDefects.put(PRIORITY, SN);

								MetricCount metricCountResponseNormal = new MetricCount();
								metricCountResponseNormal.setLabel(normalDefects);
								metricCountResponseNormal.setValue(normalCount);
								metricCountResponseList.add(metricCountResponseNormal);
								MetricCount metricCountResponseBlocker = new MetricCount();
								metricCountResponseBlocker.setLabel(blockerDefects);
								metricCountResponseBlocker.setValue(blockerCount);
								metricCountResponseList.add(metricCountResponseBlocker);
								MetricCount metricCountResponseHigh = new MetricCount();
								metricCountResponseHigh.setLabel(highDefects);
								metricCountResponseHigh.setValue(highCount);
								metricCountResponseList.add(metricCountResponseHigh);
								MetricCount metricCountResponseLow = new MetricCount();
								metricCountResponseLow.setLabel(lowDefects);
								metricCountResponseLow.setValue(lowCount);
								metricCountResponseList.add(metricCountResponseLow);
								MetricCount metricCountResponseTotal = new MetricCount();
								metricCountResponseTotal.setLabel(totalDefects);
								metricCountResponseTotal.setValue(totalCount);
								metricCountResponseList.add(metricCountResponseTotal);
								MetricCount metricCountResponseCmis = new MetricCount();
								metricCountResponseCmis.setLabel(cmisDefects);
								metricCountResponseCmis.setValue(cmisCount);
								metricCountResponseList.add(metricCountResponseCmis);
								MetricCount metricCountResponseSN = new MetricCount();
								metricCountResponseSN.setLabel(snDefects);
								metricCountResponseSN.setValue(snCount);
								metricCountResponseList.add(metricCountResponseSN);

								seriesList.put(days, metricCountResponseList);
							} else {
								List<SeriesCount> countList = executiveMetricsSeries.getCounts();
								List<MetricCount> countsProcessed = new ArrayList<>();
								List<MetricCount> counts = seriesList.get(days);

								for (MetricCount metricCountResponse : counts) {
									if (metricCountResponse.getLabel().get(PRIORITY).equalsIgnoreCase(NORMAL))
										normalCount += metricCountResponse.getValue();
									if (metricCountResponse.getLabel().get(PRIORITY).equalsIgnoreCase(BLOCKER))
										blockerCount += metricCountResponse.getValue();
									if (metricCountResponse.getLabel().get(PRIORITY).equalsIgnoreCase(HIGH))
										highCount += metricCountResponse.getValue();
									if (metricCountResponse.getLabel().get(PRIORITY).equalsIgnoreCase(LOW))
										lowCount += metricCountResponse.getValue();
									if (metricCountResponse.getLabel().get(PRIORITY).equalsIgnoreCase(TOTAL))
										totalCount += metricCountResponse.getValue();
									if (metricCountResponse.getLabel().get(PRIORITY).equalsIgnoreCase(CMIS))
										cmisCount += metricCountResponse.getValue();
									if (metricCountResponse.getLabel().get(PRIORITY).equalsIgnoreCase(SN))
										snCount += metricCountResponse.getValue();
								}

								for (SeriesCount count : countList) {
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(NORMAL))
										normalCount += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(BLOCKER))
										blockerCount += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(HIGH))
										highCount += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(LOW))
										lowCount += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(TOTAL))
										totalCount += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(CMIS))
										cmisCount += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(SN))
										snCount += count.getCount();
								}

								Map<String, String> normalDefects = new HashMap<>();
								normalDefects.put(PRIORITY, NORMAL);
								Map<String, String> blockerDefects = new HashMap<>();
								blockerDefects.put(PRIORITY, BLOCKER);
								Map<String, String> highDefects = new HashMap<>();
								highDefects.put(PRIORITY, HIGH);
								Map<String, String> lowDefects = new HashMap<>();
								lowDefects.put(PRIORITY, LOW);
								Map<String, String> totalDefects = new HashMap<>();
								totalDefects.put(PRIORITY, TOTAL);
								Map<String, String> cmisDefects = new HashMap<>();
								cmisDefects.put(PRIORITY, CMIS);
								Map<String, String> snDefects = new HashMap<>();
								snDefects.put(PRIORITY, SN);

								MetricCount metricCountResponseNormal = new MetricCount();
								metricCountResponseNormal.setLabel(normalDefects);
								metricCountResponseNormal.setValue(normalCount);
								countsProcessed.add(metricCountResponseNormal);
								MetricCount metricCountResponseBlocker = new MetricCount();
								metricCountResponseBlocker.setLabel(blockerDefects);
								metricCountResponseBlocker.setValue(blockerCount);
								countsProcessed.add(metricCountResponseBlocker);
								MetricCount metricCountResponseHigh = new MetricCount();
								metricCountResponseHigh.setLabel(highDefects);
								metricCountResponseHigh.setValue(highCount);
								countsProcessed.add(metricCountResponseHigh);
								MetricCount metricCountResponseLow = new MetricCount();
								metricCountResponseLow.setLabel(lowDefects);
								metricCountResponseLow.setValue(lowCount);
								countsProcessed.add(metricCountResponseLow);
								MetricCount metricCountResponseTotal = new MetricCount();
								metricCountResponseTotal.setLabel(totalDefects);
								metricCountResponseTotal.setValue(totalCount);
								countsProcessed.add(metricCountResponseTotal);
								MetricCount metricCountResponseCmis = new MetricCount();
								metricCountResponseCmis.setLabel(cmisDefects);
								metricCountResponseCmis.setValue(cmisCount);
								countsProcessed.add(metricCountResponseCmis);
								MetricCount metricCountResponseSN = new MetricCount();
								metricCountResponseSN.setLabel(snDefects);
								metricCountResponseSN.setValue(snCount);
								countsProcessed.add(metricCountResponseSN);

								seriesList.replace(days, countsProcessed);
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

	/**
	 * processExecutiveSummary
	 * 
	 * @param configuredAppId
	 * @return
	 */

	private MetricSummary processExecutiveSummary(List<String> configuredAppId) {
		List<ExecutiveModuleMetrics> modules = new ArrayList<>();
		MetricSummary metricSummaryResponse = new MetricSummary();
		int reportingComponents = 0;
		if (configuredAppId != null && !configuredAppId.isEmpty()) {

			for (String appId : configuredAppId) {
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
						QUALITY);
				List<ExecutiveModuleMetrics> appLevelModules = new ArrayList<>();
				if (executiveComponents != null) {
					List<ExecutiveMetrics> executiveMetricsList = executiveComponents.getMetrics();
					if (executiveMetricsList != null) {
						for (ExecutiveMetrics metric : executiveMetricsList) {
							if (metric.getMetricsName().equalsIgnoreCase(QUALITY)) {
								List<ExecutiveModuleMetrics> modulesList = metric.getModules();
								for (ExecutiveModuleMetrics executiveModuleMetrics : modulesList) {
									appLevelModules.add(executiveModuleMetrics);
								}
							}
						}
						if (!appLevelModules.isEmpty()) {
							if (checkForDataAvailability(appLevelModules)) {
								reportingComponents++;
							}
							modules.addAll(appLevelModules);
						}

					}
				}
			}

			if (collectorStatusRepository.findByType(CollectorType.JiraUserStory) != null)
				metricSummaryResponse.setLastScanned(
						collectorStatusRepository.findByType(CollectorType.JiraUserStory).getLastUpdated());
			metricSummaryResponse.setLastUpdated(new Date(System.currentTimeMillis()));
			metricSummaryResponse.setCounts(processMetricSummaryCounts(modules, configuredAppId));
			metricSummaryResponse.setTrendSlope(getTrendSlopesForModules(modules));
			metricSummaryResponse.setName(QUALITY);
			metricSummaryResponse.setDataAvailable(checkForDataAvailability(modules));
			metricSummaryResponse.setReportingComponents(reportingComponents);
			return metricSummaryResponse;
		}
		return metricSummaryResponse;
	}

	/**
	 * processComponentDetailsMetrics
	 * 
	 * @return Boolean
	 */
	public Boolean processComponentDetailsMetrics() {
		LOG.info("Processing Quality Details : building_block_components . . . . ");
		Query query = new Query(new Criteria().where(METRICSNAME).is(QUALITY));
		List<String> appIds = mongoTemplate.getCollection(EXECUTIVEMETRICS).distinct(APPID, query.getQueryObject());
		if (!appIds.isEmpty()) {
			for (String appId : appIds) {
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
						QUALITY);
				List<BuildingBlocks> response = buildingBlocksRepository.findByMetricLevelIdAndMetricLevelAndMetricType(appId,
						MetricLevel.COMPONENT, MetricType.QUALITY);
				List<BuildingBlocks> buildingBlockResponse = new ArrayList<>();

				List<ExecutiveModuleMetrics> modules = executiveComponents.getMetrics().get(0).getModules();
				ApplicationDetails appDetails = applicationDetailsRepository.findByAppId(appId);

				if (!modules.isEmpty() && appDetails != null) {
					for (ExecutiveModuleMetrics module : modules) {
						BuildingBlocks summaryResponse = new BuildingBlocks();
						summaryResponse.setMetricLevelId(appId);
						summaryResponse.setLob(appDetails.getLob());
						summaryResponse.setMetrics(processComponentMetrics(module, appId));
						summaryResponse.setName(module.getModuleName());
						String projectKey = module.getTeamId();
						if (projectKey != null) {
							summaryResponse.setCustomField(projectKey);
							summaryResponse.setUrl(getJiraProjectLink(projectKey));
						}
						summaryResponse.setPoc(appDetails.getPoc());
						summaryResponse.setTotalComponents(1);
						summaryResponse.setTotalExpectedMetrics(1);
						summaryResponse.setMetricType(MetricType.QUALITY);
						summaryResponse.setMetricLevel(MetricLevel.COMPONENT);
						buildingBlockResponse.add(summaryResponse);
					}
				}

				if (response != null)
					buildingBlocksRepository.delete(response);
				buildingBlocksRepository.save(buildingBlockResponse);

			}
		}
		LOG.info("Completed Quality Details : building_block_components . . . . ");
		return true;
	}

	private String getJiraProjectLink(String projectKey) {
		if (projectKey != null)
			return metricsSettings.getJiraBaseUrl() + projectKey + metricsSettings.getQualityLink();
		return null;
	}

	/**
	 * processComponentMetrics
	 * 
	 * @param module
	 * @param appId
	 * @return
	 */

	private List<MetricSummary> processComponentMetrics(ExecutiveModuleMetrics module, String appId) {
		List<MetricSummary> metricSummaryResponseList = new ArrayList<>();
		List<ExecutiveModuleMetrics> modules = new ArrayList<>();
		List<String> appIdList = new ArrayList<>();
		MetricSummary metricSummaryResponse = new MetricSummary();
		metricSummaryResponse.setName(QUALITY);
		metricSummaryResponse.setTotalComponents(1);
		metricSummaryResponse.setReportingComponents(1);
		metricSummaryResponse.setLastScanned(module.getLastScanned());
		metricSummaryResponse.setLastUpdated(module.getLastUpdated());
		modules.add(module);
		appIdList.add(appId);
		metricSummaryResponse.setCounts(processMetricSummaryCounts(modules, appIdList));
		Boolean dataAvailabilityStatus = checkForDataAvailability(modules);
		metricSummaryResponse.setDataAvailable(dataAvailabilityStatus);
		if (!dataAvailabilityStatus)
			metricSummaryResponse.setConfMessage(MESSAGE);
		metricSummaryResponseList.add(metricSummaryResponse);
		return metricSummaryResponseList;
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
	 * getPortfolioId
	 * 
	 * @param eid
	 * @return
	 */
	private String getPortfolioId(String eid) {
		PortfolioResponse portfolioResponse = portfolioResponseRepository.findByEid(eid);
		return portfolioResponse == null ? null : portfolioResponse.getId().toString();
	}

	/**
	 * checkForDataAvailability
	 * 
	 * @param modules
	 * @return
	 */

	private Boolean checkForDataAvailability(List<ExecutiveModuleMetrics> modules) {
		int cmisCount = 0;
		int snCount = 0;
		int normalCount = 0;
		int blockerCount = 0;
		int highCount = 0;
		int lowCount = 0;

		try {
			for (ExecutiveModuleMetrics module : modules) {
				List<ExecutiveMetricsSeries> seriesList = module.getSeries();
				if (!seriesList.isEmpty()) {
					for (ExecutiveMetricsSeries series : seriesList) {
						List<SeriesCount> countList = series.getCounts();
						for (SeriesCount count : countList) {
							if (count.getLabel().get(PRIORITY).equalsIgnoreCase(CMIS))
								cmisCount += count.getCount();
							if (count.getLabel().get(PRIORITY).equalsIgnoreCase(SN))
								snCount += count.getCount();
							if (count.getLabel().get(PRIORITY).equalsIgnoreCase(NORMAL))
								normalCount += count.getCount();
							if (count.getLabel().get(PRIORITY).equalsIgnoreCase(BLOCKER))
								blockerCount += count.getCount();
							if (count.getLabel().get(PRIORITY).equalsIgnoreCase(HIGH))
								highCount += count.getCount();
							if (count.getLabel().get(PRIORITY).equalsIgnoreCase(LOW))
								lowCount += count.getCount();
						}
					}
				}
			}
			if ((cmisCount + snCount + normalCount + blockerCount + highCount + lowCount) > 0)
				return true;
			return false;
		} catch (Exception e) {
			LOG.info("Error while checkForDataAvailability: " + e);
		}
		return false;
	}

	/**
	 * getDateTimeStamp
	 * 
	 * @param days
	 * @return Long
	 */
	public Long getDateTimeStamp(int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -days);
		String dateStr = DatatypeConverter.printDateTime(calendar).substring(0, 10);
		try {
			Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
			return date.getTime();
		} catch (Exception e) {
			LOG.info("getDateTimeStamp Info :: " + e);
		}
		return (long) 0;
	}

	/**
	 * getTrendSlopesForModules
	 * 
	 * @param modules
	 * @return Double
	 */

	private Double getTrendSlopesForModules(List<ExecutiveModuleMetrics> modules) {
		Map<Integer, Long> seriesList = new HashMap<>();
		if (!modules.isEmpty()) {
			for (ExecutiveModuleMetrics executiveModuleMetrics : modules) {
				List<ExecutiveMetricsSeries> executiveMetricsSeriesList = executiveModuleMetrics.getSeries();
				if (!executiveMetricsSeriesList.isEmpty()) {
					for (ExecutiveMetricsSeries executiveMetricsSeries : executiveMetricsSeriesList) {
						int days = executiveMetricsSeries.getDaysAgo();
						Long finalCount = (long) 0;
						if (executiveMetricsSeries.getCounts() != null
								&& !executiveMetricsSeries.getCounts().isEmpty()) {
							if (!seriesList.containsKey(days)) {
								List<SeriesCount> countList = executiveMetricsSeries.getCounts();
								for (SeriesCount count : countList) {
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(NORMAL))
										finalCount += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(BLOCKER))
										finalCount += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(HIGH))
										finalCount += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(LOW))
										finalCount += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(CMIS))
										finalCount += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(SN))
										finalCount += count.getCount();
								}
								seriesList.put(days, finalCount);
							} else {
								List<SeriesCount> countList = executiveMetricsSeries.getCounts();
								finalCount += seriesList.get(days);

								for (SeriesCount count : countList) {
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(NORMAL))
										finalCount += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(BLOCKER))
										finalCount += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(HIGH))
										finalCount += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(LOW))
										finalCount += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(CMIS))
										finalCount += count.getCount();
									if (count.getLabel().get(PRIORITY).equalsIgnoreCase(SN))
										finalCount += count.getCount();
								}

								seriesList.replace(days, finalCount);
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
	 * getLastCreatedDateForJiraModule
	 * 
	 * @param client
	 * @param projectName
	 * @return Date
	 */

	private Date getLastCreatedDateForJiraModule(String projectName, MongoClient client) {
		FeatureUserStory userStory = qualityDetailsDAO.getLatestUserStorySorted(projectName, client);
		if (userStory != null && userStory.getChangeDate() != null) {
			String creationDate = userStory.getChangeDate();
			LOG.info("Quality ::: Return LastUpdated Date from JIRA for Module : " + projectName + " :: "
					+ creationDate);
			return fromISODateTimeFormatz(creationDate);
		} else {
			Date lastUpdated = null;
			CollectorStatus collectorStatus = collectorStatusRepository.findByType(CollectorType.JiraUserStory);
			if (collectorStatus != null)
				lastUpdated = collectorStatus.getLastUpdated();

			LOG.info("Quality ::: Return LastUpdated Date collectorStatusRepository : " + projectName + " :: "
					+ lastUpdated);
			return lastUpdated;
		}

	}

	/**
	 * 
	 * @param appId
	 * @param client
	 * @return
	 */

	private Date getLastCreatedDateForSNModule(String appId) {
		ServiceNowIssues serviceNowIssue = serviceNowDAO.getLatestServiceNowSorted(appId);
		if (serviceNowIssue != null && serviceNowIssue.getAysCreatedTimeStamp() != null) {
			Long timestamp = serviceNowIssue.getAysCreatedTimeStamp();
			return getISODateTime(timestamp);
		} else {
			Date lastUpdated = null;
			CollectorStatus collectorStatus = collectorStatusRepository.findByType(CollectorType.ServiceNow);
			if (collectorStatus != null)
				lastUpdated = collectorStatus.getLastUpdated();
			return lastUpdated;
		}
	}

	/**
	 * fromISODateTimeFormatz
	 * 
	 * @param isoString
	 * @return Date
	 */

	private Date fromISODateTimeFormatz(String isoString) {
		String iString = isoString;
		if (isoString != null && !isoString.isEmpty()) {
			int charIndex = iString.indexOf('.');
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
	 * getTrendSlope
	 * 
	 * @param execMetricsSeriesList
	 * @return Double
	 */
	private Double getTrendSlope(List<ExecutiveMetricsSeries> execMetricsSeriesList) {
		Map<Long, Integer> mappedValue = new HashMap<>();
		if (execMetricsSeriesList != null) {
			for (ExecutiveMetricsSeries seris : execMetricsSeriesList) {
				long timestamp = seris.getTimeStamp();
				int counting = 0;
				List<SeriesCount> counts = seris.getCounts();
				for (SeriesCount count : counts) {
					if (count.getLabel().get(PRIORITY).equalsIgnoreCase(NORMAL))
						counting += count.getCount();
					if (count.getLabel().get(PRIORITY).equalsIgnoreCase(BLOCKER))
						counting += count.getCount();
					if (count.getLabel().get(PRIORITY).equalsIgnoreCase(HIGH))
						counting += count.getCount();
					if (count.getLabel().get(PRIORITY).equalsIgnoreCase(LOW))
						counting += count.getCount();
					if (count.getLabel().get(PRIORITY).equalsIgnoreCase(CMIS))
						counting += count.getCount();
					if (count.getLabel().get(PRIORITY).equalsIgnoreCase(SN))
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
	 * getFailureRate
	 * 
	 * @param appIds
	 * @param defectCounts
	 * @return
	 */

	private double getFailureRate(List<String> appIds, long defectCounts) {
		MongoClient client = null;
		try {
			client = qualityDetailsDAO.getMongoClient();
			double casCount = getChangeActivity(appIds, client);
			if (casCount > 0 && defectCounts > 0)
				return defectCounts / casCount;
		} catch (Exception e) {
			LOG.info("Error inside getFailureRate " + e);
		} finally {
			if (client != null)
				client.close();
		}
		return 0;
	}

	/**
	 * isClosedTicket
	 * 
	 * @param featureUserStory
	 * @return
	 */

	private long isClosedTicket(FeatureUserStory featureUserStory) {
		if ("Done".equalsIgnoreCase(featureUserStory.getStatusCategory()))
			return 1;
		return 0;
	}

	/**
	 * getTimeStamp
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
	 * getChangeActivity
	 * 
	 * @param appIds
	 * @param client
	 * @return Double
	 */

	public Double getChangeActivity(List<String> appIds, MongoClient client) {
		try {
			List<ServiceNowTicket> snTickets = serviceNowDAO.getServiceNowTickets(appIds, client);
			if (snTickets != null && !snTickets.isEmpty())
				return processChangeActivity(snTickets);

		} catch (Exception e) {
			LOG.info("Error in getting the Deployment Cadence :: " + e);
		}
		return 0.0;
	}

	/**
	 * processChangeActivity
	 * 
	 * @param serviceNowTickets
	 * @return
	 */

	private Double processChangeActivity(List<ServiceNowTicket> serviceNowTickets) {
		List<String> dates = new ArrayList<>();
		List<String> changeActivities = new ArrayList<>();
		List<ServiceNowTicket> rnTickets = new ArrayList<>();
		for (ServiceNowTicket sn : serviceNowTickets) {
			String date = sn.getStartdate() + "::" + sn.getEnddate();
			if (!dates.contains(date)) {
				rnTickets.add(sn);
				dates.add(date);
				changeActivities.add(sn.getNumber());
			}
		}

		if (!rnTickets.isEmpty())
			return (double) (rnTickets.size());

		return 0.0;
	}

	/**
	 * QualityAnalysis removeUnusedQualityDetails()
	 * 
	 * @return Boolean
	 **/
	public Boolean removeUnusedQualityDetails() {
		LOG.info("Removing Unused Quality Details : executives_metrics . . . . ");
		MongoClient client = null;
		try {
			client = qualityDetailsDAO.getMongoClient();
			List<String> appIdList = vastDetailsDAO.getAllAppIds(client);
			if (appIdList != null && !appIdList.isEmpty()) {
				List<ExecutiveComponents> securityDataList = executiveComponentRepository.getNotUsedAppIdList(appIdList,
						QUALITY);
				if (securityDataList != null)
					executiveComponentRepository.delete(securityDataList);
			}
		} catch (Exception e) {
			LOG.info("Error inside removeUnusedQualityDetails " + e);
		} finally {
			if (client != null)
				client.close();
		}
		LOG.info("Completed Removing Unused Quality Details : executives_metrics . . . . ");
		return true;
	}

}
