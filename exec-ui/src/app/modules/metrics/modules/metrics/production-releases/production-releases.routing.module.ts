import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Routes, RouterModule} from '@angular/router';
import {ProductionReleasesDetailComponent} from './components/metric-detail/production-releases-detail.component';
import {ProductionReleasesConfiguration} from './production-releases.configuration';

const routes: Routes = [
  {
    path: 'portfolio/:portfolio-name/:portfolio-lob/production-releases',
    component: ProductionReleasesDetailComponent
  },
  {
    path: 'portfolio/:portfolio-name/:portfolio-lob/product/:product-id',
    component: ProductionReleasesDetailComponent,
    data: { animation: ProductionReleasesConfiguration.identifier}
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

export class ProductionReleasesRoutingModule { }
