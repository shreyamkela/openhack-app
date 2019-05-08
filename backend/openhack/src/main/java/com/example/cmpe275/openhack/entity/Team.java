package com.example.cmpe275.openhack.entity;
import java.util.*;

import javax.persistence.CascadeType;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;

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
	
	private String Idea;

	@OneToOne
	private User teamLead;
	
	//@ManyToMany(mappedBy="teams", fetch=FetchType.EAGER)
	@ManyToMany(cascade= {CascadeType.DETACH,CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.REMOVE},fetch=FetchType.EAGER)
	@JoinTable(
			name="User_Teams",
			joinColumns= {@JoinColumn(name="Team",referencedColumnName="id")},
			inverseJoinColumns= {@JoinColumn(name="User",referencedColumnName="id")})
	private Set<User> members;

	@ManyToOne(fetch=FetchType.EAGER, cascade={CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name="participatedHackathon")
	private Hackathon participatedHackathon;

	@ColumnDefault(value="false")
	private boolean paymentStatus;
	
	@ColumnDefault(value="false")
	private boolean submitted;
	
	@ColumnDefault(value="false")
	private boolean graded;
	
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

	public String getIdea() {
		return Idea;
	}

	public void setIdea(String idea) {
		Idea = idea;
	}

	public Hackathon getParticipatedHackathon() {
		return participatedHackathon;
	}

	public void setParticipatedHackathon(Hackathon participatedHackathon) {
		this.participatedHackathon = participatedHackathon;
	}

	public boolean getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(boolean paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public boolean isSubmitted() {
		return submitted;
	}

	public void setSubmitted(boolean submitted) {
		this.submitted = submitted;
	}

	public boolean isGraded() {
		return graded;
	}

	public void setGraded(boolean graded) {
		this.graded = graded;
	}
	
	
}
