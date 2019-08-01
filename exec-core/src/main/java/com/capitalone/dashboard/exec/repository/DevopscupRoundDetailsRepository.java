package com.capitalone.dashboard.exec.repository;

import org.bson.types.ObjectId;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.capitalone.dashboard.exec.model.DevopscupRoundDetails;

/**
 * Interface DevopscupRoundDetailsRepository
 * gingAndSortingRepository<DevopscupRoundDetails, ObjectId>
 * 
 *
 */
public interface DevopscupRoundDetailsRepository extends PagingAndSortingRepository<DevopscupRoundDetails, ObjectId> {
	/**
	 * 
	 * @param active
	 * @return DevopscupRoundDetails
	 */
	DevopscupRoundDetails getByActive(Boolean active);
}
