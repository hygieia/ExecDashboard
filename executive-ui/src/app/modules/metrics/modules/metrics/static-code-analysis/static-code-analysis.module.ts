import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FlexLayoutModule} from '@angular/flex-layout';

import {SharedModule} from '../../../../shared/shared.module';
import {MetricMapService} from '../../shared/services/metric-map.service';
import {DashboardSharedModule} from '../../shared/shared.module';
import {StaticCodeAnalysisGraphStrategy} from './strategies/static-code-analysis-graph-strategy';
import {StaticCodeAnalysisSegmentationStrategy} from './strategies/static-code-analysis-segmentation-strategy';
import {StaticCodeAnalysisPreviewStrategy} from './strategies/static-code-analysis-preview-strategy';
import {StaticCodeAnalysisDetailStrategy} from './strategies/static-code-analysis-detail-strategy';
import {StaticCodeAnalysisBuildingBlocksStrategy} from './strategies/static-code-analysis-building-blocks-strategy';
import {StaticCodeAnalysisPrimaryMetricStrategy} from './strategies/static-code-analysis-primary-metric-strategy';
import {StaticCodeAnalysisTrendStrategy} from './strategies/static-code-analysis-trend-strategy';
import {StaticCodeAnalysisDetailComponent} from './components/metric-detail/static-code-analysis-detail.component';
import {StaticCodeAnalysisPreviewComponent} from './components/metric-preview/static-code-analysis-preview.component';
import {StaticCodeAnalysisRoutingModule} from './static-code-analysis.routing.module';
import {StaticCodeAnalysisAuxiliaryFigureStrategy} from './strategies/static-code-analysis-auxiliary-figure-strategy';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Ng4LoadingSpinnerModule, Ng4LoadingSpinnerComponent, Ng4LoadingSpinnerService } from 'ng4-loading-spinner';

@NgModule({
  imports: [
    FlexLayoutModule,
    CommonModule,
    SharedModule,
    DashboardSharedModule, FormsModule, ReactiveFormsModule,
    Ng4LoadingSpinnerModule,
    StaticCodeAnalysisRoutingModule
  ],
  declarations: [
    StaticCodeAnalysisPreviewComponent,
    StaticCodeAnalysisDetailComponent
  ],
  exports: [
    StaticCodeAnalysisPreviewComponent,
    StaticCodeAnalysisDetailComponent
  ],
  providers: [
    MetricMapService,
    StaticCodeAnalysisTrendStrategy,
    StaticCodeAnalysisAuxiliaryFigureStrategy,
    StaticCodeAnalysisPrimaryMetricStrategy,
    StaticCodeAnalysisBuildingBlocksStrategy,
    StaticCodeAnalysisDetailStrategy,
    StaticCodeAnalysisPreviewStrategy,
    StaticCodeAnalysisSegmentationStrategy,
    StaticCodeAnalysisGraphStrategy
  ]
})

export class StaticCodeAnalysisModule {}
