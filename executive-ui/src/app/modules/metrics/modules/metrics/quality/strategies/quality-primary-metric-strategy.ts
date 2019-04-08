import { Count } from '../../../shared/domain-models/count';
import { PrimaryMetricStrategyBase } from '../../../shared/strategies/primary-metric-strategy-base';
import { Injectable } from '@angular/core';
import { MetricValueModel } from '../../../shared/component-models/metric-value-model';
import { QualityConfiguration } from '../quality.configuration';

@Injectable()
export class QualityPrimaryMetricStrategy extends PrimaryMetricStrategyBase {
  parse(counts: Count[]): MetricValueModel {
    if (!counts || !counts.length) {
      return { name: QualityConfiguration.buildingBlockLabel, value: 0 };
    }

    var cmis = 0;
    var serviceNow = 0;
    var normal = 0;
    var blocker = 0;
    var high = 0;
    var low = 0;
    var total = 0;

    if (null != counts[0] && counts[0].label['priority'] === 'normal')
      normal = counts[0].value;
    if (null != counts[1] && counts[1].label['priority'] === 'blocker')
      blocker = counts[1].value;
    if (null != counts[2] && counts[2].label['priority'] === 'high')
      high = counts[2].value;
    if (null != counts[3] && counts[3].label['priority'] === 'low')
      low = counts[3].value;
    if (null != counts[4] && counts[4].label['priority'] === 'cmis')
      cmis = counts[4].value;
    if (null != counts[5] && counts[5].label['priority'] === 'cmis')
      cmis = counts[5].value;
    if (null != counts[19] && counts[19].label['priority'] === 'serviceNow')
      serviceNow = counts[19].value;

    var finalDefects = normal + blocker + high + low + cmis + serviceNow;


    return {
      name: QualityConfiguration.buildingBlockLabel,
      value: finalDefects
    };
  }
}
