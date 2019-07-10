import { Component, OnInit } from '@angular/core';
import { UserTrackService } from '../../services/user-track.service';
import { MetricGraphModel } from '../../../metrics/modules/shared/component-models/metric-graph-model';
import { MetricTrendModel } from '../../../metrics/modules/shared/component-models/metric-trend-model';
import { MetricValueModel } from '../../../metrics/modules/shared/component-models/metric-value-model';
import { MetricGraphComponent } from '../../../metrics/modules/shared/components/metric-graph/metric-graph.component';
import { ExecutiveStatus } from '../../../../../app/modules/shared/domain-models/executiveStatus';
import { Router } from '@angular/router';
import { AuthService } from '../../../../services/vz/auth.service';
import { TrackService } from '../../../../services/vz/track.service';

@Component({
  selector: 'app-status',
  templateUrl: './status.component.html',
  styleUrls: ['./status.component.scss'],
  providers: [
    UserTrackService]
})
export class StatusComponent implements OnInit {

  private hitsOfTheData = new Map();
  private recentVisitors = {};
  private recentVisitorsModified = [];
  private recentAccessedExecutives = [];
  private recentAccessedCards = [];
  private recentAccessedApplications = [];
  public executiveList = new Array<ExecutiveStatus>();
  public executiveListMap = new Array<ExecutiveStatus>();
  private uniqueHits = 0;
  private totalHits = 0;
  public pageNumber: number;
  private metricToBuildingBlocksMap = [];
  public sortReverse: Boolean = true;

  constructor(private userTrackService: UserTrackService,
    private authService: AuthService,
    private trackService: TrackService) { }

  ngOnInit() {

    this.pageNumber = 1;
    this.userTrackService.getHitsForTheDay()
      .subscribe(
      result => {
        Object.keys(result).forEach(key => {
          if (key === 'uniqueHits') {
            this.uniqueHits = result[key];
          }
          if (key === 'totalHits') {
            this.totalHits = result[key];
          }
        });
      },
      error => {
        console.log(error);
      }
      );

    this.userTrackService.getExecutivesListAccessed()
      .subscribe(
      result => {
        this.executiveList = result;
        this.sort();
        this.sort();
      },
      error => {
        console.log(error);
      }
      );

    this.getRecentVisitors();
    this.getRecentExecutives();
    this.getRecentApps();
    this.getRecentCards();
    this.trackStatusPage();
  }

  getRecentExecutives() {
    this.recentAccessedExecutives = [];
    this.trackService.getFrequentExecutives()
      .subscribe(
      result => {
          this.recentAccessedExecutives = result;
      },
      error => {
        console.log(error);
      }
      );
  }

  getRecentApps() {
    this.recentAccessedApplications = [];
    this.trackService.getFrequentApplications()
      .subscribe(
      result => {
          this.recentAccessedApplications = result;
      },
      error => {
        console.log(error);
      }
      );
  }

  getRecentCards() {
    this.recentAccessedCards = [];
    this.trackService.getFrequentCards()
      .subscribe(
      result => {
          this.recentAccessedCards = result;
      },
      error => {
        console.log(error);
      }
      );
  }

  getRecentVisitors() {
    this.recentAccessedExecutives = [];
    this.trackService.getFrequentUsers()
      .subscribe(
      result => {
        this.recentVisitors = result;
        for (const key in this.recentVisitors) {
          this.recentVisitorsModified.push({ 'id': key, 'itemName': this.recentVisitors[key] });
        }
      },
      error => {
        console.log(error);
      }
      );
  }

  trackStatusPage() {
    this.trackService.savePageTracking('Status View', this.authService.getAuthEid()).subscribe(
      result => {
      },
      error => {
      }
    );
  }

  sort() {
    if (this.executiveList !== undefined) {

      this.metricToBuildingBlocksMap = [];
      this.executiveListMap = this.executiveList;
      if (this.executiveList !== undefined) {
        for (let i = 0; i < this.executiveList.length; i++) {
          this.metricToBuildingBlocksMap.push({ model: this.executiveList[i], value: this.executiveList[i].lastAccessed });
        }
      }
    }

    if (this.metricToBuildingBlocksMap.length > 0) {
      const temp = new Array();
      if (this.sortReverse) {
        this.metricToBuildingBlocksMap.sort(function (a, b) {
          return a.value - b.value;
        });

        this.metricToBuildingBlocksMap.forEach(function (element) {
          temp.push(element.model);
        });
        this.executiveListMap = temp;

      } else {
        const temp1 = new Array();
        this.metricToBuildingBlocksMap.sort(function (a, b) {
          return b.value - a.value;
        });

        this.metricToBuildingBlocksMap.forEach(function (element) {
          temp1.push(element.model);
        });

        this.executiveListMap = temp1;
      }
    }
    this.sortReverse = !this.sortReverse;
  }

}
