import {MetricSummary} from '../../../shared/domain-models/metric-summary';
import {MetricDetailModel} from '../../../shared/component-models/metric-detail-model';
import {MetricDetail} from '../../../shared/domain-models/metric-detail';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {ThroughputGraphStrategy} from './throughput-graph-strategy';
import {ThroughputSegmentationStrategy} from './throughput-segmentation-strategy';
import {ThroughputTrendStrategy} from './throughput-trend-strategy';
import {DetailStrategyBase} from '../../../shared/strategies/detail-strategy-base';
import {Injectable} from '@angular/core';

@Injectable()
export class ThroughputDetailStrategy extends DetailStrategyBase {

  constructor (
               private graphStrategy: ThroughputGraphStrategy,
               private trendStrategy: ThroughputTrendStrategy,
               private segmentationStrategy: ThroughputSegmentationStrategy) { super(); }

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
