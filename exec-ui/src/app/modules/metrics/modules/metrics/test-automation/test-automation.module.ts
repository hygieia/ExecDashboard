import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FlexLayoutModule} from '@angular/flex-layout';

import {SharedModule} from '../../../../shared/shared.module';
import {MetricMapService} from '../../shared/services/metric-map.service';
import {DashboardSharedModule} from '../../shared/shared.module';
import {TestAutomationGraphStrategy} from './strategies/test-automation-graph-strategy';
import {TestAutomationPreviewStrategy} from './strategies/test-automation-preview-strategy';
import {TestAutomationDetailStrategy} from './strategies/test-automation-detail-strategy';
import {TestAutomationBuildingBlocksStrategy} from './strategies/test-automation-building-blocks-strategy';
import {TestAutomationPrimaryMetricStrategy} from './strategies/test-automation-primary-metric-strategy';
import {TestAutomationTrendStrategy} from './strategies/test-automation-trend-strategy';
import {TestAutomationDetailComponent} from './components/metric-detail/test-automation-detail.component';
import {TestAutomationPreviewComponent} from './components/metric-preview/test-automation-preview.component';
import {TestAutomationRoutingModule} from './test-automation.routing.module';

@NgModule({
  imports: [
    FlexLayoutModule,
    CommonModule,
    SharedModule,
    DashboardSharedModule,
    TestAutomationRoutingModule
  ],
  declarations: [
    TestAutomationPreviewComponent,
    TestAutomationDetailComponent
  ],
  exports: [
    TestAutomationPreviewComponent,
    TestAutomationDetailComponent
  ],
  providers: [
    MetricMapService,
    TestAutomationTrendStrategy,
    TestAutomationPrimaryMetricStrategy,
    TestAutomationBuildingBlocksStrategy,
    TestAutomationDetailStrategy,
    TestAutomationPreviewStrategy,
    TestAutomationGraphStrategy
  ]
})

export class TestAutomationModule {}
