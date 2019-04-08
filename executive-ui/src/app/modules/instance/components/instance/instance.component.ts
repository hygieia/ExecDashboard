import { Component, OnInit, OnChanges } from '@angular/core';
import { PatchDetail } from '../../../shared/domain-models/patchDetail';
import { HygieiaInstanceService } from '../../services/hygieia-instance.service';
import { SoftwareVersion } from '../../../shared/domain-models/softwareVersion';
import { PortfolioService } from '../../../shared/shared.module';
import { saveAs } from 'file-saver/FileSaver';

@Component({
  selector: 'app-instance',
  templateUrl: './instance.component.html',
  styleUrls: ['./instance.component.scss'],
  providers:[HygieiaInstanceService]
})
export class InstanceComponent implements OnInit,OnChanges {
  
  [x: string]: any;
  public bunit :string;
  public selectedItems = [];
  public dropdownList = [{"id": "All", "itemName": "All Portfolio" }];
  public dropdownSettings = {};
  public patchVersion:PatchDetail[] = null;
  public softwareVersionDetails:SoftwareVersion;
  public pageNumber : number = 1;
  public bUnits = [];
  public sortSelected:string = "appName"; 
  public sortReverse: Boolean = false;
  public spinnerVisibility:string  = "visible spinner center" ;
  constructor(private portfolioService:PortfolioService,private hygieiaInstanceService:HygieiaInstanceService) {
    this.loadBusinessUnits();
    this.loadPatchDetails();
    this.spinnerVisibility = "visible spinner center" ;
   }

  ngOnInit() {
    this.pageNumber=1;
  }
  ngOnChanges(): void {
    
  }
  loadPatchDetails()
  {
    this.pageNumber=1;
    if(this.bunit === undefined)
    {
      this.bunit = "All";
    }
    this.spinnerVisibility = "visible spinner center" ;
    this.hygieiaInstanceService.getPatchBusinessUnitsData(this.bunit)
      .subscribe(
        result => {
          this.softwareVersionDetails = result;
          this.spinnerVisibility = "hidden spinner center";
        },
        error => {
          console.log(error);
          this.spinnerVisibility = "hidden spinner center";
        }
        );
  }
  loadBusinessUnits() {
    this.pageNumber=1;
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
      this.bunit = "All";
  }
  onSelectedItem(item:any)
  {
    this.pageNumber = 1;
    this.loadPatchDetails();
  }
  downloadExcel()
  {
    let header = "#, Application Name, Application Vast Id, Instance Ip, Version Available in the Instance, Updated with Latest Version\n";
    let content = '';
    if (this.softwareVersionDetails.patch !== undefined) {
      this.patchVersion = this.softwareVersionDetails.patch;
      for (let i = 0; i < this.patchVersion.length; i++) {
        content = content +(i+1)+",";
        content = content + this.patchVersion[i].appName+",";
        content = content + this.patchVersion[i].appId+",";
        content = content + this.patchVersion[i].privateIp+",";
        content = content + this.patchVersion[i].version+",";
        content = content + this.patchVersion[i].latestVersion+"\n";
      }
    }

    const blob = new Blob([header + content], { type: 'text/csv;charset=utf-8' });
    saveAs(blob, this.bunit+'_Instance_Patch_Status.csv');
  }
  sort(value:string)
  {
    this.sortSelected = value;  

    if(this.softwareVersionDetails.patch !== undefined && this.softwareVersionDetails.patch != null)
    {
      if (this.sortReverse) {
        if(value === "appId"){
            this.softwareVersionDetails.patch.sort( function( element1, element2){
                if (element1.appId.trim().toLowerCase() > element2.appId.trim().toLowerCase())
                  return 1;
                if (element1.appId.trim().toLowerCase() < element2.appId.trim().toLowerCase())
                   return -1;
                   
                return 0;
                }) ;
        }
        else if(value === "appName")
        {
          this.softwareVersionDetails.patch.sort( function( element1, element2){
            if (element1.appName.trim().toLowerCase() > element2.appName.trim().toLowerCase())
              return 1;
            if (element1.appName.trim().toLowerCase() < element2.appName.trim().toLowerCase())
               return -1;
               
            return 0;
            }) ;
        }
        else if(value === "version")
        {
          this.softwareVersionDetails.patch.sort( function( element1, element2){
            if (element1.version.trim().toLowerCase() > element2.version.trim().toLowerCase())
              return 1;
            if (element1.version.trim().toLowerCase() < element2.version.trim().toLowerCase())
               return -1;
               
            return 0;
            }) ;
        }
       
      }
      else{
        if(value === "appId"){
          this.softwareVersionDetails.patch.sort( function( element1, element2){
              if (element1.appId.trim().toLowerCase() < element2.appId.trim().toLowerCase())
                return 1;
              if (element1.appId.trim().toLowerCase() > element2.appId.trim().toLowerCase())
                 return -1;
                 
              return 0;
              }) ;
      }
      else if(value === "appName")
      {
        this.softwareVersionDetails.patch.sort( function( element1, element2){
          if (element1.appName.trim().toLowerCase() < element2.appName.trim().toLowerCase())
            return 1;
          if (element1.appName.trim().toLowerCase() > element2.appName.trim().toLowerCase())
             return -1;
             
          return 0;
          }) ;
      }
      else if(value === "version")
      {
        this.softwareVersionDetails.patch.sort( function( element1, element2){
          if (element1.version.trim().toLowerCase() < element2.version.trim().toLowerCase())
            return 1;
          if (element1.version.trim().toLowerCase() > element2.version.trim().toLowerCase())
             return -1;
             
          return 0;
          }) ;
      }
      }
    }

    this.sortReverse = !this.sortReverse;
  }
}
