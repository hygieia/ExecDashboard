import { BuildingBlockModel } from '../../component-models/building-block-model';
import { Input } from '@angular/core';
import { Strategy } from '../../../../../shared/strategies/strategy';
import { MetricDetailModel } from '../../component-models/metric-detail-model';
import {MetricService} from '../../services/metric.service';
import {Router} from '@angular/router';
import {PortfolioService} from '../../../../../shared/services/portfolio.service';
import {Portfolio} from '../../../../../shared/domain-models/portfolio';
import {ProductService} from '../../services/product.service';
import {HeadingModel} from '../../../../../shared/component-models/heading-model';
import {NavigationModel} from '../../../../../shared/component-models/navigation-model';
import {Location} from '@angular/common';
import {BuildingBlockMetricSummary} from '../../domain-models/building-block-metric-summary';

/**
 *This is a base component class for displaying component instances in the metric detail container component.
 *Extend this class to build details views for additional metrics.
 * @export
 * @abstract
 * @class MetricDetailBaseComponent
 */
export abstract class MetricDetailBaseComponent {
  @Input() public portfolioName: string;
  @Input() public portfolioLob: string;
  @Input() public productId: string;
  @Input() public heading: string;
  @Input() public metricOwnerSubTitle: string;
  public productName: string;

  public dataService: MetricService;
  public detailStrategy: Strategy<any, MetricDetailModel>;
  public buildingBlockStrategy: Strategy<any, BuildingBlockModel[]>;
  public metricDetailView: MetricDetailModel;
  public buildingBlocks: BuildingBlockModel[];

  protected portfolioService: PortfolioService;
  protected productService: ProductService;
  protected router: Router;
  protected location: Location;

  public headingModel: HeadingModel;

  constructor() {}

  abstract getHeader();

  getReturnRouteModel(): NavigationModel {
    if (this.productId) {
      return {
        label: 'Back',
        action: () => this.location.back(),
        extras: {}
      };
    } else {
      return {
        label: 'Change Metric',
        commands: ['portfolio', this.portfolioName, this.portfolioLob],
        extras: {}
      };
    }
  }

  loadData() {
    this.loadDetailData();
    this.loadBuildingBlocks();
    this.loadPortfolioData();
    this.loadProductData();
  }

  getCrumbs() {
    if (this.productId) {
      return [{
        label: this.metricOwnerSubTitle,
        commands: ['portfolio', this.portfolioName, this.portfolioLob],
        extras: {}
      }, {
        label: this.productName,
        commands: ['portfolio', this.portfolioName, this.portfolioLob, 'product', this.productId],
        extras: {}
      }];
    }
    return [{
      label: this.metricOwnerSubTitle,
      commands: ['portfolio', this.portfolioName, this.portfolioLob],
      extras: {}
    }];
  }

  getIcon() {
    return this.productId ? 'box' : 'briefcase';
  }

  private loadDetailData() {
    this.getDetail()
      .subscribe(
        result => {
          const details = this.detailStrategy.parse(result);

          this.metricDetailView = Object.assign(details);
          this.headingModel = this.getHeader();
        },
        error => {
          console.log(error);
        }
      );
  }

  private loadBuildingBlocks() {
    this.getBuildingBlocks()
      .subscribe(
        result => {
          this.buildingBlocks = this.buildingBlockStrategy.parse(result);
            if(localStorage.getItem("DetailsConditon")){
                this.buildingBlocks.forEach((value,index)=>{
                    value.metrics.forEach((val,ind)=>{
                        if(val.value.name !== "Error Rate"){
                            value.metrics.splice(ind,2)
                        }
                    })
                })
            }
          this.headingModel = this.getHeader();
        },
        error => {
          console.log(error);
        }
      );
  }

  private loadPortfolioData() {
    if (!this.metricOwnerSubTitle) {
      this.portfolioService.getPortfolio(this.portfolioName, this.portfolioLob)
        .subscribe(
          result => {
            this.metricOwnerSubTitle = metricOwnerSubTitle(result);
            this.headingModel = this.getHeader();
          },
          error => {
            console.log(error);
          }
        );
    }

    function metricOwnerSubTitle(portfolio: Portfolio) {
      if (!portfolio.name) {
        return `${possessive(`${portfolio.executive.firstName} ${portfolio.executive.lastName}`)} Portfolio`;
      }
      return `${portfolio.name} Portfolio`;

      function possessive(string) {
        return string[string.length - 1] === 's' ? `${string}'` : `${string}'s`;
      }
    }
  }

  private loadProductData() {
    if (this.productId) {
      this.productService.getPortfolioProduct(this.portfolioName, this.portfolioLob, this.productId)
        .subscribe(
          result => {
            this.productName = result.name;
            this.headingModel = this.getHeader();
          },
          error => console.log(error)
        );
    }
  }

  private getDetail() {
    if (this.productId) {
      return this.dataService.getProductDetail(this.portfolioName, this.portfolioLob, this.productId);
    }
    return this.dataService.getPortfolioDetail(this.portfolioName, this.portfolioLob);
  }

  private getBuildingBlocks() {
    if (this.productId) {
      return this.dataService.getProductComponents(this.portfolioName, this.portfolioLob, this.productId);
    }
    return this.dataService.getPortfolioProducts(this.portfolioName, this.portfolioLob);
  }
}
