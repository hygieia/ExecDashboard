package com.capitalone.dashboard.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.collector.MetricsProcessorConfig;
import com.capitalone.dashboard.exec.model.vz.DateWiseMetricsSeries;
import com.capitalone.dashboard.exec.model.vz.ExecutiveMetricsSeries;
import com.capitalone.dashboard.exec.model.vz.ExecutiveSecurityData;
import com.capitalone.dashboard.exec.model.vz.SeriesCount;
import com.capitalone.dashboard.exec.model.vz.Vast;
import com.capitalone.dashboard.utils.LinearRegression;
import com.mongodb.MongoClient;

/**
 * SecurityCsDetailsDAO
 * 
 * @param
 * @return
 * @author Guru
 */
@Component
@SuppressWarnings("PMD")
public class SecurityCsDetailsDAO {

	// GETS MONGO CLIENT
	private static final Logger LOG = LoggerFactory.getLogger(SecurityCsDetailsDAO.class);

	private static final String CODEMAJORSTRING = "codeMajor";
	private static final String CODECRITICALSTRING = "codeCritical";
	private static final String CODEBLOCKERSTRING = "codeBlocker";

	private static final String PORTMAJORSTRING = "portMajor";
	private static final String PORTCRITICALSTRING = "portCritical";
	private static final String PORTBLOCKERSTRING = "portBlocker";

	private static final String WEBMAJORSTRING = "webMajor";
	private static final String WEBCRITICALSTRING = "webCritical";
	private static final String WEBBLOCKERSTRING = "webBlocker";

	private static final String BLACKDUCKMAJORSTRING = "blackDuckMajor";
	private static final String BLACKDUCKCRITICALSTRING = "blackDuckCritical";
	private static final String BLACKDUCKBLOCKERSTRING = "blackDuckBlocker";

	private static final String CRITICALOVERDUE = "overdueCritical";
	private static final String MEDIUMOVERDUE = "overdueMajor";
	private static final String HIGHOVERDUE = "overdueBlocker";

	private static final String SECURITY = "security-violations";
	private static final String APPID = "appId";
	private static final String SEVERITY = "severity";

	@Autowired
	MetricsProcessorConfig metricsProcessorConfig;

	/**
	 * SecurityCsDetailsDAO getMongoClient()
	 * 
	 * @return
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
	 * SecurityCsDetailsDAO getSecurityData
	 * 
	 * @param appId
	 * @param client
	 * @param timeStamp
	 * @return
	 */
	public List<DateWiseMetricsSeries> getSecurityData(String appId, MongoClient client, Long timeStamp) {
		List<DateWiseMetricsSeries> seriesList = new ArrayList<>();
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where(APPID).is(appId));
			basicQuery.addCriteria(Criteria.where("timestamp").gt(timeStamp));

			List<ExecutiveSecurityData> securityDataList = mongoOperations.find(basicQuery,
					ExecutiveSecurityData.class);

			if (!securityDataList.isEmpty()) {

				for (ExecutiveSecurityData exeData : securityDataList) {

					DateWiseMetricsSeries series = new DateWiseMetricsSeries();

					List<SeriesCount> countList = new ArrayList<>();

					SeriesCount countCodeMajor = new SeriesCount();
					SeriesCount countCodeCritical = new SeriesCount();
					SeriesCount countCodeBlocker = new SeriesCount();

					SeriesCount countPortMajor = new SeriesCount();
					SeriesCount countPortCritical = new SeriesCount();
					SeriesCount countPortBlocker = new SeriesCount();

					SeriesCount countWebMajor = new SeriesCount();
					SeriesCount countWebCritical = new SeriesCount();
					SeriesCount countWebBlocker = new SeriesCount();

					SeriesCount countOverDueCritical = new SeriesCount();
					SeriesCount countOverDueHigh = new SeriesCount();
					SeriesCount countOverDueMedium = new SeriesCount();

					SeriesCount countBlackDuckCritical = new SeriesCount();
					SeriesCount countBlackDuckHigh = new SeriesCount();
					SeriesCount countBlackDuckMedium = new SeriesCount();

					Map<String, String> codeBlocker = new HashMap<>();
					codeBlocker.put(SEVERITY, CODEBLOCKERSTRING);

					Map<String, String> codeCritical = new HashMap<>();
					codeCritical.put(SEVERITY, CODECRITICALSTRING);

					Map<String, String> codeMajor = new HashMap<>();
					codeMajor.put(SEVERITY, CODEMAJORSTRING);

					Map<String, String> webBlocker = new HashMap<>();
					webBlocker.put(SEVERITY, WEBBLOCKERSTRING);

					Map<String, String> webCritical = new HashMap<>();
					webCritical.put(SEVERITY, WEBCRITICALSTRING);

					Map<String, String> webMajor = new HashMap<>();
					webMajor.put(SEVERITY, WEBMAJORSTRING);

					Map<String, String> blackDuckBlocker = new HashMap<>();
					blackDuckBlocker.put(SEVERITY, BLACKDUCKBLOCKERSTRING);

					Map<String, String> blackDuckCritical = new HashMap<>();
					blackDuckCritical.put(SEVERITY, BLACKDUCKCRITICALSTRING);

					Map<String, String> blackDuckMajor = new HashMap<>();
					blackDuckMajor.put(SEVERITY, BLACKDUCKMAJORSTRING);

					Map<String, String> portBlocker = new HashMap<>();
					portBlocker.put(SEVERITY, PORTBLOCKERSTRING);

					Map<String, String> portCritical = new HashMap<>();
					portCritical.put(SEVERITY, PORTCRITICALSTRING);

					Map<String, String> portMajor = new HashMap<>();
					portMajor.put(SEVERITY, PORTMAJORSTRING);

					Map<String, String> overdueCritical = new HashMap<>();
					overdueCritical.put(SEVERITY, CRITICALOVERDUE);

					Map<String, String> overdueHigh = new HashMap<>();
					overdueHigh.put(SEVERITY, HIGHOVERDUE);

					Map<String, String> overdueMedium = new HashMap<>();
					overdueMedium.put(SEVERITY, MEDIUMOVERDUE);

					countCodeMajor.setLabel(codeMajor);
					countCodeMajor.setCount((long) exeData.getScanMajorValue());
					countCodeCritical.setLabel(codeCritical);
					countCodeCritical.setCount((long) exeData.getScanCriticalValue());
					countCodeBlocker.setLabel(codeBlocker);
					countCodeBlocker.setCount((long) exeData.getScanBlockerValue());

					countPortMajor.setLabel(webMajor);
					countPortMajor.setCount((long) exeData.getWebMajorValue());
					countPortCritical.setLabel(webCritical);
					countPortCritical.setCount((long) exeData.getWebCriticalValue());
					countPortBlocker.setLabel(webBlocker);
					countPortBlocker.setCount((long) exeData.getWebBlockerValue());

					countWebMajor.setLabel(portMajor);
					countWebMajor.setCount((long) exeData.getVulnerMajorValue());
					countWebCritical.setLabel(portCritical);
					countWebCritical.setCount((long) exeData.getVulnerCriticalValue());
					countWebBlocker.setLabel(portBlocker);
					countWebBlocker.setCount((long) exeData.getVulnerBlockerValue());

					countBlackDuckMedium.setLabel(blackDuckMajor);
					countBlackDuckMedium.setCount((long) exeData.getBlackDuckMajorValue());
					countBlackDuckCritical.setLabel(blackDuckCritical);
					countBlackDuckCritical.setCount((long) exeData.getBlackDuckCriticalValue());
					countBlackDuckHigh.setLabel(blackDuckBlocker);
					countBlackDuckHigh.setCount((long) exeData.getBlackDuckBlockerValue());

					countOverDueCritical.setLabel(overdueCritical);
					countOverDueCritical.setCount((long) exeData.getTotalCriticalOverdue());
					countOverDueHigh.setLabel(overdueHigh);
					countOverDueHigh.setCount((long) exeData.getTotalHighOverdue());
					countOverDueMedium.setLabel(overdueMedium);
					countOverDueMedium.setCount((long) exeData.getTotalMediumOverdue());

					countList.add(countWebBlocker);
					countList.add(countWebCritical);
					countList.add(countWebMajor);

					countList.add(countPortBlocker);
					countList.add(countPortCritical);
					countList.add(countPortMajor);

					countList.add(countCodeBlocker);
					countList.add(countCodeCritical);
					countList.add(countCodeMajor);

					countList.add(countBlackDuckHigh);
					countList.add(countBlackDuckCritical);
					countList.add(countBlackDuckMedium);

					countList.add(countOverDueCritical);
					countList.add(countOverDueHigh);
					countList.add(countOverDueMedium);

					series.setAppId(appId);
					series.setMetricsName(SECURITY);
					series.setTimeStamp(exeData.getTimestamp());
					series.setModuleName(appId + " " + SECURITY + " Module");
					series.setDateValue(exeData.getDatewise());
					series.setCounts(countList);

					seriesList.add(series);

				}
			}

			return seriesList;
		} catch (Exception e) {
			LOG.info("Error inside Security Details CAO file " + e);
		}
		return seriesList;

	}

	/**
	 * SecurityCsDetailsDAO convertToDaysAgo
	 * 
	 * @param timeStamp
	 * @return
	 */
	public int convertToDaysAgo(Long timeStamp) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

			Date secondDate = new Date();
			String secDate = sdf.format(secondDate);

			String firdate = sdf.format(timeStamp);

			Date firstDate = sdf.parse(firdate);
			secondDate = sdf.parse(secDate);

			return (int) TimeUnit.DAYS.convert(Math.abs(secondDate.getTime() - firstDate.getTime()),
					TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			LOG.info("Error inside Security convertToDaysAgo CAO file " + e);
		}
		return 0;
	}

	/**
	 * SecurityCsDetailsDAO getConfiguredAppIds
	 * 
	 * @param client
	 * @return
	 */
	public List<String> getConfiguredAppIds(MongoClient client) {
		List<String> confAppIds = new ArrayList<>();

		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where(APPID).exists(true));
			List<String> results = mongoOperations.getCollection("security_formulated_data").distinct(APPID);
			if (results != null)
				return results;
		} catch (Exception e) {
			LOG.info("Error in getConfiguredAppIds() (SecurityCsDetailsDAO Class)" + e);
		}
		return confAppIds;
	}

	/**
	 * SecurityCsDetailsDAO getVastId
	 * 
	 * @param client
	 * @param appId
	 * @return
	 */
	public List<Vast> getVastId(MongoClient client, String appId) {
		List<Vast> result = null;
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where("vastApplID").is(appId));
			result = mongoOperations.find(basicQuery, Vast.class);
		} catch (Exception e) {
			LOG.info("Error in getVastId() (SecurityCsDetailsDAO Class)" + e);
		}
		return result;
	}

	/**
	 * SecurityCsDetailsDAO getTrendSlope
	 * 
	 * @param seriesList
	 * @return
	 */
	public double getTrendSlope(List<ExecutiveMetricsSeries> seriesList) {

		Map<Long, Integer> mappedValue = new HashMap<>();
		if (seriesList != null) {

			for (ExecutiveMetricsSeries seris : seriesList) {
				if (seris.getTimeStamp() != null) {
					long timestamp = seris.getTimeStamp();
					int counting = 0;
					List<SeriesCount> counts = seris.getCounts();

					for (SeriesCount count : counts) {
						counting += count.getCount();

					}
					mappedValue.put(timestamp, counting);
				}
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
		return 0;
	}

	/**
	 * SecurityCsDetailsDAO getISODateTime
	 * 
	 * @param lastScanned
	 * @return
	 */
	public Date getISODateTime(Long lastScanned) {
		if (lastScanned == null)
			return null;
		return new Date(lastScanned);
	}

}