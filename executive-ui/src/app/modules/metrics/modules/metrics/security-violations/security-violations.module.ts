import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FlexLayoutModule} from '@angular/flex-layout';

import {SharedModule} from '../../../../shared/shared.module';
import {MetricMapService} from '../../shared/services/metric-map.service';
import {DashboardSharedModule} from '../../shared/shared.module';
import {SecurityViolationsGraphStrategy} from './strategies/security-violations-graph-strategy';
import {SecurityViolationsSegmentationStrategy} from './strategies/security-violations-segmentation-strategy';
import {SecurityViolationsPreviewStrategy} from './strategies/security-violations-preview-strategy';
import {SecurityViolationsDetailStrategy} from './strategies/security-violations-detail-strategy';
import {SecurityViolationsBuildingBlocksStrategy} from './strategies/security-violations-building-blocks-strategy';
import {SecurityViolationsPrimaryMetricStrategy} from './strategies/security-violations-primary-metric-strategy';
import {SecurityViolationsTrendStrategy} from './strategies/security-violations-trend-strategy';
import {SecurityViolationsDetailComponent} from './components/metric-detail/security-violations-detail.component';
import {SecurityViolationsPreviewComponent} from './components/metric-preview/security-violations-preview.component';
import {SecurityViolationsRoutingModule} from './security-violations.routing.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Ng4LoadingSpinnerModule, Ng4LoadingSpinnerComponent, Ng4LoadingSpinnerService } from 'ng4-loading-spinner';

@NgModule({
  imports: [
    FlexLayoutModule,
    CommonModule,
    SharedModule,
    DashboardSharedModule, FormsModule, ReactiveFormsModule,
    Ng4LoadingSpinnerModule,
    SecurityViolationsRoutingModule
  ],
  declarations: [
    SecurityViolationsPreviewComponent,
    SecurityViolationsDetailComponent
  ],
  exports: [
    SecurityViolationsPreviewComponent,
    SecurityViolationsDetailComponent
  ],
  providers: [
    MetricMapService,
    SecurityViolationsTrendStrategy,
    SecurityViolationsPrimaryMetricStrategy,
    SecurityViolationsBuildingBlocksStrategy,
    SecurityViolationsDetailStrategy,
    SecurityViolationsPreviewStrategy,
    SecurityViolationsSegmentationStrategy,
    SecurityViolationsGraphStrategy
  ]
})

export class SecurityViolationsModule {}
