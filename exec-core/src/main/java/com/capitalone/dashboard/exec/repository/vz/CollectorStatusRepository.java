package com.capitalone.dashboard.exec.repository.vz;

import org.bson.types.ObjectId;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.capitalone.dashboard.exec.model.vz.CollectorStatus;
import com.capitalone.dashboard.exec.model.vz.CollectorType;

public interface CollectorStatusRepository extends PagingAndSortingRepository<CollectorStatus, ObjectId> {

	CollectorStatus findByType(CollectorType type);

}