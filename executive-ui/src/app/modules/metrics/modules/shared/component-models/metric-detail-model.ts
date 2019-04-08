import { Count } from '../domain-models/count';
import { MetricGraphModel } from './metric-graph-model';
import { MetricSegmentationModel } from './metric-segmentation-model';
import { MetricTrendModel } from './metric-trend-model';
import { MetricValueModel } from './metric-value-model';
import { MttrModel } from './mttr-model';
import { OperationalMetricsModel } from './operational-metrics-model';

export class MetricDetailModel {
  public isRatio: boolean;
  public metricOwnerSubTitle: string;
  public issues: Count[];
  public lastScanned: string;
  public totalReporting: number;
  public trend: MetricTrendModel;
  public graphModel: MetricGraphModel;
  public segmentationModel: MetricSegmentationModel;
  public secondaryFigureModel: MetricValueModel;
  public outageListModel?: MttrModel[];
  public eventListModel?: MttrModel[];
  public operationalListModel?: OperationalMetricsModel[];
}
