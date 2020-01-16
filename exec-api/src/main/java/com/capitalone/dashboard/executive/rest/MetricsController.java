package com.capitalone.dashboard.executive.rest;

import com.capitalone.dashboard.exec.model.BuildingBlockMetricSummary;
import com.capitalone.dashboard.exec.model.BuildingBlocks;
import com.capitalone.dashboard.exec.model.MetricDetails;
import com.capitalone.dashboard.exec.model.MetricLevel;
import com.capitalone.dashboard.exec.model.MetricSummary;
import com.capitalone.dashboard.exec.model.MetricType;
import com.capitalone.dashboard.exec.model.MetricsDetail;
import com.capitalone.dashboard.executive.service.MetricsDetailService;
import com.capitalone.dashboard.executive.service.MetricsService;
import com.capitalone.dashboard.executive.service.BuildingBlocksService;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.beans.PropertyEditorSupport;
import java.util.List;

@RestController
@RequestMapping("/metrics")
@CrossOrigin
public class MetricsController {
    //TODO: Need to merge both vz and default Hygieia project
    private final MetricsService metricsService;
    private final MetricsDetailService metricsDetailService;
    private final BuildingBlocksService buildingBlocksService;

    public MetricsController(MetricsService metricsService,
                             BuildingBlocksService buildingBlocksService,
                             MetricsDetailService metricsDetailService) {

        this.metricsService = metricsService;
        this.buildingBlocksService = buildingBlocksService;
        this.metricsDetailService = metricsDetailService;
    }

    @GetMapping("/{metric}/lob/{name}/{lob}/summary")
    public MetricDetails getLobMetricSummary(
            @PathVariable("metric") MetricType metricType,
            @PathVariable("name") String name,
            @PathVariable("lob") String lob) {

        return metricsService.getLobMetricSummary(metricType, name, lob);
    }

    @GetMapping("/{metric}/lob/{name}/{lob}/detail")
    public MetricDetails getLobMetricDetail(
            @PathVariable("metric") MetricType metricType,
            @PathVariable("name") String name,
            @PathVariable("lob") String lob) {

        return metricsService.getLobMetricDetail(metricType, name, lob);
    }

    @GetMapping("/{metric}/lob/{name}/{lob}/product")
    public List<BuildingBlockMetricSummary> getLobMetricProducts(
            @PathVariable("metric") MetricType metricType,
            @PathVariable("name") String name,
            @PathVariable("lob") String lob) {

        return metricsService.getLobMetricProducts(metricType, name, lob);
    }

    @GetMapping("/{metric}/portfolio/{name}/{lob}/summary")
    public MetricDetails getPortfolioMetricSummary(
            @PathVariable("metric") MetricType metricType,
            @PathVariable("name") String name,
            @PathVariable("lob") String lob) {

        return metricsService.getPortfolioMetricSummary(metricType, name, lob);
    }

    @GetMapping("/{metric}/portfolio/{name}/{lob}/detail")
    public MetricDetails getPortfolioMetricDetail(
            @PathVariable("metric") MetricType metricType,
            @PathVariable("name") String name,
            @PathVariable("lob") String lob) {

        return metricsService.getPortfolioMetricDetail(metricType, name, lob);
    }

    @GetMapping("/{metric}/portfolio/{name}/{lob}/product")
    public List<BuildingBlockMetricSummary> getPortfolioMetricProducts(
            @PathVariable("metric") MetricType metricType,
            @PathVariable("name") String name,
            @PathVariable("lob") String lob) {

        return metricsService.getPortfolioMetricProducts(metricType, name, lob);
    }

    @GetMapping("/{metric}/product/{name}/{lob}/{productName}/summary")
    public MetricDetails getProductMetricSummary(
            @PathVariable("metric") MetricType metricType,
            @PathVariable("name") String name,
            @PathVariable("lob") String lob,
            @PathVariable("productName") String productName) {

        return metricsService.getProductMetricSummary(metricType, name, lob, productName);
    }

    @GetMapping("/{metric}/product/{name}/{lob}/{productName}/detail")
    public MetricDetails getProductMetricDetail(
            @PathVariable("metric") MetricType metricType,
            @PathVariable("name") String name,
            @PathVariable("lob") String lob,
            @PathVariable("productName") String productName) {

        return metricsService.getProductMetricDetail(metricType, name, lob, productName);
    }

    @GetMapping("/{metric}/product/{name}/{lob}/{productName}/component")
    public List<BuildingBlockMetricSummary> getProductMetricComponents(
            @PathVariable("metric") MetricType metricType,
            @PathVariable("name") String name,
            @PathVariable("lob") String lob,
            @PathVariable("productName") String productName) {

        return metricsService.getProductMetricComponents(metricType, name, lob, productName);
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
    @InitBinder
    public void initBinder(final WebDataBinder webdataBinder) {
        webdataBinder.registerCustomEditor(MetricType.class, new MetricTypeConverter());
    }

    private static class MetricTypeConverter extends PropertyEditorSupport {
        public void setAsText(final String text) {
            setValue(MetricType.fromString(text));
        }
    }
}
