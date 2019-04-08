import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { PipelineLeadTimeDetailComponent } from './components/metric-detail/pipeline-lead-time-detail.component';
import { PipelineLeadTimeConfiguration } from './pipeline-lead-time.configuration';

const routes: Routes = [
  {
    path: 'portfolio/:portfolio-id/pipeline-lead-time',
    component: PipelineLeadTimeDetailComponent
  },
  {
    path: 'portfolio/:portfolio-id/pipeline-lead-time/product/:product-id',
    component: PipelineLeadTimeDetailComponent,
    data: { animation: PipelineLeadTimeConfiguration.identifier }
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

export class PipelineLeadTimeRoutingModule { }
