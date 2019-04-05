package com.capitalone.dashboard.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.collector.MetricsProcessorConfig;
import com.capitalone.dashboard.exec.model.vz.ExecutiveMetricsSeries;
import com.capitalone.dashboard.exec.model.vz.ExecutiveModuleMetrics;
import com.capitalone.dashboard.exec.model.vz.SeriesCount;
import com.capitalone.dashboard.exec.model.vz.TestMetrics;
import com.mongodb.MongoClient;

/**
 * 
 * @author asthaaa
 *
 */
@Component
public class TestMetricsDAO {

	private static final Logger LOG = LoggerFactory.getLogger(TestMetricsDAO.class);

	private static final String APPID = "appId";
	private static final String TIMESTAMP = "timestamp";

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
			basicQuery.addCriteria(Criteria.where("isIT").is(true));
			results = mongoOperations.getCollection("vast").distinct("vastApplID", basicQuery.getQueryObject());
			if (results != null) {
				return results;
			}

		} catch (Exception e) {
			LOG.info("Error in getEntireAppList() (BuildDetailsDAO Class)" + e);
		}
		return new ArrayList<>();
	}

	/**
	 * 
	 * @param client
	 * @param appId
	 * @param timeStamp
	 * @return
	 */
	public List<String> getListOfModules(MongoClient client, String appId, Long timeStamp) {
		List<String> result = new ArrayList();
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where(APPID).is(appId));
			basicQuery.addCriteria(Criteria.where(TIMESTAMP).gte(timeStamp));
			result = mongoOperations.getCollection("testmetrics").distinct("jobName", basicQuery.getQueryObject());
			return result;

		} catch (Exception e) {
			LOG.info("Error in getListOfModules() (TestMetricsDAO Class)" + e);
		}

		return result;
	}

	/**
	 * 
	 * @param appId
	 * @param jobName
	 * @param startDate
	 * @param endDate
	 * @param client
	 * @return
	 */
	public List<TestMetrics> getTestDetailsForModule(String appId, String jobName, Long startDate, Long endDate,
			MongoClient client) {
		List<TestMetrics> result = new ArrayList<>();
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(new Criteria().andOperator(Criteria.where(APPID).is(appId),
					Criteria.where("jobName").is(jobName), Criteria.where(TIMESTAMP).gte(startDate).lt(endDate)));
			result = mongoOperations.find(basicQuery, TestMetrics.class);
		} catch (Exception e) {
			LOG.info("Error in getTestDetailsForModule() (TestMetricsDAO Class)" + e);
		}
		return result;
	}

	/**
	 * 
	 * @return
	 */
	public long getLast90daystd() {
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
	 * 
	 * @param timeStamp
	 * @return
	 */
	public int getDaysAgoValue(Long timeStamp) {
		Date dateFromDB = new Date(timeStamp);
		Date presentDate = new Date();
		long diff = presentDate.getTime() - dateFromDB.getTime();
		return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}

	/**
	 * 
	 * @param days
	 * @return
	 */
	public Long getTimeStamp(int days) {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calendar.add(Calendar.DAY_OF_MONTH, -days);
		return calendar.getTimeInMillis();
	}

	/**
	 * 
	 * @param modules
	 * @return
	 */

	public String checkForDataAvailabilityStatus(List<ExecutiveModuleMetrics> modules) {
		if (modules.isEmpty()) {
			return "No Tests Executed";
		}
		return null;
	}

	/**
	 * 
	 * @param modules
	 * @return
	 */
	public Boolean checkForDataAvailability(List<ExecutiveModuleMetrics> modules) {
		if (!modules.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param modules
	 * @return
	 */
	public Integer getReporting(List<ExecutiveModuleMetrics> modules) {
		int count = 0;
		for (ExecutiveModuleMetrics executiveModuleMetrics : modules) {
			if (checkIfDataAvailableInModule(executiveModuleMetrics)) {
				count += 1;
			}
		}
		return count;
	}

	/**
	 * 
	 * @param executiveModuleMetrics
	 * @return
	 */
	public boolean checkIfDataAvailableInModule(ExecutiveModuleMetrics executiveModuleMetrics) {
		for (ExecutiveMetricsSeries series : executiveModuleMetrics.getSeries()) {
			for (SeriesCount counts : series.getCounts()) {
				if (counts.getCount() > 0l) {
					return true;
				}
			}
		}
		return false;
	}

}
