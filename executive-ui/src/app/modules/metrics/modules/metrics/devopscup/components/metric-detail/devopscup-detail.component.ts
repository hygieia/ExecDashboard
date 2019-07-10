import { MetricDetailModel } from '../../../../shared/component-models/metric-detail-model';
import { DevopscupDetailStrategy } from '../../strategies/devopscup-detail-strategy';
import { DevopscupService } from '../../services/devopscup.service';
import { Component, OnInit } from '@angular/core';
import { MetricDetailBaseComponent } from '../../../../shared/components/metric-detail/metric-detail-base.component';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { PortfolioService } from '../../../../../../shared/shared.module';
import { AuthService } from '../../../../../../../services/vz/auth.service';
import { TrackService } from '../../../../../../../services/vz/track.service';
import { DevopscupBuildingBlocksStrategy } from '../../strategies/devopscup-building-blocks-strategy';
import { ProductService } from '../../../../shared/services/product.service';
import { DevopsCupConfiguration } from '../../devopscup.configuration';
import { Location } from '@angular/common';
import { Ng4LoadingSpinnerModule, Ng4LoadingSpinnerComponent, Ng4LoadingSpinnerService } from 'ng4-loading-spinner';
@Component({
  selector: 'app-devopscup-detail',
  templateUrl: '../../../../shared/components/metric-detail/metric-detail-base.component.html',
  styleUrls: ['../../../../shared/components/metric-detail/metric-detail-base.component.scss'],
  providers: [
    DevopscupService,
    Ng4LoadingSpinnerComponent,
    ProductService
  ]
})
export class DevopscupDetailComponent extends MetricDetailBaseComponent implements OnInit {

  constructor(protected router: Router,
    private activatedRoute: ActivatedRoute,
    public devopscupService: DevopscupService,
    protected portfolioService: PortfolioService,
    protected productService: ProductService,
    public buildingBlockStrategy: DevopscupBuildingBlocksStrategy,
    public detailStrategy: DevopscupDetailStrategy,
    protected ng4LoadingSpinnerService: Ng4LoadingSpinnerService,
    protected location: Location,
    protected authService: AuthService,
    protected trackService: TrackService
  ) {

    super();
  }

  ngOnInit() {
    this.metricDetailView = new MetricDetailModel;
    this.dataService = this.devopscupService;

    this.activatedRoute.params.subscribe((params: Params) => {
      this.portfolioId = params['portfolio-id'];
      this.productId = params['product-id'];
      this.loadData();
      this.headingModel = this.getHeader();
    });
  }

  getHeader() {
    return {
      primaryText: this.heading || DevopsCupConfiguration.detailHeading,
      crumbs: this.getCrumbs(),
      button: this.getReturnRouteModel(),
      icon: this.getIcon()
    };
  }
}