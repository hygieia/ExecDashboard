import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { DeployDetailComponent } from './components/metric-detail/deploy-detail.component';
import { DeployConfiguration } from './deploy.configuration';

const routes: Routes = [
  {
    path: 'portfolio/:portfolio-id/deploy',
    component: DeployDetailComponent
  },
  {
    path: 'portfolio/:portfolio-id/deploy/product/:product-id',
    component: DeployDetailComponent,
    data: { animation: DeployConfiguration.identifier }
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

export class DeployRoutingModule {
}
