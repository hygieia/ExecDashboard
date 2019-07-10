import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ExternalMonitorComponent } from './components/externalmonitor/externalmonitor.component';
import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
  {
    path: '', component: ExternalMonitorComponent,
    data: { animation: 'status' }
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
export class ExternalMonitorRoutingModule { }