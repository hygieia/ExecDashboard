package com.capitalone.dashboard.exec.collector;

import com.capitalone.dashboard.exec.model.*;
import com.capitalone.dashboard.exec.repository.PortfolioMetricRepository;
import org.apache.commons.collections.CollectionUtils;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import scala.collection.JavaConversions;
import scala.collection.Seq;
import scala.collection.mutable.WrappedArray;

import java.util.*;

@Component
public class AuditResultCollector extends DefaultMetricCollector {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditResultCollector.class);
    public AuditResultCollector(PortfolioMetricRepository portfolioMetricRepository) {
        super(portfolioMetricRepository);
    }

    @Override
    protected MetricType getMetricType() {
        return MetricType.TRACEABILITY;
    }

    @Override
    protected String getQuery() {
        return HygieiaSparkQuery.TRACEABILITY_QUERY_ALL_COLLECTOR_ITEMS;
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
            System.out.println(("Checking row list++++++++"));
            return collectorItemMetricDetail;
        }
        collectorItemMetricDetail.setType(getMetricType());
        for (Row row : rowList) {
            updateCollectorItemMetricDetail(collectorItemMetricDetail, row);
        }
        return collectorItemMetricDetail;
    }

    private void updateCollectorItemMetricDetail(CollectorItemMetricDetail collectorItemMetricDetail,Row itemRow){

        System.out.println("+++++++++++++++++++++++++++++"+itemRow);


        Date timeWindowDt = itemRow.getAs("timeWindow");
        List<String> traceability = Arrays.asList("Automated","Manual");
        GenericRowWithSchema javaCollection = (((GenericRowWithSchema) itemRow.getAs("traceability")));
        System.out.println("javaCollection");

        GenericRowWithSchema Manual = (((GenericRowWithSchema) javaCollection.getAs("Manual")));
        String valueStr = Manual.getAs("percentTraceability");
        try {
            double value = Double.parseDouble(valueStr);
            MetricCount mc = getMetricCount("", value, "manual");
            if (mc != null) {
                collectorItemMetricDetail.setStrategy(getCollectionStrategy());
                collectorItemMetricDetail.addCollectorItemMetricCount(timeWindowDt, mc);
                collectorItemMetricDetail.setLastScanDate(timeWindowDt);
            }
        } catch (NumberFormatException e) {
            LOGGER.info("Exception: Not a number, 'value' = "+valueStr,e);
        }
        GenericRowWithSchema Automated = (((GenericRowWithSchema) javaCollection.getAs("Automated")));
        String valueS = Automated.getAs("percentTraceability");
        try {
            double value = Double.parseDouble(valueS);
            MetricCount mc = getMetricCount("", value, "automated");
            if (mc != null) {
                collectorItemMetricDetail.setStrategy(getCollectionStrategy());
                collectorItemMetricDetail.addCollectorItemMetricCount(timeWindowDt, mc);
                collectorItemMetricDetail.setLastScanDate(timeWindowDt);
            }
        } catch (NumberFormatException e) {
            LOGGER.info("Exception: Not a number, 'value' = "+valueStr,e);
        }
    }


    @Override
    protected CollectorType getCollectorType() {
        return CollectorType.CodeQuality;
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
