package com.capitalone.dashboard.executive.service;

import com.capitalone.dashboard.exec.model.BunitCredentials;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import java.util.List;
import java.util.Map;

public interface DefaultHygieiaService {

	List<String> getUsers(String appId);

	MongoDatabase getMongoDatabase(MongoClient client, String databaseName);

	MongoClient getMongoClient(BunitCredentials bunitCredential);

	Boolean deleteUser(String appId, String user);

	boolean closeConnection(MongoClient mongoClient);

	/**
	 * usertoAdmin
	 * 
	 * @param appId
	 * @param user
	 *            return
	 */
	Boolean promoteToAdmin(String appId, String user);

	/**
	 * getUsersforAdmin
	 * 
	 * @param appId
	 *            return
	 */
	List<Map<String, String>> getUsersForAdmin(String appId);
}
