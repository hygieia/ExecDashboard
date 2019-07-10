import { Component, OnInit, Input, Output, EventEmitter, OnDestroy } from '@angular/core';
import { OpenSourceViolationsConfiguration } from '../../../metrics/open-source-violations/open-source-violations.configuration';
import { PipelineLeadTimeConfiguration } from '../../../metrics/pipeline-lead-time/pipeline-lead-time.configuration';
import { ProductionIncidentsConfiguration } from '../../../metrics/production-incidents/production-incidents.configuration';
import { ProductionReleasesConfiguration } from '../../../metrics/production-releases/production-releases.configuration';
import { SecurityViolationsConfiguration } from '../../../metrics/security-violations/security-violations.configuration';
import { QualityConfiguration } from '../../../metrics/quality/quality.configuration';
import { CodeRepoConfiguration } from '../../../metrics/code-repo/code-repo.configuration';
import { StaticCodeAnalysisConfiguration } from '../../../metrics/static-code-analysis/static-code-analysis.configuration';
import { TestAutomationConfiguration } from '../../../metrics/test-automation/test-automation.configuration';
import { UnitTestCoverageConfiguration } from '../../../metrics/unit-test-coverage/unit-test-coverage.configuration';
import { WorkInProgressConfiguration } from '../../../metrics/work-in-progress/work-in-progress.configuration';
import { DevopsCupConfiguration } from '../../../metrics/devopscup/devopscup.configuration';
import { PortfolioService } from '../../../../../shared/services/portfolio.service';
import { MetricTrendModel } from '../../../shared/component-models/metric-trend-model';
import { Clarity } from '../../../../../shared/domain-models/clarity';
// import {ProgramModule} from '../../../programs/components/program-module/ProgramModule';
import { Observable } from 'rxjs/Observable';
import { Router } from '@angular/router';


@Component({
  selector: 'app-metric-programs',
  templateUrl: './metric-programs.component.html',
  styleUrls: ['./metric-programs.component.scss'],
  providers: []
})

export class MetricProgramsComponent implements OnInit {
  @Input()
  public portfolioId: string;

  public clarity: Clarity;

  protected router: Router;

  public selectedApp: any;

  public isdetailsenabled: boolean = true;

  public showDetails: Array<string>;

  constructor(private portfolioService: PortfolioService) { }

  ngOnInit() {
    this.portfolioService.getClarityListMap(this.portfolioId)
      .subscribe(
        result => {
          this.clarity = result;
        },
        error => {
          console.log(error);
        }
      );
  }

  getMetricPreviewTrend(): MetricTrendModel {
    const metricTrend = new MetricTrendModel();
    metricTrend.direction = 'neutral';
    metricTrend.danger = false;

    return metricTrend;
  }

  viewDetails(selectedApp: any): void {
    this.selectedApp = selectedApp;
    this.isdetailsenabled = false;

  }


}
