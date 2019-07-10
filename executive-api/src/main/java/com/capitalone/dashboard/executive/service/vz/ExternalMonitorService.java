package com.capitalone.dashboard.executive.service.vz;

import java.util.List;

import com.capitalone.dashboard.exec.model.vz.ExternalSystemMonitor;

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
