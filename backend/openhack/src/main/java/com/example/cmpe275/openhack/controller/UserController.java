package com.example.cmpe275.openhack.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.cmpe275.openhack.dao.OrganizationDao;
import com.example.cmpe275.openhack.dao.OrganizationDaoImpl;
import com.example.cmpe275.openhack.dao.UserDao;
import com.example.cmpe275.openhack.dao.UserDaoImpl;
import com.example.cmpe275.openhack.entity.Address;
import com.example.cmpe275.openhack.entity.Hackathon;
import com.example.cmpe275.openhack.entity.Organization;
import com.example.cmpe275.openhack.entity.Team;
import com.example.cmpe275.openhack.entity.User;

//@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600,allowedHeaders=
//{"Origin", "Content-Type", "Accept"})

@RestController
public class UserController {
	private UserDao userdao;
	private OrganizationDao orgdao;

	public UserController()
	{
		userdao = new UserDaoImpl();
		orgdao =  new OrganizationDaoImpl();
	}
	
	@GetMapping("/getuser/{email}")
	@ResponseBody
	@Transactional
	public  Map<Object, Object> getUser(@PathVariable("email") String email){
		System.out.println("\ngetUSer method called for the User");	
		User user = new User();
		try
		{
			user = userdao.findUserbyEmail(email);
			if(user==null)
			{
				//response.setStatus( HttpServletResponse.SC_BAD_REQUEST  );
				return formUserObject(user);
			}
		}
		catch(Exception e)
		{
			if(e.getMessage()!=null)
			{
				System.out.println("Error :"+e.getMessage());
				//response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
				return formUserObject(user);
			}
		}
		return formUserObject(user);
	}
	
	@GetMapping("/getuserid/{id}")
	@ResponseBody
	@Transactional
	public  Map<Object, Object>  getUserByID(@PathVariable("id") long id){
		System.out.println("\ngetUserByID method called for the User");	
		User user = new User();
		try
		{
			user = userdao.findUserbyID(id);
			if(user==null) 
			{
				//response.setStatus( HttpServletResponse.SC_BAD_REQUEST  );
				return formUserObject(user);
			}
		}
		catch(Exception e)
		{
			if(e.getMessage()!=null)
			{
				System.out.println("Error :"+e.getMessage());
				//response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
				return formUserObject(user);
			}
		}
		return formUserObject(user);
	}
	
	@PostMapping("/adduser")
	@ResponseBody
	@Transactional
	public User addUser(@RequestBody HashMap<String,String> map){
		System.out.println("\n addUser method called for the User");
		System.out.println("User data from post "+ map.get("email") +"maap "+map);
		User user = new User();
		if(map.get("screenName")!=null)
			user.setScreenName(map.get("screenName"));
		if(map.get("password")!=null)
			user.setPassword(map.get("password"));
		if(map.get("name")!=null)
			user.setName(map.get("name"));
		if(map.get("email")!=null)
			user.setEmail(map.get("email"));
		if(map.get("verified")!=null)
			user.setVerified(map.get("verified"));
		if(map.get("usertype")!=null)
			user.setUsertype(map.get("usertype"));
		if(map.get("lastname")!=null)
			user.setLastname(map.get("lastname"));
		
		try {
			user = userdao.createUser(user);
		}catch (Exception e) {
			System.out.println("Exception while creating a user"+e);
		}
		return user;
	}
	
	@PutMapping("/updateuser/{id}")
	@ResponseBody
	@Transactional
	public User updateUser(@PathVariable long id,@RequestBody HashMap<String,String> map){
		System.out.println("\n updateUser method called for the User");
		System.out.println("User data from put "+map);
		User user = userdao.findUserbyID(id);
		if(map.get("street")!=null || map.get("city")!=null || map.get("state")!=null || map.get("zip")!=null ||map.get("country")!=null) {
			Address address = new Address();
			if(map.get("street")!=null)
				address.setStreet(map.get("street"));
			if(map.get("city")!=null)
				address.setCity(map.get("city"));
			if(map.get("state")!=null)
				address.setState(map.get("state"));
			if(map.get("zip")!=null)
				address.setZip(map.get("zip"));
			if(map.get("country")!=null)
				address.setCountry(map.get("country"));
			user.setAddress(address);
		}
		if(map.get("organization")!=null){
			Organization org = null;
			org = orgdao.findOrganizationByName("organization");
			user.setOrganization(org);
		}
		if(map.get("aboutMe")!=null)
			user.setAboutMe(map.get("aboutMe"));
		if(map.get("password")!=null)
			user.setPassword(map.get("password"));
		if(map.get("name")!=null)
			user.setName(map.get("name"));
		if(map.get("title")!=null)
			user.setTitle(map.get("title"));
		if(map.get("verified")!=null)
			user.setVerified(map.get("verified"));
		if(map.get("usertype")!=null)
			user.setUsertype(map.get("usertype"));
		if(map.get("lastname")!=null)
			user.setLastname(map.get("lastname"));
		
		
		try {
			user = userdao.updateUser(user);
		}catch (Exception e) {
			System.out.println("Exception while updating a user"+e);
		}
		return user;
	}
	

	@GetMapping(value="/user/notHackathon/{hackathonId}/{userId}")
	@ResponseBody
	public Map<Object,Object> getUsersNotInHackathon(HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable(name="hackathonId") long hackathonId,
			@PathVariable(name="userId") long userId){
				List<User> users = userdao.findAllUsers();
				Map<Object,Object> responseBody = new HashMap<>();
				List<Map<Object,Object>> userDetails = new ArrayList<>();
				for(User user : users) {
					boolean flag=true;
					if(user.getId()!=userId) {
						Set<Team> teams = user.getTeams();
						for(Team team : teams) {
							if(team.getParticipatedHackathon().getId()==hackathonId) {
								flag=false;
								break;
							}
						}
						if(flag) {
							Map<Object,Object> temp = new HashMap<>();
							temp.put("id", user.getId());
							temp.put("name", user.getName());
							temp.put("title", user.getTitle());
							userDetails.add(temp);
						}
					}
				}
				responseBody.put("userDetails", userDetails);
				return responseBody;
			}
	@GetMapping("/getalluser")
	@ResponseBody
	public List<User> getAllUser(){
		System.out.println("\ngetAllUser method called for the User");	
		List<User> listusers =new ArrayList<>();
		try
		{
			listusers = userdao.findAllUsers();
			if(listusers==null) 
			{
				//response.setStatus( HttpServletResponse.SC_BAD_REQUEST  );
				return listusers;
			}
		}
		catch(Exception e)
		{
			if(e.getMessage()!=null)
			{
				//response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
				return listusers;
			}
		}
		return listusers;
	}
	
	@GetMapping("/getallscreennames")
	@ResponseBody
	@Transactional
	public List<String> getAllScreenNames(){
		System.out.println("\ngetAllScreenNames method called for the User");	
		List<String> listScreenNames =new ArrayList<String>();
		List<User> listusers =new ArrayList<User>();
		
		try
		{
			
			listusers = userdao.findAllUsers();
			for(User user :listusers){
				if(user.getScreenName()!=null){
					listScreenNames.add(user.getScreenName());
				}
			}
			if(listScreenNames==null) 
			{
				//response.setStatus( HttpServletResponse.SC_BAD_REQUEST  );
				return listScreenNames;
			}
		}
		catch(Exception e)
		{
			if(e.getMessage()!=null)
			{
				//response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
				return listScreenNames;
			}
		}
		return listScreenNames;
	}
	public Map<Object, Object> formUserObject(User user)
	{
		Map<Object, Object> hmap = new HashMap<Object, Object>();
		System.out.println("USER "+user);
		hmap.put("id", user.getId());
		hmap.put("name", user.getName());
		hmap.put("lastname", user.getLastname());
		hmap.put("email", user.getEmail());
		hmap.put("screenName", user.getScreenName());
		hmap.put("aboutMe", user.getAboutMe());
		hmap.put("title", user.getTitle());
		hmap.put("imageurl", user.getImageurl());
		hmap.put("usertype", user.getUsertype());
		hmap.put("verified", user.getVerified());
		
		Address addr = user.getAddress();
		Map<Object, Object> hmap_addr = new HashMap<Object, Object>();
		if(addr!=null)
		{
			hmap_addr.put("street", addr.getStreet());
			hmap_addr.put("city", addr.getCity());
			hmap_addr.put("state", addr.getState());
			hmap_addr.put("zip", addr.getZip());
			hmap_addr.put("country", addr.getCountry());
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
//				inner_map.put("no_of_teams",hack.getTeams().size());
//				inner_map.put("no_of_sponsors", hack.getSponsors().size());
				hackathonDetails.add(inner_map);
			}
		}
		hmap.put("judged_hackathons", hackathonDetails);
		
		return hmap;
	}
}
