package com.capitalone.dashboard.executive.rest.vz;
	
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capitalone.dashboard.executive.service.vz.DefaultHygieiaService;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class DefaultHygieiaController {
	private final DefaultHygieiaService defaultHygieiaService;

	@Autowired
    public DefaultHygieiaController(DefaultHygieiaService defaultHygieiaService) {
        this.defaultHygieiaService =defaultHygieiaService;     
    }
    
    @RequestMapping(value = "/defaulthygieia/{appId}", method = GET, produces = APPLICATION_JSON_VALUE)
	public List<String> getUsers(@PathVariable String appId) {	
		return this.defaultHygieiaService.getUsers(appId);
	}
    
    @RequestMapping(value = "/defaulthygieia/{appId}/delete/{user}", method = GET, produces = APPLICATION_JSON_VALUE)
	public Boolean deleteUser(@PathVariable String appId,@PathVariable String user) {	
		return this.defaultHygieiaService.deleteUser(appId, user);

	}
    
    /**
	 * getUsersforAdmin
	 * @param appId
	 * return this.defaultHygieiaService.getUsersForAdmin(appId);
	 */
    @RequestMapping(value = "/defaulthygieia/admin/{appId}", method = GET, produces = APPLICATION_JSON_VALUE)
	public List<Map<String,String>> getUsersforAdmin(@PathVariable String appId) {	
		return this.defaultHygieiaService.getUsersForAdmin(appId);
	}
    
    /**
	 * usertoAdmin
	 * @param appId
	 * @param user
	 * return this.defaultHygieiaService.promoteToAdmin(appId, user);
	 */
    @RequestMapping(value = "/defaulthygieia/{appId}/admin/{user}", method = GET, produces = APPLICATION_JSON_VALUE)
	public Boolean usertoAdmin(@PathVariable String appId,@PathVariable String user) {	
		return this.defaultHygieiaService.promoteToAdmin(appId, user);

	}
}
