import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FlexLayoutModule} from '@angular/flex-layout';

import {SharedModule} from '../../../../shared/shared.module';
import {OpenSourceViolationsPreviewComponent} from './components/metric-preview/open-source-violations-preview.component';
import {OpenSourceViolationsDetailComponent} from './components/metric-detail/open-source-violations-detail.component';
import {OpenSourceViolationsTrendStrategy} from './strategies/open-source-violations-trend-strategy';
import {OpenSourceViolationsPrimaryMetricStrategy} from './strategies/open-source-violations-primary-metric-strategy';
import {OpenSourceViolationsBuildingBlocksStrategy} from './strategies/open-source-violations-building-blocks-strategy';
import {OpenSourceViolationsDetailStrategy} from './strategies/open-source-violations-detail-strategy';
import {OpenSourceViolationsPreviewStrategy} from './strategies/open-source-violations-preview-strategy';
import {OpenSourceViolationsSegmentationStrategy} from './strategies/open-source-violations-segmentation-strategy';
import {OpenSourceViolationsGraphStrategy} from './strategies/open-source-violations-graph-strategy';
import {MetricMapService} from '../../shared/services/metric-map.service';
import {DashboardSharedModule} from '../../shared/shared.module';
import {OpenSourceViolationsRoutingModule} from './open-source-violations.routing.module';
import {OpenSourceViolationsConfiguration} from './open-source-violations.configuration';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Ng4LoadingSpinnerModule, Ng4LoadingSpinnerComponent, Ng4LoadingSpinnerService } from 'ng4-loading-spinner';

@NgModule({
  imports: [
    FlexLayoutModule,
    CommonModule,
    SharedModule,
    DashboardSharedModule,
    OpenSourceViolationsRoutingModule,
     Ng4LoadingSpinnerModule,
    FormsModule, ReactiveFormsModule
  ],
  declarations: [
    OpenSourceViolationsPreviewComponent,
    OpenSourceViolationsDetailComponent
  ],
  exports: [
    OpenSourceViolationsPreviewComponent,
    OpenSourceViolationsDetailComponent
  ],
  providers: [
    MetricMapService,
    OpenSourceViolationsTrendStrategy,
    OpenSourceViolationsPrimaryMetricStrategy,
    OpenSourceViolationsBuildingBlocksStrategy,
    OpenSourceViolationsDetailStrategy,
    OpenSourceViolationsPreviewStrategy,
    OpenSourceViolationsSegmentationStrategy,
    OpenSourceViolationsGraphStrategy
  ]
})

export class OpenSourceViolationsModule {}