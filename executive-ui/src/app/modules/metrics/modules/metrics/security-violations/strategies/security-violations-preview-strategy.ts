import {MetricPreviewModel} from '../../../shared/component-models/metric-preview-model';
import {MetricSummary} from '../../../shared/domain-models/metric-summary';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {SecurityViolationsPrimaryMetricStrategy} from './security-violations-primary-metric-strategy';
import {SecurityViolationsTrendStrategy} from './security-violations-trend-strategy';
import {PreviewStrategyBase} from '../../../shared/strategies/preview-strategy-base';
import {Injectable} from '@angular/core';
import {SecurityViolationsConfiguration} from '../security-violations.configuration';

@Injectable()
export class SecurityViolationsPreviewStrategy extends PreviewStrategyBase {

  constructor (private primaryMetricStrategy: SecurityViolationsPrimaryMetricStrategy,
               private trendStrategy: SecurityViolationsTrendStrategy) { super(); }

  public parse(model: MetricSummary): MetricPreviewModel {
    const metricPreview = new MetricPreviewModel();
    metricPreview.description = SecurityViolationsConfiguration.description;
    metricPreview.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.lastScanned);
    metricPreview.id = SecurityViolationsConfiguration.identifier;
    metricPreview.score = mapPrimary(this.primaryMetricStrategy.parse(model.counts));
    metricPreview.totalReporting = model.reportingComponents / model.totalComponents;
    metricPreview.trend = this.trendStrategy.parse(model);
    metricPreview.secondaryMetrics = this.calculateSecondaryMetric(model);
    metricPreview.available = model.dataAvailable == undefined ? true : model.dataAvailable;
    metricPreview.message = model.confMessage;
    metricPreview.dataSource = SecurityViolationsConfiguration.dataSource;
    return metricPreview;

    function mapPrimary(valueModel) {
      return {
        name: SecurityViolationsConfiguration.previewHeading,
        value: formattedData(valueModel.value),
        unit: getUnit(valueModel.value),
        unitKey: 'Vulnerabilities'
      };
    }
    
    function formattedData(value){
      if (value >= 1000) {
        return Math.round(value / 1000);
      }
      return Math.round(value);
  	}

    function getUnit(value){
      if (value >= 1000) {
        return 'k';
      }
      return '';
    }
  } 

  private calculateSecondaryMetric(model: MetricSummary) {
    const sums = model.counts.reduce((runningSums, count) => {
      if (!runningSums.has(count.label['severity'])) {
        runningSums.set(count.label['severity'], 0);
      }
      const newCount = runningSums.get(count.label['severity']) + count.value;
      return runningSums.set(count.label['severity'], newCount);
    }, new Map());
   
    // if (sums.get('codeCritical')) {
    //   console.log("inside critical");
    //   var codeValue = sums.get('codeCritical') + sums.get('webCritical') + sums.get('portCritical')+sums.get('blackDuckCritical');
    //   if (codeValue >= 1000) {
    //     codeValue = Math.round(codeValue / 1000) + ' k';
    //   }
    //   if (sums.get('codeBlocker')) {
    //   	var value = sums.get('codeBlocker')+sums.get('webBlocker')+sums.get('portBlocker')+sums.get('blackDuckBlocker');
    //   	if (value >= 1000) {
    //     	value = Math.round(value / 1000) + ' k';
    //   	}
    //   	return [{name: 'critical', value: codeValue.toLocaleString()},{name: 'high', value: value.toLocaleString()}];
    //   }else{
    //  	 return [{name: 'critical', value: codeValue.toLocaleString()}];
    //   }
    // }

    // if (sums.get('codeBlocker')) {
    //   console.log("inside Blocker");
    //   var value = sums.get('codeBlocker')+sums.get('webBlocker')+sums.get('portBlocker')+sums.get('blackDuckBlocker');
    //   if (value >= 1000) {
    //     value = Math.round(value / 1000) + ' k';
    //   }
    //   return [{name: 'high', value: value.toLocaleString()}];
    // }

    // if (sums.get('codeMajor')) {
    //   console.log("inside Major");
    //   var value = sums.get('codeMajor')+sums.get('webMajor')+sums.get('portMajor')+sums.get('blackDuckMajor');
    //   if (value >= 1000) {
    //     value = Math.round(value / 1000) + ' k';
    //   }
    //   return [{name: 'medium', value: value.toLocaleString()}];
    // }

    //return [];

    var codeValue = sums.get('codeCritical') + sums.get('webCritical') + sums.get('portCritical') + sums.get('blackDuckCritical');
    
      if (codeValue >= 1000) {
      codeValue = Math.round(codeValue / 1000) + ' k';
    }

    return [{name: 'critical', value: codeValue.toLocaleString()}];


  }
}

