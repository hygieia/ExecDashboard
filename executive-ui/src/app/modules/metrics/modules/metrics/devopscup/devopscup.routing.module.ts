import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { DevopscupDetailComponent } from './components/metric-detail/devopscup-detail.component';
import { DevopsCupConfiguration } from './devopscup.configuration';

const routes: Routes = [
  {
    path: 'portfolio/:portfolio-id/devopscup',
    component: DevopscupDetailComponent
  },
  {
    path: 'portfolio/:portfolio-id/devopscup/product/:product-id',
    component: DevopscupDetailComponent,
    data: { animation: DevopsCupConfiguration.identifier }
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

export class DevopscupRoutingModule {
}
