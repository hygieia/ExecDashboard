import {MetricDetailModel} from '../../../shared/component-models/metric-detail-model';
import {MetricDetail} from '../../../shared/domain-models/metric-detail';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {PipelineLeadTimeGraphStrategy} from './pipeline-lead-time-graph-strategy';
import {PipelineLeadTimeTrendStrategy} from './pipeline-lead-time-trend-strategy';
import {DetailStrategyBase} from '../../../shared/strategies/detail-strategy-base';
import {Injectable} from '@angular/core';

@Injectable()
export class PipelineLeadTimeDetailStrategy extends DetailStrategyBase {

  constructor (private graphStrategy: PipelineLeadTimeGraphStrategy,
               private trendStrategy: PipelineLeadTimeTrendStrategy) { super(); }

  public parse(model: MetricDetail): MetricDetailModel {
    const metricDetailView = new MetricDetailModel();
    metricDetailView.issues = model.summary.counts;
    metricDetailView.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.summary.lastScanned);
    metricDetailView.totalReporting = model.reportingComponents / model.totalComponents;
    metricDetailView.trend = this.trendStrategy.parse(model.summary);
    metricDetailView.graphModel = this.graphStrategy.parse(model);
    metricDetailView.segmentationModel = null;
    metricDetailView.isRatio = true;
    return metricDetailView;
  }
}
