import {ThroughputPreviewStrategy} from '../../strategies/throughput-preview-strategy';
import {MetricPreviewBaseComponent} from '../../../../shared/components/metric-preview/metric-preview-base.component';
import {Component, OnInit} from '@angular/core';
import {ThroughputService} from '../../services/throughput.service';
import {Router} from '@angular/router';
import {ThroughputConfiguration} from '../../throughput.configuration';

/**
 * @export
 * @class ThroughputPreviewComponent
 * @extends {MetricPreviewBaseComponent}
 * @implements {OnInit}
 */
@Component({
  selector: 'app-throughput-preview',
  templateUrl: '../../../../shared/components/metric-preview/metric-preview-base.component.html',
  styleUrls: ['../../../../shared/components/metric-preview/metric-preview-base.component.scss'],
  providers: [ThroughputService]
})
export class ThroughputPreviewComponent extends MetricPreviewBaseComponent implements OnInit {

  constructor(private throughputService: ThroughputService,
              protected router: Router,
              public strategy: ThroughputPreviewStrategy) { super(); }

  ngOnInit() {
    this.dataService = this.throughputService;
    this.metric = ThroughputConfiguration.identifier;
    this.loadMetricSummaryData();
  }
}
