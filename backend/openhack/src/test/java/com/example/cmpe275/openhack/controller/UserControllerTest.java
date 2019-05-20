package com.example.cmpe275.openhack.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.cmpe275.openhack.dao.UserDao;
import com.example.cmpe275.openhack.entity.Address;
import com.example.cmpe275.openhack.entity.User;

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
	 }
	 
	 @Test
	 public void getAllUsers() {
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
