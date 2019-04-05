package com.capitalone.dashboard.executive.service.vz;

import java.util.List;

import com.capitalone.dashboard.exec.model.vz.CollectorUpdatedStatus;

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
