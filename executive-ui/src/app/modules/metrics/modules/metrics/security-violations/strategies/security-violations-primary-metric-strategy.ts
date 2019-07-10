import {Count} from '../../../shared/domain-models/count';
import {PrimaryMetricStrategyBase} from '../../../shared/strategies/primary-metric-strategy-base';
import {Injectable} from '@angular/core';
import {MetricValueModel} from '../../../shared/component-models/metric-value-model';
import {SecurityViolationsConfiguration} from '../security-violations.configuration';

@Injectable()
export class SecurityViolationsPrimaryMetricStrategy extends PrimaryMetricStrategyBase {
  parse(counts: Count[]): MetricValueModel {
    const validSet = new Set(['codeBlocker', 'codeCritical', 'codeMajor','portBlocker', 'portCritical', 'portMajor','webBlocker', 'webCritical', 'webMajor' ,'blackDuckMajor','blackDuckCritical','blackDuckBlocker']);

    return {
      name: SecurityViolationsConfiguration.buildingBlockLabel,
      value: counts
        .filter(c => validSet.has(c.label['severity']))
        .map(c => c.value)
        .reduce((a, b) => a + b, 0)
    };
  }
}
