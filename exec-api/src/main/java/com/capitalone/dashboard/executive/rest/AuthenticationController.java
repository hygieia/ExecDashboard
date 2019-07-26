package com.capitalone.dashboard.executive.rest;

import com.capitalone.dashboard.exec.model.AuthenticationRequest;
import com.capitalone.dashboard.executive.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * AuthenticationController class
 */
@RestController
@CrossOrigin
public class AuthenticationController {

	private final AuthenticationService authenticationService;

	/**
	 * Constructor
	 * 
	 * @param AuthenticationService
	 *            authenticationService
	 */
	public AuthenticationController(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	/**
	 * Rest Mapping
	 * 
	 * @param AuthenticationRequest
	 *            authenticationRequest
	 * @return data
	 */
	@RequestMapping(value = "/auth/resgisterUser", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<String> registerUser(@RequestBody AuthenticationRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(authenticationService.register(request));
	}

	/**
	 * Rest Mapping
	 * 
	 * @param String
	 *            eid
	 * @return data
	 */
	@GetMapping(value = "/auth/getPortfolioId/{eid}")
	public String getId(@PathVariable("eid") String eid) {
		return authenticationService.getPortfolioId(eid);
	}
	
	/**
     * Rest Mapping
     * @param String eId
     * @return data
     */
    @RequestMapping(value = "/auth/checkisAdmin/{eId}", method = GET)
    public Boolean checkisAdmin( @PathVariable("eId") String eId) {
        return authenticationService.isAdmin(eId);
    }


}
