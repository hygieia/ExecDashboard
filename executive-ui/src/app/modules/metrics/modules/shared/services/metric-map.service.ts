import {Injectable} from '@angular/core';
import {OpenSourceViolationsPrimaryMetricStrategy} from '../../metrics/open-source-violations/strategies/open-source-violations-primary-metric-strategy';
import {OpenSourceViolationsTrendStrategy} from '../../metrics/open-source-violations/strategies/open-source-violations-trend-strategy';
import {SecurityViolationsPrimaryMetricStrategy} from '../../metrics/security-violations/strategies/security-violations-primary-metric-strategy';
import {SecurityViolationsTrendStrategy} from '../../metrics/security-violations/strategies/security-violations-trend-strategy';
import {QualityPrimaryMetricStrategy} from '../../metrics/quality/strategies/quality-primary-metric-strategy';
import {QualityTrendStrategy} from '../../metrics/quality/strategies/quality-trend-strategy';
import {TestAutomationPrimaryMetricStrategy} from '../../metrics/test-automation/strategies/test-automation-primary-metric-strategy';
import {TestAutomationTrendStrategy} from '../../metrics/test-automation/strategies/test-automation-trend-strategy';
import {StaticCodeAnalysisPrimaryMetricStrategy} from '../../metrics/static-code-analysis/strategies/static-code-analysis-primary-metric-strategy';
import {StaticCodeAnalysisTrendStrategy} from '../../metrics/static-code-analysis/strategies/static-code-analysis-trend-strategy';
import {ProductionIncidentsPrimaryMetricStrategy} from '../../metrics/production-incidents/strategies/production-incidents-primary-metric-strategy';
import {ProductionIncidentsTrendStrategy} from '../../metrics/production-incidents/strategies/production-incidents-trend-strategy';
import {UnitTestCoveragePrimaryMetricStrategy} from '../../metrics/unit-test-coverage/strategies/unit-test-coverage-primary-metric-strategy';
import {UnitTestCoverageTrendStrategy} from '../../metrics/unit-test-coverage/strategies/unit-test-coverage-trend-strategy';
import {ProductionReleasesPrimaryMetricStrategy} from '../../metrics/production-releases/strategies/production-releases-primary-metric-strategy';
import {ProductionReleasesTrendStrategy} from '../../metrics/production-releases/strategies/production-releases-trend-strategy';
import {PipelineLeadTimePrimaryMetricStrategy} from '../../metrics/pipeline-lead-time/strategies/pipeline-lead-time-primary-metric-strategy';
import {PipelineLeadTimeTrendStrategy} from '../../metrics/pipeline-lead-time/strategies/pipeline-lead-time-trend-strategy';
import {PipelineLeadTimeBuildingBlockPrimaryMetricStrategy} from '../../metrics/pipeline-lead-time/strategies/pipeline-lead-time-building-block-primary-metric-strategy';
import {OpenSourceViolationsConfiguration} from '../../metrics/open-source-violations/open-source-violations.configuration';
import {PipelineLeadTimeConfiguration} from '../../metrics/pipeline-lead-time/pipeline-lead-time.configuration';
import {ProductionIncidentsConfiguration} from '../../metrics/production-incidents/production-incidents.configuration';
import {ProductionReleasesConfiguration} from '../../metrics/production-releases/production-releases.configuration';
import {SecurityViolationsConfiguration} from '../../metrics/security-violations/security-violations.configuration';
import {QualityConfiguration} from '../../metrics/quality/quality.configuration';
import {StaticCodeAnalysisConfiguration} from '../../metrics/static-code-analysis/static-code-analysis.configuration';
import {TestAutomationConfiguration} from '../../metrics/test-automation/test-automation.configuration';
import {UnitTestCoverageConfiguration} from '../../metrics/unit-test-coverage/unit-test-coverage.configuration';
import {WorkInProgressPrimaryMetricStrategy} from '../../metrics/work-in-progress/strategies/work-in-progress-primary-metric-strategy';
import {WorkInProgressTrendStrategy} from '../../metrics/work-in-progress/strategies/work-in-progress-trend-strategy';
import {WorkInProgressConfiguration} from '../../metrics/work-in-progress/work-in-progress.configuration';
import {BuildConfiguration} from '../../metrics/build/build.configuration';
import {DeployConfiguration} from '../../metrics/deploy/deploy.configuration';
import {BuildPrimaryMetricStrategy} from '../../metrics/build/strategies/build-primary-metric-strategy';
import {BuildTrendStrategy} from '../../metrics/build/strategies/build-trend-strategy';
import {DeployPrimaryMetricStrategy} from '../../metrics/deploy/strategies/deploy-primary-metric-strategy';
import {DeployTrendStrategy} from '../../metrics/deploy/strategies/deploy-trend-strategy';
import {ThroughputPrimaryMetricStrategy} from '../../metrics/throughput/strategies/throughput-primary-metric-strategy';
import {ThroughputTrendStrategy} from '../../metrics/throughput/strategies/throughput-trend-strategy';
import {ThroughputConfiguration} from '../../metrics/throughput/throughput.configuration';
import {CodeRepoPrimaryMetricStrategy} from '../../metrics/code-repo/strategies/code-repo-primary-metric-strategy';
import {CodeRepoTrendStrategy} from '../../metrics/code-repo/strategies/code-repo-trend-strategy';
import {CodeRepoConfiguration} from '../../metrics/code-repo/code-repo.configuration';
import {CloudPrimaryMetricStrategy} from '../../metrics/cloud/strategies/cloud-primary-metric-strategy';
import {CloudTrendStrategy} from '../../metrics/cloud/strategies/cloud-trend-strategy';
import {CloudConfiguration} from '../../metrics/cloud/cloud.configuration';
import {SayDoRatioPrimaryMetricStrategy} from '../../metrics/saydoratio/strategies/saydoratio-primary-metric-strategy';
import {SayDoRatioTrendStrategy} from '../../metrics/saydoratio/strategies/saydoratio-trend-strategy';
import {SayDoRatioConfiguration} from '../../metrics/saydoratio/saydoratio.configuration';
import {TestPrimaryMetricStrategy} from '../../metrics/test/strategies/test-primary-metric-strategy';
import {TestTrendStrategy} from '../../metrics/test/strategies/test-trend-strategy';
import {TestConfiguration} from '../../metrics/test/test.configuration';
import {DevopsCupConfiguration } from '../../metrics/devopscup/devopscup.configuration';
import {DevopscupPrimaryMetricStrategy} from '../../metrics/devopscup/strategies/devopscup-primary-metric-strategy';
import {DevopscupTrendStrategy} from '../../metrics/devopscup/strategies/devopscup-trend-strategy';

@Injectable()
export class MetricMapService {
  constructor(private openSourceViolationsTrendStrategy: OpenSourceViolationsTrendStrategy,
              private openSourceViolationsPrimaryMetricStrategy: OpenSourceViolationsPrimaryMetricStrategy,
              private securityViolationsPrimaryMetricStrategy: SecurityViolationsPrimaryMetricStrategy,
              private securityViolationsTrendStrategy: SecurityViolationsTrendStrategy,
              private qualityPrimaryMetricStrategy: QualityPrimaryMetricStrategy,
              private qualityTrendStrategy: QualityTrendStrategy,
              private workInProgressPrimaryMetricStrategy: WorkInProgressPrimaryMetricStrategy,
              private workInProgressTrendStrategy: WorkInProgressTrendStrategy,
              private throughputPrimaryMetricStrategy:ThroughputPrimaryMetricStrategy,
              private throughputTrendStrategy: ThroughputTrendStrategy,
              private codeRepoPrimaryMetricStrategy: CodeRepoPrimaryMetricStrategy,
              private codeRepoTrendStrategy: CodeRepoTrendStrategy,
              private testAutomationPrimaryMetricStrategy: TestAutomationPrimaryMetricStrategy,
              private testAutomationTrendStrategy: TestAutomationTrendStrategy,
              private staticCodeAnalysisPrimaryMetricStrategy: StaticCodeAnalysisPrimaryMetricStrategy,
              private staticCodeAnalysisTrendStrategy: StaticCodeAnalysisTrendStrategy,
              private productionIncidentsPrimaryMetricStrategy: ProductionIncidentsPrimaryMetricStrategy,
              private productionIncidentsTrendStrategy: ProductionIncidentsTrendStrategy,
              private unitTestCoveragePrimaryMetricStrategy: UnitTestCoveragePrimaryMetricStrategy,
              private unitTestCoverageTrendStrategy: UnitTestCoverageTrendStrategy,
              private productionReleasesPrimaryMetricStrategy: ProductionReleasesPrimaryMetricStrategy,
              private productionReleasesTrendStrategy: ProductionReleasesTrendStrategy,
              private pipelineLeadTimePrimaryMetricStrategy: PipelineLeadTimePrimaryMetricStrategy,
              private pipelineLeadTimeTrendStrategy: PipelineLeadTimeTrendStrategy,
              private pipelineLeadTimeBuildingBlockPrimaryMetricStrategy: PipelineLeadTimeBuildingBlockPrimaryMetricStrategy,
              private buildPrimaryMetricStrategy: BuildPrimaryMetricStrategy,
              private buildTrendStrategy: BuildTrendStrategy,
              private deployPrimaryMetricStrategy: DeployPrimaryMetricStrategy,
              private deployTrendStrategy: DeployTrendStrategy,
              private cloudPrimaryMetricStrategy: CloudPrimaryMetricStrategy,
              private cloudTrendStrategy: CloudTrendStrategy,
              private sayDoRatioPrimaryMetricStrategy: SayDoRatioPrimaryMetricStrategy,
              private sayDoRatioTrendStrategy: SayDoRatioTrendStrategy,
              private testPrimaryMetricStrategy: TestPrimaryMetricStrategy,
              private testTrendStrategy: TestTrendStrategy,
              private devopscupPrimaryMetricStrategy: DevopscupPrimaryMetricStrategy,
              private devopscupTrendStrategy: DevopscupTrendStrategy) { }

  public metrics(): Map<string, any> {
    return new Map<string, any>([
      [OpenSourceViolationsConfiguration.identifier, {
        primaryMetricStrategy: this.openSourceViolationsPrimaryMetricStrategy,
        trendStrategy: this.openSourceViolationsTrendStrategy,
        isRatio: false,
        label: OpenSourceViolationsConfiguration.buildingBlockLabel,
        type: OpenSourceViolationsConfiguration.api
      }],
       [PipelineLeadTimeConfiguration.identifier, {
        primaryMetricStrategy: this.pipelineLeadTimePrimaryMetricStrategy,
        trendStrategy: this.pipelineLeadTimeTrendStrategy,
        buildingBlockPrimaryMetricStrategy: this.pipelineLeadTimeBuildingBlockPrimaryMetricStrategy,
        isRatio: false,
        label: PipelineLeadTimeConfiguration.buildingBlockLabel,
        type: PipelineLeadTimeConfiguration.api
      }],
      [SecurityViolationsConfiguration.identifier, {
        primaryMetricStrategy: this.securityViolationsPrimaryMetricStrategy,
        trendStrategy: this.securityViolationsTrendStrategy,
        isRatio: false,
        label: SecurityViolationsConfiguration.buildingBlockLabel,
        type: SecurityViolationsConfiguration.api
      }],
      [ProductionIncidentsConfiguration.identifier, {
        primaryMetricStrategy: this.productionIncidentsPrimaryMetricStrategy,
        trendStrategy: this.productionIncidentsTrendStrategy,
        isRatio: false,
        label: ProductionIncidentsConfiguration.buildingBlockLabel,
        type: ProductionIncidentsConfiguration.api
      }],
      [QualityConfiguration.identifier, {
        primaryMetricStrategy: this.qualityPrimaryMetricStrategy,
        trendStrategy: this.qualityTrendStrategy,
        isRatio: false,
        label: QualityConfiguration.buildingBlockLabel,
        type: QualityConfiguration.api
      }],
      [WorkInProgressConfiguration.identifier, {
        primaryMetricStrategy: this.workInProgressPrimaryMetricStrategy,
        trendStrategy: this.workInProgressTrendStrategy,
        isRatio: false,
        label: WorkInProgressConfiguration.buildingBlockLabel,
        type: WorkInProgressConfiguration.api
      }],
      [ThroughputConfiguration.identifier, {
        primaryMetricStrategy: this.throughputPrimaryMetricStrategy,
        trendStrategy: this.throughputTrendStrategy,
        isRatio: false,
        label: ThroughputConfiguration.buildingBlockLabel,
        type: ThroughputConfiguration.api
      }],
      [BuildConfiguration.identifier, {
        primaryMetricStrategy: this.buildPrimaryMetricStrategy,
        trendStrategy: this.buildTrendStrategy,
        isRatio: false,
        label: BuildConfiguration.buildingBlockLabel,
        type: BuildConfiguration.api
      }],
      [DeployConfiguration.identifier, {
        primaryMetricStrategy: this.deployPrimaryMetricStrategy,
        trendStrategy: this.deployTrendStrategy,
        isRatio: false,
        label: DeployConfiguration.buildingBlockLabel,
        type: DeployConfiguration.api
      }],
      [CloudConfiguration.identifier, {
        primaryMetricStrategy: this.cloudPrimaryMetricStrategy,
        trendStrategy: this.cloudTrendStrategy,
        isRatio: false,
        label: CloudConfiguration.buildingBlockLabel,
        type: CloudConfiguration.api
      }],
      [CodeRepoConfiguration.identifier, {
        primaryMetricStrategy: this.codeRepoPrimaryMetricStrategy,
        trendStrategy: this.codeRepoTrendStrategy,
        isRatio: false,
        label: CodeRepoConfiguration.buildingBlockLabel,
        type: CodeRepoConfiguration.api
      }],
      [SayDoRatioConfiguration.identifier, {
        primaryMetricStrategy: this.sayDoRatioPrimaryMetricStrategy,
        trendStrategy: this.sayDoRatioTrendStrategy,
        isRatio: false,
        label: SayDoRatioConfiguration.buildingBlockLabel,
        type: SayDoRatioConfiguration.api
      }],
      [TestConfiguration.identifier, {
        primaryMetricStrategy: this.testPrimaryMetricStrategy,
        trendStrategy: this.testTrendStrategy,
        isRatio: false,
        label: TestConfiguration.buildingBlockLabel,
        type: TestConfiguration.api
      }],
      [DevopsCupConfiguration.identifier, {
        primaryMetricStrategy: this.devopscupPrimaryMetricStrategy,
        trendStrategy: this.devopscupTrendStrategy,
        isRatio: false,
        label: DevopsCupConfiguration.buildingBlockLabel,
        type: DevopsCupConfiguration.api
      }]
    ]);
  }
}

