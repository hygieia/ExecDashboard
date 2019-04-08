import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FlexLayoutModule } from '@angular/flex-layout';
import { SharedModule } from '../../../shared/shared.module';
import { DashboardSharedModule } from '../shared/shared.module';
import { MetricMapService } from '../shared/services/metric-map.service';
import { OpenSourceViolationsModule } from '../metrics/open-source-violations/open-source-violations.module';
import { ProductionIncidentsModule } from '../metrics/production-incidents/production-incidents.module';
import { ProductionReleasesModule } from '../metrics/production-releases/production-releases.module';
import { SecurityViolationsModule } from '../metrics/security-violations/security-violations.module';
import { QualityModule } from '../metrics/quality/quality.module';
import { StaticCodeAnalysisModule } from '../metrics/static-code-analysis/static-code-analysis.module';
import { TestAutomationModule } from '../metrics/test-automation/test-automation.module';
import { UnitTestCoverageModule } from '../metrics/unit-test-coverage/unit-test-coverage.module';
import { MetricPreviewsComponent } from '../previews/components/metric-previews/metric-previews.component';
import { MetricProgramsComponent } from '../previews/components/metric-programs/metric-programs.component';
import { PipelineLeadTimeModule } from '../metrics/pipeline-lead-time/pipeline-lead-time.module';
import { WorkInProgressModule } from '../metrics/work-in-progress/work-in-progress.module';
import { CloudModule } from '../metrics/cloud/cloud.module';
import { CodeRepoModule } from '../metrics/code-repo/code-repo.module';
import { ThroughputModule } from '../metrics/throughput/throughput.module';
import { PreviewsRoutingModule } from './previews.routing.module';
import { BuildModule } from '../metrics/build/build.module';
import { DeployModule } from '../metrics/deploy/deploy.module';
import { SayDoRatioModule } from '../metrics/saydoratio/saydoratio.module';
import { TestModule } from '../metrics/test/test.module';
import { DevopscupModule } from '../metrics/devopscup/devopscup.module';
//import {ProgramDetailsComponent} from '../../programs/components/program-details/program-details.component';
//import {ProgramModule} from '../../programs/components/program-module';

@NgModule({
  imports: [
    FlexLayoutModule,
    PreviewsRoutingModule,
    CommonModule,
    SharedModule,
    DashboardSharedModule,
    OpenSourceViolationsModule,
    ProductionIncidentsModule,
    SecurityViolationsModule,
    PipelineLeadTimeModule,
    WorkInProgressModule,
    //ProgramModule,
    //ProgramDetailsComponent, 
    QualityModule,
    ThroughputModule,
    CloudModule,
    CodeRepoModule,
    BuildModule,
    DeployModule,
    SayDoRatioModule,
    TestModule,
    DevopscupModule
  ],
  declarations: [
    MetricPreviewsComponent,
    MetricProgramsComponent
  ],
  exports: [
    MetricPreviewsComponent,
    MetricProgramsComponent
  ],
  providers: [
    MetricMapService
  ]
})

export class PreviewsModule { }