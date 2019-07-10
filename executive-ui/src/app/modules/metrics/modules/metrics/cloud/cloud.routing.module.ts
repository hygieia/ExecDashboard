import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { CloudDetailComponent } from './components/metric-detail/cloud-detail.component';
import { CloudConfiguration } from './cloud.configuration';

const routes: Routes = [
  {
    path: 'portfolio/:portfolio-id/cloud',
    component: CloudDetailComponent
  },
  {
    path: 'portfolio/:portfolio-id/cloud/product/:product-id',
    component: CloudDetailComponent,
    data: { animation: CloudConfiguration.identifier }
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

export class CloudRoutingModule {
}
