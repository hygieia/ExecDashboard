import {Count} from '../../../shared/domain-models/count';
import {PrimaryMetricStrategyBase} from '../../../shared/strategies/primary-metric-strategy-base';
import {Injectable} from '@angular/core';
import {MetricValueModel} from '../../../shared/component-models/metric-value-model';
import {WorkInProgressConfiguration} from '../work-in-progress.configuration';

@Injectable()
export class WorkInProgressPrimaryMetricStrategy extends PrimaryMetricStrategyBase {
  parse(counts: Count[]): MetricValueModel {
    if (!counts || !counts.length) {
			return {name: WorkInProgressConfiguration.buildingBlockLabel, value: 0};
		}
   
    const validSet = new Set(['Epic', 'Story', 'Bugs' ,'Other']);

    return {
      
      name: WorkInProgressConfiguration.buildingBlockLabel,
      value: counts
        .filter(c => validSet.has(c.label['type']))
        .map(c => c.value)
        .reduce((a, b) => a + b, 0)
    };
  }
}
