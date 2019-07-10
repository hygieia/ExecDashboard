package com.capitalone.dashboard.exec.repository.vz;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.capitalone.dashboard.exec.model.vz.VonkinatorDataSet;

/**
 * @author raish4s
 *
 */
public interface VonkinatorDataSetRepository extends PagingAndSortingRepository<VonkinatorDataSet, ObjectId> {

	/**
	 * @param appId
	 * @param period
	 * @return
	 */
	VonkinatorDataSet findByAppIdAndPeriod(String appId, String period);

	/**
	 * @param portfolio
	 * @return
	 */
	List<VonkinatorDataSet> findByPortfolio(String portfolio);

	/**
	 * @param apps
	 * @return
	 */
	@Query(value = "{'appId' :{$in:?0}}")
	List<VonkinatorDataSet> findByAppIds(List<String> apps);

	/**
	 * @param isIT
	 * @return
	 */
	List<VonkinatorDataSet> findByIsIT(boolean isIT);
}
