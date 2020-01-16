package com.capitalone.dashboard.executive.service;

import com.capitalone.dashboard.exec.model.ApplicationDetails;

import java.util.List;

/**
 * interface ApplicationService
 */
public interface ApplicationService {

	/**
	 * 
	 * @return List
	 */
	List<ApplicationDetails> getAllApplicationDetails();

}
