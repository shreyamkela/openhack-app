package com.example.cmpe275.openhack.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cmpe275.openhack.entity.Submission;
import com.example.cmpe275.openhack.repository.SubmissionRepository;

@Service
public class SubmissionRepositoryService {

	@Autowired
	SubmissionRepository submissionRepository;
	
	@Autowired
	EntityManager em;
	
	@Transactional
	public Submission create(Submission submission) {
		
			Submission createdSubmission = submissionRepository.save(submission);
			return createdSubmission;
		
	}

	
	@Transactional
	public Submission updateById(long id, Submission submission) {
	
			Submission updatedSubmission = submissionRepository.save(submission);
	
			System.out.println("\n - - - - - - - - - - Submission " + submission.getURL() + " updated to "
					+ updatedSubmission.getURL() + " successfully! - - - - - - - - - - -\n");
			return updatedSubmission;
		}

	
	@Transactional
	public Submission deleteById(long Id) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Transactional
	public Submission findById(long id) {
			Submission submission = submissionRepository.getOne(id);		
			return submission;
		
	}

	
	@Transactional
	public List<Submission> findAll() {
			return submissionRepository.findAll();
			
	}

}
