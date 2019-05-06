package com.example.cmpe275.openhack.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.example.cmpe275.openhack.entity.Hackathon;
import com.example.cmpe275.openhack.entity.Payment;

public class PaymentDaoImpl implements PaymentDao{

	
	private EntityManagerFactory emfactory;
	
	public PaymentDaoImpl() {
		// TODO Auto-generated constructor stub
		emfactory = Persistence.createEntityManagerFactory("openhack");
	}
	@Override
	public Payment createPayment(Payment payment) {
		// TODO Auto-generated method stub
		EntityManager em = emfactory.createEntityManager();
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
	public boolean deletePaymentByTeamId(long id) {
		// TODO Auto-generated method stub
		EntityManager em = emfactory.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.createQuery("delete from Team t where t.teamId = :id").setParameter(0, id);
			tx.commit();
			return true;
		}catch (Exception e) {
			// TODO: handle exception
			tx.rollback();
			throw e;
		}finally {
			em.close();
		}
	}

	@Override
	public Payment updatePayment(Payment payment) {
		// TODO Auto-generated method stub
		return null;
	}

}
