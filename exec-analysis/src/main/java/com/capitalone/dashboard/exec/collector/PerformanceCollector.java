package com.capitalone.dashboard.exec.collector;

import com.capitalone.dashboard.exec.model.MetricType;
import com.capitalone.dashboard.exec.model.MetricCount;
import com.capitalone.dashboard.exec.model.HygieiaSparkQuery;
import com.capitalone.dashboard.exec.model.CollectorItemMetricDetail;
import com.capitalone.dashboard.exec.model.MetricCollectionStrategy;
import com.capitalone.dashboard.exec.model.CollectorType;
import com.capitalone.dashboard.exec.repository.PortfolioMetricRepository;
import org.apache.commons.collections.CollectionUtils;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Date;

@Component
public class PerformanceCollector extends DefaultMetricCollector {
    
    private static final String STR_TIMEWINDOW = "timeWindow";
    private static final String STR_AVG_RESPONSE_TIME = "averageResponseTime";
    private static final String STR_CALLSPER_MINUTE = "callsperMinute";
    private static final String STR_ERROR_RATE = "actualErrorRate";
    private static final String STR_AVGRESPONSE = "Response Time";
    private static final String STR_TPS = "TPS"; // TPS - Transaction Per Minute
    private static final String STR_ERRORRATE = "Error Rate";



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
        return "performance";
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
        List<String> performanceMetricList = Arrays.asList(STR_AVG_RESPONSE_TIME,STR_CALLSPER_MINUTE,STR_ERROR_RATE);
        GenericRowWithSchema pefMetrics = row.getAs("metrics");

        for(String perfMetric :performanceMetricList){
            double value;
            try {
                Long valueStr = pefMetrics.getAs(perfMetric);
                value = valueStr.doubleValue();
            }catch (IllegalArgumentException exception){
                value = 0.0;
            }

            MetricCount mc = getMetricCount("", value, perfMetric);
            if (!mc.getLabel().isEmpty()) {
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

        if(!metricName.isEmpty()) {
            label.put("type", metricName);
            metricCount.setLabel(label);
            if(STR_TPS.equalsIgnoreCase(metricName)){
            metricCount.setValue(value/60);
            }else {
                metricCount.setValue(value);
            }
            return metricCount;
        }
        return metricCount;
    }

    private String getMetricLabel(String inputMetricLabel) {
        if(inputMetricLabel.equals(STR_AVG_RESPONSE_TIME)) {
            return STR_AVGRESPONSE;
        } else if (inputMetricLabel.equals(STR_CALLSPER_MINUTE)) {
            return STR_TPS;
        } else if (inputMetricLabel.equals(STR_ERROR_RATE)) {
            return STR_ERRORRATE;
        }
        return "";
    }


}
