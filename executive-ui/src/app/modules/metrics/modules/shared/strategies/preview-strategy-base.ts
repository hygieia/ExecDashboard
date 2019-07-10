import {Strategy} from '../../../../shared/strategies/strategy';
import {MetricPreviewModel} from '../component-models/metric-preview-model';
import {MetricSummary} from '../domain-models/metric-summary';

export abstract class PreviewStrategyBase implements Strategy<MetricSummary, MetricPreviewModel> {
  public abstract parse(model: MetricSummary): MetricPreviewModel;
}
