package com.capitalone.dashboard.executive.service;

import com.capitalone.dashboard.exec.model.Vast;

import java.util.Map;

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
