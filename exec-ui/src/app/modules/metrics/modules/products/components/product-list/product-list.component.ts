import {Component, OnInit} from '@angular/core';
import {Params, ActivatedRoute, Router} from '@angular/router';
import {PortfolioService} from '../../../../../shared/shared.module';
import {Portfolio} from '../../../../../shared/domain-models/portfolio';
import {ProductService} from '../../../shared/services/product.service';
import {BuildingBlockModel} from '../../../shared/component-models/building-block-model';
import {HeadingModel} from '../../../../../shared/component-models/heading-model';
import {ProductListProductsStrategy} from '../../strategies/product-list-products-strategy';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss'],
  providers: [
    PortfolioService,
    ProductService
  ]
})
export class ProductListComponent implements OnInit {
  public portfolioHeading: string;
  public portfolioName: string;
  public portfolioLob: string;
  public productHeading: string;
  public productId: string;
  public role: string;
  public products: BuildingBlockModel[];
  public headingModel: HeadingModel;
  public searchString: string;
  allProducts = [];

  constructor(private router: Router,
              private activatedRoute: ActivatedRoute,
              private portfolioService: PortfolioService,
              private productService: ProductService,
              private productListProductsStrategy: ProductListProductsStrategy) {
  }

  ngOnInit() {
    this.activatedRoute.params.subscribe((params: Params) => {
      this.portfolioName = params['portfolio-name'];
      this.portfolioLob = params['portfolio-lob'];
      this.productId = params['product-id'];

      this.portfolioService.getPortfolio(this.portfolioName, this.portfolioLob)
        .subscribe(
          result => {
            this.portfolioHeading = `${getPortfolioName(result)}'s Products`;
            this.role = result.executive.role;
            this.headingModel = this.getHeadingModel();
          },
          error => {
            console.log(error);
          }
        );

      if (this.productId) {
        this.productService.getPortfolioProduct(this.portfolioName, this.portfolioLob, this.productId)
          .subscribe(
            result => {
              this.productHeading = result.name;
              this.headingModel = this.getHeadingModel();
            },
            error => console.log(error)
          );

      } else {
        this.productService.getPortfolioProducts(this.portfolioName, this.portfolioLob)
          .subscribe(
            result => {
              this.products = this.productListProductsStrategy.parse(result);
              this.allProducts = this.products;
              this.headingModel = this.getHeadingModel();
            },
            error => console.log(error)
          );
      }
    });

    this.headingModel = this.getHeadingModel();

    function getPortfolioName(portfolio: Portfolio) {
      if (portfolio.name) {
        return portfolio.name;
      }
      return `${portfolio.executive.firstName} ${portfolio.executive.lastName}`;
    }
  }

  search() {
    this.products = this.allProducts;
    const value = this.searchString;

    if (this.searchString && !!this.searchString.length) {
      const searchResults = [];
      this.products.forEach(element => {
        if ((element.name).toLowerCase().includes(value.toLowerCase())) {
          searchResults.push(element);
        }
      });

      this.products = searchResults;
    }
  }

  getHeadingModel() {
    return {
      primaryText: this.getHeading(),
      icon: this.getIcon(),
      crumbs: this.getCrumbs(),
      button: this.getReturnRouteModel()
    };
  }

  getReturnRouteModel() {
    return {
      label: 'Change Portfolio',
      commands: ['directory'],
      extras: {}
    };
  }

  getIcon() {
    return 'briefcase';
  }

  getHeading() {
    return this.portfolioHeading;
  }

  getCrumbs() {
    return [];
  }

  goToPortfolioDashboard() {
    this.router.navigate(['portfolio', this.portfolioName, this.portfolioLob]);
  }
}
