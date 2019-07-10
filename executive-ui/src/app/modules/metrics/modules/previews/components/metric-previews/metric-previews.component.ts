import { Component, OnInit, Input, Output, EventEmitter, OnDestroy } from '@angular/core';
import { OpenSourceViolationsConfiguration } from '../../../metrics/open-source-violations/open-source-violations.configuration';
import { PipelineLeadTimeConfiguration } from '../../../metrics/pipeline-lead-time/pipeline-lead-time.configuration';
import { ProductionIncidentsConfiguration } from '../../../metrics/production-incidents/production-incidents.configuration';
import { ProductionReleasesConfiguration } from '../../../metrics/production-releases/production-releases.configuration';
import { SecurityViolationsConfiguration } from '../../../metrics/security-violations/security-violations.configuration';
import { QualityConfiguration } from '../../../metrics/quality/quality.configuration';
import { StaticCodeAnalysisConfiguration } from '../../../metrics/static-code-analysis/static-code-analysis.configuration';
import { TestAutomationConfiguration } from '../../../metrics/test-automation/test-automation.configuration';
import { UnitTestCoverageConfiguration } from '../../../metrics/unit-test-coverage/unit-test-coverage.configuration';
import { WorkInProgressConfiguration } from '../../../metrics/work-in-progress/work-in-progress.configuration';
import { BuildConfiguration } from '../../../metrics/build/build.configuration';
import { DeployConfiguration } from '../../../metrics/deploy/deploy.configuration';
import { CloudConfiguration } from '../../../metrics/cloud/cloud.configuration';
import { CodeRepoConfiguration } from '../../../metrics/code-repo/code-repo.configuration';
import { SayDoRatioConfiguration } from '../../../metrics/saydoratio/saydoratio.configuration';
import { TestConfiguration } from '../../../metrics/test/test.configuration';
import { ThroughputConfiguration } from '../../../metrics/throughput/throughput.configuration';
import { ProductService } from '../../../shared/services/product.service';
import { DevopsCupConfiguration } from '../../../metrics/devopscup/devopscup.configuration';
@Component({
  selector: 'app-metric-previews',
  templateUrl: './metric-previews.component.html',
  styleUrls: ['./metric-previews.component.scss'],
  providers: []
})
export class MetricPreviewsComponent implements OnInit, OnDestroy {
  @Input() public portfolioId: string;
  @Input() public productId: string;
  @Input() public compress: boolean;
  @Input() public selectedMetric: string;
  @Output() showBuildingBlocksEvent = new EventEmitter<string>();
  @Output() hideBuildingBlocksEvent = new EventEmitter<boolean>();
  @Output() showSpinnerLoad = new EventEmitter<boolean>();

  public count = 0;

  private shuffleInstance;
  private shuffleInstanceTimerId;
  public showVelocity = false;
  public showCycleTime = false;
  public showSecurity = false;
  public showEvents = false;
  public showQuality = false;
  public showWip = false;
  public showStories = false;
  public showBuild = false;
  public showDeploy = false;
  public showCloud = false;
  public showCodeRepo = false;
  public showSayDoRatio = false;
  public showTest = false;
  public showDevopsCup = false;

  public cardsList: string[];

  public sortMap = new Map<string, any>([
    [OpenSourceViolationsConfiguration.identifier, {
      defaultSort: 1,
      currentSort: 1
    }],
    [PipelineLeadTimeConfiguration.identifier, {
      defaultSort: 2,
      currentSort: 2
    }],
    [SecurityViolationsConfiguration.identifier, {
      defaultSort: 3,
      currentSort: 3
    }],
    [ProductionIncidentsConfiguration.identifier, {
      defaultSort: 4,
      currentSort: 4
    }],
    [QualityConfiguration.identifier, {
      defaultSort: 5,
      currentSort: 5
    }],
    [WorkInProgressConfiguration.identifier, {
      defaultSort: 6,
      currentSort: 6
    }],
    [ThroughputConfiguration.identifier, {
      defaultSort: 7,
      currentSort: 7
    }],
    [BuildConfiguration.identifier, {
      defaultSort: 8,
      currentSort: 8
    }],
    [DeployConfiguration.identifier, {
      defaultSort: 9,
      currentSort: 9
    }],
    [CloudConfiguration.identifier, {
      defaultSort: 91,
      currentSort: 91
    }],
    [CodeRepoConfiguration.identifier, {
      defaultSort: 92,
      currentSort: 92
    }],
    [SayDoRatioConfiguration.identifier, {
      defaultSort: 93,
      currentSort: 93
    }],
    [TestConfiguration.identifier, {
      defaultSort: 94,
      currentSort: 94
    }],
    [DevopsCupConfiguration.identifier,
    {
      defaultSort: 95,
      currentSort: 95
    }]
  ]);

  constructor(private productService: ProductService) { }

  ngOnInit() {
    this.productService.getCardsList()
      .subscribe(
        result => {
          this.cardsList = result;
          this.setCards();
          this.shuffleInstance = this.createShuffleInstance(0);
          this.queueCreationOfAnimatedShuffleInstance();
        },
        error => {
          console.log(error);
        }
      );
  }

  private setCards() {
    this.cardsList.forEach(element => {
      switch (element) {
        case 'open-source-violations':
          this.showVelocity = true;
          break;
        case 'pipeline-lead-time':
          this.showCycleTime = true;
          break;
        case 'security-violations':
          this.showSecurity = true;
          break;
        case 'production-incidents':
          this.showEvents = true;
          break;
        case 'quality':
          this.showQuality = true;
          break;
        case 'work-in-progress':
          this.showWip = true;
          break;
        case 'total-value':
          this.showStories = true;
          break;
        case 'build':
          this.showBuild = true;
          break;
        case 'deploy':
          this.showDeploy = true;
          break;
        case 'cloud':
          this.showCloud = true;
          break;
        case 'stash':
          this.showCodeRepo = true;
          break;
        case 'saydoratio':
          this.showSayDoRatio = true;
          break;
        case 'test':
          this.showTest = true;
          break;
        case 'devopscup':
          this.showDevopsCup = true;
          break;
      }
    });
  }

  private createShuffleInstance(speed: number) {
    const element = document.querySelector('.metric-previews');
    const sizer = element.querySelector('.placeholder');
    const Shuffle = window['Shuffle'];

    return new Shuffle(element, {
      itemSelector: '.metric',
      sizer: sizer,
      speed: speed
    });
  }

  ngOnDestroy() {
    this.shuffleInstance.destroy();
  }

  isDisabled(metric: string) {
    return this.isOtherMetricSelected(metric);
  }

  isMetricSelected() {
    return Array.from(this.sortMap.values()).some((metric) => metric.currentSort === 0);
  }

  isOtherMetricSelected(metric: string) {
    return this.isMetricSelected() && this.sortMap.get(metric).currentSort !== 0;
  }

  reset(): void {
    this.selectedMetric = undefined;
    this.hideBuildingBlocks();
    this.resetSort();
    setTimeout(() => this.shuffleInstance.update());
  }

  addMetric() {
    this.shuffleInstance = this.createShuffleInstance(0);
    this.resetSort();
    setTimeout(() => this.shuffleInstance.update());
    this.queueCreationOfAnimatedShuffleInstance();
  }

  queueCreationOfAnimatedShuffleInstance() {
    if (this.shuffleInstanceTimerId) {
      clearTimeout(this.shuffleInstanceTimerId);
    }

    this.shuffleInstanceTimerId = setTimeout(() => {
      this.shuffleInstance = this.createShuffleInstance(400);
    }, 100);
  }

  hideBuildingBlocks(): void {
    this.selectedMetric = undefined;
    this.hideBuildingBlocksEvent.emit(true);
    this.updateSort();
    setTimeout(() => this.shuffleInstance.update());
  }

  returnReversedCardsToContentSide() {
    Array.from(document.querySelectorAll('.show-description')).forEach(item => {
      (<HTMLElement>item.querySelector('.close-button')).click();
    });
  }

  showBuildingBlocks(metric: string): void {
    this.showBuildingBlocksEvent.emit(metric);
    this.returnReversedCardsToContentSide();
    setTimeout(() => this.shuffleInstance.update());
  }

  selectMetric(metric: string): void {
    this.selectedMetric = metric;
    this.sortMap.get(metric).currentSort = 0;
    this.updateSort();
  }

  private resetSort() {
    this.sortMap.forEach((metric) => {
      metric.currentSort = metric.defaultSort;
    });
  }

  private updateSort(): void {
    this.shuffleInstance.sort({
      by: function (element) {
        return element.getAttribute('data-sort');
      }
    });
  }
  showSpinnerFromChild(value: boolean): void {

    if (value === false) {
      this.count++;
      if (this.count === 14) {
        this.showSpinnerLoad.emit(false);
      } else {
        this.showSpinnerLoad.emit(true);
      }
    }

  }

}
