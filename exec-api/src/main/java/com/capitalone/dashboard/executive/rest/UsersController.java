package com.capitalone.dashboard.executive.rest;

import com.capitalone.dashboard.exec.model.ExecutiveStatus;
import com.capitalone.dashboard.executive.service.UserInfoService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * UsersController
 * 
 */
@RestController
@RequestMapping(value = "/users")
@CrossOrigin
public class UsersController {
	private final UserInfoService userInfoService;

	/**
	 * UsersController
	 * 
	 * @param userInfoService
	 * @return
	 */
	public UsersController(UserInfoService userInfoService) {
		this.userInfoService = userInfoService;
	}

	@GetMapping(value = "/hitsInfo")
	public Map<Long, Integer> getHitsInfo() {
		return userInfoService.getUniqueHitsInfo();
	}

	@GetMapping(value = "/hitsInfoForTheDay")
	public Map<String, Integer> getUniqueHits() {
		return userInfoService.getUniqueHits();
	}

	@GetMapping(value = "/executivesList")
	public List<ExecutiveStatus> getExecutivesAccessedList() {
		return userInfoService.getExecutivesAccessedList();
	}

	/**
	 * savePageTrackForApp
	 * 
	 * @param view
	 * @param userId
	 * @param appIds
	 * @return Boolean
	 */
	@GetMapping("/application/{view}/{userId}/{appIds}")
	public Boolean savePageTrackForApp(@PathVariable("view") String view, @PathVariable("userId") String userId,
			@PathVariable("appIds") List<String> appIds) {
		return userInfoService.putAppTrack(view, userId, appIds);
	}

	/**
	 * savePageTrackForAppMetric
	 * 
	 * @param view
	 * @param userId
	 * @param appIds
	 * @param metricName
	 * @return Boolean
	 */
	@GetMapping("/application/{view}/{userId}/{appIds}/{metricName}")
	public Boolean savePageTrackForAppMetric(@PathVariable("view") String view, @PathVariable("userId") String userId,
			@PathVariable("appIds") List<String> appIds, @PathVariable("metricName") String metricName) {
		return userInfoService.putAppMetricTrack(view, userId, appIds, metricName);
	}

	/**
	 * savePageTrackForExec
	 * 
	 * @param view
	 * @param userId
	 * @param eids
	 * @return Boolean
	 */
	@GetMapping("/executive/{view}/{userId}/{eids}")
	public Boolean savePageTrackForExec(@PathVariable("view") String view, @PathVariable("userId") String userId,
			@PathVariable("eids") List<String> eids) {
		return userInfoService.putExecTrack(view, userId, eids);
	}

	/**
	 * savePageTrackForExecMetric
	 * 
	 * @param view
	 * @param userId
	 * @param eids
	 * @param metricName
	 * @return Boolean
	 */
	@GetMapping("/executive/{view}/{userId}/{eids}/{metricName}")
	public Boolean savePageTrackForExecMetric(@PathVariable("view") String view, @PathVariable("userId") String userId,
			@PathVariable("eids") List<String> eids, @PathVariable("metricName") String metricName) {
		return userInfoService.putExecMetricTrack(view, userId, eids, metricName);
	}

	/**
	 * savePageTracks
	 * 
	 * @param view
	 * @param userId
	 * @return Boolean
	 */
	@GetMapping("/tracking/{view}/{userId}")
	public Boolean savePageTracks(@PathVariable("view") String view, @PathVariable("userId") String userId) {
		return userInfoService.putPageTrack(view, userId);
	}

	/**
	 * getFrequentUsers
	 * 
	 * @return Map<String, Long>
	 */
	@GetMapping("/recentUsers")
	public Map<String, Long> getFrequentUsers() {
		return userInfoService.getFrequentUsers();
	}

	/**
	 * getFrequentExecutives
	 * 
	 * @return List<String>
	 */
	@GetMapping("/recentExecutives")
	public List<String> getFrequentExecutives() {
		return userInfoService.getFrequentExecutives();
	}

	/**
	 * getFrequentApplications
	 * 
	 * @return List<String>
	 */
	@GetMapping("/recentApplications")
	public List<String> getFrequentApplications() {
		return userInfoService.getFrequentApplications();
	}

	/**
	 * getFrequentCards
	 * 
	 * @return List<String>
	 */
	@GetMapping("/recentCards")
	public List<String> getFrequentCards() {
		return userInfoService.getFrequentCards();
	}
}
