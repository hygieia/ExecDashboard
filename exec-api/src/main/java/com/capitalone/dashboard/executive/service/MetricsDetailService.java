package com.capitalone.dashboard.executive.service;

import com.capitalone.dashboard.exec.model.MetricLevel;
import com.capitalone.dashboard.exec.model.MetricSummary;
import com.capitalone.dashboard.exec.model.MetricType;
import com.capitalone.dashboard.exec.model.MetricsDetail;

public interface MetricsDetailService {

    MetricSummary getMetricSummary(MetricLevel level, MetricType type, String id);

    MetricSummary getMetricSummaryForProducts(MetricLevel level, MetricType type, String id);

    MetricsDetail getMetricsDetail(MetricLevel level, MetricType type, String id);

    MetricsDetail getMetricsDetailForProducts(MetricLevel level, MetricType type, String id);

}
