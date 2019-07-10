package com.capitalone.dashboard.exec.model.vz;

import java.util.List;
import java.util.Map;

public class SeriesCount {

	private Long count;
	private Map<String, String> label;
	private Map<String, List<String>> labelStash;
	private Map<String, Map<String, Long>> labelLoc;

	public Map<String, List<String>> getLabelStash() {
		return labelStash;
	}

	public void setLabelStash(Map<String, List<String>> labelStash) {
		this.labelStash = labelStash;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public Map<String, String> getLabel() {
		return label;
	}

	public void setLabel(Map<String, String> label) {
		this.label = label;
	}

	public Map<String, Map<String, Long>> getLabelLoc() {
		return labelLoc;
	}

	public void setLabelLoc(Map<String, Map<String, Long>> labelLoc) {
		this.labelLoc = labelLoc;
	}

}
