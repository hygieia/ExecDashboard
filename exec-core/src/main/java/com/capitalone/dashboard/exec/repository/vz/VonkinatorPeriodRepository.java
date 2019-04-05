package com.capitalone.dashboard.exec.repository.vz;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.capitalone.dashboard.exec.model.vz.VonkinatorPeriod;

/**
 * @author RAISH4S
 *
 */
public interface VonkinatorPeriodRepository extends PagingAndSortingRepository<VonkinatorPeriod, ObjectId> {

	/**
	 * @param period
	 * @return
	 */
	List<VonkinatorPeriod> findByPeriod(String period);

	/**
	 * @param active
	 * @return
	 */
	List<VonkinatorPeriod> findByActive(Boolean active);

}
