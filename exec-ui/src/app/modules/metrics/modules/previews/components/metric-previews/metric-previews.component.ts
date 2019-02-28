import {Component, OnInit, Input, Output, EventEmitter, OnDestroy} from '@angular/core';
import {OpenSourceViolationsConfiguration} from '../../../metrics/open-source-violations/open-source-violations.configuration';
import {PipelineLeadTimeConfiguration} from '../../../metrics/pipeline-lead-time/pipeline-lead-time.configuration';
import {ProductionIncidentsConfiguration} from '../../../metrics/production-incidents/production-incidents.configuration';
import {ProductionReleasesConfiguration} from '../../../metrics/production-releases/production-releases.configuration';
import {SecurityViolationsConfiguration} from '../../../metrics/security-violations/security-violations.configuration';
import {StaticCodeAnalysisConfiguration} from '../../../metrics/static-code-analysis/static-code-analysis.configuration';
import {TestAutomationConfiguration} from '../../../metrics/test-automation/test-automation.configuration';
import {UnitTestCoverageConfiguration} from '../../../metrics/unit-test-coverage/unit-test-coverage.configuration';
import {TraceabilityConfiguration} from "../../../metrics/traceability/traceability.configuration";
import {SCMCommitsConfiguration} from '../../../metrics/scm-commits/scm-commits.configuration';

@Component({
  selector: 'app-metric-previews',
  templateUrl: './metric-previews.component.html',
  styleUrls: ['./metric-previews.component.scss'],
  providers: []
})
export class MetricPreviewsComponent implements OnInit, OnDestroy {
  @Input() public portfolioName: string;
  @Input() public portfolioLob: string;
  @Input() public productId: string;
  @Input() public compress: boolean;
  @Input() public selectedMetric: string;
  @Output() showBuildingBlocksEvent = new EventEmitter<string>();
  @Output() hideBuildingBlocksEvent = new EventEmitter<boolean>();

  private shuffleInstance;
  private shuffleInstanceTimerId;

  public sortMap = new Map<string, any>([
    [OpenSourceViolationsConfiguration.identifier, {
      defaultSort: 1,
      currentSort: 1
    }],
    [SecurityViolationsConfiguration.identifier, {
      defaultSort: 2,
      currentSort: 2
    }],
    [TestAutomationConfiguration.identifier, {
      defaultSort: 3,
      currentSort: 3
    }],
    [StaticCodeAnalysisConfiguration.identifier, {
      defaultSort: 4,
      currentSort: 4
    }],
    [ProductionIncidentsConfiguration.identifier, {
      defaultSort: 5,
      currentSort: 5
    }],
    [UnitTestCoverageConfiguration.identifier, {
      defaultSort: 6,
      currentSort: 6
    }],
    [ProductionReleasesConfiguration.identifier, {
      defaultSort: 7,
      currentSort: 7
    }],
    [PipelineLeadTimeConfiguration.identifier, {
      defaultSort: 8,
      currentSort: 8
    }],
    [SCMCommitsConfiguration.identifier, {
      defaultSort: 9,
      currentSort: 9
    }],
    [TraceabilityConfiguration.identifier, {
        defaultSort:10,
        currentSort: 10
    }],
  ]);

  constructor() {}

  ngOnInit() {
    this.shuffleInstance = this.createShuffleInstance(0);
    this.queueCreationOfAnimatedShuffleInstance();
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
}
