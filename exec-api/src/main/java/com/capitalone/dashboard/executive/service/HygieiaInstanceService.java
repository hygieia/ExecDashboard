package com.capitalone.dashboard.executive.service;

import com.capitalone.dashboard.exec.model.SoftwareVersion;

import java.util.List;
import java.util.Map;

/**
 * interface HygieiaInstanceService
 *
 */
public interface HygieiaInstanceService {

	/**
	 * @param bunit
	 * @return
	 */
	SoftwareVersion getPatchVersions(String bunit);

	/**
	 * 
	 * @param instanceIP
	 * @param check
	 * @return Map
	 */
	Map<String, Object> getPatchVersionsByInstance(String instanceIP, String check);

	/**
	 * 
	 * @return List
	 */
	List<String> getBusinessUnits();

}
