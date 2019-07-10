import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Routes, RouterModule} from '@angular/router';
import {TestDetailComponent} from './components/metric-detail/test-detail.component';
import {TestConfiguration} from './test.configuration';

const routes: Routes = [
  {
    path: 'portfolio/:portfolio-id/test',
    component: TestDetailComponent
  },
  {
    path: 'portfolio/:portfolio-id/test/product/:product-id',
    component: TestDetailComponent,
    data: {animation: TestConfiguration.identifier}
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

export class TestRoutingModule {
}
