package com.capitalone.dashboard.executive.rest;

import com.capitalone.dashboard.exec.model.ApplicationDetails;
import com.capitalone.dashboard.executive.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * class ApplicationController
 *
 *
 *
 */
@RestController
@CrossOrigin
public class ApplicationController {

	private final ApplicationService applicationService;

	/**
	 * 
	 * @param applicationService
	 */
	@Autowired
	public ApplicationController(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	/**
	 * getAllApplicationDetails
	 * 
	 * @return List<ApplicationDetails>
	 */
	@RequestMapping(value = "/applications", method = GET, produces = APPLICATION_JSON_VALUE)
	public List<ApplicationDetails> getAllApplicationDetails() {
		return applicationService.getAllApplicationDetails();
	}

}