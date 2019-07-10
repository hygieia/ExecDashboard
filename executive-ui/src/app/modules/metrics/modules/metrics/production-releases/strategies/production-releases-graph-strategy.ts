import { MetricGraphModel } from '../../../shared/component-models/metric-graph-model';
import { MetricDetail } from '../../../shared/domain-models/metric-detail';
import { MetricTimeSeriesElement } from '../../../shared/domain-models/metric-time-series-element';
import { ProductionReleasesTrendStrategy } from './production-releases-trend-strategy';
import { GraphStrategyBase } from '../../../shared/strategies/graph-strategy-base';
import { PresentationFunctions } from '../../../shared/utils/presentation-functions';
import { ProductionReleasesPrimaryMetricStrategy } from './production-releases-primary-metric-strategy';
import { Injectable } from '@angular/core';
import { ProductionReleasesConfiguration } from '../production-releases.configuration';

@Injectable()
export class ProductionReleasesGraphStrategy extends GraphStrategyBase {

  constructor(private trendStrategy: ProductionReleasesTrendStrategy,
    private primaryMetricStrategy: ProductionReleasesPrimaryMetricStrategy) { super(); }

  public parse(model: MetricDetail): MetricGraphModel {
    const metricGraph = new MetricGraphModel();

    metricGraph.trend = this.trendStrategy.parse(model.summary);
    metricGraph.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.summary.lastScanned);
    metricGraph.score = this.primaryMetricStrategy.parse(model.summary.counts);
    metricGraph.values = this.getMap(model.timeSeries);
    //model.timeSeries.sort((a, b) => a.daysAgo - b.daysAgo).map(this.count);
    metricGraph.isRatio = false;
    metricGraph.valueLabel = ProductionReleasesConfiguration.graphHeading.toUpperCase();
    metricGraph.toolTipLabel = x => x === 1 ? '1 release' : x.toLocaleString() + ' releases';

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
    return timeMap;
  }

}
