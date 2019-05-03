package com.example.cmpe275.openhack.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.example.cmpe275.openhack.entity.Organization;
import com.example.cmpe275.openhack.entity.User;

public class UserDaoImpl implements UserDao {
	
	private EntityManagerFactory emfactory;
	
	public UserDaoImpl(){
		emfactory =Persistence.createEntityManagerFactory("openhack");
	}

	public User create(User user) {
		// TODO Auto-generated method stub
		EntityManager em = emfactory.createEntityManager();
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
//    @Override
	public User findUserbyID(long id) {
		EntityManager em = emfactory.createEntityManager();
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

	public User updateUser(User user) {
		EntityManager em = emfactory.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try
		{
			tx.begin();
			User updated_user = em.merge(user);
			tx.commit();
			System.out.println("\n- - - - - - - - - - User "+user.getName()+" updated successfully! - - - - - - - - - -\n");
			return updated_user;
		}
		catch(RuntimeException e)
		{
			tx.rollback();
			throw e;
		}
		finally
		{
			em.close();	
		}
	}

	public User deleteUser(long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
