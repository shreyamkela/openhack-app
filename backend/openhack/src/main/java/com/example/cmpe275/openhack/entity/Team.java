package com.example.cmpe275.openhack.entity;
import java.util.*;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.example.cmpe275.openhack.entity.*;

@Entity
public class Team {
	
	public Team() {}
	
	public Team(long id, String teamName, User teamLead) {
		super();
		this.id = id;
		this.teamName = teamName;
		this.teamLead = teamLead;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column(name="Team_Name",unique=true, nullable=false)
	private String teamName;
	
	@OneToOne
	private User teamLead;
	
	@ManyToMany(mappedBy="teams")
	private Set<User> members;

	@ManyToMany
	@JoinTable(
			name="Hackathon_Participated",
			joinColumns= {@JoinColumn(name="Team",referencedColumnName="id")},
			inverseJoinColumns= {@JoinColumn(name="Hackathon",referencedColumnName="id")})
	private Set<Hackathon> participatedHackathon;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public User getTeamLead() {
		return teamLead;
	}

	public void setTeamLead(User teamLead) {
		this.teamLead = teamLead;
	}

	public Set<User> getMembers() {
		return members;
	}

	public void setMembers(Set<User> members) {
		this.members = members;
	}

	public Set<Hackathon> getParticipatedHackathon() {
		return participatedHackathon;
	}

	public void setParticipatedHackathon(Set<Hackathon> participatedHackathon) {
		this.participatedHackathon = participatedHackathon;
	}
	
}
