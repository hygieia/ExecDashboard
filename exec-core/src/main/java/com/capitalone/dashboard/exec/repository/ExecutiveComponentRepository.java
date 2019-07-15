package com.capitalone.dashboard.exec.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.capitalone.dashboard.exec.model.ExecutiveComponents;

/**
 * Interface ExecutiveComponentRepository extends
 * PagingAndSortingRepository<ExecutiveComponents, ObjectId>
 * 
 * @author v143611
 */
public interface ExecutiveComponentRepository extends PagingAndSortingRepository<ExecutiveComponents, ObjectId> {

	/**
	 * findByAppId()
	 * 
	 * @param appId
	 * @return ExecutiveComponents
	 */
	ExecutiveComponents findByAppId(String appId);

	/**
	 * findByAppIdAndMetric()
	 * 
	 * @param appId
	 * @param metric
	 * @return ExecutiveComponents
	 */
	@Query(value = " {'appId' : ?0, 'metrics.metricsName' : ?1}")
	ExecutiveComponents findByAppIdAndMetric(String appId, String metric);

	/**
	 * findByMetric()
	 * 
	 * @param metric
	 * @return List<ExecutiveComponents>
	 */
	@Query(value = " {'metrics.metricsName' : ?0}")
	List<ExecutiveComponents> findByMetric(String metric);

	/**
	 * getNotUsedAppIdList()
	 * 
	 * @param appIdList
	 * @param metric
	 * @return List<ExecutiveComponents>
	 */
	@Query(value = " {'appId' : {$nin : ?0}, 'metrics.metricsName' : ?1}")
	List<ExecutiveComponents> getNotUsedAppIdList(List<String> appIdList, String metric);
}
