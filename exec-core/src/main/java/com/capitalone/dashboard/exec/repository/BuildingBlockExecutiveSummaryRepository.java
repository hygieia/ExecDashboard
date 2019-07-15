package com.capitalone.dashboard.exec.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.capitalone.dashboard.exec.model.BuildingBlockExecutiveSummaryResponse;

/**
 * Interface BuildingBlockExecutiveSummaryRepository extends
 * PagingAndSortingRepository<BuildingBlockExecutiveSummaryResponse, ObjectId>
 * 
 * @author v143611 and aje8p9a
 */
public interface BuildingBlockExecutiveSummaryRepository
		extends PagingAndSortingRepository<BuildingBlockExecutiveSummaryResponse, ObjectId> {
	/**
	 * findByAppId()
	 * 
	 * @param appId
	 * @return BuildingBlockExecutiveSummaryResponse
	 */
	BuildingBlockExecutiveSummaryResponse findByAppId(String appId);

	/**
	 * getByAppIds()
	 * 
	 * @param eids
	 * @param sort
	 * @return List<BuildingBlockExecutiveSummaryResponse>
	 */
	@Query(value = "{'appId': {'$in' : ?0 }}")
	List<BuildingBlockExecutiveSummaryResponse> getByAppIds(List<String> eids);

	/**
	 * getByAppIdsAndMetric()
	 * 
	 * @param appId
	 * @param metric
	 * @return List<BuildingBlockExecutiveSummaryResponse>
	 */
	@Query(value = "{'appId': {'$in' : ?0 }, 'metrics.name': ?1 }")
	List<BuildingBlockExecutiveSummaryResponse> getByAppIdsAndMetric(List<String> appId, String metric);

	/**
	 * getByTimestamp()
	 * 
	 * @param timestamp
	 * @return List<BuildingBlockExecutiveSummaryResponse>
	 */
	@Query(value = "{'timestamp':{'$ne': ?0}}")
	List<BuildingBlockExecutiveSummaryResponse> getByTimestamp(long timestamp);

}
