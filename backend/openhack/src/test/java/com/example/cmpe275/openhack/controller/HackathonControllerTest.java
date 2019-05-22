package com.example.cmpe275.openhack.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.cmpe275.openhack.entity.Hackathon;
import com.example.cmpe275.openhack.entity.Organization;
import com.example.cmpe275.openhack.entity.User;
import com.example.cmpe275.openhack.service.HackathonRepositoryService;
import com.example.cmpe275.openhack.service.RequestRepositoryService;
import com.example.cmpe275.openhack.service.UserRepositoryService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import static org.mockito.ArgumentMatchers.anyLong;

public class HackathonControllerTest {
	
	@Autowired
	private MockMvc mockmvc;
	
	@InjectMocks
	private HackathonController hackController;
	
	@Mock
	HackathonRepositoryService hackdao;
	@Mock
	UserRepositoryService userdao;
	@Mock
	RequestRepositoryService reqdao;
	
	User test_user;
	User user1;
	Hackathon test_hackhathon;
	Set<User> judges;
	
	@Before
	public void setUp() throws Exception{
		MockitoAnnotations.initMocks(this);
		mockmvc = MockMvcBuilders.standaloneSetup(hackController).build();
		
		test_user = new User();
		test_user.setId(new Long(111));
		test_user.setName("Donald");
		test_user.setLastname("Duck");
		test_user.setEmail("donald@sjsu.edu");
		test_user.setScreenName("DonaldDuck");
		test_user.setUsertype("admin");
		test_user.setVerified("Y");
		
		user1 = new User();
		user1.setName("Mickey");
		user1.setLastname("Mouse");
		user1.setId(new Long(222));
		user1.setEmail("mickey@getnada.com");
		user1.setScreenName("MickeyMouse");
		judges = new HashSet<>();
		judges.add(user1);
		
		test_hackhathon = new Hackathon();
		test_hackhathon.setId(new Long(999));
		test_hackhathon.setName("Code For Fun");
		test_hackhathon.setDescription("This hackathon is for fun");
//		Date startDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.'S'Z'").parse("Thu May 23 2019 03:42:11 GMT-0700 (Pacific Daylight Time)");
//		Date endDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.'S'Z'").parse("Sun Jun 30 2019 03:42:11 GMT-0700 (Pacific Daylight Time)");
		Date startDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse("2019-05-23T12:00:00");
		System.out.println("______ The start date is : "+startDate.toString());
		Date endDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse("2019-06-30T12:00:00");
		System.out.println("______ The end date is : "+endDate.toString());
		test_hackhathon.setStartDate(startDate);
		test_hackhathon.setEndDate(endDate);
		test_hackhathon.setTeamSizeMin(1);
		test_hackhathon.setTeamSizeMax(6);
		test_hackhathon.setFee(100);
		test_hackhathon.setJudges(judges);
		test_hackhathon.setDiscount(new Double(0));	
	}
	
//	@Test
//	public void testCreateHackathon() throws Exception
//	{
//		
//		List<Integer> judgesId = new ArrayList<Integer>();
//		judgesId.add((int) user1.getId());
//		
//		// setting up response body for hackathon creation
//		HashMap<Object,Object> params_map = new HashMap<>();
//		params_map.put("name", "Code For Fun");
//		params_map.put("description", "This hackathon is for fun");
//		params_map.put("startDate", "Thu May 23 2019 03:42:11 GMT-0700 (Pacific Daylight Time)");
//		params_map.put("endDate", "Sun Jun 30 2019 03:42:11 GMT-0700 (Pacific Daylight Time)");
//		params_map.put("teamSizeMin", "1");
//		params_map.put("teamSizeMax", "6");
//		params_map.put("fee", "100");
//		params_map.put("judgesId", judgesId);
//		
//		when(hackdao.create(any(Hackathon.class))).thenReturn(test_hackhathon);
//		when(userdao.findUserbyID(anyLong())).thenReturn(user1);
//		
//		Gson gsonBuilder = new GsonBuilder().create();
//        String json = gsonBuilder.toJson(params_map);
//		
//		mockmvc.perform( MockMvcRequestBuilders
//	      .post("/hackathon")
//	      .content(json)
//	      .contentType("application/JSON")
//	      .accept("application/json"))
//	      .andExpect(MockMvcResultMatchers.status().isOk())
//	      .andExpect(MockMvcResultMatchers.jsonPath("$.msg").exists())
//	      .andExpect(MockMvcResultMatchers.jsonPath("$.msg", CoreMatchers.is("Successfully created")));
//	}
	
	@Test
	public void testGetHackathonDetails() throws Exception {
		HashMap<Object,Object> params_map = new HashMap<>();
		params_map.put("userId","111");
		
		Gson gsonBuilder = new GsonBuilder().create();
		String json = gsonBuilder.toJson(params_map);
		
		when(userdao.findUserbyID(anyLong())).thenReturn(test_user);
		when(hackdao.findById(anyLong())).thenReturn(test_hackhathon);
		
		mockmvc.perform( MockMvcRequestBuilders
			      .post("/hackathon/{id}", new Long(999))
			      .content(json)
			      .contentType("application/JSON")
			      .accept("application/json"))
			      .andExpect(MockMvcResultMatchers.status().isOk())
			      .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
			      .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is("Code For Fun")));
		
	}
	
	@Test
	public void testGetAllHackathons() throws Exception {
		
		List<Hackathon> all_hackathons = new ArrayList<>();
		all_hackathons.add(test_hackhathon);
		
		when(userdao.findUserbyID(anyLong())).thenReturn(user1);
		when(hackdao.findById(anyLong())).thenReturn(test_hackhathon);
		when(hackdao.findAll()).thenReturn(all_hackathons);
		
		mockmvc.perform(MockMvcRequestBuilders
				 .get("/hackathon/getAllHackathons/{userId}", new Long(222))
				 .accept("application/json"))
				 .andExpect(MockMvcResultMatchers.status().isOk())
				 .andExpect(MockMvcResultMatchers.jsonPath("$.hackathonDetails").exists())
				 .andExpect(MockMvcResultMatchers.jsonPath("$.hackathonDetails[0].name", CoreMatchers.is("Code For Fun")))
				 .andExpect(MockMvcResultMatchers.jsonPath("$.hackathonDetails[0].message", CoreMatchers.is("judge")));
	}
	
	@Test
	public void testOpenHackathon() throws Exception {
		
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date current_date = new Date();
		Calendar cal = Calendar.getInstance();
		
		HashMap<Object,Object> params_map = new HashMap<>();
		params_map.put("hackathonId","999");
		params_map.put("currentDate", current_date.getTime());
		
		Gson gsonBuilder = new GsonBuilder().create();
		String json = gsonBuilder.toJson(params_map);
		
		when(hackdao.findById(anyLong())).thenReturn(test_hackhathon);
		
		test_hackhathon.setStartDate(current_date);
		
		when(hackdao.updateById(anyLong(), any(Hackathon.class))).thenReturn(test_hackhathon);

		mockmvc.perform( MockMvcRequestBuilders
			      .post("/hackathon/open")
			      .content(json)
			      .contentType("application/JSON")
			      .accept("application/json"))
			      .andExpect(MockMvcResultMatchers.status().isOk());		
	}
	
	

}
