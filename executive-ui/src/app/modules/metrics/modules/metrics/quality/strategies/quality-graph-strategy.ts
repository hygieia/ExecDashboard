import { MetricGraphModel } from '../../../shared/component-models/metric-graph-model';
import { MetricDetail } from '../../../shared/domain-models/metric-detail';
import { MetricTimeSeriesElement } from '../../../shared/domain-models/metric-time-series-element';
import { QualityTrendStrategy } from './quality-trend-strategy';
import { GraphStrategyBase } from '../../../shared/strategies/graph-strategy-base';
import { PresentationFunctions } from '../../../shared/utils/presentation-functions';
import { QualityPrimaryMetricStrategy } from './quality-primary-metric-strategy';
import { Injectable } from '@angular/core';
import { QualityConfiguration } from '../quality.configuration';

@Injectable()
export class QualityGraphStrategy extends GraphStrategyBase {

  constructor(private primaryMetricStrategy: QualityPrimaryMetricStrategy,
    private trendStrategy: QualityTrendStrategy) { super(); }

  public parse(model: MetricDetail): MetricGraphModel {
    const metricGraph = new MetricGraphModel();

    metricGraph.trend = this.trendStrategy.parse(model.summary);
    metricGraph.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.summary.lastScanned);

    metricGraph.score = this.primaryMetricStrategy.parse(model.summary.counts);
    metricGraph.values = this.getMap(model.timeSeries);
    //model.timeSeries.sort().map(this.count);
    metricGraph.isRatio = false;
    metricGraph.valueLabel = QualityConfiguration.graphHeading.toUpperCase();
    metricGraph.toolTipLabel = x => x === 1 ? '1 Defect' : x.toLocaleString() + ' Defects';

    return metricGraph;
  }

  protected getMap(timeSeries: MetricTimeSeriesElement[]): any {
    const validSet = new Set(['normal', 'blocker', 'high', 'low', 'cmis', 'serviceNow']);
    var timeMap = new Map<number, number>();
    for (let time of timeSeries) {
      var count = time.counts.reduce((sum, item) => validSet.has(item.label['priority']) ? sum + item.value : sum, 0);
      var daysAgo = time.daysAgo;
      timeMap.set(daysAgo, count);
    }
    return timeMap;
  }

  protected count(seriesElement: MetricTimeSeriesElement): number {
    const validSet = new Set(['normal', 'blocker', 'high', 'low', 'cmis', 'serviceNow']);
    return seriesElement.counts
      .reduce((sum, item) => validSet.has(item.label['priority']) ? sum + item.value : sum, 0);
  }
}
