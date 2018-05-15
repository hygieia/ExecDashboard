import {Strategy} from '../../../../../shared/strategies/strategy';
import {BuildingBlockMetricSummaryModel} from '../../../shared/component-models/building-block-metric-summary-model';
import {MetricValueModel} from '../../../shared/component-models/metric-value-model';
import {PipelineLeadTimeTrendStrategy} from './pipeline-lead-time-trend-strategy';
import {PipelineLeadTimePrimaryMetricStrategy} from './pipeline-lead-time-primary-metric-strategy';
import {Injectable} from '@angular/core';
import {MetricSummary} from '../../../shared/domain-models/metric-summary';
import {PipelineLeadTimeConfiguration} from '../pipeline-lead-time.configuration';

@Injectable()
export class PipelineLeadTimeBuildingBlockPrimaryMetricStrategy implements Strategy<MetricSummary, BuildingBlockMetricSummaryModel> {
  constructor(private primaryMetricStrategy: PipelineLeadTimePrimaryMetricStrategy,
              private trendStrategy: PipelineLeadTimeTrendStrategy) {}

  parse(metric: MetricSummary) {
    if (!metric) { return null; }

    return {
      value: mapPrimary(this.primaryMetricStrategy.parse(metric.counts)),
      trend: this.trendStrategy.parse(metric),
      isRatio: false
    };

    function mapPrimary(primary: MetricValueModel) {
      return {
        name: PipelineLeadTimeConfiguration.buildingBlockLabel,
        unit: unit(primary),
        value: primary.value,
        prefix: primary.prefix
      };

      function unit(valueModel) {
        switch (valueModel.unit) {
          case 'days': return 'd';
          case 'hours': return valueModel.value === 1 ? 'hr' : 'hrs';
          case 'minutes': return 'm';
        }
      }
    }
  }
}
