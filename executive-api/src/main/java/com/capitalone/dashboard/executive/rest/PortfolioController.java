package com.capitalone.dashboard.executive.rest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capitalone.dashboard.exec.model.vz.BuildingBlocks;
import com.capitalone.dashboard.exec.model.vz.PortfolioResponse;
import com.capitalone.dashboard.executive.service.BuildingBlocksService;
import com.capitalone.dashboard.executive.service.PortfolioResponseService;

/**
 * PortfolioController class
 */
@RestController
@RequestMapping("/portfolio")
@CrossOrigin
public class PortfolioController {
	private final PortfolioResponseService portfolioResponseService;
	private final BuildingBlocksService buildingBlocksService;

	@Autowired
	public PortfolioController(PortfolioResponseService portfolioResponseService,
			BuildingBlocksService buildingBlocksService) {
		this.portfolioResponseService = portfolioResponseService;
		this.buildingBlocksService = buildingBlocksService;
	}

	@GetMapping()
	public List<PortfolioResponse> getPortfolios() {
		return portfolioResponseService.findAll();
	}

	@GetMapping("/{id}")
	public PortfolioResponse getPortfolio(@PathVariable("id") String id) {
		return portfolioResponseService.findById(id);
	}

	@GetMapping("/businessUnits")
	public List<String> getPortfolioBusinessUnits() {
		return portfolioResponseService.getAllBusinessUnits();
	}

	@GetMapping("/businessUnits/{eid}")
	public List<String> getPortfolioBusinessUnitsForExec(@PathVariable("eid") String eid) {
		return portfolioResponseService.getAllBusinessUnitsForExec(eid);
	}

	@GetMapping("/executivesLists/{businessUnit}")
	public Map<String, String> getExecutivesListBusinessUnits(@PathVariable("businessUnit") String businessUnit) {
		return portfolioResponseService.getExecutivesLists(businessUnit);
	}

	@GetMapping("/executivesListsAll/{businessUnit}")
	public Map<String, String> getExecutivesListAllBusinessUnits(@PathVariable("businessUnit") String businessUnit) {
		return portfolioResponseService.getExecutivesListsAll(businessUnit);
	}

	@GetMapping("/applicationListForExec/{businessUnit}/{id}")
	public Map<String, String> getApplicationListForExecWithPortfolio(@PathVariable("businessUnit") String businessUnit,
			@PathVariable("id") String id) {
		return portfolioResponseService.getApplicationListForExecWithPortfolio(businessUnit, id);
	}

	@GetMapping("/applicationListForExec/{id}")
	public Map<String, String> getApplicationListForExec(@PathVariable("id") String id) {
		return portfolioResponseService.getApplicationListForExec(id);
	}

	@GetMapping("/getAllApplicationList")
	public Map<String, String> getAllApplicationList() {
		return portfolioResponseService.getAllApplicationList();
	}

	@GetMapping("/applicationList/{lob}")
	public Map<String, String> getApplicationListForLob(@PathVariable("lob") String lob) {
		return portfolioResponseService.getAllApplicationListForLob(lob);
	}

	@GetMapping("/reportings/{eid}")
	public Map<String, String> getReportings(@PathVariable("eid") String eid) {
		return portfolioResponseService.getReportings(eid);
	}

	@GetMapping("/executivesHierarchy/{eid}/{businessUnit}")
	public List<PortfolioResponse> getExecutivesHierarchy(@PathVariable("eid") String eid,
			@PathVariable("businessUnit") String businessUnit) {
		return portfolioResponseService.findByExecutivesHierarchy(eid, businessUnit);
	}

	@GetMapping("/executives/{businessUnit}")
	public List<PortfolioResponse> getPortfolios(@PathVariable("businessUnit") String businessUnit) {
		return portfolioResponseService.findByBusinessUnit(businessUnit);
	}

	@GetMapping("/{id}/products")
	public List<BuildingBlocks> getBuildingBlocksProducts(@PathVariable("id") String id) {
		return buildingBlocksService.getBuildingBlocksProducts(id);
	}

	@GetMapping("/{id}/executiveFavs")
	public List<BuildingBlocks> getPortfolioExecutivesFav(@PathVariable("id") String id) {
		return buildingBlocksService.getPortfolioProductsFavs(id);
	}

	@GetMapping("/{id}/executives")
	public List<BuildingBlocks> getBuildingBlocksPortfolios(@PathVariable("id") String id) {
		return buildingBlocksService.getBuildingBlocksPortfolios(id);
	}

	@GetMapping("/{id}/allexecutives")
	public List<BuildingBlocks> getAllPortfolioExecutives(@PathVariable("id") String[] eidList) {
		return buildingBlocksService.getAllPortfolioProducts(eidList);
	}

	@GetMapping("/{portfolioId}/products/{productId}")
	public BuildingBlocks getPorfolioProduct(@PathVariable("portfolioId") String portfolioId,
			@PathVariable("productId") String productId) {
		return buildingBlocksService.getBuildingBlocksProducts(portfolioId, productId);
	}

	@GetMapping("/{portfolioId}/products/{productId}/components")
	public List<BuildingBlocks> getProductComponents(@PathVariable("portfolioId") String portfolioId,
			@PathVariable("productId") String productId) {
		return buildingBlocksService.getBuildingBlocksComponents(productId);
	}

	@GetMapping("/configapps/{portfolioId}")
	public List<String> getConfigApps(@PathVariable("portfolioId") String portfolioId) {
		return portfolioResponseService.getConfigApps(portfolioId);
	}

}
