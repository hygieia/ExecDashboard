import { Component, OnInit, Input, OnChanges, Output, EventEmitter, ChangeDetectionStrategy } from '@angular/core';
import { BuildingBlockModel } from '../../component-models/building-block-model';
import { ProductService } from '../../services/product.service';
import { saveAs } from 'file-saver/FileSaver';

@Component({
  selector: 'app-building-blocks',
  templateUrl: './building-blocks.component.html',
  styleUrls: ['./building-blocks.component.scss'], changeDetection: ChangeDetectionStrategy.OnPush
})
export class BuildingBlocksComponent implements OnInit, OnChanges {

  @Input() public buildingBlocks: BuildingBlockModel[];
  @Input() public isComponentList: boolean;
  @Input() public portfolioId: string;
  @Input() public productView: boolean;
  @Input() public cardsList: string[];
  @Input() public metric: string;
  @Output() public showSpinnerApp = new EventEmitter<boolean>();
  public spinnerVisibility: string = 'visible spinner center';

  public reportingBuildingBlocks: number;
  public totalBuildingBlocks: number;
  public totalBuildingBlocksCount: number;
  public icon: string;
  public check: boolean;
  public sortIcon: string;
  public productService: ProductService;
  public buildingBlocksFiltered: BuildingBlockModel[] = new Array();
  public buildingBlocksCopy: BuildingBlockModel[] = new Array();
  public buildingBlocksCopyOnScroll: BuildingBlockModel[] = new Array();

  public buildingBlocksMttrInM: BuildingBlockModel[] = new Array();
  public buildingBlocksMttrInHr: BuildingBlockModel[] = new Array();
  public data: Object;
  public checkSelection: string;
  public checkSelectionf: string;

  /*true for asc*/
  public sortOrderForAppName: boolean;
  public sortOrder: boolean;
  public sortOrderForMttr: boolean;
  public sortOrderForBuiildAvgTime: boolean;
  public sortOrderForDeployAvgTime: boolean;
  public sortOrderForStoriesRatio: boolean;
  public sortOrderForCycleTime: boolean;
  public sortOrderForCadence: boolean;
  public sortOrderForDevopscup: boolean;
  public filterName: string;

  /*Icons for Sort Dropdown*/
  public appNameIcon: string;
  public outagesIcon: string;
  public mttrIcon: string;
  public velocityIcon: string;
  public cycleTimeIcon: string;
  public cadenceIcon: string;
  public securityIcon: string;
  public criticalIcon: string;
  public majorIcon: string;
  public blockerIcon: string;
  public qualityIcon: string;
  public wipIcon: string;
  public stashIcon: string;
  public cloudIcon: string;
  public totalValueIcon: string;
  public buildIcon: string;
  public deployIcon: string;
  public saydoratioIcon: string;
  public testIcon: string;
  public buildavgtimeIcon: string;
  public deployavgtimeIcon: string;
  public saydoratiostoriesIcon: string;
  public devopscupIcon: string;
  public isOutages: boolean;
  public isVelocity: boolean;
  public isCycleTime: boolean;
  public isSecurity: boolean;
  public isQuality: boolean;
  public isWip: boolean;
  public isCloud: boolean;
  public isStash: boolean;
  public isBuild: boolean;
  public isDeploy: boolean;
  public isTotalValue: boolean;
  public isModule: boolean;
  public showModules: boolean;
  public showMttr: boolean;
  public showAvgTimeBuild: boolean;
  public showAvgTimeDeploy: boolean;
  public showStoriesRatio: boolean;

  public showCadence: boolean;
  public component: Boolean = false;
  public configAppLength: number;
  public pageNumber: number;
  public allbuildingBlocks: BuildingBlockModel[];
  private metricToBuildingBlocksMap = [];
  public sortType: String = 'Velocity';
  public sortReverse: Boolean = true;
  public dataAvailable: Boolean = false;
  public isSayDo: Boolean = false;
  public isDevopscup: Boolean = false;
  public isTest: Boolean = false;

  /*allappscheck*/
  public allapps: Boolean = false;

  constructor(productService: ProductService) {
    this.productService = productService;
    this.sortIcon = 'up-arrow';
    this.appNameIcon = 'up-arrow';
    this.outagesIcon = 'up-arrow';
    this.mttrIcon = 'up-arrow';
    this.buildavgtimeIcon = 'up-arrow';
    this.deployavgtimeIcon = 'up-arrow';
    this.saydoratiostoriesIcon = 'up-arrow';
    this.velocityIcon = 'up-arrow';
    this.cycleTimeIcon = 'up-arrow';
    this.cadenceIcon = 'up-arrow';
    this.criticalIcon = 'up-arrow';
    this.majorIcon = 'up-arrow';
    this.blockerIcon = 'up-arrow';
    this.securityIcon = 'up-arrow';
    this.qualityIcon = 'up-arrow';
    this.wipIcon = 'up-arrow';
    this.filterName = 'All';
    this.totalValueIcon = 'up-arrow';
    this.stashIcon = 'up-arrow';
    this.buildIcon = 'up-arrow';
    this.deployIcon = 'up-arrow';
    this.saydoratioIcon = 'up-arrow';
    this.testIcon = 'up-arrow';
    this.cloudIcon = 'up-arrow';
    this.devopscupIcon = 'up-arrow';

    this.check = true;
    this.sortOrderForAppName = true;
    this.sortOrder = true;
    this.sortOrderForMttr = true;
    this.sortOrderForBuiildAvgTime = true;
    this.sortOrderForDevopscup = true;
    this.isOutages = false;
    this.isVelocity = false;
    this.isCycleTime = false;
    this.isSecurity = false;
    this.isQuality = false;
    this.isWip = false;
    this.isStash = false;
    this.isCloud = false;
    this.isBuild = false;
    this.isDeploy = false;
    this.isTest = false;

    this.isModule = true;
    this.showModules = true;
    this.sortOrderForCycleTime = false;
    this.sortOrderForCadence = false;
    this.showMttr = false;
    this.showAvgTimeBuild = false;
    this.showAvgTimeDeploy = false;
    this.showStoriesRatio = false;
    this.showCadence = false;
    this.isTotalValue = false;
    this.isSayDo = false;
    this.isDevopscup = false;
    this.spinnerVisibility = 'visible spinner center';
  }

  ngOnInit() {
    this.icon = this.isComponentList ? 'components' : 'box';
    this.component = this.isComponentList;
    this.pageNumber = 1;
  }

  onScroll() {
    if (this.buildingBlocksCopy.length > 0 && this.buildingBlocksCopy.length > 10) {
      this.buildingBlocksCopyOnScroll = this.buildingBlocksCopy.slice(0, this.buildingBlocksCopyOnScroll.length + 10);
    } else {
      this.buildingBlocksCopyOnScroll = this.buildingBlocksCopy;
    }
  }

  setCards(value: string) {
    if (this.cardsList !== undefined && this.cardsList.includes(value)) {
      return true;
    }
  }

  ngOnChanges() {
    if (this.buildingBlocks && this.buildingBlocks[0] !== undefined) {
      this.totalBuildingBlocksCount = this.buildingBlocks.length;
      this.showModules = true;
      this.buildingBlocksCopy = this.buildingBlocks;
      if (this.metric !== undefined) {
        this.buildingBlocksCopy = this.buildingBlocks.filter(buildingBlock => buildingBlock.metricType === this.metric);
      }
      this.buildingBlocksFiltered = this.buildingBlocksCopy;
      if (this.buildingBlocksCopy[0] !== undefined && this.buildingBlocksCopy[0].metrics !== undefined
        && this.buildingBlocksCopy[0].metrics[0] !== undefined) {
        this.isModule = false;
        if (this.buildingBlocksCopy[0].metrics[0].value.name === 'Production Events') {
          this.isOutages = true;
          this.showModules = true;
          this.sortForMetrics('outages');
          this.sortForMetrics('outages');
          if (this.buildingBlocksCopy[0].metrics[1] !== undefined) {
            this.showMttr = true;
          }
        }

        if (this.buildingBlocksCopy[0].metrics[0].value.name === 'Total Builds') {
          this.showModules = true;
          this.isBuild = true;
          if (this.component) {
            this.filteringZeroNonZero();
          }
          if (this.buildingBlocksCopy[0].metrics[1] !== undefined) {
            this.showAvgTimeBuild = true;
          }
        }



        if (this.buildingBlocksCopy[0].metrics[0].value.name === 'Test Cases') {
          this.showModules = true;
          this.isTest = true;
          if (this.component) {
            this.filteringZeroNonZero();
          }
        }

        if (this.buildingBlocksCopy[0].metrics[0].value.name === 'Story Points Completed') {
          this.showModules = true;
          this.isSayDo = true;
          if (this.component) {
            this.filteringZeroNonZero();
            const updatedArray: BuildingBlockModel[] = new Array();
            for (const record of this.buildingBlocksCopy) {
              record.name = record.metrics[2] !== undefined ? record.metrics[2].value.name : '';
              if (record.name !== '') {
                updatedArray.push(record);
              }
            }
            this.buildingBlocksCopy = updatedArray;
          }

          if (this.buildingBlocksCopy[0].metrics[1] !== undefined) {
            this.showStoriesRatio = true;
          }

        }

        if (this.buildingBlocksCopy[0].metrics[0].value.name === 'Total Deploys') {
          this.showModules = true;
          this.isDeploy = true;
          if (this.component) {
            this.filteringZeroNonZero();
          }
          if (this.buildingBlocksCopy[0].metrics[1] !== undefined) {
            this.showAvgTimeDeploy = true;
          }
        }

        if (this.buildingBlocksCopy[0].metrics[0].value.name === 'Velocity as Days per Story Point') {
          this.isVelocity = true;
          this.sortForMetrics('velocity');
          this.sortForMetrics('velocity');
          if (this.component) {
            this.filteringZeroNonZero();
          }
        }
        if (this.buildingBlocksCopy[0].metrics[0].value.name === 'DevOps Cup Improvements') {
          this.isDevopscup = true;
          this.sortForMetrics('devopscup');
          this.sortForMetrics('devopscup');

        }

        if (this.buildingBlocksCopy[0].metrics[0].value.name === 'Story Points Completed') {
          this.isSayDo = true;
          this.sortForMetrics('saydoratio');
          this.sortForMetrics('saydoratio');

        }
        if (this.buildingBlocksCopy[0].metrics[0].value.name === 'Cycle Time') {
          this.isCycleTime = true;
          this.sortByTimeZoneUnits('cycleTime');
          this.sortByTimeZoneUnits('cycleTime');
          if (this.buildingBlocksCopy[0].metrics[1] !== undefined) {
            this.showCadence = true;
          }
          if (this.component) {
            this.filteringZeroNonZero();
          }
        }

        if (this.buildingBlocksCopy[0].metrics[0].value.name === 'Security Vulnerabilities') {
          this.isSecurity = true;
          this.showModules = false;
          this.sortForMetrics('security');
          this.sortForMetrics('security');
          this.trendFilter();
        }
        if (this.buildingBlocksCopy[0].metrics[0].value.name === 'Defects') {
          this.isQuality = true;
          this.sortForMetrics('quality');
          this.sortForMetrics('quality');
          if (this.component) {
            this.filteringZeroNonZero();
          }
        }

        if (this.buildingBlocksCopy[0].metrics[0].value.name === 'Code Commits Per Day') {
          this.isStash = true;
          this.sortForMetrics('stash');
          this.sortForMetrics('stash');
          if (this.component) {
            this.filteringZeroNonZero();
          }
        }

        if (this.buildingBlocksCopy[0].metrics[0].value.name === 'Work In Progress') {
          this.isWip = true;
          this.sortForMetrics('wip');
          this.sortForMetrics('wip');
          if (this.component) {
            this.filteringZeroNonZero();
          }
        }

        if (this.buildingBlocksCopy[0].metrics[0].value.name === this.getCloudName()) {
          this.isCloud = true;
          this.sortForMetrics('cloud');
          this.sortForMetrics('cloud');
          if (this.component) {
            this.filteringZeroNonZero();
          }
        }

        if (this.buildingBlocksCopy[0].metrics[0].value.name === 'Stories Completed') {
          this.isTotalValue = true;
          this.sortForMetrics('totalValue');
          this.sortForMetrics('totalValue');
          if (this.component) {
            this.filteringZeroNonZero();
          }
        }
      }
      if (!this.isComponentList && this.buildingBlocksCopy[0] !== undefined && this.buildingBlocksCopy[0].metrics[0] !== undefined) {
        if (this.buildingBlocksCopy[0].metrics[0].value.name === 'Work In Progress' || this.buildingBlocksCopy[0].metrics[0].value.name === 'Velocity as Days per Story Point'
          || this.buildingBlocksCopy[0].metrics[0].value.name === 'Stories Completed' || this.buildingBlocksCopy[0].metrics[0].value.name === 'Total Deploys'
          || this.buildingBlocksCopy[0].metrics[0].value.name === 'Total Builds') {
          const updatedArray: BuildingBlockModel[] = new Array();
          for (const record of this.buildingBlocksCopy) {
            if (record.metrics[0] !== undefined && record.metrics[0].message === 'Not Configured') {
              updatedArray.push(record);
            }
          }
          this.reportingBuildingBlocks = this.buildingBlocksCopy.length - updatedArray.length;
        } else if (this.buildingBlocksCopy[0].metrics[0].value.name === 'Test Cases') {
          const updatedArray: BuildingBlockModel[] = new Array();
          for (const record of this.buildingBlocksCopy) {
            if (record.metrics[0] !== undefined && record.metrics[0].message === undefined && record.metrics[0].message !== 'No Tests Executed'
              && record.metrics[0].value.value > 0) {
              updatedArray.push(record);
            }
          }
          this.reportingBuildingBlocks = updatedArray.length;
        } else if (this.buildingBlocksCopy[0].metrics[0].value.name === 'Code Commits Per Day' || this.buildingBlocksCopy[0].metrics[0].value.name === 'DevOps Cup Improvements') {
          const updatedArray: BuildingBlockModel[] = new Array();
          for (const record of this.buildingBlocksCopy) {
            if (record.metrics[0] !== undefined && record.metrics[0].available) {
              updatedArray.push(record);
            }
          }
          this.reportingBuildingBlocks = updatedArray.length;
        } else if (this.buildingBlocksCopy[0].metrics[0].value.name === 'Defects') {
          const updatedArray: BuildingBlockModel[] = new Array();
          for (const record of this.buildingBlocksCopy) {
            if (record.metrics[0] !== undefined && record.metrics[0].message === 'No Jira/CMIS/SN Defects') {
              updatedArray.push(record);
            }
          }
          this.reportingBuildingBlocks = this.buildingBlocksCopy.length - updatedArray.length;
        } else {
          if (this.buildingBlocksCopy[0].metrics[0].value.name != 'Security Vulnerabilities')
            this.reportingBuildingBlocks = this.buildingBlocksCopy.filter(this.reportingProductsFilter).length;
        }
      }

      this.totalBuildingBlocks = this.buildingBlocksCopy.length;

      if (this.buildingBlocksCopy[0] !== undefined && this.buildingBlocksCopy[0].metrics[0] !== undefined) {
        if (this.buildingBlocksCopy[0].metrics[0].value.name === 'Cycle Time'
          && !this.isComponentList) {
          const updatedArray: BuildingBlockModel[] = new Array();
          for (const record of this.buildingBlocksCopy) {
            if (record.metrics[0] !== undefined && record.metrics[0].available) {
              updatedArray.push(record);
            }
          }
          this.reportingBuildingBlocks = updatedArray.length;
        }

        if (this.buildingBlocksCopy[0].metrics[0].value.name === this.getCloudName()) {
          const updatedArray: BuildingBlockModel[] = new Array();
          for (const record of this.buildingBlocksCopy) {
            if (record.metrics[0] !== undefined && record.metrics[0].available) {
              updatedArray.push(record);
            }
          }
          this.reportingBuildingBlocks = updatedArray.length;
          this.buildingBlocksCopy = updatedArray;
        }

        if (this.buildingBlocksCopy[0] !== undefined && this.buildingBlocksCopy[0].metrics[0] !== undefined && this.buildingBlocksCopy[0].metrics[0].value.name === 'Story Points Completed') {
          const updatedArray: BuildingBlockModel[] = new Array();
          for (const record of this.buildingBlocksCopy) {
            if (record.metrics.length > 0 && record.metrics[0].message === undefined) {
              updatedArray.push(record);
            }
          }
          this.reportingBuildingBlocks = updatedArray.length;
        }
      }
      const appIdList: string[] = new Array();
      for (const item of this.buildingBlocksCopy) {
        appIdList.push(item.name);
      }
      this.onScroll();

    } else {
      this.onScroll();
    }
    const property = document.getElementById('All');
    if (property != null) {
      property.style.backgroundColor = '#66b3af';
    }

  }

  getCloudName() {
    const d = new Date();
    const monthNames = ['December', 'January', 'February', 'March', 'April', 'May', 'June',
      'July', 'August', 'September', 'October', 'November', 'December'
    ];
    return monthNames[d.getMonth()] + ' Cost';
  }

  downloadExcel() {
    const header = 'Application Name, Portfolio, POC, Updated Time, Velocity, Cycle Time, Security Vulnerabilities, Production Events,'
      + ' Quality, Work In Progress, Stories Completed, Builds, Deploys, Cloud Cost, Code Commits per day, Say Do, Test Metrics, DevOps Cup\n ';
    let content = '';
    if (this.buildingBlocks !== undefined) {
      for (let i = 0; i < this.buildingBlocks.length; i++) {
        content = content + this.buildingBlocks[i].name + ',';
        content = content + this.buildingBlocks[i].lob + ',';
        content = content + this.buildingBlocks[i].poc + ',';
        content = content + this.buildingBlocks[i].lastScanned + ',';
        for (let j = 0; j < this.buildingBlocks[i].metrics.length; j++) {
          content = content + this.buildingBlocks[i].metrics[j].value.value;
          if (this.buildingBlocks[i].metrics[j].value.unit !== undefined) {
            content = content + ' ' + this.buildingBlocks[i].metrics[j].value.unit;
          }
          content = content + ',';
        }
        content = content + '\n';
      }
    }

    const blob = new Blob([header + content], { type: 'text/csv;charset=utf-8' });
    saveAs(blob, 'ApplicationMetricsReport.csv');
  }

  sort(value: String) {
    if (value !== undefined) {
      this.sortType = value;
      if (value === 'cloudCost') {
        value = this.getCloudName();
        this.sortType = 'cloudCost';
      }
      let temp: BuildingBlockModel[];
      this.metricToBuildingBlocksMap = [];
      this.allbuildingBlocks = this.buildingBlocks;
      if (this.buildingBlocks !== undefined) {
        for (let i = 0; i < this.allbuildingBlocks.length; i++) {
          for (let j = 0; j < this.allbuildingBlocks[i].metrics.length; j++) {
            if (this.allbuildingBlocks[i].metrics[j].value.name === value) {
              this.metricToBuildingBlocksMap.push({ model: this.allbuildingBlocks[i], value: this.allbuildingBlocks[i].metrics[j].value.value });
            }
          }
        }
      }
      if (this.metricToBuildingBlocksMap.length > 0) {
        temp = new Array();
        if (this.sortReverse) {
          this.metricToBuildingBlocksMap.sort(function (a, b) {
            return a.value - b.value;
          });

          this.metricToBuildingBlocksMap.forEach(function (element) {
            temp.push(element.model);
          });
          this.buildingBlocks = temp;

        } else {
          temp = new Array();
          this.metricToBuildingBlocksMap.sort(function (a, b) {
            return b.value - a.value;
          });

          this.metricToBuildingBlocksMap.forEach(function (element) {
            temp.push(element.model);
          });

          this.buildingBlocks = temp;
        }
      }
      this.sortReverse = !this.sortReverse;
    }
  }

  setReporting(result: number) {
    this.configAppLength = result;
    this.reportingBuildingBlocks = this.configAppLength;
  }

  trendFilter() {
    let tempBuilding: BuildingBlockModel[] = new Array();
    const tempBuildingPositive: BuildingBlockModel[] = new Array();
    const tempBuildingNegative: BuildingBlockModel[] = new Array();
    const tempBuildingNeutral: BuildingBlockModel[] = new Array();
    for (let i = 0; i < (this.buildingBlocksCopy.length); i++) {
      for (let j = 0; j < (this.buildingBlocksCopy[i].metrics.length); j++) {
        if ('up' === this.buildingBlocksCopy[i].metrics[j].trend.direction) {
          tempBuildingNegative.push(this.buildingBlocksCopy[i]);
        }
        if ('down' === this.buildingBlocksCopy[i].metrics[j].trend.direction) {
          tempBuildingPositive.push(this.buildingBlocksCopy[i]);
        }
        if ('neutral' === this.buildingBlocksCopy[i].metrics[j].trend.direction) {
          tempBuildingNeutral.push(this.buildingBlocksCopy[i]);
        }
      }
    }
    if (tempBuildingNegative.length > 0) {
      this.filterName = 'Negative Trend';
      tempBuilding = tempBuildingNegative;
    } else if (tempBuildingPositive.length > 0) {
      this.filterName = 'Positive Trend';
      tempBuilding = tempBuildingPositive;
    } else if (tempBuildingNeutral.length > 0) {
      this.filterName = 'Neutral Trend';
      tempBuilding = tempBuildingNeutral;
    } else {
      this.filterName = 'All Applications';
      tempBuilding = this.buildingBlocksCopy;
    }

    this.reportingBuildingBlocks = tempBuilding.length;
    this.onScroll();
  }

  filteringZeroNonZero() {
    let tempBuilding: BuildingBlockModel[] = new Array();
    const tempBuildingNonZero: BuildingBlockModel[] = new Array();
    const tempBuildingZero: BuildingBlockModel[] = new Array();
    for (let i = 0; i < (this.buildingBlocksCopy.length); i++) {
      if (this.buildingBlocksCopy[i].metrics[0].value.value !== 0) {
        tempBuildingNonZero.push(this.buildingBlocksCopy[i]);
      }
      if (this.buildingBlocksCopy[i].metrics[0].value.value === 0) {
        tempBuildingZero.push(this.buildingBlocksCopy[i]);
      }
    }
    if (tempBuildingNonZero.length > 0) {
      this.filterName = 'Non-Zero Values';
      tempBuilding = tempBuildingNonZero;
      this.reportingBuildingBlocks = tempBuilding.length;
    } else if (tempBuildingZero.length > 0) {
      this.filterName = 'Zero Values';
      tempBuilding = tempBuildingZero;
      this.reportingBuildingBlocks = tempBuilding.length;
    } else {
      this.filterName = 'All';
      tempBuilding = this.buildingBlocksCopy;
      this.reportingBuildingBlocks = tempBuilding.length;
    }
    this.buildingBlocksCopy = new Array();
    this.buildingBlocksCopy = tempBuilding;
    this.totalBuildingBlocks = this.buildingBlocks.length;
    this.onScroll();
  }

  setFilterData(result: Object) {
    this.data = result;
  }

  reportingProductsFilter(buildingBlock: BuildingBlockModel) {
    return buildingBlock.metrics.length;
  }

  changeColor(btn) {
    this.resetColor();
    const property = document.getElementById(btn);
    property.style.backgroundColor = '#66b3af';
  }

  resetColor() {
    let property = document.getElementById('All');
    property.style.backgroundColor = '#021829';
    property.style.opacity = '1';
    property = document.getElementById('missionc');
    property.style.backgroundColor = '#021829';
    property.style.opacity = '1';
    property = document.getElementById('businessc');
    property.style.backgroundColor = '#021829';
    property.style.opacity = '1';
    property = document.getElementById('systemc');
    property.style.backgroundColor = '#021829';
    property.style.opacity = '1';
    property = document.getElementById('nonc');
    property.style.backgroundColor = '#021829';
    property.style.opacity = '1';
  }

  getReportingCount(tempBuilding: BuildingBlockModel[]): number {

    if (tempBuilding[0].metrics[0].value.name === 'Work In Progress' || tempBuilding[0].metrics[0].value.name === 'Velocity as Days per Story Point'
      || tempBuilding[0].metrics[0].value.name === 'Stories Completed' || tempBuilding[0].metrics[0].value.name === 'Total Deploys'
      || tempBuilding[0].metrics[0].value.name === 'Total Builds' || tempBuilding[0].metrics[0].value.name === 'DevOps Cup Improvements') {
      const updatedArray: BuildingBlockModel[] = new Array();
      for (const record of tempBuilding) {
        if (record.metrics[0] !== undefined && record.metrics[0].message === 'Not Configured') {
          updatedArray.push(record);
        }
      }
      return (tempBuilding.length - updatedArray.length);
    } else if (tempBuilding[0].metrics[0].value.name === 'Story Points Completed') {
      const updatedArray: BuildingBlockModel[] = new Array();
      for (const record of tempBuilding) {
        if (record.metrics[0] !== undefined && record.metrics[0].message === 'No Sprints') {
          updatedArray.push(record);
        }
      }
      return (tempBuilding.length - updatedArray.length);
    } else if (tempBuilding[0].metrics[0].value.name === 'Test Cases') {
      const updatedArray: BuildingBlockModel[] = new Array();
      for (const record of tempBuilding) {
        if (record.metrics[0] !== undefined && record.metrics[0].message === undefined
          && record.metrics[0].message !== 'No Tests Executed' && record.metrics[0].value.value > 0) {
          updatedArray.push(record);
        }
      }
      return (updatedArray.length);
    } else if (tempBuilding[0].metrics[0].value.name === 'Code Commits Per Day' || tempBuilding[0].metrics[0].value.name === 'Cycle Time'
      || tempBuilding[0].metrics[0].value.name === this.getCloudName() || tempBuilding[0].metrics[0].value.name === 'DevOps Cup Improvements') {
      const updatedArray: BuildingBlockModel[] = new Array();
      for (const record of tempBuilding) {
        if (record.metrics[0] !== undefined && record.metrics[0].available) {
          updatedArray.push(record);
        }
      }
      return updatedArray.length;
    } else if (tempBuilding[0].metrics[0].value.name === 'Defects') {
      const updatedArray: BuildingBlockModel[] = new Array();
      for (const record of tempBuilding) {
        if (record.metrics[0] !== undefined && record.metrics[0].message === 'No Jira/CMIS/SN Defects') {
          updatedArray.push(record);
        }
      }
      return (tempBuilding.length - updatedArray.length);
    } else {
      return (tempBuilding.filter(this.reportingProductsFilter).length);
    }
  }

  filterFunNew(type) {
    switch (type) {
      case 'missionc': this.filterName = 'Mission Critical';
        break;
      case 'businessc': this.filterName = 'Business Critical';
        break;
      case 'nonc': this.filterName = 'Non-Critical';
        break;
      case 'systemc': this.filterName = 'System Critical';
        break;
      case 'All': this.filterName = 'All';
        break;
      default: this.filterName = 'Filter';
        break;
    }
    if (this.checkSelectionf !== type && type !== 'All') {
      const tempBuilding: BuildingBlockModel[] = new Array();
      if (this.buildingBlocks !== undefined) {
        this.sortOrderForAppName = true;
        for (let i = 0; i < (this.buildingBlocks.length); i++) {
          if (this.filterName === this.buildingBlocks[i].appCriticality) {
            tempBuilding.push(this.buildingBlocks[i]);
          }
        }
        if (tempBuilding != null && tempBuilding.length !== 0) {
          this.reportingBuildingBlocks = this.getReportingCount(tempBuilding);
        } else {
          this.reportingBuildingBlocks = 0;
        }
      }
      this.buildingBlocksCopy = tempBuilding;
      this.totalBuildingBlocks = this.buildingBlocksCopy.length;
      this.sortForAppName();
      this.checkSelectionf = type;
    } else {
      this.buildingBlocksCopy = this.buildingBlocks;
      this.sortOrderForAppName = true;
      this.sortForAppName();
      this.reportingBuildingBlocks = this.getReportingCount(this.buildingBlocks);
      this.totalBuildingBlocks = this.buildingBlocks.length;
      this.checkSelectionf = 'none';
    }
    this.onScroll();
  }

  secFilterFun(type) {
    switch (type) {
      case 'up': this.filterName = 'Negative Trend';
        break;
      case 'down': this.filterName = 'Positive Trend';
        break;
      case 'neutral': this.filterName = 'Neutral';
        break;
      case 'all': this.filterName = 'All Applications';
        break;
      default: this.filterName = 'Filter';
        break;
    }
    if (this.checkSelection !== type && type !== 'all') {
      const tempBuilding: BuildingBlockModel[] = new Array();
      if (this.buildingBlocks !== undefined) {
        this.sortOrderForAppName = true;
        for (let i = 0; i < (this.buildingBlocks.length); i++) {
          for (let j = 0; j < (this.buildingBlocks[i].metrics.length); j++) {
            if (type === this.buildingBlocks[i].metrics[j].trend.direction) {
              tempBuilding.push(this.buildingBlocks[i]);
            }
          }
        }
      }
      this.buildingBlocksCopy = tempBuilding;
      this.reportingBuildingBlocks = this.buildingBlocksCopy.length;
      this.totalBuildingBlocks = this.buildingBlocks.length;
      this.sortForAppName();
      this.checkSelection = type;
    } else {
      this.buildingBlocksCopy = this.buildingBlocks;
      this.sortOrderForAppName = true;
      this.sortForAppName();
      this.reportingBuildingBlocks = this.buildingBlocksCopy.length;
      this.totalBuildingBlocks = this.buildingBlocks.length;
      this.checkSelection = 'none';
    }

    this.onScroll();
  }

  filterJiraFun(type) {
    switch (type) {
      case 'all': this.filterName = 'All';
        break;
      case 'zero': this.filterName = 'Zero Values';
        break;
      case 'nonzero': this.filterName = 'Non-Zero Values';
        break;
      default: this.filterName = 'Filter';
        break;
    }

    if (this.checkSelection !== type && type !== 'all') {
      const tempBuilding: BuildingBlockModel[] = new Array();
      if (this.buildingBlocks !== undefined) {
        this.sortOrderForAppName = true;
        for (let i = 0; i < (this.buildingBlocks.length); i++) {
          if (type === 'zero') {
            if (this.buildingBlocks[i].metrics[0] !== undefined && this.buildingBlocks[i].metrics[0] != null) {
              if (this.buildingBlocks[i].metrics[0].value.value === 0) {
                tempBuilding.push(this.buildingBlocks[i]);
                this.reportingBuildingBlocks = tempBuilding.length;
              }
            }
          } else if (type === 'nonzero') {
            if (this.buildingBlocks[i].metrics[0] !== undefined && this.buildingBlocks[i].metrics[0] != null) {
              if (this.buildingBlocks[i].metrics[0].value.value !== 0) {
                tempBuilding.push(this.buildingBlocks[i]);
                this.reportingBuildingBlocks = tempBuilding.length;
              }
            }
          }
        }
      }
      this.buildingBlocksCopy = tempBuilding;
      this.totalBuildingBlocks = this.buildingBlocks.length;
      this.sortForAppName();
      this.checkSelection = type;
    } else {
      this.buildingBlocksCopy = this.buildingBlocks;
      this.sortOrderForAppName = true;
      this.sortForAppName();
      this.reportingBuildingBlocks = this.buildingBlocksCopy.filter(this.reportingProductsFilter).length;
      this.totalBuildingBlocks = this.buildingBlocks.length;
      this.checkSelection = 'none';
    }
    if (this.metric !== undefined) {
      this.buildingBlocksCopy = this.buildingBlocksCopy.filter(buildingBlock => buildingBlock.metricType === this.metric);
    }
    this.onScroll();
  }

  sortFor(type: string) {
    switch (type) {
      case 'appName': this.sortForAppName();
        break;
      case 'cycleTime': this.sortByTimeZoneUnits('cycleTime');
        break;
      case 'mttr': this.sortByTimeZoneUnits('mttr');
        break;
      case 'cadence': this.sortByValue('cadence');
        break;
      case 'buildavgtime': this.sortByTimeZoneUnits('buildavgtime');
        break;
      case 'deployavgtime': this.sortByTimeZoneUnits('deployavgtime');
        break;
      case 'saydoratioStoriesRatio': this.sortByValue('saydoratioStoriesRatio');
        break;
      case 'devopscup': this.sortByValue('devopscup');
        break;
      default: this.sortForMetrics(type);
        break;
    }
  }

  sortByValue(metric: string) {
    if (this.buildingBlocksCopy !== undefined && this.buildingBlocksCopy.length !== 0) {
      let temp: BuildingBlockModel[] = new Array();
      const temp1: BuildingBlockModel[] = new Array();
      const unUsed: BuildingBlockModel[] = new Array();
      this.buildingBlocksCopy.forEach(a => {
        if (a.metrics.length !== 0) {
          if ((a.metrics[0].message !== undefined && a.metrics[0].message === 'No Sprints')
            || (metric === 'devopscup' && !a.metrics[0].available)) {
            unUsed.push(a);
          } else {
            temp.push(a);
          }
        } else {
          temp1.push(a);
        }
      });
      temp.sort((a1, a2) => {
        return a1.metrics[0].value.value - a2.metrics[0].value.value;
      });

      let indicators = '';
      switch (metric) {
        case 'saydoratioStoriesRatio':
          if (this.sortOrderForStoriesRatio) {
            this.sortOrderForStoriesRatio = false;
            this.saydoratiostoriesIcon = 'down-arrow';
            indicators = 'down-arrow';
          } else {
            temp = temp.reverse();
            this.sortOrderForStoriesRatio = true;
            this.saydoratiostoriesIcon = 'up-arrow';
            indicators = 'up-arrow';
          }
          break;
        case 'cadence':
          if (this.sortOrderForCadence) {
            this.sortOrderForCadence = false;
            this.cadenceIcon = 'down-arrow';
            indicators = 'down-arrow';
          } else {
            temp = temp.reverse();
            this.sortOrderForCadence = true;
            this.cadenceIcon = 'up-arrow';
            indicators = 'up-arrow';
          }
          break;
        case 'devopscup':
          if (this.sortOrderForDevopscup) {
            this.sortOrderForDevopscup = false;
            this.devopscupIcon = 'up-arrow';
            indicators = 'up-arrow';
          } else {
            temp = temp.reverse();
            this.sortOrderForDevopscup = true;
            this.devopscupIcon = 'down-arrow';
            indicators = 'down-arrow';
          }
          break;
      }
      this.buildingBlocksCopy = temp;
      temp1.forEach(a => this.buildingBlocksCopy.push(a));
      this.changeSortOrder(indicators);
      this.onScroll();
    }
  }

  changeSortOrder(arrow: string) {
    if ('up-arrow'.localeCompare(arrow)) {
      this.sortOrder = true;
    } else {
      this.sortOrder = false;
    }
  }

  sortByTimeZoneUnits(metric: string) {
    if (this.buildingBlocksCopy !== undefined && this.buildingBlocksCopy.length !== 0) {
      const mins: BuildingBlockModel[] = new Array();
      const days: BuildingBlockModel[] = new Array();
      const hours: BuildingBlockModel[] = new Array();
      const undef: BuildingBlockModel[] = new Array();
      this.buildingBlocksCopy.forEach(item => {
        if (item.metrics[1] !== undefined) {
          if (item.metrics[1].value.unit === 'm') {
            mins.push(item);
          }
          if (item.metrics[1].value.unit === 'h' || item.metrics[1].value.unit === 'hr' || item.metrics[1].value.unit === 'hrs') {
            hours.push(item);
          }
          if (item.metrics[1].value.unit === 'd') {
            days.push(item);
          }
        } else {
          undef.push(item);
        }
      });
      for (let itr1 = 0; itr1 < mins.length; itr1++) {
        for (let itr2 = 0; itr2 < mins.length - 1; itr2++) {
          if (mins[itr2].metrics[1].value.value > mins[itr2 + 1].metrics[1].value.value) {
            const minute = mins[itr2];
            mins[itr2] = mins[itr2 + 1];
            mins[itr2 + 1] = minute;
          }
        }
      }
      for (let itr1 = 0; itr1 < hours.length; itr1++) {
        for (let itr2 = 0; itr2 < hours.length - 1; itr2++) {
          if (hours[itr2].metrics[1].value.value > hours[itr2 + 1].metrics[1].value.value) {
            const hour = hours[itr2];
            hours[itr2] = hours[itr2 + 1];
            hours[itr2 + 1] = hour;
          }
        }
      }
      for (let itr1 = 0; itr1 < days.length; itr1++) {
        for (let itr2 = 0; itr2 < days.length - 1; itr2++) {
          if (days[itr2].metrics[1].value.value > days[itr2 + 1].metrics[1].value.value) {
            const day = days[itr2];
            days[itr2] = days[itr2 + 1];
            days[itr2 + 1] = day;
          }
        }
      }
      this.buildingBlocksCopy = days;
      hours.forEach(item => {
        this.buildingBlocksCopy.push(item);
      });
      mins.forEach(item => {
        this.buildingBlocksCopy.push(item);
      });
      undef.forEach(item => {
        this.buildingBlocksCopy.push(item);
      });

      this.buildingBlocksCopy = this.buildingBlocksCopy.reverse();

      switch (metric) {
        case 'deploy':
          if (this.sortOrderForDeployAvgTime) {
            this.sortOrderForDeployAvgTime = false;
            this.deployavgtimeIcon = 'down-arrow';
            this.sortOrder = false;
          } else {
            this.sortOrderForDeployAvgTime = true;
            this.deployavgtimeIcon = 'up-arrow';
            this.sortOrder = true;
          }
          break;
        case 'cycleTime':
          if (this.sortOrderForCycleTime) {
            this.sortOrderForCycleTime = false;
            this.cycleTimeIcon = 'down-arrow';
            this.sortOrder = false;
          } else {
            this.sortOrderForCycleTime = true;
            this.cycleTimeIcon = 'up-arrow';
            this.sortOrder = true;
          }
          break;
        case 'mttr':
          if (this.sortOrderForMttr) {
            this.sortOrderForMttr = false;
            this.mttrIcon = 'down-arrow';
            this.sortOrder = false;
          } else {
            this.sortOrderForMttr = true;
            this.mttrIcon = 'up-arrow';
            this.sortOrder = true;
          }
          break;
        case 'build':
          if (this.sortOrderForBuiildAvgTime) {
            this.sortOrderForBuiildAvgTime = false;
            this.buildavgtimeIcon = 'down-arrow';
            this.sortOrder = false;
          } else {
            this.sortOrderForBuiildAvgTime = true;
            this.buildavgtimeIcon = 'up-arrow';
            this.sortOrder = true;
          }
          break;
      }
      this.onScroll();
    }
  }

  sortForMetrics(type: string) {
    if (this.buildingBlocksCopy !== undefined && this.buildingBlocksCopy.length !== 0) {
      let indicator = 'up-arrow';
      let temp: BuildingBlockModel[] = new Array();
      const temp1: BuildingBlockModel[] = new Array();
      const unUsed: BuildingBlockModel[] = new Array();
      this.buildingBlocksCopy.forEach(a => {
        if (type === 'saydoratio') {
          if (a.metrics.length !== 0) {
            if (a.metrics[0].message !== undefined && a.metrics[0].message === 'No Sprints') {
              unUsed.push(a);
            } else {
              temp.push(a);
            }
          } else {
            temp1.push(a);
          }
        } else {
          if (a.metrics.length !== 0) {
            temp.push(a);
          } else {
            temp1.push(a);
          }
        }
      });
      switch (type) {
        case 'blocker': temp.sort((a1, a2) => {
          return a1.metrics[0].blocker - a2.metrics[0].blocker;
        });
          break;
        case 'critical': temp.sort((a1, a2) => {
          return a1.metrics[0].critical - a2.metrics[0].critical;
        });
          break;
        case 'major': temp.sort((a1, a2) => {
          return a1.metrics[0].major - a2.metrics[0].major;
        });
          break;
        default: temp.sort((a1, a2) => {
          return a1.metrics[0].value.value - a2.metrics[0].value.value;
        });
          break;
      }
      if (!this.sortOrder) {
        temp = temp.reverse();
        indicator = 'down-arrow';
      }
      this.buildingBlocksCopy = temp;
      temp1.forEach(a => this.buildingBlocksCopy.push(a));
      if (type === 'saydoratio') {
        unUsed.forEach(a => this.buildingBlocksCopy.push(a));
      }
      switch (type) {
        case 'outages': this.outagesIcon = indicator;
          break;
        case 'velocity': this.velocityIcon = indicator;
          break;
        case 'security': this.securityIcon = indicator;
          break;
        case 'quality': this.qualityIcon = indicator;
          break;
        case 'wip': this.wipIcon = indicator;
          break;
        case 'totalValue': this.totalValueIcon = indicator;
          break;
        case 'stash': this.stashIcon = indicator;
          break;
        case 'build': this.buildIcon = indicator;
          break;
        case 'deploy': this.deployIcon = indicator;
          break;
        case 'saydoratio': this.saydoratioIcon = indicator;
          break;
        case 'test': this.testIcon = indicator;
          break;
        case 'cloud': this.cloudIcon = indicator;
          break;
        case 'devopscup': this.devopscupIcon = indicator;
          break;
        case 'blocker': this.blockerIcon = indicator;
          break;
        case 'major': this.majorIcon = indicator;
          break;
        case 'critical': this.criticalIcon = indicator;
          break;
      }
      this.changeSortOrder(indicator);
      this.onScroll();
    }
  }

  sortForAppName() {
    if (this.buildingBlocksCopy !== undefined && this.buildingBlocksCopy.length !== 0) {
      if (this.sortOrderForAppName) {
        this.buildingBlocksCopy.sort((a1, a2) => {
          if (a1.name.trim().toLowerCase() > a2.name.trim().toLowerCase()) {
            return 1;
          }
          if (a1.name.trim().toLowerCase() < a2.name.trim().toLowerCase()) {
            return -1;
          }
          return 0;
        });

        this.sortOrderForAppName = false;
        this.appNameIcon = 'up-arrow';
      } else {
        this.buildingBlocksCopy.sort((a1, a2) => {
          if (a1.name.trim().toLowerCase() < a2.name.trim().toLowerCase()) {
            return 1;
          }
          if (a1.name.trim().toLowerCase() > a2.name.trim().toLowerCase()) {
            return -1;
          }
          return 0;
        });
        this.sortOrderForAppName = true;
        this.appNameIcon = 'down-arrow';
      }
      this.onScroll();
    }
  }
  setSpinnerStatusForApp(val: boolean) {
    if (val === false) {
      this.spinnerVisibility = 'hidden spinner center';
    }
    this.showSpinnerApp.emit(val);
  }

}
