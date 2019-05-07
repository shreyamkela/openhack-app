package com.example.cmpe275.openhack.dao;

import java.util.List;

import com.example.cmpe275.openhack.entity.Organization;


public interface OrganizationDao 
{
	
	Organization create(Organization org);
	
	Organization findOrganizationById(long orgId);
	
	Organization findOrganizationByName(String name);
	
	List<Organization> findAllOrganization();
	
	Organization update(Organization org);
	
	Organization delete(long orgId);
}
