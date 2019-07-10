import { Count } from '../../../shared/domain-models/count';
import { PrimaryMetricStrategyBase } from '../../../shared/strategies/primary-metric-strategy-base';
import { Injectable } from '@angular/core';
import { MetricValueModel } from '../../../shared/component-models/metric-value-model';
import { ProductionIncidentsConfiguration } from '../production-incidents.configuration';

@Injectable()
export class ProductionIncidentsPrimaryMetricStrategy extends PrimaryMetricStrategyBase {
  parse(counts: Count[]): MetricValueModel {
    const validSet = new Set(['1', '2']);

    return {
      name: ProductionIncidentsConfiguration.buildingBlockLabel,
      value: counts
        // .filter(c => c.label['type'] === 'issue')
        // .filter(c => c.label['event'] === 'closed')
        .filter(c => validSet.has(c.label['severity']))
        .map(c => c.value)
        .reduce((a, b) => a + b, 0)
    };
  }
}
