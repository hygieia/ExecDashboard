package com.capitalone.dashboard.exec.collector;

import com.capitalone.dashboard.exec.model.CollectorItemMetricDetail;
import com.capitalone.dashboard.exec.model.CollectorType;
import com.capitalone.dashboard.exec.model.HygieiaSparkQuery;
import com.capitalone.dashboard.exec.model.MetricCollectionStrategy;
import com.capitalone.dashboard.exec.model.MetricCount;
import com.capitalone.dashboard.exec.model.MetricType;
import com.capitalone.dashboard.exec.repository.PortfolioMetricRepository;
import org.apache.commons.collections.CollectionUtils;
import org.apache.spark.sql.Row;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class IncidentCollector extends DefaultMetricCollector {

    public IncidentCollector(PortfolioMetricRepository portfolioMetricRepository) {
        super(portfolioMetricRepository);
    }

    @Override
    public MetricType getMetricType() {
        return MetricType.PRODUCTION_INCIDENTS;
    }

    @Override
    public String getQuery() {
        return HygieiaSparkQuery.CMDB_INCIDENT_QUERY;
    }

    @Override
    public String getCollection() {
        return "incident";
    }

    @Override
    protected CollectorType getCollectorType() { return CollectorType.Incident; }

    @Override
    protected MetricCollectionStrategy getCollectionStrategy() {
        return MetricCollectionStrategy.CUMULATIVE;
    }

    @Override
    protected CollectorItemMetricDetail getCollectorItemMetricDetail(List<Row> rowList, MetricType metricType) {
         CollectorItemMetricDetail collectorItemMetricDetail = new CollectorItemMetricDetail();
        if (CollectionUtils.isEmpty(rowList)) { return collectorItemMetricDetail; }

        collectorItemMetricDetail.setType(metricType);
        for (Row row : rowList) {
            if (row.getAs("attachedToBusinessServiceOrEnvironmentOnly")) {
                collectorItemMetricDetail.setAttachedToBusinessServiceOrEnvironmentOnly(true);
            }
            updateCollectorItemMetricDetail(collectorItemMetricDetail, row);
        }
        return collectorItemMetricDetail;
    }

    private void updateCollectorItemMetricDetail(CollectorItemMetricDetail collectorItemMetricDetail, Row itemRow) {
        Date openDate = itemRow.getAs("timeWindow");
        Date closeDate = null;
        Long closedTime = itemRow.getAs("closedTime");
        if ((closedTime != null) && (closedTime != 0L)) {
            closeDate = itemRow.getAs("closeDate");
        }
        String severity = itemRow.getAs("severity");
        String status = itemRow.getAs("status");
        double value = itemRow.getAs("count");

        long meanTimeToResolveInMilliSeconds = 0;
        if ((openDate != null) && (closeDate != null)) {
            meanTimeToResolveInMilliSeconds = Math.abs(closeDate.getTime()-openDate.getTime());
        }
        MetricCount mc = getMetricCount(severity, value, status);

        collectorItemMetricDetail.setStrategy(getCollectionStrategy());
        collectorItemMetricDetail.addCollectorItemMetricCount(openDate, mc);

        collectorItemMetricDetail.setStrategy(MetricCollectionStrategy.AVERAGE);
        collectorItemMetricDetail.updateMeanTimeToResolve(meanTimeToResolveInMilliSeconds);
        collectorItemMetricDetail.setStrategy(getCollectionStrategy());

        collectorItemMetricDetail.setLastScanDate(openDate);
    }

    protected MetricCount getMetricCount(String severity, double value, String status) {
        MetricCount mc = new MetricCount();
        Map<String, String> label = new HashMap<>();
        label.put("type", "issue");
        label.put("severity", severity);
        label.put("event", status);
        mc.setLabel(label);
        mc.setValue(value);
        return mc;
    }
}