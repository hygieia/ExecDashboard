package com.capitalone.dashboard.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.capitalone.dashboard.collector.MetricsProcessorConfig;
import com.capitalone.dashboard.exec.model.Dashboard;
import com.capitalone.dashboard.exec.model.ProductPipelineData;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

/**
 * Component ThroughPutDAO
 * 
 * @param MetricsProcessorConfig
 * @return
 */
@Component
public class ThroughPutDAO {

	private static final Logger LOG = LoggerFactory.getLogger(ThroughPutDAO.class);

	private static final String APPID = "appId";
	private static final String DBNAME = "dbname";
	private static final String DBUSERNAME = "dbusername";

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
	 * getConfiguredAppIds()
	 * 
	 * @param MongoClient
	 * @return List<String> confAppIds
	 */
	public List<String> getConfiguredAppIds(MongoClient client) {
		List<String> confAppIds = new ArrayList<>();
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			List<String> results = mongoOperations.getCollection("product_view_data").distinct(APPID);
			if (results != null)
				return results;
		} catch (Exception e) {
			LOG.info("Error in getConfiguredAppIds() (SecurityCsDetailsDAO Class)" + e);
		}
		return confAppIds;
	}

	/**
	 * getDbDetails()
	 * 
	 * @param appId
	 * @param client
	 * @return Map<String,String>
	 */
	public Map<String, String> getDbDetails(String appId, MongoClient client) {
		Map<String, String> dbDetails = new HashMap<>();
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where(APPID).is(appId));
			Dashboard dashboard = mongoOperations.findOne(basicQuery, Dashboard.class);
			if (dashboard != null) {
				dbDetails.put(DBNAME, dashboard.getDbCredentials().getDbName());
				dbDetails.put("dbhost", dashboard.getDbCredentials().getHost());
				dbDetails.put("dbport", String.valueOf(dashboard.getDbCredentials().getPort()));
				dbDetails.put(DBUSERNAME, dashboard.getDbCredentials().getDbUserName());
				dbDetails.put("dbpassword", dashboard.getDbCredentials().getDbUserCredentials());
			}

		} catch (Exception e) {
			LOG.info("Error in getDbDetails() ThroughPutDAO Class : " + e);
		}
		return dbDetails;
	}

	/**
	 * createDbConnection()
	 * 
	 * @param dbDetails
	 * @return MongoClient
	 */
	public MongoClient createDbConnection(Map<String, String> dbDetails) {
		MongoClient client;
		ServerAddress serverAddr = new ServerAddress(dbDetails.get("dbhost"), Integer.valueOf(dbDetails.get("dbport")));
		LOG.info("Initializing Mongo Client server at: {}", serverAddr);
		if (StringUtils.isEmpty(dbDetails.get(DBUSERNAME))) {
			client = new MongoClient(serverAddr);
		} else {
			MongoCredential mongoCredential = MongoCredential.createScramSha1Credential(dbDetails.get(DBUSERNAME),
					dbDetails.get(DBNAME), dbDetails.get("dbpassword").toCharArray());
			client = new MongoClient(serverAddr, Collections.singletonList(mongoCredential));
		}
		LOG.info("Connecting to Mongo: {}", client);
		return client;
	}

	/**
	 * getRemainingModuleList()
	 * 
	 * @param dbDetails
	 * @return List<String>
	 */
	public List<String> getRemainingModuleList(Map<String, String> dbDetails) {
		List<String> remainingModuleList = new ArrayList<>();
		MongoClient mongoClient = new MongoClient();
		try {
			List<String> allModuleNames = new ArrayList<>();
			List<String> configuredModuleNames = new ArrayList<>();
			mongoClient = createDbConnection(dbDetails);

			DB db = mongoClient.getDB(dbDetails.get(DBNAME));
			DBCollection collection = db.getCollection("dashboards");
			BasicDBObject query = new BasicDBObject();
			query.put("type", "Product");

			DBCursor cursor = collection.find(query);

			while (cursor.hasNext()) {
				BasicDBObject obj = (BasicDBObject) cursor.next();
				List<BasicDBObject> widgets = (List<BasicDBObject>) obj.get("widgets");
				for (BasicDBObject widget : widgets) {
					BasicDBObject options = (BasicDBObject) widget.get("options");
					List<BasicDBObject> teams = (List<BasicDBObject>) options.get("teams");
					for (BasicDBObject team : teams) {
						configuredModuleNames.add(team.get("name").toString());// +"-"+
																				// team.get("customName"));
					}

				}
			}

			BasicDBObject query1 = new BasicDBObject();
			query1.put("type", "Team");

			DBCursor cursor1 = collection.find(query1);

			while (cursor1.hasNext()) {
				BasicDBObject obj = (BasicDBObject) cursor1.next();
				allModuleNames.add(obj.get("title").toString());
			}

			for (String module : allModuleNames) {
				if (!configuredModuleNames.contains(module)) {
					remainingModuleList.add(module);
				}
			}

		} catch (Exception e) {
			LOG.info("Error in getRemainingModuleList() ThroughPutDAO Class : " + e);

		} finally {
			mongoClient.close();
		}

		return remainingModuleList;
	}

	/**
	 * getByAppId(String appId)
	 * 
	 * @param appId
	 * @param MongoClient
	 * @return ProductPipelineData
	 */
	public ProductPipelineData getByAppId(String appId, MongoClient client) {
		try {
			MongoOperations mongoOperations = metricsProcessorConfig.metricsProcessorTemplate(client);
			Query basicQuery = new Query();
			basicQuery.addCriteria(Criteria.where(APPID).is(appId));
			ProductPipelineData results = mongoOperations.findOne(basicQuery, ProductPipelineData.class);
			if (results != null)
				return results;
		} catch (Exception e) {
			LOG.info("Error in getByAppId() (ThroughPutDAO Class)" + e + appId);
		}
		return null;
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
			results = mongoOperations.getCollection("vast").distinct("vastApplID");
		} catch (Exception e) {
			LOG.info("Error in getMappingVastId() (VastDetailsDAO Class)" + e);
		}
		return results;
	}
}