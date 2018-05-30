import {Count} from '../../../shared/domain-models/count';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {PrimaryMetricStrategyBase} from '../../../shared/strategies/primary-metric-strategy-base';
import {Injectable} from '@angular/core';
import {MetricValueModel} from '../../../shared/component-models/metric-value-model';
import {UnitTestCoverageConfiguration} from '../unit-test-coverage.configuration';

@Injectable()
export class UnitTestCoveragePrimaryMetricStrategy extends PrimaryMetricStrategyBase {
  parse(counts: Count[]): MetricValueModel {
    if (!counts || !counts.length) {
      return {
        name: UnitTestCoverageConfiguration.buildingBlockLabel,
        value: 0
      };
    }
    return {
      name: UnitTestCoverageConfiguration.buildingBlockLabel,
      value: counts[0].value
    };
  }
}
