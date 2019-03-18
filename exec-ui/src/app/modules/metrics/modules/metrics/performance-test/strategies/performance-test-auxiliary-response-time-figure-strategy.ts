import {Strategy} from '../../../../../shared/strategies/strategy';
import {MetricSummary} from '../../../shared/domain-models/metric-summary';
import {Injectable} from '@angular/core';
import {MetricValueModel} from '../../../shared/component-models/metric-value-model';
import {PerformanceTestConfiguration} from "../performance-test.configuration";

@Injectable()
export class PerformanceTestAuxiliaryResponseTimeFigureStrategy implements Strategy<MetricSummary, MetricValueModel> {
    parse(model: MetricSummary) {
        const response_time = model.counts.find(count => count.label['type'] === PerformanceTestConfiguration.auxilliaryResponseTimeIdentifier);

        if (!response_time) {
            return {hasData: false, name: PerformanceTestConfiguration.auxilliaryResponseTimeFigureHeading, value: null};
        }

        return {

            name: PerformanceTestConfiguration.auxilliaryResponseTimeFigureHeading,
            value: Math.round(response_time.value)
        };
    }
}
