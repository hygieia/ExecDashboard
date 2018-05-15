import {MetricDetailModel} from '../component-models/metric-detail-model';
import {MetricDetail} from '../domain-models/metric-detail';
import {Strategy} from '../../../../shared/strategies/strategy';

export abstract class DetailStrategyBase implements Strategy<MetricDetail, MetricDetailModel> {
  public abstract parse(model: MetricDetail): MetricDetailModel;
}
