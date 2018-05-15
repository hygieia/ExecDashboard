import {Count} from '../../../shared/domain-models/count';
import {PrimaryMetricStrategyBase} from '../../../shared/strategies/primary-metric-strategy-base';
import {Injectable} from '@angular/core';
import {MetricValueModel} from '../../../shared/component-models/metric-value-model';
import {SCMCommitsConfiguration} from '../scm-commits.configuration';

@Injectable()
export class SCMCommitsPrimaryMetricStrategy extends PrimaryMetricStrategyBase {
  parse(counts: Count[]): MetricValueModel {
    return {
      name: SCMCommitsConfiguration.buildingBlockLabel,
      value: counts.reduce((a, b) => a + b.value, 0)
    };
  }
}
