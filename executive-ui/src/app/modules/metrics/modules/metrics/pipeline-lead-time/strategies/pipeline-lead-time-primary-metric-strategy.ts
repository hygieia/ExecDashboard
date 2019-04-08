import { Count } from '../../../shared/domain-models/count';
import { PrimaryMetricStrategyBase } from '../../../shared/strategies/primary-metric-strategy-base';
import { Injectable } from '@angular/core';
import { MetricValueModel } from '../../../shared/component-models/metric-value-model';
import { PipelineLeadTimeConfiguration } from '../pipeline-lead-time.configuration';

@Injectable()
export class PipelineLeadTimePrimaryMetricStrategy extends PrimaryMetricStrategyBase {
  parse(counts: Count[]): MetricValueModel {
    if (!counts || !counts.length) {
      return { name: PipelineLeadTimeConfiguration.buildingBlockLabel, value: 0, unit: 'minutes' };
    }

    const minutes = counts[0].value / 1000 / 60;
    const hours = counts[0].value / 1000 / 60 / 60;
    const days = counts[0].value / 1000 / 60 / 60 / 24;

    if (days >= 1) { return { prefix: '~', name: PipelineLeadTimeConfiguration.buildingBlockLabel, value: Math.round(days), unit: 'days' }; }
    if (hours >= 1) { return { prefix: '~', name: PipelineLeadTimeConfiguration.buildingBlockLabel, value: Math.round(hours), unit: 'hours' }; }
    return { prefix: '~', name: PipelineLeadTimeConfiguration.buildingBlockLabel, value: Math.round(minutes), unit: 'minutes' };
  }
}