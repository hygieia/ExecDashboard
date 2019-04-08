import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FlexLayoutModule} from '@angular/flex-layout';

import {SharedModule} from '../../../../shared/shared.module';
import {ThroughputPreviewComponent} from './components/metric-preview/throughput-preview.component';
import {ThroughputDetailComponent} from './components/metric-detail/throughput-detail.component';
import {ThroughputTrendStrategy} from './strategies/throughput-trend-strategy';
import {ThroughputPrimaryMetricStrategy} from './strategies/throughput-primary-metric-strategy';
import {ThroughputBuildingBlocksStrategy} from './strategies/throughput-building-blocks-strategy';
import {ThroughputDetailStrategy} from './strategies/throughput-detail-strategy';
import {ThroughputPreviewStrategy} from './strategies/throughput-preview-strategy';
import {ThroughputSegmentationStrategy} from './strategies/throughput-segmentation-strategy';
import {ThroughputGraphStrategy} from './strategies/throughput-graph-strategy';
import {MetricMapService} from '../../shared/services/metric-map.service';
import {DashboardSharedModule} from '../../shared/shared.module';
import {ThroughputRoutingModule} from './throughput.routing.module';
import {ThroughputConfiguration} from './throughput.configuration';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Ng4LoadingSpinnerModule, Ng4LoadingSpinnerComponent, Ng4LoadingSpinnerService } from 'ng4-loading-spinner';

@NgModule({
  imports: [
    FlexLayoutModule,
    CommonModule,
    SharedModule,
    DashboardSharedModule,
    ThroughputRoutingModule,
    Ng4LoadingSpinnerModule,
     FormsModule, ReactiveFormsModule
  ],
  declarations: [
    ThroughputPreviewComponent,
    ThroughputDetailComponent
  ],
  exports: [
    ThroughputPreviewComponent,
    ThroughputDetailComponent
  ],
  providers: [
    MetricMapService,
    ThroughputTrendStrategy,
    ThroughputPrimaryMetricStrategy,
    ThroughputBuildingBlocksStrategy,
    ThroughputDetailStrategy,
    ThroughputPreviewStrategy,
    ThroughputSegmentationStrategy,
    ThroughputGraphStrategy
  ]
})

export class ThroughputModule {}