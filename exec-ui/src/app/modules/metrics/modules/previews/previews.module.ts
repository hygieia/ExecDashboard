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
import {StaticCodeAnalysisModule} from '../metrics/static-code-analysis/static-code-analysis.module';
import {TestAutomationModule} from '../metrics/test-automation/test-automation.module';
import {UnitTestCoverageModule} from '../metrics/unit-test-coverage/unit-test-coverage.module';
import {MetricPreviewsComponent} from '../previews/components/metric-previews/metric-previews.component';
import {PipelineLeadTimeModule} from '../metrics/pipeline-lead-time/pipeline-lead-time.module';
import {PreviewsRoutingModule} from './previews.routing.module';
import {TraceabilityModule} from "../metrics/traceability/traceability.module";
import {SCMCommitsModule} from '../metrics/scm-commits/scm-commits.module';

@NgModule({
  imports: [
    FlexLayoutModule,
    PreviewsRoutingModule,
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
    TraceabilityModule,
    PipelineLeadTimeModule,
    SCMCommitsModule
  ],
  declarations: [
    MetricPreviewsComponent
  ],
  exports: [
    MetricPreviewsComponent
  ],
  providers: [
    MetricMapService
  ]
})

export class PreviewsModule {}
