import {MetricDetailModel} from '../../../../shared/component-models/metric-detail-model';
import {WorkInProgressDetailStrategy} from '../../strategies/work-in-progress-detail-strategy';
import {WorkInProgressService} from '../../services/work-in-progress.service';
import {Component, OnInit} from '@angular/core';
import {MetricDetailBaseComponent} from '../../../../shared/components/metric-detail/metric-detail-base.component';
import {ActivatedRoute, Router, Params} from '@angular/router';
import {PortfolioService} from '../../../../../../shared/shared.module';
import {AuthService} from '../../../../../../../services/vz/auth.service';
import {TrackService} from '../../../../../../../services/vz/track.service';
import {WorkInProgressBuildingBlocksStrategy} from '../../strategies/work-in-progress-building-blocks-strategy';
import {ProductService} from '../../../../shared/services/product.service';
import {WorkInProgressConfiguration} from '../../work-in-progress.configuration';
import {Location} from '@angular/common';
import { Ng4LoadingSpinnerModule, Ng4LoadingSpinnerComponent, Ng4LoadingSpinnerService } from 'ng4-loading-spinner';

@Component({
  selector: 'app-work-in-progress-detail',
  templateUrl: '../../../../shared/components/metric-detail/metric-detail-base.component.html',
  styleUrls: ['../../../../shared/components/metric-detail/metric-detail-base.component.scss'],
  providers: [
    WorkInProgressService,
    ProductService,
    Ng4LoadingSpinnerComponent
  ]
})
export class WorkInProgressDetailComponent extends MetricDetailBaseComponent implements OnInit {

  constructor(protected router: Router,
              private activatedRoute: ActivatedRoute,
              private WorkInProgressService: WorkInProgressService,
              protected portfolioService: PortfolioService,
              protected productService: ProductService,
              public buildingBlockStrategy: WorkInProgressBuildingBlocksStrategy,
              public detailStrategy: WorkInProgressDetailStrategy,
              protected ng4LoadingSpinnerService: Ng4LoadingSpinnerService,
              protected location: Location,
              protected authService: AuthService,
              protected trackService: TrackService) {

    super();
  }

  ngOnInit() {
    this.metricDetailView = new MetricDetailModel;
    this.dataService = this.WorkInProgressService;
    this.activatedRoute.params.subscribe((params: Params) => {
      this.portfolioId = params['portfolio-id'];
      this.productId = params['product-id'];
      this.loadData();
      this.headingModel = this.getHeader();
    });
  }

  getHeader() {
    return {
      primaryText: this.heading || WorkInProgressConfiguration.detailHeading,
      crumbs: this.getCrumbs(),
      button: this.getReturnRouteModel(),
      icon: this.getIcon()
    };
  }
}