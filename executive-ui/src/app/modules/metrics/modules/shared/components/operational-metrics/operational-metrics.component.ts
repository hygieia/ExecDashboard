import { Component, Input, OnChanges } from '@angular/core';
import { OperationalMetricsModel } from '../../../shared/component-models/operational-metrics-model';
import { MttrModel } from '../../../shared/component-models/mttr-model';
import { saveAs } from 'file-saver/FileSaver';

@Component({
  selector: 'app-operational-metrics',
  templateUrl: './operational-metrics.component.html',
  styleUrls: ['./operational-metrics.component.scss']
})
export class OperationalMetricsComponent implements OnChanges {
  @Input() public model: OperationalMetricsModel[] = null;
  @Input() public outageModel: MttrModel[] = null;
  @Input() public eventModel: MttrModel[] = null;
  public pageNumberEvent: number;
  public sortType: String = 'category';
  public sortReverse: Boolean = true;
  constructor() { }

  ngOnChanges() {
    this.pageNumberEvent = 1;
  }

  downloadExcel() {
    const header = 'Root Cause Breakdown\n\n ';
    let content = 'Category, Impacted Portfolios, Impacted Apps, Total Outages, Outage MTTR (min), Total Events, Event MTTR (min)\n';
    content += this.getOperationalMetrics();
    content += '\n\nOutages Breakdown \n\n';
    content += 'CrisisId, Impacted Apps, Root Cause, MTTR (min), Change Activity, MIP, Outage Date\n';
    content += this.getOutageMetrics();
    content += '\n\nEvents Breakdown \n\n';
    content += 'CrisisId, Impacted Apps, Root Cause, MTTR (min), Change Activity, MIP, Event Date\n';
    content += this.getEventMetrics();

    const blob = new Blob([header + content], { type: 'text/csv;charset=utf-8' });
    saveAs(blob, 'ExecutiveMetricsReport.csv');
  }

  getOperationalMetrics() {
    let operationalMetrics = '';
    if (this.model !== undefined && this.model.length > 0) {
      this.model.forEach(k => {
        operationalMetrics += k.category + ',';
        operationalMetrics += this.getString(k.impactedOrgs) + ',';
        operationalMetrics += this.getString(k.impactedApps) + ',';
        operationalMetrics += k.outages + ',';
        operationalMetrics += k.outageMTTR + ',';
        operationalMetrics += k.events + ',';
        operationalMetrics += k.eventMTTR + '\n';
      });
    }
    return operationalMetrics;
  }

  getOutageMetrics() {
    let outageMetrics = '';
    if (this.outageModel !== undefined && this.outageModel.length > 0) {
      this.outageModel.forEach(k => {
        outageMetrics += k.crisisId + ',';
        if (k.appIdList !== undefined && k.appIdList.length > 0) {
          outageMetrics += this.getString(k.appIdList) + ',';
        } else {
          outageMetrics += k.appId + ',';
        }
        outageMetrics += k.causeCode + ',';
        outageMetrics += k.itduration + ',';
        outageMetrics += k.changeNumber + ',';
        outageMetrics += k.mipNumber + ',';
        outageMetrics += new Date(k.timeStamp).toDateString() + '\n';
      });
    }
    return outageMetrics;
  }

  getEventMetrics() {
    let eventMetrics = '';
    if (this.eventModel !== undefined && this.eventModel.length > 0) {
      this.eventModel.forEach(k => {
        eventMetrics += k.crisisId + ',';
        if (k.appIdList !== undefined && k.appIdList.length > 0) {
          eventMetrics += this.getString(k.appIdList) + ',';
        } else {
          eventMetrics += k.appId + ',';
        }
        eventMetrics += k.causeCode + ',';
        eventMetrics += k.itduration + ',';
        eventMetrics += k.changeNumber + ',';
        eventMetrics += k.mipNumber + ',';
        eventMetrics += new Date(k.timeStamp).toDateString() + '\n';
      });
    }
    return eventMetrics;
  }

  getString(data) {
    let datum = '';
    if (data !== undefined && data.length > 0) {
      data.forEach(k => {
        datum += k + ' ; ';
      });
      return datum;
    }
  }

  sortOperationalMetrics(value: string) {
    if (value !== undefined) {
      this.sortType = value;
      this.model = this.sortByKey(this.model, value, this.sortReverse);
      this.sortReverse = !this.sortReverse;
    }
  }

  sortOutageMetrics(value: string) {
    if (value !== undefined) {
      this.sortType = value + 'Out';
      this.outageModel = this.sortByKey(this.outageModel, value, this.sortReverse);
      this.sortReverse = !this.sortReverse;
    }
  }

  sortEventMetrics(value: string) {
    if (value !== undefined) {
      this.sortType = value + 'Evt';
      this.eventModel = this.sortByKey(this.eventModel, value, this.sortReverse);
      this.sortReverse = !this.sortReverse;
    }
  }

  sortByKey(array, key, sortOrder) {
      if (sortOrder) {
        return array.sort(function (a, b) {
          const x = a[key]; const y = b[key];
          console.log(x, y);
          return ((x < y) ? -1 : ((x > y) ? 1 : 0));
        });
      } else {
        return array.sort(function (a, b) {
          const x = a[key]; const y = b[key];
          console.log(x, y);
          return ((x < y) ? 1 : ((x > y) ? -1 : 0));
        });
      }
    }

}
