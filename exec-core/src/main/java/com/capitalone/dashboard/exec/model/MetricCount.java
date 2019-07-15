package com.capitalone.dashboard.exec.model;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MetricCount {
    private Map<String, String> label;
    private double value;
    
    private Map<String, Map<String, Long>> labelLoc;
	private Map<String, List<String>> labelStash;
	private List<MTTR> labelMttr;
	private List<OperationalMetrics> operationMetrics;


    public MetricCount() {
    }

    public MetricCount(Map<String, String> label, double value) {
        this.label = label;
        this.value = value;
    }

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

    public void addValue(double val) {
        value += val;
    }

    public void addAverageValue(double val,int reportingComponents) {
        value = ((val*(reportingComponents-1)) + value)/reportingComponents;
    }
    public void addMetricSummaryAverageValue(double val) {
        value = (value + val)/2.0;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MetricCount)) return false;
        MetricCount count = (MetricCount) o;
        return Objects.equals(label, count.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label);
    }

    protected MetricCount copy() {
        return new MetricCount(this.getLabel(), this.getValue());
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
