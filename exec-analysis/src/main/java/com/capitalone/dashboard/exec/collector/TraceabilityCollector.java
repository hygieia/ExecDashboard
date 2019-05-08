package com.capitalone.dashboard.exec.collector;

import com.capitalone.dashboard.exec.model.MetricType;
import com.capitalone.dashboard.exec.model.HygieiaSparkQuery;
import com.capitalone.dashboard.exec.model.MetricCollectionStrategy;
import com.capitalone.dashboard.exec.model.CollectorItemMetricDetail;
import com.capitalone.dashboard.exec.model.MetricCount;
import com.capitalone.dashboard.exec.model.CollectorType;
import com.capitalone.dashboard.exec.repository.PortfolioMetricRepository;
import org.apache.commons.collections.CollectionUtils;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema;
import org.springframework.stereotype.Component;


import java.util.Date;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class TraceabilityCollector extends DefaultMetricCollector {
    private static final String STR_AUDIT_RESULTS = "audit_results";
    private static final String STR_AUTOMATED = "Automated";
    private static final String STR_MANUAL = "Manual";
    private static final String STR_TIME_WINDOW = "timeWindow";
    private static final String STR_TRACEABILITY = "traceability";

    public TraceabilityCollector(PortfolioMetricRepository portfolioMetricRepository) {
        super(portfolioMetricRepository);
    }

    @Override
    protected MetricType getMetricType() {
        return MetricType.TRACEABILITY;
    }

    @Override
    protected String getQuery() {
        return HygieiaSparkQuery.TRACEABILITY_QUERY;
    }

    @Override
    protected String getCollection() {
        return STR_AUDIT_RESULTS;
    }

    @Override
    protected MetricCollectionStrategy getCollectionStrategy() {
        return MetricCollectionStrategy.LATEST;
    }

    @Override
    protected CollectorItemMetricDetail getCollectorItemMetricDetail(List<Row> rowList, MetricType metricType) {
        CollectorItemMetricDetail collectorItemMetricDetail = new CollectorItemMetricDetail();
        if (!CollectionUtils.isEmpty(rowList)) {
            collectorItemMetricDetail.setType(getMetricType());
            for (Row row : rowList) {
                updateCollectorItemMetricDetail(collectorItemMetricDetail, row);
            }
        }

        return collectorItemMetricDetail;
    }


    /**
     *
     * @param collectorItemMetricDetail
     * @param itemRow
     *
     * Update collectorItemMetric details with latest timeSeries data and summary.
     *
     */
    private void updateCollectorItemMetricDetail(CollectorItemMetricDetail collectorItemMetricDetail,Row itemRow) {

        Date timeWindowDt = itemRow.getAs(STR_TIME_WINDOW);
        List<String> testTypes = Arrays.asList(STR_AUTOMATED, STR_MANUAL);
        GenericRowWithSchema javaCollection = itemRow.getAs(STR_TRACEABILITY);
        testTypes.forEach(testType -> {
            double value = javaCollection.getAs(testType);
            MetricCount mc = getMetricCount("", value, testType);
            if (mc != null) {
                collectorItemMetricDetail.setStrategy(getCollectionStrategy());
                collectorItemMetricDetail.addCollectorItemMetricCount(timeWindowDt, mc);
                collectorItemMetricDetail.setLastScanDate(timeWindowDt);
            }
        });
    }


    @Override
    protected CollectorType getCollectorType() {
        return CollectorType.Audit;
    }

    @Override
    protected MetricCount getMetricCount(String level, double value, String type)
    {
        MetricCount mc = new MetricCount();
        Map<String, String> label = new HashMap<>();
        String metricLabel = type;
        if(metricLabel != null) {
            label.put("type", metricLabel);
            mc.setLabel(label);
            mc.setValue(value);
            return mc;
        }
        return null;
    }
}
