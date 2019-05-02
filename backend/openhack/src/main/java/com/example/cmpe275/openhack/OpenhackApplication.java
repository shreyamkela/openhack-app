package com.example.cmpe275.openhack;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.cmpe275.openhack.dao.OrganizationDao;
import com.example.cmpe275.openhack.dao.OrganizationDaoImpl;
import com.example.cmpe275.openhack.entity.Address;
import com.example.cmpe275.openhack.entity.Organization;
import com.example.cmpe275.openhack.entity.User;

@SpringBootApplication
public class OpenhackApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenhackApplication.class, args);
		EntityManagerFactory emfactory;
		emfactory = Persistence.createEntityManagerFactory("openhack");
		EntityManager em = emfactory.createEntityManager();
		Address address1 = new Address("33 S", "SJ", "CA", "95113", "USA");
		Address address2 = new Address("201 S", "SJ", "CA", "95112", "USA");
		Address address3 = new Address("101 N", "SJ", "CA", "95111", "USA");
		User user1 = new User();
		user1.setEmail("darshil@gmail.com");
		user1.setName("Darshil");
		user1.setAddress(address1);
		user1.setScreenName("Darshil");
		user1.setAboutMe("SJSU Graduate student");
		User user2 = new User();
		user2.setEmail("kavina@gmail.com");
		user2.setName("Kavina");
		user2.setAddress(address2);
		user2.setScreenName("DesaiKavina");
		user2.setAboutMe("Software Engineering graduate student");
		em.getTransaction().begin();
		em.persist(user1);
		em.persist(user2);
		em.getTransaction().commit();
		em.close();
		
		Organization org1 = new Organization();
		List <User> mem = new ArrayList<>();
		mem.add(user2);
		org1.setAddress(address3);
		org1.setName("Org1");
		org1.setDescription("This organiztion was founded in 2010");
		org1.setOwner(user1);
		Organization org2 = new Organization();
		org2.setAddress(address2);
		org2.setName("Org2");
		org2.setOwner(user2);
		org2.setDescription("This was founded in 2012");
		
		OrganizationDao orgdao = new OrganizationDaoImpl();
		orgdao.create(org1);
		orgdao.create(org2);
		List <Organization> org_result = orgdao.findAllOrganization();
		for(Organization org : org_result)
		{
			System.out.println(org.toString());
		}
	}

}
