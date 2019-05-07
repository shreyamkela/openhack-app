package com.example.cmpe275.openhack.dao;

import com.example.cmpe275.openhack.entity.Team;

public interface TeamDao {
	
	public Team createTeam(Team team);
	public Team updateTeam(Team team);
	public Team getTeamById(long id);
	public Team deleteTeamById(long id);
	

}
