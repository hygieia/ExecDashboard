import { Portfolio } from '../../../shared/shared.module';
import { Component, ElementRef, Input, OnInit } from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-portfolio-card',
  templateUrl: './portfolio-card.component.html',
  styleUrls: ['./portfolio-card.component.scss']
})
export class PortfolioCardComponent implements OnInit {

  @Input() public portfolio: Portfolio;
  public id: string;
  public name: string;
  public role: string;
  public lob: string;
  public initials: string;

  constructor(private router: Router) {
  }

  ngOnInit() {

    this.id = this.portfolio.id;
    this.name = this.portfolio.name || this.formatExecutiveName(this.portfolio.executive);
    this.role = this.portfolio.executive.role;
    this.lob = this.portfolio.lob;
    this.initials = !this.portfolio.name
      ? this.portfolio.executive.firstName.charAt(0).concat(this.portfolio.executive.lastName.charAt(0))
      : this.portfolio.name.charAt(0);
  }

  formatCardNameWithFirstInitial(first: string, last: string) {
    return `${first[0].slice(0, 1)}. ${last}`;
  }

  formatExecutiveName(executive) {
    const nameLength =
      (this.portfolio.name || `${executive.firstName} ${executive.lastName}` as string).length;

    return nameLength >= 27
      ? this.formatCardNameWithFirstInitial(executive.firstName, executive.lastName)
      : `${executive.firstName} ${executive.lastName}`;
  }
  goToPortfolio(name: string, lob: string) {
    this.router.navigate(['portfolio', name, lob]);
  }
}
