import { BuildingBlockMetricSummaryModel } from '../../../shared/component-models/building-block-metric-summary-model';
import { BuildingBlockMetricSummary } from '../../../shared/domain-models/building-block-metric-summary';
import { CloudPrimaryMetricStrategy } from './cloud-primary-metric-strategy';
import { CloudTrendStrategy } from './cloud-trend-strategy';
import { BuildingBlockModel } from '../../../shared/component-models/building-block-model';
import { BuildingBlocksStrategyBase } from '../../../shared/strategies/building-blocks-strategy-base';
import { CloudAuxiliaryFigureStrategy } from './cloud-auxiliary-figure-strategy';
import { NavigationModel } from '../../../../../shared/component-models/navigation-model';
import { Injectable } from '@angular/core';
import { CloudConfiguration } from '../cloud.configuration';

@Injectable()
export class CloudBuildingBlocksStrategy extends BuildingBlocksStrategyBase {

  constructor(private auxiliaryFigureStrategy: CloudAuxiliaryFigureStrategy,
    private trendStrategy: CloudTrendStrategy,
    private primaryMetricStrategy: CloudPrimaryMetricStrategy) { super(); }

  parse(model: BuildingBlockMetricSummary[]): BuildingBlockModel[] {
    const buildingBlockCards = new Array<BuildingBlockModel>();
    model.forEach((p) => {
      buildingBlockCards.push({
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
        metrics: this.mapCloudMetric(p),
        detail: this.mapNavigationModel(p),
        appCriticality: p.appCriticality
      });
    });

    return buildingBlockCards.sort(this.sortComponents);
  }

  private mapNavigationModel(buildingBlockMetricSummary: BuildingBlockMetricSummary): NavigationModel {
    const navigationModel = new NavigationModel;
    navigationModel.commands = ['product', buildingBlockMetricSummary.id];
    navigationModel.url = buildingBlockMetricSummary.url;
    return navigationModel;
  }

  private mapCloudMetric(buildingBlockMetricSummary: BuildingBlockMetricSummary): BuildingBlockMetricSummaryModel[] {
    const metric = buildingBlockMetricSummary.metrics.find(m => m.name === CloudConfiguration.identifier);
    if (!metric) {
      return [];
    }
    const mapSecondarys = mapSecondaryMetric(metric);

    if (mapSecondarys.value.value == null) {
      return [
        {
          value: this.primaryMetricStrategy.parse(metric.counts),
          trend: this.trendStrategy.parse(metric),
          isRatio: false,
          available: metric.dataAvailable == undefined ? true : metric.dataAvailable,
          message: "No Reports"
        }
      ];
    }
    return [
      {
        value: this.primaryMetricStrategy.parse(metric.counts),
        trend: this.trendStrategy.parse(metric),
        isRatio: false,
        available: metric.dataAvailable == undefined ? true : metric.dataAvailable,
        message: "No Reports"
      },
      mapSecondarys
    ];

    function mapSecondaryMetric(value) {

      if (value.counts == null || value.counts == undefined) {
        return {
          isEmpty: true,
          isRatio: false,
          value: { name: CloudConfiguration.auxilliaryIdentifier, value: null },
          trend: null,
          available: true,
          message: ''
        };
      }

      const migratedCount = value.counts
        .filter(i => i.label['type'] === "migrationEnabled")
        .map(c => c.value)
        .reduce((a, b) => a + b, 0);

      if (migratedCount > 0) {
        return {
          value: mapSecondary(migratedCount),
          trend: null,
          isRatio: false,
          available: true,
          message: ''
        };
      } else {
        return {
          isEmpty: true,
          isRatio: false,
          value: { name: CloudConfiguration.auxilliaryIdentifier, value: null },
          trend: null,
          available: true,
          message: ''
        };
      }
    }

    function mapSecondary(valueModel) {
      return {
        prefix: '',
        name: CloudConfiguration.auxilliaryIdentifier,
        value: valueModel,
        unit: ''
      };
    }
  }
}



