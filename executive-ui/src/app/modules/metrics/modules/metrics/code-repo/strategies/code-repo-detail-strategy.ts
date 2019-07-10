import { MetricDetailModel } from '../../../shared/component-models/metric-detail-model';
import { MetricDetail } from '../../../shared/domain-models/metric-detail';
import { PresentationFunctions } from '../../../shared/utils/presentation-functions';
import { CodeRepoGraphStrategy } from './code-repo-graph-strategy';
import { CodeRepoSegmentationStrategy } from './code-repo-segmentation-strategy';
import { CodeRepoTrendStrategy } from './code-repo-trend-strategy';
import { DetailStrategyBase } from '../../../shared/strategies/detail-strategy-base';
import { Injectable } from '@angular/core';


@Injectable()
export class CodeRepoDetailStrategy extends DetailStrategyBase {

  constructor(private graphStrategy: CodeRepoGraphStrategy,
    private trendStrategy: CodeRepoTrendStrategy,
    private segmentationStrategy: CodeRepoSegmentationStrategy) { super(); }

  public parse(model: MetricDetail): MetricDetailModel {
    const metricDetailView = new MetricDetailModel();
    metricDetailView.issues = model.summary.counts;
    metricDetailView.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.summary.lastScanned);
    metricDetailView.totalReporting = model.summary.reportingComponents / model.summary.totalComponents;
    metricDetailView.trend = this.trendStrategy.parse(model.summary);
    metricDetailView.graphModel = this.graphStrategy.parse(model);
    metricDetailView.segmentationModel = this.segmentationStrategy.parse(model.summary);
    return Object.assign(metricDetailView);
  }
}
