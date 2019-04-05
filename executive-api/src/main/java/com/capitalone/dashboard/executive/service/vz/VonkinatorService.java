package com.capitalone.dashboard.executive.service.vz;

import java.util.List;

import com.capitalone.dashboard.exec.model.vz.VonkinatorDataSet;

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
