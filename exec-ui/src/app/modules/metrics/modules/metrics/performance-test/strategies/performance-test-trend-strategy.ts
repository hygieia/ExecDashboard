import {MetricSummary} from '../../../shared/domain-models/metric-summary';
import {MetricTrendModel} from '../../../shared/component-models/metric-trend-model';
import {TrendStrategyBase} from '../../../shared/strategies/trend-strategy-base';
import {Injectable} from '@angular/core';

@Injectable()
export class PerformanceTestTrendStrategy extends TrendStrategyBase {

  public parse(model: MetricSummary): MetricTrendModel {
    return this.trendDetails(model.trendSlope);
  }

  protected trendDetails(trendSlope: number): MetricTrendModel {
    const metricTrend = new MetricTrendModel();
    metricTrend.direction = this.trendDirection(trendSlope);
    metricTrend.danger = trendSlope < 0;

    return metricTrend;
  }

  protected trendDirection(trendSlope: number) {
    if (trendSlope === 0) {
      return 'neutral';
    }

    return trendSlope > 0 ? 'up' : 'down';
  }
}
