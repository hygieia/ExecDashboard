import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FlexLayoutModule} from '@angular/flex-layout';
import { MaterialModule, NoConflictStyleCompatibilityMode } from '@angular/material';
import {SharedModule} from '../../../shared/shared.module';
import {MetricGraphComponent} from './components/metric-graph/metric-graph.component';
import {MetricBargraphComponent} from './components/metric-bargraph/metric-bargraph.component';
import {MetricSegmentationComponent} from './components/metric-segmentation/metric-segmentation.component';
import {TotalReportingComponent} from './components/metric-total-reporting/metric-total-reporting.component';
import {TotalReportingPipe} from './components/metric-total-reporting/metric-total-reporting.pipe';
import {TotalReportingPercentPipe} from './components/metric-total-reporting/metric-total-reporting-percent.pipe';
import {MetricTrendIndicatorComponent} from './components/metric-trend-indicator/metric-trend-indicator.component';
import {MetricAuxiliaryFigureComponent} from './components/metric-auxiliary-figure/metric-auxiliary-figure.component';
import {MetricMapService} from './services/metric-map.service';
import {BuildingBlockComponent} from './components/building-block/building-block.component';
import {BuildingBlocksComponent} from './components/building-blocks/building-blocks.component';
import {BuildingBlockMetricNamePipe} from './components/building-block/building-block-metric-name.pipe';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { NgxPaginationModule } from 'ngx-pagination';
import { BuildingExecutiveBlocksComponent } from './components/building-executive-blocks/building-executive-blocks.component';
import { BuildingExecutiveBlockComponent } from './components/building-executive-block/building-executive-block.component';
import { CloudMetricsComponent } from './components/cloud-metrics/cloud-metrics.component';
import { Ng4LoadingSpinnerModule, Ng4LoadingSpinnerComponent, Ng4LoadingSpinnerService } from 'ng4-loading-spinner';
import { OperationalMetricsComponent } from './components/operational-metrics/operational-metrics.component';
import { DevopscupMetricsComponent } from './components/devopscup-metrics/devopscup-metrics.component';
import { AngularMultiSelectModule } from 'angular2-multiselect-dropdown/multiselect.component';
import { DevopscupGraphComponent } from './components/devopscup-graph/devopscup-graph.component';


@NgModule({
  imports: [
    FlexLayoutModule,
    CommonModule,
    SharedModule,
    Ng4LoadingSpinnerModule,
    FormsModule, ReactiveFormsModule, InfiniteScrollModule, NgxPaginationModule,
    MaterialModule,
    NoConflictStyleCompatibilityMode,
    AngularMultiSelectModule
  ],
  declarations: [
    BuildingBlockComponent,
    BuildingBlocksComponent,
    BuildingExecutiveBlocksComponent,
    BuildingExecutiveBlockComponent,
    BuildingBlockMetricNamePipe,
    MetricGraphComponent,
    MetricBargraphComponent,
    MetricSegmentationComponent,
    MetricTrendIndicatorComponent,
    MetricAuxiliaryFigureComponent,
    TotalReportingComponent,
    TotalReportingPipe,
    TotalReportingPercentPipe,
    BuildingExecutiveBlocksComponent,
    BuildingExecutiveBlockComponent,
    CloudMetricsComponent,
    OperationalMetricsComponent,
    DevopscupMetricsComponent,
    DevopscupGraphComponent
  ],
  exports: [
    BuildingBlockComponent,
    BuildingBlocksComponent,
    BuildingExecutiveBlocksComponent,
    BuildingExecutiveBlockComponent,
    BuildingBlockMetricNamePipe,
    MetricGraphComponent,
    MetricBargraphComponent,
    MetricSegmentationComponent,
    MetricTrendIndicatorComponent,
    MetricAuxiliaryFigureComponent,
    TotalReportingComponent,
    TotalReportingPipe,
    TotalReportingPercentPipe,
    CloudMetricsComponent,
    OperationalMetricsComponent,
    DevopscupMetricsComponent  ],
  providers: [
    MetricMapService
  ]
})
export class DashboardSharedModule {
}
