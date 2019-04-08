import { Count } from '../../../shared/domain-models/count';
import { PrimaryMetricStrategyBase } from '../../../shared/strategies/primary-metric-strategy-base';
import { Injectable } from '@angular/core';
import { MetricValueModel } from '../../../shared/component-models/metric-value-model';
import { DeployConfiguration } from '../deploy.configuration';

@Injectable()
export class DeployPrimaryMetricStrategy extends PrimaryMetricStrategyBase {
  parse(counts: Count[]): MetricValueModel {

    return {
      name: DeployConfiguration.buildingBlockLabel,
      value: counts
        .map(c => c.value)
        .reduce((a, b) => a + b, 0)
    };
  }
}
