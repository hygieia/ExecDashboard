import {MetricPreviewBaseComponent} from '../../../../shared/components/metric-preview/metric-preview-base.component';
import {Component, OnInit} from '@angular/core';
import {UnitTestCoverageService} from '../../services/unit-test-coverage.service';
import {Router} from '@angular/router';
import {UnitTestCoveragePreviewStrategy} from '../../strategies/unit-test-coverage-preview-strategy';
import {UnitTestCoverageConfiguration} from '../../unit-test-coverage.configuration';

/**
 * @export
 * @class UnitTestCoveragePreviewComponent
 * @extends {MetricPreviewBaseComponent}
 * @implements {OnInit}
 */
@Component({
  selector: 'app-unit-test-coverage-card',
  templateUrl: '../../../../shared/components/metric-preview/metric-preview-base.component.html',
  styleUrls: ['../../../../shared/components/metric-preview/metric-preview-base.component.scss'],
  providers: [UnitTestCoverageService]
})
export class UnitTestCoveragePreviewComponent extends MetricPreviewBaseComponent implements OnInit {

  constructor(private unitTestCoverageService: UnitTestCoverageService,
              protected router: Router,
              public strategy: UnitTestCoveragePreviewStrategy) { super(); }

  ngOnInit() {
    this.dataService = this.unitTestCoverageService;
    this.metric = UnitTestCoverageConfiguration.identifier;
    this.loadMetricSummaryData();
  }
}
