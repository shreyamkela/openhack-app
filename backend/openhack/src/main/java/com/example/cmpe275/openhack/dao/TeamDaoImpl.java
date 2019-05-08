package com.example.cmpe275.openhack.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.example.cmpe275.openhack.entity.Hackathon;
import com.example.cmpe275.openhack.entity.Team;
@Component
public class TeamDaoImpl implements TeamDao{

	
	private EntityManagerFactory emfactory;

	public TeamDaoImpl()
	{
		emfactory = Persistence.createEntityManagerFactory("openhack");
	}

	@Override
	public Team createTeam(Team team) {
		// TODO Auto-generated method stub
		EntityManager em = emfactory.createEntityManager();
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
	public Team updateTeam(Team team) {
		EntityManager em = emfactory.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Team updatedTeam = em.merge(team);
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
	public Team getTeamById(long id) {
		// TODO Auto-generated method stub
		EntityManager em = emfactory.createEntityManager();
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
			em.close();
		}
	}

	@Override
	public Team deleteTeamById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
