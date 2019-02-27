import { MetricMapService } from '../../shared/services/metric-map.service';
import {BuildingBlockMetricSummaryModel} from '../../shared/component-models/building-block-metric-summary-model';
import {BuildingBlockMetricSummary} from '../../shared/domain-models/building-block-metric-summary';
import {BuildingBlockModel} from '../../shared/component-models/building-block-model';
import {BuildingBlocksStrategyBase} from '../../shared/strategies/building-blocks-strategy-base';
import {Injectable} from '@angular/core';
import {NavigationModel} from '../../../../shared/component-models/navigation-model';

@Injectable()
export class ProductListProductsStrategy extends BuildingBlocksStrategyBase {

  constructor(private metricMapService: MetricMapService) { super(); }

  parse(model: BuildingBlockMetricSummary[]): BuildingBlockModel[] {
    const products = [];
    model.forEach((p) => {
      products.push({
        id: p.id,
        name: p.name,
        commonName: p.commonName,
        lob: p.lob,
        poc: p.poc,
        reporting: this.mapReportingComponents(p),
        total: this.mapTotalComponents(p),
        completeness: this.mapCompleteness(p),
        lastScanned: this.mapLastScanned(p),
        metrics: this.mapMetrics(p),
        detail: this.mapNavigationModel(p),
      });
    });

    return products.sort(this.sortComponents);
  }

  private mapMetrics(buildingBlockMetricSummary: BuildingBlockMetricSummary): BuildingBlockMetricSummaryModel[] {
    return Array.from(this.metricMapService.metrics().keys()).map((metric) => {
      return this.mapMetric(buildingBlockMetricSummary, metric);
    });
  }

  private mapMetric(buildingBlockMetricSummary: BuildingBlockMetricSummary, metricName: string): BuildingBlockMetricSummaryModel {
    const metric = this.metricMapService.metrics().get(metricName);
    const metrics = buildingBlockMetricSummary.metrics
      .filter((metricData) => {
        return metricData.name === metricName;
      })
      .map((metricData) => {
        if (metric.buildingBlockPrimaryMetricStrategy) {
          return metric.buildingBlockPrimaryMetricStrategy.parse(metricData);
        } else {
          return {
            isRatio: metric.isRatio,
            name: metricData.name,
            unit: null,
            value: metric.primaryMetricStrategy.parse(metricData.counts),
            trend: metric.trendStrategy.parse(metricData)
          };
        }
      });

    return metrics.length ? metrics[0] : this.emptyMetric(metricName, metric.label);
  }

  private emptyMetric(name: string, label: string) {
    return {
      isEmpty: true,
      isRatio: false,
      unit: null,
      name: name,
      value: {
        value: null,
        name: label
      },
      trend: {}
    };
  }

  private mapNavigationModel(buildingBlockMetricSummary: BuildingBlockMetricSummary): NavigationModel {
    const navigationModel = new NavigationModel;
    navigationModel.commands = ['..', 'product', buildingBlockMetricSummary.id];
    navigationModel.url = buildingBlockMetricSummary.url;
    return navigationModel;
  }

  protected sortComponents(a, b) {
    if (!a.lob && !b.lob) {
      if (a.name.toLowerCase() > b.name.toLowerCase()) {
        return 1;
      } else if (a.name.toLowerCase() < b.name.toLowerCase()) {
        return -1;
      } else {
        return 0;
      }
    }

    if (!a.lob || !b.lob) {
      if (a.lob) {
        return -1;
      } else {
        return 1;
      }
    }

    if (a.lob.toLowerCase() > b.lob.toLowerCase()) {
      return 1;
    } else if (a.lob.toLowerCase() < b.lob.toLowerCase()) {
      return -1;
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
