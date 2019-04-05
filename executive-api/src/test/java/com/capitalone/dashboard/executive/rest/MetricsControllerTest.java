package com.capitalone.dashboard.executive.rest;

import com.capitalone.dashboard.exec.model.MetricType;
import com.capitalone.dashboard.executive.common.TestUtils;
import com.capitalone.dashboard.executive.config.TestConfig;
import com.capitalone.dashboard.executive.config.WebMVCConfig;
import com.capitalone.dashboard.executive.service.MetricsService;
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
public class MetricsControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private MetricsService metricsService;

    @Before
    public void before() {
        SecurityContextHolder.clearContext();
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void getPortfolioMetricSummary() throws Exception {
        when(metricsService.getPortfolioMetricSummary(MetricType.SCM_COMMITS,"portfolio1", "lob")).thenReturn(TestUtils.makeProductMetricDetail());
        mockMvc.perform(MockMvcRequestBuilders.get("/metrics/SCM/portfolio/portfolio1/lob/summary"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(metricsService).getPortfolioMetricSummary(null,"portfolio1", "lob");
    }

    @Test
    public void getPortfolioMetricProducts() throws Exception {
        when(metricsService.getPortfolioMetricProducts(MetricType.SCM_COMMITS,"portfolio1", "lob")).thenReturn(Stream.of(TestUtils.makeBuildingBlockMetricSummary()).collect(Collectors.toList()));
        mockMvc.perform(MockMvcRequestBuilders.get("/metrics/SCM/portfolio/portfolio1/lob/product"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(metricsService).getPortfolioMetricProducts(null,"portfolio1", "lob");
    }

    @Test
    public void getProductMetricDetail() throws Exception {
        when(metricsService.getProductMetricDetail(MetricType.SCM_COMMITS,"portfolio1", "lob","product1")).thenReturn(TestUtils.makeProductMetricDetail());
        mockMvc.perform(MockMvcRequestBuilders.get("/metrics/SCM/product/portfolio1/lob/product1/detail"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(metricsService).getProductMetricDetail(null,"portfolio1", "lob","product1");
    }

    @Test
    public void getProductMetricSummary() throws Exception {
        when(metricsService.getProductMetricSummary(MetricType.SCM_COMMITS,"portfolio1", "lob","product1")).thenReturn(TestUtils.makeProductMetricDetail());
        mockMvc.perform(MockMvcRequestBuilders.get("/metrics/SCM/product/portfolio1/lob/product1/summary"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(metricsService).getProductMetricSummary(null,"portfolio1", "lob","product1");
    }

    @Test
    public void getPortfolioMetricDetail() throws Exception {
        when(metricsService.getPortfolioMetricDetail(MetricType.SCM_COMMITS,"portfolio1", "lob")).thenReturn(TestUtils.makeProductMetricDetail());
        mockMvc.perform(MockMvcRequestBuilders.get("/metrics/SCM/portfolio/portfolio1/lob/detail"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(metricsService).getPortfolioMetricDetail(null,"portfolio1", "lob");
    }

    @Test
    public void getProductMetricComponents() throws Exception {
        when(metricsService.getProductMetricComponents(MetricType.SCM_COMMITS,"portfolio1", "lob","product1")).thenReturn(Stream.of(TestUtils.makeBuildingBlockMetricSummary()).collect(Collectors.toList()));
        mockMvc.perform(MockMvcRequestBuilders.get("/metrics/SCM/product/portfolio1/lob/product1/component"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(metricsService).getProductMetricComponents(null,"portfolio1", "lob","product1");
    }


}