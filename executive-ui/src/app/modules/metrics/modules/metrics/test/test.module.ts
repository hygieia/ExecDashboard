import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FlexLayoutModule} from '@angular/flex-layout';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {SharedModule} from '../../../../shared/shared.module';
import {MetricMapService} from '../../shared/services/metric-map.service';
import {DashboardSharedModule} from '../../shared/shared.module';
import {TestPreviewComponent} from './components/metric-preview/test-preview.component';
import {TestDetailComponent} from './components/metric-detail/test-detail.component';
import {TestTrendStrategy} from './strategies/test-trend-strategy';
import {TestPrimaryMetricStrategy} from './strategies/test-primary-metric-strategy';
import {TestBuildingBlocksStrategy} from './strategies/test-building-blocks-strategy';
import {TestDetailStrategy} from './strategies/test-detail-strategy';
import {TestPreviewStrategy} from './strategies/test-preview-strategy';
import {TestSegmentationStrategy} from './strategies/test-segmentation-strategy';
import {TestGraphStrategy} from './strategies/test-graph-strategy';
import {TestRoutingModule} from './test.routing.module';
import {TestAuxiliaryFigureStrategy} from './strategies/test-auxiliary-figure-strategy';
import { Ng4LoadingSpinnerModule, Ng4LoadingSpinnerComponent, Ng4LoadingSpinnerService } from 'ng4-loading-spinner';

@NgModule({
  imports: [
    FlexLayoutModule,
    CommonModule,
    SharedModule,
    DashboardSharedModule,
     Ng4LoadingSpinnerModule,
    TestRoutingModule, FormsModule, ReactiveFormsModule
  ],
  declarations: [
    TestPreviewComponent,
    TestDetailComponent
  ],
  exports: [
    TestPreviewComponent,
    TestDetailComponent
  ],
  providers: [
    MetricMapService,
    TestTrendStrategy,
    TestAuxiliaryFigureStrategy,
    TestPrimaryMetricStrategy,
    TestBuildingBlocksStrategy,
    TestDetailStrategy,
    TestPreviewStrategy,
    TestSegmentationStrategy,
    TestGraphStrategy
  ]
})

export class TestModule {}
