import {MetricPreviewBaseComponent} from '../../../../shared/components/metric-preview/metric-preview-base.component';
import {Component, OnInit} from '@angular/core';
import {PipelineLeadTimeService} from '../../services/pipeline-lead-time.service';
import {Router} from '@angular/router';
import {PipelineLeadTimePreviewStrategy} from '../../strategies/pipeline-lead-time-preview-strategy';
import {PipelineLeadTimeConfiguration} from '../../pipeline-lead-time.configuration';

/**
 * @export
 * @class PipelineLeadTimePreviewComponent
 * @extends {MetricPreviewBaseComponent}
 * @implements {OnInit}
 */
@Component({
  selector: 'app-pipeline-lead-time-card',
  templateUrl: '../../../../shared/components/metric-preview/metric-preview-base.component.html',
  styleUrls: ['../../../../shared/components/metric-preview/metric-preview-base.component.scss'],
  providers: [PipelineLeadTimeService]
})
export class PipelineLeadTimePreviewComponent extends MetricPreviewBaseComponent implements OnInit {

  constructor(private pipelineLeadTimeService: PipelineLeadTimeService,
              protected router: Router,
              public strategy: PipelineLeadTimePreviewStrategy) { super(); }

  ngOnInit() {
    this.dataService = this.pipelineLeadTimeService;
    this.metric = PipelineLeadTimeConfiguration.identifier;
    this.loadMetricSummaryData();
  }
}
