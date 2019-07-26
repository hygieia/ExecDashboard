package com.capitalone.dashboard.exec.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.capitalone.dashboard.exec.model.VonkinatorPeriod;

/**
 *
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
