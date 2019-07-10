import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FlexLayoutModule } from '@angular/flex-layout';

import { SharedModule } from '../../../../shared/shared.module';
import { CodeRepoPreviewComponent } from './components/metric-preview/code-repo-preview.component';
import { CodeRepoDetailComponent } from './components/metric-detail/code-repo-detail.component';
import { CodeRepoTrendStrategy } from './strategies/code-repo-trend-strategy';
import { CodeRepoPrimaryMetricStrategy } from './strategies/code-repo-primary-metric-strategy';
import { CodeRepoBuildingBlocksStrategy } from './strategies/code-repo-building-blocks-strategy';
import { CodeRepoDetailStrategy } from './strategies/code-repo-detail-strategy';
import { CodeRepoPreviewStrategy } from './strategies/code-repo-preview-strategy';
import { CodeRepoSegmentationStrategy } from './strategies/code-repo-segmentation-strategy';
import { CodeRepoGraphStrategy } from './strategies/code-repo-graph-strategy';
import { MetricMapService } from '../../shared/services/metric-map.service';
import { DashboardSharedModule } from '../../shared/shared.module';
import { CodeRepoRoutingModule } from './code-repo.routing.module';
import { CodeRepoConfiguration } from './code-repo.configuration';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Ng4LoadingSpinnerModule, Ng4LoadingSpinnerComponent, Ng4LoadingSpinnerService } from 'ng4-loading-spinner';

@NgModule({
  imports: [
    FlexLayoutModule,
    CommonModule,
    SharedModule,
    DashboardSharedModule,
    CodeRepoRoutingModule,
    Ng4LoadingSpinnerModule,
    FormsModule, ReactiveFormsModule
  ],
  declarations: [
    CodeRepoPreviewComponent,
    CodeRepoDetailComponent
  ],
  exports: [
    CodeRepoPreviewComponent,
    CodeRepoDetailComponent
  ],
  providers: [
    MetricMapService,
    CodeRepoTrendStrategy,
    CodeRepoPrimaryMetricStrategy,
    CodeRepoBuildingBlocksStrategy,
    CodeRepoDetailStrategy,
    CodeRepoPreviewStrategy,
    CodeRepoSegmentationStrategy,
    CodeRepoGraphStrategy
  ]
})

export class CodeRepoModule { }