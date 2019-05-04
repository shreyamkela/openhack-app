package com.example.cmpe275.openhack.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.example.cmpe275.openhack.entity.User;

public class UserDaoImpl implements UserDao{

	private EntityManagerFactory emfactory;
	
	public UserDaoImpl() {
		// TODO Auto-generated constructor stub
		emfactory = Persistence.createEntityManagerFactory("openhack");
	}
	
	@Override
	public User findById(long id) {
		// TODO Auto-generated method stub
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
	
}
