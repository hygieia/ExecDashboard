package com.capitalone.dashboard.executive.service;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capitalone.dashboard.exec.model.MetricLevel;
import com.capitalone.dashboard.exec.model.MetricSummary;
import com.capitalone.dashboard.exec.model.MetricType;
import com.capitalone.dashboard.exec.model.BuildingBlocks;
import com.capitalone.dashboard.exec.model.MetricsDetail;
import com.capitalone.dashboard.exec.model.PortfolioResponse;
import com.capitalone.dashboard.exec.repository.BuildingBlocksRepository;
import com.capitalone.dashboard.exec.repository.MetricsDetailRepository;
import com.capitalone.dashboard.exec.repository.PortfolioResponseRepository;

@Service
public class MetricsDetailServiceImpl implements MetricsDetailService {

    private MetricsDetailRepository metricsDetailRepository;
    private PortfolioResponseRepository portfolioResponseRepository;
    private BuildingBlocksRepository buildingBlocksRepository;

    @Autowired
    public MetricsDetailServiceImpl(MetricsDetailRepository metricsDetailRepository,
                                    PortfolioResponseRepository portfolioResponseRepository,
                                    BuildingBlocksRepository buildingBlocksRepository) {
        this.metricsDetailRepository = metricsDetailRepository;
        this.portfolioResponseRepository = portfolioResponseRepository;
        this.buildingBlocksRepository = buildingBlocksRepository;
    }

    @Override
    public MetricSummary getMetricSummary(MetricLevel level, MetricType type, String id) {
        ObjectId portfolioId = new ObjectId(id);
        PortfolioResponse portfolioResponse = portfolioResponseRepository.findOne(portfolioId);
        if (portfolioResponse != null) {
            MetricsDetail metricPortfolioDetailResponse = metricsDetailRepository
                    .findByMetricLevelIdAndLevelAndType(portfolioResponse.getEid(), level, type);
            if (metricPortfolioDetailResponse != null)
                return metricPortfolioDetailResponse.getSummary();
        }
        return null;
    }

    @Override
    public MetricsDetail getMetricsDetail(MetricLevel level, MetricType type, String id) {
        ObjectId portfolioId = new ObjectId(id);
        PortfolioResponse portfolioResponse = portfolioResponseRepository.findOne(portfolioId);
        if (portfolioResponse != null) {
            MetricsDetail metricPortfolioDetailResponse = metricsDetailRepository
                    .findByMetricLevelIdAndLevelAndType(portfolioResponse.getEid(), level, type);
            if (metricPortfolioDetailResponse != null)
                return metricPortfolioDetailResponse;
        }
        return null;
    }

    @Override
    public MetricSummary getMetricSummaryForProducts(MetricLevel level, MetricType type, String productId) {
        ObjectId id = new ObjectId(productId);
        BuildingBlocks buildingBlockMetricSummary = buildingBlocksRepository.findOne(id);
        if (buildingBlockMetricSummary != null) {
            MetricsDetail metricPortfolioDetailResponse = metricsDetailRepository
                    .findByMetricLevelIdAndLevelAndType(buildingBlockMetricSummary.getMetricLevelId(), level, type);
            if (metricPortfolioDetailResponse != null)
                return metricPortfolioDetailResponse.getSummary();
        }
        return null;
    }

    @Override
    public MetricsDetail getMetricsDetailForProducts(MetricLevel level, MetricType type, String productId) {
        ObjectId id = new ObjectId(productId);
        BuildingBlocks buildingBlockMetricSummary = buildingBlocksRepository.findOne(id);
        if (buildingBlockMetricSummary != null) {
            MetricsDetail metricPortfolioDetailResponse = metricsDetailRepository
                    .findByMetricLevelIdAndLevelAndType(buildingBlockMetricSummary.getMetricLevelId(), level, type);
            if (metricPortfolioDetailResponse != null)
                return metricPortfolioDetailResponse;
        }
        return null;
    }

}
