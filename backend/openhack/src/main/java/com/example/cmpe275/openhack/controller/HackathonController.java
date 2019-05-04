package com.example.cmpe275.openhack.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
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
import com.example.cmpe275.openhack.entity.User;

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
			@RequestParam(value="name",required=true) String name,
			@RequestParam(value="description",required=true) String description,
			@RequestParam(value="startDate",required=true) Date startDate,
			@RequestParam(value="endDate",required=true) Date endDate,
			@RequestParam(value="teamSizeMin",required=true) int teamSizeMin,
			@RequestParam(value="teamSizeMax",required=true) int teamSizeMax,
			@RequestParam(value="fee",required=true) double fee,
			@RequestParam(value="discount") double discount,
			@RequestParam(value="judges",required=true) List<Long> judgesId,
			@RequestParam(value="sponsors") List<Long> sponsorsId,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception 
	{
		System.out.println("\n Hackathon to be created");
		Map<Object,Object> map = new HashMap<>();
		Hackathon hackathon = new Hackathon();
		hackathon.setName(name);
		hackathon.setDescription(description);
		hackathon.setStartDate(startDate);
		hackathon.setEndDate(endDate);
		hackathon.setTeamSizeMin(teamSizeMin);
		hackathon.setTeamSizeMax(teamSizeMax);
		hackathon.setFee(fee);
		if(discount != new Double(null)) {
			hackathon.setDiscount(discount);
		}
		Set<User> judges = new HashSet<>();
		for(int i=0;i<judgesId.size();i++) {
			User temp = userDao.findById(judgesId.get(i));
			judges.add(temp);
		}
		hackathon.setJudges(judges);
		if(sponsorsId!=null && sponsorsId.size()>0) {
			Set<Organization> sponsors = new HashSet<>();
			for(int i=0;i<sponsorsId.size();i++) {
				Organization temp = organizationDao.findOrganizationById(sponsorsId.get(i));
				sponsors.add(temp);
			}
			hackathon.setSponsors(sponsors);
		}
		return null;
	}

}