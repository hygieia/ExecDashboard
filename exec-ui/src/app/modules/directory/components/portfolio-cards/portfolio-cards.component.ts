import { Portfolio } from '../../../shared/shared.module';
import { Component, HostBinding, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-portfolio-cards',
  templateUrl: './portfolio-cards.component.html',
  styleUrls: ['./portfolio-cards.component.scss'],
})
export class PortfolioCardsComponent implements OnInit {

  @Input() public portfolios: Portfolio[];
  constructor() { }

  ngOnInit() {
  }

}
