package com.capitalone.dashboard.executive.rest;

import com.capitalone.dashboard.exec.model.ExternalSystemMonitor;
import com.capitalone.dashboard.executive.service.ExternalMonitorService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * AuthenticationController class
 */
@RestController
@CrossOrigin
public class ExternalMonitorController {

	private final ExternalMonitorService externalMonitorService;

	/**
	 * Constructor
	 * 
	 * @param externalMonitorService
	 */
	public ExternalMonitorController(ExternalMonitorService externalMonitorService) {
		this.externalMonitorService = externalMonitorService;
	}

	/**
	 * Rest Mapping
	 * 
	 * @return data
	 */
	@RequestMapping(value = "/externalmonitor/status", produces = APPLICATION_JSON_VALUE)
	public List<ExternalSystemMonitor> getLatestRecord() {
		return externalMonitorService.getLatestRecord();
	}

}