import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FlexLayoutModule } from '@angular/flex-layout';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedModule } from '../../../../shared/shared.module';
import { MetricMapService } from '../../shared/services/metric-map.service';
import { DashboardSharedModule } from '../../shared/shared.module';
import { CloudPreviewComponent } from './components/metric-preview/cloud-preview.component';
import { CloudDetailComponent } from './components/metric-detail/cloud-detail.component';
import { CloudTrendStrategy } from './strategies/cloud-trend-strategy';
import { CloudPrimaryMetricStrategy } from './strategies/cloud-primary-metric-strategy';
import { CloudBuildingBlocksStrategy } from './strategies/cloud-building-blocks-strategy';
import { CloudDetailStrategy } from './strategies/cloud-detail-strategy';
import { CloudPreviewStrategy } from './strategies/cloud-preview-strategy';
import { CloudSegmentationStrategy } from './strategies/cloud-segmentation-strategy';
import { CloudGraphStrategy } from './strategies/cloud-graph-strategy';
import { CloudRoutingModule } from './cloud.routing.module';
import { CloudAuxiliaryFigureStrategy } from './strategies/cloud-auxiliary-figure-strategy';
import { Ng4LoadingSpinnerModule, Ng4LoadingSpinnerComponent, Ng4LoadingSpinnerService } from 'ng4-loading-spinner';

@NgModule({
  imports: [
    FlexLayoutModule,
    CommonModule,
    SharedModule,
    DashboardSharedModule,
    Ng4LoadingSpinnerModule,
    CloudRoutingModule, FormsModule, ReactiveFormsModule
  ],
  declarations: [
    CloudPreviewComponent,
    CloudDetailComponent
  ],
  exports: [
    CloudPreviewComponent,
    CloudDetailComponent
  ],
  providers: [
    MetricMapService,
    CloudTrendStrategy,
    CloudAuxiliaryFigureStrategy,
    CloudPrimaryMetricStrategy,
    CloudBuildingBlocksStrategy,
    CloudDetailStrategy,
    CloudPreviewStrategy,
    CloudSegmentationStrategy,
    CloudGraphStrategy
  ]
})

export class CloudModule { }
