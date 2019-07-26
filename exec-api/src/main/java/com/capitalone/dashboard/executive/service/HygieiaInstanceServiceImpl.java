package com.capitalone.dashboard.executive.service;

import com.capitalone.dashboard.exec.model.Dashboard;
import com.capitalone.dashboard.exec.model.HygieiaArtifactDetails;
import com.capitalone.dashboard.exec.model.Instance;
import com.capitalone.dashboard.exec.model.PatchRequest;
import com.capitalone.dashboard.exec.model.SoftwareVersion;
import com.capitalone.dashboard.exec.repository.DashboardRepository;
import com.capitalone.dashboard.exec.repository.HygieiaArtifactDetailsRepository;
import com.capitalone.dashboard.exec.repository.InstanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * HygieiaInstanceServiceImpl class
 *
 */
@Service
public class HygieiaInstanceServiceImpl implements HygieiaInstanceService {

	private final InstanceRepository instanceRepository;
	private final HygieiaArtifactDetailsRepository hygieiaArtifactDetailsRepository;
	private final DashboardRepository dashboardRepository;
	private final MongoTemplate mongoTemplate;
	private static final String METRICS = ":METRICS";
	private static final Logger LOG = LoggerFactory.getLogger(UserInfoServiceImpl.class);
	private static final String API = "API";

	/**
	 * 
	 * @param instanceRepository
	 * @param hygieiaArtifactDetailsRepository
	 * @param dashboardRepository
	 * @param mongoTemplate
	 */
	@Autowired
	public HygieiaInstanceServiceImpl(InstanceRepository instanceRepository,
			HygieiaArtifactDetailsRepository hygieiaArtifactDetailsRepository, DashboardRepository dashboardRepository,
			MongoTemplate mongoTemplate) {
		this.instanceRepository = instanceRepository;
		this.hygieiaArtifactDetailsRepository = hygieiaArtifactDetailsRepository;
		this.dashboardRepository = dashboardRepository;
		this.mongoTemplate = mongoTemplate;
	}

	/**
	 * @return SoftwareVersion
	 */
	@Override
	public SoftwareVersion getPatchVersions(String bunit) {
		StringBuilder strMetrics = new StringBuilder();
		StringBuilder strApi = new StringBuilder();
		SoftwareVersion softwareVersionDetails = new SoftwareVersion();
		List<Dashboard> dashboardList;
		if ("All".equalsIgnoreCase(bunit)) {
			dashboardList = dashboardRepository.findAll();
		} else {
			dashboardList = dashboardRepository.getByBunitDetails(bunit);
		}
		List<HygieiaArtifactDetails> hygieiaArtifactDetailsList = (List<HygieiaArtifactDetails>) hygieiaArtifactDetailsRepository
				.findAll();
		List<PatchRequest> patchDetails = new ArrayList<>();

		int totalCount = 0;
		int updatedCount = 0;
		for (HygieiaArtifactDetails hygieiaArtifactDetails : hygieiaArtifactDetailsList) {
			if ("CollectorArtifactory".equalsIgnoreCase(hygieiaArtifactDetails.getArtifactName()))
				strApi.append(API + hygieiaArtifactDetails.getVersion());

			if ("InstanceMetricsArtifactory".equalsIgnoreCase(hygieiaArtifactDetails.getArtifactName()))
				strMetrics.append(METRICS + hygieiaArtifactDetails.getVersion());

		}

		String standardVersion = strApi.toString() + strMetrics.toString();
		if (dashboardList != null) {
			for (Dashboard dashboard : dashboardList) {
				PatchRequest patchRequest = new PatchRequest();
				if (dashboard.getInstance() != null) {
					patchRequest.setAppId(dashboard.getAppId());
					patchRequest.setAppName(dashboard.getTitle());
					Instance instance = instanceRepository.findByPrivateIpAndActive(dashboard.getInstance(), true);
					if (instance != null) {
						String instanceVersion = API + instance.getArtifactCollectorVersion() + METRICS
								+ instance.getArtifactInstanceVersion();
						if (instance.getArtifactCollectorVersion() != null) {
							patchRequest.setVersion(instanceVersion);
						} else {
							patchRequest.setVersion("Not Available");
						}
						if (standardVersion.equalsIgnoreCase(instanceVersion)) {
							patchRequest.setLatestVersion(true);
							updatedCount++;
						}
						totalCount++;
						patchRequest.setPrivateIp(dashboard.getInstance());
						patchDetails.add(patchRequest);
					}
				}
			}
		}
		softwareVersionDetails.setPatch(patchDetails);
		softwareVersionDetails.setStandardVersion(standardVersion);
		softwareVersionDetails.setTotalCount(totalCount);
		softwareVersionDetails.setUpdatedCount(updatedCount);

		return softwareVersionDetails;
	}

	/**
	 * @return Map
	 */
	@Override
	public Map<String, Object> getPatchVersionsByInstance(String instanceIP, String check) {
		StringBuilder strMetrics = new StringBuilder();
		StringBuilder strApi = new StringBuilder();
		Map<String, Object> patchDetailsMap = new HashMap<>();
		Dashboard dashboardIP = dashboardRepository.getDashboardByInstanceDetails(instanceIP);
		List<HygieiaArtifactDetails> hygieiaArtifactDetailsList = (List<HygieiaArtifactDetails>) hygieiaArtifactDetailsRepository
				.findAll();
		List<PatchRequest> patchDetails = new ArrayList<>();

		for (HygieiaArtifactDetails hygieiaArtifactDetails : hygieiaArtifactDetailsList) {
			if ("CollectorArtifactory".equalsIgnoreCase(hygieiaArtifactDetails.getArtifactName()))
				strApi.append(API + hygieiaArtifactDetails.getVersion());

			if ("InstanceMetricsArtifactory".equalsIgnoreCase(hygieiaArtifactDetails.getArtifactName()))
				strMetrics.append(METRICS + hygieiaArtifactDetails.getVersion());

		}

		String standardVersion = strApi.toString() + strMetrics.toString();
		PatchRequest patchRequest = new PatchRequest();
		if (dashboardIP != null) {
			if (dashboardIP.getInstance() != null) {
				patchRequest.setAppId(dashboardIP.getAppId());
				patchRequest.setAppName(dashboardIP.getApplication().getName());
				Instance instance = instanceRepository.findByPrivateIpAndActive(dashboardIP.getInstance(), true);
				if (instance != null) {
					String instanceVersion = API + instance.getArtifactCollectorVersion() + METRICS
							+ instance.getArtifactInstanceVersion();
					if (instance.getArtifactCollectorVersion() != null) {
						patchRequest.setVersion(instanceVersion);
					} else {
						patchRequest.setVersion("Not Available");
					}
					if (standardVersion.equalsIgnoreCase(instanceVersion)) {
						patchRequest.setLatestVersion(true);
					}
					patchRequest.setPrivateIp(dashboardIP.getInstance());
					patchDetails.add(patchRequest);
				}
			}

			patchDetailsMap.put("StandardVersion", standardVersion);
			patchDetailsMap.put("appId", patchRequest.getAppId());
			patchDetailsMap.put("appName", patchRequest.getAppName());
			patchDetailsMap.put("instanceVersion", patchRequest.getVersion());
			patchDetailsMap.put("instanceIP", patchRequest.getPrivateIp());
			patchDetailsMap.put("isLatestVersion", patchRequest.isLatestVersion());
		} else {
			String error = "IP not available in Dashboard table";
			patchDetailsMap.put("Error", error);
		}
		return patchDetailsMap;
	}

	/**
	 * @return List
	 */
	@Override
	public List<String> getBusinessUnits() {
		return mongoTemplate.getCollection("dashboards").distinct("businessUnit");
	}

	/**
	 * 
	 * @param instanceIP
	 * @return Boolean
	 */
	public Boolean checkApiOnline(String instanceIP) {
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL("http://" + instanceIP + ":8080/api/ping")
					.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(1000);
			connection.setReadTimeout(1000);
			if (connection.getResponseCode() == 200) {
				return true;
			}
		} catch (Exception e) {
			LOG.error("Exception in checkAPI Online:: " + e);
		}
		return false;
	}

}
