import {MetricPreviewModel} from '../../../shared/component-models/metric-preview-model';
import {MetricSummary} from '../../../shared/domain-models/metric-summary';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {StaticCodeAnalysisPrimaryMetricStrategy} from './static-code-analysis-primary-metric-strategy';
import {StaticCodeAnalysisTrendStrategy} from './static-code-analysis-trend-strategy';
import {PreviewStrategyBase} from '../../../shared/strategies/preview-strategy-base';
import {StaticCodeAnalysisAuxiliaryFigureStrategy} from './static-code-analysis-auxiliary-figure-strategy';
import {Injectable} from '@angular/core';
import {StaticCodeAnalysisConfiguration} from '../static-code-analysis.configuration';

@Injectable()
export class StaticCodeAnalysisPreviewStrategy extends PreviewStrategyBase {

  constructor (private primaryMetricStrategy: StaticCodeAnalysisPrimaryMetricStrategy,
               private trendStrategy: StaticCodeAnalysisTrendStrategy,
               private auxiliaryFigureStrategy: StaticCodeAnalysisAuxiliaryFigureStrategy) { super(); }

  public parse(model: MetricSummary): MetricPreviewModel {
    const metricPreview = new MetricPreviewModel();
    metricPreview.description = StaticCodeAnalysisConfiguration.description;
    metricPreview.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.lastScanned);
    metricPreview.id = StaticCodeAnalysisConfiguration.identifier;
    metricPreview.score = mapPrimary(this.primaryMetricStrategy.parse(model.counts));
    metricPreview.totalReporting = model.reportingComponents / model.totalComponents;
    metricPreview.trend = this.trendStrategy.parse(model);
    metricPreview.secondaryMetrics = this.calculateSecondaryMetric(model);
    return metricPreview;

    function mapPrimary(valueModel) {
      return {
        name: StaticCodeAnalysisConfiguration.previewHeading,
        value: valueModel.value
      };
    }
  }

  private calculateSecondaryMetric(model: MetricSummary) {
    const sums = model.counts
      .filter(count => count.label['type'] === 'issue')
      .reduce((runningSums, count) => {
        if (!runningSums.has(count.label['severity'])) {
          runningSums.set(count.label['severity'], 0);
        }
        const newCount = runningSums.get(count.label['severity']) + count.value;
        return runningSums.set(count.label['severity'], newCount);
      }, new Map());

    const technicalDebt = this.auxiliaryFigureStrategy.parse(model);

    const technicalDebtInDays = !technicalDebt.hasData ? [] : [{
      name: 'technical debt',
      value: technicalDebt.value,
      unit: 'd'
    }];

    if (sums.get('blocker')) {
      return [{name: 'blocker', value: sums.get('blocker').toLocaleString()}, ...technicalDebtInDays];
    }

    if (sums.get('critical')) {
      return [{name: 'critical', value: sums.get('critical').toLocaleString()}, ...technicalDebtInDays];
    }

    if (sums.get('major')) {
      return [{name: 'major', value: sums.get('major').toLocaleString()}, ...technicalDebtInDays];
    }

    return [...technicalDebtInDays];
  }
}
