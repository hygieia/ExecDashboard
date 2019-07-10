import { Strategy } from '../../../../../shared/strategies/strategy';
import { MetricSummary } from '../../../shared/domain-models/metric-summary';
import { Injectable } from '@angular/core';
import { MetricValueModel } from '../../../shared/component-models/metric-value-model';
import { SayDoRatioConfiguration } from '../saydoratio.configuration';

@Injectable()
export class SayDoRatioAuxiliaryFigureStrategy implements Strategy<MetricSummary, MetricValueModel> {
  parse(model: MetricSummary) {

    function formattedData(value) {
      if (value > 0.9) {
        return Math.round(value);
      } else if (value < 0.9 && value > 0.0) {
        return value.toFixed(1);
      } else if (value < 0.9 && value > 0.00) {
        return value.toFixed(2);
      } else {
        return Math.round(value)
      }

    }

    return {
      prefix: '',
      hasData: true,
      name: SayDoRatioConfiguration.auxilliaryFigureHeading,
      unit: '%',
      value: formattedData(Number(model.counts[0].label['totalStories']) != 0 ? (Number(model.counts[0].label['completedStories']) * 100) / Number(model.counts[0].label['totalStories']) : 0)
    };
  }

}

