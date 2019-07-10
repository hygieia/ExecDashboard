package com.capitalone.dashboard.executive.rest.vz;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capitalone.dashboard.exec.model.vz.DigitalCockpitResponse;
import com.capitalone.dashboard.executive.service.vz.DigitalCockpitService;

@RestController
@CrossOrigin
public class DigitalCockpitController {

	private final DigitalCockpitService digitalCockpitService;

	public DigitalCockpitController(DigitalCockpitService digitalCockpitService) {
		this.digitalCockpitService = digitalCockpitService;
	}

	@GetMapping("/changefailure/TODAY")
	public List<DigitalCockpitResponse> getFilteredCFMetricDetails() {
		return digitalCockpitService.getFilteredCFMetricDetails();
	}

	@GetMapping("/codecommit/TODAY")
	public List<DigitalCockpitResponse> getFilteredCodeCommitMetricDetails() {
		return digitalCockpitService.getFilteredCodeCommitMetricDetails();
	}

	@GetMapping("/deploymentca/TODAY")
	public List<DigitalCockpitResponse> getFilteredDeploymentCAMetricDetails() {
		return digitalCockpitService.getFilteredDeploymentCAMetricDetails();
	}

	@GetMapping("/quality/TODAY")
	public List<DigitalCockpitResponse> getFilteredQualityMetricDetails() {
		return digitalCockpitService.getFilteredQualityMetricDetails();
	}

	@GetMapping("/velocity/TODAY")
	public List<DigitalCockpitResponse> getFilteredVelocityMetricDetails() {
		return digitalCockpitService.getFilteredVelocityMetricDetails();
	}
}
