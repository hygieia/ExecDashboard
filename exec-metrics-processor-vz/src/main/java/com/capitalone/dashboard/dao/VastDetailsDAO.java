package com.capitalone.dashboard.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.collector.MetricsProcessorConfig;
import com.capitalone.dashboard.exec.model.vz.Vast;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

/**
 * Component VastDetailsDAO
 * 
 * @param MetricsProcessorConfig
 * @return
 */
@Component
@SuppressWarnings("PMD")
public class VastDetailsDAO {

	private static final Logger LOG = LoggerFactory.getLogger(VastDetailsDAO.class);

	private static final String VAST = "vast";
	private static final String ISIT = "isIT";
	private static final String APPID = "vastApplID";
	private static final String VASTTTIERONE = "vastTierOneContactEid";
	private static final String VASTTTIERTWO = "vastTierTwoContactEid";
	private static final String VASTTTIERTHREE = "vastTierThreeContactEid";
	private static final String VASTTTIERFOUR = "vastTierFourContactEid";
	private static final String VASTTTIERFIVE = "vastTierFiveContactEid";
	private static final String VASTTTIERSIX = "vastTierSixContactEid";
	private static final String VASTTTIERSEVEN = "vastTierSevenContactEid";
	private static final String MTTR_ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm";
	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final String METRICSNAME = "metrics.metricsName";

	@Autowired
	MetricsProcessorConfig metricsProcessorConfig;
	@Autowired
	MongoTemplate mongoTemplate;

	/**
	 * getMongoClient
	 * 
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
	 * getMappingVastId
	 * 
	 * @param client
	 * @return List<Vast>
	 */
	public List<Vast> getMappingVastId(MongoClient client) {
		List<Vast> results = new ArrayList<>();
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where(ISIT).is(true));
			results = mongoOperations.find(basicQuery, Vast.class);
			return results;
		} catch (Exception e) {
			LOG.info("Error in getMappingVastId() (VastDetailsDAO Class)" + e);
		}
		return results;

	}

	/**
	 * getVastEids
	 * 
	 * @param client
	 * @param vastTier
	 * @return List<String>
	 */
	public List<String> getVastEids(MongoClient client, int vastTier) {
		List<String> result = new ArrayList<>();
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			String vastTagName;
			switch (vastTier) {
			case 1:
				vastTagName = VASTTTIERONE;
				break;
			case 2:
				vastTagName = VASTTTIERTWO;
				break;
			case 3:
				vastTagName = VASTTTIERTHREE;
				break;
			case 4:
				vastTagName = VASTTTIERFOUR;
				break;
			case 5:
				vastTagName = VASTTTIERFIVE;
				break;
			case 6:
				vastTagName = VASTTTIERSIX;
				break;
			case 7:
				vastTagName = VASTTTIERSEVEN;
				break;
			default:
				LOG.info("Unexpected Parameter in getVastEids : " + vastTier);
				vastTagName = "";
			}
			if (vastTagName.length() > 5) {
				Query basicQuery = new Query();
				basicQuery.addCriteria(Criteria.where(ISIT).is(true));
				result = mongoOperations.getCollection(VAST).distinct(vastTagName, basicQuery.getQueryObject());
				return result;
			}

		} catch (Exception e) {
			LOG.info("Error in getVastEids() (VastDetailsDAO Class)" + e);
		}
		return result;
	}

	/**
	 * getAllAppIds
	 * 
	 * @param client
	 * @return List<String>
	 */
	public List<String> getAllAppIds(MongoClient client) {
		List<String> result = new ArrayList<>();
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where(ISIT).is(true));
			result = mongoOperations.getCollection(VAST).distinct(APPID, basicQuery.getQueryObject());
			return result;
		} catch (Exception e) {
			LOG.info("Error in getAllAppIds() (VastDetailsDAO Class)" + e);
		}
		return result;

	}

	/**
	 * getVastEidsForEid
	 * 
	 * @param client
	 * @param vastTier
	 * @param eid
	 * @return List<String>
	 */
	public List<String> getVastEidsForEid(MongoClient client, int vastTier, String eid) {
		List<String> result = new ArrayList<>();
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			String vastTagName;
			String vastDirectReporteeName = "";
			switch (vastTier) {
			case 2:
				vastTagName = VASTTTIERONE;
				vastDirectReporteeName = VASTTTIERTWO;
				break;
			case 3:
				vastTagName = VASTTTIERTWO;
				vastDirectReporteeName = VASTTTIERTHREE;
				break;
			case 4:
				vastTagName = VASTTTIERTHREE;
				vastDirectReporteeName = VASTTTIERFOUR;
				break;
			case 5:
				vastTagName = VASTTTIERFOUR;
				vastDirectReporteeName = VASTTTIERFIVE;
				break;
			case 6:
				vastTagName = VASTTTIERFIVE;
				vastDirectReporteeName = VASTTTIERSIX;
				break;
			case 7:
				vastTagName = VASTTTIERSIX;
				vastDirectReporteeName = VASTTTIERSEVEN;
				break;
			default:
				LOG.info("Unexpected Parameter in getVastEidsForEid : " + vastTier);
				vastTagName = "";
			}
			if (vastTagName.length() > 5) {
				Query basicQuery = new Query();
				basicQuery.addCriteria(Criteria.where(ISIT).is(true));
				basicQuery.addCriteria(Criteria.where(vastTagName).is(eid));
				result = mongoOperations.getCollection(VAST).distinct(vastDirectReporteeName,
						basicQuery.getQueryObject());
				return result;
			}

		} catch (Exception e) {
			LOG.info("Error in getVastEidsForEid() (VastDetailsDAO Class)" + e);
		}
		return result;
	}

	/**
	 * getLastUpdatedDate()
	 * 
	 * @param collectionName
	 * @param timeStampField
	 * @param client
	 * @return Long
	 */
	public Long getLastUpdatedDate(String collectionName, String timeStampField, MongoClient client) {

		Long updatedDate = 0L;

		MongoOperations mongoOperations;
		try {
			mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);

			SortOperation sortOperation = Aggregation.sort(Sort.Direction.DESC, timeStampField);
			ProjectionOperation projectOperation = Aggregation.project(timeStampField).andExclude("_id");
			LimitOperation limitOperation = Aggregation.limit(1);
			Aggregation agg = Aggregation.newAggregation(sortOperation, projectOperation, limitOperation);

			AggregationResults<DBObject> temp = mongoOperations.aggregate(agg, collectionName, DBObject.class);
			String dateValue = temp.getUniqueMappedResult().get(timeStampField).toString();
			updatedDate = getLongDate(dateValue, collectionName);

		} catch (Exception e) {
			LOG.info("Error in getLastUpdatedDate() (VastDetailsDAO Class) in " + collectionName + e);
		}

		return updatedDate;
	}

	/**
	 * 
	 * @param isoString
	 * @return long
	 */
	public long fromMttrISODateTimeFormat(String isoString) {
		String iString = isoString;
		int charIndex = iString.indexOf('.');
		if (charIndex != -1) {
			iString = iString.substring(0, charIndex);
		}

		Date dt = new Date();

		try {
			dt = new SimpleDateFormat(MTTR_ISO_DATE_TIME_FORMAT).parse(iString);
		} catch (ParseException e) {
			LOG.error("Parsing MTTR ISO DateTime: " + isoString, e);
		}
		return dt.getTime();
	}

	private long fromDateToLong(String date) {
		String iString = date;
		int charIndex = iString.indexOf('.');
		if (charIndex != -1) {
			iString = iString.substring(0, charIndex);
		}
		Date dt = new Date();
		try {
			dt = new SimpleDateFormat(DATE_TIME_FORMAT).parse(iString);
		} catch (ParseException e) {
			LOG.error("Parsing ISO DateTime: " + date, e);
		}
		return dt.getTime();
	}

	private Long getLongDate(String timeStamp, String collectionName) {
		long updatedTimeStamp = 0;
		switch (collectionName) {
		case "mttr":
			updatedTimeStamp = fromMttrISODateTimeFormat(timeStamp);
			break;
		case "ebs":
			updatedTimeStamp = fromDateToLong(timeStamp);
			break;
		case "ami":
		case "s3":
		case "elb":
			updatedTimeStamp = fromDateToLong(timeStamp);
			break;
		default:
			updatedTimeStamp = Long.parseLong(timeStamp);
			break;
		}

		return updatedTimeStamp;
	}

	/**
	 * getLast90DaysTimeStamp
	 * 
	 * @return Long
	 */
	public Long getLast90DaysTimeStamp() {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
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
	 * getLast90DaysTimeStampWithFormat
	 * 
	 * @param format
	 * @return String
	 */
	public String getLast90DaysTimeStampWithFormat(String format) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat targetFormat = new SimpleDateFormat(format);
			Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			calendar.add(Calendar.DAY_OF_MONTH, -90);
			String strDate = DatatypeConverter.printDateTime(calendar).substring(0, 10);
			Date date = formatter.parse(strDate);
			return targetFormat.format(date);

		} catch (Exception e) {
			LOG.info("Error in getting 90 day :: Format :: " + format + "::: " + e);
		}
		return "";
	}

	/**
	 * getLastUpdatedAppCount
	 * 
	 * @param collectionName
	 * @param fieldName
	 * @param client
	 * @param appFieldName
	 * @return long
	 */
	public long getLastUpdatedAppCount(String collectionName, String fieldName, MongoClient client,
			String appFieldName) {
		MongoOperations mongoOperations;
		long appCount = 0;
		try {
			mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);

			Criteria criteria;
			switch (collectionName) {
			case "mttr":
				criteria = new Criteria().where(fieldName)
						.gte(getLast90DaysTimeStampWithFormat(MTTR_ISO_DATE_TIME_FORMAT));
				break;
			case "ebs":
			case "ami":
			case "s3":
			case "elb":
				criteria = new Criteria().where(fieldName).gte(getLast90DaysTimeStampWithFormat(DATE_TIME_FORMAT));
				break;
			default:
				criteria = new Criteria().where(fieldName).gte(getLast90DaysTimeStamp());
				break;
			}
			Query query = new Query();
			query.addCriteria(criteria);
			if (appFieldName.isEmpty()) {
				appCount = mongoOperations.getCollection(collectionName).count(query.getQueryObject());
			} else {
				appCount = mongoOperations.getCollection(collectionName).distinct(appFieldName, query.getQueryObject())
						.size();
			}
		} catch (Exception e) {
			LOG.info("Error in getLastUpdatedAppCount() (VastDetailsDAO Class) in " + collectionName + e);
		}

		return appCount;
	}

	/**
	 * 
	 * @param client
	 * @return List
	 */
	public List<Vast> getAllDetails(MongoClient client) {
		MongoOperations mongoOperations;
		try {
			mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);

			return mongoOperations.findAll(Vast.class);

		} catch (Exception e) {
			LOG.info("Error in getAllDetails() (DevopscupDAO Class)" + e);
		}
		return new ArrayList<>();
	}

	/**
	 * getAppCount
	 * 
	 * @param metricsName
	 * @return
	 */
	public int getAppCount(String metricsName) {
		Query basicQuery = new Query();
		Criteria criteria;
		try {
			switch (metricsName) {
			case "Security":
				criteria = new Criteria().where(METRICSNAME).is("security-violations");
				break;
			case "Through Put":
				criteria = new Criteria().where(METRICSNAME).is("pipeline-lead-time");
				break;
			case "Quality":
				criteria = new Criteria().where(METRICSNAME).is("quality");
				break;
			case "WIP":
				criteria = new Criteria().where(METRICSNAME).is("work-in-progress");
				break;
			case "Total Stories":
			case "Velocity":
				criteria = new Criteria().where(METRICSNAME).is("open-source-violations");
				break;
			case "Production Incidents":
				criteria = new Criteria().where(METRICSNAME).is("production-incidents");
				break;
			case "Stash":
				criteria = new Criteria().where(METRICSNAME).is("stash");
				break;
			case "Cloud":
				criteria = new Criteria().where(METRICSNAME).is("cloud");
				break;
			case "Test":
				criteria = new Criteria().where(METRICSNAME).is("test");
				break;
			case "Build":
				criteria = new Criteria().where(METRICSNAME).is("build");
				break;
			case "Deploy":
				criteria = new Criteria().where(METRICSNAME).is("deploy");
				break;
			case "Say Do":
				criteria = new Criteria().where(METRICSNAME).is("saydoratio");
				break;
			default:
				return 0;
			}
			basicQuery.addCriteria(criteria);
			return mongoTemplate.getCollection("executives_metrics").distinct("appId", basicQuery.getQueryObject())
					.size();
		} catch (Exception e) {
			LOG.info("Error in getAppCount() (VastDAO Class)" + e);
		}
		return 0;

	}

}
