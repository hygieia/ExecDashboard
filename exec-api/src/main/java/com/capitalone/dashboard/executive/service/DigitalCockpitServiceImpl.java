package com.capitalone.dashboard.executive.service;

import com.capitalone.dashboard.exec.model.DigitalCockpitResponse;
import com.capitalone.dashboard.exec.model.MetricCount;
import com.capitalone.dashboard.exec.model.MetricLevel;
import com.capitalone.dashboard.exec.model.MetricType;
import com.capitalone.dashboard.exec.model.MetricsDetail;
import com.capitalone.dashboard.exec.model.PortfolioResponse;
import com.capitalone.dashboard.exec.repository.DigitalCockpitRepository;
import com.capitalone.dashboard.exec.repository.PortfolioResponseRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@SuppressWarnings("PMD")
public class DigitalCockpitServiceImpl implements DigitalCockpitService {

	private DigitalCockpitRepository digitalCockpitRepository;
	private PortfolioResponseRepository portfolioResponseRepository;

	private static final Logger LOG = LoggerFactory.getLogger(DigitalCockpitServiceImpl.class);
	private static final String PRIORITY = "priority";

	/**
	 * DigitalCockpitServiceImpl
	 * 
	 * @param digitalCockpitRepository
	 * @param portfolioResponseRepository
	 */
	@Autowired
	public DigitalCockpitServiceImpl(DigitalCockpitRepository digitalCockpitRepository,
			PortfolioResponseRepository portfolioResponseRepository) {
		this.digitalCockpitRepository = digitalCockpitRepository;
		this.portfolioResponseRepository = portfolioResponseRepository;
	}

	@Override
	public List<DigitalCockpitResponse> getFilteredCFMetricDetails() {
		List<DigitalCockpitResponse> cfRespList = new ArrayList<>();
		try {
			List<MetricsDetail> metricPortfolioDetailResponse = digitalCockpitRepository
					.findByLevelAndType(MetricLevel.PORTFOLIO, MetricType.QUALITY);
			for (MetricsDetail detailResponse : metricPortfolioDetailResponse) {
				if (detailResponse != null) {
					String eid = detailResponse.getMetricLevelId();
					ObjectId executiveId = new ObjectId();
					PortfolioResponse portfolioResponse = portfolioResponseRepository.findByEid(eid);
					if (portfolioResponse != null)
						executiveId = portfolioResponse.getId();
					List<MetricCount> countsList = (detailResponse != null
							&& detailResponse.getSummary() != null && detailResponse.getSummary().getCounts() != null)
									? detailResponse.getSummary().getCounts() : new ArrayList<>();
					for (MetricCount cnt : countsList) {
						if (cnt.getLabel() != null && cnt.getLabel().get("type") != null
								&& ("changeFailureRate").equals(cnt.getLabel().get("type"))) {
							DigitalCockpitResponse cfRespValue = new DigitalCockpitResponse();
							cfRespValue.setId(executiveId);
							cfRespValue.setValue(cnt.getValue());
							cfRespValue.setEid(eid);
							cfRespList.add(cfRespValue);
						}
					}
				}
			}
		} catch (Exception e) {
			LOG.info("Exception occured : " + e);
		}
		return cfRespList;
	}

	@Override
	public List<DigitalCockpitResponse> getFilteredCodeCommitMetricDetails() {
		List<DigitalCockpitResponse> ccRespList = new ArrayList<>();
		try {
			List<MetricsDetail> metricPortfolioDetailResponse = digitalCockpitRepository
					.findByLevelAndType(MetricLevel.PORTFOLIO, MetricType.STASH);
			for (MetricsDetail detailResponse : metricPortfolioDetailResponse) {
				if (detailResponse != null) {
					String eid = detailResponse.getMetricLevelId();
					ObjectId executiveId = new ObjectId();
					PortfolioResponse portfolioResponse = portfolioResponseRepository.findByEid(eid);
					if (portfolioResponse != null)
						executiveId = portfolioResponse.getId();
					List<MetricCount> countsList = (detailResponse != null
							&& detailResponse.getSummary() != null && detailResponse.getSummary().getCounts() != null)
									? detailResponse.getSummary().getCounts() : new ArrayList<>();
					for (MetricCount cnt : countsList) {
						if (cnt.getLabel() != null && cnt.getLabel().get("type") != null
								&& ("uniqueContributors").equals(cnt.getLabel().get("type"))) {
							DigitalCockpitResponse ccRespValue = new DigitalCockpitResponse();
							ccRespValue.setId(executiveId);
							ccRespValue.setValue(cnt.getValue());
							ccRespValue.setEid(eid);
							ccRespList.add(ccRespValue);
						}
					}
				}
			}
		} catch (Exception e) {
			LOG.info("Exception occured : " + e);
		}
		return ccRespList;
	}

	@Override
	public List<DigitalCockpitResponse> getFilteredDeploymentCAMetricDetails() {
		List<DigitalCockpitResponse> dcRespList = new ArrayList<>();
		try {
			List<MetricsDetail> metricPortfolioDetailResponse = digitalCockpitRepository
					.findByLevelAndType(MetricLevel.PORTFOLIO, MetricType.PIPELINE_LEAD_TIME);
			for (MetricsDetail detailResponse : metricPortfolioDetailResponse) {
				if (detailResponse != null) {
					String eid = detailResponse.getMetricLevelId();
					ObjectId executiveId = new ObjectId();
					PortfolioResponse portfolioResponse = portfolioResponseRepository.findByEid(eid);
					if (portfolioResponse != null)
						executiveId = portfolioResponse.getId();
					List<MetricCount> countsList = (detailResponse != null
							&& detailResponse.getSummary() != null && detailResponse.getSummary().getCounts() != null)
									? detailResponse.getSummary().getCounts() : new ArrayList<>();
					for (MetricCount cnt : countsList) {
						if (cnt.getLabel() != null && cnt.getLabel().get("type") != null
								&& ("cadence").equals(cnt.getLabel().get("type"))) {
							DigitalCockpitResponse dcRespValue = new DigitalCockpitResponse();
							dcRespValue.setId(executiveId);
							dcRespValue.setValue(cnt.getValue());
							dcRespValue.setEid(eid);
							dcRespList.add(dcRespValue);
						}
					}
				}
			}
		} catch (Exception e) {
			LOG.info("Exception occured : " + e);
		}
		return dcRespList;
	}

	@Override
	public List<DigitalCockpitResponse> getFilteredQualityMetricDetails() {
		List<DigitalCockpitResponse> ccRespList = new ArrayList<>();
		try {
			List<MetricsDetail> metricPortfolioDetailResponse = digitalCockpitRepository
					.findByLevelAndType(MetricLevel.PORTFOLIO, MetricType.QUALITY);
			for (MetricsDetail detailResponse : metricPortfolioDetailResponse) {
				double totalValue = 0;
				if (detailResponse != null) {
					String eid = detailResponse.getMetricLevelId();
					ObjectId executiveId = new ObjectId();
					PortfolioResponse portfolioResponse = portfolioResponseRepository.findByEid(eid);
					if (portfolioResponse != null)
						executiveId = portfolioResponse.getId();
					List<MetricCount> countsList = (detailResponse != null
							&& detailResponse.getSummary() != null && detailResponse.getSummary().getCounts() != null)
									? detailResponse.getSummary().getCounts() : new ArrayList<>();
					for (MetricCount cnt : countsList) {
						if (cnt.getLabel() != null && cnt.getLabel().get(PRIORITY) != null) {
							if (("serviceNow").equals(cnt.getLabel().get(PRIORITY))
									|| ("cmis").equals(cnt.getLabel().get(PRIORITY))
									|| ("total").equals(cnt.getLabel().get(PRIORITY))) {
								double cmis = 0;
								double serviceNow = 0;
								double total = 0;
								if (("cmis").equals(cnt.getLabel().get(PRIORITY))) {
									cmis = cnt.getValue();
								} else if (("serviceNow").equals(cnt.getLabel().get(PRIORITY))) {
									serviceNow = cnt.getValue();
								} else if (("total").equals(cnt.getLabel().get(PRIORITY))) {
									total = cnt.getValue();
								}
								totalValue = cmis + serviceNow + total;
							}
						}
					}
					DigitalCockpitResponse ccRespValue = new DigitalCockpitResponse();
					ccRespValue.setId(executiveId);
					ccRespValue.setValue(totalValue);
					ccRespValue.setEid(eid);
					ccRespList.add(ccRespValue);
				}
			}
		} catch (Exception e) {
			LOG.info("Exception occured : " + e);
		}
		return ccRespList;
	}

	@Override
	public List<DigitalCockpitResponse> getFilteredVelocityMetricDetails() {
		List<DigitalCockpitResponse> ccRespList = new ArrayList<>();
		try {
			List<MetricsDetail> metricPortfolioDetailResponse = digitalCockpitRepository
					.findByLevelAndType(MetricLevel.PORTFOLIO, MetricType.VELOCITY);
			for (MetricsDetail detailResponse : metricPortfolioDetailResponse) {
				if (detailResponse != null) {
					String eid = detailResponse.getMetricLevelId();
					double timeTaken = 0;
					double storyPoints = 0;
					ObjectId executiveId = new ObjectId();
					PortfolioResponse portfolioResponse = portfolioResponseRepository.findByEid(eid);
					if (portfolioResponse != null)
						executiveId = portfolioResponse.getId();
					List<MetricCount> countsList = (detailResponse != null
							&& detailResponse.getSummary() != null && detailResponse.getSummary().getCounts() != null)
									? detailResponse.getSummary().getCounts() : new ArrayList<>();
					for (MetricCount cnt : countsList) {
						if (cnt.getLabel() != null && cnt.getLabel().get("type") != null
								&& ("Total Time").equals(cnt.getLabel().get("type"))) {
							timeTaken = cnt.getValue();
						}
						if (cnt.getLabel() != null && cnt.getLabel().get("type") != null
								&& ("Total Story Points").equals(cnt.getLabel().get("type"))) {
							storyPoints = cnt.getValue();
						}
					}
					if (timeTaken > 0 && storyPoints > 0) {
						DigitalCockpitResponse ccRespValue = new DigitalCockpitResponse();
						ccRespValue.setId(executiveId);
						ccRespValue.setValue(timeTaken / storyPoints);
						ccRespValue.setEid(eid);
						ccRespList.add(ccRespValue);
					}
				}
			}
		} catch (Exception e) {
			LOG.info("Exception occured : " + e);
		}
		return ccRespList;
	}
}
