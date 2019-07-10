import { NgModule } from '@angular/core';
import {Component} from '@angular/core';
import { CommonModule } from '@angular/common';
import { DirectoryRoutingModule } from './directory.routing.module';
import { SharedModule } from '../shared/shared.module';
import { DirectoryComponent } from './components/directory/directory.component';
import { PortfolioCardsComponent } from './components/portfolio-cards/portfolio-cards.component';
import { PortfolioCardComponent } from './components/portfolio-card/portfolio-card.component';
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
    DirectoryRoutingModule,
    AngularMultiSelectModule,
    FormsModule, ReactiveFormsModule,
    Ng4LoadingSpinnerModule,
    Angular2FontawesomeModule,
    NgxPaginationModule
  ],
  declarations: [
    DirectoryComponent,
    PortfolioCardsComponent,
    PortfolioCardComponent,
  ],
  bootstrap: [DirectoryComponent]
})

export class DirectoryModule { }
