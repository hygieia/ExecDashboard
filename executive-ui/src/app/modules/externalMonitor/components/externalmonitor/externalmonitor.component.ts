import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import { ExternalSystemMonitor } from '../../../../../app/modules/shared/domain-models/externalSystemMonitor';
import { CollectorUpdatedStatus } from '../../../../../app/modules/shared/domain-models/collectorUpdatedStatus';
import { ExternalMonitorService } from '../../services/external-monitor.service';
import { AuthService } from '../../../../services/vz/auth.service';
import { TrackService } from '../../../../services/vz/track.service';
import { PatchDetail } from '../../../shared/domain-models/patchDetail';
import { SoftwareVersion } from '../../../shared/domain-models/softwareVersion';
import { PortfolioService } from '../../../shared/services/portfolio.service';

@Component({
  selector: 'app-externalMonitor',
  templateUrl: './externalmonitor.component.html',
  styleUrls: ['./externalmonitor.component.scss'],
  providers: [ExternalMonitorService]
})
export class ExternalMonitorComponent implements OnInit {
  
  public collectorUpdatedStatusList = new Array<CollectorUpdatedStatus>();
  public metricUpdatedStatusList = new Array<CollectorUpdatedStatus>();
  public softwareVersionDetails:SoftwareVersion= null;
  public user: string;
  public isAdmin = false;


  constructor(private externalMonitorService: ExternalMonitorService,
    private authService: AuthService,
    private trackService: TrackService) { }

  ngOnInit() {

      this.externalMonitorService.getCollectorUpdatedTimeStamps()
      .subscribe(
      result => {
        this.collectorUpdatedStatusList = result;

        this.collectorUpdatedStatusList.forEach((item) => {
          item.collectionUpdatedTime = item.collectionUpdatedTime != 0 ? new Date(item.collectionUpdatedTime).getTime() : 0;
          item.collectorUpdateTime = item.collectorUpdateTime != 0 ? new Date(item.collectorUpdateTime).getTime() : 0;
        })

        
      },
      error => {
        console.log(error);
      }
      );

      this.externalMonitorService.getMetricCollectorUpdatedTimeStamps()
      .subscribe(
      result => {
        this.metricUpdatedStatusList = result;

        this.metricUpdatedStatusList.forEach((item) => {
          item.collectorStartTime = item.collectorStartTime != 0 ? new Date(item.collectorStartTime).getTime() : 0;
          item.collectorUpdateTime = item.collectorUpdateTime != 0 ? new Date(item.collectorUpdateTime).getTime() : 0;
        })
        
      },
      error => {
        console.log(error);
      }
      );

      this.authService.currentAuthData.subscribe(data => {this.user = data['eid']});
      this.authService.isAdmin(this.user).subscribe(data=> {if(data == true) {this.isAdmin = true}});
      this.trackMonitorPage();
  }

  
  trackMonitorPage() {
    this.trackService.savePageTracking('External Monitor View', this.authService.getAuthEid()).subscribe(
      result => {
      },
      error => {
      }
    );
  }
  
}
