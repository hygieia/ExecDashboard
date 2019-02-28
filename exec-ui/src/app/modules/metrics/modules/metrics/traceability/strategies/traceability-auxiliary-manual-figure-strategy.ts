import {Strategy} from '../../../../../shared/strategies/strategy';
import {MetricSummary} from '../../../shared/domain-models/metric-summary';
import {Injectable} from '@angular/core';
import {MetricValueModel} from '../../../shared/component-models/metric-value-model';
import {TraceabilityConfiguration} from "../traceability.configuration";

@Injectable()
export class TraceabilityAuxiliaryManualFigureStrategy implements Strategy<MetricSummary, MetricValueModel> {
  parse(model: MetricSummary) {
    const automated = model.counts.find(count => count.label['type'] === TraceabilityConfiguration.auxilliaryManualIdentifier);

    if (!automated) {
      return {name: TraceabilityConfiguration.auxilliaryManualFigureHeading, value: null};
    }

    return {

      name: TraceabilityConfiguration.auxilliaryManualFigureHeading,
      value: Math.round(automated.value)
    };
  }
}
