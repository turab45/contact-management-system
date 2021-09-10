package com.scm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scm.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer>{
	
	public UserEntity findByEmail(String email);
}
