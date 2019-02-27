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
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import scala.collection.JavaConversions;
import scala.collection.mutable.WrappedArray;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class SecurityCollector extends DefaultMetricCollector {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityCollector.class);

    @Autowired
    public SecurityCollector(PortfolioMetricRepository portfolioMetricRepository) {
        super(portfolioMetricRepository);
    }

    @Override
    protected MetricType getMetricType() {
        return MetricType.SECURITY_VIOLATIONS;
    }

    @Override
    protected String getQuery() {
        return HygieiaSparkQuery.SECURITY_ANALYSIS_QUERY_ALL_COLLECTOR_ITEMS;
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
        List<String> metricList = Arrays.asList("High", "Medium", "Low");
        Collection<Object> javaCollection = JavaConversions.asJavaCollection(((WrappedArray) itemRow.getAs("metrics")).toList());

        Optional.ofNullable(javaCollection)
                .orElseGet(Collections::emptyList)
                .forEach(m -> {
                    GenericRowWithSchema genericRowWithSchema = (GenericRowWithSchema) m;
                    String existingLabelName = genericRowWithSchema.getAs("name");
                    if (metricList.contains(existingLabelName)) {
                        String valueStr = genericRowWithSchema.getAs("value");
                        try {
                            double value = Double.parseDouble(valueStr);
                            MetricCount mc = getMetricCount("", value, existingLabelName);
                            if (mc != null) {
                                collectorItemMetricDetail.setStrategy(getCollectionStrategy());
                                collectorItemMetricDetail.addCollectorItemMetricCount(timeWindowDt, mc);
                                collectorItemMetricDetail.setLastScanDate(timeWindowDt);
                            }
                        } catch (NumberFormatException e) {
                            LOGGER.info("Exception: Not a number, 'value' = "+valueStr,e);
                        }
                    }
                });
    }

    @Override
    protected CollectorType getCollectorType() {
        return CollectorType.StaticSecurityScan;
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
        switch (inputMetricLabel){
            case "High":
                return "blocker";
            case "Medium":
                return "critical";
            case "Low":
                return "major";
            default:
                return null;
        }
    }
}
