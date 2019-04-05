package com.capitalone.dashboard.executive.service.vz;

import java.util.List;
import java.util.Map;

import com.capitalone.dashboard.exec.model.vz.ExecutiveStatus;

/**
 * UserInfoService
 * 
 */
public interface UserInfoService {

	/**
	 * getUniqueHitsInfo
	 * 
	 * @param ...
	 * @return Map<Long, Integer>
	 */
	Map<Long, Integer> getUniqueHitsInfo();

	/**
	 * getUniqueHits
	 * 
	 * @param ...
	 * @return Map<String, Integer>
	 */
	List<ExecutiveStatus> getExecutivesAccessedList();

	/**
	 * getExecutivesAccessedList
	 * 
	 * @param ...
	 * @return List<ExecutiveStatus>
	 */
	Map<String, Integer> getUniqueHits();

	/**
	 * putAppTrack
	 * 
	 * @param view
	 * @param userId
	 * @param appIds
	 * @return Boolean
	 */
	Boolean putAppTrack(String view, String userId, List<String> appIds);

	/**
	 * putExecTrack
	 * 
	 * @param view
	 * @param userId
	 * @param eids
	 * @return Boolean
	 */
	Boolean putExecTrack(String view, String userId, List<String> eids);

	/**
	 * putPageTrack
	 * 
	 * @param view
	 * @param userId
	 * @return Boolean
	 */
	Boolean putPageTrack(String view, String userId);

	/**
	 * putAppMetricTrack
	 * 
	 * @param view
	 * @param userId
	 * @param appIds
	 * @param metricName
	 * @return Boolean
	 */
	Boolean putAppMetricTrack(String view, String userId, List<String> appIds, String metricName);

	/**
	 * putExecMetricTrack
	 * 
	 * @param view
	 * @param userId
	 * @param eids
	 * @param metricName
	 * @return Boolean
	 */
	Boolean putExecMetricTrack(String view, String userId, List<String> eids, String metricName);

	/**
	 * getFrequentUsers
	 * 
	 * @return Map<String, Long>
	 */
	Map<String, Long> getFrequentUsers();

	/**
	 * getFrequentExecutives
	 * 
	 * @return List<String>
	 */
	List<String> getFrequentExecutives();

	/**
	 * getFrequentApplications
	 * 
	 * @return List<String>
	 */
	List<String> getFrequentApplications();

	/**
	 * getFrequentCards
	 * 
	 * @return List<String>
	 */
	List<String> getFrequentCards();

}
