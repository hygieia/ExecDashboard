import {Count} from '../../../shared/domain-models/count';
import {PrimaryMetricStrategyBase} from '../../../shared/strategies/primary-metric-strategy-base';
import {Injectable} from '@angular/core';
import {MetricValueModel} from '../../../shared/component-models/metric-value-model';
import {ThroughputConfiguration} from '../throughput.configuration';

@Injectable()
export class ThroughputPrimaryMetricStrategy extends PrimaryMetricStrategyBase {
	parse(counts: Count[]): MetricValueModel {

		if (!counts || !counts.length) {
			return {name: ThroughputConfiguration.buildingBlockLabel, value: 0};
		}

		let totalStories = 0;
		
		if (counts.length > 3 && null != counts[3] && counts[3].value != null) {
			totalStories = counts[3].value;
		}


		if(totalStories != 0){
			return{
				name: ThroughputConfiguration.buildingBlockLabel,
				value: totalStories
			};
		}

		else{
			return{
				name: ThroughputConfiguration.buildingBlockLabel,
				value: 0
			};
		}


	}  
}

