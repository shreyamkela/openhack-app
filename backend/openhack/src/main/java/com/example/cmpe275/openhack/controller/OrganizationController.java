package com.example.cmpe275.openhack.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.example.cmpe275.openhack.dao.RequestDaoImpl;
import com.example.cmpe275.openhack.dao.UserDao;
import com.example.cmpe275.openhack.dao.UserDaoImpl;
import com.example.cmpe275.openhack.entity.Address;
import com.example.cmpe275.openhack.entity.Hackathon;
import com.example.cmpe275.openhack.entity.Organization;
import com.example.cmpe275.openhack.entity.Request;
import com.example.cmpe275.openhack.entity.User;

@Controller
public class OrganizationController {
	
	private OrganizationDao orgdao;
	private UserDao userdao;
	private RequestDaoImpl reqdao;
	
	public OrganizationController() 
	{
		orgdao =  new OrganizationDaoImpl();
		userdao = new UserDaoImpl();
		reqdao = new RequestDaoImpl();
	}
	
	
	@RequestMapping(value = "/hacker/createOrganization", method = RequestMethod.POST, produces = { "application/json"},
					consumes = {"application/JSON"})
	@ResponseBody
	@Transactional
	public Map<Object, Object> createOrganization(HttpServletRequest request,
			HttpServletResponse response, @RequestBody HashMap<String,String> params_map) throws Exception 
	{
		System.out.println("\ncreateOrganization method called for the organization : "+params_map.get("organization_name"));
		Map<Object,Object> map = new HashMap<>();
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
			response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
			map.put("msg", "Some error occured in creating the organization");
			return map;
		}
		map = formOrganizationObject(org);
		return map;	
	}
	
	@RequestMapping(value = "/hacker/getOrganizations", method = RequestMethod.GET, produces = { "application/json"})
	@ResponseBody
	@Transactional
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
			response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
			map.put("msg", "Some error occured in getting all organizations");
			return map;
		}

		map.put("organizations", OrganizationsObject(result_organizations));
		return map;
	}
	
	@RequestMapping(value = "/hacker/getOneOrganization/{user_id}/{org_id}", method = RequestMethod.GET, produces = { "application/json"})
	@ResponseBody
	@Transactional
	public Map<Object, Object> getOneOrganization(@PathVariable("org_id") long orgId, @PathVariable("user_id") long userId, 
			HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		System.out.println("\ngetOneOrganization method called for the organization : "+orgId);
		Map<Object,Object> map = new HashMap<>();
		Organization org = new Organization();
		User user = new User();
		try {
			org = orgdao.findOrganizationById(new Long(orgId));
			user = userdao.findUserbyID(new Long(userId));
			if(org==null) 
			{
				response.setStatus( HttpServletResponse.SC_BAD_REQUEST  );
				map.put("msg", "No Organization having id "+orgId);
				return map;
			}
			if(user==null) 
			{
				response.setStatus( HttpServletResponse.SC_BAD_REQUEST  );
				map.put("msg", "No User having id "+userId);
				return map;
			}
		}
		catch (Exception e) {
			System.out.println("Exception while fetching the organizations \n"+e);
			response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
			map.put("msg", "Some error occured in getting an organization");
			return map;
		}
		map = formUserOrganizationObject(user,org);
		return map;
	}
	
	@RequestMapping(value = "/hacker/joinOrganization/{user_id}/{org_id}/{request_id}", method = RequestMethod.PUT, produces = { "application/json"})
	@ResponseBody
	@Transactional
	public Map<Object, Object> joinOrganization(@PathVariable("user_id") long userId, @PathVariable("org_id") long orgId, 
			@PathVariable("request_id") long requestId, HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		Map<Object, Object> result_map = new HashMap<Object, Object>();
		System.out.println("\njoinOrganization method called by user "+userId+" for organization "+orgId);
		User user = userdao.findUserbyID(new Long(userId));
		Organization organization = orgdao.findOrganizationById(new Long(orgId));
		if(user!=null && organization!=null)
		{
			user.setOrganization(organization);
			User updated_user = new User();
			// adding a member to the organization
			try {
				updated_user = userdao.updateUser(user);
				if(updated_user==null) 
				{
					response.setStatus( HttpServletResponse.SC_BAD_REQUEST  );
					result_map.put("msg", "Some error in the updation of user "+userId);
					return result_map;
				}
				
				// forming an array of all requests to be deleted (have to delete all outstanding join requests for userId)
				List<Long> req_to_be_deleted = new ArrayList<>();
				List<Request> all_requests = reqdao.getAllRequests();
				for(Request r: all_requests)
				{
					if(r.getRequested_by_user().getId() == userId)
					{
						req_to_be_deleted.add(r.getId());
					}
				}
				System.out.println("The user "+userId+" had "+req_to_be_deleted.size()+" outstanding join requests");
				// deleting the join organization request that was previously sent!
				try {
					System.out.println("Now deleting all such requests");
					for(Long request_id : req_to_be_deleted)
					{
						Request deleted_request = reqdao.deleteRequest(request_id);
					}
				}
				catch (Exception e) {
					System.out.println("\nException while deleting all outstanding join requests for a user "+userId+"\n"+e);
				}
				
			}
			catch (Exception e) {
				System.out.println("\nException while setting the organizations for a user\n"+e);
				response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
				result_map.put("msg", "Some error occured in creating the organization");
				return result_map;
			}
			
			result_map.put("user", formUserObject(updated_user));
		}
		else
		{
			result_map.put("msg", "Both the user and the organization should be existing");
		}
		return result_map;
	}
	
	@RequestMapping(value = "/hacker/declineJoinOrganization/{user_id}/{org_id}/{request_id}", method = RequestMethod.PUT, produces = { "application/json"})
	@ResponseBody
	@Transactional
	public Map<Object, Object> DeclineJoinOrganization(@PathVariable("user_id") long userId, @PathVariable("org_id") long orgId, 
			@PathVariable("request_id") long requestId, HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		Map<Object, Object> result_map = new HashMap<Object, Object>();
		System.out.println("\ndeclineJoinOrganization method for organization "+orgId);
		User user = userdao.findUserbyID(new Long(userId));
		Organization organization = orgdao.findOrganizationById(new Long(orgId));
		Request req = reqdao.findRequestById(new Long(requestId));
		if(user!=null && organization!=null && req!=null)
		{	
			// deleting the join organization request that was previously sent!
			try {
				Request deleted_request = reqdao.deleteRequest(requestId);
			}
			catch (Exception e) {
				System.out.println("\nException while deleting the join request for a user "+userId+"\n"+e);
				response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
				result_map.put("msg", "Some error while deleting the join request for a user");
				return result_map;
			}			
			result_map.put("organization", formUserOrganizationObject(user, organization));
		}
		else
		{
			result_map.put("msg", "Both the user and the organization should be existing");
			
		}
		return result_map;
	}
	
	@RequestMapping(value = "/hacker/requestJoinOrganization/{user_id}/{org_id}", method = RequestMethod.PUT, produces = { "application/json"})
	@ResponseBody
	@Transactional
	public Map<Object, Object> requestJoinOrganization(@PathVariable("user_id") long userId, @PathVariable("org_id") long orgId, 
			HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		Map<Object, Object> map = new HashMap<Object, Object>();
		System.out.println("\nrequestJoinOrganization method called by user "+userId+" for organization "+orgId);
		User user = userdao.findUserbyID(new Long(userId));
		Organization organization = orgdao.findOrganizationById(new Long(orgId));
		Request req  = new Request();
		Set<Request> reqs_owner;
		Set<Request> reqs_org;
		if(user!=null && organization!=null)
		{
<<<<<<< HEAD
			// checking whether the request that just came already exists or not
			try
			{
				List<Request> existing_requests = reqdao.getAllRequests();
				for(Request r: existing_requests)
				{
					if(r.getRequested_by_user().getId() == userId && r.getRequested_for_org().getId() == orgId)
					{
						//this request already exists in the database
						map.put("msg", "This request to join this organization is already registered!");
						return map;
					}
				}
			}
			catch(Exception e) 
			{
				System.out.println("\nException while getting all join requests for organization "+orgId+"\n"+e);
				response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
				map.put("msg", "Some error while getting all join requests to check for restricting duplicate requests");
				return map;
			}
=======
>>>>>>> parent of 423de8f... DI and AOP addition, saving user type in localStorage
			if(organization.getOwner()!=null)
			{
				User owner = organization.getOwner();
				User updated_owner = new User();
				Organization updated_organization = new Organization();
				req.setRequested_for_org(organization);
				req.setRequested_by_user(user);
				System.out.println("___________ req.setRequested_by_user(user) done");
				System.out.println("___________ req.setRequested_for_org(organization);");
				try {
					Request new_request = reqdao.addRequest(req);
				}
				catch (Exception e) {
					System.out.println("\nException while creating a new join request for organization "+organization.getId()+"\n"+e);
					response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
					map.put("msg", "Some error while deleting the join request for a user");
					return map;
				}

				map = formUserOrganizationObject(user, organization);
				return map;
			}
			else
			{
				System.out.println("This organization does not have an owner!!");
				map.put("msg", "This organization does not have an owner!!");
				return map;
			}
		}
		else
		{
			map.put("msg", "Both the user and the organization should be existing");
			return map;
		}
		
	}
	
	@RequestMapping(value = "hacker/getOtherOrganizations/{user_id}", method = RequestMethod.GET, produces = { "application/json"})
	@ResponseBody
	@Transactional
	public Map<Object, Object> getOtherOrganizations(@PathVariable("user_id") long userId, 
			HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		System.out.println("\ngetOtherOrganizations method called for the organizations by the user : "+userId);
		Map<Object,Object> map = new HashMap<>();
		List<Organization> own_organizations = new ArrayList<>();
		List<Organization> other_organizations = new ArrayList<>();
		List<Organization> result_organizations = new ArrayList<>();
		try {
			result_organizations = orgdao.findAllOrganization();

			for(Organization res_org : result_organizations)
			{
				if(res_org.getOwner()!=null)
				{
					if((long)res_org.getOwner().getId() == userId)
					{
						own_organizations.add(res_org);
					}
					else
					{
						other_organizations.add(res_org);
					}
				}
				else
				{
					other_organizations.add(res_org);
				}
			}
		}
		catch (Exception e) {
			System.out.println("Exception while fetching all organizations \n"+e);
			response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
			map.put("msg", "Some error while fetching all organizations");
			return map;
		}
		map.put("other_organizations", OrganizationsObject(other_organizations));
		map.put("own_organizations", OrganizationsObject(own_organizations));
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
			User owner = org.getOwner();
			if(owner!=null)
				innermap.put("owner", owner.getId());
			else
				innermap.put("owner", null);
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
		Map<Object, Object> hmap_addr = new HashMap<Object, Object>();
		if(addr!=null)
		{
			hmap_addr.put("street", addr.getStreet());
			hmap_addr.put("city", addr.getCity());
			hmap_addr.put("state", addr.getState());
			hmap_addr.put("zip", addr.getZip());
		}
		hmap.put("Address", hmap_addr);
		
		User owner = org.getOwner();
		Map<Object, Object> hmap_owner = new HashMap<Object, Object>();
		if(owner!=null)
		{
			hmap_owner.put("id", owner.getId());
			hmap_owner.put("name", owner.getName());
			hmap_owner.put("email", owner.getEmail());
			hmap_owner.put("title", owner.getTitle());
		}
		hmap.put("owner", hmap_owner);
		
		Set<Hackathon> hackathons = org.getSponsoredHackathons();
		List<Map<Object, Object>> outerlist = new ArrayList<Map<Object, Object>>();		
		if(hackathons!=null)
		{
			for(Hackathon hck : hackathons) 
			{
				Map<Object, Object> innermap = new HashMap<Object, Object>();
				innermap.put("id", hck.getId());
				innermap.put("name", hck.getName());
				innermap.put("description", hck.getDescription());
				outerlist.add(innermap);
			}	
		}
		hmap.put("sponsoredHackathons", outerlist);
		
		Set<User> members = org.getMembers();
		List<Map<Object, Object>> outerlist1 = new ArrayList<Map<Object, Object>>();	
		if(members!=null)
		{
			for(User mem : members)
			{
				Map<Object, Object> innermap1 = new HashMap<Object, Object>();
				innermap1.put("id", mem.getId());
				innermap1.put("name", mem.getName());
				innermap1.put("email", mem.getEmail());
				innermap1.put("title", mem.getTitle());
				outerlist1.add(innermap1);
			}
		}
		hmap.put("members",outerlist1);
		
		return hmap;
	}
	
	public Map<Object, Object> formUserOrganizationObject(User user, Organization org)
	{
		Map<Object, Object> hmap = new HashMap<Object, Object>();
		String status = "";
		hmap.put("id", org.getId());
		hmap.put("name", org.getName());
		hmap.put("description", org.getDescription());
		
		Address addr = org.getAddress();
		Map<Object, Object> hmap_addr = new HashMap<Object, Object>();
		if(addr!=null)
		{
			hmap_addr.put("street", addr.getStreet());
			hmap_addr.put("city", addr.getCity());
			hmap_addr.put("state", addr.getState());
			hmap_addr.put("zip", addr.getZip());
		}
		hmap.put("Address", hmap_addr);
		
		User owner = org.getOwner();
		Map<Object, Object> hmap_owner = new HashMap<Object, Object>();
		if(owner!=null)
		{
			hmap_owner.put("id", owner.getId());
			hmap_owner.put("name", owner.getName());
			hmap_owner.put("email", owner.getEmail());
			hmap_owner.put("title", owner.getTitle());
			if(owner.getId() == user.getId())
				status = "owner";
			if(owner.getJoin_requests()!=null)
			{
				Set<Request>join_reqs = owner.getJoin_requests();
				Set<Object>join_reqs_object = new HashSet<>();
				hmap_owner.put("join_requests", join_reqs_object);
			}
			
		}
		hmap.put("owner", hmap_owner);
		
		Set<Hackathon> hackathons = org.getSponsoredHackathons();
		List<Map<Object, Object>> outerlist = new ArrayList<Map<Object, Object>>();		
		if(hackathons!=null)
		{
			for(Hackathon hck : hackathons) 
			{
				Map<Object, Object> innermap = new HashMap<Object, Object>();
				innermap.put("id", hck.getId());
				innermap.put("name", hck.getName());
				innermap.put("description", hck.getDescription());
				outerlist.add(innermap);
			}	
		}
		hmap.put("sponsoredHackathons", outerlist);
		
		Set<User> members = org.getMembers();
		List<Map<Object, Object>> outerlist1 = new ArrayList<Map<Object, Object>>();	
		if(members!=null)
		{
			for(User mem : members)
			{
				Map<Object, Object> innermap1 = new HashMap<Object, Object>();
				innermap1.put("id", mem.getId());
				innermap1.put("name", mem.getName());
				innermap1.put("email", mem.getEmail());
				innermap1.put("title", mem.getTitle());
				outerlist1.add(innermap1);
			}
			if(status!="owner" && members.contains(user) )
				status = "member";
		}
		hmap.put("members",outerlist1);
		
		
		Set<Request> org_join_requests = org.getJoin_requests();
		List<Map<Object, Object>> outerlist2 = new ArrayList<Map<Object, Object>>();
		if(org_join_requests!=null) 
		{
			for(Request req : org_join_requests)
			{
				User join_request_user = req.getRequested_by_user();
				Organization join_request_organization = req.getRequested_for_org();
				Map<Object, Object> inner_requests = new HashMap<>();
				Map<Object, Object> inner_user_map = new HashMap<>();
				Map<Object, Object> inner_org_map = new HashMap<>();
				
				inner_user_map.put("id", join_request_user.getId());
				inner_user_map.put("name", join_request_user.getName());
				inner_user_map.put("email", join_request_user.getEmail());
				if(join_request_user.getTitle()!=null)
					inner_user_map.put("title", join_request_user.getTitle());
				
				if(status!="owner" && status!="member" && join_request_user.getId()==user.getId())
					status = "requested";
				
				inner_org_map.put("id", join_request_organization.getId());
				inner_org_map.put("name", join_request_organization.getName());
				inner_org_map.put("description", join_request_organization.getDescription());
				
				inner_requests.put("user", inner_user_map);
				inner_requests.put("organization", inner_org_map);
				inner_requests.put("request_id", req.getId());
				outerlist2.add(inner_requests);
			}
			
		}
		hmap.put("org_join_requests", outerlist2);
		
		hmap.put("user_org_status", status);
		
		return hmap;
	}
	
	public Map<Object, Object> formUserObject(User user)
	{
		Map<Object, Object> hmap = new HashMap<Object, Object>();
		hmap.put("id", user.getId());
		hmap.put("name", user.getName());
		hmap.put("email", user.getEmail());
		hmap.put("screenName", user.getScreenName());
		hmap.put("aboutMe", user.getAboutMe());
		hmap.put("title", user.getTitle());
		
		Address addr = user.getAddress();
		Map<Object, Object> hmap_addr = new HashMap<Object, Object>();
		if(addr!=null)
		{
			hmap_addr.put("street", addr.getStreet());
			hmap_addr.put("city", addr.getCity());
			hmap_addr.put("state", addr.getState());
			hmap_addr.put("zip", addr.getZip());
		}
		hmap.put("address", hmap_addr);

		Organization org = user.getOrganization();
		Map<Object, Object> hmap_org = new HashMap<Object, Object>();
		if(org!=null) 
		{
			hmap_org.put("id", org.getId());
			hmap_org.put("name", org.getName());
			hmap_org.put("description", org.getDescription());
			hmap_org.put("owner",org.getOwner().getId());
		}
		hmap.put("organization",hmap_org);
		
		Set<Hackathon> judged_hackathon = user.getJudgedHackathons();
		List<Map<Object,Object>> hackathonDetails = new ArrayList<>();
		if(judged_hackathon!=null)
		{
			for(Hackathon hack : judged_hackathon)
			{
				Map<Object,Object> inner_map = new HashMap<>();
				inner_map.put("id", hack.getId());
				inner_map.put("name", hack.getName());
				inner_map.put("description", hack.getDescription());
				hackathonDetails.add(inner_map);
			}
		}
		hmap.put("judged_hackathons", hackathonDetails);
		
		return hmap;
	}
}
