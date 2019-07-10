import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { OpenSourceViolationsDetailComponent } from './components/metric-detail/open-source-violations-detail.component';
import { OpenSourceViolationsConfiguration } from './open-source-violations.configuration';

const routes: Routes = [
  {
    path: 'portfolio/:portfolio-id/open-source-violations',
    component: OpenSourceViolationsDetailComponent
  },
  {
    path: 'portfolio/:portfolio-id/open-source-violations/product/:product-id',
    component: OpenSourceViolationsDetailComponent,
    data: { animation: OpenSourceViolationsConfiguration.identifier }
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
export class OpenSourceViolationsRoutingModule { }
