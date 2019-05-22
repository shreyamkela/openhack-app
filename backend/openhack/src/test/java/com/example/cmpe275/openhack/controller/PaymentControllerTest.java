package com.example.cmpe275.openhack.controller;

import static org.mockito.ArgumentMatchers.any;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import org.hamcrest.CoreMatchers;

import com.example.cmpe275.openhack.entity.Payment;
import com.example.cmpe275.openhack.service.PaymentRepositoryService;

public class PaymentControllerTest {
	
	@Autowired
	private MockMvc mockmvc;
	
	@InjectMocks
	private PaymentController paymentController;
	
	@Mock
	PaymentRepositoryService paymentdao;
	
	Payment test_payment;
	
	@Before
	public void setUp() throws Exception{
		MockitoAnnotations.initMocks(this);
		mockmvc = MockMvcBuilders.standaloneSetup(paymentController).build();
		
		test_payment = new Payment();
		test_payment.setId(555);
		test_payment.setFee(100);
		test_payment.setMemberId(111);
		test_payment.setTeamId(new Long(222));
		test_payment.setStatus(false);
		
	}
	
	@Test
	public void testGetPayment() throws Exception {
		
		when(paymentdao.getPaymentById(anyLong())).thenReturn(test_payment);
		
		mockmvc.perform(MockMvcRequestBuilders.get("/payment/{id}", new Long(555)))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.teamId").exists());
	}

}
