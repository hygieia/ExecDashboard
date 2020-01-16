package com.capitalone.dashboard.exec.repository;

import org.bson.types.ObjectId;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.capitalone.dashboard.exec.model.AppsJobDetails;

public interface AppsJobDetailsRepository extends PagingAndSortingRepository<AppsJobDetails, ObjectId> {

	AppsJobDetails findByAppId(String appId);

}
