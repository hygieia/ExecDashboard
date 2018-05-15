import {BuildingBlockMetricSummaryModel} from '../../shared/component-models/building-block-metric-summary-model';
import {BuildingBlockMetricSummary} from '../../shared/domain-models/building-block-metric-summary';
import {BuildingBlockModel} from '../../shared/component-models/building-block-model';
import {PresentationFunctions} from '../../shared/utils/presentation-functions';
import {Strategy} from '../../../../shared/strategies/strategy';
import {NavigationModel} from '../../../../shared/component-models/navigation-model';
import {Injectable} from '@angular/core';
import {MetricMapService} from '../../shared/services/metric-map.service';

@Injectable()
export class MetricBuildingBlocksMapStrategy implements Strategy<BuildingBlockMetricSummary[], Map<string, BuildingBlockModel[]>> {

  constructor(private metricMapService: MetricMapService) { }

  parse(model: BuildingBlockMetricSummary[]): Map<string, BuildingBlockModel[]> {
    const map = new Map<string, BuildingBlockModel[]>();
    for (const metricType of Array.from(this.metricMapService.metrics().keys())) {
      map.set(metricType, model
        .map(p => ({
          id: p.id,
          name: p.name,
          commonName: p.commonName,
          dashboardDisplayName: p.dashboardDisplayName,
          lob: p.lob,
          poc: p.poc,
          itemType: p.itemType,
          reporting: p.reportingComponents,
          total: p.totalComponents,
          completeness: this.mapCompleteness(p, metricType),
          lastScanned: this.mapLastScanned(p, metricType),
          metrics: this.mapMetric(p, metricType),
          detail: this.mapNavigationModel(p, metricType),
        }))
        .sort(this.sortComponents));
    }

    return map;
  }

  protected mapCompleteness(productMetricSummary: BuildingBlockMetricSummary, metricType: string): number {
    const total = productMetricSummary.totalComponents;
    const reporting = productMetricSummary.reportingComponents;

    return (total > 0) ? Math.round(reporting / total * 100) : 0;
  }

  protected mapLastScanned(productMetricSummary: BuildingBlockMetricSummary, metricType: string): string {
    const metric = productMetricSummary.metrics.find(m => m.name === metricType);
    return metric ? PresentationFunctions.calculateLastScannedPresentation(metric.lastScanned) : null;
  }

  private mapMetric(buildingBlockMetricSummary: BuildingBlockMetricSummary, metricName: string): BuildingBlockMetricSummaryModel[] {
    const metrics = buildingBlockMetricSummary.metrics
      .filter((metric) => {
        return metric.name === metricName;
      })
      .map((metric) => {
        const metricMapEntry = this.metricMapService.metrics().get(metricName);
        if (metricMapEntry.buildingBlockPrimaryMetricStrategy) {
          return metricMapEntry.buildingBlockPrimaryMetricStrategy.parse(metric);
        }

        return {
          isRatio: this.metricMapService.metrics().get(metricName).isRatio,
          value: this.metricMapService.metrics().get(metricName).primaryMetricStrategy.parse(metric.counts),
          trend: this.metricMapService.metrics().get(metricName).trendStrategy.parse(metric)
        };
      });

    return metrics.length ? metrics : [];
  }

  private mapNavigationModel(buildingBlockMetricSummary: BuildingBlockMetricSummary, metricName: string): NavigationModel {
    const navigationModel = new NavigationModel;
    const metric = buildingBlockMetricSummary.metrics.find(m => m.name === metricName);

    if (metric) {
      navigationModel.commands = [metric.name, 'product', buildingBlockMetricSummary.id];
    } else {
      navigationModel.commands = [];
    }
    navigationModel.url = buildingBlockMetricSummary.url;

    return navigationModel;
  }

  protected mapComponentUrl(buildingBlockMetricSummary: BuildingBlockMetricSummary): string {
    return buildingBlockMetricSummary.url;
  }

  protected sortComponents(a, b) {
    const timeUnitMap = new Map([
      ['d', 1],
      ['hr', 2],
      ['hrs', 2],
      ['m', 3]
    ]);

    if (!a.metrics.length && !b.metrics.length) {
      if (a.name.toLowerCase() > b.name.toLowerCase()) {
        return 1;
      } else if (a.name.toLowerCase() < b.name.toLowerCase()) {
        return -1;
      } else {
        return 0;
      }
    }

    if (!a.metrics.length || !b.metrics.length) {
      if (a.metrics.length) {
        return -1;
      } else if (b.metrics.length) {
        return 1;
      } else {
        return 0;
      }
    }

    const unitA = timeUnitMap.get(a.metrics[0].value.unit);
    const unitB = timeUnitMap.get(b.metrics[0].value.unit);

    if ((unitA && unitB) && (unitA !== unitB)) {
      return unitA - unitB;
    }

    if (a.metrics[0].value.value > b.metrics[0].value.value) {
      return -1;
    } else if (a.metrics[0].value.value < b.metrics[0].value.value) {
      return 1;
    } else {
      if (a.name.toLowerCase() > b.name.toLowerCase()) {
        return 1;
      } else if (a.name.toLowerCase() < b.name.toLowerCase()) {
        return -1;
      } else {
        return 0;
      }
    }
  }
}
