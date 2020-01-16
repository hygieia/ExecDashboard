package com.capitalone.dashboard.exec.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.capitalone.dashboard.exec.model.CollectorType;
import com.capitalone.dashboard.exec.model.CollectorUpdatedDetails;

/**
 * interface CollectorUpdatedDetailsRepository
 * 
 *
 *
 */
public interface CollectorUpdatedDetailsRepository
		extends PagingAndSortingRepository<CollectorUpdatedDetails, ObjectId> {
	/**
	 * findByCollectionNameAndType
	 * 
	 * @param collectionName
	 * @param type
	 * @return
	 */
	CollectorUpdatedDetails findByCollectionNameAndType(String collectionName, CollectorType type);

	/**
	 * findByType
	 * 
	 * @param type
	 * @return
	 */
	List<CollectorUpdatedDetails> findByType(CollectorType type);

	@Query(value = "{ 'type' : {$ne : ?0} }")
	/**
	 * findByOtherType
	 * 
	 * @param type
	 * @return
	 */
	List<CollectorUpdatedDetails> findByOtherType(CollectorType type);

	/**
	 * findByCollectionNameAndTypeOrderByCollectorStartTimeDesc
	 * 
	 * @param metricsName
	 * @param metricsprocessor
	 * @return
	 */
	CollectorUpdatedDetails findByCollectionNameAndTypeOrderByCollectorStartTimeDesc(String metricsName,
			CollectorType metricsprocessor);
	/**
	 * findByCollectionNameAndTypeOrderByCollectorUpdateTimeDesc
	 * @param metricsName
	 * @param metricsprocessor
	 * @return
	 */
	CollectorUpdatedDetails findByCollectionNameAndTypeOrderByCollectorUpdateTimeDesc(String metricsName, CollectorType metricsprocessor);

	/**
	 * findDistinctCollectionNameByType
	 * 
	 * @param metricsprocessor
	 * @return
	 */
	List<String> findDistinctCollectionNameByType(CollectorType metricsprocessor);

}
