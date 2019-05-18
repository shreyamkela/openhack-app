package com.example.cmpe275.openhack.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.example.cmpe275.openhack.EntityManagerSingleton;
import com.example.cmpe275.openhack.entity.Organization;
import com.example.cmpe275.openhack.entity.User;

//@Component
public class OrganizationDaoImpl implements OrganizationDao 
{
	
	private EntityManagerSingleton emfactory;
	
	public OrganizationDaoImpl()
	{
		emfactory = EntityManagerSingleton.getInstance();
	}

	@Override
	@Transactional
	public Organization create(Organization org) 
	{
		EntityManager em = emfactory.emfactory.createEntityManager();
//		EntityManager em = emfactory.em;
		EntityTransaction tx = em.getTransaction();
		try
		{
			tx.begin();
			em.merge(org);
			tx.commit();
			System.out.println("\n - - - - - - - - - - Organization "+org.getName()+" added successfully! - - - - - - - - - - -\n");
			return org;
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

	@Override
	@Transactional
	public Organization findOrganizationById(long orgId) 
	{
		EntityManager em = emfactory.emfactory.createEntityManager();
//		EntityManager em = emfactory.em;
//		EntityTransaction tx = em.getTransaction();
		try
		{
//			tx.begin();
			Organization org = em.find(Organization.class, orgId);
//			tx.commit();
			return org;
		}
		catch(RuntimeException e)
		{
//			tx.rollback();
			throw e;
		}
		finally
		{
//			em.close();	
		}
	}
	
	@Override
	@Transactional
	public Organization findOrganizationByName(String name) 
	{
//		EntityManager em = emfactory.emfactory.createEntityManager();
		EntityManager em = emfactory.em;
		Organization org =null;
		try
		{
//			em.getTransaction().begin();
			 Query query = em.createQuery("SELECT o FROM Organization o WHERE o.name = :name");
		        query.setParameter("name", name);
		        org = (Organization) query.getSingleResult();
//			em.getTransaction().commit();
			return org;
		}
		catch(RuntimeException e)
		{
//			em.getTransaction().rollback();
			throw e;
		}
		finally
		{
			//em.close();	
		}
	}
	
	@Override
//	@Transactional
	public List<Organization> findAllOrganization() 
	{
		EntityManager em = emfactory.emfactory.createEntityManager();
//		EntityManager em = emfactory.em;
//		EntityTransaction tx = em.getTransaction();
		try
		{
//			tx.begin();
			List<Organization> organizations = (List<Organization>) em.createQuery("from Organization").getResultList();
//			tx.commit();
	        return organizations;
		}
		catch(RuntimeException e)
		{
//			tx.rollback();
			throw e;
		}
		finally
		{
//			em.close();	
		}
	}

	@Override
	@Transactional
	public Organization update(Organization org) 
	{
		EntityManager em = emfactory.emfactory.createEntityManager();
//		EntityManager em = emfactory.em;
		EntityTransaction tx = em.getTransaction();
		try
		{
			tx.begin();
			Organization updated_organization = em.merge(org);
			tx.commit();
			System.out.println("\n- - - - - - - - - - Organization "+org.getName()+" updated successfully! - - - - - - - - - -\n");
			return updated_organization;
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

	@Override
	@Transactional
	public Organization delete(long orgId) 
	{
		EntityManager em = emfactory.emfactory.createEntityManager();
//		EntityManager em = emfactory.em;
		EntityTransaction tx = em.getTransaction();
		try
		{
			tx.begin();
			Organization delete_organization = em.find(Organization.class, orgId);
			em.remove(delete_organization);
			tx.commit();
			System.out.println("\n - - - - - - - - - - Organization "+delete_organization.getName()+" deleted successfully! - - - - - - - - - - \n");
			return delete_organization;
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
