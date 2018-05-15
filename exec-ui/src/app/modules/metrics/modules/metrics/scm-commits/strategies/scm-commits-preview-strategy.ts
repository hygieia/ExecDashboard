import {MetricPreviewModel} from '../../../shared/component-models/metric-preview-model';
import {MetricSummary} from '../../../shared/domain-models/metric-summary';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {SCMCommitsPrimaryMetricStrategy} from './scm-commits-primary-metric-strategy';
import {SCMCommitsTrendStrategy} from './scm-commits-trend-strategy';
import {PreviewStrategyBase} from '../../../shared/strategies/preview-strategy-base';
import {Injectable} from '@angular/core';
import {SCMCommitsConfiguration} from '../scm-commits.configuration';
import {MetricDetail} from '../../../shared/domain-models/metric-detail';

@Injectable()
export class SCMCommitsPreviewStrategy extends PreviewStrategyBase {

  constructor (private primaryMetricStrategy: SCMCommitsPrimaryMetricStrategy,
               private trendStrategy: SCMCommitsTrendStrategy) { super(); }

  public parse(model: MetricDetail): MetricPreviewModel {

    const metricPreview = new MetricPreviewModel();
    metricPreview.description = SCMCommitsConfiguration.description;
    metricPreview.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.summary.lastScanned);
    metricPreview.id = SCMCommitsConfiguration.identifier;
    metricPreview.score = mapPrimary(this.primaryMetricStrategy.parse(model.summary.counts));
    metricPreview.totalReporting = model.reportingComponents / model.totalComponents;
    metricPreview.trend = this.trendStrategy.parse(model.summary);
    metricPreview.secondaryMetrics = [];
    return metricPreview;

    function mapPrimary(valueModel) {
      return {
        name: SCMCommitsConfiguration.previewHeading,
        value: valueModel.value
      };
    }
  }
}
