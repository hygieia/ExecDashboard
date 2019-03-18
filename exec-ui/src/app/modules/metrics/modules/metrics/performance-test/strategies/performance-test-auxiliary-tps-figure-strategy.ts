import {Strategy} from '../../../../../shared/strategies/strategy';
import {MetricSummary} from '../../../shared/domain-models/metric-summary';
import {Injectable} from '@angular/core';
import {MetricValueModel} from '../../../shared/component-models/metric-value-model';
import {PerformanceTestConfiguration} from "../performance-test.configuration";

@Injectable()
export class PerformanceTestAuxiliaryTPSFigureStrategy implements Strategy<MetricSummary, MetricValueModel> {
    parse(model: MetricSummary) {
        const TPS = model.counts.find(count => count.label['type'] === PerformanceTestConfiguration.auxilliaryTPSIdentifier);

        if (!TPS) {
            return {hasData: false, name: PerformanceTestConfiguration.auxilliaryTPSFigureHeading, value: null};
        }

        return {

            name: PerformanceTestConfiguration.auxilliaryTPSFigureHeading,
            value: Math.round(TPS.value)
        };
    }
}
