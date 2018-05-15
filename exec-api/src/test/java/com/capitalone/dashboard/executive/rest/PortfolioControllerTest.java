package com.capitalone.dashboard.executive.rest;

import com.capitalone.dashboard.exec.model.MetricLevel;
import com.capitalone.dashboard.exec.model.RoleRelationShipType;
import com.capitalone.dashboard.executive.common.TestUtils;
import com.capitalone.dashboard.executive.config.TestConfig;
import com.capitalone.dashboard.executive.config.WebMVCConfig;
import com.capitalone.dashboard.executive.model.PortfolioResponse;
import com.capitalone.dashboard.executive.service.PortfolioService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class, WebMVCConfig.class})
@WebAppConfiguration
public class PortfolioControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private PortfolioService portfolioService;

    @Before
    public void before() {
        SecurityContextHolder.clearContext();
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void portfolio() throws Exception {
        when(portfolioService.getPortfolio("portfolio1", "LOB")).thenReturn(PortfolioResponse.getPortfolio(TestUtils.makePortfolio(MetricLevel.PORTFOLIO, RoleRelationShipType.BusinessOwner)));
        mockMvc.perform(MockMvcRequestBuilders.get("/portfolio/portfolio1/LOB"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(portfolioService).getPortfolio("portfolio1", "LOB");
    }

    @Test
    public void getPortfolioProducts() throws Exception {
        when(portfolioService.getPortfolioProducts("test", "lob")).thenReturn(Stream.of(TestUtils.makeBuildingBlockMetricSummary()).collect(Collectors.toList()));
        mockMvc.perform(MockMvcRequestBuilders.get("/portfolio/test/lob/products"))
                .andExpect(status().isOk());
        verify(portfolioService).getPortfolioProducts("test", "lob");
    }

    @Test
    public void getPortfolioProduct() throws Exception {
        when(portfolioService.getPortfolioProduct("test", "LOB","product1")).thenReturn(TestUtils.makeBuildingBlockMetricSummary());
        mockMvc.perform(MockMvcRequestBuilders.get("/portfolio/test/lob/products/product1"))
                .andExpect(status().isOk());
        verify(portfolioService).getPortfolioProduct("test", "lob","product1");
    }

    @Test
    public void getProductComponents() throws Exception {
        when(portfolioService.getProductComponents("test", "LOB","product1")).thenReturn(Stream.of(TestUtils.makeBuildingBlockMetricSummary()).collect(Collectors.toList()));
        mockMvc.perform(MockMvcRequestBuilders.get("/portfolio/test/lob/products/product1/components"))
                .andExpect(status().isOk());
        verify(portfolioService).getProductComponents("test", "lob","product1");
    }
}