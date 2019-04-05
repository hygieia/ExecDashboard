package com.capitalone.dashboard.exec.repository.vz;

import org.bson.types.ObjectId;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.capitalone.dashboard.exec.model.vz.MetricDetailResponse;

public interface MetricDetailResponseRepository extends PagingAndSortingRepository<MetricDetailResponse, ObjectId> {

	MetricDetailResponse findByAppIdAndMetricsName(String appId, String metricsName);

	MetricDetailResponse findByAppId(String appId);

}
