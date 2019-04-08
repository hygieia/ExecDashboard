import { Component, OnInit } from '@angular/core';
import { Params, ActivatedRoute, Router } from '@angular/router';
import { PortfolioService } from '../../../../../shared/shared.module';
import { Portfolio } from '../../../../../shared/domain-models/portfolio';
import { ProductService } from '../../../shared/services/product.service';
import { BuildingBlockModel } from '../../../shared/component-models/building-block-model';
import { HeadingModel } from '../../../../../shared/component-models/heading-model';
import { MetricBuildingBlocksMapStrategy } from '../../strategies/metric-building-blocks-map-strategy';
import { Ng4LoadingSpinnerModule, Ng4LoadingSpinnerComponent, Ng4LoadingSpinnerService } from 'ng4-loading-spinner';
import { AuthService } from '../../../../../../services/vz/auth.service';
import { TrackService } from '../../../../../../services/vz/track.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
  providers: [
    PortfolioService,
    ProductService,
    Ng4LoadingSpinnerComponent
  ]
})
export class DashboardComponent implements OnInit {
  public portfolioHeading: string;
  public portfolioId: string;
  public productHeading: string;
  public productId: string;
  public role: string;
  public buildingBlocks: BuildingBlockModel[];
  public showBuildingBlocks: boolean;
  public headingModel: HeadingModel;
  public productView: boolean = false;
  public metric: string;
  public showSpinner: boolean = true;
  public showSpinnerForApp: string = 'visible spinner right';
  private metricToBuildingBlocksMap = new Map<string, BuildingBlockModel[]>();
  private selectedMetric: any;
  constructor(private router: Router,
    private route: ActivatedRoute,
    private portfolioService: PortfolioService,
    private productService: ProductService,
    private metricBuildingBlocksMapStrategy: MetricBuildingBlocksMapStrategy,
    private ng4LoadingSpinnerService: Ng4LoadingSpinnerService,
    private authService: AuthService,
    private trackService: TrackService) {
    this.showSpinner = true;
  }

  ngOnInit() {
    this.ng4LoadingSpinnerService.show();
    this.route.params.subscribe((params: Params) => {
      this.portfolioId = params['portfolio-id'];
      this.productId = params['product-id'];
      this.portfolioService.getPortfolio(this.portfolioId)
        .subscribe(
          result => {
            this.portfolioHeading = `${getPortfolioName(result)}'s Portfolio`;
            this.role = result.executive.role;
            this.headingModel = this.getHeadingModel();
            if (!this.productId) {
              this.trackExecMetrics(result.eid);
            }
          },
          error => {
            console.log(error);
          }
        );

      if (this.productId) {
        this.productService.getPortfolioProduct(this.portfolioId, this.productId)
          .subscribe(
            result => {
              this.productHeading = result.name;
              this.headingModel = this.getHeadingModel();
              this.trackAppMetrics(result.metricLevelId);
            },
            error => {
              console.log(error);
            }
          );

        this.productService.getProductComponents(this.portfolioId, this.productId)
          .subscribe(
            result => {
              this.metricToBuildingBlocksMap = this.metricBuildingBlocksMapStrategy.parse(result);
              this.headingModel = this.getHeadingModel();
              this.ng4LoadingSpinnerService.hide();
              if (this.showBuildingBlocks) {
                this.showBuildingBlocksForMetric(this.selectedMetric);
              }
              this.showSpinnerForApp = 'hidden spinner';
            },
            error => {
              console.log(error);
              this.ng4LoadingSpinnerService.hide();
              if (this.showBuildingBlocks) {
                this.showBuildingBlocksForMetric(this.selectedMetric);
              }
              this.showSpinnerForApp = 'hidden spinner';
            }
          );
      } else {
        this.productService.getPortfolioProducts(this.portfolioId)
          .subscribe(
            result => {
              this.metricToBuildingBlocksMap = this.metricBuildingBlocksMapStrategy.parse(result);
              this.headingModel = this.getHeadingModel();
              this.ng4LoadingSpinnerService.hide();
              if (this.showBuildingBlocks) {
                this.showBuildingBlocksForMetric(this.selectedMetric);
              }
              this.showSpinnerForApp = 'hidden spinner';
            },
            error => {
              console.log(error);
              this.ng4LoadingSpinnerService.hide();
              if (this.showBuildingBlocks) {
                this.showBuildingBlocksForMetric(this.selectedMetric);
              }
              this.showSpinnerForApp = 'hidden spinner';
            }
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

      if (this.productId == this.portfolioId) {
        return {
          label: 'Change Application',
          commands: ['application'],
          extras: {}
        };

      } else {
        return {
          label: 'Change Application',
          commands: ['portfolio', this.portfolioId, 'products'],
          extras: {}
        };

      }


    } else {
      return {
        label: 'Change Portfolio',
        commands: ['portfolio'],
        extras: {}
      };
    }
  }

  trackExecMetrics(eid: string) {
    const eids: string[] = new Array();
    eids.push(eid);
    this.trackService.savePageTrackForExec('Portfolio View', this.authService.getAuthEid(), eids).subscribe(
      result => {
      },
      error => {
      }
    );
  }

  trackAppMetrics(appId: string) {
    const appIds: string[] = new Array();
    appIds.push(appId);
    this.trackService.savePageTrackForApp('Product View', this.authService.getAuthEid(), appIds).subscribe(
      results => {
      },
      error => {
      }
    );
  }

  showBuildingBlocksForMetric(metric: any) {
    this.selectedMetric = metric;
    if (this.productId) {
      this.metric = metric;
    }
    this.buildingBlocks = this.metricToBuildingBlocksMap.get(metric);
    this.showBuildingBlocks = true;
  }

  hideBuildingBlocks() {
    this.showBuildingBlocks = false;
  }

  goToProductList() {
    this.router.navigate(['products'], { relativeTo: this.route });
  }

  goToPortfolioList() {
    this.router.navigate(['executives'], { relativeTo: this.route });
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
  setSpinnerStatus(value: boolean) {
    this.showSpinner = value;
  }
  setSpinnerForAppln(val: boolean) {
    if (val === false) {
      this.showSpinnerForApp = 'hidden spinner';
    }
  }
}
