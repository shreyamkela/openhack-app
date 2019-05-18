package com.example.cmpe275.openhack.dao;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.transaction.Transactional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.example.cmpe275.openhack.EntityManagerSingleton;
import com.example.cmpe275.openhack.entity.Organization;
import com.example.cmpe275.openhack.entity.Request;

//@Component
public class RequestDaoImpl 
{
	
    private EntityManagerSingleton emfactory;
	
	public RequestDaoImpl()
	{
		emfactory = EntityManagerSingleton.getInstance();
	}

	@Transactional
	public Request addRequest(Request request) 
	{
		EntityManager em = emfactory.emfactory.createEntityManager();
//		EntityManager em = emfactory.em;
		EntityTransaction tx = em.getTransaction();
		try
		{
			tx.begin();
			em.merge(request);
			tx.commit();
			System.out.println("\n - - - - - - - - - - Request "+request.getId()+" added successfully! - - - - - - - - - - -\n");
			return request;
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
	
	@Transactional
	public Request findRequestById(long requestId) 
	{
//		EntityManager em = emfactory.emfactory.createEntityManager();
		EntityManager em = emfactory.em;
		EntityTransaction tx = em.getTransaction();
		try
		{
			tx.begin();
			Request req = em.find(Request.class, requestId);
			tx.commit();
			return req;
		}
		catch(RuntimeException e)
		{
			tx.rollback();
			throw e;
		}
		finally
		{
//			em.close();	
		}
	}
	
	@Transactional
	public List<Request> getAllRequests() 
	{
//		EntityManager em = emfactory.emfactory.createEntityManager();
		EntityManager em = emfactory.em;
		
		try
		{
			List<Request> requests = (List<Request>) em.createQuery("from Request").getResultList();
	        return requests;
		}
		catch(RuntimeException e)
		{
			
			throw e;
		}
		finally
		{
//			em.close();	
		}
	}
	
	@Transactional
	public Request deleteRequest(long reqId) 
	{
		EntityManager em = emfactory.emfactory.createEntityManager();
//		EntityManager em = emfactory.em;
		EntityTransaction tx = em.getTransaction();
		try
		{
			tx.begin();
			Request delete_request = em.find(Request.class, reqId);
			em.remove(delete_request);
			tx.commit();
			System.out.println("\n - - - - - - - - - - Request "+delete_request.getId()+" removed successfully! - - - - - - - - - - \n");
			return delete_request;
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
	
}
