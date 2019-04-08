import { Strategy } from '../../../../../shared/strategies/strategy';
import { MetricSummary } from '../../../shared/domain-models/metric-summary';
import { Injectable } from '@angular/core';
import { MetricValueModel } from '../../../shared/component-models/metric-value-model';
import { DeployConfiguration } from '../deploy.configuration';

@Injectable()
export class DeployAuxiliaryFigureStrategy implements Strategy<MetricSummary, MetricValueModel> {
  parse(model: MetricSummary) {
    const deploy = model.counts.find(count => count.label['avgDuration'] != undefined);

    if (!deploy) { return { hasData: false, name: DeployConfiguration.auxilliaryFigureHeading, value: null, unit: null, prefix: null }; }

    const avgExecutionTime = calculateAvgExecutionTime(model.counts);

    return {
      prefix: '~',
      hasData: true,
      name: DeployConfiguration.auxilliaryFigureHeading,
      unit: avgExecutionTime.unit,
      value: avgExecutionTime.value
    };

    function calculateAvgExecutionTime(deploys) {

      var dur = 0;
      for (let entry of deploys) {
        dur = +entry.label['avgDuration']
      }

      const minutes = dur / (1000 * 60);
      const hours = dur / (1000 * 60 * 60);
      const days = dur / (1000 * 60 * 60 * 24);

      if (days >= 1) { return { value: Math.round(days), unit: 'd' }; }
      if (hours >= 1) { return { value: Math.round(hours), unit: 'h' }; }
      return { value: Math.round(minutes), unit: 'm' };
    }
  }
}

