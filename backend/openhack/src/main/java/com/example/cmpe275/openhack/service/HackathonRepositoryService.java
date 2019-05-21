package com.example.cmpe275.openhack.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cmpe275.openhack.entity.Hackathon;
import com.example.cmpe275.openhack.repository.HackathonRepository;

@Service
public class HackathonRepositoryService {

	@Autowired
	HackathonRepository hackathonRepository;


	public Hackathon create(Hackathon hackathon) {	
		Hackathon createdHackathon = hackathonRepository.save(hackathon);
		System.out.println("Successfull creation inside DAO");
		return createdHackathon;
	}


	public Hackathon updateById(long id, Hackathon hackathon) {
		Hackathon updatedHackathon = hackathonRepository.save(hackathon);
		return updatedHackathon;
	}

	public Hackathon deleteById(long Id) {
		// TODO Auto-generated method stub
		return null;
	}

	public Hackathon findById(long id) {
		Hackathon hackathon = hackathonRepository.getOne(id);			
		return hackathon;

	}

	public List<Hackathon> findAll() {
		return hackathonRepository.findAll();
	}
}
