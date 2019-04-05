package com.capitalone.dashboard.exec.repository.vz;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.capitalone.dashboard.exec.model.vz.DevOpsCupScores;

/**
 * Interface DevOpsCupScoresRepository extends
 * PagingAndSortingRepository<DevOpsCupScores, ObjectId>
 */
public interface DevOpsCupScoresRepository extends PagingAndSortingRepository<DevOpsCupScores, ObjectId> {
	/**
	 * 
	 * @param appList
	 * @return List
	 */
	@Query(value = " {'appId': {$in: ?0}}")
	List<DevOpsCupScores> getDevOpsCupScoresByAppList(String[] appList);

	/**
	 * 
	 * @param portfolio
	 * @return List
	 */
	@Query(value = " {'portfolio': ?0}")
	List<DevOpsCupScores> getDevOpsCupScoresByPortfolio(String portfolio);

	/**
	 * 
	 * @param appId
	 * @return DevOpsCupScores
	 */
	@Query(value = " {'appId': ?0}")
	DevOpsCupScores getDevOpsCupScoresByAppId(String appId);

	/**
	 * 
	 * @param appList
	 * @return List
	 */
	@Query(value = " {'appId': {$in: ?0}}")
	List<DevOpsCupScores> getDevOpsCupScoresByAppLists(List<String> appList);

}
