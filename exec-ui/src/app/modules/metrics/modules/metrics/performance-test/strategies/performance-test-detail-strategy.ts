import {Injectable} from '@angular/core';

import {MetricDetailModel} from '../../../shared/component-models/metric-detail-model';
import {MetricDetail} from '../../../shared/domain-models/metric-detail';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {PerformanceTestGraphStrategy} from './performance-test-graph-strategy';
import {PerformanceTestTrendStrategy} from './performance-test-trend-strategy';
import {DetailStrategyBase} from '../../../shared/strategies/detail-strategy-base';
import {PerformanceTestSegmentationStrategy} from "./performance-test-segmentation-strategy";

@Injectable()
export class PerformanceTestDetailStrategy extends DetailStrategyBase {

  constructor (private graphStrategy: PerformanceTestGraphStrategy,
               private trendStrategy: PerformanceTestTrendStrategy,
               private segmentationStrategy: PerformanceTestSegmentationStrategy) { super(); }

  public parse(model: MetricDetail): MetricDetailModel {
    const metricDetailView = new MetricDetailModel();
    metricDetailView.issues = model.summary.counts;
    metricDetailView.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.summary.lastScanned);
    metricDetailView.totalReporting = model.reportingComponents / model.totalComponents;
    metricDetailView.trend = this.trendStrategy.parse(model.summary);
    metricDetailView.graphModel = this.graphStrategy.parse(model);
    metricDetailView.isRatio = false;
    return metricDetailView;
  }
}
