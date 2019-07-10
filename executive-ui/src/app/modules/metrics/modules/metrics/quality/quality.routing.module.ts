import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Routes, RouterModule} from '@angular/router';
import {QualityDetailComponent} from './components/metric-detail/quality-detail.component';
import {QualityConfiguration} from './quality.configuration';

const routes: Routes = [
  {
    path: 'portfolio/:portfolio-id/quality',
    component: QualityDetailComponent
  },
  {
    path: 'portfolio/:portfolio-id/quality/product/:product-id',
    component: QualityDetailComponent,
    data: {animation: QualityConfiguration.identifier}
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

export class QualityRoutingModule {
}
