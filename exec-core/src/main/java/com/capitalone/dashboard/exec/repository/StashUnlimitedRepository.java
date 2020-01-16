package com.capitalone.dashboard.exec.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.capitalone.dashboard.exec.model.StashUnlimitedInfo;

@Repository
public interface StashUnlimitedRepository extends PagingAndSortingRepository<StashUnlimitedInfo, ObjectId> {

	@Query(value = " {'appId' : ?0}")
	StashUnlimitedInfo checkByAppId(String appId);

	@Query(value = " {'appId' : ?0, 'isUnlimitedData' : ?1}")
	StashUnlimitedInfo checkByAppIdAndIsUnlimitedData(String appId, boolean isUnlimitedData);

	List<StashUnlimitedInfo> findAll();

}
