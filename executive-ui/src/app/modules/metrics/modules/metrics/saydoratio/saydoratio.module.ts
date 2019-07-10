import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FlexLayoutModule} from '@angular/flex-layout';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {SharedModule} from '../../../../shared/shared.module';
import {MetricMapService} from '../../shared/services/metric-map.service';
import {DashboardSharedModule} from '../../shared/shared.module';
import {SayDoRatioPreviewComponent} from './components/metric-preview/saydoratio-preview.component';
import {SayDoRatioDetailComponent} from './components/metric-detail/saydoratio-detail.component';
import {SayDoRatioTrendStrategy} from './strategies/saydoratio-trend-strategy';
import {SayDoRatioPrimaryMetricStrategy} from './strategies/saydoratio-primary-metric-strategy';
import {SayDoRatioBuildingBlocksStrategy} from './strategies/saydoratio-building-blocks-strategy';
import {SayDoRatioDetailStrategy} from './strategies/saydoratio-detail-strategy';
import {SayDoRatioPreviewStrategy} from './strategies/saydoratio-preview-strategy';
import {SayDoRatioSegmentationStrategy} from './strategies/saydoratio-segmentation-strategy';
import {SayDoRatioGraphStrategy} from './strategies/saydoratio-graph-strategy';
import {SayDoRatioRoutingModule} from './saydoratio.routing.module';
import {SayDoRatioAuxiliaryFigureStrategy} from './strategies/saydoratio-auxiliary-figure-strategy';
import { Ng4LoadingSpinnerModule, Ng4LoadingSpinnerComponent, Ng4LoadingSpinnerService } from 'ng4-loading-spinner';

@NgModule({
  imports: [
    FlexLayoutModule,
    CommonModule,
    SharedModule,
    DashboardSharedModule,
     Ng4LoadingSpinnerModule,
    SayDoRatioRoutingModule, FormsModule, ReactiveFormsModule
  ],
  declarations: [
    SayDoRatioPreviewComponent,
    SayDoRatioDetailComponent
  ],
  exports: [
    SayDoRatioPreviewComponent,
    SayDoRatioDetailComponent
  ],
  providers: [
    MetricMapService,
    SayDoRatioTrendStrategy,
    SayDoRatioAuxiliaryFigureStrategy,
    SayDoRatioPrimaryMetricStrategy,
    SayDoRatioBuildingBlocksStrategy,
    SayDoRatioDetailStrategy,
    SayDoRatioPreviewStrategy,
    SayDoRatioSegmentationStrategy,
    SayDoRatioGraphStrategy
  ]
})

export class SayDoRatioModule {}
