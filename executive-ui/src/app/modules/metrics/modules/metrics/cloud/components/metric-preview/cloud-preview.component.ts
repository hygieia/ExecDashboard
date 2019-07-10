import { MetricPreviewBaseComponent } from '../../../../shared/components/metric-preview/metric-preview-base.component';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CloudService } from '../../services/cloud.service';
import { CloudPreviewStrategy } from '../../strategies/cloud-preview-strategy';
import { CloudConfiguration } from '../../cloud.configuration';

/**
 * @export
 * @class CloudPreviewComponent
 * @extends {MetricPreviewBaseComponent}
 * @implements {OnInit}
 */
@Component({
  selector: 'app-cloud-preview',
  templateUrl: '../../../../shared/components/metric-preview/metric-preview-base.component.html',
  styleUrls: ['../../../../shared/components/metric-preview/metric-preview-base.component.scss'],
  providers: [CloudService]
})
export class CloudPreviewComponent extends MetricPreviewBaseComponent implements OnInit {

  constructor(private cloudService: CloudService,
    protected router: Router,
    public strategy: CloudPreviewStrategy) { super(); }

  ngOnInit() {
    this.dataService = this.cloudService;
    this.metric = CloudConfiguration.identifier;
    this.loadMetricSummaryData();
  }
}
