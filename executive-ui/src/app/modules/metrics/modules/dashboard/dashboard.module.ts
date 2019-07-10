import {DashboardRoutingModule} from './dashboard.routing.module';
import {SharedModule} from '../../../shared/shared.module';
import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FlexLayoutModule} from '@angular/flex-layout';
import {MetricBuildingBlocksMapStrategy} from './strategies/metric-building-blocks-map-strategy';
import {PreviewsModule} from '../previews/previews.module';
import {DashboardComponent} from './components/dashboard/dashboard.component';
import {DashboardSharedModule} from '../shared/shared.module';
import { Ng4LoadingSpinnerModule, Ng4LoadingSpinnerComponent, Ng4LoadingSpinnerService } from 'ng4-loading-spinner';

@NgModule({
  imports: [
    FlexLayoutModule,
    CommonModule,
    SharedModule,
    DashboardSharedModule,
    DashboardRoutingModule,
    Ng4LoadingSpinnerModule,
    PreviewsModule
  ],
  declarations: [
    DashboardComponent
  ],
  exports: [
    DashboardComponent
  ],
  providers: [
    MetricBuildingBlocksMapStrategy
  ]
})

export class DashboardModule {
}
