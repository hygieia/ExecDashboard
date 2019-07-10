import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Routes, RouterModule} from '@angular/router';
import {ThroughputDetailComponent} from './components/metric-detail/throughput-detail.component';
import {ThroughputConfiguration} from './throughput.configuration';

const routes: Routes = [
  {
    path: 'portfolio/:portfolio-id/total-value',
    component: ThroughputDetailComponent
  },
  {
    path: 'portfolio/:portfolio-id/total-value/product/:product-id',
    component: ThroughputDetailComponent,
    data: { animation: ThroughputConfiguration.identifier }
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
export class ThroughputRoutingModule { }
