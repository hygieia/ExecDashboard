package com.capitalone.dashboard.executive.service.vz;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.capitalone.dashboard.exec.model.vz.Vast;
import com.capitalone.dashboard.exec.repository.vz.VastRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * VastServiceImpl
 */
@Service
public class VastServiceImpl implements VastService {
	private final VastRepository vastRepository;

	private final MongoTemplate mongoTemplate;
	private static final Logger LOG = LoggerFactory.getLogger(VastServiceImpl.class);

	/**
	 * 
	 * @param vastRepository
	 * @param mongoTemplate
	 */
	public VastServiceImpl(VastRepository vastRepository, MongoTemplate mongoTemplate) {
		this.vastRepository = vastRepository;
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public Map<String, Vast> getVastForDevopscupApps() {

		HashMap<String, Vast> hashMap = new HashMap<>();
		try {
			List<String> devopscupAppList = mongoTemplate.getCollection("devopscup_scores").distinct("appId");
			List<Vast> vastDetailsList = vastRepository.getVastByAppIdList(devopscupAppList);
			for (Vast vastDetail : vastDetailsList) {
				hashMap.put(vastDetail.getVastApplID(), vastDetail);
			}
		} catch (Exception ex) {
			LOG.error("Exception occurred  in getVastForDevopscupApps :: " + ex);
		}
		return hashMap;

	}

}
