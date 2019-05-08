package com.example.cmpe275.openhack;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerSingleton {

	private static EntityManagerSingleton singleInstance = null;
	
	public EntityManagerFactory emfactory;
	
	private EntityManagerSingleton() {
		// TODO Auto-generated constructor stub
		emfactory = Persistence.createEntityManagerFactory("openhack");
	}
	
	public static EntityManagerSingleton getInstance() {
		if(singleInstance==null) {
			singleInstance = new EntityManagerSingleton();
		}
		return singleInstance;
	}
}
