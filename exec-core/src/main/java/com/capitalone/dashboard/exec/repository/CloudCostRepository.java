package com.capitalone.dashboard.exec.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.capitalone.dashboard.exec.model.CloudCost;

/**
 * Interface CloudCostRepository extends PagingAndSortingRepository<CloudCost,
 * ObjectId>
 * 
 * @author raish4s
 */
public interface CloudCostRepository extends PagingAndSortingRepository<CloudCost, ObjectId> {

	/**
	 * @param appId
	 * @param time
	 * @return
	 */
	@Query(value = " {'appId' : ?0, 'time' : ?1}")
	CloudCost findByAppIdAndTime(String appId, String time);
	
	/**
	 * @param appId
	 * @param label
	 * @return
	 */
	@Query(value = " {'appId' : ?0, 'label' : ?1}")
	CloudCost findByAppIdAndLabel(String appId, String label);

	/**
	 * @param appId
	 * @return
	 */
	List<CloudCost> findByAppId(String appId);
}