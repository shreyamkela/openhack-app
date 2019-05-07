package com.example.cmpe275.openhack.dao;

import java.util.List;

import com.example.cmpe275.openhack.entity.Payment;

public interface PaymentDao {

	public Payment createPayment(Payment payment);
	public List<Payment> findPaymentByTeamId(long id);
	public Payment deletePayment(Payment payment);
	public Payment updatePayment(Payment payment);
	public Payment getPaymentById(long id);
}
