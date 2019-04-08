import { ProductRoutingModule } from './product.routing.module';
import { SharedModule } from '../../../shared/shared.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FlexLayoutModule } from '@angular/flex-layout';
import { PreviewsModule } from '../previews/previews.module';
import { ProductComponent } from './components/product/product.component';
import { MetricBuildingBlocksMapStrategy } from './strategies/metric-building-blocks-map-strategy';
import { DashboardSharedModule } from '../shared/shared.module';
import { Ng4LoadingSpinnerModule } from 'ng4-loading-spinner';

@NgModule({
  imports: [
    FlexLayoutModule,
    CommonModule,
    SharedModule,
    ProductRoutingModule,
    PreviewsModule,
    DashboardSharedModule,
    Ng4LoadingSpinnerModule
  ],
  declarations: [
    ProductComponent
  ],
  exports: [
    ProductComponent
  ],
  providers: [
    MetricBuildingBlocksMapStrategy
  ]
})

export class ProductModule {
}
