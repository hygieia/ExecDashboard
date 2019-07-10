import {MetricDetailModel} from '../../../../shared/component-models/metric-detail-model';
import {TestAutomationDetailStrategy} from '../../strategies/test-automation-detail-strategy';
import {TestAutomationService} from '../../services/test-automation.service';
import {Component, OnInit} from '@angular/core';
import {MetricDetailBaseComponent} from '../../../../shared/components/metric-detail/metric-detail-base.component';
import {ActivatedRoute, Router, Params} from '@angular/router';
import {PortfolioService} from '../../../../../../shared/shared.module';
import {AuthService} from '../../../../../../../services/vz/auth.service';
import {TrackService} from '../../../../../../../services/vz/track.service';
import {TestAutomationBuildingBlocksStrategy} from '../../strategies/test-automation-building-blocks-strategy';
import {ProductService} from '../../../../shared/services/product.service';
import {TestAutomationConfiguration} from '../../test-automation.configuration';
import {Location} from '@angular/common';
import { Ng4LoadingSpinnerModule, Ng4LoadingSpinnerComponent, Ng4LoadingSpinnerService } from 'ng4-loading-spinner';

@Component({
  selector: 'app-test-automation-detail',
  templateUrl: '../../../../shared/components/metric-detail/metric-detail-base.component.html',
  styleUrls: ['../../../../shared/components/metric-detail/metric-detail-base.component.scss'],
  providers: [
    TestAutomationService,
    ProductService,
    Ng4LoadingSpinnerComponent
  ]
})
export class TestAutomationDetailComponent extends MetricDetailBaseComponent implements OnInit {

  constructor(protected router: Router,
              private activatedRoute: ActivatedRoute,
              private testAutomationService: TestAutomationService,
              protected portfolioService: PortfolioService,
              protected productService: ProductService,
              public buildingBlockStrategy: TestAutomationBuildingBlocksStrategy,
              public detailStrategy: TestAutomationDetailStrategy,
              protected ng4LoadingSpinnerService: Ng4LoadingSpinnerService,
              protected location: Location,
              protected authService: AuthService,
              protected trackService: TrackService) {
    super();
  }

  ngOnInit() {
    this.metricDetailView = new MetricDetailModel;
    this.heading = this.heading || TestAutomationConfiguration.detailHeading;
    this.dataService = this.testAutomationService;
   
    this.activatedRoute.params.subscribe((params: Params) => {
      this.portfolioId = params['portfolio-id'];
      this.productId = params['product-id'];
      this.loadData();
      this.headingModel = this.getHeader();
    });
  }

  getHeader() {
    return {
      primaryText: this.heading || TestAutomationConfiguration.detailHeading,
      crumbs: this.getCrumbs(),
      button: this.getReturnRouteModel(),
      icon: this.getIcon()
    };
  }
}
