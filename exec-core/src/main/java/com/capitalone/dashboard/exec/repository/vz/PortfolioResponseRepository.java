package com.capitalone.dashboard.exec.repository.vz;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.capitalone.dashboard.exec.model.vz.PortfolioResponse;

@Repository
public interface PortfolioResponseRepository extends CrudRepository<PortfolioResponse, ObjectId> {

	@Query(value = "{'eid': ?0}")
	PortfolioResponse findByEidWithSort(String eid, Sort sort);

	@Query(value = "{'eid': ?0}")
	PortfolioResponse findByEid(String eid);

	List<PortfolioResponse> findAll(Sort sort);

	@Query(value = "{'eid': {'$in' : ?0 }}")
	List<PortfolioResponse> getByEidsWithSort(List<String> eids, Sort sort);

	@Query(value = "{'eid': {'$in' : ?0 }}")
	List<PortfolioResponse> getByEids(List<String> eids);

	/**
	 * getNotUpdatedList()
	 * 
	 * @param lastUpdated
	 * @return List<PortfolioResponse>
	 */
	@Query(value = " {'lastUpdated' : {$ne : ?0 }}")
	List<PortfolioResponse> getNotUpdatedList(Long lastUpdated);

}