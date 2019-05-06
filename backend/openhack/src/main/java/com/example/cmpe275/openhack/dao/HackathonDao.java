package com.example.cmpe275.openhack.dao;

import java.util.List;

import com.example.cmpe275.openhack.entity.Hackathon;

public interface HackathonDao {

	public Hackathon create(Hackathon hackathon);
	public Hackathon updateById(long id,Hackathon hackathon);
	public Hackathon deleteById(long Id);
	public Hackathon findById(long id);
	public List<Hackathon> findAll();
}
