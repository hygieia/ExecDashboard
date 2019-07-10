package com.capitalone.dashboard.executive.rest.vz;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capitalone.dashboard.exec.model.vz.Vast;
import com.capitalone.dashboard.executive.service.vz.VastService;

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
	@Autowired
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
