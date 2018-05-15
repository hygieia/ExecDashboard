import {Component, OnInit} from '@angular/core';
import {Params, ActivatedRoute, Router} from '@angular/router';
import {PortfolioService} from '../../../../../shared/shared.module';
import {Portfolio} from '../../../../../shared/domain-models/portfolio';
import {ProductService} from '../../../shared/services/product.service';
import {BuildingBlockModel} from '../../../shared/component-models/building-block-model';
import {HeadingModel} from '../../../../../shared/component-models/heading-model';
import {MetricBuildingBlocksMapStrategy} from '../../strategies/metric-building-blocks-map-strategy';
import {SCMCommitsConfiguration} from '../../../metrics/scm-commits/scm-commits.configuration';
import {ItemType} from '../../../shared/component-models/item-type';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
  providers: [
    PortfolioService,
    ProductService
  ]
})
export class DashboardComponent implements OnInit {
  public portfolioHeading: string;
  public portfolioName: string;
  public portfolioLob: string;
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
              private productService: ProductService,
              private metricBuildingBlocksMapStrategy: MetricBuildingBlocksMapStrategy) {
  }

  ngOnInit() {
    this.route.params.subscribe((params: Params) => {
      this.portfolioName = params['portfolio-name'];
      this.portfolioLob = params['portfolio-lob'];
      this.productId = params['product-id'];

      this.portfolioService.getPortfolio(this.portfolioName, this.portfolioLob)
        .subscribe(
          result => {
            this.portfolioHeading = `${getPortfolioName(result)}'s Portfolio`;
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

        this.productService.getProductComponents(this.portfolioName, this.portfolioLob, this.productId)
          .subscribe(
            result => {
              this.metricToBuildingBlocksMap = this.metricBuildingBlocksMapStrategy.parse(result);
              this.headingModel = this.getHeadingModel();
            },
            error => console.log(error)
          );
      } else { // Get all the products for the given portfolio
        this.productService.getPortfolioProducts(this.portfolioName, this.portfolioLob)
          .subscribe(
            result => {
              this.metricToBuildingBlocksMap = this.metricBuildingBlocksMapStrategy.parse(result);
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

  getHeadingModel() {
    return {
      primaryText: this.getHeading(),
      icon: this.getIcon(),
      crumbs: this.getCrumbs(),
      button: this.getReturnRouteModel()
    };
  }

  getReturnRouteModel() {
    if (this.productId) {
      return {
        label: 'Change Product',
        commands: ['portfolio', this.portfolioName, this.portfolioLob, 'products'],
        extras: {}
      };
    } else {
      return {
        label: 'Change Portfolio',
        commands: ['directory'],
        extras: {}
      };
    }
  }

  showBuildingBlocksForMetric(metric: any) {
    const metricType = metric.metric;
    const listingType = metric.listingType;
    this.buildingBlocks = this.metricToBuildingBlocksMap.get(metricType);

    if (metricType === SCMCommitsConfiguration.identifier
        && (listingType === ItemType.component)) {
      this.buildingBlocks = this.filterComponentBuildingBlocksForMetric(this.buildingBlocks);
    }
    this.showBuildingBlocks = true;
  }

  hideBuildingBlocks() {
    this.showBuildingBlocks = false;
  }

  goToProductList() {
    this.router.navigate(['products'], {relativeTo: this.route});
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
        commands: ['portfolio', this.portfolioName, this.portfolioLob],
        extras: {}
      }];
    }

    return [];
  }

  filterComponentBuildingBlocksForMetric(buildingBlocks: BuildingBlockModel[]): BuildingBlockModel[] {
    let filteredBuildingBlocks: BuildingBlockModel[];

    filteredBuildingBlocks = buildingBlocks
                              .filter((buildingBlockModel) => {
                                return buildingBlockModel.itemType === ItemType.component;
                              });

    return filteredBuildingBlocks;
  }
}
