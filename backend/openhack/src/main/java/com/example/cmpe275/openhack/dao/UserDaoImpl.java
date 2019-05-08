package com.example.cmpe275.openhack.dao;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.example.cmpe275.openhack.EntityManagerSingleton;
import com.example.cmpe275.openhack.entity.Organization;
import com.example.cmpe275.openhack.entity.User;

//@Component
public class UserDaoImpl implements UserDao {
	
	
	private EntityManagerSingleton emfactory;
	
	public UserDaoImpl(){
		emfactory = emfactory.getInstance();
	}
	
	@Override
	@Transactional
	public User createUser(User user) {
		// TODO Auto-generated method stub
		EntityManager em = emfactory.emfactory.createEntityManager();
		try
		{			
			em.getTransaction().begin();
			User user2 = em.merge(user);
			em.getTransaction().commit();
			System.out.println("\n - - - - - - - - - - User "+user.getName()+" added successfully! - - - - - - - - - - -\n");
			return user2;
		}
		catch(RuntimeException e)
		{	
			em.getTransaction().rollback();
			throw e;
		}
		finally
		{
			em.close();	
		}
	}
	
	@Override
	@Transactional
	public User updateUser(User user) {
		// TODO Auto-generated method stub
		EntityManager em = emfactory.emfactory.createEntityManager();
		try
		{			
			em.getTransaction().begin();
			User user2 = em.merge(user);
			em.getTransaction().commit();
			System.out.println("\n - - - - - - - - - - User "+user.getName()+" updated successfully! - - - - - - - - - - -\n");
			return user2;
		}
		catch(RuntimeException e)
		{	
			em.getTransaction().rollback();
			throw e;
		}
		finally
		{
			em.close();	
		}
	}
	
	@Override
	@Transactional
	public User findUserbyEmail(String email) {
		EntityManager em = emfactory.emfactory.createEntityManager();
		try
		{
			em.getTransaction().begin();
			 Query query = em.createQuery("SELECT u FROM User u WHERE u.email = :email");
		        query.setParameter("email", email);
		        User user = (User) query.getSingleResult();
			em.getTransaction().commit();
			return user;
		}
		catch(RuntimeException e)
		{
			em.getTransaction().rollback();
			throw e;
		}
		finally
		{
			em.close();	
		}
	}
	
	@Override
	@Transactional
	public User findUserbyID(long id) {
		EntityManager em = emfactory.emfactory.createEntityManager();
		try
		{
			em.getTransaction().begin();
			User user = em.find(User.class, id);
			em.getTransaction().commit();
			return user;
		}
		catch(RuntimeException e)
		{
			em.getTransaction().rollback();
			throw e;
		}
		finally
		{
			em.close();	
		}
	}
	
	@Override
	@Transactional
	public User deleteUser(long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	@Transactional
	public List<User> findAllUsers(){
		EntityManager em = emfactory.emfactory.createEntityManager();
		try
		{
			em.getTransaction().begin();
			String verified="Y";
			String usertype="user";
			return (List<User>) em.createQuery("select e from User e where e.verified = :verified and e.usertype= :usertype ",
				    User.class).setParameter("verified",verified ).setParameter("usertype",usertype ).getResultList();
		}
		catch(RuntimeException e)
		{
			em.getTransaction().rollback();
			throw e;
		}
		finally
		{
			em.close();	
		}
		
	}

}
