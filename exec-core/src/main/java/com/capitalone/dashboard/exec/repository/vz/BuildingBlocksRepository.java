package com.capitalone.dashboard.exec.repository.vz;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.capitalone.dashboard.exec.model.MetricLevel;
import com.capitalone.dashboard.exec.model.MetricType;
import com.capitalone.dashboard.exec.model.vz.BuildingBlocks;

/**
 * Interface BuildingBlocks extends PagingAndSortingRepository<BuildingBlocks,
 * ObjectId>
 * 
 * @author raish4s
 */
public interface BuildingBlocksRepository extends PagingAndSortingRepository<BuildingBlocks, ObjectId> {

	/**
	 * 
	 * @param metricLevelId
	 * @param metricLevel
	 * @return
	 */
	
	BuildingBlocks findByMetricLevelIdAndMetricLevel(String metricLevelId, MetricLevel metricLevel);

	/**
	 * 
	 * @param metricLevelId
	 * @param metricLevel
	 * @return
	 */
	
	@Query(value = "{'metricLevelId': {'$in' : ?0 } , 'metricLevel' : ?1 }")
	List<BuildingBlocks> findByMetricLevelIdAndMetricLevel(List<String> metricLevelId, MetricLevel metricLevel);

	/**
	 * 
	 * @param metricLevelId
	 * @param metricLevel
	 * @param metricType
	 * @return
	 */
	
	@Query(value = "{'metricLevelId': {'$in' : ?0 },'metricLevel':?1, 'metrics.name':?2}", fields = "{ 'metricLevelId' : 1, 'name' :1,   'completeness':1 ,   'lob':1 ,   'poc':1,   'metricLevel':1 ,   'totalExpectedMetrics' :1,   'totalComponents':1 ,   'reportingComponents':1, 'appCriticality' : 1,'customField':1,metrics:	{$elemMatch: {name: ?2}}}")
	List<BuildingBlocks> findByMetricLevelIdAndMetricLevelForProduct(List<String> metricLevelId,
			MetricLevel metricLevel, String metricType);

	/**
	 * 
	 * @param metricLevelId
	 * @param metricLevel
	 * @return
	 */
	
	@Query(value = "{'metricLevelId': ?0 , 'metricLevel' : ?1 }")
	List<BuildingBlocks> getAllByMetricLevelIdAndMetricLevel(String metricLevelId, MetricLevel metricLevel);

	/**
	 * 
	 * @param metricLevelId
	 * @param metricLevel
	 * @param metricType
	 * @return
	 */
	
	List<BuildingBlocks> findByMetricLevelIdAndMetricLevelAndMetricType(String metricLevelId, MetricLevel metricLevel,
			MetricType metricType);

	/**
	 * 
	 * @param metricLevelIds
	 * @param metricLevel
	 * @param metricType
	 * @return
	 */
	
	@Query(value = "{'metricLevelId': {'$in' : ?0 } , 'metricLevel' : ?1, 'metricType' : ?2 }")
	List<BuildingBlocks> findByMetricLevelIdAndMetricLevelAndMetricType(List<String> metricLevelIds,
			MetricLevel metricLevel, MetricType metricType);

}