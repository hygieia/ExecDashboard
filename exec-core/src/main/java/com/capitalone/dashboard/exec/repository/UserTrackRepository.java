package com.capitalone.dashboard.exec.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.capitalone.dashboard.exec.model.UserTrack;

/**
 * @author V143611
 *
 */
public interface UserTrackRepository extends PagingAndSortingRepository<UserTrack, ObjectId> {

	/**
	 * findByUserEid
	 * 
	 * @param userEid
	 *            return UserTrack
	 */
	UserTrack findByUserEid(String userEid);

	/**
	 * getByTimeStamp
	 * 
	 * @param startTimeStamp
	 * @param endTimeStamp
	 *            return List<UserTrack>
	 */
	@Query(value = " {'logginTime' : {$gt: ?0, $lte: ?1}}")
	List<UserTrack> getByTimeStamp(long startTimeStamp, long endTimeStamp);

	/**
	 * getByTimeStampAndEid
	 * 
	 * @param timeStamp
	 * @param executiveIds
	 *            return List<UserTrack>
	 */
	@Query(value = " {'logginTime' : {$gte: ?0}, 'userEid' : {$in : ?1}}")
	List<UserTrack> getByTimeStampAndEid(long timeStamp, List<String> executiveIds);

	/**
	 * findTop30ByOrderByLogginTimeDesc
	 * 
	 * @return List<UserTrack>
	 */
	List<UserTrack> findTop30ByOrderByLogginTimeDesc();

}
