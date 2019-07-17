package com.capitalone.dashboard.executive.service;

import com.capitalone.dashboard.exec.model.ApplicationDetails;
import com.capitalone.dashboard.exec.model.ExecutiveHierarchy;
import com.capitalone.dashboard.exec.model.ExecutiveSummaryList;
import com.capitalone.dashboard.exec.model.PortfolioResponse;
import com.capitalone.dashboard.exec.repository.ApplicationDetailsRepository;
import com.capitalone.dashboard.exec.repository.ExecutiveHierarchyRepository;
import com.capitalone.dashboard.exec.repository.ExecutiveSummaryListRepository;
import com.capitalone.dashboard.exec.repository.PortfolioResponseRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * class PortfolioResponseServiceImpl
 */
@Service
public class PortfolioResponseServiceImpl implements PortfolioResponseService {

	private final PortfolioResponseRepository portfolioResponseRepository;
	private final ExecutiveSummaryListRepository executiveSummaryListRepository;
	private final ExecutiveHierarchyRepository executiveHierarchyRepository;
	private final ApplicationDetailsRepository applicationDetailsRepository;
	private final MongoTemplate mongoTemplate;

	private static final String ALL = "ALL";

	/**
	 * @param PortfolioResponseRepository
	 *            portfolioResponseRepository, MongoTemplate mongoTemplate,
	 *            ExecutiveSummaryListRepository executiveSummaryListRepository,
	 *            ExecutiveHierarchyRepository executiveHierarchyRepository,
	 *            ApplicationDetailsRepository applicationDetailsRepository
	 */
	@Autowired
	public PortfolioResponseServiceImpl(PortfolioResponseRepository portfolioResponseRepository,
			MongoTemplate mongoTemplate, ExecutiveSummaryListRepository executiveSummaryListRepository,
			ExecutiveHierarchyRepository executiveHierarchyRepository,
			ApplicationDetailsRepository applicationDetailsRepository) {
		this.portfolioResponseRepository = portfolioResponseRepository;
		this.mongoTemplate = mongoTemplate;
		this.executiveSummaryListRepository = executiveSummaryListRepository;
		this.executiveHierarchyRepository = executiveHierarchyRepository;
		this.applicationDetailsRepository = applicationDetailsRepository;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.capitalone.dashboard.executive.service.PortfolioResponseService#findAll()
	 * @return
	 * 
	 */
	public List<PortfolioResponse> findAll() {
		List<ExecutiveSummaryList> executiveSummaryList = (List<ExecutiveSummaryList>) executiveSummaryListRepository
				.findAll();
		List<String> eids = new ArrayList<>();
		if (executiveSummaryList != null && !executiveSummaryList.isEmpty()) {
			for (ExecutiveSummaryList executiveSummary : executiveSummaryList) {
				eids.add(executiveSummary.getEid());
			}
			if (!eids.isEmpty()) {
				Sort sort = new Sort(new Order(Direction.ASC, "order"),
						new Order(Direction.ASC, "executive.firstName"));
				return portfolioResponseRepository.getByEidsWithSort(eids, sort);
			}
		}
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.capitalone.dashboard.executive.service.PortfolioResponseService#findById(String)
	 * @param id
	 * @return
	 */
	@Override
	public PortfolioResponse findById(String id) {
		ObjectId portfolioId = new ObjectId(id);
		return portfolioResponseRepository.findOne(portfolioId);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see com.capitalone.dashboard.executive.service.PortfolioResponseService#getAllBusinessUnits()
	 * @return
	 */
	@Override
	public List<String> getAllBusinessUnits() {
		return mongoTemplate.getCollection("app_details").distinct("lob");
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see com.capitalone.dashboard.executive.service.PortfolioResponseService#findByBusinessUnit(String)
	 * @param businessUnit
	 * @return
	 */
	@Override
	public List<PortfolioResponse> findByBusinessUnit(String businessUnit) {
		List<ExecutiveSummaryList> executiveSummaryList = executiveSummaryListRepository
				.findByBusinessUnits(businessUnit);
		List<String> eids = new ArrayList<>();
		if (executiveSummaryList != null && !executiveSummaryList.isEmpty()) {
			for (ExecutiveSummaryList executiveSummary : executiveSummaryList) {
				eids.add(executiveSummary.getEid());
			}
			if (!eids.isEmpty()) {
				Sort sort = new Sort(new Order(Direction.ASC, "order"),
						new Order(Direction.ASC, "executive.firstName"));
				return portfolioResponseRepository.getByEidsWithSort(eids, sort);
			}
		}

		return new ArrayList();
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see com.capitalone.dashboard.executive.service.PortfolioResponseService#getExecutivesLists(String)
	 * @param businessUnit
	 * @return
	 */
	@Override
	public Map<String, String> getExecutivesLists(String businessUnit) {
		Map<String, String> executives = new HashMap<>();
		List<ExecutiveSummaryList> executiveSummaryList = new ArrayList<>();
		if (ALL.equalsIgnoreCase(businessUnit)) {
			executiveSummaryList = executiveSummaryListRepository.getSeniorExecutives();
		} else {
			executiveSummaryList = executiveSummaryListRepository.findByBusinessUnitsAndSeniorExecutive(businessUnit,
					true);
		}
		List<String> eids = new ArrayList<>();
		if (!executiveSummaryList.isEmpty()) {
			for (ExecutiveSummaryList executiveSummary : executiveSummaryList) {
				if (!eids.contains(executiveSummary.getEid()))
					executives.put(executiveSummary.getEid(),
							executiveSummary.getFirstName() + " " + executiveSummary.getLastName());
			}
		}
		return executives;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see com.capitalone.dashboard.executive.service.PortfolioResponseService#findByExecutivesHierarchy(String,
	 *      String)
	 * @param businessUnit
	 * @return
	 */
	@Override
	public List<PortfolioResponse> findByExecutivesHierarchy(String eid, String businessUnit) {

		List<String> reportingEids = new ArrayList<>();
		ExecutiveHierarchy executivesHierarchy = executiveHierarchyRepository.findByEid(eid);
		if (executivesHierarchy != null) {
			Map<String, List<String>> reportees = executivesHierarchy.getReportees();
			if (reportees != null && !reportees.isEmpty()) {
				if (!ALL.equalsIgnoreCase(businessUnit)) {
					reportingEids.addAll(reportees.get(businessUnit));
				} else {
					for (Map.Entry<String, List<String>> entry : reportees.entrySet()) {
						reportingEids.addAll(entry.getValue());
					}
				}
			}
		}
		if (!reportingEids.isEmpty()) {
			Sort sort = new Sort(new Order(Direction.ASC, "order"), new Order(Direction.ASC, "executive.firstName"));
			return portfolioResponseRepository.getByEidsWithSort(reportingEids, sort);
		}
		return new ArrayList();
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see com.capitalone.dashboard.executive.service.PortfolioResponseService#getConfigApps(String)
	 * @param portfolioId
	 * @return
	 */
	@Override
	public List<String> getConfigApps(String portfolioId) {
		PortfolioResponse portfolioResponse = portfolioResponseRepository.findOne(new ObjectId(portfolioId));
		ExecutiveSummaryList executiveSummaryList = executiveSummaryListRepository
				.findByEid(portfolioResponse.getEid());
		return executiveSummaryList.getConfiguredAppId();
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see com.capitalone.dashboard.executive.service.PortfolioResponseService#getExecutivesListsAll(String)
	 * @param businessUnit
	 * @return
	 */
	@Override
	public Map<String, String> getExecutivesListsAll(String businessUnit) {
		Map<String, String> executives = new HashMap<>();
		List<ExecutiveSummaryList> executiveSummaryList = new ArrayList();
		if (ALL.equalsIgnoreCase(businessUnit)) {
			executiveSummaryList = (List<ExecutiveSummaryList>) executiveSummaryListRepository.findAll();
		} else {
			executiveSummaryList = executiveSummaryListRepository.findByBusinessUnits(businessUnit);
		}
		List<String> eids = new ArrayList();
		if (!executiveSummaryList.isEmpty()) {
			for (ExecutiveSummaryList executiveSummary : executiveSummaryList) {
				if (!eids.contains(executiveSummary.getEid()))
					executives.put(executiveSummary.getEid(),
							executiveSummary.getFirstName() + " " + executiveSummary.getLastName());
			}
		}
		return executives;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see com.capitalone.dashboard.executive.service.PortfolioResponseService#getReportings(String)
	 * @param eid
	 * @return
	 */
	@Override
	public Map<String, String> getReportings(String eid) {
		ObjectId portfolioId = new ObjectId(eid);
		Map<String, String> executives = new HashMap<>();
		PortfolioResponse portfolioResponse = portfolioResponseRepository.findOne(portfolioId);
		if (portfolioResponse != null) {
			ExecutiveHierarchy executiveHierarchy = executiveHierarchyRepository.findByEid(portfolioResponse.getEid());
			if (executiveHierarchy != null && executiveHierarchy.getReportees() != null) {
				List<String> reportings = executiveHierarchy.getDirectReportees();
				if (reportings != null && !reportings.isEmpty()) {
					reportings = reportings.stream().distinct().collect(Collectors.toList());

					List<ExecutiveSummaryList> executiveSummaryList = executiveSummaryListRepository
							.getEids(reportings);
					for (ExecutiveSummaryList executiveSummary : executiveSummaryList) {
						executives.put(executiveSummary.getEid(),
								executiveSummary.getFirstName() + " " + executiveSummary.getLastName());
					}
				}
			}
		}
		return executives;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see com.capitalone.dashboard.executive.service.PortfolioResponseService#getAllBusinessUnitsForExec(String)
	 * @param eid
	 * @return
	 */
	@Override
	public List<String> getAllBusinessUnitsForExec(String eid) {
		List<String> bunits = new ArrayList<>();
		ObjectId portfolioId = new ObjectId(eid);
		PortfolioResponse portfolioResponse = portfolioResponseRepository.findOne(portfolioId);
		if (portfolioResponse != null) {
			ExecutiveSummaryList executiveSummaryList = executiveSummaryListRepository
					.findByEid(portfolioResponse.getEid());
			if (executiveSummaryList != null)
				return executiveSummaryList.getBusinessUnits();
		}
		return bunits;
	}

	@Override
	public Map<String, String> getApplicationListForExec(String id) {
		Map<String, String> appIds = new HashMap<>();
		ObjectId portfolioId = new ObjectId(id);
		PortfolioResponse portfolioResponse = portfolioResponseRepository.findOne(portfolioId);
		if (portfolioResponse != null) {
			ExecutiveSummaryList executiveSummaryList = executiveSummaryListRepository
					.findByEid(portfolioResponse.getEid());
			if (executiveSummaryList != null && executiveSummaryList.getAppDetails() != null)
				return executiveSummaryList.getAppDetails();
		}
		return appIds;
	}

	@Override
	public Map<String, String> getAllApplicationList() {
		Map<String, String> appIds = new HashMap<>();
		List<ApplicationDetails> appDetails = (List<ApplicationDetails>) applicationDetailsRepository.findAll();
		if (appDetails != null && !appDetails.isEmpty()) {
			for (ApplicationDetails app : appDetails) {
				appIds.put(app.getAppId(), app.getAppName());
			}
		}
		return appIds;
	}

	@Override
	public Map<String, String> getAllApplicationListForLob(String lob) {
		Map<String, String> appIds = new HashMap<>();
		List<ApplicationDetails> appDetails = applicationDetailsRepository.findByLob(lob);
		if (appDetails != null && !appDetails.isEmpty()) {
			for (ApplicationDetails app : appDetails) {
				appIds.put(app.getAppId(), app.getAppName());
			}
		}
		return appIds;
	}

	@Override
	public Map<String, String> getApplicationListForExecWithPortfolio(String businessUnit, String id) {
		Map<String, String> appIds = new HashMap<>();
		ObjectId portfolioId = new ObjectId(id);
		PortfolioResponse portfolioResponse = portfolioResponseRepository.findOne(portfolioId);
		if (portfolioResponse != null) {
			ExecutiveSummaryList executiveSummaryList = executiveSummaryListRepository
					.findByEid(portfolioResponse.getEid());
			if (executiveSummaryList != null && executiveSummaryList.getAppDetailsWithBunit() != null) {
				Map<String, Map<String, String>> appDetailsWithBunit = executiveSummaryList.getAppDetailsWithBunit();
				if (appDetailsWithBunit != null)
					return appDetailsWithBunit.get(businessUnit);
			}
		}
		return appIds;
	}

}
