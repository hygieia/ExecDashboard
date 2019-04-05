package com.capitalone.dashboard.exec.model.vz;

import java.util.List;

/**
 * Model ThroughPutModel
 * @param 
 * @return 
 */
public class ThroughPutModel {
	private String dashboardName;
	private List<ComputedPipelineMetrics> computedPipelineMetrics;
	
	public String getDashboardName() {
		return dashboardName;
	}
	public void setDashboardName(String dashboardName) {
		this.dashboardName = dashboardName;
	}
	
	public List<ComputedPipelineMetrics> getComputedPipelineMetrics() {
		return computedPipelineMetrics;
	}
	public void setComputedPipelineMetrics(List<ComputedPipelineMetrics> computedPipelineMetrics) {
		this.computedPipelineMetrics = computedPipelineMetrics;
	}
}
