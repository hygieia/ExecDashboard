package com.capitalone.dashboard.executive.rest;

import com.capitalone.dashboard.exec.model.CollectorUpdatedStatus;
import com.capitalone.dashboard.exec.model.DevOpsCupScores;
import com.capitalone.dashboard.exec.model.DevopscupRoundDetails;
import com.capitalone.dashboard.exec.model.MetricType;
import com.capitalone.dashboard.executive.service.CollectorUpdatedStatusService;
import com.capitalone.dashboard.executive.service.UtilityService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * MetricsController class
 */
@RestController
@CrossOrigin
public class MetricsCustomController {
	private final UtilityService utilityService;
	private final CollectorUpdatedStatusService collectorUpdatedStatusService;

	public MetricsCustomController(UtilityService utilityService,
			CollectorUpdatedStatusService collectorUpdatedStatusService) {
		this.utilityService = utilityService;
		this.collectorUpdatedStatusService = collectorUpdatedStatusService;
	}

	@GetMapping("/metrics/product/{appId}")
	public String getProductId(@PathVariable("appId") String appId) {
		return utilityService.getProductId(appId);
	}

	@GetMapping("/metrics/cardsList")
	public List<String> getCardsList() {
		return utilityService.getActiveCards();
	}

	@GetMapping("/metrics/previewList")
	public Map<String, String> getCardsPreviewList() {
		return utilityService.getActiveCardsPreview();
	}

	@GetMapping("/metrics/previewSelectList")
	public Map<String, String> getCardsPreviewSelectList() {
		return utilityService.getActiveCardsSelectPreview();
	}

	@PostMapping(value = "/metrics/criticality/status")
	public Map<String, List<String>> getAppCriticalityStatus(@RequestBody String[] appList) {
		return utilityService.getAppCriticalityStatus(Arrays.asList(appList));
	}

	@GetMapping("/metrics/getCollectorTimeStamps/{metric}")
	public List<CollectorUpdatedStatus> getCollectorUpdatedTimeStamps(@PathVariable("metric") String metricType) {
		return collectorUpdatedStatusService.getCollectorUpdatedTimestamp(metricType);
	}

	@GetMapping("/metrics/{metric}/portfolio/{id}/productForDevopscup")
	public List<DevOpsCupScores> getPortfolioMetricProductsForDevopscup(@PathVariable("metric") MetricType metricType,
			@PathVariable("id") String portfolioId) {

		return utilityService.getPortfolioMetricProductsForDevopscup(metricType, portfolioId);
	}

	@GetMapping("/metrics/roundForDevopscup")
	public DevopscupRoundDetails getRoundDetailsForDevopscup() {
		return utilityService.getRoundDetailsForDevopscup();
	}

	@GetMapping(value = "/portfolio/setFav/{eid}/{favEid}")
	public boolean setFav(@PathVariable String eid, @PathVariable List<String> favEid) {
		return utilityService.setAsFav(eid, favEid);
	}

	@GetMapping(value = "/portfolio/removeFav/{eid}")
	public boolean removeFav(@PathVariable String eid) {
		return utilityService.removeFav(eid);
	}

	@GetMapping("/portfolio/getFavsOfEid/{eid}")
	public Map<String, String> getFavsOfEid(@PathVariable String eid) {
		return utilityService.getFavsOfEid(eid);
	}

	@GetMapping("/portfolio/timePeriods")
	public Map<Integer, String> getTimePeriods() {
		return utilityService.getTimePeriods();
	}
}
