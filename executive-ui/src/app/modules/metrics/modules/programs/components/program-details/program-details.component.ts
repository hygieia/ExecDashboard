import {Component, OnInit} from '@angular/core';
import {Params, ActivatedRoute, Router} from '@angular/router';
import {PortfolioService} from '../../../../../shared/shared.module';
import {Portfolio} from '../../../../../shared/domain-models/portfolio';
import {ProductService} from '../../../shared/services/product.service';
import {BuildingBlockModel} from '../../../shared/component-models/building-block-model';
import {HeadingModel} from '../../../../../shared/component-models/heading-model';
//import {Clarity} from '../../../shared/domain-models/clarity';


@Component({
  selector: 'app-program-details',
  templateUrl: './program-details.component.html',
  styleUrls: ['./program-details.component.scss'],
  providers: [
    PortfolioService,
    ProductService
  ]
})
export class ProgramDetailsComponent implements OnInit {

  //@Input() public clarity: Clarity;

  public portfolioId: string;
  


  public portfolioHeading: string;
  public productHeading: string;
  public productId: string;
  public role: string;
  public buildingBlocks: BuildingBlockModel[];
  public showBuildingBlocks: boolean;
  public headingModel: HeadingModel;

  private metricToBuildingBlocksMap = new Map<string, BuildingBlockModel[]>();

  constructor(private router: Router,
              private route: ActivatedRoute,
              private portfolioService: PortfolioService,
              private productService: ProductService) {
              
  }

  ngOnInit() {
    this.route.params.subscribe((params: Params) => {
      this.portfolioId = params['portfolio-id'];
      this.productId = params['product-id'];
     
    });

    this.headingModel = this.getHeadingModel();

    function getPortfolioName(portfolio: Portfolio) {
      if (portfolio.name) {
        return portfolio.name;
      }
      return `${portfolio.executive.firstName} ${portfolio.executive.lastName}`;
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

  getIcon() {
    return this.productId ? 'box' : 'briefcase';
  }

  getHeading() {
    return this.productId ? this.productHeading : this.portfolioHeading;
  }

  getCrumbs() {
    if (this.productId) {
      return [{
        label: this.portfolioHeading,
        commands: ['portfolio', this.portfolioId],
        extras: {}
      }];
    }

    return [];
  }

  getReturnRouteModel() {
    if (this.productId) {
      return {
        label: 'Change Application',
        commands: ['portfolio', this.portfolioId, 'products'],
        extras: {}
      };
    } else {
      return {
        label: 'Change Portfolio',
        commands: ['porfolio'],
        extras: {}
      };
    }
  }
}
