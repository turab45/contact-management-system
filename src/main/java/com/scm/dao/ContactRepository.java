package com.scm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.scm.entities.ContactEntity;
import com.scm.entities.UserEntity;

public interface ContactRepository extends JpaRepository<ContactEntity, Integer>{
	
	@Query("from ContactEntity as c where c.user.id=:userId")
	public List<ContactEntity> findContactsByUser(@Param("userId") Integer userId);
	
	public List<ContactEntity> findByNameContainingAndUser(String name, UserEntity user);
}
