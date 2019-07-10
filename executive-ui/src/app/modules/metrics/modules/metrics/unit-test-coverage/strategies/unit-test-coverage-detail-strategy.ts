import {MetricDetailModel} from '../../../shared/component-models/metric-detail-model';
import {MetricDetail} from '../../../shared/domain-models/metric-detail';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {UnitTestCoverageGraphStrategy} from './unit-test-coverage-graph-strategy';
import {UnitTestCoverageTrendStrategy} from './unit-test-coverage-trend-strategy';
import {DetailStrategyBase} from '../../../shared/strategies/detail-strategy-base';
import {Injectable} from '@angular/core';

@Injectable()
export class UnitTestCoverageDetailStrategy extends DetailStrategyBase {

  constructor (private graphStrategy: UnitTestCoverageGraphStrategy,
               private trendStrategy: UnitTestCoverageTrendStrategy) { super(); }

  public parse(model: MetricDetail): MetricDetailModel {
    const metricDetailView = new MetricDetailModel();
    metricDetailView.issues = model.summary.counts;
    metricDetailView.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.summary.lastScanned);
    metricDetailView.totalReporting = model.summary.reportingComponents / model.summary.totalComponents;
    metricDetailView.trend = this.trendStrategy.parse(model.summary);
    metricDetailView.graphModel = this.graphStrategy.parse(model);
    metricDetailView.segmentationModel = null;
    metricDetailView.isRatio = true;
    return metricDetailView;
  }
}
