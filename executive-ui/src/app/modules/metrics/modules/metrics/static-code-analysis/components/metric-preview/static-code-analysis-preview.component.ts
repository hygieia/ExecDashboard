import {MetricPreviewBaseComponent} from '../../../../shared/components/metric-preview/metric-preview-base.component';
import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {StaticCodeAnalysisService} from '../../services/static-code-analysis.service';
import {StaticCodeAnalysisPreviewStrategy} from '../../strategies/static-code-analysis-preview-strategy';
import {StaticCodeAnalysisConfiguration} from '../../static-code-analysis.configuration';

/**
 * @export
 * @class StaticCodeAnalysisPreviewComponent
 * @extends {MetricPreviewBaseComponent}
 * @implements {OnInit}
 */
@Component({
  selector: 'app-static-code-analysis-preview',
  templateUrl: '../../../../shared/components/metric-preview/metric-preview-base.component.html',
  styleUrls: ['../../../../shared/components/metric-preview/metric-preview-base.component.scss'],
  providers: [StaticCodeAnalysisService]
})
export class StaticCodeAnalysisPreviewComponent extends MetricPreviewBaseComponent implements OnInit {

  constructor(private staticCodeAnalysisService: StaticCodeAnalysisService,
              protected router: Router,
              public strategy: StaticCodeAnalysisPreviewStrategy) { super(); }

  ngOnInit() {
    this.dataService = this.staticCodeAnalysisService;
    this.metric = StaticCodeAnalysisConfiguration.identifier;
    this.loadMetricSummaryData();
  }
}
