package com.capitalone.dashboard.exec.repository.vz;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.capitalone.dashboard.exec.model.MetricLevel;
import com.capitalone.dashboard.exec.model.MetricType;
import com.capitalone.dashboard.exec.model.vz.MetricsDetail;

/**
 * 
 * @author PRAKPR5
 *
 */

public interface MetricsDetailRepository extends PagingAndSortingRepository<MetricsDetail, ObjectId> {

	/**
	 * 
	 * @param metricLevelId
	 * @param level
	 * @return
	 */
	
	List<MetricsDetail> findByMetricLevelIdAndLevel(String metricLevelId, MetricLevel level);
	
	/**
	 * 
	 * @param metricLevelId
	 * @param level
	 * @param type
	 * @return
	 */
	
	MetricsDetail findByMetricLevelIdAndLevelAndType(String metricLevelId, MetricLevel level, MetricType type);
	
	/**
	 * 
	 * @param metricLevelIds
	 * @param level
	 * @return
	 */
	
	@Query(value = "{'metricLevelId': {'$in' : ?0 } , 'level' : ?1 }")
	List<MetricsDetail> findByMetricLevelIdsAndLevel(List<String> metricLevelIds, MetricLevel level);

}
