package com.capitalone.dashboard.exec.repository.vz;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.capitalone.dashboard.exec.model.vz.ExecutiveSummaryList;

public interface ExecutiveSummaryListRepository extends PagingAndSortingRepository<ExecutiveSummaryList, ObjectId> {

	ExecutiveSummaryList findByEid(String eid);

	List<ExecutiveSummaryList> findByBusinessUnits(String businessUnits);

	@Query(value = " {'seniorExecutive' : true}")
	List<ExecutiveSummaryList> getSeniorExecutives();

	List<ExecutiveSummaryList> findByBusinessUnitsAndSeniorExecutive(String businessUnits, Boolean seniorExecutive);

	List<ExecutiveSummaryList> findBySeniorExecutive(Boolean seniorExecutive);

	@Query(value = " {'eid' : {$in : ?0 }}")
	List<ExecutiveSummaryList> getEids(List<String> reportings);

	/**
	 * getNotUpdatedList()
	 * 
	 * @param lastUpdated
	 * @return List<ExecutiveSummaryList>
	 */
	@Query(value = " {'lastUpdated' : {$ne : ?0 }}")
	List<ExecutiveSummaryList> getNotUpdatedList(Long lastUpdated);

}
