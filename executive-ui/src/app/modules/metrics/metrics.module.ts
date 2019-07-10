import {MetricsRoutingModule} from './metrics.routing.module';
import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FlexLayoutModule} from '@angular/flex-layout';
import {SharedModule} from '../shared/shared.module';
import {PreviewsModule} from './modules/previews/previews.module';
import {ProductModule} from './modules/product/product.module';
import {ProductsModule} from './modules/products/products.module';
import {DashboardModule} from './modules/dashboard/dashboard.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@NgModule({
  imports: [
    FlexLayoutModule,
    CommonModule,
    SharedModule,
    MetricsRoutingModule,
    DashboardModule,
    PreviewsModule,
    ProductModule,
    ProductsModule,
    FormsModule, ReactiveFormsModule
  ],
  declarations: [],
  exports: [],
  providers: []
})

export class MetricsModule {
}
