package com.capitalone.dashboard.exec.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.capitalone.dashboard.exec.model.Dashboard;

/**
 * DashboardRepository extends PagingAndSortingRepository
 */
public interface DashboardRepository extends PagingAndSortingRepository<Dashboard, ObjectId> {
	/**
	 * 
	 * @param owner
	 * @return
	 */
	List<Dashboard> findByOwner(String owner);

	/**
	 * 
	 * @param title
	 * @return
	 */
	List<Dashboard> findByTitle(String title);

	List<Dashboard> findAll();

	/**
	 * 
	 * @param appId
	 * @return
	 */
	@Query(value = "{ 'appId' : ?0}")
	Dashboard getDashboardByappId(String appId);

	/**
	 * 
	 * @param appIdPresent
	 * @return
	 */
	@Query(value = "{ 'appId' : {$exists:?0}}", fields = "{'appId' : 1}")
	List<Dashboard> checkByAppId(boolean appIdPresent);

	/**
	 * 
	 * @param appId
	 * @return
	 */
	List<Dashboard> findByappId(String appId);

	/**
	 * 
	 * @param appId
	 * @return
	 */
	@Query(value = "{ 'appId' : ?0}", fields = "{'_id' : 1}")
	Dashboard checkappIdexists(String appId);

	/**
	 * 
	 * @param businessUnit
	 * @return
	 */
	@Query(value = "{ 'businessUnit' : ?0}")
	List<Dashboard> getByBunit(String businessUnit);

	/**
	 * 
	 * @param businessUnit
	 * @return
	 */
	@Query(value = "{ 'businessUnit' : ?0}")
	List<Dashboard> getByBunitDetails(String businessUnit);

	/**
	 * 
	 * @param appName
	 * @return
	 */
	@Query(value = "{ 'application.name' : ?0}")
	Dashboard findByAppName(String appName);

	/**
	 * 
	 * @param instance
	 * @return
	 */
	@Query(value = "{ 'instance' : ?0}")
	List<Dashboard> getByInstanceID(String instance);

	/**
	 * 
	 * @param instanceIP
	 * @return Dashboard
	 */
	@Query(value = "{ 'instance' : ?0}")
	Dashboard getDashboardByInstanceDetails(String instanceIP);
}