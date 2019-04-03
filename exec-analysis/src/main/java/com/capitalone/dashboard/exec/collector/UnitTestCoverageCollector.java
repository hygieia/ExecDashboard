package com.capitalone.dashboard.exec.collector;

import com.capitalone.dashboard.exec.model.HygieiaSparkQuery;
import com.capitalone.dashboard.exec.model.MetricType;
import com.capitalone.dashboard.exec.model.MetricCollectionStrategy;
import com.capitalone.dashboard.exec.model.CollectorItemMetricDetail;
import com.capitalone.dashboard.exec.model.CollectorType;
import com.capitalone.dashboard.exec.model.MetricCount;
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

import java.util.List;
import java.util.Collection;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Date;
import java.util.HashMap;

@Component
public class UnitTestCoverageCollector extends DefaultMetricCollector {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnitTestCoverageCollector.class);

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
                            String valueStr = genericRowWithSchema.getAs("value");
                            try{
                            double value = Double.parseDouble(valueStr);
                            MetricCount mc = getMetricCount("", value, "unit-test-coverage");
                            if (mc != null) {
                                collectorItemMetricDetail.setStrategy(getCollectionStrategy());
                                collectorItemMetricDetail.addCollectorItemMetricCount(timeWindowDt, mc);
                                collectorItemMetricDetail.setLastScanDate(timeWindowDt);
                            }
                            }catch (Exception e){
                                LOGGER.info("Exception: Not a number, 'value' = "+valueStr,e);
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

    @Override
    protected boolean isCollectByCollectorItem() {
        return true;
    }
}
