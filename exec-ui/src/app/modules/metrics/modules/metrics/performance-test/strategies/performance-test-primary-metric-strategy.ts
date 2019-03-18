import {Count} from '../../../shared/domain-models/count';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {PrimaryMetricStrategyBase} from '../../../shared/strategies/primary-metric-strategy-base';
import {Injectable} from '@angular/core';
import {MetricValueModel} from '../../../shared/component-models/metric-value-model';
import {PerformanceTestConfiguration} from '../performance-test.configuration';

@Injectable()
export class PerformanceTestPrimaryMetricStrategy extends PrimaryMetricStrategyBase {
    parse(counts: Count[]): MetricValueModel {
        const validSet = new Set(['Error Rate']);
        return {
            name: PerformanceTestConfiguration.buildingBlockLabel,
            value: Math.round(counts
                .filter(c => validSet.has(c.label['type']))
                .reduce((a, b) => a + b.value, 0)/counts.filter(c => validSet.has(c.label['type'])).length)
        };
    }
}
