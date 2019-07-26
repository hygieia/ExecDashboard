package com.capitalone.dashboard.exec.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.capitalone.dashboard.exec.model.ApplicationDetails;

/**
 * Interface ApplicationDetailsRepository extends
 * PagingAndSortingRepository<ApplicationDetails, ObjectId>
 *
 */
public interface ApplicationDetailsRepository extends PagingAndSortingRepository<ApplicationDetails, ObjectId> {

	/**
	 * getConfiguredApplicationDetails()
	 * 
	 * @param
	 * @return List<ApplicationDetails>
	 */
	@Query(value = "{'dashboardAvailable':true}")
	List<ApplicationDetails> getConfiguredApplicationDetails();

	ApplicationDetails findByAppId(String appId);

	ApplicationDetails findByAppName(String appName);

	/**
	 * findByLob()
	 * 
	 * @param lob
	 * @return List<ApplicationDetails>
	 */
	@Query(value = "{'lob' : ?0}")
	List<ApplicationDetails> findByLob(String lob);

	/**
	 * getLimitedAppDetails()
	 * 
	 * @return List<ApplicationDetails>
	 */
	@Query(value = "{ 'appId' : {$exists:true}}", fields = "{'appId' : 1,'appAcronym':1,'appName':1,'lob':1}")
	List<ApplicationDetails> getLimitedAppDetails();

}