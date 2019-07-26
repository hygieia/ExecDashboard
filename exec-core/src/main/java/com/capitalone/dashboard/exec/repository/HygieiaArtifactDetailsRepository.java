package com.capitalone.dashboard.exec.repository;

import org.bson.types.ObjectId;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.capitalone.dashboard.exec.model.HygieiaArtifactDetails;

/**
 * HygieiaArtifactDetailsRepository interface extends PagingAndSortingRepository
 *
 *
 */
public interface HygieiaArtifactDetailsRepository extends PagingAndSortingRepository<HygieiaArtifactDetails, ObjectId> {
	/**
	 * 
	 * @param artifactName
	 * @return HygieiaArtifactDetails
	 */
	HygieiaArtifactDetails findByArtifactName(String artifactName);

}
