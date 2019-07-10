import { ApplicationService, Application, Portfolio, PortfolioService } from '../../../shared/shared.module';
import { Component, OnInit, Input } from '@angular/core';
import { HeadingModel } from '../../../shared/component-models/heading-model';
import { ApplicationHeadingStrategy } from '../../strategies/application-heading-strategy';
import { Ng4LoadingSpinnerComponent, Ng4LoadingSpinnerService } from 'ng4-loading-spinner';
import { AuthService } from '../../../../services/vz/auth.service';
import { TrackService } from '../../../../services/vz/track.service';

@Component({
  selector: 'app-application',
  templateUrl: './application.component.html',
  styleUrls: ['./application.component.scss'],
  providers: [ApplicationService, PortfolioService, ApplicationHeadingStrategy, Ng4LoadingSpinnerComponent]
})

export class ApplicationComponent implements OnInit {

  @Input() public heading = 'Select Application';
  public applications = new Array<Application>();
  public selectedPortfolios = new Array<Portfolio>();
  public businessUnits = new Array<String>();
  public headingModel: HeadingModel;
  public selectedLob = '';
  public selectedApplication = '';
  public qryString = '';

  dropdownList = [];
  selectedItems = [];
  dropdownSettingsApplication = {};
  dropdownListApplication = [];
  selectedItemsApplication = [];
  dropdownSettings = {};
  bUnits = [];
  applicationList = [];
  allApplications = [];
  show = false;

  constructor(private applicationService: ApplicationService,
    private portfolioService: PortfolioService,
    private strategy: ApplicationHeadingStrategy,
    private ng4LoadingSpinnerService: Ng4LoadingSpinnerService,
    private authService: AuthService,
    private trackService: TrackService) { }

  ngOnInit() {
    this.headingModel = this.strategy.parse();
    this.dropdownList = [{ 'id': 1, 'itemName': 'All Portfolios' }];
    this.selectedItems = [{ 'id': 1, 'itemName': 'All Portfolios' }];
    this.dropdownSettings = { singleSelection: true, text: 'Select Portfolio', enableSearchFilter: true, classes: 'myclass custom-class' };
    this.dropdownListApplication = [];
    this.selectedItemsApplication = [];
    this.dropdownSettingsApplication = { singleSelection: true, text: 'Select Application', enableSearchFilter: true, classes: 'myclass custom-class' };
    this.ng4LoadingSpinnerService.show();
    this.loadApplicationsList();
    this.loadBusinessUnits();
    this.trackHomePage();
  }

  trackHomePage() {
    this.trackService.savePageTracking('Application View', this.authService.getAuthEid()).subscribe(
      result => {
      },
      error => {
      }
    );
  }

  loadApplicationsList() {
    this.applicationService.getApplications()
      .subscribe(
        result => {
          this.applications = result;
          this.allApplications = result;
          for (let i: 0; i < this.applications.length; i++) {
            this.applicationList.push({ 'id': this.applications[i].appId, 'itemName': this.applications[i].appId + ' : ' + this.applications[i].appName });
          }
          this.dropdownListApplication = this.applicationList;
        },
        error => { console.log(error); }
      );
  }

  loadBusinessUnits() {
    this.portfolioService.getBusinessUnits()
      .subscribe(
        result => {
          this.bUnits = result;
          let count: 1;
          for (const entry of this.bUnits) {
            count++;
            const businessUnit = { 'id': count, 'itemName': entry };
            this.dropdownList.push(businessUnit);
          }
          this.selectedLob = 'All Portfolios';
        },
        error => { console.log(error); }
      );
  }

  search() {
    const value = this.qryString;
    if (this.qryString != null) {
      const application = [];
      this.allApplications.forEach(element => {
        if ((element.appId).toLowerCase().includes(value.toLowerCase()) || (element.appName).toLowerCase().includes(value.toLowerCase())
              || (element.appAcronym).toLowerCase().includes(value.toLowerCase())) {
          application.push(element);
        }
      });
      this.applications = application;
    }
  }

  onItemSelect(item: any) {
    this.applications = [];
    this.selectedLob = item.itemName;
    this.selectedApplication = '';
    this.dropdownListApplication = [];
    this.selectedItemsApplication = [];
    this.ng4LoadingSpinnerService.show();
    if (item.itemName === 'All Portfolios') {
      this.applications = this.allApplications;
      this.dropdownListApplication = this.applicationList;
      this.ng4LoadingSpinnerService.hide();
    } else {
      this.allApplications.forEach(element => {
        if ((element.lob).toLowerCase() === item.itemName.toLowerCase()) {
          this.applications.push(element);
          this.dropdownListApplication.push({ 'id': element.appId, 'itemName': element.appId + ' : ' + element.appName });
        }
      });
    }
  }

  OnItemDeSelect(item: any) {
    this.dropdownList = [{ 'id': 1, 'itemName': 'All Portfolios' }];
    this.selectedItems = [{ 'id': 1, 'itemName': 'All Portfolios' }];
    this.dropdownListApplication = [];
    this.selectedItemsApplication = [];
    this.applications = this.allApplications;
    this.dropdownListApplication = this.applicationList;
  }

  onSelectAll(items: any) {
    console.log(items);
  }
  onDeSelectAll(items: any) {
    console.log(items);
  }

  onItemSelectApplication(item: any) {
    this.applications = [];
    this.selectedApplication = item.id;
    if (this.selectedLob !== undefined) {
      if (this.selectedLob !== 'All Portfolios') {
        this.applications = this.allApplications;
        this.dropdownListApplication = this.applicationList;
      } else {
        this.allApplications.forEach(element => {
          if ((element.appId).toLowerCase() === this.selectedApplication.toLowerCase()) {
            this.applications.push(element);
            this.dropdownListApplication.push({ 'id': element.appId, 'itemName': element.appId + ' : ' + element.appName });
          }
        });
      }
    }
  }

  OnItemDeSelectApplication(item: any) {
    this.dropdownList = [{ 'id': 1, 'itemName': 'All Portfolios' }];
    this.selectedItems = [{ 'id': 1, 'itemName': 'All Portfolios' }];
    this.dropdownListApplication = [];
    this.selectedItemsApplication = [];
    this.ng4LoadingSpinnerService.show();
    this.applications = this.allApplications;
    this.dropdownListApplication = this.applicationList;
  }

  onSelectAllApplication(items: any) {
    console.log(items);
  }

  onDeSelectAllApplication(items: any) {
    console.log(items);
  }

}

