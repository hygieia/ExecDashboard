import { BuildingBlockModel } from '../../component-models/building-block-model';
import { Input } from '@angular/core';
import { Strategy } from '../../../../../shared/strategies/strategy';
import { MetricDetailModel } from '../../component-models/metric-detail-model';
import { MetricService } from '../../services/metric.service';
import { Router } from '@angular/router';
import { PortfolioService } from '../../../../../shared/services/portfolio.service';
import { Portfolio } from '../../../../../shared/domain-models/portfolio';
import { ProductService } from '../../services/product.service';
import { HeadingModel } from '../../../../../shared/component-models/heading-model';
import { NavigationModel } from '../../../../../shared/component-models/navigation-model';
import { Location } from '@angular/common';
import { AuthService } from '../../../../../../services/vz/auth.service';
import { TrackService } from '../../../../../../services/vz/track.service';
import { Ng4LoadingSpinnerModule, Ng4LoadingSpinnerComponent, Ng4LoadingSpinnerService } from 'ng4-loading-spinner';
import { DevopscupScores } from '../../../../../shared/domain-models/devopscupScores';
import { Observable } from 'rxjs/Observable';
import { DevopscupService } from '../../../metrics/devopscup/services/devopscup.service';
import { Vast } from '../../../../../shared/domain-models/vast';
import { DevopscupRoundDetails } from '../../../../../shared/domain-models/devopscupRoundDetails';

/**
 *This is a base component class for displaying component instances in the metric detail container component.
 *Extend this class to build details views for additional metrics.
 * @export
 * @abstract
 * @class MetricDetailBaseComponent
 */
export abstract class MetricDetailBaseComponent {
  @Input() public portfolioId: string;
  @Input() public productId: string;
  @Input() public heading: string;
  @Input() public metricOwnerSubTitle: string;
  @Input() public userId: string;
  public productName: string;
  public isProduct: boolean;
  public showApplicationreporting: boolean;
  public isOutages: boolean;
  public isEvents: boolean;
  public isSayDoRatio: boolean;
  public isTest: boolean;
  public isCloud: boolean;
  public isDevopscup: boolean;
  public isStoriesOrCloud: boolean;
  public qryString = '';

  public dataService: MetricService;
  public devopscupService: DevopscupService;
  public detailStrategy: Strategy<any, MetricDetailModel>;
  public buildingBlockStrategy: Strategy<any, BuildingBlockModel[]>;
  public metricDetailView: MetricDetailModel;
  public buildingBlocks: BuildingBlockModel[];
  //public devopscupScoresStrategy  :Strategy<any, DevopscupScores[]>;
  public devopscupScoresList: DevopscupScores[] = new Array<DevopscupScores>();
  public vastDetailsList: Map<string, Vast>;
  public devopscupRoundDet: DevopscupRoundDetails;

  protected portfolioService: PortfolioService;
  protected authService: AuthService;
  protected trackService: TrackService;
  protected ng4LoadingSpinnerService: Ng4LoadingSpinnerService;
  protected productService: ProductService;
  protected router: Router;
  protected location: Location;

  public headingModel: HeadingModel;
  public showDevopscupFilter: boolean = true;
  allApplications = [];
  public spinnerVisibility: string = 'visible spinner center';

  constructor() {
    this.spinnerVisibility = 'visible spinner center';
  }

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
        commands: ['portfolio', this.portfolioId],
        extras: {}
      };
    }
  }

  loadData() {
    this.showApplicationreporting = true;
    this.isOutages = true;
    this.isSayDoRatio = false;
    this.isTest = false;
    this.isProduct = false;
    this.isCloud = false;
    this.isDevopscup = false;
    this.isEvents = false;
    this.isStoriesOrCloud = false;
    this.ng4LoadingSpinnerService.show();
    this.loadDetailData();
    this.loadPortfolioData();
    this.loadProductData();
    this.loadBuildingBlocks();
    this.getDevopscupDetails();
  }

  getDevopscupDetails() {
    if (this.devopscupService != undefined) {
      this.devopscupService.getPortfolioProductsForDevopscup(this.portfolioId).subscribe(
        result => {
          this.devopscupScoresList = this.parseDevopscupDetails(result);
          this.ng4LoadingSpinnerService.hide();
        },
        error => {
          console.log('Error While assigning devopscup details::' + error);
          this.ng4LoadingSpinnerService.hide();
        }
      );

      this.devopscupService.getVastDetailsForDevopscup().subscribe(
        result => {
          this.vastDetailsList = result;
        },
        error => {
          console.log('Error While assigning vast details::' + error);
        }
      );

      this.devopscupService.getRoundDetailsForDevopscup().subscribe(
        result => {
          this.devopscupRoundDet = result;
        },
        error => {
          console.log('Error While assigning vast details::' + error);
        }
      );
    }
  }
  parseDevopscupDetails(model: DevopscupScores[]): DevopscupScores[] {
    const devopscupScoreDetails = new Array<DevopscupScores>();
    if (this.productName != undefined) {
      model.forEach((p) => {
        if (p.appName === this.productName) {
          devopscupScoreDetails.push(p);
        }
      });
      this.showDevopscupFilter = false;
    }
    else {
      model.forEach((p) => {
        devopscupScoreDetails.push(p);
      });
      this.showDevopscupFilter = true;
    }
    return devopscupScoreDetails;
  }
  getCrumbs() {
    if (this.productId) {
      return [{
        label: this.metricOwnerSubTitle,
        commands: ['portfolio', this.portfolioId],
        extras: {}
      }, {
        label: this.productName,
        commands: ['portfolio', this.portfolioId, 'product', this.productId],
        extras: {}
      }];
    }
    return [{
      label: this.metricOwnerSubTitle,
      commands: ['portfolio', this.portfolioId],
      extras: {}
    }];
  }

  getIcon() {
    return this.productId ? 'box' : 'briefcase';
  }


  search() {
    this.buildingBlocks = this.allApplications;
    let value = this.qryString;
    if (this.qryString != null) {
      let application = [];
      this.buildingBlocks.forEach(element => {
        if ((element.name).toLowerCase().includes(value.toLowerCase())) {
          application.push(element);
        }
      });
      this.buildingBlocks = application;
    }
  }

  getmonth() {
    const d = new Date();
    const monthNames = ['December', 'January', 'February', 'March', 'April', 'May', 'June',
      'July', 'August', 'September', 'October', 'November', 'December'
    ];
    return monthNames[d.getMonth()] + ' Cost';
  }

  private loadDetailData() {
    this.ng4LoadingSpinnerService.show();
    this.getDetail()
      .subscribe(
        result => {
          const details = this.detailStrategy.parse(result);
          if (details.graphModel.score != null && details.graphModel.score != undefined) {
            var name = details.graphModel.score.name;
            var namesep = name.split(' ');

            if (namesep != undefined && namesep[1] != undefined && namesep[1].match('Cost')) {
              this.isStoriesOrCloud = true;
            }

            if (details.graphModel.score.name.match('Stories Completed')) {
              this.isStoriesOrCloud = true;
            }
            if (details.graphModel.score.name.match('Security Vulnerabilities') && this.isProduct) {
              this.showApplicationreporting = false;
              this.spinnerVisibility = 'hidden spinner center';
            }
            if (details.graphModel.score.name.match('Story Points Completed')) {
              this.isSayDoRatio = true;
            }
            if (details.graphModel.score.name.match('Test Cases')) {
              this.isTest = true;
            }
            if (details.graphModel.score.name.match('Production Events')) {
              this.isEvents = true;
            }
            if (details.graphModel.score.name.match(this.getmonth()) && this.isProduct) {
              this.showApplicationreporting = false;
              this.isCloud = true;
              this.spinnerVisibility = 'hidden spinner center';
            }
            if (details.graphModel.score.name.match('DevOps Cup')) {
              this.showApplicationreporting = false;
              this.isDevopscup = true;
            }


          }
          this.metricDetailView = Object.assign(details);
          this.headingModel = this.getHeader();
        },
        error => {
          console.log(error);
        }
      );
  }

  private loadBuildingBlocks() {
    this.ng4LoadingSpinnerService.show();
    this.getBuildingBlocks()
      .subscribe(
        result => {
          this.buildingBlocks = this.buildingBlockStrategy.parse(result);
          this.allApplications = this.buildingBlocks;
          this.headingModel = this.getHeader();
        },
        error => {
          console.log(error);
        }
      );
    this.ng4LoadingSpinnerService.hide();
  }

  private loadPortfolioData() {
    this.ng4LoadingSpinnerService.show();
    if (!this.metricOwnerSubTitle) {
      this.portfolioService.getPortfolio(this.portfolioId)
        .subscribe(
          result => {
            this.metricOwnerSubTitle = metricOwnerSubTitle(result);
            this.headingModel = this.getHeader();
            this.trackExecMetricsDetail(result.eid, this.headingModel.primaryText);
          },
          error => {
            console.log(error);
          }
        );
    }

    function metricOwnerSubTitle(portfolio: Portfolio) {
      if (!portfolio.name) {
        return `${possessive(`${portfolio.executive.firstName} ${portfolio.executive.lastName}`)} LOB`;
      }
      return `${portfolio.name} LOB`;

      function possessive(string) {
        return string[string.length - 1] === 's' ? `${string}'` : `${string}'s`;
      }
    }
  }

  private loadProductData() {
    this.ng4LoadingSpinnerService.show();
    if (this.productId) {
      this.productService.getPortfolioProduct(this.portfolioId, this.productId)
        .subscribe(
          result => {
            this.productName = result.name;
            this.headingModel = this.getHeader();
            this.trackAppMetricsDetail(result.metricLevelId, this.headingModel.primaryText);
          },
          error => console.log(error)
        );
    }
  }

  trackExecMetricsDetail(eid: string, metricName: string) {
    const eids: string[] = new Array();
    eids.push(eid);
    this.trackService.savePageTrackForExecMetric('Portfolio Metric View', this.authService.getAuthEid(), eids, metricName).subscribe(
      results => {
      },
      error => {
      }
    );
  }

  trackAppMetricsDetail(appId: string, metricName: string) {
    const appIds: string[] = new Array();
    appIds.push(appId);
    this.trackService.savePageTrackForAppMetric('Product Metric View', this.authService.getAuthEid(), appIds, metricName).subscribe(
      results => {
      },
      error => {
      }
    );
  }


  private getDetail() {
    if (this.productId) {
      this.isProduct = true;
      return this.dataService.getProductDetail(this.productId);
    }
    return this.dataService.getPortfolioDetail(this.portfolioId);
  }

  private getBuildingBlocks() {
    if (this.productId) {
      return this.dataService.getProductComponents(this.productId);
    }
    return this.dataService.getPortfolioProducts(this.portfolioId);
  }
  setSpinnerForApp(val: boolean) {
    if (val === false) {
      this.spinnerVisibility = 'hidden spinner center';
    }
  }
}

