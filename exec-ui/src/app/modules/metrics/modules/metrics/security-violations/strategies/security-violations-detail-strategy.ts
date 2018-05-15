import {MetricDetailModel} from '../../../shared/component-models/metric-detail-model';
import {MetricDetail} from '../../../shared/domain-models/metric-detail';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {SecurityViolationsTrendStrategy} from './security-violations-trend-strategy';
import {SecurityViolationsGraphStrategy} from './security-violations-graph-strategy';
import {SecurityViolationsSegmentationStrategy} from './security-violations-segmentation-strategy';
import {DetailStrategyBase} from '../../../shared/strategies/detail-strategy-base';
import {Injectable} from '@angular/core';

@Injectable()
export class SecurityViolationsDetailStrategy extends DetailStrategyBase {

  constructor (private graphStrategy: SecurityViolationsGraphStrategy,
               private trendStrategy: SecurityViolationsTrendStrategy,
               private segmentationStrategy: SecurityViolationsSegmentationStrategy) { super(); }

  public parse(model: MetricDetail): MetricDetailModel {
    const metricDetailView = new MetricDetailModel();
    metricDetailView.issues = model.summary.counts;
    metricDetailView.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.summary.lastScanned);
    metricDetailView.totalReporting = model.reportingComponents / model.totalComponents;
    metricDetailView.trend = this.trendStrategy.parse(model.summary);
    metricDetailView.graphModel = this.graphStrategy.parse(model);
    metricDetailView.segmentationModel = this.segmentationStrategy.parse(model.summary);
    return Object.assign(metricDetailView);
  }
}
