package com.example.cmpe275.openhack.dao;

import java.util.List;

import com.example.cmpe275.openhack.entity.Submission;

public interface SubmissionDao {
	public Submission create(Submission submission);

	public Submission updateById(long id, Submission submission);

	public Submission deleteById(long Id);

	public Submission findById(long id);

	public List<Submission> findAll();
	
}
