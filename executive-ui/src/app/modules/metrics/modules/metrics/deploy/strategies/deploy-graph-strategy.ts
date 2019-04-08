import { MetricGraphModel } from '../../../shared/component-models/metric-graph-model';
import { MetricDetail } from '../../../shared/domain-models/metric-detail';
import { MetricTimeSeriesElement } from '../../../shared/domain-models/metric-time-series-element';
import { DeployTrendStrategy } from './deploy-trend-strategy';
import { GraphStrategyBase } from '../../../shared/strategies/graph-strategy-base';
import { PresentationFunctions } from '../../../shared/utils/presentation-functions';
import { DeployPrimaryMetricStrategy } from './deploy-primary-metric-strategy';
import { Injectable } from '@angular/core';
import { DeployConfiguration } from '../deploy.configuration';

@Injectable()
export class DeployGraphStrategy extends GraphStrategyBase {

  constructor(private trendStrategy: DeployTrendStrategy,
    private primaryMetricStrategy: DeployPrimaryMetricStrategy) { super(); }

  public parse(model: MetricDetail): MetricGraphModel {
    const metricGraph = new MetricGraphModel();

    metricGraph.trend = this.trendStrategy.parse(model.summary);
    metricGraph.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.summary.lastScanned);
    metricGraph.score = this.primaryMetricStrategy.parse(model.summary.counts);
    metricGraph.values = this.getMap(model.timeSeries);
    //model.timeSeries.sort((a, b) => a.daysAgo - b.daysAgo).map(this.count);
    metricGraph.isRatio = false;
    metricGraph.valueLabel = DeployConfiguration.graphHeading.toUpperCase();
    metricGraph.toolTipLabel = x => x === 1 ? '1 Deploy' : x.toLocaleString() + ' Deploys';

    return metricGraph;
  }

  protected count(seriesElement: MetricTimeSeriesElement): number {

    return seriesElement.counts
      .reduce((sum, item) => sum + item.value, 0);
  }

  protected getMap(timeSeries: MetricTimeSeriesElement[]): any {
    var timeMap = new Map<number, number>();
    for (let time of timeSeries) {
      var count = time.counts.reduce((sum, item) => sum + item.value, 0);
      var daysAgo = time.daysAgo;
      timeMap.set(daysAgo, count);
    }

    for (let time of timeSeries) {
      var rate = Number(time.counts[0].label['successRate'])
      var count = Number(time.counts[0].value)

      var newCount = Math.round((count * rate) / 100)

      var daysAgo = time.daysAgo + 90
      timeMap.set(daysAgo, newCount);
    }


    return timeMap;
  }
}
