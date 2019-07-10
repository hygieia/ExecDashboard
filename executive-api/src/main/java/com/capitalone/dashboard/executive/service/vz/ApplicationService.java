package com.capitalone.dashboard.executive.service.vz;

import java.util.List;

import com.capitalone.dashboard.exec.model.vz.ApplicationDetails;

/**
 * interface ApplicationService
 */
@FunctionalInterface
public interface ApplicationService {

	/**
	 * 
	 * @return List
	 */
	List<ApplicationDetails> getAllApplicationDetails();

}
