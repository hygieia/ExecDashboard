package com.capitalone.dashboard.executive.service;

import com.capitalone.dashboard.exec.model.BuildingBlockMetricSummary;
import com.capitalone.dashboard.executive.model.PortfolioResponse;

import java.util.List;

public interface PortfolioService {
   List<PortfolioResponse> getPortfolios();

   PortfolioResponse getPortfolio(String businessOwnerName, String businessOwnerLob);

   List<BuildingBlockMetricSummary> getPortfolioProducts(String businessOwnerName, String businessOwnerLob);

   BuildingBlockMetricSummary getPortfolioProduct(String businessOwnerName, String businessOwnerLob,
                                                  String productName);

   List<BuildingBlockMetricSummary> getProductComponents(String businessOwnerName, String businessOwnerLob,
                                                         String productName);
}
