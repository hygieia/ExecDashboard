import {Strategy} from '../../../../../shared/strategies/strategy';
import {MetricSummary} from '../../../shared/domain-models/metric-summary';
import {Injectable} from '@angular/core';
import {MetricValueModel} from '../../../shared/component-models/metric-value-model';
import {PerformanceTestConfiguration} from "../performance-test.configuration";

@Injectable()
export class PerformanceTestAuxiliaryErrorRateFigureStrategy implements Strategy<MetricSummary, MetricValueModel> {
    parse(model: MetricSummary) {
        const error_rate = model.counts.find(count => count.label['type'] === PerformanceTestConfiguration.auxilliaryErrorRateIdentifier);

        if (!error_rate) {
            return {hasData: false, name: PerformanceTestConfiguration.auxilliaryErrorRateFigureHeading, value: null};
        }

        return {

            name: PerformanceTestConfiguration.auxilliaryErrorRateFigureHeading,
            value: Math.round(error_rate.value)
        };
    }
}
