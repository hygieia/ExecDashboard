package com.capitalone.dashboard.executive.config;

import com.capitalone.dashboard.executive.service.MetricsService;
import com.capitalone.dashboard.executive.service.PortfolioService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {
    @Bean
    public PortfolioService portfolioService() {
        return Mockito.mock(PortfolioService.class);
    }

    @Bean
    public MetricsService metricsService() {
        return Mockito.mock(MetricsService.class);
    }


}
