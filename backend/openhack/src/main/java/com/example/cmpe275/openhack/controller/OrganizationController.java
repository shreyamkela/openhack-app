package com.example.cmpe275.openhack.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.example.cmpe275.openhack.entity.Hackathon;
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
	
	@Transactional
	@RequestMapping(value = "/hacker/getOrganizations", method = RequestMethod.GET, produces = { "application/json"})
	@ResponseBody
	public Map<Object, Object> getAllOrganizations(HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		System.out.println("\ngetAllOrganizations method called for the organization : ");
		Map<Object,Object> map = new HashMap<>();
		List<Organization> result_organizations = new ArrayList<>();
		try {
			result_organizations = orgdao.findAllOrganization();
		}
		catch (Exception e) {
			System.out.println("Exception while fetching all organizations \n"+e);
		}

		map.put("organizations", OrganizationsObject(result_organizations));
		return map;
	}
	
	@Transactional
	@RequestMapping(value = "/hacker/getOneOrganization/{id}", method = RequestMethod.GET, produces = { "application/json"})
	@ResponseBody
	public Map<Object, Object> getOneOrganization(@PathVariable("id") long orgId, HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		System.out.println("\ngetOneOrganization method called for the organization : ");
		Map<Object,Object> map = new HashMap<>();
		Organization org = new Organization();
		try {
			org = orgdao.findOrganizationById(orgId);
		}
		catch (Exception e) {
			System.out.println("Exception while fetching the organizations \n"+e);
		}
		map = formOrganizationObject(org);
		return map;
	}
	
	public List<Map<Object, Object>> OrganizationsObject(List<Organization> oganizations)
	{
		List<Map<Object, Object>> outerlist = new ArrayList<Map<Object, Object>>();
		for(Organization org : oganizations) 
		{
			Map<Object, Object> innermap = new HashMap<Object, Object>();
			innermap.put("id", org.getId());
			innermap.put("name", org.getName());
			innermap.put("description", org.getDescription());
			outerlist.add(innermap );
		}
		return outerlist;	
	}
	
	public Map<Object, Object> formOrganizationObject(Organization org)
	{
		Map<Object, Object> hmap = new HashMap<Object, Object>();
		hmap.put("id", org.getId());
		hmap.put("name", org.getName());
		hmap.put("description", org.getDescription());
		
		
		Address addr = org.getAddress();
		if(addr!=null)
		{
			Map<Object, Object> hmap_addr = new HashMap<Object, Object>();
			hmap_addr.put("street", addr.getStreet());
			hmap_addr.put("city", addr.getCity());
			hmap_addr.put("state", addr.getState());
			hmap_addr.put("zip", addr.getZip());
			hmap.put("Address", hmap_addr);
		}	
		
		User owner = org.getOwner();
		if(owner!=null)
		{
			Map<Object, Object> hmap_owner = new HashMap<Object, Object>();
			hmap_owner.put("id", owner.getId());
			hmap_owner.put("name", owner.getName());
			hmap_owner.put("email", owner.getEmail());
			hmap_owner.put("title", owner.getTitle());
			hmap.put("owner", hmap_owner);
		}
		
		List<Hackathon> hackathons = org.getSponsoredHackathons();
		if(hackathons!=null)
		{
			List<Map<Object, Object>> outerlist = new ArrayList<Map<Object, Object>>();		
			for(Hackathon hck : hackathons) 
			{
				Map<Object, Object> innermap = new HashMap<Object, Object>();
				innermap.put("id", hck.getId());
				innermap.put("name", hck.getName());
				innermap.put("description", hck.getDescription());
				outerlist.add(innermap);
			}	
			hmap.put("sponsoredHackathons", outerlist);
		}
		
		List<User> members = org.getMembers();
		if(members!=null)
		{
			List<Map<Object, Object>> outerlist1 = new ArrayList<Map<Object, Object>>();	
			for(User mem : members)
			{
				Map<Object, Object> innermap1 = new HashMap<Object, Object>();
				innermap1.put("id", mem.getId());
				innermap1.put("name", mem.getName());
				innermap1.put("email", mem.getEmail());
				innermap1.put("title", mem.getTitle());
				outerlist1.add(innermap1);
			}
			hmap.put("members",outerlist1);
		}
		
		return hmap;
	}

}
