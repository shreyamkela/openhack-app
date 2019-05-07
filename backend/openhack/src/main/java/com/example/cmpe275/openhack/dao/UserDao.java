package com.example.cmpe275.openhack.dao;

import java.util.List;
import java.util.Set;

import com.example.cmpe275.openhack.entity.User;

public interface UserDao {
	User createUser(User user);
	User findUserbyEmail(String email);
	User findUserbyID(long id);
	User updateUser(User user);
	User deleteUser(long id);
	List<User> findAllUsers();
}
