package com.capitalone.dashboard.exec.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.capitalone.dashboard.exec.model.BuildingBlockMetricSummaryResponse;

/**
 * Interface BuildingBlockMetricSummaryRepository extends
 * PagingAndSortingRepository<BuildingBlockMetricSummaryResponse, ObjectId>
 * 
 *
 */
public interface BuildingBlockMetricSummaryRepository
		extends PagingAndSortingRepository<BuildingBlockMetricSummaryResponse, ObjectId> {
	/**
	 * findByAppId()
	 * 
	 * @param appId
	 * @return BuildingBlockMetricSummaryResponse
	 */
	BuildingBlockMetricSummaryResponse findByAppId(String appId);

	/**
	 * getByAppIds()
	 * 
	 * @param configuredAppId
	 * @return List<BuildingBlockMetricSummaryResponse>
	 */
	@Query(value = "{'appId': {'$in' : ?0 }}")
	List<BuildingBlockMetricSummaryResponse> getByAppIds(List<String> configuredAppId);

	/**
	 * getByAppIdsAndMetric()
	 * 
	 * @param appId
	 * @param metric
	 * @return List<BuildingBlockMetricSummaryResponse>
	 */
	@Query(value = "{'appId': {'$in' : ?0 }, 'metrics.name': ?1 }")
	List<BuildingBlockMetricSummaryResponse> getByAppIdsAndMetric(List<String> appId, String metric);

	/**
	 * @param appId
	 * @param pipelinemetrics
	 * @return
	 */
	BuildingBlockMetricSummaryResponse findByAppIdAndMetricsName(String appId, String pipelinemetrics);

}