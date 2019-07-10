package com.capitalone.dashboard.exec.repository.vz;

import org.bson.types.ObjectId;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.capitalone.dashboard.exec.model.vz.AppsJobDetails;

public interface AppsJobDetailsRepository extends PagingAndSortingRepository<AppsJobDetails, ObjectId> {

	AppsJobDetails findByAppId(String appId);

}
