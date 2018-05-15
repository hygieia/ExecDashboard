export class MetricValueModel {
  prefix?: string;
  value: number;
  name: string;
  unit?: string;
  hasData? = true;
}
