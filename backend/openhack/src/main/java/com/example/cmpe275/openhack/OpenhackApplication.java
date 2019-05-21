package com.example.cmpe275.openhack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages="com.example.cmpe275.openhack.repository")
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class OpenhackApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenhackApplication.class, args);
	}
}
