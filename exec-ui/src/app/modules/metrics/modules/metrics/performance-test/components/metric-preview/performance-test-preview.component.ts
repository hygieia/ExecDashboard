import {MetricPreviewBaseComponent} from '../../../../shared/components/metric-preview/metric-preview-base.component';
import {Component, OnInit} from '@angular/core';
import {PerformanceTestService} from '../../services/performance-test.service';
import {Router} from '@angular/router';
import {PerformanceTestPreviewStrategy} from '../../strategies/performance-test-preview-strategy';
import {PerformanceTestConfiguration} from '../../performance-test.configuration';

/**
 * @export
 * @class PerformanceTestPreviewComponent
 * @extends {MetricPreviewBaseComponent}
 * @implements {OnInit}
 */
@Component({
  selector: 'app-performance-test-card',
  templateUrl: '../../../../shared/components/metric-preview/metric-preview-base.component.html',
  styleUrls: ['../../../../shared/components/metric-preview/metric-preview-base.component.scss'],
  providers: [PerformanceTestService]
})
export class PerformanceTestPreviewComponent extends MetricPreviewBaseComponent implements OnInit {

  constructor(private performanceTestService: PerformanceTestService,
              protected router: Router,
              public strategy: PerformanceTestPreviewStrategy) { super(); }

  ngOnInit() {
    this.dataService = this.performanceTestService;
    this.metric = PerformanceTestConfiguration.identifier;
    this.loadMetricSummaryData();
  }
}
