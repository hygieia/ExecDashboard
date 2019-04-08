import { MetricDetailModel } from '../../../../shared/component-models/metric-detail-model';
import { CloudDetailStrategy } from '../../strategies/cloud-detail-strategy';
import { CloudService } from '../../services/cloud.service';
import { Component, OnInit } from '@angular/core';
import { MetricDetailBaseComponent } from '../../../../shared/components/metric-detail/metric-detail-base.component';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { PortfolioService } from '../../../../../../shared/shared.module';
import { AuthService } from '../../../../../../../services/vz/auth.service';
import { TrackService } from '../../../../../../../services/vz/track.service';
import { CloudBuildingBlocksStrategy } from '../../strategies/cloud-building-blocks-strategy';
import { ProductService } from '../../../../shared/services/product.service';
import { CloudConfiguration } from '../../cloud.configuration';
import { Location } from '@angular/common';
import { Ng4LoadingSpinnerModule, Ng4LoadingSpinnerComponent, Ng4LoadingSpinnerService } from 'ng4-loading-spinner';

@Component({
  selector: 'app-cloud-detail',
  templateUrl: '../../../../shared/components/metric-detail/metric-detail-base.component.html',
  styleUrls: ['../../../../shared/components/metric-detail/metric-detail-base.component.scss'],
  providers: [
    CloudService,
    Ng4LoadingSpinnerComponent,
    ProductService
  ]
})
export class CloudDetailComponent extends MetricDetailBaseComponent implements OnInit {

  constructor(protected router: Router,
    private activatedRoute: ActivatedRoute,
    private cloudService: CloudService,
    protected portfolioService: PortfolioService,
    protected productService: ProductService,
    public buildingBlockStrategy: CloudBuildingBlocksStrategy,
    public detailStrategy: CloudDetailStrategy,
    protected ng4LoadingSpinnerService: Ng4LoadingSpinnerService,
    protected location: Location,
    protected authService: AuthService,
    protected trackService: TrackService) {
    super();
  }

  ngOnInit() {
    this.metricDetailView = new MetricDetailModel;
    this.heading = this.heading || CloudConfiguration.detailHeading;
    this.dataService = this.cloudService;

    this.activatedRoute.params.subscribe((params: Params) => {
      this.portfolioId = params['portfolio-id'];
      this.productId = params['product-id'];
      this.loadData();
      this.headingModel = this.getHeader();
    });
  }

  getHeader() {
    return {
      primaryText: this.heading || CloudConfiguration.detailHeading,
      crumbs: this.getCrumbs(),
      button: this.getReturnRouteModel(),
      icon: this.getIcon()
    };
  }
}
