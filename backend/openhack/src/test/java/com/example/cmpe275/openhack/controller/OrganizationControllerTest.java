package com.example.cmpe275.openhack.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.thymeleaf.standard.expression.AndExpression;

import com.example.cmpe275.openhack.dao.OrganizationDao;
import com.example.cmpe275.openhack.dao.RequestDaoImpl;
import com.example.cmpe275.openhack.dao.UserDao;
import com.example.cmpe275.openhack.entity.Organization;
import com.example.cmpe275.openhack.entity.Request;
import com.example.cmpe275.openhack.entity.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import static org.mockito.ArgumentMatchers.anyLong;

public class OrganizationControllerTest {
	
	@Autowired
	private MockMvc mockmvc;
	
	@InjectMocks
	private OrganizationController orgController;
	
	@Mock
	OrganizationDao orgdao;
	@Mock
	UserDao userdao;
	@Mock
	RequestDaoImpl reqdao;

	Organization test_org;
	User test_user;
	
	@Before
	public void setUp() throws Exception{
		MockitoAnnotations.initMocks(this);
		mockmvc = MockMvcBuilders.standaloneSetup(orgController).build();
		
		test_user = new User();
		test_user.setId(333);
		test_user.setName("Test");
		test_user.setLastname("User");
		test_user.setEmail("testuser@getnada.com");
		test_user.setScreenName("TestUser");
		test_user.setUsertype("user");
		test_user.setVerified("Y");
		
		test_org = new Organization();
		test_org.setId(222);
		test_org.setName("TestOrganization");
		test_org.setDescription("Test description of this organization");
		test_org.setOwner(test_user);
		
	}
	
	@Test
	public void testGetAllOrganizations() throws Exception {
		
		mockmvc.perform(MockMvcRequestBuilders.get("/hacker/getOrganizations"))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.organizations").exists());
	}
	
	@Test
	public void testCreateOrganization() throws Exception
	{	
		// creating a dummy organization
		HashMap<String,String> params_map = new HashMap<>();
		params_map.put("organization_name", "TestOrganization");
		params_map.put("organization_desc", "Test description of this organization");
		params_map.put("owner_id", "333");
		
		when(orgdao.create(any(Organization.class))).thenReturn(test_org);
		when(userdao.createUser(any(User.class))).thenReturn(test_user);
		when(userdao.findUserbyID(anyLong())).thenReturn(test_user);
		
		Gson gsonBuilder = new GsonBuilder().create();
        String json = gsonBuilder.toJson(params_map);
		
		mockmvc.perform( MockMvcRequestBuilders
	      .post("/hacker/createOrganization")
	      .content(json)
	      .contentType("application/JSON")
	      .accept("application/json"))
	      .andExpect(MockMvcResultMatchers.status().isOk())
	      .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
	      .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is("TestOrganization")))
	      .andExpect(MockMvcResultMatchers.jsonPath("$.owner.name", CoreMatchers.is("Test")));
	}

	@Test
	public void testGetOneOrganization() throws Exception {
		
		when(orgdao.findOrganizationById(anyLong())).thenReturn(test_org);
		when(userdao.findUserbyID(anyLong())).thenReturn(test_user);
		
		mockmvc.perform(MockMvcRequestBuilders
				 .get("/hacker/getOneOrganization/{user_id}/{org_id}", new Long(333), new Long(222))
				 .accept("application/json"))
				 .andExpect(MockMvcResultMatchers.status().isOk())
				 .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
				 .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(test_org.getName())))
				 .andExpect(MockMvcResultMatchers.jsonPath("$.user_org_status", CoreMatchers.is("owner")));
	}
	
	@Test
	public void testJoinOrganization() throws Exception {
		
		User user1 = new User();
		user1.setName("Mickey");
		user1.setLastname("Mouse");
		user1.setId(777);
		user1.setEmail("mickey@getnada.com");
		user1.setScreenName("MickeyMouse");
		
		Request req = new Request();
		req.setId(1);
		req.setRequested_by_user(user1);
		req.setRequested_for_org(test_org);
		List<Request> all_requests = new ArrayList<>();
		all_requests.add(req);
		
		when(orgdao.findOrganizationById(anyLong())).thenReturn(test_org);
		when(userdao.findUserbyID(anyLong())).thenReturn(user1);
		when(reqdao.getAllRequests()).thenReturn(all_requests);
		when(reqdao.deleteRequest(new Long(1))).thenReturn(req);
		user1.setOrganization(test_org);
		when(userdao.updateUser(any(User.class))).thenReturn(user1);
		
		mockmvc.perform(MockMvcRequestBuilders
				 .put("/hacker/joinOrganization/{user_id}/{org_id}/{request_id}", new Long(777), new Long(222), new Long(1))
				 .accept("application/json"))
				 .andExpect(MockMvcResultMatchers.status().isOk())
				 .andExpect(MockMvcResultMatchers.jsonPath("$.user.id").exists())
				 .andExpect(MockMvcResultMatchers.jsonPath("$.user.name", CoreMatchers.is("Mickey")))
				 .andExpect(MockMvcResultMatchers.jsonPath("$.user.organization").isNotEmpty())
				 .andExpect(MockMvcResultMatchers.jsonPath("$.user.judged_hackathons").isEmpty())
				 .andExpect(MockMvcResultMatchers.jsonPath("$.user.organization.name", CoreMatchers.is("TestOrganization")));
	}
}

