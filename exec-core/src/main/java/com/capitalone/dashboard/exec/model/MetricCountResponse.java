package com.capitalone.dashboard.exec.model;

import java.util.List;
import java.util.Map;

public class MetricCountResponse {
	private Map<String, String> label;
	private Map<String, Map<String, Long>> labelLoc;
	private Map<String, List<String>> labelStash;
	private List<MTTR> labelMttr;
	private List<OperationalMetrics> operationMetrics;
	private double value;

	public Map<String, String> getLabel() {
		return label;
	}

	public void setLabel(Map<String, String> label) {
		this.label = label;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public Map<String, Map<String, Long>> getLabelLoc() {
		return labelLoc;
	}

	public void setLabelLoc(Map<String, Map<String, Long>> labelLoc) {
		this.labelLoc = labelLoc;
	}

	public Map<String, List<String>> getLabelStash() {
		return labelStash;
	}

	public void setLabelStash(Map<String, List<String>> labelStash) {
		this.labelStash = labelStash;
	}

	public List<MTTR> getLabelMttr() {
		return labelMttr;
	}

	public void setLabelMttr(List<MTTR> labelMttr) {
		this.labelMttr = labelMttr;
	}

	public List<OperationalMetrics> getOperationMetrics() {
		return operationMetrics;
	}

	public void setOperationMetrics(List<OperationalMetrics> operationMetrics) {
		this.operationMetrics = operationMetrics;
	}

}
