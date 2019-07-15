package com.capitalone.dashboard.exec.repository;

import org.bson.types.ObjectId;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.capitalone.dashboard.exec.model.Authentication;

public interface AuthenticationRepository extends PagingAndSortingRepository<Authentication, ObjectId> {

	Authentication findByEid(String eid);

}
