import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Routes, RouterModule} from '@angular/router';
import {ProgramComponent} from './components/programs/programs.component';
import {ProgramDetailsComponent} from './components/program-details/program-details.component';

const routes: Routes = [    
 /* {
    path: 'programs',
    component: ProgramComponent,
    data: { animation: 'metric-metrics' }
  }, */
  {
    path: 'programs/:portfolio-id',
    component: ProgramComponent,
    data: { animation: 'product' }
  },
  {
    path: 'programs/:portfolio-id/detail',
    component: ProgramDetailsComponent,
    data: { animation: 'product' }
  }
  /*{
    path: '',
    redirectTo: 'programs/:portfolio-id',
    pathMatch: 'full',

  } */
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(routes)
  ],
  declarations: [],
  exports: [RouterModule]
})

export class ProgramRoutingModule { }
