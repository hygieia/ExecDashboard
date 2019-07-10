import { PortfolioService, Portfolio } from '../../../shared/shared.module';
import { Component, OnInit, Input } from '@angular/core';
import { SortingService } from '../../../../services/sorting.service';
import { HeadingModel } from '../../../shared/component-models/heading-model';
import { DirectoryHeadingStrategy } from '../../strategies/directory-heading-strategy';
import { Ng4LoadingSpinnerModule, Ng4LoadingSpinnerComponent, Ng4LoadingSpinnerService } from 'ng4-loading-spinner'
import { AuthService } from '../../../../services/vz/auth.service';
import { TrackService } from '../../../../services/vz/track.service';

@Component({
	selector: 'app-directory',
	templateUrl: './directory.component.html',
	styleUrls: ['./directory.component.scss'],
	providers: [PortfolioService, SortingService, DirectoryHeadingStrategy, Ng4LoadingSpinnerComponent]
})

export class DirectoryComponent implements OnInit {

	@Input() public heading = 'Select Executive';
	public portfolios = new Array<Portfolio>();
	public selectedPortfolios = new Array<Portfolio>();
	public businessUnits = new Array<String>();
	public headingModel: HeadingModel;
	public selectedLob = '';
	public selectedExecutive = '';
	public qryString = "";

	dropdownList = [];
	selectedItems = [];
	dropdownSettingsExecutive = {};
	dropdownListExecutive = [];
	selectedItemsExecutive = [];
	dropdownSettings = {};
	bUnits = [];
	executiveList = {};
	allExecutives = [];
	show = false;

	constructor(private portfolioService: PortfolioService,
		private sortingService: SortingService,
		private strategy: DirectoryHeadingStrategy,
		private ng4LoadingSpinnerService: Ng4LoadingSpinnerService,
		private authService: AuthService,
		private trackService: TrackService) { }

	ngOnInit() {
		this.headingModel = this.strategy.parse();

		this.dropdownList = [{ "id": 1, "itemName": "All Portfolios" }];
		this.selectedItems = [{ "id": 1, "itemName": "All Portfolios" }];
		this.dropdownSettings = { singleSelection: true, text: "Select Portfolio", enableSearchFilter: true, classes: "myclass custom-class" };

		this.dropdownListExecutive = [];
		this.selectedItemsExecutive = [];
		this.dropdownSettingsExecutive = { singleSelection: true, text: "Select EVP / SVP / VP", enableSearchFilter: true, classes: "myclass custom-class" };
		this.ng4LoadingSpinnerService.show();
		this.loadPortfolios();
		this.loadExecutivesList();
		this.loadBusinessUnits();
		this.trackHomePage();
	}
	
	trackHomePage() {
		this.trackService.savePageTracking('Home View', this.authService.getAuthEid()).subscribe(
			result => {
			},
			error => {
			}
		);
	}

	loadPortfolios() {
		this.portfolioService.getPortfolios()
			.subscribe(
			result => {
				this.portfolios = result;
				this.allExecutives = this.portfolios;
				this.show = true;
				this.ng4LoadingSpinnerService.hide();
				//  this.portfolios = result;
			},
			error => {
				this.show = true;
				this.ng4LoadingSpinnerService.hide();
			}
			);
	}

	loadExecutivesList() {

		this.portfolioService.getExecutivesList('All')
			.subscribe(
			result => {
				this.executiveList = result;
				for (var key in this.executiveList) {
					this.dropdownListExecutive.push({ "id": key, "itemName": this.executiveList[key] });
				}
				if (this.dropdownListExecutive.length > 0) {
					this.dropdownListExecutive.sort(function (a, b) {
						var nameA = a.itemName.toLowerCase(), nameB = b.itemName.toLowerCase()
						if (nameA < nameB) return -1
						if (nameA > nameB) return 1
						return 0
					})
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
				let count: number = 1;
				for (let entry of this.bUnits) {
					count++;
					var businessUnit = { "id": count, "itemName": entry };
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
				this.selectedLob = 'All Portfolios';
			},
			error => { console.log(error); }
			);
	}


	search() {
		this.portfolios = this.allExecutives;
		let value = this.qryString;
		if (this.qryString != null) {
			let portfolio = [];
			this.portfolios.forEach(element => {
				if ((element.executive.firstName).toLowerCase().includes(value.toLowerCase()) || (element.executive.lastName).toLowerCase().includes(value.toLowerCase())) {
					portfolio.push(element);
				}
			});
			this.portfolios = portfolio;
		}
	}


	onItemSelect(item: any) {
		this.portfolios = [];
		this.selectedLob = item.itemName;
		this.selectedExecutive = '';
		this.dropdownListExecutive = [];
		this.selectedItemsExecutive = [];
		this.show = false;
		this.ng4LoadingSpinnerService.show();
		if (item.itemName == 'All Portfolios') {
			this.portfolioService.getPortfolios()
				.subscribe(
				result => {
					this.portfolios = result;

					this.allExecutives = this.portfolios;
					this.show = true;
					this.ng4LoadingSpinnerService.hide();
				},
				error => {
					this.show = true;
					this.ng4LoadingSpinnerService.hide();
				}
				);
			this.portfolioService.getExecutivesList('All')
				.subscribe(
				result => {
					this.executiveList = result;
					for (var key in this.executiveList) {
						this.dropdownListExecutive.push({ "id": key, "itemName": this.executiveList[key] });
					}
					if (this.dropdownListExecutive.length > 0) {
						this.dropdownListExecutive.sort(function (a, b) {
							var nameA = a.itemName.toLowerCase(), nameB = b.itemName.toLowerCase()
							if (nameA < nameB) return -1
							if (nameA > nameB) return 1
							return 0
						})
					}
				},
				error => { console.log(error); }
				);

		} else {
			this.portfolioService.getPortfoliosExecutive(item.itemName)
				.subscribe(
				result => {
					this.portfolios = result;
					this.allExecutives = this.portfolios;
					this.show = true;
					this.ng4LoadingSpinnerService.hide();
				},
				error => {
					this.show = true;
					this.ng4LoadingSpinnerService.hide();
				}
				);
			this.portfolioService.getExecutivesList(item.itemName)
				.subscribe(
				result => {
					this.executiveList = result;
					for (var key in this.executiveList) {
						this.dropdownListExecutive.push({ "id": key, "itemName": this.executiveList[key] });
					}
					if (this.dropdownListExecutive.length > 0) {
						this.dropdownListExecutive.sort(function (a, b) {
							var nameA = a.itemName.toLowerCase(), nameB = b.itemName.toLowerCase()
							if (nameA < nameB) return -1
							if (nameA > nameB) return 1
							return 0
						})
					}
				},
				error => { console.log(error); }
				);
		}
	}
	
	OnItemDeSelect(item: any) {
		this.dropdownList = [{ "id": 1, "itemName": "All Portfolios" }];
		this.selectedItems = [{ "id": 1, "itemName": "All Portfolios" }];
		this.dropdownListExecutive = [];
		this.selectedItemsExecutive = [];
		this.ng4LoadingSpinnerService.show();
		this.loadPortfolios();
		this.loadExecutivesList();
		this.loadBusinessUnits();
	}
	onSelectAll(items: any) {
		console.log(items);
	}
	onDeSelectAll(items: any) {
		console.log(items);
	}

	onItemSelectExecutive(item: any) {
		this.portfolios = [];
		this.ng4LoadingSpinnerService.show();
		this.selectedExecutive = item.itemName;
		this.show = false;
		if (this.selectedLob != undefined) {
			if (this.selectedLob != 'All Portfolios') {
				this.portfolioService.getExecutivesListForBunit(item.id, this.selectedLob)
					.subscribe(
					result => {
						this.portfolios = result;
						this.allExecutives = this.portfolios;
						this.show = true;
						this.ng4LoadingSpinnerService.hide();
					},
					error => {
						this.show = true;
						this.ng4LoadingSpinnerService.hide();
					}
					);
			} else {
				this.portfolioService.getExecutivesListForBunit(item.id, 'All')
					.subscribe(
					result => {
						this.portfolios = result;
						this.allExecutives = this.portfolios;
						this.show = true;
						this.ng4LoadingSpinnerService.hide();
					},
					error => {
						this.show = true;
						this.ng4LoadingSpinnerService.hide();
					}
					);
			}
		}
	}
	OnItemDeSelectExecutive(item: any) {
		this.dropdownList = [{ "id": 1, "itemName": "All Portfolios" }];
		this.selectedItems = [{ "id": 1, "itemName": "All Portfolios" }];
		this.dropdownListExecutive = [];
		this.selectedItemsExecutive = [];
		this.ng4LoadingSpinnerService.show();
		this.loadPortfolios();
		this.loadExecutivesList();
		this.loadBusinessUnits();
	}
	onSelectAllExecutive(items: any) {
		console.log(items);
	}
	onDeSelectAllExecutive(items: any) {
		console.log(items);
	}
	
}
