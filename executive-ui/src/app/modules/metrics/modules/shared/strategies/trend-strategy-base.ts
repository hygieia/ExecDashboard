import {Strategy} from '../../../../shared/strategies/strategy';
import {MetricSummary} from '../domain-models/metric-summary';
import {MetricTrendModel} from '../component-models/metric-trend-model';

export abstract class TrendStrategyBase implements Strategy<MetricSummary, MetricTrendModel> {
  abstract parse(model: MetricSummary): MetricTrendModel;

  protected abstract trendDetails(trendSlope: number): MetricTrendModel;

  protected abstract trendDirection(trendSlope: number): string;
}
