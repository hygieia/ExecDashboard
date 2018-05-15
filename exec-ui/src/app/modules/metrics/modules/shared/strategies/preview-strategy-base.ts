import {Strategy} from '../../../../shared/strategies/strategy';
import {MetricPreviewModel} from '../component-models/metric-preview-model';
import {MetricDetail} from '../domain-models/metric-detail';

export abstract class PreviewStrategyBase implements Strategy<MetricDetail, MetricPreviewModel> {
  public abstract parse(model: MetricDetail): MetricPreviewModel;
}
