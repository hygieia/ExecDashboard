package com.capitalone.dashboard.executive.rest;

import com.capitalone.dashboard.exec.model.Vast;
import com.capitalone.dashboard.executive.service.VastService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * class VastController
 */
@RestController
@RequestMapping(value = "/vast")
@CrossOrigin
public class VastController {
	private final VastService vastService;

	/**
	 * Constructors
	 * 
	 * @param vastService
	 */
	public VastController(VastService vastService) {
		this.vastService = vastService;
	}

	@GetMapping(value = "/devopscup")
	/**
	 * 
	 * @return Map
	 */
	public Map<String, Vast> getDevopsCupApps() {
		return vastService.getVastForDevopscupApps();
	}
}
