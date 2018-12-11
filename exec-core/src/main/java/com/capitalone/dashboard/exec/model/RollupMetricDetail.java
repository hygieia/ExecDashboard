package com.capitalone.dashboard.exec.model;

import java.util.*;

public class RollupMetricDetail extends MetricDetails {
    protected static final String MTTR = "mttr";
    protected static final String TYPE = "type";
    protected static final int TREND_SLOPE_BUCKETS = 6;

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
        double values[] = new double[TREND_SLOPE_BUCKETS];

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

            if (daysAgo <= 15) {
                values[values.length-1] += metricCountValue;
            } else if (daysAgo <= 31) {
                values[values.length-2] += metricCountValue;
            } else if (daysAgo <= 46) {
                values[values.length-3] += metricCountValue;
            } else if (daysAgo <= 61) {
                values[values.length-4] += metricCountValue;
            } else if (daysAgo <= 76) {
                values[values.length-5] += metricCountValue;
            } else {
                values[values.length-6] += metricCountValue;
            }
        };

        return deriveTrend(values);
    }

    protected Double deriveTrend(double values[]) {
        double trend1 = values[1] - values[0]; // compare 1st bucket and 2nd bucket

        double trend2 = values[2] - values[1]; // compare 2nd bucket and 3rd bucket

        double trend12 = trend2 + trend1; // compare the trends between 1st, 2nd, and 2nd, 3rd buckets.

        double trend3 = values[3] - values[2]; // compare 3rd and 4th buckets.

        double trend123 = trend3 + trend12; // compare the net trend between 1,2,3 buckets, with the trend between buckets 3,4.

        double trend4 = values[4] - values[3]; // compare 4th and 5th buckets.

        double trend1234 = trend4 + trend123; // compare the net trend between 1,2,3,4 buckets, with the trend between buckets 4,5

        double trend5 = values[5] - values[4]; // compare 5th and 6th buckets.

        double trend12345 = trend5 + trend1234; // compare the net trend between 1,2,3,4,5 buckets, with the trend between buckets 5,6

        if (trend12345 == 0.0) {
            return 0.0;
        }
        return (trend12345 > 0.0)?1.0:-1.0;
    }
}
