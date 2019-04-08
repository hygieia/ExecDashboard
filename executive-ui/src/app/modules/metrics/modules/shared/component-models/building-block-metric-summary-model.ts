import {MetricTrendModel} from './metric-trend-model';
import {MetricValueModel} from './metric-value-model';

export class BuildingBlockMetricSummaryModel {
  isEmpty?: boolean;
  isRatio: boolean;
  value: MetricValueModel;
  trend: MetricTrendModel;
  available: boolean;
  message: string;
  critical?: number;
  major?: number;
  blocker?: number;
}
