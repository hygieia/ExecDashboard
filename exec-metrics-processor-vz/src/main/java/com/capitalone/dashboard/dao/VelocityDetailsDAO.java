package com.capitalone.dashboard.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
import com.capitalone.dashboard.exec.model.vz.FeatureUserStory;
import com.capitalone.dashboard.exec.model.vz.JiraDetailsFinal;
import com.mongodb.MongoClient;

/**
 * VelocityDetailsDAO
 * 
 * @param
 * @return
 * @author pranav
 */
@Component
public class VelocityDetailsDAO {

	private static final Logger LOG = LoggerFactory.getLogger(VelocityDetailsDAO.class);
	private static final String STEAMNAME = "sTeamName";
	private static final String CREATIONDATE = "creationDate";
	private static final String STYPENAME = "sTypeName";
	private static final String VZAGILESTORY = "VZAgile Story";
	private static final String STORY = "Story";
	private static final String ENHANCEMENT = "Enhancement";
	private static final String NEWFEATURE = "New Feature";
	private static final String ISIT = "isIT";
	private static final String VAST = "vast";
	private static final String VASTAPPLNID = "vastApplID";
	private static final String APPID = "appId";
	private static final String JIRAFINALLIST = "jira_final_list";
	private static final String STATUSCATEGORY = "statusCategory";
	private static final String DONE = "Done";
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
	 * getEntireAppList()
	 * 
	 * @param projectName
	 * @param startDate
	 * @param endDate
	 * @param client
	 * @return List<FeatureUserStory> userStoryList
	 */

	public List<FeatureUserStory> getUserStoriesList(String projectName, long startDate, long endDate,
			MongoClient client) {
		List<FeatureUserStory> result = new ArrayList<>();
		try {
			String startdate = formatteddate(startDate);
			String enddate = formatteddate(endDate);
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(new Criteria().andOperator(Criteria.where(STEAMNAME).is(projectName),
					Criteria.where(CREATIONDATE).gte(enddate).lte(startdate), Criteria.where(STATUSCATEGORY).is(DONE),
					Criteria.where(STYPENAME).in(VZAGILESTORY, STORY, ENHANCEMENT, NEWFEATURE)));
			result = mongoOperations.find(basicQuery, FeatureUserStory.class);
			return result;

		} catch (Exception e) {
			LOG.info("Error in getUSerStoriesList() (VelocityDetailsDAO Class)" + e);
		}
		return result;

	}

	/**
	 * getUserStoryList()
	 * 
	 * @param projectName
	 * @param startDate
	 * @param endDate
	 * @param client
	 * @return List<FeatureUserStory> userStoryList
	 */

	public List<FeatureUserStory> getUserStoryList(String projectName, long startDate, long endDate,
			MongoClient client) {
		List<FeatureUserStory> result = new ArrayList<>();
		try {
			String startdate = formatteddate(startDate);
			String enddate = formatteddate(endDate);
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(new Criteria().andOperator(Criteria.where(STEAMNAME).is(projectName),
					Criteria.where(CREATIONDATE).gte(enddate).lte(startdate),
					Criteria.where(STYPENAME).in(VZAGILESTORY, STORY)));
			result = mongoOperations.find(basicQuery, FeatureUserStory.class);
			return result;

		} catch (Exception e) {
			LOG.info("Error in getUSerStoriesList() (VelocityDetailsDAO Class)" + e);
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
					Criteria.where(STYPENAME).in(VZAGILESTORY, STORY, ENHANCEMENT, NEWFEATURE)));
			basicQuery.with(new Sort(Sort.Direction.DESC, UPDATIONDATE));
			result = mongoOperations.findOne(basicQuery, FeatureUserStory.class);

			return result;

		} catch (Exception e) {
			LOG.info("Error in getLatestUserStorySorted() (VelocityDetailsDAO Class)" + e);
		}
		return result;
	}

	/**
	 * formatteddate
	 * 
	 * @param long
	 *            date
	 * @return String date
	 */

	private String formatteddate(long date) {
		Calendar start = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		start.setTimeInMillis(date);
		String startformatted = DatatypeConverter.printDateTime(start);
		return startformatted;
	}

	/**
	 * getEntireAppList()
	 * 
	 * @param client
	 * @return List<String> appIds
	 */

	public List<String> getEntireAppList(MongoClient client) {
		List<String> results = new ArrayList<>();
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where(ISIT).is(true));
			results = mongoOperations.getCollection(VAST).distinct(VASTAPPLNID, basicQuery.getQueryObject());
		} catch (Exception e) {
			LOG.info("Error in getEntireAppList() (VastDetailsDAO Class)" + e);
		}
		return results;
	}

	/**
	 * getEntireProjectList()
	 * 
	 * @param client,
	 *            appId
	 * @return List<String> JiraDetailsFinal
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
			LOG.info("Error in getEntireProjectList() (VastDetailsDAO Class)" + e);
		}
		return result;
	}

	/**
	 * getTotalList()
	 * 
	 * @param client
	 * @return
	 */
	public List<String> getTotalList(MongoClient client) {
		List<String> results = new ArrayList<>();
		MongoOperations mongoOperations;
		try {
			mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			results = mongoOperations.getCollection(JIRAFINALLIST).distinct(APPID);
			return results;
		} catch (Exception e) {
			LOG.info("Error in velocityDetails Dao get TotalList" + e);
		}
		return results;
	}

}
