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

import java.util.*;

@Component
public class UnitTestCoverageCollector extends DefaultMetricCollector {

    @Autowired
    public UnitTestCoverageCollector(PortfolioMetricRepository portfolioMetricRepository) {
        super(portfolioMetricRepository);
    }

    @Override
    protected MetricType getMetricType() {
        return MetricType.UNIT_TEST_COVERAGE;
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
        List<String> scaMetricList = Arrays.asList("coverage");
        Collection<Object> javaCollection = JavaConversions.asJavaCollection(((WrappedArray) itemRow.getAs("metrics")).toList());

        Optional.ofNullable(javaCollection)
                .orElseGet(Collections::emptyList)
                .forEach(m -> {
                    GenericRowWithSchema genericRowWithSchema = (GenericRowWithSchema) m;
                    String existingLabelName = genericRowWithSchema.getAs("name");
                    if (scaMetricList.contains(existingLabelName)) {
                        double value = genericRowWithSchema.getAs("value");
                        MetricCount mc = getMetricCount("", value, "unit-test-coverage");
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
    protected MetricCount getMetricCount(String level, double value, String type) {
        MetricCount mc = new MetricCount();
        Map<String, String> label = new HashMap<>();
        label.put("type", type);
        mc.setLabel(label);
        mc.setValue(value);
        return mc;
    }
}
