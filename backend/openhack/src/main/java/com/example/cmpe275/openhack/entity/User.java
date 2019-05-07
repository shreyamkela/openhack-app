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



/**
 * @author darsh
 *
 */
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
	@ManyToMany(mappedBy="members", fetch=FetchType.EAGER)
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



/*
 * INSERT INTO `openhack_darshil`.`User` (`id`, `aboutMe`, `city`, `country`, `state`, `street`, `zip`, `email`, `lastname`, `name`, `password`, `screenName`, `title`, `usertype`, `verified`) VALUES ('1', 'New User', 'SJ', 'USA', 'CA', '33', '95113', 'gundagaon@zetmail.com', 'Gundagaon', 'Gundagaon', '12345678', 'Gundagaon', 'Developer', 'user', 'Y');
INSERT INTO `openhack_darshil`.`User` (`id`, `aboutMe`, `city`, `country`, `state`, `street`, `zip`, `email`, `lastname`, `name`, `password`, `screenName`, `title`, `usertype`, `verified`) VALUES ('2', 'New User', 'SJ', 'USA', 'CA', '33', '95113', 'salah@zetmail.com', 'Moh', 'Salah', '12345678', 'MoSalah', 'Manager', 'user', 'Y');
INSERT INTO `openhack_darshil`.`User` (`id`, `aboutMe`, `city`, `country`, `state`, `street`, `zip`, `email`, `imageurl`, `lastname`, `name`, `password`, `screenName`, `title`, `usertype`, `verified`) VALUES ('3', 'New User', 'SJ', 'USA', 'CA', '33', '95113', 'mane@zetmail.com', '', 'Sadio', 'Mane', '12345678', 'SMane', 'Developer', 'user', 'Y');
INSERT INTO `openhack_darshil`.`User` (`id`, `aboutMe`, `city`, `country`, `state`, `street`, `zip`, `email`, `lastname`, `name`, `password`, `screenName`, `title`, `usertype`, `verified`) VALUES ('4', 'New USer', 'SJ', 'USA', 'CA', '33', '95113', 'bale@zetmail.com', 'Gareth ', 'Bale', '12345678', 'GBale', 'Developer', 'user', 'Y');
INSERT INTO `openhack_darshil`.`User` (`id`, `aboutMe`, `city`, `country`, `state`, `street`, `zip`, `email`, `lastname`, `name`, `password`, `screenName`, `title`, `usertype`, `verified`) VALUES ('5', 'New User', 'SJ', 'USA', 'CA', '33', '95113', 'vinicious@zetmail.com', 'Vinicious', 'Jr', '12345678', 'VJR', 'Tester', 'user', 'Y');
INSERT INTO `openhack_darshil`.`User` (`id`, `aboutMe`, `city`, `country`, `state`, `street`, `zip`, `email`, `lastname`, `name`, `password`, `screenName`, `title`, `usertype`, `verified`) VALUES ('6', 'New User', 'SJ', 'USA', 'CA', '33', '95113', 'tony@zetmail.com', 'Tony', 'Stark', '12345678', 'IronMan', 'Manager', 'user', 'Y');
INSERT INTO `openhack_darshil`.`User` (`id`, `aboutMe`, `city`, `country`, `state`, `street`, `zip`, `email`, `lastname`, `name`, `password`, `screenName`, `title`, `usertype`, `verified`) VALUES ('7', 'New User', 'SJ', 'USA', 'CA', '33', '95113', 'steve@zetmail.com', 'Steve', 'Rogers', '12345678', 'CaptainA', 'Leader', 'user', 'Y');
INSERT INTO `openhack_darshil`.`User` (`id`, `aboutMe`, `city`, `country`, `state`, `street`, `zip`, `email`, `lastname`, `name`, `password`, `screenName`, `title`, `usertype`, `verified`) VALUES ('8', 'New User', 'SJ', 'USA', 'CA', '33', '95113', 'zidane@zetmail.com', 'Zinedine', 'Zidane', '12345678', 'Zizou', 'Developer', 'user', 'Y');
INSERT INTO `openhack_darshil`.`User` (`id`, `aboutMe`, `city`, `country`, `state`, `street`, `zip`, `email`, `lastname`, `name`, `password`, `screenName`, `title`, `usertype`, `verified`) VALUES ('9', 'New User', 'SJ', 'USA', 'Ca', '33 ', '95113', 'mandhana@zetmail.com', 'Smriti', 'Mandhana', '12345678', 'Mandhana', 'Developer', 'user', 'Y');

 * */