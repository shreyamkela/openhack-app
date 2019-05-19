package com.example.cmpe275.openhack.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.transaction.Transactional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.example.cmpe275.openhack.EntityManagerSingleton;
import com.example.cmpe275.openhack.entity.Hackathon;
import com.example.cmpe275.openhack.entity.User;

//@Component
public class HackathonDaoImpl implements HackathonDao{

	
	private EntityManagerSingleton emfactory;

	public HackathonDaoImpl()
	{
		emfactory = EntityManagerSingleton.getInstance();
	}
	
	
	@Override
	@Transactional
	public Hackathon create(Hackathon hackathon) {
		// TODO Auto-generated method stub
		EntityManager em = emfactory.emfactory.createEntityManager();
//		EntityManager em = emfactory.em;
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Hackathon createdHackathon = em.merge(hackathon);
			tx.commit();
			System.out.println("Successfull creation inside DAO");
			return createdHackathon;
		}catch (Exception e) {
			// TODO: handle exception
			tx.rollback();
			throw e;
		}finally {
			em.close();
		}
	}

	@Override
	@Transactional
	public Hackathon updateById(long id, Hackathon hackathon) {
		// TODO Auto-generated method stub
		EntityManager em = emfactory.emfactory.createEntityManager();
//		EntityManager em = emfactory.em;
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Hackathon updatedHackathon = em.merge(hackathon);
			tx.commit();
			return updatedHackathon;
		}catch (Exception e) {
			// TODO: handle exception
			tx.rollback();
			throw e;
		}finally {
			em.close();
		}
	}

	@Override
	@Transactional
	public Hackathon deleteById(long Id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public Hackathon findById(long id) {
		// TODO Auto-generated method stub
		EntityManager em = emfactory.emfactory.createEntityManager();
//		EntityManager em = emfactory.em;
		try {
			
			
			Hackathon hackathon = em.find(Hackathon.class, id);
			
			return hackathon;
		}catch (Exception e) {
			// TODO: handle exception
			
			throw e;
		}finally {
//			em.close();
		}
	}

	@Override
	@Transactional
	public List<Hackathon> findAll() {
		// TODO Auto-generated method stub
		EntityManager em = emfactory.emfactory.createEntityManager();
//		EntityManager em = emfactory.em;
		try
		{
//			em.getTransaction().begin();
			return (List<Hackathon>) em.createQuery("select h from Hackathon h",
				    Hackathon.class).getResultList();
		}
		catch(RuntimeException e)
		{
//			em.getTransaction().rollback();
			throw e;
		}
		finally
		{
//			em.close();	
		}
	}

}
