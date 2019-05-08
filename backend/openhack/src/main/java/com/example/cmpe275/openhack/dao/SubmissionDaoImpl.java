package com.example.cmpe275.openhack.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.transaction.Transactional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.example.cmpe275.openhack.entity.Submission;
import com.example.cmpe275.openhack.entity.User;

//@Component
public class SubmissionDaoImpl implements SubmissionDao {


	private EntityManagerFactory emfactory;

	public SubmissionDaoImpl() {
		emfactory = Persistence.createEntityManagerFactory("openhack");
	}

	@Override
	@Transactional
	public Submission create(Submission submission) {
		EntityManager em = emfactory.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Submission createdSubmission = em.merge(submission);
			tx.commit();
			return createdSubmission;
		} catch (Exception e) {
			tx.rollback();
			throw e;
		} finally {
			em.close();
		}
	}

	@Override
	@Transactional
	public Submission updateById(long id, Submission submission) {
		EntityManager em = emfactory.createEntityManager();
		try {
			em.getTransaction().begin();
			Submission updatedSubmission = em.merge(submission);
			em.getTransaction().commit();
			System.out.println("\n - - - - - - - - - - Submission " + submission.getURL() + " updated to "
					+ updatedSubmission.getURL() + " successfully! - - - - - - - - - - -\n");
			return updatedSubmission;
		} catch (RuntimeException e) {
			em.getTransaction().rollback();
			throw e;
		} finally {
			em.close();
		}
	}

	@Override
	public Submission deleteById(long Id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public Submission findById(long id) {
		EntityManager em = emfactory.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Submission submission = em.find(Submission.class, id);
			tx.commit();
			return submission;
		} catch (Exception e) {
			tx.rollback();
			throw e;
		} finally {
			em.close();
		}
	}

	@Override
	@Transactional
	public List<Submission> findAll() {
		EntityManager em = emfactory.createEntityManager();
		try
		{
			em.getTransaction().begin();
			return (List<Submission>) em.createQuery("select s from Submission s",Submission.class).getResultList();
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
		
		// TODO Auto-generated method stub
		
	}

}
