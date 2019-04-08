import {MetricPreviewBaseComponent} from '../../../../shared/components/metric-preview/metric-preview-base.component';
import {Component, OnInit} from '@angular/core';
import {TestAutomationService} from '../../services/test-automation.service';
import {Router} from '@angular/router';
import {TestAutomationPreviewStrategy} from '../../strategies/test-automation-preview-strategy';
import {TestAutomationConfiguration} from '../../test-automation.configuration';

/**
 * @export
 * @class TestAutomationPreviewComponent
 * @extends {MetricPreviewBaseComponent}
 * @implements {OnInit}
 */
@Component({
  selector: 'app-test-automation-card',
  templateUrl: '../../../../shared/components/metric-preview/metric-preview-base.component.html',
  styleUrls: ['../../../../shared/components/metric-preview/metric-preview-base.component.scss'],
  providers: [TestAutomationService]
})
export class TestAutomationPreviewComponent extends MetricPreviewBaseComponent implements OnInit {

  constructor(private testAutomationService: TestAutomationService,
              protected router: Router,
              public strategy: TestAutomationPreviewStrategy) { super(); }

  ngOnInit() {
    this.dataService = this.testAutomationService;
    this.metric = TestAutomationConfiguration.identifier;
    this.loadMetricSummaryData();
  }
}
