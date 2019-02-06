import {MetricGraphModel} from '../../../shared/component-models/metric-graph-model';
import {MetricDetail} from '../../../shared/domain-models/metric-detail';
import {MetricTimeSeriesElement} from '../../../shared/domain-models/metric-time-series-element';
import {TraceabilityTrendStrategy} from './traceability-trend-strategy';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {GraphStrategyBase} from '../../../shared/strategies/graph-strategy-base';
import decimalToPercent = PresentationFunctions.decimalToPercent;
import {TraceabilityPrimaryMetricStrategy} from './traceability-primary-metric-strategy';
import {Injectable} from '@angular/core';
import {TraceabilityConfiguration} from '../traceability.configuration';

@Injectable()
export class TraceabilityGraphStrategy extends GraphStrategyBase {

  constructor (private primaryMetricStrategy: TraceabilityPrimaryMetricStrategy,
               private trendStrategy: TraceabilityTrendStrategy) { super(); }

  public parse(model: MetricDetail): MetricGraphModel {
    const metricGraph = new MetricGraphModel();

    metricGraph.trend = this.trendStrategy.parse(model.summary);
    metricGraph.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.summary.lastScanned);
    metricGraph.score = this.primaryMetricStrategy.parse(model.summary.counts);
    metricGraph.values = model.timeSeries.sort((a, b) => a.daysAgo - b.daysAgo).map(this.count);
    metricGraph.isRatio = true;
    metricGraph.valueLabel = TraceabilityConfiguration.graphHeading.toUpperCase();
    metricGraph.toolTipLabel = (x) => `${x}% traceability`;

    return metricGraph;
  }

    protected count(seriesElement: MetricTimeSeriesElement): number {
        const validSet = new Set(['Automated', 'Manual']);

        return Math.round(seriesElement.counts.reduce((sum, item) => validSet.has(item.label['type']) ? sum + item.value : sum, 0)/2);
    }
}
