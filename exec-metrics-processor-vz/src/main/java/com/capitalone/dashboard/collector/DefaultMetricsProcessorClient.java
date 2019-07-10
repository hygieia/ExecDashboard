package com.capitalone.dashboard.collector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.custom.BuildAnalysis;
import com.capitalone.dashboard.custom.DeployAnalysis;
import com.capitalone.dashboard.custom.DevopscupAnalysis;
import com.capitalone.dashboard.custom.CloudAnalysis;
import com.capitalone.dashboard.custom.ExecutiveViewAnalysis;
import com.capitalone.dashboard.custom.ProductionIncidentsAnalysis;
import com.capitalone.dashboard.custom.QualityAnalysis;
import com.capitalone.dashboard.custom.SayDoRatioAnalysis;
import com.capitalone.dashboard.custom.SecurityAnalysis;
import com.capitalone.dashboard.custom.StashAnalysis;
import com.capitalone.dashboard.custom.TestAnalysis;
import com.capitalone.dashboard.custom.ThroughPutAnalysis;
import com.capitalone.dashboard.custom.TotalValueAnalysis;
import com.capitalone.dashboard.custom.VastAnalysis;
import com.capitalone.dashboard.custom.VelocityAnalysis;
import com.capitalone.dashboard.custom.WipAnalysis;

/**
 * DefaultMetricsProcessorClient implements MetricsProcessorCollectorClient
 * 
 * @param ...
 * @return
 */
@Component
@SuppressWarnings("PMD")
public class DefaultMetricsProcessorClient implements MetricsProcessorCollectorClient {

	private final VastAnalysis vastAnalysis;
	private final VelocityAnalysis velocityAnalysis;
	private final ThroughPutAnalysis throughPutAnalysis;
	private final BuildAnalysis buildAnalysis;
	private final DeployAnalysis deployAnalysis;
	private final SecurityAnalysis securityAnalysis;
	private final ProductionIncidentsAnalysis productionIncidentsAnalysis;
	private final QualityAnalysis qualityAnalysis;
	private final WipAnalysis wipAnalysis;
	private final StashAnalysis stashAnalysis;
	private final ExecutiveViewAnalysis executiveViewAnalysis;
	private final CloudAnalysis cloudAnalysis;
	private final TotalValueAnalysis totalValueAnalysis;
	private final SayDoRatioAnalysis sayDoRatioAnalysis;
	private final TestAnalysis testAnalysis;
	private final DevopscupAnalysis devopscupAnalysis;
	private static final String COMMENT = "==================================================";
	private static final String TRUE = "true";
	private static final Logger LOG = LoggerFactory.getLogger(DefaultMetricsProcessorClient.class);

	/**
	 * 
	 * @param vastAnalysis
	 * @param velocityAnalysis
	 * @param throughPutAnalysis
	 * @param securityAnalysis
	 * @param productionIncidentsAnalysis
	 * @param qualityAnalysis
	 * @param wipAnalysis
	 * @param stashAnaylsis
	 * @param executiveViewAnalysis
	 * @param cloudAnalysis
	 * @param totalValueAnalysis
	 * @param buildAnalysis
	 * @param deployAnalysis
	 * @param sayDoRatioAnalysis
	 * @param testAnalysis
	 * @param devopscupAnalysis
	 */
	@Autowired
	public DefaultMetricsProcessorClient(VastAnalysis vastAnalysis, VelocityAnalysis velocityAnalysis,
			ThroughPutAnalysis throughPutAnalysis, SecurityAnalysis securityAnalysis,
			ProductionIncidentsAnalysis productionIncidentsAnalysis, QualityAnalysis qualityAnalysis,
			WipAnalysis wipAnalysis, StashAnalysis stashAnaylsis, ExecutiveViewAnalysis executiveViewAnalysis,
			CloudAnalysis cloudAnalysis, TotalValueAnalysis totalValueAnalysis, BuildAnalysis buildAnalysis,
			DeployAnalysis deployAnalysis, SayDoRatioAnalysis sayDoRatioAnalysis, TestAnalysis testAnalysis,
			DevopscupAnalysis devopscupAnalysis) {
		this.vastAnalysis = vastAnalysis;
		this.velocityAnalysis = velocityAnalysis;
		this.throughPutAnalysis = throughPutAnalysis;
		this.securityAnalysis = securityAnalysis;
		this.productionIncidentsAnalysis = productionIncidentsAnalysis;
		this.qualityAnalysis = qualityAnalysis;
		this.wipAnalysis = wipAnalysis;
		this.stashAnalysis = stashAnaylsis;
		this.executiveViewAnalysis = executiveViewAnalysis;
		this.buildAnalysis = buildAnalysis;
		this.deployAnalysis = deployAnalysis;
		this.cloudAnalysis = cloudAnalysis;
		this.totalValueAnalysis = totalValueAnalysis;
		this.sayDoRatioAnalysis = sayDoRatioAnalysis;
		this.testAnalysis = testAnalysis;
		this.devopscupAnalysis = devopscupAnalysis;
	}

	/**
	 * collectCommonMetrics
	 * 
	 * @return Boolean
	 */
	@Override
	public Boolean collectCommonMetrics() {
		try {
			LOG.info("Processing Common Details . . . . ");
			LOG.info(COMMENT);
			vastAnalysis.processCollectorStatus();
			LOG.info(COMMENT);
		} catch (Exception e) {
			LOG.info("Error inside DefaultMetricsProcessorClient file - collectCommonMetrics() : " + e);
		}
		return true;
	}

	/**
	 * processVastMetricsDetails
	 * 
	 * @return Boolean
	 */
	@Override
	public Boolean processVastMetricsDetails() {
		try {
			LOG.info("Processing Vast Details . . . . ");
			LOG.info(COMMENT);
			vastAnalysis.processVastMetricsDetails();
			vastAnalysis.processPortfolioResponse();
			vastAnalysis.processVastDirectReportees();
			LOG.info(COMMENT);
		} catch (Exception e) {
			LOG.info("Error inside DefaultMetricsProcessorClient file - processVastMetricsDetails() : " + e);
		}
		return true;
	}

	/**
	 * processThroughPutMetricsDetails
	 * 
	 * @return Boolean
	 */
	@Override
	public Boolean processThroughPutMetricsDetails() {
		try {
			LOG.info("Processing ThroughPut Details . . . . ");
			LOG.info(COMMENT);
			throughPutAnalysis.processExecutiveMetricsDetails();
			throughPutAnalysis.removeUnusedThroughPutDetails();
			throughPutAnalysis.processMetricsDetailResponse();
			throughPutAnalysis.processBuildingBlockMetrics();
			throughPutAnalysis.processExecutiveDetailsMetrics();
			throughPutAnalysis.processComponentDetailsMetrics();
			LOG.info(COMMENT);
		} catch (Exception e) {
			LOG.info("Error inside DefaultMetricsProcessorClient file - processThroughPutMetricsDetails() : " + e);
		}
		return true;
	}

	/**
	 * securityMetricsDetails
	 * 
	 * @return Boolean
	 */
	@Override
	public Boolean securityMetricsDetails(String collectDateWise) {
		try {
			LOG.info("Processing Security Details . . . . ");
			LOG.info(COMMENT);
			if (TRUE.equalsIgnoreCase(collectDateWise))
				securityAnalysis.processDatewiseDetails();

			securityAnalysis.processExecutiveMetricsDetails();
			securityAnalysis.processMetricsDetailResponse();
			securityAnalysis.processBuildingBlockMetrics();
			securityAnalysis.processExecutiveDetailsMetrics();
			securityAnalysis.processComponentDetailsMetrics();

			LOG.info(COMMENT);
		} catch (Exception e) {
			LOG.info("Error inside DefaultMetricsProcessorClient file - securityMetricsDetails() : " + e);
		}
		return true;
	}

	/**
	 * processVelocityMetricsDetails
	 * 
	 * @return Boolean
	 */
	@Override
	public Boolean processVelocityMetricsDetails() {

		try {
			LOG.info(COMMENT);
			LOG.info("Processing Velocity Details . . . . ");
			velocityAnalysis.processExecutiveMetricsDetails();
			velocityAnalysis.removeUnusedVelocityDetails();
			velocityAnalysis.processMetricsDetailResponse();
			velocityAnalysis.processBuildingBlockMetrics();
			velocityAnalysis.processExecutiveDetailsMetrics();
			velocityAnalysis.processComponentDetailsMetrics();
			LOG.info(COMMENT);
		} catch (Exception e) {
			LOG.info("Error inside DefaultMetricsProcessorClient file - processVelocityMetricsDetails() : " + e);
		}

		return true;
	}

	/**
	 * processQualityMetricsDetails
	 * 
	 * @return Boolean
	 */
	@Override
	public Boolean processQualityMetricsDetails() {
		try {
			LOG.info(COMMENT);
			LOG.info("Processing Quality Details . . . . ");
			qualityAnalysis.processExecutiveMetricsDetails();
			qualityAnalysis.removeUnusedQualityDetails();
			qualityAnalysis.processMetricsDetailResponse();
			qualityAnalysis.processBuildingBlockMetrics();
			qualityAnalysis.processExecutiveDetailsMetrics();
			qualityAnalysis.processComponentDetailsMetrics();
			LOG.info(COMMENT);
		} catch (Exception e) {
			LOG.info("Error inside DefaultMetricsProcessorClient file - processQualityMetricsDetails() : " + e);
		}
		return true;
	}

	/**
	 * processProductionIncidentsMetricsDetails
	 * 
	 * @return Boolean
	 */
	@Override
	public Boolean processProductionIncidentsMetricsDetails(String collectDateWise) {
		try {
			LOG.info("Processing ProductionIncidents Details . . . . ");
			LOG.info(COMMENT);
			if (TRUE.equalsIgnoreCase(collectDateWise))
				productionIncidentsAnalysis.processDateWiseTrend();
			productionIncidentsAnalysis.processExecutiveMetricsDetails();
			productionIncidentsAnalysis.removeUnusedProductionIncidentsDetails();
			productionIncidentsAnalysis.processMetricsDetailResponse();
			productionIncidentsAnalysis.processBuildingBlockMetrics();
			productionIncidentsAnalysis.processExecutiveDetailsMetrics();
			productionIncidentsAnalysis.processComponentDetailsMetrics();
			LOG.info(COMMENT);
		} catch (Exception e) {
			LOG.info("Error inside DefaultMetricsProcessorClient file - processProductionIncidentsMetricsDetails() : "
					+ e);
		}
		return true;
	}

	/**
	 * processWipMetricDetails
	 * 
	 * @return Boolean
	 */
	@Override
	public Boolean processWipMetricDetails() {
		try {
			LOG.info("Processing WIP Details . . . . ");
			LOG.info(COMMENT);
			wipAnalysis.processExecutiveMetricsDetails();
			wipAnalysis.removeUnusedWipDetails();
			wipAnalysis.processMetricsDetailResponse();
			wipAnalysis.processBuildingBlockMetrics();
			wipAnalysis.processExecutiveDetailsMetrics();
			wipAnalysis.processComponentDetailsMetrics();
			LOG.info(COMMENT);
		} catch (Exception e) {
			LOG.info("Error inside DefaultMetricsProcessorClient file - processWIPMetricsDetails() : " + e);
		}

		return true;
	}

	/**
	 * securityMetricsDetails
	 * 
	 * @return Boolean
	 */
	@Override
	public Boolean processStashMetricDetails(String collectDateWise) {
		try {
			LOG.info("Processing Stash Details . . . . ");
			LOG.info(COMMENT);
			if (TRUE.equalsIgnoreCase(collectDateWise))
				stashAnalysis.processDateWiseTrend();
			stashAnalysis.processExecutiveMetricsDetails();
			stashAnalysis.removeUnusedStashDetails();
			stashAnalysis.processMetricsDetailResponse();
			stashAnalysis.processBuildingBlockMetrics();
			stashAnalysis.processExecutiveDetailsMetrics();
			stashAnalysis.processComponentDetailsMetrics();
			LOG.info(COMMENT);
		} catch (Exception e) {
			LOG.info("Error inside DefaultMetricsProcessorClient file - stashMetricsDetails() : " + e);
		}
		return true;
	}

	/**
	 * processCloudMetricsDetails
	 * 
	 * @return Boolean
	 */
	@Override
	public Boolean processCloudMetricsDetails(String collectDateWise) {
		try {
			LOG.info(COMMENT);
			LOG.info("Processing Cloud Details . . . . ");
			if (TRUE.equalsIgnoreCase(collectDateWise))
				cloudAnalysis.processDateWiseTrend();
			cloudAnalysis.processExecutiveMetricsDetails();
			cloudAnalysis.processMetricsDetailResponse();
			cloudAnalysis.processBuildingBlockMetrics();
			cloudAnalysis.processExecutiveDetailsMetrics();
			cloudAnalysis.processComponentDetailsMetrics();
			LOG.info(COMMENT);
		} catch (Exception e) {
			LOG.info("Error inside DefaultMetricsProcessorClient file - processCloudMetricsDetails() : " + e);
		}
		return true;
	}

	/**
	 * processTotalValueMetricsDetails
	 * 
	 * @return Boolean
	 */
	@Override
	public Boolean processTotalValueMetricsDetails() {
		try {
			LOG.info(COMMENT);
			LOG.info("Processing Total Value Details . . . . ");
			totalValueAnalysis.processMetricsDetailResponse();
			totalValueAnalysis.processBuildingBlockMetrics();
			totalValueAnalysis.processExecutiveDetailsMetrics();
			totalValueAnalysis.processComponentDetailsMetrics();
			LOG.info(COMMENT);
		} catch (Exception e) {
			LOG.info("Error inside DefaultMetricsProcessorClient file - processTotalValueMetricsDetails() : " + e);
		}
		return true;
	}

	/**
	 * processExecutiveViewMetrics
	 * 
	 * @return Boolean
	 */
	@Override
	public Boolean processExecutiveViewMetrics() {

		try {
			LOG.info("Processing Executive Metrics View . . . . ");
			LOG.info(COMMENT);
			executiveViewAnalysis.processExecutiveDetailsMetrics();
			LOG.info(COMMENT);
		} catch (Exception e) {
			LOG.info("Error inside DefaultMetricsProcessorClient file - Processing Executive Metrics View() : " + e);
		}

		return true;
	}

	@Override
	public Boolean processBuildMetricDetails(String collectDateWise) {
		try {
			LOG.info("Processing Build Details . . . . ");
			LOG.info(COMMENT);
			if (TRUE.equalsIgnoreCase(collectDateWise))
				buildAnalysis.processDateWiseTrend();
			buildAnalysis.processExecutiveMetricsDetails();
			buildAnalysis.removeUnusedBuildDetails();
			buildAnalysis.processMetricsDetailResponse();
			buildAnalysis.processBuildingBlockMetrics();
			buildAnalysis.processExecutiveDetailsMetrics();
			buildAnalysis.processComponentDetailsMetrics();

			LOG.info(COMMENT);
		} catch (Exception e) {
			LOG.info("Error inside DefaultMetricsProcessorClient file - processBuildMetricsDetails() : " + e);
		}
		return true;
	}

	@Override
	public Boolean processDeployMetricDetails(String collectDateWise) {
		try {
			LOG.info("Processing Deploy Details . . . . ");
			LOG.info(COMMENT);
			if (TRUE.equalsIgnoreCase(collectDateWise))
				deployAnalysis.processDateWiseTrend();
			deployAnalysis.processExecutiveMetricsDetails();
			deployAnalysis.removeUnusedDeployDetails();
			deployAnalysis.processMetricsDetailResponse();
			deployAnalysis.processBuildingBlockMetrics();
			deployAnalysis.processExecutiveDetailsMetrics();
			deployAnalysis.processComponentDetailsMetrics();
			LOG.info(COMMENT);
		} catch (Exception e) {
			LOG.info("Error inside DefaultMetricsProcessorClient file - processDeployMetricsDetails() : " + e);
		}
		return true;
	}

	@Override
	public Boolean processSayDoRatioMetricsDetails(String collectDateWise) {
		try {
			LOG.info("Processing Say/Do Ratio Details . . . . ");
			LOG.info(COMMENT);
			if (TRUE.equalsIgnoreCase(collectDateWise))
				sayDoRatioAnalysis.processDateWiseTrend();
			sayDoRatioAnalysis.processExecutiveMetricsDetails();
			sayDoRatioAnalysis.removeUnusedSayDoDetails();
			sayDoRatioAnalysis.processMetricsDetailResponse();
			sayDoRatioAnalysis.processBuildingBlockMetrics();
			sayDoRatioAnalysis.processExecutiveDetailsMetrics();
			sayDoRatioAnalysis.processComponentDetailsMetrics();
			LOG.info(COMMENT);
		} catch (Exception e) {
			LOG.info("Error inside DefaultMetricsProcessorClient file - processSayDoRatioMetricsDetails() : " + e);
		}
		return true;
	}

	@Override
	public Boolean processTestMetricsDetails(String collectDateWise) {
		try {
			LOG.info("Processing Test Details . . . . ");
			LOG.info(COMMENT);
			if (TRUE.equalsIgnoreCase(collectDateWise))
				testAnalysis.processDateWiseTrend();
			testAnalysis.processExecutiveMetricsDetails();
			testAnalysis.removeUnusedTestDetails();
			testAnalysis.processMetricsDetailResponse();
			testAnalysis.processBuildingBlockMetrics();
			testAnalysis.processExecutiveDetailsMetrics();
			testAnalysis.processComponentDetailsMetrics();
			LOG.info(COMMENT);
		} catch (Exception e) {
			LOG.info("Error inside DefaultMetricsProcessorClient file - processTestMetricsDetails() : " + e);
		}
		return true;
	}

	@Override
	public Boolean collectCollectorMetrics() {
		try {
			LOG.info("Processing Collector Updated Details . . . . ");
			LOG.info(COMMENT);
			vastAnalysis.processCollectorUpdatedTimestamp();
			LOG.info(COMMENT);
		} catch (Exception e) {
			LOG.info("Error inside DefaultMetricsProcessorClient file - collectCollectorMetrics() : " + e);
		}
		return true;
	}

	@Override
	public Boolean processDevopscupMetricsDetails() {

		LOG.info("Processing DevopsCup Details . . . . ");
		LOG.info(COMMENT);
		devopscupAnalysis.setDevopscupRoundDetails();
		devopscupAnalysis.processExecutiveMetricsDetails();
		devopscupAnalysis.processMetricsDetailResponse();
		devopscupAnalysis.processExecutiveDetailsMetrics();
		devopscupAnalysis.processBuildingBlockMetrics();
		devopscupAnalysis.processComponentDetailsMetrics();
		LOG.info(COMMENT);
		return true;
	}

	@Override
	public Boolean updateCollectorMetrics(String metricsName, Long startTime) {
		try {
			LOG.info("Processing Collector Updated Details . . . . ");
			LOG.info(COMMENT);
			vastAnalysis.updateCollectorUpdatedTimestamp(metricsName, startTime);
			LOG.info(COMMENT);
		} catch (Exception e) {
			LOG.info("Error in DefaultMetricsProcessorClient file - collectCollectorMetrics() : " + e);
		}
		return true;
	}

	@Override
	public Boolean copyVastDetails() {
		vastAnalysis.copyVastDetails();
		return true;
	}

	/**
	 * processTotalValueMetricsDetails()
	 * 
	 * @param metricsName
	 * @param timestamp
	 * @param status
	 * 
	 * @return Boolean
	 */
	@Override
	public Boolean updateCollectorStatus(String metricsName, Long timestamp, boolean status) {
		try {
			LOG.info("Processing Collector Status Details . . . . ");
			LOG.info(COMMENT);
			vastAnalysis.updateCollectorStatus(metricsName, timestamp, status);
			LOG.info(COMMENT);
		} catch (Exception e) {
			LOG.info("Error inside DefaultMetricsProcessorClient file - updateCollectorStatus() : " + e);
		}
		return true;
	}

}
