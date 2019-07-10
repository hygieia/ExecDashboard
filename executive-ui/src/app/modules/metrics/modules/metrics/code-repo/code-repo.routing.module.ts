import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { CodeRepoDetailComponent } from './components/metric-detail/code-repo-detail.component';
import { CodeRepoConfiguration } from './code-repo.configuration';

const routes: Routes = [
  {
    path: 'portfolio/:portfolio-id/stash',
    component: CodeRepoDetailComponent
  },
  {
    path: 'portfolio/:portfolio-id/stash/product/:product-id',
    component: CodeRepoDetailComponent,
    data: { animation: CodeRepoConfiguration.identifier }
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
export class CodeRepoRoutingModule { }
