package com.capitalone.dashboard.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.collector.MetricsProcessorConfig;
import com.capitalone.dashboard.exec.model.CloudCustodianAMI;
import com.capitalone.dashboard.exec.model.CloudCustodianEBS;
import com.capitalone.dashboard.exec.model.CloudCustodianEbsUnused;
import com.capitalone.dashboard.exec.model.CloudCustodianElbUnused;
import com.capitalone.dashboard.exec.model.CloudCustodianEniUnused;
import com.capitalone.dashboard.exec.model.CloudCustodianRdsAll;
import com.capitalone.dashboard.exec.model.CloudCustodianS3;
import com.capitalone.dashboard.exec.model.ELB;
import com.mongodb.MongoClient;

/**
 * @author raish4s Component CloudDAO
 * @param MetricsProcessorConfig
 * @return
 */
@Component
public class CloudDAO {

	private static final Logger LOG = LoggerFactory.getLogger(CloudDAO.class);
	private static final String ENVIRONMENT = "environment";
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
			LOG.error("Error MongoClient file " + e);
		}
		return null;
	}

	/**
	 * @param appId
	 * @param client
	 * @return
	 */
	public long getInstancesByAppId(String appId, MongoClient client) {
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where("amiVastAcronym").is(appId));
			List<CloudCustodianAMI> results = mongoOperations.find(basicQuery, CloudCustodianAMI.class);
			if (!results.isEmpty())
				return results.size();
		} catch (Exception e) {
			LOG.error("Error in getInstancesByAppId() (CloudDAO Class)" + e + appId);
		}
		return 0;
	}

	/**
	 * @param appId
	 * @param client
	 * @return
	 */
	public long getRDSByAppId(String appId, MongoClient client) {
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where("vsad").is(appId));
			List<CloudCustodianRdsAll> results = mongoOperations.find(basicQuery, CloudCustodianRdsAll.class);
			if (!results.isEmpty())
				return results.size();
		} catch (Exception e) {
			LOG.error("Error in getRDSByAppId() (CloudDAO Class)" + e + appId);
		}
		return 0;
	}

	/**
	 * @param appId
	 * @param client
	 * @param env
	 * @return
	 */
	public long getRDSByAppIdAndEnv(String appId, MongoClient client, String env) {
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where("vsad").is(appId));
			basicQuery.addCriteria(Criteria.where(ENVIRONMENT).is(env));
			List<CloudCustodianRdsAll> results = mongoOperations.find(basicQuery, CloudCustodianRdsAll.class);
			if (!results.isEmpty())
				return results.size();
		} catch (Exception e) {
			LOG.error("Error in getRDSByAppIdAndEnv() (CloudDAO Class)" + e + appId);
		}
		return 0;
	}

	/**
	 * @param appId
	 * @param client
	 * @return
	 */
	public long getEBSByAppId(String appId, MongoClient client) {
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where("ebsVastAcronym").is(appId));
			List<CloudCustodianEBS> results = mongoOperations.find(basicQuery, CloudCustodianEBS.class);
			if (!results.isEmpty())
				return results.size();
		} catch (Exception e) {
			LOG.error("Error in getEBSByAppId() (CloudDAO Class)" + e + appId);
		}
		return 0;
	}

	/**
	 * @param appId
	 * @param client
	 * @return
	 */
	public long getS3BucketsByAppId(String appId, MongoClient client) {
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where("s3VastAcronym").is(appId));
			List<CloudCustodianS3> results = mongoOperations.find(basicQuery, CloudCustodianS3.class);
			if (!results.isEmpty())
				return results.size();
		} catch (Exception e) {
			LOG.error("Error in getS3BucketsByAppId() (CloudDAO Class)" + e + appId);
		}
		return 0;
	}

	/**
	 * @param appId
	 * @param client
	 * @return
	 */
	public long getEncryptedEBSByAppId(String appId, MongoClient client) {
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where("ebsVastAcronym").is(appId));
			basicQuery.addCriteria(Criteria.where("ebsEncrypted").is("YES"));
			List<CloudCustodianEBS> results = mongoOperations.find(basicQuery, CloudCustodianEBS.class);
			if (!results.isEmpty())
				return results.size();
		} catch (Exception e) {
			LOG.error("Error in getEBSByAppId() (CloudDAO Class)" + e + appId);
		}
		return 0;
	}

	/**
	 * @param appId
	 * @param client
	 * @return
	 */
	public long getEncryptedS3BucketsByAppId(String appId, MongoClient client) {
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where("s3VastAcronym").is(appId));
			basicQuery.addCriteria(Criteria.where("s3Encrypted").is("YES"));
			List<CloudCustodianS3> results = mongoOperations.find(basicQuery, CloudCustodianS3.class);
			if (!results.isEmpty())
				return results.size();
		} catch (Exception e) {
			LOG.error("Error in getS3BucketsByAppId() (CloudDAO Class)" + e + appId);
		}
		return 0;
	}

	/**
	 * @param appId
	 * @param client
	 * @return
	 */
	public long getELBsByAppId(String appId, MongoClient client) {
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where("vastAcroymn").is(appId));
			List<ELB> results = mongoOperations.find(basicQuery, ELB.class);
			if (!results.isEmpty())
				return results.size();
		} catch (Exception e) {
			LOG.error("Error in getELBsByAppId() (CloudDAO Class)" + e + appId);
		}
		return 0;
	}

	/**
	 * @param appId
	 * @param client
	 * @return
	 */
	public long getUnusedEBsByAppId(String appId, MongoClient client) {
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where("vastAcroymn").is(appId));
			List<CloudCustodianEbsUnused> results = mongoOperations.find(basicQuery, CloudCustodianEbsUnused.class);
			if (!results.isEmpty())
				return results.size();
		} catch (Exception e) {
			LOG.error("Error in getUnusedEBsByAppId() (CloudDAO Class)" + e + appId);
		}
		return 0;
	}

	/**
	 * @param appId
	 * @param client
	 * @return
	 */
	public long getUnusedELBsByAppId(String appId, MongoClient client) {
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where("vastAcronym").is(appId));
			List<CloudCustodianElbUnused> results = mongoOperations.find(basicQuery, CloudCustodianElbUnused.class);
			if (!results.isEmpty())
				return results.size();
		} catch (Exception e) {
			LOG.error("Error in getUnusedEIPsByAppId() (CloudDAO Class)" + e + appId);
		}
		return 0;
	}

	/**
	 * @param appId
	 * @param client
	 * @return
	 */
	public long getUnusedENIsByAppId(String appId, MongoClient client) {
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where("vastAcronym").is(appId));
			List<CloudCustodianEniUnused> results = mongoOperations.find(basicQuery, CloudCustodianEniUnused.class);
			if (!results.isEmpty())
				return results.size();
		} catch (Exception e) {
			LOG.error("Error in getUnusedENIsByAppId() (CloudDAO Class)" + e + appId);
		}
		return 0;
	}

	/**
	 * @param appId
	 * @param env
	 * @param client
	 * @return
	 */
	public long getInstancesByAppIdAndEnv(String appId, MongoClient client, String env) {
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where("amiVastAcronym").is(appId));
			basicQuery.addCriteria(Criteria.where(ENVIRONMENT).is(env));
			List<CloudCustodianAMI> results = mongoOperations.find(basicQuery, CloudCustodianAMI.class);
			if (!results.isEmpty())
				return results.size();
		} catch (Exception e) {
			LOG.error("Error in getInstancesByAppIdAndEnv() (CloudDAO Class)" + e + appId);
		}
		return 0;
	}

	/**
	 * @param appId
	 * @param env
	 * @param client
	 * @return
	 */
	public long getEBSByAppIdAndEnv(String appId, MongoClient client, String env) {
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where("ebsVastAcronym").is(appId));
			basicQuery.addCriteria(Criteria.where(ENVIRONMENT).is(env));
			List<CloudCustodianEBS> results = mongoOperations.find(basicQuery, CloudCustodianEBS.class);
			if (!results.isEmpty())
				return results.size();
		} catch (Exception e) {
			LOG.error("Error in getEBSByAppId() (CloudDAO Class)" + e + appId);
		}
		return 0;
	}

	/**
	 * @param appId
	 * @param env
	 * @param client
	 * @return
	 */
	public long getS3BucketsByAppIdAndEnv(String appId, MongoClient client, String env) {
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where("s3VastAcronym").is(appId));
			basicQuery.addCriteria(Criteria.where(ENVIRONMENT).is(env));
			List<CloudCustodianS3> results = mongoOperations.find(basicQuery, CloudCustodianS3.class);
			if (!results.isEmpty())
				return results.size();
		} catch (Exception e) {
			LOG.error("Error in getS3BucketsByAppId() (CloudDAO Class)" + e + appId);
		}
		return 0;
	}

	/**
	 * @param appId
	 * @param env
	 * @param client
	 * @return
	 */
	public long getEncryptedEBSByAppIdAndEnv(String appId, MongoClient client, String env) {
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where("ebsVastAcronym").is(appId));
			basicQuery.addCriteria(Criteria.where(ENVIRONMENT).is(env));
			basicQuery.addCriteria(Criteria.where("ebsEncrypted").is("YES"));
			List<CloudCustodianEBS> results = mongoOperations.find(basicQuery, CloudCustodianEBS.class);
			if (!results.isEmpty())
				return results.size();
		} catch (Exception e) {
			LOG.error("Error in getEBSByAppId() (CloudDAO Class)" + e + appId);
		}
		return 0;
	}

	/**
	 * @param appId
	 * @param env
	 * @param client
	 * @return
	 */
	public long getEncryptedS3BucketsByAppIdAndEnv(String appId, MongoClient client, String env) {
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where("s3VastAcronym").is(appId));
			basicQuery.addCriteria(Criteria.where("s3Encrypted").is("YES"));
			basicQuery.addCriteria(Criteria.where(ENVIRONMENT).is(env));
			List<CloudCustodianS3> results = mongoOperations.find(basicQuery, CloudCustodianS3.class);
			if (!results.isEmpty())
				return results.size();
		} catch (Exception e) {
			LOG.error("Error in getS3BucketsByAppId() (CloudDAO Class)" + e + appId);
		}
		return 0;
	}

	/**
	 * @param appId
	 * @param env
	 * @param client
	 * @return
	 */
	public long getELBsByAppIdAndEnv(String appId, MongoClient client, String env) {
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where("vastAcroymn").is(appId));
			basicQuery.addCriteria(Criteria.where(ENVIRONMENT).is(env));
			List<ELB> results = mongoOperations.find(basicQuery, ELB.class);
			if (!results.isEmpty())
				return results.size();
		} catch (Exception e) {
			LOG.error("Error in getELBsByAppId() (CloudDAO Class)" + e + appId);
		}
		return 0;
	}

	/**
	 * @param appId
	 * @param env
	 * @param client
	 * @return
	 */
	public long getUnusedEBsByAppIdAndEnv(String appId, MongoClient client, String env) {
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where("vastAcroymn").is(appId));
			basicQuery.addCriteria(Criteria.where(ENVIRONMENT).is(env));
			List<CloudCustodianEbsUnused> results = mongoOperations.find(basicQuery, CloudCustodianEbsUnused.class);
			if (!results.isEmpty())
				return results.size();
		} catch (Exception e) {
			LOG.error("Error in getUnusedEBsByAppId() (CloudDAO Class)" + e + appId);
		}
		return 0;
	}

	/**
	 * @param appId
	 * @param env
	 * @param client
	 * @return
	 */
	public long getUnusedELBsByAppIdAndEnv(String appId, MongoClient client, String env) {
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where("vastAcronym").is(appId));
			basicQuery.addCriteria(Criteria.where(ENVIRONMENT).is(env));
			List<CloudCustodianElbUnused> results = mongoOperations.find(basicQuery, CloudCustodianElbUnused.class);
			if (!results.isEmpty())
				return results.size();
		} catch (Exception e) {
			LOG.error("Error in getUnusedEIPsByAppId() (CloudDAO Class)" + e + appId);
		}
		return 0;
	}

	/**
	 * @param appId
	 * @param env
	 * @param client
	 * @return
	 */
	public long getUnusedENIsByAppIdAndEnv(String appId, MongoClient client, String env) {
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where("vastAcronym").is(appId));
			basicQuery.addCriteria(Criteria.where(ENVIRONMENT).is(env));
			List<CloudCustodianEniUnused> results = mongoOperations.find(basicQuery, CloudCustodianEniUnused.class);
			if (!results.isEmpty())
				return results.size();
		} catch (Exception e) {
			LOG.error("Error in getUnusedENIsByAppId() (CloudDAO Class)" + e + appId);
		}
		return 0;
	}

	/**
	 * getEntireAppList()
	 * 
	 * @param client
	 * @return List<String> appIds
	 */
	@SuppressWarnings("unchecked")
	public List<String> getEntireAppList(MongoClient client) {
		List<String> results = new ArrayList<>();
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where("isIT").is(true));
			results = mongoOperations.getCollection("vast").distinct("vastApplID", basicQuery.getQueryObject());
		} catch (Exception e) {
			LOG.error("Error in getMappingVastId() (VastDetailsDAO Class)" + e);
		}
		return results;
	}
}
