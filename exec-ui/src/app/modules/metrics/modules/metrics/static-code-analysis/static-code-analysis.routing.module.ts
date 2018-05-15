import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Routes, RouterModule} from '@angular/router';
import {StaticCodeAnalysisDetailComponent} from './components/metric-detail/static-code-analysis-detail.component';
import {StaticCodeAnalysisConfiguration} from './static-code-analysis.configuration';

const routes: Routes = [
  {
    path: 'portfolio/:portfolio-name/:portfolio-lob/static-code-analysis',
    component: StaticCodeAnalysisDetailComponent
  },
  {
    path: 'portfolio/:portfolio-name/:portfolio-lob/static-code-analysis/product/:product-id',
    component: StaticCodeAnalysisDetailComponent,
    data: {animation: StaticCodeAnalysisConfiguration.identifier}
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

export class StaticCodeAnalysisRoutingModule {
}
