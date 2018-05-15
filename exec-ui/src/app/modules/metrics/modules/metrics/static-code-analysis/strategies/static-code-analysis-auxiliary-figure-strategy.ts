import {Strategy} from '../../../../../shared/strategies/strategy';
import {MetricSummary} from '../../../shared/domain-models/metric-summary';
import {Injectable} from '@angular/core';
import {MetricValueModel} from '../../../shared/component-models/metric-value-model';
import {StaticCodeAnalysisConfiguration} from '../static-code-analysis.configuration';

@Injectable()
export class StaticCodeAnalysisAuxiliaryFigureStrategy implements Strategy<MetricSummary, MetricValueModel> {
  parse(model: MetricSummary) {
    const debt = model.counts.find(count => count.label['type'] === StaticCodeAnalysisConfiguration.auxilliaryIdentifier);

    if (!debt) {
      return {hasData: false, name: StaticCodeAnalysisConfiguration.auxilliaryFigureHeading, value: null, unit: null, prefix: null};
    }

    return {
      prefix: '~',
      hasData: true,
      name: StaticCodeAnalysisConfiguration.auxilliaryFigureHeading,
      unit: 'days',
      value: Math.round(debt.value / 60 / 8)
    };
  }
}
