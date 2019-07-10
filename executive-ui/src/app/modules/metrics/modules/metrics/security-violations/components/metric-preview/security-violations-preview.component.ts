import {MetricPreviewBaseComponent} from '../../../../shared/components/metric-preview/metric-preview-base.component';
import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {SecurityViolationsService} from '../../services/security-violations.service';
import {SecurityViolationsPreviewStrategy} from '../../strategies/security-violations-preview-strategy';
import {SecurityViolationsConfiguration} from '../../security-violations.configuration';

/**
 * @export
 * @class SecurityViolationsPreviewComponent
 * @extends {MetricPreviewBaseComponent}
 * @implements {OnInit}
 */
@Component({
  selector: 'app-security-violations-preview',
  templateUrl: '../../../../shared/components/metric-preview/metric-preview-base.component.html',
  styleUrls: ['../../../../shared/components/metric-preview/metric-preview-base.component.scss'],
  providers: [SecurityViolationsService]
})
export class SecurityViolationsPreviewComponent extends MetricPreviewBaseComponent implements OnInit {

  constructor(private securityViolationsService: SecurityViolationsService,
              protected router: Router,
              public strategy: SecurityViolationsPreviewStrategy) { super(); }

  ngOnInit() {
    this.dataService = this.securityViolationsService;
    this.metric = SecurityViolationsConfiguration.identifier;
    this.loadMetricSummaryData();
  }
}
