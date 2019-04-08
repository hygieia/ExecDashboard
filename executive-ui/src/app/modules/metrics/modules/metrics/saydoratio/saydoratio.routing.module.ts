import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Routes, RouterModule} from '@angular/router';
import {SayDoRatioDetailComponent} from './components/metric-detail/saydoratio-detail.component';
import {SayDoRatioConfiguration} from './saydoratio.configuration';

const routes: Routes = [
  {
    path: 'portfolio/:portfolio-id/saydoratio',
    component: SayDoRatioDetailComponent
  },
  {
    path: 'portfolio/:portfolio-id/saydoratio/product/:product-id',
    component: SayDoRatioDetailComponent,
    data: {animation: SayDoRatioConfiguration.identifier}
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

export class SayDoRatioRoutingModule {
}
