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

import com.example.cmpe275.openhack.EntityManagerSingleton;
import com.example.cmpe275.openhack.entity.Hackathon;
import com.example.cmpe275.openhack.entity.Payment;
import com.example.cmpe275.openhack.entity.User;

//@Component
public class PaymentDaoImpl implements PaymentDao{

	
	private EntityManagerSingleton emfactory;
	
	public PaymentDaoImpl() {
		// TODO Auto-generated constructor stub
		emfactory = EntityManagerSingleton.getInstance();
	}
	
	@Override
	@Transactional
	public Payment createPayment(Payment payment) {
		// TODO Auto-generated method stub
		EntityManager em = emfactory.emfactory.createEntityManager();
//		EntityManager em = emfactory.em;
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Payment createdPayment = em.merge(payment);
			tx.commit();
			return createdPayment;
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
	public List<Payment> findPaymentByTeamId(long id) {
		// TODO Auto-generated method stub
//		EntityManager em = emfactory.emfactory.createEntityManager();
		EntityManager em = emfactory.em;
		
		try {
			
			return (List<Payment>) em.createQuery("select p from Payment p where p.teamId = :teamId",
				    Payment.class).setParameter("teamId",id).getResultList();
		}catch (Exception e) {
			// TODO: handle exception
			
			throw e;
		}finally {
//			em.close();
		}
	}

	@Override
	@Transactional
	public Payment updatePayment(Payment payment) {
		// TODO Auto-generated method stub
		EntityManager em = emfactory.emfactory.createEntityManager();
//		EntityManager em = emfactory.em;
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Payment updatedPayment = em.merge(payment);
			tx.commit();
			return updatedPayment;
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
	public Payment getPaymentById(long id) {
		// TODO Auto-generated method stub
//		EntityManager em = emfactory.emfactory.createEntityManager();
		EntityManager em = emfactory.em;
		EntityTransaction tx = em.getTransaction();
		try {
			
			Payment payment = em.find(Payment.class, id);
			
			return payment;
		}catch (Exception e) {
			// TODO: handle exception
			
			throw e;
		}finally {
//			em.close();
		}
	}
	
	@Override
	@Transactional
	public Payment deletePayment(Payment payment) {
		// TODO Auto-generated method stub
		EntityManager em = emfactory.emfactory.createEntityManager();
//		EntityManager em = emfactory.em;
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.remove(payment);
			tx.commit();
			return payment;
		}catch (Exception e) {
			// TODO: handle exception
			tx.rollback();
			throw e;
		}finally {
			em.close();
		}
	}

}
