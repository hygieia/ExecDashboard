package com.capitalone.dashboard.executive.service;

import com.capitalone.dashboard.exec.model.ApplicationDetails;
import com.capitalone.dashboard.exec.model.Authentication;
import com.capitalone.dashboard.exec.model.ExecutiveStatus;
import com.capitalone.dashboard.exec.model.ExecutiveSummaryList;
import com.capitalone.dashboard.exec.model.TrackPageViews;
import com.capitalone.dashboard.exec.model.UserTrack;
import com.capitalone.dashboard.exec.repository.ApplicationDetailsRepository;
import com.capitalone.dashboard.exec.repository.AuthenticationRepository;
import com.capitalone.dashboard.exec.repository.ExecutiveSummaryListRepository;
import com.capitalone.dashboard.exec.repository.TrackPageViewsRepository;
import com.capitalone.dashboard.exec.repository.UserTrackRepository;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * UserInfoServiceImpl
 * 
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

	private ExecutiveSummaryListRepository executiveSummaryListRepository;
	private UserTrackRepository userTrackRepository;
	private TrackPageViewsRepository trackPageViewsRepository;
	private AuthenticationRepository authenticationRepository;
	private ApplicationDetailsRepository applicationDetailsRepository;
	private MongoTemplate mongoTemplate;

	private static final String UNIQUEHITS = "uniqueHits";
	private static final String TOTALHITS = "totalHits";
	private static final String EXECUTIVES = "executives";
	private static final String EID = "eid";
	private static final String TRACKVIEWS = "track_user_views";
	private static final String TOTAL = "total";
	private static final String ID = "_id";
	private static final String FIRSTORDER = "0";
	private static final String VIEW = "view";
	private static final String TIMESTAMP = "timeStamp";
	private static final String METRICSNAME = "metricsName";
	private static final String APPLICATIONVIEWID = "applicationViewId";
	private static final String EXECUTIVEVIEWID = "executiveViewId";
	private static final String PRODUCTMETRIC = "Product Metric View";
	private static final String PORTFOLIOMETRIC = "Portfolio Metric View";
	private static final String PRODUCT = "Product View";
	private static final String PORTFOLIO = "Portfolio View";

	private static final Logger LOG = LoggerFactory.getLogger(UserInfoServiceImpl.class);

	/**
	 * UserInfoServiceImpl
	 * 
	 * @param userTrackRepository
	 * @param executiveSummaryListRepository
	 * @param trackPageViewsRepository
	 * @param authenticationRepository
	 * @param applicationDetailsRepository
	 * @param mongoTemplate
	 * @return
	 */
	@Autowired
	public UserInfoServiceImpl(UserTrackRepository userTrackRepository,
			ExecutiveSummaryListRepository executiveSummaryListRepository,
			TrackPageViewsRepository trackPageViewsRepository, AuthenticationRepository authenticationRepository,
			ApplicationDetailsRepository applicationDetailsRepository, MongoTemplate mongoTemplate) {
		this.userTrackRepository = userTrackRepository;
		this.executiveSummaryListRepository = executiveSummaryListRepository;
		this.trackPageViewsRepository = trackPageViewsRepository;
		this.authenticationRepository = authenticationRepository;
		this.applicationDetailsRepository = applicationDetailsRepository;
		this.mongoTemplate = mongoTemplate;
	}

	/**
	 * getUniqueHitsInfo
	 * 
	 * @param ...
	 * @return Map<Long, Integer>
	 */
	@Override
	public Map<Long, Integer> getUniqueHitsInfo() {
		Map<Long, Integer> hitsInfo = new HashMap<>();
		long timeStamp = System.currentTimeMillis();
		long daysCount = 86400000;
		for (int i = 9; i >= 1; i--) {
			Long updatedStartTimeStamp = timeStamp - (i * 7l * daysCount);
			Long updatedEndTimeStamp = timeStamp - ((i - 1) * 7l * daysCount);
			List<UserTrack> userTrackList = userTrackRepository.getByTimeStamp(updatedStartTimeStamp,
					updatedEndTimeStamp);
			if (userTrackList != null)
				hitsInfo.put((long) (i * 7), userTrackList.size());

		}
		return hitsInfo;
	}

	/**
	 * getUniqueHits
	 * 
	 * @param ...
	 * @return Map<String, Integer>
	 */
	@Override
	public Map<String, Integer> getUniqueHits() {
		Map<String, Integer> hitsInfo = new HashMap<>();
		Long timeStamp = System.currentTimeMillis();
		List<UserTrack> userTrackList = userTrackRepository.getByTimeStamp(timeStamp - 86400000, timeStamp);
		if (userTrackList != null) {
			hitsInfo.put(UNIQUEHITS, userTrackList.size());
			hitsInfo.put(TOTALHITS, getTotalHits(userTrackList, timeStamp - 86400000));
		} else {
			hitsInfo.put(UNIQUEHITS, 0);
			hitsInfo.put(TOTALHITS, 0);
		}
		return hitsInfo;
	}

	private Integer getTotalHits(List<UserTrack> userTrackList, Long timeStamp) {
		List<Long> userLoggins = new ArrayList<>();
		for (UserTrack user : userTrackList) {
			userLoggins.addAll(user.getLogginTime());
		}
		return (int) userLoggins.stream().filter(s -> s > timeStamp).count();
	}

	/**
	 * getExecutivesAccessedList
	 * 
	 * @param ...
	 * @return List<ExecutiveStatus>
	 */
	@Override
	public List<ExecutiveStatus> getExecutivesAccessedList() {
		List<ExecutiveStatus> executivesList = new ArrayList<>();
		List<String> executiveIds = mongoTemplate.getCollection(EXECUTIVES).distinct(EID);
		Long timeStamp = System.currentTimeMillis() - 5184000000l;
		List<UserTrack> userTrackList = userTrackRepository.getByTimeStampAndEid(timeStamp, executiveIds);
		if (userTrackList != null)
			return getExecutivesList(userTrackList);
		return executivesList;
	}

	private List<ExecutiveStatus> getExecutivesList(List<UserTrack> userTrackList) {
		List<ExecutiveStatus> executivesList = new ArrayList<>();
		for (UserTrack userInfo : userTrackList) {
			ExecutiveStatus executiveStatus = new ExecutiveStatus();
			ExecutiveSummaryList executiveSummaryList = executiveSummaryListRepository.findByEid(userInfo.getUserEid());
			List<Long> userLoggings = userInfo.getLogginTime();
			executiveStatus.setName(executiveSummaryList.getFirstName() + " " + executiveSummaryList.getLastName());
			executiveStatus.setLastAccessed(userLoggings.get(userLoggings.size() - 1));
			executiveStatus.setRole(executiveSummaryList.getRole());
			executivesList.add(executiveStatus);
		}
		return executivesList;
	}

	@Override
	public Boolean putAppTrack(String view, String userId, List<String> appIds) {
		TrackPageViews trackHits = new TrackPageViews();
		trackHits.setView(view);
		trackHits.setApplicationViewId(appIds);
		trackHits.setUserId(userId);
		trackHits.setTimeStamp(System.currentTimeMillis());
		trackPageViewsRepository.save(trackHits);
		return true;
	}

	@Override
	public Boolean putExecTrack(String view, String userId, List<String> eids) {
		TrackPageViews trackHits = new TrackPageViews();
		trackHits.setView(view);
		trackHits.setExecutiveViewId(eids);
		trackHits.setUserId(userId);
		trackHits.setTimeStamp(System.currentTimeMillis());
		trackPageViewsRepository.save(trackHits);
		return true;
	}

	@Override
	public Boolean putPageTrack(String view, String userId) {
		TrackPageViews trackHits = new TrackPageViews();
		trackHits.setView(view);
		trackHits.setUserId(userId);
		trackHits.setTimeStamp(System.currentTimeMillis());
		trackPageViewsRepository.save(trackHits);
		return true;
	}

	@Override
	public Boolean putAppMetricTrack(String view, String userId, List<String> appIds, String metricName) {
		TrackPageViews trackHits = new TrackPageViews();
		trackHits.setView(view);
		trackHits.setApplicationViewId(appIds);
		trackHits.setUserId(userId);
		trackHits.setTimeStamp(System.currentTimeMillis());
		trackHits.setMetricsName(metricName);
		trackPageViewsRepository.save(trackHits);
		return true;
	}

	@Override
	public Boolean putExecMetricTrack(String view, String userId, List<String> eids, String metricName) {
		TrackPageViews trackHits = new TrackPageViews();
		trackHits.setView(view);
		trackHits.setExecutiveViewId(eids);
		trackHits.setUserId(userId);
		trackHits.setTimeStamp(System.currentTimeMillis());
		trackHits.setMetricsName(metricName);
		trackPageViewsRepository.save(trackHits);
		return true;
	}

	@Override
	public Map<String, Long> getFrequentUsers() {
		Map<String, Long> frequentUsers = new HashMap<>();
		Map<String, Long> frequentUsersSorted = new LinkedHashMap<>();
		try {
			List<UserTrack> userTracks = userTrackRepository.findTop30ByOrderByLogginTimeDesc();
			if (userTracks != null && !userTracks.isEmpty()) {
				for (UserTrack user : userTracks) {
					String userEid = user.getUserEid();
					Long timeStamp = user.getLogginTime().get(user.getLogginTime().size() - 1);
					Authentication auth = authenticationRepository.findByEid(userEid);
					if (auth != null && userEid.length() > 1) {
						frequentUsers.put(auth.getFirstname() + ", " + auth.getLastname(), timeStamp);
					}
				}
				frequentUsers.entrySet().stream().sorted(Map.Entry.<String, Long> comparingByValue().reversed())
						.forEachOrdered(x -> frequentUsersSorted.put(x.getKey(), x.getValue()));
			}
		} catch (Exception e) {
			LOG.error("User Tracking, getFrequentUsers :: " + e);
		}
		return frequentUsersSorted;
	}

	@Override
	public List<String> getFrequentExecutives() {
		List<String> executives = new LinkedList<>();
		try {
			Map<String, Integer> resultsMap = new HashMap<>();
			Map<String, Integer> resultsMapSorted = new LinkedHashMap<>();
			List<String> views = new ArrayList<>();
			views.add(PORTFOLIO);
			views.add(PORTFOLIOMETRIC);
			GroupOperation groupByExecutiveViewId = Aggregation.group(EXECUTIVEVIEWID).count().as(TOTAL);
			MatchOperation filter = Aggregation.match(new Criteria(VIEW).in(views).andOperator(
					Criteria.where(TIMESTAMP).gte(getTimeStamp(30)), Criteria.where(EXECUTIVEVIEWID).ne(null)));
			Aggregation aggregation = Aggregation.newAggregation(filter, groupByExecutiveViewId);
			AggregationResults<DBObject> temp = mongoTemplate.aggregate(aggregation, TRACKVIEWS, DBObject.class);
			if (temp != null) {
				List<DBObject> results = temp.getMappedResults();
				if (results != null && !results.isEmpty()) {
					for (DBObject object : results) {
						if (object.get(FIRSTORDER) != null) {
							String id = object.get(FIRSTORDER).toString();
							Integer total = (Integer) object.get(TOTAL);
							resultsMap.put(id, total);
						}
					}
					resultsMap.entrySet().stream().sorted(Map.Entry.<String, Integer> comparingByValue().reversed())
							.forEachOrdered(x -> resultsMapSorted.put(x.getKey(), x.getValue()));
					resultsMapSorted.forEach((eid, v) -> {
						ExecutiveSummaryList executive = executiveSummaryListRepository.findByEid(eid);
						if (executive != null) {
							executives.add(executive.getFirstName() + ", " + executive.getLastName());
						}
					});
				}
			}
		} catch (Exception e) {
			LOG.error("User Tracking, getFrequentExecutives :: " + e);
		}
		return executives;
	}

	/**
	 * getTimeStamp
	 * 
	 * @param days
	 * @return Long
	 */
	public Long getTimeStamp(int days) {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calendar.add(Calendar.DAY_OF_MONTH, -days);
		return calendar.getTimeInMillis();
	}

	@Override
	public List<String> getFrequentApplications() {
		List<String> applications = new LinkedList<>();
		try {
			Map<String, Integer> resultsMap = new HashMap<>();
			Map<String, Integer> resultsMapSorted = new LinkedHashMap<>();
			List<String> views = new ArrayList<>();
			views.add(PRODUCTMETRIC);
			views.add(PRODUCT);
			GroupOperation groupByApplicationViewId = Aggregation.group(APPLICATIONVIEWID).count().as(TOTAL);
			MatchOperation filter = Aggregation.match(new Criteria(VIEW).in(views).andOperator(
					Criteria.where(TIMESTAMP).gte(getTimeStamp(30)), Criteria.where(APPLICATIONVIEWID).ne(null)));
			Aggregation aggregation = Aggregation.newAggregation(filter, groupByApplicationViewId);
			AggregationResults<DBObject> temp = mongoTemplate.aggregate(aggregation, TRACKVIEWS, DBObject.class);
			if (temp != null) {
				List<DBObject> results = temp.getMappedResults();
				if (results != null && !results.isEmpty()) {
					for (DBObject object : results) {
						if (object.get(FIRSTORDER) != null) {
							String id = object.get(FIRSTORDER).toString();
							Integer total = (Integer) object.get(TOTAL);
							resultsMap.put(id, total);
						}
					}
					resultsMap.entrySet().stream().sorted(Map.Entry.<String, Integer> comparingByValue().reversed())
							.forEachOrdered(x -> resultsMapSorted.put(x.getKey(), x.getValue()));
					resultsMapSorted.forEach((appId, v) -> {
						ApplicationDetails app = applicationDetailsRepository.findByAppId(appId);
						if (app != null) {
							applications.add(app.getAppName() + " - " + app.getLob());
						}
					});
				}
			}
		} catch (Exception e) {
			LOG.error("User Tracking, getFrequentApplications :: " + e);
		}
		return applications;
	}

	@Override
	public List<String> getFrequentCards() {
		List<String> metrics = new LinkedList<>();
		try {
			Map<String, Integer> resultsMap = new HashMap<>();
			Map<String, Integer> resultsMapSorted = new LinkedHashMap<>();
			List<String> views = new ArrayList<>();
			views.add(PRODUCTMETRIC);
			views.add(PORTFOLIOMETRIC);
			GroupOperation groupByApplicationViewId = Aggregation.group(METRICSNAME).count().as(TOTAL);
			MatchOperation filter = Aggregation.match(new Criteria(VIEW).in(views).andOperator(
					Criteria.where(TIMESTAMP).gte(getTimeStamp(30)), Criteria.where(METRICSNAME).ne(null)));
			Aggregation aggregation = Aggregation.newAggregation(filter, groupByApplicationViewId);
			AggregationResults<DBObject> temp = mongoTemplate.aggregate(aggregation, TRACKVIEWS, DBObject.class);
			if (temp != null) {
				List<DBObject> results = temp.getMappedResults();
				if (results != null && !results.isEmpty()) {
					for (DBObject object : results) {
						if (object.get(ID) != null) {
							String id = object.get(ID).toString();
							Integer total = (Integer) object.get(TOTAL);
							resultsMap.put(id, total);
						}
					}
					resultsMap.entrySet().stream().sorted(Map.Entry.<String, Integer> comparingByValue().reversed())
							.forEachOrdered(x -> resultsMapSorted.put(x.getKey(), x.getValue()));
					resultsMapSorted.forEach((k, v) -> metrics.add(k));
				}
			}
		} catch (Exception e) {
			LOG.error("User Tracking, getFrequentCards :: " + e);
		}
		return metrics;
	}

}
