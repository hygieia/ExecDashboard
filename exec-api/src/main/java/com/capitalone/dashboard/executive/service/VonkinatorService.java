package com.capitalone.dashboard.executive.service;

import com.capitalone.dashboard.exec.model.VonkinatorDataSet;

import java.util.List;

/**
 * interface VonkinatorService
 *
 */
public interface VonkinatorService {

	/**
	 * 
	 * @param portfolio
	 * @return List<VonkinatorDataSet>
	 */
	List<VonkinatorDataSet> getVonkinatorDataSetForPortfolio(String portfolio);

	/**
	 * 
	 * @return List<VonkinatorDataSet>
	 */
	List<VonkinatorDataSet> getVonkinatorAllDataSet();

	/**
	 * 
	 * @return List<VonkinatorDataSet>
	 */
	List<VonkinatorDataSet> getAllVonkinatorNonITDataSet();

}
