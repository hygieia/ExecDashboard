package com.capitalone.dashboard.exec.repository;

import org.bson.types.ObjectId;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.capitalone.dashboard.exec.model.JenkinsPipelineMetrics;

/**
 *
 *
 */
public interface JenkinsPipelineMetricsRepository extends PagingAndSortingRepository<JenkinsPipelineMetrics, ObjectId> {

	/**
	 * @param appId
	 * @return JenkinsPipelineMetrics
	 */
	JenkinsPipelineMetrics findByAppId(String appId);
	
	/**
	 * @param jobName
	 * @param executionId
	 * @return JenkinsPipelineMetrics
	 */
	JenkinsPipelineMetrics findByJobNameAndExecutionId(String jobName, String executionId);

}
