package com.capitalone.dashboard.exec.repository.vz;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.capitalone.dashboard.exec.model.vz.StashDetailsExcel;

/**
 * Data Model for Stash Details obtained from excel sheet
 * 
 * @author prakpr5
 */

@Repository
public interface StashExcelRepository extends PagingAndSortingRepository<StashDetailsExcel, ObjectId> {

	/**
	 * checkIfPresent()
	 * 
	 * @param appId
	 * @param repoSlug
	 * @param repoBranch
	 * @param projectKey
	 * @return StashDetailsExcel
	 */

	@Query(value = "{'appId': ?0 , 'repoSlug': ?1 , 'repoBranch': ?2 , 'projectKey': ?3 }")
	StashDetailsExcel checkIfPresent(String appId, String repoSlug, String repoBranch, String projectKey);

	/**
	 * findByAppId()
	 * 
	 * @param appId
	 * @return List<StashDetailsExcel>
	 */

	@Query(value = "{'appId': ?0 }")
	List<StashDetailsExcel> findByAppId(String appId);

	/**
	 * findAll()
	 * 
	 * @return List<StashDetailsExcel>
	 */

	List<StashDetailsExcel> findAll();

}
