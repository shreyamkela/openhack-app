package com.example.cmpe275.openhack.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Request {
	
	public Request() {
		
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade= {CascadeType.MERGE,CascadeType.PERSIST})
	@JoinColumn(name="requested_by_user_id")
	private User requested_by_user;
	
//	@ManyToOne(mappedBy = "", cascade= {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.REMOVE})
//	@JoinColumn(name="requested_by_user_id")
//	private User requested_by_user;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="requested_for_org_id")
	private Organization requested_for_org;

	public User getRequested_by_user() {
		return requested_by_user;
	}

	public void setRequested_by_user(User requested_by_user) {
		this.requested_by_user = requested_by_user;
	}

	public Organization getRequested_for_org() {
		return requested_for_org;
	}

	public void setRequested_for_org(Organization requested_for_org) {
		this.requested_for_org = requested_for_org;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	
}
