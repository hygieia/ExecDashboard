import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FlexLayoutModule} from '@angular/flex-layout';

import {SharedModule} from '../../../../shared/shared.module';
import {MetricMapService} from '../../shared/services/metric-map.service';
import {DashboardSharedModule} from '../../shared/shared.module';
import {ProductionIncidentsPreviewComponent} from './components/metric-preview/production-incidents-preview.component';
import {ProductionIncidentsDetailComponent} from './components/metric-detail/production-incidents-detail.component';
import {ProductionIncidentsTrendStrategy} from './strategies/production-incidents-trend-strategy';
import {ProductionIncidentsPrimaryMetricStrategy} from './strategies/production-incidents-primary-metric-strategy';
import {ProductionIncidentsBuildingBlocksStrategy} from './strategies/production-incidents-building-blocks-strategy';
import {ProductionIncidentsDetailStrategy} from './strategies/production-incidents-detail-strategy';
import {ProductionIncidentsPreviewStrategy} from './strategies/production-incidents-preview-strategy';
import {ProductionIncidentsSegmentationStrategy} from './strategies/production-incidents-segmentation-strategy';
import {ProductionIncidentsGraphStrategy} from './strategies/production-incidents-graph-strategy';
import {ProductionIncidentsRoutingModule} from './production-incidents.routing.module';
import {ProductionIncidentsAuxiliaryFigureStrategy} from './strategies/production-incidents-auxiliary-figure-strategy';

@NgModule({
  imports: [
    FlexLayoutModule,
    CommonModule,
    SharedModule,
    DashboardSharedModule,
    ProductionIncidentsRoutingModule
  ],
  declarations: [
    ProductionIncidentsPreviewComponent,
    ProductionIncidentsDetailComponent
  ],
  exports: [
    ProductionIncidentsPreviewComponent,
    ProductionIncidentsDetailComponent
  ],
  providers: [
    MetricMapService,
    ProductionIncidentsTrendStrategy,
    ProductionIncidentsAuxiliaryFigureStrategy,
    ProductionIncidentsPrimaryMetricStrategy,
    ProductionIncidentsBuildingBlocksStrategy,
    ProductionIncidentsDetailStrategy,
    ProductionIncidentsPreviewStrategy,
    ProductionIncidentsSegmentationStrategy,
    ProductionIncidentsGraphStrategy
  ]
})

export class ProductionIncidentsModule {}
