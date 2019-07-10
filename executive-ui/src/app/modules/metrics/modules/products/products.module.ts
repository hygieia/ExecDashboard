import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FlexLayoutModule} from '@angular/flex-layout';
import {SharedModule} from '../../../shared/shared.module';
import {DashboardSharedModule} from '../shared/shared.module';
import {MetricMapService} from '../shared/services/metric-map.service';
import {OpenSourceViolationsModule} from '../metrics/open-source-violations/open-source-violations.module';
import {ProductionIncidentsModule} from '../metrics/production-incidents/production-incidents.module';
import {ProductionReleasesModule} from '../metrics/production-releases/production-releases.module';
import {SecurityViolationsModule} from '../metrics/security-violations/security-violations.module';
import {QualityModule} from '../metrics/quality/quality.module';
import {WorkInProgressModule} from '../metrics/work-in-progress/work-in-progress.module';
import {CodeRepoModule} from '../metrics/code-repo/code-repo.module';
import {StaticCodeAnalysisModule} from '../metrics/static-code-analysis/static-code-analysis.module';
import {TestAutomationModule} from '../metrics/test-automation/test-automation.module';
import {UnitTestCoverageModule} from '../metrics/unit-test-coverage/unit-test-coverage.module';
import {PipelineLeadTimeModule} from '../metrics/pipeline-lead-time/pipeline-lead-time.module';
import {ProductListComponent} from './components/product-list/product-list.component';
import {PortfolioListComponent} from './components/portfolio-list/portfolio-list.component';
import {ProductsRoutingModule} from './products.routing.module';
import {ProductListProductsStrategy} from './strategies/product-list-products-strategy';
import {PortfolioListProductsStrategy} from './strategies/portfolio-list-products-strategy';
import { AngularMultiSelectModule } from 'angular2-multiselect-dropdown/multiselect.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {BuildModule} from '../metrics/build/build.module';
import {DeployModule} from '../metrics/deploy/deploy.module';
import {DevopscupModule} from '../metrics/devopscup/devopscup.module';
import { Ng4LoadingSpinnerModule, Ng4LoadingSpinnerComponent, Ng4LoadingSpinnerService } from 'ng4-loading-spinner';

@NgModule({
  imports: [
    FlexLayoutModule,
    ProductsRoutingModule,
    CommonModule,
    SharedModule,
    DashboardSharedModule,
    OpenSourceViolationsModule,
    ProductionIncidentsModule,
    ProductionReleasesModule,
    SecurityViolationsModule,
    StaticCodeAnalysisModule,
    TestAutomationModule,
    UnitTestCoverageModule,
    PipelineLeadTimeModule,
    QualityModule,
    BuildModule,
    DeployModule,
    Ng4LoadingSpinnerModule,
    WorkInProgressModule,
    CodeRepoModule,
    FormsModule, ReactiveFormsModule, AngularMultiSelectModule,
    DevopscupModule
  ],
  declarations: [
    ProductListComponent,
    PortfolioListComponent
  ],
  exports: [
    ProductListComponent,
    PortfolioListComponent
  ],
  providers: [
    ProductListProductsStrategy,
    PortfolioListProductsStrategy,
    MetricMapService
  ]
})

export class ProductsModule {}
