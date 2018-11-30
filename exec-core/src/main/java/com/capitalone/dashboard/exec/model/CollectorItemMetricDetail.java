package com.capitalone.dashboard.exec.model;

import com.capitalone.dashboard.exec.util.HygieiaExecutiveUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CollectorItemMetricDetail extends MetricDetails {
    private MetricCollectionStrategy strategy;
    private Date lastScanDate;
    private boolean attachedToBusinessServiceOrEnvironmentOnly;

    public boolean isAttachedToBusinessServiceOrEnvironmentOnly() { return attachedToBusinessServiceOrEnvironmentOnly; }

    public void setAttachedToBusinessServiceOrEnvironmentOnly(boolean attachedToBusinessServiceOrEnvironmentOnly) { this.attachedToBusinessServiceOrEnvironmentOnly = attachedToBusinessServiceOrEnvironmentOnly; }

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
        summary.addCollectorItemCount(mc, strategy, null);
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
                    throw new UnsupportedOperationException("Not supported yet");
                default:
                    break;
            }
        } else {
            timeSeries.add(element);
        }
    }

}
