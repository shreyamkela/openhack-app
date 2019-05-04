package com.example.cmpe275.openhack.entity;

import java.util.List;

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

import com.example.cmpe275.openhack.entity.User;

@Entity
@Table
public class Organization {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column(name="Organization_name",unique=true,nullable=false)
	private String name;
	private String description;
	private Address address;
	
	//No mapping, manipulation needs to be done manually
//	@Column
	@OneToOne(fetch=FetchType.EAGER)
	private User owner;
	
	@OneToMany(mappedBy="organization",fetch=FetchType.EAGER)
	private List<User> members;
	
//	@ManyToMany(fetch=FetchType.LAZY)
	@ManyToMany
	@JoinTable(
			name="Sponsored_Hackathons",
			joinColumns= {@JoinColumn(name="Organization",referencedColumnName="id")},
			inverseJoinColumns= {@JoinColumn(name="Hackathon",referencedColumnName="id")})
	private List<Hackathon> sponsoredHackathons;
	
	public Organization() {}

	public Organization(long id, String name, String description, Address address, User owner) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.address = address;
		this.owner = owner;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public List<User> getMembers() {
		return members;
	}

	public void setMembers(List<User> members) {
		this.members = members;
	}

	public List<Hackathon> getSponsoredHackathons() {
		return sponsoredHackathons;
	}

	public void setSponsoredHackathons(List<Hackathon> sponsoredHackathons) {
		this.sponsoredHackathons = sponsoredHackathons;
	}

	@Override
	public String toString() {
		return "Organization [id=" + id + ", name=" + name + ", description=" + description + ", address=" + address
				+ ", owner=" + owner + ", members=" + members + "]";
	}
	
}
