package com.capitalone.dashboard.executive.service;

import com.capitalone.dashboard.exec.model.ExternalSystemMonitor;

import java.util.List;

/**
 * interface ExternalMonitorService
 * 
 */
@FunctionalInterface
public interface ExternalMonitorService {

	/**
	 * @return
	 */
	List<ExternalSystemMonitor> getLatestRecord();

}
