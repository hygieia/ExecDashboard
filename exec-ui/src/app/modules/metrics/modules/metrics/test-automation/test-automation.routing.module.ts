import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Routes, RouterModule} from '@angular/router';
import {TestAutomationDetailComponent} from './components/metric-detail/test-automation-detail.component';
import {TestAutomationConfiguration} from './test-automation.configuration';

const routes: Routes = [
  {
    path: 'portfolio/:portfolio-name/:portfolio-lob/test-automation',
    component: TestAutomationDetailComponent
  },
  {
    path: 'portfolio/:portfolio-name/:portfolio-lob/test-automation/product/:product-id',
    component: TestAutomationDetailComponent,
    data: {animation: TestAutomationConfiguration.identifier}
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

export class TestAutomationRoutingModule {
}
