import {Strategy} from '../../../../../shared/strategies/strategy';
import {MetricSummary} from '../../../shared/domain-models/metric-summary';
import {Injectable} from '@angular/core';
import {MetricValueModel} from '../../../shared/component-models/metric-value-model';
import {TraceabilityConfiguration} from "../traceability.configuration";

@Injectable()
export class TraceabilityAuxiliaryAutomatedFigureStrategy implements Strategy<MetricSummary, MetricValueModel> {
  parse(model: MetricSummary) {
    const automated = model.counts.find(count => count.label['type'] === TraceabilityConfiguration.auxilliaryAutomatedIdentifier);

    if (!automated) {
      return {hasData: false, name: TraceabilityConfiguration.auxilliaryAutomatedFigureHeading, value: null};
    }

    return {

      name: TraceabilityConfiguration.auxilliaryAutomatedFigureHeading,
      value: Math.round(automated.value)
    };
  }
}
