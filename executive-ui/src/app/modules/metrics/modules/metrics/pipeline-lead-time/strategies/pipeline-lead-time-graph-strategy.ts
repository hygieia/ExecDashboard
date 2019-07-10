import { MetricGraphModel } from '../../../shared/component-models/metric-graph-model';
import { MetricDetail } from '../../../shared/domain-models/metric-detail';
import { MetricTimeSeriesElement } from '../../../shared/domain-models/metric-time-series-element';
import { PipelineLeadTimeTrendStrategy } from './pipeline-lead-time-trend-strategy';
import { PresentationFunctions } from '../../../shared/utils/presentation-functions';
import { GraphStrategyBase } from '../../../shared/strategies/graph-strategy-base';
import { PipelineLeadTimePrimaryMetricStrategy } from './pipeline-lead-time-primary-metric-strategy';
import { Injectable } from '@angular/core';
import { PipelineLeadTimeConfiguration } from '../pipeline-lead-time.configuration';

@Injectable()
export class PipelineLeadTimeGraphStrategy extends GraphStrategyBase {

  constructor(private primaryMetricStrategy: PipelineLeadTimePrimaryMetricStrategy,
    private trendStrategy: PipelineLeadTimeTrendStrategy) { super(); }

  public parse(model: MetricDetail): MetricGraphModel {
    const metricGraph = new MetricGraphModel();

    metricGraph.trend = this.trendStrategy.parse(model.summary);
    metricGraph.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.summary.lastScanned);
    metricGraph.score = mapPrimary(this.primaryMetricStrategy.parse(model.summary.counts));
    metricGraph.values = this.getMap(model.timeSeries);
    //model.timeSeries.sort((a, b) => a.daysAgo - b.daysAgo).map(this.count);
    metricGraph.isRatio = false;
    metricGraph.valueLabel = PipelineLeadTimeConfiguration.graphHeading.toUpperCase();
    metricGraph.toolTipLabel = (x) => x === 1 ? '~1 day' : `~${x} days`;

    return metricGraph;

    function mapPrimary(valueModel) {
      return {
        name: valueModel.name,
        value: valueModel.value,
        unit: unit(valueModel),
        prefix: valueModel.prefix
      };
    }

    function unit(valueModel) {
      switch (valueModel.unit) {
        case 'days': return 'd';
        case 'hours': return valueModel.value === 1 ? 'hr' : 'hrs';
        case 'minutes': return 'm';
      }
    }
  }

  protected count(seriesElement: MetricTimeSeriesElement): number {
    return Math.round(seriesElement.counts[0].value / 1000 / 60 / 60 / 24);
  }

  protected getMap(timeSeries: MetricTimeSeriesElement[]): any {
    var timeMap = new Map<number, number>();
    for (let time of timeSeries) {
      var count = Math.round((time.counts[0].value / 1000 / 60 / 60 / 24) * 100) / 100;
      var daysAgo = time.daysAgo;
      timeMap.set(daysAgo, count);
    }
    return timeMap;
  }

}
