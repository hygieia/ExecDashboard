import { Count } from '../../../shared/domain-models/count';
import { PrimaryMetricStrategyBase } from '../../../shared/strategies/primary-metric-strategy-base';
import { Injectable } from '@angular/core';
import { MetricValueModel } from '../../../shared/component-models/metric-value-model';
import { DevopsCupConfiguration } from '../devopscup.configuration';


@Injectable()
export class DevopscupPrimaryMetricStrategy extends PrimaryMetricStrategyBase {
  parse(counts: Count[]): MetricValueModel {
    const sums = counts.reduce((runningSums, count) => {
      if (!runningSums.has(count.label['type'])) {
        runningSums.set(count.label['type'], 0);
      }
      const newCount = runningSums.get(count.label['type']) + count.value;
      return runningSums.set(count.label['type'], newCount);
    }, new Map());

    var totalPercent = sums.get('totalPercent').toFixed(2);
    return {
      name: DevopsCupConfiguration.buildingBlockLabel,
      value: totalPercent,
      unit: '%'
    };
  }
}
