import { NgModule } from '@angular/core';
import {Component} from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialModule, NoConflictStyleCompatibilityMode } from '@angular/material';
import { StatusRoutingModule } from './status.routing.module';
import { SharedModule } from '../shared/shared.module';
import { StatusComponent } from './components/status/status.component';
import { StatusGraphComponent } from './components/status-graph/status-graph.component';
import { FlexLayoutModule } from '@angular/flex-layout';
import { AngularMultiSelectModule } from 'angular2-multiselect-dropdown/multiselect.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Ng4LoadingSpinnerModule, Ng4LoadingSpinnerComponent, Ng4LoadingSpinnerService } from 'ng4-loading-spinner';
import { Angular2FontawesomeModule } from 'angular2-fontawesome/angular2-fontawesome';
import { NgxPaginationModule } from 'ngx-pagination';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    FlexLayoutModule,
    StatusRoutingModule,
    AngularMultiSelectModule,
    FormsModule, ReactiveFormsModule,
    Ng4LoadingSpinnerModule,
    Angular2FontawesomeModule,
    NgxPaginationModule,
    MaterialModule,
    NoConflictStyleCompatibilityMode
  ],
  declarations: [
    StatusComponent,
    StatusGraphComponent
  ],
  bootstrap: [StatusComponent]
})

export class StatusModule { }
