package com.example.cmpe275.openhack.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cmpe275.openhack.entity.Organization;
import com.example.cmpe275.openhack.repository.OrganizationRepository;

@Service
public class OrganizationRepositoryService {

	
	@Autowired
	OrganizationRepository organizationRepository;
	
	@Autowired
	EntityManager em;
	
	@Transactional
	public Organization create(Organization org) 
	{
			Organization org1 = organizationRepository.save(org);
			return org1;
		}

	
	@Transactional
	public Organization findOrganizationById(long orgId) 
	{
			Organization org = organizationRepository.getOne(orgId);
			return org;
		
	}
	

	@Transactional
	public Organization findOrganizationByName(String name) 
	{
			List<Organization> organizations = organizationRepository.findAll();
			Organization org=null;
			for(Organization organization:organizations) {
				if(organization.getName().equals(name)) {
					org=organization;
				}
			}
			return org;
	}
	

//	@Transactional
	public List<Organization> findAllOrganization() 
	{
			List<Organization> organizations = organizationRepository.findAll();
			return organizations;
	}


	@Transactional
	public Organization update(Organization org) 
	{
			Organization updated_organization = organizationRepository.save(org);
			System.out.println("\n- - - - - - - - - - Organization "+org.getName()+" updated successfully! - - - - - - - - - -\n");
			return updated_organization;
		
	}

	
	@Transactional
	public Organization delete(long orgId) 
	{
			Organization delete_organization = organizationRepository.getOne(orgId);
			organizationRepository.delete(delete_organization);
			return delete_organization;
	}
}
