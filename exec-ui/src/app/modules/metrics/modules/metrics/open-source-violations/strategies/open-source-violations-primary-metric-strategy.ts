import {Count} from '../../../shared/domain-models/count';
import {PrimaryMetricStrategyBase} from '../../../shared/strategies/primary-metric-strategy-base';
import {Injectable} from '@angular/core';
import {MetricValueModel} from '../../../shared/component-models/metric-value-model';
import {OpenSourceViolationsConfiguration} from '../open-source-violations.configuration';

@Injectable()
export class OpenSourceViolationsPrimaryMetricStrategy extends PrimaryMetricStrategyBase {
  parse(counts: Count[]): MetricValueModel {
    const validSet = new Set(['Critical', 'High', 'Medium', 'Low']);

    return {
      name: OpenSourceViolationsConfiguration.buildingBlockLabel,
      value: counts
        .filter(c => validSet.has(c.label['severity']))
        .map(c => c.value)
        .reduce((a, b) => a + b, 0)
    };
  }
}
