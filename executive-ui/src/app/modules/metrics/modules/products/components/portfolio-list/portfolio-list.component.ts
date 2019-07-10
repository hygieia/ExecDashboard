import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { Params, ActivatedRoute, Router } from '@angular/router';
import { PortfolioService } from '../../../../../shared/shared.module';
import { Portfolio } from '../../../../../shared/domain-models/portfolio';
import { ProductService } from '../../../shared/services/product.service';
import { BuildingBlockModel } from '../../../shared/component-models/building-block-model';
import { HeadingModel } from '../../../../../shared/component-models/heading-model';
import { PortfolioListProductsStrategy } from '../../strategies/portfolio-list-products-strategy';
import { Ng4LoadingSpinnerModule, Ng4LoadingSpinnerComponent, Ng4LoadingSpinnerService } from 'ng4-loading-spinner';
import { AuthService } from '../../../../../../services/vz/auth.service';
import { TrackService } from '../../../../../../services/vz/track.service';

@Component({
  selector: 'app-portfolio-list',
  templateUrl: './portfolio-list.component.html',
  styleUrls: ['./portfolio-list.component.scss'],
  providers: [
    PortfolioService,
    ProductService,
    Ng4LoadingSpinnerComponent
  ]
})
export class PortfolioListComponent implements OnInit {
  public portfolioHeading: string;
  public portfolioId: string;
  public productHeading: string;
  public productId: string;
  public role: string;
  public products: BuildingBlockModel[];
  public productsCopy: BuildingBlockModel[];
  public productSearched: BuildingBlockModel[];
  public headingModel: HeadingModel;
  public searchString: string;
  public msg = 'Loading Portfolios';
  public productsAvailable = true;
  public portfolios = new Array<Portfolio>();
  public selectedPortfolios = new Array<Portfolio>();
  public businessUnits = new Array<String>();
  public selectedLob = '';
  public selectedExecutive = '';
  public qryString = '';
  public closeable = false;
  public directLoading = false;
  public isFav: boolean = false;
  public eid: string;
  public currentEid: string;
  public showFav: boolean;
  public favEid: string;
  public selectedExecs = new Array<String>();
  public actualFavouriteList = new Array<String>();
  public closeableMetrics = false;
  public cardsList = [];
  public selectedCardsList = [];

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
  allProducts = [];
  previewCardsList = {};
  dropdownSettingsMetrics = {};
  dropdownListMetrics = [];
  selectedItemsMetrics = [];

  constructor(private router: Router,
    private activatedRoute: ActivatedRoute,
    private authService: AuthService,
    private trackService: TrackService,
    private portfolioService: PortfolioService,
    private productService: ProductService,
    private portfolioListProductsStrategy: PortfolioListProductsStrategy,
    private ng4LoadingSpinnerService: Ng4LoadingSpinnerService) {
  }

  changeFavStatus() {
    this.isFav = !this.isFav;
    if (this.isFav) {
      this.portfolioService.setFav(this.eid, this.selectedExecs).subscribe((result) => { console.log('Save Fav Status: ' + result) });
    } else {
      this.portfolioService.removeFav(this.eid).subscribe((result) => { console.log('Remove Fav Status: ' + result) });
    }
  }

  loadPortfolios() {
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
  }

  loadExecutivesList() {
    this.portfolioService.getExecutivesListAll('All')
      .subscribe(
      result => {
        this.executiveList = result;
        for (const key in this.executiveList) {
          this.dropdownListExecutive.push({ 'id': key, 'itemName': this.executiveList[key] });
        }

        if (this.dropdownListExecutive.length > 0) {
          this.dropdownListExecutive.sort(function (a, b) {
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
      },
      error => { console.log(error); }
      );
  }

   loadMetricsName() {
     this.productService.getPreviewCards()
        .subscribe(
        result => {
          this.previewCardsList = result;
          for (var key in this.previewCardsList) {
            this.dropdownListMetrics.push({ 'id': key, 'itemName': this.previewCardsList[key] });
          }
          if (this.dropdownListMetrics.length > 0) {
            this.dropdownListMetrics.sort(function (a, b) {
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
        error => {
          console.log(error);
        }
        );

    this.productService.getPreviewSelectedCards()
        .subscribe(
        result => {
          this.previewCardsList = result;
          let selected = [];
          for (var key in this.previewCardsList) {
            selected.push({ 'id': key, 'itemName': this.previewCardsList[key] });
            this.cardsList.push(key);
            this.selectedCardsList.push(key);
          }
         this.selectedItemsMetrics = selected;
        },
        error => {
          console.log(error);
        }
        );
  }

  getReportingCards() {

    this.portfolioService.getReportings(this.portfolioId)
      .subscribe(
      result => {
        this.executiveList = result;

        for (var key in this.executiveList) {
          this.selectedItemsExecutive.push({ 'id': key, 'itemName': this.executiveList[key] });
          this.selectedExecs.push(key)
        }

        this.productService.getPortfolioExecutives(this.portfolioId)
          .subscribe(
          result => {
            this.products = this.portfolioListProductsStrategy.parse(result);
            this.allProducts = this.products;
            this.headingModel = this.getHeadingModel();
          },
          error => console.log(error)
          );
      },
      error => { console.log(error); }
      );


  }

  ngOnInit() {

    this.isFav = false;
    this.authService.currentAuthData.subscribe((data) => { let ssoData: Object = data; this.eid = ssoData['eid']});
    this.dropdownList = [{ 'id': 1, 'itemName': 'All Portfolios' }];
    this.selectedItems = [{ 'id': 1, 'itemName': 'All Portfolios' }];
    this.dropdownSettings = { singleSelection: true, text: 'Select Portfolio', enableSearchFilter: true, classes: 'myclass custom-class' };
    this.dropdownListExecutive = [];
    this.selectedItemsExecutive = [];
    this.selectedItemsMetrics = [];
    this.dropdownSettingsExecutive = { singleSelection: false, text: 'Select Executives', enableSearchFilter: true, badgeShowLimit: 10 };
    this.dropdownSettingsMetrics = { singleSelection: false, text: 'Select Metrics', enableSearchFilter: true, badgeShowLimit: 3, limitSelection: 9 };
    this.ng4LoadingSpinnerService.show();
    this.loadPortfolios();
    this.loadExecutivesList();
    this.loadBusinessUnits();
    this.loadMetricsName();
    this.trackComparisonPage();

    this.activatedRoute.params.subscribe((params: Params) => {
    this.portfolioId = params['portfolio-id'];

    this.portfolioService.getPortfolio(this.portfolioId)
      .subscribe(
      result => {
        this.currentEid = result['eid'];
        this.portfolioHeading = `${getPortfolioName(result)}'s Directs`;
        this.role = result.executive.role;
        this.headingModel = this.getHeadingModel();
        this.getDataForPage();
        this.ng4LoadingSpinnerService.hide();
      },
      error => {
        console.log(error);
        this.ng4LoadingSpinnerService.hide();
      });

      
    this.productService.getPortfolioExecutives(this.portfolioId)
      .subscribe(
      result => {
        this.products = this.portfolioListProductsStrategy.parse(result);
        this.allProducts = this.products;
        this.headingModel = this.getHeadingModel();
        this.ng4LoadingSpinnerService.hide();
      },
      error => {
        console.log(error);
        this.ng4LoadingSpinnerService.hide();
      }
      );

    });

    this.headingModel = this.getHeadingModel();
    this.directLoading = true;
    
    function getPortfolioName(portfolio: Portfolio) {
      if (portfolio.name) {
        return portfolio.name;
      }
      return `${portfolio.executive.firstName} ${portfolio.executive.lastName}`;
    }
  }

  trackComparisonPage() {
    this.trackService.savePageTracking('Portfolio Metric Comparison View', this.authService.getAuthEid()).subscribe(
      result => {
      },
      error => {
      }
    );
  }

  getDataForPage() {
    if (this.eid == this.currentEid) {
      this.showFav = true;
      this.portfolioService.getFavourite(this.eid).subscribe(
        result => {
          this.executiveList = result;
          if (JSON.stringify(this.executiveList) != JSON.stringify({})) {
            this.isFav = true;
            for (var key in this.executiveList) {
              this.selectedItemsExecutive.push({ 'id': key, 'itemName': this.executiveList[key] });
              this.selectedExecs.push(key)
            }

          this.productService.getPortfolioExecutiveFavs(this.currentEid)
          .subscribe(
          result => {
            this.products = this.portfolioListProductsStrategy.parse(result);
            this.allProducts = this.products;
            this.headingModel = this.getHeadingModel();
          },
          error => console.log(error)
          );

          } else {
            this.getReportingCards()
          }

        },
        error => { console.log(error); }
      );
    } else {
      this.getReportingCards()
      this.showFav = false;
    }

    if (!this.isFav) {
        
      }else{
       
        this.productService.getPortfolioExecutiveFavs(this.currentEid)
          .subscribe(
          result => {
            this.products = this.portfolioListProductsStrategy.parse(result);
            this.allProducts = this.products;
            this.headingModel = this.getHeadingModel();
          },
          error => console.log(error)
          );
      }

  }


  onItemSelect(item: any) {
    this.directLoading = false;
    this.portfolios = [];
    this.selectedLob = item.itemName;
    this.selectedExecutive = '';
    this.dropdownListExecutive = [];
    this.selectedItemsExecutive = [];
    this.show = false;
    this.ng4LoadingSpinnerService.show();
    if (item.itemName === 'All Portfolios') {
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

      this.portfolioService.getExecutivesListAll('All')
        .subscribe(
        result => {
          this.executiveList = result;
          for (var key in this.executiveList) {
            this.dropdownListExecutive.push({ 'id': key, 'itemName': this.executiveList[key] });
          }
          if (this.dropdownListExecutive.length > 0) {
            this.dropdownListExecutive.sort(function (a, b) {
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

      this.portfolioService.getExecutivesListAll(item.itemName)
        .subscribe(
        result => {
          this.executiveList = result;
          for (var key in this.executiveList) {
            this.dropdownListExecutive.push({ 'id': key, 'itemName': this.executiveList[key] });
            this.selectedExecs.push(key)
          }
          if (this.dropdownListExecutive.length > 0) {
            this.dropdownListExecutive.sort(function (a, b) {
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

    this.isFav = false;
    this.directLoading = true;
    this.dropdownList = [{ 'id': 1, 'itemName': 'All Portfolios' }];
    this.selectedItems = [{ 'id': 1, 'itemName': 'All Portfolios' }];
    this.dropdownListExecutive = [];
    this.selectedItemsExecutive = [];
    this.ng4LoadingSpinnerService.show();
    this.loadPortfolios();
    this.loadExecutivesList();
    this.loadBusinessUnits();
  }

  onSelectAll(items: any) {
    this.isFav = false;
    this.directLoading = false;
  }

  onDeSelectAll(items: any) {
    this.isFav = false;
    this.directLoading = false;
  }

  onCloseExecutive(items: any) {
    const eids = [];
    if (this.closeable) {
      this.directLoading = false;
      this.selectedItemsExecutive.forEach(function (arrayItem) {
        eids.push(arrayItem.id);
      });

      this.selectedExecs = eids;
      this.productService.getPortfolioExecutivesEids(eids)
        .subscribe(
        result => {
          this.products = this.portfolioListProductsStrategy.parse(result);
          this.allProducts = this.products;
          this.headingModel = this.getHeadingModel();
        },
        error => console.log(error)
        );


      if (this.selectedItemsExecutive.length <= 0) {

        this.dropdownList = [{ 'id': 1, 'itemName': 'All Portfolios' }];
        this.selectedItems = [{ 'id': 1, 'itemName': 'All Portfolios' }];
        this.dropdownListExecutive = [];
        this.selectedItemsExecutive = [];
        //this.ng4LoadingSpinnerService.show();
        this.loadPortfolios();
        this.loadExecutivesList();
        this.loadBusinessUnits();
        this.portfolioService.getReportings(this.portfolioId)
          .subscribe(
          result => {
            this.executiveList = result;
            for (var key in this.executiveList) {
              this.selectedItemsExecutive.push({ 'id': key, 'itemName': this.executiveList[key] });
              this.selectedExecs.push(key)
            }
            //this.ng4LoadingSpinnerService.hide();
          },
          error => { console.log(error); }
          );

        this.directLoading = true;
        this.productService.getPortfolioExecutives(this.portfolioId)
          .subscribe(
          result => {
            this.products = this.portfolioListProductsStrategy.parse(result);
            this.allProducts = this.products;
            this.headingModel = this.getHeadingModel();
            //this.ng4LoadingSpinnerService.hide();
          },
          error => console.log(error)
          );


      }
    }
    this.closeable = false;

  }

  onItemSelectExecutive(items: any) {

    this.isFav = false;
    this.closeable = true;
    this.directLoading = false;
  }


  OnItemDeSelectExecutive(item: any) {

    this.isFav = false;
    this.closeable = true;
    this.directLoading = false;
  }

  onSelectAllExecutive(items: any) {

    this.closeable = true;
    this.directLoading = false;
  }

  onDeSelectAllExecutive(items: any) {
    this.closeable = true;
  }

  getHeadingModel() {
    return {
      primaryText: this.getHeading(),
      icon: this.getIcon(),
      crumbs: this.getCrumbs(),
      button: this.getReturnRouteModel()
    };
  }

  getReturnRouteModel() {
    return {
      label: 'Change Portfolio',
      commands: ['directory'],
      extras: {}
    };
  }

  search() {
    this.products = this.allProducts;
    const value: string = this.searchString;
    this.productsAvailable = true;

    if (this.searchString != null) {
      const product = [];
      const product2 = [];
      this.products.forEach(element => {
        if ((element.name).toLowerCase().includes(value.toLowerCase())) {
          product.push(element);
        }
      });

      this.products = product;
      if (this.products.length <= 0) {
        this.productsAvailable = false;
      }
      this.msg = 'No Matches Found !!';
    }
  }

  getIcon() {
    return 'briefcase';
  }

  getHeading() {
    return this.portfolioHeading;
  }

  getCrumbs() {
    return [];
  }

  goToPortfolioDashboard() {
    this.router.navigate(['portfolio', this.portfolioId]);
  }

   onCloseMetrics(items: any) {
    const ids = [];
    if (this.closeableMetrics) {
       this.selectedItemsMetrics.forEach(function (arrayItem) {
        ids.push(arrayItem.id);
      });
       this.selectedCardsList = ids;

       if (this.selectedCardsList.length === 0) {
          this.selectedCardsList = this.cardsList;
       }

    }

    this.closeableMetrics = false;

  }

  onItemSelectMetrics(items: any) {
    this.closeableMetrics = true;
  }


  OnItemDeSelectMetrics(item: any) {
    this.closeableMetrics = true;
    this.directLoading = false;
  }

  onSelectAllMetrics(items: any) {
    this.closeableMetrics = true;
  }

  onDeSelectAllMetrics(items: any) {
    this.closeableMetrics = true;
  }


}
