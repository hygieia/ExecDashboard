import { Component, Input, OnChanges } from '@angular/core';
import { MetricSegmentationModel } from '../../component-models/metric-segmentation-model';

@Component({
  selector: 'app-metric-segmentation',
  templateUrl: 'metric-segmentation.component.html',
  styleUrls: ['metric-segmentation.component.scss']
})
export class MetricSegmentationComponent implements OnChanges {
  @Input() public model: MetricSegmentationModel = null;
  public isSecurity: boolean;
  public isVelocity: boolean;
  public isCloud: boolean;
  public isEvent: boolean;
  public isWip: boolean;
  public isRepo: boolean;
  public isQuality: boolean;
  public isBuildDeploy: boolean;
  public isSayDoRatio: boolean;
  public isTest: boolean;
  public isDevopscup: boolean;

  constructor() {
  }

  columnLabels() {

    if (!this.model) {
      return [];
    }

    const labels = this.model.rows[0].columns
      .sort((a, b) => b.order - a.order)
      .map(x => x.name);

    if (labels[0] === 'blackDuck' || labels[0] === 'overdue' || labels[0] === 'web' || labels[0] === 'code' || labels[0] === 'port') {
      this.isSecurity = true;
    }
    if (this.model.rows[0].name === 'Merged PR') {
      this.isRepo = true;
    }
    if (this.model.rows[0].name === 'Total Time' || this.model.rows[0].name === 'Story Points' || this.model.rows[0].name === 'Total Issues'
      || this.model.rows[0].name === 'User Stories' || this.model.rows[0].name === 'Enhancements' || this.model.rows[0].name === 'New Features') {
      this.isVelocity = true;
    }
    if (labels[0] === 'Impacted Applications') {
      this.isEvent = true;
    }
    if (this.model.rows[0].name === 'Story' || this.model.rows[0].name === 'Epic'
      || this.model.rows[0].name === 'Bugs' || this.model.rows[0].name === 'Other') {
      this.isWip = true;
    }
    if (this.model.rows[0].name === 'AWS Compliance Report') {
      this.isCloud = true;
    }
    if (this.model.rows[0].name === 'Prod Deploys' || this.model.rows[0].name === 'Total Build Executions') {
      this.isBuildDeploy = true;
    }
    if (this.model.rows[0].name === 'cmis defects') {
      this.isQuality = true;
    }
    if (this.model.rows[0].name === 'Integration/Functional (SOAP UI)' || this.model.rows[0].name === 'Integration/Functional (SELENIUM)'
      || this.model.rows[0].name === 'Integration/Functional (CUCUMBER)' || this.model.rows[0].name === 'Integration/Functional (HTML)') {
      this.isTest = true;
    }

    if (labels[0] === 'Committed' || labels[0] === 'Completed' || labels[0] === 'Ratio') {
      this.isSayDoRatio = true;
    }
    if (labels[0] === 'Points' || labels[0] === 'Improvements') {
      this.isDevopscup = true;
    }
    return labels[0] !== null ? labels : [];
  }

  rowValues() {
    if (!this.model) {
      return [];
    }
    return this.model.rows
      .sort((a, b) => a.order - b.order)
      .map(row => ({
        label: row.name,
        unit: row.unit,
        values: row.columns
          .sort((a, b) => b.order - a.order)
          .map(column => column.value)
      }));
  }

  rowValuesForQuality(metric: string) {
    if (!this.model) {
      return [];
    }
    return this.model.rows
      .sort((a, b) => a.order - b.order)
      .map(row => ({
        label: row.name,
        unit: row.unit,
        values: row.columns
          .sort((a, b) => b.order - a.order)
          .map(column => column.value)
      })).filter(row => row.label === metric);
  }

  rowValuesForQualityDetails(metric: string) {
    if (!this.model) {
      return [];
    }
    return this.model.rows
      .sort((a, b) => a.order - b.order)
      .map(row => ({
        label: row.name,
        unit: row.unit,
        values: row.columns
          .sort((a, b) => b.order - a.order)
          .map(column => column.value)
      })).filter(row => row.label.includes('P') && row.label.includes(metric));
  }

  ngOnChanges() {
    this.columnLabels();
    this.rowValues();

  }
}

