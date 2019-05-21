package com.example.cmpe275.openhack.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.example.cmpe275.openhack.dao.UserDao;
import com.example.cmpe275.openhack.entity.Organization;
import com.example.cmpe275.openhack.entity.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UserControllerTest {
	
	@Autowired
	private MockMvc mockmvc;
	
	 @InjectMocks
	 UserController userController;
	 
	 @Mock
	 UserDao userdao;
	 
	 User user;
	 
	 @Before
	 public void setUp() {
		 MockitoAnnotations.initMocks(this);
		 mockmvc = MockMvcBuilders.standaloneSetup(userController).build();
		 
		 user = new User();
		 user.setId(111);
		 user.setName("Daisy");
		 user.setLastname("The Flower");
		 user.setEmail("daisy@getnada.com");
		 user.setScreenName("YellowDaisy");
		 user.setUsertype("user");
		 user.setVerified("Y");
	 }
	 
	 @Test
	 public void testGetUserById() {
		 when(userdao.findUserbyID(anyLong())).thenReturn(user);
		 
		 Map<Object, Object> returned_user = userController.getUserByID(new Long(111));
		 assertNotNull(returned_user);
		 assertEquals(returned_user.get("name"), user.getName());
		 assertEquals(returned_user.get("email"), user.getEmail());
		 assertEquals(returned_user.get("screenName"), user.getScreenName());
	 }
	 
	 @Test
	 public void testGetAllUsers() {
		 when(userdao.findAllUsers()).thenReturn(dummyUsers());
		 Map<Object, Object> ansMap = userController.getAllUser();
		 
		 List<Map<Object,Object>> ansUserList = (List<Map<Object,Object>>) ansMap.get("userDetails");
		 
		 assertEquals(ansUserList.size(), dummyUsers().size());
		 assertEquals(ansUserList.get(0).get("name"), dummyUsers().get(0).getName());
	 }
	 
	 @Test
	 public void testGetAllScreenNames() throws Exception {
			mockmvc.perform(MockMvcRequestBuilders.get("/getallscreennames"))
					.andExpect(MockMvcResultMatchers.status().isOk());
		}
	 
	 @Test 
	 public void testGetUserByEmail() throws Exception {
		 
		 when(userdao.findUserbyEmail(anyString())).thenReturn(user);
		 
		 Map<Object, Object> returned_user = userController.getUser("daisy@getnada.com");
		 assertNotNull(returned_user);
		 assertEquals(returned_user.get("name"), user.getName());
		 assertEquals(returned_user.get("email"), user.getEmail());
		 assertEquals(returned_user.get("screenName"), user.getScreenName());
		 
//		 mockmvc.perform(MockMvcRequestBuilders
//		 .get("/getuser/{email}", "daisy@getnada.com")
//		 .accept("application/json"))
//		 .andExpect(MockMvcResultMatchers.status().isOk());
	 }
	 
	 @Test
	 public void testAddUser() throws Exception {
		 
		 User test_user = new User();
		 test_user.setId(333);
		 test_user.setName("Test");
		 test_user.setLastname("User");
		 test_user.setEmail("testuser@getnada.com");
		 test_user.setScreenName("TestUser");
		 test_user.setUsertype("user");
		 test_user.setVerified("Y");
		 
		 when(userdao.createUser(any(User.class))).thenReturn(test_user);
		 
		 HashMap<String,String> params_map = new HashMap<>();
		 params_map.put("name", test_user.getName());
		 params_map.put("screenName", test_user.getScreenName());
		 params_map.put("email", test_user.getEmail());
		 params_map.put("lastname", test_user.getLastname());
		 params_map.put("usertype", test_user.getUsertype());
			
		 Gson gsonBuilder = new GsonBuilder().create();
	     String json = gsonBuilder.toJson(params_map);
	     
	     mockmvc.perform( MockMvcRequestBuilders
	   	      .post("/adduser")
	   	      .content(json)
	   	      .contentType("application/JSON")
	   	      .accept("application/json"))
	     	  .andDo(MockMvcResultHandlers.print())
	   	      .andExpect(MockMvcResultMatchers.status().isOk())
	   	      .andExpect(MockMvcResultMatchers.jsonPath("$.name").exists())
	   	      .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is("testuser@getnada.com")))
	   	      .andExpect(MockMvcResultMatchers.jsonPath("$.screenName", CoreMatchers.is("TestUser")));
	 }
	 
	 @Test
	 public void testUpdateUserById() {
		 when(userdao.findUserbyID(anyLong())).thenReturn(user);
		 user.setTitle("Flower");
		 user.setAboutMe("I am a flower. I love sun");
		 when(userdao.updateUser(any(User.class))).thenReturn(user);
		 
		 HashMap<String, String> map = new HashMap<>();
		 map.put("aboutMe", user.getAboutMe());
		 map.put("title", user.getTitle());
		 
		 User updated_user = userController.updateUser(new Long(111), map);
		 
		 assertNotNull(updated_user);
		 assertEquals(updated_user.getAboutMe(), "I am a flower. I love sun");
		 assertEquals(updated_user.getTitle(), "Flower");
		 
	 }
	 
	 private List<User> dummyUsers(){
		 List<User> userslist = new ArrayList<>();
		 Map<Object, Object> returnmap = new HashMap<>(); 
		 User user1 = new User(1, "Tom", "tom@getnada.com", "123456", "BigTom", null, "I am a student", "Java Developer", null);
		 user1.setUsertype("user");
		 user.setVerified("Y");
		 User user2 = new User(2, "Jerry", "jerry@sjsu.edu", "123456", "LittleJerry", null, "I am a programmer", "Data Scientist", null);
		 user2.setUsertype("admin");
		 userslist.add(user1);
		 userslist.add(user2);

		 return userslist;
	 }

}
