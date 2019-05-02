package com.example.cmpe275.openhack.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.example.cmpe275.openhack.entity.Organization;

public class OrganizationDaoImpl implements OrganizationDao 
{
	
	private EntityManagerFactory emfactory;
	
	public OrganizationDaoImpl()
	{
		emfactory = Persistence.createEntityManagerFactory("openhack");
	}

	@Override
	public Organization create(Organization org) 
	{
		EntityManager em = emfactory.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try
		{
			tx.begin();
			em.persist(org);
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
	public Organization findOrganizationById(long orgId) 
	{
		EntityManager em = emfactory.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try
		{
			tx.begin();
			Organization org = em.find(Organization.class, orgId);
			tx.commit();
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
	public List<Organization> findAllOrganization() 
	{
		EntityManager em = emfactory.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try
		{
			List<Organization> organizations = (List<Organization>) em.createQuery("from Organization").getResultList();
	        return organizations;
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
	public Organization update(Organization org) 
	{
		EntityManager em = emfactory.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try
		{
			tx.begin();
			Organization updated_organization = em.merge(org);
			tx.commit();
			System.out.println("\n- - - - - - - - - - Employer "+org.getName()+" updated successfully! - - - - - - - - - -\n");
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
	public Organization delete(long orgId) 
	{
		// TODO Auto-generated method stub
		return null;
	}


}
