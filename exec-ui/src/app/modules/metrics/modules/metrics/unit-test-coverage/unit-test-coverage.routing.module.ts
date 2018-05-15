import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Routes, RouterModule} from '@angular/router';
import {UnitTestCoverageDetailComponent} from './components/metric-detail/unit-test-coverage-detail.component';
import {UnitTestCoverageConfiguration} from './unit-test-coverage.configuration';

const routes: Routes = [
  {
    path: 'portfolio/:portfolio-name/:portfolio-lob/unit-test-coverage',
    component: UnitTestCoverageDetailComponent
  },
  {
    path: 'portfolio/:portfolio-name/:portfolio-lob/unit-test-coverage/product/:product-id',
    component: UnitTestCoverageDetailComponent,
    data: {animation: UnitTestCoverageConfiguration.identifier}
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

export class UnitTestCoverageRoutingModule {
}
