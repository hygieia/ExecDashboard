import { MetricPreviewModel } from '../../../shared/component-models/metric-preview-model';
import { MetricSummary } from '../../../shared/domain-models/metric-summary';
import { PresentationFunctions } from '../../../shared/utils/presentation-functions';
import { DevopscupPrimaryMetricStrategy } from './devopscup-primary-metric-strategy';
import { DevopscupTrendStrategy } from './devopscup-trend-strategy';
import { PreviewStrategyBase } from '../../../shared/strategies/preview-strategy-base';
import { Injectable } from '@angular/core';
import { DevopsCupConfiguration } from '../devopscup.configuration';

@Injectable()
export class DevopscupPreviewStrategy extends PreviewStrategyBase {

  constructor(private primaryMetricStrategy: DevopscupPrimaryMetricStrategy,
    private trendStrategy: DevopscupTrendStrategy) { super(); }

  public parse(model: MetricSummary): MetricPreviewModel {
    const metricPreview = new MetricPreviewModel();
    metricPreview.description = DevopsCupConfiguration.description;
    metricPreview.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.lastScanned);
    metricPreview.id = DevopsCupConfiguration.identifier;
    metricPreview.score = mapPrimary(this.primaryMetricStrategy.parse(model.counts));
    metricPreview.totalReporting = model.reportingComponents / model.totalComponents;
    metricPreview.trend = this.trendStrategy.parse(model);
    metricPreview.secondaryMetrics = this.calculateSecondaryMetric(model);
    metricPreview.available = true;
    metricPreview.message = 'No Reports';
    metricPreview.dataSource = DevopsCupConfiguration.dataSource;
    return metricPreview;


    function mapPrimary(valueModel) {
      return {
        name: DevopsCupConfiguration.previewHeading,
        value: model.reportingComponents,
        prefix: '',
        unit: '',
        unitKey: 'Out of ' + model.totalComponents + ' teams'
      };
    }




  }

  private calculateSecondaryMetric(model: MetricSummary) {
    const sums = model.counts.reduce((runningSums, count) => {
      if (!runningSums.has(count.label['type'])) {
        runningSums.set(count.label['type'], 0);
      }
      const newCount = runningSums.get(count.label['type']) + count.value;
      return runningSums.set(count.label['type'], newCount);
    }, new Map());

    var enggProgress = sums.get('enggImprovements').toFixed(2);
    var cloudProgress = sums.get('cloudImprovements').toFixed(2);


    return [{ name: 'Engineering Progress', value: enggProgress + '%' }, { name: 'Cloud Progress', value: cloudProgress + '%' }];
  }
}
