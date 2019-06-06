package com.capitalone.dashboard.exec.collector;

import com.capitalone.dashboard.exec.model.*;
import com.capitalone.dashboard.exec.repository.LobMetricRepository;
import org.apache.commons.collections.CollectionUtils;
import org.apache.spark.sql.Row;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class EngineeringMaturityCollector extends DefaultMetricCollector {

    private static final String STR_TIMEWINDOW = "timeWindow";
    private static final String STR_AUDIT_STATUS = "auditStatus";
    private Map auditMap = new HashMap();


    public EngineeringMaturityCollector(LobMetricRepository lobMetricRepository) {
        super(lobMetricRepository);
    }

    @Override
    protected MetricType getMetricType() {
        return MetricType.ENGINEERING_MATURITY;
    }

    @Override
    protected String getQuery() {
        return HygieiaSparkQuery.ENGG_MATURITY_QUERY_ALL_COLLECTOR_ITEMS;
    }

    @Override
    protected String getCollection() {
        return "audit_results";
    }

    @Override
    protected MetricCollectionStrategy getCollectionStrategy() {
        return MetricCollectionStrategy.LATEST;
    }

    @Override
    protected CollectorItemMetricDetail getCollectorItemMetricDetail(List<Row> rowList, MetricType metricType) {
        CollectorItemMetricDetail collectorItemMetricDetail = new CollectorItemMetricDetail();
        if (CollectionUtils.isEmpty(rowList)) {
            return collectorItemMetricDetail;
        }
        collectorItemMetricDetail.setType(getMetricType());
        for (Row row : rowList) {
            updateCollectorItemMetricDetail(collectorItemMetricDetail, row);
        }

        return collectorItemMetricDetail;
    }

    private void updateCollectorItemMetricDetail(CollectorItemMetricDetail collectorItemMetricDetail, Row row) {
        Date timeWindowDt = row.getAs(STR_TIMEWINDOW);
        String auditStatus = row.getAs(STR_AUDIT_STATUS);
        String auditType = row.getAs("auditType");
        String auditTypeStatus = row.getAs("auditTypeStatus");
        auditMap.put("auditStatus", auditStatus);
        auditMap.put("auditType", auditType);
        auditMap.put("auditTypeStatus", auditTypeStatus);

        double value = 0.0;
        if (auditStatus.equalsIgnoreCase("OK")){
            value = 1.0;
        }
        MetricCount mc = getMetricCount("", value, auditType);
        if (!mc.getLabel().isEmpty()) {
            collectorItemMetricDetail.setStrategy(getCollectionStrategy());
            collectorItemMetricDetail.addCollectorItemMetricCount(timeWindowDt, mc);
            collectorItemMetricDetail.setLastScanDate(timeWindowDt);
        }
    }

    @Override
    protected CollectorType getCollectorType() {
        return CollectorType.Audit;
    }


    @Override
    protected MetricCount getMetricCount(String level, double value, String type) {
        MetricCount metricCount = new MetricCount();
        Map<String, String> label = new HashMap<>();
//        String metricName = getMetricLabel(type);
        String metricName = type;

        if(!metricName.isEmpty()) {
            label.put("type", auditMap.get("auditType").toString());
            label.put("auditTypeStatus", auditMap.get("auditTypeStatus").toString());
            label.put("auditStatus", auditMap.get("auditStatus").toString());
            metricCount.setLabel(label);
            if (value == 1.0){
                metricCount.setValue(1.0);
            }else {
                metricCount.setValue(0.0);
            }
            return metricCount;
        }
        return metricCount;
    }
}
