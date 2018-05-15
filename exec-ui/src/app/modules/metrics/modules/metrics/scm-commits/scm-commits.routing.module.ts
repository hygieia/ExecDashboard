import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Routes, RouterModule} from '@angular/router';
import {SCMCommitsDetailComponent} from './components/metric-detail/scm-commits-detail.component';
import {SCMCommitsConfiguration} from './scm-commits.configuration';

const routes: Routes = [
  {
    path: 'portfolio/:portfolio-name/:portfolio-lob/scm-commits',
    component: SCMCommitsDetailComponent
  },
  {
    path: 'portfolio/:portfolio-name/:portfolio-lob/product/:product-id',
    component: SCMCommitsDetailComponent
  },
  {
    path: 'portfolio/:portfolio-name/:portfolio-lob/scm-commits/product/:product-id',
    component: SCMCommitsDetailComponent,
    data: { animation: SCMCommitsConfiguration.identifier}
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

export class SCMCommitsRoutingModule { }
