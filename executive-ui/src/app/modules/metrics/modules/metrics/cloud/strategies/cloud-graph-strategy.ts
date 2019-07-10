import { MetricGraphModel } from '../../../shared/component-models/metric-graph-model';
import { MetricDetail } from '../../../shared/domain-models/metric-detail';
import { MetricTimeSeriesElement } from '../../../shared/domain-models/metric-time-series-element';
import { CloudTrendStrategy } from './cloud-trend-strategy';
import { GraphStrategyBase } from '../../../shared/strategies/graph-strategy-base';
import { PresentationFunctions } from '../../../shared/utils/presentation-functions';
import { CloudPrimaryMetricStrategy } from './cloud-primary-metric-strategy';
import { Injectable } from '@angular/core';
import { CloudConfiguration } from '../cloud.configuration';

@Injectable()
export class CloudGraphStrategy extends GraphStrategyBase {

  constructor(private trendStrategy: CloudTrendStrategy,
    private primaryMetricStrategy: CloudPrimaryMetricStrategy) { super(); }

  public parse(model: MetricDetail): MetricGraphModel {
    const metricGraph = new MetricGraphModel();

    metricGraph.trend = this.trendStrategy.parse(model.summary);
    metricGraph.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.summary.lastScanned);
    metricGraph.score = this.primaryMetricStrategy.parse(model.summary.counts);
    metricGraph.values = this.getMap(model.timeSeries);
    //model.timeSeries.sort((a, b) => a.daysAgo - b.daysAgo).map(this.count);
    metricGraph.isRatio = false;
    metricGraph.valueLabel = CloudConfiguration.graphHeading.toUpperCase();
    metricGraph.toolTipLabel = x => x === 1 ? '1 $' : x.toLocaleString() + '$';

    return metricGraph;
  }

  protected count(seriesElement: MetricTimeSeriesElement): number {
    return seriesElement.counts
      .filter(i => i.label['type'] === 'cost')
      .map(c => c.value)
      .reduce((a, b) => a + b, 0);
  }

  protected getMap(timeSeries: MetricTimeSeriesElement[]): any {
    var timeMap = new Map<number, number>();
    for (let time of timeSeries) {
      var count = time.counts
        .filter(i => i.label['type'] === 'cost')
        .map(c => c.value)
        .reduce((a, b) => a + b, 0);
      var daysAgo = time.daysAgo;
      timeMap.set(daysAgo, count);
    }
    return timeMap;
  }
}


