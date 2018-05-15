import {MetricsRoutingModule} from './metrics.routing.module';
import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FlexLayoutModule} from '@angular/flex-layout';
import {SharedModule} from '../shared/shared.module';
import {PreviewsModule} from './modules/previews/previews.module';
import {ProductsModule} from './modules/products/products.module';
import {DashboardModule} from './modules/dashboard/dashboard.module';

@NgModule({
  imports: [
    FlexLayoutModule,
    CommonModule,
    SharedModule,
    MetricsRoutingModule,
    DashboardModule,
    PreviewsModule,
    ProductsModule
  ],
  declarations: [],
  exports: [],
  providers: []
})

export class MetricsModule {
}
