package com.capitalone.dashboard.exec.repository;

import org.bson.types.ObjectId;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.capitalone.dashboard.exec.model.CollectorStatus;
import com.capitalone.dashboard.exec.model.CollectorType;

public interface CollectorStatusRepository extends PagingAndSortingRepository<CollectorStatus, ObjectId> {

	CollectorStatus findByType(CollectorType type);

}