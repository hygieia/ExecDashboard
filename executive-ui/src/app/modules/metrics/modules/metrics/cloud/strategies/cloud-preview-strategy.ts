import { MetricPreviewModel } from '../../../shared/component-models/metric-preview-model';
import { MetricValueModel } from '../../../shared/component-models/metric-value-model';
import { MetricTrendModel } from '../../../shared/component-models/metric-trend-model';
import { MetricSummary } from '../../../shared/domain-models/metric-summary';
import { PresentationFunctions } from '../../../shared/utils/presentation-functions';
import { CloudPrimaryMetricStrategy } from './cloud-primary-metric-strategy';
import { CloudTrendStrategy } from './cloud-trend-strategy';
import { PreviewStrategyBase } from '../../../shared/strategies/preview-strategy-base';
import { CloudAuxiliaryFigureStrategy } from './cloud-auxiliary-figure-strategy';
import { Injectable } from '@angular/core';
import { CloudConfiguration } from '../cloud.configuration';

@Injectable()
export class CloudPreviewStrategy extends PreviewStrategyBase {

  constructor(private auxiliaryFigureStrategy: CloudAuxiliaryFigureStrategy,
    private primaryMetricStrategy: CloudPrimaryMetricStrategy,
    private trendStrategy: CloudTrendStrategy) { super(); }

  public parse(model: MetricSummary): MetricPreviewModel {

    const metricPreview = new MetricPreviewModel();
    metricPreview.description = CloudConfiguration.description;
    metricPreview.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.lastScanned);
    metricPreview.id = CloudConfiguration.identifier;
    metricPreview.score = mapPrimary(this.primaryMetricStrategy.parse(model.counts));
    metricPreview.totalReporting = model.reportingComponents / model.totalComponents;
    metricPreview.trend = this.trendStrategy.parse(model);
    metricPreview.secondaryMetrics = this.calculateSecondaryMetric(model);
    metricPreview.available = model.dataAvailable == undefined ? true : model.dataAvailable;
    metricPreview.dataSource = CloudConfiguration.dataSource;
    metricPreview.message = 'NA';

    return metricPreview;

    function mapPrimary(valueModel) {
      return {
        prefix: '$',
        name: CloudConfiguration.previewHeading,
        value: formattedData(valueModel.value),
        unit: getUnit(valueModel.value),
        unitKey: getmonth()
      };
      function formattedData(value) {
        if (value >= 100000) {
          var a = (value / 1000000);
          return a.toFixed(1);

        }
        if (value >= 1000) {
          return Math.round(value / 1000);
        }
        return Math.round(value);
      }


      function getUnit(value) {
        if (value >= 100000) {
          return 'M';
        }
        if (value >= 1000) {
          return 'k';
        }
        return '';
      }


      function getmonth() {
        const d = new Date();
        const monthNames = ['December', 'January', 'February', 'March', 'April', 'May', 'June',
          'July', 'August', 'September', 'October', 'November'
        ];
        return monthNames[d.getMonth()] + ' Cost';
      }
    }
  }


  private calculateSecondaryMetric(model: MetricSummary) {
    const cost = model.counts
      .filter(i => i.label['type'] === 'cost')
      .map(c => c.value)
      .reduce((a, b) => a + b, 0);

    const unencryptedEbs = model.counts
      .filter(i => i.label['type'] === 'unencryptedEBS')
      .map(c => c.value)
      .reduce((a, b) => a + b, 0);

    const encryptedEbs = model.counts
      .filter(i => i.label['type'] === 'encryptedEBS')
      .map(c => c.value)
      .reduce((a, b) => a + b, 0);

    const totalEbs = encryptedEbs + unencryptedEbs;

    let ebspercent = 0;
    if (totalEbs != 0)
      ebspercent = (encryptedEbs / totalEbs) * 100;
    const unencryptedS3 = model.counts
      .filter(i => i.label['type'] === 'unencryptedS3')
      .map(c => c.value)
      .reduce((a, b) => a + b, 0);

    const encryptedS3 = model.counts
      .filter(i => i.label['type'] === 'encryptedS3')
      .map(c => c.value)
      .reduce((a, b) => a + b, 0);

    const totalS3 = encryptedS3 + unencryptedS3;
    let s3percent = 0;
    if (totalS3 != 0)
      s3percent = (encryptedS3 / totalS3) * 100;
    const compliance = (Math.round(ebspercent + s3percent) / 2) + '%';

    const migratedCount = model.counts
      .filter(i => i.label['type'] === 'migrationEnabled')
      .map(c => c.value)
      .reduce((a, b) => a + b, 0);

    const costOptimizedApps = model.counts
      .filter(i => i.label['type'] === 'costOptimized')
      .map(c => c.value)
      .reduce((a, b) => a + b, 0);

    const cloud = this.auxiliaryFigureStrategy.parse(model);
    const cloudDisplay = !cloud.hasData ? [] : [{
      name: 'Cloud',
      value: cloud.value,
      unit: '$'
    }];

    return [{ name: 'AWS Compliance Report', value: compliance },
    { name: 'Prod Migration Enabled Apps', value: migratedCount },
    ...cloudDisplay];
  }
}



