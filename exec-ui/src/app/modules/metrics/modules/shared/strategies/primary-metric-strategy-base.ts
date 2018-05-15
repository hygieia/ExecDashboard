import { Strategy } from '../../../../shared/strategies/strategy';
import {Count} from '../domain-models/count';
import {MetricValueModel} from '../component-models/metric-value-model';

export abstract class PrimaryMetricStrategyBase implements Strategy<Count[], MetricValueModel> {
  public abstract parse(counts: Count[]): MetricValueModel;
}
