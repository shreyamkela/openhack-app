package com.example.cmpe275.openhack.dao;

import com.example.cmpe275.openhack.entity.Payment;

public interface PaymentDao {

	public Payment createPayment(Payment payment);
	public boolean deletePaymentByTeamId(long id);
	public Payment updatePayment(Payment payment);
}
