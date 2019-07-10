import {Count} from '../../../shared/domain-models/count';
import {PrimaryMetricStrategyBase} from '../../../shared/strategies/primary-metric-strategy-base';
import {Injectable} from '@angular/core';
import {MetricValueModel} from '../../../shared/component-models/metric-value-model';
import {SayDoRatioConfiguration} from '../saydoratio.configuration';

@Injectable()
export class SayDoRatioPrimaryMetricStrategy extends PrimaryMetricStrategyBase {
  parse(counts: Count[]): MetricValueModel {
    

    const primaryCount = counts[0].label["avgTotalStoryPoints"] != 0 ? ((counts[0].label["avgCompletedStoryPoints"]*100)/counts[0].label["avgTotalStoryPoints"]) : 0;

    function formattedData(value){
      if (value > 0.9) {
        return Math.round(value);
      }else if(value < 0.9 && value > 0.0)
      {
        return value.toFixed(1);
      }else if(value < 0.9 && value > 0.00)
      {
        return value.toFixed(2);
      }else{
        return Math.round(value)
      }
     
    }

    return {
      name: SayDoRatioConfiguration.buildingBlockLabel,
      value: formattedData(primaryCount),
      unit: '%'
    };
  }
}
