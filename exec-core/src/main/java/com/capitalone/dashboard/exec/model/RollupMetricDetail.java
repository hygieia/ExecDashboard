package com.capitalone.dashboard.exec.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RollupMetricDetail extends MetricDetails {
    protected static final String MTTR = "mttr";
    protected static final String TYPE = "type";

    protected void updateSummary(MetricDetails metricDetails) {
        if (metricDetails.isProcessed()) {return;}
        MetricSummary itemMetricSummary = metricDetails.getSummary();
        setReportingComponents(getReportingComponents() + 1);
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
        List<Double> daysAgoList = new ArrayList<>();
        List<Double> valuesList = new ArrayList<>();
        timeSeriesElements.forEach(t ->{
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
            if (metricCount != null) {
                daysAgoList.add(daysAgo);
                valuesList.add(metricCount.getValue());
            }
        });
        Double trendSlope;
        Double linearCoefficient = 0.5;

        Double standardDeviationForDaysAgo = calculateStandardDeviation(daysAgoList);
        Double standardDeviationForValues = calculateStandardDeviation(valuesList);

        trendSlope = linearCoefficient * (standardDeviationForValues/standardDeviationForDaysAgo);

        return trendSlope;
    }

    protected Double calculateStandardDeviation(List<Double> values) {
        double sum = 0.0;
        double averageValue = getAverageValue(values);

        for(Double value: values) { sum += Math.pow((value - averageValue), 2); }

        return Math.sqrt( sum / ( values.size() - 1 ) );
    }

    protected Double getAverageValue(List<Double> values) {
        if (values == null || values.isEmpty()) { return 0.0; }

        Double averageValue = Optional.ofNullable(values)
                .orElseGet(Collections::emptyList).stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .getAsDouble();
        return averageValue;
    }
}
