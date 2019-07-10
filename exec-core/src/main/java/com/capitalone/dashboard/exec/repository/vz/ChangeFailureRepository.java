package com.capitalone.dashboard.exec.repository.vz;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;

import com.capitalone.dashboard.exec.model.vz.MetricPortfolioDetailResponse;

/**
 * Interface ChangeFailureRepository extends
 * CrudRepository<MetricPortfolioDetailResponse, ObjectId>
 * 
 * @author v925481
 */

public interface ChangeFailureRepository extends CrudRepository<MetricPortfolioDetailResponse, ObjectId> {

	/**
	 * findByMetricsName()
	 * 
	 * @param metricsName
	 * @return MetricPortfolioDetailResponse
	 */
	List<MetricPortfolioDetailResponse> findByMetricsName(String metricsName);
}
