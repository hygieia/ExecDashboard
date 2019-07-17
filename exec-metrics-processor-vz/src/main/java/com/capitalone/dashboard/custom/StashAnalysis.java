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
import com.capitalone.dashboard.dao.StashDAO;
import com.capitalone.dashboard.dao.VastDetailsDAO;
import com.capitalone.dashboard.exec.model.MetricCount;
import com.capitalone.dashboard.exec.model.MetricLevel;
import com.capitalone.dashboard.exec.model.MetricSummary;
import com.capitalone.dashboard.exec.model.MetricTimeSeriesElement;
import com.capitalone.dashboard.exec.model.MetricType;
import com.capitalone.dashboard.exec.model.ApplicationDetails;
import com.capitalone.dashboard.exec.model.BitbucketPullRequest;
import com.capitalone.dashboard.exec.model.BuildingBlocks;
import com.capitalone.dashboard.exec.model.CollectorStatus;
import com.capitalone.dashboard.exec.model.CollectorType;
import com.capitalone.dashboard.exec.model.Commit;
import com.capitalone.dashboard.exec.model.DateWiseMetricsSeries;
import com.capitalone.dashboard.exec.model.ExecutiveComponents;
import com.capitalone.dashboard.exec.model.ExecutiveMetrics;
import com.capitalone.dashboard.exec.model.ExecutiveMetricsSeries;
import com.capitalone.dashboard.exec.model.ExecutiveModuleMetrics;
import com.capitalone.dashboard.exec.model.ExecutiveSummaryList;
import com.capitalone.dashboard.exec.model.MetricsDetail;
import com.capitalone.dashboard.exec.model.PortfolioResponse;
import com.capitalone.dashboard.exec.model.RepoDetails;
import com.capitalone.dashboard.exec.model.SeriesCount;
import com.capitalone.dashboard.exec.model.StashDetailsInfo;
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
import com.mongodb.MongoClient;

/**
 * StashAnalysis
 * 
 * @param
 * @return
 * @author Pranav
 */
@Component
@SuppressWarnings("PMD")
public class StashAnalysis implements MetricsProcessor {

	private final StashDAO stashDAO;
	private final VastDetailsDAO vastDetailsDAO;
	private final ExecutiveComponentRepository executiveComponentRepository;
	private final MetricsDetailRepository metricsDetailRepository;
	private final BuildingBlocksRepository buildingBlocksRepository;
	private final ApplicationDetailsRepository applicationDetailsRepository;
	private final CollectorStatusRepository collectorStatusRepository;
	private final ExecutiveSummaryListRepository executiveSummaryListRepository;
	private final PortfolioResponseRepository portfolioResponseRepository;
	private final MongoTemplate mongoTemplate;
	private final MetricsProcessorSettings metricsSettings;
	private final GenericMethods genericMethods;
	private final DateWiseMetricsSeriesRepository dateWiseMetricsSeriesRepository;

	private static final String APPID = "appId";
	private static final String STASH = "stash";
	private static final String TYPE = "type";
	private static final String METRICSNAME = "metrics.metricsName";
	private static final String EXECUTIVEMETRICS = "executives_metrics";
	private static final String TOTALCOMMITS = "totalCommits";
	private static final String UNIQUECONTRIBUTORS = "uniqueContributors";
	private static final String MERGEDPR = "mergedPullRequests";
	private static final String DECLINEDPR = "declinedPullRequests";
	private static final String CONTRIBUTORSLIST = "contributorsList";
	private static final String MERGED = "Merged";
	private static final String DECLINED = "Declined";
	private static final String ADDEDLINES = "addedLines";
	private static final String REMOVEDLINES = "removedLines";
	private static final String COMMITS = "commits";
	private static final String PULLREQUESTDETAILS = "Pull Request";
	private static final String MESSAGE = "No Commits/Pull Requests";

	private static final Logger LOG = LoggerFactory.getLogger(StashAnalysis.class);

	/**
	 * StashAnalysis
	 * 
	 * @param stashDAO,
	 * @param executiveComponentRepository,
	 * @param mongoTemplate,
	 * @param executiveSummaryListRepository,
	 * @param applicationDetailsRepository,
	 * @param buildingBlocksRepository,
	 * @param metricsDetailRepository,
	 * @param collectorStatusRepository,
	 * @param portfolioResponseRepository
	 * @param vastDetailsDAO
	 * @param metricsSettings
	 * @param dateWiseMetricsSeriesRepository
	 * @param genericMethods
	 * @return
	 */

	@Autowired
	public StashAnalysis(StashDAO stashDAO, ExecutiveComponentRepository executiveComponentRepository,
			MetricsDetailRepository metricsDetailRepository, BuildingBlocksRepository buildingBlocksRepository,
			ApplicationDetailsRepository applicationDetailsRepository,
			CollectorStatusRepository collectorStatusRepository,
			ExecutiveSummaryListRepository executiveSummaryListRepository,
			PortfolioResponseRepository portfolioResponseRepository, MongoTemplate mongoTemplate,
			VastDetailsDAO vastDetailsDAO, MetricsProcessorSettings metricsSettings,
			DateWiseMetricsSeriesRepository dateWiseMetricsSeriesRepository, GenericMethods genericMethods) {
		this.stashDAO = stashDAO;
		this.executiveComponentRepository = executiveComponentRepository;
		this.metricsDetailRepository = metricsDetailRepository;
		this.buildingBlocksRepository = buildingBlocksRepository;
		this.applicationDetailsRepository = applicationDetailsRepository;
		this.collectorStatusRepository = collectorStatusRepository;
		this.executiveSummaryListRepository = executiveSummaryListRepository;
		this.portfolioResponseRepository = portfolioResponseRepository;
		this.mongoTemplate = mongoTemplate;
		this.vastDetailsDAO = vastDetailsDAO;
		this.metricsSettings = metricsSettings;
		this.dateWiseMetricsSeriesRepository = dateWiseMetricsSeriesRepository;
		this.genericMethods = genericMethods;
	}

	/**
	 * processDateWiseTrend
	 * 
	 * @return Boolean
	 */
	public Boolean processDateWiseTrend() {
		LOG.info("Processing Stash Date Wise Trend : date_wise_metrics . . . . ");
		MongoClient client = null;
		try {
			client = stashDAO.getMongoClient();
			List<String> appIds = stashDAO.getEntireAppList(client);
			Long timeStamp = metricsSettings.getDateRange();
			for (String appId : appIds) {
				List<StashDetailsInfo> stashDetailsList = stashDAO.getEntireProjectList(client, appId);
				if (stashDetailsList != null && !stashDetailsList.isEmpty()) {
					LOG.info("Starting collection date_wise_metrics for appId: " + appId);
					processDateWiseTrendSeriesForCommits(client, appId, stashDetailsList, timeStamp);
					processDateWiseTrendSeriesForPullRequests(client, appId, timeStamp);
					LOG.info("Completed collection date_wise_metrics for appId: " + appId);
				}
			}
		} catch (Exception e) {
			LOG.info("Error in Stash Analysis Date Wise Trend :: " + e);
		} finally {
			if (client != null)
				client.close();
		}
		LOG.info("Completed Stash Date Wise Trend : date_wise_metrics . . . . ");
		return true;
	}

	/**
	 * 
	 * @param client
	 * @param appId
	 * @param timeStamp
	 * @return
	 */

	private Boolean processDateWiseTrendSeriesForPullRequests(MongoClient client, String appId, Long timeStamp) {
		try {
			Long revisedTimeStamp = timeStamp;
			String moduleName = appId + PULLREQUESTDETAILS;
			DateWiseMetricsSeries dateWiseMetricsSeries = dateWiseMetricsSeriesRepository
					.findByAppIdAndModuleNameAndMetricsNameOrderByTimeStampDesc(appId, moduleName, STASH);
			if (dateWiseMetricsSeries != null && dateWiseMetricsSeries.getTimeStamp() != null)
				revisedTimeStamp = dateWiseMetricsSeries.getTimeStamp();
			boolean proceedPullRequest = stashDAO.isDataAvailableforPullRequests(appId, revisedTimeStamp, client);
			if (proceedPullRequest) {
				fetchAndUpdateForRevisedTimeForPullRequests(client, appId, moduleName, revisedTimeStamp);
			}
		} catch (Exception e) {
			LOG.info("Error in Stash Analysis Date Wise Trend Series :: " + e);
		}
		return true;
	}

	/**
	 * 
	 * @param client
	 * @param appId
	 * @param moduleName
	 * @param revisedTimeStamp
	 */

	private Boolean fetchAndUpdateForRevisedTimeForPullRequests(MongoClient client, String appId, String moduleName,
			Long timeStamp) {
		try {
			Long presentTimeStamp = System.currentTimeMillis();
			Date revisedDate = new Date(timeStamp);
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String revisedStringDate = dateFormat.format(revisedDate);
			Date date = dateFormat.parse(revisedStringDate);
			Long revisedTimeStamp = date.getTime();
			while (revisedTimeStamp < presentTimeStamp) {
				DateWiseMetricsSeries dateWiseMetricsSeries = dateWiseMetricsSeriesRepository
						.findByAppIdAndModuleNameAndMetricsNameAndTimeStamp(appId, moduleName, STASH, revisedTimeStamp);
				if (dateWiseMetricsSeries == null) {
					dateWiseMetricsSeries = new DateWiseMetricsSeries();
				}

				List<SeriesCount> seriesCountList = new ArrayList<>();
				SeriesCount mergedPRType = new SeriesCount();
				SeriesCount declinedPRType = new SeriesCount();

				Map<String, String> mergedPullRequests = new HashMap<>();
				Map<String, String> declinedPullRequests = new HashMap<>();

				List<BitbucketPullRequest> pullRequestList = stashDAO.getPullRequestList(appId, revisedTimeStamp,
						revisedTimeStamp + 86399999, client);
				Map<String, Object> pullRequestMap = getPullRequestDetailsMap(pullRequestList);

				mergedPullRequests.put(TYPE, MERGEDPR);
				declinedPullRequests.put(TYPE, DECLINEDPR);

				mergedPRType.setCount((long) pullRequestMap.get(MERGEDPR));
				mergedPRType.setLabel(mergedPullRequests);
				declinedPRType.setCount((long) pullRequestMap.get(DECLINEDPR));
				declinedPRType.setLabel(declinedPullRequests);

				seriesCountList.add(mergedPRType);
				seriesCountList.add(declinedPRType);

				dateWiseMetricsSeries.setAppId(appId);
				dateWiseMetricsSeries.setMetricsName(STASH);
				dateWiseMetricsSeries.setModuleName(appId + PULLREQUESTDETAILS);
				dateWiseMetricsSeries.setTimeStamp(revisedTimeStamp);
				dateWiseMetricsSeries.setDateValue(dateFormat.format(revisedDate));
				dateWiseMetricsSeries.setCounts(seriesCountList);

				dateWiseMetricsSeriesRepository.save(dateWiseMetricsSeries);

				revisedTimeStamp += 86400000;
				revisedDate = new Date(revisedTimeStamp);

			}

		} catch (Exception e) {
			LOG.info("Error in Stash Analysis Date Wise Trend fetch And Update For Revised Time :: " + e);
		}
		return true;

	}

	/**
	 * processDateWiseTrendSeriesForCommits()
	 * 
	 * @param client
	 * @param appId
	 * @param timeStamp
	 * @param stashDetailsList
	 * @return Boolean
	 */

	private Boolean processDateWiseTrendSeriesForCommits(MongoClient client, String appId,
			List<StashDetailsInfo> stashDetailsList, Long timeStamp) {
		try {
			for (StashDetailsInfo stashDetails : stashDetailsList) {
				Long revisedTimeStamp = timeStamp;
				List<RepoDetails> repoDetailsList = stashDetails.getRepoDetails();
				for (RepoDetails repoDetails : repoDetailsList) {
					String projectKey = repoDetails.getProjectKey();
					String repoBranch = repoDetails.getRepoBranch();
					String repoUrl = repoDetails.getRepoUrl();
					String moduleName = repoDetails.getProjectKey() + "/" + repoDetails.getRepoSlug() + "/"
							+ repoDetails.getRepoBranch();
					DateWiseMetricsSeries dateWiseMetricsSeries = dateWiseMetricsSeriesRepository
							.findByAppIdAndModuleNameAndMetricsNameOrderByTimeStampDesc(appId, moduleName, STASH);
					if (dateWiseMetricsSeries != null && dateWiseMetricsSeries.getTimeStamp() != null)
						revisedTimeStamp = dateWiseMetricsSeries.getTimeStamp();
					boolean proceedCommits = stashDAO.isDataAvailableForCommits(appId, repoUrl, repoBranch,
							revisedTimeStamp, client);
					if (proceedCommits) {
						fetchAndUpdateForRevisedTimeForCommits(client, appId, moduleName, revisedTimeStamp, projectKey,
								repoBranch, repoUrl);
					}
				}
			}
		} catch (Exception e) {
			LOG.info("Error in Stash Analysis Date Wise Trend Series :: " + e);
		}
		return true;
	}

	/**
	 * fetchAndUpdateForRevisedTimeForCommits
	 * 
	 * @param client
	 * @param appId
	 * @param timeStamp
	 * @param moduleName
	 * @param projectKey
	 * @param repoBranch
	 * @param repoUrl
	 * @param pullRequestList
	 * @param pullRequestMap
	 * @return Boolean
	 */

	private Boolean fetchAndUpdateForRevisedTimeForCommits(MongoClient client, String appId, String moduleName,
			Long timeStamp, String projectKey, String repoBranch, String repoUrl) {
		try {
			Long presentTimeStamp = System.currentTimeMillis();
			Date revisedDate = new Date(timeStamp);
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String revisedStringDate = dateFormat.format(revisedDate);
			Date date = dateFormat.parse(revisedStringDate);
			Long revisedTimeStamp = date.getTime();
			while (revisedTimeStamp < presentTimeStamp) {
				DateWiseMetricsSeries dateWiseMetricsSeries = dateWiseMetricsSeriesRepository
						.findByAppIdAndModuleNameAndMetricsNameAndTimeStamp(appId, moduleName, STASH, revisedTimeStamp);
				if (dateWiseMetricsSeries == null)
					dateWiseMetricsSeries = new DateWiseMetricsSeries();

				List<SeriesCount> seriesCountList = new ArrayList<>();
				SeriesCount totalCommitsType = new SeriesCount();
				SeriesCount uniqueContributorsType = new SeriesCount();

				Map<String, String> totalCommits = new HashMap<>();
				Map<String, List<String>> uniqueContributorsList = new HashMap<>();

				List<Commit> commitList = stashDAO.getCommitsList(appId, repoUrl, repoBranch, revisedTimeStamp,
						revisedTimeStamp + 86399999, client);
				Map<String, Object> contributorsMap = getContributorsDetailMap(commitList);
				Map<String, Map<String, Long>> contributorsLocMap = getContributorsLOCMap(commitList);

				totalCommits.put(TYPE, TOTALCOMMITS);
				uniqueContributorsList.put(CONTRIBUTORSLIST, (List<String>) contributorsMap.get(CONTRIBUTORSLIST));

				totalCommitsType.setCount((long) commitList.size());
				totalCommitsType.setLabel(totalCommits);
				uniqueContributorsType.setCount((long) contributorsMap.get(UNIQUECONTRIBUTORS));
				uniqueContributorsType.setLabelStash(uniqueContributorsList);
				uniqueContributorsType.setLabelLoc(contributorsLocMap);

				seriesCountList.add(totalCommitsType);
				seriesCountList.add(uniqueContributorsType);

				dateWiseMetricsSeries.setAppId(appId);
				dateWiseMetricsSeries.setMetricsName(STASH);
				dateWiseMetricsSeries.setModuleName(moduleName);
				dateWiseMetricsSeries.setTeamId(projectKey);
				dateWiseMetricsSeries.setTimeStamp(revisedTimeStamp);
				dateWiseMetricsSeries.setDateValue(dateFormat.format(revisedDate));
				dateWiseMetricsSeries.setCounts(seriesCountList);

				dateWiseMetricsSeriesRepository.save(dateWiseMetricsSeries);

				revisedTimeStamp += 86400000;
				revisedDate = new Date(revisedTimeStamp);

			}

		} catch (Exception e) {
			LOG.info("Error in Stash Analysis Date Wise Trend fetch And Update For Revised Time :: " + e);
		}
		return true;
	}

	/**
	 *
	 * getPullRequestDetailsMap
	 * 
	 * @param pullRequestList
	 * @return Map<String, Object>
	 */

	private Map<String, Object> getPullRequestDetailsMap(List<BitbucketPullRequest> pullRequestList) {
		Map<String, Object> rtMap = new HashMap<>();
		long mergedPRCount = 0;
		long declinedPRCount = 0;

		try {
			if (pullRequestList != null && !pullRequestList.isEmpty()) {
				for (BitbucketPullRequest pullRequest : pullRequestList) {
					if (pullRequest.getMergetype().toString().equalsIgnoreCase(MERGED))
						mergedPRCount++;
					else if (pullRequest.getMergetype().toString().equalsIgnoreCase(DECLINED))
						declinedPRCount++;
				}
			}
			rtMap.put(MERGEDPR, mergedPRCount);
			rtMap.put(DECLINEDPR, declinedPRCount);

		} catch (Exception e) {
			LOG.info("Error in getPullRequestDetailsMap() (StashAnalysis Class)" + e);
		}
		return rtMap;
	}

	/**
	 *
	 * getContributorsDetailMap
	 * 
	 * @param commitsList
	 * @return Map<String, Object>
	 */

	private Map<String, Object> getContributorsDetailMap(List<Commit> commitsList) {
		Map<String, Object> rtMap = new HashMap<>();
		long uniqueContributorsCount = 0;
		List<String> uniqueContributorsList = new ArrayList<>();
		try {
			if (commitsList != null && !commitsList.isEmpty()) {
				for (Commit commit : commitsList) {
					String name = commit.getScmAuthor().toLowerCase();
					name = name.replaceAll("\\.", " ");
					if (!uniqueContributorsList.contains(name)) {
						uniqueContributorsCount++;
						uniqueContributorsList.add(name);
					}
				}
			}
			rtMap.put(UNIQUECONTRIBUTORS, uniqueContributorsCount);
			rtMap.put(CONTRIBUTORSLIST, uniqueContributorsList);

		} catch (Exception e) {
			LOG.info("Error in getContributorsDetailMap() (StashAnalysis Class)" + e);
		}
		return rtMap;
	}

	/**
	 * getContributorsLOCMap - to get lines
	 * 
	 * @param commitsList
	 * @return
	 */

	private Map<String, Map<String, Long>> getContributorsLOCMap(List<Commit> commitsList) {
		Map<String, Map<String, Long>> rtMap = new HashMap<>();
		List<String> uniqueContributorsList = new ArrayList<>();
		try {
			if (commitsList != null && !commitsList.isEmpty()) {
				for (Commit commit : commitsList) {
					String name = commit.getScmAuthor().toLowerCase();
					name = name.replaceAll("\\.", " ");

					if (!uniqueContributorsList.contains(name)) {
						uniqueContributorsList.add(name);
						Map<String, Long> commitMetrics = new HashMap<>();
						commitMetrics.put(ADDEDLINES, commit.getAddedNoOfLines());
						commitMetrics.put(REMOVEDLINES, commit.getRemovedNoOfLines());
						commitMetrics.put(COMMITS, commit.getNumberOfChanges());
						rtMap.put(name, commitMetrics);
					} else {

						Map<String, Long> commitMetrics = rtMap.get(name);

						if (commitMetrics.containsKey(ADDEDLINES)) {
							Long addedLines = commitMetrics.get(ADDEDLINES);
							addedLines += commit.getAddedNoOfLines();
							commitMetrics.put(ADDEDLINES, addedLines);
						} else {
							commitMetrics.put(ADDEDLINES, commit.getAddedNoOfLines());
						}

						if (commitMetrics.containsKey(REMOVEDLINES)) {
							Long removedLines = commitMetrics.get(REMOVEDLINES);
							removedLines += commit.getRemovedNoOfLines();
							commitMetrics.put(REMOVEDLINES, removedLines);
						} else {
							commitMetrics.put(REMOVEDLINES, commit.getRemovedNoOfLines());
						}

						if (commitMetrics.containsKey(COMMITS)) {
							Long commits = commitMetrics.get(COMMITS);
							commits += commit.getNumberOfChanges();
							commitMetrics.put(COMMITS, commits);
						} else {
							commitMetrics.put(COMMITS, commit.getNumberOfChanges());
						}

						rtMap.put(name, commitMetrics);
					}
				}
			}
		} catch (Exception e) {
			LOG.info("Error in getContributorsLOCMap() (StashAnalysis Class)" + e);
		}
		return rtMap;
	}

	/**
	 *
	 * processStashDetails
	 * 
	 * @return Boolean
	 */

	public Boolean processExecutiveMetricsDetails() {
		LOG.info("Processing Stash Details : executives_metrics . . . . ");
		MongoClient client = null;
		try {
			client = stashDAO.getMongoClient();
			List<String> appIds = stashDAO.getEntireAppList(client);
			for (String appId : appIds) {
				ApplicationDetails appDetails = applicationDetailsRepository.findByAppId(appId);
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
						STASH);
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
			LOG.info("processStashDetails Stash Analysis Info :: " + e);
		} finally {
			if (client != null)
				client.close();
		}
		LOG.info("Completed Stash Details : executives_metrics . . . . ");
		return true;
	}

	/**
	 *
	 * processAppMetrics
	 * 
	 * @param client
	 * @param appId
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
		executiveMetrics.setMetricsName(STASH);

		List<StashDetailsInfo> stashDetailsList = stashDAO.getEntireProjectList(client, appId);
		for (StashDetailsInfo stashDetails : stashDetailsList) {
			List<RepoDetails> repoDetailsList = stashDetails.getRepoDetails();
			for (RepoDetails repoDetails : repoDetailsList) {
				ExecutiveModuleMetrics executiveModuleMetrics = getModuleMetrics(appId, repoDetails, client);
				modules.add(executiveModuleMetrics);
			}
		}
		modules.add(processPullRequestData(appId));
		executiveMetrics.setTrendSlope(getTrendSlopesForModules(modules));
		executiveMetrics.setModules(modules);
		execMetricsList.add(executiveMetrics);
		return execMetricsList;
	}

	private ExecutiveModuleMetrics processPullRequestData(String appId) {
		ExecutiveModuleMetrics execModuleMetrics = new ExecutiveModuleMetrics();
		if (appId != null) {
			Date lastUpdated = null;
			CollectorStatus collectorStatus = collectorStatusRepository.findByType(CollectorType.SCM);
			if (collectorStatus != null)
				lastUpdated = collectorStatus.getLastUpdated();
			List<ExecutiveMetricsSeries> execMetricsSeriesList = getSeriesCountList(appId, appId + PULLREQUESTDETAILS);
			execModuleMetrics.setModuleName(appId + PULLREQUESTDETAILS);
			execModuleMetrics.setLastScanned(lastUpdated);
			execModuleMetrics.setLastUpdated(getISODateTime(System.currentTimeMillis()));
			execModuleMetrics.setTrendSlope(getTrendSlope(execMetricsSeriesList));
			execModuleMetrics.setSeries(execMetricsSeriesList);
		}
		return execModuleMetrics;
	}

	/**
	 *
	 * getModuleMetrics
	 * 
	 * @param appId
	 * @param repoDetails
	 * @param client
	 * @return ExecutiveModuleMetrics
	 */

	private ExecutiveModuleMetrics getModuleMetrics(String appId, RepoDetails repoDetails, MongoClient client) {
		try {
			ExecutiveModuleMetrics execModuleMetrics = new ExecutiveModuleMetrics();
			String moduleName = repoDetails.getProjectKey() + "/" + repoDetails.getRepoSlug() + "/"
					+ repoDetails.getRepoBranch();
			List<ExecutiveMetricsSeries> execMetricsSeriesList = getSeriesCountList(appId, moduleName);
			execModuleMetrics.setModuleName(moduleName);
			execModuleMetrics.setTeamId(repoDetails.getRepoUrl());
			execModuleMetrics.setLastScanned(getLastCommitforModule(repoDetails, client));
			execModuleMetrics.setLastUpdated(getISODateTime(System.currentTimeMillis()));
			execModuleMetrics.setTrendSlope(getTrendSlope(execMetricsSeriesList));
			execModuleMetrics.setSeries(execMetricsSeriesList);
			return execModuleMetrics;

		} catch (Exception e) {
			LOG.info("Error in StashAnalysis class :: getModuleMetrics()" + e);
		}
		return null;
	}

	/**
	 *
	 * getSeriesCountList
	 * 
	 * @param appId
	 * @param moduleName
	 * @return List<ExecutiveMetrics>
	 */

	private List<ExecutiveMetricsSeries> getSeriesCountList(String appId, String moduleName) {

		List<ExecutiveMetricsSeries> executiveMetricsSeriesList = new ArrayList<>();
		List<String> contributorsListFinal = new ArrayList<>();
		Map<String, Map<String, Long>> uniqueContributorsList = new HashMap<>();
		Map<String, List<String>> uniqueContributorsNames = new HashMap<>();

		List<DateWiseMetricsSeries> dateWiseMetricsSeriesList = dateWiseMetricsSeriesRepository
				.getThreeMonthsList(appId, moduleName, STASH, getTimeStamp(90));
		for (DateWiseMetricsSeries dateWiseMetricsSeries : dateWiseMetricsSeriesList) {
			ExecutiveMetricsSeries executiveMetricsSeries = new ExecutiveMetricsSeries();
			executiveMetricsSeries.setDaysAgo(getDaysAgoValue(dateWiseMetricsSeries.getTimeStamp()));
			executiveMetricsSeries.setTimeStamp(dateWiseMetricsSeries.getTimeStamp());

			List<SeriesCount> countsListStored = dateWiseMetricsSeries.getCounts();
			List<SeriesCount> countsList = new ArrayList<>();
			SeriesCount totalUniqueContributorsType = new SeriesCount();
			SeriesCount mergedPullRequestType = new SeriesCount();
			SeriesCount declinedPullRequestType = new SeriesCount();
			SeriesCount totalCommitsType = new SeriesCount();

			Map<String, String> totalCommits = new HashMap<>();
			totalCommits.put(TYPE, TOTALCOMMITS);
			Map<String, String> totalUniqueContributors = new HashMap<>();
			totalUniqueContributors.put(TYPE, UNIQUECONTRIBUTORS);
			Map<String, String> mergedPullRequests = new HashMap<>();
			mergedPullRequests.put(TYPE, MERGEDPR);
			Map<String, String> declinedPullRequests = new HashMap<>();
			declinedPullRequests.put(TYPE, DECLINEDPR);

			List<String> contributorsList = new ArrayList<>();
			long totalCommitsCount = 0;
			long contributorsCount = 0;
			long mergedPRCount = 0;
			long declinedPRCount = 0;
			for (SeriesCount count : countsListStored) {
				if (null != count.getLabel() && count.getLabel().get(TYPE).equalsIgnoreCase(TOTALCOMMITS))
					totalCommitsCount = count.getCount();
				if (null != count.getLabel() && count.getLabel().get(TYPE).equalsIgnoreCase(MERGEDPR))
					mergedPRCount = count.getCount();
				if (null != count.getLabel() && count.getLabel().get(TYPE).equalsIgnoreCase(DECLINEDPR))
					declinedPRCount = count.getCount();

				if (null != count.getLabelStash()) {
					contributorsList = count.getLabelStash().get(CONTRIBUTORSLIST);
					uniqueContributorsNames = count.getLabelStash();
					for (String name : contributorsList) {
						if (!contributorsListFinal.contains(name)) {
							contributorsListFinal.add(name);
							contributorsCount++;
						}
					}
				}

				if (null != count.getLabelLoc()) {
					uniqueContributorsList = count.getLabelLoc();
				}
			}

			totalCommitsType.setLabel(totalCommits);
			totalCommitsType.setCount(totalCommitsCount);
			totalUniqueContributorsType.setCount(contributorsCount);
			totalUniqueContributorsType.setLabel(totalUniqueContributors);
			totalUniqueContributorsType.setLabelLoc(uniqueContributorsList);
			totalUniqueContributorsType.setLabelStash(uniqueContributorsNames);
			mergedPullRequestType.setCount(mergedPRCount);
			mergedPullRequestType.setLabel(mergedPullRequests);
			declinedPullRequestType.setCount(declinedPRCount);
			declinedPullRequestType.setLabel(declinedPullRequests);

			countsList.add(totalCommitsType);
			countsList.add(totalUniqueContributorsType);
			countsList.add(mergedPullRequestType);
			countsList.add(declinedPullRequestType);
			executiveMetricsSeries.setCounts(countsList);
			executiveMetricsSeriesList.add(executiveMetricsSeries);
		}
		return executiveMetricsSeriesList;
	}

	/**
	 * StashAnalysis removeUnusedQualityDetails()
	 * 
	 * @return Boolean
	 **/

	public Boolean removeUnusedStashDetails() {
		LOG.info("Removing Unused Stash Details : executives_metrics . . . . ");
		MongoClient client = null;
		try {
			client = stashDAO.getMongoClient();
			List<String> appIdList = vastDetailsDAO.getAllAppIds(client);
			if (appIdList != null && !appIdList.isEmpty()) {
				List<ExecutiveComponents> securityDataList = executiveComponentRepository.getNotUsedAppIdList(appIdList,
						STASH);
				if (securityDataList != null)
					executiveComponentRepository.delete(securityDataList);
			}
		} catch (Exception e) {
			LOG.info("Error inside removeUnusedStashDetails " + e);
		} finally {
			if (client != null)
				client.close();
		}
		LOG.info("Completed Removing Unused Stash Details : executives_metrics . . . . ");
		return true;
	}

	/**
	 * processMetricsDetailResponse
	 * 
	 * @return Boolean
	 */

	public Boolean processMetricsDetailResponse() {
		try {
			LOG.info("Processing Stash Details : app_metrics_details . . . . ");
			Query query = new Query(new Criteria().where(METRICSNAME).is(STASH));
			List<String> appIds = mongoTemplate.getCollection(EXECUTIVEMETRICS).distinct(APPID, query.getQueryObject());
			if (!appIds.isEmpty()) {
				for (String appId : appIds) {
					ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
							STASH);
					MetricsDetail metricDetailResponseProcessed = processMetricDetailResponse(executiveComponents);
					MetricsDetail metricDetailResponseStored = metricsDetailRepository.findByMetricLevelIdAndLevelAndType(appId,
							MetricLevel.PRODUCT, MetricType.STASH);
					if (metricDetailResponseStored != null) {
						metricsDetailRepository.delete(metricDetailResponseStored);
					}
					metricsDetailRepository.save(metricDetailResponseProcessed);
				}
			}
			LOG.info("Completed Stash Details : app_metrics_details . . . . ");
			return true;

		} catch (Exception e) {
			LOG.info("Error in StashAnalysis class :: processMetricsDetailResponse()" + e);
		}
		return null;
	}

	/**
	 * processMetricDetailResponse
	 * 
	 * @param executiveComponents
	 * @return MetricsDetail
	 */

	private MetricsDetail processMetricDetailResponse(ExecutiveComponents executiveComponents) {
		MetricsDetail metricDetailResponse = new MetricsDetail();
		metricDetailResponse.setMetricLevelId(executiveComponents.getAppId());
		metricDetailResponse.setType(MetricType.STASH);
		metricDetailResponse.setLevel(MetricLevel.PRODUCT);
		metricDetailResponse.setSummary(processMetricsSummary(executiveComponents));
		metricDetailResponse.setTimeSeries(processTimeSeries(executiveComponents));
		return metricDetailResponse;
	}

	/**
	 * processMetricsSummary
	 * 
	 * @param executiveComponents
	 * @return MetricSummary
	 */

	private MetricSummary processMetricsSummary(ExecutiveComponents executiveComponents) {
		MetricSummary metricSummaryResponse = new MetricSummary();
		List<ExecutiveModuleMetrics> modules = new ArrayList<>();
		List<String> teamIds = new ArrayList<>();
		metricSummaryResponse.setLastScanned(executiveComponents.getMetrics().get(0).getLastScanned());
		metricSummaryResponse.setLastUpdated(executiveComponents.getMetrics().get(0).getLastUpdated());
		if (executiveComponents.getMetrics() != null) {
			List<ExecutiveMetrics> metrics = executiveComponents.getMetrics();
			for (ExecutiveMetrics metric : metrics) {
				if (metric.getMetricsName().equalsIgnoreCase(STASH)) {
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
					Boolean dataAvailabilityStatus = checkForDataAvailability(modules);
					metricSummaryResponse.setDataAvailable(dataAvailabilityStatus);
					if (!dataAvailabilityStatus)
						metricSummaryResponse.setConfMessage(MESSAGE);
				}
			}
		}
		metricSummaryResponse.setName(STASH);
		return metricSummaryResponse;
	}

	/**
	 * processMetricSummaryCounts
	 * 
	 * @param modules
	 * @return List<MetricCount>
	 */

	private List<MetricCount> processMetricSummaryCounts(List<ExecutiveModuleMetrics> modules) {

		List<MetricCount> metricCountResponseList = new ArrayList<>();
		MetricCount metricCountResponseTotalCommits = new MetricCount();
		MetricCount metricCountResponseUniqueContributors = new MetricCount();
		MetricCount metricCountResponseMergedPR = new MetricCount();
		MetricCount metricCountResponseDeclinedPR = new MetricCount();
		List<String> teamIds = new ArrayList<>();
		Map<String, Map<String, Long>> labelLoc = new HashMap<>();
		Map<String, List<String>> labelStash = new HashMap<>();

		Long totalCommitsCount = (long) 0;
		Long uniqueContributorsCount = (long) 0;
		Long mergedPRCount = (long) 0;
		Long declinedPRCount = (long) 0;

		if (!modules.isEmpty()) {
			List<String> contributorsListFinal = new ArrayList<>();
			for (ExecutiveModuleMetrics moduleMetric : modules) {
				List<ExecutiveMetricsSeries> seriesList = moduleMetric.getSeries();
				if (!teamIds.contains(moduleMetric.getModuleName())) {
					if (!seriesList.isEmpty()) {
						for (ExecutiveMetricsSeries series : seriesList) {
							List<SeriesCount> countsList = series.getCounts();
							if (!countsList.isEmpty()) {
								for (SeriesCount count : countsList) {
									if (null != count.getLabel()
											&& count.getLabel().get(TYPE).equalsIgnoreCase(TOTALCOMMITS))
										totalCommitsCount += count.getCount();
									if (null != count.getLabelStash()) {
										List<String> uniqueContributorsList = count.getLabelStash()
												.get(CONTRIBUTORSLIST);
										if (uniqueContributorsList != null && !uniqueContributorsList.isEmpty()) {
											for (String name : uniqueContributorsList) {
												if (!contributorsListFinal.contains(name)) {
													contributorsListFinal.add(name);
													uniqueContributorsCount++;
												}
											}
										}
									}
									if (null != count.getLabelLoc())
										labelLoc = processLabelLoc(labelLoc, count.getLabelLoc());
									if (null != count.getLabel()) {
										if (count.getLabel().get(TYPE).equalsIgnoreCase(DECLINEDPR)) {
											declinedPRCount += count.getCount();
										}
										if (count.getLabel().get(TYPE).equalsIgnoreCase(MERGEDPR)) {
											mergedPRCount += count.getCount();
										}
									}
								}
							}
						}
					}
					teamIds.add(moduleMetric.getModuleName());
				}
			}

			Map<String, String> totalCommits = new HashMap<>();
			totalCommits.put(TYPE, TOTALCOMMITS);
			Map<String, String> uniqueContributors = new HashMap<>();
			uniqueContributors.put(TYPE, UNIQUECONTRIBUTORS);
			Map<String, String> mergedPR = new HashMap<>();
			mergedPR.put(TYPE, MERGEDPR);
			Map<String, String> declinedPR = new HashMap<>();
			declinedPR.put(TYPE, DECLINEDPR);

			labelStash.put(CONTRIBUTORSLIST, contributorsListFinal);

			metricCountResponseTotalCommits.setLabel(totalCommits);
			metricCountResponseTotalCommits.setValue(totalCommitsCount);
			metricCountResponseList.add(metricCountResponseTotalCommits);
			metricCountResponseUniqueContributors.setLabelLoc(labelLoc);
			metricCountResponseUniqueContributors.setLabel(uniqueContributors);
			metricCountResponseUniqueContributors.setLabelStash(labelStash);
			metricCountResponseUniqueContributors.setValue(uniqueContributorsCount);
			metricCountResponseList.add(metricCountResponseUniqueContributors);
			metricCountResponseMergedPR.setLabel(mergedPR);
			metricCountResponseMergedPR.setValue(mergedPRCount);
			metricCountResponseList.add(metricCountResponseMergedPR);
			metricCountResponseDeclinedPR.setLabel(declinedPR);
			metricCountResponseDeclinedPR.setValue(declinedPRCount);
			metricCountResponseList.add(metricCountResponseDeclinedPR);

			return metricCountResponseList;
		}
		return metricCountResponseList;
	}

	/**
	 * 
	 * @param processedLoc
	 * @param newLabelLoc
	 * @return
	 */

	private Map<String, Map<String, Long>> processLabelLoc(Map<String, Map<String, Long>> processedLoc,
			Map<String, Map<String, Long>> newLabelLoc) {
		newLabelLoc.forEach((k, v) -> {
			if (processedLoc.containsKey(k)) {
				processedLoc.put(k, processLoc(processedLoc.get(k), v));
			} else {
				processedLoc.put(k, v);
			}
		});
		return processedLoc;
	}

	/**
	 * 
	 * @param commitMetrics
	 * @param v
	 * @return
	 */

	private Map<String, Long> processLoc(Map<String, Long> commitMetrics, Map<String, Long> v) {
		if (commitMetrics.containsKey(ADDEDLINES)) {
			commitMetrics.put(ADDEDLINES, commitMetrics.get(ADDEDLINES) + v.get(ADDEDLINES));
		} else {
			commitMetrics.put(ADDEDLINES, v.get(ADDEDLINES));
		}
		if (commitMetrics.containsKey(REMOVEDLINES)) {
			commitMetrics.put(REMOVEDLINES, commitMetrics.get(REMOVEDLINES) + v.get(REMOVEDLINES));
		} else {
			commitMetrics.put(REMOVEDLINES, v.get(REMOVEDLINES));
		}
		if (commitMetrics.containsKey(COMMITS)) {
			commitMetrics.put(COMMITS, commitMetrics.get(COMMITS) + v.get(COMMITS));
		} else {
			commitMetrics.put(COMMITS, v.get(COMMITS));
		}
		return commitMetrics;
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
						long totalCommitsCount = 0;
						long uniqueContributorsCount = 0;

						if (!seriesList.containsKey(days)) {
							List<SeriesCount> countList = executiveMetricsSeries.getCounts();
							List<MetricCount> metricCountResponseList = new ArrayList<>();

							for (SeriesCount count : countList) {
								if (null != count.getLabel()
										&& count.getLabel().get(TYPE).equalsIgnoreCase(TOTALCOMMITS))
									totalCommitsCount += count.getCount();
								if (null != count.getLabel()
										&& count.getLabel().get(TYPE).equalsIgnoreCase(UNIQUECONTRIBUTORS))
									uniqueContributorsCount += count.getCount();
							}

							Map<String, String> totalCommits = new HashMap<>();
							totalCommits.put(TYPE, TOTALCOMMITS);
							Map<String, String> uniqueContributors = new HashMap<>();
							uniqueContributors.put(TYPE, UNIQUECONTRIBUTORS);

							MetricCount metricCountResponseTotalCommits = new MetricCount();
							metricCountResponseTotalCommits.setLabel(totalCommits);
							metricCountResponseTotalCommits.setValue(totalCommitsCount);
							metricCountResponseList.add(metricCountResponseTotalCommits);
							MetricCount metricCountResponseUniqueContributors = new MetricCount();
							metricCountResponseUniqueContributors.setLabel(uniqueContributors);
							metricCountResponseUniqueContributors.setValue(uniqueContributorsCount);
							metricCountResponseList.add(metricCountResponseUniqueContributors);

							seriesList.put(days, metricCountResponseList);

						} else {
							List<SeriesCount> countList = executiveMetricsSeries.getCounts();
							List<MetricCount> countsProcessed = new ArrayList<>();
							List<MetricCount> counts = seriesList.get(days);

							for (MetricCount metricCountResponse : counts) {
								if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(TOTALCOMMITS))
									totalCommitsCount += metricCountResponse.getValue();
								if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(UNIQUECONTRIBUTORS))
									uniqueContributorsCount += metricCountResponse.getValue();
							}

							for (SeriesCount count : countList) {
								if (null != count.getLabel()
										&& count.getLabel().get(TYPE).equalsIgnoreCase(TOTALCOMMITS))
									totalCommitsCount += count.getCount();
								if (null != count.getLabel()
										&& count.getLabel().get(TYPE).equalsIgnoreCase(UNIQUECONTRIBUTORS))
									uniqueContributorsCount += count.getCount();
							}

							Map<String, String> totalCommits = new HashMap<>();
							totalCommits.put(TYPE, TOTALCOMMITS);
							Map<String, String> uniqueContributors = new HashMap<>();
							uniqueContributors.put(TYPE, UNIQUECONTRIBUTORS);

							MetricCount metricCountResponseTotalCommits = new MetricCount();
							metricCountResponseTotalCommits.setLabel(totalCommits);
							metricCountResponseTotalCommits.setValue(totalCommitsCount);
							countsProcessed.add(metricCountResponseTotalCommits);
							MetricCount metricCountResponseUniqueContributors = new MetricCount();
							metricCountResponseUniqueContributors.setLabel(uniqueContributors);
							metricCountResponseUniqueContributors.setValue(uniqueContributorsCount);
							countsProcessed.add(metricCountResponseUniqueContributors);

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
	 * processBuildingBlockMetrics
	 * 
	 * @return Boolean
	 */

	public Boolean processBuildingBlockMetrics() {
		LOG.info("Processing Stash Details : building_block_metrics . . . . ");
		try {
			Query query = new Query(new Criteria().where(METRICSNAME).is(STASH));
			List<String> appIds = mongoTemplate.getCollection(EXECUTIVEMETRICS).distinct(APPID, query.getQueryObject());
			if (!appIds.isEmpty()) {
				for (String appId : appIds) {
					BuildingBlocks buildingBlockMetricSummary = buildingBlocksRepository.findByMetricLevelIdAndMetricLevel(appId,
							MetricLevel.PRODUCT);
					if (buildingBlockMetricSummary == null)
						buildingBlockMetricSummary = new BuildingBlocks();
					ApplicationDetails appDetails = applicationDetailsRepository.findByAppId(appId);
					MetricsDetail metricDetailResponse = metricsDetailRepository.findByMetricLevelIdAndLevelAndType(appId,
							MetricLevel.PRODUCT, MetricType.STASH);
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
								if (!metricSummaryResponse.getName().equalsIgnoreCase(STASH))
									metricsResponseProcessed.add(metricSummaryResponse);
							}
						}
						metricsResponseProcessed.add(metricDetailResponse.getSummary());
						buildingBlockMetricSummary.setMetrics(metricsResponseProcessed);
						buildingBlocksRepository.save(buildingBlockMetricSummary);
					}
				}
			}
			LOG.info("Completed Stash Details : building_block_metrics . . . . ");
		} catch (Exception e) {
			LOG.info("processBuildingBlockMetrics Stash Analysis Info :: " + e);
		}
		return true;
	}

	/**
	 * processExecutiveDetailsMetrics
	 * 
	 * @return Boolean
	 */

	public Boolean processExecutiveDetailsMetrics() {

		LOG.info("Processing Stash Details : portfolio_metrics_details . . . . ");
		try {
			List<ExecutiveSummaryList> executiveSummaryLists = (List<ExecutiveSummaryList>) executiveSummaryListRepository
					.findAll();
			if (!executiveSummaryLists.isEmpty()) {
				for (ExecutiveSummaryList executiveSummaryList : executiveSummaryLists) {
					String eid = executiveSummaryList.getEid();
					MetricsDetail metricPortfolioDetailResponse = metricsDetailRepository.findByMetricLevelIdAndLevelAndType(eid,
							MetricLevel.PORTFOLIO, MetricType.STASH);
					if (metricPortfolioDetailResponse == null)
						metricPortfolioDetailResponse = new MetricsDetail();
					metricPortfolioDetailResponse.setType(MetricType.STASH);
					metricPortfolioDetailResponse.setLevel(MetricLevel.PORTFOLIO);
					metricPortfolioDetailResponse.setMetricLevelId(eid);
					metricPortfolioDetailResponse.setCustomField(getPortfolioId(eid));
					metricPortfolioDetailResponse.setSummary(processExecutiveSummary(executiveSummaryList.getAppId()));
					metricPortfolioDetailResponse
							.setTimeSeries(processExecutiveTimeSeries(executiveSummaryList.getAppId()));
					if (metricPortfolioDetailResponse.getSummary() != null) {
						metricPortfolioDetailResponse.getSummary()
								.setTotalComponents(executiveSummaryList.getTotalApps());
						metricPortfolioDetailResponse.getSummary()
								.setReportingComponents(getReportingCount(executiveSummaryList));
					}
					metricsDetailRepository.save(metricPortfolioDetailResponse);
				}
			}
		} catch (Exception e) {
			LOG.info("Error in processExecutiveDetailsMetrics() :: StashAnalysis Class " + e);
		}
		LOG.info("Completed Stash Details : portfolio_metrics_details . . . . ");
		return true;
	}

	/**
	 * processExecutiveSummary
	 * 
	 * @param configuredAppId
	 * @return MetricSummary
	 */

	private MetricSummary processExecutiveSummary(List<String> configuredAppId) {
		List<ExecutiveModuleMetrics> modules = new ArrayList<>();
		MetricSummary metricSummaryResponse = new MetricSummary();
		if (configuredAppId != null && !configuredAppId.isEmpty()) {
			for (String appId : configuredAppId) {
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
						STASH);
				if (executiveComponents != null) {
					List<ExecutiveMetrics> executiveMetricsList = executiveComponents.getMetrics();
					if (executiveMetricsList != null) {
						for (ExecutiveMetrics metric : executiveMetricsList) {
							if (metric.getMetricsName().equalsIgnoreCase(STASH)) {
								List<ExecutiveModuleMetrics> modulesList = metric.getModules();
								for (ExecutiveModuleMetrics executiveModuleMetrics : modulesList) {
									modules.add(executiveModuleMetrics);
								}
							}
						}
					}
				}
			}

			if (collectorStatusRepository.findByType(CollectorType.SCM) != null)
				metricSummaryResponse
						.setLastScanned(collectorStatusRepository.findByType(CollectorType.SCM).getLastUpdated());
			else
				metricSummaryResponse.setLastScanned(new Date(System.currentTimeMillis()));
			metricSummaryResponse.setLastUpdated(new Date(System.currentTimeMillis()));
			metricSummaryResponse.setCounts(processMetricSummaryCounts(modules));
			metricSummaryResponse.setTrendSlope(getTrendSlopesForModules(modules));
			metricSummaryResponse.setName(STASH);
			metricSummaryResponse.setDataAvailable(checkForDataAvailability(modules));
			return metricSummaryResponse;
		}
		return metricSummaryResponse;
	}

	/**
	 * processExecutiveTimeSeries
	 * 
	 * @param configuredAppId
	 * @return List<MetricTimeSeriesElement>
	 */

	private List<MetricTimeSeriesElement> processExecutiveTimeSeries(List<String> configuredAppId) {
		List<MetricTimeSeriesElement> metricTimeSeriesElementResponseList = new ArrayList<>();
		if (configuredAppId != null && !configuredAppId.isEmpty()) {
			List<ExecutiveModuleMetrics> modules = new ArrayList<>();
			List<String> teamIds = new ArrayList<>();
			for (String appId : configuredAppId) {
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
						STASH);
				if (executiveComponents != null) {
					List<ExecutiveMetrics> executiveMetricsList = executiveComponents.getMetrics();
					if (executiveMetricsList != null) {
						for (ExecutiveMetrics metric : executiveMetricsList) {
							if (metric.getMetricsName().equalsIgnoreCase(STASH)) {
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
							long totalCommitsCount = 0;
							long uniqueContributorsCount = 0;

							if (!seriesList.containsKey(days)) {
								List<SeriesCount> countList = executiveMetricsSeries.getCounts();
								List<MetricCount> metricCountResponseList = new ArrayList<>();
								for (SeriesCount count : countList) {
									if (null != count.getLabel()
											&& count.getLabel().get(TYPE).equalsIgnoreCase(TOTALCOMMITS))
										totalCommitsCount += count.getCount();
									if (null != count.getLabel()
											&& count.getLabel().get(TYPE).equalsIgnoreCase(UNIQUECONTRIBUTORS))
										uniqueContributorsCount += count.getCount();
								}

								Map<String, String> totalCommits = new HashMap<>();
								totalCommits.put(TYPE, TOTALCOMMITS);
								Map<String, String> uniqueContributors = new HashMap<>();
								uniqueContributors.put(TYPE, UNIQUECONTRIBUTORS);

								MetricCount metricCountResponseTotalCommits = new MetricCount();
								metricCountResponseTotalCommits.setLabel(totalCommits);
								metricCountResponseTotalCommits.setValue(totalCommitsCount);
								metricCountResponseList.add(metricCountResponseTotalCommits);
								MetricCount metricCountResponseUniqueContributors = new MetricCount();
								metricCountResponseUniqueContributors.setLabel(uniqueContributors);
								metricCountResponseUniqueContributors.setValue(uniqueContributorsCount);
								metricCountResponseList.add(metricCountResponseUniqueContributors);

								seriesList.put(days, metricCountResponseList);

							} else {
								List<SeriesCount> countList = executiveMetricsSeries.getCounts();
								List<MetricCount> countsProcessed = new ArrayList<>();
								List<MetricCount> counts = seriesList.get(days);
								for (MetricCount metricCountResponse : counts) {
									if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(TOTALCOMMITS))
										totalCommitsCount += metricCountResponse.getValue();
									if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(UNIQUECONTRIBUTORS))
										uniqueContributorsCount += metricCountResponse.getValue();
								}

								for (SeriesCount count : countList) {
									if (null != count.getLabel()
											&& count.getLabel().get(TYPE).equalsIgnoreCase(TOTALCOMMITS))
										totalCommitsCount += count.getCount();
									if (null != count.getLabel()
											&& count.getLabel().get(TYPE).equalsIgnoreCase(UNIQUECONTRIBUTORS))
										uniqueContributorsCount += count.getCount();
								}

								Map<String, String> totalCommits = new HashMap<>();
								totalCommits.put(TYPE, TOTALCOMMITS);
								Map<String, String> uniqueContributors = new HashMap<>();
								uniqueContributors.put(TYPE, UNIQUECONTRIBUTORS);

								MetricCount metricCountResponseTotalCommits = new MetricCount();
								metricCountResponseTotalCommits.setLabel(totalCommits);
								metricCountResponseTotalCommits.setValue(totalCommitsCount);
								countsProcessed.add(metricCountResponseTotalCommits);
								MetricCount metricCountResponseUniqueContributors = new MetricCount();
								metricCountResponseUniqueContributors.setLabel(uniqueContributors);
								metricCountResponseUniqueContributors.setValue(uniqueContributorsCount);
								countsProcessed.add(metricCountResponseUniqueContributors);

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
		return metricTimeSeriesElementResponseList;
	}

	/**
	 * processComponentDetailsMetrics
	 * 
	 * @return Boolean
	 */

	public Boolean processComponentDetailsMetrics() {
		LOG.info("Processing Stash Details : building_block_components . . . . ");
		Query query = new Query(new Criteria().where(METRICSNAME).is(STASH));
		List<String> appIds = mongoTemplate.getCollection(EXECUTIVEMETRICS).distinct(APPID, query.getQueryObject());
		if (!appIds.isEmpty()) {
			for (String appId : appIds) {
				ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId,
						STASH);
				List<BuildingBlocks> response = buildingBlocksRepository.findByMetricLevelIdAndMetricLevelAndMetricType(appId,
						MetricLevel.COMPONENT, MetricType.STASH);
				List<BuildingBlocks> buildingBlockResponse = new ArrayList<>();
				List<ExecutiveModuleMetrics> modules = executiveComponents.getMetrics().get(0).getModules();
				ApplicationDetails appDetails = applicationDetailsRepository.findByAppId(appId);
				if (!modules.isEmpty() && appDetails != null) {
					for (ExecutiveModuleMetrics module : modules) {
						if (!module.getModuleName().contains(PULLREQUESTDETAILS)) {
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
							summaryResponse.setMetricType(MetricType.STASH);
							buildingBlockResponse.add(summaryResponse);
						}
					}
				}
				if (response != null)
					buildingBlocksRepository.delete(response);
				buildingBlocksRepository.save(buildingBlockResponse);
			}
		}
		LOG.info("Completed Stash Details : building_block_components . . . . ");
		return true;
	}

	/**
	 * processComponentMetrics
	 * 
	 * @param module
	 * @return Boolean
	 */

	private List<MetricSummary> processComponentMetrics(ExecutiveModuleMetrics module) {
		List<MetricSummary> metricSummaryResponseList = new ArrayList<>();
		List<ExecutiveModuleMetrics> modules = new ArrayList<>();
		MetricSummary metricSummaryResponse = new MetricSummary();
		metricSummaryResponse.setName(STASH);
		metricSummaryResponse.setTotalComponents(1);
		metricSummaryResponse.setReportingComponents(1);
		metricSummaryResponse.setLastScanned(module.getLastScanned());
		metricSummaryResponse.setLastUpdated(module.getLastUpdated());
		modules.add(module);
		metricSummaryResponse.setCounts(processMetricSummaryCounts(modules));
		Boolean dataAvailabilityStatus = checkForDataAvailability(modules);
		metricSummaryResponse.setDataAvailable(dataAvailabilityStatus);
		if (!dataAvailabilityStatus)
			metricSummaryResponse.setConfMessage(MESSAGE);
		metricSummaryResponseList.add(metricSummaryResponse);
		return metricSummaryResponseList;
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
					if (null != count.getLabel() && count.getLabel().get(TYPE).equalsIgnoreCase(TOTALCOMMITS))
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
						if (executiveMetricsSeries.getCounts() != null) {
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
	 * @return ObjectId
	 */
	private String getPortfolioId(String eid) {
		PortfolioResponse portfolioResponse = portfolioResponseRepository.findByEid(eid);
		return portfolioResponse == null ? null : portfolioResponse.getId().toString();
	}

	/**
	 * checkForDataAvailability
	 * 
	 * @param modules
	 * @return true/false
	 */

	private Boolean checkForDataAvailability(List<ExecutiveModuleMetrics> modules) {
		int countCheck = 0;
		if (!modules.isEmpty()) {
			for (ExecutiveModuleMetrics module : modules) {
				if (!module.getModuleName().contains(PULLREQUESTDETAILS)) {
					List<ExecutiveMetricsSeries> execMetricSeries = module.getSeries();
					if (execMetricSeries != null && !execMetricSeries.isEmpty()) {
						countCheck++;
					}
				}
			}
		}
		if (countCheck > 0) {
			return true;
		}
		return false;
	}

	/**
	 *
	 * getLastCommitforModule
	 * 
	 * @param client
	 * @param repoDetails
	 * @return ExecutiveModuleMetrics
	 */

	private Date getLastCommitforModule(RepoDetails repoDetails, MongoClient client) {
		String repoUrl = repoDetails.getRepoUrl();
		String repoBranch = repoDetails.getRepoBranch();
		Commit commit = stashDAO.getTotalCommitsSorted(repoUrl, repoBranch, client);
		if (commit != null && commit.getScmCommitTimestamp() != 0) {
			Long timestamp = commit.getScmCommitTimestamp();
			Date returnDate = getISODateTime(timestamp);
			return returnDate;
		} else {
			return getISODateTime(System.currentTimeMillis());
		}
	}

	/**
	 * getDaysAgoValue
	 * 
	 * @param timeStamp
	 * @return int
	 */

	private int getDaysAgoValue(Long timeStamp) {
		Date dateFromDB = new Date(timeStamp);
		Date presentDate = new Date();
		long diff = presentDate.getTime() - dateFromDB.getTime();
		return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}

	/**
	 * getReportingCount
	 * 
	 * @param executiveSummaryList
	 * @return int
	 */

	private int getReportingCount(ExecutiveSummaryList executiveSummaryList) {
		int count = 0;
		List<String> appList = executiveSummaryList.getAppId();
		for (String appId : appList) {
			int countCheck = 0;
			ExecutiveComponents executiveComponents = executiveComponentRepository.findByAppIdAndMetric(appId, STASH);
			if (executiveComponents != null) {
				List<ExecutiveMetrics> executiveMetricsList = executiveComponents.getMetrics();
				if (executiveMetricsList != null) {
					for (ExecutiveMetrics metric : executiveMetricsList) {
						List<ExecutiveModuleMetrics> modulesList = metric.getModules();
						if (modulesList != null && !modulesList.isEmpty()) {
							for (ExecutiveModuleMetrics module : modulesList) {
								List<ExecutiveMetricsSeries> metricSeries = module.getSeries();
								if (metricSeries != null && !metricSeries.isEmpty()) {
									countCheck++;
								}
							}
						}
					}
				}
			}
			if (countCheck > 0)
				count++;
		}
		return count;
	}

}
