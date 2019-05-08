package com.capitalone.dashboard.exec.collector;

import com.capitalone.dashboard.exec.model.CollectorItemMetricDetail;
import com.capitalone.dashboard.exec.model.CollectorType;
import com.capitalone.dashboard.exec.model.HygieiaSparkQuery;
import com.capitalone.dashboard.exec.model.MetricCollectionStrategy;
import com.capitalone.dashboard.exec.model.MetricCount;
import com.capitalone.dashboard.exec.model.MetricType;
import com.capitalone.dashboard.exec.repository.PortfolioMetricRepository;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import scala.collection.JavaConversions;
import scala.collection.mutable.WrappedArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;


@Component
public class LibraryPolicyCollector extends DefaultMetricCollector{

    @Autowired
    public LibraryPolicyCollector(PortfolioMetricRepository portfolioMetricRepository) {
        super(portfolioMetricRepository);
    }

    @Override
    public MetricType getMetricType() {
        return MetricType.OPEN_SOURCE_VIOLATIONS;
    }

    @Override
    public String getQuery() {
        return HygieiaSparkQuery.OPEN_SOURCE_QUERY_ALL_COLLECTOR_ITEMS;
    }

    @Override
    public String getCollection() {
        return "library_policy";
    }

    @Override
    protected CollectorType getCollectorType() {
        return CollectorType.LibraryPolicy;
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
        collectorItemMetricDetail.setType(metricType);
        for (Row row : rowList) {
            updateCollectorItemMetricDetail(collectorItemMetricDetail, row, "license");
            updateCollectorItemMetricDetail(collectorItemMetricDetail, row, "security");
        }
        return collectorItemMetricDetail;
    }

    private void updateCollectorItemMetricDetail(CollectorItemMetricDetail collectorItemMetricDetail, Row itemRow, String type) {
        Date timeWindowDt = itemRow.getAs("timeWindow");
        Collection<Object> javaCollection = JavaConversions.asJavaCollection(((WrappedArray) itemRow.getAs(type)).toList());

        Optional.ofNullable(javaCollection)
            .orElseGet(Collections::emptyList)
            .forEach(m -> {
                GenericRowWithSchema genericRowWithSchema = (GenericRowWithSchema) m;
                String level = genericRowWithSchema.getAs("level");
                int value = genericRowWithSchema.getAs("count");
                MetricCount mc = getMetricCount(level, value, type);
                if (mc != null) {
                    collectorItemMetricDetail.setStrategy(getCollectionStrategy());
                    collectorItemMetricDetail.addCollectorItemMetricCount(timeWindowDt, mc);
                    collectorItemMetricDetail.setLastScanDate(timeWindowDt);
                }
            });
    }

    @Override
    protected MetricCount getMetricCount(String level, double value, String type) {
        if (StringUtils.isEmpty(level) || "None".equalsIgnoreCase(level)) {
            return null;
        }
        MetricCount mc = new MetricCount();
        Map<String, String> label = new HashMap<>();
        label.put("severity", level);
        label.put("type", type);
        mc.setLabel(label);
        mc.setValue(value);
        return mc;
    }
}
