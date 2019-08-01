package com.capitalone.dashboard.executive.service;

import com.capitalone.dashboard.exec.model.DevOpsCupScores;
import com.capitalone.dashboard.exec.model.DevopscupRoundDetails;
import com.capitalone.dashboard.exec.model.MetricType;

import java.util.List;
import java.util.Map;

/**
 * interface UtilityService
 */
public interface UtilityService {
	/**
	 * @param appList
	 * 
	 * @return
	 */
	Map<String, List<String>> getAppCriticalityStatus(List<String> appList);

	/**
	 * @param favEid
	 * 
	 * @return
	 */
	boolean setAsFav(String eid, List<String> favEid);

	/**
	 * @param eid
	 * 
	 * @return
	 */
	boolean removeFav(String eid);

	/**
	 * @param eid
	 * 
	 * @return
	 */
	Map<String, String> getFavsOfEid(String eid);

	/**
	 * getActiveCards()
	 * 
	 * @return List<String>
	 */
	List<String> getActiveCards();

	/**
	 * getActiveCardsPreview()
	 * 
	 * @return Map<String, String>
	 */
	Map<String, String> getActiveCardsPreview();

	/**
	 * getActiveCardsSelectPreview()
	 * 
	 * @return Map<String, String>
	 */
	Map<String, String> getActiveCardsSelectPreview();

	/**
	 * getTimePeriods()
	 * 
	 * @return Map<Integer, String>
	 */
	Map<Integer, String> getTimePeriods();

	String getProductId(String appId);

	List<DevOpsCupScores> getPortfolioMetricProductsForDevopscup(MetricType metricType, String portfolioId);

	DevopscupRoundDetails getRoundDetailsForDevopscup();

}
