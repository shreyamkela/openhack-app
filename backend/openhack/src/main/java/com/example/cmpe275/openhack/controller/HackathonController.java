package com.example.cmpe275.openhack.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.cmpe275.openhack.dao.HackathonDao;
import com.example.cmpe275.openhack.dao.HackathonDaoImpl;
import com.example.cmpe275.openhack.dao.OrganizationDao;
import com.example.cmpe275.openhack.dao.OrganizationDaoImpl;
import com.example.cmpe275.openhack.dao.UserDao;
import com.example.cmpe275.openhack.dao.UserDaoImpl;
import com.example.cmpe275.openhack.entity.Hackathon;
import com.example.cmpe275.openhack.entity.Organization;
import com.example.cmpe275.openhack.entity.Submission;
import com.example.cmpe275.openhack.entity.Team;
import com.example.cmpe275.openhack.entity.User;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;

@RequestMapping("/hackathon")
@Controller
public class HackathonController {


	private UserDao userDao;
	private HackathonDao hackathonDao;
	private OrganizationDao organizationDao;
	
	public HackathonController() {
		// TODO Auto-generated constructor stub
		userDao = new UserDaoImpl();
		hackathonDao = new HackathonDaoImpl();
		organizationDao = new OrganizationDaoImpl();
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> createHackathon(
			@RequestBody HashMap<Object,Object> map,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception 
	{
		System.out.println("\n Hackathon to be created");
		Map<Object,Object> responseObject = new HashMap<>();
		String name = (String)map.get("name");
		Date startDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.'S'Z'").parse((String)map.get("startDate"));
		Date endDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.'S'Z'").parse((String)map.get("endDate"));
		String description = (String)map.get("description");
		int teamSizeMin = (int) map.get("teamSizeMin");
		int teamSizeMax = (int) map.get("teamSizeMax");
		double fee = new Double((int)map.get("fee"));
		List<Integer> judgesId = (List<Integer>) map.get("judgesId");
		Hackathon hackathon = new Hackathon();
		hackathon.setName(name);
		hackathon.setDescription(description);
		hackathon.setStartDate(startDate);
		hackathon.setEndDate(endDate);
		hackathon.setTeamSizeMin(teamSizeMin);
		hackathon.setTeamSizeMax(teamSizeMax);
		hackathon.setFee(fee);
		if(map.get("discount") != null) {
			double discount = new Double((String)map.get("discount"));
			hackathon.setDiscount(discount);
		}
		Set<User> judges = new HashSet<>();
		for(int i=0;i<judgesId.size();i++) {
			User temp = userDao.findUserbyID(judgesId.get(i));
			judges.add(temp);
		}
		hackathon.setJudges(judges);
		if(map.get("sponsorsId")!=null) {
			List<Integer> sponsorsId = (List<Integer>)map.get("sponsorsId");
			Set<Organization> sponsors = new HashSet<>();
			for(int i=0;i<sponsorsId.size();i++) {
				Organization temp = organizationDao.findOrganizationById(sponsorsId.get(i));
				sponsors.add(temp);
			}
			hackathon.setSponsors(sponsors);
		}
		try {
			hackathonDao.create(hackathon);
			responseObject.put("msg", "Successfully created");
		}catch (Exception e) {
			// TODO: handle exception
			response.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR  );
			responseObject.put("msg",e.getMessage());
		}
		return responseObject;
	}
	
	@RequestMapping(value="{id}",method=RequestMethod.POST)
	@ResponseBody
	public Map<Object,Object> getHackathonDetails(HttpServletRequest request,
												HttpServletResponse response,
												@RequestBody Map<Object,Object> map,
												@PathVariable(name="id") long hackathonId){
		System.out.print("Getting hackathon details");
		long userId = new Long((String)map.get("userId"));
		Hackathon hackathon = hackathonDao.findById(hackathonId);
		User user = userDao.findUserbyID(userId);
		return createHackathonResponseBody(hackathon, user);
	}
	
	public Map<Object,Object> createHackathonResponseBody(Hackathon hackathon,User user){
		Map<Object,Object> responseBody = new HashMap<>();
		String message="";
		responseBody.put("id",hackathon.getId());
		responseBody.put("name",hackathon.getName());
		responseBody.put("description",hackathon.getDescription());
		responseBody.put("startDate",hackathon.getStartDate());
		responseBody.put("endDate",hackathon.getEndDate());
		responseBody.put("fee",hackathon.getFee());
		responseBody.put("teamSizeMin", hackathon.getTeamSizeMax());
		responseBody.put("teamSizeMax", hackathon.getTeamSizeMax());
		responseBody.put("discount",hackathon.getDiscount());
		Set<Team> teams = hackathon.getTeams();
		Set<User> judges = hackathon.getJudges();
		Set<Organization> sponsors = hackathon.getSponsors();
		Set<Submission> submissions = hackathon.getSubmissions();
		List<Map<Object,Object>> teamDetails = new ArrayList<>();
		List<Map<Object,Object>> judgeDetails = new ArrayList<>();
		List<Map<Object,Object>> sponsorDetails = new ArrayList<>();
		List<Map<Object,Object>> submissionDetails = new ArrayList<>();
		for(Team team : teams) {
			Map<Object,Object> temp = new HashMap<>();
			temp.put("teamId", team.getId());
			temp.put("teamSize", team.getMembers().size());
			temp.put("teamName", team.getTeamName());
			teamDetails.add(temp);
			if(team.getMembers().contains(user)) {
				message="registred";
			}
		}
		for(User judge : judges) {
			Map<Object,Object> temp = new HashMap<>();
			temp.put("judgeId",judge.getId());
			temp.put("judgeName",judge.getName());
			temp.put("judgeEmail",judge.getEmail());
			temp.put("judgeScreenName", judge.getScreenName());
			judgeDetails.add(temp);
			if(judge.getId() == user.getId()) {
				message="judge";
			}
		}
		for(Organization sponsor : sponsors) {
			Map<Object,Object> temp = new HashMap<>();
			temp.put("sponsorId",sponsor.getId());
			temp.put("sponsorName",sponsor.getName());
			temp.put("sponsorDescription",sponsor.getDescription());
			sponsorDetails.add(temp);
		}
		for(Submission submission : submissions) {
			Map<Object,Object> temp = new HashMap<>();
			temp.put("submissionId",submission.getId());
			temp.put("url", submission.getURL());
			temp.put("submittedBy",submission.getTeam().getTeamName());
			temp.put("grade",submission.getGrade());
			submissionDetails.add(temp);
		}
		responseBody.put("teamDetails", teamDetails);
		responseBody.put("judgeDetails", judgeDetails);
		responseBody.put("sponsorDetails", sponsorDetails);
		responseBody.put("submissionDetails", submissionDetails);
		responseBody.put("message", message);
		return responseBody;
	}
}