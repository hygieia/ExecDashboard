import { MetricPreviewBaseComponent } from '../../../../shared/components/metric-preview/metric-preview-base.component';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DeployService } from '../../services/deploy.service';
import { DeployPreviewStrategy } from '../../strategies/deploy-preview-strategy';
import { DeployConfiguration } from '../../deploy.configuration';

/**
 * @export
 * @class DeployPreviewComponent
 * @extends {MetricPreviewBaseComponent}
 * @implements {OnInit}
 */
@Component({
  selector: 'app-deploy-preview',
  templateUrl: '../../../../shared/components/metric-preview/metric-preview-base.component.html',
  styleUrls: ['../../../../shared/components/metric-preview/metric-preview-base.component.scss'],
  providers: [DeployService]
})
export class DeployPreviewComponent extends MetricPreviewBaseComponent implements OnInit {

  constructor(private DeployService: DeployService,
    protected router: Router,
    public strategy: DeployPreviewStrategy) { super(); }

  ngOnInit() {
    this.dataService = this.DeployService;
    this.metric = DeployConfiguration.identifier;
    this.loadMetricSummaryData();
  }
}
