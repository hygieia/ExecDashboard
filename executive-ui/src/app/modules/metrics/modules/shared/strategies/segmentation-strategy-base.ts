import {Strategy} from '../../../../shared/strategies/strategy';
import {MetricSummary} from '../domain-models/metric-summary';
import {MetricSegmentationModel} from '../component-models/metric-segmentation-model';

export abstract class SegmentationStrategyBase implements Strategy<MetricSummary, MetricSegmentationModel> {
  public abstract parse(model: MetricSummary): MetricSegmentationModel;
}
