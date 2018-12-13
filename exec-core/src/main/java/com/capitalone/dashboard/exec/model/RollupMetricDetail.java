package com.capitalone.dashboard.exec.model;

import java.util.*;

public class RollupMetricDetail extends MetricDetails {
    protected static final String MTTR = "mttr";
    protected static final String TYPE = "type";
    protected static final int BUCKET_SIZE = 15;
    protected static final int RANGE = 90;

    protected void updateSummary(MetricDetails metricDetails) {
        if (metricDetails.isProcessed()) {return;}
        MetricSummary itemMetricSummary = metricDetails.getSummary();
        setReportingComponents(metricDetails);

        if ((summary.getLastScanned() == null)||summary.getLastScanned().before(itemMetricSummary.getLastScanned())) {
            summary.setLastScanned(itemMetricSummary.getLastScanned());
        }
        List<MetricCount> itemSummaryCounts = itemMetricSummary.getCounts();
        List<MetricCount> rollupSummaryCounts = summary.getCounts() != null ? summary.getCounts() : new ArrayList<>();

        for (MetricCount count : itemSummaryCounts) {
            MetricCount copyCount = count.copy();
            MetricCount existing = rollupSummaryCounts.stream().filter(cs -> cs.getLabel().equals(copyCount.getLabel())).findFirst().orElse(null);
            if (existing == null) {
                rollupSummaryCounts.add(copyCount);
            } else {
                processExistingMetricCount(existing, copyCount, rollupSummaryCounts);
            }
        }
        summary.setCounts(rollupSummaryCounts);
        if (getType() != null) { summary.setName(getType().getName()); }
    }

    protected void setReportingComponents(MetricDetails metricDetails) {
        if ( (metricDetails instanceof CollectorItemMetricDetail)
                && ((CollectorItemMetricDetail) metricDetails).isAttachedToBusinessServiceOrEnvironmentOnly() ) { return; }

        setReportingComponents(getReportingComponents() + 1);
    }

    protected void processExistingMetricCount(MetricCount existing, MetricCount copyCount,
                                              List<MetricCount> rollupSummaryCounts) {
        rollupSummaryCounts.remove(existing);
        Map<String, String> label = existing.getLabel();
        if (MTTR.equalsIgnoreCase(label.get(TYPE))) { // Mean Time To Resolve
            copyCount.addAverageValue(existing.getValue());
        } else {
            copyCount.addValue(existing.getValue());
        }
        rollupSummaryCounts.add(copyCount);
    }

    protected void updateTimeSeries(MetricDetails itemMetricDetails) {
        if (itemMetricDetails.isProcessed()) {return;}
        List<MetricTimeSeriesElement> itemMetricDetailTimeSeries = itemMetricDetails.getTimeSeries();
        for (MetricTimeSeriesElement itemDetailsTimeSeriesElement : itemMetricDetailTimeSeries) {
            List<MetricCount> itemTimeSeriesElementCounts = itemDetailsTimeSeriesElement.getCounts();
            for (MetricCount itemCount : itemTimeSeriesElementCounts) {
                timeSeries.get(itemDetailsTimeSeriesElement.getDaysAgo()).addCount(itemCount);
            }
        }

        summary.setTrendSlope(calculateTrendSlope(timeSeries));
    }

    protected Double calculateTrendSlope(List<MetricTimeSeriesElement> timeSeriesElements) {
        int arraySize = RANGE/BUCKET_SIZE;
        double values[] = new double[arraySize];

        // Get the total counts in each bucket
        for (MetricTimeSeriesElement t: timeSeriesElements) {
            double daysAgo = t.getDaysAgo();
            List<MetricCount> metricCountsList = t.getCounts();

            MetricCount metricCount =
                    Optional.ofNullable(metricCountsList)
                            .orElseGet(Collections::emptyList).stream()
                            .filter(m -> (m.getLabel() != null)
                                    && (m.getLabel().get("type") != null) ||
                                    (m.getLabel().containsKey("severity") &&
                                            !m.getLabel().get("severity").equalsIgnoreCase("None") ))
                            .findFirst().orElse(null);

            double metricCountValue = (metricCount != null)?metricCount.getValue():0.0;

            for (int i=1; i<=arraySize; i++) {
                if (daysAgo <= (BUCKET_SIZE*i)) {
                    values[values.length-i] += metricCountValue;
                    break;
                }
            }
        }

        return deriveTrend(values);
    }

    protected Double deriveTrend(double values[]) {
        double previousTrend = 0.0;
        double cumulativeTrend = 0.0;

        for (int i=0; i<values.length; i++) {
            double currentTrend = 0.0;
            if (i > 0) {
                currentTrend = (values[i] > 0)?(values[i] - values[i-1]):values[i];
            }

            cumulativeTrend = currentTrend + previousTrend;
            previousTrend = currentTrend;
        }

        if (cumulativeTrend == 0.0) {
            return 0.0;
        }

        return (cumulativeTrend > 0.0)?1.0:-1.0;
    }
}
