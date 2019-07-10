import {MetricDetailModel} from '../../../../shared/component-models/metric-detail-model';
import {ThroughputDetailStrategy} from '../../strategies/throughput-detail-strategy';
import {ThroughputService} from '../../services/throughput.service';
import {Component, OnInit} from '@angular/core';
import {MetricDetailBaseComponent} from '../../../../shared/components/metric-detail/metric-detail-base.component';
import {ActivatedRoute, Router, Params} from '@angular/router';
import {PortfolioService} from '../../../../../../shared/shared.module';
import {AuthService} from '../../../../../../../services/vz/auth.service';
import {TrackService} from '../../../../../../../services/vz/track.service';
import {ThroughputBuildingBlocksStrategy} from '../../strategies/throughput-building-blocks-strategy';
import {ProductService} from '../../../../shared/services/product.service';
import {ThroughputConfiguration} from '../../throughput.configuration';
import {Location} from '@angular/common';
import { Ng4LoadingSpinnerModule, Ng4LoadingSpinnerComponent, Ng4LoadingSpinnerService } from 'ng4-loading-spinner';

@Component({
  selector: 'app-throughput-detail',
  templateUrl: '../../../../shared/components/metric-detail/metric-detail-base.component.html',
  styleUrls: ['../../../../shared/components/metric-detail/metric-detail-base.component.scss'],
  providers: [
    ThroughputService,
    ProductService,
    Ng4LoadingSpinnerComponent
  ]
})
export class ThroughputDetailComponent extends MetricDetailBaseComponent implements OnInit {

  constructor(protected router: Router,
              private activatedRoute: ActivatedRoute,
              private openSourceViolationsService: ThroughputService,
              protected portfolioService: PortfolioService,
              protected productService: ProductService,
              public buildingBlockStrategy: ThroughputBuildingBlocksStrategy,
              public detailStrategy: ThroughputDetailStrategy,
              protected ng4LoadingSpinnerService: Ng4LoadingSpinnerService,
              protected location: Location,
              protected authService: AuthService,
              protected trackService: TrackService) {

    super();
  }

  ngOnInit() {
    this.metricDetailView = new MetricDetailModel;
    this.heading = this.heading || ThroughputConfiguration.detailHeading;
    this.dataService = this.openSourceViolationsService;

    this.activatedRoute.params.subscribe((params: Params) => {
      this.portfolioId = params['portfolio-id'];
      this.productId = params['product-id'];
      this.loadData();
      this.headingModel = this.getHeader();
    });
  }

  getHeader() {
    return {
      primaryText: this.heading || ThroughputConfiguration.detailHeading,
      crumbs: this.getCrumbs(),
      button: this.getReturnRouteModel(),
      icon: this.getIcon()
    };
  }
}
