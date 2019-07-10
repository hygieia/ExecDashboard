import { Count } from '../../../shared/domain-models/count';
import { PrimaryMetricStrategyBase } from '../../../shared/strategies/primary-metric-strategy-base';
import { Injectable } from '@angular/core';
import { MetricValueModel } from '../../../shared/component-models/metric-value-model';
import { CloudConfiguration } from '../cloud.configuration';

@Injectable()
export class CloudPrimaryMetricStrategy extends PrimaryMetricStrategyBase {
  parse(counts: Count[]): MetricValueModel {

    if (counts == undefined) {
      return {
        prefix: '$',
        name: CloudConfiguration.buildingBlockLabel,
        value: 0
      };
    }

    const cost = counts.filter(i => i.label['type'] === 'cost')
      .map(c => c.value)
      .reduce((a, b) => a + b, 0);
    return {
      prefix: '$',
      name: CloudConfiguration.buildingBlockLabel,
      value: cost
    };
  }
}



