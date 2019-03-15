package com.capitalone.dashboard.exec.collector;

import com.capitalone.dashboard.exec.model.*;
import com.capitalone.dashboard.exec.repository.PortfolioMetricRepository;
import org.apache.commons.collections.CollectionUtils;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import java.util.*;

@Component
public class PerformanceCollector extends DefaultMetricCollector {

    private static final String STR_PERFORMANCE = "performance";
    private static final String STR_TIMEWINDOW = "timeWindow";
    private static final String STR_AVG_RESPONSE_TIME = "averageResponseTime";
    private static final String STR_CALLSPER_MINUTE = "callsperMinute";
    private static final String STR_ERROR_RATE = "actualErrorRate";


    public PerformanceCollector(PortfolioMetricRepository portfolioMetricRepository) {
        super(portfolioMetricRepository);
    }

    @Override
    protected MetricType getMetricType() {
        return MetricType.PERFORMANCE_TEST;
    }

    @Override
    protected String getQuery() {
        return HygieiaSparkQuery.PERFORMANCE_QUERY_ALL_COLLECTOR_ITEMS;
    }

    @Override
    protected String getCollection() {
        return STR_PERFORMANCE;
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
        List<String> performanceMetricList = Arrays.asList("averageResponseTime","callsperMinute","actualErrorRate");
        GenericRowWithSchema pefMetrics = row.getAs("metrics");

        for(String perfMetric :performanceMetricList){
            Long valueStr =  pefMetrics.getAs(perfMetric);
            double value = valueStr;
            MetricCount mc = getMetricCount("", value, perfMetric);
            if (mc != null) {
                collectorItemMetricDetail.setStrategy(getCollectionStrategy());
                collectorItemMetricDetail.addCollectorItemMetricCount(timeWindowDt, mc);
                collectorItemMetricDetail.setLastScanDate(timeWindowDt);
            }
        }
    }

    @Override
    protected CollectorType getCollectorType() {
        return CollectorType.AppPerformance;
    }

    @Override
    protected MetricCount getMetricCount(String level, double value, String type) {
        MetricCount metricCount = new MetricCount();
        Map<String, String> label = new HashMap<>();
        String metricName = getMetricLabel(type);

        if(metricName != null) {
            label.put("type", metricName);
            metricCount.setLabel(label);
            if(metricName.equalsIgnoreCase("Transaction Per Second")){
            metricCount.setValue(value/60);
            }else {
                metricCount.setValue(value);
            }
            return metricCount;
        }
        return null;
    }

    private String getMetricLabel(String inputMetricLabel) {
        if(inputMetricLabel.equals(STR_AVG_RESPONSE_TIME)) {
            return "Avg Response Times";
        } else if (inputMetricLabel.equals(STR_CALLSPER_MINUTE)) {
            return "Transaction Per Second";
        } else if (inputMetricLabel.equals(STR_ERROR_RATE)) {
            return "Error Rate Threshold";
        }
        return null;
    }


}
