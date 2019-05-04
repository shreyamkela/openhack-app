package com.example.cmpe275.openhack.entity;

import java.util.Date;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.cmpe275.openhack.entity.*;

@Entity
@Table
public class Hackathon {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column(name="Hackathon_name",unique=true,nullable=false)
	private String name;
	private Date startDate;
	private Date endDate;
	private String description; // text, at least 10 alphabetic characters
	private double fee; // USD
	private int teamSizeMin; // inclusive
	private int teamSizeMax; // inclusive
	private double discount; // percentage
	
	//@ManyToMany(mappedBy="judgedHackathons",cascade= {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.REMOVE})
	@ManyToMany(cascade= {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.REMOVE})
	@JoinTable(
			name="Judge_Hackathons",
			joinColumns= {@JoinColumn(name="Hackathon",referencedColumnName="id")},
			inverseJoinColumns= {@JoinColumn(name="User",referencedColumnName="id")})
	private Set<User> judges;

	//@ManyToMany(mappedBy="sponsoredHackathons",cascade= {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.REMOVE})
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
			name="Sponsored_Hackathons",
			joinColumns= {@JoinColumn(name="Hackathon",referencedColumnName="id")},
			inverseJoinColumns= {@JoinColumn(name="Organization",referencedColumnName="id")})
	private Set<Organization> sponsors;

	@OneToMany(mappedBy="hackathon",cascade= {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.REMOVE})
	private Set<Submission> submissions;
//
	@ManyToMany(mappedBy="participatedHackathon",cascade= {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.REMOVE})
	private Set<Team> teams;
	
	public Hackathon() 
	{}

	public Hackathon(long id, String name, Date startDate, Date endDate, String description, double fee, int teamSizeMin,
		int teamSizeMax) {
	super();
	this.id = id;
	this.name = name;
	this.startDate = startDate;
	this.endDate = endDate;
	this.description = description;
	this.fee = fee;
	this.teamSizeMin = teamSizeMin;
	this.teamSizeMax = teamSizeMax;
}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Set<User> getJudges() {
		return judges;
	}

	public void setJudges(Set<User> judges) {
		this.judges = judges;
	}

	public Set<Submission> getSubmissions() {
		return submissions;
	}

	public void setSubmissions(Set<Submission> submissions) {
		this.submissions = submissions;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getFee() {
		return fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

	public int getTeamSizeMin() {
		return teamSizeMin;
	}

	public void setTeamSizeMin(int teamSizeMin) {
		this.teamSizeMin = teamSizeMin;
	}

	public int getTeamSizeMax() {
		return teamSizeMax;
	}

	public void setTeamSizeMax(int teamSizeMax) {
		this.teamSizeMax = teamSizeMax;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public Set<Organization> getSponsors() {
		return sponsors;
	}

	public void setSponsors(Set<Organization> sponsors) {
		this.sponsors = sponsors;
	}

	public Set<Team> getTeams() {
		return teams;
	}

	public void setTeams(Set<Team> teams) {
		this.teams = teams;
	}	

}
