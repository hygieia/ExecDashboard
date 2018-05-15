import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeadingComponent } from './components/heading/heading.component';
import { PortfolioService } from './services/portfolio.service';
export { PortfolioService } from './services/portfolio.service';
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
  providers: [PortfolioService]
})
export class SharedModule { }
