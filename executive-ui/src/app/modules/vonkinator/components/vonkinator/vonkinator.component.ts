import { Component, OnInit } from '@angular/core';
import { VonkinatorService } from '../../services/vonkinator.service';
import { Vonkinator } from '../../../shared/domain-models/vonkinator';
import { PortfolioService } from '../../../shared/shared.module';
import { saveAs } from 'file-saver/FileSaver';

@Component({
  selector: 'app-vonkinator',
  templateUrl: './vonkinator.component.html',
  styleUrls: ['./vonkinator.component.scss'],
  providers: [VonkinatorService]
})
export class VonkinatorComponent implements OnInit {

  public vonkinatorDataSet = new Array<Vonkinator>();
  public vonkinatorNonITDataSet = new Array<Vonkinator>();
  public vonkinatorSelectedDataSet = new Array<Vonkinator>();
  public pageNumber: number;
  public searchString: string;
  public businessUnits = new Array<String>();
  public selectedApps = new Array<String>();
  public allApps = new Array<String>();
  public selectedLob = '';
  public selectedApplication = '';
  public closeableTimePeriod = false;
  public directLoading = false;
  public closeable = false;

  allSelectedProducts = [];
  dropdownList = [];
  selectedItems = [];
  dropdownSettingsApplication = {};
  dropdownListApplication = [];
  selectedItemsApplication = [];
  dropdownSettings = {};
  bUnits = [];
  applicationList = {};
  allApplications = [];
  show = false;
  timePeriodList = {};
  dropdownSettingsTimePeriod = {};
  dropdownListTimePeriod = [];
  timePeriodEntireList = [];
  selectedItemsTimePeriod = [];

  constructor(private vonkinatorService: VonkinatorService,
    private portfolioService: PortfolioService) {
  }

  ngOnInit() {
    this.pageNumber = 1;
    this.loadTimePeriodName();
    this.loadBusinessUnits();
    this.loadVonkinatorData();
    this.loadVonkinatorNonITData();
    this.dropdownListApplication = [];
    this.selectedItemsApplication = [];
    this.selectedItemsTimePeriod = [];
    this.dropdownList = [{ 'id': 1, 'itemName': 'All Portfolios' }];
    this.selectedItems = [{ 'id': 1, 'itemName': 'All Portfolios' }];
    this.dropdownSettings = { singleSelection: true, text: 'Select Portfolio', enableSearchFilter: true, classes: 'myclass custom-class' };
    this.dropdownSettingsApplication = { singleSelection: false, text: 'Select Applications', enableSearchFilter: true, badgeShowLimit: 1 };
    this.dropdownSettingsTimePeriod = { singleSelection: false, text: 'Select TimePeriod', enableSearchFilter: true, badgeShowLimit: 4 };
  }

  loadApplicationsList() {
    this.dropdownListApplication = [];
    this.selectedItemsApplication = [];
    this.selectedApps = [];
    this.portfolioService.getAllApplicationList()
      .subscribe(
        result => {
          this.applicationList = result;
          let selected = [];
          for (const key in this.applicationList) {
            this.dropdownListApplication.push({ 'id': key, 'itemName': this.applicationList[key] });
            selected.push({ 'id': key, 'itemName': this.applicationList[key] });
            this.allApps.push(key);
          }
          this.selectedItemsApplication = selected;
          this.selectedApps = this.allApps;
          if (this.vonkinatorDataSet !== undefined) {
            this.vonkinatorSelectedDataSet = this.vonkinatorDataSet;
          }
          if (this.timePeriodEntireList.length > 0) {
            this.selectedItemsTimePeriod = this.timePeriodEntireList;
          }
          if (this.dropdownListApplication.length > 0) {
            this.dropdownListApplication.sort(function (a, b) {
              const nameA = a.itemName.toLowerCase();
              const nameB = b.itemName.toLowerCase();
              if (nameA < nameB) {
                return -1;
              }
              if (nameA > nameB) {
                return 1;
              }
              return 0;
            });
          }
        },
        error => { console.log(error); }
      );
  }


  loadBusinessUnits() {
    this.portfolioService.getBusinessUnits()
      .subscribe(
        result => {
          this.bUnits = result;
          let count = 1;
          for (const entry of this.bUnits) {
            count++;
            const businessUnit = { 'id': count, 'itemName': entry };
            this.dropdownList.push(businessUnit);
          }
          if (this.dropdownList.length > 0) {
            this.dropdownList.sort(function (a, b) {
              const nameA = a.itemName.toLowerCase();
              const nameB = b.itemName.toLowerCase();
              if (nameA < nameB) {
                return -1;
              }
              if (nameA > nameB) {
                return 1;
              }
              return 0;
            });
          }
          this.selectedLob = 'All Portfolios';
          this.onItemSelect(this.selectedLob); // need to chk this
        },
        error => { console.log(error); }
      );
  }

  loadVonkinatorData() {
    this.vonkinatorService.getAllVonkinatorData().subscribe(
      result => {
        this.vonkinatorDataSet = result;
        this.vonkinatorSelectedDataSet = result;
        this.loadApplicationsList();
      },
      error => {
        console.log(error);
      }
    );
  }

  loadVonkinatorNonITData() {
    this.vonkinatorService.getAllNonITVonkinatorData().subscribe(
      result => {
        this.vonkinatorNonITDataSet = result;
      },
      error => {
        console.log(error);
      }
    );
  }

  onItemSelect(item: any) {
    this.directLoading = false;
    this.selectedLob = item.itemName;
    this.selectedApplication = '';
    this.dropdownListApplication = [];
    this.selectedItemsApplication = [];
    this.selectedApps = [];
    this.applicationList = [];
    this.show = false;
    this.vonkinatorSelectedDataSet = [];
    this.pageNumber = 1;
    if (item.itemName === 'All Portfolios') {
      this.loadApplicationsList();
    } else {
      this.portfolioService.getApplicationListAllForLob(this.selectedLob)
        .subscribe(
          result => {
            this.applicationList = result;
            const selected = [];
            for (const key in this.applicationList) {
              this.dropdownListApplication.push({ 'id': key, 'itemName': this.applicationList[key] });
              selected.push({ 'id': key, 'itemName': this.applicationList[key] });
              this.selectedApps.push(key);
            }
            this.selectedItemsApplication = selected;
            if (this.vonkinatorDataSet !== undefined) {
              for (let i = 0; i < this.vonkinatorDataSet.length; i++) {
                if (this.selectedApps.includes(this.vonkinatorDataSet[i].appId)) {
                  this.vonkinatorSelectedDataSet.push(this.vonkinatorDataSet[i]);
                }
              }
            }
            if (this.timePeriodEntireList.length > 0) {
              this.selectedItemsTimePeriod = this.timePeriodEntireList;
            }
            if (this.dropdownListApplication.length > 0) {
              this.dropdownListApplication.sort(function (a, b) {
                const nameA = a.itemName.toLowerCase();
                const nameB = b.itemName.toLowerCase();
                if (nameA < nameB) {
                  return -1;
                }
                if (nameA > nameB) {
                  return 1;
                }
                return 0;
              });
            }
          },
          error => { console.log(error); }
        );
    }
  }

  OnItemDeSelect(item: any) {
    this.pageNumber = 1;
    this.directLoading = true;
    this.dropdownList = [{ 'id': 1, 'itemName': 'All Portfolios' }];
    this.selectedItems = [{ 'id': 1, 'itemName': 'All Portfolios' }];
    this.dropdownListApplication = [];
    this.selectedItemsApplication = [];
    this.loadBusinessUnits();
    this.loadApplicationsList();
  }

  onSelectAll(items: any) {
    this.directLoading = false;
  }

  onDeSelectAll(items: any) {
    this.directLoading = false;
  }

  onCloseApplication(items: any) {
    const appIds = [];
    const ids = [];
    const vonkinatorSelectSet = [];
    if (this.closeable) {
      this.pageNumber = 1;
      this.directLoading = false;
      this.vonkinatorSelectedDataSet = [];
      this.selectedItemsApplication.forEach(function (arrayItem) {
        appIds.push(arrayItem.id);
      });
      this.selectedApps = appIds;
      if (this.vonkinatorDataSet !== undefined) {
        for (let i = 0; i < this.vonkinatorDataSet.length; i++) {
          if (this.selectedApps.includes(this.vonkinatorDataSet[i].appId)) {
            vonkinatorSelectSet.push(this.vonkinatorDataSet[i]);
          }
        }
        this.selectedItemsTimePeriod.forEach(function (arrayItem) {
          ids.push(arrayItem.itemName);
        });
        if (vonkinatorSelectSet !== undefined) {
          for (let i = 0; i < vonkinatorSelectSet.length; i++) {
            if (ids.includes(vonkinatorSelectSet[i].period)) {
              this.vonkinatorSelectedDataSet.push(vonkinatorSelectSet[i]);
            }
          }
        }
      }
      if (this.selectedItemsApplication.length <= 0) {
        this.dropdownList = [{ 'id': 1, 'itemName': 'All Portfolios' }];
        this.selectedItems = [{ 'id': 1, 'itemName': 'All Portfolios' }];
        this.dropdownListApplication = [];
        this.selectedItemsApplication = [];
        this.loadApplicationsList();
        this.loadBusinessUnits();
        this.directLoading = true;
      }
    }
    this.closeable = false;
  }

  onItemSelectApplication(items: any) {
    this.closeable = true;
    this.directLoading = false;
  }


  OnItemDeSelectApplication(item: any) {
    this.closeable = true;
    this.directLoading = false;
  }

  onSelectAllApplication(items: any) {

    this.closeable = true;
    this.directLoading = false;
  }

  onDeSelectAllApplication(items: any) {
    this.closeable = true;
  }

  loadTimePeriodName() {
    this.portfolioService.getTimePeriod()
      .subscribe(
        result => {
          this.timePeriodList = result;
          for (const key in this.timePeriodList) {
            this.dropdownListTimePeriod.push({ 'id': key, 'itemName': this.timePeriodList[key] });
          };
          this.selectedItemsTimePeriod = this.dropdownListTimePeriod;
          this.timePeriodEntireList = this.dropdownListTimePeriod;
        },
        error => {
          console.log(error);
        }
      );
  }

  onCloseTimePeriod(items: any) {
    const ids = [];
    if (this.closeableTimePeriod) {
      this.pageNumber = 1;
      this.vonkinatorSelectedDataSet = [];
      const vonkinatorSelectSet = [];
      this.selectedItemsTimePeriod.forEach(function (arrayItem) {
        ids.push(arrayItem.itemName);
      });
      this.vonkinatorSelectedDataSet = [];
      if (this.vonkinatorDataSet !== undefined) {
        for (let i = 0; i < this.vonkinatorDataSet.length; i++) {
          if (this.selectedApps.includes(this.vonkinatorDataSet[i].appId)) {
            vonkinatorSelectSet.push(this.vonkinatorDataSet[i]);
          }
        }
        this.selectedItemsTimePeriod.forEach(function (arrayItem) {
          ids.push(arrayItem.itemName);
        });
        if (vonkinatorSelectSet !== undefined) {
          for (let i = 0; i < vonkinatorSelectSet.length; i++) {
            if (ids.includes(vonkinatorSelectSet[i].period)) {
              this.vonkinatorSelectedDataSet.push(vonkinatorSelectSet[i]);
            }
          }
        }
      }
      if (this.selectedItemsTimePeriod.length === 0) {
        this.selectedItemsTimePeriod = this.timePeriodEntireList;
      }

    }
    this.closeableTimePeriod = false;
  }

  onItemSelectTimePeriod(items: any) {
    this.closeableTimePeriod = true;
  }


  OnItemDeSelectTimePeriod(item: any) {
    this.closeableTimePeriod = true;
  }

  onSelectAllTimePeriod(items: any) {
    this.closeableTimePeriod = true;
  }

  onDeSelectAllTimePeriod(items: any) {
    this.closeableTimePeriod = true;
  }

  downloadITExcel() {
    this.downloadExcel(this.vonkinatorDataSet, 'VonkinatorITList', 'IT');
  }

  downloadNonITExcel() {
    this.downloadExcel(this.vonkinatorNonITDataSet, 'VonkinatorNonITList', 'NonIT');
  }

  downloadSelectedITExcel() {
    this.downloadExcel(this.vonkinatorSelectedDataSet, 'VonkinatorSelectedList', 'IT');
  }

  downloadExcel(vonkinatorDownloadSet, header, itSegment) {
    let content = 'Application Id, Application Name, Application Status, IT/ Non IT, Portfolio, Order, Period,' +
      'Tier 2 Executive Name, Tier 2 Executive Designation, Tier 3 Name, Tier 3 Executive Designation,' +
      'Tier 4 Executive Name, Tier 4 Executive Designation,	Tier 5 Executive Name,	Tier 5 Executive Designation,' +
      'Change Activities,	Change Failure Rate,	Deployment Cadence,	Outages,	Mean Time To Resolve Outages,' +
      'Events, Mean Time To Resolve Events,	Fortify Vulnerabilities,	Port Scan Vulnerabilities, Web Scan Vulnerabilities, Jira Bugs,' +
      'Cmis Tickets, SN Defects,	Cycle Time,	Jira Stories,	Jira Story Points,	Time Taken,' +
      'ADD DOJO FLAG,	# Developers,	# Weeks In Dojo,	# Hours/Day In Dojo,	Dojo StartQtr,	Dojo Start Date (mm/yy)\n';
    if (vonkinatorDownloadSet !== undefined) {
      for (let i = 0; i < vonkinatorDownloadSet.length; i++) {
        content = content + vonkinatorDownloadSet[i].appId + ',';
        content = content + vonkinatorDownloadSet[i].appName + ',';
        content = content + vonkinatorDownloadSet[i].appStatus + ',';
        content = content + itSegment + ',';
        content = content + vonkinatorDownloadSet[i].portfolio + ',';
        content = content + vonkinatorDownloadSet[i].order + ',';
        content = content + vonkinatorDownloadSet[i].period + ',';
        content = content + vonkinatorDownloadSet[i].twoExecutive + ',';
        content = content + vonkinatorDownloadSet[i].twoEid + ',';
        content = content + vonkinatorDownloadSet[i].threeExecutive + ',';
        content = content + vonkinatorDownloadSet[i].threeEid + ',';
        content = content + vonkinatorDownloadSet[i].fourExecutive + ',';
        content = content + vonkinatorDownloadSet[i].fourEid + ',';
        content = content + vonkinatorDownloadSet[i].fiveExecutive + ',';
        content = content + vonkinatorDownloadSet[i].fiveEid + ',';
        content = content + vonkinatorDownloadSet[i].changeActivities + ',';
        content = content + vonkinatorDownloadSet[i].changeFailureRate + ',';
        content = content + vonkinatorDownloadSet[i].deploymentCadence + ',';
        content = content + vonkinatorDownloadSet[i].outages + ',';
        content = content + vonkinatorDownloadSet[i].meanTimeToResolveOutages + ',';
        content = content + vonkinatorDownloadSet[i].events + ',';
        content = content + vonkinatorDownloadSet[i].meanTimeToResolveEvents + ',';
        content = content + vonkinatorDownloadSet[i].fortifyVulnerabilities + ',';
        content = content + vonkinatorDownloadSet[i].portScanVulnerabilities + ',';
        content = content + vonkinatorDownloadSet[i].webScanVulnerabilities + ',';
        content = content + vonkinatorDownloadSet[i].jiraBugs + ',';
        content = content + vonkinatorDownloadSet[i].cmisTickets + ',';
        content = content + vonkinatorDownloadSet[i].snTickets + ',';
        content = content + vonkinatorDownloadSet[i].cycleTime + ',';
        content = content + vonkinatorDownloadSet[i].stories + ',';
        content = content + vonkinatorDownloadSet[i].storyPoints + ',';
        content = content + vonkinatorDownloadSet[i].timeTaken + ',';
        content = content + vonkinatorDownloadSet[i].isDojo + ',';
        content = content + vonkinatorDownloadSet[i].developers + ',';
        content = content + vonkinatorDownloadSet[i].weeks + ',';
        content = content + vonkinatorDownloadSet[i].hours + ',';
        content = content + vonkinatorDownloadSet[i].dojoStartQtr + ',';
        content = content + vonkinatorDownloadSet[i].dojoStartDate + ',';
        content = content + '\n';
      }
    }
    const blob = new Blob([ content  ], { type : 'text/csv;charset=utf-8' });
    saveAs(blob, header + '.csv');
  }

}
