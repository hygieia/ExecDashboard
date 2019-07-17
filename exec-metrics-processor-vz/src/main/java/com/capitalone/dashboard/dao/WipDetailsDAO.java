package com.capitalone.dashboard.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.collector.MetricsProcessorConfig;
import com.capitalone.dashboard.exec.model.ExecutiveMetricsSeries;
import com.capitalone.dashboard.exec.model.ExecutiveModuleMetrics;
import com.capitalone.dashboard.exec.model.FeatureUserStory;
import com.capitalone.dashboard.exec.model.JiraDetailsFinal;
import com.capitalone.dashboard.exec.model.SeriesCount;
import com.capitalone.dashboard.utils.LinearRegression;
import com.mongodb.MongoClient;

/**
 * WipDetails
 * 
 * @param
 * @return
 * @author Guru
 */
@Component
@SuppressWarnings("PMD")
public class WipDetailsDAO {

	private static final Logger LOG = LoggerFactory.getLogger(WipDetailsDAO.class);
	private static final String TYPE = "type";
	private static final String STORY = "Story";
	private static final String OTHER = "Other";
	private static final String BUGS = "Bugs";
	private static final String EPIC = "Epic";
	private static final String APPID = "appId";
	private static final String VAST = "vast";
	private static final String ISIT = "isIT";
	private static final String VASTAPPLNID = "vastApplID";
	private static final String STEAMNAME = "sTeamName";
	private static final String CREATIONDATE = "creationDate";
	private static final String STATUSCATEGORY = "statusCategory";
	private static final String INPROGRESS = "In Progress";
	private static final String STYPENAME = "sTypeName";
	private static final String VZAGILESTORY = "VZAgile Story";
	private static final String BUG = "Bug";
	private static final String TASK = "Task";
	private static final String NEWFEATURE = "New Feature";
	private static final String ENHANCEMENT = "Enhancement";
	private static final String JIRAFINALLIST = "jira_final_list";
	private static final String UPDATIONDATE = "changeDate";

	@Autowired
	MetricsProcessorConfig metricsProcessorConfig;

	public MongoClient getMongoClient() {
		MongoClient client = null;
		try {
			client = metricsProcessorConfig.mongo();
			return client;
		} catch (Exception e) {
			LOG.info("Error MongoClient file " + e);
		}
		return null;
	}

	/**
	 * WipAnalysis
	 * 
	 * @param appId
	 * @param startDate
	 * @param endDate
	 * @param client
	 * @return
	 * @author Guru
	 */

	public List<FeatureUserStory> getUserStoriesList(String appId, long startDate, long endDate, MongoClient client) {

		List<FeatureUserStory> result = new ArrayList<>();
		try {
			String startdate = formatteddate(startDate);
			String enddate = formatteddate(endDate);
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();

			basicQuery.addCriteria(new Criteria().andOperator(Criteria.where(STEAMNAME).is(appId),
					Criteria.where(CREATIONDATE).gte(enddate).lte(startdate),
					Criteria.where(STATUSCATEGORY).is(INPROGRESS),
					Criteria.where(STYPENAME).in(VZAGILESTORY, STORY, BUG, TASK, EPIC, NEWFEATURE, ENHANCEMENT)));

			result = mongoOperations.find(basicQuery, FeatureUserStory.class);
			return result;

		} catch (Exception e) {
			LOG.info("Error in getUSerStoriesList() (WIPDetailsDAO Class)" + e);
		}
		return result;

	}

	/**
	 * getLatestUserStorySorted
	 * 
	 * @param projectName
	 * @param client
	 * @return FeatureUserStory
	 */

	public FeatureUserStory getLatestUserStorySorted(String projectName, MongoClient client) {
		FeatureUserStory result = new FeatureUserStory();
		try {
			Long startTimestamp = System.currentTimeMillis();
			Long endTimestamp = startTimestamp - 7776000000l;
			String startdate = formatteddate(startTimestamp);
			String enddate = formatteddate(endTimestamp);
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(new Criteria().andOperator(Criteria.where(STEAMNAME).is(projectName),
					Criteria.where(CREATIONDATE).gte(enddate).lte(startdate),

					Criteria.where(STYPENAME).in(VZAGILESTORY, STORY, BUG, TASK, EPIC, NEWFEATURE, ENHANCEMENT)));
			basicQuery.with(new Sort(Sort.Direction.DESC, UPDATIONDATE));
			result = mongoOperations.findOne(basicQuery, FeatureUserStory.class);
			return result;

		} catch (Exception e) {
			LOG.info("Error in getLatestUserStorySorted() (WIPDetailsDAO Class)" + e);
		}
		return result;
	}

	/**
	 * WipAnalysis
	 * 
	 * @param appId
	 * @param client
	 * @return
	 * @author Guru
	 */
	public List<JiraDetailsFinal> getEntireProjectList(MongoClient client, String appId) {
		List<JiraDetailsFinal> result = new ArrayList<>();
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where(APPID).is(appId));
			result = mongoOperations.find(basicQuery, JiraDetailsFinal.class);
			return result;
		} catch (Exception e) {
			LOG.info("Error in getEntireProjectList() (WIPDetailsDAO Class)" + e);
		}
		return result;
	}

	private String formatteddate(long date) {
		Calendar start = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		start.setTimeInMillis(date);
		String startformatted = DatatypeConverter.printDateTime(start);
		return startformatted;
	}

	/**
	 * WipAnalysis
	 * 
	 * @param client
	 * @return
	 * @author Guru
	 */
	public List<String> getEntireAppList(MongoClient client) {
		List<String> results = new ArrayList<>();
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);

			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where(ISIT).is(true));

			results = mongoOperations.getCollection(VAST).distinct(VASTAPPLNID, basicQuery.getQueryObject());
		} catch (Exception e) {
			LOG.info("Error in getMappingVastId() (VastDetailsDAO Class)" + e);
		}
		return results;
	}

	/**
	 * WipAnalysis
	 * 
	 * @param modules
	 * @return
	 * @author Guru
	 */

	public Double getTrendSlopesForModules(List<ExecutiveModuleMetrics> modules) {
		Map<Integer, Long> seriesList = new HashMap<>();
		if (!modules.isEmpty()) {
			for (ExecutiveModuleMetrics executiveModuleMetrics : modules) {
				List<ExecutiveMetricsSeries> executiveMetricsSeriesList = executiveModuleMetrics.getSeries();
				if (!executiveMetricsSeriesList.isEmpty()) {
					for (ExecutiveMetricsSeries executiveMetricsSeries : executiveMetricsSeriesList) {
						if (executiveMetricsSeries.getCounts() != null) {
							int days = executiveMetricsSeries.getDaysAgo();
							Long finalCount = (long) 0;

							if (executiveMetricsSeries.getCounts() != null
									&& !executiveMetricsSeries.getCounts().isEmpty()) {
								if (!seriesList.containsKey(days)) {
									List<SeriesCount> countList = executiveMetricsSeries.getCounts();
									for (SeriesCount count : countList) {
										if (count.getLabel().get(TYPE).equalsIgnoreCase(STORY))
											finalCount += count.getCount();
										if (count.getLabel().get(TYPE).equalsIgnoreCase(BUGS))
											finalCount += count.getCount();
										if (count.getLabel().get(TYPE).equalsIgnoreCase(EPIC))
											finalCount += count.getCount();
										if (count.getLabel().get(TYPE).equalsIgnoreCase(OTHER))
											finalCount += count.getCount();
									}
									seriesList.put(days, finalCount);
								} else {
									List<SeriesCount> countList = executiveMetricsSeries.getCounts();
									finalCount += seriesList.get(days);

									for (SeriesCount count : countList) {
										if (count.getLabel().get(TYPE).equalsIgnoreCase(STORY))
											finalCount += count.getCount();
										if (count.getLabel().get(TYPE).equalsIgnoreCase(BUGS))
											finalCount += count.getCount();
										if (count.getLabel().get(TYPE).equalsIgnoreCase(EPIC))
											finalCount += count.getCount();
										if (count.getLabel().get(TYPE).equalsIgnoreCase(OTHER))
											finalCount += count.getCount();
									}

									seriesList.replace(days, finalCount);
								}
							}
						}
					}
				}
			}

			return getSlope(seriesList);
		}
		return (double) 0;
	}

	private double getSlope(Map<Integer, Long> seriesList) {
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
		return 0;
	}

	private Long getTimeStamp(int days) {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calendar.add(Calendar.DAY_OF_MONTH, -days);
		return calendar.getTimeInMillis();
	}

	/**
	 * WipAnalysis
	 * 
	 * @param client
	 * @return
	 * @author Guru
	 */
	public List<String> getTotalList(MongoClient client) {
		List<String> results = new ArrayList<>();
		MongoOperations mongoOperations;
		try {
			mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			results = mongoOperations.getCollection(JIRAFINALLIST).distinct(APPID);
			return results;
		} catch (Exception e) {
			LOG.info("Error in WipDetails Dao get TotalList" + e);
		}
		return results;
	}

}
