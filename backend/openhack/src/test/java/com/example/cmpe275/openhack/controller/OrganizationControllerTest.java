package com.example.cmpe275.openhack.controller;

//import static org.hamcrest.CoreMatchers.any;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;
import org.springframework.web.context.WebApplicationContext;

import com.example.cmpe275.openhack.dao.OrganizationDao;
import com.example.cmpe275.openhack.dao.UserDao;
import com.example.cmpe275.openhack.entity.Organization;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class OrganizationControllerTest {
	
	@Autowired
	private MockMvc mockmvc;
	
	@InjectMocks
	private OrganizationController orgController;
	
	@Mock
	OrganizationDao orgdao;
	
	@Before
	public void setUp() throws Exception{
		MockitoAnnotations.initMocks(this);
		mockmvc = MockMvcBuilders.standaloneSetup(orgController).build();
	}
	
	@Test
	public void testGetAllOrganizations() throws Exception {
		
		mockmvc.perform(MockMvcRequestBuilders.get("/hacker/getOrganizations"))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.organizations").exists());
	}
	
	@Test
	public void createOrganizationTest() throws Exception
	{	
		// creating a dummy organization
		HashMap<String,String> params_map = new HashMap<>();
		params_map.put("organization_name", "TestOrganization");
		params_map.put("organization_desc", "Test description of this organization");
		
		Organization test_org = new Organization();
		test_org.setName("TestOrganization");
		test_org.setDescription("Test description of this organization");
		
		when(orgdao.create(any(Organization.class))).thenReturn(test_org);
		
		Gson gsonBuilder = new GsonBuilder().create();
        String json = gsonBuilder.toJson(params_map);
		
		mockmvc.perform( MockMvcRequestBuilders
	      .post("/hacker/createOrganization")
	      .content(json)
	      .contentType("application/JSON")
	      .accept("application/json"))
	      .andExpect(MockMvcResultMatchers.status().isOk())
	      .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
	      .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is("TestOrganization")));
	}

}
