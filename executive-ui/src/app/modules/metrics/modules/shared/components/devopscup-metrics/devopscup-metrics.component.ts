import { Component, OnInit, Input, OnChanges, Output, EventEmitter } from '@angular/core';
import { DevopscupScores } from '../../../../../shared/domain-models/devopscupScores';
import { saveAs } from 'file-saver/FileSaver';
import { Vast } from '../../../../../shared/domain-models/vast';
import { MetricTrendModel } from '../../component-models/metric-trend-model';
import { Ng4LoadingSpinnerModule, Ng4LoadingSpinnerComponent, Ng4LoadingSpinnerService } from 'ng4-loading-spinner';
import { PortfolioService } from '../../../../../shared/shared.module';
import { Params, ActivatedRoute, Router } from '@angular/router';
import { Portfolio } from '../../../../../shared/domain-models/portfolio';
import { utc } from 'moment';
import { DevopscupRoundDetails } from '../../../../../shared/domain-models/devopscupRoundDetails';

@Component({
  selector: 'app-devopscup-metrics',
  templateUrl: './devopscup-metrics.component.html',
  styleUrls: ['./devopscup-metrics.component.scss']
})
export class DevopscupMetricsComponent implements OnInit, OnChanges {
  @Input() public devopscupScoreList: DevopscupScores[] = null;
  @Input() public vastDetailsList: Map<string, Vast> = null;
  @Input() public showDevopscupFilter: boolean;
  @Input() public devopscupRoundDet: DevopscupRoundDetails = null;
  @Output() public showSpinnerApp = new EventEmitter<boolean>();

  public pageNumberEvent: number;
  public panelOpenState: Boolean = false;
  public sortType: String = 'rank';
  public sortReverse: Boolean = false;

  public allDevopscupScoreList: DevopscupScores[] = null
  public portfolioHeading: string;
  public portfolioId: string;
  public productHeading: string;
  public productId: string;
  public selectedApps = new Array<String>();
  public role: string;

  public closeableMetrics = false;
  public dropdownList = [];
  public selectedItems = [];
  public dropdownSettingsApplication = {};
  public dropdownListApplication = [];
  public selectedItemsApplication = [];
  public dropdownSettings = {};
  public bUnits = [];
  public selectedLob = '';
  applicationList = {};
  allApplications = [];
  public allApps = new Array<String>();
  public closeable = false;

  public buttonValue = new Array<String>();
  public cloudButtonValue = new Array<String>();
  public showGraph = new Array<boolean>();
  public showCloudDetails = new Array<boolean>()

  public metricsList = ['ct', 'dc', 'sec', 'cf', 'mttr', 'mtbf', 'tech'];
  constructor(private portfolioService: PortfolioService, private ng4LoadingSpinnerService: Ng4LoadingSpinnerService,
    private activatedRoute: ActivatedRoute) {
  }
  ngOnChanges() {
    this.pageNumberEvent = 1;
    this.sortType = 'rank';
    this.sortReverse = false;
    this.allDevopscupScoreList = this.devopscupScoreList;
    for (let i = 0; i < this.devopscupScoreList.length; i++) {
      this.showGraph.push(false);
      this.buttonValue.push('Details');

      this.showCloudDetails.push(false);
      this.cloudButtonValue.push('Details');
    }
    this.showSpinnerApp.emit(false);

  }

  ngOnInit() {

    this.ng4LoadingSpinnerService.show();
    this.activatedRoute.params.subscribe((params: Params) => {
      this.portfolioId = params['portfolio-id'];
      this.productId = params['product-id'];
      this.portfolioService.getPortfolio(this.portfolioId)
        .subscribe(
          result => {
            this.portfolioHeading = `${getPortfolioName(result)}'s Products`;
            this.role = result.executive.role;

          },
          error => {
            console.log(error);
          }
        );


    });


    this.dropdownListApplication = [];
    this.selectedItemsApplication = [];

    this.dropdownList = [{ 'id': 1, 'itemName': 'All Portfolios' }];
    this.selectedItems = [{ 'id': 1, 'itemName': 'All Portfolios' }];
    this.dropdownSettings = { singleSelection: true, text: 'Select Portfolio', enableSearchFilter: true, classes: 'myclass custom-class' };
    this.dropdownSettingsApplication = { singleSelection: false, text: 'Select Applications', enableSearchFilter: true, badgeShowLimit: 1 };

    this.ng4LoadingSpinnerService.show();
    this.loadBusinessUnitsForExecutive(this.portfolioId);
    this.loadApplicationsListForExecutive(this.portfolioId);



    function getPortfolioName(portfolio: Portfolio) {
      if (portfolio.name) {
        return portfolio.name;
      }
      return `${portfolio.executive.firstName} ${portfolio.executive.lastName}`;
    }
    this.showSpinnerApp.emit(false);
  }

  correctNull(obj: any): any {
    return (obj === undefined || obj === null || obj === '') ? '0' : obj;
  }
  replaceCommas(obj: string): string {
    return obj.replace(',', '-');
  }

  getVastDetailsByAppId(appId: string, custodian: string): string {
    let vastInfo: string = 'N/A';
    let vastDetail: Vast = this.vastDetailsList[appId];
    if (vastDetail !== undefined) {
      vastInfo = vastDetail[custodian];
    }

    return vastInfo;
  }
  protected trendDetails(trendSlope: number): MetricTrendModel {
    const metricTrend = new MetricTrendModel();
    metricTrend.direction = this.trendDirection(trendSlope);
    metricTrend.danger = trendSlope < 0;

    return metricTrend;
  }

  protected trendDirection(trendSlope: number) {
    if (trendSlope === 0 || trendSlope === 0.00) {
      return 'neutral';
    }
    return trendSlope < 0 ? 'down' : 'up';
  }
  downloadExcel() {
    let header = 'Rank,AppId,Application Name,Portfolio,';

    if (this.vastDetailsList !== null && this.vastDetailsList !== undefined) {
      header += 'Custodian,Custodian Email,Tier3 Executive,Tier4 Executive,Tier5 Executive,';
    }

    header += 'Cycle Time,Deployment Cadence,HP Fortify,Change Failure Rate %,' +
      'MTTR,MTBF,Tech Byte,Engineering Excellence Points,Engineering Excellence Improvements,Architecture Score,' +
      'Non Prod Migration Score,Stage Migration Score,Prod Migration Score,Presentation Score,' +
      'Cloud Excellence Points,Cloud Excellence Improvements,Total Points,Total Improvements \n';

    let content = '';
    if (this.devopscupScoreList !== undefined) {
      for (let i = 0; i < this.devopscupScoreList.length; i++) {
        let appId = this.devopscupScoreList[i].appId.toString();
        content = content + this.devopscupScoreList[i].rank + ',';
        content = content + this.devopscupScoreList[i].appId + ',';
        content = content + this.replaceCommas(this.devopscupScoreList[i].appName.toString()) + ',';
        content = content + this.devopscupScoreList[i].portfolio + ',';

        if (this.vastDetailsList !== null && this.vastDetailsList !== undefined) {
          content = content + this.getVastDetailsByAppId(appId, 'vastCustodianContactName') + ',';
          content = content + this.getVastDetailsByAppId(appId, 'vastCustodianContactEmail') + ',';
          content = content + this.getVastDetailsByAppId(appId, 'vastTierThreeContactName') + ' - ' + this.getVastDetailsByAppId(appId, 'vastTierThreeContactTitle') + ',';
          content = content + this.getVastDetailsByAppId(appId, 'vastTierFourContactName') + ' - ' + this.getVastDetailsByAppId(appId, 'vastTierFourContactTitle') + ',';
          content = content + this.getVastDetailsByAppId(appId, 'vastTierFiveContactName') + ' - ' + this.getVastDetailsByAppId(appId, 'vastTierFiveContactTitle') + ',';
        }

        content = content + this.correctNull(this.devopscupScoreList[i].enggExcel.cycleTimePoints) + ',';
        content = content + this.correctNull(this.devopscupScoreList[i].enggExcel.deploymentCadencyPoints) + ',';
        content = content + this.correctNull(this.devopscupScoreList[i].enggExcel.fortifyPoints) + ',';
        content = content + this.correctNull(this.devopscupScoreList[i].enggExcel.cfRatePoints) + ',';
        content = content + this.correctNull(this.devopscupScoreList[i].enggExcel.mttrPoints) + ',';
        content = content + this.correctNull(this.devopscupScoreList[i].enggExcel.mtbfPoints) + ',';
        content = content + this.correctNull(this.devopscupScoreList[i].enggExcel.techBytePoints) + ',';
        content = content + this.correctNull(this.devopscupScoreList[i].enggExcel.totalPoints) + ',';
        content = content + this.correctNull(this.devopscupScoreList[i].enggExcel.totalImprovements) + '%,';

        content = content + this.correctNull(this.devopscupScoreList[i].cloudExcel.architecturePoints) + ',';
        content = content + this.correctNull(this.devopscupScoreList[i].cloudExcel.nonProdMigrationPoints) + ',';
        content = content + this.correctNull(this.devopscupScoreList[i].cloudExcel.stageMigrationPoints) + ',';
        content = content + this.correctNull(this.devopscupScoreList[i].cloudExcel.prodMigrationPoints) + ',';
        content = content + this.correctNull(this.devopscupScoreList[i].cloudExcel.presentationPoints) + ',';
        content = content + this.correctNull(this.devopscupScoreList[i].cloudExcel.totalPoints) + ',';
        content = content + this.correctNull(this.devopscupScoreList[i].cloudExcel.totalImprovements) + '%,';

        content = content + this.correctNull(this.devopscupScoreList[i].totalPoints) + ',';
        content = content + this.correctNull(this.devopscupScoreList[i].totalPercent) + '%';
        content = content + '\n';
      }
    }

    const blob = new Blob([header + content], { type: 'text/csv;charset=utf-8' });
    saveAs(blob, 'DevOpsCupScores.csv');

  }

  sortDevopscup(value: string) {
    if (value !== undefined) {
      this.sortType = value;
      this.devopscupScoreList = this.sortByKey(this.devopscupScoreList, value, this.sortReverse);
      this.sortReverse = !this.sortReverse;

      for (let i = 0; i < this.devopscupScoreList.length; i++) {
        this.showGraph[i] = false;
        this.buttonValue[i] = 'Details';
        this.showCloudDetails[i] = false;
        this.cloudButtonValue[i] = 'Details';
      }
    }
  }

  sortByKey(devopscupScoreList, key, sortOrder) {
    if (devopscupScoreList.length !== undefined && devopscupScoreList.length > 0) {
      if (sortOrder) {

        if (key === 'rank') {
          this.devopscupScoreList.sort(function (element1, element2) {
            return element1.rank - element2.rank;
          });
        } else if (key == 'appName') {
          this.devopscupScoreList.sort(function (element1, element2) {
            if (element1.appName.trim().toLowerCase() > element2.appName.trim().toLowerCase())
              return 1;
            if (element1.appName.trim().toLowerCase() < element2.appName.trim().toLowerCase())
              return -1;

            return 0;
          });
        }
        else if (key == 'portfolio') {
          this.devopscupScoreList.sort(function (element1, element2) {
            if (element1.portfolio.trim().toLowerCase() > element2.portfolio.trim().toLowerCase())
              return 1;
            if (element1.portfolio.trim().toLowerCase() < element2.portfolio.trim().toLowerCase())
              return -1;

            return 0;
          });
        } else if (key === 'totalPoints') {
          this.devopscupScoreList.sort(function (element1, element2) {
            return element1.totalPoints - element2.totalPoints;
          });
        }
        else if (key === 'totalPercent') {
          this.devopscupScoreList.sort(function (element1, element2) {
            return element1.totalPercent - element2.totalPercent;
          });
        }


      } else {
        if (key === 'rank') {
          this.devopscupScoreList.sort(function (element1, element2) {
            return element2.rank - element1.rank;
          });
        } else if (key === 'appName') {
          this.devopscupScoreList.sort(function (element1, element2) {
            if (element1.appName.trim().toLowerCase() > element2.appName.trim().toLowerCase())
              return -1;
            if (element1.appName.trim().toLowerCase() < element2.appName.trim().toLowerCase())
              return 1;

            return 0;
          });
        }
        else if (key === 'portfolio') {
          this.devopscupScoreList.sort(function (element1, element2) {
            if (element2.portfolio.trim().toLowerCase() > element1.portfolio.trim().toLowerCase())
              return 1;
            if (element2.portfolio.trim().toLowerCase() < element1.portfolio.trim().toLowerCase())
              return -1;

            return 0;
          });
        } else if (key === 'totalPoints') {
          this.devopscupScoreList.sort(function (element1, element2) {
            return element2.totalPoints - element1.totalPoints;
          });
        }
        else if (key === 'totalPercent') {
          this.devopscupScoreList.sort(function (element1, element2) {
            return element2.totalPercent - element1.totalPercent;
          });
        }
      }
    }
    return devopscupScoreList;
  }


  OnItemDeSelect(item: any) {


    this.dropdownList = [{ 'id': 1, 'itemName': 'All Portfolios' }];
    this.selectedItems = [{ 'id': 1, 'itemName': 'All Portfolios' }];
    this.dropdownListApplication = [];
    this.selectedItemsApplication = [];
    this.ng4LoadingSpinnerService.show();
    this.loadBusinessUnitsForExecutive(this.portfolioId);
    this.loadApplicationsListForExecutive(this.portfolioId);
  }

  public loadBusinessUnitsForExecutive(id: string) {
    this.portfolioService.getBusinessUnitForExecutive(id)
      .subscribe(
        result => {
          this.bUnits = result;
          let count = 1;
          for (const entry of this.bUnits) {
            count++;
            const businessUnit = { 'id': count, 'itemName': entry };
            this.dropdownList.push(businessUnit);
          }
          if (this.dropdownList.length > 0) {
            this.dropdownList.sort(function (a, b) {
              const nameA = a.itemName.toLowerCase();
              const nameB = b.itemName.toLowerCase();
              if (nameA < nameB) {
                return -1;
              }
              if (nameA > nameB) {
                return 1;
              }
              return 0;
            });
          }
          this.selectedLob = 'All Portfolios';
        },
        error => { console.log(error); }
      );
  }
  loadApplicationsListForExecutive(id: string) {
    this.dropdownListApplication = [];
    this.selectedItemsApplication = [];
    this.selectedApps = [];

    this.portfolioService.getApplicationListAllForExec(id)
      .subscribe(
        result => {
          this.applicationList = result;
          let selected = [];
          for (const key in this.applicationList) {
            this.dropdownListApplication.push({ 'id': key, 'itemName': this.applicationList[key] });
            selected.push({ 'id': key, 'itemName': this.applicationList[key] });
            this.allApps.push(key);
          }
          this.selectedItemsApplication = selected;
          this.selectedApps = this.allApps;


          if (this.dropdownListApplication.length > 0) {
            this.dropdownListApplication.sort(function (a, b) {
              const nameA = a.itemName.toLowerCase();
              const nameB = b.itemName.toLowerCase();
              if (nameA < nameB) {
                return -1;
              }
              if (nameA > nameB) {
                return 1;
              }
              return 0;
            });
          }
          this.ng4LoadingSpinnerService.hide();
        },
        error => { console.log(error); }
      );
  }
  onItemSelect(item: any) {

    this.selectedLob = item.itemName;
    this.dropdownListApplication = [];
    this.selectedItemsApplication = [];
    this.selectedApps = [];
    this.applicationList = [];

    this.devopscupScoreList = [];
    if (item.itemName === 'All Portfolios') {
      this.loadApplicationsListForExecutive(this.portfolioId);
      this.devopscupScoreList = this.allDevopscupScoreList;
    } else {
      this.portfolioService.getApplicationListAllForExecWithBunit(this.selectedLob, this.portfolioId)
        .subscribe(
          result => {
            this.applicationList = result;
            let selected = [];
            for (const key in this.applicationList) {
              this.dropdownListApplication.push({ 'id': key, 'itemName': this.applicationList[key] });
              selected.push({ 'id': key, 'itemName': this.applicationList[key] });
              this.selectedApps.push(key);
            }
            this.selectedItemsApplication = selected;
            if (this.allDevopscupScoreList != undefined) {
              for (let i = 0; i < this.allDevopscupScoreList.length; i++) {
                if (this.selectedApps.includes(this.allDevopscupScoreList[i].appId)) {
                  this.devopscupScoreList.push(this.allDevopscupScoreList[i]);
                }
              }
            }


            this.ng4LoadingSpinnerService.hide();
            if (this.dropdownListApplication.length > 0) {
              this.dropdownListApplication.sort(function (a, b) {
                const nameA = a.itemName.toLowerCase();
                const nameB = b.itemName.toLowerCase();
                if (nameA < nameB) {
                  return -1;
                }
                if (nameA > nameB) {
                  return 1;
                }
                return 0;
              });
            }
          },
          error => { console.log(error); }
        );
    }
  }
  onCloseApplication(items: any) {
    const appIds = [];
    if (this.closeable) {

      this.devopscupScoreList = [];
      this.selectedItemsApplication.forEach(function (arrayItem) {
        appIds.push(arrayItem.id);
      });
      this.selectedApps = appIds;
      if (this.allDevopscupScoreList != undefined) {
        for (let i = 0; i < this.allDevopscupScoreList.length; i++) {
          if (this.selectedApps.includes(this.allDevopscupScoreList[i].appId)) {
            this.devopscupScoreList.push(this.allDevopscupScoreList[i]);
          }
        }
      }

      if (this.selectedItemsApplication.length <= 0) {
        this.dropdownList = [{ 'id': 1, 'itemName': 'All Portfolios' }];
        this.selectedItems = [{ 'id': 1, 'itemName': 'All Portfolios' }];
        this.dropdownListApplication = [];
        this.selectedItemsApplication = [];
        this.loadApplicationsListForExecutive(this.portfolioId);
        this.loadBusinessUnitsForExecutive(this.portfolioId);

      }
    }
    this.closeable = false;

  }
  onItemSelectApplication(items: any) {
    this.closeable = true;

  }


  OnItemDeSelectApplication(item: any) {
    this.closeable = true;
  }

  onSelectAllApplication(items: any) {
    this.closeable = true;
  }

  onDeSelectAllApplication(items: any) {
    this.closeable = true;
  }

  changeStatus(index) {
    if (this.showGraph[index]) {
      this.showGraph[index] = false;
      this.buttonValue[index] = 'Details';
    } else {
      this.showGraph[index] = true;
      this.buttonValue[index] = 'Hide';
    }
  }
  changeCloudStatus(index) {
    if (this.showCloudDetails[index]) {
      this.showCloudDetails[index] = false;
      this.cloudButtonValue[index] = 'Details';
    }
    else {
      this.showCloudDetails[index] = true;
      this.cloudButtonValue[index] = 'Hide'
    }
  }
  goToFeedback() {
    window.open('https://goo.gl/forms/OiHMFrTQTwJ2LQ082', '_blank');
  }
}
