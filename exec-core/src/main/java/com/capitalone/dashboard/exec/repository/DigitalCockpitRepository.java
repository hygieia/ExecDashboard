package com.capitalone.dashboard.exec.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;

import com.capitalone.dashboard.exec.model.MetricLevel;
import com.capitalone.dashboard.exec.model.MetricType;
import com.capitalone.dashboard.exec.model.MetricsDetail;

/**
 * 
 *
 *
 */

public interface DigitalCockpitRepository extends CrudRepository<MetricsDetail, ObjectId> {

	/**
	 * 
	 * @param level
	 * @param type
	 * @return
	 */
	
	List<MetricsDetail> findByLevelAndType(MetricLevel level, MetricType type);
}