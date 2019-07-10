import { MetricPreviewBaseComponent } from '../../../../shared/components/metric-preview/metric-preview-base.component';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { QualityService } from '../../services/quality.service';
import { QualityPreviewStrategy } from '../../strategies/quality-preview-strategy';
import { QualityConfiguration } from '../../quality.configuration';

/**
 * @export
 * @class QualityPreviewComponent
 * @extends {MetricPreviewBaseComponent}
 * @implements {OnInit}
 */
@Component({
  selector: 'app-quality-preview',
  templateUrl: '../../../../shared/components/metric-preview/metric-preview-base.component.html',
  styleUrls: ['../../../../shared/components/metric-preview/metric-preview-base.component.scss'],
  providers: [QualityService]
})
export class QualityPreviewComponent extends MetricPreviewBaseComponent implements OnInit {

  constructor(private securityViolationsService: QualityService,
    protected router: Router,
    public strategy: QualityPreviewStrategy) { super(); }

  ngOnInit() {
    this.dataService = this.securityViolationsService;
    this.metric = QualityConfiguration.identifier;
    this.loadMetricSummaryData();
  }
}
