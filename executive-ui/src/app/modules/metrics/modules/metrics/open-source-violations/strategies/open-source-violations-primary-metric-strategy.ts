import { Count } from '../../../shared/domain-models/count';
import { PrimaryMetricStrategyBase } from '../../../shared/strategies/primary-metric-strategy-base';
import { Injectable } from '@angular/core';
import { MetricValueModel } from '../../../shared/component-models/metric-value-model';
import { OpenSourceViolationsConfiguration } from '../open-source-violations.configuration';

@Injectable()
export class OpenSourceViolationsPrimaryMetricStrategy extends PrimaryMetricStrategyBase {
	parse(counts: Count[]): MetricValueModel {

		if (!counts || !counts.length) {
			return { name: OpenSourceViolationsConfiguration.buildingBlockLabel, value: 0 };
		}

		var totalStoryPoints = 0;
		var totalTime = 0;
		var totalStories = 0;

		if (null != counts[0] && counts[0].label['type'] === 'Total Time')
			totalTime = counts[0].value;
		if (null != counts[1] && counts[1].label['type'] === 'Total Stories')
			totalStories = counts[1].value;
		if (null != counts[2] && counts[2].label['type'] === 'Total Story Points')
			totalStoryPoints = counts[2].value;


		if (totalStoryPoints != 0) {
			return {
				name: OpenSourceViolationsConfiguration.buildingBlockLabel,
				value: (totalTime / totalStoryPoints)
			};
		}

		else if (totalStoryPoints == 0 && totalStories != 0) {
			return {
				name: OpenSourceViolationsConfiguration.buildingBlockLabel,
				value: (totalTime / totalStories)
			};
		}

		else {
			return {
				name: OpenSourceViolationsConfiguration.buildingBlockLabel,
				value: 0
			};
		}


	}
}

