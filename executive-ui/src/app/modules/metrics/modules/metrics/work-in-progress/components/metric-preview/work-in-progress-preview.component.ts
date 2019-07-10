import {WorkInProgressPreviewStrategy} from '../../strategies/work-in-progress-preview-strategy';
import {MetricPreviewBaseComponent} from '../../../../shared/components/metric-preview/metric-preview-base.component';
import {Component, OnInit} from '@angular/core';
import {WorkInProgressService} from '../../services/work-in-progress.service';
import {Router} from '@angular/router';
import {WorkInProgressConfiguration} from '../../work-in-progress.configuration';

/**
 * @export
 * @class WorkInProgressPreviewComponent
 * @extends {MetricPreviewBaseComponent}
 * @implements {OnInit}
 */
@Component({
  selector: 'app-work-in-progress-preview',
  templateUrl: '../../../../shared/components/metric-preview/metric-preview-base.component.html',
  styleUrls: ['../../../../shared/components/metric-preview/metric-preview-base.component.scss'],
  providers: [WorkInProgressService]
})
export class WorkInProgressPreviewComponent extends MetricPreviewBaseComponent implements OnInit {

  constructor(private WorkInProgressService: WorkInProgressService,
              protected router: Router,
              public strategy: WorkInProgressPreviewStrategy) { super(); }

  ngOnInit() {
    this.dataService = this.WorkInProgressService;
    this.metric = WorkInProgressConfiguration.identifier;
    this.loadMetricSummaryData();
  }
}
