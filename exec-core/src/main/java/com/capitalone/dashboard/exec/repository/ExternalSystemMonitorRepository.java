package com.capitalone.dashboard.exec.repository;

import java.util.List;

import org.bson.types.ObjectId;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.capitalone.dashboard.exec.model.ExternalSystemMonitor;

@Repository
public interface ExternalSystemMonitorRepository extends MongoRepository<ExternalSystemMonitor, ObjectId> {

	/**
	 * @return
	 */
	List<ExternalSystemMonitor> findAll();
}
