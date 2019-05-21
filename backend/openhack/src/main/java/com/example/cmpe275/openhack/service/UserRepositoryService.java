package com.example.cmpe275.openhack.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cmpe275.openhack.entity.User;
import com.example.cmpe275.openhack.repository.UserRepository;

@Service
public class UserRepositoryService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	EntityManager em;
	

	public User createUser(User user) {
			User user2 = userRepository.save(user);
			System.out.println("\n - - - - - - - - - - User "+user.getName()+" added successfully! - - - - - - - - - - -\n");
			return user2;
	
	}
	

	public User updateUser(User user) {
		
			User user2 = userRepository.save(user);
			System.out.println("\n - - - - - - - - - - User "+user.getName()+" updated successfully! - - - - - - - - - - -\n");
			return user2;
	}
	
	

	public User findUserbyEmail(String email) {
//		
			 Query query = em.createQuery("SELECT u FROM User u WHERE u.email = :email");
		        query.setParameter("email", email);
		        User user = (User) query.getSingleResult();
//		
			return user;
	}
	
	
	public User findUserbyID(long id) {
			User user = userRepository.getOne(id);
			return user;
	
	}
	
	

	public User deleteUser(long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	public List<User> findAllUsers(){
		List<User> users = userRepository.findAll();
		List<User> results = null;
		for(User user:users) {
			if(user.getVerified().equals("Y") && user.getUsertype().equals("user")) {
				results.add(user);
			}
		}
		return results;
	}


}
