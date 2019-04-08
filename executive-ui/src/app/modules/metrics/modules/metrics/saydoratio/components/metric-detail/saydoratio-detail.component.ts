import { MetricDetailModel } from '../../../../shared/component-models/metric-detail-model';
import { SayDoRatioDetailStrategy } from '../../strategies/saydoratio-detail-strategy';
import { SayDoRatioService } from '../../services/saydoratio.service';
import { Component, OnInit } from '@angular/core';
import { MetricDetailBaseComponent } from '../../../../shared/components/metric-detail/metric-detail-base.component';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { PortfolioService } from '../../../../../../shared/shared.module';
import { AuthService } from '../../../../../../../services/vz/auth.service';
import { TrackService } from '../../../../../../../services/vz/track.service';
import { SayDoRatioBuildingBlocksStrategy } from '../../strategies/saydoratio-building-blocks-strategy';
import { ProductService } from '../../../../shared/services/product.service';
import { SayDoRatioConfiguration } from '../../saydoratio.configuration';
import { Location } from '@angular/common';
import { Ng4LoadingSpinnerModule, Ng4LoadingSpinnerComponent, Ng4LoadingSpinnerService } from 'ng4-loading-spinner';

@Component({
  selector: 'app-saydoratio-detail',
  templateUrl: '../../../../shared/components/metric-detail/metric-detail-base.component.html',
  styleUrls: ['../../../../shared/components/metric-detail/metric-detail-base.component.scss'],
  providers: [
    SayDoRatioService,
    Ng4LoadingSpinnerComponent,
    ProductService
  ]
})
export class SayDoRatioDetailComponent extends MetricDetailBaseComponent implements OnInit {

  constructor(protected router: Router,
    private activatedRoute: ActivatedRoute,
    private securityViolationsService: SayDoRatioService,
    protected portfolioService: PortfolioService,
    protected productService: ProductService,
    public buildingBlockStrategy: SayDoRatioBuildingBlocksStrategy,
    public detailStrategy: SayDoRatioDetailStrategy,
    protected ng4LoadingSpinnerService: Ng4LoadingSpinnerService,
    protected location: Location,
    protected authService: AuthService,
    protected trackService: TrackService) {
    super();
  }

  ngOnInit() {
    this.metricDetailView = new MetricDetailModel;
    this.heading = this.heading || SayDoRatioConfiguration.detailHeading;
    this.dataService = this.securityViolationsService;

    this.activatedRoute.params.subscribe((params: Params) => {
      this.portfolioId = params['portfolio-id'];
      this.productId = params['product-id'];
      this.loadData();
      this.headingModel = this.getHeader();

    });
  }

  getHeader() {
    return {
      primaryText: this.heading || SayDoRatioConfiguration.detailHeading,
      crumbs: this.getCrumbs(),
      button: this.getReturnRouteModel(),
      icon: this.getIcon()
    };
  }
}
