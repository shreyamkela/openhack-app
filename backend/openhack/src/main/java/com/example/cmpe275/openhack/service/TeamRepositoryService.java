package com.example.cmpe275.openhack.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cmpe275.openhack.entity.Team;
import com.example.cmpe275.openhack.repository.TeamRepository;

@Service
public class TeamRepositoryService {

	@Autowired
	EntityManager em;
	
	@Autowired
	TeamRepository teamRepository;
	
	@Transactional
	public Team createTeam(Team team) {
			Team createdTeam = teamRepository.save(team);
			return createdTeam;
	}


	@Transactional
	public Team updateTeam(Team team) {
			Team updatedTeam = teamRepository.save(team);
			System.out.println("Team updated"+updatedTeam);
			return updatedTeam;
		
	}

	
	@Transactional
	public Team getTeamById(long id) {
			Team team = teamRepository.getOne(id);	
			return team;
		
	}

	@Transactional
	public Team deleteTeamById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
