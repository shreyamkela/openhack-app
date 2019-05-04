package com.example.cmpe275.openhack.dao;

import com.example.cmpe275.openhack.entity.User;

public interface UserDao {
	User create(User user);
	User findUserbyID(long id);
	User updateUser(User user);
	User deleteUser(long id);
}
