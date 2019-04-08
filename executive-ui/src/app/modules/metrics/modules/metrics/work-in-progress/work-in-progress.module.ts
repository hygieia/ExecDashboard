import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FlexLayoutModule} from '@angular/flex-layout';

import {SharedModule} from '../../../../shared/shared.module';
import {WorkInProgressPreviewComponent} from './components/metric-preview/work-in-progress-preview.component';
import {WorkInProgressDetailComponent} from './components/metric-detail/work-in-progress-detail.component';
import {WorkInProgressTrendStrategy} from './strategies/work-in-progress-trend-strategy';
import {WorkInProgressPrimaryMetricStrategy} from './strategies/work-in-progress-primary-metric-strategy';
import {WorkInProgressBuildingBlocksStrategy} from './strategies/work-in-progress-building-blocks-strategy';
import {WorkInProgressDetailStrategy} from './strategies/work-in-progress-detail-strategy';
import {WorkInProgressPreviewStrategy} from './strategies/work-in-progress-preview-strategy';
import {WorkInProgressSegmentationStrategy} from './strategies/work-in-progress-segmentation-strategy';
import {WorkInProgressGraphStrategy} from './strategies/work-in-progress-graph-strategy';
import {MetricMapService} from '../../shared/services/metric-map.service';
import {DashboardSharedModule} from '../../shared/shared.module';
import {WorkInProgressRoutingModule} from './work-in-progress.routing.module';
import {WorkInProgressConfiguration} from './work-in-progress.configuration';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Ng4LoadingSpinnerModule, Ng4LoadingSpinnerComponent, Ng4LoadingSpinnerService } from 'ng4-loading-spinner';

@NgModule({
  imports: [
    FlexLayoutModule,
    CommonModule,
    SharedModule,
    DashboardSharedModule,
    WorkInProgressRoutingModule,
    Ng4LoadingSpinnerModule,
     FormsModule, ReactiveFormsModule
  ],
  declarations: [
    WorkInProgressPreviewComponent,
    WorkInProgressDetailComponent
  ],
  exports: [
    WorkInProgressPreviewComponent,
    WorkInProgressDetailComponent
  ],
  providers: [
    MetricMapService,
    WorkInProgressTrendStrategy,
    WorkInProgressPrimaryMetricStrategy,
    WorkInProgressBuildingBlocksStrategy,
    WorkInProgressDetailStrategy,
    WorkInProgressPreviewStrategy,
    WorkInProgressSegmentationStrategy,
    WorkInProgressGraphStrategy
  ]
})

export class WorkInProgressModule {}