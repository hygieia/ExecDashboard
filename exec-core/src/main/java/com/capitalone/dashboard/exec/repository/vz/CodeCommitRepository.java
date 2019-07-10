package com.capitalone.dashboard.exec.repository.vz;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;

import com.capitalone.dashboard.exec.model.vz.MetricPortfolioDetailResponse;

/**
 * Interface CodeCommitRepository extends
 * CrudRepository<MetricPortfolioDetailResponse, ObjectId>
 * 
 * @author v925481
 */

public interface CodeCommitRepository extends CrudRepository<MetricPortfolioDetailResponse, ObjectId> {

	/**
	 * findByMetricsName()
	 * 
	 * @param metricsName
	 * @return MetricPortfolioDetailResponse
	 */
	List<MetricPortfolioDetailResponse> findByMetricsName(String metricsName);
}
