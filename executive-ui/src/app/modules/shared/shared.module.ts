import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeadingComponent } from './components/heading/heading.component';
import { ApplicationService } from './services/application.service';
import { PortfolioService } from './services/portfolio.service';
export { ApplicationService } from './services/application.service';
export { PortfolioService } from './services/portfolio.service';
export { Application } from './domain-models/application';
export { Portfolio } from './domain-models/portfolio';
export { Executive } from './domain-models/executive';

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [
    HeadingComponent
  ],
  exports: [
    HeadingComponent
  ],
  providers: [ApplicationService, PortfolioService]
})
export class SharedModule { }
