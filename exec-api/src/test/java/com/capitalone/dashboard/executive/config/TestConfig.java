package com.capitalone.dashboard.executive.config;

import com.capitalone.dashboard.executive.service.ApplicationService;
import com.capitalone.dashboard.executive.service.AuthenticationService;
import com.capitalone.dashboard.executive.service.BuildingBlocksService;
import com.capitalone.dashboard.executive.service.CollectorUpdatedStatusService;
import com.capitalone.dashboard.executive.service.DefaultHygieiaService;
import com.capitalone.dashboard.executive.service.DigitalCockpitService;
import com.capitalone.dashboard.executive.service.ExternalMonitorService;
import com.capitalone.dashboard.executive.service.HygieiaInstanceService;
import com.capitalone.dashboard.executive.service.MetricsDetailService;
import com.capitalone.dashboard.executive.service.MetricsService;
import com.capitalone.dashboard.executive.service.PortfolioService;
import com.capitalone.dashboard.executive.service.UserInfoService;
import com.capitalone.dashboard.executive.service.UtilityService;
import com.capitalone.dashboard.executive.service.VastService;
import com.capitalone.dashboard.executive.service.VonkinatorService;
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

    @Bean
    public ApplicationService applicationService() { return Mockito.mock(ApplicationService.class); }

    @Bean
    public AuthenticationService authenticationService() { return Mockito.mock(AuthenticationService.class); }

    @Bean
    public BuildingBlocksService buildingBlocksService() { return Mockito.mock(BuildingBlocksService.class); }

    @Bean
    public CollectorUpdatedStatusService collectorUpdatedStatusService() { return Mockito.mock(CollectorUpdatedStatusService.class); }

    @Bean
    public DefaultHygieiaService defaultHygieiaService() { return Mockito.mock(DefaultHygieiaService.class); }

    @Bean
    public DigitalCockpitService digitalCockpitService() { return Mockito.mock(DigitalCockpitService.class); }

    @Bean
    public ExternalMonitorService externalMonitorService() { return Mockito.mock(ExternalMonitorService.class); }

    @Bean
    public HygieiaInstanceService hygieiaInstanceService() { return Mockito.mock(HygieiaInstanceService.class); }

    @Bean
    public MetricsDetailService metricsDetailService() { return Mockito.mock(MetricsDetailService.class); }

    @Bean
    public UtilityService utilityService() { return Mockito.mock(UtilityService.class); }

    @Bean
    public UserInfoService userInfoService() { return Mockito.mock(UserInfoService.class); }

    @Bean
    public VastService vastService() { return Mockito.mock(VastService.class); }

    @Bean
    public VonkinatorService vonkinatorService() { return Mockito.mock(VonkinatorService.class); }
}
