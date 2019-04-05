package com.capitalone.dashboard.exec.repository.vz;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.capitalone.dashboard.exec.model.vz.ExecutiveHierarchy;

public interface ExecutiveHierarchyRepository extends PagingAndSortingRepository<ExecutiveHierarchy, ObjectId> {

	ExecutiveHierarchy findByEid(String eid);

	/**
	 * getNotUpdatedList()
	 * 
	 * @param lastUpdated
	 * @return List<ExecutiveHierarchy>
	 */
	@Query(value = " {'lastUpdated' : {$ne : ?0 }}")
	List<ExecutiveHierarchy> getNotUpdatedList(Long lastUpdated);

}
