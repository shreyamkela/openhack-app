package com.example.cmpe275.openhack.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cmpe275.openhack.entity.Payment;
import com.example.cmpe275.openhack.repository.PaymentRepository;

@Service
public class PaymentRepositoryService {

	
	@Autowired
	PaymentRepository paymentRepository;
	
	@Autowired
	EntityManager em;
	
	
	@Transactional
	public Payment createPayment(Payment payment) {
			Payment createdPayment = paymentRepository.save(payment);
			return createdPayment;
			}

	
	@Transactional
	public List<Payment> findPaymentByTeamId(long id) {
		List<Payment> result = new ArrayList<>();
		List<Payment> payments = paymentRepository.findAll();
		for(Payment payment:payments) {
			if(payment.getTeamId()== id) {
				result.add(payment);
			}
		}
		
		return result;
	}

	
	@Transactional
	public Payment updatePayment(Payment payment) {
			Payment updatedPayment = paymentRepository.save(payment);
			return updatedPayment;
	
	}
	
	
	@Transactional
	public Payment getPaymentById(long id) {
			Payment payment = paymentRepository.getOne(id); 		
			return payment;
		
	}
	
	
	@Transactional
	public Payment deletePayment(Payment payment) {
			paymentRepository.delete(payment);
			return payment;
	
	}

}
