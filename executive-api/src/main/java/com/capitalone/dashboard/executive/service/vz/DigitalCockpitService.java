package com.capitalone.dashboard.executive.service.vz;

import java.util.List;

import com.capitalone.dashboard.exec.model.vz.DigitalCockpitResponse;

public interface DigitalCockpitService {

	List<DigitalCockpitResponse> getFilteredCFMetricDetails();

	List<DigitalCockpitResponse> getFilteredCodeCommitMetricDetails();

	List<DigitalCockpitResponse> getFilteredDeploymentCAMetricDetails();

	List<DigitalCockpitResponse> getFilteredQualityMetricDetails();

	List<DigitalCockpitResponse> getFilteredVelocityMetricDetails();
}
