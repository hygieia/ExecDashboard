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
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SCMCollector extends DefaultMetricCollector {

    public SCMCollector(PortfolioMetricRepository portfolioMetricRepository) {
        super(portfolioMetricRepository);
    }

    @Override
    public MetricType getMetricType() {
        return MetricType.SCM_COMMITS;
    }

    @Override
    public String getQuery() {
        return HygieiaSparkQuery.SCM_COMMITS_QUERY;
    }

    @Override
    public String getCollection() {
        return "commits";
    }

    @Override
    protected CollectorType getCollectorType() {
        return CollectorType.SCM;
    }
    @Override
    protected MetricCollectionStrategy getCollectionStrategy() {
        return MetricCollectionStrategy.CUMULATIVE;
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
        Collection<Object> javaCollection = new ArrayList<>();
        javaCollection.add((itemRow.getAs("count")));

        javaCollection.forEach(m -> {
                    double value = (double) m;
                    MetricCount mc = getMetricCount("", value, "scm-commits");

                    if (mc != null) {
                        collectorItemMetricDetail.setStrategy(getCollectionStrategy());
                        collectorItemMetricDetail.addCollectorItemMetricCount(timeWindowDt, mc);
                        collectorItemMetricDetail.setLastScanDate(timeWindowDt);
                    }
                });
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
