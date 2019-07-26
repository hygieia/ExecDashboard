package com.capitalone.dashboard.executive.service;

import com.capitalone.dashboard.exec.model.DigitalCockpitResponse;

import java.util.List;

public interface DigitalCockpitService {

	List<DigitalCockpitResponse> getFilteredCFMetricDetails();

	List<DigitalCockpitResponse> getFilteredCodeCommitMetricDetails();

	List<DigitalCockpitResponse> getFilteredDeploymentCAMetricDetails();

	List<DigitalCockpitResponse> getFilteredQualityMetricDetails();

	List<DigitalCockpitResponse> getFilteredVelocityMetricDetails();
}
