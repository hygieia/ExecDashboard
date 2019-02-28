import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FlexLayoutModule} from '@angular/flex-layout';
import {SharedModule} from '../../../../shared/shared.module';
import {MetricMapService} from '../../shared/services/metric-map.service';
import {DashboardSharedModule} from '../../shared/shared.module';
import {TraceabilityGraphStrategy} from './strategies/traceability-graph-strategy';
import {TraceabilitySegmentationStrategy} from "./strategies/traceability-segmentation-strategy";
import {TraceabilityPreviewStrategy} from './strategies/traceability-preview-strategy';
import {TraceabilityDetailStrategy} from './strategies/traceability-detail-strategy';
import {TraceabilityBuildingBlocksStrategy} from './strategies/traceability-building-blocks-strategy';
import {TraceabilityPrimaryMetricStrategy} from './strategies/traceability-primary-metric-strategy';
import {TraceabilityTrendStrategy} from './strategies/traceability-trend-strategy';
import {TraceabilityDetailComponent} from './components/metric-detail/traceability-detail.component';
import {TraceabilityPreviewComponent} from './components/metric-preview/traceability-preview.component';
import {TraceabilityRoutingModule} from './traceability.routing.module';
import {TraceabilityAuxiliaryAutomatedFigureStrategy} from "./strategies/traceability-auxiliary-automated-figure-strategy";
import {TraceabilityAuxiliaryManualFigureStrategy} from "./strategies/traceability-auxiliary-manual-figure-strategy";

@NgModule({
  imports: [
    FlexLayoutModule,
    CommonModule,
    SharedModule,
    DashboardSharedModule,
    TraceabilityRoutingModule
  ],
  declarations: [
    TraceabilityPreviewComponent,
    TraceabilityDetailComponent
  ],
  exports: [
    TraceabilityPreviewComponent,
    TraceabilityDetailComponent
  ],
  providers: [
    MetricMapService,
    TraceabilityTrendStrategy,
    TraceabilityPrimaryMetricStrategy,
    TraceabilityBuildingBlocksStrategy,
    TraceabilityDetailStrategy,
    TraceabilityPreviewStrategy,
    TraceabilityGraphStrategy,
    TraceabilitySegmentationStrategy,
    TraceabilityAuxiliaryAutomatedFigureStrategy,
    TraceabilityAuxiliaryManualFigureStrategy
  ]
})

export class TraceabilityModule {}
