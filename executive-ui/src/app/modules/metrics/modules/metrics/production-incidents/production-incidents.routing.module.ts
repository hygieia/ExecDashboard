import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { ProductionIncidentsDetailComponent } from './components/metric-detail/production-incidents-detail.component';
import { ProductionIncidentsConfiguration } from './production-incidents.configuration';

const routes: Routes = [
  {
    path: 'portfolio/:portfolio-id/production-incidents',
    component: ProductionIncidentsDetailComponent
  },
  {
    path: 'portfolio/:portfolio-id/production-incidents/product/:product-id',
    component: ProductionIncidentsDetailComponent,
    data: { animation: ProductionIncidentsConfiguration.identifier }
  }
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(routes)
  ],
  declarations: [],
  exports: [RouterModule]
})

export class ProductionIncidentsRoutingModule {
}
