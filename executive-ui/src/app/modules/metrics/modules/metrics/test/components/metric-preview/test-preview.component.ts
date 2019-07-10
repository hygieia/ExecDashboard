import {MetricPreviewBaseComponent} from '../../../../shared/components/metric-preview/metric-preview-base.component';
import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {TestService} from '../../services/test.service';
import {TestPreviewStrategy} from '../../strategies/test-preview-strategy';
import {TestConfiguration} from '../../test.configuration';

/**
 * @export
 * @class TestPreviewComponent
 * @extends {MetricPreviewBaseComponent}
 * @implements {OnInit}
 */
@Component({
  selector: 'app-test-preview',
  templateUrl: '../../../../shared/components/metric-preview/metric-preview-base.component.html',
  styleUrls: ['../../../../shared/components/metric-preview/metric-preview-base.component.scss'],
  providers: [TestService]
})
export class TestPreviewComponent extends MetricPreviewBaseComponent implements OnInit {

  constructor(private TestService: TestService,
              protected router: Router,
              public strategy: TestPreviewStrategy) { super(); }

  ngOnInit() {
    this.dataService = this.TestService;
    this.metric = TestConfiguration.identifier;
    this.loadMetricSummaryData();
  }
}
