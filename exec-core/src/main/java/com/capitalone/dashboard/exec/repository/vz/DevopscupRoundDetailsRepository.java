package com.capitalone.dashboard.exec.repository.vz;

import org.bson.types.ObjectId;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.capitalone.dashboard.exec.model.vz.DevopscupRoundDetails;

/**
 * Interface DevopscupRoundDetailsRepository
 * gingAndSortingRepository<DevopscupRoundDetails, ObjectId>
 * 
 * @author rhe94mg
 */
public interface DevopscupRoundDetailsRepository extends PagingAndSortingRepository<DevopscupRoundDetails, ObjectId> {
	/**
	 * 
	 * @param active
	 * @return DevopscupRoundDetails
	 */
	DevopscupRoundDetails getByActive(Boolean active);
}
