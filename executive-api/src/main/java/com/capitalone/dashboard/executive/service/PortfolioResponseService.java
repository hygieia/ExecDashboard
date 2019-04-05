package com.capitalone.dashboard.executive.service;

import java.util.List;
import java.util.Map;

import com.capitalone.dashboard.exec.model.vz.PortfolioResponse;

/**
 * interface PortfolioResponseService
 */
public interface PortfolioResponseService {

	List<PortfolioResponse> findAll();

	PortfolioResponse findById(String id);

	List<String> getAllBusinessUnits();

	Map<String, String> getAllApplicationList();

	Map<String, String> getAllApplicationListForLob(String lob);

	List<PortfolioResponse> findByBusinessUnit(String businessUnit);

	Map<String, String> getExecutivesLists(String businessUnit);

	List<PortfolioResponse> findByExecutivesHierarchy(String eid, String businessUnit);

	List<String> getConfigApps(String portfolioId);

	Map<String, String> getExecutivesListsAll(String businessUnit);

	Map<String, String> getReportings(String eid);

	List<String> getAllBusinessUnitsForExec(String eid);

	Map<String, String> getApplicationListForExec(String id);

	Map<String, String> getApplicationListForExecWithPortfolio(String businessUnit, String id);

}
