import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApplicationRoutingModule } from './application.routing.module';
import { SharedModule } from '../shared/shared.module';
import { ApplicationComponent } from './components/application/application.component';
import { ApplicationCardComponent } from './components/application-card/application-card.component';
import { ApplicationCardsComponent } from './components/application-cards/application-cards.component';
import { FlexLayoutModule } from '@angular/flex-layout';
import { AngularMultiSelectModule } from 'angular2-multiselect-dropdown/multiselect.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Ng4LoadingSpinnerModule} from 'ng4-loading-spinner';
import { Angular2FontawesomeModule } from 'angular2-fontawesome/angular2-fontawesome';
import { NgxPaginationModule } from 'ngx-pagination';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    FlexLayoutModule,
    ApplicationRoutingModule,
    AngularMultiSelectModule,
    FormsModule, ReactiveFormsModule,
    Ng4LoadingSpinnerModule,
    Angular2FontawesomeModule,
    NgxPaginationModule
  ],
  declarations: [
    ApplicationComponent, ApplicationCardComponent, ApplicationCardsComponent
  ],
  exports: [
    ApplicationComponent
  ],
  bootstrap: [ApplicationComponent]
})

export class ApplicationModule { }
