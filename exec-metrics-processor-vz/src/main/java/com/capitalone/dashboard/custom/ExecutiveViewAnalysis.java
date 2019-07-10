package com.capitalone.dashboard.custom;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.exec.model.MetricLevel;
import com.capitalone.dashboard.exec.model.MetricSummary;
import com.capitalone.dashboard.exec.model.vz.BuildingBlocks;
import com.capitalone.dashboard.exec.model.vz.ExecutiveSummaryList;
import com.capitalone.dashboard.exec.model.vz.MetricsDetail;
import com.capitalone.dashboard.exec.model.vz.PortfolioResponse;
import com.capitalone.dashboard.exec.repository.vz.BuildingBlocksRepository;
import com.capitalone.dashboard.exec.repository.vz.ExecutiveSummaryListRepository;
import com.capitalone.dashboard.exec.repository.vz.MetricsDetailRepository;
import com.capitalone.dashboard.exec.repository.vz.PortfolioResponseRepository;

/**
 * ExecutiveViewAnalysis
 * 
 * @param
 * @return
 * @author Hari
 */
@Component
@SuppressWarnings("PMD")
public class ExecutiveViewAnalysis {

	private final ExecutiveSummaryListRepository executiveSummaryListRepository;
	private final BuildingBlocksRepository buildingBlocksRepository;
	private final MetricsDetailRepository metricsDetailRepository;
	private final PortfolioResponseRepository portfolioResponseRepository;

	private static final Logger LOG = LoggerFactory.getLogger(ExecutiveViewAnalysis.class);

	/**
	 * ExecutiveViewAnalysis
	 * 
	 * @param executiveSummaryListRepository
	 * @param metricsDetailRepository
	 * @param executiveHierarchyRepository
	 * @param buildingBlocksRepository
	 * @return
	 */
	@Autowired
	public ExecutiveViewAnalysis(ExecutiveSummaryListRepository executiveSummaryListRepository,
			MetricsDetailRepository metricsDetailRepository, BuildingBlocksRepository buildingBlocksRepository,
			PortfolioResponseRepository portfolioResponseRepository) {
		this.executiveSummaryListRepository = executiveSummaryListRepository;
		this.metricsDetailRepository = metricsDetailRepository;
		this.buildingBlocksRepository = buildingBlocksRepository;
		this.portfolioResponseRepository = portfolioResponseRepository;
	}

	/**
	 * processExecutiveDetailsMetrics
	 * 
	 * @return Boolean
	 */
	public Boolean processExecutiveDetailsMetrics() {
		LOG.info("Processing Executive View Details : ExecutiveDetailsMetrics . . . . ");
		List<ExecutiveSummaryList> executiveSummaryLists = (List<ExecutiveSummaryList>) executiveSummaryListRepository
				.findAll();
		if (!executiveSummaryLists.isEmpty()) {
			for (ExecutiveSummaryList executiveSummaryList : executiveSummaryLists) {
				String eid = executiveSummaryList.getEid();
				List<MetricsDetail> metricPortfolioDetailResponseList = metricsDetailRepository.findByMetricLevelIdAndLevel(eid,
						MetricLevel.PORTFOLIO);
				if (metricPortfolioDetailResponseList != null && !metricPortfolioDetailResponseList.isEmpty()) {
					BuildingBlocks response = buildingBlocksRepository.findByMetricLevelIdAndMetricLevel(eid,
							MetricLevel.PORTFOLIO);
					PortfolioResponse portfolioResponse = portfolioResponseRepository.findByEid(eid);
					if (response == null)
						response = new BuildingBlocks();
					response.setMetricLevelId(eid);
					response.setName(executiveSummaryList.getFirstName() + ' ' + executiveSummaryList.getLastName());
					response.setCompleteness(100);
					response.setPoc(executiveSummaryList.getRole());
					if (portfolioResponse != null)
						response.setLob(portfolioResponse.getId().toString());
					List<MetricSummary> metrics = new ArrayList<>();
					for (MetricsDetail detailResponse : metricPortfolioDetailResponseList) {
						if (detailResponse.getSummary() != null) {
							metrics.add(detailResponse.getSummary());
						}
					}
					response.setMetricLevel(MetricLevel.PORTFOLIO);
					response.setMetrics(metrics);
					response.setTotalComponents(executiveSummaryList.getConfiguredApps());
					response.setTotalExpectedMetrics(executiveSummaryList.getTotalApps());
					buildingBlocksRepository.save(response);
				}
			}
		}
		LOG.info("Completed Executive View Details : ExecutiveDetailsMetrics . . . . ");
		return true;
	}

}
