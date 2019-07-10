package com.capitalone.dashboard.executive.service;

import com.capitalone.dashboard.exec.model.BuildingBlockMetricSummary;
import com.capitalone.dashboard.exec.model.MetricDetails;
import com.capitalone.dashboard.exec.model.MetricType;

import java.util.List;

public interface MetricsService {
	MetricDetails getPortfolioMetricDetail(MetricType metricType, String name, String lob);

	MetricDetails getPortfolioMetricSummary(MetricType metricType, String name, String lob);

	List<BuildingBlockMetricSummary> getPortfolioMetricProducts(MetricType metricType, String name, String lob);

	MetricDetails getProductMetricSummary(MetricType metricType, String name, String lob, String productName);

	MetricDetails getProductMetricDetail(MetricType metricType, String name, String lob, String productName);

	List<BuildingBlockMetricSummary> getProductMetricComponents(MetricType metricType, String name, String lob,
			String productName);

}