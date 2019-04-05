package com.capitalone.dashboard.custom;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.dao.ServiceNowDAO;
import com.capitalone.dashboard.exec.model.vz.ServiceNowTicket;
import com.mongodb.MongoClient;

/**
 * ServiceNowAnalysis
 * 
 * @param
 * @return
 * @author Hari
 */
@Component
@SuppressWarnings("PMD")
public class ServiceNowAnalysis {

	private final ServiceNowDAO serviceNowDAO;
	private static final Logger LOG = LoggerFactory.getLogger(ServiceNowAnalysis.class);

	/**
	 * ServiceNowAnalysis
	 * 
	 * @param serviceNowDAO
	 * @return
	 */
	@Autowired
	public ServiceNowAnalysis(ServiceNowDAO serviceNowDAO) {
		this.serviceNowDAO = serviceNowDAO;
	}

	/**
	 * getDeploymentCadence
	 * 
	 * @param appId
	 * @param client
	 * @return Double
	 */
	public Double getDeploymentCadence(String appId, MongoClient client) {
		try {
			List<ServiceNowTicket> snTickets = serviceNowDAO.getServiceNowTickets(appId, client);
			if (snTickets != null && !snTickets.isEmpty())
				return processDeploymentCadence(snTickets);

		} catch (Exception e) {
			LOG.info("Error in getting the Deployment Cadence :: " + e);
		}
		return 0.0;
	}

	/**
	 * getChangeActivity
	 * 
	 * @param appId
	 * @param client
	 * @return Double
	 */
	public Double getChangeActivity(String appId, MongoClient client) {
		try {
			List<ServiceNowTicket> snTickets = serviceNowDAO.getServiceNowTickets(appId, client);
			if (snTickets != null && !snTickets.isEmpty())
				return processChangeActivity(snTickets);

		} catch (Exception e) {
			LOG.info("Error in getting the Deployment Cadence :: " + e);
		}
		return 0.0;
	}

	private Double processChangeActivity(List<ServiceNowTicket> serviceNowTickets) {
		List<String> dates = new ArrayList<>();
		List<String> changeActivities = new ArrayList<>();
		List<ServiceNowTicket> rnTickets = new ArrayList<>();
		if (serviceNowTickets.size() > 1) {
			for (ServiceNowTicket sn : serviceNowTickets) {
				String date = sn.getStartdate() + "::" + sn.getEnddate();
				if (!dates.contains(date)) {
					rnTickets.add(sn);
					dates.add(date);
					changeActivities.add(sn.getNumber());
				}
			}
		}

		if (!rnTickets.isEmpty())
			return (double) (rnTickets.size());

		return 0.0;
	}

	private Double processDeploymentCadence(List<ServiceNowTicket> serviceNowTickets) {
		List<String> dates = new ArrayList<>();
		List<String> changeActivities = new ArrayList<>();
		List<ServiceNowTicket> rnTickets = new ArrayList<>();
		if (serviceNowTickets.size() > 1) {
			for (ServiceNowTicket sn : serviceNowTickets) {
				String date = sn.getStartdate() + "::" + sn.getEnddate();
				if (!dates.contains(date)) {
					rnTickets.add(sn);
					dates.add(date);
					changeActivities.add(sn.getNumber());
				}
			}
		}

		Long date = getDate(90);
		long count = 0;
		if (rnTickets.size() > 1) {
			for (ServiceNowTicket sn : rnTickets) {
				if (getDeploymentMinutes(date, convertStringToData(sn.getStartdate())) > 0)
					count = count + getDeploymentMinutes(date, convertStringToData(sn.getStartdate()));
				date = convertStringToData(sn.getEnddate());
			}
		}
		count = count + getDeploymentMinutes(date, getDate(0));

		if (count > 0 && !rnTickets.isEmpty())
			return (double) count / (rnTickets.size() * 60 * 24);

		return 0.0;
	}

	private Long getDeploymentMinutes(long startDateMS, long endDateMS) {
		long diffInMillies = endDateMS - startDateMS;
		return TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
	}

	/**
	 * convertStringToData()
	 * 
	 * @param strDate
	 * @return Long
	 */
	public Long convertStringToData(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = formatter.parse(strDate);
			return date.getTime();
		} catch (Exception e) {
			LOG.info("Error in psrsing date!" + e);
		}
		return (long) 0;
	}

	private Long getDate(int days) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			calendar.add(Calendar.DAY_OF_MONTH, -days);
			String strDate = DatatypeConverter.printDateTime(calendar).substring(0, 10);
			Date date = formatter.parse(strDate);
			return date.getTime();
		} catch (Exception e) {
			LOG.info("Error in getting 90 days date!" + e);
		}
		return (long) 0;
	}

}
