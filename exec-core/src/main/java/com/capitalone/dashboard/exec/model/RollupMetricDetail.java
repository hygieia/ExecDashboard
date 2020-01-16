package com.capitalone.dashboard.exec.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.util.Optional;

public class RollupMetricDetail extends MetricDetails {
    protected static final String MTTR = "mttr";
    protected static final String TYPE = "type";
    @SuppressWarnings("PMD.NPathComplexity")
    protected void updateSummary(MetricDetails metricDetails, int size) {
        if (metricDetails.isProcessed()) {return;}
        MetricSummary itemMetricSummary = metricDetails.getSummary();
        setReportingComponents(getReportingComponents() + 1);
        if ((summary.getLastScanned() == null) || summary.getLastScanned().before(itemMetricSummary.getLastScanned())) {
            summary.setLastScanned(itemMetricSummary.getLastScanned());
        }
        List<MetricCount> itemSummaryCounts = itemMetricSummary.getCounts();
        List<MetricCount> rollupSummaryCounts = summary.getCounts() != null ? summary.getCounts() : new ArrayList<>();

        for (MetricCount count : itemSummaryCounts) {
//            MetricCount copyCount = count.copy();
//            MetricCount existing = rollupSummaryCounts.stream().filter(cs -> cs.getLabel().equals(copyCount.getLabel())).findFirst().orElse(null);
            MetricCount copyCount = getCopyCount(count,metricDetails);
            MetricCount existing = rollupSummaryCounts.stream().filter(cs -> cs.getLabel().get("type").equals(copyCount.getLabel().get("type"))).findFirst().orElse(null);
            if (existing == null) {
                rollupSummaryCounts.add(copyCount);
            } else {
                rollupSummaryCounts.remove(existing);
                MetricType.DataType  dataType= metricDetails.getType().getDataType();
                if(existing.getLabel().get("type").equalsIgnoreCase("mttr")){
                    dataType = MetricType.DataType.AVERAGE;
                }
                MetricCount updatedMetricCount = getUpdatedMetricCount(copyCount, dataType, existing.getValue(),size);
                rollupSummaryCounts.add(updatedMetricCount);
            }
        }
        summary.setCounts(rollupSummaryCounts);
        if (getType() != null) { summary.setName(getType().getName()); }
    }
    private MetricCount getUpdatedMetricCount(MetricCount metricCount, MetricType.DataType metricDataType, Double existingValue,int reporting) {
        if(metricDataType.equals(MetricType.DataType.SUM)) {
            metricCount.addValue(existingValue);
        } else {
            metricCount.addAverageValue(existingValue,reporting);
        }
        return metricCount;
    }

    protected void updateTimeSeries(MetricDetails itemMetricDetails, int size) {
        if (itemMetricDetails.isProcessed()) {return;}
        List<MetricTimeSeriesElement> itemMetricDetailTimeSeries = itemMetricDetails.getTimeSeries();
        // Temp fix - the timeseries count should not be more than 90
        if (itemMetricDetailTimeSeries!=null && itemMetricDetailTimeSeries.size() > 90)
        	itemMetricDetailTimeSeries.subList(90, itemMetricDetailTimeSeries.size()).clear();
        for (MetricTimeSeriesElement itemDetailsTimeSeriesElement : itemMetricDetailTimeSeries) {
            List<MetricCount> itemTimeSeriesElementCounts = itemDetailsTimeSeriesElement.getCounts();
            // Temp fix - the timeseries count should not be more than 90
            //itemTimeSeriesElementCounts.subList(91, itemTimeSeriesElementCounts.size()).clear();
            for (MetricCount itemCount : itemTimeSeriesElementCounts) {
                if(itemMetricDetails.getType().getDataType().equals(MetricType.DataType.SUM)) {
                    timeSeries.get(itemDetailsTimeSeriesElement.getDaysAgo()).addCount(itemCount);
                } else {
                    timeSeries.get(itemDetailsTimeSeriesElement.getDaysAgo()).averageCount(itemCount,size);
                }

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

    private MetricCount getCopyCount(MetricCount count, MetricDetails metricDetails){
        MetricCount copyCount;
        if(metricDetails.getType().equals(MetricType.ENGINEERING_MATURITY )&& !metricDetails.getLevel().equals(MetricLevel.COLLECTOR_ITEM)){
            MetricCount copyCount1 = new MetricCount();
            HashMap<String,String> label = new HashMap<>();
            label.put("type", count.getLabel().get("type"));
            double val = count.getValue();
            copyCount1.setLabel(label);
            copyCount1.setValue(val);
            copyCount = copyCount1.copy();
        }else {
            copyCount = count.copy();
        }
        return copyCount;
    }
}
