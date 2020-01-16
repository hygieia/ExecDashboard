package com.capitalone.dashboard.exec.collector;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.exec.model.CollectorItemMetricDetail;
import com.capitalone.dashboard.exec.model.CollectorType;
import com.capitalone.dashboard.exec.model.HygieiaSparkQuery;
import com.capitalone.dashboard.exec.model.MetricCollectionStrategy;
import com.capitalone.dashboard.exec.model.MetricCount;
import com.capitalone.dashboard.exec.model.MetricType;
import com.capitalone.dashboard.exec.repository.PortfolioMetricRepository;

import scala.collection.JavaConversions;
import scala.collection.mutable.WrappedArray;

@Component
public class PipelineCollector extends DefaultMetricCollector {

    private static final Logger LOGGER = LoggerFactory.getLogger(PipelineCollector.class);

    public PipelineCollector(PortfolioMetricRepository portfolioMetricRepository, PortfolioCollectorSetting portfolioCollectorSetting) {
        super(portfolioMetricRepository, portfolioCollectorSetting);
    }

    @Override
    public MetricType getMetricType() {
        return MetricType.PIPELINE_LEAD_TIME;
    }

    @Override
    public String getQuery() {
        return HygieiaSparkQuery.PIPELINE_QUERY_BY_COLLECTOR_ITEMS_ID;
    }

    @Override
    public String getCollection() {
        return "pipelines";
    }

    @Override
    protected CollectorType getCollectorType() {
        return CollectorType.Product;
    }
    @Override
    protected MetricCollectionStrategy getCollectionStrategy() {
        return MetricCollectionStrategy.AVERAGE;
    }

    @Override
    protected CollectorItemMetricDetail getCollectorItemMetricDetail(List<Row> rowList, MetricType metricType) {
        CollectorItemMetricDetail collectorItemMetricDetail = new CollectorItemMetricDetail();
        if (CollectionUtils.isEmpty(rowList)) {
            return collectorItemMetricDetail;
        }
        collectorItemMetricDetail.setType(metricType);
        for (Row row : rowList) {
            updateCollectorItemMetricDetail(collectorItemMetricDetail, row);
        }
        return collectorItemMetricDetail;
    }

    private void updateCollectorItemMetricDetail(CollectorItemMetricDetail collectorItemMetricDetail, Row itemRow) {
        Date timeWindowDt = itemRow.getAs("timeWindow");
        LOGGER.info("TimeWindow:" +timeWindowDt );
        LOGGER.info("itemRow :" + itemRow);
        Collection<Object> javaCollection = JavaConversions.asJavaCollection(((WrappedArray) itemRow.getAs("prodStageList")).toList());

        Optional.ofNullable(javaCollection)
                .orElseGet(Collections::emptyList).stream().map(m -> (GenericRowWithSchema) m).forEach(genericRowWithSchema -> {
            Long pipelineTimeL = genericRowWithSchema.getAs("timestamp");
            Date dateObj = new Timestamp(new Date(pipelineTimeL).getTime());
            LOGGER.info("Date Object :" + dateObj);
            Long scmTimeL = genericRowWithSchema.getAs("scmCommitTimestamp");
            Long pipelineTimeAfterIgnore = pipelineTimeL/1000;
            Long scmTimeAfterIgnore = scmTimeL/1000;
            try {
                Long diffTimestamp = Math.abs(pipelineTimeAfterIgnore - scmTimeAfterIgnore);
                String strTimestampInsec = Long.toString(diffTimestamp);
                double value = Double.parseDouble(strTimestampInsec);
                MetricCount mc = getMetricCount("", value, "pipeline-lead-time");
                if (mc != null) {
                    collectorItemMetricDetail.setStrategy(getCollectionStrategy());
                    collectorItemMetricDetail.addCollectorItemMetricCount(dateObj, mc);
                    collectorItemMetricDetail.setLastScanDate(dateObj);
                }
            } catch (NumberFormatException e) {
                LOGGER.info("Exception: Not a number, 'value' = " + scmTimeAfterIgnore, e);
            }
        });
    }

    @Override
    protected MetricCount getMetricCount(String level, double value, String name) {
        MetricCount mc = new MetricCount();
        Map<String, String> label = new HashMap<>();
        String metricLabel = getMetricLabel(name);
        if(metricLabel != null) {
            label.put("type", metricLabel);
            mc.setLabel(label);
            mc.setValue(value);
        }
        return mc;
    }

    private String getMetricLabel(String inputMetricLabel) {
        if(StringUtils.equals(inputMetricLabel,"pipeline-lead-time")){
            return "pipeline-lead-time";
        } else {
            return inputMetricLabel;
        }
    }
}
