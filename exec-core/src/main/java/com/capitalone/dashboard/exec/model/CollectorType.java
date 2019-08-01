package com.capitalone.dashboard.exec.model;

/**
 * Enumerates the possible {@link Collector} types.
 */
public enum CollectorType {

	Artifact, AgileTool, analytics, ApiMetrics, AppPerformance, ARCHIVE, Build, blueGreen, BuildBreaker, BuildBreakerDB, CaAPM, CaApm, ChatOps, Cloud, CloudCustodian, Cmd, CMDB, CMIS, CodeQuality, CodeQualityNewCoverage, codereview, ComplianceTesting, ChangeFailure, DAST, DataValidation, DC_CA, Deployment, DBDeployment, DIT, DBProd, DBUat, DBSit, DBDit, E2E, Feature, FeatureToggle, Fortify, HPFortify, InfraPerformance, JiraUserStory, Jmeter, LibraryPolicy, Lisa, LoadRunner, LoadTest, Mail, Metrics, MQStatus, MTTR, OneHygieia, Portfolio_DB, PROD, Product, Project, PublicApi, RavenHealthCheck, Rraas, SCM, Scope, ScopeOwner, Security, SELENIUM, ServiceNow, SIT, SoapUI, SERIAL, StaticSecurityScan, SprintData, Tapsium, Test, TestData, TestReport, TestResult, UAT, Vast, VCOP, VCOP_CA, VDeployment, Virtual, MetricsProcessor, HygieiaLite, WLMonitor, Reports, DeployFrequency, DevopscupMTTR, DevopscupSecurity, Incident, MTBF, LeadTime, DevOpsCup, Vonkinator, StashUnlimited, EXTERNALMONITOR, DevopscupLeadTime, LOCProcessor, CLOUDPROCESSOR, CENTRALDATAPROCESSOR, JenkinsProcessor, JenkinsPipeline;

	public static CollectorType fromString(String value) {
		for (CollectorType collectorType : values()) {
			if (collectorType.toString().equalsIgnoreCase(value)) {
				return collectorType;
			}
		}
		throw new IllegalArgumentException(value + " is not a CollectorType");
	}
}
