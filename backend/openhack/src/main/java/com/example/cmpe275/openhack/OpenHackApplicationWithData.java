package com.example.cmpe275.openhack;

import java.util.ArrayList;
import java.util.List;

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

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class OpenHackApplicationWithData {

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
		User user3 = new User();
		user3.setEmail("sayalee@gmail.com");
		user3.setName("Sayalee");
		user3.setAddress(address3);
		user3.setScreenName("Sayaleee");
		user3.setAboutMe("Grad student at SJSU");
//		
		UserDao userdao = new UserDaoImpl();
//		
		user1 = userdao.createUser(user1);
		user2 = userdao.createUser(user2);
		user3 = userdao.createUser(user3);
<<<<<<< HEAD
	}
}


=======
>>>>>>> parent of 423de8f... DI and AOP addition, saving user type in localStorage
//
//import com.example.cmpe275.openhack.dao.OrganizationDao;
//import com.example.cmpe275.openhack.dao.OrganizationDaoImpl;
//import com.example.cmpe275.openhack.dao.UserDao;
//import com.example.cmpe275.openhack.dao.UserDaoImpl;
//import com.example.cmpe275.openhack.entity.Address;
//import com.example.cmpe275.openhack.entity.Organization;
//import com.example.cmpe275.openhack.entity.User;
//
//@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
//public class OpenHackApplicationWithData {
//
//	public static void main(String[] args) {
//		SpringApplication.run(OpenhackApplication.class, args);
//		EntityManagerFactory emfactory;
//		emfactory = Persistence.createEntityManagerFactory("openhack");
//		EntityManager em = emfactory.createEntityManager();
//		
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
////		
//		UserDao userdao = new UserDaoImpl();
////		
//		user1 = userdao.create(user1);
//		user2 = userdao.create(user2);
//		user3 = userdao.create(user3);
////
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
////		
//		OrganizationDao orgdao = new OrganizationDaoImpl();
//		org1 = orgdao.create(org1);
//		org2 = orgdao.create(org2);
//		org3 = orgdao.create(org3);
//			
//	}
//}
