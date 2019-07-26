package com.capitalone.dashboard.executive.service;

import com.capitalone.dashboard.exec.model.Authentication;
import com.capitalone.dashboard.exec.model.AuthenticationRequest;
import com.capitalone.dashboard.exec.model.UserTrack;
import com.capitalone.dashboard.exec.repository.AuthenticationRepository;
import com.capitalone.dashboard.exec.repository.UserTrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	private AuthenticationRepository authenticationRepository;
	private UserTrackRepository userTrackRepository;
	// private PortfolioResponseRepository portfolioResponseRepository;

	/**
	 * 
	 * @param authenticationRepository
	 * @param userTrackRepository
	 * @param portfolioResponseRepository
	 */
	@Autowired
	public AuthenticationServiceImpl(AuthenticationRepository authenticationRepository,
			UserTrackRepository userTrackRepository) {
		// UserTrackRepository userTrackRepository, PortfolioResponseRepository
		// portfolioResponseRepository) {
		this.authenticationRepository = authenticationRepository;
		this.userTrackRepository = userTrackRepository;
		// this.portfolioResponseRepository = portfolioResponseRepository;
	}

	/**
	 * @param request
	 * @return string
	 */
	@Override
	public String register(AuthenticationRequest request) {

		Authentication currentUser = authenticationRepository.findByEid(request.getEid());
		long loggededTime = System.currentTimeMillis();
		if (currentUser == null) {
			Authentication newUser = new Authentication();

			newUser.setEid(request.getEid());
			newUser.setEmail(request.getEmail());
			newUser.setFirstname(request.getFirstname());
			newUser.setLastname(request.getLastname());
			newUser.setUsername(request.getUsername());
			newUser.setLastLoggedin(loggededTime);
			newUser.setIsAdmin(false);

			authenticationRepository.save(newUser);
			logUser(request.getEid(), loggededTime);
			return "New User Created";

		} else {

			logUser(request.getEid(), loggededTime);
			currentUser.setLastLoggedin(loggededTime);
			authenticationRepository.save(currentUser);
			
			if(currentUser.getIsAdmin() == null ){
				currentUser.setIsAdmin(false);
			}
			
			return "User Updated";
		}
	}

	/**
	 * 
	 * @param eid
	 * @param loggededTime
	 */
	public void logUser(String eid, long loggededTime) {

		UserTrack userLog = userTrackRepository.findByUserEid(eid);

		if (userLog == null) {
			UserTrack newUser = new UserTrack();
			newUser.setUserEid(eid);
			newUser.setLogginTime(Arrays.asList(loggededTime));
			userTrackRepository.save(newUser);
		} else {
			List<Long> timeToLog = userLog.getLogginTime();
			timeToLog.add(loggededTime);
			userLog.setLogginTime(timeToLog);
			userTrackRepository.save(userLog);
		}
	}

	/**
	 * @param eid
	 * @return string
	 */
	@Override
	public String getPortfolioId(String eid) {

		/*
		 * PortfolioResponse portfolioResponse =
		 * this.portfolioResponseRepository.findByEid(eid);
		 * 
		 * if(portfolioResponse != null){ return
		 * portfolioResponse.getId().toString(); }
		 */

		return null;
	}
	
	/**
	 * @param eid
	 * @return 
	 */
	@Override
	public Boolean isAdmin(String eid) {
		Authentication currentUser = authenticationRepository.findByEid(eid);
		if(currentUser != null){
               return currentUser.getIsAdmin();			
		}
		
		return false;
		
		
	}


}