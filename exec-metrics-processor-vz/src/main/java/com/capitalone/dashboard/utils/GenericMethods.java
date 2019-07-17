package com.capitalone.dashboard.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.exec.model.ApplicationDetails;
import com.capitalone.dashboard.exec.model.ExecutiveMetricsSeries;
import com.capitalone.dashboard.exec.model.ExecutiveModuleMetrics;
import com.capitalone.dashboard.exec.model.SeriesCount;
import com.capitalone.dashboard.exec.repository.ApplicationDetailsRepository;
import com.capitalone.dashboard.exec.repository.CloudCostRepository;

/**
 * @author raish4s
 *
 */
/**
 * @author raish4s
 *
 */
@Component
@SuppressWarnings("PMD")
public class GenericMethods {

	private static final String COST = "cost";
	private static final String TYPE = "type";
	private static final String DATESTRING = "30-12-";
	private static final String DATEFORMAT = "dd-MM-yyyy";
	private final CloudCostRepository cloudCostRepository;
	private final MongoTemplate mongoTemplate;
	private final ApplicationDetailsRepository applicationDetailsRepository;
	private static final Logger LOG = LoggerFactory.getLogger(GenericMethods.class);

	/**
	 * @param cloudCostRepository
	 * @param applicationDetailsRepository
	 * @param mongoTemplate
	 */
	@Autowired
	public GenericMethods(CloudCostRepository cloudCostRepository, MongoTemplate mongoTemplate,
			ApplicationDetailsRepository applicationDetailsRepository) {
		this.cloudCostRepository = cloudCostRepository;
		this.mongoTemplate = mongoTemplate;
		this.applicationDetailsRepository = applicationDetailsRepository;
	}

	/**
	 * @param days
	 * @return
	 */
	public Long getTimeStamp(int days) {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calendar.add(Calendar.DAY_OF_MONTH, -days);
		return calendar.getTimeInMillis();
	}

	/**
	 * @param execMetricsSeriesList
	 * @return
	 */
	public Double getTrendSlope(List<ExecutiveMetricsSeries> execMetricsSeriesList) {
		Map<Long, Integer> mappedValue = new HashMap<>();
		if (execMetricsSeriesList != null && !execMetricsSeriesList.isEmpty()) {

			for (ExecutiveMetricsSeries seris : execMetricsSeriesList) {
				if (seris.getTimeStamp() != null) {
					long timestamp = seris.getTimeStamp();
					int counting = 0;
					List<SeriesCount> counts = seris.getCounts();
					if (counts != null) {
						for (SeriesCount count : counts) {
							counting += count.getCount();
						}
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
		return (double) 0;
	}

	/**
	 * @param appId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Boolean checkForDataAvailability(String appId) {
		List<String> appIds = mongoTemplate.getCollection("cloudCost").distinct("appId");
		return appIds.contains(appId);
	}

	/**
	 * @param modules
	 * @return
	 */
	public String checkForDataAvailabilityStatus(List<ExecutiveModuleMetrics> modules) {
		if (modules.isEmpty())
			return "Not Configured";
		return null;
	}

	/**
	 * @param modules
	 * @return
	 */
	public Double getTrendSlopesForModules(List<ExecutiveModuleMetrics> modules) {
		Map<Integer, Long> seriesTimeList = new HashMap<>();
		if (!modules.isEmpty()) {
			for (ExecutiveModuleMetrics executiveModuleMetrics : modules) {
				List<ExecutiveMetricsSeries> executiveMetricsSeriesList = executiveModuleMetrics.getSeries();
				if (!executiveMetricsSeriesList.isEmpty()) {
					for (ExecutiveMetricsSeries executiveMetricsSeries : executiveMetricsSeriesList) {
						int days = executiveMetricsSeries.getDaysAgo();
						long cost = 0;
						if (days == getLastMonth()) {
							List<SeriesCount> countList = executiveMetricsSeries.getCounts();
							for (SeriesCount count : countList) {
								if (count.getLabel().get(TYPE).equalsIgnoreCase(COST)) {
									cost += count.getCount();
								}
							}
							seriesTimeList.put(days, cost);
						}
					}
				}
			}
			if (!seriesTimeList.isEmpty()) {
				Map<Long, Long> mappedValue = new HashMap<>();
				for (Entry<Integer, Long> entry : seriesTimeList.entrySet()) {
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
	 * processAppCriticality()
	 * 
	 * @param appId
	 * @return criticalityStatus
	 */

	public String processAppCriticality(String appId) {
		ApplicationDetails appDetails = applicationDetailsRepository.findByAppId(appId);
		String criticalityStatus = null;
		if (appDetails != null) {
			criticalityStatus = appDetails.getAvailabilityStatus();
		}
		return criticalityStatus;
	}

	/**
	 * @param countList
	 * @return
	 */
	public Map<String, Long> processSeriesCount(List<SeriesCount> countList) {
		Map<String, Long> processedSeries = new HashMap<>();
		long costCount = 0;
		long encryptedEBSCount = 0;
		long unencryptedEBSCount = 0;
		long encryptedS3Count = 0;
		long unencryptedS3Count = 0;
		long migrationEnabledCount = 0;
		long costOptimizedCount = 0;
		long amiCount = 0;
		long elbCount = 0;
		long rdsCount = 0;
		long unusedElbCount = 0;
		long unusedEniCount = 0;
		long unusedEbsCount = 0;// Prod
		long prodEncryptedEBSCount = 0;
		long prodUnencryptedEBSCount = 0;
		long prodEncryptedS3Count = 0;
		long prodUnencryptedS3Count = 0;
		long prodMigrationEnabledCount = 0;
		long prodCostOptimizedCount = 0;
		long prodAmiCount = 0;
		long prodElbCount = 0;
		long prodRdsCount = 0;
		long prodUnusedElbCount = 0;
		long prodUnusedEniCount = 0;
		long prodUnusedEbsCount = 0;// nonprod
		long nonprodEncryptedEBSCount = 0;
		long nonprodUnencryptedEBSCount = 0;
		long nonprodEncryptedS3Count = 0;
		long nonprodUnencryptedS3Count = 0;
		long nonprodMigrationEnabledCount = 0;
		long nonprodCostOptimizedCount = 0;
		long nonprodAmiCount = 0;
		long nonprodElbCount = 0;
		long nonprodRdsCount = 0;
		long nonprodUnusedElbCount = 0;
		long nonprodUnusedEniCount = 0;
		long nonprodUnusedEbsCount = 0; // staging
		long stagingEncryptedEBSCount = 0;
		long stagingUnencryptedEBSCount = 0;
		long stagingEncryptedS3Count = 0;
		long stagingUnencryptedS3Count = 0;
		long stagingMigrationEnabledCount = 0;
		long stagingCostOptimizedCount = 0;
		long stagingAmiCount = 0;
		long stagingElbCount = 0;
		long stagingRdsCount = 0;
		long stagingUnusedElbCount = 0;
		long stagingUnusedEniCount = 0;
		long stagingUnusedEbsCount = 0;
		for (SeriesCount count : countList) {
			if ("cost".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				costCount += count.getCount();
			}
			if ("encryptedEBS".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				encryptedEBSCount += count.getCount();
			}
			if ("unencryptedEBS".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				unencryptedEBSCount += count.getCount();
			}
			if ("encryptedS3".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				encryptedS3Count += count.getCount();
			}
			if ("unencryptedS3".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				unencryptedS3Count += count.getCount();
			}
			if ("migrationEnabled".equalsIgnoreCase(count.getLabel().get(TYPE)) && count.getCount() > 0) {
				migrationEnabledCount++;
			}
			if ("costOptimized".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				costOptimizedCount += count.getCount();
			}
			if ("Total AMI Count".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				amiCount += count.getCount();
			}
			if ("Total ELB Count".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				elbCount += count.getCount();
			}
			if ("Total RDS Count".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				rdsCount += count.getCount();
			}
			if ("Total unusedElb".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				unusedElbCount += count.getCount();
			}
			if ("Total unusedEni".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				unusedEniCount += count.getCount();
			}
			if ("Total unusedEbs".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				unusedEbsCount += count.getCount();
			}
			if ("PROD encryptedEBS".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				prodEncryptedEBSCount += count.getCount();
			}
			if ("PROD unencryptedEBS".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				prodUnencryptedEBSCount += count.getCount();
			}
			if ("PROD encryptedS3".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				prodEncryptedS3Count += count.getCount();
			}
			if ("PROD unencryptedS3".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				prodUnencryptedS3Count += count.getCount();
			}
			if ("PROD migrationEnabled".equalsIgnoreCase(count.getLabel().get(TYPE)) && count.getCount() > 0) {
				prodMigrationEnabledCount++;
			}
			if ("PROD costOptimized".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				prodCostOptimizedCount += count.getCount();
			}
			if ("PROD AMI Count".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				prodAmiCount += count.getCount();
			}
			if ("PROD ELB Count".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				prodElbCount += count.getCount();
			}
			if ("PROD RDS Count".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				prodRdsCount += count.getCount();
			}
			if ("PROD unusedElb".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				prodUnusedElbCount += count.getCount();
			}
			if ("PROD unusedEni".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				prodUnusedEniCount += count.getCount();
			}
			if ("PROD unusedEbs".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				prodUnusedEbsCount += count.getCount();
			}
			if ("NONPROD encryptedEBS".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				nonprodEncryptedEBSCount += count.getCount();
			}
			if ("NONPROD unencryptedEBS".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				nonprodUnencryptedEBSCount += count.getCount();
			}
			if ("NONPROD encryptedS3".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				nonprodEncryptedS3Count += count.getCount();
			}
			if ("NONPROD unencryptedS3".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				nonprodUnencryptedS3Count += count.getCount();
			}
			if ("NONPROD migrationEnabled".equalsIgnoreCase(count.getLabel().get(TYPE)) && count.getCount() > 0) {
				nonprodMigrationEnabledCount++;
			}
			if ("NONPROD costOptimized".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				nonprodCostOptimizedCount += count.getCount();
			}
			if ("NONPROD AMI Count".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				nonprodAmiCount += count.getCount();
			}
			if ("NONPROD ELB Count".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				nonprodElbCount += count.getCount();
			}
			if ("NONPROD RDS Count".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				nonprodRdsCount += count.getCount();
			}
			if ("NONPROD unusedElb".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				nonprodUnusedElbCount += count.getCount();
			}
			if ("NONPROD unusedEni".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				nonprodUnusedEniCount += count.getCount();
			}
			if ("NONPROD unusedEbs".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				nonprodUnusedEbsCount += count.getCount();
			}
			if ("STAGING encryptedEBS".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				stagingEncryptedEBSCount += count.getCount();
			}
			if ("STAGING unencryptedEBS".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				stagingUnencryptedEBSCount += count.getCount();
			}
			if ("STAGING encryptedS3".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				stagingEncryptedS3Count += count.getCount();
			}
			if ("STAGING unencryptedS3".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				stagingUnencryptedS3Count += count.getCount();
			}
			if ("STAGING migrationEnabled".equalsIgnoreCase(count.getLabel().get(TYPE)) && count.getCount() > 0) {
				stagingMigrationEnabledCount++;
			}
			if ("STAGING costOptimized".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				stagingCostOptimizedCount += count.getCount();
			}
			if ("STAGING AMI Count".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				stagingAmiCount += count.getCount();
			}
			if ("STAGING ELB Count".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				stagingElbCount += count.getCount();
			}
			if ("STAGING RDS Count".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				stagingRdsCount += count.getCount();
			}
			if ("STAGING unusedElb".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				stagingUnusedElbCount += count.getCount();
			}
			if ("STAGING unusedEni".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				stagingUnusedEniCount += count.getCount();
			}
			if ("STAGING unusedEbs".equalsIgnoreCase(count.getLabel().get(TYPE))) {
				stagingUnusedEbsCount += count.getCount();
			}
		}
		processedSeries.put("costCount", costCount);
		processedSeries.put("encryptedEBSCount", encryptedEBSCount);
		processedSeries.put("unencryptedEBSCount", unencryptedEBSCount);
		processedSeries.put("encryptedS3Count", encryptedS3Count);
		processedSeries.put("unencryptedS3Count", unencryptedS3Count);
		processedSeries.put("migrationEnabledCount", migrationEnabledCount);
		processedSeries.put("costOptimizedCount", costOptimizedCount);
		processedSeries.put("amiCount", amiCount);
		processedSeries.put("elbCount", elbCount);
		processedSeries.put("rdsCount", rdsCount);
		processedSeries.put("unusedElbCount", unusedElbCount);
		processedSeries.put("unusedEniCount", unusedEniCount);
		processedSeries.put("unusedEbsCount", unusedEbsCount);

		processedSeries.put("prodEncryptedEBSCount", prodEncryptedEBSCount);
		processedSeries.put("prodUnencryptedEBSCount", prodUnencryptedEBSCount);
		processedSeries.put("prodEncryptedS3Count", prodEncryptedS3Count);
		processedSeries.put("prodUnencryptedS3Count", prodUnencryptedS3Count);
		processedSeries.put("prodMigrationEnabledCount", prodMigrationEnabledCount);
		processedSeries.put("prodCostOptimizedCount", prodCostOptimizedCount);
		processedSeries.put("prodAmiCount", prodAmiCount);
		processedSeries.put("prodElbCount", prodElbCount);
		processedSeries.put("prodRdsCount", prodRdsCount);
		processedSeries.put("prodUnusedElbCount", prodUnusedElbCount);
		processedSeries.put("prodUnusedEniCount", prodUnusedEniCount);
		processedSeries.put("prodUnusedEbsCount", prodUnusedEbsCount);

		processedSeries.put("nonprodEncryptedEBSCount", nonprodEncryptedEBSCount);
		processedSeries.put("nonprodUnencryptedEBSCount", nonprodUnencryptedEBSCount);
		processedSeries.put("nonprodEncryptedS3Count", nonprodEncryptedS3Count);
		processedSeries.put("nonprodUnencryptedS3Count", nonprodUnencryptedS3Count);
		processedSeries.put("nonprodMigrationEnabledCount", nonprodMigrationEnabledCount);
		processedSeries.put("nonprodCostOptimizedCount", nonprodCostOptimizedCount);
		processedSeries.put("nonprodAmiCount", nonprodAmiCount);
		processedSeries.put("nonprodElbCount", nonprodElbCount);
		processedSeries.put("nonprodRdsCount", nonprodRdsCount);
		processedSeries.put("nonprodUnusedElbCount", nonprodUnusedElbCount);
		processedSeries.put("nonprodUnusedEniCount", nonprodUnusedEniCount);
		processedSeries.put("nonprodUnusedEbsCount", nonprodUnusedEbsCount);

		processedSeries.put("stagingEncryptedEBSCount", stagingEncryptedEBSCount);
		processedSeries.put("stagingUnencryptedEBSCount", stagingUnencryptedEBSCount);
		processedSeries.put("stagingEncryptedS3Count", stagingEncryptedS3Count);
		processedSeries.put("stagingUnencryptedS3Count", stagingUnencryptedS3Count);
		processedSeries.put("stagingMigrationEnabledCount", stagingMigrationEnabledCount);
		processedSeries.put("stagingCostOptimizedCount", stagingCostOptimizedCount);
		processedSeries.put("stagingAmiCount", stagingAmiCount);
		processedSeries.put("stagingElbCount", stagingElbCount);
		processedSeries.put("stagingRdsCount", stagingRdsCount);
		processedSeries.put("stagingUnusedElbCount", stagingUnusedElbCount);
		processedSeries.put("stagingUnusedEniCount", stagingUnusedEniCount);
		processedSeries.put("stagingUnusedEbsCount", stagingUnusedEbsCount);
		return processedSeries;
	}

	/**
	 * @param modules
	 * @return
	 */
	public Map<String, Long> processExecSeriesCount(List<ExecutiveModuleMetrics> modules) {
		Map<String, Long> processedSeries = new HashMap<>();
		long costCount = 0;
		long encryptedEBSCount = 0;
		long unencryptedEBSCount = 0;
		long encryptedS3Count = 0;
		long unencryptedS3Count = 0;
		long migrationEnabledCount = 0;
		long costOptimizedCount = 0;
		long amiCount = 0;
		long elbCount = 0;
		long rdsCount = 0;
		long unusedElbCount = 0;
		long unusedEniCount = 0;
		long unusedEbsCount = 0;// Prod
		long prodEncryptedEBSCount = 0;
		long prodUnencryptedEBSCount = 0;
		long prodEncryptedS3Count = 0;
		long prodUnencryptedS3Count = 0;
		long prodMigrationEnabledCount = 0;
		long prodCostOptimizedCount = 0;
		long prodAmiCount = 0;
		long prodElbCount = 0;
		long prodRdsCount = 0;
		long prodUnusedElbCount = 0;
		long prodUnusedEniCount = 0;
		long prodUnusedEbsCount = 0;// nonprod
		long nonprodEncryptedEBSCount = 0;
		long nonprodUnencryptedEBSCount = 0;
		long nonprodEncryptedS3Count = 0;
		long nonprodUnencryptedS3Count = 0;
		long nonprodMigrationEnabledCount = 0;
		long nonprodCostOptimizedCount = 0;
		long nonprodAmiCount = 0;
		long nonprodElbCount = 0;
		long nonprodRdsCount = 0;
		long nonprodUnusedElbCount = 0;
		long nonprodUnusedEniCount = 0;
		long nonprodUnusedEbsCount = 0; // staging
		long stagingEncryptedEBSCount = 0;
		long stagingUnencryptedEBSCount = 0;
		long stagingEncryptedS3Count = 0;
		long stagingUnencryptedS3Count = 0;
		long stagingMigrationEnabledCount = 0;
		long stagingCostOptimizedCount = 0;
		long stagingAmiCount = 0;
		long stagingElbCount = 0;
		long stagingRdsCount = 0;
		long stagingUnusedElbCount = 0;
		long stagingUnusedEniCount = 0;
		long stagingUnusedEbsCount = 0;
		for (ExecutiveModuleMetrics executiveModuleMetrics : modules) {
			List<ExecutiveMetricsSeries> executiveMetricsSeriesList = executiveModuleMetrics.getSeries();
			if (!executiveMetricsSeriesList.isEmpty()) {
				for (ExecutiveMetricsSeries executiveMetricsSeries : executiveMetricsSeriesList) {
					if (executiveMetricsSeries.getDaysAgo() == getLastMonth()) {
						List<SeriesCount> countList = executiveMetricsSeries.getCounts();
						if (!countList.isEmpty()) {
							for (SeriesCount count : countList) {
								if ("cost".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									costCount += count.getCount();
								}
								if ("encryptedEBS".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									encryptedEBSCount += count.getCount();
								}
								if ("unencryptedEBS".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									unencryptedEBSCount += count.getCount();
								}
								if ("encryptedS3".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									encryptedS3Count += count.getCount();
								}
								if ("unencryptedS3".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									unencryptedS3Count += count.getCount();
								}
								if ("migrationEnabled".equalsIgnoreCase(count.getLabel().get(TYPE))
										&& count.getCount() > 0) {
									migrationEnabledCount++;
								}
								if ("costOptimized".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									costOptimizedCount += count.getCount();
								}
								if ("Total AMI Count".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									amiCount += count.getCount();
								}
								if ("Total ELB Count".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									elbCount += count.getCount();
								}
								if ("Total RDS Count".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									rdsCount += count.getCount();
								}
								if ("Total unusedElb".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									unusedElbCount += count.getCount();
								}
								if ("Total unusedEni".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									unusedEniCount += count.getCount();
								}
								if ("Total unusedEbs".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									unusedEbsCount += count.getCount();
								}
								if ("PROD encryptedEBS".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									prodEncryptedEBSCount += count.getCount();
								}
								if ("PROD unencryptedEBS".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									prodUnencryptedEBSCount += count.getCount();
								}
								if ("PROD encryptedS3".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									prodEncryptedS3Count += count.getCount();
								}
								if ("PROD unencryptedS3".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									prodUnencryptedS3Count += count.getCount();
								}
								if ("PROD migrationEnabled".equalsIgnoreCase(count.getLabel().get(TYPE))
										&& count.getCount() > 0) {
									prodMigrationEnabledCount++;
								}
								if ("PROD costOptimized".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									prodCostOptimizedCount += count.getCount();
								}
								if ("PROD AMI Count".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									prodAmiCount += count.getCount();
								}
								if ("PROD ELB Count".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									prodElbCount += count.getCount();
								}
								if ("PROD RDS Count".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									prodRdsCount += count.getCount();
								}
								if ("PROD unusedElb".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									prodUnusedElbCount += count.getCount();
								}
								if ("PROD unusedEni".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									prodUnusedEniCount += count.getCount();
								}
								if ("PROD unusedEbs".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									prodUnusedEbsCount += count.getCount();
								}
								if ("NONPROD encryptedEBS".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									nonprodEncryptedEBSCount += count.getCount();
								}
								if ("NONPROD unencryptedEBS".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									nonprodUnencryptedEBSCount += count.getCount();
								}
								if ("NONPROD encryptedS3".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									nonprodEncryptedS3Count += count.getCount();
								}
								if ("NONPROD unencryptedS3".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									nonprodUnencryptedS3Count += count.getCount();
								}
								if ("NONPROD migrationEnabled".equalsIgnoreCase(count.getLabel().get(TYPE))
										&& count.getCount() > 0) {
									nonprodMigrationEnabledCount++;
								}
								if ("NONPROD costOptimized".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									nonprodCostOptimizedCount += count.getCount();
								}
								if ("NONPROD AMI Count".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									nonprodAmiCount += count.getCount();
								}
								if ("NONPROD ELB Count".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									nonprodElbCount += count.getCount();
								}
								if ("NONPROD RDS Count".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									nonprodRdsCount += count.getCount();
								}
								if ("NONPROD unusedElb".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									nonprodUnusedElbCount += count.getCount();
								}
								if ("NONPROD unusedEni".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									nonprodUnusedEniCount += count.getCount();
								}
								if ("NONPROD unusedEbs".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									nonprodUnusedEbsCount += count.getCount();
								}
								if ("STAGING encryptedEBS".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									stagingEncryptedEBSCount += count.getCount();
								}
								if ("STAGING unencryptedEBS".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									stagingUnencryptedEBSCount += count.getCount();
								}
								if ("STAGING encryptedS3".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									stagingEncryptedS3Count += count.getCount();
								}
								if ("STAGING unencryptedS3".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									stagingUnencryptedS3Count += count.getCount();
								}
								if ("STAGING migrationEnabled".equalsIgnoreCase(count.getLabel().get(TYPE))
										&& count.getCount() > 0) {
									stagingMigrationEnabledCount++;
								}
								if ("STAGING costOptimized".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									stagingCostOptimizedCount += count.getCount();
								}
								if ("STAGING AMI Count".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									stagingAmiCount += count.getCount();
								}
								if ("STAGING ELB Count".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									stagingElbCount += count.getCount();
								}
								if ("STAGING RDS Count".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									stagingRdsCount += count.getCount();
								}
								if ("STAGING unusedElb".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									stagingUnusedElbCount += count.getCount();
								}
								if ("STAGING unusedEni".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									stagingUnusedEniCount += count.getCount();
								}
								if ("STAGING unusedEbs".equalsIgnoreCase(count.getLabel().get(TYPE))) {
									stagingUnusedEbsCount += count.getCount();
								}
							}
						}
					}
				}
			}
		}
		processedSeries.put("costCount", costCount);
		processedSeries.put("encryptedEBSCount", encryptedEBSCount);
		processedSeries.put("unencryptedEBSCount", unencryptedEBSCount);
		processedSeries.put("encryptedS3Count", encryptedS3Count);
		processedSeries.put("unencryptedS3Count", unencryptedS3Count);
		processedSeries.put("migrationEnabledCount", migrationEnabledCount);
		processedSeries.put("costOptimizedCount", costOptimizedCount);
		processedSeries.put("amiCount", amiCount);
		processedSeries.put("elbCount", elbCount);
		processedSeries.put("rdsCount", rdsCount);
		processedSeries.put("unusedElbCount", unusedElbCount);
		processedSeries.put("unusedEniCount", unusedEniCount);
		processedSeries.put("unusedEbsCount", unusedEbsCount);

		processedSeries.put("prodEncryptedEBSCount", prodEncryptedEBSCount);
		processedSeries.put("prodUnencryptedEBSCount", prodUnencryptedEBSCount);
		processedSeries.put("prodEncryptedS3Count", prodEncryptedS3Count);
		processedSeries.put("prodUnencryptedS3Count", prodUnencryptedS3Count);
		processedSeries.put("prodMigrationEnabledCount", prodMigrationEnabledCount);
		processedSeries.put("prodCostOptimizedCount", prodCostOptimizedCount);
		processedSeries.put("prodAmiCount", prodAmiCount);
		processedSeries.put("prodElbCount", prodElbCount);
		processedSeries.put("prodRdsCount", prodRdsCount);
		processedSeries.put("prodUnusedElbCount", prodUnusedElbCount);
		processedSeries.put("prodUnusedEniCount", prodUnusedEniCount);
		processedSeries.put("prodUnusedEbsCount", prodUnusedEbsCount);

		processedSeries.put("nonprodEncryptedEBSCount", nonprodEncryptedEBSCount);
		processedSeries.put("nonprodUnencryptedEBSCount", nonprodUnencryptedEBSCount);
		processedSeries.put("nonprodEncryptedS3Count", nonprodEncryptedS3Count);
		processedSeries.put("nonprodUnencryptedS3Count", nonprodUnencryptedS3Count);
		processedSeries.put("nonprodMigrationEnabledCount", nonprodMigrationEnabledCount);
		processedSeries.put("nonprodCostOptimizedCount", nonprodCostOptimizedCount);
		processedSeries.put("nonprodAmiCount", nonprodAmiCount);
		processedSeries.put("nonprodElbCount", nonprodElbCount);
		processedSeries.put("nonprodRdsCount", nonprodRdsCount);
		processedSeries.put("nonprodUnusedElbCount", nonprodUnusedElbCount);
		processedSeries.put("nonprodUnusedEniCount", nonprodUnusedEniCount);
		processedSeries.put("nonprodUnusedEbsCount", nonprodUnusedEbsCount);

		processedSeries.put("stagingEncryptedEBSCount", stagingEncryptedEBSCount);
		processedSeries.put("stagingUnencryptedEBSCount", stagingUnencryptedEBSCount);
		processedSeries.put("stagingEncryptedS3Count", stagingEncryptedS3Count);
		processedSeries.put("stagingUnencryptedS3Count", stagingUnencryptedS3Count);
		processedSeries.put("stagingMigrationEnabledCount", stagingMigrationEnabledCount);
		processedSeries.put("stagingCostOptimizedCount", stagingCostOptimizedCount);
		processedSeries.put("stagingAmiCount", stagingAmiCount);
		processedSeries.put("stagingElbCount", stagingElbCount);
		processedSeries.put("stagingRdsCount", stagingRdsCount);
		processedSeries.put("stagingUnusedElbCount", stagingUnusedElbCount);
		processedSeries.put("stagingUnusedEniCount", stagingUnusedEniCount);
		processedSeries.put("stagingUnusedEbsCount", stagingUnusedEbsCount);
		return processedSeries;
	}

	/**
	 * @param appIds
	 * @return
	 */
	public Integer getReportingCount(List<String> appIds) {
		int count = 0;
		for (String appId : appIds) {
			Calendar cal = Calendar.getInstance();
			String date = "30-0" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.YEAR);
			if (cal.get(Calendar.MONTH) == 0)
				date = DATESTRING + (cal.get(Calendar.YEAR) - 1);
			if (cal.get(Calendar.MONTH) > 9)
				date = "30-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.YEAR);
			if (cloudCostRepository.findByAppIdAndTime(appId, date) != null)
				count++;
		}
		return count;
	}

	/**
	 * @return
	 */
	public int getLastMonth() {
		int daysAgo = 0;
		try {
			Calendar cal = Calendar.getInstance();
			String lastmonth = "30-0" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.YEAR);
			if (cal.get(Calendar.MONTH) == 0)
				lastmonth = DATESTRING + (cal.get(Calendar.YEAR) - 1);
			if (cal.get(Calendar.MONTH) > 9)
				lastmonth = "30-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.YEAR);
			Date dateFromDB = new SimpleDateFormat(DATEFORMAT).parse(lastmonth);
			Date presentDate = new Date();
			long diff = presentDate.getTime() - dateFromDB.getTime();
			daysAgo = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
		} catch (ParseException e) {
			LOG.error("ERROR while parsing the date: GenericMethods.getLastMonth() : ", e);
		}
		return daysAgo;
	}
	
	
	/**
	 * 
	 * @return
	 */
	
	public long getLastMonthTimestamp() {
		try {
			Calendar cal = Calendar.getInstance();
			String lastmonth = "30-0" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.YEAR);
			if (cal.get(Calendar.MONTH) == 0)
				lastmonth = DATESTRING + (cal.get(Calendar.YEAR) - 1);
			if(cal.get(Calendar.MONTH) == 1)
				lastmonth = "28-0" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.YEAR);
			if (cal.get(Calendar.MONTH) > 9)
				lastmonth = "30-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.YEAR);
			Date lastMonthDate = new SimpleDateFormat(DATEFORMAT).parse(lastmonth);
			return lastMonthDate.getTime();
		} catch (ParseException e) {
			LOG.error("ERROR in GenericMethods.getLastMonthTimestamp() : ", e);
			return 0;
		}
		
	}


	/**
	 * @param date
	 * @return
	 */
	public int getDaysAgoValue(String date) {
		try {
			Date dateFromDB = new SimpleDateFormat(DATEFORMAT).parse(date);
			Date presentDate = new Date();
			long diff = presentDate.getTime() - dateFromDB.getTime();
			return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
		} catch (ParseException e) {
			LOG.error("ERROR while parsing the date: GenericMethods.getLastMonth() : ", e);
			return 0;
		}
	}
	
	/**
	 * 
	 * @return
	 */

	private Date yesterday() {
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	/**
	 * @return
	 */
	public String getYesterdayDateString() {
		DateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);
		return dateFormat.format(yesterday());
	}

	/**
	 * @param configuredAppId
	 * @return
	 */
	public Boolean checkForDataAvailabilityForExec(List<String> configuredAppId) {
		Boolean status = false;
		List<String> appIds = mongoTemplate.getCollection("cloudCost").distinct("appId");
		for (String appId : configuredAppId) {
			if (appIds.contains(appId))
				status = true;
		}
		return status;
	}
}
