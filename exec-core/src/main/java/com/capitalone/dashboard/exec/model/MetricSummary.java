package com.capitalone.dashboard.exec.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MetricSummary {
    protected List<MetricCount> counts;
    private Date lastScanned;
    private Date lastUpdated;
    private double trendSlope;
    private String name;

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public List<MetricCount> getCounts() {
        return counts;
    }

    public void setCounts(List<MetricCount> counts) { this.counts = counts; }

    public Date getLastScanned() {
        return lastScanned;
    }

    public void setLastScanned(Date lastScanned) {
        this.lastScanned = lastScanned;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public double getTrendSlope() {
        return trendSlope;
    }

    public void setTrendSlope(double trendSlope) {
        this.trendSlope = trendSlope;
    }

    public MetricCount getMetricCountByLabel (Map<String, String> label) {
        return counts.stream().filter(c -> c.getLabel().equals(label)).findFirst().orElse(null);
    }

    public void addCollectorItemCount(MetricCount count, MetricCollectionStrategy strategy, Date scanTime) {

        MetricCount cloneCount = count.copy();
        this.setLastUpdated(new Date());
        if (counts == null) {
            counts = new ArrayList<>();
        }
        MetricCount oCount = this.getMetricCountByLabel(count.getLabel());
        if (scanTime != null) { this.setLastScanned(scanTime); }

        if (oCount == null) {
            counts.add(cloneCount);
        } else {
            counts.remove(oCount);
            switch (strategy) {
                case CUMULATIVE:
                    cloneCount.addValue(oCount.getValue());
                    counts.add(cloneCount);
                    break;

                case LATEST:
                    if (getLastScanned() == null || getLastScanned().before(scanTime) || getLastScanned().equals(scanTime)) {
                        counts.add(cloneCount);
                    }
                    break;

                case AVERAGE:
                    cloneCount.addAverageValue(oCount.getValue());
                    counts.add(cloneCount);
                    break;

                default:
                    break;
            }
        }
    }
}
