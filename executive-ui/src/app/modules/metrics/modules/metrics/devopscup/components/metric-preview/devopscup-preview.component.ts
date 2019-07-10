import { MetricPreviewBaseComponent } from '../../../../shared/components/metric-preview/metric-preview-base.component';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DevopscupService } from '../../services/devopscup.service';
import { DevopscupPreviewStrategy } from '../../strategies/devopscup-preview-strategy';
import { DevopsCupConfiguration } from '../../devopscup.configuration';


/**
 * @export
 * @class DevopscupPreviewComponent
 * @extends {MetricPreviewBaseComponent}
 * @implements {OnInit}
 */
@Component({
  selector: 'app-devopscup-preview',
  templateUrl: '../../../../shared/components/metric-preview/metric-preview-base.component.html',
  styleUrls: ['../../../../shared/components/metric-preview/metric-preview-base.component.scss'],
  providers: [DevopscupService]
})
export class DevopscupPreviewComponent extends MetricPreviewBaseComponent implements OnInit {

  constructor(private DevopscupService: DevopscupService,
    protected router: Router,
    public strategy: DevopscupPreviewStrategy) {
    super();
  }
  ngOnInit() {
    this.dataService = this.DevopscupService;
    this.metric = DevopsCupConfiguration.identifier;
    this.loadMetricSummaryData();
  }

}
