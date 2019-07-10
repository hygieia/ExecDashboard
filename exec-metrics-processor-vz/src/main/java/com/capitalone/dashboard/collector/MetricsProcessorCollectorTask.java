package com.capitalone.dashboard.collector;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.exec.collector.vz.CollectorTask;
import com.capitalone.dashboard.exec.model.vz.Collector;
import com.capitalone.dashboard.exec.model.vz.CollectorType;
import com.capitalone.dashboard.exec.repository.vz.BaseCollectorRepository;

/**
 * MetricsProcessorCollectorTask extends CollectorTask<Collector>
 * 
 * @param ...
 * @return
 */
@Component
@SuppressWarnings("PMD")
public class MetricsProcessorCollectorTask extends CollectorTask<Collector> {

	private final BaseCollectorRepository<Collector> collectorRepository;
	private final MetricsProcessorSettings metricsProcessorSettings;
	private final MetricsProcessorCollectorClient metricsProcessorCollectorClient;
	private static final Logger LOG = LoggerFactory.getLogger(MetricsProcessorCollectorTask.class);

	/**
	 * MetricsProcessorCollectorTask
	 * 
	 * @param taskScheduler
	 *            ...
	 * @param collectorRepository
	 * @param metricsProcessorSettings
	 * @param metricsProcessorCollectorClient
	 * @return
	 */

	@Autowired
	public MetricsProcessorCollectorTask(TaskScheduler taskScheduler,
			BaseCollectorRepository<Collector> collectorRepository, MetricsProcessorSettings metricsProcessorSettings,
			MetricsProcessorCollectorClient metricsProcessorCollectorClient) {
		super(taskScheduler, "MetricsProcessor");
		this.collectorRepository = collectorRepository;
		this.metricsProcessorSettings = metricsProcessorSettings;
		this.metricsProcessorCollectorClient = metricsProcessorCollectorClient;
	}

	/**
	 * getCollector
	 * 
	 * @return Collector
	 */

	@Override
	public Collector getCollector() {
		Collector protoType = new Collector();
		protoType.setName("MetricsProcessor");
		protoType.setCollectorType(CollectorType.MetricsProcessor);
		protoType.setOnline(true);
		protoType.setEnabled(true);
		return protoType;
	}

	/**
	 * getCollectorRepository
	 * 
	 * @return BaseCollectorRepository<Collector>
	 */

	@Override
	public BaseCollectorRepository<Collector> getCollectorRepository() {
		return collectorRepository;
	}

	/**
	 * getCron
	 * 
	 * @return String
	 */

	@Override
	public String getCron() {
		return metricsProcessorSettings.getCron();
	}

	/**
	 * collect
	 * 
	 * @param collector
	 */

	@Override
	public void collect(Collector collector) {
		String metricsName = "";
		try {

			logBanner("Starting... .. ..");

			metricsProcessorCollectorClient.collectCommonMetrics();

			if (metricsProcessorSettings.getCopyVast())
				metricsProcessorCollectorClient.copyVastDetails();

			List<Integer> metricsOrder = metricsProcessorSettings.getMetrics();
			if (metricsOrder != null && !metricsOrder.isEmpty()) {
				for (Integer order : metricsOrder) {
					switch (order) {
					case 1:
						metricsName = "Security";
						metricsProcessorCollectorClient.updateCollectorStatus(metricsName, getTimeStampUTC(), true);
						metricsProcessorCollectorClient
								.securityMetricsDetails(metricsProcessorSettings.getCollectDateWise());
						metricsProcessorCollectorClient.updateCollectorStatus(metricsName, getTimeStampUTC(), false);
						break;
					case 2:
						metricsName = "Through Put";
						metricsProcessorCollectorClient.updateCollectorStatus(metricsName, getTimeStampUTC(), true);
						metricsProcessorCollectorClient.processThroughPutMetricsDetails();
						metricsProcessorCollectorClient.updateCollectorStatus(metricsName, getTimeStampUTC(), false);
						break;
					case 3:
						metricsName = "Quality";
						metricsProcessorCollectorClient.updateCollectorStatus(metricsName, getTimeStampUTC(), true);
						metricsProcessorCollectorClient.processQualityMetricsDetails();
						metricsProcessorCollectorClient.updateCollectorStatus(metricsName, getTimeStampUTC(), false);
						break;
					case 4:
						metricsName = "Production Incidents";
						metricsProcessorCollectorClient.updateCollectorStatus(metricsName, getTimeStampUTC(), true);
						metricsProcessorCollectorClient.processProductionIncidentsMetricsDetails(
								metricsProcessorSettings.getCollectDateWise());
						metricsProcessorCollectorClient.updateCollectorStatus(metricsName, getTimeStampUTC(), false);
						break;
					case 5:
						metricsName = "Velocity";
						metricsProcessorCollectorClient.updateCollectorStatus(metricsName, getTimeStampUTC(), true);
						metricsProcessorCollectorClient.processVelocityMetricsDetails();
						metricsProcessorCollectorClient.updateCollectorStatus(metricsName, getTimeStampUTC(), false);
						break;
					case 6:
						metricsName = "WIP";
						metricsProcessorCollectorClient.updateCollectorStatus(metricsName, getTimeStampUTC(), true);
						metricsProcessorCollectorClient.processWipMetricDetails();
						metricsProcessorCollectorClient.updateCollectorStatus(metricsName, getTimeStampUTC(), false);
						break;
					case 7:
						metricsName = "Stash";
						metricsProcessorCollectorClient.updateCollectorStatus(metricsName, getTimeStampUTC(), true);
						metricsProcessorCollectorClient
								.processStashMetricDetails(metricsProcessorSettings.getCollectDateWise());
						metricsProcessorCollectorClient.updateCollectorStatus(metricsName, getTimeStampUTC(), false);
						break;
					case 8:
						metricsName = "Cloud";
						metricsProcessorCollectorClient.updateCollectorStatus(metricsName, getTimeStampUTC(), true);
						metricsProcessorCollectorClient
								.processCloudMetricsDetails(metricsProcessorSettings.getCollectDateWise());
						metricsProcessorCollectorClient.updateCollectorStatus(metricsName, getTimeStampUTC(), false);
						break;
					case 9:
						metricsName = "Total Stories";
						metricsProcessorCollectorClient.updateCollectorStatus(metricsName, getTimeStampUTC(), true);
						metricsProcessorCollectorClient.processTotalValueMetricsDetails();
						metricsProcessorCollectorClient.updateCollectorStatus(metricsName, getTimeStampUTC(), false);
						break;
					case 10:
						metricsName = "Build";
						metricsProcessorCollectorClient.updateCollectorStatus(metricsName, getTimeStampUTC(), true);
						metricsProcessorCollectorClient
								.processBuildMetricDetails(metricsProcessorSettings.getCollectDateWise());
						metricsProcessorCollectorClient.updateCollectorStatus(metricsName, getTimeStampUTC(), false);
						break;
					case 11:
						metricsName = "Deploy";
						metricsProcessorCollectorClient.updateCollectorStatus(metricsName, getTimeStampUTC(), true);
						metricsProcessorCollectorClient
								.processDeployMetricDetails(metricsProcessorSettings.getCollectDateWise());
						metricsProcessorCollectorClient.updateCollectorStatus(metricsName, getTimeStampUTC(), false);
						break;
					case 12:
						metricsName = "Say Do";
						metricsProcessorCollectorClient.updateCollectorStatus(metricsName, getTimeStampUTC(), true);
						metricsProcessorCollectorClient
								.processSayDoRatioMetricsDetails(metricsProcessorSettings.getCollectDateWise());
						metricsProcessorCollectorClient.updateCollectorStatus(metricsName, getTimeStampUTC(), false);
						break;
					case 13:
						metricsName = "DevOps Cup";
						metricsProcessorCollectorClient.updateCollectorStatus(metricsName, getTimeStampUTC(), true);
						metricsProcessorCollectorClient.processDevopscupMetricsDetails();
						metricsProcessorCollectorClient.updateCollectorStatus(metricsName, getTimeStampUTC(), false);
						break;
					case 14:
						metricsName = "Collector Up Status";
						metricsProcessorCollectorClient.updateCollectorStatus(metricsName, getTimeStampUTC(), true);
						metricsProcessorCollectorClient.collectCollectorMetrics();
						metricsProcessorCollectorClient.updateCollectorStatus(metricsName, getTimeStampUTC(), false);
						break;
					case 15:
						metricsName = "Test";
						metricsProcessorCollectorClient.updateCollectorStatus(metricsName, getTimeStampUTC(), true);
						metricsProcessorCollectorClient
								.processTestMetricsDetails(metricsProcessorSettings.getCollectDateWise());
						metricsProcessorCollectorClient.updateCollectorStatus(metricsName, getTimeStampUTC(), false);
						break;
					case 16:
						metricsName = "Vast";
						metricsProcessorCollectorClient.updateCollectorStatus(metricsName, getTimeStampUTC(), true);
						metricsProcessorCollectorClient.processVastMetricsDetails();
						metricsProcessorCollectorClient.updateCollectorStatus(metricsName, getTimeStampUTC(), false);
						break;
					default:
						LOG.info("Unexpected Parameter : " + order);
					}
				}
				metricsProcessorCollectorClient.processExecutiveViewMetrics();
			}
			logBanner("Completed...");
		} catch (Exception e) {
			LOG.info("Error in collect() (MetricsProcessorCollectorTask Class) : " + e);
		}
	}

	/**
	 * 
	 * @return Long timeStamp
	 */
	private static Long getTimeStampUTC() {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		return calendar.getTimeInMillis();
	}

}
