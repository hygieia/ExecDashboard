import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FlexLayoutModule} from '@angular/flex-layout';

import {SharedModule} from '../../../../shared/shared.module';
import {MetricMapService} from '../../shared/services/metric-map.service';
import {DashboardSharedModule} from '../../shared/shared.module';
import {UnitTestCoverageGraphStrategy} from './strategies/unit-test-coverage-graph-strategy';
import {UnitTestCoveragePreviewStrategy} from './strategies/unit-test-coverage-preview-strategy';
import {UnitTestCoverageDetailStrategy} from './strategies/unit-test-coverage-detail-strategy';
import {UnitTestCoverageBuildingBlocksStrategy} from './strategies/unit-test-coverage-building-blocks-strategy';
import {UnitTestCoveragePrimaryMetricStrategy} from './strategies/unit-test-coverage-primary-metric-strategy';
import {UnitTestCoverageTrendStrategy} from './strategies/unit-test-coverage-trend-strategy';
import {UnitTestCoverageDetailComponent} from './components/metric-detail/unit-test-coverage-detail.component';
import {UnitTestCoveragePreviewComponent} from './components/metric-preview/unit-test-coverage-preview.component';
import {UnitTestCoverageRoutingModule} from './unit-test-coverage.routing.module';

@NgModule({
  imports: [
    FlexLayoutModule,
    CommonModule,
    SharedModule,
    DashboardSharedModule,
    UnitTestCoverageRoutingModule
  ],
  declarations: [
    UnitTestCoveragePreviewComponent,
    UnitTestCoverageDetailComponent
  ],
  exports: [
    UnitTestCoveragePreviewComponent,
    UnitTestCoverageDetailComponent
  ],
  providers: [
    MetricMapService,
    UnitTestCoverageTrendStrategy,
    UnitTestCoveragePrimaryMetricStrategy,
    UnitTestCoverageBuildingBlocksStrategy,
    UnitTestCoverageDetailStrategy,
    UnitTestCoveragePreviewStrategy,
    UnitTestCoverageGraphStrategy
  ]
})

export class UnitTestCoverageModule {}
