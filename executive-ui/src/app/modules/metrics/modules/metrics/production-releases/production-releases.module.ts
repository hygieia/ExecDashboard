import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FlexLayoutModule } from '@angular/flex-layout';

import { SharedModule } from '../../../../shared/shared.module';
import { MetricMapService } from '../../shared/services/metric-map.service';
import { DashboardSharedModule } from '../../shared/shared.module';
import { ProductionReleasesGraphStrategy } from './strategies/production-releases-graph-strategy';
import { ProductionReleasesPreviewStrategy } from './strategies/production-releases-preview-strategy';
import { ProductionReleasesDetailStrategy } from './strategies/production-releases-detail-strategy';
import { ProductionReleasesBuildingBlocksStrategy } from './strategies/production-releases-building-blocks-strategy';
import { ProductionReleasesPrimaryMetricStrategy } from './strategies/production-releases-primary-metric-strategy';
import { ProductionReleasesTrendStrategy } from './strategies/production-releases-trend-strategy';
import { ProductionReleasesDetailComponent } from './components/metric-detail/production-releases-detail.component';
import { ProductionReleasesPreviewComponent } from './components/metric-preview/production-releases-preview.component';
import { ProductionReleasesRoutingModule } from './production-releases.routing.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Ng4LoadingSpinnerModule, Ng4LoadingSpinnerComponent, Ng4LoadingSpinnerService } from 'ng4-loading-spinner';

@NgModule({
  imports: [
    FlexLayoutModule,
    CommonModule,
    SharedModule,
    DashboardSharedModule, FormsModule, ReactiveFormsModule,
    Ng4LoadingSpinnerModule,
    ProductionReleasesRoutingModule
  ],
  declarations: [
    ProductionReleasesPreviewComponent,
    ProductionReleasesDetailComponent
  ],
  exports: [
    ProductionReleasesPreviewComponent,
    ProductionReleasesDetailComponent
  ],
  providers: [
    MetricMapService,
    ProductionReleasesTrendStrategy,
    ProductionReleasesPrimaryMetricStrategy,
    ProductionReleasesBuildingBlocksStrategy,
    ProductionReleasesDetailStrategy,
    ProductionReleasesPreviewStrategy,
    ProductionReleasesGraphStrategy
  ]
})

export class ProductionReleasesModule { }
