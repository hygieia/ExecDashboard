package com.capitalone.dashboard.exec.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.capitalone.dashboard.exec.model.StashDetailsInfo;

@Repository
public interface StashDetailsInfoRepository extends PagingAndSortingRepository<StashDetailsInfo, ObjectId> {

	@Query(value = " {'appId' : ?0}")
	StashDetailsInfo checkByAppId(String appId);

	@Query(value = " {'appId' : ?0, 'isUnlimitedData' : ?1}")
	StashDetailsInfo checkByAppIdAndIsUnlimitedData(String appId, boolean isUnlimitedData);

	List<StashDetailsInfo> findAll();

}
