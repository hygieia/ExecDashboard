import { Component, OnInit, OnChanges } from '@angular/core';

import { AdminAccessService } from '../services/admin-access.service';
import { PortfolioService } from '../../shared/services/portfolio.service';
import { UserDetails } from '../../shared/domain-models/userDetails';




@Component({
  selector: 'app-admin-access',
  templateUrl: './admin-access.component.html',
  styleUrls: ['./admin-access.component.scss'],
  providers: [AdminAccessService]
})
export class AdminAccessComponent implements OnInit,OnChanges {
  
  public bunit :string;
  public selectedItems = [];
  public dropdownList = [{"id": "select", "itemName": "Select Portfolio" }];
  public bUnits = [];
  public selectedLob = '';
  applicationList = {};
  public dropdownListApplication = [{"id":"select","itemName":"--Select--"}];
  public userList:UserDetails[] = null;  
  public appId = "select";
  public selectedUser:string = "";
  public submittedStatus : boolean = false;
  public spinnerVisibility:string  = "visible spinner center" ;
  public pageNumberEvent: number;
  public currentUser : UserDetails  = null ; 

  constructor(private portfolioService:PortfolioService , private adminAccessService:AdminAccessService) { 
    this.spinnerVisibility = "visible spinner center" ;
  }

  ngOnInit() {
    this.loadBusinessUnits(); 
  }
  ngOnChanges(): void {
    this.pageNumberEvent = 1;
  }
  loadBusinessUnits() {
    this.spinnerVisibility = "visible spinner center" ;
		this.portfolioService.getBusinessUnits()
			.subscribe(
			result => {
				this.bUnits = result;
				let count: number = 1;
				for (let entry of this.bUnits) {
					count++;
					var businessUnit = { "id": entry, "itemName": entry };
					this.dropdownList.push(businessUnit);
				}
				if (this.dropdownList.length > 0) {
					this.dropdownList.sort(function (a, b) {
             var nameA = a.itemName.toLowerCase(), nameB = b.itemName.toLowerCase()
						  if (nameA < nameB) return -1
						  if (nameA > nameB) return 1
              return 0
					})
        }
        this.spinnerVisibility = "hidden spinner center";
			},
			error => { console.log(error); }
      );
      this.bunit = "select";
      this.userList = null;
      this.pageNumberEvent = 1;
  }
  onSelectedItem(item:any)
  {
    this.spinnerVisibility = "visible spinner center" ;
    this.dropdownListApplication = [{"id":"select","itemName":"--Select--"}];
    this.portfolioService.getApplicationListAllForLob(this.bunit)
    .subscribe(
    result => {
      this.applicationList = result;
     
      for (const key in this.applicationList) {
        this.dropdownListApplication.push({ 'id': key, 'itemName': key});       
      }
      if (this.dropdownListApplication.length > 0) {
        this.dropdownListApplication.sort(function (a, b) {
           var nameA = a.itemName.toLowerCase(), nameB = b.itemName.toLowerCase()
            if (nameA < nameB) return -1
            if (nameA > nameB) return 1
            return 0
        })
      }
      this.spinnerVisibility = "hidden spinner center";
    });
    this.appId = "select";

    this.pageNumberEvent = 1;
  }

  onSelectedAppId(item:any)
  {
    this.pageNumberEvent = 1;
    this.loadUserDetails();
  }
  loadUserDetails()
  {
    this.spinnerVisibility = "visible spinner center" ;
    if(this.appId !== null && this.appId !== undefined)
    {
      this.adminAccessService.getUsersforAdmin(this.appId).subscribe(
        result => {
          this.userList =  result;
          this.sortByName();
          this.spinnerVisibility = "hidden spinner center";
        }
      );
    } 
  }
  makeAdmin(selUser:UserDetails)
  {
    this.currentUser =  selUser;
    this.spinnerVisibility = "visible spinner center" ;
    this.selectedUser = selUser.userName;
    this.adminAccessService.makeAdmin(this.appId,this.selectedUser).subscribe(
      result => {
        this.submittedStatus = result;
        this.spinnerVisibility = "hidden spinner center";
        this.loadUserDetails();
      }
    );
  }
  sortByName()
  {
    if(this.userList !== null &&  this.userList.length > 1)
    {
      this.userList.sort( function( element1, element2){
        if (element1.displayName.trim().toLowerCase() < element2.displayName.trim().toLowerCase())
          return 1;
        if (element1.displayName.trim().toLowerCase() > element2.displayName.trim().toLowerCase())
           return -1;
           
        return 0;
        }) ;
    }
  }
}
