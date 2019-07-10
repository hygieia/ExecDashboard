package com.capitalone.dashboard.executive.rest.vz;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capitalone.dashboard.exec.model.vz.ExternalSystemMonitor;
import com.capitalone.dashboard.executive.service.vz.ExternalMonitorService;

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
	@Autowired
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