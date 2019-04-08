import { MetricSummary } from '../../../shared/domain-models/metric-summary';
import { MetricTrendModel } from '../../../shared/component-models/metric-trend-model';
import { TrendStrategyBase } from '../../../shared/strategies/trend-strategy-base';
import { Injectable } from '@angular/core';

@Injectable()
export class DevopscupTrendStrategy extends TrendStrategyBase {

  public parse(model: MetricSummary): MetricTrendModel {
    const sums = model.counts.reduce((runningSums, count) => {
      if (!runningSums.has(count.label['type'])) {
        runningSums.set(count.label['type'], 0);
      }
      const newCount = runningSums.get(count.label['type']) + count.value;
      return runningSums.set(count.label['type'], newCount);
    }, new Map());

    var totalPercent = sums.get('totalPercent').toFixed(2);

    return this.trendDetails(totalPercent);
  }

  protected trendDetails(trendSlope: number): MetricTrendModel {
    const metricTrend = new MetricTrendModel();
    metricTrend.direction = this.trendDirection(trendSlope);
    metricTrend.danger = trendSlope < 0;

    return metricTrend;
  }

  protected trendDirection(trendSlope: number) {
    if (trendSlope === 0 || trendSlope === 0.00) {
      return 'neutral';
    }
    return trendSlope < 0 ? 'down' : 'up';
  }
}
