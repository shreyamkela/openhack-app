package com.example.cmpe275.openhack.entity;

import java.util.List;
import java.util.Set;

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
import javax.persistence.Table;



@Entity
@Table
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Column(nullable=false)
	private String name;
	@Column(nullable=false,unique=true)
	private String email;
	private String password;
	private String screenName;
	
	@Embedded
	private Address address;
	
	private String aboutMe;
	private String title;
	private String imageurl;
	private String verified;
	private String usertype;
	private String lastname;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade={CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name="organization_id")
	private Organization organization;

	
//	@ManyToMany(fetch=FetchType.EAGER)
	@ManyToMany(mappedBy="judges",cascade= {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.REMOVE},fetch=FetchType.EAGER)
	private Set<Hackathon> judgedHackathons;
	
//	@ManyToMany(fetch=FetchType.EAGER)
	@ManyToMany(cascade= {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.REMOVE})
	@JoinTable(
			name="User_Teams",
			joinColumns= {@JoinColumn(name="User",referencedColumnName="id")},
			inverseJoinColumns= {@JoinColumn(name="Team",referencedColumnName="id")})
	private Set<Team> teams;
	
	public User() {
		
	}
	
	public User(long id, String name, String email, String password, String screenName, Address address, String aboutMe,
			String title, String imageurl) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.screenName = screenName;
		this.address = address;
		this.aboutMe = aboutMe;
		this.title = title;
		this.imageurl = imageurl;
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

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getAboutMe() {
		return aboutMe;
	}

	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
	
	public Organization getOrganization() {
		return organization;
	}

	public String getVerified() {
		return verified;
	}

	public void setVerified(String verified) {
		this.verified = verified;
	}

	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Set<Hackathon> getJudgedHackathons() {
		return judgedHackathons;
	}

	public void setJudgedHackathons(Set<Hackathon> judgedHackathons) {
		this.judgedHackathons = judgedHackathons;
	}

	public Set<Team> getTeams() {
		return teams;
	}

	public void setTeams(Set<Team> teams) {
		this.teams = teams;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		int hash = 3;
	    hash = 7 * hash + this.email.hashCode();
	    hash = 7 * hash + this.name.hashCode();
	    return hash;
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if((obj instanceof User) && ((User) obj).id == this.id)
		{
			return true;
		}
		else
			return false;
	}
}
