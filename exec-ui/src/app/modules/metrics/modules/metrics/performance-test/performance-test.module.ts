import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FlexLayoutModule} from '@angular/flex-layout';

import {SharedModule} from '../../../../shared/shared.module';
import {MetricMapService} from '../../shared/services/metric-map.service';
import {DashboardSharedModule} from '../../shared/shared.module';
import {PerformanceTestGraphStrategy} from './strategies/performance-test-graph-strategy';
import {PerformanceTestPreviewStrategy} from './strategies/performance-test-preview-strategy';
import {PerformanceTestDetailStrategy} from './strategies/performance-test-detail-strategy';
import {PerformanceTestSegmentationStrategy} from "./strategies/performance-test-segmentation-strategy";
import {PerformanceTestBuildingBlocksStrategy} from './strategies/performance-test-building-blocks-strategy';
import {PerformanceTestPrimaryMetricStrategy} from './strategies/performance-test-primary-metric-strategy';
import {PerformanceTestTrendStrategy} from './strategies/performance-test-trend-strategy';
import {PerformanceTestDetailComponent} from './components/metric-detail/performance-test-detail.component';
import {PerformanceTestPreviewComponent} from './components/metric-preview/performance-test-preview.component';
import {PerformanceTestRoutingModule} from './performance-test.routing.module';
import {PerformanceTestAuxiliaryErrorRateFigureStrategy} from "./strategies/performance-test-auxiliary-error-rate-figure-strategy";
import {PerformanceTestAuxiliaryTPSFigureStrategy} from "./strategies/performance-test-auxiliary-tps-figure-strategy";
import {PerformanceTestAuxiliaryResponseTimeFigureStrategy} from "./strategies/performance-test-auxiliary-response-time-figure-strategy";

@NgModule({
  imports: [
    FlexLayoutModule,
    CommonModule,
    SharedModule,
    DashboardSharedModule,
    PerformanceTestRoutingModule
  ],
  declarations: [
    PerformanceTestPreviewComponent,
    PerformanceTestDetailComponent
  ],
  exports: [
    PerformanceTestPreviewComponent,
    PerformanceTestDetailComponent
  ],
  providers: [
    MetricMapService,
    PerformanceTestTrendStrategy,
    PerformanceTestPrimaryMetricStrategy,
    PerformanceTestBuildingBlocksStrategy,
    PerformanceTestSegmentationStrategy,
    PerformanceTestDetailStrategy,
    PerformanceTestPreviewStrategy,
    PerformanceTestGraphStrategy,
    PerformanceTestAuxiliaryErrorRateFigureStrategy,
    PerformanceTestAuxiliaryTPSFigureStrategy,
    PerformanceTestAuxiliaryResponseTimeFigureStrategy
  ]
})

export class PerformanceTestModule {}
