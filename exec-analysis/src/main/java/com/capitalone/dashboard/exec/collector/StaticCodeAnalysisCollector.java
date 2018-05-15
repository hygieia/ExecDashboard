package com.capitalone.dashboard.exec.collector;

import com.capitalone.dashboard.exec.model.*;
import com.capitalone.dashboard.exec.repository.PortfolioMetricRepository;
import org.apache.commons.collections.CollectionUtils;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import scala.collection.JavaConversions;
import scala.collection.mutable.WrappedArray;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

@Component
public class StaticCodeAnalysisCollector extends DefaultMetricCollector {

    @Autowired
    public StaticCodeAnalysisCollector(PortfolioMetricRepository portfolioMetricRepository) {
        super(portfolioMetricRepository);
    }

    @Override
    protected MetricType getMetricType() {
        return MetricType.STATIC_CODE_ANALYSIS;
    }

    @Override
    protected String getQuery() {
        return HygieiaSparkQuery.STATIC_CODE_ANALYSIS_QUERY_ALL_COLLECTOR_ITEMS;
    }

    @Override
    protected String getCollection() {
        return "code_quality";
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

    private void updateCollectorItemMetricDetail(CollectorItemMetricDetail collectorItemMetricDetail, Row itemRow) {
        Date timeWindowDt = itemRow.getAs("timeWindow");
        List<String> scaMetricList = Arrays.asList("blocker_violations", "critical_violations", "major_violations", "sqale_index");
        Collection<Object> javaCollection = JavaConversions.asJavaCollection(((WrappedArray) itemRow.getAs("metrics")).toList());

        Optional.ofNullable(javaCollection)
                .orElseGet(Collections::emptyList)
                .forEach(m -> {
                    GenericRowWithSchema genericRowWithSchema = (GenericRowWithSchema) m;
                    String existingLabelName = genericRowWithSchema.getAs("name");
                    if (scaMetricList.contains(existingLabelName)) {
                        double value = genericRowWithSchema.getAs("value");
                        MetricCount mc = getMetricCount("", value, existingLabelName);
                        if (mc != null) {
                            collectorItemMetricDetail.setStrategy(getCollectionStrategy());
                            collectorItemMetricDetail.addCollectorItemMetricCount(timeWindowDt, mc);
                            collectorItemMetricDetail.setLastScanDate(timeWindowDt);
                        }
                    }
                });
    }

    @Override
    protected CollectorType getCollectorType() {
        return CollectorType.CodeQuality;
    }

    @Override
    protected MetricCount getMetricCount(String level, double value, String labelName) {
        MetricCount mc = new MetricCount();
        Map<String, String> label = new HashMap<>();
        String metricLabel = getMetricLabel(labelName);
        if(metricLabel != null) {
            label.put("type", metricLabel);
            mc.setLabel(label);
            mc.setValue(value);
            return mc;
        }
        return null;
    }

    private String getMetricLabel(String inputMetricLabel) {
        if(inputMetricLabel.equals("blocker_violations")) {
            return "blocker";
        } else if (inputMetricLabel.equals("critical_violations")) {
            return "critical";
        } else if (inputMetricLabel.equals("major_violations")) {
            return "major";
        } else if (inputMetricLabel.equals("sqale_index")) {
            return "technical-debt";
        }
        return null;
    }
}
