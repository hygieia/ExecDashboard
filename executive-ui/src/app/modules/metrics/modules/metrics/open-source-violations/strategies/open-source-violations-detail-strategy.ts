import { MetricSummary } from '../../../shared/domain-models/metric-summary';
import { MetricDetailModel } from '../../../shared/component-models/metric-detail-model';
import { MetricDetail } from '../../../shared/domain-models/metric-detail';
import { PresentationFunctions } from '../../../shared/utils/presentation-functions';
import { OpenSourceViolationsGraphStrategy } from './open-source-violations-graph-strategy';
import { OpenSourceViolationsSegmentationStrategy } from './open-source-violations-segmentation-strategy';
import { OpenSourceViolationsTrendStrategy } from './open-source-violations-trend-strategy';
import { DetailStrategyBase } from '../../../shared/strategies/detail-strategy-base';
import { Injectable } from '@angular/core';

@Injectable()
export class OpenSourceViolationsDetailStrategy extends DetailStrategyBase {

  constructor(
    private graphStrategy: OpenSourceViolationsGraphStrategy,
    private trendStrategy: OpenSourceViolationsTrendStrategy,
    private segmentationStrategy: OpenSourceViolationsSegmentationStrategy) { super(); }

  public parse(model: MetricDetail): MetricDetailModel {
    const metricDetailView = new MetricDetailModel();
    metricDetailView.issues = model.summary.counts;
    metricDetailView.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.summary.lastScanned);
    metricDetailView.totalReporting = model.summary.reportingComponents / model.summary.totalComponents;
    metricDetailView.trend = this.trendStrategy.parse(model.summary);
    metricDetailView.graphModel = this.graphStrategy.parse(model);
    metricDetailView.segmentationModel = this.segmentationStrategy.parse(model.summary);
    //  metricDetailView.secondaryFigureModel = this.secondaryFigureModel(model.summary);
    return Object.assign(metricDetailView);
  }

}
