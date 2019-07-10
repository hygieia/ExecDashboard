import { NgModule } from '@angular/core';
import {Component} from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialModule, NoConflictStyleCompatibilityMode } from '@angular/material';
import { ExternalMonitorRoutingModule } from './externalmonitor.routing.module';
import { SharedModule } from '../shared/shared.module';
import { ExternalMonitorComponent } from './components/externalmonitor/externalmonitor.component';
import { FlexLayoutModule } from '@angular/flex-layout';
import { AngularMultiSelectModule } from 'angular2-multiselect-dropdown/multiselect.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Ng4LoadingSpinnerModule, Ng4LoadingSpinnerComponent, Ng4LoadingSpinnerService } from 'ng4-loading-spinner';
import { Angular2FontawesomeModule } from 'angular2-fontawesome/angular2-fontawesome';
import { NgxPaginationModule } from 'ngx-pagination';
import { InstanceModule } from '../instance/instance.module';
import { InstanceComponent } from '../instance/components/instance/instance.component';
import { VonkinatorComponent } from '../vonkinator/components/vonkinator/vonkinator.component';
import { AdminAccessComponent } from '../admin-access/components/admin-access.component';


@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    FlexLayoutModule,
    ExternalMonitorRoutingModule,
    AngularMultiSelectModule,
    FormsModule, ReactiveFormsModule,
    Ng4LoadingSpinnerModule,
    Angular2FontawesomeModule,
    NgxPaginationModule,
    MaterialModule,
    NoConflictStyleCompatibilityMode
  ],
  declarations: [
    ExternalMonitorComponent,
    InstanceComponent,
    VonkinatorComponent,
    AdminAccessComponent
  ],
  bootstrap: [ExternalMonitorComponent]
})

export class ExternalMonitorModule { }
