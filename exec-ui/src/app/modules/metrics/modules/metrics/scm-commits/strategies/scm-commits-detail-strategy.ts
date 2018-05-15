import {MetricDetailModel} from '../../../shared/component-models/metric-detail-model';
import {MetricDetail} from '../../../shared/domain-models/metric-detail';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {SCMCommitsTrendStrategy} from './scm-commits-trend-strategy';
import {SCMCommitsGraphStrategy} from './scm-commits-graph-strategy';
import {DetailStrategyBase} from '../../../shared/strategies/detail-strategy-base';
import {Injectable} from '@angular/core';

@Injectable()
export class SCMCommitsDetailStrategy extends DetailStrategyBase {

  constructor (private trendStrategy: SCMCommitsTrendStrategy,
               private graphStrategy: SCMCommitsGraphStrategy) { super(); }

  public parse(model: MetricDetail): MetricDetailModel {
    const metricDetailView = new MetricDetailModel();
    metricDetailView.issues = model.summary.counts;
    metricDetailView.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.summary.lastScanned);

    metricDetailView.totalReporting = model.reportingComponents / model.totalComponents;

    metricDetailView.trend = this.trendStrategy.parse(model.summary);
    metricDetailView.graphModel = this.graphStrategy.parse(model);

    return Object.assign(metricDetailView);
  }
}
