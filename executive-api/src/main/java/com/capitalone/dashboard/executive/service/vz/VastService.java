package com.capitalone.dashboard.executive.service.vz;

import java.util.Map;

import com.capitalone.dashboard.exec.model.vz.Vast;

/**
 * 
 * interface VastService
 */
@FunctionalInterface
public interface VastService {
	/**
	 * 
	 * @return Map
	 */
	Map<String, Vast> getVastForDevopscupApps();
}
