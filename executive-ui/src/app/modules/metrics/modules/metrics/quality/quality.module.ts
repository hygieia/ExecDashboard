import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FlexLayoutModule} from '@angular/flex-layout';

import {SharedModule} from '../../../../shared/shared.module';
import {MetricMapService} from '../../shared/services/metric-map.service';
import {DashboardSharedModule} from '../../shared/shared.module';
import {QualityGraphStrategy} from './strategies/quality-graph-strategy';
import {QualitySegmentationStrategy} from './strategies/quality-segmentation-strategy';
import {QualityPreviewStrategy} from './strategies/quality-preview-strategy';
import {QualityDetailStrategy} from './strategies/quality-detail-strategy';
import {QualityBuildingBlocksStrategy} from './strategies/quality-building-blocks-strategy';
import {QualityPrimaryMetricStrategy} from './strategies/quality-primary-metric-strategy';
import {QualityTrendStrategy} from './strategies/quality-trend-strategy';
import {QualityDetailComponent} from './components/metric-detail/quality-detail.component';
import {QualityPreviewComponent} from './components/metric-preview/quality-preview.component';
import {QualityRoutingModule} from './quality.routing.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Ng4LoadingSpinnerModule, Ng4LoadingSpinnerComponent, Ng4LoadingSpinnerService } from 'ng4-loading-spinner';

@NgModule({
  imports: [
    FlexLayoutModule,
    CommonModule,
    SharedModule,
    Ng4LoadingSpinnerModule,
    DashboardSharedModule, FormsModule, ReactiveFormsModule,
    QualityRoutingModule
  ],
  declarations: [
    QualityPreviewComponent,
    QualityDetailComponent
  ],
  exports: [
    QualityPreviewComponent,
    QualityDetailComponent
  ],
  providers: [
    MetricMapService,
    QualityTrendStrategy,
    QualityPrimaryMetricStrategy,
    QualityBuildingBlocksStrategy,
    QualityDetailStrategy,
    QualityPreviewStrategy,
    QualitySegmentationStrategy,
    QualityGraphStrategy
  ]
})

export class QualityModule {}
