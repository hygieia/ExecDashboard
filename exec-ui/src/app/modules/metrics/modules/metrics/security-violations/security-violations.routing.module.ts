import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Routes, RouterModule} from '@angular/router';
import {SecurityViolationsDetailComponent} from './components/metric-detail/security-violations-detail.component';
import {SecurityViolationsConfiguration} from './security-violations.configuration';

const routes: Routes = [
  {
    path: 'portfolio/:portfolio-name/:portfolio-lob/security-violations',
    component: SecurityViolationsDetailComponent
  },
  {
    path: 'portfolio/:portfolio-name/:portfolio-lob/security-violations/product/:product-id',
    component: SecurityViolationsDetailComponent,
    data: {animation: SecurityViolationsConfiguration.identifier}
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

export class SecurityViolationsRoutingModule {
}
