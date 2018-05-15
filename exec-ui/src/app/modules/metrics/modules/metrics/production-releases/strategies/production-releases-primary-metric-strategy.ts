import {Count} from '../../../shared/domain-models/count';
import {PrimaryMetricStrategyBase} from '../../../shared/strategies/primary-metric-strategy-base';
import {Injectable} from '@angular/core';
import {MetricValueModel} from '../../../shared/component-models/metric-value-model';
import {ProductionReleasesConfiguration} from '../production-releases.configuration';

@Injectable()
export class ProductionReleasesPrimaryMetricStrategy extends PrimaryMetricStrategyBase {
  parse(counts: Count[]): MetricValueModel {
    return {
      name: ProductionReleasesConfiguration.buildingBlockLabel,
      value: counts.reduce((a, b) => a + b.value, 0)
    };
  }
}
