package com.capitalone.dashboard.exec.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.capitalone.dashboard.exec.model.StashDashboardInfo;

@Repository
public interface StashDashboardInfoRepository extends PagingAndSortingRepository<StashDashboardInfo, ObjectId> {

	@Query(value = " {'appId' : ?0}")
	StashDashboardInfo checkByAppId(String appId);

	@Query(value = " {'appId' : ?0, 'isUnlimitedData' : ?1}")
	StashDashboardInfo checkByAppIdAndIsUnlimitedData(String appId, boolean isUnlimitedData);

	List<StashDashboardInfo> findAll();

}
