import {BuildingBlockMetricSummaryModel} from '../../../shared/component-models/building-block-metric-summary-model';
import {BuildingBlockMetricSummary} from '../../../shared/domain-models/building-block-metric-summary';
import {BuildingBlockModel} from '../../../shared/component-models/building-block-model';
import {TraceabilityTrendStrategy} from './traceability-trend-strategy';
import {TraceabilityPrimaryMetricStrategy} from './traceability-primary-metric-strategy';
import {BuildingBlocksStrategyBase} from '../../../shared/strategies/building-blocks-strategy-base';
import {Injectable} from '@angular/core';
import {NavigationModel} from '../../../../../shared/component-models/navigation-model';
import {TraceabilityConfiguration} from '../traceability.configuration';
import {TraceabilityAuxiliaryAutomatedFigureStrategy} from "./traceability-auxiliary-automated-figure-strategy";
import {TraceabilityAuxiliaryManualFigureStrategy} from "./traceability-auxiliary-manual-figure-strategy";


@Injectable()
export class TraceabilityBuildingBlocksStrategy extends BuildingBlocksStrategyBase {

  constructor (private primaryMetricStrategy: TraceabilityPrimaryMetricStrategy,
               private trendStrategy: TraceabilityTrendStrategy,
               private auxiliaryAutomatedFigureStrategy: TraceabilityAuxiliaryAutomatedFigureStrategy,
               private auxiliaryManualFigureStrategy: TraceabilityAuxiliaryManualFigureStrategy) { super(); }

  parse(model: BuildingBlockMetricSummary[]): BuildingBlockModel[] {
    const buildingBlocks = new Array<BuildingBlockModel>();
    model.forEach((p) => {
      buildingBlocks.push({
        id: p.id,
        name: p.name,
        commonName: p.commonName,
        dashboardDisplayName: p.dashboardDisplayName,
        lob: p.lob,
        poc: p.poc,
        itemType: p.itemType,
        reporting: this.mapReportingComponents(p),
        total: this.mapTotalComponents(p),
        completeness: this.mapCompleteness(p),
        lastScanned: this.mapLastScanned(p),
        metrics: this.mapTraceabilityMetric(p),
        detail: this.mapNavigationModel(p)
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

  private mapTraceabilityMetric(buildingBlockMetricSummary: BuildingBlockMetricSummary): BuildingBlockMetricSummaryModel[] {
      const metric = buildingBlockMetricSummary.metrics.find(m => m.name === TraceabilityConfiguration.identifier);

      if (!metric) {
          return [];
      }

      return [
          mapAutomatedMetric(this.auxiliaryAutomatedFigureStrategy.parse(metric)),
          mapManualMetric(this.auxiliaryManualFigureStrategy.parse(metric))
      ];

      function mapAutomatedMetric(value) {
          return {
                  value: mapAutomated(value),
                  trend: null,
                  isRatio: true
              };
      }


      function mapAutomated(valueModel) {
          return {

              name: TraceabilityConfiguration.auxilliaryAutomatedIdentifier,
              value: valueModel.value
          };
      }

      function mapManualMetric(value) {
          return {
              value: mapManual(value),
              trend: null,
              isRatio: true
          };

      }


      function mapManual(valueModel) {
          return {

              name: TraceabilityConfiguration.auxilliaryManualIdentifier,
              value: valueModel.value
          };
      }
  }
}
