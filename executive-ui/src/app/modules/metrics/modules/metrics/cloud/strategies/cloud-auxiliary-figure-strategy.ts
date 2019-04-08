import { Strategy } from '../../../../../shared/strategies/strategy';
import { MetricSummary } from '../../../shared/domain-models/metric-summary';
import { Injectable } from '@angular/core';
import { MetricValueModel } from '../../../shared/component-models/metric-value-model';
import { CloudConfiguration } from '../cloud.configuration';

@Injectable()
export class CloudAuxiliaryFigureStrategy implements Strategy<MetricSummary, MetricValueModel> {
  parse(model: MetricSummary) {

    const metrics = model.counts.find(count => count.label['cost'] != undefined);

    if (!metrics) { return { hasData: false, name: CloudConfiguration.auxilliaryFigureHeading, value: null, unit: null, prefix: null }; }

    const metricsTiming = calculateMetrics(model.counts);

    return {
      prefix: '$',
      hasData: true,
      name: CloudConfiguration.auxilliaryFigureHeading,
      unit: metricsTiming.unit,
      value: metricsTiming.value
    };

    function calculateMetrics(cost) {
      return { value: cost, unit: '$' };
    }
  }
}


