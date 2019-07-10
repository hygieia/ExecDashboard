import { MetricDetailModel } from '../../../shared/component-models/metric-detail-model';
import { MetricDetail } from '../../../shared/domain-models/metric-detail';
import { PresentationFunctions } from '../../../shared/utils/presentation-functions';
import { QualityTrendStrategy } from './quality-trend-strategy';
import { QualityGraphStrategy } from './quality-graph-strategy';
import { QualitySegmentationStrategy } from './quality-segmentation-strategy';
import { DetailStrategyBase } from '../../../shared/strategies/detail-strategy-base';
import { Injectable } from '@angular/core';

@Injectable()
export class QualityDetailStrategy extends DetailStrategyBase {

  constructor(private graphStrategy: QualityGraphStrategy,
    private trendStrategy: QualityTrendStrategy,
    private segmentationStrategy: QualitySegmentationStrategy) { super(); }

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
