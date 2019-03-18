import {MetricGraphModel} from '../../../shared/component-models/metric-graph-model';
import {MetricDetail} from '../../../shared/domain-models/metric-detail';
import {MetricTimeSeriesElement} from '../../../shared/domain-models/metric-time-series-element';
import {PerformanceTestTrendStrategy} from './performance-test-trend-strategy';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {GraphStrategyBase} from '../../../shared/strategies/graph-strategy-base';
import decimalToPercent = PresentationFunctions.decimalToPercent;
import {PerformanceTestPrimaryMetricStrategy} from './performance-test-primary-metric-strategy';
import {Injectable} from '@angular/core';
import {PerformanceTestConfiguration} from '../performance-test.configuration';

@Injectable()
export class PerformanceTestGraphStrategy extends GraphStrategyBase {

  constructor (private primaryMetricStrategy: PerformanceTestPrimaryMetricStrategy,
               private trendStrategy: PerformanceTestTrendStrategy) { super(); }

  public parse(model: MetricDetail): MetricGraphModel {
    const metricGraph = new MetricGraphModel();

    metricGraph.trend = this.trendStrategy.parse(model.summary);
    metricGraph.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.summary.lastScanned);
    metricGraph.score = this.primaryMetricStrategy.parse(model.summary.counts);
    metricGraph.values = model.timeSeries.sort((a, b) => a.daysAgo - b.daysAgo).map(this.count);
    metricGraph.isRatio = true;
    metricGraph.valueLabel = PerformanceTestConfiguration.graphHeading.toUpperCase();
    metricGraph.toolTipLabel = (x) => `${x}% passing`;

    return metricGraph;
  }

    protected count(seriesElement: MetricTimeSeriesElement): number {
        const validSet = new Set(['Error Rate']);
        return Math.round(seriesElement.counts.reduce((sum, item) => validSet.has(item.label['type']) ? sum + item.value : sum, 0));
    }
}
