package com.example.cmpe275.openhack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cmpe275.openhack.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
