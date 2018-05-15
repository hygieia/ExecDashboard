import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DirectoryRoutingModule } from './directory.routing.module';
import { SharedModule } from '../shared/shared.module';
import { DirectoryComponent } from './components/directory/directory.component';
import { PortfolioCardsComponent } from './components/portfolio-cards/portfolio-cards.component';
import { PortfolioCardComponent } from './components/portfolio-card/portfolio-card.component';
import { FlexLayoutModule } from '@angular/flex-layout';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    FlexLayoutModule,
    DirectoryRoutingModule,
    FormsModule,
    ReactiveFormsModule
  ],
  declarations: [
    DirectoryComponent,
    PortfolioCardsComponent,
    PortfolioCardComponent,
  ]
})

export class DirectoryModule { }
