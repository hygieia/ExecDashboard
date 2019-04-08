import { Strategy } from '../../../../../shared/strategies/strategy';
import { MetricSummary } from '../../../shared/domain-models/metric-summary';
import { Injectable } from '@angular/core';
import { MetricValueModel } from '../../../shared/component-models/metric-value-model';
import { ProductionIncidentsConfiguration } from '../production-incidents.configuration';

@Injectable()
export class ProductionIncidentsAuxiliaryFigureStrategy implements Strategy<MetricSummary, MetricValueModel> {
  parse(model: MetricSummary) {
    const mttr = model.counts.find(count => count.label['timeToResolve'] != undefined);

    if (!mttr) { return { hasData: false, name: ProductionIncidentsConfiguration.auxilliaryFigureHeading, value: null, unit: null, prefix: null }; }

    const mttrTiming = calculateMttr(model.counts);

    return {
      prefix: '~',
      hasData: true,
      name: ProductionIncidentsConfiguration.auxilliaryFigureHeading,
      unit: mttrTiming.unit,
      value: mttrTiming.value
    };

    function calculateMttr(mttrMillis) {
      let ttr = 0;
      let count = 0;

      for (const entry of mttrMillis) {
        if (entry.label['timeToResolve'] != undefined) {
          const values = +entry.label['timeToResolve'];
          ttr += values;
          count += entry.value;
        }
      }


      if (count === 0) {
        return { value: 0, unit: 'minutes' };
      }

      const minutes = ttr / count;
      const hours = ttr / (count * 60);
      const days = ttr / (count * 60 * 24);

      if (days >= 1) { return { value: Math.round(days), unit: 'days' }; }
      if (hours >= 1) { return { value: Math.round(hours), unit: 'hours' }; }
      return { value: Math.round(minutes), unit: 'minutes' };
    }
  }
}

