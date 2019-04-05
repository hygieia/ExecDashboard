package com.capitalone.dashboard.exec.repository.vz;

import org.bson.types.ObjectId;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.capitalone.dashboard.exec.model.vz.Authentication;

public interface AuthenticationRepository extends PagingAndSortingRepository<Authentication, ObjectId> {

	Authentication findByEid(String eid);

}
