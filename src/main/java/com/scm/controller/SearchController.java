package com.scm.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.scm.dao.ContactRepository;
import com.scm.dao.UserRepository;
import com.scm.entities.ContactEntity;
import com.scm.entities.UserEntity;

@RestController
public class SearchController {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ContactRepository contactRepository;
	
	@GetMapping("/user/search/{query}")
	public ResponseEntity<List<ContactEntity>> search(@PathVariable("query") String query, Principal principal){
		UserEntity userEntity = userRepository.findByEmail(principal.getName());
		
		List<ContactEntity> list = contactRepository.findByNameContainingAndUser(query, userEntity);
		
		return ResponseEntity.ok(list);
	}
}
