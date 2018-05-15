import {OpenSourceViolationsPreviewStrategy} from '../../strategies/open-source-violations-preview-strategy';
import {MetricPreviewBaseComponent} from '../../../../shared/components/metric-preview/metric-preview-base.component';
import {Component, OnInit} from '@angular/core';
import {OpenSourceViolationsService} from '../../services/open-source-violations.service';
import {Router} from '@angular/router';
import {OpenSourceViolationsConfiguration} from '../../open-source-violations.configuration';

/**
 * @export
 * @class OpenSourceViolationsPreviewComponent
 * @extends {MetricPreviewBaseComponent}
 * @implements {OnInit}
 */
@Component({
  selector: 'app-open-source-violations-card',
  templateUrl: '../../../../shared/components/metric-preview/metric-preview-base.component.html',
  styleUrls: ['../../../../shared/components/metric-preview/metric-preview-base.component.scss'],
  providers: [OpenSourceViolationsService]
})
export class OpenSourceViolationsPreviewComponent extends MetricPreviewBaseComponent implements OnInit {

  constructor(private openSourceViolationsService: OpenSourceViolationsService,
              protected router: Router,
              public strategy: OpenSourceViolationsPreviewStrategy) { super(); }

  ngOnInit() {
    this.dataService = this.openSourceViolationsService;
    this.metric = OpenSourceViolationsConfiguration.identifier;
    this.loadMetricSummaryData();
  }
}
