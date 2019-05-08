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

import com.example.cmpe275.openhack.entity.Hackathon;

@Component
public class HackathonDaoImpl implements HackathonDao{

	
	private EntityManagerFactory emfactory;

	public HackathonDaoImpl()
	{
		emfactory = Persistence.createEntityManagerFactory("openhack");
	}
	
	
	@Override
	@Transactional
	public Hackathon create(Hackathon hackathon) {
		// TODO Auto-generated method stub
		EntityManager em = emfactory.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Hackathon createdHackathon = em.merge(hackathon);
			tx.commit();
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
	public Hackathon updateById(long id, Hackathon hackathon) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Hackathon deleteById(long Id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public Hackathon findById(long id) {
		// TODO Auto-generated method stub
		EntityManager em = emfactory.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Hackathon hackathon = em.find(Hackathon.class, id);
			tx.commit();
			return hackathon;
		}catch (Exception e) {
			// TODO: handle exception
			tx.rollback();
			throw e;
		}finally {
			em.close();
		}
	}

	@Override
	public List<Hackathon> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
