package com.capitalone.dashboard.executive.service;

import com.capitalone.dashboard.exec.model.VonkinatorDataSet;
import com.capitalone.dashboard.exec.repository.VonkinatorDataSetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 * VonkinatorServiceImpl class
 *
 */
@Service
public class VonkinatorServiceImpl implements VonkinatorService {

	private final VonkinatorDataSetRepository vonkinatorDataSetRepository;
	private final MongoTemplate mongoTemplate;

	private static final String ALL = "ALL";
	private static final Logger LOG = LoggerFactory.getLogger(VonkinatorServiceImpl.class);

	/**
	 * 
	 * @param vonkinatorDataSetRepository
	 * @param mongoTemplate
	 */
	@Autowired
	public VonkinatorServiceImpl(VonkinatorDataSetRepository vonkinatorDataSetRepository, MongoTemplate mongoTemplate) {
		this.vonkinatorDataSetRepository = vonkinatorDataSetRepository;
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public List<VonkinatorDataSet> getVonkinatorDataSetForPortfolio(String portfolio) {

		if (ALL.equalsIgnoreCase(portfolio)) {
			return getVonkinatorAllDataSet();
		}

		List<VonkinatorDataSet> vonkinatorDataSet = vonkinatorDataSetRepository.findByPortfolio(portfolio);
		try {
			if (vonkinatorDataSet != null) {
				return vonkinatorDataSet.stream().sorted(
						Comparator.comparing(VonkinatorDataSet::getAppId).thenComparing(VonkinatorDataSet::getOrder))
						.collect(Collectors.toList());
			}
		} catch (Exception e) {
			LOG.error("Exception in Vonkinator getVonkinatorDataSetForPortfolio:: " + e);
		}
		return vonkinatorDataSet;
	}

	@Override
	public List<VonkinatorDataSet> getVonkinatorAllDataSet() {
		List<String> appIds = mongoTemplate.getCollection("app_details").distinct("appId");
		List<VonkinatorDataSet> vonkinatorDataSet = vonkinatorDataSetRepository.findByAppIds(appIds);
		try {
			if (vonkinatorDataSet != null) {
				return vonkinatorDataSet.stream().sorted(
						Comparator.comparing(VonkinatorDataSet::getAppId).thenComparing(VonkinatorDataSet::getOrder))
						.collect(Collectors.toList());
			}
		} catch (Exception e) {
			LOG.error("Exception in Vonkinator getVonkinatorAllDataSet:: " + e);
		}
		return vonkinatorDataSet;
	}

	@Override
	public List<VonkinatorDataSet> getAllVonkinatorNonITDataSet() {
		List<VonkinatorDataSet> vonkinatorDataSet = vonkinatorDataSetRepository.findByIsIT(false);
		try {
			if (vonkinatorDataSet != null) {
				return vonkinatorDataSet.stream().sorted(
						Comparator.comparing(VonkinatorDataSet::getAppId).thenComparing(VonkinatorDataSet::getOrder))
						.collect(Collectors.toList());
			}
		} catch (Exception e) {
			LOG.error("Exception in Vonkinator getAllVonkinatorNonITDataSet:: " + e);
		}
		return vonkinatorDataSet;
	}

}
