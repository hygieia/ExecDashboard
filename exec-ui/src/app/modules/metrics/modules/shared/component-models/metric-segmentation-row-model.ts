import {MetricSegmentationColumnModel} from './metric-segmentation-column-model';

export class MetricSegmentationRowModel {
  name: string;
  order: number;
  columns: MetricSegmentationColumnModel[];
}
