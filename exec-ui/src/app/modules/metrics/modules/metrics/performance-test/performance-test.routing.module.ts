import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Routes, RouterModule} from '@angular/router';
import {PerformanceTestDetailComponent} from './components/metric-detail/performance-test-detail.component';
import {PerformanceTestConfiguration} from './performance-test.configuration';

const routes: Routes = [
  {
    path: 'portfolio/:portfolio-name/:portfolio-lob/performance-test',
    component: PerformanceTestDetailComponent
  },
  {
    path: 'portfolio/:portfolio-name/:portfolio-lob/performance-test/product/:product-id',
    component: PerformanceTestDetailComponent,
    data: {animation: PerformanceTestConfiguration.identifier}
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

export class PerformanceTestRoutingModule {
}
