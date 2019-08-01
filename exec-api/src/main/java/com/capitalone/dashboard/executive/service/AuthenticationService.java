package com.capitalone.dashboard.executive.service;

import com.capitalone.dashboard.exec.model.AuthenticationRequest;

/**
 * interface AuthenticationService
 */
public interface AuthenticationService {

	/**
	 * @param AuthenticationRequest
	 *            request
	 * @return data
	 */
	String register(AuthenticationRequest request);

	/**
	 * @param String
	 *            eid
	 * @return data
	 */
	String getPortfolioId(String eid);
	
	/**
	 * @param String eid
	 * @return data
	 */
	Boolean isAdmin(String eid);


}