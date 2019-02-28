import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Routes, RouterModule} from '@angular/router';
import {TraceabilityDetailComponent} from './components/metric-detail/traceability-detail.component';
import {TraceabilityConfiguration} from './traceability.configuration';

const routes: Routes = [
  {
    path: 'portfolio/:portfolio-name/:portfolio-lob/traceability',
    component: TraceabilityDetailComponent
  },
  {
    path: 'portfolio/:portfolio-name/:portfolio-lob/traceability/product/:product-id',
    component: TraceabilityDetailComponent,
    data: {animation: TraceabilityConfiguration.identifier}
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

export class TraceabilityRoutingModule {
}
