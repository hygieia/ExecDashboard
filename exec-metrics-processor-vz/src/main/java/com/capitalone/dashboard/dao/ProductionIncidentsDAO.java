package com.capitalone.dashboard.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.collector.MetricsProcessorConfig;
import com.capitalone.dashboard.exec.model.vz.MTTR;
import com.capitalone.dashboard.exec.model.vz.OperationalMetrics;
import com.mongodb.MongoClient;

/**
 * 
 * @author ASTHAAA
 *
 */
@Component
@SuppressWarnings("PMD")
public class ProductionIncidentsDAO {

	private static final Logger LOG = LoggerFactory.getLogger(ProductionIncidentsDAO.class);
	private static final String APPID = "appId";
	private static final String SEVERITY1 = "SEV1";
	private static final String SEVERITY2 = "SEV2";

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
	 * 
	 * @param appId
	 * @param client
	 * @return
	 */
	public List<MTTR> getProductionIncidentsDataByAppId(String appId, MongoClient client) {

		List<MTTR> results = new ArrayList<>();
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where(APPID).is(appId));
			results = mongoOperations.find(basicQuery, MTTR.class);
		} catch (Exception e) {
			LOG.info("Error in getProductionIncidents Data() (ProductionIncidents DAO Class)" + e);
		}
		return results;

	}

	/**
	 * 
	 * @param appId
	 * @param client
	 * @param date
	 * @return
	 */
	public List<MTTR> getProductionIncidentsDataByAppIdByRegex(String appId, MongoClient client, String date) {

		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);

			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where(APPID).is(appId));
			basicQuery.addCriteria(Criteria.where("eventStartDT").gte(date));

			List<MTTR> results = mongoOperations.find(basicQuery, MTTR.class);
			return results;

		} catch (Exception e) {
			LOG.info("Error in getProductionIncidentsData() (ProductionIncidentsDAO Class)" + e);
		}
		return null;

	}

	/**
	 * getProcessedDetailsForExec
	 * 
	 * @param productionTotalEventsLabel
	 * @param crisisLevel
	 * @return
	 */
	public List<MTTR> getProcessedDetailsForExec(List<MTTR> productionTotalEventsLabel, String crisisLevel) {
		List<MTTR> processedDetailsForExec = new ArrayList<>();
		List<String> crisisIds = new ArrayList<>();
		List<MTTR> processedDetails = productionTotalEventsLabel.stream()
				.filter(c -> c.getCrisisLevel().equalsIgnoreCase(crisisLevel)).collect(Collectors.toList());
		for (MTTR mttr : processedDetails) {
			String crisisId = mttr.getCrisisId();
			if (!crisisIds.contains(crisisId)) {
				List<MTTR> respectiveCrisisList = productionTotalEventsLabel.stream()
						.filter(c -> c.getCrisisId().equalsIgnoreCase(crisisId)).collect(Collectors.toList());
				mttr.setAppIdList(getAppIds(respectiveCrisisList));
				processedDetailsForExec.add(mttr);
				crisisIds.add(crisisId);
			}
		}
		return processedDetailsForExec;
	}

	/**
	 * getOperationalMetrics
	 * 
	 * @param productionTotalEventsLabel
	 * @return
	 */
	public List<OperationalMetrics> getOperationalMetrics(List<MTTR> productionTotalEventsLabel) {
		Map<String, List<MTTR>> categorizedMTTR = new HashMap<>();
		List<OperationalMetrics> operationalMetricsList = new ArrayList<>();
		for (MTTR mttr : productionTotalEventsLabel) {
			String category = mttr.getCauseCode();
			if (!categorizedMTTR.containsKey(category)) {
				List<MTTR> mttrList = new ArrayList<>();
				mttrList.add(mttr);
				categorizedMTTR.put(category, mttrList);
			} else {
				List<MTTR> mttrList = categorizedMTTR.get(category);
				mttrList.add(mttr);
				categorizedMTTR.put(category, mttrList);
			}
		}

		if (!categorizedMTTR.isEmpty())
			return processOperationalMetrics(categorizedMTTR);

		return operationalMetricsList;
	}

	private List<OperationalMetrics> processOperationalMetrics(Map<String, List<MTTR>> categorizedMTTR) {
		List<OperationalMetrics> operationalMetricsList = new ArrayList<>();
		categorizedMTTR.forEach((category, mttrList) -> {
			OperationalMetrics operationalMetrics = new OperationalMetrics();
			operationalMetrics.setCategory(category);
			operationalMetrics = processMetricsFromMTTR(operationalMetrics, mttrList);
			operationalMetricsList.add(operationalMetrics);
		});
		return operationalMetricsList;
	}

	private OperationalMetrics processMetricsFromMTTR(OperationalMetrics operationalMetrics, List<MTTR> mttrList) {

		List<String> crisisIds = new ArrayList<>();
		List<String> impactedOrgs = new ArrayList<>();
		List<String> impactedApps = new ArrayList<>();
		Long outages = (long) 0;
		Long events = (long) 0;
		Long outageMttr = (long) 0;
		Long eventMttr = (long) 0;

		for (MTTR mttrDetails : mttrList) {
			String crisisId = mttrDetails.getCrisisId();
			if (!crisisIds.contains(crisisId)) {
				crisisIds.add(crisisId);
				outages += SEVERITY1.equalsIgnoreCase(mttrDetails.getCrisisLevel()) ? 1 : 0;
				outageMttr += SEVERITY1.equalsIgnoreCase(mttrDetails.getCrisisLevel()) ? mttrDetails.getItduration()
						: 0;
				events += SEVERITY2.equalsIgnoreCase(mttrDetails.getCrisisLevel()) ? 1 : 0;
				eventMttr += SEVERITY2.equalsIgnoreCase(mttrDetails.getCrisisLevel()) ? mttrDetails.getItduration() : 0;
			}
			String org = mttrDetails.getOwningEntity();
			String appId = mttrDetails.getAppId();
			if (!impactedOrgs.contains(org)) {
				impactedOrgs.add(org);
			}
			if (!impactedApps.contains(appId)) {
				impactedApps.add(appId);
			}
		}
		if (outages > 0) {
			outageMttr = outageMttr / outages;
		}
		if (events > 0) {
			eventMttr = eventMttr / events;
		}
		operationalMetrics.setImpactedApps(impactedApps);
		operationalMetrics.setImpactedOrgs(impactedOrgs);
		operationalMetrics.setEventMTTR(eventMttr);
		operationalMetrics.setEvents(events);
		operationalMetrics.setOutages(outages);
		operationalMetrics.setOutageMTTR(outageMttr);
		return operationalMetrics;
	}

	private List<String> getAppIds(List<MTTR> respectiveCrisisList) {
		List<String> appIds = new ArrayList<>();
		for (MTTR mttr : respectiveCrisisList) {
			String appId = mttr.getAppId();
			if (!appIds.contains(appId)) {
				appIds.add(appId);
			}
		}
		return appIds;
	}

	/**
	 * 
	 * @param client
	 * @return
	 */
	public List<String> getAppIdList(MongoClient client) {

		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);

			List<String> appIds = mongoOperations.getCollection("mttr").distinct(APPID);
			return appIds;

		} catch (Exception e) {
			LOG.info("Error in getAppIdList() (ProductionIncidentsDAO Class)" + e);
		}
		return null;
	}

	/**
	 * 
	 * @param client
	 * @return
	 */
	public List<String> getEntireAppList(MongoClient client) {

		List<String> results = new ArrayList<>();
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where("isIT").is(true));
			results = mongoOperations.getCollection("vast").distinct("vastApplID", basicQuery.getQueryObject());
		} catch (Exception e) {
			LOG.info("Error in getMappingVastId() (VastDetailsDAO Class)" + e);
		}
		return results;

	}

	private Long getTimeStamp(int days) {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calendar.add(Calendar.DAY_OF_MONTH, -days);
		return calendar.getTimeInMillis();
	}

	/**
	 * getMTBFDataforApp
	 * 
	 * @param appId
	 * @return long
	 */
	public long getMTBFDataforApp(String appId) {
		MongoClient client = getMongoClient();
		List<MTTR> results = new ArrayList<>();
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			long date = getTimeStamp(90);
			Date startDate = new Date(date);
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);

			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where(APPID).is(appId));
			basicQuery.addCriteria(Criteria.where("eventStartDT").gte(format.format(startDate)));
			results = mongoOperations.find(basicQuery, MTTR.class);

			List<Long> crisisDate = new ArrayList<>();
			List<String> crisisRecords = new ArrayList<>();
			long sumofDays = 0;

			crisisDate.add(date);

			if (results != null) {

				for (MTTR mttr : results) {
					Date d = format.parse(mttr.getEventStartDT());
					crisisDate.add(d.getTime());
					crisisRecords.add(mttr.getCrisisId());
				}

			}

			crisisDate.add(System.currentTimeMillis());
			Collections.sort(crisisDate, Collections.reverseOrder());
			for (int i = 0; i < crisisDate.size(); i++) {

				if (i < crisisDate.size() - 1) {
					long diffInMilis = crisisDate.get(i) - crisisDate.get(i + 1);
					long diffInDays = diffInMilis / (24 * 60 * 60 * 1000);
					sumofDays = sumofDays + diffInDays;
				}
			}

			return sumofDays / (crisisDate.size() - 1);

		} catch (Exception e) {
			LOG.error("Error in getProductionIncidentsData() (ProductionIncidentsDAO Class)" + e);
		} finally {
			if (client != null)
				client.close();
		}
		return 0;
	}

	/**
	 * getMTTRDetails
	 * 
	 * @param productionEvents
	 * @return List<MTTR>
	 */
	public List<MTTR> getMTTRDetails(List<String> productionEvents) {
		List<MTTR> results = new ArrayList<>();
		MongoClient client = getMongoClient();
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where("crisisId").in(productionEvents));
			results = mongoOperations.find(basicQuery, MTTR.class);
		} catch (Exception e) {
			LOG.error("Error in getMTTRDetails() (ProductionIncidentsDAO Class)" + e);
		} finally {
			if (client != null)
				client.close();
		}
		return results;
	}

}