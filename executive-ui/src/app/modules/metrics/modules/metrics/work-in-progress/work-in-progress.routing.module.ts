import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Routes, RouterModule} from '@angular/router';
import {WorkInProgressDetailComponent} from './components/metric-detail/work-in-progress-detail.component';
import {WorkInProgressConfiguration} from './work-in-progress.configuration';

const routes: Routes = [
  {
    path: 'portfolio/:portfolio-id/work-in-progress',
    component: WorkInProgressDetailComponent
  },
  {
    path: 'portfolio/:portfolio-id/work-in-progress/product/:product-id',
    component: WorkInProgressDetailComponent,
    data: { animation: WorkInProgressConfiguration.identifier }
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
export class WorkInProgressRoutingModule { }