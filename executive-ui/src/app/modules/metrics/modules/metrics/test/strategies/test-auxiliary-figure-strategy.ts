import { Strategy } from '../../../../../shared/strategies/strategy';
import { MetricSummary } from '../../../shared/domain-models/metric-summary';
import { Injectable } from '@angular/core';
import { MetricValueModel } from '../../../shared/component-models/metric-value-model';
import { TestConfiguration } from '../test.configuration';

@Injectable()
export class TestAuxiliaryFigureStrategy implements Strategy<MetricSummary, MetricValueModel> {
  parse(model: MetricSummary) {


    return {
      prefix: '',
      hasData: true,
      name: '',
      unit: '',
      value: undefined
    };
  }
}

