package com.capitalone.dashboard.executive.service;

import java.util.List;

import com.capitalone.dashboard.exec.model.MetricLevel;
import com.capitalone.dashboard.exec.model.MetricType;
import com.capitalone.dashboard.exec.model.vz.BuildingBlocks;

public interface BuildingBlocksService {

	List<BuildingBlocks> getBuildingBlocks(MetricLevel portfolio, MetricType metricType, String portfolioId);

	List<BuildingBlocks> getBuildingBlocksProducts(String id);

	List<BuildingBlocks> getPortfolioProductsFavs(String id);

	List<BuildingBlocks> getBuildingBlocksPortfolios(String id);

	List<BuildingBlocks> getAllPortfolioProducts(String[] idList);

	List<BuildingBlocks> getBuildingBlocksComponents(String productId);

	List<BuildingBlocks> getBuildingBlocksComponentsForMetric(String productId, MetricType metricType);
	
	BuildingBlocks getBuildingBlocksProducts(String portfolioId, String productId);

}
