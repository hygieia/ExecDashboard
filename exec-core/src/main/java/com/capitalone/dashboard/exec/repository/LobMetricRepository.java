package com.capitalone.dashboard.exec.repository;

import com.capitalone.dashboard.exec.model.LobMetricDetail;
import com.capitalone.dashboard.exec.model.MetricType;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LobMetricRepository
        extends CrudRepository<LobMetricDetail, ObjectId>, QueryDslPredicateExecutor<LobMetricDetail> {

    LobMetricDetail findByNameAndType(String name, MetricType type);
    List<LobMetricDetail> findByName(String name);

    @Query(value="{ 'name' : ?0, 'type': ?1, 'productMetricDetailList.name': ?2}")
    LobMetricDetail findByNameAndTypeAndProductName(String name, MetricType type, String productName);

    @Query(value="{ 'name' : ?0, 'type': ?1, 'productMetricDetailList.name': ?2}, 'productMetricDetailList.componentMetricDetailList.name' ?3 ")
    LobMetricDetail findByNameAndTypeAndProductNameAndComponentName(String name, MetricType type, String productName, String componentName);

    void deleteAllByType(MetricType type);
}
