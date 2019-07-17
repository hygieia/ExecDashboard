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
import com.capitalone.dashboard.exec.model.FeatureUserStory;
import com.capitalone.dashboard.exec.model.JiraDetailsFinal;
import com.mongodb.MongoClient;

/**
 * QualityDetailsDAO
 * 
 * @param
 * @return
 * @author Hari
 */
@Component
public class QualityDetailsDAO {

	private static final Logger LOG = LoggerFactory.getLogger(QualityDetailsDAO.class);

	private static final String STEAMNAME = "sTeamName";
	private static final String PRODUCTION = "Production";
	private static final String PROD = "Prod";
	private static final String PRODTICKET = "PROD";
	private static final String CREATIONDATE = "creationDate";
	private static final String STYPENAME = "sTypeName";
	private static final String MENVIRONMENT = "mEnvironment";
	private static final String TESTPHASE = "testPhase";
	private static final String DSPENVIRONMENT = "dspEnvironment";
	private static final String SDLCENVIRONMENT = "sdlcEnvironment";
	private static final String BUG = "Bug";
	private static final String APPID = "appId";
	private static final String VAST = "vast";
	private static final String ISIT = "isIT";
	private static final String VASTAPPLNID = "vastApplID";
	private static final String CMIS = "cmis";
	private static final String JIRAFINALLIST = "jira_final_list";
	private static final String ENVIRONMENT = "environment";
	private static final String TESTENVIRONMENT = "testEnvironment";
	private static final String UPDATIONDATE = "changeDate";

	@Autowired
	MetricsProcessorConfig metricsProcessorConfig;

	/**
	 * getMongoClient()
	 * 
	 * @param
	 * @return MongoClient client
	 */

	public MongoClient getMongoClient() {
		MongoClient client = null;
		try {
			client = metricsProcessorConfig.mongo();
			return client;
		} catch (Exception e) {
			LOG.info("Error MongoClient file " + e);
		}
		return client;
	}

	/**
	 * getDefectsList()
	 * 
	 * @param projectName
	 * @param formattedDate
	 * @param client
	 * @return List<String> FeatureUserStory
	 */

	public List<FeatureUserStory> getDefectsList(String projectName, String formattedDate, MongoClient client) {
		List<FeatureUserStory> results = new ArrayList<>();
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(new Criteria().andOperator(Criteria.where(STEAMNAME).is(projectName),
					Criteria.where(CREATIONDATE).lte(formattedDate + "23:59:59.999Z")
							.gte(formattedDate + "00:00:00.000Z"),
					new Criteria().orOperator(Criteria.where(TESTPHASE).is(PRODUCTION),
							Criteria.where(MENVIRONMENT).is(PRODUCTION), Criteria.where(DSPENVIRONMENT).is(PROD),
							Criteria.where(SDLCENVIRONMENT).is(PROD), Criteria.where(ENVIRONMENT).is(PRODTICKET),
							Criteria.where(TESTENVIRONMENT).is("PRD")),
					Criteria.where(STYPENAME).is(BUG)));
			results = mongoOperations.find(basicQuery, FeatureUserStory.class);
			return results;
		} catch (Exception e) {
			LOG.info("Error in getDefectsList() (QualityDetailsDAO Class)" + e + client);
		}
		return results;

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
		MongoClient proxyClient = null;
		try {
			proxyClient = metricsProcessorConfig.mongo();
			Long startTimestamp = System.currentTimeMillis();
			Long endTimestamp = startTimestamp - 7776000000l;
			String startdate = formatteddate(startTimestamp);
			String enddate = formatteddate(endTimestamp);
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(proxyClient);
			Query basicQuery = new Query();
			basicQuery.addCriteria(new Criteria().andOperator(Criteria.where(STEAMNAME).is(projectName),
					Criteria.where(CREATIONDATE).gte(enddate).lte(startdate),
					new Criteria().orOperator(Criteria.where(TESTPHASE).is(PRODUCTION),
							Criteria.where(MENVIRONMENT).is(PRODUCTION), Criteria.where(DSPENVIRONMENT).is(PROD),
							Criteria.where(SDLCENVIRONMENT).is(PROD), Criteria.where(ENVIRONMENT).is(PRODTICKET),
							Criteria.where(TESTENVIRONMENT).is("PRD")),
					Criteria.where(STYPENAME).is(BUG)));
			basicQuery.with(new Sort(Sort.Direction.DESC, UPDATIONDATE));
			result = mongoOperations.findOne(basicQuery, FeatureUserStory.class);
			return result;

		} catch (Exception e) {
			LOG.info("Error in getLatestUserStorySorted() (QualityDetailsDAO Class)" + e);
		} finally {
			if (proxyClient != null)
				proxyClient.close();
		}
		return result;
	}

	/**
	 * formatteddate()
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
			LOG.info("Error in getMappingVastId() (VastDetailsDAO Class)" + e);
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
			return mongoOperations.find(basicQuery, JiraDetailsFinal.class);
		} catch (Exception e) {
			LOG.info("Error in getEntireProjectList() (VastDetailsDAO Class)" + e);
		}
		return result;
	}

	/**
	 * isDataAvailableForQuality()
	 * 
	 * checks whether data is available or not
	 * 
	 * @param projectName,
	 *            date, client
	 * @return Boolean true/false
	 */

	public Boolean isDataAvailableForQuality(String projectName, String formattedDate, MongoClient client) {

		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(new Criteria().andOperator(Criteria.where(STEAMNAME).is(projectName),
					Criteria.where(CREATIONDATE).gte(formattedDate + "00:00:00.000Z"),
					new Criteria().orOperator(Criteria.where(TESTPHASE).is(PRODUCTION),
							Criteria.where(MENVIRONMENT).is(PRODUCTION), Criteria.where(DSPENVIRONMENT).is(PROD),
							Criteria.where(SDLCENVIRONMENT).is(PROD), Criteria.where(ENVIRONMENT).is(PRODTICKET),
							Criteria.where(TESTENVIRONMENT).is("PRD")),
					Criteria.where(STYPENAME).is(BUG)));
			List<FeatureUserStory> result = mongoOperations.find(basicQuery, FeatureUserStory.class);
			if (result != null && !result.isEmpty()) {
				return true;
			}
		} catch (Exception e) {
			LOG.info("Error in isDataAvailableForQuality() (QualityDetailsDAO Class)" + e + client);
		}
		return false;

	}

	/**
	 * getJiraApps()
	 * 
	 * @param client
	 * @return
	 */

	public List<String> getJiraApps(MongoClient client) {
		List<String> appIdListJira = new ArrayList<>();
		MongoOperations mongoOperations;
		try {
			mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			appIdListJira = mongoOperations.getCollection(JIRAFINALLIST).distinct(APPID);
			List<String> appIdListCmis = mongoOperations.getCollection(CMIS).distinct(APPID);
			for (String appId : appIdListCmis) {
				if (!appIdListJira.contains(appId)) {
					appIdListJira.add(appId);
				}
			}
			return appIdListJira;
		} catch (Exception e) {
			LOG.info("Error in QualityDetails Dao get TotalList" + e);
		}
		return appIdListJira;
	}

}