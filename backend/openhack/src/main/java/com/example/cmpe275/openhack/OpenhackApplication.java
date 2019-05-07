package com.example.cmpe275.openhack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import com.example.cmpe275.openhack.dao.OrganizationDao;
import com.example.cmpe275.openhack.dao.OrganizationDaoImpl;
import com.example.cmpe275.openhack.dao.UserDao;
import com.example.cmpe275.openhack.dao.UserDaoImpl;
import com.example.cmpe275.openhack.entity.Address;
import com.example.cmpe275.openhack.entity.Organization;
import com.example.cmpe275.openhack.entity.User;
import com.example.cmpe275.openhack.entity.Request;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class OpenhackApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenhackApplication.class, args);
//		EntityManagerFactory emfactory;
//		emfactory = Persistence.createEntityManagerFactory("openhack");
//		EntityManager em = emfactory.createEntityManager();
		
//		Address address1 = new Address("33 S", "SJ", "CA", "95113", "USA");
//		Address address2 = new Address("201 S", "SJ", "CA", "95112", "USA");
//		Address address3 = new Address("101 N", "SJ", "CA", "95111", "USA");
//		
//		User user1 = new User();
//		user1.setEmail("darshil@gmail.com");
//		user1.setName("Darshil");
//		user1.setAddress(address1);
//		user1.setScreenName("Darshil");
//		user1.setAboutMe("SJSU Graduate student");
//		User user2 = new User();
//		user2.setEmail("kavina@gmail.com");
//		user2.setName("Kavina");
//		user2.setAddress(address2);
//		user2.setScreenName("DesaiKavina");
//		user2.setAboutMe("Software Engineering graduate student");
//		User user3 = new User();
//		user3.setEmail("sayalee@gmail.com");
//		user3.setName("Sayalee");
//		user3.setAddress(address3);
//		user3.setScreenName("Sayaleee");
//		user3.setAboutMe("Grad student at SJSU");
//		
//		UserDao userdao = new UserDaoImpl();
//		
//		user1 = userdao.create(user1);
//		user2 = userdao.create(user2);
//		user3 = userdao.create(user3);
//
//		Organization org1 = new Organization();
//		org1.setAddress(address3);
//		org1.setName("Org1");
//		org1.setDescription("This organiztion was founded in 2010");
//		org1.setOwner(user1);
//		Organization org2 = new Organization();
//		org2.setAddress(address2);
//		org2.setName("Org2");
//		org2.setOwner(user2);
//		org2.setDescription("This was founded in 2012");
//		Organization org3 = new Organization();
//		org3.setAddress(new Address("901 San Carlos", "Milpitas", "CA", "95671", "USA"));
//		org3.setName("Org3");
//		org3.setDescription("This is a new organization");
//		
//		OrganizationDao orgdao = new OrganizationDaoImpl();
////		Organization deleted_organization = orgdao.delete(1);
////		System.out.println("\nThe organization that was deleted was : \n"+deleted_organization.toString());
//		org1 = orgdao.create(org1);
//		org2 = orgdao.create(org2);
//		org3 = orgdao.create(org3);
//		
//		System.out.println("\nOrganization 1 created with id : "+org1.getId());
//		System.out.println("\nOrganization 2 created with id : "+org2.getId());
//		System.out.println("\nOrganization 3 created with id : "+org3.getId());
//		
////		Organization result1 = orgdao.findOrganizationById(result_org1.getId());
////		Organization result2 = orgdao.findOrganizationById(result_org2.getId());
////		Organization result3 = orgdao.findOrganizationById(result_org3.getId());
//		
//		System.out.println("\nBefore updation, org2 is : \n"+org2.toString());
//		
//		org2.setName("Organization2");
//		org2.setAddress(new Address("100 San Salvador", "SJ", "CA", "95116", "USA"));
//		Organization updated_org = orgdao.update(org2);
//		
//		System.out.println("\nAfter updation, org2 is : \n"+updated_org.toString());
//		
//		user2.setOrganization(org1);
//		User updated_user = userdao.updateUser(user2);
//
//		
//		if(updated_user!=null)
//			System.out.println("\nAfter the updating the user : \n"+updated_user.toString());
////		
//		List <Organization> org_result = orgdao.findAllOrganization();
//		System.out.println("\n <<<<<<<<<<<<<< Listing all organization >>>>>>>>>>>>>>\n");
//		for(Organization org : org_result)
//		{
//			System.out.println(org.toString());
//		}
//	}
//		UserDao userdao = new UserDaoImpl();
//		Set<Request> res = new HashSet<>();
//		User user1 = userdao.findUserbyID(5);
//		res = user1.getJoin_requests();
//		System.out.println("The length of res : "+res.size());
//		if(res.size()>0)
//		{
//			for(Request req : res)
//			{
//				System.out.println(req.toString());
//			}
//		}
//		
//		OrganizationDao orgdao = new OrganizationDaoImpl();
//		Set<Request> res1 = new HashSet<>();
//		Organization org1 = orgdao.findOrganizationById(1);
//		res1 = org1.getJoin_requests();
//		System.out.println("The length of res1 : "+res1.size());
//		if(res1.size()>0)
//		{
//			for(Request req1 : res1)
//			{
//				System.out.println(req1.toString());
//			}
//		}
		
	}
}
