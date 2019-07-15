package com.capitalone.dashboard.exec.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.capitalone.dashboard.exec.model.Instance;

/**
 * InstanceRepository extends CrudRepository
 * 
 * @author RHE94MG
 *
 */
public interface InstanceRepository extends CrudRepository<Instance, ObjectId> {
	/**
	 * 
	 * @param appId
	 * @return
	 */
	Instance findByappId(String appId);

	/**
	 * 
	 * @param appId
	 * @param active
	 * @return
	 */
	Instance findByAppIdAndActive(String appId, boolean active);

	/**
	 * 
	 * @param privateIp
	 * @return
	 */
	Instance findByPrivateIp(String privateIp);

	/**
	 * 
	 * @param appId
	 * @return
	 */
	@Query(value = "{ 'appId' : ?0}")
	List<Instance> getByAppId(String appId);

	/**
	 * 
	 * @param protfolio
	 * @return
	 */
	List<Instance> findByProtfolio(String protfolio);

	/**
	 * 
	 * @param protfolio
	 * @param active
	 * @return
	 */
	List<Instance> findByProtfolioAndActive(String protfolio, boolean active);

	/**
	 * 
	 * @param instance
	 * @param active
	 * @return
	 */
	Instance findByPrivateIpAndActive(String instance, boolean active);

}
