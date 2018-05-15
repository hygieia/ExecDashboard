import {Strategy} from '../../../../../shared/strategies/strategy';
import {MetricSummary} from '../../../shared/domain-models/metric-summary';
import {Injectable} from '@angular/core';
import {MetricValueModel} from '../../../shared/component-models/metric-value-model';
import {ProductionIncidentsConfiguration} from '../production-incidents.configuration';

@Injectable()
export class ProductionIncidentsAuxiliaryFigureStrategy implements Strategy<MetricSummary, MetricValueModel> {
  parse(model: MetricSummary) {
    const mttr = model.counts.find(count => count.label['type'] === 'mttr');

    if (!mttr) { return { hasData: false, name: ProductionIncidentsConfiguration.auxilliaryFigureHeading, value: null, unit: null, prefix: null}; }

    const mttrTiming = calculateMttr(mttr.value);

    return {
      prefix: '~',
      hasData: true,
      name: ProductionIncidentsConfiguration.auxilliaryFigureHeading,
      unit: mttrTiming.unit,
      value: mttrTiming.value
    };

    function calculateMttr(mttrMillis) {
      const minutes = mttrMillis / 1000 / 60;
      const hours = mttrMillis / 1000 / 60 / 60;
      const days = mttrMillis / 1000 / 60 / 60 / 24;

      if (days >= 1) { return { value: Math.round(days), unit: 'days' }; }
      if (hours >= 1) { return { value: Math.round(hours), unit: 'hours' }; }
      return { value: Math.round(minutes), unit: 'minutes'};
    }
  }
}
