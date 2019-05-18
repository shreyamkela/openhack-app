package com.example.cmpe275.openhack.dao;

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
import com.example.cmpe275.openhack.entity.Team;
//@Component
public class TeamDaoImpl implements TeamDao{

	
	private EntityManagerSingleton emfactory;

	public TeamDaoImpl()
	{
		emfactory = EntityManagerSingleton.getInstance();
	}

	@Override
	@Transactional
	public Team createTeam(Team team) {
		// TODO Auto-generated method stub
		EntityManager em = emfactory.emfactory.createEntityManager();
//		EntityManager em = emfactory.em;
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Team createdTeam = em.merge(team);
			tx.commit();
			return createdTeam;
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
	public Team updateTeam(Team team) {
		EntityManager em = emfactory.emfactory.createEntityManager();
//		EntityManager em = emfactory.em;
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Team updatedTeam = em.merge(team);
			System.out.println("Team updated"+updatedTeam);
			tx.commit();
			return updatedTeam;
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
	public Team getTeamById(long id) {
		// TODO Auto-generated method stub
//		EntityManager em = emfactory.emfactory.createEntityManager();
		EntityManager em = emfactory.em;
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Team team = em.find(Team.class, id);
			tx.commit();
			return team;
		}catch (Exception e) {
			// TODO: handle exception
			tx.rollback();
			throw e;
		}finally {
//			em.close();
		}
	}

	@Override
	@Transactional
	public Team deleteTeamById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
