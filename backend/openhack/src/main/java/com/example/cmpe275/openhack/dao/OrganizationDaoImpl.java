package com.example.cmpe275.openhack.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.transaction.Transactional;

import com.example.cmpe275.openhack.entity.Organization;

public class OrganizationDaoImpl implements OrganizationDao 
{
	
	private EntityManagerFactory emfactory;
	
	public OrganizationDaoImpl()
	{
		emfactory = Persistence.createEntityManagerFactory("openhack");
	}

	@Override
	@Transactional
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
	@Transactional
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
	@Transactional
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
	@Transactional
	public Organization update(Organization org) 
	{
		EntityManager em = emfactory.createEntityManager();
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
		EntityManager em = emfactory.createEntityManager();
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
