package com.capitalone.dashboard.exec.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.capitalone.dashboard.exec.model.MetricPortfolioDetailResponse;

/**
 * Interface ApplicationDetailsRepository extends
 * PagingAndSortingRepository<ApplicationDetails, ObjectId>
 * 
 * @author v143611
 */
public interface MetricPortfolioDetailRepository
		extends PagingAndSortingRepository<MetricPortfolioDetailResponse, ObjectId> {

	/**
	 * findByExecutiveObjectIdAndMetricsName()
	 * 
	 * @param executiveObjectId
	 * @param metricName
	 * @return MetricPortfolioDetailResponse
	 */
	MetricPortfolioDetailResponse findByExecutiveObjectIdAndMetricsName(String executiveObjectId, String metricName);

	/**
	 * findByEid()
	 * 
	 * @param eid
	 * @return MetricPortfolioDetailResponse
	 */
	MetricPortfolioDetailResponse findByEid(String eid);

	/**
	 * findByEidAndMetricsName()
	 * 
	 * @param eid
	 * @param metricName
	 * @return MetricPortfolioDetailResponse
	 */
	MetricPortfolioDetailResponse findByEidAndMetricsName(String eid, String metricName);

	/**
	 * findAllEids()
	 * 
	 * @param eid
	 * @return List<MetricPortfolioDetailResponse>
	 */
	@Query(value = "{'eid': ?0 }")
	List<MetricPortfolioDetailResponse> findAllEids(String eid);

}
