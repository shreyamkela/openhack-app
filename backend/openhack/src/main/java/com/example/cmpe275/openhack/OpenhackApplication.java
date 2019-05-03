package com.example.cmpe275.openhack;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

//import com.example.cmpe275.openhack.entity.Address;
//import com.example.cmpe275.openhack.entity.User;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class OpenhackApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenhackApplication.class, args);
		EntityManagerFactory emfactory;
		emfactory = Persistence.createEntityManagerFactory("openhack");
		EntityManager em = emfactory.createEntityManager();
//		Address address = new Address("33 S", "SJ", "CA", "95113", "USA");
//		User user = new User();
//		user.setEmail("darshil@gmail.com");
//		user.setName("Darshil");
//		user.setAddress(address);
//		user.setScreenName("Darshil");
//		user.setAboutMe("SJSU Graduate student");
		em.getTransaction().begin();
//		em.persist(user);
		em.getTransaction().commit();
		em.close();
		
		
	}
    
}
