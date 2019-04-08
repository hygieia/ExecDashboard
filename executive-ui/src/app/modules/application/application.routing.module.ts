import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApplicationComponent } from './components/application/application.component';
import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
  {
    path: 'application', component: ApplicationComponent,
    data: { animation: 'application'}
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
export class ApplicationRoutingModule { }
