package com.capitalone.dashboard.executive.rest;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capitalone.dashboard.exec.model.MetricLevel;
import com.capitalone.dashboard.exec.model.MetricSummary;
import com.capitalone.dashboard.exec.model.MetricType;
import com.capitalone.dashboard.exec.model.vz.BuildingBlocks;
import com.capitalone.dashboard.exec.model.vz.MetricsDetail;
import com.capitalone.dashboard.executive.service.BuildingBlocksService;
import com.capitalone.dashboard.executive.service.MetricsDetailService;

/**
 * MetricsController class
 */
@RestController
@RequestMapping(value = "/metrics")
@CrossOrigin
public class MetricsController {

	private final MetricsDetailService metricsDetailService;
	private final BuildingBlocksService buildingBlocksService;

	/**
	 * 
	 * @param metricsDetailService
	 * @param buildingBlocksService
	 */
	public MetricsController(MetricsDetailService metricsDetailService, BuildingBlocksService buildingBlocksService) {
		this.metricsDetailService = metricsDetailService;
		this.buildingBlocksService = buildingBlocksService;
	}

	@GetMapping("/{metric}/portfolio/{id}/summary")
	public MetricSummary getPortfolioMetricSummary(@PathVariable("metric") MetricType metricType,
			@PathVariable("id") String portfolioId) {
		return metricsDetailService.getMetricSummary(MetricLevel.PORTFOLIO, metricType, portfolioId);
	}

	@GetMapping("/{metric}/portfolio/{id}/detail")
	public MetricsDetail getPortfolioMetricDetail(@PathVariable("metric") MetricType metricType,
			@PathVariable("id") String portfolioId) {
		return metricsDetailService.getMetricsDetail(MetricLevel.PORTFOLIO, metricType, portfolioId);
	}

	@GetMapping("/{metric}/portfolio/{id}/product")
	public List<BuildingBlocks> getPortfolioMetricProducts(@PathVariable("metric") MetricType metricType,
			@PathVariable("id") String portfolioId) {
		return buildingBlocksService.getBuildingBlocks(MetricLevel.PRODUCT, metricType, portfolioId);
	}

	@GetMapping("/{metric}/product/{id}/summary")
	public MetricSummary getProductMetricSummary(@PathVariable("metric") MetricType metricType,
			@PathVariable("id") String productId) {
		return metricsDetailService.getMetricSummaryForProducts(MetricLevel.PRODUCT, metricType, productId);
	}

	@GetMapping("/{metric}/product/{id}/detail")
	public MetricsDetail getProductMetricDetail(@PathVariable("metric") MetricType metricType,
			@PathVariable("id") String productId) {
		return metricsDetailService.getMetricsDetailForProducts(MetricLevel.PRODUCT, metricType, productId);
	}

	@GetMapping("/{metric}/product/{id}/component")
	public List<BuildingBlocks> getProductMetricComponents(@PathVariable("metric") MetricType metricType,
			@PathVariable("id") String productId) {
		return buildingBlocksService.getBuildingBlocksComponentsForMetric(productId, metricType);
	}

}
