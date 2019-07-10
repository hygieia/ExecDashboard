import { BuildingBlockMetricSummary } from '../../../shared/domain-models/building-block-metric-summary';
import { BuildingBlockModel } from '../../../shared/component-models/building-block-model';
import { BuildingBlockMetricSummaryModel } from '../../../shared/component-models/building-block-metric-summary-model';
import { BuildingBlocksStrategyBase } from '../../../shared/strategies/building-blocks-strategy-base';
import { Injectable } from '@angular/core';
import { NavigationModel } from '../../../../../shared/component-models/navigation-model';
import { PipelineLeadTimeBuildingBlockPrimaryMetricStrategy } from './pipeline-lead-time-building-block-primary-metric-strategy';
import { PipelineLeadTimeConfiguration } from '../pipeline-lead-time.configuration';

@Injectable()
export class PipelineLeadTimeBuildingBlocksStrategy extends BuildingBlocksStrategyBase {
  constructor(private primaryMetricsStrategy: PipelineLeadTimeBuildingBlockPrimaryMetricStrategy) { super(); }

  parse(model: BuildingBlockMetricSummary[]): BuildingBlockModel[] {
    const buildingBlocks = new Array<BuildingBlockModel>();
    model.forEach((p) => {
      buildingBlocks.push({
        id: p.id,
        name: p.name,
        lob: p.lob,
        poc: p.poc,
        projectKey: p.customField,
        vastId: p.customField,
        reporting: this.mapReportingComponents(p),
        total: this.mapTotalComponents(p),
        //completeness: this.mapCompleteness(p),
        lastScanned: this.mapLastScanned(p),
        metrics: this.mapMetrics(p),
        detail: this.mapNavigationModel(p),
        appCriticality: p.appCriticality
      });
    });

    return buildingBlocks.sort(this.sortComponents);
  }

  private mapNavigationModel(buildingBlockMetricSummary: BuildingBlockMetricSummary): NavigationModel {
    const navigationModel = new NavigationModel;
    navigationModel.commands = ['product', buildingBlockMetricSummary.id];
    navigationModel.url = buildingBlockMetricSummary.url;
    return navigationModel;
  }

  private mapMetrics(buildingBlockMetricSummary: BuildingBlockMetricSummary): BuildingBlockMetricSummaryModel[] {
    const metric = buildingBlockMetricSummary.metrics.find(m => m.name === PipelineLeadTimeConfiguration.identifier);
    if (!metric) {
      return [];
    }

    const mapSecondarys = mapSecondaryMetric(metric);

    if (mapSecondarys.value.value == null) {
      return [
        this.primaryMetricsStrategy.parse(metric)
      ];

    }

    return [
      this.primaryMetricsStrategy.parse(metric),
      mapSecondarys
    ];

    function mapSecondaryMetric(value) {

      const cadence = value.counts.find(count => count.label['type'] != undefined);
      const cadenceTiming = calculateCadence(value.counts);

      if (cadence && cadenceTiming.value > 0) {
        return {
          value: mapSecondary(cadenceTiming),
          trend: null,
          isRatio: false,
          available: true,
          message: ''
        };
      } else {
        return {
          isEmpty: true,
          isRatio: false,
          value: { name: PipelineLeadTimeConfiguration.auxilliaryIdentifier, value: null },
          trend: null,
          available: true,
          message: ''
        };
      }
    }

    function calculateCadence(cadenceMillis) {
      var cad = 0;
      for (let entry of cadenceMillis) {
        const type = entry.label['type'];
        if (type != undefined && type == 'cadence')
          cad += entry.value;

      }
      return { value: Math.round(cad), unit: 'days' };
    }

    function mapSecondary(valueModel) {
      return {
        name: PipelineLeadTimeConfiguration.auxilliaryIdentifier,
        prefix: '*',
        value: valueModel.value,
        unit: 'd'
      };
    }

  }
}
