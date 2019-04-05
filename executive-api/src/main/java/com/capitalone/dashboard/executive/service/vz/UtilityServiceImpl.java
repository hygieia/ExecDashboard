package com.capitalone.dashboard.executive.service.vz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.capitalone.dashboard.exec.model.MetricLevel;
import com.capitalone.dashboard.exec.model.MetricType;
import com.capitalone.dashboard.exec.model.vz.ApplicationDetails;
import com.capitalone.dashboard.exec.model.vz.BuildingBlocks;
import com.capitalone.dashboard.exec.model.vz.CardsList;
import com.capitalone.dashboard.exec.model.vz.DevOpsCupScores;
import com.capitalone.dashboard.exec.model.vz.DevopscupRoundDetails;
import com.capitalone.dashboard.exec.model.vz.ExecutiveSummaryList;
import com.capitalone.dashboard.exec.model.vz.PortfolioResponse;
import com.capitalone.dashboard.exec.model.vz.VonkinatorPeriod;
import com.capitalone.dashboard.exec.repository.vz.ApplicationDetailsRepository;
import com.capitalone.dashboard.exec.repository.vz.BuildingBlocksRepository;
import com.capitalone.dashboard.exec.repository.vz.CardsListRepository;
import com.capitalone.dashboard.exec.repository.vz.DevOpsCupScoresRepository;
import com.capitalone.dashboard.exec.repository.vz.DevopscupRoundDetailsRepository;
import com.capitalone.dashboard.exec.repository.vz.ExecutiveSummaryListRepository;
import com.capitalone.dashboard.exec.repository.vz.PortfolioResponseRepository;
import com.capitalone.dashboard.exec.repository.vz.VonkinatorPeriodRepository;

/**
 * class UtilityServiceImpl
 */
@Service
@SuppressWarnings("PMD")
public class UtilityServiceImpl implements UtilityService {

	private final ApplicationDetailsRepository applicationDetailsRepository;
	private final ExecutiveSummaryListRepository executiveSummaryListRepository;
	private final CardsListRepository cardsListRepository;
	private final VonkinatorPeriodRepository vonkinatorPeriodRepository;
	private final BuildingBlocksRepository buildingBlocksRepository;
	private final PortfolioResponseRepository portfolioResponseRepository;
	private final DevOpsCupScoresRepository devOpsCupScoresRepository;
	private final DevopscupRoundDetailsRepository devopscupRoundDetailsRepository;
	private static final Logger LOG = LoggerFactory.getLogger(UtilityServiceImpl.class);

	/**
	 * @param ApplicationDetailsRepository
	 *            applicationDetailsRepository, ExecutiveSummaryListRepository
	 *            executiveSummaryListRepository, CardsListRepository
	 *            cardsListRepository, VonkinatorPeriodRepository
	 *            vonkinatorPeriodRepository, buildingBlocksRepository,
	 *            portfolioResponseRepository, devOpsCupScoresRepository
	 */
	@Autowired
	public UtilityServiceImpl(ApplicationDetailsRepository applicationDetailsRepository,
			ExecutiveSummaryListRepository executiveSummaryListRepository, CardsListRepository cardsListRepository,
			VonkinatorPeriodRepository vonkinatorPeriodRepository, BuildingBlocksRepository buildingBlocksRepository,
			PortfolioResponseRepository portfolioResponseRepository,
			DevOpsCupScoresRepository devOpsCupScoresRepository,
			DevopscupRoundDetailsRepository devopscupRoundDetailsRepository) {
		this.applicationDetailsRepository = applicationDetailsRepository;
		this.executiveSummaryListRepository = executiveSummaryListRepository;
		this.cardsListRepository = cardsListRepository;
		this.vonkinatorPeriodRepository = vonkinatorPeriodRepository;
		this.buildingBlocksRepository = buildingBlocksRepository;
		this.portfolioResponseRepository = portfolioResponseRepository;
		this.devOpsCupScoresRepository = devOpsCupScoresRepository;
		this.devopscupRoundDetailsRepository = devopscupRoundDetailsRepository;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.capitalone.dashboard.executive.service.UtilityService#getAppCriticalityStatus(java.util.List)
	 * @param appList
	 * @return data
	 */
	@Override
	public Map<String, List<String>> getAppCriticalityStatus(List<String> appList) {
		Map<String, List<String>> result = new HashMap<>();
		List<String> businessCritical = new ArrayList<>();
		List<String> systemCritical = new ArrayList<>();
		List<String> missionCritical = new ArrayList<>();
		List<String> nonCritical = new ArrayList<>();

		for (String appName : appList) {
			ApplicationDetails appDetails = applicationDetailsRepository.findByAppName(appName);

			if (appDetails != null) {
				switch (appDetails.getAvailabilityStatus()) {

				case "Business Critical":
					businessCritical.add(appName);
					result.put("businessc", businessCritical);
					break;
				case "System Critical":
					systemCritical.add(appName);
					result.put("systemc", systemCritical);
					break;
				case "Mission Critical":
					missionCritical.add(appName);
					result.put("missionc", missionCritical);
					break;
				case "Non-Critical":
					nonCritical.add(appName);
					result.put("nonc", nonCritical);
					break;
				}
			}
		}

		return result;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.capitalone.dashboard.executive.service.UtilityService#setAsFav(java.lang.String,
	 *      java.util.List)
	 * @param eid,
	 *            favEid
	 * @return data
	 */
	@Override
	public boolean setAsFav(String eid, List<String> favEids) {
		ExecutiveSummaryList executiveSummaryList = executiveSummaryListRepository.findByEid(eid);
		if (executiveSummaryList != null) {
			executiveSummaryListRepository.delete(executiveSummaryList);
			executiveSummaryList.setFavourite(favEids);
		}
		executiveSummaryListRepository.save(executiveSummaryList);
		return true;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.capitalone.dashboard.executive.service.UtilityService#removeFav(java.lang.String)
	 * @param eid
	 * @return data
	 */
	@Override
	public boolean removeFav(String eid) {
		ExecutiveSummaryList executiveSummaryList = executiveSummaryListRepository.findByEid(eid);
		if (executiveSummaryList != null) {
			executiveSummaryListRepository.delete(executiveSummaryList);
			executiveSummaryList.setFavourite(new ArrayList<>());
		}
		executiveSummaryListRepository.save(executiveSummaryList);
		return true;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.capitalone.dashboard.executive.service.UtilityService#getFavsOfEid(java.lang.String)
	 * @param eid
	 * @return data
	 */
	@Override
	public Map<String, String> getFavsOfEid(String eid) {
		Map<String, String> executives = new HashMap<>();
		ExecutiveSummaryList executiveSummaryList = executiveSummaryListRepository.findByEid(eid);
		try {
			if (executiveSummaryList.getFavourite() != null) {
				for (String favExecEid : executiveSummaryList.getFavourite()) {
					ExecutiveSummaryList favExec = executiveSummaryListRepository.findByEid(favExecEid);
					if (favExec != null) {
						executives.put(favExec.getEid(), favExec.getFirstName() + " " + favExec.getLastName());
					}
				}
			}
		} catch (Exception e) {
			LOG.info("No fav Available for eid: " + eid);
		}
		return executives;
	}

	/**
	 * Javadoc
	 * 
	 * @see com.capitalone.dashboard.executive.service.UtilityService#getActiveCards()
	 * @return List<String>
	 */
	@Override
	public List<String> getActiveCards() {
		List<String> cardsEnabled = new ArrayList<>();
		List<CardsList> cardsList = cardsListRepository.findByEnabled(true);
		if (cardsList != null && !cardsList.isEmpty()) {
			for (CardsList cards : cardsList) {
				cardsEnabled.add(cards.getCardName());
			}
		}
		return cardsEnabled;
	}

	/**
	 * Javadoc
	 * 
	 * @see com.capitalone.dashboard.executive.service.UtilityService#getActiveCardsPreview()
	 * @return List<String>
	 */
	@Override
	public Map<String, String> getActiveCardsPreview() {
		Map<String, String> cardsEnabled = new HashMap<>();
		List<CardsList> cardsList = cardsListRepository.findByEnabled(true);
		if (cardsList != null && !cardsList.isEmpty()) {
			for (CardsList cards : cardsList) {
				cardsEnabled.put(cards.getCardName(), cards.getPreviewName());
			}
		}
		return cardsEnabled;
	}

	/**
	 * Javadoc
	 * 
	 * @see com.capitalone.dashboard.executive.service.UtilityService#getActiveCardsSelectPreview()
	 * @return List<String>
	 */
	@Override
	public Map<String, String> getActiveCardsSelectPreview() {
		Map<String, String> cardsEnabled = new HashMap<>();
		List<CardsList> cardsList = cardsListRepository.findByDefaultMetricsAndEnabled(true, true,
				new PageRequest(0, 9));
		if (cardsList != null && !cardsList.isEmpty()) {
			for (CardsList cards : cardsList) {
				cardsEnabled.put(cards.getCardName(), cards.getPreviewName());
			}
		}
		return cardsEnabled;
	}

	/**
	 * Javadoc
	 * 
	 * @see com.capitalone.dashboard.executive.service.UtilityService#getTimePeriods()
	 * @return List<String>
	 */
	@Override
	public Map<Integer, String> getTimePeriods() {
		Map<Integer, String> timePeriods = new HashMap<>();
		List<VonkinatorPeriod> vonkinatorPeriodList = (List<VonkinatorPeriod>) vonkinatorPeriodRepository.findAll();
		if (vonkinatorPeriodList != null && !vonkinatorPeriodList.isEmpty()) {
			for (VonkinatorPeriod vonkinatorPeriod : vonkinatorPeriodList) {
				timePeriods.put(vonkinatorPeriod.getOrder(), vonkinatorPeriod.getPeriod());
			}
		}
		return timePeriods;
	}

	@Override
	public String getProductId(String metricLevelId) {
		BuildingBlocks buildingBlockMetricSummary = buildingBlocksRepository
				.findByMetricLevelIdAndMetricLevel(metricLevelId, MetricLevel.PRODUCT);
		if (buildingBlockMetricSummary != null)
			return buildingBlockMetricSummary.getId().toString();
		return null;
	}

	@Override
	public List<DevOpsCupScores> getPortfolioMetricProductsForDevopscup(MetricType metricType, String portfolioId) {
		ObjectId id = new ObjectId(portfolioId);
		List<DevOpsCupScores> devopscupScoreList = new ArrayList<>();
		PortfolioResponse portfolioResponse = portfolioResponseRepository.findOne(id);
		if (portfolioResponse != null) {
			ExecutiveSummaryList executiveSummaryList = executiveSummaryListRepository
					.findByEid(portfolioResponse.getEid());
			if (executiveSummaryList != null) {
				devopscupScoreList = devOpsCupScoresRepository
						.getDevOpsCupScoresByAppLists(executiveSummaryList.getAppId());
				Collections.sort(devopscupScoreList,
						(DevOpsCupScores d1, DevOpsCupScores d2) -> d2.getTotalPoints().compareTo(d1.getTotalPoints()));
			}
		}
		return devopscupScoreList;
	}

	@Override
	public DevopscupRoundDetails getRoundDetailsForDevopscup() {
		return devopscupRoundDetailsRepository.getByActive(true);
	}

}
