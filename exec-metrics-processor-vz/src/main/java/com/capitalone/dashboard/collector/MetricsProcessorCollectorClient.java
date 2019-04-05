package com.capitalone.dashboard.collector;

/**
 * Interface to be implemented by DefaultMetricsClient
 * 
 * @param ...
 * @return Boolean
 */
public interface MetricsProcessorCollectorClient {

	/**
	 * Abstract Method processVastMetricsDetails()
	 * 
	 * @param ...
	 * @return Boolean
	 */
	Boolean processVastMetricsDetails();

	/**
	 * Abstract Method processVelocityMetricsDetails()
	 * 
	 * @param ...
	 * @return Boolean
	 */
	Boolean processVelocityMetricsDetails();

	/**
	 * Abstract Method processThroughPutMetricsDetails()
	 * 
	 * @param ...
	 * @return Boolean
	 */
	Boolean processThroughPutMetricsDetails();

	/**
	 * Abstract Method securityMetricsDetails()
	 * 
	 * @param collectDatewise
	 * @param ...
	 * @return Boolean
	 */
	Boolean securityMetricsDetails(String collectDatewise);

	/**
	 * Abstract Method processProductionIncidentsMetricsDetails()
	 * 
	 * @param collectDatewise
	 * @return Boolean
	 */
	Boolean processProductionIncidentsMetricsDetails(String collectDatewise);

	/**
	 * Abstract Method processQualityMetricsDetails()
	 * 
	 * @param ...
	 * @return Boolean
	 */
	Boolean processQualityMetricsDetails();

	/**
	 * Abstract Method processWipMetricsDetails()
	 * 
	 * @param ...
	 * @return Boolean
	 */
	Boolean processWipMetricDetails();

	/**
	 * Abstract Method processStashMetricDetails()
	 * 
	 * @param collectDatewise
	 * @return Boolean
	 */
	Boolean processStashMetricDetails(String collectDatewise);

	/**
	 * Abstract Method processExecutiveViewMetrics()
	 * 
	 * @param ...
	 * @return Boolean
	 */
	Boolean processExecutiveViewMetrics();

	/**
	 * Abstract Method processBuildMetricDetails()
	 * 
	 * @param collectDatewise
	 * @return
	 */
	Boolean processBuildMetricDetails(String collectDatewise);

	/**
	 * Abstract Method processDeployMetricDetails()
	 * 
	 * @param collectDatewise
	 * @return
	 */
	Boolean processDeployMetricDetails(String collectDatewise);

	/**
	 * Abstract Method collectCommonMetrics()
	 * 
	 * @param ...
	 * @return Boolean
	 */
	Boolean collectCommonMetrics();

	/**
	 * Abstract Method processCloudMetricsDetails()
	 * 
	 * @param collectDatewise
	 * @return Boolean
	 */
	Boolean processCloudMetricsDetails(String collectDatewise);

	/**
	 * Abstract Method processTotalValueMetricsDetails()
	 * 
	 * @param ...
	 * @return Boolean
	 */
	Boolean processTotalValueMetricsDetails();

	/**
	 * 
	 * @param collectDateWise
	 * @return
	 */
	Boolean processSayDoRatioMetricsDetails(String collectDateWise);

	/**
	 * 
	 * @param collectDateWise
	 * @return
	 */
	Boolean processTestMetricsDetails(String collectDateWise);

	/**
	 * Abstract Method collectCollectorMetrics()
	 * 
	 * @param metricsName
	 * @return Boolean
	 */
	Boolean collectCollectorMetrics();

	/**
	 * @param ...
	 * @return Boolean
	 */
	Boolean processDevopscupMetricsDetails();

	/**
	 * Abstract Method collectCollectorMetrics()
	 * 
	 * @param metricsName
	 * @param startTime
	 * @return Boolean
	 */
	Boolean updateCollectorMetrics(String metricsName, Long startTime);

	/**
	 * @param ...
	 * @return Boolean
	 */
	Boolean copyVastDetails();

	/**
	 * Abstract Method updateCollectorStatus()
	 * 
	 * @param metricsName
	 * @param timestamp
	 * @param status
	 * @return
	 */
	Boolean updateCollectorStatus(String metricsName, Long timestamp, boolean status);

}
