package com.capitalone.dashboard.executive.rest;

import com.capitalone.dashboard.exec.model.SoftwareVersion;
import com.capitalone.dashboard.executive.service.HygieiaInstanceService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * class HygieiaInstanceController
 *
 *
 */
@RestController
@CrossOrigin
public class HygieiaInstanceController {

	private final HygieiaInstanceService hygieiaInstanceService;

	/**
	 * 
	 * @param hygieiaInstanceService
	 */
	public HygieiaInstanceController(HygieiaInstanceService hygieiaInstanceService) {
		this.hygieiaInstanceService = hygieiaInstanceService;
	}

	/**
	 * @param bunit
	 * @return SoftwareVersion
	 */
	@RequestMapping(value = "/instance/getPatchVersions/{bunit}", method = GET, produces = APPLICATION_JSON_VALUE)
	public SoftwareVersion getPatchVersionsBunitWise(@PathVariable String bunit) {
		return hygieiaInstanceService.getPatchVersions(bunit);
	}

	/**
	 * @return List
	 */
	@RequestMapping(value = "/getBusinessUnits", method = GET, produces = APPLICATION_JSON_VALUE)
	public List<String> getBusinessUnits() {
		return hygieiaInstanceService.getBusinessUnits();
	}

	/**
	 * @param instanceIP
	 * @param check
	 * @return Map
	 */
	@RequestMapping(value = "/getPatchVersionsByInstance/{instanceIP}/{check}", method = GET, produces = APPLICATION_JSON_VALUE)
	public Map<String, Object> getPatchVersionsInstanceWise(@PathVariable String instanceIP,
			@PathVariable String check) {
		return hygieiaInstanceService.getPatchVersionsByInstance(instanceIP, check);
	}

}