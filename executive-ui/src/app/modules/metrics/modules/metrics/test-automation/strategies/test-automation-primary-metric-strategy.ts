import {Count} from '../../../shared/domain-models/count';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {PrimaryMetricStrategyBase} from '../../../shared/strategies/primary-metric-strategy-base';
import {Injectable} from '@angular/core';
import decimalToPercent = PresentationFunctions.decimalToPercent;
import {MetricValueModel} from '../../../shared/component-models/metric-value-model';
import {TestAutomationConfiguration} from '../test-automation.configuration';

@Injectable()
export class TestAutomationPrimaryMetricStrategy extends PrimaryMetricStrategyBase {
  parse(counts: Count[]): MetricValueModel {
    if (!counts || !counts.length) {
      return {
        name: TestAutomationConfiguration.buildingBlockLabel,
        value: 0
      };
    }
    return {
      name: TestAutomationConfiguration.buildingBlockLabel,
      value: decimalToPercent(counts[0].value)
    };
  }
}
