package com.capitalone.dashboard.executive.service;

import com.capitalone.dashboard.exec.model.BunitCredentials;
import com.capitalone.dashboard.exec.model.Dashboard;
import com.capitalone.dashboard.exec.repository.DashboardRepository;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;

@Service
@SuppressWarnings("PMD")
public class DefaultHygieiaServiceImpl implements DefaultHygieiaService {
	private final DashboardRepository dashboardRepository;
	private static final Logger LOG = LoggerFactory.getLogger(DefaultHygieiaServiceImpl.class);
	private static final String USERNAME = "username";

	@Autowired
	public DefaultHygieiaServiceImpl(DashboardRepository dashboardRepository) {
		this.dashboardRepository = dashboardRepository;
	}

	@Override
	public List<String> getUsers(String appId) {
		List<Dashboard> dashboardList = null;
		try {
			dashboardList = dashboardRepository.findByappId(appId);

			if (!dashboardList.isEmpty()) {
				for (Dashboard dashboard : dashboardList) {

					BunitCredentials bunitCredential = dashboard.getDbCredentials();
					MongoClient mongoClient = getMongoClient(bunitCredential);
					if (null != mongoClient) {

						MongoDatabase mongoDB = getMongoDatabase(mongoClient, bunitCredential.getDbName());
						if (null != mongoDB) {
							MongoCollection<Document> users = mongoDB.getCollection("authentication");
							if (null != users) {
								MongoCursor<Document> iterator = users.find().iterator();
								List<String> list = new ArrayList<String>();
								while (iterator.hasNext()) {
									Document doc = iterator.next();
									if (doc.get(USERNAME) != null) {
										list.add(doc.get(USERNAME).toString());
									}
								}
								return list;
							}
						}
					}

				}
			}

		} catch (Exception e) {
			LOG.info(e.toString());
		}
		return null;

	}

	@Override
	public List<Map<String, String>> getUsersForAdmin(String appId) {
		List<Dashboard> dashboardList = null;
		List<Map<String, String>> list = new ArrayList<>();
		try {
			dashboardList = dashboardRepository.findByappId(appId);

			if (!dashboardList.isEmpty()) {
				for (Dashboard dashboard : dashboardList) {

					BunitCredentials bunitCredential = dashboard.getDbCredentials();
					MongoClient mongoClient = getMongoClient(bunitCredential);
					if (null != mongoClient) {
						list = new ArrayList<>();
						MongoDatabase mongoDB = getMongoDatabase(mongoClient, bunitCredential.getDbName());
						if (null != mongoDB) {
							MongoCollection<Document> users = mongoDB.getCollection("user_info");
							if (null != users) {
								MongoCursor<Document> iterator = users.find().iterator();

								while (iterator.hasNext()) {
									Document doc = iterator.next();
									Map<String, String> usersMap = new HashMap<>();
									usersMap.put("userName", doc.get(USERNAME).toString());
									usersMap.put("displayName", doc.get("displayName").toString());
									if ("[ROLE_ADMIN]".equalsIgnoreCase(doc.get("authorities").toString())) {
										usersMap.put("authority", "Admin");
									} else {
										usersMap.put("authority", "User");
									}

									list.add(usersMap);
								}

								return list;
							}
						}
					}

				}
			}

		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
		return list;

	}

	@Override
	public Boolean deleteUser(String appId, String user) {

		List<Dashboard> dashboardList = null;
		try {
			dashboardList = dashboardRepository.findByappId(appId);

			if (!dashboardList.isEmpty()) {
				for (Dashboard dashboard : dashboardList) {

					BunitCredentials bunitCredential = dashboard.getDbCredentials();
					MongoClient mongoClient = getMongoClient(bunitCredential);
					if (null != mongoClient) {

						MongoDatabase mongoDB = getMongoDatabase(mongoClient, bunitCredential.getDbName());
						if (null != mongoDB) {
							MongoCollection<Document> users = mongoDB.getCollection("authentication");
							if (null != users) {
								DeleteResult deleteResult = users.deleteOne(eq(USERNAME, user));
								LOG.info("deleteOne() - # of records deleted - " + deleteResult.getDeletedCount());
								if (deleteResult.getDeletedCount() != 0) {
									return true;
								} else {
									return false;
								}
							}
						}
					}

				}
			}

		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
		return null;
	}

	@Override
	public MongoDatabase getMongoDatabase(MongoClient client, String databaseName) {
		try {
			if (client != null) {
				MongoDatabase testDB = client.getDatabase(databaseName);
				return testDB;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
		return null;
	}

	@Override
	public MongoClient getMongoClient(BunitCredentials bunitCredential) {
		try {
			if (bunitCredential != null) {
				ServerAddress serverAddr = new ServerAddress(bunitCredential.getHost(), bunitCredential.getPort());

				MongoCredential mongoCredential = MongoCredential.createScramSha1Credential(
						bunitCredential.getDbUserName(), bunitCredential.getDbName(),
						bunitCredential.getDbUserCredentials().toCharArray());
				MongoClient client = new MongoClient(serverAddr, Collections.singletonList(mongoCredential));
				return client;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
		return null;
	}

	@Override
	public boolean closeConnection(MongoClient mongoClient) {
		try {
			if (mongoClient != null) {
				mongoClient.close();
				return true;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
		return false;
	}

	@Override
	public Boolean promoteToAdmin(String appId, String user) {

		List<Dashboard> dashboardList = null;

		dashboardList = dashboardRepository.findByappId(appId);

		if (!dashboardList.isEmpty()) {
			for (Dashboard dashboard : dashboardList) {

				String instanceIp = dashboard.getInstance();

				String output = getOutput(instanceIp, user);
				if ("true".equalsIgnoreCase(output)) {
					return true;
				}
				return false;
			}
		}

		return true;

	}

	private String getOutput(String instanceIp, String user) {
		String output = "";
		try {
			URL url;
			url = new URL("http://" + instanceIp + ":8080/api/userAccess/admin/" + user);
			HttpURLConnection conn;

			conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("GET");

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			StringBuilder out = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				out.append(line);
			}
			br.close();
			conn.disconnect();
			output = out.toString();
			return output;
		} catch (Exception e) {
			LOG.error(e.getMessage());

		}
		return output;
	}

}
