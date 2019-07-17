package com.capitalone.dashboard.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.collector.MetricsProcessorConfig;
import com.capitalone.dashboard.exec.model.ServiceNowIssues;
import com.capitalone.dashboard.exec.model.ServiceNowTicket;
import com.capitalone.dashboard.exec.model.Vast;
import com.mongodb.MongoClient;

/**
 * Component ServiceNowDAO
 * 
 * @param MetricsProcessorConfig
 * @return
 */
@Component
public class ServiceNowDAO {

	private static final Logger LOG = LoggerFactory.getLogger(ServiceNowDAO.class);
	private static final String APPID = "appId";
	private static final String VASTAPPLNID = "vastApplID";
	private static final String APPLICATIONAME = "applicationName";
	private static final String APPSOFTWARE = "Application Software";
	private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final String CATEGORY = "category";
	private static final String FORMATONE = "yyyy-MM-dd";
	private static final String UTC = "UTC";
	private static final String SNTIMESTAMP = "aysCreatedTimeStamp";

	@Autowired
	MetricsProcessorConfig metricsProcessorConfig;

	/**
	 * getMongoClient()
	 * 
	 * @param
	 * @return MongoClient
	 */
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
	 * getMappingVastId()
	 * 
	 * @param client
	 * @return List<Vast>
	 */
	public List<Vast> getMappingVastId(MongoClient client) {

		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where(VASTAPPLNID).exists(true));
			List<Vast> results = mongoOperations.find(basicQuery, Vast.class);
			return results;
		} catch (Exception e) {
			LOG.info("Error in getMappingVastId() (ServiceNowDAO Class)" + e);
		}
		return null;

	}

	/**
	 * getServiceNowTickets()
	 * 
	 * @param appId
	 * @param client
	 * @return List<Vast>
	 */
	public List<ServiceNowTicket> getServiceNowTickets(String appId, MongoClient client) {

		try {
			MongoTemplate mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where(APPLICATIONAME).is(appId));
			basicQuery.addCriteria(Criteria.where(CATEGORY).is(APPSOFTWARE));
			List<ServiceNowTicket> results = mongoOperations.find(basicQuery, ServiceNowTicket.class);
			List<ServiceNowTicket> refined90daysTickets = results.stream()
					.filter(ticket -> getDate() <= convertStringToData(ticket.getStartdate()))
					.collect(Collectors.toList());
			List<ServiceNowTicket> processedTickets = new ArrayList<>();
			for (ServiceNowTicket sn : refined90daysTickets) {
				if (sn.getEnddate() != null && sn.getEnddate().length() > 10)
					processedTickets.add(sn);
			}
			Collections.sort(processedTickets, new Comparator<ServiceNowTicket>() {
				@Override
				public int compare(ServiceNowTicket s1, ServiceNowTicket s2) {
					return s1.getStartdate().compareTo(s2.getStartdate());
				}
			});
			return processedTickets;
		} catch (Exception e) {
			LOG.info("Error in getServiceNowTickets() (ServiceNowDAO Class)" + e);
		}
		return null;
	}

	/**
	 * getServiceNowTickets()
	 * 
	 * @param appIds
	 * @param client
	 * @return List<ServiceNowTicket>
	 */
	public List<ServiceNowTicket> getServiceNowTickets(List<String> appIds, MongoClient client) {

		List<ServiceNowTicket> processedTickets = new ArrayList<>();
		try {
			MongoTemplate mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where(APPLICATIONAME).in(appIds));
			basicQuery.addCriteria(Criteria.where(CATEGORY).is(APPSOFTWARE));
			List<ServiceNowTicket> results = mongoOperations.find(basicQuery, ServiceNowTicket.class);
			List<ServiceNowTicket> refined90daysTickets = results.stream()
					.filter(ticket -> getDate() <= convertStringToData(ticket.getStartdate()))
					.collect(Collectors.toList());
			for (ServiceNowTicket sn : refined90daysTickets) {
				if (sn.getEnddate() != null && sn.getEnddate().length() > 10)
					processedTickets.add(sn);
			}
			Collections.sort(processedTickets, (s1, s2) -> s1.getStartdate().compareTo(s2.getStartdate()));
		} catch (Exception e) {
			LOG.info("Error in getServiceNowTickets() (ServiceNowDAO Class)" + e);
		}
		return processedTickets;
	}

	/**
	 * convertStringToData()
	 * 
	 * @param strDate
	 * @return Long
	 */
	public Long convertStringToData(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat(FORMAT);
		try {
			Date date = formatter.parse(strDate);
			return date.getTime();
		} catch (Exception e) {
			LOG.info("Error in parsing date!" + e);
		}
		return (long) 0;
	}

	private Long getDate() {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(FORMATONE);
			Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(UTC));
			calendar.add(Calendar.DAY_OF_MONTH, -90);
			String strDate = DatatypeConverter.printDateTime(calendar).substring(0, 10);
			Date date = formatter.parse(strDate);
			return date.getTime();
		} catch (Exception e) {
			LOG.info("Error in getting 90 days date!" + e);
		}
		return (long) 0;
	}

	/**
	 * 
	 * @param appId
	 * @param startDate
	 * @param client
	 * @return
	 */

	public Boolean isDataAvailableForSN(String appId, Long startDate, MongoClient client) {
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(new Criteria().andOperator(Criteria.where(APPID).is(appId),
					Criteria.where(SNTIMESTAMP).gte(startDate)));
			List<ServiceNowIssues> result = mongoOperations.find(basicQuery, ServiceNowIssues.class);
			if (result != null && !result.isEmpty())
				return true;
		} catch (Exception e) {
			LOG.info("Error in isDataAvailableForSN() (ServiceNowDAO Class)" + e);
		}
		return false;
	}

	/**
	 * 
	 * @param appId
	 * @param startDate
	 * @param endDate
	 * @param client
	 * @return
	 */

	public List<ServiceNowIssues> getSNDefectsList(String appId, Long startDate, Long endDate, MongoClient client) {
		List<ServiceNowIssues> result = new ArrayList<>();
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(new Criteria().andOperator(Criteria.where(APPID).is(appId),
					Criteria.where(SNTIMESTAMP).gte(startDate).lt(endDate)));
			result = mongoOperations.find(basicQuery, ServiceNowIssues.class);
			if (result != null && !result.isEmpty())
				return result;
		} catch (Exception e) {
			LOG.info("Error in getSNDefectsList() (ServiceNowDAO Class)" + e);
		}
		return result;
	}

	/**
	 * 
	 * @param appId
	 * @param client
	 * @return
	 */

	public ServiceNowIssues getLatestServiceNowSorted(String appId) {
		MongoClient proxyClient = null;
		ServiceNowIssues result = new ServiceNowIssues();
		try {
			proxyClient = metricsProcessorConfig.mongo();
			Long timeStamp = System.currentTimeMillis();
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(proxyClient);
			Query basicQuery = new Query();
			basicQuery.addCriteria(new Criteria().andOperator(Criteria.where(APPID).is(appId),
					Criteria.where(SNTIMESTAMP).gte(timeStamp - 7776000000l)));
			basicQuery.with(new Sort(Sort.Direction.DESC, SNTIMESTAMP));
			result = mongoOperations.findOne(basicQuery, ServiceNowIssues.class);
			return result;
		} catch (Exception e) {
			LOG.info("Error in getLatestServiceNowSorted() (ServiceNowDAO Class)" + e);
		} finally {
			if (proxyClient != null)
				proxyClient.close();
		}
		return null;
	}

}
