import {MetricGraphModel} from '../../../shared/component-models/metric-graph-model';
import {MetricDetail} from '../../../shared/domain-models/metric-detail';
import {MetricTimeSeriesElement} from '../../../shared/domain-models/metric-time-series-element';
import {StaticCodeAnalysisTrendStrategy} from './static-code-analysis-trend-strategy';
import {GraphStrategyBase} from '../../../shared/strategies/graph-strategy-base';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {StaticCodeAnalysisPrimaryMetricStrategy} from './static-code-analysis-primary-metric-strategy';
import {Injectable} from '@angular/core';
import {StaticCodeAnalysisConfiguration} from '../static-code-analysis.configuration';

@Injectable()
export class StaticCodeAnalysisGraphStrategy extends GraphStrategyBase {

  constructor (private trendStrategy: StaticCodeAnalysisTrendStrategy,
               private primaryMetricStrategy: StaticCodeAnalysisPrimaryMetricStrategy) { super(); }

  public parse(model: MetricDetail): MetricGraphModel {
    const metricGraph = new MetricGraphModel();

    metricGraph.trend = this.trendStrategy.parse(model.summary);
    metricGraph.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.summary.lastScanned);

    metricGraph.score = this.primaryMetricStrategy.parse(model.summary.counts);
    metricGraph.values = model.timeSeries.sort((a, b) => a.daysAgo - b.daysAgo).map(this.count);
    metricGraph.isRatio = false;
    metricGraph.valueLabel = StaticCodeAnalysisConfiguration.graphHeading.toUpperCase();
    metricGraph.toolTipLabel = x => x === 1 ? '1 issue' : x.toLocaleString() + ' issues';

    return metricGraph;
  }

  protected count(seriesElement: MetricTimeSeriesElement): number {
    const validSet = new Set(['blocker', 'critical', 'major']);
    return seriesElement.counts.reduce((sum, item) => validSet.has(item.label['type']) ? sum + item.value : sum, 0);
  }
}
