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
	
	
	@Transactional
	public User createUser(User user) {
			User user2 = userRepository.save(user);
			System.out.println("\n - - - - - - - - - - User "+user.getName()+" added successfully! - - - - - - - - - - -\n");
			return user2;
	
	}
	
	
	@Transactional
	public User updateUser(User user) {
		
			User user2 = userRepository.save(user);
			System.out.println("\n - - - - - - - - - - User "+user.getName()+" updated successfully! - - - - - - - - - - -\n");
			return user2;
	}
	
	
	@Transactional
	public User findUserbyEmail(String email) {
//		
			 Query query = em.createQuery("SELECT u FROM User u WHERE u.email = :email");
		        query.setParameter("email", email);
		        User user = (User) query.getSingleResult();
//		
			return user;
	}
	
	
	@Transactional
	public User findUserbyID(long id) {
			User user = userRepository.getOne(id);
			return user;
	
	}
	
	
	@Transactional
	public User deleteUser(long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Transactional
	public List<User> findAllUsers(){
		return userRepository.findAll();
				
	}


}
