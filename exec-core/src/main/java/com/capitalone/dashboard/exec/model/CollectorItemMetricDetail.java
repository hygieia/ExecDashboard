package com.capitalone.dashboard.exec.model;

import com.capitalone.dashboard.exec.util.HygieiaExecutiveUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CollectorItemMetricDetail extends MetricDetails {
    private MetricCollectionStrategy strategy;
    private Date lastScanDate;


    public CollectorItemMetricDetail() {
        setLevel(MetricLevel.COLLECTOR_ITEM);
    }

    public MetricCollectionStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(MetricCollectionStrategy strategy) {
        this.strategy = strategy;
    }

    public Date getLastScanDate() {
        return lastScanDate;
    }

    public void setLastScanDate(Date lastScanDate) {
        this.lastScanDate = lastScanDate;
    }

    public void addCollectorItemMetricCount(Date scanDate, MetricCount metricCount) {
        if (summary == null) {
            summary = new MetricSummary();
        }

        summary.addCollectorItemCount(metricCount, strategy, scanDate);

        switch (strategy) {
            case LATEST:
                if ((lastScanDate != null) && lastScanDate.after(scanDate)) {
                    return;
                } else {
                    updateTimeSeries(metricCount, scanDate);
                }
                break;
            case CUMULATIVE:
                updateTimeSeries(metricCount, scanDate);
                break;
            case AVERAGE:
                updateTimeSeries(metricCount,scanDate);
                break;
            default:
                throw new UnsupportedOperationException("Not supported yet");
        }
    }

    public void updateMeanTimeToResolve(long meanTimeToResolve) {
        if (summary == null) {return;}
        MetricCount mc = new MetricCount();
        Map<String, String> label = new HashMap<>();
        label.put("type", "mttr");
        mc.setLabel(label);
        mc.setValue(meanTimeToResolve);
        summary.addCollectorItemCount(mc, MetricCollectionStrategy.AVERAGE, null);
    }

    private void updateTimeSeries(MetricCount metricCount, Date scanTime) {
        MetricTimeSeriesElement mts = new MetricTimeSeriesElement();
        mts.setTimestamp(scanTime.getTime());
        mts.setDaysAgo(HygieiaExecutiveUtil.getDaysAgo(scanTime));
        mts.addCount(metricCount);
        addTimeSeriesElement(mts, metricCount, scanTime);
    }

    public void addTimeSeriesElement(MetricTimeSeriesElement element, MetricCount metricCount, Date scanTime) {
        if (timeSeries == null) {
            timeSeries = HygieiaExecutiveUtil.getEmptyTimeSeries();
        }
        MetricTimeSeriesElement existing = timeSeries.stream()
                .filter(ts -> ts.getDaysAgo() == element.getDaysAgo())
                .findFirst().orElse(null);
        if (existing != null) {
            switch (strategy) {
                case CUMULATIVE:
                    existing.addCount(metricCount);
                    break;
                case LATEST:
                    timeSeries.remove(existing);
                    MetricCount existingMetricCount = existing.getCounts().stream().
                            filter(mc -> mc.getLabel().equals(element.getCounts().get(0).getLabel()))
                            .findFirst().orElse(null);
                    if(existingMetricCount != null && !lastScanDate.after(scanTime)) {
                       existing.getCounts().remove(existingMetricCount);
                    }
                    existing.getCounts().add(metricCount);
                    timeSeries.add(existing.getDaysAgo(), existing);
                    break;
                case AVERAGE:
                    if (this.timeSeries != null && this.timeSeries.get(existing.getDaysAgo()).getCounts().size() > 0) {
                        (this.timeSeries.get(existing.getDaysAgo()).getCounts().get(0)).setValue(((this.timeSeries.get(existing.getDaysAgo()).getCounts().get(0)).getValue() + metricCount.getValue())/2);
                    }else {
                        existing.getCounts().add(metricCount);
                    }
                    break;
                    //throw new UnsupportedOperationException("Not supported yet");
                default:
                    break;
            }
        } else {
            // looks like a bug, it should not add value simply for more than 90 days, doing a fix for pipeline lead time
            // Need to check for other widgets.. commit, sonar.. etc...
            if ((metricCount.getLabel().get("type")).equals(MetricType.PIPELINE_LEAD_TIME.getName())) {
                if (element != null && element.getDaysAgo() < 90) {
                    timeSeries.add(element);
                }
            } else {
                timeSeries.add(element);
            }
        }
    }

}
