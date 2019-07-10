import { MetricPreviewModel } from '../../../shared/component-models/metric-preview-model';
import { MetricSummary } from '../../../shared/domain-models/metric-summary';
import { PresentationFunctions } from '../../../shared/utils/presentation-functions';
import { CodeRepoPrimaryMetricStrategy } from './code-repo-primary-metric-strategy';
import { CodeRepoTrendStrategy } from './code-repo-trend-strategy';
import { PreviewStrategyBase } from '../../../shared/strategies/preview-strategy-base';
import { Injectable } from '@angular/core';
import { CodeRepoConfiguration } from '../code-repo.configuration';

@Injectable()
export class CodeRepoPreviewStrategy extends PreviewStrategyBase {

  constructor(private primaryMetricStrategy: CodeRepoPrimaryMetricStrategy,
    private trendStrategy: CodeRepoTrendStrategy) { super(); }

  public parse(model: MetricSummary): MetricPreviewModel {
    const metricPreview = new MetricPreviewModel();
    metricPreview.description = CodeRepoConfiguration.description;
    metricPreview.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.lastScanned);
    metricPreview.id = CodeRepoConfiguration.identifier;
    metricPreview.score = mapPrimary(this.primaryMetricStrategy.parse(model.counts));
    metricPreview.totalReporting = model.reportingComponents / model.totalComponents;
    metricPreview.trend = this.trendStrategy.parse(model);
    metricPreview.secondaryMetrics = this.calculateSecondaryMetric(model);
    metricPreview.available = model.dataAvailable == undefined ? true : model.dataAvailable;
    metricPreview.message = 'NA';
    metricPreview.dataSource = CodeRepoConfiguration.dataSource;
    return metricPreview;

    function mapPrimary(valueModel) {
      return {
        name: CodeRepoConfiguration.previewHeading,
        value: formattedData(valueModel.value),
        prefix: '~',
        unit: getUnit(valueModel.value),
        unitKey: 'Commits Per Day'
      };
    }

    function formattedData(value) {
      if (value >= 1000) {
        return Math.round(value / 1000);
      }
      return Math.round(value);
    }

    function getUnit(value) {
      if (value >= 1000) {
        return 'k';
      }
      return '';
    }
  }

  private calculateSecondaryMetric(model: MetricSummary) {
    const sums = model.counts.reduce((runningSums, count) => {
      if (!runningSums.has(count.label['type'])) {
        runningSums.set(count.label['type'], 0);
      }
      const newCount = runningSums.get(count.label['type']) + count.value;
      return runningSums.set(count.label['type'], newCount);
    }, new Map());


    var totalCommits = sums.get('totalCommits');
    if (totalCommits >= 1000) {
      totalCommits = Math.round(totalCommits / 1000) + ' k';
    }

    var uniqueContributors = sums.get('uniqueContributors');
    if (uniqueContributors >= 1000) {
      uniqueContributors = Math.round(uniqueContributors / 1000) + ' k';
    }

    const UniqueContributorsDisplay = [{
      name: 'Unique Contributors',
      value: uniqueContributors
    }];

    return [{ name: 'Total Commits', value: totalCommits }, ...UniqueContributorsDisplay];
  }
}
