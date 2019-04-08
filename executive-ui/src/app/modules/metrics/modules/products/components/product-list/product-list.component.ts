import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { Params, ActivatedRoute, Router } from '@angular/router';
import { PortfolioService } from '../../../../../shared/shared.module';
import { Portfolio } from '../../../../../shared/domain-models/portfolio';
import { ProductService } from '../../../shared/services/product.service';
import { BuildingBlockModel } from '../../../shared/component-models/building-block-model';
import { HeadingModel } from '../../../../../shared/component-models/heading-model';
import { ProductListProductsStrategy } from '../../strategies/product-list-products-strategy';
import { Ng4LoadingSpinnerModule, Ng4LoadingSpinnerComponent, Ng4LoadingSpinnerService } from 'ng4-loading-spinner';
import { AuthService } from '../../../../../../services/vz/auth.service';
import { TrackService } from '../../../../../../services/vz/track.service';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss'],
  providers: [
    PortfolioService,
    ProductService,
    Ng4LoadingSpinnerComponent
  ]
})
export class ProductListComponent implements OnInit {
  public portfolioHeading: string;
  public portfolioId: string;
  public productHeading: string;
  public productId: string;
  public role: string;
  public products: BuildingBlockModel[];
  public allProducts: BuildingBlockModel[];
  public productsCopy: BuildingBlockModel[];
  public headingModel: HeadingModel;
  public searchString: string;
  public msg: string = 'Loading Products';
  public productsAvailable: boolean = true;
  public productView: boolean = true;
  public noOfApps: number;
  public selectedPortfolios = new Array<Portfolio>();
  public businessUnits = new Array<String>();
  public selectedApps = new Array<String>();
  public allApps = new Array<String>();
  public selectedLob = '';
  public selectedApplication = '';
  public closeableMetrics = false;
  public cardsList = [];
  public selectedCardsList = [];
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
  previewCardsList = {};
  dropdownSettingsMetrics = {};
  dropdownListMetrics = [];
  selectedItemsMetrics = [];

  constructor(private router: Router,
    private activatedRoute: ActivatedRoute,
    private portfolioService: PortfolioService,
    private productService: ProductService,
    private authService: AuthService,
    private trackService: TrackService,
    private productListProductsStrategy: ProductListProductsStrategy,
    private ng4LoadingSpinnerService: Ng4LoadingSpinnerService) {
  }

  ngOnInit() {
    this.ng4LoadingSpinnerService.show();
    this.activatedRoute.params.subscribe((params: Params) => {
      this.portfolioId = params['portfolio-id'];
      this.productId = params['product-id'];
      this.portfolioService.getPortfolio(this.portfolioId)
        .subscribe(
        result => {
          this.portfolioHeading = `${getPortfolioName(result)}'s Products`;
          this.role = result.executive.role;
          this.headingModel = this.getHeadingModel();
          this.trackComparisonPage();
        },
        error => {
          console.log(error);
        }
        );

      if (this.productId) {
        this.productService.getPortfolioProduct(this.portfolioId, this.productId)
          .subscribe(
          result => {
            this.productHeading = result.name;
            this.headingModel = this.getHeadingModel();
          },
          error => {
            console.log(error);
          }
          );

      } else {
        this.productService.getPortfolioProducts(this.portfolioId)
          .subscribe(
          result => {
            this.products = this.productListProductsStrategy.parse(result);
            this.allProducts = this.products;
            this.allSelectedProducts = this.products;
            this.noOfApps = this.allProducts.length;
            this.headingModel = this.getHeadingModel();
            this.ng4LoadingSpinnerService.hide();
          },
          error => {
            console.log(error);
            this.ng4LoadingSpinnerService.hide();
          }
          );
      }
    });

    this.headingModel = this.getHeadingModel();
    this.dropdownListApplication = [];
    this.selectedItemsApplication = [];
    this.selectedItemsMetrics = [];
    this.dropdownList = [{ 'id': 1, 'itemName': 'All Portfolios' }];
    this.selectedItems = [{ 'id': 1, 'itemName': 'All Portfolios' }];
    this.dropdownSettings = { singleSelection: true, text: 'Select Portfolio', enableSearchFilter: true, classes: 'myclass custom-class' };
    this.dropdownSettingsApplication = { singleSelection: false, text: 'Select Applications', enableSearchFilter: true, badgeShowLimit: 1 };
    this.dropdownSettingsMetrics = { singleSelection: false, text: 'Select Metrics', enableSearchFilter: true, badgeShowLimit: 3, limitSelection: 9 };
    this.ng4LoadingSpinnerService.show();
    this.loadBusinessUnitsForExecutive(this.portfolioId);
    this.loadApplicationsListForExecutive(this.portfolioId);
    this.loadMetricsName();
    function getPortfolioName(portfolio: Portfolio) {
      if (portfolio.name) {
        return portfolio.name;
      }
      return `${portfolio.executive.firstName} ${portfolio.executive.lastName}`;
    }
  }

   loadApplicationsListForExecutive(id: string) {
    this.dropdownListApplication = [];
    this.selectedItemsApplication = [];
    this.selectedApps = [];
    this.portfolioService.getApplicationListAllForExec(id)
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
        if (this.allProducts != undefined) {
          for (let i = 0; i < this.allProducts.length; i++) {
            if (this.selectedApps.includes(this.allProducts[i].appId)) {
              this.allSelectedProducts.push(this.allProducts[i]);
            }
          }
        }
        this.products = this.allSelectedProducts;
        this.noOfApps = this.products.length;
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
        this.ng4LoadingSpinnerService.hide();
      },
      error => { console.log(error); }
      );
  }


  loadBusinessUnitsForExecutive(id: string) {
    this.portfolioService.getBusinessUnitForExecutive(id)
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
        this.ng4LoadingSpinnerService.show();
        this.onItemSelect(this.selectedLob); // need to chk this
      },
      error => { console.log(error); }
      );
    }

   trackComparisonPage() {
    this.trackService.savePageTracking('Product Metric Comparison View', this.authService.getAuthEid()).subscribe(
      result => {
      },
      error => {
      }
    );
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
      commands: ['portfolio'],
      extras: {}
    };
  }

  search() {
    this.products = this.allSelectedProducts;
    let value = this.searchString;
    this.productsAvailable = true;

    if (this.searchString != null) {
      let product = [];
      this.products.forEach(element => {
        if ((element.name).toLowerCase().includes(value.toLowerCase())) {
          product.push(element);
        }
      });

      this.products = product;
      if (this.products.length <= 0) {
        this.productsAvailable = false;
      }
      this.msg = 'Please Check the application name';
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
  }

  onSelectAllMetrics(items: any) {
    this.closeableMetrics = true;
  }

  onDeSelectAllMetrics(items: any) {
    this.closeableMetrics = true;
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
    this.allSelectedProducts = [];
    this.ng4LoadingSpinnerService.show();
    if (item.itemName === 'All Portfolios') {
        this.loadApplicationsListForExecutive(this.portfolioId);
    } else {
      this.portfolioService.getApplicationListAllForExecWithBunit(this.selectedLob, this.portfolioId)
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
        if (this.allProducts !== undefined) {
          for (let i = 0; i < this.allProducts.length; i++) {
            if (this.selectedApps.includes(this.allProducts[i].appId)) {
              this.allSelectedProducts.push(this.allProducts[i]);
            }
          }
        }
        this.products = this.allSelectedProducts;
        this.noOfApps = this.products.length;

        this.ng4LoadingSpinnerService.hide();
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

    this.directLoading = true;
    this.dropdownList = [{ 'id': 1, 'itemName': 'All Portfolios' }];
    this.selectedItems = [{ 'id': 1, 'itemName': 'All Portfolios' }];
    this.dropdownListApplication = [];
    this.selectedItemsApplication = [];
    this.ng4LoadingSpinnerService.show();
    this.loadBusinessUnitsForExecutive(this.portfolioId);
    this.loadApplicationsListForExecutive(this.portfolioId);
  }

  onSelectAll(items: any) {
    this.directLoading = false;
  }

  onDeSelectAll(items: any) {
    this.directLoading = false;
  }

  onCloseApplication(items: any) {
    const appIds = [];
    if (this.closeable) {
      this.directLoading = false;
      this.products = [];
      this.selectedItemsApplication.forEach(function (arrayItem) {
        appIds.push(arrayItem.id);
      });
      this.selectedApps = appIds;
      if (this.allProducts !== undefined) {
        for (let i = 0; i < this.allProducts.length; i++) {
          if (this.selectedApps.includes(this.allProducts[i].appId)) {
            this.products.push(this.allProducts[i]);
          }
        }
      }
       this.allSelectedProducts = this.products;
       this.noOfApps = this.products.length;
      if (this.selectedItemsApplication.length <= 0) {
        this.dropdownList = [{ 'id': 1, 'itemName': 'All Portfolios' }];
        this.selectedItems = [{ 'id': 1, 'itemName': 'All Portfolios' }];
        this.dropdownListApplication = [];
        this.selectedItemsApplication = [];
        this.loadApplicationsListForExecutive(this.portfolioId);
        this.loadBusinessUnitsForExecutive(this.portfolioId);
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



}

