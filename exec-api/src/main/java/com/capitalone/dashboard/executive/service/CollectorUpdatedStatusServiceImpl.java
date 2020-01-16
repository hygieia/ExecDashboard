package com.capitalone.dashboard.executive.service;

import com.capitalone.dashboard.exec.model.CollectorType;
import com.capitalone.dashboard.exec.model.CollectorUpdatedDetails;
import com.capitalone.dashboard.exec.model.CollectorUpdatedStatus;
import com.capitalone.dashboard.exec.repository.CollectorUpdatedDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
/**
 * Collector Updated Status Service Impl
 *
 *
 */
public class CollectorUpdatedStatusServiceImpl implements CollectorUpdatedStatusService {

	private final CollectorUpdatedDetailsRepository collectorUpdatedDetailsRepository;
	private final MongoTemplate mongoTemplate;

	@Autowired
	/**
	 * CollectorUpdatedStatusServiceImpl
	 * 
	 * @param collectorUpdatedDetailsRepository
	 */
	public CollectorUpdatedStatusServiceImpl(CollectorUpdatedDetailsRepository collectorUpdatedDetailsRepository,
			MongoTemplate mongoTemplate) {
		this.collectorUpdatedDetailsRepository = collectorUpdatedDetailsRepository;
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	/**
	 * getCollectorUpdatedTimestamp
	 * 
	 * @param metricType
	 *            return List<CollectorUpdatedStatus>
	 */
	public List<CollectorUpdatedStatus> getCollectorUpdatedTimestamp(String metricType) {
		List<CollectorUpdatedStatus> collectorDetList = new ArrayList<>();
		List<CollectorUpdatedDetails> collectorUpdatedDetailsList;

		if (metricType.equalsIgnoreCase(CollectorType.MetricsProcessor.toString())) {

			List<String> collectors = mongoTemplate.getCollection("collector_updated_details")
					.distinct("collectionName");

			for (String collectionName : collectors) {
				CollectorUpdatedDetails collectorCurrentDetails = collectorUpdatedDetailsRepository
						.findByCollectionNameAndTypeOrderByCollectorStartTimeDesc(collectionName,
								CollectorType.MetricsProcessor);

				CollectorUpdatedDetails collectorUpdatedDetails = collectorUpdatedDetailsRepository
						.findByCollectionNameAndTypeOrderByCollectorUpdateTimeDesc(collectionName,
								CollectorType.MetricsProcessor);

				if (collectorUpdatedDetails != null) {
					CollectorUpdatedStatus collectorUpdStatus = new CollectorUpdatedStatus();
					collectorUpdStatus.setCollectionName(collectorUpdatedDetails.getCollectionName());
					collectorUpdStatus.setCollectionUpdatedTime(collectorUpdatedDetails.getCollectionUpdatedTime());
					collectorUpdStatus.setCollectorUpdateTime(collectorUpdatedDetails.getCollectorUpdateTime());
					collectorUpdStatus.setType(collectorUpdatedDetails.getType());
					collectorUpdStatus.setAppCount(collectorUpdatedDetails.getAppCount());
					collectorUpdStatus.setDuration(collectorUpdatedDetails.getTotalExecutionTime());
					collectorUpdStatus.setCollectorStartTime(collectorUpdatedDetails.getCollectorStartTime());
					if (collectorCurrentDetails != null) {
						collectorUpdStatus.setIsRunning(collectorCurrentDetails.getIsRunning());
						collectorUpdStatus.setLastStartTime(collectorCurrentDetails.getCollectorStartTime());
					}
					collectorDetList.add(collectorUpdStatus);
				}
			}
		} else {
			collectorUpdatedDetailsList = collectorUpdatedDetailsRepository
					.findByOtherType(CollectorType.MetricsProcessor);

			for (CollectorUpdatedDetails collectorUpdatedDetails : collectorUpdatedDetailsList) {
				CollectorUpdatedStatus collectorUpdStatus = new CollectorUpdatedStatus();
				collectorUpdStatus.setCollectionName(collectorUpdatedDetails.getCollectionName());
				collectorUpdStatus.setCollectionUpdatedTime(collectorUpdatedDetails.getCollectionUpdatedTime());
				collectorUpdStatus.setCollectorUpdateTime(collectorUpdatedDetails.getCollectorUpdateTime());
				collectorUpdStatus.setType(collectorUpdatedDetails.getType());
				collectorUpdStatus.setAppCount(collectorUpdatedDetails.getAppCount());
				collectorUpdStatus.setDuration(collectorUpdatedDetails.getTotalExecutionTime());
				collectorUpdStatus.setCollectorStartTime(collectorUpdatedDetails.getCollectorStartTime());
				collectorDetList.add(collectorUpdStatus);
			}
		}

		return collectorDetList;
	}

}
