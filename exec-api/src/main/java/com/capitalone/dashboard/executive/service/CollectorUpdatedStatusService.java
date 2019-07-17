package com.capitalone.dashboard.executive.service;

import com.capitalone.dashboard.exec.model.CollectorUpdatedStatus;

import java.util.List;

/**
 * interface CollectorUpdatedStatusService
 * 
 * @author rhe94mg
 *
 */
@FunctionalInterface
public interface CollectorUpdatedStatusService {

	/**
	 * getCollectorUpdatedTimestamp
	 * 
	 * @param metricType
	 * @return List<CollectorUpdatedStatus>
	 */
	List<CollectorUpdatedStatus> getCollectorUpdatedTimestamp(String metricType);
}
