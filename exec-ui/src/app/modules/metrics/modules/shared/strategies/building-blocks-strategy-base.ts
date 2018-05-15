import {Strategy} from '../../../../shared/strategies/strategy';
import {BuildingBlockModel} from '../component-models/building-block-model';
import { BuildingBlockMetricSummary } from '../domain-models/building-block-metric-summary';
import {PresentationFunctions} from '../utils/presentation-functions';

export abstract class BuildingBlocksStrategyBase implements Strategy<BuildingBlockMetricSummary[], BuildingBlockModel[]> {
  constructor() {}

  public abstract parse(model: BuildingBlockMetricSummary[]): BuildingBlockModel[];

  protected mapReportingComponents(productMetricSummary: BuildingBlockMetricSummary): number {
    if (!productMetricSummary.metrics) {
      return 0;
    }
    return productMetricSummary.reportingComponents;
  }

  protected mapTotalComponents(productMetricSummary: BuildingBlockMetricSummary): number {
    return productMetricSummary.totalExpectedMetrics * productMetricSummary.totalComponents;
  }

  protected mapCompleteness(productMetricSummary: BuildingBlockMetricSummary): number {
    const total = this.mapTotalComponents(productMetricSummary);
    const reporting = this.mapReportingComponents(productMetricSummary);
    return Math.round(reporting / total * 100);
  }

  protected mapLastScanned(productMetricSummary: BuildingBlockMetricSummary): string {
    if (!productMetricSummary.metrics) {
      return null;
    }

    const scanDates = productMetricSummary.metrics
      .map(x => x.lastScanned)
      .filter(x => x);

    if (!scanDates.length) {
      return null;
    }

    const oldestScan = scanDates.reduce((oldest, current) => oldest > current ? current : oldest);
    return PresentationFunctions.calculateLastScannedPresentation(oldestScan);
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
