package com.capitalone.dashboard.executive;

import com.capitalone.dashboard.exec.model.BuildingBlockMetricSummary;
import com.capitalone.dashboard.exec.model.MetricLevel;
import com.capitalone.dashboard.exec.model.RoleRelationShipType;
import com.capitalone.dashboard.exec.repository.PortfolioMetricRepository;
import com.capitalone.dashboard.exec.repository.PortfolioRepository;
import com.capitalone.dashboard.executive.common.TestUtils;
import com.capitalone.dashboard.executive.model.PortfolioResponse;
import com.capitalone.dashboard.executive.service.PortfolioServiceImpl;
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
public class PortfolioServiceTest {
    @Mock
    private PortfolioRepository portfolioRepository;
    @Mock
    private PortfolioMetricRepository portfolioMetricRepository;
    @InjectMocks
    private PortfolioServiceImpl portfolioService;

    @Test
    public void testGetPortfolioForBusinessOwner(){
        when(portfolioRepository.findByNameAndLob(any(String.class),any(String.class))).thenReturn(TestUtils.makePortfolio(MetricLevel.PORTFOLIO,RoleRelationShipType.BusinessOwner));
        PortfolioResponse portfolioResponse = portfolioService.getPortfolio("test","LOB");
        Assert.assertEquals(portfolioResponse.getName(),"portfolio1");
        Assert.assertEquals(portfolioResponse.getLob(),"LOB");
        Assert.assertEquals(portfolioResponse.getProductList().get(0).getName(),"Product1");
        Assert.assertNotNull(portfolioResponse.getProductList().get(0).getEnvironments());
        Assert.assertEquals(portfolioResponse.getExecutive().getFirstName(),"firstName");

    }

    @Test
    public void testGetPortfolioForAppServiceOwner(){
        when(portfolioRepository.findByNameAndLob(any(String.class),any(String.class))).thenReturn(TestUtils.makePortfolio(MetricLevel.PORTFOLIO,RoleRelationShipType.AppServiceOwner));
        PortfolioResponse portfolioResponse = portfolioService.getPortfolio("test","LOB");
        Assert.assertEquals(portfolioResponse.getName(),"portfolio1");
        Assert.assertEquals(portfolioResponse.getLob(),"LOB");
        Assert.assertEquals(portfolioResponse.getProductList().get(0).getName(),"Product1");
        Assert.assertNotNull(portfolioResponse.getProductList().get(0).getEnvironments());
        Assert.assertNull(portfolioResponse.getExecutive());

    }

    @Test
    public void testGetProductComponents(){
        when(portfolioRepository.findByNameAndLob(any(String.class),any(String.class))).thenReturn(TestUtils.makePortfolio(MetricLevel.PORTFOLIO,RoleRelationShipType.AppServiceOwner));
        when(portfolioMetricRepository.findByNameAndLob(any(String.class),any(String.class))).thenReturn(Stream.of(TestUtils.makePortfolioMetricDetail()).collect(Collectors.toList()));
        List<BuildingBlockMetricSummary> a = portfolioService.getProductComponents("test","LOB","Product1");
        BuildingBlockMetricSummary actual = a.get(0);
        Assert.assertEquals(actual.getMetrics().size(),1);
        Assert.assertEquals(actual.getName(),"product1");
        Assert.assertEquals(actual.getTotalExpectedMetrics(),11);
        Assert.assertEquals(actual.getTotalComponents(),1);
        Assert.assertEquals(actual.getReportingComponents(),0);
    }

    @Test
    public void testGetPortfolios(){
        when(portfolioRepository.findAllByOwnersNotNull()).thenReturn(Stream.of(TestUtils.makePortfolio(MetricLevel.PORTFOLIO,RoleRelationShipType.BusinessOwner)).collect(Collectors.toList()));
        List<PortfolioResponse> portfolioResponseList = portfolioService.getPortfolios();
        PortfolioResponse portfolioResponse = portfolioResponseList.get(0);
        Assert.assertEquals(portfolioResponse.getName(),"portfolio1");
        Assert.assertEquals(portfolioResponse.getLob(),"LOB");
        Assert.assertEquals(portfolioResponse.getProductList().get(0).getName(),"Product1");
        Assert.assertNotNull(portfolioResponse.getProductList().get(0).getEnvironments());
        Assert.assertEquals(portfolioResponse.getExecutive().getFirstName(),"firstName");
    }

    @Test
    public void testGetPortfolioProduct(){
        when(portfolioRepository.findByNameAndLob(any(String.class),any(String.class))).thenReturn(TestUtils.makePortfolio(MetricLevel.PORTFOLIO,RoleRelationShipType.AppServiceOwner));
        when(portfolioMetricRepository.findByNameAndLob(any(String.class),any(String.class))).thenReturn(Stream.of(TestUtils.makePortfolioMetricDetail()).collect(Collectors.toList()));
        BuildingBlockMetricSummary actual = portfolioService.getPortfolioProduct("test","LOB","Product1");
        Assert.assertEquals(actual.getMetrics().size(),1);
        Assert.assertEquals(actual.getName(),"Product1");
        Assert.assertEquals(actual.getTotalExpectedMetrics(),11);
        Assert.assertEquals(actual.getTotalComponents(),1);
        Assert.assertEquals(actual.getReportingComponents(),0);
    }

    @Test
    public void testGetPortfolioProducts(){
        when(portfolioRepository.findByNameAndLob(any(String.class),any(String.class))).thenReturn(TestUtils.makePortfolio(MetricLevel.PORTFOLIO,RoleRelationShipType.AppServiceOwner));
        when(portfolioMetricRepository.findByNameAndLob(any(String.class),any(String.class))).thenReturn(Stream.of(TestUtils.makePortfolioMetricDetail()).collect(Collectors.toList()));
        List<BuildingBlockMetricSummary> a = portfolioService.getPortfolioProducts("Product1","LOB");
        BuildingBlockMetricSummary actual = a.get(0);
        Assert.assertEquals(actual.getMetrics().size(),1);
        Assert.assertEquals(actual.getName(),"Product1");
        Assert.assertEquals(actual.getTotalExpectedMetrics(),11);
        Assert.assertEquals(actual.getTotalComponents(),1);
        Assert.assertEquals(actual.getReportingComponents(),0);
    }

}
