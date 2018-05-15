package com.capitalone.dashboard.executive.rest;

import com.capitalone.dashboard.exec.model.BuildingBlockMetricSummary;
import com.capitalone.dashboard.executive.model.PortfolioResponse;
import com.capitalone.dashboard.executive.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/portfolio")
@CrossOrigin
public class PortfolioController {
    private final PortfolioService portfolioService;

    @Autowired
    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @GetMapping()
    public List<PortfolioResponse> getPortfolios() {
        return portfolioService.getPortfolios();
    }

    @GetMapping("/{name}/{lob}")
    public PortfolioResponse getPortfolio(@PathVariable("name") String name, @PathVariable("lob") String lob) {
        return portfolioService.getPortfolio(name, lob);
    }

    @GetMapping("/{name}/{lob}/products")
    public List<BuildingBlockMetricSummary> getPortfolioProducts(@PathVariable("name") String name,
                                                                         @PathVariable("lob") String lob) {
        return portfolioService.getPortfolioProducts(name, lob);
    }

    @GetMapping("/{name}/{lob}/products/{productName}")
    public BuildingBlockMetricSummary getPortfolioProduct(
            @PathVariable("name") String name,
            @PathVariable("lob") String lob,
            @PathVariable("productName") String productName) {
        return portfolioService.getPortfolioProduct(name, lob, productName);
    }

    @GetMapping("/{name}/{lob}/products/{productName}/components")
    public List<BuildingBlockMetricSummary> getProductComponents(
            @PathVariable("name") String name,
            @PathVariable("lob") String lob,
            @PathVariable("productName") String productName) {
        return portfolioService.getProductComponents(name, lob, productName);
    }
}
