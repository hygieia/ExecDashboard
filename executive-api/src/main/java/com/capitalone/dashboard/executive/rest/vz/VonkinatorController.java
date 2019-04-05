package com.capitalone.dashboard.executive.rest.vz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capitalone.dashboard.exec.model.vz.VonkinatorDataSet;
import com.capitalone.dashboard.executive.service.vz.VonkinatorService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

/**
 * class VonkinatorController
 * 
 * @author v143611
 *
 */
@RestController
@CrossOrigin
public class VonkinatorController {

	private final VonkinatorService vonkinatorService;

	/**
	 * 
	 * @param vonkinatorService
	 */
	@Autowired
	public VonkinatorController(VonkinatorService vonkinatorService) {
		this.vonkinatorService = vonkinatorService;
	}

	/**
	 * getAllVonkinatorDataSet
	 * 
	 * @return List<VonkinatorDataSet>
	 */
	@RequestMapping(value = "/vonkinator/getAll", method = GET, produces = APPLICATION_JSON_VALUE)
	public List<VonkinatorDataSet> getAllVonkinatorDataSet() {
		return vonkinatorService.getVonkinatorAllDataSet();
	}

	/**
	 * getAllVonkinatorNonITDataSet
	 * 
	 * @return List<VonkinatorDataSet>
	 */
	@RequestMapping(value = "/vonkinator/getAllNonIT", method = GET, produces = APPLICATION_JSON_VALUE)
	public List<VonkinatorDataSet> getAllVonkinatorNonITDataSet() {
		return vonkinatorService.getAllVonkinatorNonITDataSet();
	}

	/**
	 * getForPortfolio
	 * 
	 * @param portfolio
	 * @return List<VonkinatorDataSet>
	 */
	@RequestMapping(value = "/vonkinator/getForPortfolio/{portfolio}", method = GET, produces = APPLICATION_JSON_VALUE)
	public List<VonkinatorDataSet> getForPortfolio(@PathVariable("portfolio") String portfolio) {
		return vonkinatorService.getVonkinatorDataSetForPortfolio(portfolio);
	}

}