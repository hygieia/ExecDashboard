import {Count} from '../../../shared/domain-models/count';
import {PrimaryMetricStrategyBase} from '../../../shared/strategies/primary-metric-strategy-base';
import {Injectable} from '@angular/core';
import {MetricValueModel} from '../../../shared/component-models/metric-value-model';
import {TestConfiguration} from '../test.configuration';

@Injectable()
export class TestPrimaryMetricStrategy extends PrimaryMetricStrategyBase {
  parse(counts: Count[]): MetricValueModel {
    
   const primaryCount = counts[0].value - Number(counts[0].label["cucSkipCount"]) - Number(counts[0].label["htmlSkipCount"]) - Number(counts[0].label["selSkipCount"]) - Number(counts[0].label["soapSkipCount"]) - Number(counts[0].label["jmeterSkipCount"]);
   
    if(primaryCount > 1000){
      return {
        name: TestConfiguration.buildingBlockLabel,
        value: Math.round(primaryCount/1000),
        unit: 'k'
      };
    }else{
      return {
        name: TestConfiguration.buildingBlockLabel,
        value: primaryCount,
        unit: ''
      };
    }

  
  }
}
