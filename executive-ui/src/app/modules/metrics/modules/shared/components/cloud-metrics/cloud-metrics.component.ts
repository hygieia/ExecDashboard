import { Component, Input, OnChanges } from '@angular/core';
import { MetricDetailModel } from '../../component-models/metric-detail-model';
import { MetricBreakdownModel } from '../../component-models/metrics-breakdown-model';
import { MetricSegmentationModel } from '../../../shared/component-models/metric-segmentation-model';

@Component({
  selector: 'app-cloud-metrics',
  templateUrl: 'cloud-metrics.component.html',
  styleUrls: ['cloud-metrics.component.scss']
})
export class CloudMetricsComponent implements OnChanges {
  // @Input() public model: MetricSegmentationModel = null;
  @Input() public model: MetricDetailModel = null;
  public isSecurity: boolean = false;
  public isVelocity: boolean = false;
  public isCloud: boolean = false;
  public isEvent: boolean = false;
  public isWip: boolean = false;

  constructor() {

  }

  breakdownValues() {
    const cost = this.model.issues
      .filter(i => i.label['type'] === 'cost')
      .map(c => c.value)
      .reduce((a, b) => a + b, 0);

    const encryptedEbs = this.model.issues
      .filter(i => i.label['type'] === 'encryptedEBS')
      .map(c => c.value)
      .reduce((a, b) => a + b, 0);

    const unencryptedEbs = this.model.issues
      .filter(i => i.label['type'] === 'unencryptedEBS')
      .map(c => c.value)
      .reduce((a, b) => a + b, 0);

    const totalEbs = encryptedEbs + unencryptedEbs;
    let ebspercent = 0;
    if (totalEbs != 0)
      ebspercent = (encryptedEbs / totalEbs) * 100;

    const encryptedS3 = this.model.issues
      .filter(i => i.label['type'] === 'encryptedS3')
      .map(c => c.value)
      .reduce((a, b) => a + b, 0);
    const unencryptedS3 = this.model.issues
      .filter(i => i.label['type'] === 'unencryptedS3')
      .map(c => c.value)
      .reduce((a, b) => a + b, 0);

    const totalS3 = encryptedS3 + unencryptedS3;

    let s3percent = 0;
    if (totalS3 != 0)
      s3percent = (encryptedS3 / totalS3) * 100;
    const complaince = Math.round(ebspercent + s3percent) / 2;

    const breakdownValues: MetricBreakdownModel[] = [{
      heading: 'EBS Count',
      headingvalue: totalEbs,
      row1: 'Encrypted',
      row1value: encryptedEbs,
      row2: 'Unencrypted',
      row2value: unencryptedEbs
    },
    {
      heading: 'S3 Count',
      headingvalue: totalS3,
      row1: 'Encrypted',
      row1value: encryptedS3,
      row2: 'Unencrypted',
      row2value: unencryptedS3
    }];

    return breakdownValues;
  }

  costSavings() {
    const unusedElbCount = this.model.issues
      .filter(i => i.label['type'] === 'Total unusedElb')
      .map(c => c.value)
      .reduce((a, b) => a + b, 0);
    const unusedEbsCount = this.model.issues
      .filter(i => i.label['type'] === 'Total unusedEbs')
      .map(c => c.value)
      .reduce((a, b) => a + b, 0);
    const unusedEniCount = this.model.issues
      .filter(i => i.label['type'] === 'Total unusedEni')
      .map(c => c.value)
      .reduce((a, b) => a + b, 0);

    const rows = [
      { name: 'Idle ELB', order: 1, value: unusedElbCount },
      { name: 'Idle EBS', order: 2, value: unusedEbsCount },
      { name: 'Idle ENI', order: 3, value: unusedEniCount }
    ];
    return rows
      .sort((a, b) => a.order - b.order)
      .map(row => ({
        label: row.name,
        value: row.value
      }));
  }

  rowValues() {
    const elbCount = this.model.issues
      .filter(i => i.label['type'] === 'Total ELB Count')
      .map(c => c.value)
      .reduce((a, b) => a + b, 0);
    const rdsCount = this.model.issues
      .filter(i => i.label['type'] === 'Total RDS Count')
      .map(c => c.value)
      .reduce((a, b) => a + b, 0);
    const amiCount = this.model.issues
      .filter(i => i.label['type'] === 'Total AMI Count')
      .map(c => c.value)
      .reduce((a, b) => a + b, 0);

    const rows = [
      { name: 'EC2 Count', order: 1, value: amiCount },
      { name: 'RDS Count', order: 2, value: rdsCount },
      { name: 'ELB Count', order: 3, value: elbCount }
    ];
    return rows
      .sort((a, b) => a.order - b.order)
      .map(row => ({
        label: row.name,
        value: row.value
      }));
  }


  prodValues() {
    let prodenabled = 'No';
    const migratedCount = this.model.issues
      .filter(i => i.label['type'] === 'migrationEnabled')
      .map(c => c.value)
      .reduce((a, b) => a + b, 0);
    if (migratedCount > 0)
      prodenabled = 'Yes';
    const rows = [
      { name: 'Prod Migration Enabled', order: 1, value: prodenabled }
    ];
    return rows
      .sort((a, b) => a.order - b.order)
      .map(row => ({
        label: row.name,
        value: row.value
      }));
  }

  ngOnChanges() {
    this.prodValues();
    this.breakdownValues();
    this.rowValues();
  }
}

