import { Count } from '../../../shared/domain-models/count';
import { PrimaryMetricStrategyBase } from '../../../shared/strategies/primary-metric-strategy-base';
import { Injectable } from '@angular/core';
import { MetricValueModel } from '../../../shared/component-models/metric-value-model';
import { CodeRepoConfiguration } from '../code-repo.configuration';

@Injectable()
export class CodeRepoPrimaryMetricStrategy extends PrimaryMetricStrategyBase {
  parse(counts: Count[]): MetricValueModel {
    if (!counts || !counts.length) {
      return { name: CodeRepoConfiguration.buildingBlockLabel, value: 0 };
    }


    var finalCount = 0;

    if (null != counts[0] && counts[0].label['type'] === 'totalCommits')
      finalCount = Math.round((counts[0].value) / 90);

    return {
      name: CodeRepoConfiguration.buildingBlockLabel,
      value: finalCount
    };
  }
}
