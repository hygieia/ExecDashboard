import {BuildingBlockMetricSummaryModel} from '../../../shared/component-models/building-block-metric-summary-model';
import {BuildingBlockMetricSummary} from '../../../shared/domain-models/building-block-metric-summary';
import {BuildingBlockModel} from '../../../shared/component-models/building-block-model';
import {PerformanceTestTrendStrategy} from './performance-test-trend-strategy';
import {PerformanceTestPrimaryMetricStrategy} from './performance-test-primary-metric-strategy';
import {BuildingBlocksStrategyBase} from '../../../shared/strategies/building-blocks-strategy-base';
import {Injectable} from '@angular/core';
import {NavigationModel} from '../../../../../shared/component-models/navigation-model';
import {PerformanceTestConfiguration} from '../performance-test.configuration';
import {PerformanceTestAuxiliaryErrorRateFigureStrategy} from "./performance-test-auxiliary-error-rate-figure-strategy";
import {PerformanceTestAuxiliaryResponseTimeFigureStrategy} from "./performance-test-auxiliary-response-time-figure-strategy";
import {PerformanceTestAuxiliaryTPSFigureStrategy} from "./performance-test-auxiliary-tps-figure-strategy";

@Injectable()
export class PerformanceTestBuildingBlocksStrategy extends BuildingBlocksStrategyBase {

  constructor (private primaryMetricStrategy: PerformanceTestPrimaryMetricStrategy,
               private trendStrategy: PerformanceTestTrendStrategy,
               private auxiliaryErrorRateFigureStrategy: PerformanceTestAuxiliaryErrorRateFigureStrategy,
               private auxiliaryResponseTimeFigureStrategy: PerformanceTestAuxiliaryResponseTimeFigureStrategy,
               private auxiliaryTPSFigureStrategy: PerformanceTestAuxiliaryTPSFigureStrategy) { super(); }

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
        metrics: this.mapPerformanceMetric(p),
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


    private mapPerformanceMetric(buildingBlockMetricSummary: BuildingBlockMetricSummary): BuildingBlockMetricSummaryModel[] {
        const metric = buildingBlockMetricSummary.metrics.find(m => m.name === PerformanceTestConfiguration.identifier);

        if (!metric) {
            return [];
        }

        return [
            mapErrorRateMetric(this.auxiliaryErrorRateFigureStrategy.parse(metric)),
            mapResponseTimeMetric(this.auxiliaryResponseTimeFigureStrategy.parse(metric)),
            mapTPSMetric(this.auxiliaryTPSFigureStrategy.parse(metric))
        ];

        function mapErrorRateMetric(value) {
            return {
                value: mapErrorRate(value),
                trend: null,
                isRatio: true
            };
        }


        function mapErrorRate(valueModel) {
            return {

                name: PerformanceTestConfiguration.auxilliaryErrorRateIdentifier,
                value: valueModel.value
            };
        }

        function mapResponseTimeMetric(value) {
            return {
                value: mapResponseTime(value),
                trend: null,
                isRatio: false
            };
        }


        function mapResponseTime(valueModel) {
            return {

                name: PerformanceTestConfiguration.auxilliaryResponseTimeIdentifier,
                value: valueModel.value
            };
        }

        function mapTPSMetric(value) {
            return {
                value: mapTPS(value),
                trend: null,
                isRatio: false
            };
        }


        function mapTPS(valueModel) {
            return {

                name: PerformanceTestConfiguration.auxilliaryTPSIdentifier,
                value: valueModel.value
            };
        }


    }
}

