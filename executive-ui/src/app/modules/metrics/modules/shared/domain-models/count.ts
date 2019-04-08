import { MttrModel } from '../component-models/mttr-model';
import { OperationalMetricsModel } from '../component-models/operational-metrics-model';

export class Count {
  label: object;
  value: number;
  labelMttr?: MttrModel[];
  operationMetrics?: OperationalMetricsModel[];
}
