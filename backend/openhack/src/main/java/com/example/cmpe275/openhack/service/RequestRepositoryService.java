package com.example.cmpe275.openhack.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cmpe275.openhack.entity.Request;
import com.example.cmpe275.openhack.repository.PaymentRepository;
import com.example.cmpe275.openhack.repository.RequestRepository;

@Service
public class RequestRepositoryService {
	
	@Autowired
	RequestRepository requestRepository;
	
	@Autowired
	EntityManager em;


	public Request addRequest(Request request) 
	{
			requestRepository.save(request);
			System.out.println("\n - - - - - - - - - - Request "+request.getId()+" added successfully! - - - - - - - - - - -\n");
			return request;
		
	}
	

	public Request findRequestById(long requestId) 
	{
		Request req = requestRepository.getOne(requestId);
			return req;
		
	}
	

	public List<Request> getAllRequests() 
	{
//		
			return requestRepository.findAll();
		
	}
	

	public Request deleteRequest(long reqId) 
	{
			Request delete_request = requestRepository.getOne(reqId);
			requestRepository.delete(delete_request);
		
			System.out.println("\n - - - - - - - - - - Request "+delete_request.getId()+" removed successfully! - - - - - - - - - - \n");
			return delete_request;
		
	}

}
