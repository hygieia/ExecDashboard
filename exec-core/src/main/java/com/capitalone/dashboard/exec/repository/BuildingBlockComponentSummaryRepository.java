package com.capitalone.dashboard.exec.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.capitalone.dashboard.exec.model.BuildingBlockComponentSummaryResponse;

/**
 * Interface BuildingBlockComponentSummaryRepository extends
 * PagingAndSortingRepository<BuildingBlockComponentSummaryResponse, ObjectId>
 * 
 *
 */
public interface BuildingBlockComponentSummaryRepository
		extends PagingAndSortingRepository<BuildingBlockComponentSummaryResponse, ObjectId> {

	/**
	 * findByAppId()
	 * 
	 * @param appId
	 * @return List<BuildingBlockComponentSummaryResponse>
	 */
	List<BuildingBlockComponentSummaryResponse> findByAppId(String appId);

	/**
	 * findByAppIdAndMetricName()
	 * 
	 * @param appId
	 * @param metricType
	 * @return List<BuildingBlockComponentSummaryResponse>
	 */
	@Query(value = "{'appId': ?0 , 'metrics.name': ?1 }")
	List<BuildingBlockComponentSummaryResponse> findByAppIdAndMetricName(String appId, String metricType);

}