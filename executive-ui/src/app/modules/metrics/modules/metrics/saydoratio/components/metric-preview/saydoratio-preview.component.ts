import {MetricPreviewBaseComponent} from '../../../../shared/components/metric-preview/metric-preview-base.component';
import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {SayDoRatioService} from '../../services/saydoratio.service';
import {SayDoRatioPreviewStrategy} from '../../strategies/saydoratio-preview-strategy';
import {SayDoRatioConfiguration} from '../../saydoratio.configuration';

/**
 * @export
 * @class SayDoRatioPreviewComponent
 * @extends {MetricPreviewBaseComponent}
 * @implements {OnInit}
 */
@Component({
  selector: 'app-saydoratio-preview',
  templateUrl: '../../../../shared/components/metric-preview/metric-preview-base.component.html',
  styleUrls: ['../../../../shared/components/metric-preview/metric-preview-base.component.scss'],
  providers: [SayDoRatioService]
})
export class SayDoRatioPreviewComponent extends MetricPreviewBaseComponent implements OnInit {

  constructor(private SayDoRatioService: SayDoRatioService,
              protected router: Router,
              public strategy: SayDoRatioPreviewStrategy) { super(); }

  ngOnInit() {
    this.dataService = this.SayDoRatioService;
    this.metric = SayDoRatioConfiguration.identifier;
    this.loadMetricSummaryData();
  }
}
