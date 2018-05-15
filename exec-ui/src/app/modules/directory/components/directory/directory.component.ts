import { PortfolioService, Portfolio } from '../../../shared/shared.module';
import {Component, OnInit, Input} from '@angular/core';
import { SortingService } from '../../../../services/sorting.service';
import {HeadingModel} from '../../../shared/component-models/heading-model';
import { DirectoryHeadingStrategy } from '../../strategies/directory-heading-strategy';

@Component({
  selector: 'app-directory',
  templateUrl: './directory.component.html',
  styleUrls: ['./directory.component.scss'],
  providers: [PortfolioService, SortingService, DirectoryHeadingStrategy]
})

export class DirectoryComponent implements OnInit {

  @Input() public heading = 'Select an Executive';
  public portfolios = new Array<Portfolio>();
  private allPortfolios = new Array<Portfolio>();
  public headingModel: HeadingModel;
  public qryString = '';

  constructor(private portfolioService: PortfolioService,
              private sortingService: SortingService,
              private strategy: DirectoryHeadingStrategy) { }

  ngOnInit() {
    this.headingModel = this.strategy.parse();

    this.portfolioService.getPortfolios()
      .subscribe(
        result => {
          this.sortingService
            .sort<Portfolio>({array: result, byProperty: 'executive.lastName', thenByProperty: 'executive.firstName'})
            .forEach((portfolio) =>
              this.portfolios.push(portfolio));
          this.allPortfolios = this.portfolios;
        },
        error => {
          console.log(error);
        }
      );
  }

  search() {
    this.portfolios = this.allPortfolios;
    const value = this.qryString;
    if (this.qryString && !!this.qryString.length) {
      const searchResult = new Array<Portfolio>();
      this.portfolios.forEach(element => {
        if ((element.executive.firstName).toLowerCase().includes(value.toLowerCase())
          || (element.executive.lastName).toLowerCase().includes(value.toLowerCase())) {
          searchResult.push(element);
        }
      });
      this.portfolios = searchResult;
    }
  }
}
