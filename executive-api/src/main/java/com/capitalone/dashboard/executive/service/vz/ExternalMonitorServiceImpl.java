package com.capitalone.dashboard.executive.service.vz;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.capitalone.dashboard.exec.model.vz.ExternalSystemMonitor;

/**
 * ExternalMonitorServiceImpl
 * 
 */
@Service
public class ExternalMonitorServiceImpl implements ExternalMonitorService {

	private MongoTemplate mongoTemplate;

	/**
	 * ExternalMonitorServiceImpl
	 * 
	 * @param mongoTemplate
	 * @return
	 */
	@Autowired
	public ExternalMonitorServiceImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public List<ExternalSystemMonitor> getLatestRecord() {
		List<ExternalSystemMonitor> results = new ArrayList<>();
		List<String> distinctsources = mongoTemplate.getCollection("externalmonitor").distinct("sourceSystemName");
		for (String type : distinctsources) {
			Query query = new Query();
			query.addCriteria(new Criteria().where("sourceSystemName").is(type));
			query.with(new Sort(Sort.Direction.DESC, "lastConnectedTime"));
			query.limit(1);
			ExternalSystemMonitor result = mongoTemplate.findOne(query, ExternalSystemMonitor.class);
			results.add(result);
		}
		return results;
	}

}
