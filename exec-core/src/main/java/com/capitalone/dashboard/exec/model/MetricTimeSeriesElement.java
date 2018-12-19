package com.capitalone.dashboard.exec.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MetricTimeSeriesElement {
    private int daysAgo;
    private long timestamp;
    private List<MetricCount> counts;

    public int getDaysAgo() { return daysAgo; }
    public void setDaysAgo(int daysAgo) { this.daysAgo = daysAgo; }

    public List<MetricCount> getCounts() { return counts; }
    public void setCounts(List<MetricCount> counts) { this.counts = counts; }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void addCount(MetricCount count) {
        if (counts == null) {
            counts = new ArrayList<>();
        }
        MetricCount oCount = getMetricCountByLabel(count.getLabel());
        MetricCount copyCount = count.copy();
        if (oCount != null) {
            counts.remove(oCount);
            copyCount.addValue(oCount.getValue());
        }
        counts.add(copyCount);
    }

    public void averageCount(MetricCount count) {
        if (counts == null) {
            counts = new ArrayList<>();
        }
        MetricCount oCount = getMetricCountByLabel(count.getLabel());
        MetricCount copyCount = count.copy();
        if (oCount != null) {
            counts.remove(oCount);
            copyCount.averageValue(oCount.getValue());
        }
        counts.add(copyCount);
    }

    public MetricCount getMetricCountByLabel (Map<String, String> label) {
        return counts.stream().filter(c -> c.getLabel().equals(label)).findFirst().orElse(null);
    }
}
