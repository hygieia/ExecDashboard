package com.capitalone.dashboard.exec.repository.vz;

import org.bson.types.ObjectId;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.capitalone.dashboard.exec.model.vz.HygieiaArtifactDetails;

/**
 * HygieiaArtifactDetailsRepository interface extends PagingAndSortingRepository
 * 
 * @author RHE94MG
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
