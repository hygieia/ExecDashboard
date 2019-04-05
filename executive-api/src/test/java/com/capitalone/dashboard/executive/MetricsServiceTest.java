package com.capitalone.dashboard.executive;

import com.capitalone.dashboard.exec.model.BuildingBlockMetricSummary;
import com.capitalone.dashboard.exec.model.MetricDetails;
import com.capitalone.dashboard.exec.model.MetricLevel;
import com.capitalone.dashboard.exec.model.MetricType;
import com.capitalone.dashboard.exec.model.RoleRelationShipType;
import com.capitalone.dashboard.exec.repository.PortfolioMetricRepository;
import com.capitalone.dashboard.exec.repository.PortfolioRepository;
import com.capitalone.dashboard.executive.common.TestUtils;
import com.capitalone.dashboard.executive.model.PortfolioResponse;
import com.capitalone.dashboard.executive.service.MetricsServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MetricsServiceTest {
    @Mock
    private PortfolioRepository portfolioRepository;
    @Mock
    private PortfolioMetricRepository portfolioMetricRepository;
    @InjectMocks
    private MetricsServiceImpl metricsService;

    @Test
    public void testGetPortfolioMetricSummary(){
        when(portfolioMetricRepository.findByNameAndLobAndType(any(String.class),any(String.class),any(MetricType.class))).thenReturn(TestUtils.makePortfolioMetricDetail());
        MetricDetails metricDetails = metricsService.getPortfolioMetricSummary(MetricType.SCM_COMMITS,"test","LOB");
        Assert.assertEquals(metricDetails.getName(),"PortfolioMetricDetail");
    }

    @Test
    public void testGetPortfolioMetricProducts(){
        when(portfolioRepository.findByNameAndLob(any(String.class),any(String.class))).thenReturn(TestUtils.makePortfolio(MetricLevel.PORTFOLIO,RoleRelationShipType.AppServiceOwner));
        when(portfolioMetricRepository.findByNameAndLobAndType(any(String.class),any(String.class),any(MetricType.class))).thenReturn(TestUtils.makePortfolioMetricDetail());
        List<BuildingBlockMetricSummary> buildingBlockMetricSummaries = metricsService.getPortfolioMetricProducts(MetricType.SCM_COMMITS,"Product1","LOB");
        BuildingBlockMetricSummary buildingBlockMetricSummary = buildingBlockMetricSummaries.get(0);
        Assert.assertEquals(buildingBlockMetricSummary.getName(),"Product1");
        Assert.assertEquals(buildingBlockMetricSummary.getReportingComponents(),0);
        Assert.assertEquals(buildingBlockMetricSummary.getTotalComponents(),0);
        Assert.assertEquals(buildingBlockMetricSummary.getTotalExpectedMetrics(),1);

    }

    @Test
    public void  testGetProductMetricSummary(){
        when(portfolioMetricRepository.findByNameAndLobAndType(any(String.class),any(String.class),any(MetricType.class))).thenReturn(TestUtils.makePortfolioMetricDetail());
        MetricDetails metricDetails = metricsService.getProductMetricSummary(MetricType.SCM_COMMITS,"Product1","LOB","Product1");
        Assert.assertEquals(metricDetails.getName(),"Product1");
        Assert.assertEquals(metricDetails.isProcessed(),false);
        Assert.assertEquals(metricDetails.getTotalComponents(),0);
        Assert.assertEquals(metricDetails.getReportingComponents(),0);
    }

    @Test
    public void testGetProductMetricComponents(){
        when(portfolioRepository.findByNameAndLob(any(String.class),any(String.class))).thenReturn(TestUtils.makePortfolio(MetricLevel.PORTFOLIO,RoleRelationShipType.AppServiceOwner));
        when(portfolioMetricRepository.findByNameAndLobAndType(any(String.class),any(String.class),any(MetricType.class))).thenReturn(TestUtils.makePortfolioMetricDetail());
        List<BuildingBlockMetricSummary> buildingBlockMetricSummaries = metricsService.getProductMetricComponents(MetricType.SCM_COMMITS,"Product1","LOB","Product1");
        BuildingBlockMetricSummary buildingBlockMetricSummary = buildingBlockMetricSummaries.get(0);
        Assert.assertEquals(buildingBlockMetricSummary.getName(),"Product1");
        Assert.assertEquals(buildingBlockMetricSummary.getReportingComponents(),0);
        Assert.assertEquals(buildingBlockMetricSummary.getTotalComponents(),0);
        Assert.assertEquals(buildingBlockMetricSummary.getTotalExpectedMetrics(),0);

    }

}
