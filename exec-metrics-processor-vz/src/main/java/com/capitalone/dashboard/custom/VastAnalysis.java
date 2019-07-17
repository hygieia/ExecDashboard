package com.capitalone.dashboard.custom;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.dao.CollectorDetailsDAO;
import com.capitalone.dashboard.dao.ConfigurationMetricsDAO;
import com.capitalone.dashboard.dao.DashboardDetailsDAO;
import com.capitalone.dashboard.dao.VastDetailsDAO;
import com.capitalone.dashboard.exec.model.ApplicationDetails;
import com.capitalone.dashboard.exec.model.CollectorStatus;
import com.capitalone.dashboard.exec.model.CollectorType;
import com.capitalone.dashboard.exec.model.CollectorUpdatedDetails;
import com.capitalone.dashboard.exec.model.ConfigurationMetrics;
import com.capitalone.dashboard.exec.model.ExecutiveHierarchy;
import com.capitalone.dashboard.exec.model.ExecutiveResponse;
import com.capitalone.dashboard.exec.model.ExecutiveSummaryList;
import com.capitalone.dashboard.exec.model.PortfolioResponse;
import com.capitalone.dashboard.exec.model.Vast;
import com.capitalone.dashboard.exec.repository.ApplicationDetailsRepository;
import com.capitalone.dashboard.exec.repository.CollectorStatusRepository;
import com.capitalone.dashboard.exec.repository.CollectorUpdatedDetailsRepository;
import com.capitalone.dashboard.exec.repository.ExecutiveHierarchyRepository;
import com.capitalone.dashboard.exec.repository.ExecutiveSummaryListRepository;
import com.capitalone.dashboard.exec.repository.PortfolioResponseRepository;
import com.capitalone.dashboard.exec.repository.VastRepository;
import com.mongodb.MongoClient;

@Component
@SuppressWarnings("PMD")
public class VastAnalysis {

	private final VastDetailsDAO vastDetailsDAO;
	private final DashboardDetailsDAO dashboardDetailsDAO;
	private final ConfigurationMetricsDAO configurationMetricsDAO;
	private final CollectorDetailsDAO collectorDetailsDAO;
	private final ExecutiveSummaryListRepository executiveSummaryListRepository;
	private final ApplicationDetailsRepository applicationDetailsRepository;
	private final PortfolioResponseRepository portfolioResponseRepository;
	private final CollectorStatusRepository collectorStatusRepository;
	private final ExecutiveHierarchyRepository executiveHierarchyRepository;
	private final MongoTemplate mongoTemplate;
	private List<String> confAppIds;
	private final CollectorUpdatedDetailsRepository collectorUpdatedDetailsRepository;
	private final VastRepository vastRepository;

	private static final String APPLIST = "appList";
	private static final String BUNITS = "businessUnits";
	private static final String FIRSTNAME = "firstName";
	private static final String CONFAPPLIST = "confAppList";
	private static final String LASTNAME = "lastName";
	private static final String ROLE = "role";
	private static final String CHAIRMAN = "Chairman";
	private static final String CHIEFNTWK = "Chief Ntwk";
	private static final String APPID = "appId";
	private static final String TIMESTAMP = "timeStamp";
	private static final String UPDATEDDATE = "updatedDate";
	private static final Logger LOG = LoggerFactory.getLogger(VastAnalysis.class);
	private Boolean isVastUpdate = true;

	/**
	 * VastAnalysis
	 * 
	 * @param vastDetailsDAO
	 * @param dashboardDetailsDAO
	 * @param configurationMetricsDAO
	 * @param collectorDetailsDAO
	 * @param executiveSummaryListRepository
	 * @param applicationDetailsRepository
	 * @param portfolioResponseRepository
	 * @param collectorStatusRepository
	 * @param executiveHierarchyRepository
	 * @param mongoTemplate
	 * @param collectorUpdatedDetailsRepository
	 * @return
	 */
	@Autowired
	public VastAnalysis(VastDetailsDAO vastDetailsDAO, DashboardDetailsDAO dashboardDetailsDAO,
			ConfigurationMetricsDAO configurationMetricsDAO, CollectorDetailsDAO collectorDetailsDAO,
			ExecutiveSummaryListRepository executiveSummaryListRepository,
			ApplicationDetailsRepository applicationDetailsRepository,
			PortfolioResponseRepository portfolioResponseRepository,
			CollectorStatusRepository collectorStatusRepository,
			ExecutiveHierarchyRepository executiveHierarchyRepository, MongoTemplate mongoTemplate,
			CollectorUpdatedDetailsRepository collectorUpdatedDetailsRepository, VastRepository vastRepository) {
		this.vastDetailsDAO = vastDetailsDAO;
		this.dashboardDetailsDAO = dashboardDetailsDAO;
		this.configurationMetricsDAO = configurationMetricsDAO;
		this.collectorDetailsDAO = collectorDetailsDAO;
		this.executiveSummaryListRepository = executiveSummaryListRepository;
		this.applicationDetailsRepository = applicationDetailsRepository;
		this.portfolioResponseRepository = portfolioResponseRepository;
		this.collectorStatusRepository = collectorStatusRepository;
		this.executiveHierarchyRepository = executiveHierarchyRepository;
		this.mongoTemplate = mongoTemplate;
		this.collectorUpdatedDetailsRepository = collectorUpdatedDetailsRepository;
		this.vastRepository = vastRepository;
	}

	/**
	 * processVastMetricsDetails
	 * 
	 * @return Boolean
	 */
	public Boolean processVastMetricsDetails() {
		List<String> eids = new ArrayList<>();
		List<ApplicationDetails> applicationDetailsList = new ArrayList<>();
		Map<String, Object> executives = new HashMap<>();
		Map<String, List<String>> executiveHierarchy = new HashMap<>();
		MongoClient client = null;
		Long timeStamp = System.currentTimeMillis();
		try {
			client = vastDetailsDAO.getMongoClient();
			List<Vast> vastDetails = vastDetailsDAO.getMappingVastId(client);
			confAppIds = dashboardDetailsDAO.getConfiguredAppIds(client);
			if (!vastDetails.isEmpty()) {
				for (Vast vast : vastDetails) {
					ApplicationDetails appDetails = new ApplicationDetails();
					String appId = vast.getVastApplID();
					String businessUnit = vast.getVastBusinessUnit();

					if (vast.getVastTierOneContactEid() != null && vast.getVastTierOneContactEid().length() > 0) {
						if (!eids.contains(vast.getVastTierOneContactEid())) {
							executives.put(vast.getVastTierOneContactEid(),
									getExecutiveList(vast.getVastTierOneContactName(), appId, businessUnit,
											vast.getVastTierOneContactTitle()));
							eids.add(vast.getVastTierOneContactEid());
						} else {
							executives.put(vast.getVastTierOneContactEid(), getUpdatedExecutiveList(executives,
									vast.getVastTierOneContactEid(), appId, businessUnit));
						}
					}

					if (vast.getVastTierTwoContactEid() != null && vast.getVastTierTwoContactEid().length() > 0) {
						if (!eids.contains(vast.getVastTierTwoContactEid())) {
							executives.put(vast.getVastTierTwoContactEid(),
									getExecutiveList(vast.getVastTierTwoContactName(), appId, businessUnit,
											vast.getVastTierTwoContactTitle()));
							eids.add(vast.getVastTierTwoContactEid());
						} else {
							executives.put(vast.getVastTierTwoContactEid(), getUpdatedExecutiveList(executives,
									vast.getVastTierTwoContactEid(), appId, businessUnit));
						}
					}

					if (vast.getVastTierThreeContactEid() != null && vast.getVastTierThreeContactEid().length() > 0) {
						if (!eids.contains(vast.getVastTierThreeContactEid())) {
							executives.put(vast.getVastTierThreeContactEid(),
									getExecutiveList(vast.getVastTierThreeContactName(), appId, businessUnit,
											vast.getVastTierThreeContactTitle()));
							eids.add(vast.getVastTierThreeContactEid());
						} else {
							executives.put(vast.getVastTierThreeContactEid(), getUpdatedExecutiveList(executives,
									vast.getVastTierThreeContactEid(), appId, businessUnit));
						}
					}

					if (vast.getVastTierFourContactEid() != null && vast.getVastTierFourContactEid().length() > 0) {
						if (!eids.contains(vast.getVastTierFourContactEid())) {
							executives.put(vast.getVastTierFourContactEid(),
									getExecutiveList(vast.getVastTierFourContactName(), appId, businessUnit,
											vast.getVastTierFourContactTitle()));
							eids.add(vast.getVastTierFourContactEid());
						} else {
							executives.put(vast.getVastTierFourContactEid(), getUpdatedExecutiveList(executives,
									vast.getVastTierFourContactEid(), appId, businessUnit));
						}
					}

					if (vast.getVastTierFiveContactEid() != null && vast.getVastTierFiveContactEid().length() > 0) {
						if (!eids.contains(vast.getVastTierFiveContactEid())) {
							executives.put(vast.getVastTierFiveContactEid(),
									getExecutiveList(vast.getVastTierFiveContactName(), appId, businessUnit,
											vast.getVastTierFiveContactTitle()));
							eids.add(vast.getVastTierFiveContactEid());
						} else {
							executives.put(vast.getVastTierFiveContactEid(), getUpdatedExecutiveList(executives,
									vast.getVastTierFiveContactEid(), appId, businessUnit));
						}
					}

					if (vast.getVastTierSixContactEid() != null && vast.getVastTierSixContactEid().length() > 0) {
						if (!eids.contains(vast.getVastTierSixContactEid())) {
							executives.put(vast.getVastTierSixContactEid(),
									getExecutiveList(vast.getVastTierSixContactName(), appId, businessUnit,
											vast.getVastTierSixContactTitle()));
							eids.add(vast.getVastTierSixContactEid());
						} else {
							executives.put(vast.getVastTierSixContactEid(), getUpdatedExecutiveList(executives,
									vast.getVastTierSixContactEid(), appId, businessUnit));
						}
					}

					if (vast.getVastTierSevenContactEid() != null && vast.getVastTierSevenContactEid().length() > 0) {
						if (!eids.contains(vast.getVastTierSevenContactEid())) {
							executives.put(vast.getVastTierSevenContactEid(),
									getExecutiveList(vast.getVastTierSevenContactName(), appId, businessUnit,
											vast.getVastTierSevenContactTitle()));
							eids.add(vast.getVastTierSevenContactEid());
						} else {
							executives.put(vast.getVastTierSevenContactEid(), getUpdatedExecutiveList(executives,
									vast.getVastTierSevenContactEid(), appId, businessUnit));
						}
					}

					appDetails.setAppAcronym(vast.getVastAcronym());
					appDetails.setAppId(appId);
					appDetails.setVastId(vast.getVastID());
					appDetails.setAppName(vast.getVastAppName());
					appDetails.setAvailabilityStatus(vast.getVastAvailabilityStatus());
					appDetails.setLob(vast.getVastBusinessUnit());
					appDetails.setPoc(vast.getVastCustodianContactName());
					appDetails.setLastUpdated(timeStamp);
					if (confAppIds.contains(appId)) {
						appDetails.setDashboardAvailable(true);
						appDetails.setTeamBoardLink("http://" + dashboardDetailsDAO.getDashboardIp(appId, client));
						ConfigurationMetrics confMetrics = configurationMetricsDAO.getConfigurationMetrics(appId,
								client);
						if (confMetrics != null) {
							appDetails.setTotalTeamBoards(confMetrics.getModules());
							appDetails.setLastScanned(getISODateTime(confMetrics.getTimeStamp()));
						}
					}
					applicationDetailsList.add(appDetails);
					executiveHierarchy = processHierarchyMap(vast, executiveHierarchy);
				}
			}

			if (!executives.isEmpty())
				getProcessedDetails(executives);

			if (!applicationDetailsList.isEmpty())
				processApplicationDetails(applicationDetailsList);

			if (!executiveHierarchy.isEmpty())
				processExecutiveDesignation(executiveHierarchy);

		} catch (Exception e) {
			LOG.info("Error in processVastMetricsDetails() (VastAnalysis Class) : ", e);
		} finally {
			if (client != null)
				client.close();
		}
		return true;
	}

	private Boolean processExecutiveDesignation(Map<String, List<String>> executives) {
		try {
			Long lastUpdated = System.currentTimeMillis();
			for (Map.Entry<String, List<String>> entry : executives.entrySet()) {
				String eid = entry.getKey();
				List<String> reportings = entry.getValue();
				Map<String, List<String>> reportees = new HashMap<>();
				if (reportings != null && !reportings.isEmpty()) {
					ExecutiveHierarchy executiveHierarchy = executiveHierarchyRepository.findByEid(eid);
					if (executiveHierarchy == null)
						executiveHierarchy = new ExecutiveHierarchy();
					executiveHierarchy.setEid(eid);
					String role = getDesignation(eid);
					if (role != null) {
						executiveHierarchy.setDesignation(processedTitle(role));
						executiveHierarchy.setRole(role);
					}
					for (String reportee : reportings) {
						ExecutiveSummaryList executiveSummaryList = executiveSummaryListRepository.findByEid(reportee);
						if (executiveSummaryList != null) {
							reportees = processReportees(reportees, reportee, executiveSummaryList.getBusinessUnits());
						}
					}
					executiveHierarchy.setReportees(reportees);
					executiveHierarchy.setLastUpdated(lastUpdated);
					executiveHierarchyRepository.save(executiveHierarchy);
				}
			}

			List<ExecutiveHierarchy> executiveHierarchyList = executiveHierarchyRepository
					.getNotUpdatedList(lastUpdated);
			if (executiveHierarchyList != null && !executiveHierarchyList.isEmpty()) {
				executiveHierarchyRepository.delete(executiveHierarchyList);
			}

		} catch (Exception e) {
			LOG.info("Error in processExecutiveDesignation() (VastAnalysis Class) : ", e);
		}
		return true;
	}

	private Map<String, List<String>> processReportees(Map<String, List<String>> reportees, String reportee,
			List<String> businessUnits) {
		if (businessUnits != null && !businessUnits.isEmpty()) {
			for (String bUnit : businessUnits) {
				List<String> reportings = reportees.get(bUnit);
				if (reportings == null) {
					reportings = new ArrayList<>();
					if (!reportings.contains(reportee)) {
						reportings.add(reportee);
					}
					reportees.put(bUnit, reportings);
				} else {
					if (!reportings.contains(reportee)) {
						reportings.add(reportee);
					}
					reportees.replace(bUnit, reportings);
				}
			}
		}
		return reportees;
	}

	private String getDesignation(String eid) {
		ExecutiveSummaryList executiveSummaryList = executiveSummaryListRepository.findByEid(eid);
		if (executiveSummaryList != null)
			return executiveSummaryList.getRole();
		return null;
	}

	private Map<String, List<String>> processHierarchyMap(Vast vast, Map<String, List<String>> hierarchyMap) {

		if (vast.getVastTierOneContactEid() != null) {
			List<String> processedListOne = hierarchyMap.get(vast.getVastTierOneContactEid());
			if (processedListOne == null)
				processedListOne = new ArrayList<>();
			processedListOne = processHierarchyData(vast, 1, processedListOne);
			hierarchyMap.put(vast.getVastTierOneContactEid(), processedListOne);
		}

		if (vast.getVastTierTwoContactEid() != null) {
			List<String> processedListTwo = hierarchyMap.get(vast.getVastTierTwoContactEid());
			if (processedListTwo == null)
				processedListTwo = new ArrayList<>();
			processedListTwo = processHierarchyData(vast, 2, processedListTwo);
			hierarchyMap.put(vast.getVastTierTwoContactEid(), processedListTwo);
		}

		if (vast.getVastTierThreeContactEid() != null) {
			List<String> processedListThree = hierarchyMap.get(vast.getVastTierThreeContactEid());
			if (processedListThree == null)
				processedListThree = new ArrayList<>();
			processedListThree = processHierarchyData(vast, 3, processedListThree);
			hierarchyMap.put(vast.getVastTierThreeContactEid(), processedListThree);
		}

		if (vast.getVastTierFourContactEid() != null) {
			List<String> processedListFour = hierarchyMap.get(vast.getVastTierFourContactEid());
			if (processedListFour == null)
				processedListFour = new ArrayList<>();
			processedListFour = processHierarchyData(vast, 4, processedListFour);
			hierarchyMap.put(vast.getVastTierFourContactEid(), processedListFour);
		}

		if (vast.getVastTierFiveContactEid() != null) {
			List<String> processedListFive = hierarchyMap.get(vast.getVastTierFiveContactEid());
			if (processedListFive == null)
				processedListFive = new ArrayList<>();
			processedListFive = processHierarchyData(vast, 5, processedListFive);
			hierarchyMap.put(vast.getVastTierFiveContactEid(), processedListFive);
		}

		if (vast.getVastTierSixContactEid() != null) {
			List<String> processedListSix = hierarchyMap.get(vast.getVastTierSixContactEid());
			if (processedListSix == null)
				processedListSix = new ArrayList<>();
			processedListSix = processHierarchyData(vast, 6, processedListSix);
			hierarchyMap.put(vast.getVastTierSixContactEid(), processedListSix);
		}
		return hierarchyMap;
	}

	private List<String> processHierarchyData(Vast vast, int i, List<String> processedList) {
		List<String> processedAccessList = processedList;
		switch (i) {
		case 1:
			processedAccessList = addDataToList(processedAccessList, vast.getVastTierTwoContactEid());
			processedAccessList = addDataToList(processedAccessList, vast.getVastTierThreeContactEid());
			processedAccessList = addDataToList(processedAccessList, vast.getVastTierFourContactEid());
			processedAccessList = addDataToList(processedAccessList, vast.getVastTierFiveContactEid());
			processedAccessList = addDataToList(processedAccessList, vast.getVastTierSixContactEid());
			processedAccessList = addDataToList(processedAccessList, vast.getVastTierSevenContactEid());
			break;
		case 2:
			processedAccessList = addDataToList(processedAccessList, vast.getVastTierThreeContactEid());
			processedAccessList = addDataToList(processedAccessList, vast.getVastTierFourContactEid());
			processedAccessList = addDataToList(processedAccessList, vast.getVastTierFiveContactEid());
			processedAccessList = addDataToList(processedAccessList, vast.getVastTierSixContactEid());
			processedAccessList = addDataToList(processedAccessList, vast.getVastTierSevenContactEid());
			break;
		case 3:
			processedAccessList = addDataToList(processedAccessList, vast.getVastTierFourContactEid());
			processedAccessList = addDataToList(processedAccessList, vast.getVastTierFiveContactEid());
			processedAccessList = addDataToList(processedAccessList, vast.getVastTierSixContactEid());
			processedAccessList = addDataToList(processedAccessList, vast.getVastTierSevenContactEid());
			break;
		case 4:
			processedAccessList = addDataToList(processedAccessList, vast.getVastTierFiveContactEid());
			processedAccessList = addDataToList(processedAccessList, vast.getVastTierSixContactEid());
			processedAccessList = addDataToList(processedAccessList, vast.getVastTierSevenContactEid());
			break;
		case 5:
			processedAccessList = addDataToList(processedAccessList, vast.getVastTierSixContactEid());
			processedAccessList = addDataToList(processedAccessList, vast.getVastTierSevenContactEid());
			break;
		case 6:
			processedAccessList = addDataToList(processedAccessList, vast.getVastTierSevenContactEid());
			break;
		default:
			break;
		}
		return processedAccessList;
	}

	private List<String> addDataToList(List<String> processedList, String eid) {
		if (!processedList.contains(eid))
			processedList.add(eid);
		return processedList;
	}

	private Date getISODateTime(Long lastScanned) {
		if (lastScanned == null)
			return null;
		return new Date(lastScanned);
	}

	private Boolean processApplicationDetails(List<ApplicationDetails> applicationDetailsList) {
		if (!applicationDetailsList.isEmpty()) {
			applicationDetailsRepository.deleteAll();
			applicationDetailsRepository.save(applicationDetailsList);
		}
		return true;
	}

	private Map<String, Object> getExecutiveList(String name, String appId, String businessUnit, String title) {
		Map<String, Object> executiveList = new HashMap<>();
		int index = name.lastIndexOf(' ');
		String processedTitle = processedTitle(title);
		if (index > 0 && processedTitle != null) {
			executiveList.put(FIRSTNAME, name.substring(0, index));
			executiveList.put(LASTNAME, name.substring(index + 1, name.length()));
			executiveList.put(ROLE, processedTitle);
			List<String> appList = new ArrayList<>();
			appList.add(appId);
			List<String> businessUnits = new ArrayList<>();
			businessUnits.add(businessUnit);
			executiveList.put(APPLIST, appList);
			executiveList.put(BUNITS, businessUnits);
			if (confAppIds.contains(appId)) {
				List<String> confAppList = new ArrayList<>();
				confAppList.add(appId);
				executiveList.put(CONFAPPLIST, confAppList);
			}
			return executiveList;
		}
		return null;
	}

	private Boolean isSeniorLeader(String title) {
		if (title != null) {
			if (title.startsWith("SVP"))
				return true;
			if (title.startsWith("VP"))
				return true;
			if (title.startsWith("EVP"))
				return true;
			if (title.startsWith("CEO"))
				return true;
			if (title.startsWith(CHIEFNTWK))
				return true;
			if (title.startsWith(CHAIRMAN))
				return true;
		}
		return false;
	}

	/**
	 * processedTitle
	 * 
	 * @param title
	 * @return String
	 */
	public String processedTitle(String title) {
		if (title == null)
			return null;
		if (title.startsWith("SVP"))
			return title;
		if (title.startsWith("VP"))
			return title;
		if (title.startsWith("EVP"))
			return title;
		if (title.startsWith("Exec Dir"))
			return title;
		if (title.startsWith(CHIEFNTWK))
			return title;
		if (title.startsWith("CEO"))
			return title;
		if (title.startsWith(CHAIRMAN))
			return title;
		if (title.startsWith("Dir"))
			return title;
		if (title.startsWith("Assoc Dir"))
			return title;
		return null;
	}

	private Map<String, Object> getUpdatedExecutiveList(Map<String, Object> executives, String eid, String appId,
			String businessUnit) {
		Map<String, Object> executiveList = getMapStringObject(executives.get(eid));
		if (executiveList != null) {
			List<String> appList = getListedString(executiveList.get(APPLIST));
			if (appList == null)
				appList = new ArrayList<>();
			appList.add(appId);
			executiveList.put(APPLIST, appList);

			List<String> businessUnits = getListedString(executiveList.get(BUNITS));
			if (businessUnits == null) {
				businessUnits = new ArrayList<>();
			} else if (!businessUnits.isEmpty() && !businessUnits.contains(businessUnit)) {
				businessUnits.add(businessUnit);
			}
			executiveList.put(BUNITS, businessUnits);

			if (confAppIds.contains(appId)) {
				List<String> confAppList = getListedString(executiveList.get(CONFAPPLIST));
				if (confAppList == null)
					confAppList = new ArrayList<>();
				confAppList.add(appId);
				executiveList.put(CONFAPPLIST, confAppList);
			}
		}
		return executiveList;
	}

	private void getProcessedDetails(Map<String, Object> executives) {
		Long lastUpdated = System.currentTimeMillis();
		for (Map.Entry<String, Object> entry : executives.entrySet()) {
			ExecutiveSummaryList executiveSummaryList = executiveSummaryListRepository.findByEid(entry.getKey());
			Map<String, Object> executiveList = getMapStringObject(entry.getValue());
			if (executiveSummaryList == null)
				executiveSummaryList = new ExecutiveSummaryList();
			if (executiveList != null) {
				executiveSummaryList.setEid(entry.getKey());
				executiveSummaryList.setFirstName(getString(executiveList.get(FIRSTNAME)));
				executiveSummaryList.setLastName(getString(executiveList.get(LASTNAME)));
				executiveSummaryList.setRole(getString(executiveList.get(ROLE)));
				executiveSummaryList.setSeniorExecutive(isSeniorLeader(getString(executiveList.get(ROLE))));
				executiveSummaryList.setAppId(getListedString(executiveList.get(APPLIST)));
				executiveSummaryList.setBusinessUnits(getListedString(executiveList.get(BUNITS)));
				executiveSummaryList.setConfiguredAppId(getListedString(executiveList.get(CONFAPPLIST)));
				executiveSummaryList.setConfiguredApps(getListCount(executiveList.get(CONFAPPLIST)));
				executiveSummaryList.setTotalApps(getListCount(executiveList.get(APPLIST)));
				executiveSummaryList.setAppDetails(processAppDetails(getListedString(executiveList.get(APPLIST))));
				executiveSummaryList.setAppDetailsWithBunit(
						processAppDetailsWithBunit(getListedString(executiveList.get(APPLIST))));
				executiveSummaryList.setReportingPercentage(getReportingPercentage(
						getListCount(executiveList.get(APPLIST)), getListCount(executiveList.get(CONFAPPLIST))));
				executiveSummaryList.setLastUpdated(lastUpdated);
				executiveSummaryListRepository.save(executiveSummaryList);
			}
		}
		List<ExecutiveSummaryList> executiveSummaryList = executiveSummaryListRepository.getNotUpdatedList(lastUpdated);
		if (executiveSummaryList != null && !executiveSummaryList.isEmpty()) {
			executiveSummaryListRepository.delete(executiveSummaryList);
		}
	}

	private Map<String, Map<String, String>> processAppDetailsWithBunit(List<String> appList) {
		Map<String, Map<String, String>> appDetailsWithBunit = new HashMap<>();
		for (String appId : appList) {
			ApplicationDetails app = applicationDetailsRepository.findByAppId(appId);
			if (app != null && app.getAppName() != null) {
				String lob = app.getLob();
				Map<String, String> appDetails = appDetailsWithBunit.get(lob);
				if (appDetails != null) {
					appDetails.put(appId, appId + " : " + app.getAppName());
					appDetailsWithBunit.put(lob, appDetails);
				} else {
					appDetails = new HashMap<>();
					appDetails.put(appId, appId + " : " + app.getAppName());
					appDetailsWithBunit.put(lob, appDetails);
				}
			}
		}
		return appDetailsWithBunit;
	}

	private Map<String, String> processAppDetails(List<String> appList) {
		Map<String, String> appDetails = new HashMap<>();
		for (String appId : appList) {
			ApplicationDetails app = applicationDetailsRepository.findByAppId(appId);
			if (app != null && app.getAppName() != null)
				appDetails.put(appId, appId + " : " + app.getAppName());
		}
		return appDetails;
	}

	private Double getReportingPercentage(Integer appCount, Integer confCount) {
		if (appCount > 0 && confCount > 0)
			return ((double) confCount * 100) / ((double) appCount);
		return (double) 0;
	}

	private Integer getListCount(Object value) {
		if (value != null) {
			List<String> list = (List<String>) value;
			return list.size();
		}
		return 0;
	}

	private String getString(Object value) {
		if (value != null)
			return value.toString();
		return null;
	}

	private List<String> getListedString(Object value) {
		if (value != null) {
			List<String> uniqueList = (List<String>) value;
			return uniqueList.stream().distinct().collect(Collectors.toList());
		}
		return null;
	}

	private Map<String, Object> getMapStringObject(Object value) {
		if (value != null)
			return (Map<String, Object>) value;
		return null;
	}

	/**
	 * processPortfolioResponse
	 * 
	 * @return Boolean
	 */
	public Boolean processPortfolioResponse() {

		List<ExecutiveSummaryList> responseList = (List<ExecutiveSummaryList>) executiveSummaryListRepository.findAll();
		List<PortfolioResponse> portfolioResponseList = new ArrayList<>();
		Long lastUpdated = System.currentTimeMillis();

		for (ExecutiveSummaryList executiveSummary : responseList) {
			PortfolioResponse portfolioResponse = portfolioResponseRepository.findByEid(executiveSummary.getEid());
			if (portfolioResponse == null)
				portfolioResponse = new PortfolioResponse();

			portfolioResponse.setEid(executiveSummary.getEid());
			ExecutiveResponse executiveResponse = new ExecutiveResponse();
			executiveResponse.setFirstName(executiveSummary.getFirstName());
			executiveResponse.setLastName(executiveSummary.getLastName());
			executiveResponse.setRole(executiveSummary.getRole());
			portfolioResponse.setOrder(getHierarchy(executiveSummary.getRole()));
			portfolioResponse.setExecutive(executiveResponse);
			portfolioResponse.setLastUpdated(lastUpdated);
			portfolioResponseList.add(portfolioResponse);
		}

		if (!portfolioResponseList.isEmpty())
			portfolioResponseRepository.save(portfolioResponseList);

		List<PortfolioResponse> portfolioResponseNotUpdatedList = portfolioResponseRepository
				.getNotUpdatedList(lastUpdated);
		if (portfolioResponseNotUpdatedList != null && !portfolioResponseNotUpdatedList.isEmpty()) {
			portfolioResponseRepository.delete(portfolioResponseNotUpdatedList);
		}
		return true;

	}

	private Integer getHierarchy(String title) {
		if (title == null)
			return -1;
		if (title.startsWith("SVP"))
			return 4;
		if (title.startsWith("VP"))
			return 5;
		if (title.startsWith("EVP"))
			return 3;
		if (title.startsWith("Exec Dir"))
			return 6;
		if (title.startsWith(CHAIRMAN))
			return 0;
		if (title.startsWith("CEO"))
			return 1;
		if (title.startsWith(CHIEFNTWK))
			return 2;
		if (title.startsWith("Dir"))
			return 7;
		if (title.startsWith("Assoc Dir"))
			return 8;
		return -1;
	}

	/**
	 * processCollectorStatus
	 * 
	 * @return Boolean
	 */
	public Boolean processCollectorStatus() {
		List<CollectorType> collectorsList = new ArrayList<>();
		collectorsList.add(CollectorType.JiraUserStory);
		collectorsList.add(CollectorType.MTTR);
		collectorsList.add(CollectorType.Security);
		collectorsList.add(CollectorType.SCM);
		MongoClient client = null;
		try {
			client = vastDetailsDAO.getMongoClient();
			for (CollectorType type : collectorsList) {
				CollectorStatus collectorStatus = collectorStatusRepository.findByType(type);
				if (collectorStatus == null)
					collectorStatus = new CollectorStatus();
				collectorStatus.setCollectorName(type + "");
				collectorStatus.setType(type);
				collectorStatus.setLastUpdated(collectorDetailsDAO.getCollectorLastRun(type, client));
				collectorStatusRepository.save(collectorStatus);
			}
		} catch (Exception e) {
			LOG.info("Error in processCollectorStatus() (VastAnalysis Class) : ", e);
		} finally {
			if (client != null)
				client.close();
		}
		return true;
	}

	/**
	 * processVastDirectReportees
	 * 
	 * @return Boolean
	 */
	public Boolean processVastDirectReportees() {
		MongoClient client = null;
		try {
			client = vastDetailsDAO.getMongoClient();
			for (int i = 1; i < 7; i++) {
				List<String> eids = vastDetailsDAO.getVastEids(client, i);
				if (eids != null && !eids.isEmpty()) {
					for (String eid : eids) {
						List<String> directReportings = vastDetailsDAO.getVastEidsForEid(client, i + 1, eid);
						if (directReportings != null && !directReportings.isEmpty())
							saveDirectReportings(eid, directReportings);
					}
				}
			}
		} catch (Exception e) {
			LOG.info("Error in processVastDirectReportees() (VastAnalysis Class) : ", e);
		} finally {
			if (client != null)
				client.close();
		}

		return true;

	}

	private void saveDirectReportings(String eid, List<String> directReportings) {
		ExecutiveHierarchy executiveHierarchy = executiveHierarchyRepository.findByEid(eid);
		if (executiveHierarchy != null) {
			List<String> reportings = executiveHierarchy.getDirectReportees();
			if (reportings != null && !reportings.isEmpty())
				directReportings.addAll(reportings);
			directReportings = directReportings.stream().distinct().collect(Collectors.toList());
			executiveHierarchy.setDirectReportees(directReportings);
			List<String> allReportings = new ArrayList<>();
			Map<String, List<String>> reporting = executiveHierarchy.getReportees();
			if (reporting != null && !reporting.isEmpty()) {

				for (Entry<String, List<String>> entry : reporting.entrySet()) {
					allReportings.addAll(entry.getValue());
				}

				allReportings = allReportings.stream().distinct().collect(Collectors.toList());
			}
			PortfolioResponse response = portfolioResponseRepository.findByEid(eid);
			if (response != null)
				allReportings = processDirectReportings(response.getOrder(), allReportings);
			executiveHierarchy.setLinkedReportees(allReportings);
			executiveHierarchyRepository.save(executiveHierarchy);
		}
	}

	private List<String> processDirectReportings(Integer order, List<String> directReportings) {
		List<Integer> orderList = new ArrayList<>();
		switch (order) {
		case 0:
			orderList.add(0);
			orderList.add(1);
			orderList.add(2);
			orderList.add(3);
			return getSeniorExecutives(orderList, directReportings);
		case 1:
			orderList.add(1);
			orderList.add(2);
			orderList.add(3);
			orderList.add(4);
			return getSeniorExecutives(orderList, directReportings);
		case 2:
			orderList.add(2);
			orderList.add(3);
			orderList.add(4);
			return getSeniorExecutives(orderList, directReportings);
		case 3:
			orderList.add(4);
			orderList.add(5);
			orderList.add(3);
			orderList.add(6);
			return getSeniorExecutives(orderList, directReportings);
		case 4:
			orderList.add(4);
			orderList.add(5);
			orderList.add(6);
			return getSeniorExecutives(orderList, directReportings);
		case 5:
			orderList.add(5);
			orderList.add(6);
			return getSeniorExecutives(orderList, directReportings);
		case 6:
			orderList.add(6);
			return getSeniorExecutives(orderList, directReportings);
		default:
			return directReportings;
		}
	}

	private List<String> getSeniorExecutives(List<Integer> orderList, List<String> directReportings) {
		Query basicQuery = new Query();
		basicQuery.addCriteria(Criteria.where("order").in(orderList));
		basicQuery.addCriteria(Criteria.where("eid").in(directReportings));
		return mongoTemplate.getCollection("portfolio_response").distinct("eid", basicQuery.getQueryObject());
	}

	/**
	 * processCollectorStatus
	 * 
	 * @param.. @return Boolean
	 */
	public Boolean processCollectorUpdatedTimestamp() {
		List<CollectorUpdatedDetails> collectorStatusList = new ArrayList<>();
		MongoClient client = null;

		try {
			collectorStatusList.add(
					new CollectorUpdatedDetails("feature_userstory", CollectorType.JiraUserStory, "timestamp", APPID)); // 1
			collectorStatusList.add(new CollectorUpdatedDetails("service_now_new", CollectorType.ServiceNow,
					"lastUpdated", "applicationName"));// 2
			collectorStatusList.add(
					new CollectorUpdatedDetails("devopscup_lead_time", CollectorType.DevopscupLeadTime, "", "appId")); // 3
			collectorStatusList
					.add(new CollectorUpdatedDetails("security_code_scans", CollectorType.Security, TIMESTAMP, APPID));// 4
			collectorStatusList.add(new CollectorUpdatedDetails("security_formulated_data", CollectorType.Security,
					"timestamp", APPID));// 5
			collectorStatusList
					.add(new CollectorUpdatedDetails("security_web_assess", CollectorType.Security, TIMESTAMP, APPID));// 6
			collectorStatusList.add(new CollectorUpdatedDetails("mttr", CollectorType.MTTR, "eventStartDT", APPID));// 7
			// collectorStatusList.add(new CollectorUpdatedDetails("cmis",
			// CollectorType.CMIS, UPDATEDDATE,APPID));//8
			collectorStatusList
					.add(new CollectorUpdatedDetails("code_quality", CollectorType.CodeQuality, TIMESTAMP, APPID));// 9
			collectorStatusList.add(new CollectorUpdatedDetails("builds", CollectorType.Build, "timestamp", APPID));// 10
			collectorStatusList
					.add(new CollectorUpdatedDetails("commits", CollectorType.SCM, "scmCommitTimestamp", APPID));// 12
			collectorStatusList.add(
					new CollectorUpdatedDetails("ebs", CollectorType.CloudCustodian, UPDATEDDATE, "ebsVastAcronym"));
			collectorStatusList.add(
					new CollectorUpdatedDetails("ami", CollectorType.CloudCustodian, UPDATEDDATE, "amiVastAcronym"));
			collectorStatusList
					.add(new CollectorUpdatedDetails("s3", CollectorType.CloudCustodian, UPDATEDDATE, "s3VastAcronym"));
			collectorStatusList
					.add(new CollectorUpdatedDetails("elb", CollectorType.CloudCustodian, UPDATEDDATE, "vastAcronym"));

			client = vastDetailsDAO.getMongoClient();

			for (CollectorUpdatedDetails itr : collectorStatusList) {

				CollectorType currentCollector = itr.getType();
				String collectionName = itr.getCollectionName();
				String fieldName = itr.getUpdatedTimeField();
				String appFieldName = itr.getAppIdFieldName();
				CollectorUpdatedDetails collectorStatus = collectorUpdatedDetailsRepository
						.findByCollectionNameAndType(collectionName, currentCollector);

				if (collectorStatus == null)
					collectorStatus = itr;

				collectorStatus.setType(currentCollector);
				collectorStatus.setCollectionName(collectionName);
				collectorStatus.setCollectorUpdateTime(
						collectorDetailsDAO.getCollectorLastRunInLong(currentCollector, client));

				long noOfApp = 0;
				long updatedTime = 0;
				if (!fieldName.isEmpty()) {
					updatedTime = vastDetailsDAO.getLastUpdatedDate(collectionName, fieldName, client);
					noOfApp = vastDetailsDAO.getLastUpdatedAppCount(collectionName, fieldName, client, appFieldName);
				}
				collectorStatus.setCollectionUpdatedTime(updatedTime);
				collectorStatus.setAppCount(noOfApp);
				collectorUpdatedDetailsRepository.save(collectorStatus);
			}
			LOG.info("Completed processCollectorUpdatedTimestamp");
		} catch (Exception e) {
			LOG.info("Error in processCollectorUpdatedTimestamp() (VastAnalysis Class) :  ", e);
		} finally {
			if (client != null)
				client.close();
		}
		return true;
	}

	/**
	 * updateCollectorStatus
	 * 
	 * @param metricsName,@param
	 *            collectorStartTime
	 * @return Boolean
	 */
	public Boolean updateCollectorUpdatedTimestamp(String metricsName, Long collectorStartTime) {
		Long collectorUpdateTime = getTimeStampUTC();
		Long totalExecutionTime = collectorUpdateTime - collectorStartTime;
		CollectorUpdatedDetails collectorStatusForMetricProcessor = collectorUpdatedDetailsRepository
				.findByCollectionNameAndType(metricsName, CollectorType.MetricsProcessor);

		collectorStatusForMetricProcessor.setCollectorUpdateTime(collectorUpdateTime);
		collectorStatusForMetricProcessor.setCollectionUpdatedTime(0L);
		collectorStatusForMetricProcessor.setAppCount(0L);
		collectorStatusForMetricProcessor.setIsRunning(true);
		collectorStatusForMetricProcessor.setTotalExecutionTime(totalExecutionTime / 60000);
		collectorStatusForMetricProcessor.setCollectorStartTime(collectorStartTime);
		collectorUpdatedDetailsRepository.save(collectorStatusForMetricProcessor);
		return true;
	}

	/**
	 * @return Long timeStamp
	 */
	private static Long getTimeStampUTC() {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		return calendar.getTimeInMillis();
	}

	public Boolean copyVastDetails() {
		if (isVastUpdate) {
			LOG.info("Copying Vast Details   . . . . ");
			MongoClient client = null;
			try {
				/** Connecting to OneHygiea DB */
				client = vastDetailsDAO.getMongoClient();
				/** delete all entries from Executive Hyg DB **/
				vastRepository.deleteAll();
				/** getting details From OneHygieia DB **/
				List<Vast> vastDetails = vastDetailsDAO.getAllDetails(client);
				if (vastDetails != null) {
					vastRepository.save(vastDetails);
					isVastUpdate = false;
				}
			} catch (Exception ex) {
				LOG.error("Exception while fetching Vast data :: " + ex);
				return false;
			} finally {
				if (client != null) {
					client.close();
				}
			}
			LOG.info("Completed copying Vast Details");
		}
		return true;
	}

	/**
	 * updateCollectorStatus()
	 * 
	 * @param metricsName,@param
	 *            isRunning, @param timestamp
	 * @return Boolean
	 */
	public Boolean updateCollectorStatus(String metricsName, Long timestamp, boolean isRunning) {
		if (isRunning) {
			try {
				CollectorUpdatedDetails collectorStatusForMetricProcessor = new CollectorUpdatedDetails(metricsName,
						CollectorType.MetricsProcessor, "", "");

				collectorStatusForMetricProcessor.setCollectionUpdatedTime(0L);
				collectorStatusForMetricProcessor.setAppCount(0L);
				collectorStatusForMetricProcessor.setIsRunning(true);
				collectorStatusForMetricProcessor.setCollectorStartTime(timestamp);
				collectorUpdatedDetailsRepository.save(collectorStatusForMetricProcessor);
			} catch (Exception e) {
				LOG.info("Error in updateCollectorStatus " + e);
			}
		} else {
			try {
				CollectorUpdatedDetails collectorStatusForMetricProcessor = collectorUpdatedDetailsRepository
						.findByCollectionNameAndTypeOrderByCollectorStartTimeDesc(metricsName,
								CollectorType.MetricsProcessor);
				if (collectorStatusForMetricProcessor != null) {
					Long collectorUpdateTime = getTimeStampUTC();
					Long totalExecutionTime = collectorUpdateTime
							- collectorStatusForMetricProcessor.getCollectorStartTime();
					collectorStatusForMetricProcessor.setAppCount((long) vastDetailsDAO.getAppCount(metricsName));
					collectorStatusForMetricProcessor.setCollectorUpdateTime(collectorUpdateTime);
					collectorStatusForMetricProcessor.setTotalExecutionTime(totalExecutionTime / 60000);
					collectorStatusForMetricProcessor.setIsRunning(false);
					collectorUpdatedDetailsRepository.save(collectorStatusForMetricProcessor);
				}
			} catch (Exception e) {
				LOG.info("Error in updateCollectorStatus " + e);
			}
		}
		return true;
	}
}