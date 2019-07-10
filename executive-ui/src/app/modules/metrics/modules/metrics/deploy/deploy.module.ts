import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FlexLayoutModule } from '@angular/flex-layout';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedModule } from '../../../../shared/shared.module';
import { MetricMapService } from '../../shared/services/metric-map.service';
import { DashboardSharedModule } from '../../shared/shared.module';
import { DeployPreviewComponent } from './components/metric-preview/deploy-preview.component';
import { DeployDetailComponent } from './components/metric-detail/deploy-detail.component';
import { DeployTrendStrategy } from './strategies/deploy-trend-strategy';
import { DeployPrimaryMetricStrategy } from './strategies/deploy-primary-metric-strategy';
import { DeployBuildingBlocksStrategy } from './strategies/deploy-building-blocks-strategy';
import { DeployDetailStrategy } from './strategies/deploy-detail-strategy';
import { DeployPreviewStrategy } from './strategies/deploy-preview-strategy';
import { DeploySegmentationStrategy } from './strategies/deploy-segmentation-strategy';
import { DeployGraphStrategy } from './strategies/deploy-graph-strategy';
import { DeployRoutingModule } from './deploy.routing.module';
import { DeployAuxiliaryFigureStrategy } from './strategies/deploy-auxiliary-figure-strategy';
import { Ng4LoadingSpinnerModule, Ng4LoadingSpinnerComponent, Ng4LoadingSpinnerService } from 'ng4-loading-spinner';

@NgModule({
  imports: [
    FlexLayoutModule,
    CommonModule,
    SharedModule,
    DashboardSharedModule,
    Ng4LoadingSpinnerModule,
    DeployRoutingModule, FormsModule, ReactiveFormsModule
  ],
  declarations: [
    DeployPreviewComponent,
    DeployDetailComponent
  ],
  exports: [
    DeployPreviewComponent,
    DeployDetailComponent
  ],
  providers: [
    MetricMapService,
    DeployTrendStrategy,
    DeployAuxiliaryFigureStrategy,
    DeployPrimaryMetricStrategy,
    DeployBuildingBlocksStrategy,
    DeployDetailStrategy,
    DeployPreviewStrategy,
    DeploySegmentationStrategy,
    DeployGraphStrategy
  ]
})

export class DeployModule { }
