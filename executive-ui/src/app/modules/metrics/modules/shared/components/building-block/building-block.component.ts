import { Component, OnInit, Input, Output, EventEmitter, ChangeDetectionStrategy } from '@angular/core';
import { BuildingBlockModel } from '../../component-models/building-block-model';
import { BuildingBlockMetricSummaryModel } from '../../component-models/building-block-metric-summary-model';
import { ActivatedRoute, Router } from '@angular/router';


@Component({
  selector: 'app-building-block-card',
  templateUrl: './building-block.component.html',
  styleUrls: ['./building-block.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class BuildingBlockComponent implements OnInit {
  @Input() public buildingBlock: BuildingBlockModel;
  @Input() public isComponent: boolean;
  @Input() public cardsList: string[];
  @Output() public showSpinnerForApp = new EventEmitter<boolean>();
  public jiraUrl: string;
  public totalBuildingBlocks: number;
  public isDataSourceJira: boolean;
  public securityUrl: string;
  public devopscupUrl: string;
  public isDataSourceSecurity: boolean;
  public isDataSourceDevopscup: boolean;
  public vastId: string;

  constructor(private router: Router,
    private route: ActivatedRoute) {
    this.isDataSourceJira = false;
    this.isDataSourceSecurity = false;
    this.isDataSourceDevopscup = false;
    this.showSpinnerForApp.emit(false);
  }

  ngOnInit() {
    if (this.buildingBlock.metrics[0] != undefined && this.buildingBlock.metrics[1] != undefined && (this.buildingBlock.metrics[0].value.name == 'Total Builds' || this.buildingBlock.metrics[0].value.name == 'Total Deploys' || this.buildingBlock.metrics[0].value.name == 'Total Builds' || this.buildingBlock.metrics[0].value.name == 'Story Points Completed' || this.buildingBlock.metrics[0].value.name == 'DevOps Cup Improvements')) {
      if (String(this.buildingBlock.metrics[1].value.value) == 'NaN') {
        let filterObj: BuildingBlockMetricSummaryModel[] = new Array();
        filterObj.push(this.buildingBlock.metrics[0]);
        this.buildingBlock.metrics = filterObj;
      }
    }
    this.showSpinnerForApp.emit(false);
  }
  showMetrics(metric: string) {
    if (this.cardsList.includes(metric) || (this.cardsList.includes('pipeline-lead-time') && metric == undefined)) {
      return false;
    }
    return true;
  }

  gotToBuildingBlockDetails() {

    if (!!this.isComponent) {
      window.open(this.buildingBlock.detail.url, '_blank');
    } else {
      if (!this.buildingBlock.detail) {
        this.router.navigate(['product', this.buildingBlock.id], { relativeTo: this.route });
      } else if (this.buildingBlock.detail.url) {
        this.router.navigateByUrl(this.buildingBlock.detail.url);
      } else {
        this.router.navigate(this.buildingBlock.detail.commands, { relativeTo: this.route });
      }
    }
  }

  hasMetricReports() {

    return this.buildingBlock.metrics.some((metric) => {
      return !metric.isEmpty;
    });
  }

  isNavigationValid(): boolean {
    return this.hasMetricReports() && (!this.isComponent || !!this.isComponent && !!this.buildingBlock.detail.url);
  }

  isNavigationValidComponent(): boolean {
    return !!this.buildingBlock.detail.url;
  }

  isOutsideNavigationValid(): number {

    if (this.isComponent === undefined) {
      return 2;
    }


    this.totalBuildingBlocks = this.buildingBlock.metrics.length;

    if (this.isComponent) {
      if (this.buildingBlock.metrics.length != 0) {
        if (this.buildingBlock.metrics[0].value.name == 'Velocity as Days per Story Point' || this.buildingBlock.metrics[0].value.name == 'Defects' || this.buildingBlock.metrics[0].value.name == 'Work In Progress'
          || this.buildingBlock.metrics[0].value.name == 'Production Events' || this.buildingBlock.metrics[0].value.name == 'Stories') {
          if (this.buildingBlock.projectKey != undefined) {
            this.isDataSourceJira = true;
            return 1;
          } else {
            this.isDataSourceJira = false;
            return 0;
          }
        }
        else {
          this.isDataSourceJira = false;
          return 0;
        }
      }
      else {
        this.isDataSourceJira = false;
        return 0;
      }

    } else if (!this.isComponent) {
      if (this.buildingBlock.metrics.length != 0) {
        if (this.buildingBlock.metrics[0].value.name == 'Security Vulnerabilities') {
          this.isDataSourceSecurity = true;
          this.isDataSourceDevopscup = false;
          return 0;
        }
        else if (this.buildingBlock.metrics[0].value.name == 'DevOps Cup Improvements') {
          this.isDataSourceSecurity = false;
          this.isDataSourceDevopscup = true;
          return 0;
        }
        else {
          this.isDataSourceSecurity = false;
          this.isDataSourceDevopscup = false;
          return 0;
        }

      }
      else {
        this.isDataSourceSecurity = false;
        this.isDataSourceDevopscup = false;
        return 0;
      }
    }

  }

  goToOtherBoard() {
    if (this.isComponent) {
      if (this.buildingBlock.metrics[0].value.name == 'Velocity as Days per Story Point') {
        this.jiraUrl = 'https://jra.com/issues/?jql=project%20%3D%20' + this.buildingBlock.projectKey + '%20AND%20createdDate%20%3E%3D%20-90d%20AND%20statusCategory%20%3D%20Done%20AND%20issuetype%20in%20(%22VZAgile%20Story%22%2C%20Story%2C%20%22New%20Feature%22%2C%20Enhancement)';
      } else if (this.buildingBlock.metrics[0].value.name == 'Defects') {
        this.jiraUrl = 'https://jra.com/issues/?jql=project%20%3D%20' + this.buildingBlock.projectKey + '%20AND%20issuetype%20%3D%20Bug%20AND%20createdDate%20>%3D%20-90d%20AND%20(cf%5B10500%5D%20%3D%20PROD%20OR%20cf%5B42215%5D%20%3D%20Prod%20OR%20cf%5B35714%5D%20%3D%20Production%20OR%20cf%5B10421%5D%20%3D%20Production%20OR%20cf%5B11728%5D%20%3D%20PROD%20OR%20cf%5B15303%5D%20%3D%20PRD)';
      } else if (this.buildingBlock.metrics[0].value.name == 'Work In Progress') {
        this.jiraUrl = 'https://jra.com/issues/?jql=project%20%3D%20' + this.buildingBlock.projectKey + '%20AND%20createdDate%20%3E%3D%20-90d%20AND%20statusCategory%20%3D%20%22In%20Progress%22%20AND%20issuetype%20in%20(%22VZAgile%20Story%22%2C%20Story%2C%20Bug%2C%20Task%2C%20Epic%2C%20%22New%20Feature%22%2C%20Enhancement)';
      } else if (this.buildingBlock.metrics[0].value.name == 'Production Events') {
        this.jiraUrl = 'https://portal.com/Crisis/crisis/ShowCR/' + this.buildingBlock.projectKey;
      } else if (this.buildingBlock.metrics[0].value.name == 'Stories') {
        this.jiraUrl = 'https://jra.com/issues/?jql=project%20%3D%20' + this.buildingBlock.projectKey + '%20AND%20createdDate%20%3E%3D%20-90d%20AND%20statusCategory%20%3D%20Done%20AND%20issuetype%20in%20(%22VZAgile%20Story%22%2C%20Story)';
      }
      window.open(this.jiraUrl, '_blank');
    } else if (!this.isComponent) {
      if (this.buildingBlock.metrics[0].value.name == 'Security Vulnerabilities') {
        this.securityUrl = 'https://portal.com/AppDetail.aspx?vastID=' + this.buildingBlock.vastId + '&tab=fifteen';
        window.open(this.securityUrl, '_blank');
      }
      else if (this.buildingBlock.metrics[0].value.name == 'DevOps Cup Improvements') {
        this.devopscupUrl = 'https://hygieia.com';
        window.open(this.devopscupUrl, '_blank');
      }
    }
  }

  hideDetailBtn(metric): boolean {
    if (!metric.isEmpty && !metric.available) {
      return true;
    }
    if (metric.value.name == 'Production Events' && (metric.value.value == 0)) {
      return true;
    }
    if (metric.value.name == 'mean-time-to-resolve' || metric.value.name == 'Mean Time to Resolve') {
      return true
    }
    return false;
  }
  

}
