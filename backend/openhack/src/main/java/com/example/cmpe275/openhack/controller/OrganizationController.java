package com.example.cmpe275.openhack.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.cmpe275.openhack.dao.OrganizationDao;
import com.example.cmpe275.openhack.dao.OrganizationDaoImpl;
import com.example.cmpe275.openhack.dao.UserDao;
import com.example.cmpe275.openhack.dao.UserDaoImpl;
import com.example.cmpe275.openhack.entity.Address;
import com.example.cmpe275.openhack.entity.Organization;
import com.example.cmpe275.openhack.entity.User;

@Controller
public class OrganizationController {
	
	private OrganizationDao orgdao;
	private UserDao userdao;
	
	public OrganizationController() 
	{
		orgdao =  new OrganizationDaoImpl();
		userdao = new UserDaoImpl();
	}
	
	
	@RequestMapping(value = "/hacker/createOrganization", method = RequestMethod.POST, produces = { "application/json"},
					consumes = {"application/JSON"})
	@ResponseBody
	public Organization createOrganization(HttpServletRequest request,
			HttpServletResponse response, @RequestBody HashMap<String,String> params_map) throws Exception 
	{
		System.out.println("\ncreateOrganization method called for the organization : "+params_map.get("organization_name"));
		Organization org = new Organization();
		if(params_map.get("street")!=null || params_map.get("city")!=null || params_map.get("state")!=null || params_map.get("zip")!=null ||params_map.get("country")!=null) 
		{
			Address address = new Address();
			if(params_map.get("street")!=null)
				address.setStreet(params_map.get("street"));
			if(params_map.get("city")!=null)
				address.setCity(params_map.get("city"));
			if(params_map.get("state")!=null)
				address.setState(params_map.get("state"));
			if(params_map.get("zip")!=null)
				address.setZip(params_map.get("zip"));
			if(params_map.get("country")!=null)
				address.setCountry(params_map.get("country"));
			org.setAddress(address);
		}
		if(params_map.get("organization_name")!=null)
			org.setName(params_map.get("organization_name"));
		if(params_map.get("organization_desc")!=null)
			org.setDescription(params_map.get("organization_desc"));
		if(params_map.get("owner_id")!=null)
		{
			User owner = userdao.findUserbyID(new Long(params_map.get("owner_id")));
			if(owner!=null) {
				org.setOwner(owner);
			}
		}
		
		try {
			org = orgdao.create(org);
		}
		catch (Exception e) {
			System.out.println("Exception while creating an organization \n"+e);
		}
		return org;		
	}

}
